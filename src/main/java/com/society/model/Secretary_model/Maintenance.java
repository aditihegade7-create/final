package com.society.model.Secretary_model;

public class Maintenance {

    // =========================================================
    // MAINTENANCE ID
    // =========================================================

    // Firestore record/document ID
    private String maintenanceId;

    // =========================================================
    // MAINTENANCE INFORMATION
    // =========================================================

    // Maintenance amount
    private String amount;

    // Maintenance month
    private String month;

    // Maintenance date
    private String date;

    // Maintenance status
    // Example: Pending, Paid, Overdue
    private String status;

    // =========================================================
    // SECRETARY INFORMATION
    // =========================================================

    // Maintenance add करणाऱ्या Secretary चा login email
    private String addedBySecretaryEmail;

    // Maintenance add करणाऱ्या Secretary ची society
    private String society;

    // =========================================================
    // CONSTRUCTOR USED BY EXISTING CONTROLLER
    // =========================================================
    //
    // Existing MaintenanceController मध्ये हा constructor
    // 9 parameters सह call केला जात आहे.
    //
    // First 3 parameters currently empty आहेत.
    // Actual maintenance information पुढील parameters मध्ये आहे.
    //
    // =========================================================

    public Maintenance(
            String string,
            String string2,
            String string3,
            String string4,
            String string5,
            String string6,
            String string7,
            String secretaryEmail,
            String society2) {

        this.amount = string4;
        this.month = string5;
        this.date = string6;
        this.status = string7;
        this.addedBySecretaryEmail = secretaryEmail;
        this.society = society2;
    }

    // =========================================================
    // BASIC CONSTRUCTOR
    // =========================================================
    //
    // Existing code मध्ये amount, month, date, status
    // directly create करण्यासाठी.
    //
    // =========================================================

    public Maintenance(
            String amount,
            String month,
            String date,
            String status) {

        this.amount = amount;
        this.month = month;
        this.date = date;
        this.status = status;
    }

    // =========================================================
    // COMPLETE CONSTRUCTOR
    // =========================================================
    //
    // Secretary email + society सहित पूर्ण maintenance.
    //
    // =========================================================

    public Maintenance(
            String amount,
            String month,
            String date,
            String status,
            String addedBySecretaryEmail,
            String society) {

        this.amount = amount;
        this.month = month;
        this.date = date;
        this.status = status;
        this.addedBySecretaryEmail = addedBySecretaryEmail;
        this.society = society;
    }

    // =========================================================
    // FULL CONSTRUCTOR WITH ID
    // =========================================================
    //
    // Firestore मधून existing record fetch केल्यावर
    // maintenanceId सहित object तयार करण्यासाठी.
    //
    // =========================================================

    public Maintenance(
            String maintenanceId,
            String amount,
            String month,
            String date,
            String status,
            String addedBySecretaryEmail,
            String society) {

        this.maintenanceId = maintenanceId;
        this.amount = amount;
        this.month = month;
        this.date = date;
        this.status = status;
        this.addedBySecretaryEmail = addedBySecretaryEmail;
        this.society = society;
    }

    // =========================================================
    // MAINTENANCE ID
    // =========================================================

    public String getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    // =========================================================
    // AMOUNT
    // =========================================================

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    // =========================================================
    // MONTH
    // =========================================================

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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
    // ADDED BY SECRETARY EMAIL
    // =========================================================

    public String getAddedBySecretaryEmail() {
        return addedBySecretaryEmail;
    }

    public void setAddedBySecretaryEmail(
            String addedBySecretaryEmail) {

        this.addedBySecretaryEmail =
                addedBySecretaryEmail;
    }

    // =========================================================
    // SOCIETY
    // =========================================================

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "Maintenance{" +
                "maintenanceId='" + maintenanceId + '\'' +
                ", amount='" + amount + '\'' +
                ", month='" + month + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", addedBySecretaryEmail='" +
                addedBySecretaryEmail + '\'' +
                ", society='" + society + '\'' +
                '}';
    }
}