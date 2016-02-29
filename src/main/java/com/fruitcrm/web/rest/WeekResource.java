package com.fruitcrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fruitcrm.domain.Week;
import com.fruitcrm.repository.WeekRepository;
import com.fruitcrm.repository.search.WeekSearchRepository;
import com.fruitcrm.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Week.
 */
@RestController
@RequestMapping("/api")
public class WeekResource {

    private final Logger log = LoggerFactory.getLogger(WeekResource.class);
        
    @Inject
    private WeekRepository weekRepository;
    
    @Inject
    private WeekSearchRepository weekSearchRepository;
    
    /**
     * POST  /weeks -> Create a new week.
     */
    @RequestMapping(value = "/weeks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Week> createWeek(@Valid @RequestBody Week week) throws URISyntaxException {
        log.debug("REST request to save Week : {}", week);
        if (week.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("week", "idexists", "A new week cannot already have an ID")).body(null);
        }
        Week result = weekRepository.save(week);
        weekSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/weeks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("week", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /weeks -> Updates an existing week.
     */
    @RequestMapping(value = "/weeks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Week> updateWeek(@Valid @RequestBody Week week) throws URISyntaxException {
        log.debug("REST request to update Week : {}", week);
        if (week.getId() == null) {
            return createWeek(week);
        }
        Week result = weekRepository.save(week);
        weekSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("week", week.getId().toString()))
            .body(result);
    }

    /**
     * GET  /weeks -> get all the weeks.
     */
    @RequestMapping(value = "/weeks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Week> getAllWeeks() {
        log.debug("REST request to get all Weeks");
        return weekRepository.findAll();
            }

    /**
     * GET  /weeks/:id -> get the "id" week.
     */
    @RequestMapping(value = "/weeks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Week> getWeek(@PathVariable Long id) {
        log.debug("REST request to get Week : {}", id);
        Week week = weekRepository.findOne(id);
        return Optional.ofNullable(week)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /weeks/:id -> delete the "id" week.
     */
    @RequestMapping(value = "/weeks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWeek(@PathVariable Long id) {
        log.debug("REST request to delete Week : {}", id);
        weekRepository.delete(id);
        weekSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("week", id.toString())).build();
    }

    /**
     * SEARCH  /_search/weeks/:query -> search for the week corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/weeks/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Week> searchWeeks(@PathVariable String query) {
        log.debug("REST request to search Weeks for query {}", query);
        return StreamSupport
            .stream(weekSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
