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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="style/main.css">
    <link rel="stylesheet" type="text/css" href="style/form.css">
    <!--icon-->
    <link rel="shortcut icon" href="images/favicon.ico">

</head>
<body>
<div id="container" class="container">
    <img alt="UC Logo" width="100%" id="logo"
         src="https://www.uc.pt/identidadevisual/Marcas_UC_submarcas/marcas_submarcas/UC_H_FundoClaro-negro?hires">
    <div class="row">
        <div class="col-md-3">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h1 class="panel-title">
                        <span class="glyphicon glyphicon-hand-right"></span>Formulário de Voto
                        <p style="color: black"> Escolha a lista que deseja votar!</p>
                    </h1>
                </div>
                <s:actionerror/>
                <s:form action="vote" method="post">
                    <div class="panel-body">
                        <c:forEach items="${HeyBean.candidacies}" var="value">
                            <div class="radio" style="border-radius:5px;background-color: #004E64; float:left; width: 90%; margin-bottom: 10px; margin-left: 25px">
                                <label style="width: 100%; margin-left: 1px">
                                    <input type="radio" name="candidacy_id" value="${value.id}">
                                    <c:out value="${value.name}"/>
                                </label>
                            </div>
                        </c:forEach>
                        <div class="radio" style="border-radius:5px;background-color: #004E64; float:left; width: 90%; margin-bottom: 10px; margin-left: 25px">
                            <label style="width: 100%; margin-left: 1px">
                                <input type="radio" name="candidacy_id" value="-1">
                                <c:out value="Voto Nulo"/>
                            </label>
                        </div>
                        <div class="radio" style="border-radius:5px;background-color: #004E64; float:left; width: 90%; margin-bottom: 10px; margin-left: 25px">
                            <label style="width: 100%; margin-left: 1px">
                                <input type="radio" name="candidacy_id" value="-2">
                                <c:out value="Voto em branco"/>
                            </label>
                        </div>
                    </div>
                    <div class="panel-footer text-center">
                        <s:submit cssClass="button" cssStyle="margin:5% 20%; width:60%" value="Votar!"/>
                    </div>
                </s:form>
            </div>
        </div>
    </div>

    <div class="fb-share-button" data-href="https://www.telepizza.pt/" data-layout="button" data-size="small"><a target="_blank" href="https://www.facebook.com/sharer/sharer.php?u=https://www.telepizza.pt/;src=sdkpreparse" class="fb-xfbml-parse-ignore">Partilhar no facebook!</a></div>

    <%--<script>
        document.getElementById('shareBtn').onclick = function() {
            FB.ui({
                display: 'popup',
                method: 'share',
                href: 'http://localhost:8080/webserver/login.action',
            }, function(response){});
        }
    </script>--%>

    <c:if test="${HeyBean.associatedFbId == null}">
        <s:form action="associateFacebook" method="post">
            <s:submit cssClass="button" cssStyle="margin:5% 20%; width:60%" value="Associar Facebook!"/>
        </s:form>
    </c:if>

    <s:form action="" method="post">
        <button id="exit">Sign Out</button>
    </s:form>
</div>
</body>
</html>
