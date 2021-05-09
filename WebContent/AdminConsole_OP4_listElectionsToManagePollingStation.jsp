<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
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
    <title>UC eVoting | Consola de Administração | Gerir Mesas de Voto</title>
    <!--icon-->
    <link rel="shortcut icon" href="images/favicon.ico">
</head>
<body>
<div id="container" class="container">
    <img alt="UC Logo" width="100%" id="logo"
         src="https://www.uc.pt/identidadevisual/Marcas_UC_submarcas/marcas_submarcas/UC_H_FundoClaro-negro?hires">

    <h1>Gerir Mesas de Voto</h1>
    <h4>Escolha a eleição</h4>
    <s:actionerror/>
    <s:actionmessage/>
    <s:form action="chooseElectionToManage" method="post">
        <c:forEach items="${HeyBean.allElections}" var="value">
            <button name="election_to_manage" value="${value.title}">${value.title}</button>
        </c:forEach>
    </s:form>
    <button id="exit">Voltar</button>
</div>
</body>
</html>