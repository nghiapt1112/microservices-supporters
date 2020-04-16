package demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

public class Roles extends AbstractObject implements GrantedAuthority {
    private Integer roleId;

    private String roleName;

    private String roleDescription;

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

}
