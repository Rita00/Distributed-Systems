package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class AddListToAnElectionAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    String list_name;
    String message = "";

    @Override
    public String execute() throws Exception {
        if (this.getHeyBean().checkIfSelectedElectionExists()) {
            this.getHeyBean().setList_name(this.list_name);
            if (this.getHeyBean().addListToAnElection()) {
                message = "Lista adicionada com sucesso!";
                addActionMessage(message);
                return SUCCESS;
            } else {
                message = "Impossível adicionar lista!";
                addActionError(message);
                return "add_list_error";
            }
        } else {
            message = "A eleição não existe!";
            addActionError(message);
            return ERROR;
        }
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }

    public String getList_name() {
        return list_name;
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
