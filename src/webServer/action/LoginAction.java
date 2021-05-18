/**
 * Raul Barbosa 2014-11-07
 */
package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class LoginAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private int ccnumber = 0;
    private String password = null;

    @Override
    public String execute() throws Exception {
        // todo Verificar se já tem login feito, caso tenha não iniicar sessão de novo!
        // todo é preciso ter smp em atenção que se pode fazer pedidos sem ser pelo browser (exemplo: postman)
        if (this.getHeyBean().getCcnumber() == 0) {
            this.getHeyBean().setCcnumber(this.ccnumber);
            this.getHeyBean().setPassword(this.password);
            if (this.getHeyBean().getUser() != null) {
                session.put("ccnumber", ccnumber);
                session.put("loggedin", true); // this marks the user as logged in
                return SUCCESS;
            } else {
                this.getHeyBean().setCcnumber(0);
                this.getHeyBean().setPassword(null);
                return LOGIN;
            }
        } else {
            return SUCCESS;
        }

    }

    public int getCcnumber() {
        return ccnumber;
    }

    public String getPassword() {
        return password;
    }

    public void setCcnumber(int ccnumber) {
        this.ccnumber = ccnumber;
    }

    public void setPassword(String password) {
        this.password = password; // what about this input?
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