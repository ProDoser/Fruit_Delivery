package com.fruitcrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fruitcrm.domain.FruitPack;
import com.fruitcrm.repository.FruitPackRepository;
import com.fruitcrm.repository.search.FruitPackSearchRepository;
import com.fruitcrm.web.rest.util.HeaderUtil;
import com.fruitcrm.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * REST controller for managing FruitPack.
 */
@RestController
@RequestMapping("/api")
public class FruitPackResource {

    private final Logger log = LoggerFactory.getLogger(FruitPackResource.class);
        
    @Inject
    private FruitPackRepository fruitPackRepository;
    
    @Inject
    private FruitPackSearchRepository fruitPackSearchRepository;
    
    /**
     * POST  /fruitPacks -> Create a new fruitPack.
     */
    @RequestMapping(value = "/fruitPacks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FruitPack> createFruitPack(@Valid @RequestBody FruitPack fruitPack) throws URISyntaxException {
        log.debug("REST request to save FruitPack : {}", fruitPack);
        if (fruitPack.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fruitPack", "idexists", "A new fruitPack cannot already have an ID")).body(null);
        }
        FruitPack result = fruitPackRepository.save(fruitPack);
        fruitPackSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fruitPacks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fruitPack", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fruitPacks -> Updates an existing fruitPack.
     */
    @RequestMapping(value = "/fruitPacks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FruitPack> updateFruitPack(@Valid @RequestBody FruitPack fruitPack) throws URISyntaxException {
        log.debug("REST request to update FruitPack : {}", fruitPack);
        if (fruitPack.getId() == null) {
            return createFruitPack(fruitPack);
        }
        FruitPack result = fruitPackRepository.save(fruitPack);
        fruitPackSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fruitPack", fruitPack.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fruitPacks -> get all the fruitPacks.
     */
    @RequestMapping(value = "/fruitPacks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FruitPack>> getAllFruitPacks(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FruitPacks");
        Page<FruitPack> page = fruitPackRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fruitPacks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fruitPacks/:id -> get the "id" fruitPack.
     */
    @RequestMapping(value = "/fruitPacks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FruitPack> getFruitPack(@PathVariable Long id) {
        log.debug("REST request to get FruitPack : {}", id);
        FruitPack fruitPack = fruitPackRepository.findOne(id);
        return Optional.ofNullable(fruitPack)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fruitPacks/:id -> delete the "id" fruitPack.
     */
    @RequestMapping(value = "/fruitPacks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFruitPack(@PathVariable Long id) {
        log.debug("REST request to delete FruitPack : {}", id);
        fruitPackRepository.delete(id);
        fruitPackSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fruitPack", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fruitPacks/:query -> search for the fruitPack corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/fruitPacks/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FruitPack> searchFruitPacks(@PathVariable String query) {
        log.debug("REST request to search FruitPacks for query {}", query);
        return StreamSupport
            .stream(fruitPackSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
