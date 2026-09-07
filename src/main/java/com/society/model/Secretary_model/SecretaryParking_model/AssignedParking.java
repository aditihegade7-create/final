package com.society.model.Secretary_model.SecretaryParking_model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AssignedParking {

    private final String email;

    private final StringProperty member;
    private final StringProperty flat;
    private final StringProperty role;
    private final StringProperty vehicle;
    private final StringProperty slot;
    private final StringProperty status;

    public AssignedParking(
            String email,
            String member,
            String flat,
            String role,
            String vehicle,
            String slot,
            String status) {

        this.email = email;

        this.member = new SimpleStringProperty(member);
        this.flat = new SimpleStringProperty(flat);
        this.role = new SimpleStringProperty(role);
        this.vehicle = new SimpleStringProperty(vehicle);
        this.slot = new SimpleStringProperty(slot);
        this.status = new SimpleStringProperty(status);
    }

    public String getEmail() {
        return email;
    }

    public String getMember() {
        return member.get();
    }

    public String getFlat() {
        return flat.get();
    }

    public String getRole() {
        return role.get();
    }

    public String getVehicle() {
        return vehicle.get();
    }

    public String getSlot() {
        return slot.get();
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty memberProperty() {
        return member;
    }

    public StringProperty flatProperty() {
        return flat;
    }

    public StringProperty roleProperty() {
        return role;
    }

    public StringProperty vehicleProperty() {
        return vehicle;
    }

    public StringProperty slotProperty() {
        return slot;
    }

    public StringProperty statusProperty() {
        return status;
    }
}
