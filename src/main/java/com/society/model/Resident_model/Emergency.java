package com.society.model.Resident_model;

import java.util.Date;

public class Emergency {

    private String emergencyId;
    private String email;
    private String society;
    private String societyName;
    private String type;
    private String location;
    private String details;
    private String status;
    private String time;

    private String sender1;
    private String sender2;

    private Date createdAt;

    public Emergency() {
    }

    public Emergency(
            String emergencyId,
            String email,
            String society,
            String societyName,
            String type,
            String location,
            String details,
            String status,
            String time,
            String sender1,
            String sender2,
            Date createdAt) {

        this.emergencyId = emergencyId;
        this.email = email;
        this.society = society;
        this.societyName = societyName;
        this.type = type;
        this.location = location;
        this.details = details;
        this.status = status;
        this.time = time;
        this.sender1 = sender1;
        this.sender2 = sender2;
        this.createdAt = createdAt;
    }

    public String getEmergencyId() {
        return emergencyId;
    }

    public void setEmergencyId(String emergencyId) {
        this.emergencyId = emergencyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender1() {
        return sender1;
    }

    public void setSender1(String sender1) {
        this.sender1 = sender1;
    }

    public String getSender2() {
        return sender2;
    }

    public void setSender2(String sender2) {
        this.sender2 = sender2;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}