package com.wandev.vaccineslotfinder.service.doctolib;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;

/**
 * Doctolib response
 */
public class DoctolibResponse {

    @JsonProperty("availabilities")
    public List<Availability> availabilities;

    @JsonProperty("total")
    public int total;

    @JsonProperty("reason")
    public String reason;

    @JsonProperty("message")
    public String message;

    @JsonProperty("number_future_vaccinations")
    public int numberFutureVaccinations;

    public List<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNumberFutureVaccinations() {
        return numberFutureVaccinations;
    }

    public void setNumberFutureVaccinations(int numberFutureVaccinations) {
        this.numberFutureVaccinations = numberFutureVaccinations;
    }

    public static class Step {

        @JsonProperty("agenda_id")
        public int agendaId;

        @JsonProperty("practitioner_agenda_id")
        public Object practitionerAgendaId;

        @JsonProperty("start_date")
        public Date startDate;

        @JsonProperty("end_date")
        public Date endDate;

        @JsonProperty("visit_motive_id")
        public int visitMotiveId;

        public int getAgendaId() {
            return agendaId;
        }

        public void setAgendaId(int agendaId) {
            this.agendaId = agendaId;
        }

        public Object getPractitionerAgendaId() {
            return practitionerAgendaId;
        }

        public void setPractitionerAgendaId(Object practitionerAgendaId) {
            this.practitionerAgendaId = practitionerAgendaId;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public int getVisitMotiveId() {
            return visitMotiveId;
        }

        public void setVisitMotiveId(int visitMotiveId) {
            this.visitMotiveId = visitMotiveId;
        }
    }

    public static class Slot {

        @JsonProperty("agenda_id")
        public int agendaId;

        @JsonProperty("practitioner_agenda_id")
        public Object practitionerAgendaId;

        @JsonProperty("start_date")
        public Date startDate;

        @JsonProperty("end_date")
        public Date endDate;

        @JsonProperty("steps")
        public List<Step> steps;

        public int getAgendaId() {
            return agendaId;
        }

        public void setAgendaId(int agendaId) {
            this.agendaId = agendaId;
        }

        public Object getPractitionerAgendaId() {
            return practitionerAgendaId;
        }

        public void setPractitionerAgendaId(Object practitionerAgendaId) {
            this.practitionerAgendaId = practitionerAgendaId;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public List<Step> getSteps() {
            return steps;
        }

        public void setSteps(List<Step> steps) {
            this.steps = steps;
        }
    }

    public static class Availability {

        @JsonProperty("date")
        public String date;

        @JsonProperty("slots")
        public List<Slot> slots;

        @JsonProperty("substitution")
        public Object substitution;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<Slot> getSlots() {
            return slots;
        }

        public void setSlots(List<Slot> slots) {
            this.slots = slots;
        }

        public Object getSubstitution() {
            return substitution;
        }

        public void setSubstitution(Object substitution) {
            this.substitution = substitution;
        }
    }
}
