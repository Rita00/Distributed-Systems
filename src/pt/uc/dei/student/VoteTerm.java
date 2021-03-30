package pt.uc.dei.student;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import pt.uc.dei.student.others.Utilitary;


/**
 * Clientes do Multicast Server (Terminais de Voto)
 */
public class VoteTerm extends Thread {
    private final int MULTICAST_PORT;
    private final String MULTICAST_ADDRESS;

    private final int voteTermId;
    private int departmentId;
    private MulticastSocket socket;
    private InetAddress group;
    private boolean available = true;

    private final String OPTION_STRING = ">>> ";

    VoteTerm(String multicastAddress, int multicastPort) {
        this.voteTermId = (int) Math.round(Math.random() * Integer.MAX_VALUE) + 1;
        this.MULTICAST_ADDRESS = multicastAddress;
        this.MULTICAST_PORT = multicastPort;
    }

    public void run() {
        // TODO faz identifação apenas uma vez ou se der timeout à espera de resposta
        try {
            this.setSocket(new MulticastSocket(this.MULTICAST_PORT));
            this.setGroup(InetAddress.getByName(this.MULTICAST_ADDRESS));
            this.getSocket().joinGroup(this.getGroup());
            while (true) {
                String sendMsg;
                if (available) {
                    sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|available", this.getVoteTermId(), this.getDepartmentId(), "multicast");
                } else {
                    sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|occupied", this.getVoteTermId(), this.getDepartmentId(), "multicast");
                }
                byte[] buffer = sendMsg.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.getGroup(), MULTICAST_PORT);
                this.getSocket().send(packet);
                /*
                RECEBER E PARSE DO PACOTE
                 */
                //sem isto o tamanho da mensagem a receber é limitada ao tamanho da mensagem antes enviada
                byte[] bufferReceive = new byte[256];
                packet = new DatagramPacket(bufferReceive, bufferReceive.length);
                this.getSocket().receive(packet);
                String recvMsg = new String(packet.getData(), 0, packet.getLength());
                HashMap<String, String> msgHash = Utilitary.parseMessage(recvMsg);
                /*
                USAR AS INFORMACOES DO PACOTE
                 */
                doThings(msgHash);
            }
        } catch (SocketException se) {
            try {
                sleep(5000);
                System.out.println("Trying to Reconnect to the network...");
            } catch (InterruptedException e) {
                this.run();
            }
            this.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void doThings(HashMap<String, String> msgHash) {
        //so ler as mensagens do multicast
        if (msgHash.get("sender").startsWith("multicast")) {
            String[] ndep = msgHash.get("sender").split("-");
            this.setDepartmentId(Integer.parseInt(ndep[2]));
            try {
                if (Integer.parseInt(msgHash.get("destination").split("-")[1]) == this.voteTermId) {
                    switch (msgHash.get("message")) {
                        case "stop":
                            stopTerminal();
                            break;
                        case "true":
                            //do nothing? print something?
                            break;
                        case "identify":
                            this.available = false;
                            this.login(msgHash.get("cc"), msgHash.get("arrayList"), msgHash.get("arrayIds"), msgHash.get("election"), ndep[2]);
                            break;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
    }

    private void listInfo(String infoElectionByName, String infoElectionById) {
        String[] infoName = infoElectionByName.split("\\|"), infoID = infoElectionById.split("\\|");
        for (int i = 0; i < infoName.length; i++) {
            System.out.printf("\t(%s)- %s\n", infoID[i], infoName[i]);
        }
    }

    private void stopTerminal() {
        this.interrupt();
    }

    private void login(String cc, String infoByName, String infoById, String election, String ndep) {
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
            Scanner input = new Scanner(System.in);
            System.out.println("User: " + cc);
            System.out.print("Password: ");
            String password = String.format("%s",(cc+input.nextLine()).hashCode());
            String sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|login;username|%s;password|%s", this.getVoteTermId(), this.getDepartmentId(), "multicast", cc, password);
            byte[] buffer = sendMsg.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.getGroup(), MULTICAST_PORT);
            /*
            SEND TO MULTICAST FOR VERIFICATION
            */
            do {
                try {
                    this.getSocket().send(packet);
                    byte[] buffer2 = new byte[256];
                    packet = new DatagramPacket(buffer2, buffer2.length, this.getGroup(), MULTICAST_PORT);
                    this.getSocket().receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String recvMsg = new String(packet.getData(), 0, packet.getLength());
                msgHash = Utilitary.parseMessage(recvMsg);
                //TODO check if message is for me
            } while (!(msgHash.get("cc") != null && msgHash.get("cc").equals(cc)) || (!(msgHash.get("message").equals("logged in") || msgHash.get("message").equals("wrong password"))));
        } while (!(msgHash.get("cc") != null && msgHash.get("cc").equals(cc)) || !msgHash.get("message").equals("logged in"));
        System.out.println("Successfully Logged In");
        this.accessVotingForm(infoByName, infoById, election, cc, ndep);
    }

    private void accessVotingForm(String infoByName, String infoById, String election, String cc, String ndep) {
        Scanner input = new Scanner(System.in);
        String sendMsg;
        int command;
        System.out.println("Escolha uma lista para votar: ");
        this.listInfo(infoByName, infoById);
        System.out.print(OPTION_STRING);
        command = input.nextInt();
        sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|vote;id_candidacy|%s;id_election|%s;cc|%s;dep|%s", this.getVoteTermId(), this.getDepartmentId(), "multicast", command, election, cc, ndep);
        sendMessage(sendMsg);
        this.available = true;
    }

    public void sendMessage(String message) {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.getGroup(), MULTICAST_PORT);
        try {
            this.getSocket().send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getVoteTermId() {
        return this.voteTermId;
    }

    public int getDepartmentId() {
        return this.departmentId;
    }

    public MulticastSocket getSocket() {
        return this.socket;
    }

    public InetAddress getGroup() {
        return this.group;
    }
    public void setDepartmentId(int departmentId) {
        this.departmentId=departmentId;
    }
    public void setSocket(MulticastSocket socket) {
        this.socket = socket;
    }

    public void setGroup(InetAddress group) {
        this.group = group;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String network;
        switch (args.length){
            case 0:
                do {
                    System.out.println("Endereço Multicast (ex:224.3.2.1)");
                    System.out.print(">>> ");
                    network = input.nextLine();
                }while(!Utilitary.isIPv4(network));
                break;
            case 1:
                if(!Utilitary.isIPv4(args[0])){
                    System.out.println("arg1: Endereço invalido");
                    return;
                }
                network=args[0];
                break;
            default:
                System.out.println("Numeros de argumentos inválido");
                System.out.println("arg1: Endereço multicast");
                return;

        }
        VoteTerm client = new VoteTerm(network, MulticastServer.MULTICAST_PORT);
        System.out.printf("======== Terminal de Voto #%s ========\n", client.voteTermId);
        client.start();
    }
}
