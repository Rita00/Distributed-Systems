package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import pt.uc.dei.student.others.NotifierCallBack;
import webServer.model.HeyBean;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

public class ShowVotesCountAction extends ActionSupport implements SessionAware{
    private Map<String, Object> session;
    private NotifierCallBack NOTIFIER;

    @Override
    public String execute() throws Exception {
        this.NOTIFIER = new NotifierCallBack();
        this.getHeyBean().setRealTimeVotesOn(this.NOTIFIER);
        return SUCCESS;
    }

    public NotifierCallBack getNOTIFIER() {
        return NOTIFIER;
    }

    public void setNOTIFIER(NotifierCallBack NOTIFIER) {
        this.NOTIFIER = NOTIFIER;
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
