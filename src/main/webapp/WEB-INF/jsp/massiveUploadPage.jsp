<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta about="Made by UID0931174 aka Zaki"/>
    <title>Eni Qlik Tool User Management</title>
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <link rel="stylesheet" href="css/background.css"/>
    <link rel="shortcut icon" href="ico/favicon.ico"/>
    <!-- Load an icon library to show a hamburger menu (bars) on small screens -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.1/css/fileinput.min.css" media="all"
          rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" crossorigin="anonymous">
</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container text-center">
    <h5>Verra' eseguito un check verso LDAP per poi effettuare dei controlli sul DB.</h5>
    <br>
    <h5>Qualora utenza esista sull'ADLDS, tale utenza verra' censita come abilitata.</h5>
    <br>
    <h5>Per gestire un'utenza singolarmente, <a href="${pageContext.request.contextPath}/searchQUserOnDBPage">Clicca
        qui</a></h5>
    <br>
    <h4>Il formato deve essere il seguente: USERID;RUOLO;GRUPPO</h4>
    <form action="${pageContext.request.contextPath}/massiveUpload" method="post" enctype="multipart/form-data"
          id="uploadForm">
        <input id="input-b2" name="file" type="file" data-show-preview="false">
    </form>
    <%--    <div id='loadingmessage' style='display:none'>--%>
    <%--        <img src='gif/ajax-loader.gif' alt="Caricamento in corso..."/>--%>
    <%--    </div>--%>
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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="javascript" src="js/bootstrap.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.1/js/plugins/piexif.min.js"
        type="text/javascript"></script>
<!-- sortable.min.js is only needed if you wish to sort / rearrange files in initial preview.
    This must be loaded before fileinput.min.js -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.1/js/plugins/sortable.min.js"
        type="text/javascript"></script>
<!-- purify.min.js is only needed if you wish to purify HTML content in your preview for
    HTML files. This must be loaded before fileinput.min.js -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.1/js/plugins/purify.min.js"
        type="text/javascript"></script>
<script src="js/fileinput.js"></script>
<script src="js/locales/it.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.1/themes/fas/theme.min.js"></script>
<script>
    // function clickMe() {
    //     $(document).ready(function () {
    //         setInterval(function () {
    //             /* query the completion percentage from the server */
    //             $.get("/massiveUpload", function () {
    //                 $('#loadingmessage').show();
    //             });
    //         }, 1000);
    //     });
    // }
</script>
<script>
    $("#input-b2").fileinput({
        theme: "fas",
        maxFileCount: 1,
        showPreview: false,
        allowedFileExtensions: ["csv"],
        language: "it"
    });
</script>
</body>
</html>