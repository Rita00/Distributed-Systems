package pt.uc.dei.student.elections;

import java.io.Serializable;

/**
 * Classe do Objeto Lista(Candidatura)
 *
 * @author Dylan Gonçãoves Perdigão
 * @author Ana Rita Rodrigues
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
     * Construtor do Objeto Lista(Candidatura)
     *
     * @param id identificador da lista
     * @param name nome da lista
     * @param type tipo de lista (Estudante, Docente, Funcionario)
     */
    public Candidacy(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
    /**
     * Devolve uma string com informacoes relativas a uma lista
     *
     * @return string com informacoes relativas à lista
     */
    public String toString() {
        return  "===========LISTA===========\n" +
                String.format("%s\t(%s)\n", this.name, this.type) +
                "---------------------------\n";
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
}
