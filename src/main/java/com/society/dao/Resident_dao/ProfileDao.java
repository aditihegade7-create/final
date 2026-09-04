package com.society.dao.Resident_dao;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.society.config.FirebaseAuthConfig;
import com.society.config.FirebaseConfig;
import com.society.model.Resident_model.ProfileModel;

public class ProfileDao {

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore db;


    // =========================================================
    // HTTP CLIENT
    // =========================================================

    private final HttpClient httpClient =
            HttpClient.newHttpClient();


    // =========================================================
    // COLLECTION
    // =========================================================

    private static final String COLLECTION =
            "Residents";


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ProfileDao() {

        db =
                FirebaseConfig.getFirestore();
    }


    // =========================================================
    // GET PROFILE
    //
    // Firestore:
    //
    // Residents/{email}
    // =========================================================

    public ProfileModel getProfile(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return null;
            }


            email =
                    email.trim()
                            .toLowerCase();


            DocumentSnapshot document =
                    db.collection(
                            COLLECTION
                    )
                    .document(
                            email
                    )
                    .get()
                    .get();


            if (!document.exists()) {

                System.out.println(
                        "Profile not found for: "
                                + email
                );

                return null;
            }


            ProfileModel profile =
                    new ProfileModel();



            // =========================================================
// PERSONAL INFORMATION
// =========================================================

profile.setEmail(
        getString(
                document,
                "email"
        )
);

profile.setName(
        getString(
                document,
                "name"
        )
);

profile.setPhone(
        getString(
                document,
                "phone"
        ));


// =========================================================
// FLAT NUMBER
//
// Firestore uses:
// flatNo
// =========================================================

profile.setFlat(
        getString(
                document,
                "flatNo"
        )
);


// =========================================================
// WING
// =========================================================

profile.setWing(
        getString(
                document,
                "wing"
        )
);


// =========================================================
// SOCIETY
// =========================================================

profile.setSocietyName(
        getString(
                document,
                "societyName"
        )
);


// =========================================================
// RESIDENT TYPE
//
// Firestore uses:
// role
// =========================================================

profile.setResidentType(
        getString(
                document,
                "role"
        )
);


// =========================================================
// STATUS
// =========================================================

String status =
        getString(
                document,
                "status"
        );


// If status doesn't exist in Firestore,
// resident accounts can safely display Active.
if (status.isEmpty()) {
    status = "Active";
}

profile.setStatus(
        status
);


// =========================================================
// PROFILE IMAGE
// =========================================================

profile.setProfileImageUrl(
        getString(
                document,
                "profileImageUrl"
        )
);

            // =================================================
            // WING
            //
            // Some existing records may not have wing.
            // =================================================

            String wing =
                    getString(
                            document,
                            "wing"
                    );


            profile.setWing(
                    wing
            );


            // =================================================
            // ACCOUNT INFORMATION
            // =================================================

            profile.setSocietyName(
                    getString(
                            document,
                            "societyName"
                    )
            );


            profile.setResidentType(
                    getString(
                            document,
                            "residentType"
                    )
            );


            profile.setStatus(
                    getString(
                            document,
                            "status"
                    )
            );


            // =================================================
            // PROFILE IMAGE
            // =================================================

            profile.setProfileImageUrl(
                    getString(
                            document,
                            "profileImageUrl"
                    )
            );


            return profile;


        } catch (Exception e) {

            System.out.println(
                    "Error loading resident profile: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return null;
        }
    }


    // =========================================================
    // UPDATE PERSONAL INFORMATION
    //
    // Only name and phone are changed.
    //
    // Email is NOT changed.
    // Flat and society are NOT changed here.
    // =========================================================

    public boolean updateProfile(
            String email,
            String name,
            String phone) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }


            if (name == null ||
                    name.trim().isEmpty()) {

                return false;
            }


            if (phone == null ||
                    phone.trim().isEmpty()) {

                return false;
            }


            email =
                    email.trim()
                            .toLowerCase();


            DocumentSnapshot document =
                    db.collection(
                            COLLECTION
                    )
                    .document(
                            email
                    )
                    .get()
                    .get();


            if (!document.exists()) {

                System.out.println(
                        "Resident not found: "
                                + email
                );

                return false;
            }


            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "name",
                    name.trim()
            );


            updates.put(
                    "phone",
                    phone.trim()
            );


            document.getReference()
                    .update(
                            updates
                    )
                    .get();


            System.out.println(
                    "Resident profile updated."
            );


            return true;


        } catch (Exception e) {

            System.out.println(
                    "Error updating profile: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // UPDATE PROFILE IMAGE
    //
    // Cloudinary URL is stored in:
    //
    // Residents/{email}/profileImageUrl
    // =========================================================

    public boolean updateProfileImage(
            String email,
            String profileImageUrl) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }


            if (profileImageUrl == null ||
                    profileImageUrl.trim().isEmpty()) {

                return false;
            }


            email =
                    email.trim()
                            .toLowerCase();


            DocumentSnapshot document =
                    db.collection(
                            COLLECTION
                    )
                    .document(
                            email
                    )
                    .get()
                    .get();


            if (!document.exists()) {

                System.out.println(
                        "Resident not found: "
                                + email
                );

                return false;
            }


            document.getReference()
                    .update(
                            "profileImageUrl",
                            profileImageUrl.trim()
                    )
                    .get();


            System.out.println(
                    "Resident profile image updated."
            );


            return true;


        } catch (Exception e) {

            System.out.println(
                    "Error updating profile image: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // CHANGE PASSWORD
    //
    // Firebase Authentication
    //
    // Password is NOT stored in Firestore.
    // =========================================================

    public boolean changePassword(
            String email,
            String currentPassword,
            String newPassword) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }


            if (currentPassword == null ||
                    currentPassword.isEmpty()) {

                return false;
            }


            if (newPassword == null ||
                    newPassword.isEmpty()) {

                return false;
            }


            email =
                    email.trim();


            // =================================================
            // STEP 1
            // VERIFY CURRENT PASSWORD
            // =================================================

            String loginUrl =
                    "https://identitytoolkit.googleapis.com/v1/"
                            + "accounts:signInWithPassword?key="
                            + FirebaseAuthConfig.WEB_API_KEY;


            JSONObject loginBody =
                    new JSONObject();


            loginBody.put(
                    "email",
                    email
            );


            loginBody.put(
                    "password",
                    currentPassword
            );


            loginBody.put(
                    "returnSecureToken",
                    true
            );


            HttpRequest loginRequest =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            loginUrl
                                    )
                            )
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    loginBody.toString()
                                            )
                            )
                            .build();


            HttpResponse<String> loginResponse =
                    httpClient.send(
                            loginRequest,
                            HttpResponse.BodyHandlers
                                    .ofString()
                    );


            if (
                    loginResponse.statusCode()
                            != 200
            ) {

                System.out.println(
                        "Current password is incorrect."
                );

                return false;
            }


            JSONObject loginJson =
                    new JSONObject(
                            loginResponse.body()
                    );


            String idToken =
                    loginJson.optString(
                            "idToken"
                    );


            if (idToken.isEmpty()) {

                return false;
            }


            // =================================================
            // STEP 2
            // UPDATE PASSWORD
            // =================================================

            String updateUrl =
                    "https://identitytoolkit.googleapis.com/v1/"
                            + "accounts:update?key="
                            + FirebaseAuthConfig.WEB_API_KEY;


            JSONObject updateBody =
                    new JSONObject();


            updateBody.put(
                    "idToken",
                    idToken
            );


            updateBody.put(
                    "password",
                    newPassword
            );


            updateBody.put(
                    "returnSecureToken",
                    true
            );


            HttpRequest updateRequest =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            updateUrl
                                    )
                            )
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    updateBody.toString()
                                            )
                            )
                            .build();


            HttpResponse<String> updateResponse =
                    httpClient.send(
                            updateRequest,
                            HttpResponse.BodyHandlers
                                    .ofString()
                    );


            if (
                    updateResponse.statusCode()
                            == 200
            ) {

                System.out.println(
                        "Password changed successfully."
                );

                return true;
            }


            System.out.println(
                    "Password change failed."
            );


            return false;


        } catch (Exception e) {

            System.out.println(
                    "Error changing password: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // SAFE FIRESTORE STRING
    // =========================================================

    private String getString(
            DocumentSnapshot document,
            String field) {

        String value =
                document.getString(
                        field
                );

        if (value == null) {
            return "";
        }

        return value;
    }

}