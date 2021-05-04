package com.wandev.vaccineslotfinder.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wandev.vaccineslotfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Param.class);
        Param param1 = new Param();
        param1.setId(1L);
        Param param2 = new Param();
        param2.setId(param1.getId());
        assertThat(param1).isEqualTo(param2);
        param2.setId(2L);
        assertThat(param1).isNotEqualTo(param2);
        param1.setId(null);
        assertThat(param1).isNotEqualTo(param2);
    }
}
