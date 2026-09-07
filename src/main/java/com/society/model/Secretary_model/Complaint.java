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
    private String society;

    // ============================================================
    // DEFAULT CONSTRUCTOR
    // ============================================================

    public Complaint() {
    }

    // ============================================================
    // FULL CONSTRUCTOR
    // ============================================================

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

    // ============================================================
    // ID
    // ============================================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // ============================================================
    // EMAIL
    // ============================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ============================================================
    // TITLE
    // ============================================================

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // ============================================================
    // CATEGORY
    // ============================================================

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // ============================================================
    // DESCRIPTION
    // ============================================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // ============================================================
    // FLAT NUMBER
    // ============================================================

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    // ============================================================
    // PREFERRED DATE
    // ============================================================

    public String getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(String preferredDate) {
        this.preferredDate = preferredDate;
    }

    // ============================================================
    // STATUS
    // ============================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ============================================================
    // IMAGE FILE NAME
    // ============================================================

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    // ============================================================
    // CREATED AT
    // ============================================================

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // ============================================================
    // SOCIETY
    // ============================================================

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    // ============================================================
    // TO STRING
    // ============================================================

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