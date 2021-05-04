<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="login.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
    <title>UC - eVoting | Sign In</title>
</head>
<body>

<div id="logreg-forms" style="position: absolute; left: 50%; top: 50%; -webkit-transform: translate(-50%, -50%); transform: translate(-50%, -50%);">
    <s:form action="login" method="post" theme="simple" cssClass="form-signin">
        <h1 class="h3 mb-3 font-weight-normal" style="text-align: center"> Sign in</h1>
        <div class="social-login" style="text-align: center">
            <button class="btn facebook-btn social-btn" type="button"><span><i class="fab fa-facebook-f"></i> Sign in with Facebook</span> </button>
        </div>
        <p style="text-align:center"> OR  </p>
<%--        <s:text name = "CC Number" />--%>
<%--        <s:textfield name="cc number" class="form-control"/>--%>

<%--        <s:text name = "Password"/>--%>
<%--        <s:textfield name="password" class="form-control"/>--%>
<%--        --%>
        <s:textfield type="number" name="ccnumber" id="ccnumber" class="form-control" placeholder="CC Number" autofocus=""/>

        <s:password name="password" id="inputPassword" class="form-control" placeholder="Password"/>

        <s:submit cssClass="btn btn-success btn-block" placeholder="Sign in"/>
    </s:form>
    <br>
</div>
</body>
</html>