<%--
  Created by IntelliJ IDEA.
  User: UID0931174
  Date: 24/10/2019
  Time: 11:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge"/>
    <meta about="Made by UID0931174 aka Zaki"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
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
<div class="container text-center">
    <h2>Aggiunta FARM</h2>
    <hr>
    <form action="/addNewFarm" method="post" enctype="multipart/form-data">
        <div class="row">
            <div class="form-group col-md-6">
                <label for="description"><b>Descrizione</b></label>
                <input class="form-control form-control-sm" type="text" name="description" id="description"
                       placeholder="es: FARM QS 12P" required>
            </div>
            <div class="form-group col-md-6">
                <label for="dbUser"><b>Utenza DB</b></label>
                <input class="form-control form-control-sm" type="text" name="dbUser" id="dbUser"
                       placeholder="utenza del DB" required>
            </div>
            <div class="form-group col-md-6">
                <label for="dbPassword"><b>DB Password</b></label>
                <input class="form-control form-control-sm" type="text" name="dbPassword" id="dbPassword"
                       placeholder="la pwd dell utenza db" required>
            </div>
            <div class="form-group col-md-6">
                <label for="dbHost"><b>DB Host</b></label>
                <input class="form-control form-control-sm" name="dbHost" placeholder="es: xxx.services.eni.intranet"
                       id="dbHost" required>
            </div>
            <div class="form-group col-md-6">
                <label for="dbPort"><b>DB Port</b></label>
                <input class="form-control form-control-sm" name="dbPort" id="dbPort" placeholder="porta, es: 1531"
                       type="number" required>
            </div>
            <div class="form-group col-md-6">
                <label for="dbSid"><b>DB SID</b></label>
                <input class="form-control form-control-sm" name="dbSid" id="dbSid" placeholder="SID" required>
            </div>
        </div>
        <div class="row">
            <div class="form-group col-md-6">
                <label for="qsHost"><b>QS Host</b></label>
                <input class="form-control form-control-sm" name="qsHost" id="qsHost" placeholder="es: XXlogXXk0n"
                       required>
            </div>
            <div class="form-group col-md-6">
                <label for="qsXrfKey"><b>XRF KEY</b></label>
                <input class="form-control form-control-sm" name="qsXrfKey" id="qsXrfKey" placeholder="Chiave xrf"
                       required>
            </div>
            <div class="form-group col-md-6">
                <label for="qsPathClient"><b>QS Client.jks</b></label>
                <input class="form-control form-control-sm " name="qsPathClient" id="qsPathClient"
                       required>
            </div>
            <div class="form-group col-md-6">
                <label for="qsPathRoot"><b>QS Root.jks</b></label>
                <input class="form-control form-control-sm " name="qsPathRoot" id="qsPathRoot"
                       required>
            </div>
            <div class="form-group col-md-6">
                <label for="qsKsPassword"><b>QS JKS Password</b></label>
                <input class="form-control form-control-sm" name="qsKsPassword" id="qsKsPassword"
                       placeholder="La password dei keystore" required>
            </div>
            <div class="form-group col-md-6">
                <label for="qsUserHeader"><b>QS User Header</b></label>
                <input class="form-control form-control-sm" name="qsUserHeader" id="qsUserHeader"
                       placeholder="es: UserDirectory=ENINET; UserId=ADMQSLAB01" required>
            </div>
            <div class="form-group col-md-6">
                <label for="note"><b>Note</b></label>
                <input class="form-control form-control-sm" name="note" id="note" placeholder="eventuali note">
            </div>
            <div class="form-group col-md-6">
                <label for="environment"><b>Environment</b></label>
                <select name="environment" class="form-control form-control-sm" id="environment">
                    <option>DEV</option>
                    <option>TEST</option>
                    <option>PRE</option>
                    <option>PROD</option>
                </select>
            </div>
            <div class="form-group col-md-6">
                <label for="came"><b>CAME</b></label>
                <input class="form-control form-control-sm" name="came" type="number" id="came"
                       placeholder="es: 6440004" required>
            </div>
            <div class="form-group col-md-6">
                <label for="qsReloadTaskName"><b>Reload Task</b></label>
                <input class="form-control form-control-sm" name="qsReloadTaskName" type="text" id="qsReloadTaskName"
                       placeholder="es: UDC Sync_usersynctask" required>
            </div>
        </div>
        <input type="submit" value="Aggiungi" class="btn btn-success btn-sm">
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
<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js"
        integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb"
        crossorigin="anonymous"></script>
<script>
    $('#qsPathClient').on('change', function () { //get the file name
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });
</script>
<script>
    $('#qsPathRoot').on('change', function () {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });
</script>
</body>
</html>