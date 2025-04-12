<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: UID0931174
  Date: 29/10/2019
  Time: 13:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html>
<jsp:include page="header.jsp"/>
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
    <c:when test="${empty farm}">
        <div class="container text-center">
            <h2>Nessun record e` presente</h2>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container text-center">
            <h2>Modifica FARM</h2>
            <hr>
            <form method="post" action="${pageContext.request.contextPath}/saveFarm">
                <div class="row">
                    <div class="form-group col-md-6">
                        <label for="farmId"><b>Id</b></label>
                        <input type="text" name="farmId" id="farmId" class="form-control form-control-sm"
                               value="${farm.farmid}" readonly required/>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="description"><b>Descrizione</b></label>
                        <input type="text" name="description" id="description" value="${farm.description}"
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
                        <input type="text" name="dbUser" id="dbUser" value="${farm.dbuser}"
                               class="form-control form-control-sm" required/>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbPassword"><b>DB Pwd</b></label>
                        <input type="password" name="dbPassword" id="dbPassword" value="${farm.dbpassword}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbHost"><b>DB Host</b></label>
                        <input type="text" name="dbHost" id="dbHost" value="${farm.dbhost}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbPort"><b>DB Port</b></label>
                        <input type="text" name="dbPort" id="dbPort" value="${farm.dbpassword}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbSid"><b>DB SID</b></label>
                        <input type="text" name="dbSid" id="dbSid" value="${farm.dbsid}"
                               class="form-control form-control-sm" required>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-6">
                        <label for="qsHost"><b>QS Host</b></label>
                        <input type="text" name="qsHost" id="qsHost" value="${farm.qshost}}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsReloadTaskName"><b>QS RTN</b></label>
                        <input type="text" name="qsReloadTaskName" id="qsReloadTaskName"
                               value="${farm.qsreloadtaskname}" class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsPathClientJKS"><b>QS ClientJKS</b></label>
                        <input type="text" name="qsPathClientJKS" id="qsPathClientJKS"
                               value="${farm.qspathclient}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsPathRootJKS"><b>QS RootJKS</b></label>
                        <input type="text" name="qsPathRootJKS" id="qsPathRootJKS" value="${farm.qspathroot}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsXrfKey"><b>QS XrfKey</b></label>
                        <input type="text" name="qsXrfKey" id="qsXrfKey" value="${farm.qsxrfkey}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsKeyStorePwd"><b>QS KsPwd</b></label>
                        <input type="text" name="qsKeyStorePwd" id="qsKeyStorePwd" value="${farm.qskspasswd}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsHeader"><b>QS Header</b></label>
                        <input type="text" name="qsHeader" id="qsHeader" value="${farm.qsuserheader}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="note"><b>Note</b></label>
                        <input type="text" name="note" id="note" value="${farm.note}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="came"><b>CAME</b></label>
                        <input type="text" name="came" id="came" value="${farm.came}"
                               class="form-control form-control-sm" required>
                    </div>
                </div>
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
