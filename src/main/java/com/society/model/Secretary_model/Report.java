package com.society.model.Secretary_model;

/**
 * Report Model
 *
 * Used by the Secretary Portal for:
 *
 * 1. Normal reports
 * 2. Visitor reports
 * 3. Security reports
 * 4. Parking reports
 * 5. Incident reports
 * 6. Other reports
 *
 * Visitor-specific information is stored inside the
 * "details" field because the existing Firestore Reports
 * structure uses this common Report model.
 */
public class Report {

    // =========================================================
    // FIELDS
    // =========================================================

    private String id;

    private String email;

    private String societyId;

    private String societyName;

    private String source;

    private String type;

    private String title;

    private String details;

    private String submittedBy;

    private String date;

    private String status;

    private long timestamp;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    /**
     * Required by Firestore for document deserialization.
     */
    public Report() {
    }

    // =========================================================
    // FULL CONSTRUCTOR
    // =========================================================

    public Report(
            String id,
            String email,
            String societyId,
            String societyName,
            String source,
            String type,
            String title,
            String details,
            String submittedBy,
            String date,
            String status,
            long timestamp) {

        this.id = id;
        this.email = email;
        this.societyId = societyId;
        this.societyName = societyName;
        this.source = source;
        this.type = type;
        this.title = title;
        this.details = details;
        this.submittedBy = submittedBy;
        this.date = date;
        this.status = status;
        this.timestamp = timestamp;
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
    // SOCIETY ID
    // =========================================================

    public String getSocietyId() {
        return societyId;
    }

    public void setSocietyId(String societyId) {
        this.societyId = societyId;
    }

    // =========================================================
    // SOCIETY NAME
    // =========================================================

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }

    // =========================================================
    // SOURCE
    // =========================================================

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    // =========================================================
    // TYPE
    // =========================================================

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    // DETAILS
    // =========================================================

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // =========================================================
    // SUBMITTED BY
    // =========================================================

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
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
    // STATUS
    // =========================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // =========================================================
    // TIMESTAMP
    // =========================================================

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // =========================================================
    // TOSTRING
    // =========================================================

    @Override
    public String toString() {

        return "Report{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", societyId='" + societyId + '\'' +
                ", societyName='" + societyName + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", submittedBy='" + submittedBy + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}