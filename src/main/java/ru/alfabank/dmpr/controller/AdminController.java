package ru.alfabank.dmpr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.alfabank.dmpr.infrastructure.mybatis.DataLayerFacade;

import java.io.IOException;

/**
 * Контроллер, позволяющий выполнять действия администратора.
 */
@RestController
@RequestMapping("admin")
public class AdminController {
    /**
     * Очищает кэш данных слоя данных
     * @return флаг успешности очистки
     * @throws IOException
     */
    @RequestMapping(value = "/clearCaches", method = RequestMethod.POST)
    public boolean clearCaches() throws IOException {
        try{
            DataLayerFacade.clearCaches();
            return true;
        }
        catch (Exception ex){
            return false;
        }
    }
}
