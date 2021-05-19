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
    <s:actionmessage/>
    <h1>Consola de Administração</h1>
    <s:form action="registerConsole" method="post">
            <button title="Registar uma pessoa nova na base de dados">Registar Pessoas</button>
    </s:form>
    <s:form action="createElectionConsole" method="post">
            <button title="Registar uma eleição nova na base de dados">Criar Eleição</button>
    </s:form>
    <s:form action="manageElection" method="post">
            <button title="Editar detalhes de uma eleição e adicionar listas">Gerir Eleição</button>
    </s:form>
    <s:form action="managePollingStation" method="post">
            <button title="Adicionar ou remover mesas de voto para uma eleição">Gerir Mesas de Voto</button>
    </s:form>
    <s:form action="localVotes" method="post">
            <button title="Consultar o local em que cada eleitor votou">Registo de Votos</button>
    </s:form>
    <s:form action="electionsResults" method="post">
            <button title="Consultar resultados detalhados de eleições passadas">Resultados das Eleições</button>
    </s:form>
    <s:form action="statusPollingStations" method="post">
            <button title="Consultar estado das mesas de voto e respetivos terminais de voto">Estado das Mesas de Voto</button>
    </s:form>
    <s:form action="votesCount" method="post">
            <button title="Consultar contagem de votos em tempo real">Contagem dos Votos</button>
    </s:form>
    <s:form action="onlineUsers" method="post">
        <button title="Consultar os utilizadores online">Utilizadores Online</button>
    </s:form>
    <s:form action="backMenuConsole" method="post">
        <button id="exit" title="Sair da consola de administração">Sair</button>
    </s:form>
</div>

</body>
</html>