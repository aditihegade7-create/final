package com.society.model.Secretary_model;

public class Event {

    private String eventName;
    private String date;
    private String time;
    private String venue;
    private String status;

    // Logged-in secretary information
    private String email;
    private String society;

    // Created time
    private long timestamp;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public Event() {
    }

    // =========================================================
    // CONSTRUCTOR USED BY UI
    // =========================================================

    public Event(String eventName,
                 String date,
                 String time,
                 String venue,
                 String status) {

        this.eventName = eventName;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.status = status;
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public String getEventName() {
        return eventName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getVenue() {
        return venue;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public String getSociety() {
        return society;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // =========================================================
    // SETTERS
    // =========================================================

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}