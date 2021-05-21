package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class AddPersonToListAction extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Guarda o número de cartão de cidadão
     */
    int person_cc;

    /**
     * Mensagem a ser apresentada na view consoante tenha tido sucesso ou não a inserir a lista
     */
    String message = "";

    /**
     * Verifica se uma determinada pessoa existe
     * Se existir tenta adicioná-la como membro
     *
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para adicionar uma pessoa a uma lista
     */
    @Override
    public String execute() throws Exception {
        this.getHeyBean().setPerson_cc(this.person_cc);
        if (this.getHeyBean().checkIfPersonExists().equals("")) {
            message = "Pessoa inserida com sucesso!";
            addActionMessage(message);
            return SUCCESS;
        } else {
            message = "Impossível inserir pessoa!";
            addActionError(message);
            return ERROR;
        }
    }

    /**
     * Getter para o número de cartão de cidadão
     *
     * @return número do cartão de cidadão
     */
    public int getPerson_cc() {
        return person_cc;
    }

    /**
     * Setter para o número de cartão de cidadão
     *
     * @param person_cc número do cartão de cidadão
     */
    public void setPerson_cc(int person_cc) {
        this.person_cc = person_cc;
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
