<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% List<String> rolesList = (List<String>) request.getAttribute("rolesList"); %>
<!doctype html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta about="Made by UID0931174 aka Zaki"/>
    <title>Eni Qlik Tool User Management</title>
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <link rel="stylesheet" href="css/background.css"/>
    <link type="text/javascript" href="js/bootstrap.js"/>
    <link rel="shortcut icon" href="ico/favicon.ico"/>
    <!-- Load an icon library to show a hamburger menu (bars) on small screens -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container text-center">
    <form class="form-horizontal" action="/singleUpload" method="post">
        <div class="form-group">
            <label class="col-sm-2 control-label">Inserisci matricola
                <input class="form-control" name="userId" type="text">
            </label>
            <label class="col-sm-2 control-label">Inserisci ruolo
                <select name="userRole" class="form-control">
                    <c:choose>
                        <c:when test="${empty rolesList}">
                            <option>Viewer</option>
                            <option>Analyzer</option>
                            <option>Contributor</option>
                            <option>SuperUserAM</option>
                        </c:when>
                        <c:otherwise>
                            <%
                                for (String s : rolesList) {
                            %>
                            <option>
                                <%=s%>
                            </option>
                            <% } %>
                        </c:otherwise>
                    </c:choose>
                </select>
            </label>
            <label class="col-sm-2 control-label">Inserisci gruppo
                <input class="form-control" name="userGroup" type="text">
            </label>
            <input type="submit" class="btn btn-success" value="Aggiungi">
        </div>
    </form>
</div>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
        integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
        integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1"
        crossorigin="anonymous"></script>
</body>
</html>