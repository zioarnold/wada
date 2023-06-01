<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
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
<div class="container text-center" id="usersDiv">
    <h2>Utenze sul DB</h2>
    <hr>
    <div class="table-responsive">
        <table class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>
                    displayName
                </th>
                <th>
                    eniMatricolaNotes
                </th>
                <th>
                    name
                </th>
                <th>
                    mail
                </th>
                <th>
                    givenName
                </th>
                <th>
                    sn
                </th>
                <th>
                    badPwdCount
                </th>
                <th>
                    pwdLastSet
                </th>
                <th>
                    userAccountDisabled
                </th>
                <th>
                    userDontExpirePassword
                </th>
                <th>
                    memberOf
                </th>
                <th>
                    ou
                </th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="ldapuser" items="${ldapusers}">
                <tr>
                    <td>${ldapuser.displayName}</td>
                    <td>${ldapuser.eniMatricolaNotes}</td>
                    <td>${ldapuser.name}</td>
                    <td>${ldapuser.mail}</td>
                    <td>${ldapuser.givenName}</td>
                    <td>${ldapuser.sn}</td>
                    <td>${ldapuser.badPwdCount}</td>
                    <td>${ldapuser.pwdLastSet}</td>
                    <td>${ldapuser.userAccountDisabled}</td>
                    <td>${ldapuser.userDontExpirePassword}</td>
                    <td>${ldapuser.memberOf}</td>
                    <td>${ldapuser.ou}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
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