package com.society.dao.Resident_dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.model.Resident_model.VisitorModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitorDAO {

    private final Firestore firestore;

    private static final String COLLECTION = "visitors";
    private static final String SUB_COLLECTION = "visitor_records";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public VisitorDAO(Firestore firestore) {
        this.firestore = firestore;
    }

    // =====================================================
    // GET RESIDENT SOCIETY
    //
    // Residents/{residentEmail}
    //
    // First checks:
    // society
    //
    // Then fallback:
    // societyName
    // =====================================================

    private String getResidentSociety(
            String residentEmail) throws Exception {

        if (residentEmail == null ||
                residentEmail.trim().isEmpty()) {

            return "";
        }

        String email =
                residentEmail.trim().toLowerCase();

        DocumentSnapshot residentDocument =
                firestore.collection("Residents")
                        .document(email)
                        .get()
                        .get();

        if (!residentDocument.exists()) {
            return "";
        }

        String society =
                residentDocument.getString("society");

        if (society != null &&
                !society.trim().isEmpty()) {

            return society.trim();
        }

        String societyName =
                residentDocument.getString("societyName");

        if (societyName != null &&
                !societyName.trim().isEmpty()) {

            return societyName.trim();
        }

        return "";
    }

    // =====================================================
    // SAVE VISITOR
    // =====================================================

    public void saveVisitor(
            String residentEmail,
            VisitorModel visitor) throws Exception {

        // =================================================
        // VALIDATION
        // =================================================

        if (residentEmail == null ||
                residentEmail.trim().isEmpty()) {

            throw new Exception(
                    "Resident email is missing.");
        }

        if (visitor == null) {

            throw new Exception(
                    "Visitor data is missing.");
        }

        String email =
                residentEmail.trim().toLowerCase();

        if (visitor.getId() == null ||
                visitor.getId().trim().isEmpty()) {

            throw new Exception(
                    "Visitor ID is missing.");
        }

        // =================================================
        // GET SOCIETY
        // =================================================

        String society =
                getResidentSociety(email);

        // =================================================
        // PARENT DOCUMENT
        //
        // visitors/{residentEmail}
        // =================================================

        Map<String, Object> parentData =
                new HashMap<>();

        parentData.put(
                "email",
                email
        );

        parentData.put(
                "society",
                society
        );

        firestore.collection(COLLECTION)
                .document(email)
                .set(parentData)
                .get();

        // =================================================
        // VISITOR RECORD
        //
        // visitors/{email}/visitor_records/{id}
        // =================================================

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "id",
                visitor.getId()
        );

        data.put(
                "visitorName",
                visitor.getVisitorName()
        );

        data.put(
                "phoneNumber",
                visitor.getPhoneNumber()
        );

        data.put(
                "purpose",
                visitor.getPurpose()
        );

        data.put(
                "visitDate",
                visitor.getVisitDate()
        );

        data.put(
                "visitTime",
                visitor.getVisitTime()
        );

        data.put(
                "flatNumber",
                visitor.getFlatNumber()
        );

        data.put(
                "gate",
                visitor.getGate()
        );

        data.put(
                "vehicleNumber",
                visitor.getVehicleNumber()
        );

        data.put(
                "status",
                visitor.getStatus()
        );

        data.put(
                "qrToken",
                visitor.getQrToken()
        );

        data.put(
                "used",
                visitor.isUsed()
        );

        // =================================================
        // SOCIETY
        // =================================================

        data.put(
                "society",
                society
        );

        // =================================================
        // CREATED AT
        //
        // Firestore Timestamp
        // =================================================

        data.put(
                "createdAt",
                FieldValue.serverTimestamp()
        );

        // =================================================
        // SAVE
        // =================================================

        firestore.collection(COLLECTION)
                .document(email)
                .collection(SUB_COLLECTION)
                .document(visitor.getId())
                .set(data)
                .get();
    }

    // =====================================================
    // GET VISITOR BY ID
    // =====================================================

    public VisitorModel getVisitorById(
            String residentEmail,
            String id) throws Exception {

        if (residentEmail == null ||
                residentEmail.trim().isEmpty()) {

            throw new Exception(
                    "Resident email is missing.");
        }

        if (id == null ||
                id.trim().isEmpty()) {

            throw new Exception(
                    "Visitor ID is missing.");
        }

        String email =
                residentEmail.trim().toLowerCase();

        DocumentSnapshot document =
                firestore.collection(COLLECTION)
                        .document(email)
                        .collection(SUB_COLLECTION)
                        .document(id.trim())
                        .get()
                        .get();

        if (!document.exists()) {
            return null;
        }

        VisitorModel visitor =
                document.toObject(
                        VisitorModel.class
                );

        if (visitor != null) {
            visitor.setId(document.getId());

            // Fallback society
            if (visitor.getSociety() == null ||
                    visitor.getSociety().trim().isEmpty()) {

                visitor.setSociety(
                        getResidentSociety(email)
                );
            }
        }

        return visitor;
    }

    // =====================================================
    // GET VISITOR BY QR TOKEN
    // =====================================================

    public VisitorModel getVisitorByQrToken(
            String qrToken) throws Exception {

        if (qrToken == null ||
                qrToken.trim().isEmpty()) {

            throw new Exception(
                    "QR token is missing.");
        }

        String token =
                qrToken.trim();

        QuerySnapshot residentsSnapshot =
                firestore.collection(COLLECTION)
                        .get()
                        .get();

        for (DocumentSnapshot residentDocument :
                residentsSnapshot.getDocuments()) {

            String residentEmail =
                    residentDocument.getId();

            QuerySnapshot visitorSnapshot =
                    firestore.collection(COLLECTION)
                            .document(residentEmail)
                            .collection(SUB_COLLECTION)
                            .whereEqualTo(
                                    "qrToken",
                                    token
                            )
                            .limit(1)
                            .get()
                            .get();

            if (!visitorSnapshot.isEmpty()) {

                DocumentSnapshot visitorDocument =
                        visitorSnapshot
                                .getDocuments()
                                .get(0);

                VisitorModel visitor =
                        visitorDocument.toObject(
                                VisitorModel.class
                        );

                if (visitor != null) {

                    visitor.setId(
                            visitorDocument.getId()
                    );

                    if (visitor.getSociety() == null ||
                            visitor.getSociety()
                                    .trim()
                                    .isEmpty()) {

                        visitor.setSociety(
                                getResidentSociety(
                                        residentEmail
                                )
                        );
                    }
                }

                return visitor;
            }
        }

        return null;
    }

    // =====================================================
    // GET VISITORS BY DATE
    //
    // Firestore:
    // visitDate == yyyy-MM-dd
    // =====================================================

    public List<VisitorModel> getVisitorsByDate(
            String residentEmail,
            String date) throws Exception {

        if (residentEmail == null ||
                residentEmail.trim().isEmpty()) {

            throw new Exception(
                    "Resident email is missing.");
        }

        if (date == null ||
                date.trim().isEmpty()) {

            throw new Exception(
                    "Date is missing.");
        }

        String email =
                residentEmail.trim().toLowerCase();

        String requiredDate =
                date.trim();

        System.out.println(
                "===================================="
        );

        System.out.println(
                "VISITOR FETCH"
        );

        System.out.println(
                "Resident Email : " + email
        );

        System.out.println(
                "Required Date  : " + requiredDate
        );

        QuerySnapshot snapshot =
                firestore.collection(COLLECTION)
                        .document(email)
                        .collection(SUB_COLLECTION)
                        .whereEqualTo(
                                "visitDate",
                                requiredDate
                        )
                        .get()
                        .get();

        System.out.println(
                "Documents Found: "
                        + snapshot.size()
        );

        List<VisitorModel> visitors =
                new ArrayList<>();

        for (DocumentSnapshot document :
                snapshot.getDocuments()) {

            VisitorModel visitor =
                    document.toObject(
                            VisitorModel.class
                    );

            if (visitor != null) {

                visitor.setId(
                        document.getId()
                );

                // Society fallback
                if (visitor.getSociety() == null ||
                        visitor.getSociety().trim().isEmpty()) {

                    visitor.setSociety(
                            getResidentSociety(email)
                    );
                }

                visitors.add(visitor);

                System.out.println(
                        "Fetched Visitor : "
                                + visitor.getVisitorName()
                );

                System.out.println(
                        "Visit Date      : "
                                + visitor.getVisitDate()
                );

                System.out.println(
                        "Society         : "
                                + visitor.getSociety()
                );

                System.out.println(
                        "Created At      : "
                                + visitor.getCreatedAt()
                );
            }
        }

        System.out.println(
                "===================================="
        );

        return visitors;
    }

    // =====================================================
    // GET TODAY'S INVITED VISITORS
    // =====================================================

    public List<VisitorModel> getTodaysInvitedVisitors(
            String residentEmail) throws Exception {

        if (residentEmail == null ||
                residentEmail.trim().isEmpty()) {

            throw new Exception(
                    "Resident email is missing.");
        }

        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd");

        // Always calculate today's date using India time.
        sdf.setTimeZone(
                java.util.TimeZone.getTimeZone("Asia/Kolkata")
        );

        String today = sdf.format(new Date());

        System.out.println("Today's Date (India): " + today);

        return getVisitorsByDate(
                residentEmail.trim().toLowerCase(),
                today
        );
    }

    // =====================================================
    // MARK VISITOR AS USED
    // =====================================================

    public void markVisitorAsUsed(
            String residentEmail,
            String id) throws Exception {

        if (residentEmail == null ||
                residentEmail.trim().isEmpty()) {

            throw new Exception(
                    "Resident email is missing.");
        }

        if (id == null ||
                id.trim().isEmpty()) {

            throw new Exception(
                    "Visitor ID is missing.");
        }

        firestore.collection(COLLECTION)
                .document(
                        residentEmail
                                .trim()
                                .toLowerCase()
                )
                .collection(SUB_COLLECTION)
                .document(id.trim())
                .update(
                        "used",
                        true,
                        "status",
                        "VISITED"
                )
                .get();
    }
}