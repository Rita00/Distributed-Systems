package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class ChooseElectionToSeeResultsAction extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Guarda o id de uma eleição
     */
    private int election_id;

    /**
     * Guarda o número de votos nulos
     */
    private int null_votes;

    /**
     * Guarda o número de votos brancos
     */
    private int blank_votes;

    /**
     * Guarda a percentagem de votos nulos
     */
    private float null_percent;

    /**
     * Guarda a percentagem de votos brancos
     */
    private float blank_percent;

    /**
     * Guarda o título de uma eleição
     */
    private String election_title;

    /**
     * Mensagem a ser apresentada na view consoante tenha tido sucesso ou não a inserir a lista
     */
    String message = "";

    /**
     * Verifica se uma determinada eleição existe
     * Se existir dá set dos valores necessários para a view seguinte
     *
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para visualizar os resultados de uma determianda eleição
     */
    @Override
    public String execute() throws Exception {
        this.getHeyBean().setTitle(this.election_title);
        this.getHeyBean().setElection_id(this.election_id);
        if (!this.getHeyBean().checkIfSelectedElectionExists()) {
            message = "Esta eleição não existe!";
            addActionError(message);
            return ERROR;
        } else {
            this.getHeyBean().setNull_votes(this.null_votes);
            this.getHeyBean().setBlank_votes(this.blank_votes);
            this.getHeyBean().setNull_percent(this.null_percent);
            this.getHeyBean().setBlank_percent(this.blank_percent);
        }
        return SUCCESS;
    }

    /**
     * Getter para a percentagem de votos nulos
     *
     * @return percentagem de votos nulos
     */
    public float getNull_percent() {
        return null_percent;
    }

    /**
     * Getter para a percentagem de votos brancos
     *
     * @return percentagem de votos brancos
     */
    public float getBlank_percent() {
        return blank_percent;
    }

    /**
     * Getter para o número de votos nulos
     *
     * @return número de votos nulos
     */
    public int getNull_votes() {
        return null_votes;
    }

    /**
     * Getter para o número de votos brancos
     *
     * @return número de votos brancos
     */
    public int getBlank_votes() {
        return blank_votes;
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
     * Setter para o id da eleição
     *
     * @param election_id id da eleição
     */
    public void setElection_id(int election_id) {
        this.election_id = election_id;
    }

    /**
     * Setter para o número de votos nulos
     *
     * @param null_votes número de votos nulos
     */
    public void setNull_votes(int null_votes) {
        this.null_votes = null_votes;
    }

    /**
     * Setter para o número de votos brancos
     *
     * @param blank_votes número de votos brancos
     */
    public void setBlank_votes(int blank_votes) {
        this.blank_votes = blank_votes;
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
     * Setter para a percentagem de votos nulos
     *
     * @param null_percent percentagem de votos nulos
     */
    public void setNull_percent(float null_percent) {
        this.null_percent = null_percent;
    }

    /**
     * Setter para a percentagem de votos brancos
     *
     * @param blank_percent percentagem de votos brancos
     */
    public void setBlank_percent(float blank_percent) {
        this.blank_percent = blank_percent;
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
