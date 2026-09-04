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


            ownerEmail =
                    ownerEmail.trim().toLowerCase();


            // -------------------------------------------------
            // QUERY PAYMENTS
            // -------------------------------------------------

            QuerySnapshot snapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "ownerEmail",
                                    ownerEmail
                            )
                            .get()
                            .get();


            // -------------------------------------------------
            // CONVERT DOCUMENTS
            // -------------------------------------------------

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                PaymentModel payment =
                        new PaymentModel();


                payment.setPaymentId(
                        document.getId()
                );


                payment.setOwnerEmail(
                        document.getString(
                                "ownerEmail"
                        )
                );


                payment.setTenantEmail(
                        document.getString(
                                "tenantEmail"
                        )
                );


                payment.setDate(
                        document.getString(
                                "date"
                        )
                );


                payment.setTenant(
                        document.getString(
                                "tenant"
                        )
                );


                payment.setFlat(
                        document.getString(
                                "flat"
                        )
                );


                payment.setPaymentFor(
                        document.getString(
                                "paymentFor"
                        )
                );


                payment.setAmount(
                        document.getString(
                                "amount"
                        )
                );


                payment.setStatus(
                        document.getString(
                                "status"
                        )
                );


                payments.add(
                        payment
                );
            }


            System.out.println(
                    "Payments found: "
                            + payments.size()
            );


        } catch (Exception e) {

            System.out.println(
                    "Error loading payments: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }


        return payments;
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