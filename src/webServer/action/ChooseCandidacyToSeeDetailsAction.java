package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class ChooseCandidacyToSeeDetailsAction extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Guarda o id da lista
     */
    int candidacy_id;

    /**
     * Guarda o nome da lista
     */
    private String candidacy_name;

    /**
     * Guarda o tipo da lista
     */
    private String candidacy_type;

    /**
     * Mensagem a ser apresentada na view consoante tenha tido sucesso ou não a inserir a lista
     */
    String message = "";

    /**
     * Verifica se uma determinada eleição existe
     * Se sim, verifica se a lista pertence à eleição e caso exista dá set das suas variáveis para a página seguinte,
     * se não dá erro
     *
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para escolher uma lista para ver os detalhes
     */
    @Override
    public String execute() throws Exception {
        this.getHeyBean().setCandidacy_id(this.candidacy_id);
        if (this.getHeyBean().checkIfSelectedElectionExists()) {
            if (this.getHeyBean().checkSelectedCandidacy_Election()) {
                this.getHeyBean().setCandidacy_name(this.candidacy_name);
                this.getHeyBean().setCandidacy_type(this.candidacy_type);
                return SUCCESS;
            } else {
                message = "Esta lista não existe!";
                addActionError(message);
                return "error_candidacy";
            }
        } else {
            message = "A eleição que escolheu não existe.";
            addActionError(message);
            return ERROR;
        }
    }

    /**
     * Getter para o id da lista
     *
     * @return id da lista
     */
    public int getCandidacy_id() {
        return candidacy_id;
    }

    /**
     * Getter para o nome da lista
     *
     * @return nome da lista
     */
    public String getCandidacy_name() {
        return candidacy_name;
    }

    /**
     * Getter para o tipo da lista
     *
     * @return tipo da lista
     */
    public String getCandidacy_type() {
        return candidacy_type;
    }

    /**
     * Setter para o id da lista
     *
     * @param candidacy_id id da lista
     */
    public void setCandidacy_id(int candidacy_id) {
        this.candidacy_id = candidacy_id;
    }

    /**
     * Setter para o nome da lista
     *
     * @param candidacy_name nome da lista
     */
    public void setCandidacy_name(String candidacy_name) {
        this.candidacy_name = candidacy_name;
    }

    /**
     * Setter para o tipo da lista
     *
     * @param candidacy_type tipo da lista
     */
    public void setCandidacy_type(String candidacy_type) {
        this.candidacy_type = candidacy_type;
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
