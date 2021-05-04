package com.wandev.vaccineslotfinder.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VaccinationCenter.
 */
@Entity
@Table(name = "vaccination_center")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VaccinationCenter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "api_url", nullable = false)
    private String apiUrl;

    @Column(name = "reservation_url")
    private String reservationUrl;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaccinationCenter id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public VaccinationCenter name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public VaccinationCenter address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApiUrl() {
        return this.apiUrl;
    }

    public VaccinationCenter apiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getReservationUrl() {
        return this.reservationUrl;
    }

    public VaccinationCenter reservationUrl(String reservationUrl) {
        this.reservationUrl = reservationUrl;
        return this;
    }

    public void setReservationUrl(String reservationUrl) {
        this.reservationUrl = reservationUrl;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public VaccinationCenter enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VaccinationCenter)) {
            return false;
        }
        return id != null && id.equals(((VaccinationCenter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccinationCenter{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", apiUrl='" + getApiUrl() + "'" +
            ", reservationUrl='" + getReservationUrl() + "'" +
            ", enabled='" + getEnabled() + "'" +
            "}";
    }
}
