package com.fruitcrm.web.rest;

import com.fruitcrm.Application;
import com.fruitcrm.domain.DeliveryDay;
import com.fruitcrm.repository.DeliveryDayRepository;
import com.fruitcrm.repository.search.DeliveryDaySearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DeliveryDayResource REST controller.
 *
 * @see DeliveryDayResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DeliveryDayResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_WEEKDAY = 1;
    private static final Integer UPDATED_WEEKDAY = 2;

    @Inject
    private DeliveryDayRepository deliveryDayRepository;

    @Inject
    private DeliveryDaySearchRepository deliveryDaySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDeliveryDayMockMvc;

    private DeliveryDay deliveryDay;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DeliveryDayResource deliveryDayResource = new DeliveryDayResource();
        ReflectionTestUtils.setField(deliveryDayResource, "deliveryDaySearchRepository", deliveryDaySearchRepository);
        ReflectionTestUtils.setField(deliveryDayResource, "deliveryDayRepository", deliveryDayRepository);
        this.restDeliveryDayMockMvc = MockMvcBuilders.standaloneSetup(deliveryDayResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        deliveryDay = new DeliveryDay();
        deliveryDay.setName(DEFAULT_NAME);
        deliveryDay.setWeekday(DEFAULT_WEEKDAY);
    }

    @Test
    @Transactional
    public void createDeliveryDay() throws Exception {
        int databaseSizeBeforeCreate = deliveryDayRepository.findAll().size();

        // Create the DeliveryDay

        restDeliveryDayMockMvc.perform(post("/api/deliveryDays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryDay)))
                .andExpect(status().isCreated());

        // Validate the DeliveryDay in the database
        List<DeliveryDay> deliveryDays = deliveryDayRepository.findAll();
        assertThat(deliveryDays).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryDay testDeliveryDay = deliveryDays.get(deliveryDays.size() - 1);
        assertThat(testDeliveryDay.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeliveryDay.getWeekday()).isEqualTo(DEFAULT_WEEKDAY);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryDayRepository.findAll().size();
        // set the field null
        deliveryDay.setName(null);

        // Create the DeliveryDay, which fails.

        restDeliveryDayMockMvc.perform(post("/api/deliveryDays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryDay)))
                .andExpect(status().isBadRequest());

        List<DeliveryDay> deliveryDays = deliveryDayRepository.findAll();
        assertThat(deliveryDays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeekdayIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryDayRepository.findAll().size();
        // set the field null
        deliveryDay.setWeekday(null);

        // Create the DeliveryDay, which fails.

        restDeliveryDayMockMvc.perform(post("/api/deliveryDays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryDay)))
                .andExpect(status().isBadRequest());

        List<DeliveryDay> deliveryDays = deliveryDayRepository.findAll();
        assertThat(deliveryDays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDeliveryDays() throws Exception {
        // Initialize the database
        deliveryDayRepository.saveAndFlush(deliveryDay);

        // Get all the deliveryDays
        restDeliveryDayMockMvc.perform(get("/api/deliveryDays?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryDay.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].weekday").value(hasItem(DEFAULT_WEEKDAY)));
    }

    @Test
    @Transactional
    public void getDeliveryDay() throws Exception {
        // Initialize the database
        deliveryDayRepository.saveAndFlush(deliveryDay);

        // Get the deliveryDay
        restDeliveryDayMockMvc.perform(get("/api/deliveryDays/{id}", deliveryDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(deliveryDay.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.weekday").value(DEFAULT_WEEKDAY));
    }

    @Test
    @Transactional
    public void getNonExistingDeliveryDay() throws Exception {
        // Get the deliveryDay
        restDeliveryDayMockMvc.perform(get("/api/deliveryDays/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeliveryDay() throws Exception {
        // Initialize the database
        deliveryDayRepository.saveAndFlush(deliveryDay);

		int databaseSizeBeforeUpdate = deliveryDayRepository.findAll().size();

        // Update the deliveryDay
        deliveryDay.setName(UPDATED_NAME);
        deliveryDay.setWeekday(UPDATED_WEEKDAY);

        restDeliveryDayMockMvc.perform(put("/api/deliveryDays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryDay)))
                .andExpect(status().isOk());

        // Validate the DeliveryDay in the database
        List<DeliveryDay> deliveryDays = deliveryDayRepository.findAll();
        assertThat(deliveryDays).hasSize(databaseSizeBeforeUpdate);
        DeliveryDay testDeliveryDay = deliveryDays.get(deliveryDays.size() - 1);
        assertThat(testDeliveryDay.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeliveryDay.getWeekday()).isEqualTo(UPDATED_WEEKDAY);
    }

    @Test
    @Transactional
    public void deleteDeliveryDay() throws Exception {
        // Initialize the database
        deliveryDayRepository.saveAndFlush(deliveryDay);

		int databaseSizeBeforeDelete = deliveryDayRepository.findAll().size();

        // Get the deliveryDay
        restDeliveryDayMockMvc.perform(delete("/api/deliveryDays/{id}", deliveryDay.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DeliveryDay> deliveryDays = deliveryDayRepository.findAll();
        assertThat(deliveryDays).hasSize(databaseSizeBeforeDelete - 1);
    }
}
