package webServer.action;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import pt.uc.dei.student.elections.Person;
import uc.sd.apis.FacebookApi2;
import webServer.model.HeyBean;

import java.util.Map;

public class LoginFacebookAction extends ActionSupport implements SessionAware {
    /**
     * Url para a API do facebook usado para executar queries que obtenham informação sobre o utilizador atual
     */
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";

    /**
     * Guarda a sessão de um determinada utilizador
     */
    private Map<String, Object> session;

    /**
     * Token de autorização para aceder à informação do utilizador atual
     */
    private static final Token EMPTY_TOKEN = null;
    private String code = null;

    /**
     * URL para reencaminhar o utilizador para uma página onde irá autorizar o acesso da aplicação à sua informação do facebook
     */
    private String authorizationUrl = null;

    /**
     * Getter para o código
     * @return código
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter para o código
     * @param code código
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter para o URL de autorização
     *
     * @return URL de autorização
     */
    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    /**
     * Setter para o URL de autorização
     *
     * @param authorizationUrl URL de autorização
     */
    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    /**
     * Redireciona o utilizador para a página de autorização
     * definindo que após a autorização será reencaminhado para o link: http://localhost:8080/webserver/loginFacebookForRealThisTime
     *
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para efetuar o login de um utilizador
     */
    @Override
    public String execute() throws Exception {
        String apiKey = "1345313155825147";
        String apiSecret = "f6c3ca41446cc2d017a37650223f581c";
        OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("http://sd-dylanrita.ddns.net:8080/webserver/loginFacebookForRealThisTime") // Do not change this.
                .scope("public_profile")
                .build();
        this.authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        return SUCCESS;
    }

    /**
     * Efetua login com o facebook a um determinado utilizador
     *
     * @return String que informa o ficheiro struts que página deve ser apresentada
     * @throws Exception Processa o pedido para efetuar login de um determinado utilizador
     */
    public String login() throws Exception {
        String message = "";
        String apiKey = "1345313155825147";
        String apiSecret = "f6c3ca41446cc2d017a37650223f581c";


        OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("http://sd-dylanrita.ddns.net:8080/webserver/loginFacebookForRealThisTime") // Do not change this.
                .scope("public_profile")
                .build();

        Verifier verifier = new Verifier(this.code);
        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        JSONObject json = (JSONObject) JSONValue.parse(response.getBody());
        String FBPerson_id = (String) json.get("id");

        Person p = this.getHeyBean().getUserFb(FBPerson_id);
        if (p != null) {
            this.getHeyBean().setCcnumber(p.getCc_number());
            this.getHeyBean().updateStatusLogin();
            session.put("ccnumber", p.getCc_number());
            session.put("loggedin", true); // this marks the user as logged in
            return SUCCESS;
        } else {
            message = "Conta não associada!";
            addActionError(message);
            return ERROR;
        }
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
