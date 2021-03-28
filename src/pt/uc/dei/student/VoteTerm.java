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
    private boolean available = true;

    VoteTerm(int departmentId, String multicastAddress, int multicastPort) {
        this.voteTermId = (int) Math.round(Math.random() * Integer.MAX_VALUE) + 1;
        this.departmentId = departmentId;
        this.MULTICAST_ADDRESS = multicastAddress;
        this.MULTICAST_PORT = multicastPort;
    }

    public void run() {
        // TODO faz identifação apenas uma vez ou se der timeout à espera de resposta
        try (MulticastSocket socket = new MulticastSocket(this.MULTICAST_PORT)) {
            InetAddress group = InetAddress.getByName(this.MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                String sendMsg;
                if (available) {
                    sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|available", this.getVoteTermId(), this.getDepartmentId(), "multicast");
                } else {
                    sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|occupied", this.getVoteTermId(), this.getDepartmentId(), "multicast");
                }
                byte[] buffer = sendMsg.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MULTICAST_PORT);
                socket.send(packet);
                /*
                RECEBER E PARSE DO PACOTE
                 */
                //sem isto o tamanho da mensagem a receber é limitada ao tamanho da mensagem antes enviada
                byte[] bufferReceive = new byte[256];
                packet = new DatagramPacket(bufferReceive, bufferReceive.length);
                socket.receive(packet);
                String recvMsg = new String(packet.getData(), 0, packet.getLength());
                HashMap<String, String> msgHash = Utilitary.parseMessage(recvMsg);
                /*
                USAR A INFORMACOES DO PACOTE
                 */
                doThings(msgHash, socket, group);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void doThings(HashMap<String, String> msgHash, MulticastSocket socket, InetAddress group) {
        //so ler as mensagens do multicast
        if (msgHash.get("sender").startsWith("multicast")) {
            if(Integer.parseInt(msgHash.get("destination").split("-")[1]) == this.voteTermId){
                switch (msgHash.get("message")) {
                    case "stop":
                        stopTerminal();
                        break;
                    case "true":
                        //do nothing? print something?
                        break;
                    case "identify":
                        System.out.println("okokok");
                        break;
                }
            }
        }
    }

    private void stopTerminal() {
        this.interrupt();
    }

    private void login(MulticastSocket socket, InetAddress group) {
        HashMap<String, String> msgHash;
        boolean isFirstAttempt = true;
        do {
            if (!isFirstAttempt) {
                System.out.print("Wrong username or password");
            }
            isFirstAttempt = false;
            /*
            GET USERNAME PASSWORD
             */
            Scanner input = new Scanner(System.in);
            System.out.print("Número de cartão de cidadão: ");
            String username = input.nextLine();
            System.out.print("Password: ");
            String password = input.nextLine();
            String sendMsg = String.format("sender|voteterm-%s-%s;destination|%s;message|login;username|%s;password|%s", this.getVoteTermId(), this.getDepartmentId(), "multicast", username, password);
            byte[] buffer = sendMsg.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MULTICAST_PORT);
            /*
            SEND TO MULTICAST FOR VERIFICATION
            */
            try {
                socket.send(packet);
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String recvMsg = new String(packet.getData(), 0, packet.getLength());
            msgHash = Utilitary.parseMessage(recvMsg);
        } while (msgHash.get("message") != "false");
        System.out.print("Successfully Logged In");
        this.accessVotingForm();
    }

    private void accessVotingForm() {
        //TODO;
    }

    public int getVoteTermId() {
        return this.voteTermId;
    }

    public int getDepartmentId() {
        return this.departmentId;
    }

    public static void main(String[] args) {
        int departmentId = 1;
        VoteTerm client = new VoteTerm(departmentId, "224.3.2.1", MulticastServer.MULTICAST_PORT);
        System.out.printf("======== Terminal de Voto #%s ========\n", client.voteTermId);
        client.start();
    }
}
