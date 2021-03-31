package pt.uc.dei.student.others;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
//TODO ===================================
//TODO CHECK COMENTARIOS
//TODO ===================================
/**
 * Classe do Callback
 *
 * @author Dylan Gonçãoves Perdigão
 * @author Ana Rita Rodrigues
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
     * @param info ArrayList com informações dobre as eleições
     * @see InfoElectors
     */
    public void updateAdmin(ArrayList<InfoElectors> info) {
        for (InfoElectors i : info) {
            System.out.printf("%s\t%s\t%s\n", i.getElection_title(), i.getDep_name(), i.getCount());
        }
    }
}
