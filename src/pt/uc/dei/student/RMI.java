package pt.uc.dei.student;

import java.rmi.*;

public interface RMI extends Remote {
    String saySomething() throws java.rmi.RemoteException, InterruptedException;

    boolean insertPerson(String cargo, String pass, String dep, int num_phone, String address, int num_cc, int ano_cc, int mes_cc, int dia_cc) throws java.rmi.RemoteException, InterruptedException;

    boolean insertElection(int anoIni, int mesIni, int diaIni, int horaIni, int minIni, int anoFim, int mesFim, int diaFim, int horaFim, int minFim, String titulo, String descricao) throws java.rmi.RemoteException, InterruptedException;
}