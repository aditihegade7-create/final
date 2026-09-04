package com.society.model.Resident_model;

import java.util.Date;

public class ComplaintModel {

    // =========================================================
    // FIELDS
    // =========================================================

    private String id;
    private String email;
    private String flatNumber;
    private String category;
    private String title;
    private String description;
    private String imageFileName;
    private String preferredDate;
    private String status;
    private Date createdAt;

    // =========================================================
    // EMPTY CONSTRUCTOR
    // Required by Firestore
    // =========================================================

    public ComplaintModel() {
    }

    // =========================================================
    // PARAMETERIZED CONSTRUCTOR
    // =========================================================

    public ComplaintModel(
            String id,
            String email,
            String flatNumber,
            String category,
            String title,
            String description,
            String imageFileName,
            String preferredDate,
            String status,
            Date createdAt) {

        this.id = id;
        this.email = email;
        this.flatNumber = flatNumber;
        this.category = category;
        this.title = title;
        this.description = description;
        this.imageFileName = imageFileName;
        this.preferredDate = preferredDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    // =========================================================
    // ID
    // =========================================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // =========================================================
    // EMAIL
    // =========================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // =========================================================
    // FLAT NUMBER
    // =========================================================

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    // =========================================================
    // CATEGORY
    // =========================================================

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // =========================================================
    // TITLE
    // =========================================================

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // =========================================================
    // DESCRIPTION
    // =========================================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // =========================================================
    // IMAGE FILE NAME
    // =========================================================

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    // =========================================================
    // PREFERRED DATE
    // =========================================================

    public String getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(String preferredDate) {
        this.preferredDate = preferredDate;
    }

    // =========================================================
    // STATUS
    // =========================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // =========================================================
    // CREATED AT
    // =========================================================

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
