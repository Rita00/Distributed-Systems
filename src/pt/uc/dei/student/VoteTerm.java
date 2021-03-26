package pt.uc.dei.student;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import pt.uc.dei.student.elections.Department;

/**
 * Clientes do Multicast Server (Terminais de Voto)
 */
public class VoteTerm extends Thread {
    private final int MULTICAST_PORT;
    private final String MULTICAST_ADDRESS;

    private int voteTermId;
    private int departmentId;

    VoteTerm(int voteTermId, int departmentId ,String multicastAddress, int multicastPort){
        this.voteTermId=voteTermId;
        this.departmentId=departmentId;
    	this.MULTICAST_ADDRESS=multicastAddress;
    	this.MULTICAST_PORT=multicastPort;
    }

    public void run() {
        try (MulticastSocket socket = new MulticastSocket(this.MULTICAST_PORT)) {
            InetAddress group = InetAddress.getByName(this.MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                String sendMsg = String.format("sender | %s ; department | %s ; message | i'm a voteTerm", this.getVoteTermId(), this.getDepartmentId());
                byte[] buffer = sendMsg.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length,group, MULTICAST_PORT);
                socket.send(packet);
                /*
                RECEBER E PARSE DO PACOTE
                 */
                socket.receive(packet);
                String recvMsg = new String(packet.getData(), 0, packet.getLength());
                HashMap<String, String> msgHash = this.parseMessage(recvMsg);
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

    private HashMap<String,String> parseMessage(String msg){
        HashMap<String,String> hash = new HashMap<String,String>();
        String[] dividedMessage = msg.split(" ; ");
        for(String token : dividedMessage){
            String[] keyVal = token.split(" \\| ");
            if(keyVal.length == 2){
                hash.put(keyVal[0], keyVal[1]);
            }else{
                System.out.println("Error with tokens");
            }
        }
        return hash;
    }

    public int getVoteTermId(){return this.voteTermId;}
    public int getDepartmentId(){return this.departmentId;}

    public static void main(String[] args) {
        int id = 123;
        int departmentId = 1;
        VoteTerm client = new VoteTerm(id,departmentId,"224.3.2.1",MulticastServer.MULTICAST_PORT);
        client.start();
    }
}
