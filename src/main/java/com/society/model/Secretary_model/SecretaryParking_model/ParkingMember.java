package com.society.model.Secretary_model.SecretaryParking_model;

public class ParkingMember {

    private String email;
    private String name;
    private String flatNo;
    private String role;
    private String society;

    public ParkingMember(
            String email,
            String name,
            String flatNo,
            String role,
            String society) {

        this.email = email;
        this.name = name;
        this.flatNo = flatNo;
        this.role = role;
        this.society = society;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public String getRole() {
        return role;
    }

    public String getSociety() {
        return society;
    }
}