package pt.uc.dei.student;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.others.Utilitary;

import static pt.uc.dei.student.others.Utilitary.parseMessage;

/**
 * Clientes do Multicast Server (Terminais de Voto)
 */
public class VoteTerm extends Thread {
    private final int MULTICAST_PORT;
    private final String MULTICAST_ADDRESS;

    private int voteTermId;
    private int departmentId;
    private MulticastSocket socket;
    private InetAddress group;
    private boolean available = true;

    VoteTerm(int departmentId, String multicastAddress, int multicastPort) {
        this.voteTermId = (int) Math.round(Math.random() * Integer.MAX_VALUE) + 1;
        this.departmentId = departmentId;
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
                this.send(sendMsg);
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
                USAR A INFORMACOES DO PACOTE
                 */
                doThings(msgHash);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(String message) {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.getGroup(), MULTICAST_PORT);
        try {
            this.getSocket().send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doThings(HashMap<String, String> msgHash) {
        //so ler as mensagens do multicast
        if (msgHash.get("sender").startsWith("multicast")) {
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
                            this.login(msgHash.get("cc"));
                            break;
                    }
                }
            }catch(ArrayIndexOutOfBoundsException e){}
        }
    }

    private void stopTerminal() {
        this.interrupt();
    }

    private void login(String cc) {
        HashMap<String, String> msgHash;
        byte[] bufferReceive = new byte[256];
        DatagramPacket packet = new DatagramPacket(bufferReceive, bufferReceive.length);
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
            System.out.println("User: "+cc);
            //System.out.print("Número de cartão de cidadão: ");
            //String username = input.nextLine();
            System.out.print("Password: ");
            String password = input.nextLine();
            String sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|login;username|%s;password|%s", this.getVoteTermId(), this.getDepartmentId(), "multicast", cc, password);
            /*
            SEND TO MULTICAST FOR VERIFICATION
            */
            do {
                try {
                    this.send(sendMsg);
                    this.getSocket().receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String recvMsg = new String(packet.getData(), 0, packet.getLength());
                msgHash = Utilitary.parseMessage(recvMsg);
            }while(!(msgHash.get("message").equals("logged in") || msgHash.get("message").equals("wrong password")));
        } while (!msgHash.get("message").equals("logged in"));
        System.out.println("Successfully Logged In");
        this.accessVotingForm(msgHash.get("election_id"));
    }

    private void accessVotingForm(String election_id) {
        //TODO
        HashMap<String, String> msgHash;
        byte[] bufferReceive = new byte[2048];
        DatagramPacket packet = new DatagramPacket(bufferReceive, bufferReceive.length);
        System.out.println("PODES VOTAR");
        try {
            //envia confirmacao de login
            this.send("logged in");
            //recebe listas para votar
            this.getSocket().receive(packet);
            String recvMsg = new String(packet.getData(), 0, packet.getLength());
            msgHash = Utilitary.parseMessage(recvMsg);
            System.out.println(msgHash.get("message"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getVoteTermId() { return this.voteTermId; }
    public int getDepartmentId() { return this.departmentId; }
    public MulticastSocket getSocket() { return this.socket; }
    public InetAddress getGroup() { return this.group; }

    public void setSocket(MulticastSocket socket) { this.socket=socket; }
    public void setGroup(InetAddress group) { this.group=group; }

    public static void main(String[] args) {
        int departmentId = 1;
        VoteTerm client = new VoteTerm(departmentId, "224.3.2.1", MulticastServer.MULTICAST_PORT);
        System.out.printf("======== Terminal de Voto #%s ========\n", client.voteTermId);
        client.start();
    }
}
