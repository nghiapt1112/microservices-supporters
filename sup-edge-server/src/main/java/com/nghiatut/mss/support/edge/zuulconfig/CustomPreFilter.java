package com.nghiatut.mss.support.edge.zuulconfig;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;

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
        String hostRabbit = env.getProperty("spring.rabbitmq.host");
        System.out.println(hostRabbit);
        if (StringUtils.isNotEmpty(request.getParameter("sample"))) {
            // put the serviceId in `RequestContext`
            ctx.put(SERVICE_ID_KEY, request.getParameter("foo"));

        }
        return null;
    }
}
