<%--
  Created by IntelliJ IDEA.
  User: UID0931174
  Date: 18/10/2019
  Time: 11:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<jsp:include page="header.jsp"/>
<body>
<div class="container">
    <div class="d-flex justify-content-center h-100">
        <div class="card">
            <div class="card-header">
                <h3>Errore</h3>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/" method="post">
                    <h5><span style="color:whitesmoke">${errorMsg}</span></h5>
                    <div class="form-group">
                        <input type="submit" value="Login" class="btn float-right login_btn">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
