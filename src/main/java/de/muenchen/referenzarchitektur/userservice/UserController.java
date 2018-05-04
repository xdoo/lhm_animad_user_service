package de.muenchen.referenzarchitektur.userservice;

import de.muenchen.referenzarchitektur.userservice.domain.PermissionsResource;
import de.muenchen.referenzarchitektur.userservice.services.EntitlementsService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.ResourceSupport;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author rowe42
 */
@RestController
@Profile("!no-security")
@RequestMapping(value = "/permissions")
public class UserController implements ResourceProcessor<RepositoryLinksResource> {

    private final EntitlementsService entitlementsService;

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    public UserController(EntitlementsService entitlementsService) {
        this.entitlementsService = entitlementsService;
    }

    class LinksResource extends ResourceSupport {

        public LinksResource() {
        }
    }

    /**
     * This method adds the link to /businessActions to the REST-startpoint.
     *
     * @param repositoryLinksResource
     * @return
     */
    @Override
    public RepositoryLinksResource process(RepositoryLinksResource repositoryLinksResource) {
        repositoryLinksResource.add(linkTo(UserController.class).withRel("permissions"));
        return repositoryLinksResource;
    }

    @RequestMapping(method = RequestMethod.GET)
//    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    public ResponseEntity<PermissionsResource> getPermissions() {
        Set<String> permissions = entitlementsService.getPermissions(false);
        return generatePermissionsResponse(permissions, "permissions");
    }
    
    /**
     * Testmethode, um die ans Backend übergebenen Headers anzuzeigen.
     * @param headers
     * @return 
     */
    @RequestMapping(value = "/headers", method = RequestMethod.GET)
    public String showHeaders(@RequestHeader HttpHeaders headers) {
        return headers.toString();
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

}
