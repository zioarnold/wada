<%@ page import="eni.it.gsrestservice.model.LDAPUser" %>
<%@ page import="java.util.ArrayList" %>
<!doctype html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatibile" content="IE=edge"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <title>Eni Qlik Tool User Management</title>
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <link rel="stylesheet" href="css/navbar.css"/>
    <link rel="stylesheet" href="css/background.css"/>
    <link type="text/javascript" href="js/bootstrap.js"/>
    <!-- Load an icon library to show a hamburger menu (bars) on small screens -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">Qlik Users Mgmt</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="/">Home <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownUpload" role="button"
                   data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    Caricamento
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownSearch">
                    <a class="dropdown-item" href="/massiveUploadPage">Caricamento massivo</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="/singleUpload">Caricamento singolo</a>
                </div>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownSearch" role="button"
                   data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    Ricerca
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownSearch">
                    <a class="dropdown-item" href="/searchquserondbpage">Ricerca su DB</a>
                    <a class="dropdown-item" href="/searchuseronldap">Ricerca su ADLDS</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="/allqlikusersfromdb">Estrazione completa DB</a>
                    <%--                    <a class="dropdown-item" href="/allusersfromldap">Estrazione completa ADLDS</a>--%>
                </div>
            </li>
            <li class="nav-item">
                <a href="/managementPage" class="nav-link">Gestione</a>
            </li>
            <li class="nav-item">
                <a href="/userGuide" class="nav-link">Manuale</a>
            </li>
            <li class="nav-item">
                <a href="/assistance" class="nav-link">Assistenza</a>
            </li>
        </ul>
        <%--        <form class="form-inline my-2 my-lg-0">--%>
        <%--            <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">--%>
        <%--            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>--%>
        <%--        </form>--%>
    </div>
</nav>
<div class="container text-center">
    <form action="/searchuseronldap" method="get">
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
            <h2>Nessun utenza digitata oppure utenza non e' presente sull'ADLDS</h2>
        </div>
    </c:when>
    <c:otherwise>
        <%
            ArrayList<LDAPUser> std = (ArrayList<LDAPUser>) request.getAttribute("userIDDATA");
            for (LDAPUser s : std) {
        %>
        <div class="container text-center" id="userDiv">
            <h2>Utenza su ADLDS</h2>
            <hr>
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <thead>
                    <tr>
                        <th>
                            displayName
                        </th>
                        <th>
                            eniMatricolaNotes
                        </th>
                        <th>
                            name
                        </th>
                        <th>
                            mail
                        </th>
                        <th>
                            givenName
                        </th>
                        <th>
                            sn
                        </th>
                        <th>
                            badPwdCount
                        </th>
                        <th>
                            pwdLastSet
                        </th>
                        <th>
                            userAccountDisabled
                        </th>
                        <th>
                            userDontExpirePassword
                        </th>
                        <th>
                            memberOf
                        </th>
                        <th>
                            ou
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <%=s.getDisplayName()%>
                        </td>
                        <td><%=s.getENIMatricolaNotes() %>
                        </td>
                        <td><%=s.getName() %>
                        </td>
                        <td><%=s.getMail() %>
                        </td>
                        <td><%=s.getGivenName() %>
                        </td>
                        <td><%=s.getSn() %>
                        </td>
                        <td><%=s.getBadPwdCount() %>
                        </td>
                        <td><%=s.getPwdLastSet() %>
                        </td>
                        <td><%=s.getUserAccountDisabled() %>
                        </td>
                        <td><%=s.getUserDontExpirePassword() %>
                        </td>
                        <td><%=s.getMemberOf() %>
                        </td>
                        <td><%=s.getOu() %>
                        </td>
                    </tr>
                    <%}%>
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