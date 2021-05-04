package com.wandev.vaccineslotfinder.web.rest;

import com.wandev.vaccineslotfinder.domain.Param;
import com.wandev.vaccineslotfinder.repository.ParamRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.wandev.vaccineslotfinder.domain.Param}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ParamResource {

    private final Logger log = LoggerFactory.getLogger(ParamResource.class);

    private static final String ENTITY_NAME = "param";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParamRepository paramRepository;

    public ParamResource(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    /**
     * {@code POST  /params} : Create a new param.
     *
     * @param param the param to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new param, or with status {@code 400 (Bad Request)} if the param has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/params")
    public ResponseEntity<Param> createParam(@Valid @RequestBody Param param) throws URISyntaxException {
        log.debug("REST request to save Param : {}", param);
        if (param.getId() != null) {
            throw new BadRequestAlertException("A new param cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Param result = paramRepository.save(param);
        return ResponseEntity
            .created(new URI("/api/params/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /params/:id} : Updates an existing param.
     *
     * @param id the id of the param to save.
     * @param param the param to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated param,
     * or with status {@code 400 (Bad Request)} if the param is not valid,
     * or with status {@code 500 (Internal Server Error)} if the param couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/params/{id}")
    public ResponseEntity<Param> updateParam(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Param param)
        throws URISyntaxException {
        log.debug("REST request to update Param : {}, {}", id, param);
        if (param.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, param.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Param result = paramRepository.save(param);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, param.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /params/:id} : Partial updates given fields of an existing param, field will ignore if it is null
     *
     * @param id the id of the param to save.
     * @param param the param to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated param,
     * or with status {@code 400 (Bad Request)} if the param is not valid,
     * or with status {@code 404 (Not Found)} if the param is not found,
     * or with status {@code 500 (Internal Server Error)} if the param couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/params/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Param> partialUpdateParam(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Param param
    ) throws URISyntaxException {
        log.debug("REST request to partial update Param partially : {}, {}", id, param);
        if (param.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, param.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Param> result = paramRepository
            .findById(param.getId())
            .map(
                existingParam -> {
                    if (param.getName() != null) {
                        existingParam.setName(param.getName());
                    }
                    if (param.getValue() != null) {
                        existingParam.setValue(param.getValue());
                    }
                    if (param.getDescription() != null) {
                        existingParam.setDescription(param.getDescription());
                    }

                    return existingParam;
                }
            )
            .map(paramRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, param.getId().toString())
        );
    }

    /**
     * {@code GET  /params} : get all the params.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of params in body.
     */
    @GetMapping("/params")
    public List<Param> getAllParams() {
        log.debug("REST request to get all Params");
        return paramRepository.findAll();
    }

    /**
     * {@code GET  /params/:id} : get the "id" param.
     *
     * @param id the id of the param to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the param, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/params/{id}")
    public ResponseEntity<Param> getParam(@PathVariable Long id) {
        log.debug("REST request to get Param : {}", id);
        Optional<Param> param = paramRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(param);
    }

    /**
     * {@code DELETE  /params/:id} : delete the "id" param.
     *
     * @param id the id of the param to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/params/{id}")
    public ResponseEntity<Void> deleteParam(@PathVariable Long id) {
        log.debug("REST request to delete Param : {}", id);
        paramRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
