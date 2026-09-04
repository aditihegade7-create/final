package com.society.view.Guard_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.society.controller.Guard_Controller.DailyReportsController;
import com.society.controller.Guard_Controller.EmergencyController;
import com.society.controller.Guard_Controller.GuardController;
import com.society.controller.Guard_Controller.VisitorController;
import com.society.dao.Guard_dao.ParkingDao;
import com.society.model.Guard_model.EmergencyModel;
import com.society.model.Guard_model.GuardModel;
import com.society.model.Guard_model.ParkingModel;
import com.society.model.Guard_model.VisitorModel;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GuardGenerateReports {

    // =====================================================
    // CREATE SCENE
    // =====================================================

    public Scene createScene(Stage stage, String guardEmail) {

        String normalizedEmail =
                guardEmail == null
                        ? ""
                        : guardEmail.trim().toLowerCase();

        // =================================================
        // LOAD GUARD
        // =================================================

        GuardModel guard = null;

        try {

            if (!normalizedEmail.isEmpty()) {

                guard =
                        new GuardController()
                                .getGuard(normalizedEmail);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        // =================================================
        // GUARD DETAILS
        // These are final because they are used inside
        // JavaFX lambda expressions.
        // =================================================

        final String society;
        final String gate;
        final String shift;
        final String guardName;

        if (guard != null) {

            String loadedSociety =
                    guard.getSociety();

            String loadedGate =
                    guard.getGate();

            String loadedShift =
                    guard.getShift();

            String loadedGuardName =
                    guard.getName();

            society =
                    loadedSociety != null &&
                    !loadedSociety.trim().isEmpty()
                            ? loadedSociety.trim()
                            : "";

            gate =
                    loadedGate != null &&
                    !loadedGate.trim().isEmpty()
                            ? loadedGate.trim()
                            : "Main Gate";

            shift =
                    loadedShift != null &&
                    !loadedShift.trim().isEmpty()
                            ? loadedShift.trim()
                            : "Shift A";

            guardName =
                    loadedGuardName != null &&
                    !loadedGuardName.trim().isEmpty()
                            ? loadedGuardName.trim()
                            : "-";

        } else {

            society = "";
            gate = "Main Gate";
            shift = "Shift A";
            guardName = "-";
        }

        // =================================================
        // ROOT
        // =================================================

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color:#E8DDD5;"
        );

        // =================================================
        // SIDEBAR
        // DO NOT CHANGE
        // =================================================

        GuardSidebar sidebar =
                new GuardSidebar(
                        stage,
                        "Daily Reports",
                        normalizedEmail,
                        society
                );

        root.setLeft(
                sidebar.getSidebar()
        );

        // =================================================
        // MAIN AREA
        //
        // Header + content are placed together here.
        // This prevents the header from stretching across
        // the sidebar.
        // =================================================

        BorderPane mainArea =
                new BorderPane();

        mainArea.setStyle(
                "-fx-background-color:#E8DDD5;"
        );

        // =================================================
        // HEADER
        // Same design as Guard Dashboard
        // =================================================

        HBox header =
                new HBox();

        header.setPadding(
                new Insets(
                        22,
                        35,
                        22,
                        45
                )
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setStyle(
                "-fx-background-color:#4E342E;"
        );

        VBox titleBox =
                new VBox(4);

        Label title =
                new Label(
                        "Daily Reports"
                );

        title.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        Label subtitle =
                new Label(
                        "Security Shift Report  •  "
                        + gate
                        + "  •  "
                        + shift
                );

        subtitle.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#F3E9E3;"
        );

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );

        VBox dateBox =
                new VBox(3);

        dateBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        Label day =
                new Label(
                        LocalDate.now().format(
                                DateTimeFormatter.ofPattern(
                                        "EEEE"
                                )
                        )
                );

        Label date =
                new Label(
                        LocalDate.now().format(
                                DateTimeFormatter.ofPattern(
                                        "dd MMMM yyyy"
                                )
                        )
                );

        day.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        date.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:white;"
        );

        dateBox.getChildren().addAll(
                day,
                date
        );

        header.getChildren().addAll(
                titleBox,
                headerSpacer,
                dateBox
        );

        mainArea.setTop(
                header
        );

        // =================================================
        // MAIN CONTENT
        // =================================================

        VBox content =
                new VBox(12);

        content.setPadding(
                new Insets(
                        18,
                        35,
                        18,
                        35
                )
        );

        content.setFillWidth(true);

        // =================================================
        // SHIFT OVERVIEW
        // =================================================

        VBox overviewCard =
                createCard();

        Label overviewTitle =
                sectionTitle(
                        "SHIFT OVERVIEW"
                );

        GridPane overviewGrid =
                new GridPane();

        overviewGrid.setHgap(0);
        overviewGrid.setVgap(0);

        VBox societyBox =
                infoBox(
                        "SOCIETY",
                        society.isEmpty()
                                ? "-"
                                : society
                );

        VBox gateBox =
                infoBox(
                        "GATE",
                        gate
                );

        VBox shiftBox =
                infoBox(
                        "SHIFT",
                        shift
                );

        VBox guardBox =
                infoBox(
                        "GUARD",
                        guardName
                );

        overviewGrid.add(
                societyBox,
                0,
                0
        );

        overviewGrid.add(
                gateBox,
                1,
                0
        );

        overviewGrid.add(
                shiftBox,
                2,
                0
        );

        overviewGrid.add(
                guardBox,
                3,
                0
        );

        overviewCard.getChildren().addAll(
                overviewTitle,
                overviewGrid
        );

        // =================================================
        // STAT VALUES
        // =================================================

        Label totalVisitorsValue =
                statValue("0");

        Label insideVisitorsValue =
                statValue("0");

        Label checkedOutValue =
                statValue("0");

        Label deliveriesValue =
                statValue("0");

        Label parkingOccupiedValue =
                statValue("0");

        Label parkingAvailableValue =
                statValue("0");

        // =================================================
        // VISITOR ACTIVITY
        // =================================================

        VBox visitorCard =
                createCard();

        HBox visitorHeader =
                new HBox();

        visitorHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        Label visitorTitle =
                sectionTitle(
                        "VISITOR ACTIVITY"
                );

        Region visitorSpacer =
                new Region();

        HBox.setHgrow(
                visitorSpacer,
                Priority.ALWAYS
        );

        Label visitorInfo =
                new Label(
                        "Today's visitor movement"
                );

        visitorInfo.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#81756F;"
        );

        visitorHeader.getChildren().addAll(
                visitorTitle,
                visitorSpacer,
                visitorInfo
        );

        GridPane visitorGrid =
                new GridPane();

        visitorGrid.setHgap(10);
        visitorGrid.setVgap(10);

        visitorGrid.add(
                createMetric(
                        "TOTAL VISITORS",
                        totalVisitorsValue
                ),
                0,
                0
        );

        visitorGrid.add(
                createMetric(
                        "CURRENTLY INSIDE",
                        insideVisitorsValue
                ),
                1,
                0
        );

        visitorGrid.add(
                createMetric(
                        "CHECKED OUT",
                        checkedOutValue
                ),
                2,
                0
        );

        visitorGrid.add(
                createMetric(
                        "DELIVERY / SERVICE",
                        deliveriesValue
                ),
                3,
                0
        );

        HBox visitorRecordBar =
                new HBox();

        visitorRecordBar.setAlignment(
                Pos.CENTER_LEFT
        );

        visitorRecordBar.setPadding(
                new Insets(
                        8,
                        12,
                        8,
                        12
                )
        );

        visitorRecordBar.setStyle(
                "-fx-background-color:#F7F2EF;" +
                "-fx-background-radius:7;" +
                "-fx-border-color:#E5D9D2;" +
                "-fx-border-radius:7;"
        );

        Label visitorRecordText =
                new Label(
                        "Visitor entries are stored separately " +
                        "for Secretary review."
                );

        visitorRecordText.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#5F5550;"
        );

        visitorRecordBar.getChildren().add(
                visitorRecordText
        );

        visitorCard.getChildren().addAll(
                visitorHeader,
                visitorGrid,
                visitorRecordBar
        );

        // =================================================
        // PARKING
        // =================================================

        VBox parkingCard =
                createCard();

        HBox parkingHeader =
                new HBox();

        parkingHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        Label parkingTitle =
                sectionTitle(
                        "PARKING STATUS"
                );

        Region parkingSpacer =
                new Region();

        HBox.setHgrow(
                parkingSpacer,
                Priority.ALWAYS
        );

        Label parkingInfo =
                new Label(
                        "Current parking"
                );

        parkingInfo.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#81756F;"
        );

        parkingHeader.getChildren().addAll(
                parkingTitle,
                parkingSpacer,
                parkingInfo
        );

        HBox parkingMetrics =
                new HBox(10);

        parkingMetrics.setFillHeight(true);

        parkingMetrics.getChildren().addAll(

                createMetric(
                        "OCCUPIED",
                        parkingOccupiedValue
                ),

                createMetric(
                        "AVAILABLE",
                        parkingAvailableValue
                )
        );

        parkingCard.getChildren().addAll(
                parkingHeader,
                parkingMetrics
        );

        // =================================================
        // VISITOR + PARKING ROW
        // =================================================

        HBox activityRow =
                new HBox(16);

        visitorCard.setMaxWidth(
                Double.MAX_VALUE
        );

        parkingCard.setMaxWidth(
                Double.MAX_VALUE
        );

        HBox.setHgrow(
                visitorCard,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                parkingCard,
                Priority.ALWAYS
        );

        activityRow.getChildren().addAll(
                visitorCard,
                parkingCard
        );

        // =================================================
        // SECURITY STATUS
        // =================================================

        VBox securityCard =
                createCard();

        Label securityTitle =
                sectionTitle(
                        "SECURITY STATUS"
                );

        HBox securityStatusBox =
                new HBox(12);

        securityStatusBox.setAlignment(
                Pos.CENTER_LEFT
        );

        securityStatusBox.setPadding(
                new Insets(
                        10,
                        14,
                        10,
                        14
                )
        );

        securityStatusBox.setStyle(
                "-fx-background-color:#F3F7F2;" +
                "-fx-background-radius:8;" +
                "-fx-border-color:#D7E3D5;" +
                "-fx-border-radius:8;"
        );

        Label securityIcon =
                new Label("✓");

        securityIcon.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#2F6652;"
        );

        Label emergencyStatus =
                new Label(
                        "Checking security status..."
                );

        emergencyStatus.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#2F6652;"
        );

        securityStatusBox.getChildren().addAll(
                securityIcon,
                emergencyStatus
        );

        securityCard.getChildren().addAll(
                securityTitle,
                securityStatusBox
        );

        // =================================================
        // SHIFT NOTES
        // =================================================

        VBox notesCard =
                createCard();

        HBox notesHeader =
                new HBox();

        notesHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        Label notesTitle =
                sectionTitle(
                        "SHIFT NOTES"
                );

        Region notesSpacer =
                new Region();

        HBox.setHgrow(
                notesSpacer,
                Priority.ALWAYS
        );

        Label notesHint =
                new Label(
                        "Optional"
                );

        notesHint.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#81756F;"
        );

        notesHeader.getChildren().addAll(
                notesTitle,
                notesSpacer,
                notesHint
        );

        TextArea notesArea =
                new TextArea();

        notesArea.setPromptText(
                "Add any important information from your shift..."
        );

        notesArea.setPrefRowCount(2);

        notesArea.setWrapText(true);

        notesArea.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#D5C7C0;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-font-size:13px;"
        );

        notesCard.getChildren().addAll(
                notesHeader,
                notesArea
        );

        // =================================================
        // BUTTONS
        // =================================================

        Button refreshButton =
                new Button(
                        "Refresh Report"
                );

        refreshButton.setStyle(
                "-fx-background-color:white;" +
                "-fx-text-fill:#4E342E;" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-border-color:#4E342E;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-padding:9 18;"
        );

        Button sendButton =
                new Button(
                        "Send Report to Secretary"
                );

        sendButton.setStyle(
                "-fx-background-color:#4E342E;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-padding:9 20;"
        );

        HBox buttonRow =
                new HBox(10);

        buttonRow.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonRow.getChildren().addAll(
                refreshButton,
                sendButton
        );

        // =================================================
        // LOAD REPORT DATA
        // =================================================

        Runnable loadReport =
                () -> {

                    try {

                        // =====================================
                        // VISITORS
                        // =====================================

                        List<VisitorModel> visitors;

                        if (normalizedEmail.isEmpty()) {

                            visitors =
                                    new ArrayList<>();

                        } else {

                            visitors =
                                    new VisitorController()
                                            .getVisitorsByGuardEmail(
                                                    normalizedEmail
                                            );
                        }

                        if (visitors == null) {

                            visitors =
                                    new ArrayList<>();
                        }

                        int total =
                                visitors.size();

                        int inside = 0;
                        int checkedOut = 0;
                        int deliveries = 0;

                        for (VisitorModel visitor :
                                visitors) {

                            if (visitor == null) {
                                continue;
                            }

                            String status =
                                    visitor.getStatus();

                            String purpose =
                                    visitor.getPurpose();

                            if (status != null &&
                                    "Inside"
                                            .equalsIgnoreCase(
                                                    status
                                            )) {

                                inside++;
                            }

                            if (status != null &&
                                    "Checked Out"
                                            .equalsIgnoreCase(
                                                    status
                                            )) {

                                checkedOut++;
                            }

                            if (purpose != null &&
                                    (
                                            "Delivery"
                                                    .equalsIgnoreCase(
                                                            purpose
                                                    )
                                            ||
                                            "Service"
                                                    .equalsIgnoreCase(
                                                            purpose
                                                    )
                                    )) {

                                deliveries++;
                            }
                        }

                        totalVisitorsValue.setText(
                                String.valueOf(total)
                        );

                        insideVisitorsValue.setText(
                                String.valueOf(inside)
                        );

                        checkedOutValue.setText(
                                String.valueOf(checkedOut)
                        );

                        deliveriesValue.setText(
                                String.valueOf(deliveries)
                        );

                        // =====================================
                        // PARKING
                        // =====================================

                        List<ParkingModel> parkingList;

                        if (normalizedEmail.isEmpty()) {

                            parkingList =
                                    new ArrayList<>();

                        } else {

                            parkingList =
                                    new ParkingDao()
                                            .getAllParking(
                                                    normalizedEmail
                                            );
                        }

                        if (parkingList == null) {

                            parkingList =
                                    new ArrayList<>();
                        }

                        int occupied = 0;

                        for (ParkingModel parking :
                                parkingList) {

                            if (parking != null &&
                                    parking.isOccupied()) {

                                occupied++;
                            }
                        }

                        int available =
                                Math.max(
                                        0,
                                        parkingList.size()
                                                - occupied
                                );

                        parkingOccupiedValue.setText(
                                String.valueOf(
                                        occupied
                                )
                        );

                        parkingAvailableValue.setText(
                                String.valueOf(
                                        available
                                )
                        );

                        // =====================================
                        // EMERGENCY
                        // =====================================

                        boolean emergencyActive =
                                false;

                        if (!normalizedEmail.isEmpty()) {

                            List<EmergencyModel>
                                    activeEmergencies =
                                    new EmergencyController()
                                            .getActiveEmergenciesByEmail(
                                                    normalizedEmail
                                            );

                            emergencyActive =
                                    activeEmergencies != null
                                            &&
                                    !activeEmergencies.isEmpty();
                        }

                        if (emergencyActive) {

                            emergencyStatus.setText(
                                    "Active emergency alert reported"
                            );

                            emergencyStatus.setTextFill(
                                    Color.web(
                                            "#B33434"
                                    )
                            );

                            securityIcon.setText("!");

                            securityIcon.setTextFill(
                                    Color.web(
                                            "#B33434"
                                    )
                            );

                            securityStatusBox.setStyle(
                                    "-fx-background-color:#FDF1F1;" +
                                    "-fx-background-radius:8;" +
                                    "-fx-border-color:#E8CACA;" +
                                    "-fx-border-radius:8;"
                            );

                        } else {

                            emergencyStatus.setText(
                                    "No active emergency alerts"
                            );

                            emergencyStatus.setTextFill(
                                    Color.web(
                                            "#2F6652"
                                    )
                            );

                            securityIcon.setText("✓");

                            securityIcon.setTextFill(
                                    Color.web(
                                            "#2F6652"
                                    )
                            );

                            securityStatusBox.setStyle(
                                    "-fx-background-color:#F3F7F2;" +
                                    "-fx-background-radius:8;" +
                                    "-fx-border-color:#D7E3D5;" +
                                    "-fx-border-radius:8;"
                            );
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();

                        emergencyStatus.setText(
                                "Unable to load security status"
                        );

                        emergencyStatus.setTextFill(
                                Color.web(
                                        "#B33434"
                                )
                        );

                        securityIcon.setText("!");

                        securityIcon.setTextFill(
                                Color.web(
                                        "#B33434"
                                )
                        );
                    }
                };

        // =================================================
        // INITIAL LOAD
        // =================================================

        loadReport.run();

        // =================================================
        // REFRESH
        // =================================================

        refreshButton.setOnAction(e -> {

            loadReport.run();

            showMessage(
                    "Report Refreshed",
                    "Latest guard activity has been loaded."
            );
        });

        // =================================================
        // SEND REPORT
        // =================================================

        sendButton.setOnAction(e -> {

            try {

                int total =
                        Integer.parseInt(
                                totalVisitorsValue
                                        .getText()
                        );

                int inside =
                        Integer.parseInt(
                                insideVisitorsValue
                                        .getText()
                        );

                int checkedOut =
                        Integer.parseInt(
                                checkedOutValue
                                        .getText()
                        );

                int deliveries =
                        Integer.parseInt(
                                deliveriesValue
                                        .getText()
                        );

                int occupied =
                        Integer.parseInt(
                                parkingOccupiedValue
                                        .getText()
                        );

                int available =
                        Integer.parseInt(
                                parkingAvailableValue
                                        .getText()
                        );

                boolean emergencyActive =
                        emergencyStatus
                                .getText()
                                .contains(
                                        "Active emergency"
                                );

                String reportDate =
                        LocalDate.now().format(
                                DateTimeFormatter.ofPattern(
                                        "dd/MM/yyyy"
                                )
                        );

                boolean saved =
                        new DailyReportsController()
                                .sendReportToSecretary(

                                        normalizedEmail,

                                        society,

                                        reportDate,

                                        total,

                                        inside,

                                        checkedOut,

                                        deliveries,

                                        occupied,

                                        available,

                                        emergencyActive,

                                        notesArea.getText(),

                                        shift,

                                        gate
                                );

                if (saved) {

                    showMessage(
                            "Report Sent",
                            "Daily report has been sent successfully to the Secretary."
                    );

                    notesArea.clear();

                } else {

                    showMessage(
                            "Error",
                            "Unable to save the daily report."
                    );
                }

            } catch (Exception ex) {

                ex.printStackTrace();

                showMessage(
                        "Error",
                        "Unable to send the daily report."
                );
            }
        });

        // =================================================
        // MAIN CONTENT
        // =================================================

        content.getChildren().addAll(

                overviewCard,

                activityRow,

                securityCard,

                notesCard,

                buttonRow
        );

        // =================================================
        // PUT CONTENT INTO MAIN AREA
        // NO SCROLL PANE
        // =================================================

        mainArea.setCenter(
                content
        );

        root.setCenter(
                mainArea
        );

        // =================================================
        // SCENE
        // =================================================

        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }

    // =====================================================
    // CARD
    // =====================================================

    private VBox createCard() {

        VBox card =
                new VBox(10);

        card.setPadding(
                new Insets(15)
        );

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#DDD0C9;" +
                "-fx-border-radius:10;"
        );

        return card;
    }

    // =====================================================
    // SECTION TITLE
    // =====================================================

    private Label sectionTitle(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#4E342E;"
        );

        return label;
    }

    // =====================================================
    // INFO BOX
    // =====================================================

    private VBox infoBox(
            String heading,
            String value) {

        VBox box =
                new VBox(4);

        box.setPrefWidth(170);

        Label headingLabel =
                new Label(heading);

        headingLabel.setStyle(
                "-fx-font-size:10px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#91847D;"
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#333333;"
        );

        box.getChildren().addAll(
                headingLabel,
                valueLabel
        );

        return box;
    }

    // =====================================================
    // STAT VALUE
    // =====================================================

    private Label statValue(
            String value) {

        Label label =
                new Label(value);

        label.setStyle(
                "-fx-font-size:21px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#4E342E;"
        );

        return label;
    }

    // =====================================================
    // METRIC
    // =====================================================

    private VBox createMetric(
            String title,
            Label value) {

        VBox box =
                new VBox(4);

        box.setPadding(
                new Insets(10)
        );

        box.setPrefWidth(150);

        box.setMinHeight(62);

        box.setStyle(
                "-fx-background-color:#F8F4F1;" +
                "-fx-background-radius:8;" +
                "-fx-border-color:#E4D8D1;" +
                "-fx-border-radius:8;"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size:10px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#81756F;"
        );

        box.getChildren().addAll(
                titleLabel,
                value
        );

        return box;
    }

    // =====================================================
    // MESSAGE
    // =====================================================

    private void showMessage(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}