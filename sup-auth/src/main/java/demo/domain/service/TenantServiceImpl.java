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
        if (StringUtils.equals(userName, "test_common")) tenant = new Tenant("tcommon", "tcommon");
        if (StringUtils.equals(userName, "test1")) tenant = new Tenant("t1", "tenant1");
        if (StringUtils.equals(userName, "test2")) tenant = new Tenant("t2", "tenant2");
        if (StringUtils.equals(userName, "admin")) tenant = new Tenant("tcommon", "tcommon");
        if (StringUtils.equals(userName, "test3")) tenant = new Tenant("tcommon", "tcommon");
        if (StringUtils.equals(userName, "test4")) tenant = new Tenant("tcommon", "tcommon");

        return tenant;
    }
}
