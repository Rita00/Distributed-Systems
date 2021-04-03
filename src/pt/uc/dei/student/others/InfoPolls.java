package pt.uc.dei.student.others;

import java.io.Serializable;

/**
 * Classe do Objeto contendo Infomações sobre as mesas de voto
 *
 * @author Ana Rita Rodrigues
 * @author Dylan Gonçãoves Perdigão
 */
public class InfoPolls implements Serializable {
    /**
     * Nome do departamento
     */
    private String dep_name;
    /**
     * Estado da mesa de voto
     */
    private int statusPoll;
    /**
     * ID do terminal voto
     */
    private int terminal_id;
    /**
     * Estado do terminal de voto
     */
    private int statusTerminal;
    /**
     * Constutor do Objeto contendo Infomações sobre as mesas de voto
     *
     * @param dep_name nome do departamento
     * @param statusPoll estado da mesa de voto
     * @param terminal_id ID do terminal de voto
     * @param statusTerminal estado do terminal de voto
     */
    public InfoPolls(String dep_name, int statusPoll, int terminal_id, int statusTerminal) {
        this.dep_name = dep_name;
        this.statusPoll = statusPoll;
        this.terminal_id = terminal_id;
        this.statusTerminal = statusTerminal;
    }
    /**
     * Getter do nome do departamento
     * @return nome do departamento
     */
    public String getDep_name() {
        return dep_name;
    }
    /**
     * Getter do estado da mesa de voto
     * @return estado da mesa de voto
     */
    public int getStatusPoll() {
        return statusPoll;
    }
    /**
     * Getter do ID do terminal de voto
     * @return ID do terminal de voto
     */
    public int getTerminal_id() {
        return terminal_id;
    }
    /**
     * Getter do estado do terminal de voto
     * @return estado do terminal de voto
     */
    public int getStatusTerminal() {
        return statusTerminal;
    }
}
