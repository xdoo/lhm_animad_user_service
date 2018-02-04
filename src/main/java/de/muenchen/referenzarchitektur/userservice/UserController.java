package de.muenchen.referenzarchitektur.userservice;

import de.muenchen.referenzarchitektur.authorisationLib.EntitlementsService;
import de.muenchen.referenzarchitektur.userservice.domain.PermissionsResource;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author rowe42
 */
@RestController
public class UserController {

    private final EntitlementsService entitlementsService;

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    @Autowired
    private OAuth2RestTemplate restTemplate;

    public UserController(EntitlementsService entitlementsService) {
        this.entitlementsService = entitlementsService;
    }

//    @RequestMapping(value = "/getPermissions", method = RequestMethod.GET)
//    public String getPermissions() {
//        Set<String> permissions = entitlementsService.getPermissions(true, false);
//        if (permissions != null) {
//            return permissions.toString();
//        } else {
//            return "No Permissions found.";
//        }
//    }
//    
    @RequestMapping(value = "/getPermissions", method = RequestMethod.OPTIONS)
    public String getOptions() {
        return "Body";
    }

    @RequestMapping(value = "/testRestTemplate", method = RequestMethod.GET)
    public String testRestTemplate() {
        RestTemplate standardRestTemplate = new RestTemplate();
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) a.getDetails();
        String tokenValue = details.getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenValue);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = standardRestTemplate.exchange(
                "https://requestb.in/1mz6j391", HttpMethod.GET, entity, String.class);

        String response = responseEntity.getBody();
        LOG.info("Body " + response);

        return "Success! (" + response + ")";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String getTestOAuth2RestTemplate() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://requestb.in/1mz6j391", HttpMethod.GET, entity, String.class);

        String response = responseEntity.getBody();

        return "Success! (" + response + ")";
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
        return new ResponseEntity<>(permissionsResource, HttpStatus.OK);
    }

}
