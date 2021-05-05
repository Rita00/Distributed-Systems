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
    <title>UC eVoting | Consola de Administração</title>
    <!--icon-->
    <link rel="shortcut icon" href="images/favicon.ico">
</head>
<body>
<div id="container" class="container">
    <img alt="UC Logo" width="100%" id="logo"
         src="https://www.uc.pt/identidadevisual/Marcas_UC_submarcas/marcas_submarcas/UC_H_FundoClaro-negro?hires">

    <h1>Consola de Administração</h1>
    <s:form action="register" method="post">
            <button>Registar Pessoas</button>
    </s:form>
    <s:form action="createElection" method="post">
            <button>Criar Eleição</button>
    </s:form>
    <s:form action="manageElection" method="post">
            <button>Gerir Eleição</button>
    </s:form>
    <s:form action="managePollingStation" method="post">
            <button>Gerir Mesas de Voto</button>
    </s:form>
    <s:form action="localVotes" method="post">
            <button>Local em que cada eleitor votou</button>
    </s:form>
    <s:form action="electionsResults" method="post">
            <button>Consultar resultados detalhados de eleições passadas</button>
    </s:form>
    <s:form action="statusPollingStations" method="post">
            <button>Consultar estado das mesas de voto e respetivos terminais de voto</button>
    </s:form>
    <s:form action="countVotes" method="post">
            <button>Consultar contagem de votos em tempo real</button>
    </s:form>
    <s:form action="backMenuConsole" method="post">
        <button id="exit">Sair</button>
    </s:form>
</div>

</body>
</html>