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
    <title>UC eVoting | Consola de Administração | Criar Eleição</title>
    <!--icon-->
    <link rel="shortcut icon" href="images/favicon.ico">
</head>
<body>
<div id="container" class="container">
    <img alt="UC Logo" width="100%" id="logo"
         src="https://www.uc.pt/identidadevisual/Marcas_UC_submarcas/marcas_submarcas/UC_H_FundoClaro-negro?hires">

    <h1>Criar Eleição</h1>
    <form>
        <label>Titulo
            <input class="input" type="text" placeholder="Eleições NEI/AAC 2020/2021"/>
        </label>
        <label>Descrição
            <textarea class="input" type="text"
                      placeholder="O NEI/AAC (Núcleo de Estudantes de Informática da Associação Académica de Coimbra) é um órgão integrante da AAC que tem o propósito de representar os estudantes de Engenharia Informática e Design e Multimédia da Universidade de Coimbra, sócios da AAC."></textarea>
        </label>
        <label>Tipo<br>
            <select class="input" name="job" id="job">
                <option value="Estudante">Estudante</option>
                <option value="Docente">Docente</option>
                <option value="Funcionário">Funcionário</option>
            </select><br>
        </label>
        <label>Data de Início<br>
            <input class="input" type="date" placeholder="2025-10-01 11:59"/>
        </label>
        <label>Data de fim
            <input class="input" type="date" placeholder="2025-11-01 23:59"/>
        </label>
        <label>Restringir Eleição?<br>
            <select class="input" name="restriction" id="restriction">
                <option value="yes">Sim</option>
                <option value="yes">Não</option>
            </select><br>
        </label>
        <!--IF RESTRICAO -->
        <label>Departamento<br>
            <select class="input" name="department" id="department">
                <option value="TODO">TODO</option>
            </select><br>
        </label>
        <!--END IF RESTRICAO -->
        <div class="row">
            <input class="button" type="submit" id="exit" value="Voltar"/>
            <input class="button" type="submit" id="create" value="Criar"/>
        </div>
    </form>

</div>
</body>
</html>