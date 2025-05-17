package com.immi.system.models;

import com.immi.system.enums.ApplicationEventTypeEnum;
import com.immi.system.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "uscis_petition_event")
public class UscisPetitionEventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "petition_id", nullable = false)
    private PetitionModel petition;

    @NotNull(message = "Event type is required.")
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 100)
    private ApplicationEventTypeEnum eventType; // Enum: USCIS_PAYMENT, NVC_PAYMENT, INTERVIEW_SCHEDULED, etc.

    @NotNull(message = "Event date is required.")
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private StatusEnum status;

    @Lob
    @Column(name = "notes", columnDefinition = "MEDIUMTEXT")
    private String notes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public UscisPetitionEventModel() {
    }

    public UscisPetitionEventModel(Long id, PetitionModel petition, ApplicationEventTypeEnum eventType, LocalDateTime eventDate, String location, BigDecimal amount, StatusEnum status, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.petition = petition;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.location = location;
        this.amount = amount;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PetitionModel getPetition() {
        return petition;
    }

    public void setPetition(PetitionModel petition) {
        this.petition = petition;
    }

    public ApplicationEventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(ApplicationEventTypeEnum eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "UscisPetitionEventModel{" + "id=" + id + ", petition=" + petition + ", eventType=" + eventType + ", eventDate=" + eventDate + ", location=" + location + ", amount=" + amount + ", status=" + status + ", notes=" + notes + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
    
}
