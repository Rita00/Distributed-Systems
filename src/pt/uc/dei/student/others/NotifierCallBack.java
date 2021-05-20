package pt.uc.dei.student.others;

import webServer.ws.WebSocket;

import javax.websocket.Session;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Classe do Callback
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçalves Perdigão
 * @see Notifier
 * @see UnicastRemoteObject
 */
public class NotifierCallBack extends UnicastRemoteObject implements Notifier {
    /**
     * Construtor do Callback
     *
     * @throws RemoteException Falha do Servidor
     */
    public NotifierCallBack() throws RemoteException {
        super();
    }
    /**
     * Tenta fazer um ping, caso contrario lança uma exceção
     *
     * @return true se o ping for bem sucedido
     */
    public boolean ping(){
        return true;
    }
    /**
     * Print das informações do votos nas eleições
     *
     * @param info ArrayList com informações sobre as eleições
     * @see InfoElectors
     */
    public void updateAdmin(ArrayList<InfoElectors> info) {
        String strWeb="";
        String strConsole="==============================\nCONTAGEM DOS VOTOS\n==============================\n";
        if(info.size()>0){
            info.sort(Comparator.comparing(InfoElectors::getElection_title));
            String before = "";
            for (InfoElectors i : info) {
                if(!before.equals(i.getElection_title())){
                    strWeb = String.format("%s<label>%s</label>",strWeb, i.getElection_title());
                    strConsole = String.format("%s%s\n\t",strConsole, i.getElection_title());
                }else{
                    strConsole = String.format("%s\t",strConsole);

                }
                strWeb = String.format("%s<p>%s: %s voto(s)</p>", strWeb, i.getDep_name(),i.getCount());
                strConsole = String.format("%s%s\t%s\n", strConsole, i.getDep_name(),i.getCount());
                before = i.getElection_title();
            }
        }else{
            strWeb = "<p>Sem dados para apresentar atualmente.<p>";
            strConsole = "Sem dados para apresentar atualmente.\n";
        }
        WebSocket.broadcast(strWeb);
        System.out.printf("%s", strConsole);
    }
    /**
     * Print das informações das mesas de voto e respetivos terminais de voto
     *
     * @param info ArrayList com informações sobre as mesas de voto e respetivos terminais de voto
     * @see InfoPolls
     */
    public void updatePollsAdmin(ArrayList<InfoPolls> info) {
        String strConsole="==============================\nESTADO MESAS DE VOTO\n==============================\n";
        String strWeb="";
        String last = "";
        for (InfoPolls i : info) {
            if (!i.getDep_name().equals(last)) {
                if (i.getStatusPoll() == 1) {
                    strWeb = String.format("%s<label>%s 🟢</label>",strWeb,i.getDep_name());
                    strConsole = String.format("%s%s 🟢\n",strConsole,i.getDep_name());
                } else {
                    strWeb = String.format("%s<label>%s 🔴</label>",strWeb,i.getDep_name());
                    strConsole = String.format("%s%s 🔴\n",strConsole,i.getDep_name());
                }
            }
            last = i.getDep_name();
            if (i.getTerminal_id() != 0) {
                if (i.getStatusTerminal() == 0) {
                    strWeb = String.format("%s<p>Terminal #%s 🔴</p>",strWeb,i.getTerminal_id());
                    strConsole = String.format("%s\tTerminal #%s 🔴\n",strConsole,i.getTerminal_id());
                } else {
                    strWeb = String.format("%s<p>Terminal #%s 🟢</p>",strWeb,i.getTerminal_id());
                    strConsole = String.format("%s\tTerminal #%s 🟢\n",strConsole,i.getTerminal_id());
                }
            }
        }
        strWeb=strWeb.replace("</label><label>","</label><p></p><label>");
        WebSocket.broadcast(strWeb);
        System.out.printf("%s",strConsole);
    }
}
