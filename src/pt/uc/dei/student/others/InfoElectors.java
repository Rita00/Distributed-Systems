package pt.uc.dei.student.others;

import java.io.Serializable;

/**
 * Classe do Objeto contendo Infomações sobre as Eleições
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçalves Perdigão
 */
public class InfoElectors implements Serializable {
    /**
     * Contagem de votos na Eleição
     */
    private int count;
    /**
     * Nome do departamento
     */
    private String dep_name;
    /**
     * Nome da eleição
     */
    private String election_title;
    /**
     * Constutor do Objeto contendo Infomações sobre as Eleições
     *
     * @param count contador de votos
     * @param dep_name nome do departamento
     * @param election_title nome da eleição
     */
    public InfoElectors(int count, String dep_name, String election_title) {
        this.count = count;
        this.dep_name = dep_name;
        this.election_title = election_title;
    }
    /**
     * Getter do numero de votos
     *
     * @return numero de votos
     */
    public int getCount() {
        return count;
    }
    /**
     * Getter do nome do departamento
     *
     * @return nome do departamento
     */
    public String getDep_name() {
        return dep_name;
    }
    /**
     * Getter do nome da eleição
     *
     * @return nome da eleição
     */
    public String getElection_title() {
        return election_title;
    }


}
