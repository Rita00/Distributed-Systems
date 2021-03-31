package pt.uc.dei.student.others;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class NotifierCallBack extends UnicastRemoteObject implements Notifier {
    public NotifierCallBack() throws RemoteException {
        super();
    }

    public boolean ping() throws RemoteException {
        return true;
    }

    public void updateAdmin(ArrayList<InfoElectors> info) {
        for (InfoElectors i : info) {
            System.out.printf("%s\t%s\t%s\n", i.election_title, i.dep_name, i.count);
        }
    }

    public void updatePollsAdmin(ArrayList<InfoPolls> info) {
        String last = "";
        for (InfoPolls i : info) {
            if (!i.getDep_name().equals(last)) {
                if (i.getStatusPoll() == 1) {
                    System.out.printf("%s\tLigado\n", i.getDep_name());
                } else {
                    System.out.printf("%s\tDesligado\n", i.getDep_name());
                }
            }
            last = i.getDep_name();
            if (i.getStatusTerminal() == 1) {
                System.out.printf("\tTerminal %s\tLigado\n", i.getTerminal_id());
            } else {
                System.out.printf("\tTerminal %s\tDesligado\n", i.getTerminal_id());
            }
        }
    }
}
