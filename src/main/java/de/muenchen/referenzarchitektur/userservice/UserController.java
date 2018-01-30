package de.muenchen.referenzarchitektur.userservice;

import de.muenchen.referenzarchitektur.authorisationLib.EntitlementsService;
import de.muenchen.referenzarchitektur.userservice.domain.PermissionsResource;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
        Set<String> permissions = entitlementsService.getPermissions(true, false);
        if (permissions != null) {
            return permissions.toString();
        } else {
            return "No Permissions found.";
        }
    }
    
    @RequestMapping(value = "/getPermissions", method = RequestMethod.OPTIONS)
    public String getOptions() {
        return "Body";
    }    

    @CrossOrigin(origins = "http://127.0.0.1:8081")
    @RequestMapping(value = "/getPermissionsDummy", method = RequestMethod.GET)
    public ResponseEntity<PermissionsResource> getPermissionsDummy() {
        LOG.info("Called getPermissionsDummy");
        Set<String> permissions = new HashSet<>();
        permissions.add("RESOURCE3");
        permissions.add("RESOURCE2");
        permissions.add("RESOURCE1");
        permissions.add("Default Resource");

        PermissionsResource permissionsResource = new PermissionsResource();
        permissionsResource.setPermissions(permissions);

        Link link = linkTo(UserController.class).withSelfRel();
        permissionsResource.add(link);
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.info("Returning getPermissionsDummy");
//        return permissionsResource;
        return new ResponseEntity<PermissionsResource>(permissionsResource, HttpStatus.FOUND);
    }
    
    


}
