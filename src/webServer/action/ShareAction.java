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

    /**
     * Guarda o id da eleição na partilha no facebook
     */
    private int election_id;

    /**
     * Faz o set de todas as variaveis necessárias para apresentar na view
     * Verifica se a eleição em questão já terminou ou não
     * Se terminou apresenta os resultados
     * Se não, apresenta os seus detalhes
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para visualizar o link de uma eleição partilhada no facebook
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

    /**
     * Getter para o id da eleição
     * @return id da eleição
     */
    public int getElection_id() {
        return election_id;
    }

    /**
     * Setter para o id da eleição
     * @param election_id id da eleição
     */
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
