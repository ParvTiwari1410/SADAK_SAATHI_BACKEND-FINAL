package com.sadaksaathi.API.report;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportRepository reportRepository;

    public ReportController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 1️⃣ GET ALL REPORTS (not used by user dashboard normally)
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportRepository.findAll());
    }

    // 2️⃣ GET REPORTS OF A SPECIFIC USER (IMPORTANT)
    // Example:
    // GET /api/reports/user?email=parv@gmail.com
    @GetMapping("/user")
    public ResponseEntity<List<Report>> getReportsByEmail(@RequestParam String email) {
        List<Report> userReports = reportRepository.findByUserEmail(email);
        return ResponseEntity.ok(userReports);
    }

    // 3️⃣ GET SINGLE REPORT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        return reportRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4️⃣ CREATE REPORT
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody CreateReportRequest reportRequest) {

        Report newReport = new Report();

        // Save the user email — NEW
        newReport.setUserEmail(reportRequest.getUserEmail());

        newReport.setTitle(reportRequest.getTitle());
        newReport.setLocation(reportRequest.getLocation());
        newReport.setStatus(reportRequest.getStatus());
        newReport.setSeverity(reportRequest.getSeverity());
        newReport.setDescription(reportRequest.getDescription());
        newReport.setPhotos(reportRequest.getPhotos());

        // Set coordinates if provided
        if (reportRequest.getLatitude() != null && reportRequest.getLongitude() != null) {
            newReport.setLatitude(reportRequest.getLatitude());
            newReport.setLongitude(reportRequest.getLongitude());
        }

        newReport.setSubmittedAt(LocalDateTime.now());

        Report savedReport = reportRepository.save(newReport);
        return ResponseEntity.ok(savedReport);
    }

    // 5️⃣ UPDATE STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<Report> updateReportStatus(
            @PathVariable Long id,
            @RequestBody String newStatus) {

        return reportRepository.findById(id)
                .map(report -> {
                    report.setStatus(newStatus);
                    Report updatedReport = reportRepository.save(report);
                    return ResponseEntity.ok(updatedReport);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 6️⃣ GET NEARBY HAZARDS
    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyHazardResponse>> getNearbyReports(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "2") Double radius) {

        try {
            List<Report> nearbyReports = reportRepository.findReportsWithinRadius(lat, lng, radius);

            List<NearbyHazardResponse> responses = nearbyReports.stream()
                    .map(report -> {
                        NearbyHazardResponse response = new NearbyHazardResponse();
                        response.setId(report.getId());
                        response.setTitle(report.getTitle());
                        response.setSeverity(report.getSeverity());
                        response.setDescription(report.getDescription());
                        response.setLatitude(report.getLatitude());
                        response.setLongitude(report.getLongitude());
                        response.setLocation(report.getLocation());
                        response.setStatus(report.getStatus());
                        response.setSubmittedAt(report.getSubmittedAt());

                        double distance = calculateDistance(lat, lng, report.getLatitude(), report.getLongitude());
                        response.setDistance(String.format("%.1f km", distance));

                        return response;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Distance helper
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    // DTO for nearby hazards
    public static class NearbyHazardResponse {
        private Long id;
        private String title;
        private String severity;
        private String description;
        private Double latitude;
        private Double longitude;
        private String location;
        private String status;
        private String distance;
        private LocalDateTime submittedAt;

        // getters + setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }

        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getDistance() { return distance; }
        public void setDistance(String distance) { this.distance = distance; }

        public LocalDateTime getSubmittedAt() { return submittedAt; }
        public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    }
}
