<%--
  Created by IntelliJ IDEA.
  User: arn81
  Date: 07-04-2023
  Time: 18:35
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta about="Made by UID0931174 aka Zaki"/>
    <title>Eni Qlik Tool User Management</title>
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <%--    <link rel="stylesheet" href="css/custom.css"/>--%>
    <link rel="stylesheet" href="css/background.css"/>
    <link rel="shortcut icon" href="ico/favicon.ico"/>
    <!-- Load an icon library to show a hamburger menu (bars) on small screens -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
</head>
<body>
<div class="container">
    <div class="d-flex justify-content-center h-100">
        <div class="card">
            <div class="card-header">
                <h3>Crea la FARM</h3>
            </div>
            <div class="card-body">
                <form method="post" class="form-group">
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="description"><b>Descrizione</b></label>
                            <input class="form-control form-control-sm" type="text" name="description" id="description"
                                   placeholder="es: FARM QS 12P">
                        </div>
                        <div class="form-group col-md-6">
                            <label for="dbUser"><b>Utenza DB</b></label>
                            <input class="form-control form-control-sm" type="text" name="dbUser" id="dbUser"
                                   placeholder="utenza del DB">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="dbPassword"><b>DB Password</b></label>
                            <input class="form-control form-control-sm" type="text" name="dbPassword" id="dbPassword"
                                   placeholder="la pwd dell utenza db">
                        </div>
                        <div class="form-group col-md-6">
                            <label for="dbHost"><b>DB Host</b></label>
                            <input class="form-control form-control-sm" name="dbHost"
                                   placeholder="es: xxx.services.eni.intranet"
                                   id="dbHost">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="dbPort"><b>DB Port</b></label>
                            <input class="form-control form-control-sm" name="dbPort" id="dbPort"
                                   placeholder="porta, es: 1531"
                                   type="number">
                        </div>
                        <div class="form-group col-md-6">
                            <label for="dbSid"><b>DB SID</b></label>
                            <input class="form-control form-control-sm" name="dbSid" id="dbSid" placeholder="SID"
                            >
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="qsHost"><b>QS Host</b></label>
                            <input class="form-control form-control-sm" name="qsHost" id="qsHost"
                                   placeholder="es: XXlogXXk0n"
                            >
                        </div>
                        <div class="form-group col-md-6">
                            <label for="qsXrfKey"><b>XRF KEY</b></label>
                            <input class="form-control form-control-sm" name="qsXrfKey" id="qsXrfKey"
                                   placeholder="Chiave xrf"
                            >
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="qsPathClient"><b>QS Client.jks</b></label>
                            <input class="form-control form-control-sm " name="qsPathClient" id="qsPathClient"
                            >
                        </div>
                        <div class="form-group col-md-6">
                            <label for="qsPathRoot"><b>QS Root.jks</b></label>
                            <input class="form-control form-control-sm " name="qsPathRoot" id="qsPathRoot"
                            >
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="qsKsPassword"><b>QS JKS Password</b></label>
                            <input class="form-control form-control-sm" name="qsKsPassword" id="qsKsPassword"
                                   placeholder="La password dei keystore">
                        </div>
                        <div class="form-group col-md-6">
                            <label for="qsUserHeader"><b>QS User Header</b></label>
                            <input class="form-control form-control-sm" name="qsUserHeader" id="qsUserHeader"
                                   placeholder="es: UserDirectory=ENINET; UserId=ADMQSLAB01">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="note"><b>Note</b></label>
                            <input class="form-control form-control-sm" name="note" id="note"
                                   placeholder="eventuali note">
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
                                   placeholder="es: 6440004">
                        </div>
                        <div class="form-group col-md-6">
                            <label for="qsReloadTaskName"><b>Reload Task</b></label>
                            <input class="form-control form-control-sm" name="qsReloadTaskName" type="text"
                                   id="qsReloadTaskName"
                                   placeholder="es: UDC Sync_usersynctask">
                        </div>
                    </div>
                    <button formaction="/createFarmDB" formmethod="post" class="btn btn-success btn-sm float-left">
                        Crea
                    </button>
                    <button formaction="/logout" class="btn btn-danger float-right">Annulla</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
