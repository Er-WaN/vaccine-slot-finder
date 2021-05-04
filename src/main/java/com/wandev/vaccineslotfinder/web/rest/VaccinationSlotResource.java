package com.wandev.vaccineslotfinder.web.rest;

import com.wandev.vaccineslotfinder.domain.VaccinationSlot;
import com.wandev.vaccineslotfinder.repository.VaccinationSlotRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.wandev.vaccineslotfinder.domain.VaccinationSlot}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VaccinationSlotResource {

    private final Logger log = LoggerFactory.getLogger(VaccinationSlotResource.class);

    private static final String ENTITY_NAME = "vaccinationSlot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VaccinationSlotRepository vaccinationSlotRepository;

    public VaccinationSlotResource(VaccinationSlotRepository vaccinationSlotRepository) {
        this.vaccinationSlotRepository = vaccinationSlotRepository;
    }

    /**
     * {@code POST  /vaccination-slots} : Create a new vaccinationSlot.
     *
     * @param vaccinationSlot the vaccinationSlot to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vaccinationSlot, or with status {@code 400 (Bad Request)} if the vaccinationSlot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vaccination-slots")
    public ResponseEntity<VaccinationSlot> createVaccinationSlot(@Valid @RequestBody VaccinationSlot vaccinationSlot)
        throws URISyntaxException {
        log.debug("REST request to save VaccinationSlot : {}", vaccinationSlot);
        if (vaccinationSlot.getId() != null) {
            throw new BadRequestAlertException("A new vaccinationSlot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VaccinationSlot result = vaccinationSlotRepository.save(vaccinationSlot);
        return ResponseEntity
            .created(new URI("/api/vaccination-slots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vaccination-slots/:id} : Updates an existing vaccinationSlot.
     *
     * @param id the id of the vaccinationSlot to save.
     * @param vaccinationSlot the vaccinationSlot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccinationSlot,
     * or with status {@code 400 (Bad Request)} if the vaccinationSlot is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vaccinationSlot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vaccination-slots/{id}")
    public ResponseEntity<VaccinationSlot> updateVaccinationSlot(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VaccinationSlot vaccinationSlot
    ) throws URISyntaxException {
        log.debug("REST request to update VaccinationSlot : {}, {}", id, vaccinationSlot);
        if (vaccinationSlot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccinationSlot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationSlotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VaccinationSlot result = vaccinationSlotRepository.save(vaccinationSlot);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vaccinationSlot.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vaccination-slots/:id} : Partial updates given fields of an existing vaccinationSlot, field will ignore if it is null
     *
     * @param id the id of the vaccinationSlot to save.
     * @param vaccinationSlot the vaccinationSlot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccinationSlot,
     * or with status {@code 400 (Bad Request)} if the vaccinationSlot is not valid,
     * or with status {@code 404 (Not Found)} if the vaccinationSlot is not found,
     * or with status {@code 500 (Internal Server Error)} if the vaccinationSlot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vaccination-slots/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VaccinationSlot> partialUpdateVaccinationSlot(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VaccinationSlot vaccinationSlot
    ) throws URISyntaxException {
        log.debug("REST request to partial update VaccinationSlot partially : {}, {}", id, vaccinationSlot);
        if (vaccinationSlot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccinationSlot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationSlotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VaccinationSlot> result = vaccinationSlotRepository
            .findById(vaccinationSlot.getId())
            .map(
                existingVaccinationSlot -> {
                    if (vaccinationSlot.getDate() != null) {
                        existingVaccinationSlot.setDate(vaccinationSlot.getDate());
                    }
                    if (vaccinationSlot.getAlreadyTaken() != null) {
                        existingVaccinationSlot.setAlreadyTaken(vaccinationSlot.getAlreadyTaken());
                    }
                    if (vaccinationSlot.getCreationDate() != null) {
                        existingVaccinationSlot.setCreationDate(vaccinationSlot.getCreationDate());
                    }

                    return existingVaccinationSlot;
                }
            )
            .map(vaccinationSlotRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vaccinationSlot.getId().toString())
        );
    }

    /**
     * {@code GET  /vaccination-slots} : get all the vaccinationSlots.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vaccinationSlots in body.
     */
    @GetMapping("/vaccination-slots")
    public ResponseEntity<List<VaccinationSlot>> getAllVaccinationSlots(Pageable pageable) {
        log.debug("REST request to get a page of VaccinationSlots");
        Page<VaccinationSlot> page = vaccinationSlotRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vaccination-slots/:id} : get the "id" vaccinationSlot.
     *
     * @param id the id of the vaccinationSlot to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vaccinationSlot, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vaccination-slots/{id}")
    public ResponseEntity<VaccinationSlot> getVaccinationSlot(@PathVariable Long id) {
        log.debug("REST request to get VaccinationSlot : {}", id);
        Optional<VaccinationSlot> vaccinationSlot = vaccinationSlotRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vaccinationSlot);
    }

    /**
     * {@code DELETE  /vaccination-slots/:id} : delete the "id" vaccinationSlot.
     *
     * @param id the id of the vaccinationSlot to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vaccination-slots/{id}")
    public ResponseEntity<Void> deleteVaccinationSlot(@PathVariable Long id) {
        log.debug("REST request to delete VaccinationSlot : {}", id);
        vaccinationSlotRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
