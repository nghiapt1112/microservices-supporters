package demo.domain.service;

import demo.domain.Tenant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class TenantServiceImpl {

    /**
     * "read",
     * "write",
     * "user1",
     * "admin",
     */
    public Tenant findTenantByIUser(String userName) {
        Tenant tenant = null;
        if (StringUtils.equals(userName, "read")) tenant = new Tenant("t1", "tenant1");
        if (StringUtils.equals(userName, "write")) tenant = new Tenant("t1", "tenant1");
        if (StringUtils.equals(userName, "user1")) tenant = new Tenant("t2", "tenant2");
        if (StringUtils.equals(userName, "admin")) tenant = new Tenant("t2", "tenant2");

        return tenant;
    }
}
