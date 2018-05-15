package com.nghiatut.mss.support.edge.zuulconfig;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nghiatut.mss.support.edge.security.CustomOAuth2Authentication;
import com.nghiatut.mss.support.edge.util.TenantServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;


/**
 * <b>Pre</b> Filters execute before routing to the origin. <br>
 * Examples include request authentication choosing origin server, and logging debug info.
 */

@Component
public class CustomPreFilter extends ZuulFilter {
    @Autowired
    private Environment env;

    @Autowired
    private TenantServiceImpl tenantService;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

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

        CustomOAuth2Authentication auth2 = (CustomOAuth2Authentication) request.getUserPrincipal();

        LinkedHashMap additionalInfo = (LinkedHashMap) auth2.getAdditionalInfo();

        LinkedHashMap tenantInfo = (LinkedHashMap) additionalInfo.get("userTenantInfo");
//        String reqURI = (String) ctx.get(REQUEST_URI_KEY); // cai nay no chua khoi tao
        String originServletRequestURI = request.getRequestURI();


        //TODO: handle with tenantFilter.JAVA instead of code.
        String userTenantId = (String) tenantInfo.get("tenantId");
        String tenantName = tenantService.findTenantNameById(userTenantId);

        if (StringUtils.isNotEmpty(tenantName)) {
            ctx.put(SERVICE_ID_KEY, tenantName);
        }

        String forwardedAPI = tenantService.apisByTenantId(userTenantId, originServletRequestURI);
        if (StringUtils.isNotEmpty(forwardedAPI)) {
            ctx.put(REQUEST_URI_KEY, forwardedAPI);
        }

        return null;
    }
}
