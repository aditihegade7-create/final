package com.society.dao.Welcome;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseAuthConfig;
import com.society.config.FirebaseConfig;
import com.society.model.Welcome.User;

public class UserDao {

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore db =
            FirebaseConfig.getFirestore();

    // =========================================================
    // HTTP CLIENT
    // =========================================================

    private final HttpClient httpClient =
            HttpClient.newHttpClient();

    // =========================================================
    // LOGIN SESSION
    // =========================================================

    private static String loggedInEmail;
    private static String loggedInRole;

    // =========================================================
    // SET LOGGED-IN EMAIL
    // =========================================================

    public static void setLoggedInEmail(String email) {

        if (email == null ||
                email.trim().isEmpty()) {

            loggedInEmail = null;

        } else {

            loggedInEmail =
                    email.trim().toLowerCase();
        }
    }

    // =========================================================
    // GET LOGGED-IN EMAIL
    // =========================================================

    public static String getLoggedInEmail() {

        return loggedInEmail;
    }

    // =========================================================
    // SET LOGGED-IN ROLE
    // =========================================================

    public static void setLoggedInRole(String role) {

        if (role == null ||
                role.trim().isEmpty()) {

            loggedInRole = null;

        } else {

            loggedInRole =
                    role.trim();
        }
    }

    // =========================================================
    // GET LOGGED-IN ROLE
    // =========================================================

    public static String getLoggedInRole() {

        return loggedInRole;
    }

    // =========================================================
    // CLEAR SESSION
    // =========================================================

    public static void clearLoggedInUser() {

        loggedInEmail = null;
        loggedInRole = null;

        System.out.println(
                "Logged-in user session cleared.");
    }

    // =========================================================
    // COLLECTION NAME
    // =========================================================

    private String getCollectionName(String role) {

        if (role == null) {
            return null;
        }

        switch (role.trim().toLowerCase()) {

            case "resident":
                return "Residents";

            case "owner":
                return "Owners";

            case "secretary":
                return "Secretaries";

            case "guard":
            case "security":
                return "Guards";

            default:
                return null;
        }
    }

    // =========================================================
    // SAVE USER TO FIRESTORE
    // EMAIL = DOCUMENT ID
    // =========================================================

    public boolean saveUser(User user) {

        try {

            // -------------------------------------------------
            // USER VALIDATION
            // -------------------------------------------------

            if (user == null) {

                System.out.println(
                        "User cannot be null.");

                return false;
            }

            // -------------------------------------------------
            // EMAIL VALIDATION
            // -------------------------------------------------

            if (user.getEmail() == null ||
                    user.getEmail().trim().isEmpty()) {

                System.out.println(
                        "Email cannot be empty.");

                return false;
            }

            // -------------------------------------------------
            // ROLE VALIDATION
            // -------------------------------------------------

            if (user.getRole() == null ||
                    user.getRole().trim().isEmpty()) {

                System.out.println(
                        "Role cannot be empty.");

                return false;
            }

            // -------------------------------------------------
            // COLLECTION
            // -------------------------------------------------

            String collection =
                    getCollectionName(
                            user.getRole());

            if (collection == null) {

                System.out.println(
                        "Invalid role: "
                                + user.getRole());

                return false;
            }

            // -------------------------------------------------
            // CLEAN EMAIL
            // -------------------------------------------------

            String email =
                    user.getEmail()
                            .trim()
                            .toLowerCase();

            // =================================================
            // CREATE FIRESTORE DATA
            // =================================================

            Map<String, Object> data =
                    new HashMap<>();

            // =================================================
            // COMMON DATA
            // =================================================

            data.put(
                    "name",
                    safe(user.getName()));

            data.put(
                    "email",
                    email);

            data.put(
                    "phone",
                    safe(user.getPhone()));

            data.put(
                    "dob",
                    safe(user.getDob()));

            data.put(
                    "gender",
                    safe(user.getGender()));

            data.put(
                    "flatNo",
                    safe(user.getFlatNo()));

            data.put(
                    "aadhar",
                    safe(user.getAadhar()));

            data.put(
                    "society",
                    safe(user.getSociety()));

            data.put(
                    "ownerName",
                    safe(user.getOwnerName()));

            data.put(
                    "address",
                    safe(user.getAddress()));

            data.put(
                    "joiningDate",
                    safe(user.getJoiningDate()));

            data.put(
                    "role",
                    safe(user.getRole()));

            data.put(
                    "status",
                    safe(user.getStatus()));

            data.put(
                    "memberSince",
                    safe(user.getMemberSince()));

            // =================================================
            // GUARD
            // =================================================
            //
            // Guard signup मधून येणारा पूर्ण data:
            //
            // 1. name
            // 2. email
            // 3. phone
            // 4. dob
            // 5. gender
            // 6. aadhar
            // 7. society
            // 8. joiningDate
            // 9. role
            // 10. status
            // 11. memberSince
            //
            // Guard साठी flatNo, ownerName आणि address
            // रिकामे असतील, पण ते fields सुद्धा Firestore
            // मध्ये save होतील.
            //
            // =================================================

            if (user.getRole()
                    .trim()
                    .equalsIgnoreCase("Guard") ||
                user.getRole()
                    .trim()
                    .equalsIgnoreCase("Security")) {

                // Guard required fields
                data.put(
                        "name",
                        safe(user.getName()));

                data.put(
                        "email",
                        email);

                data.put(
                        "phone",
                        safe(user.getPhone()));

                data.put(
                        "dob",
                        safe(user.getDob()));

                data.put(
                        "gender",
                        safe(user.getGender()));

                data.put(
                        "aadhar",
                        safe(user.getAadhar()));

                data.put(
                        "society",
                        safe(user.getSociety()));

                data.put(
                        "joiningDate",
                        safe(user.getJoiningDate()));

                data.put(
                        "role",
                        safe(user.getRole()));

                data.put(
                        "status",
                        safe(user.getStatus()));

                data.put(
                        "memberSince",
                        safe(user.getMemberSince()));

                // Guard साठी हे fields blank असतील
                data.put(
                        "flatNo",
                        safe(user.getFlatNo()));

                data.put(
                        "ownerName",
                        safe(user.getOwnerName()));

                data.put(
                        "address",
                        safe(user.getAddress()));

                System.out.println(
                        "======================================");

                System.out.println(
                        "GUARD DATA");

                System.out.println(
                        "Name          : "
                                + safe(user.getName()));

                System.out.println(
                        "Email         : "
                                + email);

                System.out.println(
                        "Phone         : "
                                + safe(user.getPhone()));

                System.out.println(
                        "DOB           : "
                                + safe(user.getDob()));

                System.out.println(
                        "Gender        : "
                                + safe(user.getGender()));

                System.out.println(
                        "Aadhar        : "
                                + safe(user.getAadhar()));

                System.out.println(
                        "Society       : "
                                + safe(user.getSociety()));

                System.out.println(
                        "Joining Date  : "
                                + safe(user.getJoiningDate()));

                System.out.println(
                        "Role          : "
                                + safe(user.getRole()));

                System.out.println(
                        "Status        : "
                                + safe(user.getStatus()));

                System.out.println(
                        "Member Since  : "
                                + safe(user.getMemberSince()));

                System.out.println(
                        "======================================");
            }

            // =================================================
            // SAVE TO FIRESTORE
            // =================================================

            db.collection(collection)
                    .document(email)
                    .set(data)
                    .get();

            // =================================================
            // SUCCESS
            // =================================================

            System.out.println(
                    "======================================");

            System.out.println(
                    "USER SAVED SUCCESSFULLY");

            System.out.println(
                    "Collection : "
                            + collection);

            System.out.println(
                    "Document ID : "
                            + email);

            System.out.println(
                    "Role : "
                            + user.getRole());

            System.out.println(
                    "======================================");

            return true;

        } catch (Exception e) {

            System.out.println(
                    "======================================");

            System.out.println(
                    "ERROR WHILE SAVING USER");

            System.out.println(
                    "======================================");

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // FIREBASE SIGN UP
    // =========================================================

    public boolean signUp(
            String email,
            String password) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }

            if (password == null ||
                    password.isEmpty()) {

                return false;
            }

            String url =
                    "https://identitytoolkit.googleapis.com/v1/"
                            + "accounts:signUp?key="
                            + FirebaseAuthConfig.WEB_API_KEY;

            JSONObject requestBody =
                    new JSONObject();

            requestBody.put(
                    "email",
                    email.trim().toLowerCase());

            requestBody.put(
                    "password",
                    password);

            requestBody.put(
                    "returnSecureToken",
                    true);

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header(
                                    "Content-Type",
                                    "application/json")
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    requestBody.toString()))
                            .build();

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers
                                    .ofString());

            System.out.println(
                    "Firebase SignUp Response Code: "
                            + response.statusCode());

            if (response.statusCode() == 200) {

                System.out.println(
                        "Firebase account created successfully.");

                return true;
            }

            printFirebaseError(
                    response.body(),
                    "Firebase Signup Error");

            return false;

        } catch (Exception e) {

            System.out.println(
                    "Signup exception:");

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // FIREBASE LOGIN
    // =========================================================

    public boolean authenticateUser(
            String email,
            String password) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }

            if (password == null ||
                    password.isEmpty()) {

                return false;
            }

            String loginEmail =
                    email.trim().toLowerCase();

            String url =
                    "https://identitytoolkit.googleapis.com/v1/"
                            + "accounts:signInWithPassword?key="
                            + FirebaseAuthConfig.WEB_API_KEY;

            JSONObject requestBody =
                    new JSONObject();

            requestBody.put(
                    "email",
                    loginEmail);

            requestBody.put(
                    "password",
                    password);

            requestBody.put(
                    "returnSecureToken",
                    true);

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header(
                                    "Content-Type",
                                    "application/json")
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    requestBody.toString()))
                            .build();

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers
                                    .ofString());

            System.out.println(
                    "Firebase Auth Response Code: "
                            + response.statusCode());

            if (response.statusCode() == 200) {

                JSONObject responseJson =
                        new JSONObject(
                                response.body());

                String localId =
                        responseJson.optString(
                                "localId");

                setLoggedInEmail(
                        loginEmail);

                System.out.println(
                        "Firebase Authentication Successful.");

                System.out.println(
                        "Logged-in Email: "
                                + loggedInEmail);

                System.out.println(
                        "User ID: "
                                + localId);

                return true;
            }

            printFirebaseError(
                    response.body(),
                    "Firebase Login Error");

            return false;

        } catch (Exception e) {

            System.out.println(
                    "Authentication error:");

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET USER BY EMAIL
    // =========================================================

    public User getUserByEmail(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return null;
            }

            String cleanEmail =
                    email.trim().toLowerCase();

            String[] collections = {
                    "Residents",
                    "Owners",
                    "Secretaries",
                    "Guards"
            };

            for (String collection :
                    collections) {

                // First try the email as the document ID.
                DocumentSnapshot document =
                        db.collection(collection)
                                .document(cleanEmail)
                                .get()
                                .get();

                // If the document ID is different,
                // fall back to the email field.
                if (!document.exists()) {
                    QuerySnapshot querySnapshot =
                            db.collection(collection)
                                    .whereEqualTo(
                                            "email",
                                            cleanEmail)
                                    .limit(1)
                                    .get()
                                    .get();

                    if (!querySnapshot.isEmpty()) {
                        document =
                                querySnapshot.getDocuments()
                                        .get(0);
                    }
                }

                if (document.exists()) {

                    User user =
                            document.toObject(
                                    User.class);

                    if (user == null) {
                        continue;
                    }

                    // -------------------------------------------------
                    // RESIDENT / TENANT COMPATIBILITY
                    // -------------------------------------------------
                    // AddTenant stores tenants in the Residents
                    // collection and identifies them using
                    // residentType = "Tenant". Older resident records
                    // may not contain a separate "role" field.
                    // The login page needs the role to decide which
                    // portal to open, so treat both Resident and Tenant
                    // records as the Resident role.
                    // -------------------------------------------------

                    if (collection.equals("Residents") &&
                            (user.getRole() == null ||
                             user.getRole().trim().isEmpty())) {

                        String residentType =
                                document.getString("residentType");

                        if (residentType != null &&
                                !residentType.trim().isEmpty()) {

                            if (residentType.equalsIgnoreCase("Tenant") ||
                                    residentType.equalsIgnoreCase("Resident")) {

                                user.setRole("Resident");
                            }
                        }
                    }

                    System.out.println(
                            "User found in "
                                    + collection);

                    System.out.println(
                            "Resolved Role: "
                                    + user.getRole());

                    return user;
                }
            }

            System.out.println(
                    "No user found with email: "
                            + cleanEmail);

        } catch (Exception e) {

            System.out.println(
                    "Error while searching user:");

            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // GET USER BY EMAIL + ROLE
    // =========================================================

    public User getUser(
            String email,
            String role) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return null;
            }

            String collection =
                    getCollectionName(role);

            if (collection == null) {
                return null;
            }

            String cleanEmail =
                    email.trim().toLowerCase();

            DocumentSnapshot document =
                    db.collection(collection)
                            .document(cleanEmail)
                            .get()
                            .get();

            if (document.exists()) {

                User user =
                        document.toObject(
                                User.class);

                System.out.println(
                        "User fetched from "
                                + collection
                                + " / "
                                + cleanEmail);

                return user;
            }

            System.out.println(
                    "No user found in "
                            + collection
                            + " with email "
                            + cleanEmail);

        } catch (Exception e) {

            System.out.println(
                    "Error while getting user:");

            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // GET SECRETARY BY EMAIL
    // =========================================================

    public User getSecretaryByEmail(
            String email) {

        return getUser(
                email,
                "Secretary");
    }

    // =========================================================
    // GET LOGGED-IN SECRETARY
    // =========================================================

    public User getLoggedInSecretary() {

        String email =
                getLoggedInEmail();

        if (email == null ||
                email.trim().isEmpty()) {

            System.out.println(
                    "No logged-in email found.");

            return null;
        }

        System.out.println(
                "Fetching Secretary profile for: "
                        + email);

        return getSecretaryByEmail(email);
    }

    // =========================================================
    // GET USER ROLE
    // =========================================================

    public String getUserRole(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return null;
            }

            String cleanEmail =
                    email.trim().toLowerCase();

            // -------------------------------------------------
            // RESIDENT / TENANT
            // -------------------------------------------------
            DocumentSnapshot residentDocument =
                    db.collection("Residents")
                            .document(cleanEmail)
                            .get()
                            .get();

            // Support resident documents whose document ID
            // is not the email but whose email field is correct.
            if (!residentDocument.exists()) {
                QuerySnapshot residentQuery =
                        db.collection("Residents")
                                .whereEqualTo(
                                        "email",
                                        cleanEmail)
                                .limit(1)
                                .get()
                                .get();

                if (!residentQuery.isEmpty()) {
                    residentDocument =
                            residentQuery.getDocuments()
                                    .get(0);
                }
            }

            if (residentDocument.exists()) {

                String role =
                        residentDocument.getString("role");

                if (role != null &&
                        !role.trim().isEmpty()) {

                    return role.trim();
                }

                String residentType =
                        residentDocument.getString("residentType");

                if (residentType != null &&
                        !residentType.trim().isEmpty()) {

                    if (residentType.equalsIgnoreCase("Tenant") ||
                            residentType.equalsIgnoreCase("Resident")) {

                        System.out.println(
                                "Tenant/Resident profile found. "
                                        + "Resolved role: Resident");

                        return "Resident";
                    }
                }
            }

            // -------------------------------------------------
            // OTHER USER TYPES
            // -------------------------------------------------
            String[] collections = {
                    "Owners",
                    "Secretaries",
                    "Guards"
            };

            for (String collection : collections) {

                DocumentSnapshot document =
                        db.collection(collection)
                                .document(cleanEmail)
                                .get()
                                .get();

                if (!document.exists()) {
                    QuerySnapshot querySnapshot =
                            db.collection(collection)
                                    .whereEqualTo(
                                            "email",
                                            cleanEmail)
                                    .limit(1)
                                    .get()
                                    .get();

                    if (!querySnapshot.isEmpty()) {
                        document =
                                querySnapshot.getDocuments()
                                        .get(0);
                    }
                }

                if (document.exists()) {

                    String role =
                            document.getString("role");

                    if (role != null &&
                            !role.trim().isEmpty()) {

                        return role.trim();
                    }

                    if (collection.equals("Owners")) {
                        return "Owner";
                    }

                    if (collection.equals("Secretaries")) {
                        return "Secretary";
                    }

                    if (collection.equals("Guards")) {
                        return "Guard";
                    }
                }
            }

            System.out.println(
                    "No user profile found with email: "
                            + cleanEmail);

        } catch (Exception e) {

            System.out.println(
                    "Error while getting user role:");

            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // GET RESIDENT BY FLAT NUMBER
    // =========================================================

    public User getResidentByFlatNo(
            String flatNo) {

        try {

            if (flatNo == null ||
                    flatNo.trim().isEmpty()) {

                return null;
            }

            QuerySnapshot snapshot =
                    db.collection("Residents")
                            .whereEqualTo(
                                    "flatNo",
                                    flatNo.trim())
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                User resident =
                        document.toObject(
                                User.class);

                if (resident != null) {
                    return resident;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error while finding resident:");

            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // GET ALL USERS
    // =========================================================

    public List<User> getUsers(
            String role) {

        List<User> list =
                new ArrayList<>();

        try {

            String collection =
                    getCollectionName(role);

            if (collection == null) {
                return list;
            }

            QuerySnapshot snapshot =
                    db.collection(collection)
                            .get()
                            .get();

            for (DocumentSnapshot doc :
                    snapshot.getDocuments()) {

                User user =
                        doc.toObject(
                                User.class);

                if (user != null) {
                    list.add(user);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error while getting users:");

            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // UPDATE USER
    // =========================================================

    public boolean updateUser(
            User user) {

        try {

            if (user == null ||
                    user.getEmail() == null ||
                    user.getEmail()
                            .trim()
                            .isEmpty()) {

                return false;
            }

            String collection =
                    getCollectionName(
                            user.getRole());

            if (collection == null) {
                return false;
            }

            String email =
                    user.getEmail()
                            .trim()
                            .toLowerCase();

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "name",
                    safe(user.getName()));

            updates.put(
                    "phone",
                    safe(user.getPhone()));

            updates.put(
                    "email",
                    email);

            updates.put(
                    "dob",
                    safe(user.getDob()));

            updates.put(
                    "gender",
                    safe(user.getGender()));

            updates.put(
                    "flatNo",
                    safe(user.getFlatNo()));

            updates.put(
                    "aadhar",
                    safe(user.getAadhar()));

            updates.put(
                    "society",
                    safe(user.getSociety()));

            updates.put(
                    "ownerName",
                    safe(user.getOwnerName()));

            updates.put(
                    "address",
                    safe(user.getAddress()));

            updates.put(
                    "joiningDate",
                    safe(user.getJoiningDate()));

            updates.put(
                    "role",
                    safe(user.getRole()));

            updates.put(
                    "status",
                    safe(user.getStatus()));

            db.collection(collection)
                    .document(email)
                    .update(updates)
                    .get();

            System.out.println(
                    "User profile updated successfully.");

            System.out.println(
                    "Collection: "
                            + collection);

            System.out.println(
                    "Document ID: "
                            + email);

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error while updating user:");

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // UPDATE SECRETARY PROFILE
    // =========================================================

    public boolean updateSecretaryProfile(
            String email,
            String name,
            String mobile,
            String society,
            String dob,
            String gender,
            String address) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }

            String secretaryEmail =
                    email.trim().toLowerCase();

            DocumentSnapshot document =
                    db.collection("Secretaries")
                            .document(secretaryEmail)
                            .get()
                            .get();

            if (!document.exists()) {

                System.out.println(
                        "Secretary document not found: "
                                + secretaryEmail);

                return false;
            }

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "name",
                    safe(name));

            updates.put(
                    "email",
                    secretaryEmail);

            updates.put(
                    "phone",
                    safe(mobile));

            updates.put(
                    "society",
                    safe(society));

            updates.put(
                    "dob",
                    safe(dob));

            updates.put(
                    "gender",
                    safe(gender));

            updates.put(
                    "address",
                    safe(address));

            updates.put(
                    "role",
                    "Secretary");

            String existingStatus =
                    document.getString("status");

            if (existingStatus == null ||
                    existingStatus.trim().isEmpty()) {

                updates.put(
                        "status",
                        "Active");

            } else {

                updates.put(
                        "status",
                        existingStatus);
            }

            db.collection("Secretaries")
                    .document(secretaryEmail)
                    .update(updates)
                    .get();

            System.out.println(
                    "======================================");

            System.out.println(
                    "SECRETARY PROFILE UPDATED");

            System.out.println(
                    "Email: "
                            + secretaryEmail);

            System.out.println(
                    "======================================");

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error while updating Secretary profile:");

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // DELETE USER
    // =========================================================

    public boolean deleteUser(
            String email,
            String role) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }

            String collection =
                    getCollectionName(role);

            if (collection == null) {
                return false;
            }

            db.collection(collection)
                    .document(
                            email.trim()
                                    .toLowerCase())
                    .delete()
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safe(String value) {

        return value == null
                ? ""
                : value;
    }

    // =========================================================
    // FIREBASE ERROR
    // =========================================================

    private void printFirebaseError(
            String responseBody,
            String title) {

        try {

            JSONObject errorJson =
                    new JSONObject(responseBody);

            JSONObject errorObject =
                    errorJson.optJSONObject(
                            "error");

            String errorMessage =
                    errorObject != null
                            ? errorObject.optString(
                                    "message",
                                    "UNKNOWN_ERROR")
                            : "UNKNOWN_ERROR";

            System.out.println(
                    title + ": "
                            + errorMessage);

        } catch (Exception e) {

            System.out.println(
                    title + ": "
                            + responseBody);
        }
    }
}