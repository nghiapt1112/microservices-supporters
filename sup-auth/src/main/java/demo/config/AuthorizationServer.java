package demo.config;

import demo.domain.Roles;
import demo.domain.Tenant;
import demo.domain.TokenUserInfo;
import demo.domain.service.TenantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    private static final String ADDITIONAL_USER_INFO = "user";
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TenantServiceImpl tenantService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints
//                .tokenServices(tokenServices())
//                .authenticationManager(this.authenticationManager); // DCC way

//        endpoints.pathMapping("/oauth/check_token", "/my/oauth/check_token");
//        endpoints.pathMapping("/my/oauth/check_token", "/oauth/check_token");

        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.checkTokenAccess("permitAll()");
//        oauthServer.allowFormAuthenticationForClients();
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("acme")
                .secret("acmesecret")
                .authorizedGrantTypes("authorization_code", "refresh_token", "implicit", "password", "client_credentials")
                .scopes("webshop")
                .and()
                .withClient("sampleClientId")
                .authorizedGrantTypes("implicit")
                .scopes("read", "write", "foo", "bar")
                .autoApprove(false)
                .accessTokenValiditySeconds(3600)

                .and()
                .withClient("fooClientIdPassword")
                .secret("secret")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .scopes("foo", "read", "write")
                .accessTokenValiditySeconds(3600)// 1 hour
                .refreshTokenValiditySeconds(2592000)// 30 days

                .and()
                .withClient("barClientIdPassword")
                .secret("secret")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .scopes("bar", "read", "write")
                .accessTokenValiditySeconds(3600)
                // 1 hour
                .refreshTokenValiditySeconds(2592000) // 30 days
        ;
    }

    @Bean
    @Primary
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setRefreshTokenValiditySeconds(345600); // 15 days
        tokenServices.setAccessTokenValiditySeconds(172800); // 2 days
        return tokenServices;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }

    @SuppressWarnings("unused")
    @Bean
    public JwtAccessTokenConverter keyStoreAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("nghia_key");

        final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("nghiakey.jks"), "nghiapass".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("nghiatestalias"));
        return converter;
    }

    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            try {
                org.springframework.security.core.userdetails.User springUserDetails = this.getPrincipal(authentication);
                final Map<String, Object> additionalInfo = new HashMap<>();
                String name = springUserDetails.getUsername();

                Tenant userTenant = tenantService.findTenantByIUser(name);
                TokenUserInfo userInfo = new TokenUserInfo();
                userInfo.setName(name);
                userInfo.setRoles(springUserDetails.getAuthorities()
                        .stream()
                        .map(el -> new Roles(el.getAuthority()))
                        .collect(Collectors.toList())
                );
                userInfo.setUserTenantInfo(userTenant);
                userInfo.setDisable(!springUserDetails.isEnabled());

                additionalInfo.put(ADDITIONAL_USER_INFO, userInfo);
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

                return accessToken;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        };
    }

    private org.springframework.security.core.userdetails.User getPrincipal(OAuth2Authentication authentication) {
        return (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
    }

}


