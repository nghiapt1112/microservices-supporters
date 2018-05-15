package com.nghiatut.mss.support.edge.zuulconfig;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.nghiatut.mss.support.edge.security.CustomOAuth2Authentication;
import com.nghiatut.mss.support.edge.util.AbstractServiceImpl;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.WebUtils;
import reactor.core.publisher.Mono;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;


/**
 * <b>ROUTING</b> Filters handle routing the request to an origin.
 * This is where the origin HTTP request is build and sent using Apache HttpClient or Netflix Ribbon
 */

//@Component
public class CustomRoutingFilter extends ZuulFilter {

    private static final String CONTENT_TYPE = "Content-Type";

    @Autowired
    private ProxyRequestHelper helper;

    @Autowired
    private AbstractServiceImpl abstractService;

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
//        RequestContext ctx = RequestContext.getCurrentContext();
//        return Objects.nonNull(ctx.getRouteHost()) && ctx.sendZuulResponse();
        return true;
    }

    @Override
    public Object run()  {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                // customize
                .build();

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        String method = request.getMethod();

        String uri = this.helper.buildZuulRequestURI(request);



        Headers.Builder headers = new Headers.Builder();
        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String name = headerNames.nextElement();
//            Enumeration<String> values = request.getHeaders(name);
//
//            while (values.hasMoreElements()) {
//                String value = values.nextElement();
//                headers.add(name, value);
//            }
//        }
//
//        try {
//            InputStream inputStream = request.getInputStream();
//
//            RequestBody requestBody = null;
//            if (inputStream != null && HttpMethod.permitsRequestBody(method)) {
//                MediaType mediaType = null;
//                if (headers.get("Content-Type") != null) {
//                    mediaType = MediaType.parse(headers.get("Content-Type"));
//                }
//                requestBody = RequestBody.create(mediaType, StreamUtils.copyToByteArray(inputStream));
//            }
//
//            Request.Builder builder = new Request.Builder()
//                    .headers(headers.build())
//                    .url(uri)
//                    .method(method, requestBody);
//
//            Response response = httpClient.newCall(builder.build()).execute();
//
//            LinkedMultiValueMap<String, String> responseHeaders = new LinkedMultiValueMap<>();
//
//            for (Map.Entry<String, List<String>> entry : response.headers().toMultimap().entrySet()) {
//                responseHeaders.put(entry.getKey(), entry.getValue());
//            }
//
//            this.helper.setResponse(response.code(), response.body().byteStream(),
//                    responseHeaders);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        context.setRouteHost(null); // prevent SimpleHostRoutingFilter from running
        return null;
    }


}