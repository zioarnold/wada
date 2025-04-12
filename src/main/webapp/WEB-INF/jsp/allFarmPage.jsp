<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: UID0931174
  Date: 29/10/2019
  Time: 12:33
  To change this template use File | Settings | File Templates.
--%>
<!doctype html>
<html>
<jsp:include page="header.jsp"/>
<body>
<jsp:include page="navbar.jsp"/>
<c:choose>
    <c:when test="${empty all_farm}">
        <div class="container text-center">
            <h2>Nessun record e` presente! Clicca su -> <a
                    href="${pageContext.request.contextPath}/addNewFarmPage"><span class="fa fa-plus"></span></a>
                per aggiungere FARM</h2>
        </div>
    </c:when>
    <c:otherwise>
        <h2 class="text-center">Elenco FARM, clicca su -> <a
                href="${pageContext.request.contextPath}/addNewFarmPage"><span class="fa fa-plus"></span></a>
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
                    <th>CAME</th>
                    <th hidden>DBUser</th>
                    <th hidden>DBPassword</th>
                    <th hidden>QsReloadTaskName</th>
                    <th hidden>QsClientJKS</th>
                    <th hidden>QsRootJKS</th>
                    <th hidden>QsXrfKey</th>
                    <th hidden>QsJksPwd</th>
                    <th hidden>QsHeader</th>
                    <th hidden>Note</th>
                    <th>Modifica</th>
                    <th>Elimina</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="farm" items="${all_farm}">
                    <tr>
                        <td class="u-farm-id">${farm.farmid}</td>
                        <td class="u-description">${farm.description}</td>
                        <td class="u-env">${farm.environment}</td>
                        <td class="u-db-host">${farm.dbhost}</td>
                        <td class="u-db-port">${farm.dbport}</td>
                        <td class="u-db-sid">${farm.dbsid}</td>
                        <td class="u-qs-host">${farm.qshost}</td>
                        <td class="u-came">${farm.came} </td>
                        <td class="u-db-user" hidden>${farm.dbuser}</td>
                        <td class="u-db-pwd" hidden>${farm.dbpassword}</td>
                        <td class="u-qs-task-name" hidden>${farm.qsreloadtaskname}</td>
                        <td class="u-qs-path-client" hidden>${farm.qspathclient}</td>
                        <td class="u-qs-path-root" hidden>${farm.qspathroot}</td>
                        <td class="u-qs-xrf-key" hidden>${farm.qsxrfkey}</td>
                        <td class="u-qs-key-store-pwd" hidden>${farm.qskspasswd}</td>
                        <td class="u-qs-header" hidden>${farm.qsuserheader}</td>
                        <td class="u-note" hidden>${farm.note}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/editFarm?farmId=${farm.farmid}"><span
                                    class="fa fa-pencil"></span></a>
                        </td>
                        <td><a href="${pageContext.request.contextPath}/deleteFarm?farmId=${farm.farmid}"><span
                                class="fa fa-trash"></span></a></td>
                    </tr>
                </c:forEach>
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
