package pt.uc.dei.student.elections;

import java.io.Serializable;
import java.util.Date;
/**
 * Classe do Objeto Registo de Votos
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçãoves Perdigão
 */
public class VotingRecord implements Serializable {
    /**
     * Data do Voto
     */
    private final Date vote_date;
    /**
     * Nome do departamento onde o voto foi realizado
     */
    private final String department_name;
    /**
     * Nome da pessoa que efetuou o voto
     */
    private final String person_name;
    /**
     * Nome da eleição em que a pessoa realizou o voto
     */
    private final String election_title;
    /**
     * Construtor do Objeto Registo de Votos
     *
     * @param vote_date Data do voto
     * @param department_name Nome do departamento onde se realizou o voto
     * @param person_name Nome da pessoa que relizou o voto
     * @param election_title Nome da eleição onde a pessoa votou
     */
    public VotingRecord(Date vote_date, String department_name, String person_name, String election_title) {
        this.vote_date = vote_date;
        this.department_name = department_name;
        this.person_name = person_name;
        this.election_title = election_title;
    }
    /**
     * Getter da data do voto
     *
     * @return Data do voto
     */
    public Date getVote_date() { return vote_date; }
    /**
     * Getter do nome do departamento
     *
     * @return Nome do departamento
     */
    public String getDepartment_name() { return department_name; }
    /**
     * Getter do nome da pessoa
     *
     * @return Nome da pessoa
     */
    public String getPerson_name() { return person_name; }
    /**
     * Getter do nome da eleição
     *
     * @return Nome da eleição
     */
    public String getElection_title() { return election_title; }
}
