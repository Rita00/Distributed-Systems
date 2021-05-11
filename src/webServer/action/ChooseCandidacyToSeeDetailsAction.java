package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class ChooseCandidacyToSeeDetailsAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    int candidacy_id;
    String message = "";

    @Override
    public String execute() throws Exception {
        if (this.getHeyBean().checkIfSelectedElectionExists()) {
            this.getHeyBean().setCandidacy_id(this.candidacy_id);
            return SUCCESS;
        } else {
            message = "A eleição que escolheu não existe.";
            addActionError(message);
            return ERROR;
        }
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
