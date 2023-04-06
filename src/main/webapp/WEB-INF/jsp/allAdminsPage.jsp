<%@ page import="eni.it.gsrestservice.model.QsAdmins" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: arn81
  Date: 06-Nov-19
  Time: 21:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%List<QsAdmins> qsAdmins = (List<QsAdmins>) request.getAttribute("all_admins"); %>
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
<c:choose>
    <c:when test="${empty all_admins}">
        <div class="container text-center">
            <h2>Nessun record e` presente! Clicca su -> <a href="/addAdminPage"><span class="fa fa-plus"></span></a> per
                aggiungere un moder o un admin</h2>
        </div>
    </c:when>
    <c:otherwise>
        <h2 class="text-center">Elenco Admins/Moders, clicca su -> <a href="/addAdminPage"><span
                class="fa fa-plus"></span></a>
            per aggiungere un moder o un admin</h2>
        <hr>
        <div class="table-responsive">
            <table class="table table-striped table-bordered table-sm" id="myTable">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Username</th>
                    <th>Autenticato</th>
                    <th>Ruolo</th>
                    <th>Data login</th>
                    <th>Modifica</th>
                    <th>Elimina</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for (QsAdmins s : qsAdmins) {
                %>
                <tr>
                    <td><%=s.getId()%>
                    </td>
                    <td><%=s.getUsername()%>
                    </td>
                    <td><%=s.getAuth()%>
                    </td>
                    <td><%=s.getRole()%>
                    </td>
                    <td><%=s.getCurrentSessionLoginTime()%>
                    </td>
                    <td><a href="/editAdmin?adminId=<%=s.getId()%>"><span class="fa fa-pencil"></span></a></td>
                    <td><a href="/deleteAdmin?adminId=<%=s.getId()%>"><span class="fa fa-trash"></span></a></td>
                </tr>
                <% } %>
                </tbody>
            </table>
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
</body>
</html>
