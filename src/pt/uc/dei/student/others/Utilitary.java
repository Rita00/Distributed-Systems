package pt.uc.dei.student.others;

import pt.uc.dei.student.elections.Candidacy;
import pt.uc.dei.student.elections.Department;
import pt.uc.dei.student.elections.Election;
import pt.uc.dei.student.elections.Person;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Classe de Metodos Utilitarios
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçalves Perdigão
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
        HashMap<String, String> hash = new HashMap<>();
        String[] dividedMessage = msg.split(";");
        for (String token : dividedMessage) {
            String[] keyVal = token.split("\\|");
            if (keyVal.length == 2) {
                hash.put(keyVal[0], keyVal[1]);
            } else if (keyVal.length == 0) {
                System.out.println("Wrong token.");
            } else {
                StringBuilder list = new StringBuilder();
                for (int i = 1; i < keyVal.length; i++) {
                    list.append(keyVal[i]);
                    list.append("|");
                }
                try {
                    list.deleteCharAt(list.length() - 1);
                }catch(StringIndexOutOfBoundsException ignore){}
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
    /**
     * Efetua a listagem dos departamentos pelo seu ID
     *
     * @param departments ArrayList dos departamentos
     * @see Department
     */
    public static void listDepart(ArrayList<Department> departments) {
        if (departments.size() == 0) System.out.println("Sem departamentos.");
        else {
            for (Department dep : departments) {
                System.out.printf("\t(%d)- %s%n", dep.getId(), dep.getName());
            }
        }
    }
    /**
     * Efetua a listagem das eleições pelo seu ID
     *
     * @param elections ArrayList das Eleições
     * @see Election
     */
    public static void listElections(ArrayList<Election> elections) {
        if (elections.size() != 0) {
            for (Election e : elections) {
                System.out.printf("\t(%d)- %s\n", e.getId(), e.getTitle());
            }
        } else {
            System.out.println("Não existem eleições\n");
        }
    }
    /**
     * Efetua a listagem das eleições pelo indice do ArrayList
     *
     * @param elections ArrayList das Eleições
     * @see Election
     */
    public static void listElectionsByIndex(ArrayList<Election> elections) {
        if (elections.size() != 0) {
            for (Election e : elections) {
                System.out.printf("\t(%d)- %s\n", elections.indexOf(e)+1, e.getTitle());
            }
        } else {
            System.out.println("Não existem eleições\n");
        }
    }
    /**
     * Efetua listagem das pessoas pelo seu indice do ArrayList
     * @param people ArrayList com as pessoas
     * @see Person
     */
    public static void listPerson(ArrayList<Person> people) {
        if (people.size() != 0) {
            for (Person p : people) {
                System.out.printf("\t(%s)- %s (%s)\n", people.indexOf(p) + 1, p.getName(), p.getCensoredCc_number(4));
            }
        }
    }
    /**
     * Efetua listagem das listas pelo seu ID
     * @param candidacies ArrayList com as listas
     * @see Candidacy
     */
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
    /**
     * Percorre o ArrayList das listas para verificar
     * se o ID de entrada pertence a alguma das listas
     *
     * @param candidacy ID da lista
     * @param candidacies ArrayList com as listas
     * @return true se pertencer, false caso contrário
     * @see Candidacy
     */
    public static boolean hasCandidacy(int candidacy, ArrayList<Candidacy> candidacies) {
        for (Candidacy c : candidacies) {
            if (c.getId() == candidacy) return true;
        }
        return false;
    }
    /**
     * Percorre o ArrayList das eleições para verificar
     * se o ID de entrada pertence a alguma das eleições
     *
     * @param election ID da eleição
     * @param elections ArrayList com as eleições
     * @return true se pertencer, false caso contrário
     * @see Election
     */
    public static boolean hasElection(int election, ArrayList<Election> elections) {
        for (Election ele : elections) {
            if (ele.getId() == election) return true;
        }
        return false;
    }

    public static boolean hasDep(int id, ArrayList<Department> departments) {
        for (Department dep : departments) {
            if (dep.getId() == id) return true;
        }
        return false;
    }


    /**
     * Verifica se uma string é um número
     *
     * @param n string com o numero
     * @return true se é um número, false caso contário
     */
    public static boolean isNumber(String n) {
        try {
            Integer.parseInt(n);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    /**
     * Verifica se o numero de telemóvel tem a formatação correta:
     * 9 digitos;
     * primeiro numero tem de ser 9;
     * segundo numero tem de ser 1, 2, 3, 6;
     *
     * @param num_phone numero de telemóvel
     * @return true se tem a formatação correta, false caso contrário
     */
    public static boolean checkCorrectPhone(int num_phone) {
        int length = (int) (Math.log10(num_phone) + 1);
        int secondNum = num_phone / 10000000;
        return length == 9 && ((secondNum % 10) == 2 || (secondNum % 10) == 3 || (secondNum % 10) == 6 || (secondNum % 10) == 1) && secondNum / 10 == 9;
    }
    /**
     * Verifica se o numero de cartão de cidadão tem 8 dígitos
     *
     * @param cc_number numero de cartão de cidadão
     * @return true se tiver 8 digitos, false caso contrário
     */
    public static boolean checkCorrectCCNumber(int cc_number) {
        int length = (int) (Math.log10(cc_number) + 1);
        return length == 8;
    }
    /**
     * Verifica se uma string tem o format de IPv4
     *
     * @param ip string IPv4
     * @return true se é um IPv4, false caso contrário
     */
    public static boolean isIPv4(String ip) {
        Pattern IPv4_PATTERN = Pattern.compile("^([0-9]?[0-9]?[0-9][.]){3}([0-9]?[0-9]?[0-9])$");
        return IPv4_PATTERN.matcher(ip).matches();
    }
    /**
     * Prepara uma string para poder ser enviada por multicast,
     * são retirados todos os caracteres pipe ("|") e ponto-virgula (";")
     *
     * @param original string original
     * @return string tratada
     */
    public static String prepareForMulticast(String original){
        String prepared = original.replace(";"," ");
        return prepared.replace("|", " ");
    }

    /**
     * Transforma a password na encriptação do número de cartão de cidadão com a password original
     *
     * @param username número de cartão de cidadão
     * @param password password original
     * @return password encriptada
     */
    public static String getPasswordHash(String username, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            password = new String(digest.digest((username + password).getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
        }
        return password;
    }
}
