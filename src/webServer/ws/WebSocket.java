package webServer.ws;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint("/webServer/ws")
public class WebSocket {

    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public String receiveMessage(String message) {
        // one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
        //String upperCaseMessage = message.toUpperCase();
        //sendMessage("[" + username + "] " + upperCaseMessage);
        return message;
    }

    @OnError
    public void handleError(Throwable t) {
        t.printStackTrace();
    }

    public static void sendMessage(String text) {
        // uses *this* object's session to call sendText()

        for(Session s : sessions){
            try {
                s.getBasicRemote().sendText(text);
                break;
            } catch (IOException e) {
                // clean up once the WebSocket connection is closed
                try {
                    s.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }


    public static Set<Session> getSessions() {
        return sessions;
    }

    public static void setSessions(Set<Session> sessions) {
        WebSocket.sessions = sessions;
    }
}
