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
            const _button = $(e.relatedTarget);
            // console.log(_button, _button.parents("tr"));
            const _row = _button.parents("tr");
            const uFarmId = _row.find(".u-farm-id").text().trim();
            const uDescription = _row.find(".u-description").text().trim();
            const uEnv = _row.find(".u-env").text().trim();
            const uDbUser = _row.find(".u-db-user").text().trim();
            const uDbPwd = _row.find(".u-db-pwd").text().trim();
            const uDbHost = _row.find(".u-db-host").text().trim();
            const uDbPort = _row.find(".u-db-port").text().trim();
            const uDbSid = _row.find(".u-db-sid").text().trim();
            const uQsHost = _row.find(".u-qs-host").text().trim();
            const uQsTaskName = _row.find(".u-qs-task-name").text().trim();
            const uQsPathClient = _row.find(".u-qs-path-client").text().trim();
            const uQsPathRoot = _row.find(".u-qs-path-root").text().trim();
            const uQsXrfKey = _row.find(".u-qs-xrf-key").text().trim();
            const uQsKeyStorePwd = _row.find(".u-qs-key-store-pwd").text().trim();
            const uQsHeader = _row.find(".u-qs-header").text().trim();
            const uQsNote = _row.find(".u-note").text().trim();
            const uQsCame = _row.find(".u-came").text().trim();
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
