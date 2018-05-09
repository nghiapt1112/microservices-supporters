package demo.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/tk")
public class TokenController {

    @Autowired
    DefaultTokenServices defaultTokenServices;

    @Resource(name = "tokenStore")
    private TokenStore tokenStore;

    @PostMapping(value = "/oauth/token/revokeById/{tokenId}")
    public void revokeToken(@PathVariable String tokenId) {
        defaultTokenServices.revokeToken(tokenId);
    }

    @GetMapping(value = "/tokens")
    public List<String> getTokens(@RequestParam String clientId) {
        List<String> tokenValues = new ArrayList<>();
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId(clientId);
        if (!CollectionUtils.isEmpty(tokens)) {
            for (OAuth2AccessToken token : tokens) {
                tokenValues.add(token.getValue());
            }
        } else {
            tokenValues.add("nghia-empty");
        }
        return tokenValues;
    }

    @PostMapping(value = "/tokens/revokeRefreshToken/{tokenId:.*}")
    public String revokeRefreshToken(@PathVariable String tokenId) {
        if (tokenStore instanceof JdbcTokenStore) {
            ((JdbcTokenStore) tokenStore).removeRefreshToken(tokenId);
        }
        return tokenId;
    }

}
