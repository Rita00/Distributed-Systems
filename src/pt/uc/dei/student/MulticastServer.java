package pt.uc.dei.student;

import pt.uc.dei.student.elections.Candidacy;
import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.elections.Election;
import pt.uc.dei.student.elections.Person;
import pt.uc.dei.student.others.NotifierCallBack;
import pt.uc.dei.student.others.RMI;
import pt.uc.dei.student.others.Utilitary;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

//TODO CONSULTAR RESULTADOS DETALHADOS (FAIL)
//Todo listagem de registo nao aparece eleição 4001, idk why yet
//TODO verificar se nas eleiçoes que nao sao restritas a um unico departamento as pessoas so podem votar apenas 1 vez (ter cuidado se pode votar em mais que um departamento)

/**
 * Mesa de Voto (Servidor dos Terminais de voto)
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçalves Perdigão
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
     * Servidor porte do registo RMI
     */
    private final int REGISTRY_PORT;
    /**
     * Servidor nome do lookup
     */
    private final String LOOKUP_NAME;
    /**
     * Servidor RMI ao qual está ligado o Servidor Multicast
     */
    private RMI rmiServer;
    /**
     * Departamento pelo qual o Servidor Multicast é responsavel
     */
    private Department department;

    static MulticastServer multicastServer;
    /**
     * Callbaack usado para o servidor RMI verificar a conectividade com o multicast
     */
    private final NotifierCallBack NOTIFIER;
    /**
     * HashMap com o estado dos terminais de voto
     */
    private final ConcurrentHashMap<String, Boolean> availableTerminals;
    /**
     * TODO
     */
    private final ConcurrentHashMap<String, Integer> terminalPingCounter;

    /**
     * Construtor do Servidor Multicast (Mesa de Voto)
     *
     * @param multicastAddress      endereço IPv4 do Servidor Multicast
     * @param REGISTRY_PORT         porte do registo do rmi
     * @param LOOKUP_NAME           nome do lookup
     * @param rmiServer             servidor RMI
     * @param MULTICAST_SERVER_ADDRESS  endereço IPv4
     * @throws IOException exceção de I/O
     */
    public MulticastServer(String multicastAddress, int REGISTRY_PORT, String LOOKUP_NAME, RMI rmiServer, String MULTICAST_SERVER_ADDRESS) throws IOException { //TODO tratar a exceção com um try catch?
        System.setProperty("java.rmi.server.hostname", MULTICAST_SERVER_ADDRESS);
        this.rmiServer = rmiServer;
        this.MULTICAST_ADDRESS = multicastAddress;
        this.REGISTRY_PORT = REGISTRY_PORT;
        this.LOOKUP_NAME = LOOKUP_NAME;
        this.socket = new MulticastSocket(MULTICAST_PORT);
        this.group = InetAddress.getByName(multicastAddress);
        this.multicastId = 0;
        this.NOTIFIER = new NotifierCallBack();
        this.availableTerminals = new ConcurrentHashMap<>();
        this.terminalPingCounter = new ConcurrentHashMap<>();
    }

    /**
     * Menu da mesa de voto onde o eleitor pode proceder à sua identificação. <br>
     * São apresentadas as eleições a decorrerem no departamento <br>
     * Depois é possivel identificar o eleitor por: <br>
     * - Nome <br>
     * - Cargo <br>
     * - Departamento <br>
     * - Número de telemóvel <br>
     * - Morada <br>
     * - Número de cartão de cidadão <br>
     * Depois de introduzir a informação pessoal, são apresentados os eleitores correspondentes.<br>
     * Caso possa votar, é desbloquado o terminal de voto em que o eleitor pode votar.
     *
     * @param dep_id ID do departamento
     */
    public void menuPollingStation(int dep_id) {
        String command = "-1", command2 = "-1", election = "-1", campo_num = "-1";
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
                        candidacies = this.rmiServer.getCandidacies(Integer.parseInt(election));
                        break;
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                while (!Utilitary.isNumber(election) || candidacies.size() == 0 || !Utilitary.hasElection(Integer.parseInt(election), currentElections)) {
                    System.out.println("Eleições a decorrer: ");
                    Utilitary.listElections(currentElections);
                    System.out.print(OPTION_STRING);
                    election = input.nextLine();
                    if (Utilitary.isNumber(election)) {
                        while (true) {
                            try {
                                candidacies = this.rmiServer.getCandidacies(Integer.parseInt(election));
                                break;
                            } catch (RemoteException e) {
                                reconnectToRMI();
                                //e.printStackTrace();
                            }
                        }
                        if (candidacies.size() == 0) System.out.println("Eleição sem listas, impossível votar!");
                    }
                }
                break;
            } catch (RemoteException | InterruptedException e) {
                reconnectToRMI();
                //e.printStackTrace();
            }
        }
        while (!Utilitary.isNumber(command) || !(Integer.parseInt(command) >= 1 && Integer.parseInt(command) <= 6)) {
            System.out.println("Identificação de eleitor: ");
            System.out.println("Pesquisar por: ");
            System.out.println("\t(1)- Nome");
            System.out.println("\t(2)- Cargo");
            System.out.println("\t(3)- Departamento");
            System.out.println("\t(4)- Número de telemóvel");
            System.out.println("\t(5)- Morada");
            System.out.println("\t(6)- Número de cartão de cidadão");
            System.out.print(OPTION_STRING);
            command = input.nextLine();
        }

        if (Integer.parseInt(command) == 1) {
            campo_sql = "name";
            System.out.print("Introduza o seu nome: ");
            try {
                campo = reader.readLine();
            } catch (IOException e) {
                //e.printStackTrace();//TODO tratar exceção
            }
        } else if (Integer.parseInt(command) == 2) {
            campo_sql = "job";
            String cargo = "-1";
            while (!Utilitary.isNumber(cargo) || !(Integer.parseInt(cargo) >= 1 && Integer.parseInt(cargo) <= 3)) {
                System.out.println("Introduza o cargo que ocupa: ");
                System.out.println("\t(1)- Estudante");
                System.out.println("\t(2)- Docente");
                System.out.println("\t(3)- Funcionário");
                System.out.print(OPTION_STRING);
                cargo = input.nextLine();
                if (Utilitary.isNumber(cargo)) {
                    campo = Utilitary.decideCargo(Integer.parseInt(cargo));
                }
            }
        } else if (Integer.parseInt(command) == 3) {
            campo_sql = "department_id";
            try {
                ArrayList<Department> departments;
                while (!Utilitary.isNumber(campo_num) || !(Integer.parseInt(campo_num) >= 1 && Integer.parseInt(campo_num) <= 11)) {
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
                    campo_num = input.nextLine();
                }
            } catch (InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        } else if (Integer.parseInt(command) == 4) {
            campo_sql = "phone";
            do {
                System.out.print("Introduza o seu número de telemóvel: ");
                campo_num = input.nextLine();
            } while (!Utilitary.isNumber(campo_num));
        } else if (Integer.parseInt(command) == 5) {
            campo_sql = "address";
            System.out.print("Introduza a sua morada: ");
            try {
                campo = reader.readLine();
            } catch (IOException e) {
                //e.printStackTrace();//TODO tratar exceção
            }
        } else {
            campo_sql = "cc_number";
            do {
                System.out.print("Introduza o seu número de cartão de cidadão: ");
                campo_num = input.nextLine();
            } while (!Utilitary.isNumber(campo_num));
        }


        ArrayList<Person> people;
        while (true) {
            try {
                while (true) {
                    try {
                        people = this.rmiServer.getRegisPeople(Integer.parseInt(election), dep_id, campo, campo_sql, Integer.parseInt(campo_num));
                        break;
                    } catch (RemoteException e) {
                        //e.printStackTrace();
                        reconnectToRMI();
                    }
                }
                if (people.size() != 0) {
                    while (!Utilitary.isNumber(command2) || people.size() != 0 && !(Integer.parseInt(command2) >= 1 && Integer.parseInt(command2) <= people.size() + 1)) {
                        System.out.println("Escolher Pessoa de acordo com o número de cartão de cidadão");
                        Utilitary.listPerson(people);
                        System.out.printf("\t(%s)- Nenhuma das anteriores\n", people.size() + 1);
                        System.out.print(OPTION_STRING);
                        command2 = input.nextLine();
                    }
                    if (Integer.parseInt(command2) == people.size() + 1) {
                        System.out.println("Não pode votar nesta eleição!");
                    } else {
                        //select voting terminal
                        int cc_number = people.get(Integer.parseInt(command2) - 1).getCc_number();
                        boolean alreadyVote;
                        while (true) {
                            // Caso a ligação falhe tentar reconectar-se
                            try {
                                alreadyVote = this.rmiServer.checkIfAlreadyVote(cc_number, Integer.parseInt(election));
                                break;
                            } catch (InterruptedException e) {
                                //e.printStackTrace();
                                reconnectToRMI();
                            }
                        }
                        if (!alreadyVote)
                            selectTerminal(cc_number, Integer.parseInt(election));
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

    /**
     * Seleciona e desbloqueia um terminal disponivel em que o eleitor pode votar.
     *
     * @param cc_number número de cartão de cidadão
     * @param election  ID da eleição
     */
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
        //Dar update na db de quem está no terminal
        while (true) {
            try {
                this.rmiServer.updateTerminalInfoPerson(cc_number, id);
                this.rmiServer.updateTerminalInfoElection(election, id);
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }

        this.send(message);
        System.out.println("Desbloqueado terminal " + id);
    }

    /**
     * Tranforma o nome das listas da eleição com os respetivos IDs numa string
     * que possa ser enviada pelo protocolo multicast ao terminal de voto.
     *
     * @param election ID da eleição
     * @return string com as listas para serem enviadas por multicast
     */
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
                int nextOption = candidacies.get(candidacies.size() - 1).getId() + 1;
                res.append(String.format("|%d|%d", nextOption, nextOption + 1));
                break;
            } catch (InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
        return res.toString();
    }

    /**
     * Inicia a interface da consola do terminal de voto com o seu menu principal
     */
    private void connect() {
        String depName = this.department.getName();
        if (depName != null) {
            System.out.printf("======== Mesa de Voto #%s (%s) ========%n", this.getMulticastId(), depName);
            while (true) {
                this.menuPollingStation(this.department.getId());
            }
        }
    }

    /**
     * Liga o Servidor ao grupo de multicast,
     * recebe as mensagens enviadas no grupo,
     * efetua o parse da mensagem para uma HashMap e
     * a HashMap é enviada para tratamento
     */
    public void run() {
        try {
            //O servidor não recebe mensagens dos clientes (sem o Port)
            socket.joinGroup(group); // Para o servidor receber mensagens dar join ao grupo
            this.findTerminals();
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
        } catch (IOException ignore) {
        }
    }

    /**
     * Transforma a mensagem em datagramma e envia-o por multicast
     *
     * @param message mensagem para ser enviada
     */
    private void send(String message) {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MULTICAST_PORT);
        while (true) {
            try {
                socket.send(packet);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Trata a mensagem recebida
     *
     * @param msgHash mensagem recebida
     */
    private void doThings(HashMap<String, String> msgHash) {
        //nao ler as suas proprias mensagens
        String id;
        if (!msgHash.get("sender").startsWith("multicast")) {
            switch (msgHash.get("message")) {
                case "login":
                    this.verifyLogin(msgHash.get("sender"), msgHash.get("username"), msgHash.get("password"));
                    break;
                case "vote":
                    this.verifyVote(msgHash.get("id_candidacy"), msgHash.get("id_election"), msgHash.get("cc"), msgHash.get("dep"), msgHash.get("sender").split("-")[1]);
                    break;
                case "request_id":
                    registerTerminal(msgHash.get("sender"), msgHash.get("required_id"));
                    break;
                case "ping":
                    id = msgHash.get("sender");
                    this.terminalPingCounter.put(id.split("-")[1], 5);
                    break;
                case "timeout":
                    id = msgHash.get("sender").split("-")[1];
                    caseTimeout(id);
                    break;
                case "found":
                    id = msgHash.get("sender").split("-")[1];
                    this.availableTerminals.put(id, true);
                    this.terminalPingCounter.put(id, 5);
                    break;
            }
        }
    }

    /**
     * Caso o terminal não tenha interação é feito um reset dos estados de atividade
     * realacionados com esse terminal
     *
     * @param terminal_id ID do terminal
     */
    void caseTimeout(String terminal_id) {
        while (true) {
            try {
                this.rmiServer.updateTerminalInfoPerson(0, terminal_id);
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
        while (true) {
            try {
                this.rmiServer.updateTerminalInfoElection(-1, terminal_id);
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
        while (true) {
            try {
                this.rmiServer.updateTerminalStatus(terminal_id, "1");
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
        availableTerminals.put(terminal_id, true);
    }

    /**
     * Vota na lista escolhida pelo eleitor ou vota em branco ou vota nulo
     *
     * @param candidacyOption opção escolhida pelo eleitor no terminal de voto
     * @param id_election     ID da eleição em que o eleitor votou
     * @param cc              numero de cartão de cidadão do eleitor
     * @param ndep            numero do departamento onde o voto foi realizado
     * @param terminalId      ID do terminal de voto
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
        } else if (Integer.parseInt(candidacyOption) == candidacies.get(candidacies.size() - 1).getId() + 1) {
            while (true) {
                try {
                    this.rmiServer.updateBlankVotes(id_election, cc, ndep);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
        } else if (Integer.parseInt(candidacyOption) == candidacies.get(candidacies.size() - 1).getId() + 2) {
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
        while (true) {
            try {
                this.rmiServer.updateTerminalInfoPerson(0, terminalId);
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
        this.availableTerminals.put(terminalId, true);
        String message = String.format("sender|multicast-%s-%s;destination|%s;message|voteOk;cc|%s;election|%s", this.getMulticastId(), this.department.getId(), terminalId, cc, id_election);
        this.send(message);
    }

    /**
     * Verifica se o ID requerido pelo o terminal de voto está
     * disponivel. <br>
     * Caso o ID esteja disponivel, aceita. <br>
     * Caso o ID foi utilizado por um terminal que deixou de funcionar, aceita. <br>
     * Caso o ID não esteja disponivel, rejeita. <br>
     * As mensagens trocadas de aceitação/ rejeição são enviadas por multicast
     * ao terminal de voto. <br>
     * Quando aceito, o terminal de voto é registado na base de dados através do RMI
     *
     * @param id          ID random do terminal de voto que enviou a mensagem
     * @param required_id ID requerido pelo terminal de voto
     */
    private void registerTerminal(String id, String required_id) {
        //procurar terminais na base de dados com este id.
        int status;
        String message;
        while (true) {
            try {
                status = this.rmiServer.getTerminal(required_id);
                break;
            } catch (RemoteException | InterruptedException e) {
                //e.printStackTrace();
                reconnectToRMI();
            }
        }
        //se nenhum, aceitar id
        if (status == -1) {
            while (true) {
                try {
                    this.rmiServer.insertTerminal(required_id, this.getMulticastId());
                    this.availableTerminals.put(required_id, true);
                    this.terminalPingCounter.put(required_id, 5);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
            message = String.format("sender|multicast-%s-%s;destination|%s;message|request_id;allowed_id|%s", this.getMulticastId(), this.department.getId(), id, required_id);
        } else if (status == 0) { // se um, mas morto, aceitar id e enviar info adicional
            int cc_number_info;
            int id_election;
            while (true) {
                try {
                    this.rmiServer.updateTerminalStatus(required_id, "1");
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
            while (true) {
                try {
                    id_election = this.rmiServer.getElectionIdFromTerminal(required_id);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
            while (true) {
                try {
                    cc_number_info = this.rmiServer.getElectorInfo(required_id);
                    break;
                } catch (RemoteException | InterruptedException e) {
                    //e.printStackTrace();
                    reconnectToRMI();
                }
            }
            if (cc_number_info != 0) {
                this.availableTerminals.put(required_id, false);
            }
            this.terminalPingCounter.put(required_id, 5);
            String info = getElectionInfo(id_election);
            message = String.format("sender|multicast-%s-%s;destination|%s;message|request_id;allowed_id|%s;infoPerson|%s;%s", this.getMulticastId(), this.department.getId(), id, required_id, cc_number_info, info);
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
     * @param password código de acesso
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
                multicastServer.rmiServer = (RMI) LocateRegistry.getRegistry(this.REGISTRY_PORT).lookup(this.LOOKUP_NAME);
                break;
            } catch (NotBoundException | IOException remoteException) {
                remoteException.printStackTrace(); //TODO caso o porte ou o lookup estejam errados, mais vale parar o programa
            }
        }
    }

    /**
     * Cria e inicializa uma thread que vai trocar pings com o terminal de voto pelo
     * protocolo multicast para atualizar a hashMap dos seus terminais de voto. <br>
     * Caso não tenha pings no contador isto significa que o terminal de voto
     * deixou de funcionar.
     */
    public void initializeTerminalChecker() {
        new Thread(
                () -> {
                    while (true) {
                        for (String terminal_id : this.terminalPingCounter.keySet()) {
                            String message = String.format("sender|multicast-%s-%s;destination|%s;message|ping", this.getMulticastId(), this.department.getId(), terminal_id);
                            this.send(message);
                            this.terminalPingCounter.put(terminal_id, this.terminalPingCounter.get(terminal_id) - 1);
                            if (this.terminalPingCounter.get(terminal_id) == 0) {
                                while (true) {
                                    try {
                                        //0 siginifica que morreu
                                        this.rmiServer.updateTerminalStatus(terminal_id, "0");
                                        break;
                                    } catch (InterruptedException | RemoteException e) {
                                        //e.printStackTrace();
                                        reconnectToRMI();
                                    }
                                }
                            }
                        }
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                    }
                }
        ).start();
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
     * Envia mensagem para todos os terminais de voto no grupo de multicast
     * para conhecê-los
     */
    public void findTerminals() {
        String message = String.format("sender|multicast-%s-%s;destination|all;message|findTerminals", this.getMulticastId(), this.department.getId());
        send(message);
    }

    /**
     * Leitura dos dados no property file,
     * verifica o numero de argumentos ao iniciar o programa,
     * pede o endereço IPv4 caso nao seja passado em argumento,
     * pede o departamento onde se localiza a mesa de voto,
     * efetua a ligação com o Servidor RMI e
     * inicializa a mesa de voto. <br>
     * Caso nao consiga tenta até conseguir
     *
     * @param args argumentos de entrada do programa
     * @throws IOException problema na leitura do property file
     */
    public static void main(String[] args) throws IOException {
        /*
         * PROPERTIES
         */
        FileReader reader;
        try {
            //PARA OS JAR
            reader = new FileReader("config.properties");
        } catch (IOException e) {
            //PARA CORRER NO IDE
            reader = new FileReader("src/pt/uc/dei/student/config.properties");
        }
        Properties p = new Properties();
        p.load(reader);
        String SERVER_ADDRESS = p.getProperty("rmiServerAddress");
        int REGISTRY_PORT = Integer.parseInt(p.getProperty("rmiRegistryPort"));
        System.out.println("REGISTRY_PORT: " + REGISTRY_PORT);
        String LOOKUP_NAME = p.getProperty("multicastLookupName");
        System.out.println("LOOKUP_NAME: " + LOOKUP_NAME);
        /*

         */
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
        String MULTICAST_SERVER_ADDRESS = p.getProperty("multicastServerAddress");
        try {
            int dep = -1;
            RMI rmiServer = (RMI) LocateRegistry.getRegistry(SERVER_ADDRESS, REGISTRY_PORT).lookup(LOOKUP_NAME);
            multicastServer = new MulticastServer(network, REGISTRY_PORT, LOOKUP_NAME, rmiServer, MULTICAST_SERVER_ADDRESS);
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
//                multicastServer.findTerminals();
                multicastServer.initializeTerminalChecker();
                multicastServer.connect();
            } else
                System.exit(0);
        } catch (Exception e) {
            int dep = -1;
            while (true) {
                try {
                    RMI rmiServer = (RMI) LocateRegistry.getRegistry(SERVER_ADDRESS, REGISTRY_PORT).lookup(LOOKUP_NAME);
                    multicastServer = new MulticastServer(network, REGISTRY_PORT, LOOKUP_NAME, rmiServer,MULTICAST_SERVER_ADDRESS);
                    ArrayList<Department> departments;
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
//                            multicastServer.findTerminals();
                            multicastServer.initializeTerminalChecker();
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
