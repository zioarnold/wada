<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <link rel="shortcut icon" href="ico/favicon.ico"/>
    <!-- Load an icon library to show a hamburger menu (bars) on small screens -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
</head>
<body>
<jsp:include page="navbar.jsp"/>
<c:choose>
    <c:when test="${empty other_data}">
        <div class="container text-center">
            <h2>Utenza non e' presente sul DB!</h2>
            <h3>Digitare nuovamente la matricola</h3>
            <form action="/showUserType" method="post" style="text-transform: uppercase">
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <input class="btn btn-primary" type="submit" value="Cerca"/>
                    </div>
                    <div>
                        <input type="text" class="form-control" placeholder="Inserisci matricola"
                               aria-label="Inserisci matricola" aria-describedby="basic-addon2" name="quser">
                    </div>
                </div>
            </form>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container text-center" id="userDiv">
            <h2>Ruoli dell'utente</h2>
            <form action="/showUserType" method="post" style="text-transform: uppercase">
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <input class="btn btn-primary" type="submit" value="Cerca"/>
                    </div>
                    <div>
                        <input type="text" class="form-control" placeholder="Inserisci matricola"
                               aria-label="Inserisci matricola" aria-describedby="basic-addon2" name="quser">
                    </div>
                </div>
            </form>
            <hr>
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <thead>
                    <tr>
                        <th>
                            Tipo
                        </th>
                        <th>
                            Valore
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="quser" items="${other_data}">
                        <tr>
                            <td>${quser.type}</td>
                            <td>${quser.value}</td>
                        </tr>
                    </c:forEach>
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