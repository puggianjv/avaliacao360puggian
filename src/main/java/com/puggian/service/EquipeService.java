package com.puggian.service;

import com.puggian.domain.Equipe;
import com.puggian.repository.EquipeRepository;
import com.puggian.repository.search.EquipeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Equipe.
 */
@Service
@Transactional
public class EquipeService {

    private final Logger log = LoggerFactory.getLogger(EquipeService.class);
    
    @Inject
    private EquipeRepository equipeRepository;

    @Inject
    private EquipeSearchRepository equipeSearchRepository;

    /**
     * Save a equipe.
     *
     * @param equipe the entity to save
     * @return the persisted entity
     */
    public Equipe save(Equipe equipe) {
        log.debug("Request to save Equipe : {}", equipe);
        Equipe result = equipeRepository.save(equipe);
        equipeSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the equipes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Equipe> findAll(Pageable pageable) {
        log.debug("Request to get all Equipes");
        Page<Equipe> result = equipeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one equipe by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Equipe findOne(Long id) {
        log.debug("Request to get Equipe : {}", id);
        Equipe equipe = equipeRepository.findOneWithEagerRelationships(id);
        return equipe;
    }

    /**
     *  Delete the  equipe by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Equipe : {}", id);
        equipeRepository.delete(id);
        equipeSearchRepository.delete(id);
    }

    /**
     * Search for the equipe corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Equipe> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Equipes for query {}", query);
        Page<Equipe> result = equipeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
