package com.society.model.Guard_model;

public class ParkingModel {

    private String parkingId;
    private String spotNumber;
    private boolean occupied;
    private String vehicleNumber;
    private String ownerName;
    private String vehicleType;
    private String email;
    private String society;

    // Required by Firestore
    public ParkingModel() {
    }

    // =====================================================
    // PARKING ID
    // =====================================================

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    // =====================================================
    // SPOT NUMBER
    // =====================================================

    public String getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }

    // =====================================================
    // OCCUPIED
    // =====================================================

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    // =====================================================
    // VEHICLE NUMBER
    // =====================================================

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    // =====================================================
    // OWNER NAME
    // =====================================================

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    // =====================================================
    // VEHICLE TYPE
    // =====================================================

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    // =====================================================
    // EMAIL
    // =====================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // =====================================================
    // SOCIETY
    // =====================================================

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }
}