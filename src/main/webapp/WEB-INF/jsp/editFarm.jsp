<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: UID0931174
  Date: 29/10/2019
  Time: 13:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html>
<jsp:include page="header.jsp"/>
<body>
<jsp:include page="navbar.jsp"/>
<c:choose>
    <c:when test="${empty farm}">
        <div class="container text-center">
            <h2>Nessun record e` presente</h2>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container text-center">
            <h2>Modifica FARM</h2>
            <hr>
            <form method="post" action="${pageContext.request.contextPath}/saveFarm">
                <div class="row">
                    <div class="form-group col-md-6">
                        <label for="farmId"><b>Id</b></label>
                        <input type="text" name="farmId" id="farmId" class="form-control form-control-sm"
                               value="${farm.farmid}" readonly required/>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="description"><b>Descrizione</b></label>
                        <input type="text" name="description" id="description" value="${farm.description}"
                               class="form-control form-control-sm" required/>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="environment"><b>Ambiente</b></label>
                        <select name="environment" id="environment" class="form-control form-control-sm" required>
                            <option>DEV</option>
                            <option>TEST</option>
                            <option>PRE</option>
                            <option>PROD</option>
                        </select>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbUser"><b>DB User</b></label>
                        <input type="text" name="dbUser" id="dbUser" value="${farm.dbuser}"
                               class="form-control form-control-sm" required/>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbPassword"><b>DB Pwd</b></label>
                        <input type="password" name="dbPassword" id="dbPassword" value="${farm.dbpassword}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbHost"><b>DB Host</b></label>
                        <input type="text" name="dbHost" id="dbHost" value="${farm.dbhost}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbPort"><b>DB Port</b></label>
                        <input type="text" name="dbPort" id="dbPort" value="${farm.dbpassword}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="dbSid"><b>DB SID</b></label>
                        <input type="text" name="dbSid" id="dbSid" value="${farm.dbsid}"
                               class="form-control form-control-sm" required>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-6">
                        <label for="qsHost"><b>QS Host</b></label>
                        <input type="text" name="qsHost" id="qsHost" value="${farm.qshost}}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsReloadTaskName"><b>QS RTN</b></label>
                        <input type="text" name="qsReloadTaskName" id="qsReloadTaskName"
                               value="${farm.qsreloadtaskname}" class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsPathClientJKS"><b>QS ClientJKS</b></label>
                        <input type="text" name="qsPathClientJKS" id="qsPathClientJKS"
                               value="${farm.qspathclient}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsPathRootJKS"><b>QS RootJKS</b></label>
                        <input type="text" name="qsPathRootJKS" id="qsPathRootJKS" value="${farm.qspathroot}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsXrfKey"><b>QS XrfKey</b></label>
                        <input type="text" name="qsXrfKey" id="qsXrfKey" value="${farm.qsxrfkey}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsKeyStorePwd"><b>QS KsPwd</b></label>
                        <input type="text" name="qsKeyStorePwd" id="qsKeyStorePwd" value="${farm.qskspasswd}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="qsHeader"><b>QS Header</b></label>
                        <input type="text" name="qsHeader" id="qsHeader" value="${farm.qsuserheader}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="note"><b>Note</b></label>
                        <input type="text" name="note" id="note" value="${farm.note}"
                               class="form-control form-control-sm" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="came"><b>CAME</b></label>
                        <input type="text" name="came" id="came" value="${farm.came}"
                               class="form-control form-control-sm" required>
                    </div>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-success btn-sm">Salva</button>
                </div>
            </form>
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
