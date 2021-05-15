package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class VotingRecordsAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    String message = "";

    @Override
    public String execute() throws Exception {
        if (this.getHeyBean().getVotingRecords().size() == 0) {
            message = "Não há registo de votos!";
            addActionMessage(message);
        }
        return SUCCESS;
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
