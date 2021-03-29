package pt.uc.dei.student;

import java.io.Serializable;

public class InfoElectors implements Serializable {
    public int count;
    public String dep_name;
    public String election_title;

    public InfoElectors(int count, String dep_name, String election_title) {
        this.count = count;
        this.dep_name = dep_name;
        this.election_title = election_title;
    }
}
