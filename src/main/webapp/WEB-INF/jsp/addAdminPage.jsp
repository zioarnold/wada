<%--
  Created by IntelliJ IDEA.
  User: arn81
  Date: 18-Oct-19
  Time: 23:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="it">
<jsp:include page="header.jsp"/>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container text-center">
    <h2>Aggiungi nuovo admin/moder</h2>
    <form action="${pageContext.request.contextPath}/addAdmin" method="post">
        <div class="form-group">
            <label for="username" class="control-label">Username</label>
            <input class="form-control" type="text" name="username" id="username" required>
        </div>
        <div class="form-group">
            <label for="password" class="control-label">Password</label>
            <input class="form-control" type="text" name="password" id="password" required>
        </div>
        <div class="form-group">
            <label for="role" class="control-label">Ruolo</label>
            <select name="role" id="role" class="form-control">
                <option>MODER</option>
                <option>ADMIN</option>
            </select>
        </div>
        <input type="submit" class="btn btn-success" value="Aggiungi">
    </form>
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
</body>
</html>