package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Date;
import java.util.Map;

public class RegisterAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private String name, cargo, password, address, ccDate;
    private int phone, ccnumber, dep;
    private String message = "";

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
        if(this.getHeyBean().insertRegister()) {
            message = "Registo efetuado com sucesso!";
            addActionMessage(message);
            return SUCCESS;
        }
        else {
            message = "Impossível inserir registo!";
            addActionError(message);
            return ERROR;
        }
    }

    public String getName() {
        return name;
    }

    public String getCargo() {
        return cargo;
    }

    public String getPassword() {
        return password;
    }

    public int getDep() {
        return dep;
    }

    public String getAddress() {
        return address;
    }

    public int getPhone() {
        return phone;
    }

    public int getCcnumber() {
        return ccnumber;
    }

    public String getCcDate() {
        return ccDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDep(int dep) {
        this.dep = dep;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setCcnumber(int ccnumber) {
        this.ccnumber = ccnumber;
    }

    public void setCcDate(String ccDate) {
        this.ccDate = ccDate;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public HeyBean getHeyBean() {
        if(!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }
}
