package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import pt.uc.dei.student.others.NotifierCallBack;
import webServer.model.HeyBean;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

public class ShowVotesCountAction extends ActionSupport implements SessionAware{
    /**
     * Sessoes
     */
    private Map<String, Object> session;
    /**
     * Notifier do callback
     */
    private NotifierCallBack NOTIFIER;

    /**
     * Criar o callback para a contagem dos votos
     * @return SUCCESS sucesso
     * @throws Exception  exceção do notifier
     */
    @Override
    public String execute() throws Exception {
        this.NOTIFIER = new NotifierCallBack();
        this.getHeyBean().setRealTimeVotesOn(this.NOTIFIER);
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
     * Setter para a sessão
     *
     * @param session sessão
     */
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
    /**
     * Getter para o bean
     *
     * @return bean
     */
    public HeyBean getHeyBean() {
        if (!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }
    /**
     * Setter para o Bean
     *
     * @param heyBean Bean
     */
    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }

}
