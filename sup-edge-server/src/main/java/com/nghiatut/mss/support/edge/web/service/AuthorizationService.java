package com.nghiatut.mss.support.edge.web.service;

import com.nghiatut.mss.support.edge.security.CustomOAuth2Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    public boolean isValid(CustomOAuth2Authentication authentication) {
        if (authentication.getUserAuthentication().getPrincipal().toString().equalsIgnoreCase("ADMIN")) {
            return true;
        }
        return false;
    }
}
