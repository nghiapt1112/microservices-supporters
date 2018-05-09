package demo.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class TokenUserInfo {
    public String email;
    public String fullName;
    public Collection<Roles> roles;
    public boolean forceChange;
    public boolean disable;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
