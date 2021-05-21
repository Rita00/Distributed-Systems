package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class CreateElectionAction extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Guarda o título da eleição
     */
    private String title;

    /**
     * Guarda a descrição da eleição
     */
    private String description;

    /**
     * Guarda o tipo da eleiçaõ
     */
    private String type;

    /**
     * Guarda a data de início de uma eleição
     */
    private String iniDate;

    /**
     * Guarda a data de fim de uma eleição
     */
    private String fimDate;

    /**
     * Guarda a restrição de uma eleição
     */
    private String restriction;

    /**
     * Guarda o departamento da eleição
     */
    private int dep;

    /**
     * Mensagem a ser apresentada na view consoante tenha tido sucesso ou não a inserir a lista
     */
    private String message = "";

    /**
     * Faz set de todas as variáveis necessárias para criar uma eleição
     * Tenta inserir a eleição na base de dados
     * Se não conseguir retorna erro se conseguir retorna sucesso
     *
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para criar uma eleição
     */
    @Override
    public String execute() throws Exception {
        this.getHeyBean().setTitle(this.title);
        this.getHeyBean().setDescription(this.description);
        this.getHeyBean().setType(this.type);
        this.getHeyBean().setIniDate(this.iniDate);
        this.getHeyBean().setFimDate(this.fimDate);
        this.getHeyBean().setRestriction(this.restriction);
        this.getHeyBean().setDep(this.dep);
        if (this.getHeyBean().insertElection() != -1) {
            message = "Eleição inserida com sucesso!";
            addActionMessage(message);
            return SUCCESS;
        } else {
            message = "Impossível inserir eleição!";
            addActionError(message);
            return ERROR;
        }
    }

    /**
     * Setter para o título de uma eleição
     *
     * @param title título da eleição
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter para a descrição de uma eleição
     *
     * @param description descrição da eleição
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Setter para o tipo da eleição
     *
     * @param type tipo da eleição
     */
    public void setType(String type) {
        this.type = type;
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
     * Setter para a restrição de uma eleição
     *
     * @param restriction restrição da eleição
     */
    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    /**
     * Setter para o departamento de uma eleição
     *
     * @param dep departamento da eleição
     */
    public void setDep(int dep) {
        this.dep = dep;
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
     * Getter para a descrição da eleição
     *
     * @return descrição da eleição
     */
    public String getDescription() {
        return description;
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
     * Getter para o departamento da eleição
     *
     * @return departamento da eleição
     */
    public int getDep() {
        return dep;
    }

    /**
     * Getter para a restrição da eleição
     *
     * @return restrição da eleição ("yes" ou "no")
     */
    public String getRestriction() {
        return restriction;
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
