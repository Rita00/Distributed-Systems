package pt.uc.dei.student.others;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
/**
 * Classe da Interface do Notificador
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçalves Perdigão
 * @see Remote
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
     * @param info ArrayList com informações sobre as eleições
     * @throws RemoteException Falha do Servidor
     * @throws InterruptedException Interrupção da thread
     * @see InfoElectors
     */
    void updateAdmin(ArrayList<InfoElectors> info) throws RemoteException, InterruptedException;
    /**
     * Print das informações das mesas de voto e respetivos terminais de voto
     *
     * @param info ArrayList com informações sobre as mesas de voto e respetivos terminais de voto
     * @throws RemoteException Falha do Servidor
     * @throws InterruptedException Interrupção da thread
     * @see InfoPolls
     */
    void updatePollsAdmin(ArrayList<InfoPolls> info) throws RemoteException, InterruptedException;
    }

