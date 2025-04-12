<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Created by IntelliJ IDEA.
  User: UID0931174
  Date: 18/10/2019
  Time: 11:31
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
                <h3>Seleziona la FARM</h3>
            </div>
            <div class="card-body">
                <form class="form-group">
                    <c:choose>
                        <c:when test="${empty farmList}">
                            <span style="color:whitesmoke">Nessuna farm censita!</span>
                            <a href="${pageContext.request.contextPath}/">Login</a>
                        </c:when>
                        <c:otherwise>
                            <%--suppress HtmlFormInputWithoutLabel --%>
                            <select name="farm" class="form-control">
                                <c:forEach items="${farmList}" var="farm">
                                    <option value="${farm.description}">${farm.description}</option>
                                </c:forEach>
                            </select>
                        </c:otherwise>
                    </c:choose>
                    <br>
                    <div>
                        <button formaction="/selectFarm" formmethod="get" class="btn float-right login_btn">Scegli
                        </button>
                        <c:choose>
                            <c:when test="${empty farmList}">
                                <button formaction="/createFarm" class="btn float-left login_btn">Crea</button>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>