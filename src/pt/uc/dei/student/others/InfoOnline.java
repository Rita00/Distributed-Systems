package pt.uc.dei.student.others;

import java.io.Serializable;
/**
 * Classe do Objeto contendo Infomações sobre as pessoas
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçalves Perdigão
 */
public class InfoOnline implements Serializable {
    /**
     * ID da pessoa
     */
    private int id;
    /**
     * Nome da pessoa
     */
    private String name;
    /**
     * Nome da mesa de voto onde está o eleitor
     */
    private String dep;

    /**
     * Construtor das informações da pessoa
     * @param id id da pessoa
     * @param name nome da pessoa
     * @param dep nome da mesa de voto
     */
    public InfoOnline(int id, String name, String dep) {
        this.setId(id);
        this.name = name;
        this.dep = dep;
    }

    /**
     * Getter do ID da pessoa
     * @return ID da pessoa
     */
    public int getId() {
        return id;
    }
    /**
     * Setter do ID da pessoa
     * @param id ID da pessoa
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter do nome da pessoa
     * @return nome da pessoa
     */
    public String getName() {
        return name;
    }
    /**
     * Setter do nome da pessoa
     * @param name nome da pessoa
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Getter do nome da mesa de voto onde está o eleitor
     * @return ID da mesa de voto
     */
    public String getDep() {
        return dep;
    }
    /**
     * Setter do nome da mesa de voto
     * @param dep Nome da mesa de voto
     */
    public void setDep(String dep) {
        this.dep = dep;
    }
}
