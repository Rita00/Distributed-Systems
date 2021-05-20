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
    <link rel="stylesheet" type="text/css" href="style/realTime.css">
    <!-- title -->
    <title>UC eVoting | Consola de Administração | Constagem dos Votos</title>
    <!--icon-->
    <link rel="shortcut icon" href="images/favicon.ico">
    <script type="text/javascript">

        let websocket = null;

        window.onload = function() { // URI = ws://10.16.0.165:8080/WebSocket/ws
            connect('ws://' + window.location.host + '/webserver/webServer/ws');
        }

        function connect(host) { // connect to the host websocket
            if ('WebSocket' in window) {
                console.log("WebSocket in window")
                websocket = new WebSocket(host);
            } else if ('MozWebSocket' in window){
                console.log("MozWebSocket in window")
                websocket = new MozWebSocket(host);
            }else {
                alert('Get a real browser which supports WebSocket.');
                return;
            }
            websocket.onopen    = onOpen; // set the 4 event listeners below
            websocket.onclose   = onClose;
            websocket.onmessage = onMessage;
            websocket.onerror   = onError;
        }

        function onOpen(event) {
            //alert('Connected to ' + window.location.host + '.');
        }

        function onClose(event) {
            write('WebSocket closed (code ' + event.code + ').');
        }

        function onMessage(message) { // print the received message
            //alert(message.data);
            write(message.data);
        }

        function onError(event) {
            write('WebSocket error.');
        }

        function write(text) {
            var textbox = document.getElementById('text');
            textbox.style.wordWrap = 'break-word';
            textbox.innerHTML = text;
        }
    </script>

</head>
<body>
<div id="container" class="container">
    <img alt="UC Logo" width="100%" id="logo"
         src="https://www.uc.pt/identidadevisual/Marcas_UC_submarcas/marcas_submarcas/UC_H_FundoClaro-negro?hires">

    <h1>Contagem dos Votos</h1>

    <div class="history" id="text">
        ${HeyBean.infoVotes}
    </div>

    <s:form action="" method="post">
        <button id="exit">Voltar</button>
    </s:form>
</div>
</body>
</html>