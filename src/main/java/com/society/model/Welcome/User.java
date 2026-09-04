package com.society.model.Welcome;

public class User {

    // =========================================================
    // FIELDS
    // =========================================================

    private String name;
    private String email;
    private String phone;
    private String dob;
    private String gender;

    private String flatNo;
    private String aadhar;
    private String society;
    private String ownerName;
    private String address;
    private String joiningDate;

    private String role;
    private String status;
    private String memberSince;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // REQUIRED BY FIRESTORE
    // =========================================================

    public User() {
    }

    // =========================================================
    // BASIC CONSTRUCTOR
    // =========================================================

    public User(
            String name,
            String email,
            String phone,
            String role) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    // =========================================================
    // COMPLETE CONSTRUCTOR
    // =========================================================

    public User(
            String name,
            String email,
            String phone,
            String dob,
            String gender,
            String flatNo,
            String aadhar,
            String society,
            String ownerName,
            String address,
            String joiningDate,
            String role,
            String status,
            String memberSince) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
        this.flatNo = flatNo;
        this.aadhar = aadhar;
        this.society = society;
        this.ownerName = ownerName;
        this.address = address;
        this.joiningDate = joiningDate;
        this.role = role;
        this.status = status;
        this.memberSince = memberSince;
    }

    // =========================================================
    // NAME
    // =========================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    // PHONE
    // =========================================================

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // =========================================================
    // DOB
    // =========================================================

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    // =========================================================
    // GENDER
    // =========================================================

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // =========================================================
    // FLAT NO
    // =========================================================

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    // =========================================================
    // AADHAR
    // =========================================================

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
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
    // OWNER NAME
    // =========================================================

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    // =========================================================
    // ADDRESS
    // =========================================================

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // =========================================================
    // JOINING DATE
    // =========================================================

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    // =========================================================
    // ROLE
    // =========================================================

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
    // MEMBER SINCE
    // =========================================================

    public String getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }

    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", flatNo='" + flatNo + '\'' +
                ", aadhar='" + aadhar + '\'' +
                ", society='" + society + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", address='" + address + '\'' +
                ", joiningDate='" + joiningDate + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", memberSince='" + memberSince + '\'' +
                '}';
    }
}