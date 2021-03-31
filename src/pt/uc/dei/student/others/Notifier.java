package pt.uc.dei.student.others;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
//TODO ===================================
//TODO CHECK COMENTARIOS
//TODO ===================================
/**
 * Classe da Interface do Notificador
 *
 * @see Remote
 * @author Dylan Gonçãoves Perdigão
 * @author Ana Rita Rodrigues
 */
public interface Notifier extends Remote {
    /**
     * Tenta fazer um ping, caso contrario lança uma exceção
     *
     * @return true se o ping for bem sucedido
     * @throws RemoteException Falha do Servidor
     * @throws InterruptedException Interrupção da thread
     */
    boolean ping() throws RemoteException, InterruptedException;
    /**
     * Print das informações do votos nas eleições
     *
     * @param info ArrayList com informações dobre as eleições
     * @throws RemoteException Falha do Servidor
     * @throws InterruptedException Interrupção da thread
     * @see InfoElectors
     */
    void updateAdmin(ArrayList<InfoElectors> info) throws RemoteException, InterruptedException;
    void updatePollsAdmin(ArrayList<InfoPolls> info) throws RemoteException, InterruptedException;
    }

