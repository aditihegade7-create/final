package com.society.view.Guard_portal;

import com.society.view.Welcome.LogInPage;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GuardSidebar {

    private Stage stage;
    private VBox sidebar;
    private String society;

    // Logged-in Guard email
    private String guardEmail;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public GuardSidebar(
            Stage stage,
            String activePage,
            String guardEmail,
            String society
    ) {

        this.stage = stage;
        this.guardEmail = guardEmail;
        this.society = society;

        createSidebar(activePage);
    }


    // =========================================================
    // CREATE SIDEBAR
    // =========================================================

    private void createSidebar(String activePage) {

        sidebar = new VBox();

        sidebar.setPrefWidth(280);
        sidebar.setMinWidth(280);
        sidebar.setMaxWidth(280);

        sidebar.setPrefHeight(750);

        sidebar.setPadding(
                new Insets(20)
        );

        sidebar.setSpacing(14);

        sidebar.setAlignment(
                Pos.TOP_LEFT
        );

        sidebar.setStyle(
                "-fx-background-color: #593a32;"
        );


        // =====================================================
        // LOGO
        // =====================================================

        Label logo =
                new Label("Society360");

        logo.setLineSpacing(10);

        logo.setAlignment(
                Pos.CENTER_LEFT
        );

        logo.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-family:'Bodoni MT';" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;"
        );


        // =====================================================
        // PANEL TITLE
        // =====================================================

        Label guardPanel =
                new Label("Guard Panel");

        guardPanel.setStyle(
                "-fx-text-fill: lightgray;" +
                "-fx-font-family: 'Bodoni MT';" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 5px;"
        );


        // =====================================================
        // BUTTONS
        // =====================================================

        Button dashboardButton =
                createButton("Dashboard");

        Button visitorLogButton =
                createButton("Visitor Log");

        Button parkingButton =
                createButton("Parking");

        Button manualEntryButton =
                createButton("Manual Entry");

        Button sosButton =
                createButton("Emergency SOS");

        Button reportsButton =
                createButton("Daily Reports");

        Button profileButton =
                createButton("Profile");


        // =====================================================
        // ACTIVE BUTTON
        // =====================================================

        setActiveButton(
                activePage,
                dashboardButton,
                visitorLogButton,
                parkingButton,
                manualEntryButton,
                sosButton,
                reportsButton,
                profileButton
        );


        // =====================================================
        // DASHBOARD
        // =====================================================

        dashboardButton.setOnAction(e -> {

            stage.setScene(
                    GuardDashboard.createScene(
                            stage,
                            guardEmail
                    )
            );
        });


        // =====================================================
        // VISITOR LOG
        // =====================================================

        visitorLogButton.setOnAction(e -> {

            stage.setScene(
                    VisitorLog.createScene(
                            stage,
                            guardEmail
                    )
            );

        });


        // =====================================================
        // PARKING
        // =====================================================

        parkingButton.setOnAction(e -> {

            stage.setScene(
                    Parking.createScene(
                            stage,
                            guardEmail,
                            society
                    )
            );
        });


        // =====================================================
        // MANUAL ENTRY
        // =====================================================

        manualEntryButton.setOnAction(e -> {

            stage.setScene(
                    ManualVisitorEntry.createScene(
                            stage,
                            guardEmail
                    )
            );

        });


        // =====================================================
        // EMERGENCY SOS
        // =====================================================

        sosButton.setOnAction(e -> {

            stage.setScene(
                    EmergencySOS.createScene(
                            stage,
                            guardEmail
                    )
            );

        });


        // =====================================================
        // DAILY REPORTS
        // =====================================================

        reportsButton.setOnAction(e -> {

            stage.setScene(
                    new GuardGenerateReports()
                            .createScene(
                                    stage,
                                    guardEmail
                            )
            );

        });


        // =====================================================
        // PROFILE
        // =====================================================

        profileButton.setOnAction(e -> {

            stage.setScene(
                    GuardProfile.createScene(
                            stage,
                            guardEmail
                    )
            );
        });


        // =====================================================
        // ADD BUTTONS
        // =====================================================

        sidebar.getChildren().addAll(
                logo,
                guardPanel,
                dashboardButton,
                visitorLogButton,
                parkingButton,
                manualEntryButton,
                sosButton,
                reportsButton,
                profileButton
        );
    }


    // =========================================================
    // CREATE BUTTON
    // =========================================================

    private Button createButton(String text) {

        Button button =
                new Button(text);

        button.setPrefWidth(240);
        button.setMinWidth(240);
        button.setMaxWidth(240);

        button.setPrefHeight(40);

        button.setStyle(
                "-fx-background-color: #4e342e;" +
                "-fx-font-family: 'Bodoni MT';" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-alignment: CENTER_LEFT;" +
                "-fx-background-radius: 6;"
        );


        // =====================================================
        // HOVER ANIMATION
        // =====================================================

        ScaleTransition scaleUp =
                new ScaleTransition(
                        Duration.millis(120),
                        button
                );

        scaleUp.setToX(1.04);
        scaleUp.setToY(1.04);


        ScaleTransition scaleDown =
                new ScaleTransition(
                        Duration.millis(120),
                        button
                );

        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);


        button.setOnMouseEntered(e -> {

            button.setStyle(
                    "-fx-background-color: #e7c3bb;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #3e2723;" +
                    "-fx-font-size: 14px;" +
                    "-fx-alignment: CENTER_LEFT;" +
                    "-fx-background-radius: 6;"
            );

            scaleDown.stop();
            scaleUp.playFromStart();
        });


        button.setOnMouseExited(e -> {

            button.setStyle(
                    "-fx-background-color: #4e342e;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-alignment: CENTER_LEFT;" +
                    "-fx-background-radius: 6;"
            );

            scaleUp.stop();
            scaleDown.playFromStart();
        });


        return button;
    }


    // =========================================================
    // ACTIVE BUTTON
    // =========================================================

    private void setActiveButton(
            String activePage,
            Button dashboard,
            Button visitorLog,
            Button parking,
            Button manualEntry,
            Button sos,
            Button reports,
            Button profile
    ) {

        Button activeButton = null;


        switch (activePage) {

            case "Dashboard":
                activeButton = dashboard;
                break;

            case "Visitor Log":
                activeButton = visitorLog;
                break;

            case "Parking":
                activeButton = parking;
                break;

            case "Manual Entry":
                activeButton = manualEntry;
                break;

            case "Emergency SOS":
                activeButton = sos;
                break;

            case "Daily Reports":
                activeButton = reports;
                break;

            case "Profile":
                activeButton = profile;
                break;
        }


        if (activeButton != null) {

            activeButton.setStyle(
                    "-fx-background-color: #563128;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-alignment: CENTER_LEFT;" +
                    "-fx-background-radius: 6;"
            );
        }
    }


    // =========================================================
    // GET SIDEBAR
    // =========================================================

    public VBox getSidebar() {

        return sidebar;
    }
}