<%@ page import="eni.it.gsrestservice.model.QUsers" %>
<%@ page import="java.util.List" %>
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
<% //noinspection unchecked
    List<QUsers> quser_filter = (List<QUsers>) request.getAttribute("quser_filter");
    List<String> rolesList = (List<String>) request.getAttribute("rolesList");%>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge"/>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta about="Made by UID0931174 aka Zaki"/>
    <title>Eni Qlik Tool User Management</title>
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <link rel="stylesheet" href="css/background.css"/>
    <link rel="stylesheet" href="css/all.css"/>
    <link type="text/javascript" href="js/jquery-2.1.1.js">
    <link rel="stylesheet" href="http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
    <link type="text/javascript" href="js/bootstrap.js"/>
    <link rel="shortcut icon" href="ico/favicon.ico"/>
    <!-- Load an icon library to show a hamburger menu (bars) on small screens -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
</head>
<body>
<jsp:include page="navbar.jsp"/>
<br/>
<div class="container">
    <div class="input-group mb-3">
        <form action="/managementPageShowUserData" method="post" style="text-transform: uppercase">
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
                            <td class="u-user"><%=quser_filter.get(0).getUserId()%>
                            </td>
                            <td class="u-name-delete"><%=quser_filter.get(0).getName()%>
                            </td>
                            <td class="u-is-active-delete"><%=quser_filter.get(0).getUserIsActive()%>
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
                            <%
                                for (QUsers u : quser_filter) {
                            %>
                            <td class="u-role"><%=u.getType()%>
                            </td>
                            <%
                                if (u.getType().equals("organizzazione") || u.getType().equals("email")) {
                            %>
                            <td><%=u.getValue()%>
                            </td>
                            <td></td>
                            <td><input type="submit" name="nonEditButton" class="btn btn-dark btn-sm"
                                       value="Non modificabile"/></td>
                            <%
                            } else if (u.getType().equals("ruolo")) {
                            %>
                            <td class="u-value"><%=u.getValue()%>
                            </td>
                            <td><%=u.getNewUserRole()%>
                            </td>
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
                            <td class="u-user" hidden><%=u.getUserId()%>
                            </td>
                            <%
                            } else {
                            %>
                            <td class="u-value"><%=u.getValue()%>
                            </td>
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
                            <td class="u-user" hidden><%=u.getUserId()%>
                            </td>
                            <%}%>
                        </tr>
                        <%
                            }
                        %>
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
            <form action="/managementPageSyncTypeRole" method="post">
                <div class="modal-body">
                    Sicuro di voler sincronizzare?
                    <hr>
                    Attenzione! Verra' preso il dato attuale dalla QMC e storato nel DB!
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
            <form action="/managementPageEditTypeRole" method="post">
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
            <form action="/managementPageEditTypeRole" method="post">
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
            <form action="/managementPageAddTypeRole" method="post">
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
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function () {
        $('#con-close-modal-user-group').on('show.bs.modal', function (e) {
            var _button = $(e.relatedTarget);
            // console.log(_button, _button.parents("tr"));
            var _row = _button.parents("tr");
            var uRole = _row.find(".u-role").text().trim();
            var uValue = _row.find(".u-value").text().trim();
            var uUser = _row.find(".u-user").text().trim();
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
            var _button = $(e.relatedTarget);
            // console.log(_button, _button.parents("tr"));
            var _row = _button.parents("tr");
            var uRole = _row.find(".u-role").text().trim();
            var uValue = _row.find(".u-value").text().trim();
            var uUser = _row.find(".u-user").text().trim();
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
            var _button = $(e.relatedTarget);
            // console.log(_button, _button.parents("tr"));
            var _row = _button.parents("tr");
            var uRole = _row.find(".u-role").text().trim();
            var uValue = _row.find(".u-value").text().trim();
            var uUser = _row.find(".u-user").text().trim();
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
            var _button = $(e.relatedTarget);
            // console.log(_button, _button.parents("tr"));
            var _row = _button.parents("tr");
            var uUser = _row.find(".u-user").text().trim();
            // console.log(_invoiceAmt, _chequeAmt);
            $(this).find(".u-user").val(uUser);
        });
    });
</script>

<script>
    $(document).ready(function () {
        $('#con-close-modal-add').on('show.bs.modal', function (e) {
            var _button = $(e.relatedTarget);
            // console.log(_button, _button.parents("tr"));
            var _row = _button.parents("tr");
            var uUser = _row.find(".u-user").text().trim();
            var uRole = _row.find(".u-role").text().trim();
            // console.log(_invoiceAmt, _chequeAmt);
            $(this).find(".u-user").val(uUser);
            $(this).find(".u-role").val(uRole);
        });
    });
</script>

<script>
    $(document).ready(function () {
        $('#con-close-modal-sync-user-role').on('show.bs.modal', function (e) {
            var _button = $(e.relatedTarget);
            var _row = _button.parents("tr");
            var uUser = _row.find(".u-user").text().trim();
            var uValue = _row.find(".u-value").text().trim();
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
