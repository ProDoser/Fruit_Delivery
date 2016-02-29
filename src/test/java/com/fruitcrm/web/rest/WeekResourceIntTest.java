package com.fruitcrm.web.rest;

import com.fruitcrm.Application;
import com.fruitcrm.domain.Week;
import com.fruitcrm.repository.WeekRepository;
import com.fruitcrm.repository.search.WeekSearchRepository;

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
 * Test class for the WeekResource REST controller.
 *
 * @see WeekResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WeekResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_WEEK = 1;
    private static final Integer UPDATED_WEEK = 2;

    @Inject
    private WeekRepository weekRepository;

    @Inject
    private WeekSearchRepository weekSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWeekMockMvc;

    private Week week;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WeekResource weekResource = new WeekResource();
        ReflectionTestUtils.setField(weekResource, "weekSearchRepository", weekSearchRepository);
        ReflectionTestUtils.setField(weekResource, "weekRepository", weekRepository);
        this.restWeekMockMvc = MockMvcBuilders.standaloneSetup(weekResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        week = new Week();
        week.setName(DEFAULT_NAME);
        week.setWeek(DEFAULT_WEEK);
    }

    @Test
    @Transactional
    public void createWeek() throws Exception {
        int databaseSizeBeforeCreate = weekRepository.findAll().size();

        // Create the Week

        restWeekMockMvc.perform(post("/api/weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(week)))
                .andExpect(status().isCreated());

        // Validate the Week in the database
        List<Week> weeks = weekRepository.findAll();
        assertThat(weeks).hasSize(databaseSizeBeforeCreate + 1);
        Week testWeek = weeks.get(weeks.size() - 1);
        assertThat(testWeek.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWeek.getWeek()).isEqualTo(DEFAULT_WEEK);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = weekRepository.findAll().size();
        // set the field null
        week.setName(null);

        // Create the Week, which fails.

        restWeekMockMvc.perform(post("/api/weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(week)))
                .andExpect(status().isBadRequest());

        List<Week> weeks = weekRepository.findAll();
        assertThat(weeks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWeeks() throws Exception {
        // Initialize the database
        weekRepository.saveAndFlush(week);

        // Get all the weeks
        restWeekMockMvc.perform(get("/api/weeks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(week.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].week").value(hasItem(DEFAULT_WEEK)));
    }

    @Test
    @Transactional
    public void getWeek() throws Exception {
        // Initialize the database
        weekRepository.saveAndFlush(week);

        // Get the week
        restWeekMockMvc.perform(get("/api/weeks/{id}", week.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(week.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.week").value(DEFAULT_WEEK));
    }

    @Test
    @Transactional
    public void getNonExistingWeek() throws Exception {
        // Get the week
        restWeekMockMvc.perform(get("/api/weeks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeek() throws Exception {
        // Initialize the database
        weekRepository.saveAndFlush(week);

		int databaseSizeBeforeUpdate = weekRepository.findAll().size();

        // Update the week
        week.setName(UPDATED_NAME);
        week.setWeek(UPDATED_WEEK);

        restWeekMockMvc.perform(put("/api/weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(week)))
                .andExpect(status().isOk());

        // Validate the Week in the database
        List<Week> weeks = weekRepository.findAll();
        assertThat(weeks).hasSize(databaseSizeBeforeUpdate);
        Week testWeek = weeks.get(weeks.size() - 1);
        assertThat(testWeek.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWeek.getWeek()).isEqualTo(UPDATED_WEEK);
    }

    @Test
    @Transactional
    public void deleteWeek() throws Exception {
        // Initialize the database
        weekRepository.saveAndFlush(week);

		int databaseSizeBeforeDelete = weekRepository.findAll().size();

        // Get the week
        restWeekMockMvc.perform(delete("/api/weeks/{id}", week.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Week> weeks = weekRepository.findAll();
        assertThat(weeks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
