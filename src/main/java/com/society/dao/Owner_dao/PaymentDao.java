package com.society.dao.Owner_dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.model.Owner_model.PaymentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.Set;

public class PaymentDao {

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore db;


    // =========================================================
    // COLLECTION
    // =========================================================

    private static final String COLLECTION =
            "Payments";


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PaymentDao() {

        db = FirebaseConfig.getFirestore();
    }


    // =========================================================
    // ADD PAYMENT
    //
    // Firestore document ID = AUTO ID
    // =========================================================

    public boolean addPayment(
            PaymentModel payment) {

        try {

            // -------------------------------------------------
            // VALIDATION
            // -------------------------------------------------

            if (payment == null) {

                return false;
            }


            if (payment.getOwnerEmail() == null ||
                    payment.getOwnerEmail()
                            .trim()
                            .isEmpty()) {

                return false;
            }


            // -------------------------------------------------
            // OWNER EMAIL
            // -------------------------------------------------

            String ownerEmail =
                    payment.getOwnerEmail()
                            .trim()
                            .toLowerCase();


            // -------------------------------------------------
            // CREATE DATA
            // -------------------------------------------------

            Map<String, Object> data =
                    new HashMap<>();


            data.put(
                    "ownerEmail",
                    ownerEmail
            );


            data.put(
                    "tenantEmail",
                    safeString(
                            payment.getTenantEmail()
                    )
            );


            data.put(
                    "date",
                    safeString(
                            payment.getDate()
                    )
            );


            data.put(
                    "tenant",
                    safeString(
                            payment.getTenant()
                    )
            );


            data.put(
                    "flat",
                    safeString(
                            payment.getFlat()
                    )
            );


            data.put(
                    "paymentFor",
                    safeString(
                            payment.getPaymentFor()
                    )
            );


            data.put(
                    "amount",
                    safeString(
                            payment.getAmount()
                    )
            );


            data.put(
                    "status",
                    safeString(
                            payment.getStatus()
                    )
            );


            // -------------------------------------------------
            // AUTO-GENERATED DOCUMENT ID
            // -------------------------------------------------

            db.collection(COLLECTION)
                    .document()
                    .set(data)
                    .get();


            System.out.println(
                    "Payment added successfully."
            );


            return true;


        } catch (Exception e) {

            System.out.println(
                    "Error adding payment: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // GET PAYMENTS BY OWNER EMAIL
    // =========================================================

    public List<PaymentModel> getOwnerPayments(
            String ownerEmail) {

        List<PaymentModel> payments =
                new ArrayList<>();

        try {

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty()) {
                return payments;
            }

            String normalizedOwnerEmail =
                    ownerEmail.trim().toLowerCase();

            // =================================================
            // 1. LOAD NORMAL PAYMENTS
            // =================================================

            QuerySnapshot paymentSnapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "ownerEmail",
                                    normalizedOwnerEmail
                            )
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    paymentSnapshot.getDocuments()) {

                PaymentModel payment =
                        new PaymentModel();

                payment.setPaymentId(
                        document.getId()
                );

                payment.setOwnerEmail(
                        safeString(
                                document.getString("ownerEmail")
                        )
                );

                payment.setTenantEmail(
                        safeString(
                                document.getString("tenantEmail")
                        )
                );

                payment.setDate(
                        safeString(
                                document.getString("date")
                        )
                );

                payment.setTenant(
                        safeString(
                                document.getString("tenant")
                        )
                );

                payment.setFlat(
                        safeString(
                                document.getString("flat")
                        )
                );

                payment.setPaymentFor(
                        safeString(
                                document.getString("paymentFor")
                        )
                );

                payment.setAmount(
                        safeString(
                                document.getString("amount")
                        )
                );

                payment.setStatus(
                        normalizeStatus(
                                document.getString("status")
                        )
                );

                payments.add(payment);
            }

            // =================================================
            // 2. FIND ALL RESIDENTS BELONGING TO OWNER
            //
            // Includes Tenant and Tenant Resident records because
            // both are linked through ownerEmail.
            // =================================================

            Set<String> residentEmails =
                    new LinkedHashSet<>();

            Map<String, DocumentSnapshot> residentDocuments =
                    new HashMap<>();

            residentEmails.add(normalizedOwnerEmail);

            QuerySnapshot residentSnapshot =
                    db.collection("Residents")
                            .whereEqualTo(
                                    "ownerEmail",
                                    normalizedOwnerEmail
                            )
                            .get()
                            .get();

            for (DocumentSnapshot resident :
                    residentSnapshot.getDocuments()) {

                String residentEmail =
                        resident.getString("email");

                if (residentEmail == null ||
                        residentEmail.trim().isEmpty()) {
                    residentEmail = resident.getId();
                }

                if (residentEmail != null &&
                        !residentEmail.trim().isEmpty()) {

                    String normalizedResidentEmail =
                            residentEmail.trim().toLowerCase();

                    residentEmails.add(
                            normalizedResidentEmail
                    );

                    residentDocuments.put(
                            normalizedResidentEmail,
                            resident
                    );
                }
            }

            System.out.println(
                    "Maintenance owners/residents searched: "
                            + residentEmails.size()
            );

            // =================================================
            // 3. LOAD MAINTENANCE RECORDS
            //
            // Firestore structure:
            // Maintenance/{email}/records/{maintenanceId}
            //
            // We intentionally search only the owner and residents
            // belonging to that owner.
            // =================================================

            int maintenanceCount = 0;

            for (String residentEmail : residentEmails) {

                QuerySnapshot maintenanceSnapshot =
                        db.collection("Maintenance")
                                .document(residentEmail)
                                .collection("records")
                                .get()
                                .get();

                DocumentSnapshot resident =
                        residentDocuments.get(residentEmail);

                for (DocumentSnapshot document :
                        maintenanceSnapshot.getDocuments()) {

                    PaymentModel payment =
                            new PaymentModel();

                    payment.setPaymentId(
                            "maintenance-"
                                    + residentEmail
                                    + "-"
                                    + document.getId()
                    );

                    payment.setOwnerEmail(
                            normalizedOwnerEmail
                    );

                    String recordTenantEmail =
                            firstNonEmpty(
                                    document.getString("tenantEmail"),
                                    document.getString("email"),
                                    residentEmail
                            );

                    payment.setTenantEmail(
                            recordTenantEmail
                    );

                    payment.setDate(
                            safeString(
                                    document.getString("date")
                            )
                    );

                    String tenantName =
                            firstNonEmpty(
                                    document.getString("tenant"),
                                    document.getString("name"),
                                    resident == null
                                            ? ""
                                            : resident.getString("name")
                            );

                    String flat =
                            firstNonEmpty(
                                    document.getString("flat"),
                                    resident == null
                                            ? ""
                                            : resident.getString("flat")
                            );

                    payment.setTenant(tenantName);
                    payment.setFlat(flat);
                    payment.setPaymentFor("Maintenance");

                    payment.setAmount(
                            safeString(
                                    document.getString("amount")
                            )
                    );

                    payment.setStatus(
                            normalizeStatus(
                                    document.getString("status")
                            )
                    );

                    payments.add(payment);
                    maintenanceCount++;
                }
            }

            System.out.println(
                    "Maintenance records found: "
                            + maintenanceCount
            );

            System.out.println(
                    "Payments found including maintenance: "
                            + payments.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error loading owner payments and maintenance: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return payments;
    }

    // =========================================================
    // NORMALIZE MAINTENANCE/PAYMENT STATUS
    // =========================================================

    private String normalizeStatus(
            String status) {

        String value =
                safeString(status)
                        .trim()
                        .toLowerCase();

        if (value.equals("paid") ||
                value.equals("received") ||
                value.equals("success") ||
                value.equals("successful")) {
            return "received";
        }

        if (value.equals("pending") ||
                value.equals("due")) {
            return "pending";
        }

        if (value.equals("overdue")) {
            return "overdue";
        }

        return value;
    }

    // =========================================================
    // FIRST NON-EMPTY VALUE
    // =========================================================

    private String firstNonEmpty(
            String... values) {

        if (values == null) {
            return "";
        }

        for (String value : values) {
            if (value != null &&
                    !value.trim().isEmpty()) {
                return value.trim();
            }
        }

        return "";
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safeString(
            String value) {

        return value == null
                ? ""
                : value;
    }
}