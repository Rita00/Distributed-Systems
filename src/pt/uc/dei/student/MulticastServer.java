package pt.uc.dei.student;

import pt.uc.dei.student.elections.Candidacy;
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
import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//Todo Não dar print em tempo real de eleições que não estejam a decorrer
//Todo quando na consola de administração se adicionam novas mesas de voto a uma determinada eleição dar update em tempo real no multicast correspondente
//Todo verificar se o terminal de voto fica livre e ocupado no multicast
//Todo verificar que os multicast estão em redes diferentes --- passar endereço por argumento

public class MulticastServer extends Thread {
    private final String MULTICAST_ADDRESS = "224.3.2.1";
    public final static int MULTICAST_PORT = 7002;
    MulticastSocket socket;
    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);

    private boolean isON = true;
    private final String OPTION_STRING = ">>> ";

    private int multicastId = 0;
    private RMI rmiServer;
    private Department department;

    static MulticastServer multicastServer;

    private final NotifierCallBack NOTIFIER = new NotifierCallBack();

    private final HashMap<String, Boolean> availableTerminals = new HashMap<>();

    public MulticastServer(RMI rmiServer) throws IOException {
        this.rmiServer = rmiServer;
        this.socket = new MulticastSocket(MULTICAST_PORT);
    }

    public void menuPollingStation(int dep_id) throws IOException {
        int command = -1, command2 = -1, election = -1, campo_num = -1;
        String campo = "", campo_sql;
        Scanner input = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            ArrayList<Election> currentElections;
            ArrayList<Candidacy> candidacies;
            try {
                while (true) {
                    try {
                        currentElections = this.rmiServer.getCurrentElections(dep_id);
                        break;
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                while (true) {
                    try {
                        candidacies = this.rmiServer.getCandidacies(election);
                        break;
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                while (candidacies.size() == 0 || !Utilitary.hasElection(election, currentElections)) {
                    System.out.println("Eleições a decorrer: ");
                    Utilitary.listElections(currentElections);
                    System.out.print(OPTION_STRING);
                    election = input.nextInt();
                    while (true) {
                        try {
                            candidacies = this.rmiServer.getCandidacies(election);
                            break;
                        } catch (RemoteException e) {
                            reconnectToRMI();
                            //e.printStackTrace();
                        }
                    }
                    if (candidacies.size() == 0) System.out.println("Eleição sem listas, impossível votar!");
                }
                break;
            } catch (RemoteException | InterruptedException e) {
                reconnectToRMI();
                //e.printStackTrace();
            }
        }
        while (!(command >= 1 && command <= 6)) {
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

        if (command == 1) {
            campo_sql = "name";
            System.out.print("Introduza o seu nome: ");
            campo = reader.readLine();
        } else if (command == 2) {
            campo_sql = "job";
            int cargo = -1;
            while (!(cargo >= 1 && cargo <= 3)) {
                System.out.println("Introduza o cargo que ocupa: ");
                System.out.println("\t(1)- Estudante");
                System.out.println("\t(2)- Docente");
                System.out.println("\t(3)- Funcionário");
                System.out.print(OPTION_STRING);
                cargo = input.nextInt();
                campo = Utilitary.decideCargo(cargo);
            }
        } else if (command == 3) {
            campo_sql = "department_id";
            try {
                ArrayList<Department> departments;
                while (!(campo_num >= 1 && campo_num <= 11)) {
                    System.out.println("Escolha o departamento que frequenta: ");
                    while (true) {
                        try {
                            departments = this.rmiServer.getDepartments();
                            break;
                        } catch (RemoteException e) {
                            //e.printStackTrace();
                            reconnectToRMI();
                        }
                    }
                    Utilitary.listDepart(departments);
                    System.out.print(OPTION_STRING);
                    campo_num = input.nextInt();
                }
            } catch (InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        } else if (command == 4) {
            campo_sql = "phone";
            System.out.print("Introduza o seu número de telemóvel: ");
            campo_num = input.nextInt();
        } else if (command == 5) {
            campo_sql = "address";
            System.out.print("Introduza a sua morada: ");
            campo = reader.readLine();
        } else {
            campo_sql = "cc_number";
            System.out.print("Introduza o seu número de cartão de cidadão: ");
            campo_num = input.nextInt();
        }


        ArrayList<Person> people;
        while (true) {
            try {
                while (true) {
                    try {
                        people = this.rmiServer.getRegisPeople(election, dep_id, campo, campo_sql, campo_num);
                        break;
                    } catch (RemoteException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                if (people.size() != 0) {
                    while (people.size() != 0 && !(command2 >= 1 && command2 <= people.size() + 1)) {
                        System.out.println("Escolher Pessoa de acordo com o número de cartão de cidadão");
                        Utilitary.listPerson(people);
                        System.out.printf("\t(%s)- Nenhuma das anteriores\n", people.size() + 1);
                        System.out.print(OPTION_STRING);
                        command2 = input.nextInt();
                    }
                    if (command2 == people.size() + 1) {
                        System.out.println("Não pode votar nesta eleição!");
                    } else {
                        //select voting terminal
                        int cc_number = people.get(command2 - 1).getCc_number();
                        boolean alreadyVote;
                        while (true) {
                            // Caso a ligação falhe tentar reconectar-se
                            try {
                                alreadyVote = this.rmiServer.checkIfAlreadyVote(cc_number, election);
                                break;
                            } catch (InterruptedException e) {
                                //e.printStackTrace();
                                reconnectToRMI();
                            }
                        }
                        if (!alreadyVote)
                            selectTerminal(cc_number, election);
                        else System.out.println("Já votou nesta eleição!");
                    }
                } else
                    System.out.println("Não existem pessoas registadas nessas condições!");
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }

    }


    private void selectTerminal(int cc_number, int election) {
        String id = null;
        // choose terminal
        while (id == null) {
            for (String key : availableTerminals.keySet()) {
                if (availableTerminals.get(key)) {
                    id = key;
                    availableTerminals.put(key, false);
                    break;
                }
            }
        }
        //send to voting terminal the cc
        String info = getElectionInfo(election);
        String message = String.format("sender|multicast-%s-%s;destination|%s;message|identify;cc|%d;%s", this.getMulticastId(), this.department.getId(), id, cc_number, info);
        this.send(message);
        String[] getId = id.split("-");
        System.out.println("Desbloqueado terminal " + getId[1]);
    }

    private String getElectionInfo(int election) {
        StringBuilder res = new StringBuilder();
        ArrayList<Candidacy> candidacies;
        while (true) {
            try {
                while (true) {
                    try {
                        candidacies = this.rmiServer.getCandidacies(election);
                        break;
                    } catch (RemoteException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                res.append(String.format("election|%d;arrayList", election));
                for (Candidacy c : candidacies) {
                    res.append("|");
                    res.append(c.getName());
                }
                res.append("|Voto Branco|Voto Nulo");
                res.append(";arrayIds");
                for (Candidacy c : candidacies) {
                    res.append("|");
                    res.append(c.getId());
                }
                int size = candidacies.size();
                res.append(String.format("|%d|%d", size, size + 1));
                break;
            } catch (InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
        return res.toString();
    }

    private void connect() {
        String depName = this.department.getName();
        if (depName != null) {
            System.out.printf("======== Mesa de Voto #%s (%s) ========%n", this.getMulticastId(), depName);
            while (true) {
                try {
                    this.menuPollingStation(this.department.getId());
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
    }

    public void run() {
        try {
            //O servidor não recebe mensagens dos clientes (sem o Port)
            socket.joinGroup(group); // Para o servidor receber mensagens dar join ao grupo
            while (isON) {
                /*
                RECEBER E PARSE DO PACOTE
                 */
                byte[] bufferReceive = new byte[256];
                DatagramPacket packet = new DatagramPacket(bufferReceive, bufferReceive.length);
                socket.receive(packet);
                HashMap<String, String> msgHash = Utilitary.parseMessage(new String(packet.getData(), 0, packet.getLength()));
                /*
                USAR A INFORMACOES DO PACOTE
                 */
                this.doThings(msgHash);
            }
        } catch (SocketException se) {
            try {
                sleep(5000);
                System.out.println("Trying to Reconnect to the network...\n" + OPTION_STRING);
            } catch (InterruptedException e) {
                this.run();
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void send(String message) {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MULTICAST_PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void doThings(HashMap<String, String> msgHash) {
        //nao ler as suas proprias mensagens
        if (!msgHash.get("sender").startsWith("multicast")) {
            switch (msgHash.get("message")) {
                case "occupied":
                case "available":
                    registerTerminal(msgHash.get("sender"), msgHash.get("message"));
                    break;
                case "login":
                    this.verifyLogin(msgHash.get("sender"), msgHash.get("username"), msgHash.get("password"));
                    break;
                case "vote":
                    this.verifyVote(msgHash.get("id_candidacy"), msgHash.get("id_election"), msgHash.get("cc"), msgHash.get("dep"));
            }
        }
    }

    private void verifyVote(String candidacyOption, String id_election, String cc, String ndep) {
        while (true) {
            try {
                ArrayList<Candidacy> candidacies;
                while (true) {
                    try {
                        candidacies = this.rmiServer.getCandidacies(Integer.parseInt(id_election));
                        break;
                    } catch (RemoteException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                if (Utilitary.hasCandidacy(Integer.parseInt(candidacyOption), candidacies)) {
                    while (true) {
                        try {
                            this.rmiServer.updateCandidacyVotes(id_election, candidacyOption, cc, ndep);
                            break;
                        } catch (RemoteException e) {
                            //e.printStackTrace();
                            reconnectToRMI();
                        }
                    }
                } else if (Integer.parseInt(candidacyOption) == candidacies.size() + 1) {
                    while (true) {
                        try {
                            this.rmiServer.updateBlankVotes(id_election, cc, ndep);
                            break;
                        } catch (RemoteException e) {
                            //e.printStackTrace();
                            reconnectToRMI();
                        }
                    }
                } else if (Integer.parseInt(candidacyOption) == candidacies.size() + 2) {
                    while (true) {
                        try {
                            this.rmiServer.updateNullVotes(id_election, cc, ndep);
                            break;
                        } catch (RemoteException e) {
                            //e.printStackTrace();
                            reconnectToRMI();
                        }
                    }
                }
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();//TODO tratar excecao
                reconnectToRMI();
            }
        }
    }

    private void registerTerminal(String id, String status) {
        if (status.equals("available")) {
            availableTerminals.put(id, true);
        } else {
            availableTerminals.put(id, false);
        }
        while (true) {
            try {
                this.rmiServer.updateTerminals(this.getMulticastId(), availableTerminals);
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
                registerTerminal(id, status);
            }
        }
    }

    private void verifyLogin(String id, String username, String password) {
        String message;
        Person p;
        while (true) {
            try {
                while (true) {
                    try {
                        p = this.getRmiServer().getPerson(username, password);
                        break;
                    } catch (RemoteException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                if (p != null) {
                    message = String.format("sender|multicast-%s-%s;destination|%s;message|logged in;cc|%s", this.getMulticastId(), this.department.getId(), id, username);
                } else {
                    message = String.format("sender|multicast-%s-%s;destination|%s;message|wrong password", this.getMulticastId(), this.department.getId(), id);
                }
            } catch (InterruptedException e) {
                message = String.format("sender|multicast-%s-%s;destination|%s;message|wrong password", this.getMulticastId(), this.department.getId(), id);
            }
            this.send(message);
            break;
        }
    }

    public void reconnectToRMI() {
        while (true) {
            try {
                RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("clientMulticast");
                multicastServer.rmiServer = rmiServer;
                break;
            } catch (NotBoundException | IOException remoteException) {
                remoteException.printStackTrace();
            }
        }
    }

    public RMI getRmiServer() {
        return this.rmiServer;
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
            } else
                System.exit(0);
        } catch (Exception e) {
            int dep = -1;
            Scanner input = new Scanner(System.in);
            while (true) {
                try {
                    RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("clientMulticast");
                    multicastServer = new MulticastServer(rmiServer);
                    ArrayList<Department> departments = null;
                    try {
                        departments = multicastServer.rmiServer.getDepartments();
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
                        } else
                            System.exit(0);
                        break;
                    } catch (InterruptedException interruptedException) {
                        //interruptedException.printStackTrace();
                    }
                } catch (NotBoundException | IOException remoteException) {
                    //remoteException.printStackTrace();
                }
            }
        }
    }
}
