package pt.uc.dei.student;

import pt.uc.dei.student.elections.Candidacy;
import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.elections.Election;

import java.rmi.*;
import java.util.ArrayList;

public interface RMI extends Remote {
    String saySomething() throws java.rmi.RemoteException, InterruptedException;

    boolean insertPerson(String cargo, String pass, int dep, int num_phone, String address, int num_cc, int ano_cc, int mes_cc, int dia_cc) throws java.rmi.RemoteException, InterruptedException;

    boolean insertElection(int anoIni, int mesIni, int diaIni, int horaIni, int minIni, int anoFim, int mesFim, int diaFim, int horaFim, int minFim, String titulo, String descricao, String type_ele) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Election> getElections() throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Candidacy> getCandidacies(int election_id) throws java.rmi.RemoteException, InterruptedException;

    void updateElections(Election e) throws java.rmi.RemoteException, InterruptedException;

    void removeOnDB(String table, int id) throws java.rmi.RemoteException, InterruptedException;

    ArrayList<Department> getDepartments() throws java.rmi.RemoteException, InterruptedException;
}

