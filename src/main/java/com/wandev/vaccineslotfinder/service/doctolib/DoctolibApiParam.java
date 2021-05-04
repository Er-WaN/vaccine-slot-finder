package com.wandev.vaccineslotfinder.service.doctolib;

/**
 * Param in Doctolib API URL
 */
public enum DoctolibApiParam {
    START_DATE("start_date"),
    VISIT_MOTIVE_ID("visit_motive_ids"),
    AGENDA_ID("agenda_ids"),
    INSURANCE_SECTOR("insurance_sector"),
    PRACTICE_ID("practice_ids"),
    DESTROY_TEMPORARY("destroy_temporary"),
    LIMIT("limit");

    public final String param;

    DoctolibApiParam(String param) {
        this.param = param;
    }
}
