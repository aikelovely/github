package ru.alfabank.dmpr.infrastructure.spring.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Экземпляр этого класса создается при старте приложения при построении контекста Spring.
 * Когда случается AuthenticationException, то из ExceptionTranslationFilter вызывается метод commence этого класса,
 * который делает редирект на страницу errorPageUrl.
 */
public class ErrorPageAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

    private String errorPageUrl;

    private static final String AUTHENTICATION_ERROR_MESSAGE = "Ошибка авторизации";

    public ErrorPageAuthenticationEntryPoint() {
    }

    public ErrorPageAuthenticationEntryPoint(String errorPageUrl) {
        this.errorPageUrl = errorPageUrl;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(StringUtils.hasText(errorPageUrl) && UrlUtils.isValidRedirectUrl(errorPageUrl),
                "loginFormUrl must be specified and must be a valid redirect URL");
    }

    /**
     * Редирект на errorPageURL
     */
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException
    {
        if (request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") != null &&
                request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") instanceof AuthenticationServiceException) {
            authException = (AuthenticationServiceException)request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }

        request.setAttribute("errorDescription", "Ошибка авторизации");
        request.setAttribute("exceptionMessage", authException.getMessage());
        request.getRequestDispatcher(getErrorPageUrl()).forward(request, response);
    }

    public void setErrorPageUrl(String errorPageUrl) {
        this.errorPageUrl = errorPageUrl;
    }

    public String getErrorPageUrl() {
        return errorPageUrl;
    }
}