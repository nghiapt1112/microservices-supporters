package demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public class Roles implements GrantedAuthority {
    private Integer roleId;

    private String roleName;

    private String roleDescription;

    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Roles() {
    }

    public Roles(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName == null ? "" : roleName;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return roleName;
    }

    @JsonIgnore
    public String getRoleNameUpperCase() {
        return this.getRoleName().toUpperCase();
    }
}
