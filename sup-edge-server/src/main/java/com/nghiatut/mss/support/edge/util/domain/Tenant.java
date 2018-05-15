package demo.domain;

import com.nghia.libraries.commons.mss.infrustructure.domain.AbstractObject;

public class Tenant extends AbstractObject {

    private String tenantId;
    private String tenantName;

    public Tenant() {
    }

    public Tenant(String tenantId, String tenantName) {
        this.tenantId = tenantId;
        this.tenantName = tenantName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

}
