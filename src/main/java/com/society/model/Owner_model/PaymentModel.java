package com.society.model.Owner_model;

public class PaymentModel {

    // =========================================================
    // FIELDS
    // =========================================================

    private String paymentId;
    private String ownerEmail;
    private String tenantEmail;

    private String date;
    private String tenant;
    private String flat;
    private String paymentFor;
    private String amount;
    private String status;


    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public PaymentModel() {
    }


    // =========================================================
    // PARAMETERIZED CONSTRUCTOR
    // =========================================================

    public PaymentModel(
            String paymentId,
            String ownerEmail,
            String tenantEmail,
            String date,
            String tenant,
            String flat,
            String paymentFor,
            String amount,
            String status) {

        this.paymentId = paymentId;
        this.ownerEmail = ownerEmail;
        this.tenantEmail = tenantEmail;
        this.date = date;
        this.tenant = tenant;
        this.flat = flat;
        this.paymentFor = paymentFor;
        this.amount = amount;
        this.status = status;
    }


    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }


    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }


    public String getTenantEmail() {
        return tenantEmail;
    }

    public void setTenantEmail(String tenantEmail) {
        this.tenantEmail = tenantEmail;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }


    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }


    public String getPaymentFor() {
        return paymentFor;
    }

    public void setPaymentFor(String paymentFor) {
        this.paymentFor = paymentFor;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}