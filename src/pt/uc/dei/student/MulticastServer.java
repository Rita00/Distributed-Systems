package pt.uc.dei.student;

import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.elections.Election;
import pt.uc.dei.student.elections.Person;
import pt.uc.dei.student.others.Utilitary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MulticastServer extends Thread {
    private final String MULTICAST_ADDRESS = "224.3.2.1";
    public final static int MULTICAST_PORT = 7002;
    private final long SLEEP_TIME = 5000;
    private boolean isON = true;
    private final String OPTION_STRING = ">>> ";

    private int multicastId;
    private RMI rmiServer;
    private Department department;

    static MulticastServer multicastServer;

    private final NotifierCallBack NOTIFIER = new NotifierCallBack();

    public MulticastServer(RMI rmiServer) throws RemoteException {
        this.rmiServer = rmiServer;
    }

    public void menuPollingStation(int dep_id) throws IOException {
        int command = -1, election = -1;
        String campo;
        Scanner input = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            ArrayList<Election> currentElections = this.rmiServer.getCurrentElections(dep_id);
            while (!rmiServer.hasElection(election, currentElections)) {
                System.out.println("Eleições a decorrer: ");
                Utilitary.listElections(currentElections);
                election = input.nextInt();
            }
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }

        while (!(command >= 1 && command <= 3)) {
            System.out.println("Identificação de eleitor: ");
            System.out.println("Pesquisar por: ");
            System.out.println("\t(1)- Nome");
            System.out.println("\t(2)- Cargo");
            System.out.println("\t(3)- Departamento");
            System.out.println("\t(4)- Número de telemóvel");
            System.out.println("\t(5)- Morada");
            System.out.println("\t(6)- Número de cartão de cidadão");
            System.out.print(OPTION_STRING);
            command = input.nextInt();
        }
        switch (command) {
            case 1:
                System.out.print("Introduza o seu nome: ");
                campo = reader.readLine();
                try {
                    ArrayList<Person> persons = this.rmiServer.getRegisPeople(election, dep_id, campo);
                    Utilitary.listPerson(persons);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            default:
                break;
        }
    }

    private void connect() {
        String depName = this.department.getName();
        if (depName != null) {
            System.out.printf("======== Mesa de Voto #%s (%s) ========%n", this.getMulticastId(), depName);
            try {
                int id = this.department.getId();
                this.menuPollingStation(this.department.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        long counter = 0;
        try (MulticastSocket socket = new MulticastSocket(MULTICAST_PORT)) {
            //O servidor não recebe mensagens dos clientes (sem o Port)
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group); // Para o servidor receber mensagens dar join ao grupo
            while (isON) {
                String message = String.format("sender | multicast-%s-%s ; destination | %s ; message | I'm Multicast", this.getMulticastId(), this.department.getId(), "voteterm");
                DatagramPacket packet = this.send(socket, group, message);
                /*
                RECEBER E PARSE DO PACOTE
                 */
                socket.receive(packet);
                HashMap<String, String> msgHash = Utilitary.parseMessage(new String(packet.getData(), 0, packet.getLength()));
                /*
                USAR A INFORMACOES DO PACOTE
                 */
                this.doThings(msgHash, socket, group);

                try {
                    sleep((long) (Math.random() * SLEEP_TIME));
                } catch (InterruptedException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DatagramPacket send(MulticastSocket socket, InetAddress group, String message) {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MULTICAST_PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return packet;
    }

    private void doThings(HashMap<String, String> msgHash, MulticastSocket socket, InetAddress group) {
        //nao ler as suas proprias mensagens
        if (!msgHash.get("sender").startsWith("multicast")) {
            switch (msgHash.get("message")) {
                case "I'm VoteTerm":

                    break;
                case "login":
                    verifyLogin(msgHash.get("username"), msgHash.get("password"), socket, group);
                    break;
            }
        }
    }

    private void verifyLogin(String username, String password, MulticastSocket socket, InetAddress group) {
        String message;
        try {
            if (this.rmiServer.getPerson(username, password) != null) {
                message = String.format("sender | multicast-%s-%s ; destination | %s ; message | true", this.getMulticastId(), this.department.getId(), "voteterm");
            } else {
                message = String.format("sender | multicast-%s-%s ; destination | %s ; message | false", this.getMulticastId(), this.department.getId(), "voteterm");
            }
        } catch (RemoteException | InterruptedException e) {
            message = String.format("sender | multicast-%s-%s ; destination | %s ; message | false", this.getMulticastId(), this.department.getId(), "voteterm");
        }
        this.send(socket, group, message);
    }


    void listElections(ArrayList<Election> elections) {
        try {
            if (this.rmiServer.numElections() > 0) {
                for (Election e : elections) {
                    System.out.printf("\t(%s)- %s\n", elections.indexOf(e) + 1, e.getTitle());
                }
            } else {
                System.out.println("Não existem eleições\n");
            }
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void reconnectToRMI() {
        while (true) {
            try {
                RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("clientMulticast");
                multicastServer = new MulticastServer(rmiServer);
                break;
            } catch (RemoteException | NotBoundException remoteException) {
                remoteException.printStackTrace();
            }
        }
    }

    public int getMulticastId() {
        return this.multicastId;
    }

    public void setMulticastId(int multicastId) {
        this.multicastId = multicastId;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public static void main(String[] args) {
        try {
            int dep = -1;
            Scanner input = new Scanner(System.in);
            RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("clientMulticast");
            multicastServer = new MulticastServer(rmiServer);
            /*
            SETUP
             */
            ArrayList<Department> departments = multicastServer.rmiServer.getDepartments();
            while (!(dep >= 1 && dep <= 11)) {
                System.out.println("Departamento onde se localiza: ");
                Utilitary.listDepart(departments);
                System.out.print(">>> ");
                dep = input.nextInt();
            }
            if (rmiServer.initializeMulticast(dep, multicastServer.NOTIFIER) != null) {
                multicastServer.setMulticastId(dep);
                multicastServer.setDepartment(departments.get(dep - 1));
            /*
            LIGAR
             */
                multicastServer.start();
                multicastServer.connect();
            }
            System.exit(0);
        } catch (Exception e) {
            while (true) {
                try {
                    RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("clientMulticast");
                    multicastServer = new MulticastServer(rmiServer);
                    break;
                } catch (RemoteException | NotBoundException remoteException) {
                    remoteException.printStackTrace();
                }
            }
        }
    }
}
