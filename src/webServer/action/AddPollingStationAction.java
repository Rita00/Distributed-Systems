package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class AddPollingStationAction extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Guarda o id do departamento
     */
    private int department_id;

    /**
     * Mensagem a ser apresentada na view consoante tenha tido sucesso ou não a inserir a lista
     */
    String message = "";

    /**
     * Verifica se a mesa de voto a adicionar está ativa
     * Se sim tenta adicioná-la, caso contrário dá erro
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para associar uma mesa de voto a uma determinada eleiçáo
     */
    @Override
    public String execute() throws Exception {
        this.getHeyBean().setDepartment_id(this.department_id);
        if (this.getHeyBean().checkIfPollingStationIsActive()) {
            if (this.getHeyBean().addPollingStation()) {
                message = "Mesa de voto associada com sucesso!";
                addActionMessage(message);
                return SUCCESS;
            } else {
                message = "Esta eleição está restringida, não podem ser adicionadas mesas de voto!";
                addActionError(message);
                return "restrict";
            }
        } else {
            message = "Esta mesa de voto não está ativa!";
            addActionError(message);
            return ERROR;
        }
    }

    /**
     * Getter para o id do departamento
     * @return id do departamento
     */
    public int getDepartment_id() {
        return department_id;
    }

    /**
     * Setter para o id do departamento
     * @param department_id id do departamento
     */
    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
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
