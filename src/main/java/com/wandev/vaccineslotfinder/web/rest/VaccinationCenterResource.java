package com.wandev.vaccineslotfinder.web.rest;

import com.wandev.vaccineslotfinder.domain.VaccinationCenter;
import com.wandev.vaccineslotfinder.repository.VaccinationCenterRepository;
import com.wandev.vaccineslotfinder.service.VaccinationCenterService;
import com.wandev.vaccineslotfinder.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.wandev.vaccineslotfinder.domain.VaccinationCenter}.
 */
@RestController
@RequestMapping("/api")
public class VaccinationCenterResource {

    private final Logger log = LoggerFactory.getLogger(VaccinationCenterResource.class);

    private static final String ENTITY_NAME = "vaccinationCenter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VaccinationCenterService vaccinationCenterService;

    private final VaccinationCenterRepository vaccinationCenterRepository;

    public VaccinationCenterResource(
        VaccinationCenterService vaccinationCenterService,
        VaccinationCenterRepository vaccinationCenterRepository
    ) {
        this.vaccinationCenterService = vaccinationCenterService;
        this.vaccinationCenterRepository = vaccinationCenterRepository;
    }

    /**
     * {@code POST  /vaccination-centers} : Create a new vaccinationCenter.
     *
     * @param vaccinationCenter the vaccinationCenter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vaccinationCenter, or with status {@code 400 (Bad Request)} if the vaccinationCenter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vaccination-centers")
    public ResponseEntity<VaccinationCenter> createVaccinationCenter(@Valid @RequestBody VaccinationCenter vaccinationCenter)
        throws URISyntaxException {
        log.debug("REST request to save VaccinationCenter : {}", vaccinationCenter);
        if (vaccinationCenter.getId() != null) {
            throw new BadRequestAlertException("A new vaccinationCenter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VaccinationCenter result = vaccinationCenterService.save(vaccinationCenter);
        return ResponseEntity
            .created(new URI("/api/vaccination-centers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vaccination-centers/:id} : Updates an existing vaccinationCenter.
     *
     * @param id the id of the vaccinationCenter to save.
     * @param vaccinationCenter the vaccinationCenter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccinationCenter,
     * or with status {@code 400 (Bad Request)} if the vaccinationCenter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vaccinationCenter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vaccination-centers/{id}")
    public ResponseEntity<VaccinationCenter> updateVaccinationCenter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VaccinationCenter vaccinationCenter
    ) throws URISyntaxException {
        log.debug("REST request to update VaccinationCenter : {}, {}", id, vaccinationCenter);
        if (vaccinationCenter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccinationCenter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationCenterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VaccinationCenter result = vaccinationCenterService.save(vaccinationCenter);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vaccinationCenter.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vaccination-centers/:id} : Partial updates given fields of an existing vaccinationCenter, field will ignore if it is null
     *
     * @param id the id of the vaccinationCenter to save.
     * @param vaccinationCenter the vaccinationCenter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccinationCenter,
     * or with status {@code 400 (Bad Request)} if the vaccinationCenter is not valid,
     * or with status {@code 404 (Not Found)} if the vaccinationCenter is not found,
     * or with status {@code 500 (Internal Server Error)} if the vaccinationCenter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vaccination-centers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VaccinationCenter> partialUpdateVaccinationCenter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VaccinationCenter vaccinationCenter
    ) throws URISyntaxException {
        log.debug("REST request to partial update VaccinationCenter partially : {}, {}", id, vaccinationCenter);
        if (vaccinationCenter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccinationCenter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationCenterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VaccinationCenter> result = vaccinationCenterService.partialUpdate(vaccinationCenter);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vaccinationCenter.getId().toString())
        );
    }

    /**
     * {@code GET  /vaccination-centers} : get all the vaccinationCenters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vaccinationCenters in body.
     */
    @GetMapping("/vaccination-centers")
    public List<VaccinationCenter> getAllVaccinationCenters() {
        log.debug("REST request to get all VaccinationCenters");
        return vaccinationCenterService.findAll();
    }

    /**
     * {@code GET  /vaccination-centers/:id} : get the "id" vaccinationCenter.
     *
     * @param id the id of the vaccinationCenter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vaccinationCenter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vaccination-centers/{id}")
    public ResponseEntity<VaccinationCenter> getVaccinationCenter(@PathVariable Long id) {
        log.debug("REST request to get VaccinationCenter : {}", id);
        Optional<VaccinationCenter> vaccinationCenter = vaccinationCenterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vaccinationCenter);
    }

    /**
     * {@code DELETE  /vaccination-centers/:id} : delete the "id" vaccinationCenter.
     *
     * @param id the id of the vaccinationCenter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vaccination-centers/{id}")
    public ResponseEntity<Void> deleteVaccinationCenter(@PathVariable Long id) {
        log.debug("REST request to delete VaccinationCenter : {}", id);
        vaccinationCenterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
