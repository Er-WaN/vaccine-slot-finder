package com.wandev.vaccineslotfinder.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wandev.vaccineslotfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VaccinationSlotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VaccinationSlot.class);
        VaccinationSlot vaccinationSlot1 = new VaccinationSlot();
        vaccinationSlot1.setId(1L);
        VaccinationSlot vaccinationSlot2 = new VaccinationSlot();
        vaccinationSlot2.setId(vaccinationSlot1.getId());
        assertThat(vaccinationSlot1).isEqualTo(vaccinationSlot2);
        vaccinationSlot2.setId(2L);
        assertThat(vaccinationSlot1).isNotEqualTo(vaccinationSlot2);
        vaccinationSlot1.setId(null);
        assertThat(vaccinationSlot1).isNotEqualTo(vaccinationSlot2);
    }
}
