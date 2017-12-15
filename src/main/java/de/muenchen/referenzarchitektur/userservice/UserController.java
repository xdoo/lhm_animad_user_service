package de.muenchen.referenzarchitektur.userservice;

import de.muenchen.referenzarchitektur.authorisationLib.EntitlementsService;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;
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
        Set<String> permissions = entitlementsService.getPermissions(false);
        if (permissions != null) {
            return permissions.toString();
        } else {
            return "No Permissions found.";
        }
    }

    @RequestMapping(value = "/getPermissionsDummy", method = RequestMethod.GET)
    public Set<String> getPermissionsDummy() {
        LOG.info("Called getPermissionsDummy");
        Set<String> permissions = new HashSet<String>();
        permissions.add("RESOURCE3");
        permissions.add("RESOURCE2");
        permissions.add("RESOURCE1");
        permissions.add("Default Resource");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.info("Returning getPermissionsDummy");
        return permissions;
    }

}
