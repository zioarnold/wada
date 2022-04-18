<%@ page import="eni.it.gsrestservice.model.QsFarms" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: UID0931174
  Date: 29/10/2019
  Time: 13:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta about="Made by UID0931174 aka Zaki"/>
    <title>Eni Qlik Tool User Management</title>
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <link rel="stylesheet" href="css/background.css"/>
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
<% List<QsFarms> qsFarms = (List<QsFarms>) request.getAttribute("farm"); %>
<c:choose>
    <c:when test="${empty farm}">
        <div class="container text-center">
            <h2>Nessun record e` presente</h2>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container text-center">
            <h2>Modifica FARM</h2>
            <hr>
            <% for (QsFarms farms : qsFarms) {
            %>
            <form method="post" action="/saveFarm">
                <div class="row">
                    <div class="form-group col-md-6">
                        <label for="farmId"><b>Id</b></label>
                        <input type="text" name="farmId" id="farmId" class="form-control form-control-sm"
                               value="<%=farms.getFarmId()%>" readonly required/>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="description"><b>Descrizione</b></label>
                        <input type="text" name="description" id="description" value="<%=farms.getDescription()%>"
                               class="form-control form-control-sm" required/>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="environment"><b>Ambiente</b></label>
                        <select name="environment" id="environment" class="form-control form-control-sm" required>
                            <option>DEV</option>
                            <option>TEST</option>
                            <option>PRE</option>
                            <option>PROD</option>
                        </select>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbUser"><b>DB User</b></label>
                        <input type="text" name="dbUser" id="dbUser" value="<%=farms.getDbUser()%>"
                               class="form-control form-control-sm" required/>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbPassword"><b>DB Pwd</b></label>
                        <input type="text" name="dbPassword" id="dbPassword" value="<%=farms.getDbPassword()%>"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbHost"><b>DB Host</b></label>
                        <input type="text" name="dbHost" id="dbHost" value="<%=farms.getDbHost()%>"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbPort"><b>DB Port</b></label>
                        <input type="text" name="dbPort" id="dbPort" value="<%=farms.getDbPort()%>"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbSid"><b>DB SID</b></label>
                        <input type="text" name="dbSid" id="dbSid" value="<%=farms.getDbSid()%>"
                               class="form-control form-control-sm" required>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-6">
                        <label for="qsHost"><b>QS Host</b></label>
                        <input type="text" name="qsHost" id="qsHost" value="<%=farms.getQsHost()%>"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsReloadTaskName"><b>QS RTN</b></label>
                        <input type="text" name="qsReloadTaskName" id="qsReloadTaskName"
                               value="<%=farms.getQsReloadTaskName()%>" class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsPathClientJKS"><b>QS ClientJKS</b></label>
                        <input type="text" name="qsPathClientJKS" id="qsPathClientJKS"
                               value="<%=farms.getQsPathClientJKS()%>"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsPathRootJKS"><b>QS RootJKS</b></label>
                        <input type="text" name="qsPathRootJKS" id="qsPathRootJKS" value="<%=farms.getQsPathRootJKS()%>"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsXrfKey"><b>QS XrfKey</b></label>
                        <input type="text" name="qsXrfKey" id="qsXrfKey" value="<%=farms.getQsXrfKey()%>"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsKeyStorePwd"><b>QS KsPwd</b></label>
                        <input type="text" name="qsKeyStorePwd" id="qsKeyStorePwd" value="<%=farms.getQsKeyStorePwd()%>"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsHeader"><b>QS Header</b></label>
                        <input type="text" name="qsHeader" id="qsHeader" value="<%=farms.getQsHeader()%>"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="note"><b>Note</b></label>
                        <input type="text" name="note" id="note" value="<%=farms.getNote()%>"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="came"><b>CAME</b></label>
                        <input type="text" name="came" id="came" value="<%=farms.getCame()%>"
                               class="form-control form-control-sm" required>
                    </div>
                </div>
                <% } %>
                <div class="text-center">
                    <button type="submit" class="btn btn-success btn-sm">Salva</button>
                </div>
            </form>
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
