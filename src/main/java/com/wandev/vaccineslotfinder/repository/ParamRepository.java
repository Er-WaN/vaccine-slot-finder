package com.wandev.vaccineslotfinder.repository;

import com.wandev.vaccineslotfinder.domain.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Param entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParamRepository extends JpaRepository<Param, Long> {
    /**
     * Find param by name
     *
     * @param name Name
     * @return Param
     */
    Param findParamByNameEquals(String name);
}
