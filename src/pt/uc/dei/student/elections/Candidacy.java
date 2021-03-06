package pt.uc.dei.student.elections;

import java.io.Serializable;

/**
 * Classe do Objeto Lista(Candidatura)
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçalves Perdigão
 */
public class Candidacy implements Serializable {
    /**
     * ID da lista
     */
    private final int id;
    /**
     * Nome da lista
     */
    private String name;
    /**
     * Tipo de lista (Estudante, Docente, Funcionario)
     */
    private String type;

    /**
     * Número de votos da lista
     */
    private int votes;

    /**
     * Percentagem do número de votos da lista
     */
    private float votes_percent;

    /**
     * Construtor do Objeto Lista(Candidatura)
     *
     * @param id   identificador da lista
     * @param name nome da lista
     * @param type tipo de lista (Estudante, Docente, Funcionario)
     */
    public Candidacy(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    /**
     * Construtor do Objeto Lista(Candidatura)
     *
     * @param id            identificador da lista
     * @param name          nome da lista
     * @param type          tipo de lista (Estudante, Docente, Funcionario)
     * @param votes         número de votos da lista
     * @param votes_percent percentagem do número de votos da lista
     */
    public Candidacy(int id, String name, String type, int votes, float votes_percent) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.votes = votes;
        this.votes_percent = votes_percent;
    }

    /**
     * Devolve uma string com informacoes relativas a uma lista
     *
     * @return string com informacoes relativas à lista
     */
    public String toString() {
        return "===========LISTA===========\n" +
                String.format("%s\t(%s)\n", this.name, this.type) +
                "---------------------------\n";
    }

    /**
     * Getter da percentagem do número de votos da lista
     *
     * @return percentagem do número de votos da lista
     */
    public float getVotes_percent() {
        return votes_percent;
    }

    /**
     * Getter do id da lista
     *
     * @return id da lista
     */
    public int getId() {
        return this.id;
    }

    /**
     * Getter do nome da lista
     *
     * @return nome da lista
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter do tipo da lista
     *
     * @return tipo da lista
     */
    public String getType() {
        return this.type;
    }

    /**
     * Getter do número de votos da lista
     *
     * @return número de votos da lisya
     */
    public int getVotes() {
        return votes;
    }

    /**
     * Setter da percentagem de votos da lista
     *
     * @param votes_percent percentagem de votos da lista
     */
    public void setVotes_percent(float votes_percent) {
        this.votes_percent = votes_percent;
    }

    /**
     * Setter do nome da lista
     *
     * @param name nome da lista
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter do tipo da lista
     *
     * @param type tipo da lista
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Setter do número de votos da lista
     *
     * @param votes número de votos da lista
     */
    public void setVotes(int votes) {
        this.votes = votes;
    }
}
