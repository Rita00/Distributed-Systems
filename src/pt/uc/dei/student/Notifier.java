package pt.uc.dei.student;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Notifier extends Remote {
    boolean ping() throws RemoteException, InterruptedException;
    void updateAdmin(ArrayList<InfoElectors> info) throws RemoteException, InterruptedException;

}

