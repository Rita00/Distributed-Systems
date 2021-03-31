package pt.uc.dei.student.others;

import java.io.Serializable;

/**
 * Classe do Objeto Infomação
 *
 * @author Dylan Gonçãoves Perdigão
 * @author Ana Rita Rodrigues
 */
public class InfoElectors implements Serializable {
    private int count;
    private String dep_name;
    private String election_title;

    public InfoElectors(int count, String dep_name, String election_title) {
        this.count = count;
        this.dep_name = dep_name;
        this.election_title = election_title;
    }
    public int getCount() {
        return count;
    }
    public String getDep_name() {
        return dep_name;
    }
    public String getElection_title() {
        return election_title;
    }


}
