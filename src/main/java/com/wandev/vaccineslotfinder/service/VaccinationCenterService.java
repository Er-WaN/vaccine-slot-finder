package com.wandev.vaccineslotfinder.service;

import com.wandev.vaccineslotfinder.domain.VaccinationCenter;
import com.wandev.vaccineslotfinder.repository.VaccinationCenterRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VaccinationCenter}.
 */
@Service
@Transactional
public class VaccinationCenterService {

    private final Logger log = LoggerFactory.getLogger(VaccinationCenterService.class);

    private final VaccinationCenterRepository vaccinationCenterRepository;

    public VaccinationCenterService(VaccinationCenterRepository vaccinationCenterRepository) {
        this.vaccinationCenterRepository = vaccinationCenterRepository;
    }

    /**
     * Save a vaccinationCenter.
     *
     * @param vaccinationCenter the entity to save.
     * @return the persisted entity.
     */
    public VaccinationCenter save(VaccinationCenter vaccinationCenter) {
        log.debug("Request to save VaccinationCenter : {}", vaccinationCenter);
        return vaccinationCenterRepository.save(vaccinationCenter);
    }

    /**
     * Partially update a vaccinationCenter.
     *
     * @param vaccinationCenter the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VaccinationCenter> partialUpdate(VaccinationCenter vaccinationCenter) {
        log.debug("Request to partially update VaccinationCenter : {}", vaccinationCenter);

        return vaccinationCenterRepository
            .findById(vaccinationCenter.getId())
            .map(
                existingVaccinationCenter -> {
                    if (vaccinationCenter.getName() != null) {
                        existingVaccinationCenter.setName(vaccinationCenter.getName());
                    }
                    if (vaccinationCenter.getAddress() != null) {
                        existingVaccinationCenter.setAddress(vaccinationCenter.getAddress());
                    }
                    if (vaccinationCenter.getApiUrl() != null) {
                        existingVaccinationCenter.setApiUrl(vaccinationCenter.getApiUrl());
                    }
                    if (vaccinationCenter.getReservationUrl() != null) {
                        existingVaccinationCenter.setReservationUrl(vaccinationCenter.getReservationUrl());
                    }
                    if (vaccinationCenter.getEnabled() != null) {
                        existingVaccinationCenter.setEnabled(vaccinationCenter.getEnabled());
                    }

                    return existingVaccinationCenter;
                }
            )
            .map(vaccinationCenterRepository::save);
    }

    /**
     * Get all the vaccinationCenters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VaccinationCenter> findAll() {
        log.debug("Request to get all VaccinationCenters");
        return vaccinationCenterRepository.findAll();
    }

    /**
     * Get one vaccinationCenter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VaccinationCenter> findOne(Long id) {
        log.debug("Request to get VaccinationCenter : {}", id);
        return vaccinationCenterRepository.findById(id);
    }

    /**
     * Delete the vaccinationCenter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VaccinationCenter : {}", id);
        vaccinationCenterRepository.deleteById(id);
    }
}
