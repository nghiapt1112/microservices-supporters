package com.nghiatut.mss.support.edge.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

//@Controller
public class UserController {
//    @Autowired
//    private TokenStore tokenStore;
//
//    @PreAuthorize("#oauth2.hasScope('read')")
//    @RequestMapping(method = RequestMethod.GET, value = "/users/extra")
//    @ResponseBody
//    public Map<String, Object> getExtraInfo(OAuth2Authentication auth) {
//        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
//        final OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
//        System.out.println(accessToken);
//        return accessToken.getAdditionalInformation();
//    }
}
