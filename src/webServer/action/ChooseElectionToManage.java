package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class ChooseElectionToManage extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    int election_id;
    String election_title, election_type, election_description, iniDate, fimDate;
    String message = "";

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setElection_id(this.election_id);
        if (this.getHeyBean().checkIfSelectedElectionExists()) {
            this.getHeyBean().setTitle(this.election_title);
            this.getHeyBean().setType(this.election_type);
            this.getHeyBean().setDescription(this.election_description);
            this.getHeyBean().setIniDate(this.iniDate);
            this.getHeyBean().setFimDate(this.fimDate);
            return SUCCESS;
        } else {
            message = "A eleição que escolheu não existe.";
            addActionError(message);
            return ERROR;
        }
    }

    public int getElection_id() {
        return election_id;
    }

    public String getElection_title() {
        return election_title;
    }

    public String getElection_type() {
        return election_type;
    }

    public String getElection_description() {
        return election_description;
    }

    public String getIniDate() {
        return iniDate;
    }

    public String getFimDate() {
        return fimDate;
    }

    public void setElection_id(int election_id) {
        this.election_id = election_id;
    }

    public void setElection_title(String election_title) {
        this.election_title = election_title;
    }

    public void setElection_type(String election_type) {
        this.election_type = election_type;
    }

    public void setElection_description(String election_description) {
        this.election_description = election_description;
    }

    public void setIniDate(String iniDate) {
        this.iniDate = iniDate;
    }

    public void setFimDate(String fimDate) {
        this.fimDate = fimDate;
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
