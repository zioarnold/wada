<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<jsp:include page="header.jsp"/>
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
                    <c:forEach items="${qusers}" var="qu">
                        <tr>
                            <td>${qu.userid}</td>
                            <td>${qu.name}</td>
                            <td>${qu.userIsActive}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/managementPageShowUserData?quser_filter=${}">
                                    Mostrami altri dati </a></td>
                            <td>
                                <button type="button" class="btn btn-primary btn-sm" data-toggle="modal"
                                        data-target="#con-close-modal-disable-user">
                                    Modifica
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
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
            <form action="${pageContext.request.contextPath}/managementPageDisableUser" method="post">
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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
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
            const _button = $(e.relatedTarget);
            const _row = _button.parents("tr");
            const uUser = _row.find(".u-user").text().trim();
            $(this).find(".u-user").val(uUser);
        });
    });
</script>
</body>
</html>