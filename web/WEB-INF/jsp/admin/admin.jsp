<%--
  Created by IntelliJ IDEA.
  User: dmitry-sergeev
  Date: 21.09.15
  Time: 19:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
</head>
<body>
${responseJson}
Hi, ${pageContext.request.userPrincipal.name}!
</body>
</html>
