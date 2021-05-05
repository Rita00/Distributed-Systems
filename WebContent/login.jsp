<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <title>UC - eVoting | Sign In</title>
    <!-- encoding -->
    <meta charset="UTF-8">
    <!-- font -->
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,500;0,600;0,700;1,500;1,600;1,700&display=swap"
          rel="stylesheet">
    <!-- css -->
    <link rel="stylesheet" type="text/css" href="style/main.css">
    <link rel="stylesheet" type="text/css" href="style/form.css">
    <!--icon-->
    <link rel="shortcut icon" href="images/favicon.ico">
</head>
<body>
<div id="container" class="container">
    <img alt="UC Logo" width="100%" id="logo"
         src="https://www.uc.pt/identidadevisual/Marcas_UC_submarcas/marcas_submarcas/UC_H_FundoClaro-negro?hires">

    <h1>Sign In</h1>
    <s:form action="login" method="post" theme="simple">

        <button class="btn facebook-btn social-btn" type="button"><span><i class="fab fa-facebook-f"></i> Sign in with Facebook</span> </button>

        <h2> OR </h2>

        <label>Cartão de Cidadão</label><br>
        <s:textfield type="number" name="ccnumber" id="ccnumber" cssClass="input" placeholder="11223344" autofocus=""/><br>

        <label>Password</label><br>
        <s:password name="password" id="inputPassword" cssClass="input" placeholder="***********"/><br>

        <s:submit cssClass="button" id="login" placeholder="Sign in"/>



    </s:form>

</div>
</body>
</html>