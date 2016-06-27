package ru.alfabank.dmpr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import ru.alfabank.dmpr.infrastructure.mybatis.tracer.SqlQueryTracer;
import ru.alfabank.dmpr.infrastructure.mybatis.tracer.TracingQuery;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.BaseOptions;
import ru.alfabank.dmpr.widget.BaseWidget;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;

import static org.springframework.web.bind.ServletRequestUtils.*;

/**
 * Фронт-контроллер принимающий все вызовы связанные с запросом бизнес-данных приложения.
 * По-сути точка вызова API бизнес-данных. Содержит всего два публичных метода: по одному
 * для GET- и POST- запросов.
 * Данные методы извлекают из URL имя сервиса, singleton-экземпляр которого запрашивается
 * у spring-контекста. Параметры для вызова требуемого бизнес-метода данного сервиса извлекаются
 * из query-string или из body запроса - в зависимости от типа вызова GET или POST.
 * Результат выполнения бизнес-метода помещаются в экземляр класса Result, который впоследствии
 * сериализуется в JSON штатной инфраструктурой MVC Rest-контроллера.
 */
@RestController
@RequestMapping("/api")
public class FrontApiController {
    private static final Logger logger = Logger.getLogger(FrontApiController.class);

    /**
     * Класс ObjectMapper - thread-safe, можно использовать единственный экземпляр для всех вызовов.
     * Используется экземпляр, настроенный в spring-конфигурации для кастомной сериализации отдельных
     * типов данных, таких как JodaTime.
     */
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ObjectMapper jsonMapper;

    /**
     * Индекс полных имен сервисов по именам в нижнем регистре.
     * Используется для обеспечения регистро-независимости имени сервиса из URL
     */
    private final Map<String, String> serviceNameMap = new HashMap<>();

    private final ApplicationContext context;


    @Autowired
    public FrontApiController(final ApplicationContext context) {
        this.context = context;

        /**
         * Данный блок кода запрашивает у родительского контекста app-context список всех сервисов
         * и формирует индекс по их именам в нижнем регистре
         */
        ApplicationContext parent = context;
        while (parent.getParent() != null) {
            parent = parent.getParent();
        }
        String[] serviceNames = parent.getBeanNamesForAnnotation(Service.class);
        for (String serviceName : serviceNames)
            serviceNameMap.put(serviceName.toLowerCase(), serviceName);
    }

    /**
     * POST-метод для вызова сервисов-виджетов
     * пример URL: cardsDeliveryPeriodPie
     *
     * @param widgetName имя виджета
     * @param request    servlet request - для извлечения параметров витрины из body
     * @return JSON
     */
    @RequestMapping(value = "{widget}", method = RequestMethod.POST)
    public Result getWidgetData(
            @PathVariable("widget") String widgetName,
            HttpServletRequest request) {
        try {
            // все виджеты наследуют базовому BaseWidget
            BaseWidget service = (BaseWidget) getService(widgetName);
            // все параметры виджетов наследуют базовому BaseOptions
            BaseOptions options = getOptions(service.getOptionsClass(), request);
            options.widgetUrl = widgetName;

            @SuppressWarnings("unchecked")
            Object data = service.getData(options);
            return new Result(data);

        } catch (Throwable e) {
            logger.error(e.getMessage(), e);

            while (e instanceof InvocationTargetException)
                e = ((InvocationTargetException) e).getTargetException();
            return new Result(e);
        }
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String ping() {
        return "ping";
    }

    /**
     * Извлекает параметры витрины из body of request и создает из них экземляр класса optionsClass
     *
     * @param optionsClass тип возвращаемого класса параметров
     * @param request      servlet request
     * @param <T>          тип
     * @return экземляр класса optionsClass
     * @throws IOException
     */
    private <T extends BaseOptions> T getOptions(Class<T> optionsClass, HttpServletRequest request)
            throws IOException {
        try (ServletInputStream stream = request.getInputStream()) {
            try (InputStreamReader reader = new InputStreamReader(stream)) {
                String string = IOUtils.toString(reader);

                // все вызовы виджетов нуждаются в параметрах
                if (StringUtils.isEmpty(string))
                    throw new IllegalArgumentException("Нет объекта с параметрами для POST запроса");

                T options = jsonMapper.readValue(string, optionsClass);
                return options != null ? options : null;
            }
        }
    }


    /**
     * GET-метод для вызова методов сервисов-фильтров
     * пример URL: cardsDeliveryPeriodFilter/regions
     *
     * @param filterName имя класса фильтра для отдельной витрины
     * @param methodName имя метода, возвращающего список значения отдельного параметра витрины
     * @param request    servlet request - для извлечения параметров вызова из query-string
     * @return JSON
     */
    @RequestMapping(value = "{filter}/{method}", method = RequestMethod.GET)
    public Result getFilterData(
            @PathVariable("filter") String filterName,
            @PathVariable("method") String methodName,
            HttpServletRequest request) {
        try {
            Object service = getService(filterName);
            Method method = getMethod(service.getClass(), methodName);

            request.setCharacterEncoding("UTF-8");

            Object[] params = getParams(method, request);
            Object data = method.invoke(service, params);
            return new Result(data);

        } catch (Throwable e) {
            // ошибки возвращаются на клиента, а также пишутся в лог
            logger.error(e.getMessage(), e);

            while (e instanceof InvocationTargetException)
                e = ((InvocationTargetException) e).getTargetException();
            return new Result(e);
        }
    }

    /**
     * Извлекает параметры из query-string of request и помещает их в массив объектов.
     * Данный метод через рефлексию определяет имена параметров из навешенных на параметры методов сервиса-фильтра
     * аннотаций Param, а также их типы и последовательность. Далее для каждого ожидаемого параметра
     * происходит извлечение по его имени значения из query-string и помещение в результирующий массив.
     * Массив может быть пустым если целевой метод не содержит параметров.
     *
     * @param method  дескриптор рефлексии метода вызова
     * @param request servlet request
     * @return масиив упорядоченных параметров
     * @throws ServletRequestBindingException
     */
    private Object[] getParams(Method method, HttpServletRequest request)
            throws ServletRequestBindingException {
        Class<?>[] types = method.getParameterTypes();
        Object[] values = new Object[types.length];

        Annotation[][] groupedAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < groupedAnnotations.length; i++) {
            Annotation[] paramAnnotations = groupedAnnotations[i];
            boolean annotationFound = false;
            for (Annotation paramAnnotation : paramAnnotations)
                if (paramAnnotation instanceof Param) {
                    String name = ((Param) paramAnnotation).value();
                    if (!Objects.equals(name, "")) {
                        try {
                            values[i] = getParamValue(name, types[i], request);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        annotationFound = true;
                    }
                    break;
                }

            if (!annotationFound) {
                Class<? extends Class> serviceClass = method.getDeclaringClass().getClass();
                String message = "Не задана аннотация имени параметра метода сервиса " +
                        serviceClass.getName() + "." + method.getName() + "[" + i + "]";
                throw new IllegalArgumentException(message);
            }
        }
        return values;
    }

    /**
     * Извдекает значение параметра из query-string of request
     *
     * @param name    имя извлекаемого параметра
     * @param type    тип извлекаемого параметра
     * @param request servlet request
     * @return значение параметра
     * @throws ServletRequestBindingException
     */
    private Object getParamValue(String name, Class type, HttpServletRequest request)
            throws ServletRequestBindingException, UnsupportedEncodingException {
        if (type == int[].class) return getIntParameters(request, name);
        else if (type == int.class) return getIntParameter(request, name, 0);
        else if (type == long[].class) return getLongParameters(request, name);
        else if (type == long.class) return getLongParameter(request, name, 0);
        else if (type == String[].class) return getStringParameters(request, name);
        else if (type == String.class) {
            String value = request.getParameter(name);
            if(value == null) return null;
            return URLDecoder.decode(value, "UTF-8");
        }
        else if (type == Integer.class) return getIntParameter(request, name);
        else if (type == LocalDate.class) return getLocalDateParameter(request, name);
        else
            throw new ServletRequestBindingException("Не реализован binding для типа " + type.getName());
    }

    private LocalDate getLocalDateParameter(ServletRequest request, String name){
        String val = request.getParameter(name);
        return val == null ? null : LocalDate.parse(val);
    }


    /**
     * Получает у spring-контекста экземпляр сервиса
     *
     * @param name имя сервиса
     * @return экземпляр сервиса
     * @throws Exception
     */
    private Object getService(String name) throws Exception {
        String justName = serviceNameMap.get(name.toLowerCase());
        if (justName == null) {
            String message = "Сервис не найден: " + name;
            throw new Exception(message);
        }
        return context.getBean(justName);
    }

    /**
     * Находит дескриптор рефлексии метода сервиса-фильтра
     *
     * @param serviceClass тип сервиса
     * @param name         имя метода
     * @return дескриптор
     * @throws Exception
     */
    private Method getMethod(Class serviceClass, String name) throws Exception {
        // имя метода в URL указывается без префикса "get"
        String methodName = "get" + name;
        for (Method method : serviceClass.getMethods()) {
            // регистро-независимый поиск
            if (method.getName().equalsIgnoreCase(methodName))
                return method;
        }

        String message = "Метод не найден: " + serviceClass.getName() + "." + methodName;
        throw new Exception(message);
    }

    /**
     * Класс-контейнер для возврата результата вызова бизнес-метода
     */
    public static class Result {
        // бизнес-данные, в случае успешного вызова
        public Object data;
        // признак успешности вызова
        public boolean success;
        // текст ошибки, в случае неудачи
        public String error;
        // список sql-запросов выполненых/полученных из кэша по ходу выполнения данного request
        public List<TracingQuery> queryList = SqlQueryTracer.consumeQueryList();

        public Result(Object data) {
            this.data = data;
            this.success = true;
        }

        public Result(Throwable error) {
            try (StringWriter stringWriter = new StringWriter()) {
                try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
                    error.printStackTrace(printWriter);
                    this.error = stringWriter.getBuffer().toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
