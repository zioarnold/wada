<%--
  Created by IntelliJ IDEA.
  User: UID0931174
  Date: 21/10/2019
  Time: 16:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="header.jsp"/>
<body>
<div class="container">
    <div class="d-flex justify-content-center h-100">
        <div class="card">
            <div class="card-header">
                <h3>Logout</h3>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/" method="get">
                    <h5><span style="color:whitesmoke">Logout e` avvenuto con successo</span></h5>
                    <div class="form-group">
                        <input type="submit" value="Ok" class="btn float-right login_btn">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
