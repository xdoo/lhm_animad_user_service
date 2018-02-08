package de.muenchen.referenzarchitektur.userservice;

import de.muenchen.referenzarchitektur.userservice.domain.PermissionsResource;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author rowe42
 */
@RestController
@Profile("no-security")
public class NoSecurityController {

    private static final Logger LOG = Logger.getLogger(NoSecurityController.class.getName());



    @RequestMapping(value = "/permissions", method = RequestMethod.OPTIONS)
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
                "https://requestb.in/wpeip8wp", HttpMethod.GET, entity, String.class);

        String response = responseEntity.getBody();
        LOG.info("Body " + response);

        return "Success! (" + response + ")";
    }


    @CrossOrigin(origins = "http://127.0.0.1:8081")
    @RequestMapping(value = "/permissionsMock", method = RequestMethod.GET)
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

    
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(NoSecurityController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return generatePermissionsResponse(permissions, "permissionsMock");
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
