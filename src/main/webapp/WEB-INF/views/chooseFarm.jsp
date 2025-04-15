<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Created by IntelliJ IDEA.
  User: UID0931174
  Date: 18/10/2019
  Time: 11:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta about="Made by UID0931174 aka Zaki"/>
    <title>Eni Qlik Tool User Management</title>
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <link rel="stylesheet" href="css/custom.css"/>
    <link rel="stylesheet" href="css/background.css"/>
    <link rel="shortcut icon" href="ico/favicon.ico"/>
    <!-- Load an icon library to show a hamburger menu (bars) on small screens -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
</head>
<body>
<div class="container">
    <div class="d-flex justify-content-center h-100">
        <div class="card">
            <div class="card-header">
                <h3>Seleziona la FARM</h3>
            </div>
            <div class="card-body">
                <form class="form-group" method="get">
                    <c:choose>
                        <c:when test="${empty farmList}">
                            <span style="color:whitesmoke">Nessuna farm censita!</span>
                            <a href="${pageContext.request.contextPath}/">Login</a>
                        </c:when>
                        <c:otherwise>
                            <%--suppress HtmlFormInputWithoutLabel --%>
                            <select name="farm" class="form-control" id="farm">
                                <c:forEach items="${farmList}" var="farm">
                                    <option value="${farm.description}">${farm.description}</option>
                                </c:forEach>
                            </select>
                        </c:otherwise>
                    </c:choose>
                    <br>
                    <div>
                        <button formaction="/selectFarm" formmethod="get" class="btn float-right login_btn">Scegli
                        </button>
                        <c:choose>
                            <c:when test="${empty farmList}">
                                <button type="button" onclick="location.href='/createFarm'"
                                        class="btn float-left login_btn">Crea
                                </button>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>