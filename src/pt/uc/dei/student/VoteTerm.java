package pt.uc.dei.student;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import pt.uc.dei.student.others.Utilitary;

/**
 * Terminal de Voto (Cliente do Servidor Multicast)
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçalves Perdigão
 * @see MulticastServer
 * @see Thread
 */
public class VoteTerm extends Thread {
    /**
     * Guarda a eleição em que um determinado eleitor está a votar
     */
    private String electionBlocked;
    /**
     * Guarda o número de cartãoo de cidadão de um determinado eleitor que está a votar
     */
    private String ccBlocked;
    /**
     * Porte do Servidor Multicast
     */
    private final int MULTICAST_PORT;
    /**
     * Endereço IPv4 do Servidor Multicast
     */
    private final String MULTICAST_ADDRESS;
    /**
     * ID do terminal de voto
     */
    private int voteTermId;
    /**
     * ID do departamento do terminal de voto
     */
    private int departmentId;
    /**
     * Socket entre o terminal de voto e o servidor multicast
     */
    private MulticastSocket socket;
    /**
     * Grupo de multicast do terminal de voto
     */
    private InetAddress group;
    /**
     * Estado do terminal de voto
     */
    private boolean available = true;
    /**
     * String que permite identificar quando a consola está a espera de uma opção
     */
    private final String OPTION_STRING = ">>> ";

    /**
     * Construtor do Terminal de Voto,
     * gera um id aleatorio para o terminal
     *
     * @param multicastAddress endereço IPv4 do servidor multicast
     * @param multicastPort    porte do servidor multicast
     */
    VoteTerm(String multicastAddress, int multicastPort) {
        this.voteTermId = (int) Math.round(Math.random() * Integer.MAX_VALUE) + 1;
        this.MULTICAST_ADDRESS = multicastAddress;
        this.MULTICAST_PORT = multicastPort;
        try {
            this.setSocket(new MulticastSocket(this.MULTICAST_PORT));
            this.getSocket().setSoTimeout(1000);
            this.setGroup(InetAddress.getByName(this.MULTICAST_ADDRESS));
            this.getSocket().joinGroup(this.getGroup());
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    /**
     * Thread que se liga ao socket e grupo do multicast,
     * envia mensagens para o grupo de multicast,
     * recebe as mensagens provenientes do grupo de multicast
     * e descompõe as mensagens em HashMap para serem tratadas.
     */
    public void run() {
        // TODO faz identifação apenas uma vez ou se der timeout à espera de resposta
        while (true) {
            try {
                    /*
                RECEBER E PARSE DO PACOTE
                 */
                //sem isto o tamanho da mensagem a receber é limitada ao tamanho da mensagem antes enviada
                byte[] bufferReceive = new byte[256];
                DatagramPacket packet = new DatagramPacket(bufferReceive, bufferReceive.length);
                this.getSocket().receive(packet);
                String recvMsg = new String(packet.getData(), 0, packet.getLength());
                HashMap<String, String> msgHash = Utilitary.parseMessage(recvMsg);
                /*
                USAR AS INFORMACOES DO PACOTE
                 */
                doThings(msgHash);
            } catch (Exception e) {
                //e.printStackTrace();
            }

        }
    }

    /**
     * Permite atribuir o departamento proveniente do multicast,
     * Trata a mensagem recebida
     *
     * @param msgHash mensagem recebida
     */
    private void doThings(HashMap<String, String> msgHash) {
        //so ler as mensagens do multicast
        if (msgHash.get("sender").startsWith("multicast")) {
            String[] ndep = msgHash.get("sender").split("-");
            this.setDepartmentId(Integer.parseInt(ndep[2]));
            try {
                if (msgHash.get("destination").equals("all") || Integer.parseInt(msgHash.get("destination")) == this.voteTermId) {
                    switch (msgHash.get("message")) {
                        case "identify":
                            this.available = false;
                            this.login(msgHash.get("cc"), msgHash.get("arrayList"), msgHash.get("arrayIds"), msgHash.get("election"), ndep[2]);
                            break;
                        case "ping":
                            this.checkAlive();
                            break;
                        case "voteOk":
                            this.terminateVote(msgHash.get("election"), msgHash.get("cc"));
                            break;
                        case "findTerminals":
                            String sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|found", this.getVoteTermId(), this.getDepartmentId(), "multicast");
                            this.sendMessage(sendMsg);
                            break;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                //e.printStackTrace();
            }
        }
    }

    /**
     * Recebe uma mensagem da mesa de voto a confirmar que recebeu o voto efetuado
     * Só desbloqueia o terminal de voto se a eleição e o número de cartão de cidadão corresponderem ao eleitor mais recente
     *
     * @param election id da eleição recebido na mensagem da mesa de voto
     * @param cc       número de cartão de cidadão recebido na mensagem da mesa de voto
     */
    public void terminateVote(String election, String cc) {
        //verificar que é sobre o voto da pessoa para quem o terminal está desbloqueado
        if (electionBlocked.equals(election) && ccBlocked.equals(cc)) {
            this.available = true;
            System.out.println("Voto registado!");
        }
    }

    /**
     * Envia para a mesa de voto mensagens a confirmar que ainda está a funcionar
     */
    public void checkAlive() {
        String sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|ping", this.getVoteTermId(), this.getDepartmentId(), "multicast");
        byte[] buffer = sendMsg.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.getGroup(), MULTICAST_PORT);
        while (true) {
            try {
                this.getSocket().send(packet);
                break;
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    /**
     * Efetua a listagem na consola do Terminal de Voto listas em que o eleitor pode votar
     *
     * @param infoElectionByName String com os nomes das eleições
     * @param infoElectionById   String com os IDs das eleições
     */
    private void listInfo(String infoElectionByName, String infoElectionById) {
        String[] infoName = infoElectionByName.split("\\|"), infoID = infoElectionById.split("\\|");
        for (int i = 0; i < infoName.length; i++) {
            System.out.printf("\t(%s)- %s\n", infoID[i], infoName[i]);
        }
    }

    /**
     * Recolhe os dados de login do eleitor,
     * envia essa informação com o protocolo multicast para verificar se o login é valido,
     * aguarda resposta do servidor multicast se o login é valido
     *
     * @param cc         numero do cartão de cidadão
     * @param infoByName String com o nome das listas
     * @param infoById   String com o id das listas
     * @param election   nome da eleição
     * @param ndep       numero do departamento
     */
    private void login(String cc, String infoByName, String infoById, String election, String ndep) {
        final boolean[] timeout = {false};
        String timeout_message = String.format("sender|voteterm-%s-%s;destination|%s;message|timeout", this.getVoteTermId(), this.getDepartmentId(), "multicast");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Closeable reader = input;
        String password = "";
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeout[0] = true;
                sendMessage(timeout_message);
                System.out.println("\nDemorou demasiado tempo!\nPRESS ENTER TO CONTINUE");
                // Your database code here
            }
        }, 60 * 1000);

        HashMap<String, String> msgHash;
        boolean isFirstAttempt = true;
        do {
            if (!isFirstAttempt) {
                System.out.println("Wrong username or password");
            }
            isFirstAttempt = false;
            /*
            GET USERNAME PASSWORD
             */
            System.out.println("User: " + cc);
            System.out.print("Password: ");
            try {
                password = input.readLine();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            if (timeout[0]) {
                return;
            }
            //--------------

            String sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|login;username|%s;password|%s", this.getVoteTermId(), this.getDepartmentId(), "multicast", cc, password);
            byte[] buffer = sendMsg.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.getGroup(), MULTICAST_PORT);
            /*
            SEND TO MULTICAST FOR VERIFICATION
            */
            while (true) {
                try {
                    this.getSocket().send(packet);
                    byte[] buffer2 = new byte[256];
                    packet = new DatagramPacket(buffer2, buffer2.length, this.getGroup(), MULTICAST_PORT);
                    this.getSocket().receive(packet);
                    String recvMsg = new String(packet.getData(), 0, packet.getLength());
                    msgHash = Utilitary.parseMessage(recvMsg);
                    if (msgHash.get("message").equals("logged in")) {
                        if (msgHash.get("cc").equals(cc)) {
                            break;
                        }
                    }
                    if (msgHash.get("message").equals("wrong password")) {
                        while(msgHash.get("message").equals("wrong password")){
                            this.getSocket().receive(packet);
                            recvMsg = new String(packet.getData(), 0, packet.getLength());
                            msgHash = Utilitary.parseMessage(recvMsg);
                        }
                        break;
                    }
                    doThings(msgHash);
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
            if (msgHash.get("message").equals("logged in")) {
                break;
            }
        } while (!msgHash.get("message").equals("logged in"));
        System.out.println("Successfully Logged In");
        this.accessVotingForm(infoByName, infoById, election, cc, ndep, timer, timeout);
    }

    /**
     * Lista as opções em que o eleitor pode votar,
     * espera pelo input do eleitor,
     * envia por multicast ao Servidor a escolha,
     * muda o estado do terminal de voto para "disponivel"
     *
     * @param infoByName String com o nome das listas
     * @param infoById   String com o id das listas
     * @param election   nome da eleição
     * @param cc         numero do cartão de cidadão
     * @param ndep       numero do departamento
     * @param timer      temporizador da inatividade
     * @param timeout    booleano para saber se está inativo há 60 segundos
     */
    private void accessVotingForm(String infoByName, String infoById, String election, String cc, String ndep, Timer timer, boolean[] timeout) {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        electionBlocked = election;
        ccBlocked = cc;
        String sendMsg;
        int list = -1;
        String command;
        System.out.println("Escolha uma lista para votar: ");
        this.listInfo(infoByName, infoById);
        System.out.print(OPTION_STRING);
        try {
            command = input.readLine();
            list = Integer.parseInt(command);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        if (timeout[0]) {
            return;
        }

        timer.cancel();
        sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|vote;id_candidacy|%s;id_election|%s;cc|%s;dep|%s", this.getVoteTermId(), this.getDepartmentId(), "multicast", list, election, cc, ndep);
        while (true) {
            sendMessage(sendMsg);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            if (!this.available) {
                break;
            }
        }
    }

    /**
     * Transforma a mensagem em datagramma e envia-o por multicast
     *
     * @param message mensagem para ser enviada
     */
    public void sendMessage(String message) {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.getGroup(), MULTICAST_PORT);
        while (true) {
            try {
                this.getSocket().send(packet);
                break;
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    /**
     * Getter do ID do terminal de voto
     *
     * @return ID do terminal de voto
     */
    public int getVoteTermId() {
        return this.voteTermId;
    }

    /**
     * Getter do ID do departamento do terminal de voto
     *
     * @return ID do departamento do terminal de voto
     */
    public int getDepartmentId() {
        return this.departmentId;
    }

    /**
     * Getter do socket do terminal de voto
     *
     * @return socket do terminal de voto
     */
    public MulticastSocket getSocket() {
        return this.socket;
    }

    /**
     * Getter do grupo de multicast do terminal de voto
     *
     * @return grupo de multicast do terminal de voto
     */
    public InetAddress getGroup() {
        return this.group;
    }

    /**
     * Setter do ID do departamento do terminal de voto
     *
     * @param departmentId ID do departamento do terminal de voto
     */
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * Setter do socket do terminal de voto
     *
     * @param socket socket do terminal de voto
     */
    public void setSocket(MulticastSocket socket) {
        this.socket = socket;
    }

    /**
     * Setter do grupo de multicast do terminal de voto
     *
     * @param group grupo de multicast do terminal de voto
     */
    public void setGroup(InetAddress group) {
        this.group = group;
    }

    //TODO ENVIAR NDEP PARA O MULTICAST
    /**
     * Inicializa o terminal e efetua o pedido ao servidor multicast para ver
     * se o ID requerido está disponivel
     *
     * @param required_id ID requerido pelo terminal de voto
     */
    public void initializeTerminal(String required_id) {
        String sendMsg;
        sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|request_id;required_id|%s", this.getVoteTermId(), this.getDepartmentId(), "multicast", required_id);
        byte[] buffer = sendMsg.getBytes();

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.getGroup(), MULTICAST_PORT);
                this.getSocket().send(packet);
                byte[] bufferReceive = new byte[256];
                packet = new DatagramPacket(bufferReceive, bufferReceive.length);
                this.getSocket().receive(packet);
                String recvMsg = new String(packet.getData(), 0, packet.getLength());
                HashMap<String, String> msgHash = Utilitary.parseMessage(recvMsg);
                int count = 10;
                while (msgHash.get("destination").split("-").length < 2 || Integer.parseInt(msgHash.get("destination").split("-")[1]) != this.voteTermId && count > 0) {
                    this.getSocket().receive(packet);
                    recvMsg = new String(packet.getData(), 0, packet.getLength());
                    msgHash = Utilitary.parseMessage(recvMsg);
                    count--;
                }
                if (count <= 0) {
                    throw new RuntimeException("Took too long getting a reply.");
                }
                if (msgHash.get("allowed_id").equals(required_id)) {
                    this.voteTermId = Integer.parseInt(required_id);
                    if (msgHash.get("infoPerson") != null && !msgHash.get("infoPerson").equals("0")) {
                        String[] ndep = msgHash.get("sender").split("-");
                        this.login(msgHash.get("infoPerson"), msgHash.get("arrayList"), msgHash.get("arrayIds"), msgHash.get("election"), ndep[2]);
                    }
                } else {
                    System.out.println("ID não disponível, a morrer...");
                    return;
                }
                break;
            } catch (IOException | RuntimeException e) {
                //e.printStackTrace();
            }
        }

        System.out.printf("======== Terminal de Voto #%s ========\n", this.voteTermId);
        this.start();
    }

    /**
     * Verifica o numero de argumentos ao iniciar o programa,
     * pede o endereço IPv4 caso nao seja passado em argumento,
     * inicializa o terminal de voto
     *
     * @param args argumentos de entrada do programa
     * @throws IOException Problema na leitura do ID requerido
     */
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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
                System.out.println("Numero de argumentos inválido");
                System.out.println("arg1: Endereço multicast");
                return;

        }
        System.out.println("Required id:");
        String reqID;
        do {
            System.out.print(">>> ");
            reqID = reader.readLine();
        } while (!Utilitary.isNumber(reqID));

        VoteTerm client = new VoteTerm(network, MulticastServer.MULTICAST_PORT);
        client.initializeTerminal(reqID);
    }
}
