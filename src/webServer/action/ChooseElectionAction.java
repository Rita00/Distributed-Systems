package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import pt.uc.dei.student.elections.Election;
import webServer.model.HeyBean;

import java.util.Map;

public class ChooseElectionAction extends ActionSupport implements SessionAware {
    private int election_id;
    private Map<String, Object> session;
    private String message = "";

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setElection_id(this.election_id);
        if (session.get("loggedin") != null) {
            boolean res = (Boolean) session.get("loggedin");
            if (!res) {
                message = "Não tem sessão iniciada.";
                addActionError(message);
                return ERROR;
            } else {
                if (!this.getHeyBean().checkIfAlreadyVotes() && this.election_id != 0 && this.getHeyBean().getCandidacies() != null)
                    return SUCCESS;
                else {
                    message = "Já votou nesta eleição!";
                    addActionError(message);
                    return ERROR;
                }
            }
        } else {
            message = "Não tem sessão iniciada.";
            addActionError(message);
            return ERROR;
        }
    }

    public int getElection_id() {
        return election_id;
    }

    public void setElection_id(int election_id) {
        this.election_id = election_id;
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
