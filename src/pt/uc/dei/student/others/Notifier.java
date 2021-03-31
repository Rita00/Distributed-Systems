package pt.uc.dei.student.others;

import pt.uc.dei.student.others.InfoElectors;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Notifier extends Remote {
    boolean ping() throws RemoteException, InterruptedException;
    void updateAdmin(ArrayList<InfoElectors> info) throws RemoteException, InterruptedException;
    void updatePollsAdmin(ArrayList<InfoPolls> info) throws RemoteException, InterruptedException;
    }

