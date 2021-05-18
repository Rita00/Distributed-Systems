package webServer.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.struts2.interceptor.SessionAware;
import pt.uc.dei.student.others.NotifierCallBack;
import webServer.model.HeyBean;

import java.lang.annotation.Annotation;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Map;

public class ShowVotesCountInterceptor implements Interceptor {
    private static final long serialVersionUID = 189237412378L;
    private Map<String, Object> session;
    private NotifierCallBack NOTIFIER;

    @Override
    public void destroy() {

    }

    @Override
    public void init() {
        try {
            this.NOTIFIER = new NotifierCallBack();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.getHeyBean().setRealTimeOn(this.NOTIFIER);
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        this.session = invocation.getInvocationContext().getSession();
        return invocation.invoke();
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
