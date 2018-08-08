package com.nghiatut.mss.support.edge.security;

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
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

@Configuration
@EnableResourceServer
@RefreshScope
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private CustomeRemoteTokenService tokenServices;


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
                .antMatchers("/swagger*", "/v2/**").permitAll()
                .antMatchers(FOO_URI).access(FOO_EXPRESSION)
                .antMatchers(USER_URI).access(USER_EXPRESSION)
                .antMatchers(COMPOSITE_URI).access(COMPOSITE_EXPRESSION)
                .antMatchers(BACKEND_URI).access(BACKEND_EXPRESSION)
                .anyRequest().permitAll()
        ;
    }

    // JWT token store

    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenServices);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
//        converter.setJwtClaimsSetVerifier(jwtClaimsSetVerifier());
//
//        final Resource resource = new ClassPathResource("public.txt");
//        String publicKey = null;
//        try {
//            publicKey = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
//        } catch (final IOException e) {
//            throw new RuntimeException(e);
//        }
//        converter.setVerifierKey(publicKey);
        return converter;
    }

    @Bean
    public JwtClaimsSetVerifier jwtClaimsSetVerifier() {
        return new DelegatingJwtClaimsSetVerifier(Arrays.asList(issuerClaimVerifier(), customJwtClaimVerifier()));
    }

    @Bean
    public JwtClaimsSetVerifier issuerClaimVerifier() {
        try {
            return new IssuerClaimVerifier(new URL(issuerClaimVerifierURL));
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public JwtClaimsSetVerifier customJwtClaimVerifier() {
        return new CustomClaimVerifier();
    }

}
