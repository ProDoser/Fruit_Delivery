package com.fruitcrm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fruitcrm.domain.Orders;
import com.fruitcrm.repository.OrdersRepository;
import com.fruitcrm.repository.search.OrdersSearchRepository;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Orders.
 */
@RestController
@RequestMapping("/api")
public class OrdersResource {

    private final Logger log = LoggerFactory.getLogger(OrdersResource.class);

    @Inject
    private OrdersRepository ordersRepository;

    @Inject
    private OrdersSearchRepository ordersSearchRepository;

    /**
     * POST  /orderss -> Create a new orders.
     */
    @RequestMapping(value = "/orderss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Orders> createOrders(@Valid @RequestBody Orders orders) throws URISyntaxException {
        log.debug("REST request to save Orders : {}", orders);
        if (orders.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orders", "idexists", "A new orders cannot already have an ID")).body(null);
        }
        Orders result = ordersRepository.save(orders);
        ordersSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/orderss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orders", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /orderss -> Updates an existing orders.
     */
    @RequestMapping(value = "/orderss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Orders> updateOrders(@Valid @RequestBody Orders orders) throws URISyntaxException {
        log.debug("REST request to update Orders : {}", orders);
        if (orders.getId() == null) {
            return createOrders(orders);
        }
        Orders result = ordersRepository.save(orders);
        ordersSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orders", orders.getId().toString()))
            .body(result);
    }

    /**
     * GET  /orderss -> get all the orderss.
     */
    @RequestMapping(value = "/orderss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Orders>> getAllOrderss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Orderss");
        Page<Orders> page = ordersRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orderss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /today -> get all the orders for current day.
     */
    @RequestMapping(value = "/today",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Orders>> getOrdersForToday(Pageable pageable)
        throws URISyntaxException {

        /**
         * Get current day of week and week of year
         */

        ZoneId zoneId = ZoneId.of ( "Europe/Helsinki" );
        ZonedDateTime now = ZonedDateTime.now ( zoneId );
        int DayOfWeek = now.get(ChronoField.DAY_OF_WEEK);
        int WeekOfYear = now.get ( IsoFields.WEEK_OF_WEEK_BASED_YEAR )%2;
        boolean active = true;

        log.debug("REST request to get a page of Orders for today");
        Page<Orders> page = ordersRepository.findByDeliveryDay_weekdayAndIsActiveAndWeek_weekNotLike(DayOfWeek, active, WeekOfYear, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/today");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }



    /**
     * GET  /orderss/:id -> get the "id" orders.
     */
    @RequestMapping(value = "/orderss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Orders> getOrders(@PathVariable Long id) {
        log.debug("REST request to get Orders : {}", id);
        Orders orders = ordersRepository.findOne(id);
        return Optional.ofNullable(orders)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /orderss/:id -> delete the "id" orders.
     */
    @RequestMapping(value = "/orderss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrders(@PathVariable Long id) {
        log.debug("REST request to delete Orders : {}", id);
        ordersRepository.delete(id);
        ordersSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orders", id.toString())).build();
    }

    /**
     * SEARCH  /_search/orderss/:query -> search for the orders corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/orderss/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Orders> searchOrderss(@PathVariable String query) {
        log.debug("REST request to search Orderss for query {}", query);
        return StreamSupport
            .stream(ordersSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
