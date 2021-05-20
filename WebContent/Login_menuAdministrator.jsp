<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <!-- encoding -->
    <meta charset="UTF-8">
    <!-- font -->
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,500;0,600;0,700;1,500;1,600;1,700&display=swap"
          rel="stylesheet">
    <!-- css -->
    <link rel="stylesheet" type="text/css" href="style/main.css">
    <link rel="stylesheet" type="text/css" href="style/menu.css">
    <!-- title -->
    <title>UC eVoting | Menu da Administração</title>
    <!--icon-->
    <link rel="shortcut icon" href="images/favicon.ico">
</head>
<body>
<div id="container" class="container">
    <img alt="UC Logo" width="100%" id="logo"
         src="https://www.uc.pt/identidadevisual/Marcas_UC_submarcas/marcas_submarcas/UC_H_FundoClaro-negro?hires">
    <s:actionmessage/>
    <h1>Menu da Administração</h1>
    <s:form action="adminVote" method="post">
            <button>Votar</button>
    </s:form>
    <s:form action="adminConsole" method="post">
            <button>Consola de Administração</button>
    </s:form>

    <s:form action="" method="post">
        <button id="exit">Sign Out</button>
    </s:form>

    <c:if test="${HeyBean.associatedFbId == null}">
        <s:form action="associateFacebook" method="post">
            <s:submit cssClass="button" cssStyle="margin:5% 20%; width:60%" value="Associar Facebook!"/>
        </s:form>
    </c:if>
</div>

</body>
</html>