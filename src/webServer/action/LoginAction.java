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
        // TODO FAZER LOGOUT
        // TODO SE TIVER LOGIN FEITO NÃO ENTRAR NA PAGINA DE LOGIN AGAIN
        if (session.get("loggedin") == null || !(Boolean) session.get("loggedin")) {
            this.getHeyBean().setCcnumber(this.ccnumber);
            this.getHeyBean().setPassword(this.password);
            if (this.getHeyBean().getUser() != null) {
                session.put("ccnumber", ccnumber);
                session.put("loggedin", true); // this marks the user as logged in
                if (this.getHeyBean().checkIfIsAdmin())
                    return "isAdmin";
                return SUCCESS;
            } else {
                this.getHeyBean().setCcnumber(0);
                this.getHeyBean().setPassword(null);
                return LOGIN;
            }
        } else {
            if (this.getHeyBean().checkIfIsAdmin())
                return "isAdmin";
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