package com.society.model.Secretary_model;

import com.google.cloud.Timestamp;

public class Complaint {

    private String id;
    private String email;
    private String title;
    private String category;
    private String description;
    private String flatNumber;
    private String preferredDate;
    private String status;
    private String imageFileName;
    private Timestamp createdAt;

    // =====================================================
    // SOCIETY
    // =====================================================

    private String society;

    // =====================================================
    // DEFAULT CONSTRUCTOR
    // =====================================================

    public Complaint() {
    }

    // =====================================================
    // PARAMETERIZED CONSTRUCTOR
    // =====================================================

    public Complaint(
            String id,
            String email,
            String title,
            String category,
            String description,
            String flatNumber,
            String preferredDate,
            String status,
            String imageFileName,
            Timestamp createdAt,
            String society) {

        this.id = id;
        this.email = email;
        this.title = title;
        this.category = category;
        this.description = description;
        this.flatNumber = flatNumber;
        this.preferredDate = preferredDate;
        this.status = status;
        this.imageFileName = imageFileName;
        this.createdAt = createdAt;
        this.society = society;
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public String getPreferredDate() {
        return preferredDate;
    }

    public String getStatus() {
        return status;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getSociety() {
        return society;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public void setPreferredDate(String preferredDate) {
        this.preferredDate = preferredDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    // =====================================================
    // TOSTRING
    // =====================================================

    @Override
    public String toString() {

        return "Complaint{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", flatNumber='" + flatNumber + '\'' +
                ", preferredDate='" + preferredDate + '\'' +
                ", status='" + status + '\'' +
                ", imageFileName='" + imageFileName + '\'' +
                ", createdAt=" + createdAt +
                ", society='" + society + '\'' +
                '}';
    }
}