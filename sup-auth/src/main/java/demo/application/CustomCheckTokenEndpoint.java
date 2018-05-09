package demo.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/my/oauth")
public class CustomCheckTokenEndpoint {

    protected final Log logger = LogFactory.getLog(getClass());
    private DefaultTokenServices defaultTokenServices;
    private JwtAccessTokenConverter accessTokenConverter;
    private ResourceServerTokenServices resourceServerTokenServices;
    private WebResponseExceptionTranslator exceptionTranslator = new DefaultWebResponseExceptionTranslator();


    public CustomCheckTokenEndpoint(DefaultTokenServices defaultTokenServices, JwtAccessTokenConverter accessTokenConverter) {
        this.defaultTokenServices = defaultTokenServices;
        this.accessTokenConverter = accessTokenConverter;
    }

    @PostMapping(value = "/check_token")
    public Map<String, ?> checkToken(String value) {
        OAuth2AccessToken token = defaultTokenServices.readAccessToken(value);
        if (token == null) {
            throw new InvalidTokenException("Token was not recognised");
        }

        if (token.isExpired()) {
            throw new InvalidTokenException("Token has expired");
        }

        OAuth2Authentication authentication = defaultTokenServices.loadAuthentication(token.getValue());

        Map<String, Object> response = (Map<String, Object>) accessTokenConverter.convertAccessToken(token, authentication);

        // gh-1070
        response.put("active", true);    // Always true if token exists and not expired

        return response;
    }

    /**
     * @param exceptionTranslator the exception translator to set
     */
    public void setExceptionTranslator(WebResponseExceptionTranslator exceptionTranslator) {
        this.exceptionTranslator = exceptionTranslator;
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
        logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        // This isn't an oauth resource, so we don't want to send an
        // unauthorized code here. The client has already authenticated
        // successfully with basic auth and should just
        // get back the invalid token error.
        @SuppressWarnings("serial")
        InvalidTokenException e400 = new InvalidTokenException(e.getMessage()) {
            @Override
            public int getHttpErrorCode() {
                return 400;
            }
        };
        return exceptionTranslator.translate(e400);
    }

}
