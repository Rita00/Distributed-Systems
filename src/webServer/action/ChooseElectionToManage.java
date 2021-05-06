package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class ChooseElectionToManage extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    int election_to_manage;
    String message = "";

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setElection_to_manage(this.election_to_manage);
        if (this.getHeyBean().checkIfSelectedElectionExists()) {
            return SUCCESS;
        } else {
            message = "A eleição que escolheu não existe.";
            addActionError(message);
            return ERROR;
        }
    }

    public int getElection_to_manage() {
        return election_to_manage;
    }

    public void setElection_to_manage(int election_to_manage) {
        this.election_to_manage = election_to_manage;
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
