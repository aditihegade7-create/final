package com.society.controller.welcome;

import java.time.LocalDate;
import java.util.List;

import com.society.dao.Welcome.UserDao;
import com.society.model.Welcome.User;

public class UserController {

    private final UserDao dao;

    public UserController() {
        dao = new UserDao();
    }

    // =====================================================
    // ADD USER
    // =====================================================
    // public boolean

    // addUser(
    // String name,
    // String email,
    // String password,
    // String role) {

    // try {

    // // =============================================
    // // VALIDATION
    // // =============================================

    // if (name == null || name.trim().isEmpty()) {
    // System.out.println("Name cannot be empty.");
    // return false;
    // }

    // if (email == null || email.trim().isEmpty()) {
    // System.out.println("Email cannot be empty.");
    // return false;
    // }

    // if (password == null || password.isEmpty()) {
    // System.out.println("Password cannot be empty.");
    // return false;
    // }

    // if (role == null || role.trim().isEmpty()) {
    // System.out.println("Role cannot be empty.");
    // return false;
    // }

    // // =============================================
    // // CREATE FIREBASE AUTH ACCOUNT
    // // =============================================

    // boolean authCreated = dao.signUp(
    // email.trim(),
    // password);

    // if (!authCreated) {

    // System.out.println(
    // "Firebase Authentication signup failed.");

    // return false;
    // }

    // // =============================================
    // // CREATE FIRESTORE PROFILE
    // // =============================================

    // User user = new User(
    // name.trim(),
    // email.trim(),
    // null,
    // role.trim());

    // boolean saved = dao.saveUser(user);

    // if (!saved) {

    // System.out.println(
    // "Authentication succeeded, "
    // + "but Firestore save failed.");

    // return false;
    // }

    // System.out.println(
    // "Signup and Firestore save successful.");

    // return true;

    // } catch (Exception e) {

    // System.out.println(
    // "Error while adding user:");

    // e.printStackTrace();

    // return false;
    // }
    // }

    public boolean addUser(
            String name,
            String email,
            String password,
            String role,
            String phone,
            String dob,
            String gender,
            String flatNo,
            String aadhar,
            String society,
            String ownerName,
            String address,
            String joiningDate) {

        try {

            // =================================================
            // BASIC VALIDATION
            // =================================================

            if (name == null ||
                    name.trim().isEmpty()) {

                System.out.println(
                        "Name cannot be empty.");

                return false;
            }

            if (email == null ||
                    email.trim().isEmpty()) {

                System.out.println(
                        "Email cannot be empty.");

                return false;
            }

            if (password == null ||
                    password.isEmpty()) {

                System.out.println(
                        "Password cannot be empty.");

                return false;
            }

            if (role == null ||
                    role.trim().isEmpty()) {

                System.out.println(
                        "Role cannot be empty.");

                return false;
            }

            if (phone == null ||
                    phone.trim().isEmpty()) {

                System.out.println(
                        "Phone cannot be empty.");

                return false;
            }

            // =================================================
            // CLEAN DATA
            // =================================================

            String cleanName = name.trim();

            String cleanEmail = email.trim().toLowerCase();

            String cleanRole = role.trim();

            String cleanPhone = phone.trim();

            String cleanDob = dob == null
                    ? ""
                    : dob.trim();

            String cleanGender = gender == null
                    ? ""
                    : gender.trim();

            String cleanFlatNo = flatNo == null
                    ? ""
                    : flatNo.trim();

            String cleanAadhar = aadhar == null
                    ? ""
                    : aadhar.trim();

            String cleanSociety = society == null
                    ? ""
                    : society.trim();

            String cleanOwnerName = ownerName == null
                    ? ""
                    : ownerName.trim();

            String cleanAddress = address == null
                    ? ""
                    : address.trim();

            String cleanJoiningDate = joiningDate == null
                    ? ""
                    : joiningDate.trim();

            // =================================================
            // CHECK EXISTING USER
            // =================================================

            User existingUser = dao.getUserByEmail(
                    cleanEmail);

            if (existingUser != null) {

                System.out.println(
                        "User already exists with email: "
                                + cleanEmail);

                return false;
            }

            // =================================================
            // FIREBASE AUTH SIGN UP
            // =================================================

            boolean authCreated = dao.signUp(
                    cleanEmail,
                    password);

            if (!authCreated) {

                System.out.println(
                        "Firebase Authentication signup failed.");

                return false;
            }

            // =================================================
            // CREATE USER OBJECT
            // =================================================

            User user = new User();

            user.setName(
                    cleanName);

            user.setEmail(
                    cleanEmail);

            user.setPhone(
                    cleanPhone);

            user.setRole(
                    cleanRole);

            user.setDob(
                    cleanDob);

            user.setGender(
                    cleanGender);

            user.setFlatNo(
                    cleanFlatNo);

            user.setAadhar(
                    cleanAadhar);

            user.setSociety(
                    cleanSociety);

            user.setOwnerName(
                    cleanOwnerName);

            user.setAddress(
                    cleanAddress);

            user.setJoiningDate(
                    cleanJoiningDate);

            user.setStatus(
                    "Active");

            user.setMemberSince(
                    LocalDate.now().toString());

            // =================================================
            // GUARD DATA DEBUG
            // =================================================

            if (cleanRole.equalsIgnoreCase("Guard")) {

                System.out.println(
                        "======================================");

                System.out.println(
                        "GUARD DATA BEFORE FIRESTORE SAVE");

                System.out.println(
                        "Name: " + user.getName());

                System.out.println(
                        "Email: " + user.getEmail());

                System.out.println(
                        "Phone: " + user.getPhone());

                System.out.println(
                        "DOB: " + user.getDob());

                System.out.println(
                        "Gender: " + user.getGender());

                System.out.println(
                        "Aadhar: " + user.getAadhar());

                System.out.println(
                        "Society: " + user.getSociety());

                System.out.println(
                        "Joining Date: "
                                + user.getJoiningDate());

                System.out.println(
                        "Role: " + user.getRole());

                System.out.println(
                        "Status: " + user.getStatus());

                System.out.println(
                        "Member Since: "
                                + user.getMemberSince());

                System.out.println(
                        "======================================");
            }

            // =================================================
            // SAVE ALL DATA TO FIRESTORE
            // =================================================

            boolean saved = dao.saveUser(user);

            if (!saved) {

                System.out.println(
                        "Authentication succeeded, "
                                + "but Firestore save failed.");

                return false;
            }

            // =================================================
            // SUCCESS
            // =================================================

            UserDao.setLoggedInEmail(
                    cleanEmail);

            UserDao.setLoggedInRole(
                    cleanRole);

            System.out.println(
                    "======================================");

            System.out.println(
                    "REGISTRATION SUCCESSFUL");

            System.out.println(
                    "Name: " + cleanName);

            System.out.println(
                    "Email: " + cleanEmail);

            System.out.println(
                    "Role: " + cleanRole);

            System.out.println(
                    "======================================");

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error while registering user:");

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // LOGIN
    // =====================================================

    public User login(
            String email,
            String password) {

        try {

            boolean authenticated = dao.authenticateUser(
                    email,
                    password);

            if (!authenticated) {

                System.out.println(
                        "Invalid email or password.");

                return null;
            }

            // Firebase authentication succeeded.
            // Now get the user's profile from Firestore.

            User user = dao.getUserByEmail(email);

            if (user == null) {

                System.out.println(
                        "Authentication successful, "
                                + "but user profile was not found.");

                return null;
            }

            System.out.println(
                    "Login successful.");

            System.out.println(
                    "Role: " + user.getRole());

            return user;

        } catch (Exception e) {

            System.out.println(
                    "Login error:");

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // GET USER
    // =====================================================

    public User getUser(
            String email,
            String role) {

        return dao.getUser(
                email,
                role);
    }

    // =====================================================
    // GET USER BY EMAIL
    // =====================================================

    public User getUserByEmail(
            String email) {

        return dao.getUserByEmail(
                email);
    }

    // =====================================================
    // GET USER ROLE
    // =====================================================

    public String getUserRole(
            String email) {

        return dao.getUserRole(
                email);
    }

    // =====================================================
    // UPDATE USER
    // =====================================================

    public boolean updateUser(
            String name,
            String email,
            String password,
            String role) {

        try {

            User user = new User(
                    name,
                    email,
                    null,
                    role);

            // Update Firestore profile
            boolean profileUpdated = dao.updateUser(user);

            // If password is not empty,
            // update it through Firebase Authentication.
            if (password != null &&
                    !password.isEmpty()) {

                // boolean passwordUpdated =
                // dao.updateFirebasePassword(
                // email,
                // password
                // );

                // return profileUpdated &&
                // passwordUpdated;
            }

            return profileUpdated;

        } catch (Exception e) {

            System.out.println(
                    "Error while updating user:");

            e.printStackTrace();

            return false;
        }
    }

    public User getResidentByFlatNo(String flatNo) {

        return dao.getResidentByFlatNo(flatNo);
    }
    // =====================================================
    // DELETE USER
    // =====================================================

    public boolean deleteUser(
            String email,
            String role) {

        return dao.deleteUser(
                email,
                role);
    }

    // =====================================================
    // GET ALL USERS
    // =====================================================

    public List<User> getAllUsers(
            String role) {

        return dao.getUsers(
                role);
    }
}