package com.fruitcrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fruitcrm.domain.DeliveryDay;
import com.fruitcrm.repository.DeliveryDayRepository;
import com.fruitcrm.repository.search.DeliveryDaySearchRepository;
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
 * REST controller for managing DeliveryDay.
 */
@RestController
@RequestMapping("/api")
public class DeliveryDayResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryDayResource.class);
        
    @Inject
    private DeliveryDayRepository deliveryDayRepository;
    
    @Inject
    private DeliveryDaySearchRepository deliveryDaySearchRepository;
    
    /**
     * POST  /deliveryDays -> Create a new deliveryDay.
     */
    @RequestMapping(value = "/deliveryDays",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeliveryDay> createDeliveryDay(@Valid @RequestBody DeliveryDay deliveryDay) throws URISyntaxException {
        log.debug("REST request to save DeliveryDay : {}", deliveryDay);
        if (deliveryDay.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("deliveryDay", "idexists", "A new deliveryDay cannot already have an ID")).body(null);
        }
        DeliveryDay result = deliveryDayRepository.save(deliveryDay);
        deliveryDaySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/deliveryDays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("deliveryDay", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /deliveryDays -> Updates an existing deliveryDay.
     */
    @RequestMapping(value = "/deliveryDays",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeliveryDay> updateDeliveryDay(@Valid @RequestBody DeliveryDay deliveryDay) throws URISyntaxException {
        log.debug("REST request to update DeliveryDay : {}", deliveryDay);
        if (deliveryDay.getId() == null) {
            return createDeliveryDay(deliveryDay);
        }
        DeliveryDay result = deliveryDayRepository.save(deliveryDay);
        deliveryDaySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("deliveryDay", deliveryDay.getId().toString()))
            .body(result);
    }

    /**
     * GET  /deliveryDays -> get all the deliveryDays.
     */
    @RequestMapping(value = "/deliveryDays",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DeliveryDay> getAllDeliveryDays() {
        log.debug("REST request to get all DeliveryDays");
        return deliveryDayRepository.findAll();
            }

    /**
     * GET  /deliveryDays/:id -> get the "id" deliveryDay.
     */
    @RequestMapping(value = "/deliveryDays/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeliveryDay> getDeliveryDay(@PathVariable Long id) {
        log.debug("REST request to get DeliveryDay : {}", id);
        DeliveryDay deliveryDay = deliveryDayRepository.findOne(id);
        return Optional.ofNullable(deliveryDay)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /deliveryDays/:id -> delete the "id" deliveryDay.
     */
    @RequestMapping(value = "/deliveryDays/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDeliveryDay(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryDay : {}", id);
        deliveryDayRepository.delete(id);
        deliveryDaySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("deliveryDay", id.toString())).build();
    }

    /**
     * SEARCH  /_search/deliveryDays/:query -> search for the deliveryDay corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/deliveryDays/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DeliveryDay> searchDeliveryDays(@PathVariable String query) {
        log.debug("REST request to search DeliveryDays for query {}", query);
        return StreamSupport
            .stream(deliveryDaySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
