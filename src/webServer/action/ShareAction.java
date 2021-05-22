package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class ShareAction extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    private int election_id;

    /**
     * Faz o redirecionado para uma página iniciando o Bean
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para redirecionar para páginas
     */
    @Override
    public String execute() throws Exception {
        this.getHeyBean().setElection_id(this.election_id);
        this.getHeyBean().setTitle(this.getHeyBean().getTitleElectionShare());
        this.getHeyBean().setType(this.getHeyBean().getTypeElectionShare());
        this.getHeyBean().setDescription(this.getHeyBean().getDescriptionElectionShare());
        this.getHeyBean().setIniDate(this.getHeyBean().getIniDateElectionShare());
        this.getHeyBean().setFimDate(this.getHeyBean().getEndDateElectionShare());
        if (this.getHeyBean().checkEndElection())
            return "end";
        return SUCCESS;
    }

    public int getElection_id() {
        return election_id;
    }

    public void setElection_id(int election_id) {
        this.election_id = election_id;
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
