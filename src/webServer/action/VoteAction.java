package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class VoteAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private int candidacy_id;
    String message = "";

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setCandidacy_id(this.candidacy_id);
        //todo verificar se pode votar
        if(session.get("loggedin") != null) {
            boolean res = (Boolean) session.get("loggedin");
            if (!res) {
                message = "Não tem sessão iniciada.";
                addActionError(message);
                return ERROR;
            } else {
                if (!this.getHeyBean().checkIfAlreadyVoteOnVoteForm()) {
                    this.getHeyBean().updateVotes();
                    message = "Voto efetuado com sucesso!";
                    addActionMessage(message);
                    return SUCCESS;
                } else {
                    message = "Já votou nesta eleição!";
                    addActionError(message);
                    return ERROR;
                }
            }
        } else {
            message = "Não tem sessão iniciada.";
            addActionError(message);
            return ERROR;
        }
    }

    public int getCandidacy_id() {
        return candidacy_id;
    }

    public void setCandidacy_id(int candidacy_id) {
        this.candidacy_id = candidacy_id;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public HeyBean getHeyBean() {
        if (!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }
}
