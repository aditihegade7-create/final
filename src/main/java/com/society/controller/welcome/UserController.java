package com.society.controller.welcome;

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
    public boolean 
    
    addUser(
            String name,
            String email,
            String password,
            String role) {

        try {

            // =============================================
            // VALIDATION
            // =============================================

            if (name == null || name.trim().isEmpty()) {
                System.out.println("Name cannot be empty.");
                return false;
            }

            if (email == null || email.trim().isEmpty()) {
                System.out.println("Email cannot be empty.");
                return false;
            }

            if (password == null || password.isEmpty()) {
                System.out.println("Password cannot be empty.");
                return false;
            }

            if (role == null || role.trim().isEmpty()) {
                System.out.println("Role cannot be empty.");
                return false;
            }

            // =============================================
            // CREATE FIREBASE AUTH ACCOUNT
            // =============================================

            boolean authCreated = dao.signUp(
                    email.trim(),
                    password);

            if (!authCreated) {

                System.out.println(
                        "Firebase Authentication signup failed.");

                return false;
            }

            // =============================================
            // CREATE FIRESTORE PROFILE
            // =============================================

            User user = new User(
                    name.trim(),
                    email.trim(),
                    null,
                    role.trim());

            boolean saved = dao.saveUser(user);

            if (!saved) {

                System.out.println(
                        "Authentication succeeded, "
                                + "but Firestore save failed.");

                return false;
            }

            System.out.println(
                    "Signup and Firestore save successful.");

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error while adding user:");

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