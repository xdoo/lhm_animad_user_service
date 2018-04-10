/*
* Copyright (c): it@M - Dienstleister fuer Informations- und Telekommunikationstechnik
* der Landeshauptstadt Muenchen, 2018
 */
package de.muenchen.referenzarchitektur.userservice.customsearches;

import de.muenchen.referenzarchitektur.userservice.domain.CustomSearch;
import de.muenchen.referenzarchitektur.userservice.exceptions.ParameterMissingException;
import de.muenchen.referenzarchitektur.userservice.exceptions.UsernameNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.ResourceProcessor;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author rowe42
 */
@BasePathAwareController
@ExposesResourceFor(CustomSearch.class)
@RequestMapping(value = "/customsearches")
public class CustomSearchesController implements ResourceProcessor<RepositoryLinksResource> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomSearchesController.class);

    private static final int MAX_SEARCHES = 5;
    private CustomSearchesRepository customSearchesRepository;
    private CustomSearchesHelper customSearchesHelper;

    public CustomSearchesController(CustomSearchesRepository customSearchesRepository,
        CustomSearchesHelper customSearchesHelper) {
        this.customSearchesRepository = customSearchesRepository;
        this.customSearchesHelper = customSearchesHelper;
    }

    class LinksResource extends ResourceSupport {

        public LinksResource() {
        }
    }
    
    /**
     * This method returns a list of links of all mapped search function.
     */
    private List<Link> getMethodActions() {
        List<Link> methodLinks = new ArrayList<>();
        final String base = linkTo(CustomSearchesController.class).toUri().toString();

        methodLinks.add(new Link(base + "/createTestSearches", "createTestSearches"));
        methodLinks.add(new Link(base + "/replaceOldestSearchForCurrentUser", "replaceOldestSearchForCurrentUser"));
        methodLinks.add(new Link(base + "/searchesForCurrentUser", "searchesForCurrentUser"));

        return methodLinks;
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<LinksResource> getActions() {
        LinksResource links = new LinksResource();
        links.add(linkTo(LinksResource.class).withSelfRel());

        links.add(getMethodActions());

        return new ResponseEntity<>(links, HttpStatus.OK);
    }

    /**
     * This method adds the link to /businessActions to the REST-startpoint.
     *
     * @param repositoryLinksResource
     * @return
     */
    @Override
    public RepositoryLinksResource process(RepositoryLinksResource repositoryLinksResource) {
        repositoryLinksResource.add(linkTo(methodOn(CustomSearchesController.class).getActions()).withRel("customsearches"));
        return repositoryLinksResource;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/createTestSearches")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTestSearches(PersistentEntityResourceAssembler assembler) {
        for (int i = 0; i < 5; i++) {
            CustomSearch customSearch = new CustomSearch();
            customSearch.setUserName("animad_admin");
            customSearch.setComponentName("component");
            customSearch.setSearchName("abc" + i);
            customSearch.setSearch("search" + i);
            customSearch.setLastUpdated(LocalTime.now());
            customSearchesRepository.save(customSearch);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/replaceOldestSearchForCurrentUser")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void replaceOldestSearch(PersistentEntityResourceAssembler assembler, @RequestBody CustomSearch customSearch) throws UsernameNotFoundException {
        String userName = customSearchesHelper.getUsernameFromSession();
        int count = customSearchesRepository.countByUserNameAndComponentName(userName, customSearch.getComponentName());
        if (count >= MAX_SEARCHES) {
            CustomSearch searchToDelete = customSearchesRepository.findFirstByUserNameAndComponentNameOrderByLastUpdated(userName, customSearch.getComponentName());
            customSearchesRepository.delete(searchToDelete);
        }

        customSearch.setUserName(userName);
        customSearch.setLastUpdated(LocalTime.now());
        customSearchesRepository.save(customSearch);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/searchesForCurrentUser")
    @ResponseBody
    public ResponseEntity<?> findSearchesPerUser(PersistentEntityResourceAssembler assembler, @Param("componentName") String componentName) throws UsernameNotFoundException, ParameterMissingException {
        String userName = customSearchesHelper.getUsernameFromSession();
        if (componentName == null) {
            throw new ParameterMissingException("Query parameter componentName required.");
        }
        List<CustomSearch> customSearches = customSearchesRepository.findByUserNameAndComponentNameOrderBySearchName(userName, componentName);
        return new ResponseEntity<>(customSearches, HttpStatus.OK);
    }


}
