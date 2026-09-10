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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SecretarySidebar {

    private User loggedInUser;
    private Button selectedButton = null;

    // ============================================================
    // CONSTRUCTOR WITH USER
    // ============================================================

    public SecretarySidebar(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        printUserInfo();
    }

    // ============================================================
    // DEFAULT CONSTRUCTOR
    // ============================================================

    public SecretarySidebar() {

        try {

            String email = UserDao.getLoggedInEmail();

            if (email != null && !email.trim().isEmpty()) {

                UserController controller = new UserController();

                loggedInUser = controller.getUserByEmail(
                        email.trim().toLowerCase()
                );
            }

        } catch (Exception e) {

            System.out.println("Error loading logged-in user:");
            e.printStackTrace();

            loggedInUser = null;
        }

        printUserInfo();
    }

    // ============================================================
    // PRINT USER INFO
    // ============================================================

    private void printUserInfo() {

        System.out.println("================================================");
        System.out.println("SecretarySidebar created");
        System.out.println("Logged-in Email: " + getUserEmail());
        System.out.println("Logged-in Role: " + getUserRole());
        System.out.println("================================================");
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
    // GET LOGGED IN USER
    // ============================================================

    public User getLoggedInUser() {
        return loggedInUser;
    }

    // ============================================================
    // NORMAL SIDEBAR
    //
    // IMPORTANT:
    // Return type MUST remain VBox.
    //
    // Existing files can continue:
    //
    // VBox sidebar = sidebarObj.createSidebar(stage);
    //
    // ============================================================

    public VBox createSidebar(Stage stage) {

        return buildSidebar(stage);
    }

    // ============================================================
    // SCROLLABLE SIDEBAR
    //
    // Scrollbar is hidden.
    // Only this sidebar can scroll.
    // Main page does NOT become scrollable.
    //
    // ============================================================

    public ScrollPane createScrollableSidebar(Stage stage) {

        VBox sidebar = buildSidebar(stage);

        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setContent(sidebar);

        // ========================================================
        // WIDTH
        // ========================================================

        scrollPane.setPrefWidth(280);
        scrollPane.setMinWidth(280);
        scrollPane.setMaxWidth(280);

        // ========================================================
        // FIT WIDTH
        // ========================================================

        scrollPane.setFitToWidth(true);

        // Height should be controlled by parent
        scrollPane.setFitToHeight(false);

        // ========================================================
        // HIDE SCROLLBARS
        // ========================================================

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        // ========================================================
        // MOUSE WHEEL / PANNING
        // ========================================================

        scrollPane.setPannable(true);

        // ========================================================
        // BACKGROUND
        // ========================================================

        scrollPane.setStyle(
                "-fx-background-color: #4e342e;"
                        + "-fx-background: #4e342e;"
                        + "-fx-border-color:transparent;"
        );

        return scrollPane;
    }

    // ============================================================
    // BUILD SIDEBAR
    // ============================================================

    private VBox buildSidebar(Stage stage) {

        VBox sidebar = new VBox();

        sidebar.setPrefWidth(280);
        sidebar.setMinWidth(280);
        sidebar.setMaxWidth(280);

        sidebar.setPrefHeight(750);

        sidebar.setSpacing(10);

        sidebar.setPadding(
                new Insets(20)
        );

        sidebar.setAlignment(
                Pos.TOP_LEFT
        );

        sidebar.setStyle(
                "-fx-background-color:#4e342e;"
        );

        // ========================================================
        // LOGO
        // ========================================================

        Label logo = new Label("Society360");

        logo.setStyle( "-fx-font-family:'Bodoni MT';" +
                "-fx-text-fill:white;"
                        + "-fx-font-size:28px;"
                        + "-fx-font-weight:bold;"
        );

        logo.setPadding(
                new Insets(0, 0, 15, 5)
        );

        // ========================================================
        // PANEL
        // ========================================================

        Label panel = new Label("SECRETARY PANEL");

        panel.setStyle(
                "-fx-text-fill:#d9d9d9;"
                        + "-fx-font-size:13px;"
                        + "-fx-font-weight:bold;"
        );

        panel.setPadding(
                new Insets(0, 0, 5, 5)
        );

        // ========================================================
        // DASHBOARD
        // ========================================================

        Button dashboardBtn =
                createMenuButton("🏠 Dashboard");

        dashboardBtn.setOnAction(e -> {
                setButtonSelected(dashboardBtn);

            try {

                SecretaryDashboard dashboard =
                        new SecretaryDashboard(loggedInUser);

                stage.setScene(
                        dashboard.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // RESIDENTS
        // ========================================================

        Button residentsBtn =
                createMenuButton("👥 Residents");

        residentsBtn.setOnAction(e -> {
                setButtonSelected(residentsBtn);

            try {

                ManageResidents residents =
                        new ManageResidents();

                stage.setScene(
                        residents.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // OWNERS
        // ========================================================

        Button ownersBtn =
                createMenuButton("Owners");

        ownersBtn.setOnAction(e -> {
                 setButtonSelected(ownersBtn);

            try {

                ManageOwner owner =
                        new ManageOwner();

                stage.setScene(
                        owner.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // GUARDS
        // ========================================================

        Button guardsBtn =
                createMenuButton("👤 Guards");

        guardsBtn.setOnAction(e -> {
                 setButtonSelected(guardsBtn);

            try {

                ManageGuard guard =
                        new ManageGuard();

                stage.setScene(
                        guard.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // NOTICES
        // ========================================================

        Button noticesBtn =
                createMenuButton("📢 Notices");

        noticesBtn.setOnAction(e -> {
                  setButtonSelected(noticesBtn);

            try {

                ManageNotices notices =
                        new ManageNotices(loggedInUser);

                stage.setScene(
                        notices.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // COMPLAINTS
        // ========================================================

        Button complaintsBtn =
                createMenuButton("❗ Complaints");

        complaintsBtn.setOnAction(e -> {
                setButtonSelected(complaintsBtn);

            try {

                ManageComplaints complaints =
                        new ManageComplaints();

                stage.setScene(
                        complaints.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // MAINTENANCE
        // ========================================================

        Button maintenanceBtn =
                createMenuButton("📅 Maintenance");

        maintenanceBtn.setOnAction(e -> {
                   setButtonSelected(maintenanceBtn);


            try {

                ManageMaintenance maintenance =
                        new ManageMaintenance();

                stage.setScene(
                        maintenance.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // PAYMENTS
        // ========================================================

        Button paymentsBtn =
                createMenuButton("💳 Payments");

        paymentsBtn.setOnAction(e -> {
                  setButtonSelected(paymentsBtn);


            try {

                ManagePayment payment =
                        new ManagePayment();

                stage.setScene(
                        payment.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // EMERGENCY SOS
        // ========================================================

        Button sosBtn =
                createMenuButton("🆘 Emergency SOS");

        sosBtn.setOnAction(e -> {
                  setButtonSelected(sosBtn);

            try {

                ViewSos sos =
                        new ViewSos();

                stage.setScene(
                        sos.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // EVENTS
        // ========================================================

        Button eventsBtn =
                createMenuButton("📢 Events");

        eventsBtn.setOnAction(e -> {
                  setButtonSelected(eventsBtn);


            try {

                ManageEvents events =
                        new ManageEvents();

                stage.setScene(
                        events.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // DAILY REPORTS
        // ========================================================

        Button reportsBtn =
                createMenuButton("📊 Daily Reports");

        reportsBtn.setOnAction(e -> {
                 setButtonSelected(reportsBtn);


            try {

                GenerateReports reports =
                        new GenerateReports();

                stage.setScene(
                        reports.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // POLLS AND SURVEYS
        // ========================================================

        Button pollsBtn =
                createMenuButton("📄 Polls and Surveys");

        pollsBtn.setOnAction(e -> {
                  setButtonSelected(pollsBtn);

            try {

                PollsandSurveys polls =
                        new PollsandSurveys();

                stage.setScene(
                        polls.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // PARKING
        // ========================================================

        Button parkingBtn =
                createMenuButton("🚗 Parking");

        parkingBtn.setOnAction(e -> {
                setButtonSelected(parkingBtn);

            try {

                System.out.println(
                        "Parking button clicked."
                );

                SecretaryParking parking =
                        new SecretaryParking();

                stage.setScene(
                        parking.getParkingScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // PROFILE
        // ========================================================

        Button profileBtn =
                createMenuButton("👤 Profile");

        profileBtn.setOnAction(e -> {
                  setButtonSelected(profileBtn);


            try {

                Profile profile =
                        new Profile();

                stage.setScene(
                        profile.createScene(stage)
                );

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        // ========================================================
        // ADD ALL BUTTONS
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
                parkingBtn,
                profileBtn
        );

        return sidebar;
    }

    // ============================================================
    // CREATE MENU BUTTON
    // ============================================================

   private Button createMenuButton(String text) {

    Button button = new Button(text);

    button.setMaxWidth(Double.MAX_VALUE);
    button.setPrefHeight(45);

    button.setAlignment(Pos.CENTER_LEFT);

    button.setPadding(
            new Insets(
                    0,
                    15,
                    0,
                    20
            )
    );

    // ========================================================
    // NORMAL STYLE
    // ========================================================

    String normalStyle =
            "-fx-background-color:transparent;"
                    + "-fx-text-fill:white;"
                    + "-fx-font-size:15px;"
                    + "-fx-font-weight:bold;"
                    + "-fx-background-radius:8;"
                    + "-fx-cursor:hand;";

    // ========================================================
    // SELECTED / HOVER STYLE
    // ========================================================

    String activeStyle =
            "-fx-background-color:#e7c3bb;"
                    + "-fx-text-fill:#4e342e;"
                    + "-fx-font-size:15px;"
                    + "-fx-font-weight:bold;"
                    + "-fx-background-radius:8;"
                    + "-fx-cursor:hand;";

    button.setStyle(normalStyle);

    // ========================================================
    // MOUSE ENTERED
    // ========================================================

    button.setOnMouseEntered(e -> {

        button.setStyle(activeStyle);
    });

    // ========================================================
    // MOUSE EXITED
    // ========================================================

    button.setOnMouseExited(e -> {

        // जर हा button selected असेल तर activeच ठेवायचा
        if (selectedButton == button) {

            button.setStyle(activeStyle);

        } else {

            button.setStyle(normalStyle);
        }
    });

    // ========================================================
    // CLICK
    // ========================================================

    button.setOnAction(e -> {

        // आधीचा selected button normal कर
        if (selectedButton != null
                && selectedButton != button) {

            selectedButton.setStyle(normalStyle);
        }

        // नवीन button selected कर
        selectedButton = button;

        // selected button active ठेव
        button.setStyle(activeStyle);
    });

    return button;
}


    private void setButtonSelected(Button button) {

    String normalStyle =
            "-fx-background-color:transparent;"
                    + "-fx-text-fill:white;"
                    + "-fx-font-size:15px;"
                    + "-fx-font-weight:bold;"
                    + "-fx-background-radius:8;"
                    + "-fx-cursor:hand;";

    String activeStyle =
            "-fx-background-color:#e7c3bb;"
                    + "-fx-text-fill:#4e342e;"
                    + "-fx-font-size:15px;"
                    + "-fx-font-weight:bold;"
                    + "-fx-background-radius:8;"
                    + "-fx-cursor:hand;";
    

    // Make previous button normal
    if (selectedButton != null
            && selectedButton != button) {

        selectedButton.setStyle(normalStyle);
    }

    // Set new selected button
    selectedButton = button;

    // Keep selected button active
    button.setStyle(activeStyle);
    
} 
    // ============================================================
    // OLD GET SIDEBAR METHOD
    // ============================================================

    public Node getSidebar() {

        throw new UnsupportedOperationException(
                "Unimplemented method 'getSidebar'"
        );
    }
}
