package pt.uc.dei.student.others;

import pt.uc.dei.student.elections.Candidacy;
import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.elections.Election;
import pt.uc.dei.student.elections.Person;

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
     * O ";" permite separar os pares valor/chave.<br>
     * O "|" permite separar os valores das chaves.
     * Se ao separar as chaves dos valores, o tamanho do array resultante é
     * superior a dois então escreve uma mensagem de erro e nao guarda o par
     *
     * @param msg mensagem enviada por multicast pelo Servidor multicast ou
     *            pelo terminal de voto
     * @return Uma HashMap com as informacoes da mensagem
     * @see pt.uc.dei.student.MulticastServer Servidor Multicast
     * @see pt.uc.dei.student.VoteTerm Terminal de Voto
     */
    public static HashMap<String, String> parseMessage(String msg) {
        HashMap<String, String> hash = new HashMap<String, String>();
        String[] dividedMessage = msg.split(";");
        for (String token : dividedMessage) {
            String[] keyVal = token.split("\\|");
            if (keyVal.length == 2) {
                hash.put(keyVal[0], keyVal[1]);
            } else if (keyVal.length == 0) {
                System.out.println("Wrong token.");
            }
            else {
                StringBuilder list = new StringBuilder();
                for (int i = 1; i < keyVal.length; i++) {
                    list.append(keyVal[i]);
                    list.append("|");
                }
                list.deleteCharAt(list.length() - 1);
                hash.put(keyVal[0], list.toString());
            }
        }
        return hash;
    }

    /**
     * Permite verificar se uma data segue o padrao "YYYY-M-d"
     *
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
        if (departments.size() == 0) System.out.println("Sem departamentos.");
        else {
            for (Department dep : departments) {
                System.out.printf("\t(%d)- %s%n", dep.getId(), dep.getName());
            }
        }
    }

    public static void listElections(ArrayList<Election> elections) {
        if (elections.size() != 0) {
            for (Election e : elections) {
                System.out.printf("\t(%d)- %s\n", e.getId(), e.getTitle());
            }
        } else {
            System.out.println("Não existem eleições\n");
        }
    }

    public static void listPerson(ArrayList<Person> people) {
        if (people.size() != 0) {
            for (Person p : people) {
                System.out.printf("\t(%s)- %s (%s)\n", people.indexOf(p) + 1, p.getName(), p.getCensoredCc_number(4));
            }
        }
    }

    public static void listCandidacy(ArrayList<Candidacy> candidacies) {
        for (Candidacy c : candidacies) {
            System.out.printf("\t(%s)- %s%n", c.getId(), c.getName());
        }
    }

    /**
     * Função para decidir em formato String o cargo da pessoa
     * Usado para proteção de dados
     *
     * @param cargo Inteiro escolhido pela pessoa para representar o seu cargo
     * @return String que corresponde ao seu cargo
     */
    public static String decideCargo(int cargo) {
        switch (cargo) {
            case 1:
                return "Estudante";
            case 2:
                return "Docente";
            case 3:
                return "Funcionário";
            default:
                return null;
        }
    }

    public static boolean hasCandidacy(int candidacy, ArrayList<Candidacy> candidacies) {
        for (Candidacy c : candidacies) {
            if (c.getId() == candidacy) return true;
        }
        return false;
    }
}
