package com.nghiatut.mss.support.edge.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
//TODO: Modify Data structure
public class TenantServiceImpl {
    Map<String, Map<String, String>> uriByTenantId = new HashMap<>();

    @PostConstruct
    // NOTE: prepare Data for
    public void init() {
        Map<String, String> tenant1URI = new HashMap<>();

        tenant1URI.put("name", "test-tenant1");
        tenant1URI.put("/compose/composite/findBy", "compose1/composite/findBy");

        Map<String, String> tenant2URI = new HashMap<>();
        tenant2URI.put("name", "test-tenant2");
        tenant2URI.put("/compose/composite/findBy", "compose2/composite/findBy");


        uriByTenantId.put("t1", tenant1URI);
        uriByTenantId.put("t2", tenant2URI);
    }

    public String apisByTenantId(String tenantId, String uri) {
        Map<String, String> uriByTenant = this.uriByTenantId.get(tenantId);
        if (Objects.isNull(uriByTenant)) {
            return uri;
        }
        String forwardedURI = uriByTenant.get(uri);
        if (StringUtils.isEmpty(forwardedURI)) {
            return uri;
        }
        return forwardedURI;
    }

    public String findTenantNameById(String tenantId) {
        if (uriByTenantId.get(tenantId) != null)
            return uriByTenantId.get(tenantId).get("name");
        else return "";
    }
}
