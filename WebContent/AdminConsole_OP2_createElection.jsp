<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <!-- title -->
    <title>UC eVoting | Consola de Administração | Criar Eleição</title>
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

    <h1>Criar Eleição</h1>
    <s:actionerror/>
    <s:form action="createElection" method="post">
        <label>Titulo
            <s:textfield name="title" cssClass="input" placeholder="Eleições NEI/AAC 2020/2021"/>
        </label>
        <label>Descrição
            <s:textarea name="description" cssClass="input" placeholder="O NEI/AAC (Núcleo de Estudantes de Informática da Associação
            Académica de Coimbra) é um órgão integrante da AAC que tem o propósito de representar os estudantes de
            Engenharia Informática e Design e Multimédia da Universidade de Coimbra, sócios da AAC."/>
        </label>
        <label>Tipo<br>
            <select class="input" name="type" id="job">
                <option value="Estudante">Estudante</option>
                <option value="Docente">Docente</option>
                <option value="Funcionário">Funcionário</option>
            </select><br>
        </label>
        <label>Data de Início<br>
            <s:textfield name="iniDate" type="datetime-local" cssClass="input"/>
                <%--            <input class="input" type="date" placeholder="2025-10-01 11:59"/>--%>
        </label>
        <label>Data de fim
            <s:textfield name="fimDate" type="datetime-local" cssClass="input"/>
        </label>
        <label>Restringir Eleição?<br>
            <select class="input" name="restriction" id="restriction">
                <option value="yes">Sim</option>
                <option value="no">Não</option>
            </select><br>
        </label>
        <%--IF RESTRICAO --%>
        <label>Departamento<br>
            <select class="input" name="dep" id="department">
                <c:forEach items="${HeyBean.departments}" var="value">
                    <option value="${value.id}">${value.name}</option>
                </c:forEach>
            </select><br>
        </label>
        <%--END IF RESTRICAO --%>
        <div class="row">
            <input class="button" type="submit" id="exit" value="Voltar"/>
            <input class="button" type="submit" id="create" value="Criar"/>
        </div>
    </s:form>
</div>
</body>
</html>