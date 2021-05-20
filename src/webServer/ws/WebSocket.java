package webServer.ws;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint("/webServer/ws")
public class WebSocket {

    private static final Set<WebSocket> connections = new CopyOnWriteArraySet<>();
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session=session;
        connections.add(this);
        broadcast("Hey");
    }

    @OnClose
    public void onClose(Session session) {
        connections.remove(this);
        //broadcast("Bye");
    }

    @OnMessage
    public void incoming(String message) {
        System.out.println(message);
        broadcast(message);
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    public static void broadcast(String text) {
        for(WebSocket c : connections){
            try {
                synchronized (c) {
                    c.session.getBasicRemote().sendText(text);
                }
            } catch (IOException e) {
                e.printStackTrace();
                /*
                connections.remove(c);
                try {
                    c.session.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                */
                //broadcast("Removed");
            }
        }

    }
}
