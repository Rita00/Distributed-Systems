package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class AddListToAnElectionAction extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Guarda o nome de uma lista a inserir a uma eleição
     */
    String list_name;

    /**
     * Mensagem a ser apresentada na view consoante tenha tido sucesso ou não a inserir a lista
     */
    String message = "";

    /**
     * Verifica se uma determinada eleição existe
     * Se existir tenta adicioná-la, caso contrário dá return de erro
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para adicionar uma lista a uma eleição
     */
    @Override
    public String execute() throws Exception {
        if (this.getHeyBean().checkIfSelectedElectionExists()) {
            this.getHeyBean().setList_name(this.list_name);
            if (this.getHeyBean().addListToAnElection()) {
                message = "Lista adicionada com sucesso!";
                addActionMessage(message);
                return SUCCESS;
            } else {
                message = "Impossível adicionar lista!";
                addActionError(message);
                return "add_list_error";
            }
        } else {
            message = "A eleição não existe!";
            addActionError(message);
            return ERROR;
        }
    }

    /**
     * Setter para o nome da lista
     * @param list_name nome da lista
     */
    public void setList_name(String list_name) {
        this.list_name = list_name;
    }

    /**
     * Getter para o nome da lista
     * @return nome da lista
     */
    public String getList_name() {
        return list_name;
    }

    /**
     * Setter para a sessão
     * @param session sessão
     */
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    /**
     * Getter para o bean
     * @return bean
     */
    public HeyBean getHeyBean() {
        if (!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    /**
     * Setter para o Bean
     * @param heyBean Bean
     */
    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }
}
