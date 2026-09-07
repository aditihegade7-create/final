package com.society.dao.Secretary_dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;

import com.society.config.FirebaseConfig;
import com.society.model.Secretary_model.SecretaryParking_model.AssignedParking;
import com.society.model.Secretary_model.SecretaryParking_model.ParkingMember;
import com.society.model.Secretary_model.SecretaryParking_model.ParkingSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SecretaryParkingDAO {

    private final Firestore db;

    // ============================================================
    // CONSTANTS
    // ============================================================

    private static final String SLOT_TYPE_RESIDENT = "RESIDENT";
    private static final String SLOT_TYPE_VISITOR = "VISITOR";

    private static final String STATUS_AVAILABLE = "Available";
    private static final String STATUS_ASSIGNED = "Assigned";
    private static final String STATUS_OCCUPIED = "Occupied";

    private static final int MAX_VISITOR_SLOTS = 4;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public SecretaryParkingDAO() {
        db = FirebaseConfig.getFirestore();
    }

    // ============================================================
    // GET SECRETARY SOCIETY
    //
    // Secretaries/{secretaryEmail}
    // ============================================================

    public String getSecretarySociety(String secretaryEmail)
            throws Exception {

        if (secretaryEmail == null
                || secretaryEmail.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Secretary email cannot be empty.");
        }

        secretaryEmail = secretaryEmail.trim();

        DocumentSnapshot document = db.collection("Secretaries")
                .document(secretaryEmail)
                .get()
                .get();

        if (!document.exists()) {

            throw new Exception(
                    "Secretary record not found for:\n"
                            + secretaryEmail);
        }

        String society = getFirstAvailable(
                document,
                "society",
                "societyName");

        if (society.isEmpty()) {

            throw new Exception(
                    "Society name is not available in:\n"
                            + "Secretaries/"
                            + secretaryEmail);
        }

        return society;
    }

    // ============================================================
    // VERIFY SECRETARY BELONGS TO SOCIETY
    // ============================================================

    private String validateSecretarySociety(
            String secretaryEmail,
            String requestedSociety)
            throws Exception {

        if (secretaryEmail == null
                || secretaryEmail.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Secretary email cannot be empty.");
        }

        if (requestedSociety == null
                || requestedSociety.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty.");
        }

        secretaryEmail = secretaryEmail.trim();
        requestedSociety = requestedSociety.trim();

        String actualSociety =
                getSecretarySociety(secretaryEmail);

        if (!actualSociety.equalsIgnoreCase(
                requestedSociety)) {

            throw new IllegalArgumentException(
                    "Secretary does not belong to society: "
                            + requestedSociety);
        }

        return actualSociety.trim();
    }

    // ============================================================
    // GET SOCIETY MEMBERS
    //
    // Residents + Owners
    // ============================================================

    public List<ParkingMember> getSocietyMembers(
            String societyName)
            throws Exception {

        List<ParkingMember> members =
                new ArrayList<>();

        if (societyName == null
                || societyName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty.");
        }

        societyName = societyName.trim();

        Set<String> addedEmails =
                new HashSet<>();

        // ========================================================
        // RESIDENTS
        // ========================================================

        ApiFuture<QuerySnapshot> residentFuture =
                db.collection("Residents")
                        .whereEqualTo(
                                "society",
                                societyName)
                        .get();

        QuerySnapshot residentSnapshot =
                residentFuture.get();

        for (DocumentSnapshot document
                : residentSnapshot.getDocuments()) {

            String email =
                    getFirstAvailable(
                            document,
                            "email",
                            "residentEmail",
                            "userEmail");

            if (email.isEmpty()) {
                email = document.getId();
            }

            email = email.trim();

            if (email.isEmpty()
                    || addedEmails.contains(
                            email.toLowerCase())) {
                continue;
            }

            String name =
                    getFirstAvailable(
                            document,
                            "name",
                            "residentName",
                            "ownerName");

            String flatNo =
                    getFirstAvailable(
                            document,
                            "flatNo",
                            "flatNumber");

            String role =
                    getString(
                            document,
                            "role");

            if (role.isEmpty()) {
                role = "Resident";
            }

            members.add(
                    new ParkingMember(
                            email,
                            name,
                            flatNo,
                            role,
                            societyName));

            addedEmails.add(
                    email.toLowerCase());
        }

        // ========================================================
        // OWNERS
        // ========================================================

        ApiFuture<QuerySnapshot> ownerFuture =
                db.collection("Owners")
                        .whereEqualTo(
                                "society",
                                societyName)
                        .get();

        QuerySnapshot ownerSnapshot =
                ownerFuture.get();

        for (DocumentSnapshot document
                : ownerSnapshot.getDocuments()) {

            String email =
                    getFirstAvailable(
                            document,
                            "email",
                            "ownerEmail",
                            "userEmail");

            if (email.isEmpty()) {
                email = document.getId();
            }

            email = email.trim();

            if (email.isEmpty()
                    || addedEmails.contains(
                            email.toLowerCase())) {
                continue;
            }

            String name =
                    getFirstAvailable(
                            document,
                            "name",
                            "ownerName",
                            "residentName");

            String flatNo =
                    getFirstAvailable(
                            document,
                            "flatNo",
                            "flatNumber");

            String role =
                    getString(
                            document,
                            "role");

            if (role.isEmpty()) {
                role = "Owner";
            }

            members.add(
                    new ParkingMember(
                            email,
                            name,
                            flatNo,
                            role,
                            societyName));

            addedEmails.add(
                    email.toLowerCase());
        }

        // ========================================================
        // SORT
        // ========================================================

        members.sort((a, b) -> {

            int flatCompare =
                    safe(a.getFlatNo())
                            .compareToIgnoreCase(
                                    safe(b.getFlatNo()));

            if (flatCompare != 0) {
                return flatCompare;
            }

            return safe(a.getName())
                    .compareToIgnoreCase(
                            safe(b.getName()));
        });

        return members;
    }

    // ============================================================
    // GET / CREATE SOCIETY PARKING DOCUMENT
    //
    // ParkingSlots
    //    └── SocietyName-UniqueID
    //         └── secretaryEmail
    //              └── slotNumber
    // ============================================================

    private DocumentReference getSocietyParkingReference(
            String societyName)
            throws Exception {

        if (societyName == null
                || societyName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty.");
        }

        societyName = societyName.trim();

        CollectionReference parkingCollection =
                db.collection("ParkingSlots");

        QuerySnapshot snapshot =
                parkingCollection
                        .whereEqualTo(
                                "society",
                                societyName)
                        .limit(1)
                        .get()
                        .get();

        if (!snapshot.isEmpty()) {

            return snapshot
                    .getDocuments()
                    .get(0)
                    .getReference();
        }

        String uniqueId =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();

        String documentId =
                societyName + "-" + uniqueId;

        DocumentReference societyRef =
                parkingCollection
                        .document(documentId);

        Map<String, Object> societyData =
                new HashMap<>();

        societyData.put(
                "society",
                societyName);

        societyData.put(
                "societyName",
                societyName);

        societyData.put(
                "societyId",
                uniqueId);

        societyData.put(
                "parkingDocumentId",
                documentId);

        societyData.put(
                "createdAt",
                Timestamp.now());

        societyData.put(
                "lastUpdatedAt",
                Timestamp.now());

        societyRef.set(
                societyData,
                SetOptions.merge())
                .get();

        System.out.println(
                "New Parking Society Created: "
                        + documentId);

        return societyRef;
    }

    // ============================================================
    // FIND EXISTING SOCIETY PARKING DOCUMENT
    //
    // DOES NOT CREATE
    // ============================================================

    private DocumentReference findSocietyParkingReference(
            String societyName)
            throws Exception {

        if (societyName == null
                || societyName.trim().isEmpty()) {

            return null;
        }

        societyName = societyName.trim();

        QuerySnapshot snapshot =
                db.collection("ParkingSlots")
                        .whereEqualTo(
                                "society",
                                societyName)
                        .limit(1)
                        .get()
                        .get();

        if (snapshot.isEmpty()) {
            return null;
        }

        return snapshot
                .getDocuments()
                .get(0)
                .getReference();
    }

    // ============================================================
    // GET ALL PARKING SLOTS
    // ============================================================

    public List<ParkingSlot> getParkingSlots(
            String societyName)
            throws Exception {

        List<ParkingSlot> slots =
                new ArrayList<>();

        if (societyName == null
                || societyName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty.");
        }

        societyName = societyName.trim();

        DocumentReference societyRef =
                findSocietyParkingReference(
                        societyName);

        if (societyRef == null) {
            return slots;
        }

        for (CollectionReference secretaryCollection
                : societyRef.listCollections()) {

            QuerySnapshot snapshot =
                    secretaryCollection
                            .get()
                            .get();

            for (DocumentSnapshot document
                    : snapshot.getDocuments()) {

                if (!belongsToSociety(
                        document,
                        societyName)) {
                    continue;
                }

                String slotNumber =
                        getFirstAvailable(
                                document,
                                "slotNumber",
                                "slotName");

                if (slotNumber.isEmpty()) {
                    slotNumber =
                            document.getId();
                }

                String status =
                        getString(
                                document,
                                "status");

                if (status.isEmpty()) {
                    status =
                            STATUS_AVAILABLE;
                }

                String slotType =
                        normalizeSlotType(
                                getString(
                                        document,
                                        "slotType"));

                slots.add(
                        new ParkingSlot(
                                slotNumber,
                                status,
                                slotType));
            }
        }

        slots.sort((a, b) ->
                safe(a.getSlotNumber())
                        .compareToIgnoreCase(
                                safe(b.getSlotNumber())));

        return slots;
    }

    // ============================================================
    // GET ASSIGNED PARKING
    //
    // ONLY RESIDENT / OWNER
    // ============================================================

    public List<AssignedParking> getAssignedParking(
            String societyName)
            throws Exception {

        List<AssignedParking> assignments =
                new ArrayList<>();

        if (societyName == null
                || societyName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty.");
        }

        societyName = societyName.trim();

        DocumentReference societyRef =
                findSocietyParkingReference(
                        societyName);

        if (societyRef == null) {
            return assignments;
        }

        for (CollectionReference secretaryCollection
                : societyRef.listCollections()) {

            QuerySnapshot snapshot =
                    secretaryCollection
                            .get()
                            .get();

            for (DocumentSnapshot document
                    : snapshot.getDocuments()) {

                if (!belongsToSociety(
                        document,
                        societyName)) {
                    continue;
                }

                String slotType =
                        normalizeSlotType(
                                getString(
                                        document,
                                        "slotType"));

                if (SLOT_TYPE_VISITOR.equalsIgnoreCase(
                        slotType)) {
                    continue;
                }

                String slotNumber =
                        getFirstAvailable(
                                document,
                                "slotNumber",
                                "slotName");

                if (slotNumber.isEmpty()) {
                    slotNumber =
                            document.getId();
                }

                String status =
                        getString(
                                document,
                                "status");

                String memberEmail =
                        getFirstAvailable(
                                document,
                                "allocatedToEmail",
                                "memberEmail");

                String memberName =
                        getFirstAvailable(
                                document,
                                "allocatedToName",
                                "memberName",
                                "residentName",
                                "ownerName");

                String flatNo =
                        getFirstAvailable(
                                document,
                                "flatNo",
                                "flatNumber");

                String role =
                        getString(
                                document,
                                "role");

                String vehicleNumber =
                        getString(
                                document,
                                "vehicleNumber");

                if ((!memberEmail.isEmpty()
                        || !memberName.isEmpty())
                        && status.equalsIgnoreCase(
                                STATUS_ASSIGNED)) {

                    assignments.add(
                            new AssignedParking(
                                    memberEmail,
                                    memberName,
                                    flatNo,
                                    role,
                                    vehicleNumber,
                                    slotNumber,
                                    status));
                }
            }
        }

        assignments.sort((a, b) ->
                safe(a.getSlot())
                        .compareToIgnoreCase(
                                safe(b.getSlot())));

        return assignments;
    }

    // ============================================================
    // GET ASSIGNED PARKING FOR RESIDENT
    // ============================================================

    public List<AssignedParking> getAssignedParkingForResident(
            String residentEmail)
            throws Exception {

        List<AssignedParking> assignments =
                new ArrayList<>();

        if (residentEmail == null
                || residentEmail.trim().isEmpty()) {

            return assignments;
        }

        residentEmail =
                residentEmail.trim();

        ParkingMember resident =
                findMemberAcrossSociety(
                        residentEmail);

        if (resident == null) {

            System.out.println(
                    "Resident not found: "
                            + residentEmail);

            return assignments;
        }

        String societyName =
                safe(resident.getSociety());

        if (societyName.isEmpty()) {

            System.out.println(
                    "Resident society not found.");

            return assignments;
        }

        DocumentReference societyRef =
                findSocietyParkingReference(
                        societyName);

        if (societyRef == null) {
            return assignments;
        }

        for (CollectionReference secretaryCollection
                : societyRef.listCollections()) {

            QuerySnapshot snapshot =
                    secretaryCollection
                            .get()
                            .get();

            for (DocumentSnapshot document
                    : snapshot.getDocuments()) {

                if (!belongsToSociety(
                        document,
                        societyName)) {
                    continue;
                }

                String slotType =
                        normalizeSlotType(
                                getString(
                                        document,
                                        "slotType"));

                if (SLOT_TYPE_VISITOR.equalsIgnoreCase(
                        slotType)) {
                    continue;
                }

                String allocatedEmail =
                        getFirstAvailable(
                                document,
                                "allocatedToEmail",
                                "memberEmail");

                String status =
                        getString(
                                document,
                                "status");

                if (allocatedEmail.equalsIgnoreCase(
                        residentEmail)
                        && status.equalsIgnoreCase(
                                STATUS_ASSIGNED)) {

                    String slotNumber =
                            getFirstAvailable(
                                    document,
                                    "slotNumber",
                                    "slotName");

                    if (slotNumber.isEmpty()) {
                        slotNumber =
                                document.getId();
                    }

                    String memberName =
                            getFirstAvailable(
                                    document,
                                    "allocatedToName",
                                    "memberName",
                                    "residentName",
                                    "ownerName");

                    String flatNo =
                            getFirstAvailable(
                                    document,
                                    "flatNo",
                                    "flatNumber");

                    String role =
                            getString(
                                    document,
                                    "role");

                    String vehicleNumber =
                            getString(
                                    document,
                                    "vehicleNumber");

                    assignments.add(
                            new AssignedParking(
                                    allocatedEmail,
                                    memberName,
                                    flatNo,
                                    role,
                                    vehicleNumber,
                                    slotNumber,
                                    status));
                }
            }
        }

        assignments.sort((a, b) ->
                safe(a.getSlot())
                        .compareToIgnoreCase(
                                safe(b.getSlot())));

        return assignments;
    }

    // ============================================================
    // CHECK WHETHER SLOT EXISTS
    // ============================================================

    public boolean slotExists(
            String societyName,
            String slotNumber)
            throws Exception {

        if (societyName == null
                || societyName.trim().isEmpty()
                || slotNumber == null
                || slotNumber.trim().isEmpty()) {

            return false;
        }

        societyName =
                societyName.trim();

        slotNumber =
                slotNumber.trim()
                        .toUpperCase();

        DocumentReference societyRef =
                findSocietyParkingReference(
                        societyName);

        if (societyRef == null) {
            return false;
        }

        return findSlotDocument(
                societyRef,
                slotNumber) != null;
    }

    // ============================================================
    // CHECK MEMBER ALREADY HAS PERMANENT PARKING
    // ============================================================

    public boolean memberAlreadyHasParking(
            String societyName,
            String memberEmail)
            throws Exception {

        if (societyName == null
                || societyName.trim().isEmpty()
                || memberEmail == null
                || memberEmail.trim().isEmpty()) {

            return false;
        }

        societyName =
                societyName.trim();

        memberEmail =
                memberEmail.trim();

        DocumentReference societyRef =
                findSocietyParkingReference(
                        societyName);

        if (societyRef == null) {
            return false;
        }

        for (CollectionReference secretaryCollection
                : societyRef.listCollections()) {

            QuerySnapshot snapshot =
                    secretaryCollection
                            .get()
                            .get();

            for (DocumentSnapshot document
                    : snapshot.getDocuments()) {

                if (!belongsToSociety(
                        document,
                        societyName)) {
                    continue;
                }

                String slotType =
                        normalizeSlotType(
                                getString(
                                        document,
                                        "slotType"));

                if (SLOT_TYPE_VISITOR.equalsIgnoreCase(
                        slotType)) {
                    continue;
                }

                String email =
                        getFirstAvailable(
                                document,
                                "allocatedToEmail",
                                "memberEmail");

                String status =
                        getString(
                                document,
                                "status");

                if (email.equalsIgnoreCase(
                        memberEmail)
                        && status.equalsIgnoreCase(
                                STATUS_ASSIGNED)) {

                    return true;
                }
            }
        }

        return false;
    }

    // ============================================================
    // SAVE PERMANENT RESIDENT / OWNER PARKING
    // ============================================================

    public void saveParkingAllocation(
            String secretaryEmail,
            String societyName,
            String slotNumber,
            ParkingMember member)
            throws Exception {

        if (secretaryEmail == null
                || secretaryEmail.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Secretary email cannot be empty.");
        }

        if (societyName == null
                || societyName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty.");
        }

        if (slotNumber == null
                || slotNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Parking slot number cannot be empty.");
        }

        if (member == null) {

            throw new IllegalArgumentException(
                    "Parking member cannot be null.");
        }

        secretaryEmail =
                secretaryEmail.trim();

        societyName =
                societyName.trim();

        slotNumber =
                slotNumber.trim()
                        .toUpperCase();

        String actualSociety =
                validateSecretarySociety(
                        secretaryEmail,
                        societyName);

        societyName =
                actualSociety;

        if (slotNumber.startsWith("V-")) {

            throw new IllegalArgumentException(
                    "V- slots are reserved for visitor parking.");
        }

        // ========================================================
        // VERIFY MEMBER ACTUALLY BELONGS TO SOCIETY
        // ========================================================

        ParkingMember actualMember =
                findMemberInSociety(
                        societyName,
                        member.getEmail());

        if (actualMember == null) {

            throw new IllegalArgumentException(
                    "Selected member does not belong "
                            + "to society: "
                            + societyName);
        }

        if (slotExists(
                societyName,
                slotNumber)) {

            throw new IllegalArgumentException(
                    "Parking slot "
                            + slotNumber
                            + " already exists.");
        }

        if (memberAlreadyHasParking(
                societyName,
                actualMember.getEmail())) {

            throw new IllegalArgumentException(
                    "This member already has a parking slot.");
        }

        String secretaryName =
                getSecretaryName(
                        secretaryEmail);

        DocumentReference societyRef =
                getSocietyParkingReference(
                        societyName);

        societyRef.set(
                createLastUpdatedMap(),
                SetOptions.merge())
                .get();

        DocumentReference slotRef =
                societyRef
                        .collection(secretaryEmail)
                        .document(slotNumber);

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "slotNumber",
                slotNumber);

        data.put(
                "slotType",
                SLOT_TYPE_RESIDENT);

        data.put(
                "society",
                societyName);

        data.put(
                "societyName",
                societyName);

        data.put(
                "addedByName",
                secretaryName);

        data.put(
                "addedByEmail",
                secretaryEmail);

        data.put(
                "allocatedToName",
                safe(actualMember.getName()));

        data.put(
                "allocatedToEmail",
                safe(actualMember.getEmail()));

        data.put(
                "memberName",
                safe(actualMember.getName()));

        data.put(
                "memberEmail",
                safe(actualMember.getEmail()));

        data.put(
                "flatNo",
                safe(actualMember.getFlatNo()));

        data.put(
                "role",
                safe(actualMember.getRole()));

        data.put(
                "memberSociety",
                societyName);

        data.put(
                "status",
                STATUS_ASSIGNED);

        data.put(
                "vehicleNumber",
                "");

        data.put(
                "assignedBy",
                secretaryEmail);

        data.put(
                "assignedByName",
                secretaryName);

        data.put(
                "assignmentType",
                "Secretary Allocation");

        data.put(
                "assignedAt",
                Timestamp.now());

        data.put(
                "createdAt",
                Timestamp.now());

        data.put(
                "lastUpdatedAt",
                Timestamp.now());

        slotRef.set(
                data,
                SetOptions.merge())
                .get();
    }

    // ============================================================
    // GET VISITOR SLOT COUNT
    // ============================================================

    public int getVisitorSlotCount(
            String societyName)
            throws Exception {

        if (societyName == null
                || societyName.trim().isEmpty()) {

            return 0;
        }

        societyName =
                societyName.trim();

        DocumentReference societyRef =
                findSocietyParkingReference(
                        societyName);

        if (societyRef == null) {
            return 0;
        }

        int count = 0;

        for (CollectionReference secretaryCollection
                : societyRef.listCollections()) {

            QuerySnapshot snapshot =
                    secretaryCollection
                            .get()
                            .get();

            for (DocumentSnapshot document
                    : snapshot.getDocuments()) {

                if (!belongsToSociety(
                        document,
                        societyName)) {
                    continue;
                }

                String slotType =
                        normalizeSlotType(
                                getString(
                                        document,
                                        "slotType"));

                if (SLOT_TYPE_VISITOR.equalsIgnoreCase(
                        slotType)) {

                    count++;
                }
            }
        }

        return count;
    }

    // ============================================================
    // CONTROLLER COMPATIBILITY
    // ============================================================

    public int countVisitorParkingSlots(
            String societyName)
            throws Exception {

        return getVisitorSlotCount(
                societyName);
    }

    // ============================================================
    // GET NEXT VISITOR SLOT NUMBER
    //
    // Reuses missing slots:
    // V-01, V-02, V-03, V-04
    // ============================================================

    public String getNextVisitorSlotNumber(
            String societyName)
            throws Exception {

        if (societyName == null
                || societyName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty.");
        }

        societyName =
                societyName.trim();

        DocumentReference societyRef =
                findSocietyParkingReference(
                        societyName);

        Set<Integer> usedNumbers =
                new HashSet<>();

        if (societyRef != null) {

            for (CollectionReference secretaryCollection
                    : societyRef.listCollections()) {

                QuerySnapshot snapshot =
                        secretaryCollection
                                .get()
                                .get();

                for (DocumentSnapshot document
                        : snapshot.getDocuments()) {

                    if (!belongsToSociety(
                            document,
                            societyName)) {
                        continue;
                    }

                    String slotType =
                            getString(
                                    document,
                                    "slotType");

                    if (!SLOT_TYPE_VISITOR.equalsIgnoreCase(
                            slotType)) {
                        continue;
                    }

                    String slotNumber =
                            getFirstAvailable(
                                    document,
                                    "slotNumber",
                                    "slotName");

                    if (slotNumber.isEmpty()) {
                        slotNumber =
                                document.getId();
                    }

                    int number =
                            extractVisitorNumber(
                                    slotNumber);

                    if (number >= 1
                            && number <= MAX_VISITOR_SLOTS) {

                        usedNumbers.add(number);
                    }
                }
            }
        }

        for (int i = 1;
                i <= MAX_VISITOR_SLOTS;
                i++) {

            if (!usedNumbers.contains(i)) {

                return String.format(
                        "V-%02d",
                        i);
            }
        }

        return "";
    }

    // ============================================================
    // CREATE VISITOR PARKING SLOT
    //
    // EXPLICIT SLOT NUMBER
    //
    // Controller uses:
    //
    // dao.createVisitorParkingSlot(
    //     secretaryEmail,
    //     societyName,
    //     slotNumber
    // );
    // ============================================================

    public String createVisitorParkingSlot(
            String secretaryEmail,
            String societyName,
            String slotNumber)
            throws Exception {

        if (secretaryEmail == null
                || secretaryEmail.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Secretary email cannot be empty.");
        }

        if (societyName == null
                || societyName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty.");
        }

        if (slotNumber == null
                || slotNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Visitor parking slot number cannot be empty.");
        }

        secretaryEmail =
                secretaryEmail.trim();

        societyName =
                societyName.trim();

        slotNumber =
                slotNumber.trim()
                        .toUpperCase();

        String actualSociety =
                validateSecretarySociety(
                        secretaryEmail,
                        societyName);

        societyName =
                actualSociety;

        // ========================================================
        // VALIDATE FORMAT
        // ========================================================

        int visitorNumber =
                extractVisitorNumber(
                        slotNumber);

        if (!slotNumber.startsWith("V-")
                || visitorNumber <= 0
                || visitorNumber > MAX_VISITOR_SLOTS) {

            throw new IllegalArgumentException(
                    "Visitor parking slot must be between "
                            + "V-01 and V-"
                            + String.format(
                                    "%02d",
                                    MAX_VISITOR_SLOTS)
                            + ".");
        }

        // ========================================================
        // CHECK MAXIMUM
        // ========================================================

        int count =
                getVisitorSlotCount(
                        societyName);

        if (count >= MAX_VISITOR_SLOTS) {

            throw new IllegalStateException(
                    "Maximum "
                            + MAX_VISITOR_SLOTS
                            + " visitor parking slots are "
                            + "allowed for this society.");
        }

        // ========================================================
        // CHECK DUPLICATE
        // ========================================================

        if (slotExists(
                societyName,
                slotNumber)) {

            throw new IllegalArgumentException(
                    "Parking slot "
                            + slotNumber
                            + " already exists.");
        }

        String secretaryName =
                getSecretaryName(
                        secretaryEmail);

        DocumentReference societyRef =
                getSocietyParkingReference(
                        societyName);

        societyRef.set(
                createLastUpdatedMap(),
                SetOptions.merge())
                .get();

        DocumentReference slotRef =
                societyRef
                        .collection(secretaryEmail)
                        .document(slotNumber);

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "slotNumber",
                slotNumber);

        data.put(
                "slotType",
                SLOT_TYPE_VISITOR);

        data.put(
                "society",
                societyName);

        data.put(
                "societyName",
                societyName);

        data.put(
                "addedByName",
                secretaryName);

        data.put(
                "addedByEmail",
                secretaryEmail);

        data.put(
                "createdBy",
                secretaryEmail);

        data.put(
                "createdByName",
                secretaryName);

        data.put(
                "status",
                STATUS_AVAILABLE);

        data.put(
                "visitorName",
                "");

        data.put(
                "visitorPhone",
                "");

        data.put(
                "vehicleNumber",
                "");

        data.put(
                "visitingResidentEmail",
                "");

        data.put(
                "visitingResidentName",
                "");

        data.put(
                "visitingResidentFlat",
                "");

        data.put(
                "visitingResidentRole",
                "");

        data.put(
                "occupiedAt",
                null);

        data.put(
                "occupiedBy",
                "");

        data.put(
                "releasedAt",
                null);

        data.put(
                "assignmentType",
                "Visitor Parking");

        data.put(
                "createdAt",
                Timestamp.now());

        data.put(
                "lastUpdatedAt",
                Timestamp.now());

        slotRef.set(
                data,
                SetOptions.merge())
                .get();

        System.out.println(
                "==========================================");

        System.out.println(
                "VISITOR PARKING SLOT CREATED");

        System.out.println(
                "Society : "
                        + societyName);

        System.out.println(
                "Slot    : "
                        + slotNumber);

        System.out.println(
                "Status  : Available");

        System.out.println(
                "Created By : "
                        + secretaryEmail);

        System.out.println(
                "Path : "
                        + slotRef.getPath());

        System.out.println(
                "==========================================");

        return slotNumber;
    }

    // ============================================================
    // CREATE VISITOR SLOT WITHOUT SLOT NUMBER
    //
    // Compatibility overload
    // ============================================================

    public String createVisitorParkingSlot(
            String secretaryEmail,
            String societyName)
            throws Exception {

        String nextSlot =
                getNextVisitorSlotNumber(
                        societyName);

        if (nextSlot == null
                || nextSlot.isEmpty()) {

            throw new IllegalStateException(
                    "Maximum "
                            + MAX_VISITOR_SLOTS
                            + " visitor parking slots "
                            + "already exist.");
        }

        return createVisitorParkingSlot(
                secretaryEmail,
                societyName,
                nextSlot);
    }

    // ============================================================
    // ASSIGN VISITOR PARKING
    //
    // 6-ARGUMENT VERSION
    //
    // REQUIRED BY YOUR CONTROLLER
    //
    // dao.assignVisitorParking(
    //     secretaryEmail,
    //     societyName,
    //     slotNumber,
    //     visitorName,
    //     vehicleNumber,
    //     visitingResident
    // );
    // ============================================================

    public void assignVisitorParking(
            String secretaryEmail,
            String societyName,
            String slotNumber,
            String visitorName,
            String vehicleNumber,
            ParkingMember visitingResident)
            throws Exception {

        if (visitingResident == null) {

            throw new IllegalArgumentException(
                    "Visiting resident cannot be null.");
        }

        assignVisitorParking(
                secretaryEmail,
                societyName,
                slotNumber,
                visitorName,
                "",
                vehicleNumber,
                visitingResident.getEmail(),
                visitingResident.getName(),
                visitingResident.getFlatNo());
    }

    // ============================================================
    // ASSIGN VISITOR PARKING
    //
    // FULL VERSION
    // ============================================================

    public void assignVisitorParking(
            String secretaryEmail,
            String societyName,
            String slotNumber,
            String visitorName,
            String visitorPhone,
            String vehicleNumber,
            String visitingResidentEmail,
            String visitingResidentName,
            String visitingResidentFlat)
            throws Exception {

        validateVisitorAssignmentInput(
                secretaryEmail,
                societyName,
                slotNumber,
                visitorName,
                vehicleNumber,
                visitingResidentEmail);

        secretaryEmail =
                secretaryEmail.trim();

        societyName =
                societyName.trim();

        slotNumber =
                slotNumber.trim()
                        .toUpperCase();

        visitorName =
                visitorName.trim();

        visitorPhone =
                safe(visitorPhone);

        vehicleNumber =
                vehicleNumber.trim();

        visitingResidentEmail =
                visitingResidentEmail.trim();

        // ========================================================
        // VERIFY SECRETARY
        // ========================================================

        String actualSociety =
                validateSecretarySociety(
                        secretaryEmail,
                        societyName);

        societyName =
                actualSociety;

        // ========================================================
        // VERIFY VISITING MEMBER
        // ========================================================

        ParkingMember actualResident =
                findMemberInSociety(
                        societyName,
                        visitingResidentEmail);

        if (actualResident == null) {

            throw new IllegalArgumentException(
                    "Visiting resident does not belong "
                            + "to society: "
                            + societyName);
        }

        // ========================================================
        // FIND PARKING SOCIETY
        // ========================================================

        DocumentReference societyRef =
                findSocietyParkingReference(
                        societyName);

        if (societyRef == null) {

            throw new IllegalArgumentException(
                    "Parking structure does not exist "
                            + "for society: "
                            + societyName);
        }

        // ========================================================
        // FIND SLOT
        // ========================================================

        DocumentSnapshot slotDocument =
                findSlotDocument(
                        societyRef,
                        slotNumber);

        if (slotDocument == null) {

            throw new IllegalArgumentException(
                    "Visitor parking slot "
                            + slotNumber
                            + " does not exist.");
        }

        if (!belongsToSociety(
                slotDocument,
                societyName)) {

            throw new IllegalArgumentException(
                    "Parking slot belongs to another society.");
        }

        // ========================================================
        // SLOT TYPE
        // ========================================================

        String slotType =
                normalizeSlotType(
                        getString(
                                slotDocument,
                                "slotType"));

        if (!SLOT_TYPE_VISITOR.equalsIgnoreCase(
                slotType)) {

            throw new IllegalArgumentException(
                    "Slot "
                            + slotNumber
                            + " is not a visitor parking slot.");
        }

        // ========================================================
        // STATUS
        // ========================================================

        String currentStatus =
                getString(
                        slotDocument,
                        "status");

        if (!currentStatus.isEmpty()
                && !STATUS_AVAILABLE.equalsIgnoreCase(
                        currentStatus)) {

            throw new IllegalStateException(
                    "Visitor parking slot "
                            + slotNumber
                            + " is currently "
                            + currentStatus
                            + ".");
        }

        String secretaryName =
                getSecretaryName(
                        secretaryEmail);

        // ========================================================
        // UPDATE
        // ========================================================

        Map<String, Object> update =
                new HashMap<>();

        update.put(
                "slotNumber",
                slotNumber);

        update.put(
                "slotType",
                SLOT_TYPE_VISITOR);

        update.put(
                "society",
                societyName);

        update.put(
                "societyName",
                societyName);

        update.put(
                "status",
                STATUS_OCCUPIED);

        // ========================================================
        // VISITOR
        // ========================================================

        update.put(
                "visitorName",
                visitorName);

        update.put(
                "visitorPhone",
                visitorPhone);

        update.put(
                "vehicleNumber",
                vehicleNumber);

        // ========================================================
        // VISITING RESIDENT
        // ========================================================

        update.put(
                "visitingResidentEmail",
                actualResident.getEmail());

        update.put(
                "visitingResidentName",
                safe(actualResident.getName()));

        update.put(
                "visitingResidentFlat",
                safe(actualResident.getFlatNo()));

        update.put(
                "visitingResidentRole",
                safe(actualResident.getRole()));

        // ========================================================
        // IMPORTANT:
        // Visitor parking is NEVER permanent parking
        // ========================================================

        update.put(
                "allocatedToEmail",
                "");

        update.put(
                "allocatedToName",
                "");

        update.put(
                "memberEmail",
                "");

        update.put(
                "memberName",
                "");

        update.put(
                "flatNo",
                "");

        update.put(
                "role",
                "");

        update.put(
                "memberSociety",
                "");

        // ========================================================
        // OCCUPANCY
        // ========================================================

        update.put(
                "occupiedAt",
                Timestamp.now());

        update.put(
                "releasedAt",
                null);

        update.put(
                "occupiedBy",
                secretaryEmail);

        update.put(
                "occupiedByName",
                secretaryName);

        update.put(
                "assignedBy",
                secretaryEmail);

        update.put(
                "assignedByName",
                secretaryName);

        update.put(
                "assignmentType",
                "Temporary Visitor Parking");

        update.put(
                "lastUpdatedAt",
                Timestamp.now());

        // ========================================================
        // SAVE
        // ========================================================

        slotDocument
                .getReference()
                .set(
                        update,
                        SetOptions.merge())
                .get();

        System.out.println(
                "==========================================");

        System.out.println(
                "VISITOR PARKING OCCUPIED");

        System.out.println(
                "Society           : "
                        + societyName);

        System.out.println(
                "Visitor Slot      : "
                        + slotNumber);

        System.out.println(
                "Visitor Name      : "
                        + visitorName);

        System.out.println(
                "Vehicle Number    : "
                        + vehicleNumber);

        System.out.println(
                "Visiting Resident : "
                        + actualResident.getName());

        System.out.println(
                "Resident Email    : "
                        + actualResident.getEmail());

        System.out.println(
                "Resident Flat     : "
                        + actualResident.getFlatNo());

        System.out.println(
                "Status            : Occupied");

        System.out.println(
                "==========================================");
    }

    // ============================================================
    // RELEASE VISITOR PARKING
    //
    // OLD 2-ARG VERSION
    // ============================================================

    public void releaseVisitorParking(
            String societyName,
            String slotNumber)
            throws Exception {

        if (societyName == null
                || societyName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty.");
        }

        if (slotNumber == null
                || slotNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Visitor slot number cannot be empty.");
        }

        societyName =
                societyName.trim();

        slotNumber =
                slotNumber.trim()
                        .toUpperCase();

        DocumentReference societyRef =
                findSocietyParkingReference(
                        societyName);

        if (societyRef == null) {

            throw new IllegalArgumentException(
                    "Parking structure not found for society: "
                            + societyName);
        }

        DocumentSnapshot slotDocument =
                findSlotDocument(
                        societyRef,
                        slotNumber);

        if (slotDocument == null) {

            throw new IllegalArgumentException(
                    "Visitor slot "
                            + slotNumber
                            + " not found.");
        }

        if (!belongsToSociety(
                slotDocument,
                societyName)) {

            throw new IllegalArgumentException(
                    "Parking slot belongs to another society.");
        }

        String slotType =
                normalizeSlotType(
                        getString(
                                slotDocument,
                                "slotType"));

        if (!SLOT_TYPE_VISITOR.equalsIgnoreCase(
                slotType)) {

            throw new IllegalArgumentException(
                    "Slot "
                            + slotNumber
                            + " is not a visitor parking slot.");
        }

        Map<String, Object> update =
                new HashMap<>();

        update.put(
                "status",
                STATUS_AVAILABLE);

        update.put(
                "visitorName",
                "");

        update.put(
                "visitorPhone",
                "");

        update.put(
                "vehicleNumber",
                "");

        update.put(
                "visitingResidentEmail",
                "");

        update.put(
                "visitingResidentName",
                "");

        update.put(
                "visitingResidentFlat",
                "");

        update.put(
                "visitingResidentRole",
                "");

        update.put(
                "occupiedAt",
                null);

        update.put(
                "releasedAt",
                Timestamp.now());

        update.put(
                "occupiedBy",
                "");

        update.put(
                "occupiedByName",
                "");

        update.put(
                "assignmentType",
                "Visitor Parking");

        update.put(
                "slotType",
                SLOT_TYPE_VISITOR);

        // Permanent parking fields must stay empty
        update.put(
                "allocatedToEmail",
                "");

        update.put(
                "allocatedToName",
                "");

        update.put(
                "memberEmail",
                "");

        update.put(
                "memberName",
                "");

        update.put(
                "flatNo",
                "");

        update.put(
                "role",
                "");

        update.put(
                "memberSociety",
                "");

        update.put(
                "lastUpdatedAt",
                Timestamp.now());

        slotDocument
                .getReference()
                .set(
                        update,
                        SetOptions.merge())
                .get();

        System.out.println(
                "==========================================");

        System.out.println(
                "VISITOR PARKING RELEASED");

        System.out.println(
                "Society : "
                        + societyName);

        System.out.println(
                "Slot    : "
                        + slotNumber);

        System.out.println(
                "Status  : Available");

        System.out.println(
                "==========================================");
    }

    // ============================================================
    // RELEASE VISITOR PARKING
    //
    // CONTROLLER COMPATIBILITY
    //
    // releaseVisitorParking(
    //     secretaryEmail,
    //     societyName,
    //     slotNumber
    // )
    // ============================================================

    public void releaseVisitorParking(
            String secretaryEmail,
            String societyName,
            String slotNumber)
            throws Exception {

        String actualSociety =
                validateSecretarySociety(
                        secretaryEmail,
                        societyName);

        releaseVisitorParking(
                actualSociety,
                slotNumber);
    }

    // ============================================================
    // GET VISITOR PARKING SLOTS
    // ============================================================

    public List<ParkingSlot> getVisitorParkingSlots(
            String societyName)
            throws Exception {

        List<ParkingSlot> visitorSlots =
                new ArrayList<>();

        if (societyName == null
                || societyName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty.");
        }

        societyName =
                societyName.trim();

        DocumentReference societyRef =
                findSocietyParkingReference(
                        societyName);

        if (societyRef == null) {
            return visitorSlots;
        }

        for (CollectionReference secretaryCollection
                : societyRef.listCollections()) {

            QuerySnapshot snapshot =
                    secretaryCollection
                            .get()
                            .get();

            for (DocumentSnapshot document
                    : snapshot.getDocuments()) {

                if (!belongsToSociety(
                        document,
                        societyName)) {
                    continue;
                }

                String slotType =
                        getString(
                                document,
                                "slotType");

                if (!SLOT_TYPE_VISITOR.equalsIgnoreCase(
                        slotType)) {
                    continue;
                }

                String slotNumber =
                        getFirstAvailable(
                                document,
                                "slotNumber",
                                "slotName");

                if (slotNumber.isEmpty()) {
                    slotNumber =
                            document.getId();
                }

                String status =
                        getString(
                                document,
                                "status");

                if (status.isEmpty()) {
                    status =
                            STATUS_AVAILABLE;
                }

                visitorSlots.add(
                        new ParkingSlot(
                                slotNumber,
                                status,
                                SLOT_TYPE_VISITOR));
            }
        }

        visitorSlots.sort((a, b) ->
                safe(a.getSlotNumber())
                        .compareToIgnoreCase(
                                safe(b.getSlotNumber())));

        return visitorSlots;
    }

    // ============================================================
    // GET AVAILABLE VISITOR PARKING
    // ============================================================

    public List<ParkingSlot> getAvailableVisitorParkingSlots(
            String societyName)
            throws Exception {

        List<ParkingSlot> availableSlots =
                new ArrayList<>();

        List<ParkingSlot> allVisitorSlots =
                getVisitorParkingSlots(
                        societyName);

        for (ParkingSlot slot
                : allVisitorSlots) {

            if (STATUS_AVAILABLE.equalsIgnoreCase(
                    safe(slot.getStatus()))) {

                availableSlots.add(slot);
            }
        }

        return availableSlots;
    }

    // ============================================================
    // GET OCCUPIED VISITOR PARKING
    // ============================================================

    public List<ParkingSlot> getOccupiedVisitorParkingSlots(
            String societyName)
            throws Exception {

        List<ParkingSlot> occupiedSlots =
                new ArrayList<>();

        List<ParkingSlot> allVisitorSlots =
                getVisitorParkingSlots(
                        societyName);

        for (ParkingSlot slot
                : allVisitorSlots) {

            if (STATUS_OCCUPIED.equalsIgnoreCase(
                    safe(slot.getStatus()))) {

                occupiedSlots.add(slot);
            }
        }

        return occupiedSlots;
    }

    // ============================================================
    // GET VISITOR SLOT DETAILS
    // ============================================================

    public Map<String, Object> getVisitorSlotDetails(
            String societyName,
            String slotNumber)
            throws Exception {

        if (societyName == null
                || societyName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty.");
        }

        if (slotNumber == null
                || slotNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Slot number cannot be empty.");
        }

        societyName =
                societyName.trim();

        slotNumber =
                slotNumber.trim()
                        .toUpperCase();

        DocumentReference societyRef =
                findSocietyParkingReference(
                        societyName);

        if (societyRef == null) {
            return null;
        }

        DocumentSnapshot document =
                findSlotDocument(
                        societyRef,
                        slotNumber);

        if (document == null) {
            return null;
        }

        if (!belongsToSociety(
                document,
                societyName)) {
            return null;
        }

        String slotType =
                getString(
                        document,
                        "slotType");

        if (!SLOT_TYPE_VISITOR.equalsIgnoreCase(
                slotType)) {

            return null;
        }

        return document.getData();
    }

    // ============================================================
    // GET SINGLE PARKING SLOT
    // ============================================================

    public ParkingSlot getParkingSlot(
            String societyName,
            String slotNumber)
            throws Exception {

        if (societyName == null
                || societyName.trim().isEmpty()
                || slotNumber == null
                || slotNumber.trim().isEmpty()) {

            return null;
        }

        societyName =
                societyName.trim();

        slotNumber =
                slotNumber.trim()
                        .toUpperCase();

        DocumentReference societyRef =
                findSocietyParkingReference(
                        societyName);

        if (societyRef == null) {
            return null;
        }

        DocumentSnapshot document =
                findSlotDocument(
                        societyRef,
                        slotNumber);

        if (document == null) {
            return null;
        }

        if (!belongsToSociety(
                document,
                societyName)) {
            return null;
        }

        String actualSlotNumber =
                getFirstAvailable(
                        document,
                        "slotNumber",
                        "slotName");

        if (actualSlotNumber.isEmpty()) {
            actualSlotNumber =
                    document.getId();
        }

        String status =
                getString(
                        document,
                        "status");

        if (status.isEmpty()) {
            status =
                    STATUS_AVAILABLE;
        }

        String slotType =
                normalizeSlotType(
                        getString(
                                document,
                                "slotType"));

        return new ParkingSlot(
                actualSlotNumber,
                status,
                slotType);
    }

    // ============================================================
    // FIND SLOT DOCUMENT
    // ============================================================

    private DocumentSnapshot findSlotDocument(
            DocumentReference societyRef,
            String slotNumber)
            throws Exception {

        if (societyRef == null
                || slotNumber == null
                || slotNumber.trim().isEmpty()) {

            return null;
        }

        slotNumber =
                slotNumber.trim()
                        .toUpperCase();

        for (CollectionReference secretaryCollection
                : societyRef.listCollections()) {

            // ====================================================
            // DIRECT DOCUMENT ID
            // ====================================================

            DocumentSnapshot directDocument =
                    secretaryCollection
                            .document(slotNumber)
                            .get()
                            .get();

            if (directDocument.exists()
                    && belongsToSociety(
                            directDocument,
                            getSocietyFromReference(
                                    societyRef))) {

                return directDocument;
            }

            // ====================================================
            // SCAN
            // ====================================================

            QuerySnapshot snapshot =
                    secretaryCollection
                            .get()
                            .get();

            for (DocumentSnapshot document
                    : snapshot.getDocuments()) {

                String existingSlot =
                        getFirstAvailable(
                                document,
                                "slotNumber",
                                "slotName");

                if (existingSlot.isEmpty()) {
                    existingSlot =
                            document.getId();
                }

                if (existingSlot.equalsIgnoreCase(
                        slotNumber)) {

                    return document;
                }
            }
        }

        return null;
    }

    // ============================================================
    // FIND SOCIETY MEMBER
    //
    // Residents + Owners
    // ============================================================

    private ParkingMember findMemberInSociety(
            String societyName,
            String memberEmail)
            throws Exception {

        if (societyName == null
                || societyName.trim().isEmpty()
                || memberEmail == null
                || memberEmail.trim().isEmpty()) {

            return null;
        }

        societyName =
                societyName.trim();

        memberEmail =
                memberEmail.trim();

        // ========================================================
        // RESIDENTS
        // ========================================================

        QuerySnapshot residents =
                db.collection("Residents")
                        .whereEqualTo(
                                "society",
                                societyName)
                        .get()
                        .get();

        for (DocumentSnapshot document
                : residents.getDocuments()) {

            String email =
                    getFirstAvailable(
                            document,
                            "email",
                            "residentEmail",
                            "userEmail");

            if (email.isEmpty()) {
                email =
                        document.getId();
            }

            if (email.equalsIgnoreCase(
                    memberEmail)) {

                String name =
                        getFirstAvailable(
                                document,
                                "name",
                                "residentName",
                                "ownerName");

                String flatNo =
                        getFirstAvailable(
                                document,
                                "flatNo",
                                "flatNumber");

                String role =
                        getString(
                                document,
                                "role");

                if (role.isEmpty()) {
                    role = "Resident";
                }

                return new ParkingMember(
                        email,
                        name,
                        flatNo,
                        role,
                        societyName);
            }
        }

        // ========================================================
        // OWNERS
        // ========================================================

        QuerySnapshot owners =
                db.collection("Owners")
                        .whereEqualTo(
                                "society",
                                societyName)
                        .get()
                        .get();

        for (DocumentSnapshot document
                : owners.getDocuments()) {

            String email =
                    getFirstAvailable(
                            document,
                            "email",
                            "ownerEmail",
                            "userEmail");

            if (email.isEmpty()) {
                email =
                        document.getId();
            }

            if (email.equalsIgnoreCase(
                    memberEmail)) {

                String name =
                        getFirstAvailable(
                                document,
                                "name",
                                "ownerName",
                                "residentName");

                String flatNo =
                        getFirstAvailable(
                                document,
                                "flatNo",
                                "flatNumber");

                String role =
                        getString(
                                document,
                                "role");

                if (role.isEmpty()) {
                    role = "Owner";
                }

                return new ParkingMember(
                        email,
                        name,
                        flatNo,
                        role,
                        societyName);
            }
        }

        return null;
    }

    // ============================================================
    // FIND MEMBER ACROSS SOCIETIES
    //
    // Used only to determine resident's actual society.
    // ============================================================

    private ParkingMember findMemberAcrossSociety(
            String memberEmail)
            throws Exception {

        if (memberEmail == null
                || memberEmail.trim().isEmpty()) {

            return null;
        }

        memberEmail =
                memberEmail.trim();

        QuerySnapshot residents =
                db.collection("Residents")
                        .get()
                        .get();

        for (DocumentSnapshot document
                : residents.getDocuments()) {

            String email =
                    getFirstAvailable(
                            document,
                            "email",
                            "residentEmail",
                            "userEmail");

            if (email.isEmpty()) {
                email =
                        document.getId();
            }

            if (email.equalsIgnoreCase(
                    memberEmail)) {

                String society =
                        getFirstAvailable(
                                document,
                                "society",
                                "societyName");

                String name =
                        getFirstAvailable(
                                document,
                                "name",
                                "residentName",
                                "ownerName");

                String flatNo =
                        getFirstAvailable(
                                document,
                                "flatNo",
                                "flatNumber");

                String role =
                        getString(
                                document,
                                "role");

                if (role.isEmpty()) {
                    role = "Resident";
                }

                return new ParkingMember(
                        email,
                        name,
                        flatNo,
                        role,
                        society);
            }
        }

        QuerySnapshot owners =
                db.collection("Owners")
                        .get()
                        .get();

        for (DocumentSnapshot document
                : owners.getDocuments()) {

            String email =
                    getFirstAvailable(
                            document,
                            "email",
                            "ownerEmail",
                            "userEmail");

            if (email.isEmpty()) {
                email =
                        document.getId();
            }

            if (email.equalsIgnoreCase(
                    memberEmail)) {

                String society =
                        getFirstAvailable(
                                document,
                                "society",
                                "societyName");

                String name =
                        getFirstAvailable(
                                document,
                                "name",
                                "ownerName",
                                "residentName");

                String flatNo =
                        getFirstAvailable(
                                document,
                                "flatNo",
                                "flatNumber");

                String role =
                        getString(
                                document,
                                "role");

                if (role.isEmpty()) {
                    role = "Owner";
                }

                return new ParkingMember(
                        email,
                        name,
                        flatNo,
                        role,
                        society);
            }
        }

        return null;
    }

    // ============================================================
    // GET SECRETARY NAME
    // ============================================================

    private String getSecretaryName(
            String secretaryEmail)
            throws Exception {

        if (secretaryEmail == null
                || secretaryEmail.trim().isEmpty()) {

            return "";
        }

        secretaryEmail =
                secretaryEmail.trim();

        DocumentSnapshot document =
                db.collection("Secretaries")
                        .document(secretaryEmail)
                        .get()
                        .get();

        if (!document.exists()) {
            return secretaryEmail;
        }

        String name =
                getFirstAvailable(
                        document,
                        "name",
                        "secretaryName",
                        "fullName");

        if (name.isEmpty()) {
            return secretaryEmail;
        }

        return name;
    }

    // ============================================================
    // VALIDATE VISITOR ASSIGNMENT INPUT
    // ============================================================

    private void validateVisitorAssignmentInput(
            String secretaryEmail,
            String societyName,
            String slotNumber,
            String visitorName,
            String vehicleNumber,
            String visitingResidentEmail) {

        if (secretaryEmail == null
                || secretaryEmail.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Secretary email cannot be empty.");
        }

        if (societyName == null
                || societyName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Society name cannot be empty.");
        }

        if (slotNumber == null
                || slotNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Visitor parking slot cannot be empty.");
        }

        if (visitorName == null
                || visitorName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Visitor name cannot be empty.");
        }

        if (vehicleNumber == null
                || vehicleNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vehicle number cannot be empty.");
        }

        if (visitingResidentEmail == null
                || visitingResidentEmail.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Visiting resident email cannot be empty.");
        }
    }

    // ============================================================
    // EXTRACT VISITOR NUMBER
    // ============================================================

    private int extractVisitorNumber(
            String slotNumber) {

        if (slotNumber == null) {
            return 0;
        }

        String value =
                slotNumber.trim()
                        .toUpperCase();

        if (!value.startsWith("V-")) {
            return 0;
        }

        try {

            return Integer.parseInt(
                    value.substring(2));

        } catch (NumberFormatException e) {

            return 0;
        }
    }

    // ============================================================
    // NORMALIZE SLOT TYPE
    // ============================================================

    private String normalizeSlotType(
            String slotType) {

        if (slotType == null
                || slotType.trim().isEmpty()) {

            return SLOT_TYPE_RESIDENT;
        }

        if (SLOT_TYPE_VISITOR.equalsIgnoreCase(
                slotType.trim())) {

            return SLOT_TYPE_VISITOR;
        }

        return SLOT_TYPE_RESIDENT;
    }

    // ============================================================
    // CHECK DOCUMENT SOCIETY
    // ============================================================

    private boolean belongsToSociety(
            DocumentSnapshot document,
            String societyName) {

        if (document == null
                || !document.exists()) {

            return false;
        }

        String documentSociety =
                getFirstAvailable(
                        document,
                        "society",
                        "societyName");

        // Legacy documents without society field
        // are considered valid because they are already
        // inside the society's ParkingSlots document.
        if (documentSociety.isEmpty()) {
            return true;
        }

        return documentSociety.equalsIgnoreCase(
                safe(societyName));
    }

    // ============================================================
    // GET SOCIETY FROM PARKING REFERENCE
    // ============================================================

    private String getSocietyFromReference(
            DocumentReference societyRef)
            throws Exception {

        if (societyRef == null) {
            return "";
        }

        DocumentSnapshot snapshot =
                societyRef.get()
                        .get();

        return getFirstAvailable(
                snapshot,
                "society",
                "societyName");
    }

    // ============================================================
    // CREATE LAST UPDATED MAP
    // ============================================================

    private Map<String, Object> createLastUpdatedMap() {

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "lastUpdatedAt",
                Timestamp.now());

        return data;
    }

    // ============================================================
    // GET STRING
    // ============================================================

    private String getString(
            DocumentSnapshot document,
            String field) {

        if (document == null
                || field == null) {

            return "";
        }

        String value =
                document.getString(field);

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    // ============================================================
    // GET FIRST AVAILABLE
    // ============================================================

    private String getFirstAvailable(
            DocumentSnapshot document,
            String... fields) {

        if (document == null
                || fields == null) {

            return "";
        }

        for (String field : fields) {

            String value =
                    getString(
                            document,
                            field);

            if (!value.isEmpty()) {
                return value;
            }
        }

        return "";
    }

    // ============================================================
    // SAFE STRING
    // ============================================================

    private String safe(
            String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }
}