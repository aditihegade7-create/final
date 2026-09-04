package com.society.dao.Resident_dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.config.FirebaseConfig;
import com.society.model.Resident_model.Amenities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AmenitiesDAO {

    private static final String AMENITIES_COLLECTION = "Amenities";
    private static final String AMENITIES_SUBCOLLECTION = "amenities";
    private static final String BOOKINGS_SUBCOLLECTION = "bookings";
    private static final String BOOKING_DATA_SUBCOLLECTION = "bookingData";
    private static final String RESIDENTS_COLLECTION = "Residents";
    private static final String SECRETARIES_COLLECTION = "Secretaries";
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

    private final Firestore db;

    public AmenitiesDAO() {
        db = FirebaseConfig.getFirestore();
    }

    public List<Amenities> getAmenities(String secretaryEmail) {
        List<Amenities> amenitiesList = new ArrayList<>();

        String email = normalize(secretaryEmail);
        if (email.isEmpty()) {
            return amenitiesList;
        }

        try {
            CollectionReference amenitiesRef =
                    db.collection(AMENITIES_COLLECTION)
                            .document(email)
                            .collection(AMENITIES_SUBCOLLECTION);

            QuerySnapshot snapshot = amenitiesRef.get().get();

            for (DocumentSnapshot document : snapshot.getDocuments()) {
                if (document == null || !document.exists()) {
                    continue;
                }

                Amenities amenity = new Amenities();
                amenity.setAmenityId(document.getId());
                amenity.setAmenityName(document.getString("amenityName"));
                amenity.setDescription(document.getString("description"));
                amenity.setPrice(document.getString("price"));
                amenity.setAvailability(document.getString("availability"));

                // Runtime-only information used to follow the exact Firestore path.
                amenity.setSecretaryEmail(email);

                amenitiesList.add(amenity);
            }

            System.out.println("========================================");
            System.out.println("AmenitiesDAO: Fetching amenities");
            System.out.println("Secretary Email: " + email);
            System.out.println("Amenities Found: " + amenitiesList.size());
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("AmenitiesDAO: Error fetching amenities.");
            e.printStackTrace();
        }

        return amenitiesList;
    }

    /**
     * Resolves the secretary dynamically from the logged-in resident.
     * Residents/{residentEmail} -> society -> Secretaries where society matches.
     */
    public String getSecretaryEmailForResident(String residentEmail) {
        String email = normalize(residentEmail);
        if (email.isEmpty()) {
            return "";
        }

        try {
            DocumentSnapshot resident =
                    db.collection(RESIDENTS_COLLECTION)
                            .document(email)
                            .get()
                            .get();

            if (!resident.exists()) {
                System.err.println("Resident not found: " + email);
                return "";
            }

            String society = getString(resident, "society");
            if (society.isEmpty()) {
                society = getString(resident, "societyName");
            }

            if (society.isEmpty()) {
                System.err.println("Society not found for resident: " + email);
                return "";
            }

            QuerySnapshot secretarySnapshot =
                    db.collection(SECRETARIES_COLLECTION)
                            .whereEqualTo("society", society)
                            .limit(1)
                            .get()
                            .get();

            if (secretarySnapshot.isEmpty()) {
                secretarySnapshot =
                        db.collection(SECRETARIES_COLLECTION)
                                .whereEqualTo("societyName", society)
                                .limit(1)
                                .get()
                                .get();
            }

            if (secretarySnapshot.isEmpty()) {
                System.err.println(
                        "Secretary not found for society: " + society
                );
                return "";
            }

            DocumentSnapshot secretary = secretarySnapshot.getDocuments().get(0);
            String secretaryEmail = getString(secretary, "email");

            if (secretaryEmail.isEmpty()) {
                secretaryEmail = secretary.getId();
            }

            secretaryEmail = normalize(secretaryEmail);

            System.out.println("Resident Email: " + email);
            System.out.println("Resident Society: " + society);
            System.out.println("Resolved Secretary Email: " + secretaryEmail);

            return secretaryEmail;

        } catch (Exception e) {
            System.err.println("Error resolving secretary for resident: " + email);
            e.printStackTrace();
            return "";
        }
    }

    public Set<String> getUnavailableAcceptedSlots(
            String secretaryEmail,
            String amenityId,
            String bookingDate) {

        Set<String> unavailableSlots = new HashSet<>();

        String email = normalize(secretaryEmail);
        String amenity = normalize(amenityId);
        String date = safe(bookingDate);

        if (email.isEmpty() || amenity.isEmpty() || date.isEmpty()) {
            return unavailableSlots;
        }

        try {
            LocalDate selectedDate = LocalDate.parse(date);
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            DocumentReference amenityRef =
                    db.collection(AMENITIES_COLLECTION)
                            .document(email)
                            .collection(AMENITIES_SUBCOLLECTION)
                            .document(amenity);

            QuerySnapshot residentsSnapshot =
                    amenityRef.collection(BOOKINGS_SUBCOLLECTION)
                            .get()
                            .get();

            for (DocumentSnapshot residentDocument : residentsSnapshot.getDocuments()) {
                if (residentDocument == null || !residentDocument.exists()) {
                    continue;
                }

                QuerySnapshot bookingSnapshot =
                        residentDocument.getReference()
                                .collection(BOOKING_DATA_SUBCOLLECTION)
                                .get()
                                .get();

                for (DocumentSnapshot booking : bookingSnapshot.getDocuments()) {
                    if (booking == null || !booking.exists()) {
                        continue;
                    }

                    String status = getString(booking, "bookingStatus");
                    if (!"ACCEPTED".equalsIgnoreCase(status)) {
                        continue;
                    }

                    String existingDate = getString(booking, "bookingDate");
                    String startTime = getString(booking, "startTime");
                    String endTime = getString(booking, "endTime");

                    if (!date.equals(existingDate) || startTime.isEmpty() || endTime.isEmpty()) {
                        continue;
                    }

                    // An accepted slot blocks other residents only while the slot is active.
                    if (selectedDate.isBefore(today)) {
                        continue;
                    }

                    if (selectedDate.isEqual(today)) {
                        LocalTime end = parseTime(endTime);
                        if (end == null || !now.isBefore(end)) {
                            continue;
                        }
                    }

                    unavailableSlots.add(startTime + " - " + endTime);
                }
            }

        } catch (Exception e) {
            System.err.println("Error checking accepted unavailable slots.");
            e.printStackTrace();
        }

        return unavailableSlots;
    }

    public boolean isSlotAvailable(
            String secretaryEmail,
            String amenityId,
            String bookingDate,
            String startTime,
            String endTime) {

        String email = normalize(secretaryEmail);
        String amenity = normalize(amenityId);
        String date = safe(bookingDate);
        String start = safe(startTime);
        String end = safe(endTime);

        if (email.isEmpty() || amenity.isEmpty() || date.isEmpty()
                || start.isEmpty() || end.isEmpty()) {
            return false;
        }

        try {
            LocalDate selectedDate = LocalDate.parse(date);
            LocalDate today = LocalDate.now();

            if (selectedDate.isBefore(today)) {
                return false;
            }

            LocalTime endTimeValue = parseTime(end);
            if (endTimeValue == null) {
                return false;
            }

            if (selectedDate.isEqual(today)
                    && !LocalTime.now().isBefore(endTimeValue)) {
                return false;
            }

            DocumentReference amenityRef =
                    db.collection(AMENITIES_COLLECTION)
                            .document(email)
                            .collection(AMENITIES_SUBCOLLECTION)
                            .document(amenity);

            QuerySnapshot residentsSnapshot =
                    amenityRef.collection(BOOKINGS_SUBCOLLECTION)
                            .get()
                            .get();

            for (DocumentSnapshot residentDocument : residentsSnapshot.getDocuments()) {
                if (residentDocument == null || !residentDocument.exists()) {
                    continue;
                }

                QuerySnapshot bookingSnapshot =
                        residentDocument.getReference()
                                .collection(BOOKING_DATA_SUBCOLLECTION)
                                .get()
                                .get();

                for (DocumentSnapshot booking : bookingSnapshot.getDocuments()) {
                    if (booking == null || !booking.exists()) {
                        continue;
                    }

                    String status = getString(booking, "bookingStatus");
                    if (!"ACCEPTED".equalsIgnoreCase(status)) {
                        continue;
                    }

                    String existingDate = getString(booking, "bookingDate");
                    String existingStart = getString(booking, "startTime");
                    String existingEnd = getString(booking, "endTime");

                    if (!date.equals(existingDate)
                            || !start.equals(existingStart)
                            || !end.equals(existingEnd)) {
                        continue;
                    }

                    // Same accepted booking blocks this exact slot until its end time.
                    if (selectedDate.isEqual(today)) {
                        LocalTime existingEndTime = parseTime(existingEnd);
                        if (existingEndTime != null
                                && !LocalTime.now().isBefore(existingEndTime)) {
                            continue;
                        }
                    }

                    System.out.println("========================================");
                    System.out.println("SLOT UNAVAILABLE");
                    System.out.println("Amenity ID: " + amenity);
                    System.out.println("Date: " + date);
                    System.out.println("Time: " + start + " - " + end);
                    System.out.println("Status: ACCEPTED");
                    System.out.println("========================================");
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            System.err.println("Error checking slot availability.");
            e.printStackTrace();
            return false;
        }
    }

    public String saveBooking(
            String secretaryEmail,
            String amenityId,
            String amenityName,
            String bookingDate,
            String startTime,
            String endTime,
            String flatNo,
            String residentName,
            String residentEmail,
            String bookingStatus,
            String paymentStatus,
            String paymentAmount) {

        String email = normalize(secretaryEmail);
        String amenity = normalize(amenityId);
        String resident = normalize(residentEmail);

        if (email.isEmpty() || amenity.isEmpty() || resident.isEmpty()) {
            return null;
        }

        try {
            // Final Firestore check after payment and immediately before write.
            if (!isSlotAvailable(
                    email,
                    amenity,
                    bookingDate,
                    startTime,
                    endTime)) {
                System.out.println("Booking stopped: slot is unavailable.");
                return null;
            }

            String bookingId = UUID.randomUUID().toString();

            DocumentReference residentRef =
                    db.collection(AMENITIES_COLLECTION)
                            .document(email)
                            .collection(AMENITIES_SUBCOLLECTION)
                            .document(amenity)
                            .collection(BOOKINGS_SUBCOLLECTION)
                            .document(resident);

            CollectionReference bookingDataRef =
                    residentRef.collection(BOOKING_DATA_SUBCOLLECTION);

            QuerySnapshot existingBookings = bookingDataRef.get().get();
            int bookingNumber = existingBookings.size() + 1;
            String bookingDocumentId = "booking" + bookingNumber;

            // Prevent accidental overwrite if booking numbers have gaps.
            while (bookingDataRef.document(bookingDocumentId).get().get().exists()) {
                bookingNumber++;
                bookingDocumentId = "booking" + bookingNumber;
            }

            Map<String, Object> booking = new HashMap<>();
            booking.put("bookingId", bookingId);
            booking.put("residentName", safe(residentName));
            booking.put("flatNo", safe(flatNo));
            booking.put("bookingDate", safe(bookingDate));
            booking.put("startTime", safe(startTime));
            booking.put("endTime", safe(endTime));
            booking.put("paymentAmount", safe(paymentAmount));
            booking.put("paymentStatus", safe(paymentStatus));
            booking.put("bookingStatus", safe(bookingStatus));
            booking.put("residentEmail", resident);
            booking.put("amenityName", safe(amenityName));

            bookingDataRef.document(bookingDocumentId)
                    .set(booking)
                    .get();

            System.out.println("Booking saved successfully.");
            System.out.println("Booking Path: "
                    + bookingDataRef.document(bookingDocumentId).getPath());
            System.out.println("Booking ID: " + bookingId);

            return bookingId;

        } catch (Exception e) {
            System.err.println("Error saving amenity booking.");
            e.printStackTrace();
            return null;
        }
    }

    public List<Map<String, Object>> getResidentBookings(String residentEmail) {
        List<Map<String, Object>> bookings = new ArrayList<>();
        String resident = normalize(residentEmail);

        if (resident.isEmpty()) {
            return bookings;
        }

        try {
            /*
             * IMPORTANT:
             * Do not enumerate Amenities/{secretary} parent documents here.
             * A Firestore document can have subcollections while having no
             * fields of its own. In that case collection.get() can return zero
             * parent documents even though bookingData exists below it.
             *
             * collectionGroup("bookingData") directly finds the actual
             * booking documents and therefore follows the real structure:
             * Amenities/{secretary}/amenities/{amenity}/bookings/{resident}/bookingData/{booking}
             */
            Query query = db.collectionGroup(BOOKING_DATA_SUBCOLLECTION);
            QuerySnapshot snapshot = query.get().get();

            System.out.println("========================================");
            System.out.println("FETCHING RESIDENT AMENITY BOOKINGS");
            System.out.println("Logged-in Resident Email: " + resident);
            System.out.println("Booking Documents Found: " + snapshot.size());

            for (DocumentSnapshot bookingDocument : snapshot.getDocuments()) {
                if (bookingDocument == null || !bookingDocument.exists()) {
                    continue;
                }

                DocumentReference bookingRef = bookingDocument.getReference();
                DocumentReference residentRef = bookingRef.getParent().getParent();

                if (residentRef == null
                        || !resident.equalsIgnoreCase(residentRef.getId())) {
                    continue;
                }

                CollectionReference bookingsRef = residentRef.getParent();
                if (bookingsRef == null) {
                    continue;
                }

                DocumentReference amenityRef = bookingsRef.getParent();
                if (amenityRef == null) {
                    continue;
                }

                CollectionReference amenitiesRef = amenityRef.getParent();
                if (amenitiesRef == null) {
                    continue;
                }

                DocumentReference secretaryRef = amenitiesRef.getParent();
                if (secretaryRef == null) {
                    continue;
                }

                Map<String, Object> data = new HashMap<>();
                Map<String, Object> firestoreData = bookingDocument.getData();
                if (firestoreData != null) {
                    data.putAll(firestoreData);
                }

                String secretaryEmail = secretaryRef.getId();
                String amenityId = amenityRef.getId();
                String amenityName = "";

                // Amenity parent may itself be field-less, so reading by direct reference is safe.
                DocumentSnapshot amenityDocument = amenityRef.get().get();
                if (amenityDocument.exists()) {
                    amenityName = getString(amenityDocument, "amenityName");
                }

                if (amenityName.isEmpty()) {
                    amenityName = getString(bookingDocument, "amenityName");
                }

                data.put("bookingDocumentId", bookingDocument.getId());
                data.put("bookingId", getString(bookingDocument, "bookingId"));
                data.put("amenityName", amenityName);
                data.put("amenityId", amenityId);
                data.put("secretaryEmail", secretaryEmail);
                data.put("residentEmail", resident);

                String bookingStatus = getString(bookingDocument, "bookingStatus");
                if (bookingStatus.isEmpty()) {
                    bookingStatus = "PENDING";
                }
                data.put("bookingStatus", bookingStatus);

                String paymentStatus = getString(bookingDocument, "paymentStatus");
                if (paymentStatus.isEmpty()) {
                    paymentStatus = "UNKNOWN";
                }
                data.put("paymentStatus", paymentStatus);

                bookings.add(data);

                System.out.println("Booking found: "
                        + bookingDocument.getId()
                        + " | Amenity: " + amenityName
                        + " | Status: " + bookingStatus);
            }

            System.out.println("TOTAL RESIDENT BOOKINGS FETCHED: " + bookings.size());
            System.out.println("Resident Email: " + resident);
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("Error fetching resident amenity bookings.");
            e.printStackTrace();
        }

        return bookings;
    }

    public boolean updateBookingStatus(
            String secretaryEmail,
            String amenityId,
            String residentEmail,
            String bookingDocumentId,
            String status) {

        String email = normalize(secretaryEmail);
        String amenity = normalize(amenityId);
        String resident = normalize(residentEmail);
        String documentId = safe(bookingDocumentId);
        String newStatus = safe(status);

        if (email.isEmpty() || amenity.isEmpty() || resident.isEmpty()
                || documentId.isEmpty() || newStatus.isEmpty()) {
            return false;
        }

        try {
            DocumentReference bookingRef =
                    db.collection(AMENITIES_COLLECTION)
                            .document(email)
                            .collection(AMENITIES_SUBCOLLECTION)
                            .document(amenity)
                            .collection(BOOKINGS_SUBCOLLECTION)
                            .document(resident)
                            .collection(BOOKING_DATA_SUBCOLLECTION)
                            .document(documentId);

            DocumentSnapshot booking = bookingRef.get().get();
            if (!booking.exists()) {
                System.err.println("Booking not found: " + bookingRef.getPath());
                return false;
            }

            String currentStatus = getString(booking, "bookingStatus");

            if ("ACCEPTED".equalsIgnoreCase(newStatus)) {
                // If already accepted, nothing needs to be changed.
                if ("ACCEPTED".equalsIgnoreCase(currentStatus)) {
                    return true;
                }

                String bookingDate = getString(booking, "bookingDate");
                String startTime = getString(booking, "startTime");
                String endTime = getString(booking, "endTime");

                // Another accepted booking must not already occupy this slot.
                // The current booking is PENDING/REJECTED, so it does not block itself.
                if (!isSlotAvailable(
                        email,
                        amenity,
                        bookingDate,
                        startTime,
                        endTime)) {
                    System.err.println(
                            "Cannot accept booking: the slot is already accepted."
                    );
                    return false;
                }
            }

            bookingRef.update("bookingStatus", newStatus).get();

            System.out.println("Booking status updated.");
            System.out.println("Path: " + bookingRef.getPath());
            System.out.println("Status: " + newStatus);

            return true;

        } catch (Exception e) {
            System.err.println("Error updating booking status.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePaymentStatus(
            String secretaryEmail,
            String amenityId,
            String residentEmail,
            String bookingDocumentId,
            String paymentStatus) {

        String email = normalize(secretaryEmail);
        String amenity = normalize(amenityId);
        String resident = normalize(residentEmail);
        String documentId = safe(bookingDocumentId);
        String newPaymentStatus = safe(paymentStatus);

        if (email.isEmpty() || amenity.isEmpty() || resident.isEmpty()
                || documentId.isEmpty() || newPaymentStatus.isEmpty()) {
            return false;
        }

        try {
            DocumentReference bookingRef =
                    db.collection(AMENITIES_COLLECTION)
                            .document(email)
                            .collection(AMENITIES_SUBCOLLECTION)
                            .document(amenity)
                            .collection(BOOKINGS_SUBCOLLECTION)
                            .document(resident)
                            .collection(BOOKING_DATA_SUBCOLLECTION)
                            .document(documentId);

            bookingRef.update("paymentStatus", newPaymentStatus).get();
            return true;

        } catch (Exception e) {
            System.err.println("Error updating payment status.");
            e.printStackTrace();
            return false;
        }
    }

    private static LocalTime parseTime(String value) {
        try {
            return LocalTime.parse(value.trim().toUpperCase(Locale.ENGLISH), TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getString(DocumentSnapshot document, String field) {
        if (document == null || field == null) {
            return "";
        }
        String value = document.getString(field);
        return safe(value);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ENGLISH);
    }

    private static String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
