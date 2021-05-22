package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class LoginAction extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Guarda o número de cartão de cidadão
     */
    private int ccnumber = 0;

    /**
     * Guarda a password
     */
    private String password = null;

    /**
     * Verifica se o utilizador tem Login feito
     * Vê se é um utilizador normal ou administrador e redireciona-o para a página correta
     *
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para efetuar login no webSite
     */
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
                this.getHeyBean().updateStatusLogin();
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

    /**
     * Getter para o número de cartão de cidadão
     *
     * @return número do cartão de cidadão
     */
    public int getCcnumber() {
        return ccnumber;
    }

    /**
     * Getter para a password
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter para o número do cartão de cidadão
     *
     * @param ccnumber número do cartão de cidadão
     */
    public void setCcnumber(int ccnumber) {
        this.ccnumber = ccnumber;
    }

    /**
     * Setter para a password de um utilizador
     *
     * @param password password de um utilizador
     */
    public void setPassword(String password) {
        this.password = password; // what about this input?
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