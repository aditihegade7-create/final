package com.society.dao.Secretary_dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.dao.Welcome.UserDao;
import com.society.model.Secretary_model.Payment;
import com.society.model.Welcome.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PaymentDao {

    // =========================================================
    // FIRESTORE COLLECTION NAMES
    // =========================================================

    private static final String AMENITIES_COLLECTION =
            "Amenities";

    private static final String AMENITIES_SUB_COLLECTION =
            "amenities";

    private static final String BOOKINGS_SUB_COLLECTION =
            "bookings";

    private static final String BOOKING_DATA_SUB_COLLECTION =
            "bookingData";


    private final Firestore firestore;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PaymentDao() {

        firestore = FirebaseConfig.getFirestore();
    }


    // =========================================================
    // NORMALIZE EMAIL
    // =========================================================

    private String normalizeEmail(String email) {

        if (email == null) {
            return "";
        }

        return email.trim().toLowerCase();
    }


    // =========================================================
    // GET LOGGED-IN SECRETARY
    // =========================================================

    private User getLoggedInSecretary() {

        try {

            UserDao userDao = new UserDao();

            return userDao.getLoggedInSecretary();

        } catch (Exception e) {

            System.out.println(
                    "Error while getting logged-in secretary."
            );

            e.printStackTrace();

            return null;
        }
    }


    // =========================================================
    // GET LOGGED-IN SECRETARY EMAIL
    // =========================================================

    private String getLoggedInEmail() {

        User secretary = getLoggedInSecretary();

        if (secretary == null) {
            return "";
        }

        return normalizeEmail(
                secretary.getEmail()
        );
    }


    // =========================================================
    // GET LOGGED-IN SOCIETY
    // =========================================================

    private String getLoggedInSociety() {

        User secretary = getLoggedInSecretary();

        if (secretary == null) {
            return "";
        }

        String society = secretary.getSociety();

        if (society == null) {
            return "";
        }

        return society.trim();
    }


    // =========================================================
    // ADD AMENITY
    // =========================================================

    public boolean addAmenity(
            String name,
            String price,
            String description,
            String availability) {

        try {

            if (name == null ||
                    name.trim().isEmpty()) {

                return false;
            }

            if (price == null ||
                    price.trim().isEmpty()) {

                return false;
            }

            if (description == null ||
                    description.trim().isEmpty()) {

                return false;
            }

            if (availability == null ||
                    availability.trim().isEmpty()) {

                return false;
            }


            String secretaryEmail =
                    getLoggedInEmail();

            String society =
                    getLoggedInSociety();


            if (secretaryEmail.isEmpty()) {

                System.out.println(
                        "Secretary email not found."
                );

                return false;
            }


            if (society.isEmpty()) {

                System.out.println(
                        "Secretary society not found."
                );

                return false;
            }


            String amenityId =
                    UUID.randomUUID().toString();


            DocumentReference amenityRef =
                    firestore
                            .collection(
                                    AMENITIES_COLLECTION
                            )
                            .document(
                                    secretaryEmail
                            )
                            .collection(
                                    AMENITIES_SUB_COLLECTION
                            )
                            .document(
                                    amenityId
                            );


            Map<String, Object> amenity =
                    new HashMap<>();


            amenity.put(
                    "amenityId",
                    amenityId
            );

            amenity.put(
                    "amenityName",
                    name.trim()
            );

            amenity.put(
                    "price",
                    price.trim()
            );

            amenity.put(
                    "description",
                    description.trim()
            );

            amenity.put(
                    "availability",
                    availability.trim()
            );

            amenity.put(
                    "createdByEmail",
                    secretaryEmail
            );

            amenity.put(
                    "society",
                    society
            );

            amenity.put(
                    "timestamp",
                    System.currentTimeMillis()
            );


            amenityRef
                    .set(amenity)
                    .get();


            System.out.println(
                    "Amenity added successfully."
            );


            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error while adding amenity."
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // GET ALL AMENITIES
    // =========================================================

    public List<Payment> getAllAmenities() {

        List<Payment> amenities =
                new ArrayList<>();

        try {

            String secretaryEmail =
                    getLoggedInEmail();

            String loggedInSociety =
                    getLoggedInSociety();


            if (secretaryEmail.isEmpty()) {

                System.out.println(
                        "Secretary email not found."
                );

                return amenities;
            }


            CollectionReference amenitiesRef =
                    firestore
                            .collection(
                                    AMENITIES_COLLECTION
                            )
                            .document(
                                    secretaryEmail
                            )
                            .collection(
                                    AMENITIES_SUB_COLLECTION
                            );


            ApiFuture<QuerySnapshot> future =
                    amenitiesRef.get();


            QuerySnapshot snapshot =
                    future.get();


            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (!document.exists()) {
                    continue;
                }


                String amenitySociety =
                        document.getString(
                                "society"
                        );


                if (!loggedInSociety.isEmpty()
                        && (amenitySociety == null
                        || !loggedInSociety.equalsIgnoreCase(
                                amenitySociety.trim()
                        ))) {

                    continue;
                }


                Payment payment =
                        new Payment();


                payment.setAmenityId(
                        document.getId()
                );

                payment.setAmenityName(
                        getString(
                                document,
                                "amenityName"
                        )
                );

                payment.setPrice(
                        getString(
                                document,
                                "price"
                        )
                );

                payment.setDescription(
                        getString(
                                document,
                                "description"
                        )
                );

                payment.setAvailability(
                        getString(
                                document,
                                "availability"
                        )
                );

                payment.setSociety(
                        amenitySociety
                );


                amenities.add(
                        payment
                );
            }


            System.out.println(
                    "AMENITIES FETCHED SUCCESSFULLY"
            );

            System.out.println(
                    "Total Amenities : " +
                    amenities.size()
            );

            System.out.println(
                    "Secretary Email : " +
                    secretaryEmail
            );


        } catch (Exception e) {

            System.out.println(
                    "ERROR WHILE FETCHING AMENITIES"
            );

            e.printStackTrace();
        }


        return amenities;
    }


    // =========================================================
    // GET ALL BOOKINGS
    //
    // ACTUAL FIRESTORE STRUCTURE:
    //
    // Amenities
    //   └── aditi@gmail.com
    //        └── amenities
    //             └── amenityId
    //                  └── bookings
    //                       └── vaishnavi@gmail.com
    //                            └── bookingData
    //                                 └── booking1
    //
    // =========================================================

    public List<Payment> getAllBookings() {

        List<Payment> bookings =
                new ArrayList<>();

        try {

            String secretaryEmail =
                    getLoggedInEmail();

            String loggedInSociety =
                    getLoggedInSociety();


            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "FETCHING ALL BOOKINGS"
            );

            System.out.println(
                    "Secretary Email : " +
                    secretaryEmail
            );

            System.out.println(
                    "Society : " +
                    loggedInSociety
            );

            System.out.println(
                    "========================================"
            );


            if (secretaryEmail.isEmpty()) {

                System.out.println(
                        "Secretary email not found."
                );

                return bookings;
            }


            // =================================================
            // FIND ALL bookingData DOCUMENTS
            //
            // This finds booking1, booking2, etc.
            // irrespective of resident email.
            // =================================================

            QuerySnapshot bookingSnapshot =
                    firestore
                            .collectionGroup(
                                    BOOKING_DATA_SUB_COLLECTION
                            )
                            .get()
                            .get();


            System.out.println(
                    "Total bookingData Documents Found : " +
                    bookingSnapshot.size()
            );


            // =================================================
            // LOOP THROUGH ALL BOOKING DOCUMENTS
            // =================================================

            for (DocumentSnapshot bookingDocument :
                    bookingSnapshot.getDocuments()) {

                if (!bookingDocument.exists()) {
                    continue;
                }


                // =================================================
                // GET bookingData COLLECTION
                //
                // booking1
                //   ↓
                // bookingData
                //
                // getParent() returns CollectionReference
                // =================================================

                CollectionReference bookingDataCollection =
                        bookingDocument
                                .getReference()
                                .getParent();


                if (bookingDataCollection == null) {
                    continue;
                }


                // =================================================
                // GET RESIDENT DOCUMENT
                //
                // bookingData
                //   ↓
                // vaishnavi@gmail.com
                //
                // getParent() returns DocumentReference
                // =================================================

                DocumentReference residentDocument =
                        bookingDataCollection
                                .getParent();


                if (residentDocument == null) {
                    continue;
                }


                String residentEmail =
                        residentDocument.getId();


                // =================================================
                // GET bookings COLLECTION
                //
                // residentEmail
                //   ↓
                // bookings
                // =================================================

                CollectionReference bookingsCollection =
                        residentDocument
                                .getParent();


                if (bookingsCollection == null) {
                    continue;
                }


                // =================================================
                // GET AMENITY DOCUMENT
                //
                // bookings
                //   ↓
                // amenityId
                // =================================================

                DocumentReference amenityDocument =
                        bookingsCollection
                                .getParent();


                if (amenityDocument == null) {
                    continue;
                }


                String amenityId =
                        amenityDocument.getId();


                // =================================================
                // GET amenities COLLECTION
                //
                // amenityId
                //   ↓
                // amenities
                // =================================================

                CollectionReference amenitiesCollection =
                        amenityDocument
                                .getParent();


                if (amenitiesCollection == null) {
                    continue;
                }


                // =================================================
                // GET SECRETARY DOCUMENT
                //
                // amenities
                //   ↓
                // aditi@gmail.com
                // =================================================

                DocumentReference secretaryDocument =
                        amenitiesCollection
                                .getParent();


                if (secretaryDocument == null) {
                    continue;
                }


                String firestoreSecretaryEmail =
                        secretaryDocument.getId();


                // =================================================
                // SECRETARY CHECK
                // =================================================

                if (!normalizeEmail(
                        firestoreSecretaryEmail
                ).equals(
                        normalizeEmail(
                                secretaryEmail
                        )
                )) {

                    continue;
                }


                // =================================================
                // GET AMENITY DOCUMENT DATA
                // =================================================

                DocumentSnapshot amenitySnapshot =
                        amenityDocument
                                .get()
                                .get();


                if (!amenitySnapshot.exists()) {
                    continue;
                }


                String amenitySociety =
                        amenitySnapshot.getString(
                                "society"
                        );


                // =================================================
                // SOCIETY CHECK
                // =================================================

                if (!loggedInSociety.isEmpty()
                        && (amenitySociety == null
                        || !loggedInSociety.equalsIgnoreCase(
                                amenitySociety.trim()
                        ))) {

                    continue;
                }


                String amenityName =
                        getString(
                                amenitySnapshot,
                                "amenityName"
                        );


                System.out.println(
                        "----------------------------------------"
                );

                System.out.println(
                        "BOOKING FOUND"
                );

                System.out.println(
                        "Amenity : " +
                        amenityName
                );

                System.out.println(
                        "Amenity ID : " +
                        amenityId
                );

                System.out.println(
                        "Resident : " +
                        residentEmail
                );

                System.out.println(
                        "Booking Document : " +
                        bookingDocument.getId()
                );


                // =================================================
                // CONVERT BOOKING
                // =================================================

                Payment payment =
                        convertBooking(
                                bookingDocument,
                                amenitySnapshot,
                                residentEmail
                        );


                if (payment != null) {

                    bookings.add(
                            payment
                    );


                    System.out.println(
                            "Booking ID : " +
                            payment.getBookingId()
                    );

                    System.out.println(
                            "Resident Name : " +
                            payment.getResidentName()
                    );

                    System.out.println(
                            "Flat No : " +
                            payment.getFlatNo()
                    );

                    System.out.println(
                            "Date : " +
                            payment.getBookingDate()
                    );

                    System.out.println(
                            "Time : " +
                            payment.getStartTime()
                            + " - "
                            + payment.getEndTime()
                    );

                    System.out.println(
                            "Payment Amount : " +
                            payment.getPaymentAmount()
                    );

                    System.out.println(
                            "Payment Status : " +
                            payment.getPaymentStatus()
                    );

                    System.out.println(
                            "Booking Status : " +
                            payment.getStatus()
                    );
                }
            }


            // =================================================
            // FINAL OUTPUT
            // =================================================

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "BOOKINGS FETCHED SUCCESSFULLY"
            );

            System.out.println(
                    "Total Bookings : " +
                    bookings.size()
            );

            System.out.println(
                    "========================================"
            );


        } catch (Exception e) {

            System.out.println(
                    "ERROR WHILE FETCHING BOOKINGS"
            );

            e.printStackTrace();
        }


        return bookings;
    }


    // =========================================================
    // CONVERT BOOKING DOCUMENT TO PAYMENT
    // =========================================================

    private Payment convertBooking(
            DocumentSnapshot bookingDocument,
            DocumentSnapshot amenityDocument,
            String residentEmail) {

        try {

            Payment payment =
                    new Payment();


            // =================================================
            // AMENITY INFORMATION
            // =================================================

            payment.setAmenityId(
                    amenityDocument.getId()
            );

            payment.setAmenityName(
                    getString(
                            amenityDocument,
                            "amenityName"
                    )
            );

            payment.setSociety(
                    getString(
                            amenityDocument,
                            "society"
                    )
            );


            // =================================================
            // BOOKING ID
            // =================================================

            String bookingId =
                    getString(
                            bookingDocument,
                            "bookingId"
                    );


            if (bookingId.isEmpty()) {

                bookingId =
                        bookingDocument.getId();
            }


            payment.setBookingId(
                    bookingId
            );


            // =================================================
            // RESIDENT EMAIL
            // =================================================

            String storedResidentEmail =
                    getString(
                            bookingDocument,
                            "residentEmail"
                    );


            if (storedResidentEmail.isEmpty()) {

                storedResidentEmail =
                        residentEmail;
            }


            payment.setEmail(
                    normalizeEmail(
                            storedResidentEmail
                    )
            );


            // =================================================
            // RESIDENT NAME
            // =================================================

            payment.setResidentName(
                    getString(
                            bookingDocument,
                            "residentName"
                    )
            );


            // =================================================
            // FLAT NUMBER
            // =================================================

            payment.setFlatNo(
                    getString(
                            bookingDocument,
                            "flatNo"
                    )
            );


            // =================================================
            // BOOKING DATE
            // =================================================

            payment.setBookingDate(
                    getString(
                            bookingDocument,
                            "bookingDate"
                    )
            );


            // =================================================
            // START TIME
            // =================================================

            payment.setStartTime(
                    getString(
                            bookingDocument,
                            "startTime"
                    )
            );


            // =================================================
            // END TIME
            // =================================================

            payment.setEndTime(
                    getString(
                            bookingDocument,
                            "endTime"
                    )
            );


            // =================================================
            // PAYMENT AMOUNT
            // =================================================

            payment.setPaymentAmount(
                    getString(
                            bookingDocument,
                            "paymentAmount"
                    )
            );


            // =================================================
            // PAYMENT STATUS
            // =================================================

            payment.setPaymentStatus(
                    getString(
                            bookingDocument,
                            "paymentStatus"
                    )
            );


            // =================================================
            // BOOKING STATUS
            // =================================================

            String bookingStatus =
                    getString(
                            bookingDocument,
                            "bookingStatus"
                    );


            if (bookingStatus.isEmpty()) {

                bookingStatus =
                        getString(
                                bookingDocument,
                                "status"
                        );
            }


            if (bookingStatus.isEmpty()) {

                bookingStatus =
                        "PENDING";
            }


            payment.setStatus(
                    bookingStatus
            );


            return payment;

        } catch (Exception e) {

            System.out.println(
                    "Error converting booking document."
            );

            e.printStackTrace();

            return null;
        }
    }


    // =========================================================
    // UPDATE BOOKING STATUS
    //
    // IMPORTANT:
    //
    // This method ALSO uses collectionGroup("bookingData").
    //
    // Therefore it searches the exact same booking documents
    // that getAllBookings() displays.
    //
    // No manual CollectionReference/DocumentReference
    // parent traversal is required for the update itself.
    //
    // =========================================================

    public boolean updateBookingStatus(
            String email,
            String bookingId,
            String newStatus) {

        try {

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "UPDATING BOOKING STATUS"
            );

            System.out.println(
                    "Email : " +
                    email
            );

            System.out.println(
                    "Booking ID : " +
                    bookingId
            );

            System.out.println(
                    "New Status : " +
                    newStatus
            );

            System.out.println(
                    "========================================"
            );


            // =================================================
            // VALIDATION
            // =================================================

            if (email == null ||
                    email.trim().isEmpty()) {

                System.out.println(
                        "Update failed: email is empty."
                );

                return false;
            }


            if (bookingId == null ||
                    bookingId.trim().isEmpty()) {

                System.out.println(
                        "Update failed: booking ID is empty."
                );

                return false;
            }


            if (newStatus == null ||
                    newStatus.trim().isEmpty()) {

                System.out.println(
                        "Update failed: status is empty."
                );

                return false;
            }


            String status =
                    newStatus.trim().toUpperCase();


            // =================================================
            // ALLOWED STATUS
            // =================================================

            if (!"ACCEPTED".equals(status)
                    && !"REJECTED".equals(status)) {

                System.out.println(
                        "Invalid booking status: " +
                        status
                );

                return false;
            }


            // =================================================
            // LOGGED-IN SECRETARY
            // =================================================

            String secretaryEmail =
                    getLoggedInEmail();

            String loggedInSociety =
                    getLoggedInSociety();


            if (secretaryEmail.isEmpty()) {

                System.out.println(
                        "Update failed: secretary email not found."
                );

                return false;
            }


            // =================================================
            // GET ALL bookingData DOCUMENTS
            // =================================================

            QuerySnapshot bookingSnapshot =
                    firestore
                            .collectionGroup(
                                    BOOKING_DATA_SUB_COLLECTION
                            )
                            .get()
                            .get();


            System.out.println(
                    "Booking Data Documents Found : " +
                    bookingSnapshot.size()
            );


            // =================================================
            // LOOP THROUGH BOOKING DOCUMENTS
            // =================================================

            for (DocumentSnapshot bookingDocument :
                    bookingSnapshot.getDocuments()) {

                if (!bookingDocument.exists()) {
                    continue;
                }


                // =================================================
                // GET BOOKING DATA PARENT
                // =================================================

                CollectionReference bookingDataCollection =
                        bookingDocument
                                .getReference()
                                .getParent();


                if (bookingDataCollection == null) {
                    continue;
                }


                // =================================================
                // GET RESIDENT DOCUMENT
                // =================================================

                DocumentReference residentDocument =
                        bookingDataCollection
                                .getParent();


                if (residentDocument == null) {
                    continue;
                }


                String residentDocumentEmail =
                        residentDocument.getId();


                // =================================================
                // GET BOOKINGS COLLECTION
                // =================================================

                CollectionReference bookingsCollection =
                        residentDocument
                                .getParent();


                if (bookingsCollection == null) {
                    continue;
                }


                // =================================================
                // GET AMENITY DOCUMENT
                // =================================================

                DocumentReference amenityDocument =
                        bookingsCollection
                                .getParent();


                if (amenityDocument == null) {
                    continue;
                }


                // =================================================
                // GET AMENITIES COLLECTION
                // =================================================

                CollectionReference amenitiesCollection =
                        amenityDocument
                                .getParent();


                if (amenitiesCollection == null) {
                    continue;
                }


                // =================================================
                // GET SECRETARY DOCUMENT
                // =================================================

                DocumentReference secretaryDocument =
                        amenitiesCollection
                                .getParent();


                if (secretaryDocument == null) {
                    continue;
                }


                String firestoreSecretaryEmail =
                        secretaryDocument.getId();


                // =================================================
                // CHECK SECRETARY
                // =================================================

                if (!normalizeEmail(
                        firestoreSecretaryEmail
                ).equals(
                        normalizeEmail(
                                secretaryEmail
                        )
                )) {

                    continue;
                }


                // =================================================
                // GET AMENITY DATA
                // =================================================

                DocumentSnapshot amenitySnapshot =
                        amenityDocument
                                .get()
                                .get();


                if (!amenitySnapshot.exists()) {
                    continue;
                }


                String amenitySociety =
                        amenitySnapshot.getString(
                                "society"
                        );


                // =================================================
                // CHECK SOCIETY
                // =================================================

                if (!loggedInSociety.isEmpty()
                        && (amenitySociety == null
                        || !loggedInSociety.equalsIgnoreCase(
                                amenitySociety.trim()
                        ))) {

                    continue;
                }


                // =================================================
                // GET STORED BOOKING EMAIL
                // =================================================

                String storedEmail =
                        getString(
                                bookingDocument,
                                "residentEmail"
                        );


                // =================================================
                // EMAIL MATCH
                //
                // Check both:
                //
                // 1. resident document ID
                // 2. residentEmail field
                //
                // This makes the update robust.
                // =================================================

                boolean emailMatched =
                        normalizeEmail(email)
                                .equals(
                                        normalizeEmail(
                                                residentDocumentEmail
                                        )
                                )
                        ||
                        normalizeEmail(email)
                                .equals(
                                        normalizeEmail(
                                                storedEmail
                                        )
                                );


                if (!emailMatched) {
                    continue;
                }


                // =================================================
                // GET STORED BOOKING ID
                // =================================================

                String storedBookingId =
                        getString(
                                bookingDocument,
                                "bookingId"
                        );


                String bookingDocumentId =
                        bookingDocument.getId();


                // =================================================
                // BOOKING ID MATCH
                //
                // Match either:
                //
                // bookingId field
                // OR
                // booking1 document ID
                // =================================================

                boolean bookingMatched =
                        bookingId.trim().equals(
                                storedBookingId
                        )
                        ||
                        bookingId.trim().equals(
                                bookingDocumentId
                        );


                if (!bookingMatched) {
                    continue;
                }


                // =================================================
                // MATCH FOUND
                // =================================================

                System.out.println(
                        "========================================"
                );

                System.out.println(
                        "BOOKING MATCHED"
                );

                System.out.println(
                        "Resident : " +
                        residentDocumentEmail
                );

                System.out.println(
                        "Booking Document : " +
                        bookingDocumentId
                );

                System.out.println(
                        "Booking ID : " +
                        storedBookingId
                );

                System.out.println(
                        "Amenity : " +
                        getString(
                                amenitySnapshot,
                                "amenityName"
                        )
                );

                System.out.println(
                        "Old Status : " +
                        getString(
                                bookingDocument,
                                "bookingStatus"
                        )
                );

                System.out.println(
                        "New Status : " +
                        status
                );


                // =================================================
                // UPDATE ACTUAL FIRESTORE BOOKING DOCUMENT
                // =================================================

                bookingDocument
                        .getReference()
                        .update(
                                "bookingStatus",
                                status
                        )
                        .get();


                System.out.println(
                        "========================================"
                );

                System.out.println(
                        "BOOKING STATUS UPDATED SUCCESSFULLY"
                );

                System.out.println(
                        "Booking ID : " +
                        bookingId
                );

                System.out.println(
                        "Resident : " +
                        residentDocumentEmail
                );

                System.out.println(
                        "Status : " +
                        status
                );

                System.out.println(
                        "========================================"
                );


                return true;
            }


            // =================================================
            // NOT FOUND
            // =================================================

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "BOOKING NOT FOUND FOR UPDATE"
            );

            System.out.println(
                    "Email : " +
                    email
            );

            System.out.println(
                    "Booking ID : " +
                    bookingId
            );

            System.out.println(
                    "========================================"
            );


        } catch (Exception e) {

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "ERROR WHILE UPDATING BOOKING STATUS"
            );

            System.out.println(
                    "Email : " +
                    email
            );

            System.out.println(
                    "Booking ID : " +
                    bookingId
            );

            System.out.println(
                    "Status : " +
                    newStatus
            );

            System.out.println(
                    "========================================"
            );

            e.printStackTrace();
        }


        return false;
    }


    // =========================================================
    // FIRESTORE STRING HELPER
    // =========================================================

    private String getString(
            DocumentSnapshot document,
            String field) {

        try {

            String value =
                    document.getString(
                            field
                    );

            if (value == null) {
                return "";
            }

            return value;

        } catch (Exception e) {

            return "";
        }
    }
}