<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <link href="//netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="vote.css">
</head>
<body>
<div style="position: absolute; left: 50%; top: 50%; -webkit-transform: translate(-50%, -50%); transform: translate(-50%, -50%)" >
    <div class="container">
        <div class="row">
            <div class="col-md-3">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <span class="glyphicon glyphicon-hand-right"></span>Escolha a eleição que deseja votar!</h3>
                    </div>
                    <s:form action="chooseElection" method="post">
                        <div class="panel-body">
                            <ul class="list-group">
                                <c:forEach items="${HeyBean.elections}" var="value">
                                    <li class="list-group-item">
                                        <div class="radio">
                                            <label>
                                                <input type="radio" name="optionsRadios">
                                                Teste
<%--                                                <c:out value="${value.title}" />--%>
                                            </label>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="panel-footer text-center">
                            <button type="button" class="btn btn-primary btn-block btn-sm">
                                Vote</button>
                        </div>
                    </s:form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>