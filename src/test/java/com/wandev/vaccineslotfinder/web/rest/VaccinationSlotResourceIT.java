package com.wandev.vaccineslotfinder.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wandev.vaccineslotfinder.IntegrationTest;
import com.wandev.vaccineslotfinder.domain.VaccinationSlot;
import com.wandev.vaccineslotfinder.repository.VaccinationSlotRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link VaccinationSlotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaccinationSlotResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ALREADY_TAKEN = false;
    private static final Boolean UPDATED_ALREADY_TAKEN = true;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/vaccination-slots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VaccinationSlotRepository vaccinationSlotRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaccinationSlotMockMvc;

    private VaccinationSlot vaccinationSlot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VaccinationSlot createEntity(EntityManager em) {
        VaccinationSlot vaccinationSlot = new VaccinationSlot()
            .date(DEFAULT_DATE)
            .alreadyTaken(DEFAULT_ALREADY_TAKEN)
            .creationDate(DEFAULT_CREATION_DATE);
        return vaccinationSlot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VaccinationSlot createUpdatedEntity(EntityManager em) {
        VaccinationSlot vaccinationSlot = new VaccinationSlot()
            .date(UPDATED_DATE)
            .alreadyTaken(UPDATED_ALREADY_TAKEN)
            .creationDate(UPDATED_CREATION_DATE);
        return vaccinationSlot;
    }

    @BeforeEach
    public void initTest() {
        vaccinationSlot = createEntity(em);
    }

    @Test
    @Transactional
    void createVaccinationSlot() throws Exception {
        int databaseSizeBeforeCreate = vaccinationSlotRepository.findAll().size();
        // Create the VaccinationSlot
        restVaccinationSlotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccinationSlot))
            )
            .andExpect(status().isCreated());

        // Validate the VaccinationSlot in the database
        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeCreate + 1);
        VaccinationSlot testVaccinationSlot = vaccinationSlotList.get(vaccinationSlotList.size() - 1);
        assertThat(testVaccinationSlot.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testVaccinationSlot.getAlreadyTaken()).isEqualTo(DEFAULT_ALREADY_TAKEN);
        assertThat(testVaccinationSlot.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void createVaccinationSlotWithExistingId() throws Exception {
        // Create the VaccinationSlot with an existing ID
        vaccinationSlot.setId(1L);

        int databaseSizeBeforeCreate = vaccinationSlotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaccinationSlotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccinationSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationSlot in the database
        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccinationSlotRepository.findAll().size();
        // set the field null
        vaccinationSlot.setDate(null);

        // Create the VaccinationSlot, which fails.

        restVaccinationSlotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccinationSlot))
            )
            .andExpect(status().isBadRequest());

        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVaccinationSlots() throws Exception {
        // Initialize the database
        vaccinationSlotRepository.saveAndFlush(vaccinationSlot);

        // Get all the vaccinationSlotList
        restVaccinationSlotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccinationSlot.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].alreadyTaken").value(hasItem(DEFAULT_ALREADY_TAKEN.booleanValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getVaccinationSlot() throws Exception {
        // Initialize the database
        vaccinationSlotRepository.saveAndFlush(vaccinationSlot);

        // Get the vaccinationSlot
        restVaccinationSlotMockMvc
            .perform(get(ENTITY_API_URL_ID, vaccinationSlot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaccinationSlot.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.alreadyTaken").value(DEFAULT_ALREADY_TAKEN.booleanValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVaccinationSlot() throws Exception {
        // Get the vaccinationSlot
        restVaccinationSlotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVaccinationSlot() throws Exception {
        // Initialize the database
        vaccinationSlotRepository.saveAndFlush(vaccinationSlot);

        int databaseSizeBeforeUpdate = vaccinationSlotRepository.findAll().size();

        // Update the vaccinationSlot
        VaccinationSlot updatedVaccinationSlot = vaccinationSlotRepository.findById(vaccinationSlot.getId()).get();
        // Disconnect from session so that the updates on updatedVaccinationSlot are not directly saved in db
        em.detach(updatedVaccinationSlot);
        updatedVaccinationSlot.date(UPDATED_DATE).alreadyTaken(UPDATED_ALREADY_TAKEN).creationDate(UPDATED_CREATION_DATE);

        restVaccinationSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVaccinationSlot.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVaccinationSlot))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationSlot in the database
        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeUpdate);
        VaccinationSlot testVaccinationSlot = vaccinationSlotList.get(vaccinationSlotList.size() - 1);
        assertThat(testVaccinationSlot.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVaccinationSlot.getAlreadyTaken()).isEqualTo(UPDATED_ALREADY_TAKEN);
        assertThat(testVaccinationSlot.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingVaccinationSlot() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationSlotRepository.findAll().size();
        vaccinationSlot.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccinationSlot.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationSlot in the database
        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaccinationSlot() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationSlotRepository.findAll().size();
        vaccinationSlot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationSlot in the database
        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaccinationSlot() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationSlotRepository.findAll().size();
        vaccinationSlot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationSlotMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccinationSlot))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VaccinationSlot in the database
        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaccinationSlotWithPatch() throws Exception {
        // Initialize the database
        vaccinationSlotRepository.saveAndFlush(vaccinationSlot);

        int databaseSizeBeforeUpdate = vaccinationSlotRepository.findAll().size();

        // Update the vaccinationSlot using partial update
        VaccinationSlot partialUpdatedVaccinationSlot = new VaccinationSlot();
        partialUpdatedVaccinationSlot.setId(vaccinationSlot.getId());

        partialUpdatedVaccinationSlot.alreadyTaken(UPDATED_ALREADY_TAKEN).creationDate(UPDATED_CREATION_DATE);

        restVaccinationSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccinationSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccinationSlot))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationSlot in the database
        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeUpdate);
        VaccinationSlot testVaccinationSlot = vaccinationSlotList.get(vaccinationSlotList.size() - 1);
        assertThat(testVaccinationSlot.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testVaccinationSlot.getAlreadyTaken()).isEqualTo(UPDATED_ALREADY_TAKEN);
        assertThat(testVaccinationSlot.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateVaccinationSlotWithPatch() throws Exception {
        // Initialize the database
        vaccinationSlotRepository.saveAndFlush(vaccinationSlot);

        int databaseSizeBeforeUpdate = vaccinationSlotRepository.findAll().size();

        // Update the vaccinationSlot using partial update
        VaccinationSlot partialUpdatedVaccinationSlot = new VaccinationSlot();
        partialUpdatedVaccinationSlot.setId(vaccinationSlot.getId());

        partialUpdatedVaccinationSlot.date(UPDATED_DATE).alreadyTaken(UPDATED_ALREADY_TAKEN).creationDate(UPDATED_CREATION_DATE);

        restVaccinationSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccinationSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccinationSlot))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationSlot in the database
        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeUpdate);
        VaccinationSlot testVaccinationSlot = vaccinationSlotList.get(vaccinationSlotList.size() - 1);
        assertThat(testVaccinationSlot.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVaccinationSlot.getAlreadyTaken()).isEqualTo(UPDATED_ALREADY_TAKEN);
        assertThat(testVaccinationSlot.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingVaccinationSlot() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationSlotRepository.findAll().size();
        vaccinationSlot.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vaccinationSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationSlot in the database
        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaccinationSlot() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationSlotRepository.findAll().size();
        vaccinationSlot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationSlot in the database
        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaccinationSlot() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationSlotRepository.findAll().size();
        vaccinationSlot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationSlotMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationSlot))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VaccinationSlot in the database
        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaccinationSlot() throws Exception {
        // Initialize the database
        vaccinationSlotRepository.saveAndFlush(vaccinationSlot);

        int databaseSizeBeforeDelete = vaccinationSlotRepository.findAll().size();

        // Delete the vaccinationSlot
        restVaccinationSlotMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaccinationSlot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VaccinationSlot> vaccinationSlotList = vaccinationSlotRepository.findAll();
        assertThat(vaccinationSlotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
