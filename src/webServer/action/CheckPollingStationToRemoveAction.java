package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class CheckPollingStationToRemoveAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private int department_id;
    String message = "";

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setDepartment_id(this.department_id);
        if (this.getHeyBean().getAssociatedPollingStations() == null) {
            message = "Não tem mesas de voto associadas!";
            addActionError(message);
            return ERROR;
        }
        return SUCCESS;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
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
