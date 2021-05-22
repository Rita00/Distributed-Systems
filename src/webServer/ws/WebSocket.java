package webServer.ws;

import java.io.IOException;
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
    /**
     * Websockets contendo todas as sessoes ativas
     */
    private static final Set<WebSocket> connections = new CopyOnWriteArraySet<>();
    /**
     * Sessão atual
     */
    private Session session;

    /**
     * Adiciona a corrente sessão ao Set ao abrir a ligação
     * @param session sessão corrente
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session=session;
        connections.add(this);
        //broadcast("Hey");
    }

    /**
     * Remove a corrente sessão ao Set ao fechar a ligação
     * @param session sessão corrente
     */
    @OnClose
    public void onClose(Session session) {
        connections.remove(this);
        //broadcast("Bye");
    }

    /**
     * Mostra dá print da mensagem recebida
     * @param message mensagem recebida
     */
    @OnMessage
    public void incoming(String message) {
        System.out.println(message);
        //broadcast(message);
    }

    /**
     * Lança uma exceção em caso de erro
     * @param t exceção recebida
     */
    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    /**
     * Envia pelo websocket a mensagem a todas as sessões do Set de Websockets
     * @param text mensagem para enviar
     */
    public synchronized static void broadcast(String text) {
        for(WebSocket c : connections){
            try {
                c.session.getBasicRemote().sendText(text);
            } catch (IOException e) {

                e.printStackTrace();
                connections.remove(c);
                try {
                    c.session.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                //broadcast("Removed");
            }
        }

    }
}
