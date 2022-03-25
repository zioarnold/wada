<%--
  Created by IntelliJ IDEA.
  User: arn81
  Date: 18-Oct-19
  Time: 23:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta about="Made by UID0931174 aka Zaki"/>
    <title>Eni Qlik Tool User Management</title>
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <link rel="stylesheet" href="css/custom.css"/>
    <link rel="shortcut icon" href="ico/favicon.ico"/>
    <!-- Load an icon library to show a hamburger menu (bars) on small screens -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
</head>
<body>
<div class="container">
    <div class="d-flex justify-content-center h-100">
        <div class="card">
            <div class="card-header">
                <h3>Sessione</h3>
            </div>
            <div class="card-body">
                <h2 style="color:whitesmoke;">Sessione scaduta</h2>
                <div class="btn-toolbar" style="margin-top: 10px;">
                    <%--                    <form action="/renewSession" method="post">--%>
                    <%--                        <input type="submit" value="Rinnova" class="btn float-left login_btn" style="margin-right: 50px;">--%>
                    <%--                    </form>--%>
                    <form action="/" method="post">
                        <input type="submit" value="Login" class="btn float-right login_btn" style="margin-left: 50px;">
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
