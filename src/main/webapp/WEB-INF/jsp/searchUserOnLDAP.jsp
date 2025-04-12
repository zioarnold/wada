<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge"/>
    <meta about="Made by UID0931174 aka Zaki"/>
    <title>Eni Qlik Tool User Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/background.css"/>
    <link type="text/javascript" href="${pageContext.request.contextPath}/js/bootstrap.js"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/ico/favicon.ico"/>
    <!-- Load an icon library to show a hamburger menu (bars) on small screens -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
</head>
<body>
<jsp:include page="navbar.jsp"/>
<br/>
<div class="container text-center">
    <form action="<c:url value="/searchUserOnLDAP"/>" method="get">
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <input class="btn btn-primary" type="submit" value="Cerca"/>
            </div>
            <div>
                <input type="text" class="form-control" placeholder="Inserisci matricola"
                       aria-label="Inserisci matricola" aria-describedby="basic-addon2" name="userID">
            </div>
        </div>
    </form>
</div>
<c:choose>
    <c:when test="${empty userIDDATA}">
        <div class="container text-center">
            <h2>Nessun utenza digitata</h2>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container text-center" id="userDiv">
            <h2>Utenza nell'AD</h2>
            <hr>
            <div class="table-responsive">
                <table class="table table-striped table-bordered table-sm">
                    <thead>
                    <tr>
                        <th>
                            Nome visualizzato
                        </th>
                        <th>
                            Matricola
                        </th>
                        <th>
                            Nome cognome
                        </th>
                        <th>
                            E-mail
                        </th>
                        <th>
                            Nome
                        </th>
                        <th>
                            Utenza disabilitata
                        </th>
                        <th>
                            Unita` organizzativa
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${userIDDATA}" var="user">
                        <tr>
                            <td>${user.displayName}</td>
                            <td>${user.ENIMatricolaNotes}</td>
                            <td>${user.name}</td>
                            <td>${user.mail}</td>
                            <td>${user.givenName}</td>
                            <td>${user.userAccountDisabled}</td>
                            <td>${user.ou}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:otherwise>
</c:choose>
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