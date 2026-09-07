package com.society.controller.Secretary_Controller;

import com.society.dao.Secretary_dao.SecretaryParkingDAO;
import com.society.model.Secretary_model.SecretaryParking_model.AssignedParking;
import com.society.model.Secretary_model.SecretaryParking_model.ParkingMember;
import com.society.model.Secretary_model.SecretaryParking_model.ParkingSlot;
import com.society.service.resident_service.SecretarySession;

import java.util.List;

public class SecretaryParkingController {

    // ============================================================
    // CONSTANTS
    // ============================================================

    /**
     * Maximum number of visitor parking slots allowed
     * for one society.
     */
    public static final int MAX_VISITOR_SLOTS = 4;

    /**
     * Parking slot types.
     */
    public static final String SLOT_TYPE_RESIDENT = "RESIDENT";
    public static final String SLOT_TYPE_VISITOR = "VISITOR";

    /**
     * Parking statuses.
     */
    public static final String STATUS_AVAILABLE = "Available";
    public static final String STATUS_ASSIGNED = "Assigned";
    public static final String STATUS_OCCUPIED = "Occupied";

    // ============================================================
    // DAO
    // ============================================================

    private final SecretaryParkingDAO dao;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public SecretaryParkingController() {
        dao = new SecretaryParkingDAO();

        System.out.println(
                "SecretaryParkingController initialized."
        );
    }

    // ============================================================
    // GET CURRENT SECRETARY EMAIL
    // ============================================================

    /**
     * Returns the currently logged-in secretary email.
     *
     * This is always taken from SecretarySession.
     */
    private String getCurrentSecretaryEmail() throws Exception {

        String sessionEmail =
                SecretarySession.getLoggedInEmail();

        if (sessionEmail == null ||
                sessionEmail.trim().isEmpty()) {

            throw new Exception(
                    "Secretary session expired. "
                            + "Please login again."
            );
        }

        return sessionEmail.trim().toLowerCase();
    }

    // ============================================================
    // GET SECRETARY SOCIETY
    //
    // Secretaries/{secretaryEmail}
    // ============================================================

    public String getSecretarySociety(
            String secretaryEmail) throws Exception {

        if (secretaryEmail == null ||
                secretaryEmail.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Secretary email cannot be empty."
            );
        }

        secretaryEmail =
                secretaryEmail.trim().toLowerCase();

        System.out.println(
                "Fetching society for secretary: "
                        + secretaryEmail
        );

        String societyName =
                dao.getSecretarySociety(secretaryEmail);

        if (societyName == null ||
                societyName.trim().isEmpty()) {

            throw new Exception(
                    "Society not found for secretary: "
                            + secretaryEmail
            );
        }

        societyName = societyName.trim();

        System.out.println(
                "Secretary Society = "
                        + societyName
        );

        return societyName;
    }

    // ============================================================
    // GET CURRENT SECRETARY SOCIETY
    //
    // Convenience method.
    // ============================================================

    public String getCurrentSecretarySociety()
            throws Exception {

        String secretaryEmail =
                getCurrentSecretaryEmail();

        return getSecretarySociety(
                secretaryEmail
        );
    }

    // ============================================================
    // GET SOCIETY MEMBERS
    //
    // Residents + Owners
    // ============================================================

    public List<ParkingMember> getSocietyMembers(
            String societyName) throws Exception {

        societyName =
                validateSocietyName(societyName);

        System.out.println(
                "Fetching members for society: "
                        + societyName
        );

        List<ParkingMember> members =
                dao.getSocietyMembers(societyName);

        if (members == null) {
            throw new Exception(
                    "Unable to fetch society members."
            );
        }

        System.out.println(
                "Members found = "
                        + members.size()
        );

        return members;
    }

    // ============================================================
    // GET PARKING SLOTS
    //
    // Returns:
    //
    // RESIDENT slots
    // VISITOR slots
    // ============================================================

    public List<ParkingSlot> getParkingSlots(
            String societyName) throws Exception {

        societyName =
                validateSocietyName(societyName);

        System.out.println(
                "Fetching parking slots for: "
                        + societyName
        );

        List<ParkingSlot> slots =
                dao.getParkingSlots(societyName);

        if (slots == null) {
            throw new Exception(
                    "Unable to fetch parking slots."
            );
        }

        System.out.println(
                "Parking slots found = "
                        + slots.size()
        );

        return slots;
    }

    // ============================================================
    // GET ASSIGNED PARKING
    //
    // Returns permanent Resident/Owner assignments.
    //
    // Visitor slots are NOT returned as permanent
    // member assignments.
    // ============================================================

    public List<AssignedParking> getAssignedParking(
            String societyName) throws Exception {

        societyName =
                validateSocietyName(societyName);

        System.out.println(
                "Fetching assigned parking for: "
                        + societyName
        );

        List<AssignedParking> assignments =
                dao.getAssignedParking(societyName);

        if (assignments == null) {
            throw new Exception(
                    "Unable to fetch assigned parking."
            );
        }

        System.out.println(
                "Assigned parking found = "
                        + assignments.size()
        );

        return assignments;
    }

    // ============================================================
    // ALLOCATE PARKING TO RESIDENT / OWNER
    //
    // Permanent allocation.
    //
    // Visitor slots cannot be permanently allocated.
    // ============================================================

    public void allocateParking(
            String secretaryEmail,
            String societyName,
            String slotNumber,
            ParkingMember member) throws Exception {

        System.out.println();
        System.out.println(
                "=========================================="
        );
        System.out.println(
                "ALLOCATE RESIDENT / OWNER PARKING"
        );
        System.out.println(
                "=========================================="
        );

        // ========================================================
        // ALWAYS USE LOGGED-IN SECRETARY
        // ========================================================

        String sessionEmail =
                getCurrentSecretaryEmail();

        secretaryEmail = sessionEmail;

        System.out.println(
                "Logged-in Secretary Email = "
                        + secretaryEmail
        );

        // ========================================================
        // VALIDATE SOCIETY
        // ========================================================

        societyName =
                validateSocietyName(societyName);

        // ========================================================
        // VALIDATE SLOT
        // ========================================================

        slotNumber =
                validateSlotNumber(slotNumber);

        // ========================================================
        // VISITOR SLOT PROTECTION
        // ========================================================

        if (isVisitorSlotNumber(slotNumber)) {

            throw new Exception(
                    "Visitor parking slot "
                            + slotNumber
                            + " cannot be permanently allocated "
                            + "to a Resident or Owner."
            );
        }

        // ========================================================
        // VALIDATE MEMBER
        // ========================================================

        if (member == null) {

            throw new IllegalArgumentException(
                    "Please select a Resident or Owner."
            );
        }

        if (member.getEmail() == null ||
                member.getEmail().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Selected member email cannot be empty."
            );
        }

        String memberEmail =
                member.getEmail()
                        .trim()
                        .toLowerCase();

        // ========================================================
        // DEBUG
        // ========================================================

        System.out.println(
                "Secretary Email = "
                        + secretaryEmail
        );

        System.out.println(
                "Society Name = "
                        + societyName
        );

        System.out.println(
                "Slot Number = "
                        + slotNumber
        );

        System.out.println(
                "Member Name = "
                        + safeValue(member.getName())
        );

        System.out.println(
                "Member Email = "
                        + memberEmail
        );

        System.out.println(
                "Flat No = "
                        + safeValue(member.getFlatNo())
        );

        System.out.println(
                "Role = "
                        + safeValue(member.getRole())
        );

        // ========================================================
        // CHECK DUPLICATE SLOT
        // ========================================================

        System.out.println(
                "Checking whether slot already exists..."
        );

        boolean slotExists =
                dao.slotExists(
                        societyName,
                        slotNumber
                );

        if (slotExists) {

            throw new Exception(
                    "Parking slot "
                            + slotNumber
                            + " already exists in "
                            + societyName
                            + "."
            );
        }

        System.out.println(
                "Slot is available."
        );

        // ========================================================
        // CHECK MEMBER ALREADY HAS PARKING
        // ========================================================

        System.out.println(
                "Checking member parking assignment..."
        );

        boolean alreadyAssigned =
                dao.memberAlreadyHasParking(
                        societyName,
                        memberEmail
                );

        if (alreadyAssigned) {

            String memberName =
                    safeValue(member.getName());

            if (memberName.isEmpty()) {
                memberName = memberEmail;
            }

            String flatNo =
                    safeValue(member.getFlatNo());

            if (flatNo.isEmpty()) {
                flatNo = "N/A";
            }

            throw new Exception(
                    memberName
                            + " ("
                            + flatNo
                            + ") already has a parking slot."
            );
        }

        System.out.println(
                "Member does not have parking."
        );

        // ========================================================
        // SAVE
        // ========================================================

        System.out.println();
        System.out.println(
                "Calling DAO saveParkingAllocation()..."
        );

        dao.saveParkingAllocation(
                secretaryEmail,
                societyName,
                slotNumber,
                member
        );

        // ========================================================
        // SUCCESS
        // ========================================================

        System.out.println();
        System.out.println(
                "=========================================="
        );
        System.out.println(
                "PARKING ALLOCATION COMPLETED"
        );
        System.out.println(
                "=========================================="
        );

        System.out.println(
                "Secretary Email : "
                        + secretaryEmail
        );

        System.out.println(
                "Society : "
                        + societyName
        );

        System.out.println(
                "Slot : "
                        + slotNumber
        );

        System.out.println(
                "Member : "
                        + safeValue(member.getName())
        );

        System.out.println(
                "Member Email : "
                        + memberEmail
        );

        System.out.println(
                "Status : "
                        + STATUS_ASSIGNED
        );

        System.out.println(
                "Slot Type : "
                        + SLOT_TYPE_RESIDENT
        );

        System.out.println(
                "=========================================="
        );
        System.out.println();
    }

    // ============================================================
    // CREATE VISITOR PARKING SLOT
    //
    // Example:
    //
    // V-01
    // V-02
    // V-03
    // V-04
    //
    // Initial status = Available
    // slotType = VISITOR
    //
    // Maximum = 4
    // ============================================================

    public void createVisitorParkingSlot(
            String secretaryEmail,
            String societyName,
            String slotNumber) throws Exception {

        System.out.println();
        System.out.println(
                "=========================================="
        );
        System.out.println(
                "CREATE VISITOR PARKING SLOT"
        );
        System.out.println(
                "=========================================="
        );

        // ========================================================
        // ALWAYS USE LOGGED-IN SECRETARY
        // ========================================================

        secretaryEmail =
                getCurrentSecretaryEmail();

        // ========================================================
        // SOCIETY
        // ========================================================

        societyName =
                validateSocietyName(societyName);

        // ========================================================
        // SLOT NUMBER
        // ========================================================

        slotNumber =
                validateSlotNumber(slotNumber);

        // ========================================================
        // VISITOR SLOT FORMAT
        // ========================================================

        validateVisitorSlotNumber(slotNumber);

        // ========================================================
        // ONLY V-01 TO V-04
        // ========================================================

        int visitorSlotNumber =
                extractVisitorSlotNumber(slotNumber);

        if (visitorSlotNumber < 1 ||
                visitorSlotNumber > MAX_VISITOR_SLOTS) {

            throw new IllegalArgumentException(
                    "Visitor parking slot must be between "
                            + "V-01 and V-04."
            );
        }

        // ========================================================
        // CHECK DUPLICATE SLOT
        // ========================================================

        if (dao.slotExists(
                societyName,
                slotNumber)) {

            throw new Exception(
                    "Parking slot "
                            + slotNumber
                            + " already exists in "
                            + societyName
                            + "."
            );
        }

        // ========================================================
        // CHECK MAXIMUM VISITOR SLOTS
        // ========================================================

        int visitorSlotCount =
                dao.countVisitorParkingSlots(
                        societyName
                );

        System.out.println(
                "Existing Visitor Slots = "
                        + visitorSlotCount
        );

        System.out.println(
                "Maximum Visitor Slots = "
                        + MAX_VISITOR_SLOTS
        );

        if (visitorSlotCount >=
                MAX_VISITOR_SLOTS) {

            throw new Exception(
                    "Maximum "
                            + MAX_VISITOR_SLOTS
                            + " visitor parking slots are already "
                            + "created for "
                            + societyName
                            + "."
            );
        }

        // ========================================================
        // SAVE
        // ========================================================

        dao.createVisitorParkingSlot(
                secretaryEmail,
                societyName,
                slotNumber
        );

        // ========================================================
        // SUCCESS
        // ========================================================

        System.out.println(
                "Visitor parking slot created successfully."
        );

        System.out.println(
                "Secretary : "
                        + secretaryEmail
        );

        System.out.println(
                "Visitor Slot : "
                        + slotNumber
        );

        System.out.println(
                "Society : "
                        + societyName
        );

        System.out.println(
                "Status : "
                        + STATUS_AVAILABLE
        );

        System.out.println(
                "Slot Type : "
                        + SLOT_TYPE_VISITOR
        );

        System.out.println(
                "=========================================="
        );
        System.out.println();
    }

    // ============================================================
    // CREATE VISITOR SLOT AUTOMATICALLY
    //
    // Automatically creates the next available:
    //
    // V-01
    // V-02
    // V-03
    // V-04
    // ============================================================

    public String createVisitorParkingSlot(
            String secretaryEmail,
            String societyName) throws Exception {

        societyName =
                validateSocietyName(societyName);

        secretaryEmail =
                getCurrentSecretaryEmail();

        int visitorSlotCount =
                dao.countVisitorParkingSlots(
                        societyName
                );

        if (visitorSlotCount >= MAX_VISITOR_SLOTS) {

            throw new Exception(
                    "Maximum "
                            + MAX_VISITOR_SLOTS
                            + " visitor parking slots are already "
                            + "created for "
                            + societyName
                            + "."
            );
        }

        String slotNumber =
                dao.getNextVisitorSlotNumber(
                        societyName
                );

        if (slotNumber == null ||
                slotNumber.trim().isEmpty()) {

            throw new Exception(
                    "No visitor parking slot is available."
            );
        }

        createVisitorParkingSlot(
                secretaryEmail,
                societyName,
                slotNumber
        );

        return slotNumber;
    }

    // ============================================================
    // GET VISITOR PARKING SLOTS
    //
    // Returns only VISITOR slots.
    // ============================================================

    public List<ParkingSlot> getVisitorParkingSlots(
            String societyName) throws Exception {

        societyName =
                validateSocietyName(societyName);

        List<ParkingSlot> slots =
                dao.getVisitorParkingSlots(
                        societyName
                );

        if (slots == null) {

            throw new Exception(
                    "Unable to fetch visitor parking slots."
            );
        }

        System.out.println(
                "Visitor parking slots found = "
                        + slots.size()
        );

        return slots;
    }

    // ============================================================
    // GET AVAILABLE VISITOR PARKING SLOTS
    // ============================================================

    public List<ParkingSlot> getAvailableVisitorParkingSlots(
            String societyName) throws Exception {

        societyName =
                validateSocietyName(societyName);

        List<ParkingSlot> slots =
                dao.getAvailableVisitorParkingSlots(
                        societyName
                );

        if (slots == null) {

            throw new Exception(
                    "Unable to fetch available visitor slots."
            );
        }

        System.out.println(
                "Available visitor slots = "
                        + slots.size()
        );

        return slots;
    }

    // ============================================================
    // ASSIGN VISITOR PARKING
    //
    // Temporary assignment.
    //
    // Visitor slot:
    // V-01
    //
    // status:
    // Occupied
    //
    // allocatedToEmail is NOT used.
    // ============================================================

    public void assignVisitorParking(
            String secretaryEmail,
            String societyName,
            String slotNumber,
            String visitorName,
            String vehicleNumber,
            ParkingMember visitingResident) throws Exception {

        System.out.println();
        System.out.println(
                "=========================================="
        );
        System.out.println(
                "ASSIGN VISITOR PARKING"
        );
        System.out.println(
                "=========================================="
        );

        // ========================================================
        // ALWAYS USE LOGGED-IN SECRETARY
        // ========================================================

        secretaryEmail =
                getCurrentSecretaryEmail();

        // ========================================================
        // SOCIETY
        // ========================================================

        societyName =
                validateSocietyName(societyName);

        // ========================================================
        // SLOT
        // ========================================================

        slotNumber =
                validateSlotNumber(slotNumber);

        validateVisitorSlotNumber(slotNumber);

        // ========================================================
        // VISITOR NAME
        // ========================================================

        if (visitorName == null ||
                visitorName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Visitor name cannot be empty."
            );
        }

        visitorName =
                visitorName.trim();

        // ========================================================
        // VEHICLE NUMBER
        // ========================================================

        if (vehicleNumber == null ||
                vehicleNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Visitor vehicle number cannot be empty."
            );
        }

        vehicleNumber =
                vehicleNumber.trim().toUpperCase();

        // ========================================================
        // VISITING RESIDENT
        // ========================================================

        if (visitingResident == null) {

            throw new IllegalArgumentException(
                    "Please select the Resident/Owner "
                            + "whom the visitor is visiting."
            );
        }

        if (visitingResident.getEmail() == null ||
                visitingResident.getEmail().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Visiting resident email cannot be empty."
            );
        }

        String residentEmail =
                visitingResident.getEmail()
                        .trim()
                        .toLowerCase();

        // ========================================================
        // DEBUG
        // ========================================================

        System.out.println(
                "Secretary Email : "
                        + secretaryEmail
        );

        System.out.println(
                "Society : "
                        + societyName
        );

        System.out.println(
                "Visitor Slot : "
                        + slotNumber
        );

        System.out.println(
                "Visitor Name : "
                        + visitorName
        );

        System.out.println(
                "Vehicle Number : "
                        + vehicleNumber
        );

        System.out.println(
                "Visiting Resident : "
                        + safeValue(
                        visitingResident.getName())
        );

        System.out.println(
                "Resident Email : "
                        + residentEmail
        );

        System.out.println(
                "Resident Flat : "
                        + safeValue(
                        visitingResident.getFlatNo())
        );

        // ========================================================
        // CHECK SLOT EXISTS
        // ========================================================

        if (!dao.slotExists(
                societyName,
                slotNumber)) {

            throw new Exception(
                    "Visitor parking slot "
                            + slotNumber
                            + " does not exist."
            );
        }

        // ========================================================
        // READ EXISTING SLOT
        // ========================================================

        ParkingSlot existingSlot =
                dao.getParkingSlot(
                        societyName,
                        slotNumber
                );

        if (existingSlot == null) {

            throw new Exception(
                    "Unable to read visitor parking slot "
                            + slotNumber
                            + "."
            );
        }

        // ========================================================
        // CHECK SLOT TYPE
        // ========================================================

        if (!SLOT_TYPE_VISITOR.equalsIgnoreCase(
                existingSlot.getSlotType())) {

            throw new Exception(
                    slotNumber
                            + " is not a Visitor Parking slot."
            );
        }

        // ========================================================
        // CHECK AVAILABLE
        // ========================================================

        if (!STATUS_AVAILABLE.equalsIgnoreCase(
                existingSlot.getStatus())) {

            throw new Exception(
                    "Visitor parking slot "
                            + slotNumber
                            + " is currently "
                            + safeValue(
                            existingSlot.getStatus())
                            + "."
            );
        }

        // ========================================================
        // SAVE TEMPORARY VISITOR ASSIGNMENT
        // ========================================================

        dao.assignVisitorParking(
                secretaryEmail,
                societyName,
                slotNumber,
                visitorName,
                vehicleNumber,
                visitingResident
        );

        // ========================================================
        // SUCCESS
        // ========================================================

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "VISITOR PARKING ASSIGNED SUCCESSFULLY"
        );

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "Secretary : "
                        + secretaryEmail
        );

        System.out.println(
                "Society : "
                        + societyName
        );

        System.out.println(
                "Slot : "
                        + slotNumber
        );

        System.out.println(
                "Visitor : "
                        + visitorName
        );

        System.out.println(
                "Vehicle : "
                        + vehicleNumber
        );

        System.out.println(
                "Visiting Resident : "
                        + safeValue(
                        visitingResident.getName())
        );

        System.out.println(
                "Resident Email : "
                        + residentEmail
        );

        System.out.println(
                "Status : "
                        + STATUS_OCCUPIED
        );

        System.out.println(
                "=========================================="
        );

        System.out.println();
    }

    // ============================================================
    // RELEASE VISITOR PARKING
    //
    // Called when visitor leaves.
    //
    // Occupied -> Available
    //
    // Visitor information is cleared.
    // ============================================================

    public void releaseVisitorParking(
            String secretaryEmail,
            String societyName,
            String slotNumber) throws Exception {

        System.out.println();
        System.out.println(
                "=========================================="
        );
        System.out.println(
                "RELEASE VISITOR PARKING"
        );
        System.out.println(
                "=========================================="
        );

        // ========================================================
        // ALWAYS USE LOGGED-IN SECRETARY
        // ========================================================

        secretaryEmail =
                getCurrentSecretaryEmail();

        // ========================================================
        // VALIDATE SOCIETY
        // ========================================================

        societyName =
                validateSocietyName(societyName);

        // ========================================================
        // VALIDATE SLOT
        // ========================================================

        slotNumber =
                validateSlotNumber(slotNumber);

        validateVisitorSlotNumber(slotNumber);

        // ========================================================
        // CHECK SLOT
        // ========================================================

        ParkingSlot slot =
                dao.getParkingSlot(
                        societyName,
                        slotNumber
                );

        if (slot == null) {

            throw new Exception(
                    "Visitor parking slot "
                            + slotNumber
                            + " was not found."
            );
        }

        // ========================================================
        // CHECK SLOT TYPE
        // ========================================================

        if (!SLOT_TYPE_VISITOR.equalsIgnoreCase(
                slot.getSlotType())) {

            throw new Exception(
                    slotNumber
                            + " is not a Visitor Parking slot."
            );
        }

        // ========================================================
        // CHECK CURRENT STATUS
        // ========================================================

        if (!STATUS_OCCUPIED.equalsIgnoreCase(
                slot.getStatus())) {

            throw new Exception(
                    "Visitor parking slot "
                            + slotNumber
                            + " is not currently occupied."
            );
        }

        // ========================================================
        // RELEASE
        // ========================================================

        dao.releaseVisitorParking(
                secretaryEmail,
                societyName,
                slotNumber
        );

        // ========================================================
        // SUCCESS
        // ========================================================

        System.out.println(
                "Visitor parking released successfully."
        );

        System.out.println(
                "Secretary : "
                        + secretaryEmail
        );

        System.out.println(
                "Society : "
                        + societyName
        );

        System.out.println(
                "Slot : "
                        + slotNumber
        );

        System.out.println(
                "Status : "
                        + STATUS_AVAILABLE
        );

        System.out.println(
                "Visitor information cleared."
        );

        System.out.println(
                "=========================================="
        );

        System.out.println();
    }

    // ============================================================
    // COUNT VISITOR PARKING SLOTS
    // ============================================================

    public int countVisitorParkingSlots(
            String societyName) throws Exception {

        societyName =
                validateSocietyName(societyName);

        return dao.countVisitorParkingSlots(
                societyName
        );
    }

    // ============================================================
    // CHECK WHETHER SOCIETY CAN CREATE MORE
    // VISITOR PARKING SLOTS
    // ============================================================

    public boolean canCreateVisitorParkingSlot(
            String societyName) throws Exception {

        int count =
                countVisitorParkingSlots(
                        societyName
                );

        return count < MAX_VISITOR_SLOTS;
    }

    // ============================================================
    // GET REMAINING VISITOR SLOT CAPACITY
    // ============================================================

    public int getRemainingVisitorSlotCapacity(
            String societyName) throws Exception {

        int count =
                countVisitorParkingSlots(
                        societyName
                );

        int remaining =
                MAX_VISITOR_SLOTS - count;

        return Math.max(
                remaining,
                0
        );
    }

    // ============================================================
    // CHECK SLOT EXISTS
    // ============================================================

    public boolean slotExists(
            String societyName,
            String slotNumber) throws Exception {

        if (societyName == null ||
                societyName.trim().isEmpty()) {

            return false;
        }

        if (slotNumber == null ||
                slotNumber.trim().isEmpty()) {

            return false;
        }

        return dao.slotExists(
                societyName.trim(),
                slotNumber.trim().toUpperCase()
        );
    }

    // ============================================================
    // CHECK MEMBER ALREADY HAS PARKING
    // ============================================================

    public boolean memberAlreadyHasParking(
            String societyName,
            String memberEmail) throws Exception {

        if (societyName == null ||
                societyName.trim().isEmpty()) {

            return false;
        }

        if (memberEmail == null ||
                memberEmail.trim().isEmpty()) {

            return false;
        }

        return dao.memberAlreadyHasParking(
                societyName.trim(),
                memberEmail.trim().toLowerCase()
        );
    }

    // ============================================================
    // GET SINGLE PARKING SLOT
    // ============================================================

    public ParkingSlot getParkingSlot(
            String societyName,
            String slotNumber) throws Exception {

        societyName =
                validateSocietyName(societyName);

        slotNumber =
                validateSlotNumber(slotNumber);

        return dao.getParkingSlot(
                societyName,
                slotNumber
        );
    }

    // ============================================================
    // GET VISITOR SLOT DETAILS
    // ============================================================

    public java.util.Map<String, Object> getVisitorSlotDetails(
            String societyName,
            String slotNumber) throws Exception {

        societyName =
                validateSocietyName(societyName);

        slotNumber =
                validateSlotNumber(slotNumber);

        validateVisitorSlotNumber(slotNumber);

        return dao.getVisitorSlotDetails(
                societyName,
                slotNumber
        );
    }

    // ============================================================
    // VALIDATE SOCIETY
    // ============================================================

    private String validateSocietyName(
            String societyName) {

        if (societyName == null ||
                societyName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty."
            );
        }

        return societyName.trim();
    }

    // ============================================================
    // VALIDATE SLOT
    // ============================================================

    private String validateSlotNumber(
            String slotNumber) {

        if (slotNumber == null ||
                slotNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Parking slot number cannot be empty."
            );
        }

        return slotNumber
                .trim()
                .toUpperCase();
    }

    // ============================================================
    // VALIDATE VISITOR SLOT
    // ============================================================

    private void validateVisitorSlotNumber(
            String slotNumber) {

        if (slotNumber == null ||
                slotNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Visitor parking slot cannot be empty."
            );
        }

        String normalized =
                slotNumber.trim().toUpperCase();

        if (!normalized.matches("V-0[1-4]")) {

            throw new IllegalArgumentException(
                    "Invalid visitor parking slot: "
                            + normalized
                            + ". Only V-01, V-02, V-03 and V-04 "
                            + "are allowed."
            );
        }
    }

    // ============================================================
    // CHECK VISITOR SLOT NUMBER
    // ============================================================

    private boolean isVisitorSlotNumber(
            String slotNumber) {

        if (slotNumber == null) {
            return false;
        }

        return slotNumber
                .trim()
                .toUpperCase()
                .startsWith("V-");
    }

    // ============================================================
    // EXTRACT VISITOR SLOT NUMBER
    //
    // V-01 -> 1
    // V-02 -> 2
    // ============================================================

    private int extractVisitorSlotNumber(
            String slotNumber) {

        try {

            String normalized =
                    slotNumber
                            .trim()
                            .toUpperCase();

            if (!normalized.startsWith("V-")) {
                return -1;
            }

            return Integer.parseInt(
                    normalized.substring(2)
            );

        } catch (Exception e) {

            return -1;
        }
    }

    // ============================================================
    // SAFE STRING
    // ============================================================

    private String safeValue(
            String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }
}