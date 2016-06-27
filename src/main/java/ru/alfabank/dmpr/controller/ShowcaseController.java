package ru.alfabank.dmpr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.alfabank.dmpr.infrastructure.spring.CustomResourceLoader;

/**
 * Контроллер отвечающий за отображение витрин.
 */
@Controller
public class ShowcaseController {
    @Autowired
    CustomResourceLoader resourceLoader;

    @ModelAttribute("supportBtnMarkup")
    public String getSupportBtnMarkup() {
        return resourceLoader.getResourceAsString();
    }

    /**
     * Приветственная страница.
     * @return
     */
    @RequestMapping(value = {"/", "/welcome"})
    public String home() {
        return "welcome";
    }

    /**
     * Страница с ошибкой.
     * @return
     */
	@RequestMapping(value = {"/error"})
    public String displayError() {
        return "error";
    }

    @RequestMapping(value = {"/access-denied"})
    public String accessDenied(){
        return "403";
    }

    /**
     * Метод, отображащий витрину по ее названию.
     * @param showcaseName Название витрины.
     * @return
     */
    @RequestMapping("showcase/{showcaseName}")
    public String displayShowcase(@PathVariable("showcaseName") String showcaseName) {
        return "showcase/" + showcaseName;
    }
}
