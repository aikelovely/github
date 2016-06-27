<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout title="Добро пожаловать">
    <jsp:attribute name="js">
    <script>
        app.init({groups: []});
    </script>
    </jsp:attribute>

    <jsp:body>
        <div class="jumbotron">
            <h2>OpEx</h2>
            <p>Добро пожаловать!</p>
        </div>
    </jsp:body>
</t:layout>