package de.muenchen.referenzarchitektur.userservice;

import de.muenchen.referenzarchitektur.userservice.domain.PermissionsResource;
import java.util.HashSet;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;
import org.springframework.context.annotation.Profile;
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
@Profile("no-security")
public class NoSecurityController {

    private static final Logger LOG = Logger.getLogger(NoSecurityController.class.getName());


    @CrossOrigin(origins = "http://127.0.0.1:8081")
    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    public ResponseEntity<PermissionsResource> getPermissionsMock() {
        LOG.info("Called permissionsMock");
        Set<String> permissions = new HashSet<>();
        permissions.add("administration_READ_Animal");
        permissions.add("administration_READ_Keeper");
        permissions.add("administration_READ_Enclosure");
        permissions.add("administration_WRITE_Animal");
        permissions.add("administration_WRITE_Enclosure");
        permissions.add("administration_WRITE_Keeper");
        permissions.add("administration_DELETE_Animal");
        permissions.add("administration_DELETE_Enclosure");
        permissions.add("administration_DELETE_Keeper");
        permissions.add("administration_BUSINESSACTION_CreateAppointment");
        permissions.add("administration_GUI_KeepersView");
        permissions.add("administration_GUI_AnimalsView");
        permissions.add("administration_GUI_EnclosuresView");

//        Einkommentieren, um asynchrones Verhalten des Permission-Requests zu simluieren
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(NoSecurityController.class.getName()).log(Level.SEVERE, null, ex);
//        }

        return generatePermissionsResponse(permissions, "permissions");
    }

    private ResponseEntity<PermissionsResource> generatePermissionsResponse(Set<String> permissions, String self) {
        if (permissions == null) {
            permissions = new HashSet<>();
        }
        PermissionsResource permissionsResource = new PermissionsResource();
        permissionsResource.setPermissions(permissions);

        Link link = linkTo(NoSecurityController.class).slash(self).withSelfRel();
        permissionsResource.add(link);

        return new ResponseEntity<>(permissionsResource, HttpStatus.OK);
    }

}
