package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class FacebookLoginAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private String authorizationURL;

    @Override
    public String execute() throws Exception {
//        session.put("server", this.getHeyBean().getServer());
//        if(this.getHeyBean().fbLoginURL() != null){
//            this.setAuthorizationURL(getHeyBean().getAuthorizationURL());
//            return SUCCESS;
//        }
        return ERROR;
    }

    public String getAuthorizationURL() {
        return authorizationURL;
    }

    public void setAuthorizationURL(String authorizationURL) {
        this.authorizationURL = authorizationURL;
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
