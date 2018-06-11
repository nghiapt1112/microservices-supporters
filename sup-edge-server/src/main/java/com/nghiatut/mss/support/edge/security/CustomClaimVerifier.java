package com.nghiatut.mss.support.edge.security;

import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;

import java.util.Map;

@SuppressWarnings("unused")
public class CustomClaimVerifier implements JwtClaimsSetVerifier {
    @Override
    public void verify(Map<String, Object> claims) throws InvalidTokenException {
        final String username = (String) claims.get("user_name");
        if ((username == null) || (username.length() == 0)) {
            throw new InvalidTokenException("user_name claim is empty");
        }
    }
}
