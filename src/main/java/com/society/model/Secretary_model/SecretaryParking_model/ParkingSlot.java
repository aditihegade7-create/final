package com.society.model.Secretary_model.SecretaryParking_model;

public class ParkingSlot {

    // ============================================================
    // FIELDS
    // ============================================================

    private String slotNumber;
    private String status;

    // RESIDENT / VISITOR
    private String slotType;

    // ============================================================
    // CONSTRUCTORS
    // ============================================================

    /**
     * Existing constructor.
     *
     * Defaults slotType to RESIDENT.
     * त्यामुळे existing code मध्ये constructor error येणार नाही.
     */
    public ParkingSlot(String slotNumber, String status) {
        this.slotNumber = slotNumber;
        this.status = status;
        this.slotType = "RESIDENT";
    }

    /**
     * New constructor with slot type.
     */
    public ParkingSlot(
            String slotNumber,
            String status,
            String slotType) {

        this.slotNumber = slotNumber;
        this.status = status;
        this.slotType = normalizeSlotType(slotType);
    }

    // ============================================================
    // SLOT NUMBER
    // ============================================================

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    // ============================================================
    // STATUS
    // ============================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ============================================================
    // SLOT TYPE
    // ============================================================

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = normalizeSlotType(slotType);
    }

    // ============================================================
    // HELPER METHODS
    // ============================================================

    public boolean isVisitorSlot() {
        return "VISITOR".equalsIgnoreCase(slotType);
    }

    public boolean isResidentSlot() {
        return "RESIDENT".equalsIgnoreCase(slotType);
    }

    private String normalizeSlotType(String slotType) {

        if (slotType == null ||
                slotType.trim().isEmpty()) {

            return "RESIDENT";
        }

        if ("VISITOR".equalsIgnoreCase(
                slotType.trim())) {

            return "VISITOR";
        }

        return "RESIDENT";
    }

    // ============================================================
    // DEBUG
    // ============================================================

    @Override
    public String toString() {

        return "ParkingSlot{" +
                "slotNumber='" + slotNumber + '\'' +
                ", status='" + status + '\'' +
                ", slotType='" + slotType + '\'' +
                '}';
    }
}