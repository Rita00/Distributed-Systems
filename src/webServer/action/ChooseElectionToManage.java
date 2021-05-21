package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class ChooseElectionToManage extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Guarda o id de uma eleição
     */
    private int election_id;

    /**
     * Guarda o id de uma lista
     */
    private int candidacy_id;

    /**
     * Guarda o título de uma eleição
     */
    private String election_title;

    /**
     * Guarda o tipo de uma eleição
     */
    private String election_type;

    /**
     * Guarda a descrição de uma eleição
     */
    private String election_description;

    /**
     * Guarda a data de início de uma eleição
     */
    private String iniDate;

    /**
     * Guarda a data de fim de uma eleição
     */
    private String fimDate;

    /**
     * Mensagem a ser apresentada na view consoante tenha tido sucesso ou não a inserir a lista
     */
    String message = "";

    /**
     * Verifica se uma determinada eleição existe
     * Se existir dá set dos valores necessários para a view seguinte
     *
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para escolher uma eleição para editar / consultar detalhes
     */
    @Override
    public String execute() throws Exception {
        this.getHeyBean().setElection_id(this.election_id);
        if (this.getHeyBean().checkIfSelectedElectionExists()) {
            this.getHeyBean().setTitle(this.election_title);
            this.getHeyBean().setType(this.election_type);
            this.getHeyBean().setDescription(this.election_description);
            this.getHeyBean().setIniDate(this.iniDate);
            this.getHeyBean().setFimDate(this.fimDate);
            this.getHeyBean().setCandidacy_id(this.candidacy_id);
            return SUCCESS;
        } else {
            message = "A eleição que escolheu não existe.";
            addActionError(message);
            return ERROR;
        }
    }

    /**
     * Getter para o id da eleição
     *
     * @return id da eleição
     */
    public int getElection_id() {
        return election_id;
    }

    /**
     * Getter para o título da eleição
     *
     * @return título da eleição
     */
    public String getElection_title() {
        return election_title;
    }

    /**
     * Getter para o tipo da eleição
     *
     * @return tipo da eleição
     */
    public String getElection_type() {
        return election_type;
    }

    /**
     * Getter para a descrição da eleição
     *
     * @return descrição da eleição
     */
    public String getElection_description() {
        return election_description;
    }

    /**
     * Getter para a data de início da eleição
     *
     * @return data de início da eleição
     */
    public String getIniDate() {
        return iniDate;
    }

    /**
     * Getter para a data de fim da eleição
     *
     * @return data de fim da eleição
     */
    public String getFimDate() {
        return fimDate;
    }

    /**
     * Setter para o id da eleição
     *
     * @param election_id id da eleição
     */
    public void setElection_id(int election_id) {
        this.election_id = election_id;
    }

    /**
     * Setter para o título da eleição
     *
     * @param election_title título da eleição
     */
    public void setElection_title(String election_title) {
        this.election_title = election_title;
    }

    /**
     * Setter para o tipo da eleição
     *
     * @param election_type tipo da eleição
     */
    public void setElection_type(String election_type) {
        this.election_type = election_type;
    }

    /**
     * Setter para a descrição da eleição
     *
     * @param election_description descrição da eleição
     */
    public void setElection_description(String election_description) {
        this.election_description = election_description;
    }

    /**
     * Setter para a data de início da eleição
     *
     * @param iniDate data de início da eleição
     */
    public void setIniDate(String iniDate) {
        this.iniDate = iniDate;
    }

    /**
     * Setter para a data de fim da eleição
     *
     * @param fimDate data de fim da eleição
     */
    public void setFimDate(String fimDate) {
        this.fimDate = fimDate;
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
