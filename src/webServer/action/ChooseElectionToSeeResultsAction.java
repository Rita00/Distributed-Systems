package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class ChooseElectionToSeeResultsAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    int election_id, null_votes, blank_votes;
    String election_title;
    String message = "";

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setTitle(this.election_title);
        this.getHeyBean().setElection_id(this.election_id);
        if (!this.getHeyBean().checkIfSelectedElectionExists()) {
            message = "Esta eleição não existe!";
            addActionError(message);
            return ERROR;
        } else {
            this.getHeyBean().setNull_vote(this.null_votes);
            this.getHeyBean().setBlank_vote(this.blank_votes);
        }
        return SUCCESS;
    }

    public int getNull_votes() {
        return null_votes;
    }

    public int getBlank_votes() {
        return blank_votes;
    }

    public int getElection_id() {
        return election_id;
    }

    public String getElection_title() {
        return election_title;
    }

    public void setElection_id(int election_id) {
        this.election_id = election_id;
    }

    public void setNull_votes(int null_votes) {
        this.null_votes = null_votes;
    }

    public void setBlank_votes(int blank_votes) {
        this.blank_votes = blank_votes;
    }

    public void setElection_title(String election_title) {
        this.election_title = election_title;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public HeyBean getHeyBean() {
        if (!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }
}
