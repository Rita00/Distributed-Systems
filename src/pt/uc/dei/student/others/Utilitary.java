package pt.uc.dei.student.others;

import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.elections.Election;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class Utilitary {
    public static HashMap<String, String> parseMessage(String msg) {
        HashMap<String, String> hash = new HashMap<String, String>();
        String[] dividedMessage = msg.split(" ; ");
        for (String token : dividedMessage) {
            String[] keyVal = token.split(" \\| ");
            if (keyVal.length == 2) {
                hash.put(keyVal[0], keyVal[1]);
            } else {
                System.out.println("Error with tokens");
            }
        }
        return hash;
    }

    public static void listDepart(ArrayList<Department> departments) {
        if (departments.size() == 0) System.out.println("");
        for (Department dep : departments) {
            System.out.printf("\t(%d)- %s%n", dep.getId(), dep.getName());
        }
    }

    public static void listElections(ArrayList<Election> elections) {
        if (elections.size() != 0) {
            for (Election e : elections) {
                System.out.printf("\t(%s)- %s\n", elections.indexOf(e) + 1, e.getTitle());
            }
        } else {
            System.out.println("Não existem eleições\n");
        }
    }
}
