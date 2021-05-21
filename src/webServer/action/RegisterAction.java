package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Date;
import java.util.Map;

public class RegisterAction extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Guarda o nome da pessoa
     */
    private String name;

    /**
     * Guarda o cargo da pessoa
     */
    private String cargo;

    /**
     * Guarda a password da pessoa
     */
    private String password;

    /**
     * Guarda a morada da pessoa
     */
    private String address;

    /**
     * Guarda a data do cartão de cidadão da pessoa
     */
    private String ccDate;

    /**
     * Guarda o número de telefone da pessoa
     */
    private int phone;

    /**
     * Guarda o número de cartão de cidadão da pessoa
     */
    private int ccnumber;

    /**
     * Guarda o departamento aue a pessoa frequenta
     */
    private int dep;

    /**
     * Mensagem a ser apresentada na view consoante tenha tido sucesso ou não a inserir a lista
     */
    private String message = "";

    /**
     * Faz set das variáveis necessárias para inserir uma pessoa na base de dados
     * Tenta inserir a pessoa na base de dados
     *
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para registar um novo utilizador
     */
    @Override
    public String execute() throws Exception {
        this.getHeyBean().setName(this.name);
        this.getHeyBean().setCargo(this.cargo);
        this.getHeyBean().setDep(this.dep);
        this.getHeyBean().setPhone(this.phone);
        this.getHeyBean().setCcnumber(this.ccnumber);
        this.getHeyBean().setPassword(this.password);
        this.getHeyBean().setAddress(this.address);
        this.getHeyBean().setCcDate(this.ccDate);
        if (this.getHeyBean().insertRegister()) {
            message = "Registo efetuado com sucesso!";
            addActionMessage(message);
            return SUCCESS;
        } else {
            message = "Impossível inserir registo!";
            addActionError(message);
            return ERROR;
        }
    }

    /**
     * Getter para o nome da pessoa
     *
     * @return nome da pessoa
     */
    public String getName() {
        return name;
    }

    /**
     * Getter para o cargo da pessoa
     *
     * @return cargo da pessoa
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * Getter para a password da pessoa
     *
     * @return password da pessoa
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter para o departamento que a pessoa frequenta
     *
     * @return id do departamento
     */
    public int getDep() {
        return dep;
    }

    /**
     * Getter para a morada da pessoa
     *
     * @return morada da pessoa
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter para o número de telefone da pessoa
     *
     * @return número de telefone da pessoa
     */
    public int getPhone() {
        return phone;
    }

    /**
     * Getter para o número de cartão de cidadão da pessoa
     *
     * @return número de cartão de cidadão da pessoa
     */
    public int getCcnumber() {
        return ccnumber;
    }

    /**
     * Getter para a validade do cartão de cidadão da pessoa
     *
     * @return validade do número de cartão de cidadão da pessoa
     */
    public String getCcDate() {
        return ccDate;
    }

    /**
     * Setter para o nome da pessoa
     *
     * @param name nome da pessoa
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter para o cargo da pessoa
     *
     * @param cargo cargo da pessoa
     */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    /**
     * Setter para a password da pessoa
     *
     * @param password password da pessoa
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Setter para o departamento da pessoa
     *
     * @param dep id do departamento que a pessoa frequenta
     */
    public void setDep(int dep) {
        this.dep = dep;
    }

    /**
     * Setter da morada da pessoa
     *
     * @param address morada da pessoa
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Setter para o número de telefone da pessoa
     *
     * @param phone número de telefone da pessoa
     */
    public void setPhone(int phone) {
        this.phone = phone;
    }

    /**
     * Setter para o número de cartão de cidadão da pessoa
     *
     * @param ccnumber número de cartão de cidadão da pessoa
     */
    public void setCcnumber(int ccnumber) {
        this.ccnumber = ccnumber;
    }

    /**
     * Setter para a validade do cartão de cidadão da pessoa
     *
     * @param ccDate validade do cartão de cidadão da pessoa
     */
    public void setCcDate(String ccDate) {
        this.ccDate = ccDate;
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
