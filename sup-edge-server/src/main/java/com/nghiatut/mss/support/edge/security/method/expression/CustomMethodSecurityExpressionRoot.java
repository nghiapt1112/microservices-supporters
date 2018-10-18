package com.nghiatut.mss.support.edge.security.method.expression;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * Custom method expression:
 * Adding #isMember() expression
 */
public class CustomMethodSecurityExpressionRoot  extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }


    public boolean isMember(Long OrganizationId) {
        System.out.println("Checking organization id: " + OrganizationId);
//        final User user = ((MyUserPrincipal) this.getPrincipal()).getUser();
//        return user.getOrganization().getId().longValue() == OrganizationId.longValue();
        return true;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    @Override
    public void setFilterObject(Object obj) {
        this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj) {
        this.returnObject = obj;
    }


}