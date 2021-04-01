package pt.uc.dei.student;

import pt.uc.dei.student.elections.Candidacy;
import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.elections.Election;
import pt.uc.dei.student.elections.Person;
import pt.uc.dei.student.others.NotifierCallBack;
import pt.uc.dei.student.others.RMI;
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
import java.util.concurrent.ConcurrentHashMap;

//Todo permitir apenas criar eleições depois da data atual
//Todo verificar se o terminal de voto fica livre e ocupado no multicast
//Todo verificar que os multicast estão em redes diferentes --- passar endereço por argumento

/**
 * Mesa de Voto (Servidor dos Terminais de voto)
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçãoves Perdigão
 * @see RMIServer
 * @see Thread
 */
public class MulticastServer extends Thread {
    /**
     * Porte do Servidor Multicast
     */
    public final static int MULTICAST_PORT = 7002;
    /**
     * Endereço IPv4 do Servidor Multicast
     */
    private final String MULTICAST_ADDRESS;
    /**
     * Socket do Servidor Multicast
     */
    private MulticastSocket socket;
    /**
     * Grupo de multicast do Servidor Multicast
     */
    private InetAddress group;
    /**
     * Estado ON/OFF do Servidor Multicast
     */
    private boolean isON = true;
    /**
     * String que permite identificar quando a consola está a espera de uma opção
     */
    private final String OPTION_STRING = ">>> ";
    /**
     * ID do Servidor Multicast
     */
    private int multicastId;
    /**
     * Servidor RMI ao qual está ligado o Servidor Multicast
     */
    private RMI rmiServer;
    /**
     * Departamento pelo qual o Servidor Multicast é responsavel
     */
    private Department department;
    /**
     * TODO nao sei o que é istoooooo
     */
    static MulticastServer multicastServer;
    /**
     * TODO idk
     */
    private final NotifierCallBack NOTIFIER;
    /**
     * HashMap com o estado dos terminais de voto
     */
    private final ConcurrentHashMap<String, Boolean> availableTerminals;

    /**
     * Construtor do Servidor Multicast (Mesa de Voto)
     *
     * @param multicastAddress endereço IPv4 do Servidor Multicast
     * @param rmiServer        servidor RMI
     * @throws IOException
     */
    public MulticastServer(String multicastAddress, RMI rmiServer) throws IOException { //TODO tratar a exceção com um try catch?
        this.rmiServer = rmiServer;
        this.MULTICAST_ADDRESS = multicastAddress;
        this.socket = new MulticastSocket(MULTICAST_PORT);
        this.group = InetAddress.getByName(multicastAddress);
        this.multicastId = 0;
        this.NOTIFIER = new NotifierCallBack();
        this.availableTerminals = new ConcurrentHashMap<>();
    }

    public void menuPollingStation(int dep_id) {
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
            try {
                campo = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();//TODO tratar exceção
            }
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
            try {
                campo = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();//TODO tratar exceção
            }
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
        System.out.println("Desbloqueado terminal " + id);
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
                res.append(String.format("|%d|%d", size + 1, size + 2));
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


        /*//Reset da mesa de voto na DB caso mandamos a mesa abaixo
        var sigHandler = new Thread(() -> {
            try {
                System.out.println("SET hasmulticastServer to null in DB");//TODO comentar isto
                rmiServer.updateDepartmentMulticast(multicastServer.getMulticastId());
            } catch (InterruptedException | RemoteException ignore) {}
        });
        Runtime.getRuntime().addShutdownHook(sigHandler);*/


        if (depName != null) {
            System.out.printf("======== Mesa de Voto #%s (%s) ========%n", this.getMulticastId(), depName);
            while (true) {
                this.menuPollingStation(this.department.getId());
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

    /**
     * Transforma a mensagem em datagramma e envia-o por multicast
     *
     * @param message
     */
    private void send(String message) {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MULTICAST_PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    /**
     * Trata a mensagem recebida
     *
     * @param msgHash mensagem recebida
     */
    private void doThings(HashMap<String, String> msgHash) {
        //nao ler as suas proprias mensagens
        if (!msgHash.get("sender").startsWith("multicast")) {
            switch (msgHash.get("message")) {
                case "login":
                    this.verifyLogin(msgHash.get("sender"), msgHash.get("username"), msgHash.get("password"));
                    break;
                case "vote":
                    this.verifyVote(msgHash.get("id_candidacy"), msgHash.get("id_election"), msgHash.get("cc"), msgHash.get("dep"), msgHash.get("sender").split("-")[1]);
                case "request_id":
                    registerTerminal(msgHash.get("sender"), msgHash.get("required_id"));
                    break;
            }
        }
    }

    /**
     * @param candidacyOption
     * @param id_election
     * @param cc
     * @param ndep
     */
    private void verifyVote(String candidacyOption, String id_election, String cc, String ndep, String terminalId) {

        ArrayList<Candidacy> candidacies;
        while (true) {
            try {
                candidacies = this.rmiServer.getCandidacies(Integer.parseInt(id_election));
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
        if (Utilitary.hasCandidacy(Integer.parseInt(candidacyOption), candidacies)) {
            while (true) {
                try {
                    this.rmiServer.updateCandidacyVotes(id_election, candidacyOption, cc, ndep);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
        } else if (Integer.parseInt(candidacyOption) == candidacies.size() + 1) {
            while (true) {
                try {
                    this.rmiServer.updateBlankVotes(id_election, cc, ndep);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
        } else if (Integer.parseInt(candidacyOption) == candidacies.size() + 2) {
            while (true) {
                try {
                    this.rmiServer.updateNullVotes(id_election, cc, ndep);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
        }
    }


    /**
     * Regista o estado de disponibilidade do terminal de voto
     *
     * @param id ID do terminal de voto
     */
    //todo
    private void registerTerminal(String id, String required_id) {
        //procurar terminais na base de dados com este id.
        int status;
        String message = "";
        while (true) {
            try {
                status = this.rmiServer.getTerminal(required_id);
                break;
            } catch (RemoteException | InterruptedException e) {
                e.printStackTrace();
                reconnectToRMI();
            }
        }
        //se nenhum, aceitar id
        if (status == -1) {
            while (true) {
                try {
                    this.rmiServer.insertTerminal(required_id, this.getMulticastId());
                    this.availableTerminals.put(required_id, true);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    e.printStackTrace();
                    reconnectToRMI();
                }
            }
            message = String.format("sender|multicast-%s-%s;destination|%s;message|request_id;allowed_id|%s", this.getMulticastId(), this.department.getId(), id, required_id);
        } else if (status == 0) { // se um, mas morto, aceitar id e enviar info adicional
            int cc_number_info;
            int id_election;
            while (true) {
                try {
                    id_election = this.rmiServer.getElectionIdFromTerminal(id);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    e.printStackTrace();
                    reconnectToRMI();
                }
            }
            String infoElection = getElectionInfo(id_election);
            while (true) {
                try {
                    cc_number_info = this.rmiServer.getElectorInfo(id);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    e.printStackTrace();
                    reconnectToRMI();
                }
            }
            if (cc_number_info != 0) this.availableTerminals.put(required_id, false);
            message = String.format("sender|multicast-%s-%s;destination|%s;message|request_id;allowed_id|%s;infoPerson|%s;infoElection|%s", this.getMulticastId(), this.department.getId(), id, required_id, cc_number_info, infoElection);
        } else { // se um e vivo rejeitar id.
            message = String.format("sender|multicast-%s-%s;destination|%s;message|request_id;allowed_id|not_available", this.getMulticastId(), this.department.getId(), id);
        }
        this.send(message);
    }

    /**
     * Verifica se a autenticação do eleitor no terminal de voto está valida,
     * envia por multicast a mensagem de sucesso ou falha no login do eleitor
     *
     * @param id       ID do terminal de voto
     * @param username numero de cartão de cidadão do eleitor
     * @param password hashCode da concatenação do numero de CC do eleitor com a password introduzida pelo eleitor
     */
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

    /**
     * Tenta efetuar a ligação ao Servidor RMI
     */
    public void reconnectToRMI() {
        while (true) {
            try {
                RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("clientMulticast");
                multicastServer.rmiServer = rmiServer;
                break;
            } catch (NotBoundException | IOException remoteException) {
                remoteException.printStackTrace(); //TODO caso o porte ou o lookup estejam errados, mais vale parar o programa
            }
        }
    }

    /**
     * Getter do Servidor RMI
     *
     * @return servidor RMI
     */
    public RMI getRmiServer() {
        return this.rmiServer;
    }

    /**
     * Getter do ID do Servidor Multicast
     *
     * @return ID do Servidor Multicast
     */
    public int getMulticastId() {
        return this.multicastId;
    }

    /**
     * Setter do ID do Servidor Multicast
     *
     * @param multicastId ID do Servidor Multicast
     */
    public void setMulticastId(int multicastId) {
        this.multicastId = multicastId;
    }

    /**
     * Setter do Departamento do Servidor Multicast
     *
     * @param department Departamento do Servidor Multicast
     */
    public void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * Verifica o numero de argumentos ao iniciar o programa,
     * pede o endereço IPv4 caso nao seja passado em argumento,
     * pede o departamento onde se localiza a mesa de voto,
     * efetua a ligação com o Servidor RMI e
     * inicializa a mesa de voto.
     * Caso nao consiga tenta até conseguir
     *
     * @param args argumentos de entrada do programa
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String network;
        switch (args.length) {
            case 0:
                do {
                    System.out.println("Endereço Multicast (ex:224.3.2.1)");
                    System.out.print(">>> ");
                    network = input.nextLine();
                } while (!Utilitary.isIPv4(network));
                break;
            case 1:
                if (!Utilitary.isIPv4(args[0])) {
                    System.out.println("arg1: Endereço invalido");
                    return;
                }
                network = args[0];
                break;
            default:
                System.out.println("Numeros de argumentos inválido");
                System.out.println("arg1: Endereço multicast");
                return;

        }
        try {
            int dep = -1;
            RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("clientMulticast");
            multicastServer = new MulticastServer(network, rmiServer);
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
            while (true) {
                try {
                    RMI rmiServer = (RMI) LocateRegistry.getRegistry(RMIServer.RMI_PORT).lookup("clientMulticast");
                    multicastServer = new MulticastServer(network, rmiServer);
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
