package webServer.action;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import uc.sd.apis.FacebookApi2;
import webServer.model.HeyBean;

import java.util.Map;

public class VoteAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private int candidacy_id, blank_vote, null_vote;
    String message = "";
    private static final String PROTECTED_RESOURCE_URL = "https://www.facebook.com/dialog/share?";
    private static final Token EMPTY_TOKEN = null;
    private String code = null;
    private String authorizationUrl = null;

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setCandidacy_id(this.candidacy_id);
        // verifica se tem sessão iniciada para pode votar
        if (session.get("loggedin") != null) {
            boolean res = (Boolean) session.get("loggedin");
            // Verifica se estás on ou off
            if (!res) {
                message = "Não tem sessão iniciada.";
                addActionError(message);
                return ERROR;
            } else {
                if (!this.getHeyBean().checkIfAlreadyVoteOnVoteForm()) {
                    this.getHeyBean().updateVotes();

                    String apiKey = "1345313155825147";
                    String apiSecret = "f6c3ca41446cc2d017a37650223f581c";

                    OAuthService service = new ServiceBuilder()
                            .provider(FacebookApi2.class)
                            .apiKey(apiKey)
                            .apiSecret(apiSecret)
                            .callback("http://localhost:8080/webserver/postVote") // Do not change this.
                            .scope("public_profile")
                            .build();
                    this.authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
                    this.authorizationUrl  = "https://www.facebook.com/dialog/share?app_id=" + apiKey + "&display=popup&href=http://localhost:8080/webserver/index.action";
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

    public String postVote() throws Exception {
        message = "Voto efetuado com sucesso!";
        addActionMessage(message);

        String apiKey = "1345313155825147";
        String apiSecret = "f6c3ca41446cc2d017a37650223f581c";

        OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("http://localhost:8080/webserver/postVote") // Do not change this.
                .scope("public_profile")
                .build();

        Verifier verifier = new Verifier(this.code);

        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);

        // Now let's go and ask for a protected resource!

        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        Response response = request.send();
        JSONObject json = (JSONObject) JSONValue.parse(response.getBody());

        String message = "Votei na eleição " + this.getHeyBean().getTitleElection() + "! Acede ao link para visitares a página e poderes votar!";
        String postit = "https://www.facebook.com/dialog/share?app_id=" + apiKey + "&display=popup&href=http://localhost:8080/webserver/index.action";
        System.out.println(postit);
        postit = postit.replace(" ", "%20");

        OAuthRequest post = new OAuthRequest(Verb.POST, postit, service);
        service.signRequest(accessToken, post);

        response = post.send();
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        return SUCCESS;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public int getCandidacy_id() {
        return candidacy_id;
    }

    public void setCandidacy_id(int candidacy_id) {
        this.candidacy_id = candidacy_id;
    }

    public int getBlank_vote() {
        return blank_vote;
    }

    public int getNull_vote() {
        return null_vote;
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
