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

    <h1>${HeyBean.title}</h1>
    <!-- FOR EACH ELECTION -->
    <c:forEach items="${HeyBean.candidaciesWithVotes}" var="value">
        <p style="font-size: 15px ; color: black">${value.name}: ${value.votes}</p>
    </c:forEach>

    <p style="font-size: 15px ; color: black">Votos Nulos: ${HeyBean.null_votes}  (${HeyBean.null_percent}%)</p>
    <p style="font-size: 15px ; color: black">Votos em branco: ${HeyBean.blank_votes}  (${HeyBean.blank_percent}%)</p>

    <s:form action="backResultsElection" method="post">
        <button id="exit">Voltar</button>
    </s:form>

</div>
</body>
</html>