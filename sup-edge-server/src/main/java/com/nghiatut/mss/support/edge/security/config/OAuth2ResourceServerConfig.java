package com.nghiatut.mss.support.edge.security.config;

import com.nghiatut.mss.support.edge.security.CustomeRemoteTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableResourceServer
@RefreshScope
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private CustomeRemoteTokenService tokenServices;

    @Autowired
    private OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler;

    @Value("${my.security.issuer-claim-verifier.url}")
    private String issuerClaimVerifierURL;
    @Value("${my.security.oauth2.user-service.path}")
    private String USER_URI;
    @Value("${my.security.oauth2.user-service.expression}")
    private String USER_EXPRESSION;

    @Value("${my.security.oauth2.user-service.path}")
    private String FOO_URI;
    @Value("${my.security.oauth2.user-service.expression}")
    private String FOO_EXPRESSION;


    @Value("${my.security.oauth2.composite-service.path}")
    private String COMPOSITE_URI;
    @Value("${my.security.oauth2.composite-service.expression}")
    private String COMPOSITE_EXPRESSION;

    @Value("${my.security.oauth2.backend-service.path}")
    private String BACKEND_URI;
    @Value("${my.security.oauth2.backend-service.expression}")
    private String BACKEND_EXPRESSION;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .antMatchers("/swagger*", "/v2/**")
                .permitAll()
                .antMatchers(FOO_URI).access(FOO_EXPRESSION)
                .antMatchers(USER_URI).access(USER_EXPRESSION)
                .antMatchers(COMPOSITE_URI).access(COMPOSITE_EXPRESSION)
                .antMatchers(BACKEND_URI).access(BACKEND_EXPRESSION)
                .antMatchers("/bars/check-te*").access("@authorizationService.isValid(authentication)")
                .anyRequest().permitAll()
        ;
    }

    /**
     * Register CustomTokenService as default, or can using @Primary as primary bean for tokenService
     *
     * @param config
     */
    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenServices).expressionHandler(oAuth2WebSecurityExpressionHandler);
    }

    /**
     * When using remote token service, we don't need to do much, cause by Auth-Server will checkValid(), parseToken(),
     * and every thing else to make our token valid to use.
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter remoteAccessTokenConverter() {
        return new JwtAccessTokenConverter();
    }

}
