package com.wandev.vaccineslotfinder.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VaccinationSlot.
 */
@Entity
@Table(name = "vaccination_slot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VaccinationSlot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "already_taken")
    private Boolean alreadyTaken;

    @Column(name = "creation_date")
    private Instant creationDate;

    @ManyToOne
    private VaccinationCenter vaccinationCenter;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaccinationSlot id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getDate() {
        return this.date;
    }

    public VaccinationSlot date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Boolean getAlreadyTaken() {
        return this.alreadyTaken;
    }

    public VaccinationSlot alreadyTaken(Boolean alreadyTaken) {
        this.alreadyTaken = alreadyTaken;
        return this;
    }

    public void setAlreadyTaken(Boolean alreadyTaken) {
        this.alreadyTaken = alreadyTaken;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public VaccinationSlot creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public VaccinationCenter getVaccinationCenter() {
        return this.vaccinationCenter;
    }

    public VaccinationSlot vaccinationCenter(VaccinationCenter vaccinationCenter) {
        this.setVaccinationCenter(vaccinationCenter);
        return this;
    }

    public void setVaccinationCenter(VaccinationCenter vaccinationCenter) {
        this.vaccinationCenter = vaccinationCenter;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VaccinationSlot)) {
            return false;
        }
        return id != null && id.equals(((VaccinationSlot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccinationSlot{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", alreadyTaken='" + getAlreadyTaken() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
