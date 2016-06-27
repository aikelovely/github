<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width"/>

    <title>Произошла ошибка</title>
    <jwr:style src="/bundles/app.css"/>
</head>
<body>
	<h2>${requestScope.errorDescription}</h2>
	<p>${requestScope.exceptionMessage}</p>
</body>
</html>