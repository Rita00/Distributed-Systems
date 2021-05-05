package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import pt.uc.dei.student.elections.Election;
import webServer.model.HeyBean;

import java.util.Map;

public class VoteAction extends ActionSupport implements SessionAware {
    private int election_id;
    private Map<String, Object> session;

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setElection_id(this.election_id);
        if(this.getHeyBean().getCandidacies() != null)
            return SUCCESS;
        else
            return ERROR;
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
        if(!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }
}
