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
<jsp:include page="navbar.jsp"/>
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
