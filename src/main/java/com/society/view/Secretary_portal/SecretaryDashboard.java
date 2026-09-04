package com.society.view.Secretary_portal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.controller.Secretary_Controller.DashboardController;
import com.society.dao.Secretary_dao.DashboardDao;
import com.society.model.Welcome.User;
import com.society.view.ScreenSize;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SecretaryDashboard {

    // ============================================================
    // SCENE
    // ============================================================

    private Scene secretaryDash;

    // ============================================================
    // CONTROLLER
    // ============================================================

    private DashboardController dashboardController;

    // ============================================================
    // DASHBOARD CARD LABELS
    // ============================================================

    private Label residentsCount;
    private Label ownersCount;
    private Label guardsCount;
    private Label complaintsCount;
    private Label maintenanceAmount;

    // ============================================================
    // RECENT SOS
    // ============================================================

    private Label recentSOSMessage;

    // ============================================================
    // UPCOMING EVENTS
    // ============================================================

    private Label upcomingEventsMessage;

    // ============================================================
    // TODAY OVERVIEW
    // ============================================================

    private Label overviewResidents;
    private Label overviewOwners;
    private Label overviewGuards;
    private Label overviewComplaints;
    private Label overviewMaintenance;

    // ============================================================
    // LOGGED-IN USER
    // ============================================================

    private User loggedInUser;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public SecretaryDashboard(User loggedInUser) {

        this.loggedInUser = loggedInUser;

        String email = getUserEmail();

        // ========================================================
        // CREATE CONTROLLER USING SECRETARY EMAIL
        // ========================================================

        if (email == null
                || email.trim().isEmpty()
                || email.equalsIgnoreCase("NULL")) {

            System.out.println(
                    "ERROR: Secretary email is not available."
            );

            dashboardController = null;

        } else {

            dashboardController =
                    new DashboardController(email);
        }

        // ========================================================
        // DEBUG
        // ========================================================

        System.out.println(
                "================================================"
        );

        System.out.println(
                "SecretaryDashboard opened"
        );

        System.out.println(
                "Secretary Dashboard User Email: "
                        + email
        );

        System.out.println(
                "================================================"
        );
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

        return loggedInUser
                .getEmail()
                .trim()
                .toLowerCase();
    }

    // ============================================================
    // CREATE SCENE
    // ============================================================

    public Scene createScene(Stage stage) {

        // ========================================================
        // CHECK LOGGED-IN USER
        // ========================================================

        if (loggedInUser == null) {

            System.out.println(
                    "WARNING: SecretaryDashboard has NULL User."
            );

        } else {

            System.out.println(
                    "Dashboard logged-in email: "
                            + loggedInUser.getEmail()
            );
        }

        // ========================================================
        // SIDEBAR
        // ========================================================

        SecretarySidebar sidebarObj =
                new SecretarySidebar(
                        loggedInUser
                );

        VBox sidebar =
                sidebarObj.createSidebar(
                        stage
                );

        // ========================================================
        // HEADER
        // ========================================================

        HBox header =
                new HBox();

        header.setPrefHeight(80);

        header.setPadding(
                new Insets(20)
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setStyle(
                "-fx-background-color:#b3adad;"
        );

        // ========================================================
        // GREETING
        // ========================================================

        Label greeting =
                new Label(
                        getGreeting()
                                + ", Secretary 👋"
                );

        greeting.setStyle(
                "-fx-font-size:24px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#434141;"
        );

        // ========================================================
        // SPACER
        // ========================================================

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        // ========================================================
        // NOTIFICATION
        // ========================================================

        Label notification =
                new Label("🔔");

        notification.setStyle(
                "-fx-font-size:20px;"
        );

        // ========================================================
        // DATE
        // ========================================================

        LocalDate today =
                LocalDate.now();

        Label day =
                new Label(
                        today.format(
                                DateTimeFormatter.ofPattern(
                                        "EEEE"
                                )
                        )
                );

        day.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-font-weight:bold;"
        );

        Label date =
                new Label(
                        today.format(
                                DateTimeFormatter.ofPattern(
                                        "dd MMMM yyyy"
                                )
                        )
                );

        date.setStyle(
                "-fx-font-size:12px;"
                        + "-fx-text-fill:#555555;"
        );

        VBox dateBox =
                new VBox(
                        2,
                        day,
                        date
                );

        dateBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        header.getChildren().addAll(
                greeting,
                spacer,
                notification,
                dateBox
        );

        // ========================================================
        // RESIDENTS CARD
        // ========================================================

        VBox residentsCard =
                createDashboardCard(
                        "Residents",
                        "Total Residents"
                );

        residentsCount =
                (Label) residentsCard
                        .getChildren()
                        .get(1);

        // ========================================================
        // OWNERS CARD
        // ========================================================

        VBox ownersCard =
                createDashboardCard(
                        "Owners",
                        "Total Owners"
                );

        ownersCount =
                (Label) ownersCard
                        .getChildren()
                        .get(1);

        // ========================================================
        // GUARDS CARD
        // ========================================================

        VBox guardsCard =
                createDashboardCard(
                        "Guards",
                        "Total Guards"
                );

        guardsCount =
                (Label) guardsCard
                        .getChildren()
                        .get(1);

        // ========================================================
        // COMPLAINTS CARD
        // ========================================================

        VBox complaintsCard =
                createDashboardCard(
                        "Complaints",
                        "Open Complaints"
                );

        complaintsCount =
                (Label) complaintsCard
                        .getChildren()
                        .get(1);

        // ========================================================
        // MAINTENANCE CARD
        // ========================================================

        VBox maintenanceCard =
                createDashboardCard(
                        "Maintenance",
                        "Total Maintenance"
                );

        maintenanceAmount =
                (Label) maintenanceCard
                        .getChildren()
                        .get(1);

        // ========================================================
        // CARDS ROW
        // ========================================================

        HBox cardsRow =
                new HBox();

        cardsRow.setSpacing(20);

        cardsRow.setPadding(
                new Insets(20)
        );

        cardsRow.setStyle(
                "-fx-background-color:#b3adad;"
        );

        cardsRow.getChildren().addAll(
                residentsCard,
                ownersCard,
                guardsCard,
                complaintsCard,
                maintenanceCard
        );

        // ========================================================
        // QUICK ACTIONS
        // ========================================================

        VBox quickActions =
                createQuickActions(
                        stage
                );

        // ========================================================
        // TODAY OVERVIEW
        // ========================================================

        VBox todayOverview =
                createTodayOverview();

        // ========================================================
        // UPCOMING EVENTS
        // ========================================================

        VBox upcomingEvents =
                createUpcomingEvents(
                        stage
                );

        // ========================================================
        // RECENT SOS
        // ========================================================

        VBox recentSOS =
                createRecentSOS(
                        stage
                );

        // ========================================================
        // LEFT COLUMN
        // ========================================================

        VBox leftColumn =
                new VBox();

        leftColumn.setSpacing(20);

        leftColumn.getChildren().addAll(
                quickActions,
                recentSOS
        );

        // ========================================================
        // LOWER CONTENT
        // ========================================================

        HBox lowerContent =
                new HBox();

        lowerContent.setSpacing(20);

        lowerContent.setPadding(
                new Insets(
                        0,
                        20,
                        20,
                        20
                )
        );

        lowerContent.setStyle(
                "-fx-background-color:#b3adad;"
        );

        lowerContent.getChildren().addAll(
                leftColumn,
                todayOverview,
                upcomingEvents
        );

        // ========================================================
        // MAIN CONTENT
        // ========================================================

        VBox mainContent =
                new VBox();

        mainContent.setStyle(
                "-fx-background-color:#b3adad;"
        );

        mainContent.setMaxWidth(
                Double.MAX_VALUE
        );

        mainContent.getChildren().addAll(
                header,
                cardsRow,
                lowerContent
        );

        HBox.setHgrow(
                mainContent,
                Priority.ALWAYS
        );

        // ========================================================
        // BODY
        // ========================================================

        HBox body =
                new HBox();

        body.setSpacing(0);

        body.getChildren().addAll(
                sidebar,
                mainContent
        );

        // ========================================================
        // SCROLL PANE
        // ========================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                body
        );

        scrollPane.setFitToHeight(
                true
        );

        scrollPane.setFitToWidth(
                true
        );

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background:#b3adad;"
                        + "-fx-background-color:#b3adad;"
        );

        // ========================================================
        // ROOT
        // ========================================================

        VBox root =
                new VBox();

        root.setStyle(
                "-fx-background-color:#b3adad;"
        );

        root.getChildren().add(
                scrollPane
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        // ========================================================
        // SCENE
        // ========================================================

        secretaryDash =
                new Scene(
                        root,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        // ========================================================
        // LOAD DATA
        // ========================================================

        loadDashboardData();

        loadRecentSOS();

        loadUpcomingEvents();

        return secretaryDash;
    }

    // ============================================================
    // QUICK ACTIONS
    // ============================================================

    private VBox createQuickActions(Stage stage) {

        VBox box =
                new VBox();

        box.setPrefWidth(330);
        box.setMinWidth(300);
        box.setPrefHeight(280);
        box.setSpacing(10);
        box.setPadding(new Insets(20));

        box.setStyle(
                "-fx-background-color:white;"
                        + "-fx-border-color:#E5E7EB;"
                        + "-fx-border-radius:10;"
                        + "-fx-background-radius:10;"
        );

        Label title =
                new Label(
                        "Quick Actions"
                );

        title.setStyle(
                "-fx-font-size:17px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#183B56;"
        );

        Button addResident =
                new Button(
                        "👥   Add Resident"
                );

        Button addNotice =
                new Button(
                        "▣   Add Notice"
                );

        Button viewPayments =
                new Button(
                        "▣   View Payments"
                );

        addResident.setMaxWidth(
                Double.MAX_VALUE
        );

        addNotice.setMaxWidth(
                Double.MAX_VALUE
        );

        viewPayments.setMaxWidth(
                Double.MAX_VALUE
        );

        String buttonStyle =
                "-fx-background-color:#f4f6f7;"
                        + "-fx-text-fill:#183B56;"
                        + "-fx-font-size:13px;"
                        + "-fx-alignment:CENTER-LEFT;"
                        + "-fx-padding:12px;";

        addResident.setStyle(
                buttonStyle
        );

        addNotice.setStyle(
                buttonStyle
        );

        viewPayments.setStyle(
                buttonStyle
        );

        // ========================================================
        // ADD RESIDENT
        // ========================================================

        addResident.setOnAction(e -> {

            ManageEvents residents =
                    new ManageEvents();

            stage.setScene(
                    residents.createScene(
                            stage
                    )
            );
        });

        // ========================================================
        // ADD NOTICE
        // ========================================================

        addNotice.setOnAction(e -> {

            System.out.println(
                    "Quick Action Notice Email: "
                            + getUserEmail()
            );

            if (loggedInUser == null) {

                System.out.println(
                        "ERROR: Cannot open ManageNotices."
                                + " loggedInUser is NULL."
                );

                return;
            }

            ManageNotices notices =
                    new ManageNotices(
                            loggedInUser
                    );

            stage.setScene(
                    notices.createScene(
                            stage
                    )
            );
        });

        // ========================================================
        // PAYMENTS
        // ========================================================

        viewPayments.setOnAction(e -> {

            ManagePayment payment =
                    new ManagePayment();

            stage.setScene(
                    payment.createScene(
                            stage
                    )
            );
        });

        box.getChildren().addAll(
                title,
                addResident,
                addNotice,
                viewPayments
        );

        return box;
    }

    // ============================================================
    // DASHBOARD CARD
    // ============================================================

    private VBox createDashboardCard(
            String title,
            String bottomText) {

        VBox card =
                new VBox();

        card.setPrefWidth(200);
        card.setMinWidth(170);
        card.setPrefHeight(130);

        card.setPadding(
                new Insets(20)
        );

        card.setSpacing(8);

        card.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:10;"
        );

        Label titleLabel =
                new Label(
                        title
                );

        titleLabel.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-text-fill:#666666;"
        );

        Label countLabel =
                new Label(
                        "0"
                );

        countLabel.setStyle(
                "-fx-font-size:28px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#123C36;"
        );

        Label bottomLabel =
                new Label(
                        bottomText
                );

        bottomLabel.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-text-fill:#777777;"
        );

        card.getChildren().addAll(
                titleLabel,
                countLabel,
                bottomLabel
        );

        return card;
    }

    // ============================================================
    // TODAY OVERVIEW
    // ============================================================

    private VBox createTodayOverview() {

        VBox box =
                new VBox();

        box.setPrefWidth(330);
        box.setMinWidth(300);
        box.setPrefHeight(480);
        box.setSpacing(12);
        box.setPadding(new Insets(18));

        box.setStyle(
                "-fx-background-color:white;"
                        + "-fx-border-color:#E5E7EB;"
                        + "-fx-border-radius:10;"
                        + "-fx-background-radius:10;"
        );

        Label title =
                new Label(
                        "Today's Overview"
                );

        title.setStyle(
                "-fx-font-size:18px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#183B56;"
        );

        overviewResidents =
                new Label(
                        "Residents Data"
                );

        overviewOwners =
                new Label(
                        "Owners Data"
                );

        overviewGuards =
                new Label(
                        "Guards Data"
                );

        overviewComplaints =
                new Label(
                        "Complaints Data"
                );

        overviewMaintenance =
                new Label(
                        "Maintenance Data"
                );

        styleOverviewLabel(
                overviewResidents
        );

        styleOverviewLabel(
                overviewOwners
        );

        styleOverviewLabel(
                overviewGuards
        );

        styleOverviewLabel(
                overviewComplaints
        );

        styleOverviewLabel(
                overviewMaintenance
        );

        Label note =
                new Label(
                        "Dashboard data is fetched from Firestore."
                );

        note.setWrapText(
                true
        );

        note.setStyle(
                "-fx-font-size:12px;"
                        + "-fx-text-fill:#777777;"
                        + "-fx-padding:10px;"
        );

        box.getChildren().addAll(
                title,
                overviewResidents,
                overviewOwners,
                overviewGuards,
                overviewComplaints,
                overviewMaintenance,
                note
        );

        return box;
    }

    // ============================================================
    // UPCOMING EVENTS
    // ============================================================

    private VBox createUpcomingEvents(
            Stage stage) {

        VBox box =
                new VBox();

        box.setPrefWidth(330);
        box.setMinWidth(300);
        box.setPrefHeight(480);
        box.setSpacing(12);
        box.setPadding(new Insets(18));

        box.setStyle(
                "-fx-background-color:white;"
                        + "-fx-border-color:#E5E7EB;"
                        + "-fx-border-radius:10;"
                        + "-fx-background-radius:10;"
        );

        Label title =
                new Label(
                        "Upcoming Events"
                );

        title.setStyle(
                "-fx-font-size:18px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#183B56;"
        );

        upcomingEventsMessage =
                new Label(
                        "No event data loaded yet."
                );

        upcomingEventsMessage.setWrapText(
                true
        );

        upcomingEventsMessage.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-text-fill:#555555;"
                        + "-fx-padding:8px;"
        );

        Button viewEvents =
                new Button(
                        "View All Events"
                );

        viewEvents.setStyle(
                "-fx-background-color:transparent;"
                        + "-fx-text-fill:#4169A1;"
                        + "-fx-font-size:13px;"
                        + "-fx-font-weight:bold;"
        );

        viewEvents.setOnAction(e -> {

            ManageEvents events =
                    new ManageEvents();

            stage.setScene(
                    events.createScene(
                            stage
                    )
            );
        });

        box.getChildren().addAll(
                title,
                upcomingEventsMessage,
                viewEvents
        );

        return box;
    }

    // ============================================================
    // RECENT SOS
    // ============================================================

    private VBox createRecentSOS(
            Stage stage) {

        VBox box =
                new VBox();

        box.setPrefWidth(330);
        box.setMinWidth(300);
        box.setPrefHeight(180);
        box.setSpacing(10);
        box.setPadding(new Insets(18));

        box.setStyle(
                "-fx-background-color:white;"
                        + "-fx-border-color:#E5E7EB;"
                        + "-fx-border-radius:10;"
                        + "-fx-background-radius:10;"
        );

        // ========================================================
        // TITLE ROW
        // ========================================================

        HBox titleRow =
                new HBox();

        titleRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label(
                        "Recent SOS Alerts"
                );

        title.setStyle(
                "-fx-font-size:17px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#D9534F;"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button viewAll =
                new Button(
                        "View All"
                );

        viewAll.setStyle(
                "-fx-background-color:transparent;"
                        + "-fx-text-fill:#4169A1;"
                        + "-fx-font-size:13px;"
        );

        viewAll.setOnAction(e -> {

            ViewSos sos =
                    new ViewSos();

            stage.setScene(
                    sos.createScene(
                            stage
                    )
            );
        });

        titleRow.getChildren().addAll(
                title,
                spacer,
                viewAll
        );

        // ========================================================
        // SOS MESSAGE
        // ========================================================

        recentSOSMessage =
                new Label(
                        "No recent SOS alerts."
                );

        recentSOSMessage.setWrapText(
                true
        );

        recentSOSMessage.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-text-fill:#777777;"
                        + "-fx-padding:8px;"
        );

        // ========================================================
        // ADD CORRECT COMPONENTS
        // ========================================================

        box.getChildren().addAll(
                titleRow,
                recentSOSMessage
        );

        return box;
    }

    // ============================================================
    // LOAD DASHBOARD DATA
    // ============================================================

    private void loadDashboardData() {

        if (dashboardController == null) {

            System.out.println(
                    "ERROR: DashboardController is NULL."
            );

            setDefaultValues();

            return;
        }

        System.out.println(
                "========================================"
        );

        System.out.println(
                "FETCHING SECRETARY DASHBOARD DATA..."
        );

        System.out.println(
                "Secretary Email : "
                        + getUserEmail()
        );

        System.out.println(
                "========================================"
        );

        Thread dashboardThread =
                new Thread(() -> {

                    try {

                        // =================================================
                        // GET SECRETARY SOCIETY
                        // =================================================

                        String societyName =
                                dashboardController
                                        .getSocietyName();

                        System.out.println(
                                "Secretary Society : "
                                        + societyName
                        );

                        if (societyName == null
                                || societyName.trim().isEmpty()) {

                            System.out.println(
                                    "ERROR: Secretary society is NULL."
                            );

                            Platform.runLater(
                                    this::setDefaultValues
                            );

                            return;
                        }

                        // =================================================
                        // RESIDENTS
                        // =================================================

                        int totalResidents =
                                dashboardController
                                        .getResidentCount();

                        // =================================================
                        // OWNERS
                        // =================================================

                        int totalOwners =
                                dashboardController
                                        .getOwnerCount();

                        // =================================================
                        // GUARDS
                        // =================================================

                        int totalGuards =
                                dashboardController
                                        .getGuardCount();

                        // =================================================
                        // COMPLAINTS
                        // =================================================

                        int totalComplaints =
                                dashboardController
                                        .getOpenComplaints();

                        // =================================================
                        // MAINTENANCE
                        // =================================================

                        double totalMaintenance =
                                dashboardController
                                        .getMaintenanceCollection();

                        // =================================================
                        // UPDATE JAVAFX UI
                        // =================================================

                        Platform.runLater(() -> {

                            // ------------------------------------------------
                            // RESIDENTS
                            // ------------------------------------------------

                            if (residentsCount != null) {

                                residentsCount.setText(
                                        String.valueOf(
                                                totalResidents
                                        )
                                );
                            }

                            // ------------------------------------------------
                            // OWNERS
                            // ------------------------------------------------

                            if (ownersCount != null) {

                                ownersCount.setText(
                                        String.valueOf(
                                                totalOwners
                                        )
                                );
                            }

                            // ------------------------------------------------
                            // GUARDS
                            // ------------------------------------------------

                            if (guardsCount != null) {

                                guardsCount.setText(
                                        String.valueOf(
                                                totalGuards
                                        )
                                );
                            }

                            // ------------------------------------------------
                            // COMPLAINTS
                            // ------------------------------------------------

                            if (complaintsCount != null) {

                                complaintsCount.setText(
                                        String.valueOf(
                                                totalComplaints
                                        )
                                );
                            }

                            // ------------------------------------------------
                            // MAINTENANCE
                            // ------------------------------------------------

                            if (maintenanceAmount != null) {

                                maintenanceAmount.setText(
                                        formatAmount(
                                                totalMaintenance
                                        )
                                );
                            }

                            // =================================================
                            // TODAY'S OVERVIEW
                            // =================================================

                            if (overviewResidents != null) {

                                overviewResidents.setText(
                                        "Residents: "
                                                + totalResidents
                                );
                            }

                            if (overviewOwners != null) {

                                overviewOwners.setText(
                                        "Owners: "
                                                + totalOwners
                                );
                            }

                            if (overviewGuards != null) {

                                overviewGuards.setText(
                                        "Guards: "
                                                + totalGuards
                                );
                            }

                            if (overviewComplaints != null) {

                                overviewComplaints.setText(
                                        "Open Complaints: "
                                                + totalComplaints
                                );
                            }

                            if (overviewMaintenance != null) {

                                overviewMaintenance.setText(
                                        "Maintenance: ₹"
                                                + formatAmount(
                                                        totalMaintenance
                                                )
                                );
                            }
                        });

                        // =================================================
                        // DEBUG
                        // =================================================

                        System.out.println(
                                "----------------------------------------"
                        );

                        System.out.println(
                                "SECRETARY DASHBOARD DATA"
                        );

                        System.out.println(
                                "Secretary Email : "
                                        + getUserEmail()
                        );

                        System.out.println(
                                "Society         : "
                                        + societyName
                        );

                        System.out.println(
                                "Residents       : "
                                        + totalResidents
                        );

                        System.out.println(
                                "Owners          : "
                                        + totalOwners
                        );

                        System.out.println(
                                "Guards          : "
                                        + totalGuards
                        );

                        System.out.println(
                                "Open Complaints : "
                                        + totalComplaints
                        );

                        System.out.println(
                                "Maintenance     : ₹"
                                        + totalMaintenance
                        );

                        System.out.println(
                                "----------------------------------------"
                        );

                    } catch (Exception e) {

                        System.out.println(
                                "ERROR: Failed to fetch dashboard data."
                        );

                        System.out.println(
                                "Error: "
                                        + e.getMessage()
                        );

                        e.printStackTrace();

                        Platform.runLater(
                                this::setDefaultValues
                        );
                    }

                });

        dashboardThread.setDaemon(true);

        dashboardThread.start();
    }

    // ============================================================
    // DEFAULT VALUES
    // ============================================================

    private void setDefaultValues() {

        if (residentsCount != null) {
            residentsCount.setText("0");
        }

        if (ownersCount != null) {
            ownersCount.setText("0");
        }

        if (guardsCount != null) {
            guardsCount.setText("0");
        }

        if (complaintsCount != null) {
            complaintsCount.setText("0");
        }

        if (maintenanceAmount != null) {
            maintenanceAmount.setText("0");
        }

        if (overviewResidents != null) {
            overviewResidents.setText(
                    "Residents: 0"
            );
        }

        if (overviewOwners != null) {
            overviewOwners.setText(
                    "Owners: 0"
            );
        }

        if (overviewGuards != null) {
            overviewGuards.setText(
                    "Guards: 0"
            );
        }

        if (overviewComplaints != null) {
            overviewComplaints.setText(
                    "Open Complaints: 0"
            );
        }

        if (overviewMaintenance != null) {
            overviewMaintenance.setText(
                    "Maintenance: ₹0"
            );
        }
    }

    // ============================================================
    // FORMAT AMOUNT
    // ============================================================

    private String formatAmount(
            double amount) {

        if (amount == (long) amount) {

            return String.format(
                    "%d",
                    (long) amount
            );
        }

        return String.format(
                "%.2f",
                amount
        );
    }

    // ============================================================
    // OVERVIEW LABEL STYLE
    // ============================================================

    private void styleOverviewLabel(
            Label label) {

        label.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-text-fill:#555555;"
                        + "-fx-padding:7px;"
        );
    }

    // ============================================================
    // LOAD RECENT SOS
    // ============================================================

    private void loadRecentSOS() {

        if (dashboardController == null) {
            return;
        }

        Thread thread =
                new Thread(() -> {

                    try {

                        List<DashboardDao.SosAlertData> alerts =
                                dashboardController
                                        .getRecentSOSAlerts();

                        Platform.runLater(() -> {

                            if (recentSOSMessage == null) {
                                return;
                            }

                            if (alerts == null
                                    || alerts.isEmpty()) {

                                recentSOSMessage.setText(
                                        "No recent SOS alerts."
                                );

                                return;
                            }

                            StringBuilder text =
                                    new StringBuilder();

                            int limit =
                                    Math.min(
                                            alerts.size(),
                                            3
                                    );

                            for (int i = 0;
                                    i < limit;
                                    i++) {

                                DashboardDao.SosAlertData alert =
                                        alerts.get(i);

                                String type =
                                        alert.type == null
                                                || alert.type.trim().isEmpty()
                                                ? "Emergency Alert"
                                                : alert.type;

                                String location =
                                        alert.location == null
                                                || alert.location.trim().isEmpty()
                                                ? "Location unavailable"
                                                : alert.location;

                                String status =
                                        alert.status == null
                                                || alert.status.trim().isEmpty()
                                                ? "ACTIVE"
                                                : alert.status;

                                text.append(
                                        type
                                );

                                text.append(
                                        " • "
                                );

                                text.append(
                                        location
                                );

                                text.append(
                                        " • "
                                );

                                text.append(
                                        status
                                );

                                if (i < limit - 1) {

                                    text.append(
                                            "\n\n"
                                    );
                                }
                            }

                            recentSOSMessage.setText(
                                    text.toString()
                            );
                        });

                    } catch (Exception e) {

                        System.out.println(
                                "ERROR: Failed to load SOS alerts."
                        );

                        e.printStackTrace();

                        Platform.runLater(() -> {

                            if (recentSOSMessage != null) {

                                recentSOSMessage.setText(
                                        "Unable to load SOS alerts."
                                );
                            }
                        });
                    }

                });

        thread.setDaemon(true);

        thread.start();
    }

    // ============================================================
    // LOAD UPCOMING EVENTS
    // ============================================================

    private void loadUpcomingEvents() {

        if (dashboardController == null) {
            return;
        }

        Thread thread =
                new Thread(() -> {

                    try {

                        List<DashboardDao.EventData> events =
                                dashboardController
                                        .getUpcomingEvents();

                        Platform.runLater(() -> {

                            if (upcomingEventsMessage == null) {
                                return;
                            }

                            if (events == null
                                    || events.isEmpty()) {

                                upcomingEventsMessage.setText(
                                        "No upcoming events."
                                );

                                return;
                            }

                            StringBuilder text =
                                    new StringBuilder();

                            int limit =
                                    Math.min(
                                            events.size(),
                                            4
                                    );

                            for (int i = 0;
                                    i < limit;
                                    i++) {

                                DashboardDao.EventData event =
                                        events.get(i);

                                String title =
                                        event.title == null
                                                || event.title.trim().isEmpty()
                                                ? "Event"
                                                : event.title;

                                String date =
                                        event.date == null
                                                ? ""
                                                : event.date.trim();

                                String time =
                                        event.time == null
                                                ? ""
                                                : event.time.trim();

                                text.append(
                                        title
                                );

                                if (!date.isEmpty()) {

                                    text.append(
                                            "\n"
                                    );

                                    text.append(
                                            date
                                    );
                                }

                                if (!time.isEmpty()) {

                                    text.append(
                                            " • "
                                    );

                                    text.append(
                                            time
                                    );
                                }

                                if (i < limit - 1) {

                                    text.append(
                                            "\n\n"
                                    );
                                }
                            }

                            upcomingEventsMessage.setText(
                                    text.toString()
                            );
                        });

                    } catch (Exception e) {

                        System.out.println(
                                "ERROR: Failed to load upcoming events."
                        );

                        e.printStackTrace();

                        Platform.runLater(() -> {

                            if (upcomingEventsMessage != null) {

                                upcomingEventsMessage.setText(
                                        "Unable to load upcoming events."
                                );
                            }
                        });
                    }

                });

        thread.setDaemon(true);

        thread.start();
    }

    // ============================================================
    // GREETING
    // ============================================================

    private String getGreeting() {

        int hour =
                LocalTime
                        .now()
                        .getHour();

        if (hour >= 5
                && hour < 12) {

            return "Good Morning";

        } else if (hour >= 12
                && hour < 17) {

            return "Good Afternoon";

        } else if (hour >= 17
                && hour < 21) {

            return "Good Evening";

        } else {

            return "Good Night";
        }
    }
}