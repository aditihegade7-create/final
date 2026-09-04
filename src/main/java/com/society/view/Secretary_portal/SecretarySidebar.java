
package com.society.view.Secretary_portal;

import com.society.controller.welcome.UserController;
import com.society.dao.Welcome.UserDao;
import com.society.model.Welcome.User;
import com.society.view.Resident_portal.ProfilePage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SecretarySidebar {

    // ============================================================
    // LOGGED-IN USER
    // ============================================================

    private User loggedInUser;

    // ============================================================
    // CONSTRUCTOR 1
    // ============================================================

    public SecretarySidebar(User loggedInUser) {

        this.loggedInUser = loggedInUser;

        printUserInfo();
    }

    // ============================================================
    // CONSTRUCTOR 2
    // ============================================================

    public SecretarySidebar() {

        try {

            String email =
                    UserDao.getLoggedInEmail();

            if (email != null
                    && !email.trim().isEmpty()) {

                UserController controller =
                        new UserController();

                loggedInUser =
                        controller.getUserByEmail(
                                email.trim().toLowerCase());
            }

        } catch (Exception e) {

            System.out.println(
                    "Error loading logged-in user:");

            e.printStackTrace();

            loggedInUser = null;
        }

        printUserInfo();
    }

    // ============================================================
    // PRINT USER INFO
    // ============================================================

    private void printUserInfo() {

        System.out.println(
                "================================================");

        System.out.println(
                "SecretarySidebar created");

        System.out.println(
                "Logged-in Email: "
                        + getUserEmail());

        System.out.println(
                "Logged-in Role: "
                        + getUserRole());

        System.out.println(
                "================================================");
    }

    // ============================================================
    // GET USER EMAIL
    // ============================================================

    private String getUserEmail() {

        if (loggedInUser == null) {
            return "NULL";
        }

        if (loggedInUser.getEmail() == null) {
            return "NULL";
        }

        return loggedInUser.getEmail();
    }

    // ============================================================
    // GET USER ROLE
    // ============================================================

    private String getUserRole() {

        if (loggedInUser == null) {
            return "NULL";
        }

        if (loggedInUser.getRole() == null) {
            return "NULL";
        }

        return loggedInUser.getRole();
    }

    // ============================================================
    // GET LOGGED-IN USER
    // ============================================================

    public User getLoggedInUser() {

        return loggedInUser;
    }

    // ============================================================
    // CREATE SIDEBAR
    // ============================================================

    public VBox createSidebar(Stage stage) {

        VBox sidebar =
                new VBox();

        sidebar.setPrefWidth(280);
        sidebar.setMinWidth(280);
        sidebar.setPrefHeight(750);

        sidebar.setSpacing(10);

        sidebar.setPadding(
                new Insets(20));

        sidebar.setStyle(
                "-fx-background-color:#4e4b4b;");

        // ========================================================
        // LOGO
        // ========================================================

        Label logo =
                new Label("Society360");

        logo.setAlignment(
                Pos.CENTER_LEFT);

        logo.setStyle(
                "-fx-text-fill:white;"
                        + "-fx-font-size:24px;"
                        + "-fx-font-weight:bold;");

        // ========================================================
        // PANEL
        // ========================================================

        Label panel =
                new Label("Secretary Panel");

        panel.setStyle(
                "-fx-text-fill:lightgray;"
                        + "-fx-font-size:14px;"
                        + "-fx-padding:5px;");

        // ========================================================
        // DASHBOARD
        // ========================================================

        Button dashboardBtn =
                createMenuButton("Dashboard");

        dashboardBtn.setOnAction(e -> {

            System.out.println(
                    "Dashboard clicked. Email: "
                            + getUserEmail());

            SecretaryDashboard dashboard =
                    new SecretaryDashboard(
                            loggedInUser);

            stage.setScene(
                    dashboard.createScene(stage));
        });

        // ========================================================
        // MANAGE RESIDENTS
        // ========================================================

        Button residentsBtn =
                createMenuButton(
                        "Manage Residents");

        residentsBtn.setOnAction(e -> {

            System.out.println(
                    "Manage Residents clicked. Email: "
                            + getUserEmail());

            ManageResidents residents =
                    new ManageResidents();

            stage.setScene(
                    residents.createScene(stage));
        });

        // ========================================================
        // MANAGE OWNERS
        // ========================================================

        Button ownersBtn =
                createMenuButton(
                        "Manage Owners");

        ownersBtn.setOnAction(e -> {

            System.out.println(
                    "Manage Owners clicked. Email: "
                            + getUserEmail());

            ManageOwner owners =
                    new ManageOwner();

            stage.setScene(
                    owners.createScene(stage));
        });

        // ========================================================
        // MANAGE GUARDS
        // ========================================================

        Button guardsBtn =
                createMenuButton(
                        "Manage Guards");

        guardsBtn.setOnAction(e -> {

            System.out.println(
                    "Manage Guards clicked. Email: "
                            + getUserEmail());

            ManageGuard guards =
                    new ManageGuard();

            stage.setScene(
                    guards.createScene(stage));
        });

        // ========================================================
        // MANAGE NOTICES
        // ========================================================

        Button noticesBtn =
                createMenuButton(
                        "Manage Notices");

        noticesBtn.setOnAction(e -> {

            System.out.println(
                    "Manage Notices clicked. Email: "
                            + getUserEmail());

            if (loggedInUser == null) {

                System.out.println(
                        "ERROR: Logged-in user is NULL.");

                return;
            }

            ManageNotices notices =
                    new ManageNotices(
                            loggedInUser);

            stage.setScene(
                    notices.createScene(stage));
        });

        // ========================================================
        // MANAGE COMPLAINTS
        // ========================================================

        Button complaintsBtn =
                createMenuButton(
                        "Manage Complaints");

        complaintsBtn.setOnAction(e -> {

            System.out.println(
                    "Manage Complaints clicked. Email: "
                            + getUserEmail());

            ManageComplaints complaints =
                    new ManageComplaints();

            stage.setScene(
                    complaints.createScene(stage));
        });

        // ========================================================
        // MANAGE MAINTENANCE
        // ========================================================

        Button maintenanceBtn =
                createMenuButton(
                        "Manage Maintenance");

        maintenanceBtn.setOnAction(e -> {

            System.out.println(
                    "Manage Maintenance clicked. Email: "
                            + getUserEmail());

            ManageMaintenance maintenance =
                    new ManageMaintenance();

            stage.setScene(
                    maintenance.createScene(stage));
        });

        // ========================================================
        // MANAGE PAYMENTS
        // ========================================================

        Button paymentsBtn =
                createMenuButton(
                        "Manage Payments");

        paymentsBtn.setOnAction(e -> {

            System.out.println(
                    "Manage Payments clicked. Email: "
                            + getUserEmail());

            ManagePayment payment =
                    new ManagePayment();

            stage.setScene(
                    payment.createScene(stage));
        });

        // ========================================================
        // VIEW SOS
        // ========================================================

        Button sosBtn =
                createMenuButton(
                        "View SOS Alerts");

        sosBtn.setOnAction(e -> {

            System.out.println(
                    "View SOS clicked. Email: "
                            + getUserEmail());

            ViewSos sos =
                    new ViewSos();

            stage.setScene(
                    sos.createScene(stage));
        });

        // ========================================================
        // MANAGE EVENTS
        // ========================================================

        Button eventsBtn =
                createMenuButton(
                        "Manage Events");

        eventsBtn.setOnAction(e -> {

            System.out.println(
                    "Manage Events clicked. Email: "
                            + getUserEmail());

            ManageEvents events =
                    new ManageEvents();

            stage.setScene(
                    events.createScene(stage));
        });

        // ========================================================
        // Daily Reports REPORTS
        // ========================================================

        Button reportsBtn =
                createMenuButton(
                        "Daily Reports");

        reportsBtn.setOnAction(e -> {

            System.out.println(
                    "Daily Reports clicked. Email: "
                            + getUserEmail());

            GenerateReports report =
                    new GenerateReports();

            stage.setScene(
                    report.createScene(stage));
        });

        // ========================================================
        // PROFILE
        // ========================================================

        Button profileBtn =
                createMenuButton(
                        "Profile");

        profileBtn.setOnAction(e -> {

            System.out.println(
                    "================================================");

            System.out.println(
                    "Profile clicked");

            System.out.println(
                    "Profile Email: "
                            + getUserEmail());

            System.out.println(
                    "================================================");

            if (loggedInUser == null) {

                System.out.println(
                        "ERROR: Cannot open Profile.");

                return;
            }

            Profile profile =
                    new Profile();

            stage.setScene(
                    profile.createScene(stage));
        });

        // ========================================================
        // POLLS AND SURVEYS
        // ========================================================

        Button pollsBtn =
                createMenuButton(
                        "Polls and Surveys");

        pollsBtn.setOnAction(e -> {

            System.out.println(
                    "================================================");

            System.out.println(
                    "Polls and Surveys clicked");

            System.out.println(
                    "Logged-in Email: "
                            + getUserEmail());

            System.out.println(
                    "================================================");

            PollsandSurveys pollsAndSurveys =
                    new PollsandSurveys();

            stage.setScene(
                    pollsAndSurveys.createScene(stage));
        });

        // ========================================================
        // ADD ALL COMPONENTS
        // ========================================================

        sidebar.getChildren().addAll(

                logo,

                panel,

                dashboardBtn,

                residentsBtn,

                ownersBtn,

                guardsBtn,

                noticesBtn,

                complaintsBtn,

                maintenanceBtn,

                paymentsBtn,

                sosBtn,

                eventsBtn,

                reportsBtn,

                pollsBtn,

                profileBtn
        );

        return sidebar;
    }

    // ============================================================
    // CREATE MENU BUTTON
    // ============================================================

    private Button createMenuButton(
            String text) {

        Button button =
                new Button(text);

        button.setMaxWidth(
                Double.MAX_VALUE);

        button.setPrefHeight(40);

        button.setAlignment(
                Pos.CENTER_LEFT);

        // ========================================================
        // NORMAL STYLE
        // ========================================================

        button.setStyle(
                "-fx-background-color:#434141;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:white;"
                        + "-fx-font-size:14px;"
                        + "-fx-alignment:CENTER-LEFT;"
                        + "-fx-padding:0 15 0 15;"
                        + "-fx-cursor:hand;");

        // ========================================================
        // MOUSE ENTER
        // ========================================================

        button.setOnMouseEntered(e -> {

            button.setStyle(
                    "-fx-background-color:#5a5757;"
                            + "-fx-font-weight:bold;"
                            + "-fx-text-fill:white;"
                            + "-fx-font-size:14px;"
                            + "-fx-alignment:CENTER-LEFT;"
                            + "-fx-padding:0 15 0 15;"
                            + "-fx-cursor:hand;");
        });

        // ========================================================
        // MOUSE EXIT
        // ========================================================

        button.setOnMouseExited(e -> {

            button.setStyle(
                    "-fx-background-color:#434141;"
                            + "-fx-font-weight:bold;"
                            + "-fx-text-fill:white;"
                            + "-fx-font-size:14px;"
                            + "-fx-alignment:CENTER-LEFT;"
                            + "-fx-padding:0 15 0 15;"
                            + "-fx-cursor:hand;");
        });

        return button;
    }

    public Node getSidebar() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getSidebar'");
    }
}
