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
    <link rel="stylesheet" type="text/css" href="style/form.css">
    <!-- title -->
    <title>UC eVoting | Consola de Administração | Registo de Pessoas</title>
    <!--icon-->
    <link rel="shortcut icon" href="images/favicon.ico">
</head>
<body>
<div id="container" class="container">
    <img alt="UC Logo" width="100%" id="logo"
         src="https://www.uc.pt/identidadevisual/Marcas_UC_submarcas/marcas_submarcas/UC_H_FundoClaro-negro?hires">

    <h1>Registo de Pessoas</h1>
    <s:form action="register" method="post">
        <label>Nome
            <input class="input" type="text" placeholder="Alberto Caeiro"/>
        </label>
        <label>Cargo<br>
            <select class="input" name="job" id="job">
                <option value="Estudante">Estudante</option>
                <option value="Docente">Docente</option>
                <option value="Funcionário">Funcionário</option>
            </select><br>
        </label>
        <label>Password
            <input class="input" type="password" placeholder="**********"/>
        </label>
        <label>Departamento<br>
            <select class="input" name="department" id="department">
                <c:forEach items="${HeyBean.departments}" var="value">
                    <%--                        <c:out value="${value.name}"/>--%>
                    <option value="${value.id}">${value.name}</option>

                </c:forEach>
            </select><br>
                <%--                <select class="input" name="department" id="department">--%>
                <%--                    <option value="TODO">TODO</option>--%>
                <%--                </select><br>--%>
        </label>
        <label>Telemóvel
            <input class="input" type="text" placeholder="93XXXXXXX"/>
        </label>
        <label>Morada
            <input class="input" type="text" placeholder="Avenida Inês de Castro 69, 3040-390 Coimbra"/>
        </label>
        <label>Cartão de Cidadão
            <input class="input" type="text" placeholder="11223344"/>
        </label>
        <label>Validade
            <input class="input" type="date" placeholder="2025-10-01"/>
        </label>
        <div class="row">
            <input class="button" type="submit" id="exit" value="Voltar"/>
            <input class="button" type="submit" id="register" value="Registar"/>
        </div>
    </s:form>

</div>
</body>
</html>