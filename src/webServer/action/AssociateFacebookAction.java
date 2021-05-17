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
import java.util.Scanner;

public class AssociateFacebookAction extends ActionSupport implements SessionAware {
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private Map<String, Object> session;
    private static final Token EMPTY_TOKEN = null;
    private String code = null;
    private String authorizationUrl = null;


    @Override
    public String execute() throws Exception {
        String apiKey = "1345313155825147";
        String apiSecret = "f6c3ca41446cc2d017a37650223f581c";


        OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("http://localhost:8080/webserver/associateFacebookForRealThisTime") // Do not change this.
                .scope("public_profile")
                .build();

        this.authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        return SUCCESS;
    }

    public String associate() throws Exception {
        String apiKey = "1345313155825147";
        String apiSecret = "f6c3ca41446cc2d017a37650223f581c";


        OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("http://localhost:8080/webserver/associateFacebookForRealThisTime") // Do not change this.
                .scope("public_profile")
                .build();

//        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);

        Verifier verifier = new Verifier(this.code);
        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        JSONObject json = (JSONObject) JSONValue.parse(response.getBody());
        String FBPerson_id = (String) json.get("id");
        this.getHeyBean().associateFbId(FBPerson_id);
        return SUCCESS;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
