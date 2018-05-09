package com.nghiatut.mss.support.edge.security;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class CustomOAuth2Authentication extends OAuth2Authentication {
    private Object additionalInfo;

    public CustomOAuth2Authentication(OAuth2Authentication authentication) {
        super(authentication.getOAuth2Request(), authentication.getUserAuthentication());
    }

    public Object getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Object additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
