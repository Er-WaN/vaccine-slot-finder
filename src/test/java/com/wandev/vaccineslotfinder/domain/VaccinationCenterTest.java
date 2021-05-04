package com.wandev.vaccineslotfinder.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wandev.vaccineslotfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VaccinationCenterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VaccinationCenter.class);
        VaccinationCenter vaccinationCenter1 = new VaccinationCenter();
        vaccinationCenter1.setId(1L);
        VaccinationCenter vaccinationCenter2 = new VaccinationCenter();
        vaccinationCenter2.setId(vaccinationCenter1.getId());
        assertThat(vaccinationCenter1).isEqualTo(vaccinationCenter2);
        vaccinationCenter2.setId(2L);
        assertThat(vaccinationCenter1).isNotEqualTo(vaccinationCenter2);
        vaccinationCenter1.setId(null);
        assertThat(vaccinationCenter1).isNotEqualTo(vaccinationCenter2);
    }
}
