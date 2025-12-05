package com.sadaksaathi.API.report;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String location;
    private String status;
    private String severity;
    private String description;
    private String userEmail;

    // NEW: Add precise coordinates for map functionality
    private Double latitude;
    private Double longitude;

    @ElementCollection
    private List<String> photos;

    private LocalDateTime submittedAt = LocalDateTime.now();

    // UPDATED ENUM — matches your frontend EXACTLY
    public enum ReportStatus {
        SUBMITTED, 
        VERIFIED, 
        REGISTERED, 
        FIXED
    }

    public enum SeverityLevel {
        CRITICAL, MODERATE, MINOR
    }

    public enum ReportType {
        POTHOLE, WATERLOGGING, STREETLIGHT, ROAD_DAMAGE, OTHER
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}
