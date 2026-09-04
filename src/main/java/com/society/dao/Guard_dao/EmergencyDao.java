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
import com.society.model.Guard_model.EmergencyModel;

public class EmergencyDao {

    private final Firestore db;

    private static final String COLLECTION =
            "emergency_alerts";

    private static final String ALERTS_SUBCOLLECTION =
            "alerts";


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EmergencyDao() {

        db = FirebaseConfig.getFirestore();
    }


    // =====================================================
    // CREATE EMERGENCY
    // STRUCTURE:
    //
    // emergency_alerts
    //      └── email
    //            └── alerts
    //                  └── emergencyId
    // =====================================================

    public EmergencyModel createEmergency(
            EmergencyModel emergency) {

        try {

            if (db == null) {

                System.out.println(
                        "ERROR: Firestore is null."
                );

                return null;
            }


            if (emergency == null) {

                System.out.println(
                        "ERROR: Emergency is null."
                );

                return null;
            }


            if (emergency.getEmail() == null ||
                    emergency.getEmail()
                            .trim()
                            .isEmpty()) {

                System.out.println(
                        "ERROR: Email is missing."
                );

                return null;
            }


            if (emergency.getSociety() == null ||
                    emergency.getSociety()
                            .trim()
                            .isEmpty()) {

                System.out.println(
                        "ERROR: Society name is missing."
                );

                return null;
            }


            // =================================================
            // NORMALIZE EMAIL
            // =================================================

            String email =
                    emergency.getEmail()
                            .trim()
                            .toLowerCase();

            emergency.setEmail(email);


            // =================================================
            // NORMALIZE SOCIETY
            // IMPORTANT FIX
            // =================================================

            String normalizedSociety =
                    emergency.getSociety()
                            .trim();


            // =================================================
            // EMAIL DOCUMENT
            // =================================================

            DocumentReference emailDocument =
                    db.collection(COLLECTION)
                            .document(email);


            // =================================================
            // CREATE / UPDATE EMAIL PARENT DOCUMENT
            // =================================================

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
            // UNIQUE ALERT DOCUMENT
            // =================================================

            String emergencyId =
                    emailDocument
                            .collection(
                                    ALERTS_SUBCOLLECTION
                            )
                            .document()
                            .getId();

            emergency.setEmergencyId(
                    emergencyId
            );


            // =================================================
            // SAVE EMERGENCY
            // =================================================

            Map<String, Object> alertData =
                    new HashMap<>();

            alertData.put(
                    "emergencyId",
                    emergency.getEmergencyId()
            );

            alertData.put(
                    "email",
                    email
            );

            alertData.put(
                    "society",
                    normalizedSociety
            );

            alertData.put(
                    "type",
                    emergency.getType()
            );

            alertData.put(
                    "location",
                    emergency.getLocation()
            );

            alertData.put(
                    "details",
                    emergency.getDetails() == null
                            ? ""
                            : emergency.getDetails()
            );

            alertData.put(
                    "time",
                    emergency.getTime()
            );

            alertData.put(
                    "status",
                    emergency.getStatus()
            );

            alertData.put(
                    "createdAt",
                    emergency.getCreatedAt()
            );

            emailDocument
                    .collection(
                            ALERTS_SUBCOLLECTION
                    )
                    .document(emergencyId)
                    .set(alertData)
                    .get();


            // =================================================
            // CONSOLE LOG
            // =================================================

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "EMERGENCY SAVED TO FIRESTORE"
            );

            System.out.println(
                    "Email Document: "
                            + email
            );

            System.out.println(
                    "Society Name: "
                            + normalizedSociety
            );

            System.out.println(
                    "Emergency ID: "
                            + emergencyId
            );

            System.out.println(
                    "Type: "
                            + emergency.getType()
            );

            System.out.println(
                    "Location: "
                            + emergency.getLocation()
            );

            System.out.println(
                    "Status: "
                            + emergency.getStatus()
            );

            System.out.println(
                    "======================================"
            );


            return emergency;


        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to save emergency."
            );

            e.printStackTrace();

            return null;
        }
    }


    // =====================================================
    // GET ALL EMERGENCIES FOR ONE EMAIL
    // =====================================================

    public List<EmergencyModel>
    getEmergenciesByEmail(
            String email) {

        List<EmergencyModel> list =
                new ArrayList<>();


        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return list;
            }


            String normalizedEmail =
                    email.trim()
                            .toLowerCase();


            QuerySnapshot snapshot =
                    db.collection(COLLECTION)
                            .document(normalizedEmail)
                            .collection(
                                    ALERTS_SUBCOLLECTION
                            )
                            .get()
                            .get();


            for (QueryDocumentSnapshot document :
                    snapshot.getDocuments()) {

                EmergencyModel emergency =
                        document.toObject(
                                EmergencyModel.class
                        );


                if (emergency != null) {

                    emergency.setEmergencyId(
                            document.getId()
                    );

                    list.add(
                            emergency
                    );
                }
            }


        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to fetch emergencies by email."
            );

            e.printStackTrace();
        }


        return list;
    }


    // =====================================================
    // GET ACTIVE EMERGENCIES FOR ONE EMAIL
    // =====================================================

    public List<EmergencyModel>
    getActiveEmergenciesByEmail(
            String email) {

        List<EmergencyModel> list =
                new ArrayList<>();


        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return list;
            }


            String normalizedEmail =
                    email.trim()
                            .toLowerCase();


            QuerySnapshot snapshot =
                    db.collection(COLLECTION)
                            .document(normalizedEmail)
                            .collection(
                                    ALERTS_SUBCOLLECTION
                            )
                            .whereEqualTo(
                                    "status",
                                    "ACTIVE"
                            )
                            .get()
                            .get();


            for (QueryDocumentSnapshot document :
                    snapshot.getDocuments()) {

                EmergencyModel emergency =
                        document.toObject(
                                EmergencyModel.class
                        );


                if (emergency != null) {

                    emergency.setEmergencyId(
                            document.getId()
                    );

                    list.add(
                            emergency
                    );
                }
            }


        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to fetch active emergencies for email."
            );

            e.printStackTrace();
        }


        return list;
    }


    // =====================================================
    // GET ACTIVE EMERGENCIES
    //
    // NOTE:
    // This method is kept for compatibility.
    // It currently checks all email documents.
    // =====================================================

    public List<EmergencyModel>
    getActiveEmergencies() {

        List<EmergencyModel> list =
                new ArrayList<>();


        try {

            Iterable<DocumentReference> emailDocuments =
                    db.collection(
                            COLLECTION
                    ).listDocuments();


            for (DocumentReference emailDocument :
                    emailDocuments) {

                QuerySnapshot snapshot =
                        emailDocument
                                .collection(
                                        ALERTS_SUBCOLLECTION
                                )
                                .whereEqualTo(
                                        "status",
                                        "ACTIVE"
                                )
                                .get()
                                .get();


                for (QueryDocumentSnapshot document :
                        snapshot.getDocuments()) {

                    EmergencyModel emergency =
                            document.toObject(
                                    EmergencyModel.class
                            );


                    if (emergency != null) {

                        emergency.setEmergencyId(
                                document.getId()
                        );

                        list.add(
                                emergency
                        );
                    }
                }
            }


        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to fetch active emergencies."
            );

            e.printStackTrace();
        }


        return list;
    }


    // =====================================================
    // GET ALL EMERGENCIES
    // =====================================================

    public List<EmergencyModel>
    getAllEmergencies() {

        List<EmergencyModel> list =
                new ArrayList<>();


        try {

            Iterable<DocumentReference> emailDocuments =
                    db.collection(
                            COLLECTION
                    ).listDocuments();


            for (DocumentReference emailDocument :
                    emailDocuments) {

                QuerySnapshot snapshot =
                        emailDocument
                                .collection(
                                        ALERTS_SUBCOLLECTION
                                )
                                .get()
                                .get();


                for (QueryDocumentSnapshot document :
                        snapshot.getDocuments()) {

                    EmergencyModel emergency =
                            document.toObject(
                                    EmergencyModel.class
                            );


                    if (emergency != null) {

                        emergency.setEmergencyId(
                                document.getId()
                        );

                        list.add(
                                emergency
                        );
                    }
                }
            }


        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to fetch emergencies."
            );

            e.printStackTrace();
        }


        return list;
    }


    // =====================================================
    // GET BY SOCIETY
    //
    // Kept for current compatibility.
    // Since society is still stored inside each
    // emergency document, we scan the email documents.
    // =====================================================

    public List<EmergencyModel>
    getEmergenciesBySociety(
            String society) {

        List<EmergencyModel> list =
                new ArrayList<>();


        try {

            if (society == null ||
                    society.trim().isEmpty()) {

                return list;
            }


            String normalizedSociety =
                    society.trim();


            Iterable<DocumentReference> emailDocuments =
                    db.collection(
                            COLLECTION
                    ).listDocuments();


            for (DocumentReference emailDocument :
                    emailDocuments) {

                QuerySnapshot snapshot =
                        emailDocument
                                .collection(
                                        ALERTS_SUBCOLLECTION
                                )
                                .whereEqualTo(
                                        "society",
                                        normalizedSociety
                                )
                                .get()
                                .get();


                for (QueryDocumentSnapshot document :
                        snapshot.getDocuments()) {

                    EmergencyModel emergency =
                            document.toObject(
                                    EmergencyModel.class
                            );


                    if (emergency != null) {

                        emergency.setEmergencyId(
                                document.getId()
                        );

                        list.add(
                                emergency
                        );
                    }
                }
            }


        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to fetch emergencies by society."
            );

            e.printStackTrace();
        }


        return list;
    }


    // =====================================================
    // RESOLVE EMERGENCY
    //
    // EMAIL + EMERGENCY ID
    // =====================================================

    public boolean resolveEmergency(
            String email,
            String emergencyId) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Email is missing."
                );

                return false;
            }


            if (emergencyId == null ||
                    emergencyId.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Emergency ID is missing."
                );

                return false;
            }


            String normalizedEmail =
                    email.trim()
                            .toLowerCase();


            db.collection(COLLECTION)
                    .document(normalizedEmail)
                    .collection(
                            ALERTS_SUBCOLLECTION
                    )
                    .document(emergencyId)
                    .update(
                            "status",
                            "RESOLVED"
                    )
                    .get();


            System.out.println(
                    "Emergency resolved: "
                            + emergencyId
            );


            return true;


        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to resolve emergency."
            );

            e.printStackTrace();

            return false;
        }
    }
}