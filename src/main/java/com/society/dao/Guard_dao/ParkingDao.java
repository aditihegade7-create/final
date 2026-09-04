package com.society.dao.Guard_dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;

import com.society.config.FirebaseConfig;
import com.society.model.Guard_model.ParkingModel;

public class ParkingDao {

    private final Firestore db;

    private static final String COLLECTION = "parking";

    private static final String PARKING_RECORDS =
            "parking_records";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ParkingDao() {
        db = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // NORMALIZE EMAIL
    // =====================================================

    private String normalizeEmail(String email) {

        if (email == null) {
            return null;
        }

        String normalized =
                email.trim().toLowerCase();

        return normalized.isEmpty()
                ? null
                : normalized;
    }

    // =====================================================
    // NORMALIZE SOCIETY
    // =====================================================

    private String normalizeSociety(String society) {

        if (society == null) {
            return null;
        }

        String normalized =
                society.trim();

        return normalized.isEmpty()
                ? null
                : normalized;
    }

    // =====================================================
    // SAVE / ALLOT PARKING
    //
    // STRUCTURE:
    //
    // parking/{email}/parking_records/{parkingId}
    // =====================================================

    public boolean saveParking(
            ParkingModel parking,
            String society) {

        try {

            if (db == null) {

                System.out.println(
                        "ERROR: Firestore is null."
                );

                return false;
            }

            if (parking == null) {

                System.out.println(
                        "ERROR: Parking object is null."
                );

                return false;
            }

            // =================================================
            // VALIDATE SOCIETY
            // =================================================

            String normalizedSociety =
                    normalizeSociety(society);

            if (normalizedSociety == null) {

                System.out.println(
                        "ERROR: Society name is missing."
                );

                return false;
            }

            // =================================================
            // VALIDATE EMAIL
            // =================================================

            String email =
                    normalizeEmail(
                            parking.getEmail()
                    );

            if (email == null) {

                System.out.println(
                        "ERROR: Parking email is missing."
                );

                return false;
            }

            // =================================================
            // VALIDATE SPOT
            // =================================================

            String spotNumber =
                    parking.getSpotNumber() == null
                            ? null
                            : parking.getSpotNumber().trim();

            if (spotNumber == null ||
                    spotNumber.isEmpty()) {

                System.out.println(
                        "ERROR: Parking spot is missing."
                );

                return false;
            }

            parking.setEmail(email);
            parking.setSociety(normalizedSociety);
            parking.setSpotNumber(spotNumber);

            // =================================================
            // EMAIL PARENT DOCUMENT
            // =================================================

            DocumentReference emailDocument =
                    db.collection(COLLECTION)
                            .document(email);

            Map<String, Object> emailData =
                    new HashMap<>();

            emailData.put(
                    "email",
                    email
            );

            emailData.put(
                    "society",
                    normalizedSociety
            );

            emailDocument
                    .set(
                            emailData,
                            SetOptions.merge()
                    )
                    .get();

            // =================================================
            // FINAL FIRESTORE OCCUPANCY CHECK
            //
            // Prevents two records from occupying the
            // same spot for the same guard.
            // =================================================

            QuerySnapshot existingSnapshot =
                    emailDocument
                            .collection(PARKING_RECORDS)
                            .whereEqualTo(
                                    "spotNumber",
                                    spotNumber
                            )
                            .whereEqualTo(
                                    "occupied",
                                    true
                            )
                            .limit(1)
                            .get()
                            .get();

            if (!existingSnapshot.isEmpty()) {

                System.out.println(
                        "ERROR: Parking spot already occupied."
                );

                return false;
            }

            // =================================================
            // CREATE PARKING DOCUMENT
            // =================================================

            DocumentReference parkingDocument =
                    emailDocument
                            .collection(
                                    PARKING_RECORDS
                            )
                            .document();

            String parkingId =
                    parkingDocument.getId();

            parking.setParkingId(parkingId);

            // =================================================
            // PARKING DATA
            // =================================================

            Map<String, Object> parkingData =
                    new HashMap<>();

            parkingData.put(
                    "parkingId",
                    parkingId
            );

            parkingData.put(
                    "society",
                    normalizedSociety
            );

            parkingData.put(
                    "email",
                    email
            );

            parkingData.put(
                    "occupied",
                    true
            );

            parkingData.put(
                    "ownerName",
                    parking.getOwnerName()
            );

            parkingData.put(
                    "spotNumber",
                    spotNumber
            );

            parkingData.put(
                    "vehicleNumber",
                    parking.getVehicleNumber()
            );

            parkingData.put(
                    "vehicleType",
                    parking.getVehicleType()
            );

            // =================================================
            // SAVE
            // =================================================

            parkingDocument
                    .set(parkingData)
                    .get();

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "PARKING SAVED TO FIRESTORE"
            );

            System.out.println(
                    "Email: " + email
            );

            System.out.println(
                    "Society: " + normalizedSociety
            );

            System.out.println(
                    "Spot: " + spotNumber
            );

            System.out.println(
                    "Parking ID: " + parkingId
            );

            System.out.println(
                    "======================================"
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to save parking."
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET ALL PARKING FOR GUARD
    // =====================================================

   // =====================================================
// GET ALL PARKING FOR A GUARD
//
// STRUCTURE:
//
// parking/{email}/parking_records/{parkingId}
//
// No collectionGroup query.
// No Firestore index required.
// =====================================================

public List<ParkingModel> getAllParking(
        String email) {

    List<ParkingModel> parkingList =
            new ArrayList<>();

    try {

        if (db == null) {

            System.out.println(
                    "ERROR: Firestore is null."
            );

            return parkingList;
        }

        if (email == null ||
                email.trim().isEmpty()) {

            return parkingList;
        }

        String normalizedEmail =
                email.trim()
                        .toLowerCase();

        // =================================================
        // DIRECTLY ACCESS THIS GUARD'S PARKING RECORDS
        //
        // parking/{email}/parking_records
        // =================================================

        QuerySnapshot snapshot =
                db.collection(COLLECTION)
                        .document(normalizedEmail)
                        .collection(PARKING_RECORDS)
                        .get()
                        .get();

        // =================================================
        // CONVERT DOCUMENTS
        // =================================================

        for (QueryDocumentSnapshot document :
                snapshot.getDocuments()) {

            ParkingModel parking =
                    document.toObject(
                            ParkingModel.class
                    );

            if (parking == null) {
                continue;
            }

            parking.setParkingId(
                    document.getId()
            );

            parkingList.add(
                    parking
            );
        }

        System.out.println(
                "======================================"
        );

        System.out.println(
                "PARKING LOADED"
        );

        System.out.println(
                "Guard Email: "
                        + normalizedEmail
        );

        System.out.println(
                "Records: "
                        + parkingList.size()
        );

        System.out.println(
                "======================================"
        );

    } catch (Exception e) {

        System.out.println(
                "ERROR: Unable to fetch parking for guard."
        );

        e.printStackTrace();
    }

    return parkingList;
}
    // =====================================================
    // GET PARKING BY SPOT FOR GUARD
    //
    // IMPORTANT:
    // Only OCCUPIED records are searched.
    //
    // This prevents an old released record from being
    // returned instead of the currently occupied record.
    // =====================================================

    public ParkingModel getParkingBySpot(
            String spotNumber,
            String email) {

        if (spotNumber == null ||
                spotNumber.trim().isEmpty()) {

            return null;
        }

        String normalizedEmail =
                normalizeEmail(email);

        if (normalizedEmail == null) {
            return null;
        }

        try {

            String normalizedSpot =
                    spotNumber.trim();

            // =================================================
            // NEW NESTED STRUCTURE
            // =================================================

            QuerySnapshot snapshot =
                    db.collectionGroup(
                            PARKING_RECORDS
                    )
                    .whereEqualTo(
                            "spotNumber",
                            normalizedSpot
                    )
                    .whereEqualTo(
                            "email",
                            normalizedEmail
                    )
                    .whereEqualTo(
                            "occupied",
                            true
                    )
                    .limit(1)
                    .get()
                    .get();

            if (!snapshot.isEmpty()) {

                QueryDocumentSnapshot document =
                        snapshot.getDocuments().get(0);

                ParkingModel parking =
                        document.toObject(
                                ParkingModel.class
                        );

                if (parking != null) {

                    parking.setParkingId(
                            document.getId()
                    );
                }

                return parking;
            }

            return null;

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to find occupied parking spot."
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // RELEASE PARKING
    // =====================================================

    public boolean releaseParking(
            String parkingId) {

        try {

            if (db == null) {

                System.out.println(
                        "ERROR: Firestore is null."
                );

                return false;
            }

            if (parkingId == null ||
                    parkingId.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Parking ID is missing."
                );

                return false;
            }

            String normalizedParkingId =
                    parkingId.trim();

            // =================================================
            // FIND ACTIVE NESTED RECORD
            // =================================================

            QuerySnapshot snapshot =
                    db.collectionGroup(
                            PARKING_RECORDS
                    )
                    .whereEqualTo(
                            "parkingId",
                            normalizedParkingId
                    )
                    .whereEqualTo(
                            "occupied",
                            true
                    )
                    .limit(1)
                    .get()
                    .get();

            if (snapshot.isEmpty()) {

                System.out.println(
                        "ERROR: Active parking record not found."
                );

                return false;
            }

            QueryDocumentSnapshot document =
                    snapshot.getDocuments().get(0);

            // =================================================
            // RELEASE
            // =================================================

            document.getReference()
                    .update(
                            "occupied",
                            false,
                            "vehicleNumber",
                            "",
                            "ownerName",
                            "",
                            "vehicleType",
                            ""
                    )
                    .get();

            System.out.println(
                    "Parking released: "
                            + normalizedParkingId
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to release parking."
            );

            e.printStackTrace();

            return false;
        }
    }
}