package com.nghiatut.mss.support.edge.zuulconfig;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ROUTE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SIMPLE_HOST_ROUTING_FILTER_ORDER;

/**
 * <b>ROUTING</b> Filters handle routing the request to an origin.
 * This is where the origin HTTP request is build and sent using Apache HttpClient or Netflix Ribbon
 */

//@Component
public class CustomRoutingFilter extends ZuulFilter {

    private static final String CONTENT_TYPE = "Content-Type";

    @Autowired
    private ProxyRequestHelper helper;

    @Override
    public String filterType() {
        return ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SIMPLE_HOST_ROUTING_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return Objects.nonNull(ctx.getRouteHost()) && ctx.sendZuulResponse();
    }

    @Override
    public Object run() {
        OkHttpClient httpClient = new OkHttpClient().newBuilder().build();
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();

        String method = request.getMethod();
        String uri = this.helper.buildZuulRequestURI(request);

        Headers.Builder headers = this.parseHeaders(request);
        RequestBody reqBody = this.parseRequestBody(request, headers, method);

        Request.Builder builder = new Request.Builder()
                .headers(headers.build())
                .url(uri)
                .method(method, reqBody);

        try (Response response = httpClient.newCall(builder.build()).execute()) {
            MultiValueMap<String, String> resHeaders = new LinkedMultiValueMap<>();
            for (Map.Entry<String, List<String>> entry : response.headers().toMultimap().entrySet()) {
                resHeaders.put(entry.getKey(), entry.getValue());
            }
            this.helper.setResponse(response.code(), response.body().byteStream(), resHeaders);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestContext.getCurrentContext().setRouteHost(null); // prevent SimpleHostRoutingFilter from running
        return null;
    }

    private Headers.Builder parseHeaders(HttpServletRequest request) {
        Headers.Builder headers = new Headers.Builder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Enumeration<String> values = request.getHeaderNames();
            while (values.hasMoreElements()) {
                String val = values.nextElement();
                headers.add(name, val);
            }
        }
        return headers;
    }

    private RequestBody parseRequestBody(HttpServletRequest request, Headers.Builder headers, String method) {
        try (ServletInputStream iStream = request.getInputStream()) {
            if (StringUtils.isNotEmpty(method)
                    && Objects.nonNull(iStream)
                    && HttpMethod.permitsRequestBody(method)) {
                MediaType mediaType = null;
                if (StringUtils.isNotEmpty(headers.get(CONTENT_TYPE))) {
                    mediaType = MediaType.parse(headers.get(CONTENT_TYPE));
                }
                return RequestBody.create(mediaType, StreamUtils.copyToByteArray(iStream));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}