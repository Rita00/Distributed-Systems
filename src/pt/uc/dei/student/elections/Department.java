package pt.uc.dei.student.elections;

import java.io.Serializable;

/**
 * Classe do Objeto Departamento
 *
 * @author Dylan Gonçãoves Perdigão
 * @author Ana Rita Rodrigues
 */
public class Department implements Serializable {
    /**
     * ID do departamento
     */
    private final int id;
    /**
     * Nome do departamento
     */
    private final String name;
    /**
     * Construtor do Objeto Departamento
     *
     * @param id identificador da departamento
     * @param name nome do departamento
     */
    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }
    /**
     * Getter do id do departamento
     *
     * @return id do departamento
     */
    public int getId() { return id; }
    /**
     * Getter do nome do departamento
     *
     * @return nome do departamento
     */
    public String getName() {
        return name;
    }
}
