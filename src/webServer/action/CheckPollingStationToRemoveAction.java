package webServer.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import webServer.model.HeyBean;

import java.util.Map;

public class CheckPollingStationToRemoveAction extends ActionSupport implements SessionAware {
    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Mensagem a ser apresentada na view consoante tenha tido sucesso ou não a inserir a lista
     */
    String message = "";

    /**
     * Verifica se uma determianda eleição tem mesas de votos para poder desassociar
     * Se sim envia mensagem para passar para a página seguinte, caso contrário não prossegue e mantém na mesma página
     *
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para desassociar uma mesa de voto a uma eleição
     */
    @Override
    public String execute() throws Exception {
        if (this.getHeyBean().getAssociatedPollingStations().size() == 0) {
            message = "Não tem mesas de voto associadas!";
            addActionError(message);
            return ERROR;
        }
        return SUCCESS;
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
