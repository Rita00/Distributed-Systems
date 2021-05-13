package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class ChooseCandidacyToSeeDetailsAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    int candidacy_id;
    String candidacy_name, candidacy_type;
    String message = "";

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setCandidacy_id(this.candidacy_id);
        if (this.getHeyBean().checkIfSelectedElectionExists()) {
            if (this.getHeyBean().checkSelectedCandidacy_Election()) {
                this.getHeyBean().setCandidacy_name(this.candidacy_name);
                this.getHeyBean().setCandidacy_type(this.candidacy_type);
                return SUCCESS;
            } else {
                message = "Esta lista não existe!";
                addActionError(message);
                return "error_candidacy";
            }
        } else {
            message = "A eleição que escolheu não existe.";
            addActionError(message);
            return ERROR;
        }
    }

    public int getCandidacy_id() {
        return candidacy_id;
    }

    public String getCandidacy_name() {
        return candidacy_name;
    }

    public String getCandidacy_type() {
        return candidacy_type;
    }

    public void setCandidacy_id(int candidacy_id) {
        this.candidacy_id = candidacy_id;
    }

    public void setCandidacy_name(String candidacy_name) {
        this.candidacy_name = candidacy_name;
    }

    public void setCandidacy_type(String candidacy_type) {
        this.candidacy_type = candidacy_type;
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
