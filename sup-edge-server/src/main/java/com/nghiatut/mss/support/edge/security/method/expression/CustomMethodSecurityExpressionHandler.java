package com.nghiatut.mss.support.edge.security.method.expression;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * chỉ định cái expression nào sẽ được sử dụng
 */
public class CustomMethodSecurityExpressionHandler extends OAuth2MethodSecurityExpressionHandler {
    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        final CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication);

        // Other option: Disabled hasAuthority() method.
        //  final MySecurityExpressionRoot root = new MySecurityExpressionRoot(authentication);

        root.setPermissionEvaluator(new CustomPermissionEvaluator());
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }
}
