package com.society.view.Guard_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.controller.Guard_Controller.EmergencyController;
import com.society.controller.Guard_Controller.GuardController;
import com.society.controller.Guard_Controller.NoticeController;
import com.society.controller.Guard_Controller.VisitorController;
import com.society.dao.Guard_dao.ParkingDao;
import com.society.model.Guard_model.EmergencyModel;
import com.society.model.Guard_model.GuardModel;
import com.society.model.Guard_model.NoticeModel;
import com.society.model.Guard_model.ParkingModel;
import com.society.model.Guard_model.VisitorModel;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GuardDashboard {

    public static Scene createScene(
            Stage stage,
            String guardEmail) {

       

        GuardModel guard = null;
        String loadedSociety = null;

        if (guardEmail != null &&
                !guardEmail.trim().isEmpty()) {

            try {

                GuardController guardController =
                        new GuardController();

                guard =
                        guardController.getGuard(
                                guardEmail.trim()
                        );

                if (guard != null) {
                    loadedSociety =
                            guard.getSociety();
                }

            } catch (Exception ex) {

                System.out.println(
                        "WARNING: Could not load guard information."
                );

                ex.printStackTrace();
            }
        }

        final GuardModel loggedInGuard = guard;
        final String society = loadedSociety;

        // =========================================================
        // ROOT
        // =========================================================

        StackPane root =
                new StackPane();

        BorderPane dashboardRoot =
                new BorderPane();

        // =========================================================
        // NOTICE CONTROLLER
        // =========================================================

        NoticeController noticeController =
                new NoticeController();

        NoticeModel latestNotice =
                noticeController.getLatestActiveNotice();

        // =========================================================
        // SIDEBAR
        // =========================================================

        GuardSidebar sidebar =
                new GuardSidebar(
                        stage,
                        "Dashboard",
                        guardEmail,
                        society
                );

        dashboardRoot.setLeft(
                sidebar.getSidebar()
        );

        // =========================================================
        // MAIN CONTENT
        // =========================================================

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

        mainContent.setSpacing(18);

        mainContent.setStyle(
                "-fx-background-color: #e8ddd5;"
        );

        // =========================================================
        // HEADER
        // =========================================================

        HBox header =
                new HBox();

        header.setPadding(
                new Insets(
                        25,
                        40,
                        25,
                        40
                )
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setStyle(
                "-fx-background-color: #4e342e;"
        );

        VBox titleBox =
                new VBox(3);

        // =========================================================
        // GUARD NAME
        // =========================================================

        String guardName = "-";

        if (loggedInGuard != null &&
                loggedInGuard.getName() != null &&
                !loggedInGuard.getName().trim().isEmpty()) {

            guardName =
                    loggedInGuard.getName().trim();
        }

        Label title =
                new Label(
                        "Good Morning, "
                                + guardName
                );

        title.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-family: 'Bodoni MT';" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill: white;"
        );

        // =========================================================
        // GUARD DUTY INFORMATION
        // =========================================================

        String gate = "-";
        String shift = "-";

        if (loggedInGuard != null) {

            if (loggedInGuard.getGate() != null &&
                    !loggedInGuard.getGate()
                            .trim()
                            .isEmpty()) {

                gate =
                        loggedInGuard.getGate()
                                .trim();
            }

            if (loggedInGuard.getShift() != null &&
                    !loggedInGuard.getShift()
                            .trim()
                            .isEmpty()) {

                shift =
                        loggedInGuard.getShift()
                                .trim();
            }
        }

        Label description =
                new Label(
                        gate
                                + "    "
                                + shift
                );

        description.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill: white;"
        );

        titleBox.getChildren().addAll(
                title,
                description
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        // =========================================================
        // CURRENT DATE
        // =========================================================

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

        // =========================================================
        // FIRESTORE VISITOR / EMERGENCY DATA
        // =========================================================

        VisitorController visitorController =
                new VisitorController();

        EmergencyController emergencyController =
                new EmergencyController();

        ParkingDao parkingDao =
                new ParkingDao();

        List<EmergencyModel> activeEmergencies =
                emergencyController
                        .getActiveEmergencies();

        int activeEmergencyCount = 0;

        if (activeEmergencies != null) {
            activeEmergencyCount =
                    activeEmergencies.size();
        }

        List<VisitorModel> visitors =
                visitorController.getAllVisitors();

        int activeVisitorsCount = 0;
        int expectedVisitorsCount = 0;

        int totalVisitorsCount = 0;

        int deliveryCount = 0;
        int personalGuestCount = 0;
        int exitsCount = 0;

        if (visitors != null) {

            totalVisitorsCount =
                    visitors.size();

            for (VisitorModel visitor :
                    visitors) {

                if (visitor == null) {
                    continue;
                }

                String purpose =
                        visitor.getPurpose();

                String status =
                        visitor.getStatus();

                // =================================================
                // DELIVERY / SERVICE
                // =================================================

                if ("Delivery".equalsIgnoreCase(
                        purpose)
                        ||
                        "Service".equalsIgnoreCase(
                                purpose)) {

                    deliveryCount++;

                } else {

                    // =================================================
                    // PERSONAL / GUEST
                    // =================================================

                    personalGuestCount++;
                }

                // =================================================
                // EXITS
                // =================================================

                if ("Checked Out".equalsIgnoreCase(
                        status)) {

                    exitsCount++;
                }

                // =================================================
                // ACTIVE VISITORS
                // =================================================

                if ("Inside".equalsIgnoreCase(
                        status)) {

                    activeVisitorsCount++;
                }

                // =================================================
                // EXPECTED VISITORS
                // =================================================

                if ("APPROVED".equalsIgnoreCase(
                        status)) {

                    expectedVisitorsCount++;
                }
            }
        }

        // =========================================================
        // PARKING OCCUPANCY FROM FIRESTORE
        // =========================================================

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

        // =========================================================
        // TOP STATISTICS
        // =========================================================

        VBox activeVisitors =
                createStatCard(
                        "Active Visitors",
                        String.valueOf(
                                activeVisitorsCount
                        ),
                        "Currently inside"
                );

        VBox parking =
                createStatCard(
                        "Parking Occupancy",
                        occupiedParkingSpots
                                + "/"
                                + totalParkingSpots,
                        parkingPercentage
                                + "% Full"
                );

        VBox expectedVisitors =
                createStatCard(
                        "Expected Visitors",
                        String.valueOf(
                                expectedVisitorsCount
                        ),
                        "Approved visitors"
                );

        VBox sos =
                createStatCard(
                        "SOS Alerts",
                        String.valueOf(
                                activeEmergencyCount
                        ),
                        "Active alerts"
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

        // =========================================================
        // QUICK ACTIONS TITLE
        // =========================================================

        Label quickActionsTitle =
                new Label(
                        "Quick Actions"
                );

        quickActionsTitle.setStyle(
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #030A12;"
        );

        Label quickActionsSubtitle =
                new Label(
                        "Frequently used gate operations"
                );

        quickActionsSubtitle.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #263B35;"
        );

        VBox quickHeading =
                new VBox(3);

        quickHeading.getChildren().addAll(
                quickActionsTitle,
                quickActionsSubtitle
        );

        // =========================================================
        // QUICK ACTION BUTTONS
        // =========================================================

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

        // =========================================================
        // QUICK ACTION BUTTON STYLE
        // =========================================================

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

            button.setStyle(
                    "-fx-background-color: #4e342e;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;"
            );

            button.setOnMouseEntered(e -> {

                button.setStyle(
                        "-fx-background-color: #522c23;" +
                        "-fx-text-fill: #E8F0E8;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
                );
            });

            button.setOnMouseExited(e -> {

                button.setStyle(
                        "-fx-background-color: #4e342e;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
                );
            });
        }

        // =========================================================
        // QUICK ACTION NAVIGATION
        // =========================================================

        visitorLogButton.setOnAction(e -> {

            stage.setScene(
                    VisitorLog.createScene(
                            stage,
                            guardEmail
                    )
            );
        });

        manualEntryButton.setOnAction(e -> {

            stage.setScene(
                    ManualVisitorEntry.createScene(
                            stage,
                            guardEmail
                    )
            );
        });

        parkingEntryButton.setOnAction(e -> {

            stage.setScene(
                    Parking.createScene(
                            stage,
                            guardEmail,
                            society
                    )
            );
        });

        emergencySOSButton.setOnAction(e -> {

            stage.setScene(
                    EmergencySOS.createScene(
                            stage,
                            guardEmail
                    )
            );
        });

        // =========================================================
        // QUICK ACTION ROWS
        // =========================================================

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

        // =========================================================
        // TODAY'S SUMMARY
        // =========================================================

        Label summaryTitle =
                new Label(
                        "Today's Summary"
                );

        summaryTitle.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #030A12;"
        );

        Label summarySubtitle =
                new Label(
                        "Gate activity for today"
                );

        summarySubtitle.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #52605A;"
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
                "-fx-background-color: #E8F0E8;" +
                "-fx-background-radius: 10;"
        );

        summaryBox.getChildren().addAll(
                summaryHeading,
                totalVisitorsRow,
                deliveryRow,
                personalRow,
                vehiclesRow,
                exitsRow
        );

        // =========================================================
        // LOWER SECTION
        // =========================================================

        HBox lowerSection =
                new HBox(28);

        lowerSection.setAlignment(
                Pos.TOP_LEFT
        );

        lowerSection.getChildren().addAll(
                quickActionsSection,
                summaryBox
        );

        // =========================================================
        // IMPORTANT NOTICE
        // =========================================================

        VBox noticeContent =
                new VBox(4);

        Label noticeLabel =
                new Label(
                        "IMPORTANT NOTICE"
                );

        noticeLabel.setStyle(
                "-fx-font-size: 10px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #183A2D;"
        );

        Label noticeText =
                new Label();

        noticeText.setWrapText(
                true
        );

        if (latestNotice != null) {

            noticeText.setText(
                    safeText(
                            latestNotice.getTitle()
                    )
            );

        } else {

            noticeText.setText(
                    "No active notices."
            );
        }

        noticeText.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #050605;"
        );

        Label noticeInfo =
                new Label();

        noticeInfo.setWrapText(
                true
        );

        if (latestNotice != null) {

            noticeInfo.setText(
                    safeText(
                            latestNotice.getDescription()
                    )
            );

        } else {

            noticeInfo.setText(
                    "There are currently no active notices."
            );
        }

        noticeInfo.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #52605A;"
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

        viewNoticeButton.setStyle(
                "-fx-background-color: #4e342e;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;" +
                "-fx-cursor: hand;"
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
                "-fx-background-color: #E8F0E8;" +
                "-fx-background-radius: 10;"
        );

        noticeBar.getChildren().addAll(
                noticeContent,
                noticeSpacer,
                viewNoticeButton
        );

        // =========================================================
        // ADD CONTENT
        // =========================================================

        mainContent.getChildren().addAll(
                statCards,
                lowerSection,
                noticeBar
        );

        // =========================================================
        // MAIN AREA
        // =========================================================

        BorderPane mainarea =
                new BorderPane();

        mainarea.setTop(
                header
        );

        mainarea.setCenter(
                mainContent
        );

        dashboardRoot.setCenter(
                mainarea
        );

        // =========================================================
        // NOTICE POPUP OVERLAY
        // =========================================================

        Region noticeOverlay =
                new Region();

        noticeOverlay.setStyle(
                "-fx-background-color: rgba(0,0,0,0.28);"
        );

        noticeOverlay.setVisible(
                false
        );

        // =========================================================
        // NOTICE POPUP
        // =========================================================

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

        noticePopup.setStyle(
                "-fx-background-color: #F1F5F1;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: #4e342e;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 16;"
        );

        // =========================================================
        // POPUP HEADER
        // =========================================================

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
                "-fx-background-color: #E8F0E8;" +
                "-fx-background-radius: 16 16 0 0;"
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
                "-fx-background-color: #4e342e;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 50%;"
        );

        VBox headerText =
                new VBox(2);

        Label popupTitle =
                new Label(
                        "IMPORTANT NOTICES"
                );

        popupTitle.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #183A2D;"
        );

        Label popupHeading =
                new Label(
                        "Latest Notice"
                );

        popupHeading.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4e342e;"
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

        // =========================================================
        // POPUP BODY
        // =========================================================

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

        // =========================================================
        // NOTICE TITLE
        // =========================================================

        Label popupNoticeTitle =
                new Label();

        popupNoticeTitle.setWrapText(
                true
        );

        popupNoticeTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4e342e;"
        );

        // =========================================================
        // NOTICE DATE
        // =========================================================

        Label popupNoticeDate =
                new Label();

        popupNoticeDate.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #52605A;"
        );

        // =========================================================
        // NOTICE DESCRIPTION
        // =========================================================

        Label popupNoticeDescription =
                new Label();

        popupNoticeDescription.setWrapText(
                true
        );

        popupNoticeDescription.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #070707;"
        );

        // =========================================================
        // NOTICE STATUS
        // =========================================================

        Label popupNoticeStatus =
                new Label();

        popupNoticeStatus.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #263B35;"
        );

        // =========================================================
        // LOAD NOTICE DATA
        // =========================================================

        if (latestNotice != null) {

            popupNoticeTitle.setText(
                    safeText(
                            latestNotice.getTitle()
                    )
            );

            popupNoticeDate.setText(
                    "Date: "
                            + safeText(
                                    latestNotice.getDate()
                            )
            );

            popupNoticeDescription.setText(
                    safeText(
                            latestNotice.getDescription()
                    )
            );

            popupNoticeStatus.setText(
                    "Status: "
                            + safeText(
                                    latestNotice.getStatus()
                            )
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

        // =========================================================
        // SCROLLABLE NOTICE BODY
        // =========================================================

        ScrollPane noticeScroll =
                new ScrollPane(
                        popupBody
                );

        noticeScroll.setFitToWidth(
                true
        );

        noticeScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        noticeScroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        noticeScroll.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;"
        );

        VBox.setVgrow(
                noticeScroll,
                Priority.ALWAYS
        );

        // =========================================================
        // POPUP FOOTER
        // =========================================================

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

        closeNoticeButton.setStyle(
                "-fx-background-color: #4e342e;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;" +
                "-fx-cursor: hand;"
        );

        closeNoticeButton.setOnMouseEntered(e -> {

            closeNoticeButton.setStyle(
                    "-fx-background-color: #3f2924;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 12px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 7;" +
                    "-fx-cursor: hand;"
            );
        });

        closeNoticeButton.setOnMouseExited(e -> {

            closeNoticeButton.setStyle(
                    "-fx-background-color: #4e342e;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 12px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 7;" +
                    "-fx-cursor: hand;"
            );
        });

        closeNoticeButton.setOnAction(e -> {

            noticePopup.setVisible(
                    false
            );

            noticeOverlay.setVisible(
                    false
            );
        });

        popupFooter.getChildren().add(
                closeNoticeButton
        );

        // =========================================================
        // ADD POPUP CONTENT
        // =========================================================

        noticePopup.getChildren().addAll(
                popupHeader,
                noticeScroll,
                popupFooter
        );

        noticePopup.setVisible(
                false
        );

        // =========================================================
        // VIEW NOTICE ACTION
        // =========================================================

        viewNoticeButton.setOnAction(e -> {

            noticeOverlay.setVisible(
                    true
            );

            noticePopup.setVisible(
                    true
            );
        });

        // =========================================================
        // ADD EVERYTHING TO ROOT
        // =========================================================

        root.getChildren().addAll(
                dashboardRoot,
                noticeOverlay,
                noticePopup
        );

        // =========================================================
        // SCENE
        // =========================================================

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

        return scene;
    }

    // =========================================================
    // SAFE TEXT
    // =========================================================

    private static String safeText(
            String text) {

        if (text == null ||
                text.trim().isEmpty()) {

            return "-";
        }

        return text.trim();
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
                "-fx-background-color: #E8F0E8;" +
                "-fx-background-radius: 10;"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #0b0d0c;"
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #030A12;"
        );

        Label subtitleLabel =
                new Label(subtitle);

        subtitleLabel.setStyle(
                "-fx-font-size: 10px;" +
                "-fx-text-fill: #52605A;"
        );

        card.getChildren().addAll(
                titleLabel,
                valueLabel,
                subtitleLabel
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
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #183A2D;"
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #030A12;"
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
}