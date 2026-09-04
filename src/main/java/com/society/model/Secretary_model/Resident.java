package com.society.model.Secretary_model;

public class Resident {

    // =========================================================
    // FIELDS
    // =========================================================

    private String name;
    private String email;

    // Firestore field: phone
    private String phone;

    // Firestore field: flatNo
    private String flatNo;

    private String status;

    // Complete Resident Details
    private String aadhar;
    private String address;
    private String dob;
    private String gender;
    private String joiningDate;
    private String memberSince;
    private String ownerName;
    private String society;
    private String role;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public Resident() {
    }

    // =========================================================
    // UI CONSTRUCTOR
    // =========================================================

    public Resident(
            String name,
            String flat,
            String mobile,
            String email,
            String status) {

        this.name = name;
        this.flatNo = flat;
        this.phone = mobile;
        this.email = email;
        this.status = status;
    }

    // =========================================================
    // FULL CONSTRUCTOR
    // =========================================================

    public Resident(
            String name,
            String email,
            String phone,
            String flatNo,
            String status,
            String aadhar,
            String address,
            String dob,
            String gender,
            String joiningDate,
            String memberSince,
            String ownerName,
            String society,
            String role) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.flatNo = flatNo;
        this.status = status;
        this.aadhar = aadhar;
        this.address = address;
        this.dob = dob;
        this.gender = gender;
        this.joiningDate = joiningDate;
        this.memberSince = memberSince;
        this.ownerName = ownerName;
        this.society = society;
        this.role = role;
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
    // MOBILE - COMPATIBILITY WITH EXISTING UI
    // =========================================================

    public String getMobile() {
        return phone;
    }

    public void setMobile(String mobile) {
        this.phone = mobile;
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
    // FLAT - COMPATIBILITY WITH EXISTING UI
    // =========================================================

    public String getFlat() {
        return flatNo;
    }

    public void setFlat(String flat) {
        this.flatNo = flat;
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
    // AADHAR
    // =========================================================

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
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
    // JOINING DATE
    // =========================================================

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
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
    // OWNER NAME
    // =========================================================

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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
    // ROLE
    // =========================================================

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "Resident{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", flatNo='" + flatNo + '\'' +
                ", status='" + status + '\'' +
                ", aadhar='" + aadhar + '\'' +
                ", address='" + address + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", joiningDate='" + joiningDate + '\'' +
                ", memberSince='" + memberSince + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", society='" + society + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}