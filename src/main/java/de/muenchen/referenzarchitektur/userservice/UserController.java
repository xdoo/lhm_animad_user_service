package de.muenchen.referenzarchitektur.userservice;

import de.muenchen.referenzarchitektur.authorisationLib.EntitlementsService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author rowe42
 */
@RestController
public class UserController {

    private final EntitlementsService entitlementsService;

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    public UserController(EntitlementsService entitlementsService) {
        this.entitlementsService = entitlementsService;
    }

    @RequestMapping(value = "/getPermissions", method = RequestMethod.GET)
    public String getPermissions() {
        Set<String> permissions = entitlementsService.getPermissions(true);
        if (permissions != null) {
            return permissions.toString();
        } else {
            return "No Permissions found.";
        }
    }

    @RequestMapping(value = "/getPermissionsDummy", method = RequestMethod.GET)
    public Set<String> getPermissionsDummy() {
        Set<String> permissions = new HashSet<String>();
        permissions.add("RESOURCE2");
        permissions.add("RESOURCE1");
        permissions.add("Default Resource");
        return permissions;
    }

}
