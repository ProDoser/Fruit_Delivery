package com.fruitcrm.web.rest;

import com.fruitcrm.Application;
import com.fruitcrm.domain.Orders;
import com.fruitcrm.repository.OrdersRepository;
import com.fruitcrm.repository.search.OrdersSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the OrdersResource REST controller.
 *
 * @see OrdersResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OrdersResourceIntTest {

    private static final String DEFAULT_DETAILS = "AAAAA";
    private static final String UPDATED_DETAILS = "BBBBB";

    private static final LocalDate DEFAULT_ORDER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORDER_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FIRST_DELIVERY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIRST_DELIVERY = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Inject
    private OrdersRepository ordersRepository;

    @Inject
    private OrdersSearchRepository ordersSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrdersMockMvc;

    private Orders orders;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrdersResource ordersResource = new OrdersResource();
        ReflectionTestUtils.setField(ordersResource, "ordersSearchRepository", ordersSearchRepository);
        ReflectionTestUtils.setField(ordersResource, "ordersRepository", ordersRepository);
        this.restOrdersMockMvc = MockMvcBuilders.standaloneSetup(ordersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        orders = new Orders();
        orders.setDetails(DEFAULT_DETAILS);
        orders.setOrderDate(DEFAULT_ORDER_DATE);
        orders.setFirstDelivery(DEFAULT_FIRST_DELIVERY);
        orders.setIsActive(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createOrders() throws Exception {
        int databaseSizeBeforeCreate = ordersRepository.findAll().size();

        // Create the Orders

        restOrdersMockMvc.perform(post("/api/orderss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orders)))
                .andExpect(status().isCreated());

        // Validate the Orders in the database
        List<Orders> orderss = ordersRepository.findAll();
        assertThat(orderss).hasSize(databaseSizeBeforeCreate + 1);
        Orders testOrders = orderss.get(orderss.size() - 1);
        assertThat(testOrders.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testOrders.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrders.getFirstDelivery()).isEqualTo(DEFAULT_FIRST_DELIVERY);
        assertThat(testOrders.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void checkOrderDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setOrderDate(null);

        // Create the Orders, which fails.

        restOrdersMockMvc.perform(post("/api/orderss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orders)))
                .andExpect(status().isBadRequest());

        List<Orders> orderss = ordersRepository.findAll();
        assertThat(orderss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstDeliveryIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setFirstDelivery(null);

        // Create the Orders, which fails.

        restOrdersMockMvc.perform(post("/api/orderss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orders)))
                .andExpect(status().isBadRequest());

        List<Orders> orderss = ordersRepository.findAll();
        assertThat(orderss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setIsActive(null);

        // Create the Orders, which fails.

        restOrdersMockMvc.perform(post("/api/orderss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orders)))
                .andExpect(status().isBadRequest());

        List<Orders> orderss = ordersRepository.findAll();
        assertThat(orderss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderss() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the orderss
        restOrdersMockMvc.perform(get("/api/orderss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
                .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())))
                .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
                .andExpect(jsonPath("$.[*].firstDelivery").value(hasItem(DEFAULT_FIRST_DELIVERY.toString())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get the orders
        restOrdersMockMvc.perform(get("/api/orderss/{id}", orders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(orders.getId().intValue()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.firstDelivery").value(DEFAULT_FIRST_DELIVERY.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrders() throws Exception {
        // Get the orders
        restOrdersMockMvc.perform(get("/api/orderss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

		int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Update the orders
        orders.setDetails(UPDATED_DETAILS);
        orders.setOrderDate(UPDATED_ORDER_DATE);
        orders.setFirstDelivery(UPDATED_FIRST_DELIVERY);
        orders.setIsActive(UPDATED_IS_ACTIVE);

        restOrdersMockMvc.perform(put("/api/orderss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orders)))
                .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> orderss = ordersRepository.findAll();
        assertThat(orderss).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = orderss.get(orderss.size() - 1);
        assertThat(testOrders.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testOrders.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrders.getFirstDelivery()).isEqualTo(UPDATED_FIRST_DELIVERY);
        assertThat(testOrders.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void deleteOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

		int databaseSizeBeforeDelete = ordersRepository.findAll().size();

        // Get the orders
        restOrdersMockMvc.perform(delete("/api/orderss/{id}", orders.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Orders> orderss = ordersRepository.findAll();
        assertThat(orderss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
