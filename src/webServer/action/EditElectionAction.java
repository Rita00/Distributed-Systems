package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class EditElectionAction extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Guarda o título da eleição
     */
    private String title;

    /**
     * Guarda o tipo da eleição
     */
    private String type;

    /**
     * Guarda a descrição da eleição
     */
    private String description;

    /**
     * Guarda a data de início da eleição
     */
    private String iniDate;

    /**
     * Guarda a data de fim da eleição
     */
    private String FimDate;

    /**
     * Mensagem a ser apresentada na view consoante tenha tido sucesso ou não a inserir a lista
     */
    String message;

    /**
     * Verifica se uma determinada eleição existe
     * Verfica se ainda não começou para poder ser editada
     * Se ainda não começou faz-se o set das variáveis e depois tenta-se dar update na base de dados
     * Se já começou dá erro e a eleição não pode ser editada
     *
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para editar uma determinada eleição
     */
    @Override
    public String execute() throws Exception {
        if (this.getHeyBean().checkIfSelectedElectionExists()) {
            if (this.getHeyBean().checkIfCanEdit() == 0) {
                message = "Esta eleição não pode ser editada!";
                addActionError(message);
                return ERROR;
            } else {
                this.getHeyBean().setTitle(this.title);
                this.getHeyBean().setType(this.type);
                this.getHeyBean().setDescription(this.description);
                this.getHeyBean().setIniDate(this.iniDate);
                this.getHeyBean().setFimDate(this.FimDate);
                if (this.getHeyBean().editElection()) {
                    message = "Eleição editada com sucesso!";
                    addActionMessage(message);
                    return SUCCESS;
                } else {
                    message = "Não pode editar esta eleição!";
                    addActionError(message);
                    return SUCCESS;
                }
            }
        } else {
            message = "A eleição não existe!";
            addActionError(message);
            return ERROR;
        }
    }

    /**
     * Getter para o título da eleição
     *
     * @return título da eleição
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter para o tipo da eleição
     *
     * @return tipo da eleição
     */
    public String getType() {
        return type;
    }

    /**
     * Getter da descrição da eleição
     *
     * @return descrição da eleição
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter da data de início da eleição
     *
     * @return data de início da eleição
     */
    public String getIniDate() {
        return iniDate;
    }

    /**
     * Getter da data de fim da eleição
     *
     * @return data de fim da eleição
     */
    public String getFimDate() {
        return FimDate;
    }

    /**
     * Setter do título da eleição
     *
     * @param title título da eleição
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter do tipo de eleição
     *
     * @param type tipo de eleição
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Setter da descrição da eleição
     *
     * @param description descrição da eleição
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Setter da data de início da eleição
     *
     * @param iniDate data de início da eleição
     */
    public void setIniDate(String iniDate) {
        this.iniDate = iniDate;
    }

    /**
     * Setter da data de fim da eleição
     *
     * @param fimDate data de fim da eleição
     */
    public void setFimDate(String fimDate) {
        FimDate = fimDate;
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
