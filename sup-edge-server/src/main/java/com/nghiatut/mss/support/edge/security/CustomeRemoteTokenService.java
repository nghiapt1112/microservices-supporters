package com.nghiatut.mss.support.edge.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Base64;
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
import java.util.Map;


@Service
public class CustomeRemoteTokenService extends RemoteTokenServices {

    @Value("${my.security.oauth2.client.client-id}")
    private String CLIENT_ID;

    @Value("${my.security.oauth2.client.client-secret}")
    private String CLIENT_SECRET;

    @Value("${my.security.oauth2.remote-token-endpoint.uri}")
    private String CHECK_TOKEN_ENDPOINT_URL;

    private RestTemplate restTemplate;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    public CustomeRemoteTokenService() {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token", accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getAuthorizationHeader(CLIENT_ID, CLIENT_SECRET));
        Map<String, Object> map = postForMap(CHECK_TOKEN_ENDPOINT_URL, formData, headers);

        if (map.containsKey("error")) {
            logger.debug("check_token returned error: " + map.get("error"));
            throw new InvalidTokenException(accessToken);
        }

        // gh-838 :RemoteTokenServices.loadAuthentication() should not check for client_id
        if (!Boolean.TRUE.equals(map.get("active"))) {
            logger.debug("check_token returned active attribute: " + map.get("active"));
            throw new InvalidTokenException(accessToken);
        }

        OAuth2Authentication authentication = accessTokenConverter.extractAuthentication(map);


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
            return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Could not convert String");
        }
    }

    private Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        @SuppressWarnings("rawtypes")
        Map map = restTemplate.exchange(path, HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, String>>(formData, headers), Map.class).getBody();
        @SuppressWarnings("unchecked")
        Map<String, Object> result = map;
        return result;
    }


    @Override
    public void setClientId(String clientId) {
        this.CLIENT_ID = clientId;
    }

    @Override
    public void setClientSecret(String clientSecret) {
        this.CLIENT_SECRET = clientSecret;
    }

    @Override
    public void setCheckTokenEndpointUrl(String checkTokenEndpointUrl) {
        this.CHECK_TOKEN_ENDPOINT_URL = checkTokenEndpointUrl;
    }

    public void setAccessTokenConverter(JwtAccessTokenConverter accessTokenConverter) {
        this.accessTokenConverter = accessTokenConverter;
    }
}
