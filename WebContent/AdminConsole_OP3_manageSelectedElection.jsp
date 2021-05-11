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
    <title>UC eVoting | Consola de Administração | Gerir Eleição</title>
    <!--icon-->
    <link rel="shortcut icon" href="images/favicon.ico">
</head>
<body>
<div id="container" class="container">
    <img alt="UC Logo" width="100%" id="logo"
         src="https://www.uc.pt/identidadevisual/Marcas_UC_submarcas/marcas_submarcas/UC_H_FundoClaro-negro?hires">
    <s:actionerror/>
    <s:actionmessage/>
    <h1>${HeyBean.title}</h1>
    <p style="color: black">${HeyBean.type}</p>
    <p style="color: black">${HeyBean.description}</p>
    <p style="color: black">${HeyBean.iniDate} até ${HeyBean.fimDate}</p>
    <br>
    <c:forEach items="${HeyBean.candidacies}" var="value">
        <s:form action="seeDetailsList" method="post">
            <div style="width: 50%; margin-left: 200px ">
                <button name="candidacy_id" value="${value.id}">${value.name}</button>
            </div>
        </s:form>
    </c:forEach>
    <s:form action="editSelectedElectionConsole" method="post">
        <button>Editar</button>
    </s:form>
    <s:form action="addListToElectionConsole" method="post">
        <button>Adicionar Lista</button>
    </s:form>
    <s:form action="backOnManageSelectedElectionConsole" method="post">
        <button id="exit">Voltar</button>
    </s:form>

</div>
</body>
</html>