package com.nghiatut.mss.support.edge.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Base64;
import java.util.Map;


@Service
public class CustomeRemoteTokenService extends RemoteTokenServices {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Value("${my.security.oauth2.client.client-id}")
    private String CLIENT_ID;

    @Value("${my.security.oauth2.client.client-secret}")
    private String CLIENT_SECRET;

    @Value("${my.security.oauth2.remote-token-endpoint.uri}")
    private String CHECK_TOKEN_ENDPOINT_URI;

    @Value("${my.security.oauth2.remote-token-endpoint.authServiceId}")
    private final String AUTH_SERVICE_ID = "SUP-AUTH";

    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;
    //
    @Autowired
    private JwtAccessTokenConverter remoteAccessTokenConverter;

    public CustomeRemoteTokenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }

    /**
     * Using CustomOAuth2Authentication to response user-data
     */
    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token", accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getAuthorizationHeader(CLIENT_ID, CLIENT_SECRET));
        Map<String, Object> map = postForMap(CHECK_TOKEN_ENDPOINT_URI, formData, headers);

        if (map.containsKey("error")) {
            logger.debug("check_token returned error: " + map.get("error"));
            throw new InvalidTokenException(accessToken);
        }

        // gh-838 :RemoteTokenServices.loadAuthentication() should not check for client_id
        if (!Boolean.TRUE.equals(map.get("active"))) {
            logger.debug("check_token returned active attribute: " + map.get("active"));
            throw new InvalidTokenException(accessToken);
        }

        OAuth2Authentication authentication = remoteAccessTokenConverter.extractAuthentication(map);


        CustomOAuth2Authentication customOAuth2Authentication = new CustomOAuth2Authentication(authentication);

        customOAuth2Authentication.setAdditionalInfo(map.get("user"));

        return customOAuth2Authentication;
    }

    private String getAuthorizationHeader(String clientId, String clientSecret) {

        if (clientId == null || clientSecret == null) {
            logger.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
        }

        String creds = String.format("%s:%s", clientId, clientSecret);
        try {
            return "Basic " + new String(Base64.getEncoder().encode(creds.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Could not convert String");
        }
    }

    private Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }

        String fallbackURI = ""; // TODO: implement fallbackURI
        URI uri = this.getServiceURL(AUTH_SERVICE_ID, fallbackURI);

        StringBuilder serviceUrl = new StringBuilder(uri.toString());
        if (!this.urlEndWithSlash(serviceUrl)) {
            serviceUrl = serviceUrl.append("/");
        }
        serviceUrl.append(CHECK_TOKEN_ENDPOINT_URI);

        return restTemplate.exchange(serviceUrl.toString(), HttpMethod.POST, new HttpEntity<>(formData, headers), Map.class).getBody();
    }

    private boolean urlEndWithSlash(StringBuilder serviceUrl) {
        return serviceUrl.lastIndexOf("/") == serviceUrl.length() - 1;
    }

    //TODO: Duplicate code with com.nghia.libraries.commons.mss.infrustructure.service.AbstractService.java
    protected URI getServiceURL(String serviceId, String fallbackURI) {
        URI uri;
        try {
            ServiceInstance instance = this.loadBalancerClient.choose(serviceId);
            uri = instance.getUri();
            LOG.debug("Resolved serviceId '{}' to URL '{}'.", serviceId, uri);
        } catch (Exception e) {
            LOG.warn("Cannot get Instance of {}, trying to use defaultURI:", serviceId, fallbackURI);
            LOG.error("Error cause: {}, \nError message: {}", e.getCause(), e.getMessage());
            uri = URI.create(fallbackURI);
        }
        return uri;
    }
}
