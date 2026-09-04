package com.society.model.Secretary_model;

/**
 * ============================================================
 * DashboardData
 * ============================================================
 *
 * Model class used by the Secretary Dashboard.
 *
 * Stores:
 *
 * 1. Total Residents
 * 2. Total Owners
 * 3. Total Guards
 * 4. Open Complaints
 * 5. Maintenance Collection
 * 6. Logged-in Secretary Email
 * 7. Secretary Society
 *
 * Email and society are NOT hardcoded.
 * They are populated dynamically from the logged-in secretary.
 */
public class DashboardData {

    // ============================================================
    // FIELDS
    // ============================================================

    private int totalResidents;
    private int totalOwners;
    private int totalGuards;
    private int openComplaints;

    private double maintenanceCollection;

    private String email;
    private String society;


    // ============================================================
    // DEFAULT CONSTRUCTOR
    // ============================================================

    public DashboardData() {
    }


    // ============================================================
    // PARAMETERIZED CONSTRUCTOR
    // ============================================================
    /*
     * Existing constructor.
     *
     * Kept for compatibility with existing code.
     */

    public DashboardData(
            int totalResidents,
            int totalOwners,
            int totalGuards,
            int openComplaints,
            double maintenanceCollection) {

        this.totalResidents = totalResidents;
        this.totalOwners = totalOwners;
        this.totalGuards = totalGuards;
        this.openComplaints = openComplaints;
        this.maintenanceCollection = maintenanceCollection;
    }


    // ============================================================
    // FULL CONSTRUCTOR
    // ============================================================
    /*
     * Used when DashboardDao also provides:
     *
     * - logged-in secretary email
     * - secretary society
     */

    public DashboardData(
            int totalResidents,
            int totalOwners,
            int totalGuards,
            int openComplaints,
            double maintenanceCollection,
            String email,
            String society) {

        this.totalResidents = totalResidents;
        this.totalOwners = totalOwners;
        this.totalGuards = totalGuards;
        this.openComplaints = openComplaints;
        this.maintenanceCollection = maintenanceCollection;
        this.email = email;
        this.society = society;
    }


    // ============================================================
    // TOTAL RESIDENTS
    // ============================================================

    public int getTotalResidents() {
        return totalResidents;
    }

    public void setTotalResidents(int totalResidents) {
        this.totalResidents = totalResidents;
    }


    // ============================================================
    // TOTAL OWNERS
    // ============================================================

    public int getTotalOwners() {
        return totalOwners;
    }

    public void setTotalOwners(int totalOwners) {
        this.totalOwners = totalOwners;
    }


    // ============================================================
    // TOTAL GUARDS
    // ============================================================

    public int getTotalGuards() {
        return totalGuards;
    }

    public void setTotalGuards(int totalGuards) {
        this.totalGuards = totalGuards;
    }


    // ============================================================
    // OPEN COMPLAINTS
    // ============================================================

    public int getOpenComplaints() {
        return openComplaints;
    }

    public void setOpenComplaints(int openComplaints) {
        this.openComplaints = openComplaints;
    }


    // ============================================================
    // MAINTENANCE COLLECTION
    // ============================================================

    public double getMaintenanceCollection() {
        return maintenanceCollection;
    }

    public void setMaintenanceCollection(
            double maintenanceCollection) {

        this.maintenanceCollection = maintenanceCollection;
    }


    // ============================================================
    // EMAIL
    // ============================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // ============================================================
    // SOCIETY
    // ============================================================

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }


    // ============================================================
    // TO STRING
    // ============================================================

    @Override
    public String toString() {

        return "DashboardData{" +
                "totalResidents=" + totalResidents +
                ", totalOwners=" + totalOwners +
                ", totalGuards=" + totalGuards +
                ", openComplaints=" + openComplaints +
                ", maintenanceCollection=" +
                maintenanceCollection +
                ", email='" + email + '\'' +
                ", society='" + society + '\'' +
                '}';
    }
}