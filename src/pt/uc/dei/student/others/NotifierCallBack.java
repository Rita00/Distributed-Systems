package pt.uc.dei.student.others;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
        System.out.println("==============================");
        for (InfoElectors i : info) {
            System.out.printf("%s\t%s\t%s\n", i.getElection_title(), i.getDep_name(), i.getCount());
        }
    }
    /**
     * Print das informações das mesas de voto e respetivos terminais de voto
     *
     * @param info ArrayList com informações sobre as mesas de voto e respetivos terminais de voto
     * @see InfoPolls
     */
    public void updatePollsAdmin(ArrayList<InfoPolls> info) {
        String last = "";
        System.out.println("==============================");
        for (InfoPolls i : info) {
            if (!i.getDep_name().equals(last)) {
                if (i.getStatusPoll() == 1) {
                    System.out.printf("%s\tLigado\n", i.getDep_name());
                } else {
                    System.out.printf("%s\tDesligado\n", i.getDep_name());
                }
            }
            last = i.getDep_name();
            if (i.getTerminal_id() != 0) {
                if (i.getStatusTerminal() == 0) {
                    System.out.printf("\tTerminal %s\tDesligado\n", i.getTerminal_id());
                } else {
                    System.out.printf("\tTerminal %s\tLigado\n", i.getTerminal_id());
                }
            }
        }
    }
}
