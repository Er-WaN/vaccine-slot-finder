package com.wandev.vaccineslotfinder.service.dto;

import com.wandev.vaccineslotfinder.domain.VaccinationCenter;
import com.wandev.vaccineslotfinder.domain.VaccinationSlot;
import java.util.LinkedHashMap;
import java.util.List;

public class NotifyFreeSlotDTO {

    private VaccinationCenter center;

    private LinkedHashMap<String, List<VaccinationSlot>> slots = new LinkedHashMap<>();

    public VaccinationCenter getCenter() {
        return center;
    }

    public void setCenter(VaccinationCenter center) {
        this.center = center;
    }

    public LinkedHashMap<String, List<VaccinationSlot>> getSlots() {
        return slots;
    }

    public void setSlots(LinkedHashMap<String, List<VaccinationSlot>> slots) {
        this.slots = slots;
    }
}
