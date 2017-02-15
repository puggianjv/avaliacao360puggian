package com.puggian.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.puggian.domain.Equipe;
import com.puggian.service.EquipeService;
import com.puggian.web.rest.util.HeaderUtil;
import com.puggian.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Equipe.
 */
@RestController
@RequestMapping("/api")
public class EquipeResource {

    private final Logger log = LoggerFactory.getLogger(EquipeResource.class);
        
    @Inject
    private EquipeService equipeService;

    /**
     * POST  /equipes : Create a new equipe.
     *
     * @param equipe the equipe to create
     * @return the ResponseEntity with status 201 (Created) and with body the new equipe, or with status 400 (Bad Request) if the equipe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/equipes")
    @Timed
    public ResponseEntity<Equipe> createEquipe(@Valid @RequestBody Equipe equipe) throws URISyntaxException {
        log.debug("REST request to save Equipe : {}", equipe);
        if (equipe.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("equipe", "idexists", "A new equipe cannot already have an ID")).body(null);
        }
        Equipe result = equipeService.save(equipe);
        return ResponseEntity.created(new URI("/api/equipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("equipe", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /equipes : Updates an existing equipe.
     *
     * @param equipe the equipe to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated equipe,
     * or with status 400 (Bad Request) if the equipe is not valid,
     * or with status 500 (Internal Server Error) if the equipe couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/equipes")
    @Timed
    public ResponseEntity<Equipe> updateEquipe(@Valid @RequestBody Equipe equipe) throws URISyntaxException {
        log.debug("REST request to update Equipe : {}", equipe);
        if (equipe.getId() == null) {
            return createEquipe(equipe);
        }
        Equipe result = equipeService.save(equipe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("equipe", equipe.getId().toString()))
            .body(result);
    }

    /**
     * GET  /equipes : get all the equipes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of equipes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/equipes")
    @Timed
    public ResponseEntity<List<Equipe>> getAllEquipes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Equipes");
        Page<Equipe> page = equipeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/equipes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /equipes/:id : get the "id" equipe.
     *
     * @param id the id of the equipe to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the equipe, or with status 404 (Not Found)
     */
    @GetMapping("/equipes/{id}")
    @Timed
    public ResponseEntity<Equipe> getEquipe(@PathVariable Long id) {
        log.debug("REST request to get Equipe : {}", id);
        Equipe equipe = equipeService.findOne(id);
        return Optional.ofNullable(equipe)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /equipes/:id : delete the "id" equipe.
     *
     * @param id the id of the equipe to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/equipes/{id}")
    @Timed
    public ResponseEntity<Void> deleteEquipe(@PathVariable Long id) {
        log.debug("REST request to delete Equipe : {}", id);
        equipeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("equipe", id.toString())).build();
    }

    /**
     * SEARCH  /_search/equipes?query=:query : search for the equipe corresponding
     * to the query.
     *
     * @param query the query of the equipe search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/equipes")
    @Timed
    public ResponseEntity<List<Equipe>> searchEquipes(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Equipes for query {}", query);
        Page<Equipe> page = equipeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/equipes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
