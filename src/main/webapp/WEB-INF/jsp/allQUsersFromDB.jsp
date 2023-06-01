<%@ page import="eni.it.gsrestservice.model.QUsers" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%List<QUsers> qUsers = (List<QUsers>) request.getAttribute("qusers"); %>
<!doctype html>
<html lang="en">
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
<jsp:include page="navbar.jsp"/>
<c:choose>
    <c:when test="${empty qusers}">
        <div class="container text-center">
            <h2>Non ci sono utenze caricate sul DB!</h2>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container text-center">
            <h2 class="text-center">Utenze sul DB</h2>
            <hr>
            <div class="table-responsive">
                <table class="table table-striped table-bordered table-sm" id="myTable">
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
                            Altri dati
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        for (QUsers s : qUsers) {
                    %>
                    <tr>
                        <td class="u-user"><%=s.getUserId()%>
                        </td>
                        <td><%=s.getName()%>
                        </td>
                        <td><%=s.getUserIsActive()%> |
                            <button type="button" class="btn btn-primary btn-sm" data-toggle="modal"
                                    data-target="#con-close-modal-disable-user">
                                Modifica
                            </button>
                        </td>
                        <td>
                            <a href="/managementPageShowUserData?quser_filter=<%=s.getUserId()%>">
                                Mostrami altri dati </a>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </c:otherwise>
</c:choose>
<!-- Modal sync ruolo-->
<div class="modal" id="con-close-modal-disable-user" tabindex="-1" role="dialog" aria-labelledby="disableModalSyncLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="disableModalSyncLabel">Modifica</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="/managementPageDisableUser" method="post">
                <div class="modal-body">
                    Y - Utenza abilitata, N - Utenza disabilitata
                    <label>Utenza
                        <input type="text" name="userId" class="form-control input-group input-group-sm u-user"
                               readonly/>
                    </label>
                    <label class="control-label">
                        <select name="disableYN" class="form-control">
                            <option>Y</option>
                            <option>N</option>
                        </select>
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
        $('#con-close-modal-disable-user').on('show.bs.modal', function (e) {
            var _button = $(e.relatedTarget);
            var _row = _button.parents("tr");
            var uUser = _row.find(".u-user").text().trim();
            $(this).find(".u-user").val(uUser);
        });
    });
</script>
</body>
</html>