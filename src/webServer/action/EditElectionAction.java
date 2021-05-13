package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class EditElectionAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    String title, type, description, iniDate, FimDate;
    String message;

    @Override
    public String execute() throws Exception {
        if (this.getHeyBean().checkIfSelectedElectionExists()) {
            if (this.getHeyBean().checkIfCanEdit() == 0) {
                message = "Esta eleição não pode ser editada!";
                addActionError(message);
                return ERROR;
            } else {
                this.getHeyBean().setTitle(this.title);
                this.getHeyBean().setType(this.type);
                this.getHeyBean().setDescription(this.description);
                this.getHeyBean().setIniDate(this.iniDate);
                this.getHeyBean().setFimDate(this.FimDate);
                if (this.getHeyBean().editElection()) {
                    message = "Eleição editada com sucesso!";
                    addActionMessage(message);
                    return SUCCESS;
                } else {
                    message = "Não pode editar esta eleição!";
                    addActionError(message);
                    return SUCCESS;
                }
            }
        } else {
            message = "A eleição não existe!";
            addActionError(message);
            return ERROR;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getIniDate() {
        return iniDate;
    }

    public String getFimDate() {
        return FimDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIniDate(String iniDate) {
        this.iniDate = iniDate;
    }

    public void setFimDate(String fimDate) {
        FimDate = fimDate;
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
