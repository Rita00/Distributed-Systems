package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class CreateElectionAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    String title, description, type, iniDate, fimDate, restriction;
    int dep;

    private String message = "";

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setTitle(this.title);
        this.getHeyBean().setDescription(this.description);
        this.getHeyBean().setType(this.type);
        this.getHeyBean().setIniDate(this.iniDate);
        this.getHeyBean().setFimDate(this.fimDate);
        this.getHeyBean().setRestriction(this.restriction);
        this.getHeyBean().setDep(this.dep);
        if (this.getHeyBean().insertElection() != -1) {
            message = "Eleição inserida com sucesso!";
            addActionMessage(message);
            return SUCCESS;
        } else {
            message = "Impossível inserir eleição!";
            addActionError(message);
            return ERROR;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIniDate(String iniDate) {
        this.iniDate = iniDate;
    }

    public void setFimDate(String fimDate) {
        this.fimDate = fimDate;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public void setDep(int dep) {
        this.dep = dep;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getIniDate() {
        return iniDate;
    }

    public String getFimDate() {
        return fimDate;
    }

    public String getMessage() {
        return message;
    }

    public int getDep() {
        return dep;
    }

    public String getRestriction() {
        return restriction;
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
