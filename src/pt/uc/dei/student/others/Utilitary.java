package pt.uc.dei.student.others;

import java.util.HashMap;

/**
 * Classe de Metodos Utilitarios
 *
 * @author Dylan Gonçãoves Perdigão
 * @author Ana Rita Rodrigues
 */
public class Utilitary {
    /**
     * Permite decompor uma mensagem(string) enviada pelo protocolo Multicast de
     * modo a ficar armazenada numa HashMap.<br>
     * O " ; " permite separar os pares valor/chave.<br>
     * O " | " permite separar os valores das chaves.
     * Se ao separar as chaves dos valores, o tamanho do array resultante é
     * superior a dois então escreve uma mensagem de erro e nao guarda o par
     * @param msg mensagem enviada por multicast pelo Servidor multicast ou
     *            pelo terminal de voto
     * @return Uma HashMap com as informacoes da mensagem
     * @see pt.uc.dei.student.MulticastServer Servidor Multicast
     * @see pt.uc.dei.student.VoteTerm Terminal de Voto
     */
    public static HashMap<String,String> parseMessage(String msg){
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
}
