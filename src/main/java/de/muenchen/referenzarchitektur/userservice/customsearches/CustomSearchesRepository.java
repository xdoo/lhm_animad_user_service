/*
* Copyright (c): it@M - Dienstleister fuer Informations- und Telekommunikationstechnik
* der Landeshauptstadt Muenchen, 2018
 */
package de.muenchen.referenzarchitektur.userservice.customsearches;

import de.muenchen.referenzarchitektur.userservice.domain.CustomSearch;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CustomSearchesRepository extends CrudRepository<CustomSearch, UUID> { //NOSONAR

    /**
     * Name for the specific cache.
     */
    String CACHE = "SEARCHES_CACHE";

    /**
     * Get all the CustomSearch objects.
     *
     * @return an Iterable of the CustomSearch objects.
     */
    @Override
    Iterable<CustomSearch> findAll();

    /**
     * Get one specific CustomSearch by its unique oid.
     *
     * @param oid The identifier of the CustomSearch.
     * @return The CustomSearch with the requested oid.
     */
    @Override
    @Cacheable(value = CACHE, key = "#p0")
    CustomSearch findOne(UUID oid);

    /**
     * Create or update a CustomSearch.
     * <p>
     * If the oid already exists, the CustomSearch will be overridden, hence update.
     * If the oid does no already exist, a new CustomSearch will be created, hence
     * create.
     * </p>
     *
     * @param customSearch The CustomSearch that will be saved.
     * @return the saved CustomSearch.
     */
    @Override
    @CachePut(value = CACHE, key = "#p0.oid")
    <S extends CustomSearch> S save(S customSearch);

    /**
     * Delete the CustomSearch by a specified oid.
     *
     * @param oid the unique oid of the CustomSearch that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0")
    void delete(UUID oid);

    /**
     * Delete a CustomSearch by object.
     *
     * @param customSearch The CustomSearch that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0.oid")
    void delete(CustomSearch customSearch);

    /**
     * Delete multiple CustomSearch objects by their oid.
     *
     * @param customSearches The Iterable of CustomSearch objects that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    void delete(Iterable<? extends CustomSearch> customSearches);

    /**
     * Delete all CustomSearch objects.
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    void deleteAll();


    int countByUserNameAndComponentName(
            @Param(value = "userName") String userName,
            @Param(value = "componentName") String componentName);

    CustomSearch findFirstByUserNameAndComponentNameOrderByLastUpdated(
            @Param(value = "userName") String userName,
            @Param(value = "componentName") String componentName);

    List<CustomSearch> findByUserNameAndComponentNameOrderBySearchName(
            @Param(value = "userName") String userName,
            @Param(value = "componentName") String componentName);

}