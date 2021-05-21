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
     * Contagem de votos dos estudantes na Eleição
     */
    private int count_estudante;
    /**
     * Contagem de votos dos docentes na Eleição
     */
    private int count_docente;
    /**
     * Contagem de votos dos funcionários na Eleição
     */
    private int count_funcionario;
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
     * Constutor do Objeto contendo Infomações sobre as Eleições diferenciação dos votos
     *
     * @param count contador de votos
     * @param count_estudante contador de votos dos estudantes
     * @param count_docente contador de votos dos docentes
     * @param count_funcionario contador de votos dos funcionários
     * @param dep_name nome do departamento
     * @param election_title nome da eleição
     */
    public InfoElectors(int count,int count_estudante, int count_docente, int count_funcionario,String dep_name, String election_title) {
        this.count = count;
        this.count_estudante = count_estudante;
        this.count_docente = count_docente;
        this.count_funcionario = count_funcionario;
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
    /**
     * Getter da quantidade de votos de estudantes
     *
     * @return quantidade de votos
     */
    public int getCount_estudante() {
        return count_estudante;
    }
    /**
     * Getter da quantidade de votos de docentes
     *
     * @return quantidade de votos
     */
    public int getCount_docente() {
        return count_docente;
    }
    /**
     * Getter da quantidade de votos de funcionarios
     *
     * @return quantidade de votos
     */
    public int getCount_funcionario() {
        return count_funcionario;
    }
}
