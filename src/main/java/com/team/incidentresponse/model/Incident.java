package com.team.incidentresponse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Incident type is required")
    @Size(max = 100, message = "Type must be less than 100 characters")
    private String type;

    // 'user' is a reserved word in some databases (H2). Map to 'username' column name to avoid DDL errors.
    @Column(name = "username", nullable = false)
    @NotBlank(message = "User is required")
    @Size(max = 50, message = "User must be less than 50 characters")
    private String user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(nullable = false)
    private Integer score;

    // Added fields for scoring engine
    private int threatLevel;
    private int assetValue;
    private double detectionConfidence;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(length = 1000)
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    public Incident() {
        this.createdAt = LocalDateTime.now();
        this.status = Status.ACTIVE;
    }

    public Incident(String type, String user, Severity severity, Integer score) {
        this();
        this.type = type;
        this.user = user;
        this.severity = severity;
        this.score = score;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public int getThreatLevel() {
        return threatLevel;
    }

    public void setThreatLevel(int threatLevel) {
        this.threatLevel = threatLevel;
    }

    public int getAssetValue() {
        return assetValue;
    }

    public void setAssetValue(int assetValue) {
        this.assetValue = assetValue;
    }

    public double getDetectionConfidence() {
        return detectionConfidence;
    }

    public void setDetectionConfidence(double detectionConfidence) {
        this.detectionConfidence = detectionConfidence;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incident incident = (Incident) o;
        return Objects.equals(id, incident.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Incident{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", user='" + user + '\'' +
                ", severity=" + severity +
                ", score=" + score +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum Status {
        ACTIVE, RESOLVED, DISMISSED
    }
}
