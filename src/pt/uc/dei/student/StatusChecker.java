package pt.uc.dei.student;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

class StatusChecker extends Thread {
    public ConcurrentHashMap<Integer, Notifier> notifiers;

    private RMI rmi;

    public StatusChecker(ConcurrentHashMap<Integer, Notifier> notifiers, RMI rmi) {
        this.rmi = rmi;
        this.notifiers = notifiers;
        this.start();
    }

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
