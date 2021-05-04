package com.wandev.vaccineslotfinder.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wandev.vaccineslotfinder.IntegrationTest;
import com.wandev.vaccineslotfinder.domain.Param;
import com.wandev.vaccineslotfinder.repository.ParamRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ParamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParamResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/params";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParamRepository paramRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParamMockMvc;

    private Param param;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Param createEntity(EntityManager em) {
        Param param = new Param().name(DEFAULT_NAME).value(DEFAULT_VALUE).description(DEFAULT_DESCRIPTION);
        return param;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Param createUpdatedEntity(EntityManager em) {
        Param param = new Param().name(UPDATED_NAME).value(UPDATED_VALUE).description(UPDATED_DESCRIPTION);
        return param;
    }

    @BeforeEach
    public void initTest() {
        param = createEntity(em);
    }

    @Test
    @Transactional
    void createParam() throws Exception {
        int databaseSizeBeforeCreate = paramRepository.findAll().size();
        // Create the Param
        restParamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(param)))
            .andExpect(status().isCreated());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeCreate + 1);
        Param testParam = paramList.get(paramList.size() - 1);
        assertThat(testParam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testParam.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testParam.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createParamWithExistingId() throws Exception {
        // Create the Param with an existing ID
        param.setId(1L);

        int databaseSizeBeforeCreate = paramRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(param)))
            .andExpect(status().isBadRequest());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = paramRepository.findAll().size();
        // set the field null
        param.setName(null);

        // Create the Param, which fails.

        restParamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(param)))
            .andExpect(status().isBadRequest());

        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParams() throws Exception {
        // Initialize the database
        paramRepository.saveAndFlush(param);

        // Get all the paramList
        restParamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(param.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getParam() throws Exception {
        // Initialize the database
        paramRepository.saveAndFlush(param);

        // Get the param
        restParamMockMvc
            .perform(get(ENTITY_API_URL_ID, param.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(param.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingParam() throws Exception {
        // Get the param
        restParamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParam() throws Exception {
        // Initialize the database
        paramRepository.saveAndFlush(param);

        int databaseSizeBeforeUpdate = paramRepository.findAll().size();

        // Update the param
        Param updatedParam = paramRepository.findById(param.getId()).get();
        // Disconnect from session so that the updates on updatedParam are not directly saved in db
        em.detach(updatedParam);
        updatedParam.name(UPDATED_NAME).value(UPDATED_VALUE).description(UPDATED_DESCRIPTION);

        restParamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParam.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParam))
            )
            .andExpect(status().isOk());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeUpdate);
        Param testParam = paramList.get(paramList.size() - 1);
        assertThat(testParam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParam.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testParam.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingParam() throws Exception {
        int databaseSizeBeforeUpdate = paramRepository.findAll().size();
        param.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, param.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(param))
            )
            .andExpect(status().isBadRequest());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParam() throws Exception {
        int databaseSizeBeforeUpdate = paramRepository.findAll().size();
        param.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(param))
            )
            .andExpect(status().isBadRequest());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParam() throws Exception {
        int databaseSizeBeforeUpdate = paramRepository.findAll().size();
        param.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParamMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(param)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParamWithPatch() throws Exception {
        // Initialize the database
        paramRepository.saveAndFlush(param);

        int databaseSizeBeforeUpdate = paramRepository.findAll().size();

        // Update the param using partial update
        Param partialUpdatedParam = new Param();
        partialUpdatedParam.setId(param.getId());

        restParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParam.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParam))
            )
            .andExpect(status().isOk());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeUpdate);
        Param testParam = paramList.get(paramList.size() - 1);
        assertThat(testParam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testParam.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testParam.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateParamWithPatch() throws Exception {
        // Initialize the database
        paramRepository.saveAndFlush(param);

        int databaseSizeBeforeUpdate = paramRepository.findAll().size();

        // Update the param using partial update
        Param partialUpdatedParam = new Param();
        partialUpdatedParam.setId(param.getId());

        partialUpdatedParam.name(UPDATED_NAME).value(UPDATED_VALUE).description(UPDATED_DESCRIPTION);

        restParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParam.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParam))
            )
            .andExpect(status().isOk());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeUpdate);
        Param testParam = paramList.get(paramList.size() - 1);
        assertThat(testParam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParam.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testParam.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingParam() throws Exception {
        int databaseSizeBeforeUpdate = paramRepository.findAll().size();
        param.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, param.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(param))
            )
            .andExpect(status().isBadRequest());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParam() throws Exception {
        int databaseSizeBeforeUpdate = paramRepository.findAll().size();
        param.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(param))
            )
            .andExpect(status().isBadRequest());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParam() throws Exception {
        int databaseSizeBeforeUpdate = paramRepository.findAll().size();
        param.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParamMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(param)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParam() throws Exception {
        // Initialize the database
        paramRepository.saveAndFlush(param);

        int databaseSizeBeforeDelete = paramRepository.findAll().size();

        // Delete the param
        restParamMockMvc
            .perform(delete(ENTITY_API_URL_ID, param.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
