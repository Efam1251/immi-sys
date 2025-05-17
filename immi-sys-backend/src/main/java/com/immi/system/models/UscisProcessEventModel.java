package com.immi.system.models;

import com.immi.system.enums.StatusEnum;
import com.immi.system.enums.ApplicationEventTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "uscis_process_event")
public class UscisProcessEventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id; // Secuence Id.

    @NotNull(message = "Process is required.")
    private Long processId; // Process Id.

    @Column(name = "event_type", length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Event type is required.")
    private ApplicationEventTypeEnum eventType; // Use the enum for event types

    @Column(name = "event_date", nullable = false)
    @NotNull(message = "Event date is required.")
    private LocalDateTime eventDate;

    @Column(name = "location", length = 100, nullable = true)
    private String location;

    @Column(name = "status", length = 50, nullable = true)
    @Enumerated(EnumType.STRING)
    private StatusEnum status; // e.g., "Scheduled", "Completed"

    @Lob
    @Column(name = "notes", columnDefinition = "MEDIUMTEXT", nullable = true)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public UscisProcessEventModel() {
    }

    public UscisProcessEventModel(Long id, Long processId, ApplicationEventTypeEnum eventType, LocalDateTime eventDate, String location, StatusEnum status, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.processId = processId;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.location = location;
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

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
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
        return "UscisEvent{" +
                "id=" + id +
                ", processId=" + processId +
                ", eventType=" + (eventType != null ? eventType.name() : null) +
                ", eventDate=" + eventDate +
                ", location='" + location + '\'' +
                ", status=" + (status != null ? status.name() : null) +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}