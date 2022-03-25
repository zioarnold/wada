<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: UID0931174
  Date: 29/10/2019
  Time: 12:33
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
    <link type="text/javascript" href="js/bootstrap.js"/>
    <link rel="stylesheet" href="http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
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
                <a class="nav-link" href="/report">Report</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/allFarmPage">Farms</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/allAdminsPage">Admins</a>
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
<c:choose>
    <c:when test="${empty all_farm}">
        <div class="container text-center">
            <h2>Nessun record e` presente! Clicca su -> <a href="/addNewFarmPage"><span class="fa fa-plus"></span></a>
                per aggiungere FARM</h2>
        </div>
    </c:when>
    <c:otherwise>
        <h2 class="text-center">Elenco FARM, clicca su -> <a href="/addNewFarmPage"><span class="fa fa-plus"></span></a>
            per aggiungere FARM</h2>
        <hr>
        <div class="table-responsive">
            <table class="table table-striped table-bordered table-sm" id="myTable">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Descrizione</th>
                    <th>Ambiente</th>
                    <th>DBHost</th>
                    <th>DBPort</th>
                    <th>DBSid</th>
                    <th>QSHost</th>
                    <th>Came</th>
                    <th hidden>DBUser</th>
                    <th hidden>DBPassword</th>
                    <th hidden>QsReloadTaskName</th>
                    <th hidden>QsClientJKS</th>
                    <th hidden>QsRootJKS</th>
                    <th hidden>QsXrfKey</th>
                    <th hidden>QsJksPwd</th>
                    <th hidden>QsHeader</th>
                    <th hidden>Note</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="farm" items="${all_farm}">
                    <tr>
                        <td class="u-farm-id">${farm.farmId}</td>
                        <td class="u-description">${farm.description}</td>
                        <td class="u-env">${farm.environment}</td>
                        <td class="u-db-host">${farm.dbHost}</td>
                        <td class="u-db-port">${farm.dbPort}</td>
                        <td class="u-db-sid">${farm.dbSid}</td>
                        <td class="u-qs-host">${farm.qsHost}</td>
                        <td class="u-came">${farm.came} </td>
                        <td class="u-db-user" hidden>${farm.dbUser}</td>
                        <td class="u-db-pwd" hidden>${farm.dbPassword}</td>
                        <td class="u-qs-task-name" hidden>${farm.qsReloadTaskName}</td>
                        <td class="u-qs-path-client" hidden>${farm.qsPathClientJKS}</td>
                        <td class="u-qs-path-root" hidden>${farm.qsPathRootJKS}</td>
                        <td class="u-qs-xrf-key" hidden>${farm.qsXrfKey}</td>
                        <td class="u-qs-key-store-pwd" hidden>${farm.qsKeyStorePwd}</td>
                        <td class="u-qs-header" hidden>${farm.qsHeader}</td>
                        <td class="u-note" hidden>${farm.note}</td>
                        <td>
                            <button type="button" class="btn btn-primary btn-sm" data-toggle="modal"
                                    data-target="#con-close-modal-add">Modifica
                            </button>
                        </td>
                        <td><a href="/deleteFarm?farmId=${farm.farmId}"><span class="fa fa-trash"></span></a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>
<!-- Modal edit-->
<div class="modal" id="con-close-modal-add" tabindex="-1" role="dialog" aria-labelledby="addModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addModalLabel">Modifica/Dettaglio</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="/saveFarm" method="post">
                <div class="modal-body">
                    <label hidden>Farm ID
                        <input type="text" name="farmId" class="form-control input-group input-group-sm u-farm-id"
                               readonly hidden/>
                    </label>
                    <label>Descrizione
                        <input type="text" name="description"
                               class="form-control input-group input-group-sm u-description"/>
                    </label>
                    <label>Ambiente
                        <select name="environment" class="form-control input-group-sm input-group">
                            <option>DEV</option>
                            <option>TEST</option>
                            <option>PRE</option>
                            <option>PROD</option>
                        </select>
                    </label>
                    <label>DB User
                        <input type="text" name="dbUser" class="form-control input-group input-group-sm u-db-user"/>
                    </label>
                    <label>DB Pwd
                        <input type="text" name="dbPassword"
                               class="form-control input-group input-group-sm u-db-pwd">
                    </label>
                    <label>DB Host
                        <input type="text" name="dbHost"
                               class="form-control input-group input-group-sm u-db-host">
                    </label>
                    <label>DB Port
                        <input type="text" name="dbPort"
                               class="form-control input-group input-group-sm u-db-port">
                    </label>
                    <label>DB SID
                        <input type="text" name="dbSid"
                               class="form-control input-group input-group-sm u-db-sid">
                    </label>
                    <label>QS Host
                        <input type="text" name="qsHost"
                               class="form-control input-group input-group-sm u-qs-host">
                    </label>
                    <label>QS RTN
                        <input type="text" name="qsReloadTaskName"
                               class="form-control input-group input-group-sm u-qs-task-name">
                    </label>
                    <label>QS ClientJKS
                        <input type="text" name="qsPathClientJKS"
                               class="form-control input-group input-group-sm u-qs-path-client">
                    </label>
                    <label>QS RootJKS
                        <input type="text" name="qsPathRootJKS"
                               class="form-control input-group input-group-sm u-qs-path-root">
                    </label>
                    <label>QS XrfKey
                        <input type="text" name="qsXrfKey"
                               class="form-control input-group input-group-sm u-qs-xrf-key">
                    </label>
                    <label>QS KsPwd
                        <input type="text" name="qsKeyStorePwd"
                               class="form-control input-group input-group-sm u-qs-key-store-pwd">
                    </label>
                    <label>QS Header
                        <input type="text" name="qsHeader"
                               class="form-control input-group input-group-sm u-qs-header">
                    </label>
                    <label>Note
                        <input type="text" name="note"
                               class="form-control input-group input-group-sm u-note">
                    </label>
                    <label>Came
                        <input type="text" name="came"
                               class="form-control input-group input-group-sm u-came">
                    </label>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger btn-sm" data-dismiss="modal" id="close-modal-add"
                            name="close-modal-add">Annulla
                    </button>
                    <input type="submit" class="btn btn-success btn-sm" value="OK"/>
                </div>
            </form>
        </div>
    </div>
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
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function () {
        $('#myTable').dataTable({
            "oLanguage": {
                "oPaginate": {
                    "sPrevious": "Pagina precedente",
                    "sNext": "Prossima pagina",
                    "sLast": "Ultima pagina"
                },
                "sInfo": "Mostro da _START_ a _END_ di _TOTAL_ record totali",
                "sSearch": "Cerca:",
                "sLoadingRecords": "Attendere - caricamento in corso...",
                "sLengthMenu": 'Mostra <select>' +
                    '<option value="10">10</option>' +
                    '<option value="20">20</option>' +
                    '<option value="30">30</option>' +
                    '<option value="40">40</option>' +
                    '<option value="50">50</option>' +
                    '<option value="-1">Tutto</option>' +
                    '</select> record'
            }
        });
    })
</script>
<script>
    $(document).ready(function () {
        $('#con-close-modal-add').on('show.bs.modal', function (e) {
            var _button = $(e.relatedTarget);
            // console.log(_button, _button.parents("tr"));
            var _row = _button.parents("tr");
            var uFarmId = _row.find(".u-farm-id").text().trim();
            var uDescription = _row.find(".u-description").text().trim();
            var uEnv = _row.find(".u-env").text().trim();
            var uDbUser = _row.find(".u-db-user").text().trim();
            var uDbPwd = _row.find(".u-db-pwd").text().trim();
            var uDbHost = _row.find(".u-db-host").text().trim();
            var uDbPort = _row.find(".u-db-port").text().trim();
            var uDbSid = _row.find(".u-db-sid").text().trim();
            var uQsHost = _row.find(".u-qs-host").text().trim();
            var uQsTaskName = _row.find(".u-qs-task-name").text().trim();
            var uQsPathClient = _row.find(".u-qs-path-client").text().trim();
            var uQsPathRoot = _row.find(".u-qs-path-root").text().trim();
            var uQsXrfKey = _row.find(".u-qs-xrf-key").text().trim();
            var uQsKeyStorePwd = _row.find(".u-qs-key-store-pwd").text().trim();
            var uQsHeader = _row.find(".u-qs-header").text().trim();
            var uQsNote = _row.find(".u-note").text().trim();
            var uQsCame = _row.find(".u-came").text().trim();
            // console.log(_invoiceAmt, _chequeAmt);
            $(this).find(".u-farm-id").val(uFarmId);
            $(this).find(".u-description").val(uDescription);
            $(this).find(".u-env").val(uEnv);
            $(this).find(".u-db-user").val(uDbUser);
            $(this).find(".u-db-pwd").val(uDbPwd);
            $(this).find(".u-db-host").val(uDbHost);
            $(this).find(".u-db-port").val(uDbPort);
            $(this).find(".u-db-sid").val(uDbSid);
            $(this).find(".u-qs-host").val(uQsHost);
            $(this).find(".u-qs-task-name").val(uQsTaskName);
            $(this).find(".u-qs-path-client").val(uQsPathClient);
            $(this).find(".u-qs-path-root").val(uQsPathRoot);
            $(this).find(".u-qs-xrf-key").val(uQsXrfKey);
            $(this).find(".u-qs-key-store-pwd").val(uQsKeyStorePwd);
            $(this).find(".u-qs-header").val(uQsHeader);
            $(this).find(".u-note").val(uQsNote);
            $(this).find(".u-came").val(uQsCame);
        });
    });
</script>
</body>
</html>
