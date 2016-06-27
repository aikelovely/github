<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags" %>

<t:layout title="Доступ запрещен">
    <jsp:attribute name="js">
    <script>
      app.init({groups: []});
    </script>
    </jsp:attribute>

  <jsp:body>
    <div class="jumbotron">
      <h2>Доступ запрещен</h2>
      <p>Вы попытались зайти на витрину, к которой у вас нет доступа.</p>
    </div>
  </jsp:body>
</t:layout>