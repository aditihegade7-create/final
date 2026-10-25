
package com.society.model.Secretary_model;

public class Owner {

    // =========================================================
    // FIELDS
    // =========================================================

    private String id;
    private String name;
    private String flat;
    private String phone;
    private String email;
    private String status;
    private String society;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // Required by Firestore
    // =========================================================

    public Owner() {
    }

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Owner(
            String name,
            String flat,
            String phone,
            String email,
            String status,
            String society) {

        this.name = name;
        this.flat = flat;
        this.phone = phone;
        this.email = email;
        this.status = status;
        this.society = society;
    }

    // =========================================================
    // GET ID
    // =========================================================

    public String getId() {
        return id;
    }

    // =========================================================
    // SET ID
    // =========================================================

    public void setId(String id) {
        this.id = id;
    }

    // =========================================================
    // GET NAME
    // =========================================================

    public String getName() {
        return name;
    }

    // =========================================================
    // SET NAME
    // =========================================================

    public void setName(String name) {
        this.name = name;
    }

    // =========================================================
    // GET FLAT
    // =========================================================

    public String getFlat() {
        return flat;
    }

    // =========================================================
    // SET FLAT
    // =========================================================

    public void setFlat(String flat) {
        this.flat = flat;
    }

    // =========================================================
    // GET PHONE
    // Firestore field: phone
    // =========================================================

    public String getPhone() {
        return phone;
    }

    // =========================================================
    // SET PHONE
    // Firestore field: phone
    // =========================================================

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // =========================================================
    // GET EMAIL
    // =========================================================

    public String getEmail() {
        return email;
    }

    // =========================================================
    // SET EMAIL
    // =========================================================

    public void setEmail(String email) {
        this.email = email;
    }

    // =========================================================
    // GET STATUS
    // =========================================================

    public String getStatus() {
        return status;
    }

    // =========================================================
    // SET STATUS
    // =========================================================

    public void setStatus(String status) {
        this.status = status;
    }

    // =========================================================
    // GET SOCIETY
    // =========================================================

    public String getSociety() {
        return society;
    }

    // =========================================================
    // SET SOCIETY
    // =========================================================

    public void setSociety(String society) {
        this.society = society;
    }

    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "Owner{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", flat='" + flat + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", society='" + society + '\'' +
                '}';
    }
}

