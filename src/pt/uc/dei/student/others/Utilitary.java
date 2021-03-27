package pt.uc.dei.student.others;

import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.elections.Election;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
    public static HashMap<String, String> parseMessage(String msg) {
        HashMap<String, String> hash = new HashMap<String, String>();
        String[] dividedMessage = msg.split(" ; ");
        for (String token : dividedMessage) {
            String[] keyVal = token.split(" \\| ");
            if (keyVal.length == 2) {
                hash.put(keyVal[0], keyVal[1]);
            } else {
                System.out.println("Error with tokens");
            }
        }
        return hash;
    }
    /**
     * Permite verificar se uma data segue o padrao "YYYY-M-d"
     * @param date data que necessita ser verificada
     * @return data que foi verificada ou null em caso de erro
     * @throws DateTimeParseException excecao caso a data seja invalida
     */
    public static LocalDate parseDate(String date) {
        LocalDate d;
        try {
            d = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-d"));
        } catch (DateTimeParseException e) {
            return null;
        }
        return d;
    }
    public static void listDepart(ArrayList<Department> departments) {
        if (departments.size() == 0) System.out.println("");
        for (Department dep : departments) {
            System.out.printf("\t(%d)- %s%n", dep.getId(), dep.getName());
        }
    }

    public static void listElections(ArrayList<Election> elections) {
        if (elections.size() != 0) {
            for (Election e : elections) {
                System.out.printf("\t(%s)- %s\n", elections.indexOf(e) + 1, e.getTitle());
            }
        } else {
            System.out.println("Não existem eleições\n");
        }
    }
}
