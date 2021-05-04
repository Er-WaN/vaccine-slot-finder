package com.wandev.vaccineslotfinder.repository;

import com.wandev.vaccineslotfinder.domain.VaccinationCenter;
import com.wandev.vaccineslotfinder.domain.VaccinationSlot;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VaccinationSlot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaccinationSlotRepository extends JpaRepository<VaccinationSlot, Long> {
    List<VaccinationSlot> findVaccinationSlotByAlreadyTakenFalseAndVaccinationCenter(VaccinationCenter vaccinationCenter);
}
