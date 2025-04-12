<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<jsp:include page="header.jsp"/>
<body>
<jsp:include page="navbar.jsp"/>
<br/>
<div class="container">
    <div class="input-group mb-3">
        <form action="${pageContext.request.contextPath}/searchQUserOnDB" method="post"
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
<c:choose>
    <c:when test="${empty quser_filter}">
        <div class="container text-center">
            <h2>Nessun utenza digitata oppure utenza non è presente sul DB</h2>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container text-center" id="userDiv">
            <h2 class="text-center">Utenza sul DB</h2>
            <hr>
            <div class="table-responsive">
                <form>
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
                                Altri dati
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="quser" items="${quser_filter}">
                            <tr>
                                <td>${quser.userId}</td>
                                <td>${quser.name}</td>
                                <td>${quser.userIsActive}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/managementPageShowUserData?quser=${quser.userId}"><span
                                        class="">Mostrami altri dati</span> </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${empty other_data}">
    </c:when>
    <c:otherwise>
        <div class="container text-center" id="userDiv">
            <h2>Ruoli dell'utente</h2>
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