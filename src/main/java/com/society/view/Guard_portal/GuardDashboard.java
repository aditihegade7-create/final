package com.society.view.Guard_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.controller.Guard_Controller.EmergencyController;
import com.society.controller.Guard_Controller.NoticeController;
import com.society.controller.Guard_Controller.VisitorController;
import com.society.controller.Guard_Controller.GuardController;

import com.society.model.Guard_model.EmergencyModel;
import com.society.model.Guard_model.NoticeModel;
import com.society.model.Guard_model.VisitorModel;
import com.society.model.Guard_model.ParkingModel;
import com.society.model.Guard_model.GuardModel;

import com.society.dao.Guard_dao.ParkingDao;
import com.society.view.ScreenSize;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.Interpolator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import javafx.util.Duration;


public class GuardDashboard {

    // =========================================================
    // SHARED SIDEBAR
    // SAME SIDEBAR WILL BE REUSED
    // =========================================================

       
    private static GuardSidebar sharedSidebar;


    // =========================================================
    // CREATE SCENE
    // =========================================================

    public static Scene createScene(Stage stage, String guardEmail) {

        // =====================================================
        // LOAD SOCIETY
        // =====================================================

        String loadedSociety = null;

        if (guardEmail != null &&
                !guardEmail.trim().isEmpty()) {

            try {

                GuardController guardController =
                        new GuardController();

                GuardModel guard =
                        guardController.getGuard(
                                guardEmail.trim().toLowerCase()
                        );

                if (guard != null) {

                    loadedSociety =
                            guard.getSociety();
                }

            } catch (Exception ex) {

                System.out.println(
                        "WARNING: Could not load guard society."
                );

                ex.printStackTrace();
            }
        }

        final String society =
                loadedSociety;


        // =====================================================
        // ROOT
        // =====================================================

        StackPane root =
                new StackPane();


        // =====================================================
        // BACKGROUND IMAGE
        // =====================================================

        java.net.URL bgUrl =
                GuardDashboard.class.getResource(
                        "/background-Dashboard5.jpeg"
                );

        ImageView backgroundImage =
                new ImageView();

        if (bgUrl != null) {

            Image bgImage =
                    new Image(
                            bgUrl.toExternalForm()
                    );

            backgroundImage.setImage(
                    bgImage
            );

        } else {

            System.out.println(
                    "WARNING: Background image not found: " +
                    "WhatsApp Image 2026-08-27 at 1.20.20 PM.jpeg"
            );
        }

        backgroundImage.setPreserveRatio(
                false
        );

        backgroundImage.setFitWidth(
                ScreenSize.getWidth()
        );

        backgroundImage.setFitHeight(
                ScreenSize.getHeight()
        );

        backgroundImage.setMouseTransparent(
                true
        );

        root.getChildren().add(
                backgroundImage
        );


        // =====================================================
        // DASHBOARD ROOT
        // =====================================================

        BorderPane dashboardRoot =
                new BorderPane();


        // =====================================================
        // NOTICE CONTROLLER
        // =====================================================

        NoticeController noticeController =
                new NoticeController();

        NoticeModel latestNotice =
                noticeController.getLatestActiveNotice();


        // =====================================================
        // SIDEBAR
        // SAME SIDEBAR REUSED
        // SIZE AND POSITION UNCHANGED
        // =====================================================

        if (sharedSidebar == null) {

            sharedSidebar =
                    new GuardSidebar(
                            stage,
                            "Dashboard",
                            guardEmail,
                            society
                    );
        }

        VBox sidebarNode =
                sharedSidebar.getSidebar();


        // Remove sidebar from previous parent
        // without changing its size or position

        if (sidebarNode.getParent() != null &&
                sidebarNode.getParent()
                        instanceof javafx.scene.layout.Pane) {

            javafx.scene.layout.Pane oldParent =
                    (javafx.scene.layout.Pane)
                            sidebarNode.getParent();

            oldParent.getChildren().remove(
                    sidebarNode
            );
        }


        // Same left position

        dashboardRoot.setLeft(
                sidebarNode
        );


        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainContent =
                new VBox();

        mainContent.setPadding(
                new Insets(
                        25,
                        35,
                        25,
                        35
                )
        );

        mainContent.setSpacing(
                18
        );

        mainContent.setStyle(
                "-fx-background-color: #e8ddd5;"
        );


        // =====================================================
        // HEADER
        // =====================================================

        HBox header =
                new HBox();

        header.setPrefHeight(80);
        header.setMinHeight(80);
        header.setMaxHeight(80);

        header.setPadding(
                new Insets(
                        20,
                        25,
                        20,
                        25
                )
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setStyle(
                "-fx-background-color: #4e342e;"
        );


        // =====================================================
        // FIXED HEADER
        // NO HEADER ANIMATION
        // =====================================================

        header.setTranslateX(0);
        header.setTranslateY(0);
        header.setOpacity(1);
        header.setScaleX(1);
        header.setScaleY(1);


        // =====================================================
        // TITLE + DESCRIPTION
        // =====================================================

        VBox titleBox =
                new VBox(3);

        Label title =
                new Label(
                        "Good Morning, Guard"
                );

        title.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-family: 'Bodoni MT';" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill: white;"
        );


        Label description =
                new Label(
                        "Main Gate    Shift A    (08:00 AM - 04:00 PM)"
                );

        description.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill: white;"
        );


        titleBox.getChildren().addAll(
                title,
                description
        );


        // =====================================================
        // HEADER SPACER
        // =====================================================

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        // =====================================================
        // DATE
        // =====================================================

        Label day =
                new Label();

        Label date =
                new Label();


        LocalDate today =
                LocalDate.now();


        day.setText(
                today.format(
                        DateTimeFormatter.ofPattern(
                                "EEEE"
                        )
                )
        );


        date.setText(
                today.format(
                        DateTimeFormatter.ofPattern(
                                "dd MMMM yyyy"
                        )
                )
        );


        day.setTextFill(
                Color.WHITE
        );

        date.setTextFill(
                Color.WHITE
        );


        day.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;"
        );

        date.setStyle(
                "-fx-font-size:13px;"
        );


        VBox dateBox =
                new VBox(3);

        dateBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        dateBox.getChildren().addAll(
                day,
                date
        );


        header.getChildren().addAll(
                titleBox,
                spacer,
                dateBox
        );


        // =====================================================
        // FIRESTORE DATA
        // =====================================================

        VisitorController visitorController =
                new VisitorController();

        EmergencyController emergencyController =
                new EmergencyController();

        ParkingDao parkingDao =
                new ParkingDao();


        List<EmergencyModel> activeEmergencies =
                emergencyController.getActiveEmergencies();

        int activeEmergencyCount =
                activeEmergencies.size();


        List<VisitorModel> visitors =
                visitorController.getAllVisitors();


        int activeVisitorsCount = 0;

        int expectedVisitorsCount = 0;

        int totalVisitorsCount =
                visitors.size();

        int deliveryCount = 0;

        int personalGuestCount = 0;

        int exitsCount = 0;


        for (VisitorModel visitor :
                visitors) {

            String purpose =
                    visitor.getPurpose();

            String status =
                    visitor.getStatus();


            if ("Delivery".equalsIgnoreCase(purpose)
                    || "Service".equalsIgnoreCase(purpose)) {

                deliveryCount++;

            } else {

                personalGuestCount++;
            }


            if ("Checked Out".equalsIgnoreCase(status)) {

                exitsCount++;
            }
        }


        for (VisitorModel visitor :
                visitors) {

            if ("Inside".equalsIgnoreCase(
                    visitor.getStatus())) {

                activeVisitorsCount++;
            }


            if ("APPROVED".equalsIgnoreCase(
                    visitor.getStatus())) {

                expectedVisitorsCount++;
            }
        }


        // =====================================================
        // PARKING
        // =====================================================

        List<ParkingModel> parkingList =
                parkingDao.getAllParking(
                        guardEmail
                );


        int totalParkingSpots =
                parkingList == null
                        ? 0
                        : parkingList.size();


        int occupiedParkingSpots = 0;


        if (parkingList != null) {

            for (ParkingModel parkingSpot :
                    parkingList) {

                if (parkingSpot != null &&
                        parkingSpot.isOccupied()) {

                    occupiedParkingSpots++;
                }
            }
        }


        int parkingPercentage =
                totalParkingSpots == 0
                        ? 0
                        : (occupiedParkingSpots * 100)
                                / totalParkingSpots;


        // =====================================================
        // STAT CARDS
        // =====================================================

        VBox activeVisitors =
                createStatCard(
                        "Active Visitors",
                        String.valueOf(
                                activeVisitorsCount
                        ),
                        "Current visitors inside"
                );


        VBox parking =
                createStatCard(
                        "Parking Occupancy",
                        totalParkingSpots == 0
                                ? "0/0"
                                : occupiedParkingSpots
                                        + "/"
                                        + totalParkingSpots,
                        parkingPercentage + "% Full"
                );


        VBox expectedVisitors =
                createStatCard(
                        "Expected Visitors",
                        String.valueOf(
                                expectedVisitorsCount
                        ),
                        "Pending / approved visitors"
                );


        VBox sos =
                createStatCard(
                        "SOS Alerts",
                        String.valueOf(
                                activeEmergencyCount
                        ),
                        activeEmergencyCount > 0
                                ? "High Priority"
                                : "No active alerts"
                );


        HBox statCards =
                new HBox(20);

        statCards.setAlignment(
                Pos.CENTER_LEFT
        );

        statCards.getChildren().addAll(
                activeVisitors,
                parking,
                expectedVisitors,
                sos
        );


        // =====================================================
        // QUICK ACTIONS
        // =====================================================

        Label quickActionsTitle =
                new Label(
                        "Quick Actions"
                );


        quickActionsTitle.setStyle(
                "-fx-font-size:19px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#030A12;"
        );


        Label quickActionsSubtitle =
                new Label(
                        "Frequently used gate operations"
                );


        quickActionsSubtitle.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#263B35;"
        );


        VBox quickHeading =
                new VBox(3);


        quickHeading.getChildren().addAll(
                quickActionsTitle,
                quickActionsSubtitle
        );


        // =====================================================
        // QUICK ACTION BUTTONS
        // =====================================================

        Button visitorLogButton =
                new Button(
                        "Visitor Log\nView Visitor Activity"
                );


        Button manualEntryButton =
                new Button(
                        "Manual Entry\nRegister Walk-in Visitor"
                );


        Button parkingEntryButton =
                new Button(
                        "Parking Entry\nManage Vehicle Entry"
                );


        Button emergencySOSButton =
                new Button(
                        "Emergency SOS\nRaise Emergency Alert"
                );


        Button[] quickActionButtons = {

                visitorLogButton,
                manualEntryButton,
                parkingEntryButton,
                emergencySOSButton

        };


        // =====================================================
        // BUTTON STYLE + ANIMATION
        // =====================================================

        for (Button button :
                quickActionButtons) {

            button.setPrefWidth(
                    315
            );

            button.setPrefHeight(
                    78
            );

            button.setAlignment(
                    Pos.CENTER_LEFT
            );

            button.setPadding(
                    new Insets(
                            12,
                            18,
                            12,
                            18
                    )
            );

            button.setCursor(
                    Cursor.HAND
            );

            button.setStyle(
                    "-fx-background-color:#4e342e;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-size:14px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-background-radius:10;"
            );


            // =================================================
            // HOVER
            // =================================================

            button.setOnMouseEntered(e -> {

                ScaleTransition scale =
                        new ScaleTransition(
                                Duration.millis(120),
                                button
                        );

                scale.setToX(1.04);
                scale.setToY(1.04);

                scale.play();


                button.setStyle(
                        "-fx-background-color:#522c23;" +
                        "-fx-text-fill:#E8F0E8;" +
                        "-fx-font-size:14px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:10;"
                );
            });


            // =================================================
            // EXIT
            // =================================================

            button.setOnMouseExited(e -> {

                ScaleTransition scale =
                        new ScaleTransition(
                                Duration.millis(120),
                                button
                        );

                scale.setToX(1.0);
                scale.setToY(1.0);

                scale.play();


                button.setStyle(
                        "-fx-background-color:#4e342e;" +
                        "-fx-text-fill:white;" +
                        "-fx-font-size:14px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:10;"
                );
            });


            // =================================================
            // PRESS
            // =================================================

            button.setOnMousePressed(e -> {

                ScaleTransition press =
                        new ScaleTransition(
                                Duration.millis(90),
                                button
                        );

                press.setToX(0.96);
                press.setToY(0.96);

                press.setInterpolator(
                        Interpolator.EASE_OUT
                );

                press.play();
            });


            // =================================================
            // RELEASE
            // =================================================

            button.setOnMouseReleased(e -> {

                ScaleTransition release =
                        new ScaleTransition(
                                Duration.millis(130),
                                button
                        );

                release.setToX(1.0);
                release.setToY(1.0);

                release.setInterpolator(
                        Interpolator.EASE_OUT
                );

                release.play();
            });
        }


        // =====================================================
        // QUICK ACTION ROWS
        // =====================================================

        HBox firstActionRow =
                new HBox(18);


        firstActionRow.getChildren().addAll(
                visitorLogButton,
                manualEntryButton
        );


        HBox secondActionRow =
                new HBox(18);


        secondActionRow.getChildren().addAll(
                parkingEntryButton,
                emergencySOSButton
        );


        VBox quickActionsSection =
                new VBox(12);


        quickActionsSection.setPrefWidth(
                650
        );


        quickActionsSection.getChildren().addAll(
                quickHeading,
                firstActionRow,
                secondActionRow
        );


        // =====================================================
        // SUMMARY
        // =====================================================

        Label summaryTitle =
                new Label(
                        "Today's Summary"
                );


        summaryTitle.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#030A12;"
        );


        Label summarySubtitle =
                new Label(
                        "Gate activity for today"
                );


        summarySubtitle.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#52605A;"
        );


        VBox summaryHeading =
                new VBox(3);


        summaryHeading.getChildren().addAll(
                summaryTitle,
                summarySubtitle
        );


        HBox totalVisitorsRow =
                createSummaryRow(
                        "Total Visitors",
                        String.valueOf(
                                totalVisitorsCount
                        )
                );


        HBox deliveryRow =
                createSummaryRow(
                        "Delivery / Service",
                        String.valueOf(
                                deliveryCount
                        )
                );


        HBox personalRow =
                createSummaryRow(
                        "Personal / Guest",
                        String.valueOf(
                                personalGuestCount
                        )
                );


        HBox vehiclesRow =
                createSummaryRow(
                        "Vehicles Parked",
                        String.valueOf(
                                occupiedParkingSpots
                        )
                );


        HBox exitsRow =
                createSummaryRow(
                        "Exits",
                        String.valueOf(
                                exitsCount
                        )
                );


        VBox summaryBox =
                new VBox(16);


        summaryBox.setPadding(
                new Insets(20)
        );


        summaryBox.setPrefWidth(
                475
        );


        summaryBox.setPrefHeight(
                265
        );


        summaryBox.setStyle(
                "-fx-background-color:#E8F0E8;" +
                "-fx-background-radius:10;"
        );


        summaryBox.getChildren().addAll(
                summaryHeading,
                totalVisitorsRow,
                deliveryRow,
                personalRow,
                vehiclesRow,
                exitsRow
        );


        // =====================================================
        // LOWER SECTION
        // =====================================================

        HBox lowerSection =
                new HBox(28);


        lowerSection.setAlignment(
                Pos.TOP_LEFT
        );


        lowerSection.getChildren().addAll(
                quickActionsSection,
                summaryBox
        );


        // =====================================================
        // NOTICE BAR
        // =====================================================

        VBox noticeContent =
                new VBox(4);


        Label noticeLabel =
                new Label(
                        "IMPORTANT NOTICE"
                );


        noticeLabel.setStyle(
                "-fx-font-size:10px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#183A2D;"
        );


        Label noticeText =
                new Label();


        if (latestNotice != null) {

            noticeText.setText(
                    latestNotice.getTitle()
            );

        } else {

            noticeText.setText(
                    "No active notices."
            );
        }


        noticeText.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#050605;"
        );


        Label noticeInfo =
                new Label();


        if (latestNotice != null) {

            noticeInfo.setText(
                    latestNotice.getDescription()
            );

        } else {

            noticeInfo.setText(
                    "There are currently no active notices."
            );
        }


        noticeInfo.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#52605A;"
        );


        noticeContent.getChildren().addAll(
                noticeLabel,
                noticeText,
                noticeInfo
        );


        Button viewNoticeButton =
                new Button(
                        "View Notice"
                );


        viewNoticeButton.setPrefWidth(
                120
        );


        viewNoticeButton.setPrefHeight(
                38
        );


        viewNoticeButton.setCursor(
                Cursor.HAND
        );


        viewNoticeButton.setStyle(
                "-fx-background-color:#434141;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;"
        );


        Region noticeSpacer =
                new Region();


        HBox.setHgrow(
                noticeSpacer,
                Priority.ALWAYS
        );


        HBox noticeBar =
                new HBox(15);


        noticeBar.setPrefHeight(
                75
        );


        noticeBar.setMaxWidth(
                Double.MAX_VALUE
        );


        noticeBar.setPadding(
                new Insets(
                        10,
                        18,
                        10,
                        20
                )
        );


        noticeBar.setAlignment(
                Pos.CENTER_LEFT
        );


        noticeBar.setStyle(
                "-fx-background-color:#E8F0E8;" +
                "-fx-background-radius:10;"
        );


        noticeBar.getChildren().addAll(
                noticeContent,
                noticeSpacer,
                viewNoticeButton
        );


        // =====================================================
        // MAIN AREA
        // =========================================================
        // HEADER FIXED
        // =====================================================

        BorderPane mainarea =
                new BorderPane();


        mainarea.setTop(
                header
        );


        VBox animatedPageContent =
                new VBox();


        animatedPageContent.setSpacing(
                18
        );


        animatedPageContent.setPadding(
                new Insets(
                        25,
                        35,
                        25,
                        35
                )
        );


        animatedPageContent.setStyle(
                "-fx-background-color:#e8ddd5;"
        );


        animatedPageContent.getChildren().addAll(
                statCards,
                lowerSection,
                noticeBar
        );


        mainarea.setCenter(
                animatedPageContent
        );


        dashboardRoot.setCenter(
                mainarea
        );


        // =====================================================
        // ACTION POPUP
        // =====================================================

        StackPane actionOverlay =
                new StackPane();


        actionOverlay.setStyle(
                "-fx-background-color:rgba(0,0,0,0.28);"
        );


        actionOverlay.setVisible(
                false
        );


        actionOverlay.setOpacity(
                0
        );


        VBox actionPopup =
                new VBox(0);


        actionPopup.setPrefWidth(
                480
        );


        actionPopup.setMaxWidth(
                480
        );


        actionPopup.setPrefHeight(
                260
        );


        actionPopup.setMaxHeight(
                260
        );


        actionPopup.setOpacity(
                0
        );


        actionPopup.setScaleX(
                0.92
        );


        actionPopup.setScaleY(
                0.92
        );


        actionPopup.setStyle(
                "-fx-background-color:#F1F5F1;" +
                "-fx-background-radius:16;" +
                "-fx-border-color:#4e342e;" +
                "-fx-border-width:1;" +
                "-fx-border-radius:16;"
        );


        HBox actionPopupHeader =
                new HBox(12);


        actionPopupHeader.setPadding(
                new Insets(
                        20,
                        22,
                        18,
                        22
                )
        );


        actionPopupHeader.setAlignment(
                Pos.CENTER_LEFT
        );


        actionPopupHeader.setStyle(
                "-fx-background-color:#E8F0E8;" +
                "-fx-background-radius:16 16 0 0;"
        );


        Label actionIcon =
                new Label("✓");


        actionIcon.setPrefSize(
                34,
                34
        );


        actionIcon.setAlignment(
                Pos.CENTER
        );


        actionIcon.setStyle(
                "-fx-background-color:#4e342e;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:17px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:50%;"
        );


        VBox actionHeaderText =
                new VBox(3);


        Label actionSmallTitle =
                new Label(
                        "GATE OPERATION"
                );


        actionSmallTitle.setStyle(
                "-fx-font-size:10px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#183A2D;"
        );


        Label actionPopupTitle =
                new Label(
                        "Visitor Log"
                );


        actionPopupTitle.setStyle(
                "-fx-font-size:21px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#4e342e;"
        );


        actionHeaderText.getChildren().addAll(
                actionSmallTitle,
                actionPopupTitle
        );


        actionPopupHeader.getChildren().addAll(
                actionIcon,
                actionHeaderText
        );


        VBox actionPopupBody =
                new VBox(8);


        actionPopupBody.setPadding(
                new Insets(
                        18,
                        22,
                        10,
                        22
                )
        );


        Label actionPopupDescription =
                new Label(
                        "Open this gate operation?"
                );


        actionPopupDescription.setWrapText(
                true
        );


        actionPopupDescription.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#263B35;"
        );


        actionPopupBody.getChildren().add(
                actionPopupDescription
        );


        HBox actionPopupFooter =
                new HBox(10);


        actionPopupFooter.setPadding(
                new Insets(
                        10,
                        22,
                        20,
                        22
                )
        );


        actionPopupFooter.setAlignment(
                Pos.CENTER_RIGHT
        );


        Button cancelActionButton =
                new Button(
                        "Cancel"
                );


        cancelActionButton.setPrefWidth(
                90
        );


        cancelActionButton.setPrefHeight(
                36
        );


        cancelActionButton.setCursor(
                Cursor.HAND
        );


        cancelActionButton.setStyle(
                "-fx-background-color:#d8c8c2;" +
                "-fx-text-fill:#4e342e;" +
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;"
        );


        Button continueActionButton =
                new Button(
                        "Continue"
                );


        continueActionButton.setPrefWidth(
                100
        );


        continueActionButton.setPrefHeight(
                36
        );


        continueActionButton.setCursor(
                Cursor.HAND
        );


        continueActionButton.setStyle(
                "-fx-background-color:#4e342e;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;"
        );


        actionPopupFooter.getChildren().addAll(
                cancelActionButton,
                continueActionButton
        );


        actionPopup.getChildren().addAll(
                actionPopupHeader,
                actionPopupBody,
                actionPopupFooter
        );


        actionOverlay.getChildren().add(
                actionPopup
        );


        StackPane.setAlignment(
                actionPopup,
                Pos.CENTER
        );


        final String[] selectedAction =
                new String[1];


        // =====================================================
        // SHOW ACTION POPUP
        // =====================================================

        Runnable showActionPopup =
                () -> {

                    actionOverlay.setVisible(
                            true
                    );

                    actionOverlay.setOpacity(
                            0
                    );

                    actionPopup.setOpacity(
                            0
                    );

                    actionPopup.setScaleX(
                            0.92
                    );

                    actionPopup.setScaleY(
                            0.92
                    );


                    FadeTransition overlayFade =
                            new FadeTransition(
                                    Duration.millis(220),
                                    actionOverlay
                            );


                    overlayFade.setFromValue(
                            0
                    );

                    overlayFade.setToValue(
                            1
                    );


                    FadeTransition popupFade =
                            new FadeTransition(
                                    Duration.millis(250),
                                    actionPopup
                            );


                    popupFade.setFromValue(
                            0
                    );

                    popupFade.setToValue(
                            1
                    );


                    ScaleTransition popupScale =
                            new ScaleTransition(
                                    Duration.millis(250),
                                    actionPopup
                            );


                    popupScale.setFromX(
                            0.92
                    );

                    popupScale.setFromY(
                            0.92
                    );

                    popupScale.setToX(
                            1.0
                    );

                    popupScale.setToY(
                            1.0
                    );


                    popupScale.setInterpolator(
                            Interpolator.EASE_OUT
                    );


                    ParallelTransition popupAnimation =
                            new ParallelTransition(
                                    popupFade,
                                    popupScale
                            );


                    overlayFade.play();
                    popupAnimation.play();
                };


        // =====================================================
        // HIDE ACTION POPUP
        // =====================================================

        Runnable hideActionPopup =
                () -> {

                    FadeTransition overlayFade =
                            new FadeTransition(
                                    Duration.millis(180),
                                    actionOverlay
                            );


                    overlayFade.setFromValue(
                            actionOverlay.getOpacity()
                    );


                    overlayFade.setToValue(
                            0
                    );


                    FadeTransition popupFade =
                            new FadeTransition(
                                    Duration.millis(160),
                                    actionPopup
                            );


                    popupFade.setFromValue(
                            actionPopup.getOpacity()
                    );


                    popupFade.setToValue(
                            0
                    );


                    ScaleTransition popupScale =
                            new ScaleTransition(
                                    Duration.millis(180),
                                    actionPopup
                            );


                    popupScale.setToX(
                            0.92
                    );

                    popupScale.setToY(
                            0.92
                    );


                    ParallelTransition closeAnimation =
                            new ParallelTransition(
                                    popupFade,
                                    popupScale
                            );


                    closeAnimation.setOnFinished(
                            e -> {

                                actionOverlay.setVisible(
                                        false
                                );

                                actionOverlay.setOpacity(
                                        0
                                );

                                actionPopup.setOpacity(
                                        0
                                );

                                actionPopup.setScaleX(
                                        0.92
                                );

                                actionPopup.setScaleY(
                                        0.92
                                );
                            }
                    );


                    overlayFade.play();
                    closeAnimation.play();
                };


        // =====================================================
        // VISITOR LOG
        // =====================================================

        visitorLogButton.setOnAction(e -> {

            selectedAction[0] =
                    "visitor";


            actionPopupTitle.setText(
                    "Visitor Log"
            );


            actionPopupDescription.setText(
                    "View visitor activity and manage " +
                    "the current gate visitor records."
            );


            showActionPopup.run();
        });


        // =====================================================
        // MANUAL ENTRY
        // =====================================================

        manualEntryButton.setOnAction(e -> {

            selectedAction[0] =
                    "manual";


            actionPopupTitle.setText(
                    "Manual Entry"
            );


            actionPopupDescription.setText(
                    "Register a walk-in visitor manually " +
                    "from the gate."
            );


            showActionPopup.run();
        });


        // =====================================================
        // PARKING ENTRY
        // =====================================================

        parkingEntryButton.setOnAction(e -> {

            selectedAction[0] =
                    "parking";


            actionPopupTitle.setText(
                    "Parking Entry"
            );


            actionPopupDescription.setText(
                    "Manage vehicle entry and parking " +
                    "occupancy from the gate."
            );


            showActionPopup.run();
        });


        // =====================================================
        // EMERGENCY SOS
        // =====================================================

        emergencySOSButton.setOnAction(e -> {

            selectedAction[0] =
                    "emergency";


            actionPopupTitle.setText(
                    "Emergency SOS"
            );


            actionPopupDescription.setText(
                    "Raise and manage an emergency alert " +
                    "from the guard portal."
            );


            showActionPopup.run();
        });


        // =====================================================
        // CONTINUE
        // =====================================================

        continueActionButton.setOnAction(e -> {

            String action =
                    selectedAction[0];


            hideActionPopup.run();


            if ("visitor".equals(action)) {

                stage.setScene(
                        VisitorLog.createScene(
                                stage,
                                guardEmail
                        )
                );


            } else if ("manual".equals(action)) {

                stage.setScene(
                        ManualVisitorEntry.createScene(
                                stage,
                                guardEmail
                        )
                );


            } else if ("parking".equals(action)) {

                stage.setScene(
                        Parking.createScene(
                                stage,
                                guardEmail,
                                society
                        )
                );


            } else if ("emergency".equals(action)) {

                stage.setScene(
                        EmergencySOS.createScene(
                                stage,
                                guardEmail
                        )
                );
            }
        });


        // =====================================================
        // CANCEL
        // =====================================================

        cancelActionButton.setOnAction(e -> {

            hideActionPopup.run();

        });


        // =====================================================
        // NOTICE OVERLAY
        // =====================================================

        StackPane noticeOverlay =
                new StackPane();


        noticeOverlay.setStyle(
                "-fx-background-color:rgba(0,0,0,0.28);"
        );


        noticeOverlay.setVisible(
                false
        );


        noticeOverlay.setOpacity(
                0
        );


        // =====================================================
        // NOTICE POPUP
        // =====================================================

        VBox noticePopup =
                new VBox(0);


        noticePopup.setPrefSize(
                560,
                390
        );


        noticePopup.setMaxSize(
                560,
                390
        );


        noticePopup.setOpacity(
                0
        );


        noticePopup.setScaleX(
                0.92
        );


        noticePopup.setScaleY(
                0.92
        );


        noticePopup.setStyle(
                "-fx-background-color:#F1F5F1;" +
                "-fx-background-radius:16;" +
                "-fx-border-color:#4e342e;" +
                "-fx-border-width:1;" +
                "-fx-border-radius:16;"
        );


        // =====================================================
        // NOTICE POPUP HEADER
        // =====================================================

        HBox popupHeader =
                new HBox();


        popupHeader.setPadding(
                new Insets(
                        22,
                        24,
                        18,
                        24
                )
        );


        popupHeader.setAlignment(
                Pos.CENTER_LEFT
        );


        popupHeader.setStyle(
                "-fx-background-color:#E8F0E8;" +
                "-fx-background-radius:16 16 0 0;"
        );


        Label noticeIcon =
                new Label("!");


        noticeIcon.setPrefSize(
                32,
                32
        );


        noticeIcon.setAlignment(
                Pos.CENTER
        );


        noticeIcon.setStyle(
                "-fx-background-color:#4e342e;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:50%;"
        );


        VBox headerText =
                new VBox(2);


        Label popupTitle =
                new Label(
                        "IMPORTANT NOTICES"
                );


        popupTitle.setStyle(
                "-fx-font-size:11px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#183A2D;"
        );


        Label popupHeading =
                new Label(
                        "Latest Notice"
                );


        popupHeading.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#4e342e;"
        );


        headerText.getChildren().addAll(
                popupTitle,
                popupHeading
        );


        HBox.setMargin(
                headerText,
                new Insets(
                        0,
                        0,
                        0,
                        12
                )
        );


        popupHeader.getChildren().addAll(
                noticeIcon,
                headerText
        );


        // =====================================================
        // POPUP BODY
        // =====================================================

        VBox popupBody =
                new VBox(12);


        popupBody.setPadding(
                new Insets(
                        18,
                        24,
                        18,
                        24
                )
        );


        Label popupNoticeTitle =
                new Label();


        popupNoticeTitle.setWrapText(
                true
        );


        popupNoticeTitle.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#4e342e;"
        );


        Label popupNoticeDate =
                new Label();


        popupNoticeDate.setStyle(
                "-fx-font-size:11px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#52605A;"
        );


        Label popupNoticeDescription =
                new Label();


        popupNoticeDescription.setWrapText(
                true
        );


        popupNoticeDescription.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#070707;"
        );


        Label popupNoticeStatus =
                new Label();


        popupNoticeStatus.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#263B35;"
        );


        if (latestNotice != null) {

            popupNoticeTitle.setText(
                    latestNotice.getTitle()
            );


            popupNoticeDate.setText(
                    "Date: " +
                    latestNotice.getDate()
            );


            popupNoticeDescription.setText(
                    latestNotice.getDescription()
            );


            popupNoticeStatus.setText(
                    "Status: " +
                    latestNotice.getStatus()
            );

        } else {

            popupNoticeTitle.setText(
                    "No Active Notice"
            );


            popupNoticeDate.setText(
                    ""
            );


            popupNoticeDescription.setText(
                    "There are currently no active notices."
            );


            popupNoticeStatus.setText(
                    ""
            );
        }


        popupBody.getChildren().addAll(
                popupNoticeTitle,
                popupNoticeDate,
                popupNoticeDescription,
                popupNoticeStatus
        );


        // =====================================================
        // POPUP FOOTER
        // =====================================================

        HBox popupFooter =
                new HBox();


        popupFooter.setPadding(
                new Insets(
                        5,
                        24,
                        20,
                        24
                )
        );


        popupFooter.setAlignment(
                Pos.CENTER_RIGHT
        );


        Button closeNoticeButton =
                new Button(
                        "Close"
                );


        closeNoticeButton.setPrefWidth(
                100
        );


        closeNoticeButton.setPrefHeight(
                36
        );


        closeNoticeButton.setCursor(
                Cursor.HAND
        );


        closeNoticeButton.setStyle(
                "-fx-background-color:#4e342e;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;"
        );


        popupFooter.getChildren().add(
                closeNoticeButton
        );


        noticePopup.getChildren().addAll(
                popupHeader,
                popupBody,
                popupFooter
        );


        noticeOverlay.getChildren().add(
                noticePopup
        );


        StackPane.setAlignment(
                noticePopup,
                Pos.CENTER
        );


        // =====================================================
        // VIEW NOTICE
        // =====================================================

        viewNoticeButton.setOnAction(e -> {

            noticeOverlay.setVisible(
                    true
            );


            noticeOverlay.setOpacity(
                    0
            );


            noticePopup.setOpacity(
                    0
            );


            noticePopup.setScaleX(
                    0.92
            );


            noticePopup.setScaleY(
                    0.92
            );


            FadeTransition overlayFade =
                    new FadeTransition(
                            Duration.millis(220),
                            noticeOverlay
                    );


            overlayFade.setFromValue(
                    0
            );

            overlayFade.setToValue(
                    1
            );


            FadeTransition popupFade =
                    new FadeTransition(
                            Duration.millis(250),
                            noticePopup
                    );


            popupFade.setFromValue(
                    0
            );

            popupFade.setToValue(
                    1
            );


            ScaleTransition popupScale =
                    new ScaleTransition(
                            Duration.millis(250),
                            noticePopup
                    );


            popupScale.setFromX(
                    0.92
            );

            popupScale.setFromY(
                    0.92
            );


            popupScale.setToX(
                    1.0
            );

            popupScale.setToY(
                    1.0
            );


            popupScale.setInterpolator(
                    Interpolator.EASE_OUT
            );


            ParallelTransition popupAnimation =
                    new ParallelTransition(
                            popupFade,
                            popupScale
                    );


            overlayFade.play();
            popupAnimation.play();
        });


        // =====================================================
        // CLOSE NOTICE
        // =====================================================

        closeNoticeButton.setOnAction(e -> {

            FadeTransition overlayFade =
                    new FadeTransition(
                            Duration.millis(180),
                            noticeOverlay
                    );


            overlayFade.setFromValue(
                    noticeOverlay.getOpacity()
            );


            overlayFade.setToValue(
                    0
            );


            FadeTransition popupFade =
                    new FadeTransition(
                            Duration.millis(160),
                            noticePopup
                    );


            popupFade.setFromValue(
                    noticePopup.getOpacity()
            );


            popupFade.setToValue(
                    0
            );


            ScaleTransition popupScale =
                    new ScaleTransition(
                            Duration.millis(180),
                            noticePopup
                    );


            popupScale.setToX(
                    0.92
            );

            popupScale.setToY(
                    0.92
            );


            ParallelTransition closeAnimation =
                    new ParallelTransition(
                            popupFade,
                            popupScale
                    );


            closeAnimation.setOnFinished(
                    event -> {

                        noticePopup.setVisible(
                                false
                        );


                        noticeOverlay.setVisible(
                                false
                        );


                        noticeOverlay.setOpacity(
                                0
                        );


                        noticePopup.setOpacity(
                                0
                        );


                        noticePopup.setScaleX(
                                0.92
                        );


                        noticePopup.setScaleY(
                                0.92
                        );
                    }
            );


            overlayFade.play();
            closeAnimation.play();
        });


        // =====================================================
        // BUTTON CLICK ANIMATION
        // =====================================================

        addButtonClickAnimation(
                viewNoticeButton
        );


        addButtonClickAnimation(
                closeNoticeButton
        );


        addButtonClickAnimation(
                cancelActionButton
        );


        addButtonClickAnimation(
                continueActionButton
        );


        // =====================================================
        // ADD ROOT ELEMENTS
        // =====================================================

        root.getChildren().add(
                dashboardRoot
        );


        root.getChildren().add(
                noticeOverlay
        );


        root.getChildren().add(
                actionOverlay
        );


        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        root,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );


        stage.setTitle(
                "Society360 - Guard Dashboard"
        );


        stage.setScene(
                scene
        );


        stage.show();


        // =====================================================
        // ANIMATIONS
        // =====================================================

        // =====================================================
        // 1. SIDEBAR
        // FIXED - NO ANIMATION
        // =====================================================

        sidebarNode.setOpacity(
                1
        );

        sidebarNode.setTranslateX(
                0
        );

        sidebarNode.setTranslateY(
                0
        );

        sidebarNode.setScaleX(
                1
        );

        sidebarNode.setScaleY(
                1
        );


        // =====================================================
        // 2. HEADER
        // FIXED - NO ANIMATION
        // =====================================================


        // =====================================================
        // 3. STAT CARDS
        // =====================================================

        animateCard(
                activeVisitors,
                150
        );


        animateCard(
                parking,
                220
        );


        animateCard(
                expectedVisitors,
                290
        );


        animateCard(
                sos,
                360
        );


        // =====================================================
        // 4. QUICK ACTIONS
        // =====================================================

        quickActionsSection.setOpacity(
                0
        );

        quickActionsSection.setTranslateY(
                30
        );


        FadeTransition quickFade =
                new FadeTransition(
                        Duration.millis(350),
                        quickActionsSection
                );


        quickFade.setFromValue(
                0
        );

        quickFade.setToValue(
                1
        );


        TranslateTransition quickMove =
                new TranslateTransition(
                        Duration.millis(350),
                        quickActionsSection
                );


        quickMove.setFromY(
                30
        );

        quickMove.setToY(
                0
        );


        ParallelTransition quickAnimation =
                new ParallelTransition(
                        quickFade,
                        quickMove
                );


        quickAnimation.setDelay(
                Duration.millis(450)
        );


        quickAnimation.play();


        // =====================================================
        // 5. SUMMARY
        // =====================================================

        summaryBox.setOpacity(
                0
        );

        summaryBox.setTranslateY(
                30
        );


        FadeTransition summaryFade =
                new FadeTransition(
                        Duration.millis(350),
                        summaryBox
                );


        summaryFade.setFromValue(
                0
        );

        summaryFade.setToValue(
                1
        );


        TranslateTransition summaryMove =
                new TranslateTransition(
                        Duration.millis(350),
                        summaryBox
                );


        summaryMove.setFromY(
                30
        );

        summaryMove.setToY(
                0
        );


        ParallelTransition summaryAnimation =
                new ParallelTransition(
                        summaryFade,
                        summaryMove
                );


        summaryAnimation.setDelay(
                Duration.millis(500)
        );


        summaryAnimation.play();


        // =====================================================
        // 6. NOTICE BAR
        // =====================================================

        noticeBar.setOpacity(
                0
        );

        noticeBar.setTranslateY(
                40
        );


        FadeTransition noticeFade =
                new FadeTransition(
                        Duration.millis(350),
                        noticeBar
                );


        noticeFade.setFromValue(
                0
        );

        noticeFade.setToValue(
                1
        );


        TranslateTransition noticeMove =
                new TranslateTransition(
                        Duration.millis(350),
                        noticeBar
                );


        noticeMove.setFromY(
                40
        );

        noticeMove.setToY(
                0
        );


        ParallelTransition noticeAnimation =
                new ParallelTransition(
                        noticeFade,
                        noticeMove
                );


        noticeAnimation.setDelay(
                Duration.millis(700)
        );


        noticeAnimation.play();


        // =====================================================
        // 7. NOTICE BUTTON
        // =====================================================

        animateSimpleButton(
                viewNoticeButton
        );


        return scene;
    }


    // =========================================================
    // CARD CLICK ANIMATION
    // =========================================================

    private static void addCardClickAnimation(
            VBox card) {

        card.setCursor(
                Cursor.HAND
        );


        card.setOnMousePressed(e -> {

            ScaleTransition press =
                    new ScaleTransition(
                            Duration.millis(100),
                            card
                    );


            press.setToX(
                    0.97
            );


            press.setToY(
                    0.97
            );


            press.setInterpolator(
                    Interpolator.EASE_OUT
            );


            press.play();
        });


        card.setOnMouseReleased(e -> {

            ScaleTransition release =
                    new ScaleTransition(
                            Duration.millis(140),
                            card
                    );


            release.setToX(
                    1.0
            );


            release.setToY(
                    1.0
            );


            release.setInterpolator(
                    Interpolator.EASE_OUT
            );


            release.play();
        });
    }


    // =========================================================
    // BUTTON CLICK ANIMATION
    // =========================================================

    private static void addButtonClickAnimation(
            Button button) {

        button.setCursor(
                Cursor.HAND
        );


        button.setOnMousePressed(e -> {

            ScaleTransition press =
                    new ScaleTransition(
                            Duration.millis(90),
                            button
                    );


            press.setToX(
                    0.96
            );


            press.setToY(
                    0.96
            );


            press.setInterpolator(
                    Interpolator.EASE_OUT
            );


            press.play();
        });


        button.setOnMouseReleased(e -> {

            ScaleTransition release =
                    new ScaleTransition(
                            Duration.millis(130),
                            button
                    );


            release.setToX(
                    1.0
            );


            release.setToY(
                    1.0
            );


            release.setInterpolator(
                    Interpolator.EASE_OUT
            );


            release.play();
        });
    }


    // =========================================================
    // STAT CARD
    // =========================================================

    private static VBox createStatCard(
            String title,
            String value,
            String subtitle) {

        VBox card =
                new VBox(5);


        card.setPrefWidth(
                285
        );


        card.setPrefHeight(
                110
        );


        card.setPadding(
                new Insets(15)
        );


        card.setStyle(
                "-fx-background-color:#E8F0E8;" +
                "-fx-background-radius:10;"
        );


        Label titleLabel =
                new Label(
                        title
                );


        titleLabel.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#0b0d0c;"
        );


        Label valueLabel =
                new Label(
                        value
                );


        valueLabel.setStyle(
                "-fx-font-size:26px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#030A12;"
        );


        Label subtitleLabel =
                new Label(
                        subtitle
                );


        subtitleLabel.setStyle(
                "-fx-font-size:10px;" +
                "-fx-text-fill:#52605A;"
        );


        card.getChildren().addAll(
                titleLabel,
                valueLabel,
                subtitleLabel
        );


        addCardClickAnimation(
                card
        );


        return card;
    }


    // =========================================================
    // SUMMARY ROW
    // =========================================================

    private static HBox createSummaryRow(
            String title,
            String value) {

        Label titleLabel =
                new Label(
                        title
                );


        titleLabel.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#183A2D;"
        );


        Label valueLabel =
                new Label(
                        value
                );


        valueLabel.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#030A12;"
        );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        HBox row =
                new HBox();


        row.setAlignment(
                Pos.CENTER_LEFT
        );


        row.setPadding(
                new Insets(
                        2,
                        0,
                        2,
                        0
                )
        );


        row.getChildren().addAll(
                titleLabel,
                spacer,
                valueLabel
        );


        return row;
    }


    // =========================================================
    // CARD ENTRY ANIMATION
    // =========================================================

    private static void animateCard(
            VBox card,
            int delay) {

        card.setOpacity(
                0
        );


        card.setTranslateY(
                30
        );


        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(300),
                        card
                );


        fade.setFromValue(
                0
        );


        fade.setToValue(
                1
        );


        TranslateTransition move =
                new TranslateTransition(
                        Duration.millis(300),
                        card
                );


        move.setFromY(
                30
        );


        move.setToY(
                0
        );


        ParallelTransition animation =
                new ParallelTransition(
                        fade,
                        move
                );


        animation.setDelay(
                Duration.millis(delay)
        );


        animation.play();
    }


    // =========================================================
    // SIMPLE BUTTON HOVER ANIMATION
    // =========================================================

    private static void animateSimpleButton(
            Button button) {

        button.setOnMouseEntered(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );


            scale.setToX(
                    1.05
            );


            scale.setToY(
                    1.05
            );


            scale.play();


            button.setStyle(
                    "-fx-background-color:#4e342e;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-size:12px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-background-radius:7;"
            );
        });


        button.setOnMouseExited(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );


            scale.setToX(
                    1.0
            );


            scale.setToY(
                    1.0
            );


            scale.play();


            button.setStyle(
                    "-fx-background-color:#434141;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-size:12px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-background-radius:7;"
            );
        });
    }
}