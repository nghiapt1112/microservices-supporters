package demo.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class TokenUserInfo extends AbstractObject {
    private String name;
    private String fullName;
    private Collection<Roles> roles;
    private boolean forceChange;
    private boolean disable;
    private Tenant userTenantInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Collection<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Roles> roles) {
        this.roles = roles;
    }

    public boolean isForceChange() {
        return forceChange;
    }

    public void setForceChange(boolean forceChange) {
        this.forceChange = forceChange;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public void updateRoles(final String ROLE_PREFIX, final String EMPTY_STRING, Collection<GrantedAuthority> authorities) {
        if (CollectionUtils.isEmpty(authorities)) {
            this.roles = Collections.emptyList();
        } else {
            this.roles = authorities.stream()
                    .map(a -> new Roles(a.getAuthority().replace(ROLE_PREFIX, EMPTY_STRING))) // to front-end ROLE.
                    .collect(Collectors.toList());
        }
    }

    public Tenant getUserTenantInfo() {
        return userTenantInfo;
    }

    public void setUserTenantInfo(Tenant userTenantInfo) {
        this.userTenantInfo = userTenantInfo;
    }
}
