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

    VoteTerm(int departmentId ,String multicastAddress, int multicastPort){
        this.voteTermId=(int)Math.round(Math.random()*Integer.MAX_VALUE)+1;
        this.departmentId=departmentId;
    	this.MULTICAST_ADDRESS=multicastAddress;
    	this.MULTICAST_PORT=multicastPort;
    }

    public void run() {
        try (MulticastSocket socket = new MulticastSocket(this.MULTICAST_PORT)) {
            InetAddress group = InetAddress.getByName(this.MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                String sendMsg = String.format("sender | term-%s-%s ; destination | %s ; message | i'm a voteTerm", this.getVoteTermId(), this.getDepartmentId(),1);
                byte[] buffer = sendMsg.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length,group, MULTICAST_PORT);
                socket.send(packet);
                /*
                RECEBER E PARSE DO PACOTE
                 */
                socket.receive(packet);
                String recvMsg = new String(packet.getData(), 0, packet.getLength());
                HashMap<String, String> msgHash = Utilitary.parseMessage(recvMsg);
                /*
                USAR A INFORMACOES DO PACOTE
                 */
                doThings(msgHash);
                sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void doThings(HashMap<String, String> msgHash) {
        //so ler as mensagens do multicast
        if(msgHash.get("sender").startsWith("multicast")){
            System.out.println(msgHash.get("message"));
        }
    }



    public int getVoteTermId(){return this.voteTermId;}
    public int getDepartmentId(){return this.departmentId;}

    public static void main(String[] args) {
        int departmentId = 1;
        VoteTerm client = new VoteTerm(departmentId,"224.3.2.1",MulticastServer.MULTICAST_PORT);
        client.start();
    }
}
