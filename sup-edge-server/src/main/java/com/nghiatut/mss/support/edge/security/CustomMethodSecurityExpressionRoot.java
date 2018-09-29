package com.nghiatut.mss.support.edge.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot  extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
 
    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }


    public boolean isMember(Long OrganizationId) {
//        User user = ((MyUserPrincipal) this.getPrincipal()).getUser();
//        return user.getOrganization().getId().longValue() == OrganizationId.longValue();
        return true;
    }
 

}