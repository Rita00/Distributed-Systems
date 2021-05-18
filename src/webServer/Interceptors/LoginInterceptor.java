package webServer.Interceptors;

import java.util.Map;
import java.util.Calendar;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import webServer.model.HeyBean;

public class LoginInterceptor implements Interceptor {
    private static final long serialVersionUID = 189237412378L;
    Map<String, Object> session;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        session = invocation.getInvocationContext().getSession();
        Object user = this.getHeyBean().getUser();
        if (user == null) {
            // The user has not logged in yet.
            return Action.LOGIN;
        } else {
            return invocation.invoke ();
        }
        // this method intercepts the execution of the action and we get access
        // to the session, to the action, and to the context of this invocation
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
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
