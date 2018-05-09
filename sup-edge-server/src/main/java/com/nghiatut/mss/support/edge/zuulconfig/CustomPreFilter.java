package com.nghiatut.mss.support.edge.zuulconfig;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nghiatut.mss.support.edge.security.CustomOAuth2Authentication;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.LinkedHashMap;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;

/**
 * <b>Pre</b> Filters execute before routing to the origin. <br>
 * Examples include request authentication choosing origin server, and logging debug info.
 */

//@Component
public class CustomPreFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Autowired
    private Environment env;

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
//        RequestContext ctx = RequestContext.getCurrentContext();
//        return !ctx.containsKey(FORWARD_TO_KEY) // a filter has already forwarded
//                && !ctx.containsKey(SERVICE_ID_KEY); // a filter has already determined serviceId
    }

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        Authentication auth1 = SecurityContextHolder.getContext().getAuthentication();

        CustomOAuth2Authentication auth2 = (CustomOAuth2Authentication) request.getUserPrincipal();

        LinkedHashMap additionalInfo = (LinkedHashMap) auth2.getAdditionalInfo();
        String requestURI = request.getRequestURI();
//        accessTokenConverter
        String hostRabbit = env.getProperty("spring.rabbitmq.host");
        System.out.println(hostRabbit);
        if (StringUtils.isNotEmpty(request.getParameter("sample"))) {
            // put the serviceId in `RequestContext`
            ctx.put(SERVICE_ID_KEY, request.getParameter("foo"));

        }
        return null;
    }
}
