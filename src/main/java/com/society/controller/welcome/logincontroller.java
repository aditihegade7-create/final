package com.society.controller.welcome;

import com.society.dao.Welcome.UserDao;
import com.society.model.Welcome.User;
import com.society.view.Guard_portal.GuardDashboard;
import com.society.view.Owner_portal.OwnerDashboard;
import com.society.view.Resident_portal.ResidentDashboard;
import com.society.view.Secretary_portal.SecretaryDashboard;

import javafx.stage.Stage;

public class logincontroller {

        private final UserDao userDao = new UserDao();

        public void login(
                        String email,
                        String password,
                        Stage stage) {

                // ============================================
                // 1. FIREBASE AUTHENTICATION
                // ============================================

                boolean success = userDao.authenticateUser(
                                email,
                                password);

                if (!success) {

                        System.out.println(
                                        "Invalid email or password.");

                        return;
                }

                System.out.println(
                                "Authentication successful.");

                // ============================================
                // 2. GET ROLE FROM FIRESTORE
                // ============================================

                User user = userDao.getUserByEmail(email);

                String role = user == null ? null : user.getRole();

                if (role == null) {

                        System.out.println(
                                        "Role not found in Firestore.");

                        return;
                }

                System.out.println(
                                "User role: " + role);

                // ============================================
                // 3. OPEN DASHBOARD
                // ============================================

                switch (role.trim().toLowerCase()) {

                        case "resident":

                                ResidentDashboard residentDashboard = new ResidentDashboard();

                                stage.setScene(residentDashboard.getResidentDashboardScene(stage));

                                break;

                        case "owner":

                                stage.setScene(OwnerDashboard.createScene(stage, email));

                                break;

                        case "secretary":

                                SecretaryDashboard secretaryDashboard = new SecretaryDashboard(user);

                                stage.setScene(secretaryDashboard.createScene(stage));

                                break;

                        case "security":
                        case "guard":

                                stage.setScene(GuardDashboard.createScene(stage, email));

                                break;

                        default:

                                System.out.println(
                                                "Invalid role: " + role);
                }
        }
}
