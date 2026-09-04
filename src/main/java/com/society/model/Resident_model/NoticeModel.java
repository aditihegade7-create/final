package com.society.model.Resident_model;

public class NoticeModel {

    private String title;
    private String date;
    private String description;
    private String status;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public NoticeModel(
            String title,
            String date,
            String description,
            String status) {

        this.title = title;
        this.date = date;
        this.description = description;
        this.status = status;
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
    // DATE
    // =========================================================

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
    // STATUS
    // =========================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}