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
    <link rel="stylesheet" type="text/css" href="style/form.css">
    <!-- title -->
    <title>UC eVoting | Consola de Administração | Gerir Eleição</title>
    <!--icon-->
    <link rel="shortcut icon" href="images/favicon.ico">
</head>
<body>
<div id="container" class="container">
    <img alt="UC Logo" width="100%" id="logo"
         src="https://www.uc.pt/identidadevisual/Marcas_UC_submarcas/marcas_submarcas/UC_H_FundoClaro-negro?hires">

    <h1>${HeyBean.title}</h1>

    <s:form action="" method="post">
        <label>Nome
            <s:textfield cssClass="input" name="title" placeholder="${HeyBean.title}"/>
        </label>
        <label>Tipo<br>
            <select class="input" name="type" id="job">
                <!-- TODO SELECTED-->
<%--                <option selected="selected">${HeyBean.type}</option>--%>
                <option hidden>${HeyBean.type}</option>
                <option value="Estudante">Estudante</option>
                <option value="Docente">Docente</option>
                <option value="Funcionário">Funcionário</option>
            </select><br>
        </label>
        <label>Descrição<br>
            <s:textarea name="description" cssClass="input" placeholder="${HeyBean.description}"/>
        </label><br>
        <label>Data de início
            <s:textfield name="iniDate" type="text" onfocus="(this.type='datetime-local')" cssClass="input"
                         placeholder="${HeyBean.iniDate}"/>
        </label>
        <label>Data de fim
            <s:textfield name="fimDate" type="text" onfocus="(this.type='datetime-local')" cssClass="input"
                         placeholder="${HeyBean.fimDate}"/>
        </label>
        <div class="row">
            <s:submit cssClass="button" id="exit" value="Voltar"/>
            <s:submit cssClass="button" value="Guardar"/>
        </div>
    </s:form>
</div>
</body>


</html>