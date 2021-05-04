package com.wandev.vaccineslotfinder.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wandev.vaccineslotfinder.IntegrationTest;
import com.wandev.vaccineslotfinder.domain.VaccinationCenter;
import com.wandev.vaccineslotfinder.repository.VaccinationCenterRepository;
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
 * Integration tests for the {@link VaccinationCenterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaccinationCenterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_API_URL = "AAAAAAAAAA";
    private static final String UPDATED_API_URL = "BBBBBBBBBB";

    private static final String DEFAULT_RESERVATION_URL = "AAAAAAAAAA";
    private static final String UPDATED_RESERVATION_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/vaccination-centers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VaccinationCenterRepository vaccinationCenterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaccinationCenterMockMvc;

    private VaccinationCenter vaccinationCenter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VaccinationCenter createEntity(EntityManager em) {
        VaccinationCenter vaccinationCenter = new VaccinationCenter()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .apiUrl(DEFAULT_API_URL)
            .reservationUrl(DEFAULT_RESERVATION_URL)
            .enabled(DEFAULT_ENABLED);
        return vaccinationCenter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VaccinationCenter createUpdatedEntity(EntityManager em) {
        VaccinationCenter vaccinationCenter = new VaccinationCenter()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .apiUrl(UPDATED_API_URL)
            .reservationUrl(UPDATED_RESERVATION_URL)
            .enabled(UPDATED_ENABLED);
        return vaccinationCenter;
    }

    @BeforeEach
    public void initTest() {
        vaccinationCenter = createEntity(em);
    }

    @Test
    @Transactional
    void createVaccinationCenter() throws Exception {
        int databaseSizeBeforeCreate = vaccinationCenterRepository.findAll().size();
        // Create the VaccinationCenter
        restVaccinationCenterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccinationCenter))
            )
            .andExpect(status().isCreated());

        // Validate the VaccinationCenter in the database
        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeCreate + 1);
        VaccinationCenter testVaccinationCenter = vaccinationCenterList.get(vaccinationCenterList.size() - 1);
        assertThat(testVaccinationCenter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVaccinationCenter.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testVaccinationCenter.getApiUrl()).isEqualTo(DEFAULT_API_URL);
        assertThat(testVaccinationCenter.getReservationUrl()).isEqualTo(DEFAULT_RESERVATION_URL);
        assertThat(testVaccinationCenter.getEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    void createVaccinationCenterWithExistingId() throws Exception {
        // Create the VaccinationCenter with an existing ID
        vaccinationCenter.setId(1L);

        int databaseSizeBeforeCreate = vaccinationCenterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaccinationCenterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccinationCenter))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationCenter in the database
        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccinationCenterRepository.findAll().size();
        // set the field null
        vaccinationCenter.setName(null);

        // Create the VaccinationCenter, which fails.

        restVaccinationCenterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccinationCenter))
            )
            .andExpect(status().isBadRequest());

        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccinationCenterRepository.findAll().size();
        // set the field null
        vaccinationCenter.setAddress(null);

        // Create the VaccinationCenter, which fails.

        restVaccinationCenterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccinationCenter))
            )
            .andExpect(status().isBadRequest());

        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApiUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccinationCenterRepository.findAll().size();
        // set the field null
        vaccinationCenter.setApiUrl(null);

        // Create the VaccinationCenter, which fails.

        restVaccinationCenterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccinationCenter))
            )
            .andExpect(status().isBadRequest());

        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccinationCenterRepository.findAll().size();
        // set the field null
        vaccinationCenter.setEnabled(null);

        // Create the VaccinationCenter, which fails.

        restVaccinationCenterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccinationCenter))
            )
            .andExpect(status().isBadRequest());

        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVaccinationCenters() throws Exception {
        // Initialize the database
        vaccinationCenterRepository.saveAndFlush(vaccinationCenter);

        // Get all the vaccinationCenterList
        restVaccinationCenterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccinationCenter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].apiUrl").value(hasItem(DEFAULT_API_URL)))
            .andExpect(jsonPath("$.[*].reservationUrl").value(hasItem(DEFAULT_RESERVATION_URL)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getVaccinationCenter() throws Exception {
        // Initialize the database
        vaccinationCenterRepository.saveAndFlush(vaccinationCenter);

        // Get the vaccinationCenter
        restVaccinationCenterMockMvc
            .perform(get(ENTITY_API_URL_ID, vaccinationCenter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaccinationCenter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.apiUrl").value(DEFAULT_API_URL))
            .andExpect(jsonPath("$.reservationUrl").value(DEFAULT_RESERVATION_URL))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingVaccinationCenter() throws Exception {
        // Get the vaccinationCenter
        restVaccinationCenterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVaccinationCenter() throws Exception {
        // Initialize the database
        vaccinationCenterRepository.saveAndFlush(vaccinationCenter);

        int databaseSizeBeforeUpdate = vaccinationCenterRepository.findAll().size();

        // Update the vaccinationCenter
        VaccinationCenter updatedVaccinationCenter = vaccinationCenterRepository.findById(vaccinationCenter.getId()).get();
        // Disconnect from session so that the updates on updatedVaccinationCenter are not directly saved in db
        em.detach(updatedVaccinationCenter);
        updatedVaccinationCenter
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .apiUrl(UPDATED_API_URL)
            .reservationUrl(UPDATED_RESERVATION_URL)
            .enabled(UPDATED_ENABLED);

        restVaccinationCenterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVaccinationCenter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVaccinationCenter))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationCenter in the database
        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeUpdate);
        VaccinationCenter testVaccinationCenter = vaccinationCenterList.get(vaccinationCenterList.size() - 1);
        assertThat(testVaccinationCenter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVaccinationCenter.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testVaccinationCenter.getApiUrl()).isEqualTo(UPDATED_API_URL);
        assertThat(testVaccinationCenter.getReservationUrl()).isEqualTo(UPDATED_RESERVATION_URL);
        assertThat(testVaccinationCenter.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void putNonExistingVaccinationCenter() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationCenterRepository.findAll().size();
        vaccinationCenter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationCenterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccinationCenter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationCenter))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationCenter in the database
        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaccinationCenter() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationCenterRepository.findAll().size();
        vaccinationCenter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationCenterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationCenter))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationCenter in the database
        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaccinationCenter() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationCenterRepository.findAll().size();
        vaccinationCenter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationCenterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccinationCenter))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VaccinationCenter in the database
        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaccinationCenterWithPatch() throws Exception {
        // Initialize the database
        vaccinationCenterRepository.saveAndFlush(vaccinationCenter);

        int databaseSizeBeforeUpdate = vaccinationCenterRepository.findAll().size();

        // Update the vaccinationCenter using partial update
        VaccinationCenter partialUpdatedVaccinationCenter = new VaccinationCenter();
        partialUpdatedVaccinationCenter.setId(vaccinationCenter.getId());

        partialUpdatedVaccinationCenter.apiUrl(UPDATED_API_URL).enabled(UPDATED_ENABLED);

        restVaccinationCenterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccinationCenter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccinationCenter))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationCenter in the database
        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeUpdate);
        VaccinationCenter testVaccinationCenter = vaccinationCenterList.get(vaccinationCenterList.size() - 1);
        assertThat(testVaccinationCenter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVaccinationCenter.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testVaccinationCenter.getApiUrl()).isEqualTo(UPDATED_API_URL);
        assertThat(testVaccinationCenter.getReservationUrl()).isEqualTo(DEFAULT_RESERVATION_URL);
        assertThat(testVaccinationCenter.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void fullUpdateVaccinationCenterWithPatch() throws Exception {
        // Initialize the database
        vaccinationCenterRepository.saveAndFlush(vaccinationCenter);

        int databaseSizeBeforeUpdate = vaccinationCenterRepository.findAll().size();

        // Update the vaccinationCenter using partial update
        VaccinationCenter partialUpdatedVaccinationCenter = new VaccinationCenter();
        partialUpdatedVaccinationCenter.setId(vaccinationCenter.getId());

        partialUpdatedVaccinationCenter
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .apiUrl(UPDATED_API_URL)
            .reservationUrl(UPDATED_RESERVATION_URL)
            .enabled(UPDATED_ENABLED);

        restVaccinationCenterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccinationCenter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccinationCenter))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationCenter in the database
        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeUpdate);
        VaccinationCenter testVaccinationCenter = vaccinationCenterList.get(vaccinationCenterList.size() - 1);
        assertThat(testVaccinationCenter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVaccinationCenter.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testVaccinationCenter.getApiUrl()).isEqualTo(UPDATED_API_URL);
        assertThat(testVaccinationCenter.getReservationUrl()).isEqualTo(UPDATED_RESERVATION_URL);
        assertThat(testVaccinationCenter.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void patchNonExistingVaccinationCenter() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationCenterRepository.findAll().size();
        vaccinationCenter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationCenterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vaccinationCenter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationCenter))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationCenter in the database
        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaccinationCenter() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationCenterRepository.findAll().size();
        vaccinationCenter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationCenterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationCenter))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationCenter in the database
        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaccinationCenter() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationCenterRepository.findAll().size();
        vaccinationCenter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationCenterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationCenter))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VaccinationCenter in the database
        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaccinationCenter() throws Exception {
        // Initialize the database
        vaccinationCenterRepository.saveAndFlush(vaccinationCenter);

        int databaseSizeBeforeDelete = vaccinationCenterRepository.findAll().size();

        // Delete the vaccinationCenter
        restVaccinationCenterMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaccinationCenter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VaccinationCenter> vaccinationCenterList = vaccinationCenterRepository.findAll();
        assertThat(vaccinationCenterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
