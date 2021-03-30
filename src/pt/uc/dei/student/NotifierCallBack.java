package pt.uc.dei.student;

import pt.uc.dei.student.others.InfoElectors;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class NotifierCallBack extends UnicastRemoteObject implements Notifier {
    public NotifierCallBack() throws RemoteException {
        super();
    }

    public boolean ping()  throws RemoteException{
        return true;
    }

    public void updateAdmin(ArrayList<InfoElectors> info) {
        for (InfoElectors i : info) {
            System.out.printf("%s\t%s\t%s\n", i.election_title, i.dep_name, i.count);
        }
    }
}
