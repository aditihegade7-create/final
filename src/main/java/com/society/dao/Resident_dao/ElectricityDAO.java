package com.society.dao.Resident_dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.society.config.FirebaseConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ElectricityDAO {

    private final Firestore db;

    private static final String COLLECTION = "electricity";
    private static final String BILLS = "bills";

    public ElectricityDAO() {
        db = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // ADD ELECTRICITY BILL
    //
    // electricity/{email}/bills/{month}
    // =========================================================

    public boolean addElectricityBill(
            String email,
            String residentName,
            String flatNo,
            String amount,
            String date,
            String month,
            String status
    ) {

        try {

            if (email == null || email.trim().isEmpty()) {
                return false;
            }

            if (month == null || month.trim().isEmpty()) {
                return false;
            }

            email = email.trim();
            month = month.trim();

            // -------------------------------------------------
            // RESIDENT DOCUMENT
            // -------------------------------------------------

            DocumentReference residentDocument =
                    db.collection(COLLECTION)
                            .document(email);

            Map<String, Object> residentData =
                    new HashMap<>();

            residentData.put("email", email);

            residentData.put(
                    "residentName",
                    residentName != null
                            ? residentName.trim()
                            : ""
            );

            residentData.put(
                    "flatNo",
                    flatNo != null
                            ? flatNo.trim()
                            : ""
            );

            // merge = true
            // Existing bills will NOT be deleted.
            residentDocument
                    .set(
                            residentData,
                            com.google.cloud.firestore.SetOptions.merge()
                    )
                    .get();

            // -------------------------------------------------
            // BILL DOCUMENT
            // -------------------------------------------------

            DocumentReference billDocument =
                    residentDocument
                            .collection(BILLS)
                            .document(month);

            Map<String, Object> billData =
                    new HashMap<>();

            billData.put(
                    "billId",
                    month
            );

            billData.put(
                    "amount",
                    amount != null
                            ? amount.trim()
                            : ""
            );

            billData.put(
                    "date",
                    date != null
                            ? date.trim()
                            : ""
            );

            billData.put(
                    "month",
                    month
            );

            billData.put(
                    "status",
                    status != null && !status.trim().isEmpty()
                            ? status.trim()
                            : "Pending"
            );

            ApiFuture<WriteResult> future =
                    billDocument.set(billData);

            future.get();

            return true;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            e.printStackTrace();
            return false;

        } catch (ExecutionException e) {

            e.printStackTrace();
            return false;
        }
    }

    // =========================================================
    // GET ALL ELECTRICITY BILLS BY EMAIL
    // =========================================================

    public List<Map<String, Object>> getElectricityBillsByEmail(
            String email
    ) {

        List<Map<String, Object>> bills =
                new ArrayList<>();

        try {

            if (email == null || email.trim().isEmpty()) {
                return bills;
            }

            email = email.trim();

            // ONLY logged-in resident email
            CollectionReference billsCollection =
                    db.collection(COLLECTION)
                            .document(email)
                            .collection(BILLS);

            ApiFuture<QuerySnapshot> future =
                    billsCollection.get();

            QuerySnapshot querySnapshot =
                    future.get();

            for (DocumentSnapshot document :
                    querySnapshot.getDocuments()) {

                Map<String, Object> data =
                        document.getData();

                if (data == null) {
                    continue;
                }

                Map<String, Object> bill =
                        new HashMap<>(data);

                // Make sure billId exists
                bill.put(
                        "billId",
                        document.getId()
                );

                // Make sure month exists
                if (!bill.containsKey("month")
                        || bill.get("month") == null
                        || String.valueOf(
                        bill.get("month")
                ).trim().isEmpty()) {

                    bill.put(
                            "month",
                            document.getId()
                    );
                }

                bills.add(bill);
            }

            return bills;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            e.printStackTrace();
            return bills;

        } catch (ExecutionException e) {

            e.printStackTrace();
            return bills;
        }
    }

    // =========================================================
    // GET ONE ELECTRICITY BILL BY MONTH
    // =========================================================

    public Map<String, Object> getElectricityBillByMonth(
            String email,
            String month
    ) {

        try {

            if (email == null || email.trim().isEmpty()) {
                return null;
            }

            if (month == null || month.trim().isEmpty()) {
                return null;
            }

            email = email.trim();
            month = month.trim();

            DocumentReference billDocument =
                    db.collection(COLLECTION)
                            .document(email)
                            .collection(BILLS)
                            .document(month);

            DocumentSnapshot document =
                    billDocument.get().get();

            if (!document.exists()) {
                return null;
            }

            Map<String, Object> data =
                    document.getData();

            if (data == null) {
                return null;
            }

            Map<String, Object> result =
                    new HashMap<>(data);

            result.put(
                    "billId",
                    document.getId()
            );

            return result;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            e.printStackTrace();
            return null;

        } catch (ExecutionException e) {

            e.printStackTrace();
            return null;
        }
    }

    // =========================================================
    // UPDATE ELECTRICITY STATUS
    //
    // electricity/{email}/bills/{billId}
    // =========================================================

    public boolean updateElectricityStatusByBillId(
            String email,
            String billId,
            String newStatus
    ) {

        try {

            if (email == null || email.trim().isEmpty()) {
                return false;
            }

            if (billId == null || billId.trim().isEmpty()) {
                return false;
            }

            if (newStatus == null || newStatus.trim().isEmpty()) {
                return false;
            }

            email = email.trim();
            billId = billId.trim();
            newStatus = newStatus.trim();

            DocumentReference billDocument =
                    db.collection(COLLECTION)
                            .document(email)
                            .collection(BILLS)
                            .document(billId);

            DocumentSnapshot document =
                    billDocument.get().get();

            if (!document.exists()) {
                return false;
            }

            billDocument
                    .update(
                            "status",
                            newStatus
                    )
                    .get();

            return true;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            e.printStackTrace();
            return false;

        } catch (ExecutionException e) {

            e.printStackTrace();
            return false;
        }
    }

    // =========================================================
    // DELETE ELECTRICITY BILL
    //
    // Only specified resident's bill is deleted
    // =========================================================

    public boolean deleteElectricityBill(
            String email,
            String billId
    ) {

        try {

            if (email == null || email.trim().isEmpty()) {
                return false;
            }

            if (billId == null || billId.trim().isEmpty()) {
                return false;
            }

            email = email.trim();
            billId = billId.trim();

            DocumentReference billDocument =
                    db.collection(COLLECTION)
                            .document(email)
                            .collection(BILLS)
                            .document(billId);

            DocumentSnapshot document =
                    billDocument.get().get();

            if (!document.exists()) {
                return false;
            }

            billDocument
                    .delete()
                    .get();

            return true;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            e.printStackTrace();
            return false;

        } catch (ExecutionException e) {

            e.printStackTrace();
            return false;
        }
    }
}