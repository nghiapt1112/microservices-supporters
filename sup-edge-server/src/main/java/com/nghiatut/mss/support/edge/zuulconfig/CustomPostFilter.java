package com.nghiatut.mss.support.edge.zuulconfig;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;

/**
 * <b>POST</b> Filters execute after the request has been routed to the origin.<br>
 * It manipulate the response.
 * Examples include adding standard HTTP headers to the response , gathering statistics and metrics, and streaming the response from the origin to the client.
 */

//@Component
public class CustomPostFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse servletResponse = ctx.getResponse();
        servletResponse.addHeader("X-nghia", UUID.randomUUID().toString());
        return null;
    }
}
