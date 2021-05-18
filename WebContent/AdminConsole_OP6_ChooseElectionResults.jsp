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
    <title>UC eVoting | Consola de Administração | Resultados das Eleições</title>
    <!--icon-->
    <link rel="shortcut icon" href="images/favicon.ico">
</head>
<body>
<div id="container" class="container">
    <img alt="UC Logo" width="100%" id="logo"
         src="https://www.uc.pt/identidadevisual/Marcas_UC_submarcas/marcas_submarcas/UC_H_FundoClaro-negro?hires">

    <h1>Resultados das Eleições</h1>
    <c:forEach items="${HeyBean.endedElections}" var="value">
        <s:form action="chooseElectionToSeeDetails" method="post">
            <button name="election_id" value="${value.id}">${value.title}</button>
            <input type="hidden" name="election_title" value="${value.title}">
            <input type="hidden" name="blank_votes" value="${value.blank_votes}">
            <input type="hidden" name="null_votes" value="${value.null_votes}">
            <input type="hidden" name="null_percent" value="${value.null_percent}">
            <input type="hidden" name="blank_percent" value="${value.blank_percent}">
        </s:form>
    </c:forEach>

    <button id="exit">Voltar</button>
</div>
</body>
</html>