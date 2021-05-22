package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import pt.uc.dei.student.others.NotifierCallBack;
import webServer.model.HeyBean;

import java.util.Map;

public class ShowPollingStationsAction extends ActionSupport implements SessionAware{
    /**
     * Sessao
     */
    private Map<String, Object> session;
    /**
     * Notifier callback
     */
    private NotifierCallBack NOTIFIER;

    /**
     * Cria callback para estado das mesas de voto
     * @return SUCCESS sucesso
     * @throws Exception exceção do notifier
     */
    @Override
    public String execute() throws Exception {
        this.NOTIFIER = new NotifierCallBack();
        this.getHeyBean().setRealTimePollingStationOn(this.NOTIFIER);
        return SUCCESS;
    }

    /**
     * Getter do notifier
     * @return notifier
     */
    public NotifierCallBack getNOTIFIER() {
        return NOTIFIER;
    }

    /**
     * Setter do notifier
     * @param NOTIFIER notifier
     */
    public void setNOTIFIER(NotifierCallBack NOTIFIER) {
        this.NOTIFIER = NOTIFIER;
    }

    /**
     * Setter da sessao
     * @param session sessao
     */
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    /**
     * Getter da bean
     * @return bean
     */
    public HeyBean getHeyBean() {
        if (!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    /**
     * Setter da bean
     * @param heyBean bean
     */
    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }
}
