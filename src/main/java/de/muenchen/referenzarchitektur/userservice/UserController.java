package de.muenchen.referenzarchitektur.userservice;

import de.muenchen.referenzarchitektur.userservice.domain.PermissionsResource;
import de.muenchen.referenzarchitektur.userservice.services.EntitlementsService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author rowe42
 */
@RestController
@Profile("!no-security")
public class UserController {

    private final EntitlementsService entitlementsService;

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    @Autowired
    private OAuth2RestTemplate restTemplate;

    public UserController(EntitlementsService entitlementsService) {
        this.entitlementsService = entitlementsService;
    }

    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    public ResponseEntity<PermissionsResource> getPermissions() {
        Set<String> permissions = entitlementsService.getPermissions(false);
        return generatePermissionsResponse(permissions, "permissions");
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String getTestOAuth2RestTemplate() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://requestb.in/wpeip8wp", HttpMethod.GET, entity, String.class);

        String response = responseEntity.getBody();

        return "Success! (" + response + ")";
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
