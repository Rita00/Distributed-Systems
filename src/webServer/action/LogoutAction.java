package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class LogoutAction extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Desliga a sessão de um determinado utilizador
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o logout de um utilizador
     */
    @Override
    public String execute() throws Exception {
        session.put("loggedin", false);
        session.put("ccnumber", 0);
        this.getHeyBean().updateStatusLogout();
        this.getHeyBean().setCcnumber(0);
        this.getHeyBean().setPassword(null);
        return SUCCESS;
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
