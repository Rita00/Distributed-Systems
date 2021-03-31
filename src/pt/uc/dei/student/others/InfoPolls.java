package pt.uc.dei.student.others;

import java.io.Serializable;

public class InfoPolls implements Serializable {
    private String dep_name;
    private int statusPoll;
    private int terminal_id;
    private int statusTerminal;

    public String getDep_name() {
        return dep_name;
    }

    public int getStatusPoll() {
        return statusPoll;
    }

    public int getTerminal_id() {
        return terminal_id;
    }

    public int getStatusTerminal() {
        return statusTerminal;
    }

    public InfoPolls(String dep_name, int statusPoll, int terminal_id, int statusTerminal) {
        this.dep_name = dep_name;
        this.statusPoll = statusPoll;
        this.terminal_id = terminal_id;
        this.statusTerminal = statusTerminal;
    }
}
