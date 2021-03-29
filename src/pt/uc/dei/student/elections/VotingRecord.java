package pt.uc.dei.student.elections;

import java.io.Serializable;
import java.util.Date;

public class VotingRecord implements Serializable {
    private final Date vote_date;
    private final String department_name;
    private final String person_name;
    private final String election_title;

    public VotingRecord(Date vote_date, String department_name, String person_name, String election_title) {
        this.vote_date = vote_date;
        this.department_name = department_name;
        this.person_name = person_name;
        this.election_title = election_title;
    }

    public Date getVote_date() {
        return vote_date;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public String getPerson_name() {
        return person_name;
    }

    public String getElection_title() {
        return election_title;
    }
}
