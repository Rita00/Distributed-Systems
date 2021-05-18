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
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private Map<String, Object> session;
    private static final Token EMPTY_TOKEN = null;
    private String code = null;
    private String authorizationUrl = null;

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

    @Override
    public String execute() throws Exception {
        String apiKey = "1345313155825147";
        String apiSecret = "f6c3ca41446cc2d017a37650223f581c";


        OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("http://localhost:8080/webserver/loginFacebookForRealThisTime") // Do not change this.
                .scope("public_profile")
                .build();

        this.authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        return SUCCESS;
    }

    public String login() throws Exception {
        String message = "";
        String apiKey = "1345313155825147";
        String apiSecret = "f6c3ca41446cc2d017a37650223f581c";


        OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("http://localhost:8080/webserver/loginFacebookForRealThisTime") // Do not change this.
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
            session.put("ccnumber", p.getCc_number());
            session.put("loggedin", true); // this marks the user as logged in
            return SUCCESS;
        } else {
            message = "Conta não associada!";
            addActionError(message);
            return ERROR;
        }


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
