package com.fruitcrm.web.rest;

import com.fruitcrm.Application;
import com.fruitcrm.domain.FruitPack;
import com.fruitcrm.repository.FruitPackRepository;
import com.fruitcrm.repository.search.FruitPackSearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FruitPackResource REST controller.
 *
 * @see FruitPackResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FruitPackResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    @Inject
    private FruitPackRepository fruitPackRepository;

    @Inject
    private FruitPackSearchRepository fruitPackSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFruitPackMockMvc;

    private FruitPack fruitPack;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FruitPackResource fruitPackResource = new FruitPackResource();
        ReflectionTestUtils.setField(fruitPackResource, "fruitPackSearchRepository", fruitPackSearchRepository);
        ReflectionTestUtils.setField(fruitPackResource, "fruitPackRepository", fruitPackRepository);
        this.restFruitPackMockMvc = MockMvcBuilders.standaloneSetup(fruitPackResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fruitPack = new FruitPack();
        fruitPack.setName(DEFAULT_NAME);
        fruitPack.setDescription(DEFAULT_DESCRIPTION);
        fruitPack.setPrice(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createFruitPack() throws Exception {
        int databaseSizeBeforeCreate = fruitPackRepository.findAll().size();

        // Create the FruitPack

        restFruitPackMockMvc.perform(post("/api/fruitPacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fruitPack)))
                .andExpect(status().isCreated());

        // Validate the FruitPack in the database
        List<FruitPack> fruitPacks = fruitPackRepository.findAll();
        assertThat(fruitPacks).hasSize(databaseSizeBeforeCreate + 1);
        FruitPack testFruitPack = fruitPacks.get(fruitPacks.size() - 1);
        assertThat(testFruitPack.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFruitPack.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFruitPack.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fruitPackRepository.findAll().size();
        // set the field null
        fruitPack.setName(null);

        // Create the FruitPack, which fails.

        restFruitPackMockMvc.perform(post("/api/fruitPacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fruitPack)))
                .andExpect(status().isBadRequest());

        List<FruitPack> fruitPacks = fruitPackRepository.findAll();
        assertThat(fruitPacks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = fruitPackRepository.findAll().size();
        // set the field null
        fruitPack.setDescription(null);

        // Create the FruitPack, which fails.

        restFruitPackMockMvc.perform(post("/api/fruitPacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fruitPack)))
                .andExpect(status().isBadRequest());

        List<FruitPack> fruitPacks = fruitPackRepository.findAll();
        assertThat(fruitPacks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = fruitPackRepository.findAll().size();
        // set the field null
        fruitPack.setPrice(null);

        // Create the FruitPack, which fails.

        restFruitPackMockMvc.perform(post("/api/fruitPacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fruitPack)))
                .andExpect(status().isBadRequest());

        List<FruitPack> fruitPacks = fruitPackRepository.findAll();
        assertThat(fruitPacks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFruitPacks() throws Exception {
        // Initialize the database
        fruitPackRepository.saveAndFlush(fruitPack);

        // Get all the fruitPacks
        restFruitPackMockMvc.perform(get("/api/fruitPacks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fruitPack.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getFruitPack() throws Exception {
        // Initialize the database
        fruitPackRepository.saveAndFlush(fruitPack);

        // Get the fruitPack
        restFruitPackMockMvc.perform(get("/api/fruitPacks/{id}", fruitPack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fruitPack.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFruitPack() throws Exception {
        // Get the fruitPack
        restFruitPackMockMvc.perform(get("/api/fruitPacks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFruitPack() throws Exception {
        // Initialize the database
        fruitPackRepository.saveAndFlush(fruitPack);

		int databaseSizeBeforeUpdate = fruitPackRepository.findAll().size();

        // Update the fruitPack
        fruitPack.setName(UPDATED_NAME);
        fruitPack.setDescription(UPDATED_DESCRIPTION);
        fruitPack.setPrice(UPDATED_PRICE);

        restFruitPackMockMvc.perform(put("/api/fruitPacks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fruitPack)))
                .andExpect(status().isOk());

        // Validate the FruitPack in the database
        List<FruitPack> fruitPacks = fruitPackRepository.findAll();
        assertThat(fruitPacks).hasSize(databaseSizeBeforeUpdate);
        FruitPack testFruitPack = fruitPacks.get(fruitPacks.size() - 1);
        assertThat(testFruitPack.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFruitPack.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFruitPack.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void deleteFruitPack() throws Exception {
        // Initialize the database
        fruitPackRepository.saveAndFlush(fruitPack);

		int databaseSizeBeforeDelete = fruitPackRepository.findAll().size();

        // Get the fruitPack
        restFruitPackMockMvc.perform(delete("/api/fruitPacks/{id}", fruitPack.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FruitPack> fruitPacks = fruitPackRepository.findAll();
        assertThat(fruitPacks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
