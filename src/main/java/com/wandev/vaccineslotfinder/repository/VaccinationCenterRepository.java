package com.wandev.vaccineslotfinder.repository;

import com.wandev.vaccineslotfinder.domain.VaccinationCenter;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VaccinationCenter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaccinationCenterRepository extends JpaRepository<VaccinationCenter, Long> {
    /**
     * Find all enabled vaccination centers
     * @return List
     */
    List<VaccinationCenter> findByEnabledTrue();
}
