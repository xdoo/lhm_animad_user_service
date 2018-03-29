package de.muenchen.referenzarchitektur.userservice;

import de.muenchen.referenzarchitektur.userservice.domain.PermissionsResource;
import de.muenchen.referenzarchitektur.userservice.services.EntitlementsService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.logging.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author rowe42
 */
@BasePathAwareController
@Profile("!no-security")
@RequestMapping(value = "/permissions")
public class UserController implements
        ResourceProcessor<RepositoryLinksResource> {

    private final EntitlementsService entitlementsService;

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    public UserController(EntitlementsService entitlementsService) {
        this.entitlementsService = entitlementsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PermissionsResource> getPermissions() {
        Set<String> permissions = entitlementsService.getPermissions(false);
        return generatePermissionsResponse(permissions, "permissions");
    }


    private ResponseEntity<PermissionsResource> generatePermissionsResponse(Set<String> permissions, String self) {
        if (permissions == null) {
            permissions = new HashSet<>();
        }
        PermissionsResource permissionsResource = new PermissionsResource();
        permissionsResource.setPermissions(permissions);

        Link link = linkTo(UserController.class).slash(self).withSelfRel();
        permissionsResource.add(link);

        return new ResponseEntity<>(permissionsResource, HttpStatus.OK);
    }

    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        resource.add(ControllerLinkBuilder.linkTo(UserController.class).withRel("permissions"));
        return resource;
    }

}
