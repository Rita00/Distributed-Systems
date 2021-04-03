package pt.uc.dei.student.others;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe do Verificador de estado
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçãoves Perdigão
 * @see Thread
 */
public class StatusChecker extends Thread {
    /**
     * HashMap com os callback
     */
    public ConcurrentHashMap<Integer, Notifier> notifiers;
    /**
     * Servidor RMI
     */
    private RMI rmi;

    /**
     * Construtor do Objeto verificador de estado e
     * inicia a thread
     * @param notifiers callbacks
     * @param rmi servidor rmi
     * @see Notifier
     * @see RMI
     */
    public StatusChecker(ConcurrentHashMap<Integer, Notifier> notifiers, RMI rmi) {
        this.rmi = rmi;
        this.notifiers = notifiers;
        this.start();
    }
    /**
     * Tenta fazer ping todos os segundos,
     * caso seja impossivel, desliga a mesa de voto e
     * é retirada da hashMap
     */
    public void run() {
        while (true) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this.notifiers) {
                for (Integer id : this.notifiers.keySet()) {
                    try {
                        this.notifiers.get(id).ping();
                    } catch (Exception e) {
                        //multicast morreu
                        try {
                            this.rmi.turnOffPollingStation(id);
                            this.notifiers.remove(id);
                        } catch (RemoteException | InterruptedException remoteException) {
                            remoteException.printStackTrace();
                        }
                    }
                }
            }

        }
    }
}
