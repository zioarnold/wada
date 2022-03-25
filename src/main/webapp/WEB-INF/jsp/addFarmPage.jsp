<%--
  Created by IntelliJ IDEA.
  User: UID0931174
  Date: 24/10/2019
  Time: 11:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge"/>
    <meta about="Made by UID0931174 aka Zaki"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
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
    <a class="navbar-brand" href="/index">
        <img src='<c:url value="img/eni-logo.png"/>' alt="WADA ENI" style="height: 20%; width: 20%;"/>WADA <span
            style="color: yellow">Eni</span>
    </a>&nbsp;&nbsp;&nbsp;
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="/index">Home <span class="sr-only">(current)</span></a>
            </li>
            <% String user_role_logged_in = (String) request.getAttribute("user_role_logged_in");
                if (user_role_logged_in.equals("ADMIN")) {
            %>
            <li class="nav-item">
                <a class="nav-link" href="/allFarmPage">Farms</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/allAdminsPage">Admins</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/report">Report</a>
            </li>
            <%
            } else {
            %>
            <li class="nav-item">
                <a class="nav-link" href="/report">Report</a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownUsers" role="button"
                   data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    Users
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownUsers">
                    <a class="dropdown-item" href="/AllQLIKUsersFromDB">Elenco</a>
                    <a class="dropdown-item" href="/searchUserOnLDAPPage">Ricerca su AD</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="/massiveUploadPage">Caricamento massivo</a>
                    <a class="dropdown-item" href="/singleUploadPage">Caricamento singolo</a>
                </div>
            </li>
            <%
                }
            %>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownHelp" role="button"
                   data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    Help
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownHelp">
                    <a class="dropdown-item" href="/userGuide">Manuale</a>
                    <a class="dropdown-item" href="/assistance">Assistenza</a>
                </div>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownTools" role="button"
                   data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    Tools
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownTools">
                    <a class="dropdown-item" href="/executeReloadTaskQMC">Esegui reload task</a>
                    <%--                    <a class="dropdown-item" href="/managementPageMassive">Gestione massiva</a>--%>
                    <%--                    <a class="dropdown-item" href="/allusersfromldap">Estrazione completa ADLDS</a>--%>
                </div>
            </li>
            <li class="nav-item">
                <a class="nav-link">|</a>
            </li>
            <li>
                <a class="navbar-brand"><%=(String) request.getAttribute("farm_name") %>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link">|</a>
            </li>
            <li>
                <a class="navbar-brand"><%=(String) request.getAttribute("farm_environment") %>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link">|</a>
            </li>
            <li><% Integer ping_qlik = (Integer) request.getAttribute("ping_qlik");
                if (ping_qlik >= 200 && ping_qlik <= 299) {
            %>
                <a class="navbar-brand"> QS-PING: OK</a>
                <%
                    }
                    if (ping_qlik >= 100 && ping_qlik <= 199) {
                %>
                <a class="navbar-brand"> QS-PING: WARN </a>
                <%
                    }
                    if (ping_qlik >= 300 && ping_qlik <= 399) {
                %>
                <a class="navbar-brand"> QS-PING: WARN </a>
                <%
                    }
                    if (ping_qlik >= 400 && ping_qlik <= 499) {
                %>
                <a class="navbar-brand"> QS-PING: KO </a>
                <%
                    }
                    if (ping_qlik >= 500 && ping_qlik <= 599) {
                %>
                <a class="navbar-brand"> QS-PING: KO </a>
                <%}%>
            </li>
            <li class="nav-item">
                <a class="nav-link">|</a>
            </li>
            <li>
                <a class="navbar-brand"><%=(String) request.getAttribute("user_logged_in") %>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link">|</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/logout">Logout</a>
            </li>
        </ul>
        <%--        <form class="form-inline my-2 my-lg-0">--%>
        <%--            <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">--%>
        <%--            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>--%>
        <%--        </form>--%>
    </div>
</nav>
<div class="container text-center">
    <form action="/addNewFarm" method="post">
        <div class="row">
            <div class="form-group col-md-6">
                <label for="description">Descrizione </label>
                <input class="form-control" type="text" name="description" id="description"
                       placeholder="es: FARM QS 12P" required>
            </div>
            <div class="form-group col-md-6">
                <label for="dbUser">Utenza DB </label>
                <input class="form-control" type="text" name="dbUser" id="dbUser" placeholder="utenza del DB" required>
            </div>
            <div class="form-group col-md-6">
                <label for="dbPassword">DB Password </label>
                <input class="form-control" type="text" name="dbPassword" id="dbPassword"
                       placeholder="la pwd dell utenza db" required>
            </div>
            <div class="form-group col-md-6">
                <label for="dbHost">DB Host </label>
                <input class="form-control" name="dbHost" placeholder="es: jdbc:postgresql://xxx.services.eni.intranet"
                       id="dbHost" required>
            </div>
            <div class="form-group col-md-6">
                <label for="dbPort">DB Port</label>
                <input class="form-control" name="dbPort" id="dbPort" placeholder="porta, es: 1531" required>
            </div>
            <div class="form-group col-md-6">
                <label for="dbSid">DB SID</label>
                <input class="form-control" name="dbSid" id="dbSid" placeholder="SID" required>
            </div>
        </div>
        <div class="row">
            <div class="form-group col-md-6">
                <label for="qsHost">QS Host </label>
                <input class="form-control" name="qsHost" id="qsHost" placeholder="es: XXlogXXk0n.hosts.eni.intranet"
                       required>
            </div>
            <div class="form-group col-md-6">
                <label for="qsPathClient">QS Client.jks path </label>
                <input class="form-control" name="qsPathClient" id="qsPathClient"
                       placeholder="es: C:/work/wada/certs/client.jks" required>
            </div>
            <div class="form-group col-md-6">
                <label for="qsPathRoot">QS Root.jks path </label>
                <input class="form-control" name="qsPathRoot" id="qsPathRoot"
                       placeholder="es: C:/work/wada/certs/root.jks"
                       required>
            </div>
            <div class="form-group col-md-6">
                <label for="qsXrfKey">XRF KEY </label>
                <input class="form-control" name="qsXrfKey" id="qsXrfKey" placeholder="Chiave xrf" required>
            </div>
            <div class="form-group col-md-6">
                <label for="qsKsPassword">QS JKS Password </label>
                <input class="form-control" name="qsKsPassword" id="qsKsPassword" placeholder="La password dei keystore"
                       required>
            </div>
            <div class="form-group col-md-6">
                <label for="qsUserHeader">QS User Header </label>
                <input class="form-control" name="qsUserHeader" id="qsUserHeader"
                       placeholder="es: UserDirectory=ENINET; UserId=ADMQSLAB01" required>
            </div>
            <div class="form-group col-md-6">
                <label for="note">Note </label>
                <input class="form-control" name="note" id="note" placeholder="eventuali note">
            </div>
            <div class="form-group col-md-6">
                <label for="environment">Environment
                    <select name="environment" class="form-control" id="environment">
                        <option>DEV</option>
                        <option>TEST</option>
                        <option>PRE</option>
                        <option>PROD</option>
                    </select>
                </label>
            </div>
            <div class="form-group col-md-6">
                <label for="came">Came </label>
                <input class="form-control" name="came" id="came" placeholder="es: 6440004" required>
            </div>
        </div>
        <input type="submit" value="Aggiungi" class="btn btn-success">
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