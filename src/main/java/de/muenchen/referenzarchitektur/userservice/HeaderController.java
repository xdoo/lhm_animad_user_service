package de.muenchen.referenzarchitektur.userservice;

import org.springframework.web.bind.annotation.RequestMapping;
import java.util.logging.Logger;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rowe42
 */
@RestController
@RequestMapping(value = "/headers")
public class HeaderController implements
        ResourceProcessor<RepositoryLinksResource> {

    private static final Logger LOG = Logger.getLogger(HeaderController.class.getName());

    /**
     * Testmethode, um die ans Backend übergebenen Headers anzuzeigen.
     * @param headers
     * @return 
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showHeaders(@RequestHeader HttpHeaders headers) {
        LOG.fine("Headers Endpoint called.");
        return headers.toString();
    }
    
    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        resource.add(ControllerLinkBuilder.linkTo(HeaderController.class).withRel("headers"));
        return resource;
    }

}
