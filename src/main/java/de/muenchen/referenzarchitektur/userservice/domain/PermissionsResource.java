package de.muenchen.referenzarchitektur.userservice.domain;

import java.util.Set;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author roland
 */
public class PermissionsResource extends ResourceSupport {
    
    private Set<String> permissions;

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
