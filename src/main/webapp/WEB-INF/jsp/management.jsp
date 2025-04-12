<%--
  Created by IntelliJ IDEA.
  User: arn81
  Date: 29-Sep-19
  Time: 15:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="it">
<jsp:include page="header_management.jsp"/>
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
<br/>
<div class="container">
    <div class="input-group mb-3">
        <form action="${pageContext.request.contextPath}/managementPageShowUserData" method="post"
              style="text-transform: uppercase">
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <input class="btn btn-primary" type="submit" value="Cerca"/>
                </div>
                <div>
                    <input type="text" class="form-control" placeholder="Inserisci matricola"
                           aria-label="Inserisci matricola" aria-describedby="basic-addon2" name="quser_filter">
                </div>
            </div>
        </form>
    </div>
</div>
<div class="table-responsive">
    <c:choose>
        <c:when test="${empty quser_filter}">
            <div class="container text-center">
                <h2>Digitare la matricola, o utenza non esiste nel DB</h2>
            </div>
        </c:when>
        <c:otherwise>
            <div class="container text-center" id="userDiv">
                <h2 class="text-center">Utenza sul DB</h2>
                <hr>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-sm">
                        <thead>
                        <tr>
                            <th>
                                Matricola
                            </th>
                            <th>
                                Name
                            </th>
                            <th>
                                Utenza attiva
                            </th>
                            <th>
                                Opzioni
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td class="u-user">${quser_filter.userId}
                            </td>
                            <td class="u-name-delete">${quser_filter.name}
                            </td>
                            <td class="u-is-active-delete">${quser_filter.userIsActive}
                            </td>
                            <td>
                                <button type="button" class="btn btn-danger btn-sm" data-toggle="modal"
                                        data-target="#con-close-modal-user-delete">
                                    Elimina
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="container text-center" id="userDiv">
                <h2>Ruoli dell'utente</h2>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered" id="userRolesTable">
                        <thead>
                        <tr>
                            <th>
                                Tipo
                            </th>
                            <th>
                                ValoreDB
                            </th>
                            <th>
                                ValoreQS
                            </th>
                            <th>
                                Opzioni
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td class="u-role">${quser_filter.type}</td>
                            <c:choose>
                                <c:when
                                        test="${quser_filter.value.equals('organizzazione') || quser_filter.value.equals('email')}">
                                    <td>${quser_filter.value}</td>
                                    <td></td>
                                    <td><input type="submit" name="nonEditButton" class="btn btn-dark btn-sm"
                                               value="Non modificabile"/></td>
                                </c:when>
                                <c:when test="${quser_filter.type.equals('ruolo')}">
                                    <td class="u-value">${quser_filter.value}</td>
                                    <td>${quser_filter.userNewRole}</td>
                                    <td>
                                        <!-- Button trigger edit modal -->
                                        <button type="button" class="btn btn-primary btn-sm" data-toggle="modal"
                                                data-target="#con-close-modal-user-rule">
                                            Modifica
                                        </button>
                                        |
                                        <button type="button" class="btn btn-secondary btn-sm" data-toggle="modal"
                                                data-target="#con-close-modal-sync-user-role">
                                            Sync
                                        </button>
                                    </td>
                                    <td class="u-user" hidden>${quser_filter.userId}</td>
                                </c:when>
                                <c:otherwise>
                                    <td class="u-value">${quser_filter.value}</td>
                                    <td>
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-primary btn-sm" data-toggle="modal"
                                                data-target="#con-close-modal-user-group">
                                            Modifica
                                        </button>
                                        |
                                        <button type="button" class="btn btn-danger btn-sm" data-toggle="modal"
                                                data-target="#con-close-modal-delete">
                                            Elimina
                                        </button>
                                        |
                                        <button type="button" class="btn btn-success btn-sm" data-toggle="modal"
                                                data-target="#con-close-modal-add">Aggiungi
                                        </button>
                                    </td>
                                    <td class="u-user" hidden>${quser_filter.userId} </td>
                                </c:otherwise>
                            </c:choose>
                            <td>${quser_filter.userNewRole}</td>
                            <td>
                                <!-- Button trigger edit modal -->
                                <button type="button" class="btn btn-primary btn-sm" data-toggle="modal"
                                        data-target="#con-close-modal-user-rule">
                                    Modifica
                                </button>
                                |
                                <button type="button" class="btn btn-secondary btn-sm" data-toggle="modal"
                                        data-target="#con-close-modal-sync-user-role">
                                    Sync
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<!-- Modal sync ruolo-->
<div class="modal" id="con-close-modal-sync-user-role" tabindex="-1" role="dialog" aria-labelledby="editModalSyncLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editModalSyncLabel">Sincronizzazione</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="${pageContext.request.contextPath}/managementPageSyncTypeRole" method="post">
                <div class="modal-body">
                    Sicuro di voler sincronizzare?
                    <hr>
                    Attenzione! Verr&agrave; preso il dato attuale dalla QMC e storato nel DB!
                    <label hidden>Utenza
                        <input type="text" name="userId" class="form-control input-group input-group-sm u-user"
                               readonly hidden/>
                    </label>
                    <label hidden>Attuale ruolo
                        <input type="text" name="oldRole" class="form-control input-group input-group-sm u-value"
                               readonly hidden/>
                    </label>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal"
                            id="close-modal-sync-user-role" name="close-modal-sync-user-role">Annulla
                    </button>
                    <input type="submit" class="btn btn-primary" value="Si"/>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- Modal edit ruolo-->
<div class="modal" id="con-close-modal-user-rule" tabindex="-1" role="dialog" aria-labelledby="editModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editModalLabel">Modifica</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="${pageContext.request.contextPath}/managementPageEditTypeRole" method="post">
                <div class="modal-body">
                    <label>Utenza
                        <input type="text" name="userId" class="form-control input-group input-group-sm u-user"
                               readonly/>
                    </label>
                    <label>Campo ruolo
                        <input type="text" name="roleGroup" class="form-control input-group input-group-sm u-role"
                               readonly/>
                    </label>
                    <label>Attuale ruolo
                        <input type="text" name="oldRole" class="form-control input-group input-group-sm u-value"
                               readonly/>
                    </label>
                    <label>Nuovo ruolo
                        <select name="newUserRole" class="form-control">
                            <c:choose>
                                <c:when test="${empty rolesList}">
                                    <option>Viewer</option>
                                    <option>Analyzer</option>
                                    <option>Contributor</option>
                                    <option>SuperUserAM</option>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="rolesList" var="rl">
                                        <option>${rl}</option>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </select>
                    </label>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal"
                            id="close-modal-user-rule" name="close-modal-user-rule">Annulla
                    </button>
                    <input type="submit" class="btn btn-primary" value="Si"/>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- Modal edit group-->
<div class="modal" id="con-close-modal-user-group" tabindex="-1" role="dialog"
     aria-labelledby="editGroupModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">u-user
                <h5 class="modal-title" id="editGroupModalLabel">Modifica</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="${pageContext.request.contextPath}/managementPageEditTypeRole" method="post">
                <div class="modal-body">
                    <label>Utenza
                        <input type="text" name="userId" class="form-control input-group input-group-sm u-user"
                               readonly/>
                    </label>
                    <label>Campo gruppo
                        <input type="text" name="roleGroup" class="form-control input-group input-group-sm u-role"
                               readonly/>
                    </label>
                    <label>Attuale gruppo
                        <input type="text" name="oldRole" class="form-control input-group input-group-sm u-value"
                               readonly/>
                    </label>
                    <label>Nuovo gruppo
                        <input type="text" name="newUserRole" class="form-control input-group input-group-sm"/>
                    </label>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" id="close-modal-user-group"
                            name="close-modal-user-group">Annulla
                    </button>
                    <input type="submit" class="btn btn-primary" value="Si"/>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- Modal delete group-->
<div class="modal" id="con-close-modal-delete" tabindex="-1" role="dialog"
     aria-labelledby="deleteRoleGroupModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteRoleGroupModalLabel">Elimina</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="managementPageDeleteTypeRole" method="post">
                <div class="modal-body">
                    Sicuro di voler eliminare?
                    <label hidden>Utenza
                        <input type="text" name="userId" class="form-control input-group input-group-sm u-user"
                               readonly hidden/>
                    </label>
                    <label hidden>Campo gruppo/ruolo
                        <input type="text" name="type" class="form-control input-group input-group-sm u-role"
                               readonly hidden/>
                    </label>
                    <label hidden>Attuale gruppo/ruolo
                        <input type="text" name="value" class="form-control input-group input-group-sm u-value"
                               readonly hidden/>
                    </label>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" id="close-modal-delete"
                            name="close-modal-delete">Annulla
                    </button>
                    <input type="submit" class="btn btn-primary" value="Si"/>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- Modal edit-->
<div class="modal" id="con-close-modal-add" tabindex="-1" role="dialog" aria-labelledby="addModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addModalLabel">Aggiungi</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="${pageContext.request.contextPath}/managementPageAddTypeRole" method="post">
                <div class="modal-body">
                    <label>Utenza
                        <input type="text" name="userId" class="form-control input-group input-group-sm u-user"
                               readonly/>
                    </label>
                    <label hidden>Campo gruppo/ruolo
                        <input hidden type="text" name="type" class="form-control input-group input-group-sm u-role"
                               readonly/>
                    </label>
                    <label>Campo gruppo
                        <input type="text" name="userGroup" class="form-control input-group input-group-sm"/>
                    </label>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" id="close-modal-add"
                            name="close-modal-add">Annulla
                    </button>
                    <input type="submit" class="btn btn-primary" value="Si"/>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- Modal delete user-->
<div class="modal" id="con-close-modal-user-delete" tabindex="-1" role="dialog"
     aria-labelledby="deleteUserModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteUserModalLabel">Elimina</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="managementPageDelete" method="post">
                <div class="modal-body">
                    Sicuro di voler eliminare?
                    <hr>
                    Attenzione, verranno eliminate anche i suoi ruoli relative
                    <%--suppress HtmlFormInputWithoutLabel --%>
                    <input type="text" name="userId" class="form-control input-group input-group-sm u-user"
                           readonly hidden/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" id="close-modal-user-delete"
                            name="close-modal-user-delete">Annulla
                    </button>
                    <input type="submit" class="btn btn-primary" value="Si"/>
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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function () {
        $('#con-close-modal-user-group').on('show.bs.modal', function (e) {
            const _button = $(e.relatedTarget);
            // console.log(_button, _button.parents("tr"));
            const _row = _button.parents("tr");
            const uRole = _row.find(".u-role").text().trim();
            const uValue = _row.find(".u-value").text().trim();
            const uUser = _row.find(".u-user").text().trim();
            // console.log(_invoiceAmt, _chequeAmt);
            $(this).find(".u-role").val(uRole);
            $(this).find(".u-value").val(uValue);
            $(this).find(".u-user").val(uUser);
        });
    });
</script>

<script>
    $(document).ready(function () {
        $('#con-close-modal-user-rule').on('show.bs.modal', function (e) {
            const _button = $(e.relatedTarget);
            // console.log(_button, _button.parents("tr"));
            const _row = _button.parents("tr");
            const uRole = _row.find(".u-role").text().trim();
            const uValue = _row.find(".u-value").text().trim();
            const uUser = _row.find(".u-user").text().trim();
            // console.log(_invoiceAmt, _chequeAmt);
            $(this).find(".u-role").val(uRole);
            $(this).find(".u-value").val(uValue);
            $(this).find(".u-user").val(uUser);
        });
    });
</script>

<script>
    $(document).ready(function () {
        $('#con-close-modal-delete').on('show.bs.modal', function (e) {
            const _button = $(e.relatedTarget);
            // console.log(_button, _button.parents("tr"));
            const _row = _button.parents("tr");
            const uRole = _row.find(".u-role").text().trim();
            const uValue = _row.find(".u-value").text().trim();
            const uUser = _row.find(".u-user").text().trim();
            // console.log(_invoiceAmt, _chequeAmt);
            $(this).find(".u-role").val(uRole);
            $(this).find(".u-value").val(uValue);
            $(this).find(".u-user").val(uUser);
        });
    });
</script>

<script>
    $(document).ready(function () {
        $('#con-close-modal-user-delete').on('show.bs.modal', function (e) {
            const _button = $(e.relatedTarget);
            // console.log(_button, _button.parents("tr"));
            const _row = _button.parents("tr");
            const uUser = _row.find(".u-user").text().trim();
            // console.log(_invoiceAmt, _chequeAmt);
            $(this).find(".u-user").val(uUser);
        });
    });
</script>

<script>
    $(document).ready(function () {
        $('#con-close-modal-add').on('show.bs.modal', function (e) {
            const _button = $(e.relatedTarget);
            // console.log(_button, _button.parents("tr"));
            const _row = _button.parents("tr");
            const uUser = _row.find(".u-user").text().trim();
            const uRole = _row.find(".u-role").text().trim();
            // console.log(_invoiceAmt, _chequeAmt);
            $(this).find(".u-user").val(uUser);
            $(this).find(".u-role").val(uRole);
        });
    });
</script>

<script>
    $(document).ready(function () {
        $('#con-close-modal-sync-user-role').on('show.bs.modal', function (e) {
            const _button = $(e.relatedTarget);
            const _row = _button.parents("tr");
            const uUser = _row.find(".u-user").text().trim();
            const uValue = _row.find(".u-value").text().trim();
            $(this).find(".u-user").val(uUser);
            $(this).find(".u-value").val(uValue);
        });
    });
</script>

<script>
    $(document).ready(function () {
        $('#userRolesTable').dataTable({
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
                    '</select>record'
            }
        });
    })
</script>
</body>
</html>
