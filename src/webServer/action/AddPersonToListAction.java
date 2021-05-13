package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class AddPersonToListAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    int person_cc;
    String message = "";

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setPerson_cc(this.person_cc);
        if (this.getHeyBean().checkIfPersonExists().equals("")) {
            message = "Pessoa inserida com sucesso!";
            addActionMessage(message);
            return SUCCESS;
        } else {
            message = "Impossível inserir pessoa!";
            addActionError(message);
            return ERROR;
        }
    }

    public int getPerson_cc() {
        return person_cc;
    }

    public void setPerson_cc(int person_cc) {
        this.person_cc = person_cc;
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
