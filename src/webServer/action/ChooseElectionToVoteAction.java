package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import pt.uc.dei.student.elections.Election;
import webServer.model.HeyBean;

import java.util.Map;

public class ChooseElectionToVoteAction extends ActionSupport implements SessionAware {
    /**
     * Guarda id da eleição
     */
    private int election_id;

    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Mensagem a ser apresentada na view consoante tenha tido sucesso ou não a inserir a lista
     */
    private String message = "";

    /**
     * Verifica se o utilizador está com login efetuado
     * Se sim verifica se já votou numa determinada eleição e se a eleição que escolheu tem listas
     * Se já tiver votado dá erro a indicar que não pode votar
     * Se a eleição não tiver listas dá erro e informa que não pode votar
     *
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para escolher em que eleição um determinado utilizador quer votar
     */
    @Override
    public String execute() throws Exception {
        this.getHeyBean().setElection_id(this.election_id);
        if (session.get("loggedin") != null) {
            boolean res = (Boolean) session.get("loggedin");
            if (!res) {
                message = "Não tem sessão iniciada.";
                addActionError(message);
                return ERROR;
            } else {
                if (!this.getHeyBean().checkIfAlreadyVotes() && this.election_id != 0)
                    return SUCCESS;
                else if (this.getHeyBean().getCandidacies() == null) {
                    message = "A eleição não tem listas!";
                } else {
                    message = "Já votou nesta eleição!";
                    addActionError(message);
                }
                return ERROR;
            }
        } else {
            message = "Não tem sessão iniciada.";
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
     * Setter para o id da eleição
     *
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
