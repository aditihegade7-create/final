package com.society.model.Secretary_model;

public class Notice {

    // =====================================================
    // FIELDS
    // =====================================================

    private String noticeId;
    private String title;
    private String description;
    private String date;
    private String status;
    private String senderEmail;
    private String society;

    // =====================================================
    // DEFAULT CONSTRUCTOR
    // REQUIRED BY FIRESTORE
    // =====================================================

    public Notice() {
    }

    // =====================================================
    // PARAMETERIZED CONSTRUCTOR - OLD
    // =====================================================
    // Kept for compatibility with existing code
    // =====================================================

    public Notice(
            String noticeId,
            String title,
            String description,
            String date,
            String status,
            String senderEmail) {

        this.noticeId = noticeId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
        this.senderEmail = senderEmail;
    }

    // =====================================================
    // PARAMETERIZED CONSTRUCTOR - NEW
    // =====================================================
    // Includes society
    // =====================================================

    public Notice(
            String noticeId,
            String title,
            String description,
            String date,
            String status,
            String senderEmail,
            String society) {

        this.noticeId = noticeId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
        this.senderEmail = senderEmail;
        this.society = society;
    }

    // =====================================================
    // GETTER - NOTICE ID
    // =====================================================

    public String getNoticeId() {
        return noticeId;
    }

    // =====================================================
    // SETTER - NOTICE ID
    // =====================================================

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    // =====================================================
    // GETTER - TITLE
    // =====================================================

    public String getTitle() {
        return title;
    }

    // =====================================================
    // SETTER - TITLE
    // =====================================================

    public void setTitle(String title) {
        this.title = title;
    }

    // =====================================================
    // GETTER - DESCRIPTION
    // =====================================================

    public String getDescription() {
        return description;
    }

    // =====================================================
    // SETTER - DESCRIPTION
    // =====================================================

    public void setDescription(String description) {
        this.description = description;
    }

    // =====================================================
    // GETTER - DATE
    // =====================================================

    public String getDate() {
        return date;
    }

    // =====================================================
    // SETTER - DATE
    // =====================================================

    public void setDate(String date) {
        this.date = date;
    }

    // =====================================================
    // GETTER - STATUS
    // =====================================================

    public String getStatus() {
        return status;
    }

    // =====================================================
    // SETTER - STATUS
    // =====================================================

    public void setStatus(String status) {
        this.status = status;
    }

    // =====================================================
    // GETTER - SENDER EMAIL
    // =====================================================

    public String getSenderEmail() {
        return senderEmail;
    }

    // =====================================================
    // SETTER - SENDER EMAIL
    // =====================================================

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    // =====================================================
    // GETTER - SOCIETY
    // =====================================================

    public String getSociety() {
        return society;
    }

    // =====================================================
    // SETTER - SOCIETY
    // =====================================================

    public void setSociety(String society) {
        this.society = society;
    }

    // =====================================================
    // TO STRING
    // =====================================================

    @Override
    public String toString() {

        return "Notice{" +
                "noticeId='" + noticeId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", senderEmail='" + senderEmail + '\'' +
                ", society='" + society + '\'' +
                '}';
    }
}