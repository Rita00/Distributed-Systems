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
    <title>UC eVoting | Consola de Administração | Registo de Votos</title>
    <!--icon-->
    <link rel="shortcut icon" href="images/favicon.ico">
</head>
<body>
<div id="container" class="container">
    <img alt="UC Logo" width="100%" id="logo"
         src="https://www.uc.pt/identidadevisual/Marcas_UC_submarcas/marcas_submarcas/UC_H_FundoClaro-negro?hires">

    <h1>Registo de Votos</h1>
<%--    {% if ${HeyBean.allVotingRecords} == null %}--%>
    <!-- IF NAO TEM REGISTO DE VOTOS -->
    <h4>Não tem Registo de Votos</h4>
<%--    {% else %}--%>
    <c:forEach items="${HeyBean.allVotingRecords}" var="value">
        <h4>${value.title}, ${value.p_name}, ${value.d_name}, ${value.vote_date}</h4>
    </c:forEach>
    <!-- ELSE -->
<%--    <h4>vr.getElection_title(), vr.getPerson_name(), vr.getDepartment_name(), vr.getVote_date()</h4>--%>
    <!-- END IF-->
    <button id="exit">Voltar</button>
</div>
</body>
</html>