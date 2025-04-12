<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge"/>
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
<nav class="navbar navbar-expand-lg navbar-light" style="background-color: #e0e0d1 !important;">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/index">
        <img src='<c:url value="${pageContext.request.contextPath}/img/eni-logo.png"/>' alt="WADA ENI"
             style="height: 20%; width: 20%;"/>WADA <span
            style="color: yellow">Eni</span>
    </a>&nbsp;&nbsp;&nbsp;
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="${pageContext.request.contextPath}/index">Home <span
                        class="sr-only">(current)</span></a>
            </li>
            <c:choose>
                <c:when test="${user_role_logged_in.equals('ADMIN')}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/allFarmPage">Farms</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/allAdminsPage">Admins</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/report">Report</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/report">Report</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownUsers" role="button"
                           data-toggle="dropdown"
                           aria-haspopup="true" aria-expanded="false">
                            Users
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdownUsers">
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/AllQLIKUsersFromDB">Elenco</a>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/searchUserOnLDAPPage">Ricerca
                                su AD</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/massiveUploadPage">Caricamento
                                massivo</a>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/singleUploadPage">Caricamento
                                singolo</a>
                        </div>
                    </li>
                </c:otherwise>
            </c:choose>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownHelp" role="button"
                   data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    Help
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownHelp">
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/userGuide">Manuale</a>
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/assistance">Assistenza</a>
                </div>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownTools" role="button"
                   data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    Tools
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownTools">
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/executeReloadTaskQMC">Esegui
                        reload task</a>
                    <%--                    <a class="dropdown-item" href="${pageContext.request.contextPath}/managementPageMassive">Gestione massiva</a>--%>
                    <%--                    <a class="dropdown-item" href="${pageContext.request.contextPath}/allUsersFromLDAP">Estrazione completa ADLDS</a>--%>
                </div>
            </li>
            <li class="nav-item">
                <a class="nav-link">|</a>
            </li>
            <li>
                <a class="navbar-brand">${farm_name}
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link">|</a>
            </li>
            <li>
                <a class="navbar-brand">${farm_environment}
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link">|</a>
            </li>
            <li>
                <c:choose>
                    <c:when test="${ping_qlik >= 200 && ping_qlik <= 299}">
                        <a class="navbar-brand"> QS-PING: OK</a>
                    </c:when>
                    <c:when test="${ping_qlik >=100 && ping_qlik<=199}">
                        <a class="navbar-brand"> QS-PING: WARN </a>
                    </c:when>
                    <c:when test="${ping_qlik >=300 && ping_qlik<=399}">
                        <a class="navbar-brand"> QS-PING: WARN </a>
                    </c:when>
                    <c:when test="${ping_qlik >= 400 && ping_qlik <= 499}">
                        <a class="navbar-brand"> QS-PING: KO </a>
                    </c:when>
                    <c:otherwise>
                        <a class="navbar-brand"> QS-PING: KO </a>
                    </c:otherwise>
                </c:choose>
            </li>
            <li class="nav-item">
                <a class="nav-link">|</a>
            </li>
            <li>
                <a class="navbar-brand">${user_logged_in}
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link">|</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
            </li>
        </ul>
        <%--        <form class="form-inline my-2 my-lg-0">--%>
        <%--            <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">--%>
        <%--            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>--%>
        <%--        </form>--%>
    </div>
</nav>
<c:choose>
    <c:when test="${empty other_data}">
        <div class="container text-center">
            <h2>Utenza non e' presente sul DB!</h2>
            <h3>Digitare nuovamente la matricola</h3>
            <form action="${pageContext.request.contextPath}/showUserType" method="post"
                  style="text-transform: uppercase">
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <input class="btn btn-primary" type="submit" value="Cerca"/>
                    </div>
                    <div>
                        <input type="text" class="form-control" placeholder="Inserisci matricola"
                               aria-label="Inserisci matricola" aria-describedby="basic-addon2" name="quser">
                    </div>
                </div>
            </form>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container text-center" id="userDiv">
            <h2>Ruoli dell'utente</h2>
            <form action="${pageContext.request.contextPath}/showUserType" method="post"
                  style="text-transform: uppercase">
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <input class="btn btn-primary" type="submit" value="Cerca"/>
                    </div>
                    <div>
                        <input type="text" class="form-control" placeholder="Inserisci matricola"
                               aria-label="Inserisci matricola" aria-describedby="basic-addon2" name="quser">
                    </div>
                </div>
            </form>
            <hr>
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <thead>
                    <tr>
                        <th>
                            Tipo
                        </th>
                        <th>
                            Valore
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="quser" items="${other_data}">
                        <tr>
                            <td>${quser.type}</td>
                            <td>${quser.value}</td>
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