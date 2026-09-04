package com.society.view.Resident_portal;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.config.FirebaseConfig;
import com.society.dao.Resident_dao.EmergencyDao;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Emergency {

    // =========================================================
    // UI FIELDS
    // =========================================================

    private ComboBox<String> emergencyType;
    private TextField locationField;
    private TextArea additionalInfo;

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore db;

    // =========================================================
    // DAO
    // =========================================================

    private final EmergencyDao emergencyDao;

    // =========================================================
    // CURRENT ALERT ID
    // =========================================================

    private String currentEmergencyId = null;

    // =========================================================
    // ALERT STATUS LABEL
    // =========================================================

    private Label alertStatus;

    // =========================================================
    // INSTANT ALERT DESCRIPTION
    // =========================================================

    private Label instantAlertDescription;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Emergency() {

        db = FirebaseConfig.getFirestore();

        emergencyDao = new EmergencyDao();
    }

    // =========================================================
    // MAIN SCENE
    // =========================================================

    public Scene getEmergencyScene(Stage stage, String residentEmail) {

        // =========================================================
        // SIDEBAR - SAME
        // =========================================================

        panel panelobj = new panel(stage, residentEmail);

        BorderPane root = new BorderPane();

        root.setLeft(panelobj.getSidebar());

        // =========================================================
        // MAIN AREA
        // =========================================================

        BorderPane mainArea = new BorderPane();

        // =========================================================
        // TOP HEADER
        // =========================================================

        VBox header = new VBox(4);

        header.setPadding(
                new Insets(25, 35, 22, 35)
        );

        header.setStyle(
                "-fx-background-color: #4E342E;"
        );

        Label title = new Label("Emergency SOS");

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        27
                )
        );

        title.setTextFill(Color.WHITE);

        Label subtitle = new Label(
                "Quickly request emergency assistance"
        );

        subtitle.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        subtitle.setTextFill(
                Color.web("#E8DDD5")
        );

        header.getChildren().addAll(
                title,
                subtitle
        );

        mainArea.setTop(header);

        // =========================================================
        // MAIN CONTENT
        // =========================================================

        VBox mainContent = new VBox(22);

        mainContent.setPadding(
                new Insets(28, 40, 35, 40)
        );

        mainContent.setStyle(
                "-fx-background-color: #E8DDD5;"
        );

        // =========================================================
        // EMERGENCY ALERT CARD
        // =========================================================

        VBox emergencyAlertCard = new VBox(18);

        emergencyAlertCard.setPadding(
                new Insets(28)
        );

        emergencyAlertCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 0, 3);"
        );

        // =========================================================
        // ALERT TITLE
        // =========================================================

        Label alertTitle = new Label(
                "Emergency Alert"
        );

        alertTitle.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        22
                )
        );

        alertTitle.setTextFill(
                Color.web("#B23A3A")
        );

        Label alertDescription = new Label(
                "Press SOS to immediately notify society security."
        );

        alertDescription.setFont(
                Font.font("System", 14)
        );

        alertDescription.setTextFill(
                Color.web("#607D8B")
        );

        VBox alertHeading = new VBox(5);

        alertHeading.getChildren().addAll(
                alertTitle,
                alertDescription
        );

        // =========================================================
        // SOS + FEATURE CARDS
        // =========================================================

        HBox alertBody = new HBox(35);

        alertBody.setAlignment(
                Pos.CENTER
        );

        // =========================================================
        // SOS BUTTON AREA
        // =========================================================

        VBox sosArea = new VBox(12);

        sosArea.setAlignment(
                Pos.CENTER
        );

        // =========================================================
        // OUTER CIRCLE
        // =========================================================

        VBox sosOuterCircle = new VBox();

        sosOuterCircle.setAlignment(
                Pos.CENTER
        );

        sosOuterCircle.setPrefSize(
                190,
                190
        );

        sosOuterCircle.setStyle(
                "-fx-background-color: #FFF4F4;" +
                "-fx-background-radius: 100;" +
                "-fx-border-color: #F2CFCF;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 100;"
        );

        // =========================================================
        // MIDDLE CIRCLE
        // =========================================================

        VBox sosMiddleCircle = new VBox();

        sosMiddleCircle.setAlignment(
                Pos.CENTER
        );

        sosMiddleCircle.setPrefSize(
                155,
                155
        );

        sosMiddleCircle.setStyle(
                "-fx-background-color: #F9DADA;" +
                "-fx-background-radius: 100;" +
                "-fx-border-color: #F0BABA;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 100;"
        );

        // =========================================================
        // ACTUAL SOS BUTTON
        // =========================================================

        Button sosButton = new Button("SOS");

        sosButton.setPrefSize(
                125,
                125
        );

        sosButton.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        30
                )
        );

        sosButton.setTextFill(Color.WHITE);

        sosButton.setStyle(
                "-fx-background-color: #C62828;" +
                "-fx-background-radius: 70;" +
                "-fx-cursor: hand;"
        );

        // =========================================================
        // SOS ACTION
        // =========================================================

        sosButton.setOnAction(e -> {

            if (residentEmail == null ||
                    residentEmail.trim().isEmpty()) {

                showError(
                        "Resident Email Missing",
                        "Resident login email could not be found."
                );

                return;
            }

            if (emergencyType == null ||
                    locationField == null ||
                    additionalInfo == null) {

                showError(
                        "Emergency Form Error",
                        "Emergency form is not initialized correctly."
                );

                return;
            }

            if (locationField.getText() == null ||
                    locationField.getText().trim().isEmpty()) {

                showError(
                        "Location Required",
                        "Please enter the emergency location."
                );

                locationField.requestFocus();

                return;
            }

            Alert confirmation =
                    new Alert(
                            Alert.AlertType.CONFIRMATION
                    );

            confirmation.setTitle(
                    "Emergency SOS"
            );

            confirmation.setHeaderText(
                    "Send Emergency SOS?"
            );

            confirmation.setContentText(
                    "This will immediately alert " +
                    "society security that you need assistance."
            );

            confirmation.showAndWait()
                    .ifPresent(response -> {

                        if (response == ButtonType.OK) {

                            try {

                                // =================================================
                                // SAVE FIRESTORE
                                // =================================================

                                String emergencyId =
                                        emergencyDao.sendEmergencyAlert(
                                                residentEmail,
                                                emergencyType.getValue(),
                                                locationField.getText(),
                                                additionalInfo.getText()
                                        );

                                // Save current ID
                                currentEmergencyId =
                                        emergencyId;

                                // =================================================
                                // UPDATE UI
                                // =================================================

                                alertStatus.setText(
                                        "ALERT ACTIVE"
                                );

                                instantAlertDescription.setText(
                                        emergencyType.getValue()
                                        + " • "
                                        + locationField.getText()
                                );

                                Alert sent =
                                        new Alert(
                                                Alert.AlertType.INFORMATION
                                        );

                                sent.setTitle(
                                        "SOS Sent"
                                );

                                sent.setHeaderText(
                                        "Emergency Alert Sent"
                                );

                                sent.setContentText(
                                        "Society security has been notified.\n\n"
                                        + "Emergency ID: "
                                        + emergencyId
                                        + "\n"
                                        + "Please stay safe and wait for assistance."
                                );

                                sent.showAndWait();

                            } catch (Exception ex) {

                                ex.printStackTrace();

                                showError(
                                        "SOS Failed",
                                        "Emergency alert could not be saved.\n\n"
                                        + ex.getMessage()
                                );
                            }
                        }
                    });
        });

        sosMiddleCircle.getChildren().add(
                sosButton
        );

        sosOuterCircle.getChildren().add(
                sosMiddleCircle
        );

        // =========================================================
        // ALERT STATUS
        // =========================================================

        alertStatus = new Label(
                "ALERT ACTIVE"
        );

        alertStatus.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        alertStatus.setTextFill(
                Color.web("#A94442")
        );

        alertStatus.setPadding(
                new Insets(9, 22, 9, 22)
        );

        alertStatus.setStyle(
                "-fx-background-color: #FBE3E3;" +
                "-fx-background-radius: 20;"
        );

        sosArea.getChildren().addAll(
                sosOuterCircle,
                alertStatus
        );

        // =========================================================
        // FEATURE CARDS
        // =========================================================

        HBox featureCards = new HBox(15);

        featureCards.setAlignment(
                Pos.CENTER
        );

        VBox instantCard =
                createFeatureCard(
                        "⚠",
                        "Instant Alert",
                        "Loading saved emergency alert...",
                        "#D32F2F"
                );

        // =========================================================
        // SAVE DESCRIPTION LABEL REFERENCE
        // =========================================================

        instantAlertDescription =
                getDescriptionLabel(
                        instantCard
                );

        VBox secureCard =
                createFeatureCard(
                        "✓",
                        "Secure & Reliable",
                        "Emergency information is stored securely.",
                        "#2E9D62"
                );

        VBox quickCard =
                createFeatureCard(
                        "●",
                        "Quick Response",
                        "Helps residents get assistance faster.",
                        "#7E57C2"
                );

        featureCards.getChildren().addAll(
                instantCard,
                secureCard,
                quickCard
        );

        alertBody.getChildren().addAll(
                sosArea,
                featureCards
        );

        emergencyAlertCard.getChildren().addAll(
                alertHeading,
                alertBody
        );

        // =========================================================
        // EMERGENCY DETAILS CARD
        // =========================================================

        VBox detailsCard = new VBox(18);

        detailsCard.setPadding(
                new Insets(25)
        );

        detailsCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 0, 3);"
        );

        // =========================================================
        // DETAILS HEADER
        // =========================================================

        HBox detailsHeader = new HBox(12);

        detailsHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        Label detailsIcon = new Label("▤");

        detailsIcon.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        20
                )
        );

        detailsIcon.setTextFill(
                Color.web("#4E9B72")
        );

        detailsIcon.setAlignment(
                Pos.CENTER
        );

        detailsIcon.setPrefSize(
                45,
                45
        );

        detailsIcon.setStyle(
                "-fx-background-color: #EAF5EE;" +
                "-fx-background-radius: 25;"
        );

        VBox detailsTitleBox = new VBox(3);

        Label detailsTitle =
                new Label("Emergency Details");

        detailsTitle.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        20
                )
        );

        detailsTitle.setTextFill(
                Color.web("#263238")
        );

        Label detailsSubtitle =
                new Label(
                        "These details will be included in the emergency alert."
                );

        detailsSubtitle.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        detailsSubtitle.setTextFill(
                Color.web("#607D8B")
        );

        detailsTitleBox.getChildren().addAll(
                detailsTitle,
                detailsSubtitle
        );

        detailsHeader.getChildren().addAll(
                detailsIcon,
                detailsTitleBox
        );

        // =========================================================
        // EMERGENCY TYPE
        // =========================================================

        Label typeLabel =
                new Label("Emergency Type *");

        typeLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        emergencyType = new ComboBox<>();

        emergencyType.getItems().addAll(
                "Accident",
                "Medical Emergency",
                "Fire",
                "Security Issue",
                "Theft",
                "Other"
        );

        emergencyType.setValue(
                "Accident"
        );

        emergencyType.setMaxWidth(
                Double.MAX_VALUE
        );

        emergencyType.setPrefHeight(
                40
        );

        emergencyType.setStyle(
                "-fx-background-color: #FAFAFA;" +
                "-fx-border-color: #D6D6D6;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;"
        );

        // =========================================================
        // LOCATION
        // =========================================================

        Label locationLabel =
                new Label("Location *");

        locationLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        locationField = new TextField();

        locationField.setPromptText(
                "Enter emergency location"
        );

        locationField.setPrefHeight(
                40
        );

        locationField.setStyle(
                "-fx-background-color: #FAFAFA;" +
                "-fx-border-color: #D6D6D6;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;"
        );

        // =========================================================
        // GRID
        // =========================================================

        GridPane detailsGrid =
                new GridPane();

        detailsGrid.setHgap(25);

        detailsGrid.setVgap(8);

        ColumnConstraintsHelper.addColumns(
                detailsGrid
        );

        detailsGrid.add(
                typeLabel,
                0,
                0
        );

        detailsGrid.add(
                locationLabel,
                1,
                0
        );

        detailsGrid.add(
                emergencyType,
                0,
                1
        );

        detailsGrid.add(
                locationField,
                1,
                1
        );

        GridPane.setHgrow(
                emergencyType,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                locationField,
                Priority.ALWAYS
        );

        // =========================================================
        // ADDITIONAL INFORMATION
        // =========================================================

        Label additionalLabel =
                new Label("Additional Information");

        additionalLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        additionalInfo = new TextArea();

        additionalInfo.setPromptText(
                "Example: Smoke detected near the second floor..."
        );

        additionalInfo.setWrapText(true);

        additionalInfo.setPrefRowCount(
                3
        );

        additionalInfo.setStyle(
                "-fx-background-color: #FAFAFA;" +
                "-fx-border-color: #D6D6D6;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;"
        );

        // =========================================================
        // BUTTONS
        // =========================================================

        HBox detailsButtons =
                new HBox(12);

        detailsButtons.setAlignment(
                Pos.CENTER_RIGHT
        );

        // =========================================================
        // CLEAR BUTTON
        // =========================================================

        Button clearButton =
                new Button("Clear");

        clearButton.setPrefSize(
                100,
                38
        );

        clearButton.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #D0D0D0;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );

        clearButton.setOnAction(e -> {

            emergencyType.setValue(
                    "Accident"
            );

            locationField.clear();

            additionalInfo.clear();

            currentEmergencyId = null;

            alertStatus.setText(
                    "ALERT ACTIVE"
            );

            instantAlertDescription.setText(
                    "Loading saved emergency alert..."
            );
        });

        // =========================================================
        // RESOLVE ALERT BUTTON
        // =========================================================

        Button resolveAlertButton =
                new Button("✓  Resolve Alert");

        resolveAlertButton.setPrefSize(
                170,
                38
        );

        resolveAlertButton.setTextFill(
                Color.WHITE
        );

        resolveAlertButton.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        resolveAlertButton.setStyle(
                "-fx-background-color: #4E342E;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
        );

        // =========================================================
        // RESOLVE ACTION
        // =========================================================

        resolveAlertButton.setOnAction(e -> {

            if (currentEmergencyId == null ||
                    currentEmergencyId.trim().isEmpty()) {

                showError(
                        "No Active Alert",
                        "There is no active emergency alert to resolve."
                );

                return;
            }

            Alert confirmation =
                    new Alert(
                            Alert.AlertType.CONFIRMATION
                    );

            confirmation.setTitle(
                    "Resolve Emergency Alert"
            );

            confirmation.setHeaderText(
                    "Resolve Emergency Alert?"
            );

            confirmation.setContentText(
                    "Are you sure you want to resolve " +
                    "this emergency alert?"
            );

            confirmation.showAndWait()
                    .ifPresent(response -> {

                        if (response == ButtonType.OK) {

                            boolean resolved =
                                    emergencyDao.resolveEmergencyAlert(
                                            residentEmail,
                                            currentEmergencyId
                                    );

                            if (resolved) {

                                Alert resolvedAlert =
                                        new Alert(
                                                Alert.AlertType.INFORMATION
                                        );

                                resolvedAlert.setTitle(
                                        "Alert Resolved"
                                );

                                resolvedAlert.setHeaderText(
                                        "Emergency Alert Resolved"
                                );

                                resolvedAlert.setContentText(
                                        "The emergency alert has been resolved successfully."
                                );

                                resolvedAlert.showAndWait();

                                // =============================================
                                // CLEAR UI
                                // =============================================

                                emergencyType.setValue(
                                        "Accident"
                                );

                                locationField.clear();

                                additionalInfo.clear();

                                currentEmergencyId = null;

                                alertStatus.setText(
                                        "ALERT RESOLVED"
                                );

                                instantAlertDescription.setText(
                                        "No active emergency alert."
                                );

                            } else {

                                showError(
                                        "Resolve Failed",
                                        "Emergency alert could not be resolved."
                                );
                            }
                        }
                    });
        });

        detailsButtons.getChildren().addAll(
                clearButton,
                resolveAlertButton
        );

        detailsCard.getChildren().addAll(
                detailsHeader,
                detailsGrid,
                additionalLabel,
                additionalInfo,
                detailsButtons
        );

        // =========================================================
        // EMERGENCY SERVICES
        // =========================================================

        VBox servicesSection =
                new VBox(15);

        Label servicesTitle =
                new Label("Emergency Services");

        servicesTitle.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        20
                )
        );

        servicesTitle.setTextFill(
                Color.WHITE
        );

        Label servicesSubtitle =
                new Label(
                        "Contact emergency services directly"
                );

        servicesSubtitle.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        servicesSubtitle.setTextFill(
                Color.WHITE
        );

        VBox servicesHeading =
                new VBox(3);

        servicesHeading.getChildren().addAll(
                servicesTitle,
                servicesSubtitle
        );

        HBox serviceCards =
                new HBox(15);

        serviceCards.setAlignment(
                Pos.CENTER
        );

        // =========================================================
        // SOCIETY SECURITY
        // =========================================================

        VBox securityCard =
                createServiceCard(
                        "🛡",
                        "Society Security",
                        "Immediate assistance",
                        "9876543210"
                );

        // =========================================================
        // POLICE
        // =========================================================

        VBox policeCard =
                createServiceCard(
                        "☎",
                        "Police",
                        "Emergency assistance",
                        "112"
                );

        // =========================================================
        // AMBULANCE
        // =========================================================

        VBox ambulanceCard =
                createServiceCard(
                        "✚",
                        "Ambulance",
                        "Medical emergency",
                        "108"
                );

        // =========================================================
        // FIRE DEPARTMENT
        // =========================================================

        VBox fireCard =
                createServiceCard(
                        "♨",
                        "Fire Department",
                        "Fire emergency",
                        "101"
                );

        serviceCards.getChildren().addAll(
                securityCard,
                policeCard,
                ambulanceCard,
                fireCard
        );

        servicesSection.getChildren().addAll(
                servicesHeading,
                serviceCards
        );

        // =========================================================
        // ADD ALL CONTENT
        // =========================================================

        mainContent.getChildren().addAll(
                emergencyAlertCard,
                detailsCard,
                servicesSection
        );

        mainArea.setCenter(
                mainContent
        );

        root.setCenter(
                mainArea
        );

        // =========================================================
        // FETCH SAVED ALERT
        // IMPORTANT:
        // scene तयार झाल्यानंतर Firestore मधून fetch
        // =========================================================

        loadLatestEmergencyAlert(
                residentEmail
        );

        // =========================================================
        // SCREEN SIZE - SAME
        // =========================================================

        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }

    // =============================================================
    // FETCH LATEST ACTIVE EMERGENCY ALERT
    // =============================================================

    private void loadLatestEmergencyAlert(
            String residentEmail) {

        if (residentEmail == null ||
                residentEmail.trim().isEmpty()) {

            return;
        }

        Thread fetchThread = new Thread(() -> {

            try {

                String email =
                        residentEmail.trim();

                // =================================================
                // PATH
                //
                // emergency_alerts
                //      └── residentEmail
                //              └── alerts
                //                    └── emergencyId
                // =================================================

                QuerySnapshot snapshot =
                        db.collection("emergency_alerts")
                                .document(email)
                                .collection("alerts")
                                .whereEqualTo(
                                        "status",
                                        "ACTIVE"
                                )
                                .orderBy(
                                        "createdAt",
                                        Query.Direction.DESCENDING
                                )
                                .limit(1)
                                .get()
                                .get();

                if (snapshot.isEmpty()) {

                    javafx.application.Platform.runLater(() -> {

                        alertStatus.setText(
                                "NO ACTIVE ALERT"
                        );

                        instantAlertDescription.setText(
                                "No active emergency alert."
                        );

                        currentEmergencyId = null;
                    });

                    return;
                }

                QueryDocumentSnapshot alert =
                        snapshot.getDocuments().get(0);

                // =================================================
                // FETCH ALL FIELDS
                // =================================================

                String emergencyId =
                        alert.getString("emergencyId");

                String type =
                        alert.getString("type");

                String location =
                        alert.getString("location");

                String details =
                        alert.getString("details");

                String emailFromFirestore =
                        alert.getString("email");

                String sender1 =
                        alert.getString("sender1");

                String sender2 =
                        alert.getString("sender2");

                String society =
                        alert.getString("society");

                String societyName =
                        alert.getString("societyName");

                String status =
                        alert.getString("status");

                String time =
                        alert.getString("time");

                // =================================================
                // DEBUG CONSOLE
                // =================================================

                System.out.println(
                        "=========================================="
                );

                System.out.println(
                        "EMERGENCY ALERT FETCHED"
                );

                System.out.println(
                        "Emergency ID : " + emergencyId
                );

                System.out.println(
                        "Email        : " + emailFromFirestore
                );

                System.out.println(
                        "Type         : " + type
                );

                System.out.println(
                        "Location     : " + location
                );

                System.out.println(
                        "Details      : " + details
                );

                System.out.println(
                        "Sender1      : " + sender1
                );

                System.out.println(
                        "Sender2      : " + sender2
                );

                System.out.println(
                        "Society      : " + society
                );

                System.out.println(
                        "Society Name : " + societyName
                );

                System.out.println(
                        "Status       : " + status
                );

                System.out.println(
                        "Time         : " + time
                );

                System.out.println(
                        "=========================================="
                );

                // =================================================
                // UPDATE JAVAFX UI
                // =================================================

                javafx.application.Platform.runLater(() -> {

                    // =============================================
                    // STORE CURRENT ID
                    // =============================================

                    currentEmergencyId =
                            emergencyId;

                    // =============================================
                    // TYPE
                    // =============================================

                    if (type != null &&
                            !type.trim().isEmpty()) {

                        if (emergencyType
                                .getItems()
                                .contains(type)) {

                            emergencyType.setValue(
                                    type
                            );

                        } else {

                            emergencyType.setValue(
                                    type
                            );
                        }
                    }

                    // =============================================
                    // LOCATION
                    // =============================================

                    if (location != null) {

                        locationField.setText(
                                location
                        );
                    }

                    // =============================================
                    // DETAILS
                    // =============================================

                    if (details != null) {

                        additionalInfo.setText(
                                details
                        );
                    } else {

                        additionalInfo.clear();
                    }

                    // =============================================
                    // ALERT STATUS
                    // =============================================

                    if (status != null &&
                            !status.trim().isEmpty()) {

                        if ("ACTIVE".equalsIgnoreCase(status)) {

                            alertStatus.setText(
                                    "ALERT ACTIVE"
                            );

                        } else {

                            alertStatus.setText(
                                    status
                            );
                        }
                    }

                    // =============================================
                    // INSTANT ALERT CARD
                    // =============================================

                    String displayText = "";

                    if (type != null &&
                            !type.trim().isEmpty()) {

                        displayText =
                                type.trim();
                    }

                    if (location != null &&
                            !location.trim().isEmpty()) {

                        if (!displayText.isEmpty()) {

                            displayText +=
                                    " • ";
                        }

                        displayText +=
                                location.trim();
                    }

                    if (displayText.isEmpty()) {

                        displayText =
                                "Emergency alert is active.";

                    }

                    instantAlertDescription.setText(
                            displayText
                    );
                });

            } catch (Exception ex) {

                ex.printStackTrace();

                javafx.application.Platform.runLater(() -> {

                    alertStatus.setText(
                            "FETCH FAILED"
                    );

                    instantAlertDescription.setText(
                            "Unable to fetch saved emergency alert."
                    );
                });
            }

        });

        fetchThread.setDaemon(true);

        fetchThread.start();
    }

    // =============================================================
    // GET DESCRIPTION LABEL FROM FEATURE CARD
    // =============================================================

    private Label getDescriptionLabel(
            VBox card) {

        for (javafx.scene.Node node :
                card.getChildren()) {

            if (node instanceof Label) {

                Label label =
                        (Label) node;

                String text =
                        label.getText();

                if (text != null &&
                        !text.equals("Instant Alert")) {

                    return label;
                }
            }
        }

        return new Label();
    }

    // =============================================================
    // ERROR ALERT
    // =============================================================

    private void showError(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    // =============================================================
    // FEATURE CARD
    // =============================================================

    private VBox createFeatureCard(
            String icon,
            String title,
            String description,
            String iconColor) {

        VBox card =
                new VBox(10);

        card.setPrefWidth(
                185
        );

        card.setPrefHeight(
                155
        );

        card.setPadding(
                new Insets(18)
        );

        card.setStyle(
                "-fx-background-color: #FAFAFA;" +
                "-fx-border-color: #E0E0E0;" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;"
        );

        Label iconLabel =
                new Label(icon);

        iconLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        21
                )
        );

        iconLabel.setTextFill(
                Color.web(iconColor)
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        14
                )
        );

        titleLabel.setTextFill(
                Color.web("#263238")
        );

        Label descriptionLabel =
                new Label(description);

        descriptionLabel.setFont(
                Font.font(
                        "System",
                        12
                )
        );

        descriptionLabel.setTextFill(
                Color.web("#607D8B")
        );

        descriptionLabel.setWrapText(
                true
        );

        card.getChildren().addAll(
                iconLabel,
                titleLabel,
                descriptionLabel
        );

        return card;
    }

    // =============================================================
    // EMERGENCY SERVICE CARD
    // =============================================================

    private VBox createServiceCard(
            String icon,
            String title,
            String description,
            String phoneNumber) {

        VBox card =
                new VBox(9);

        card.setAlignment(
                Pos.CENTER
        );

        card.setPrefWidth(
                225
        );

        card.setPrefHeight(
                180
        );

        card.setPadding(
                new Insets(15)
        );

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #E0E0E0;" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2);"
        );

        Label iconLabel =
                new Label(icon);

        iconLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        28
                )
        );

        iconLabel.setTextFill(
                Color.web("#2878D4")
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        15
                )
        );

        titleLabel.setTextFill(
                Color.web("#263238")
        );

        Label descriptionLabel =
                new Label(description);

        descriptionLabel.setFont(
                Font.font(
                        "System",
                        12
                )
        );

        descriptionLabel.setTextFill(
                Color.web("#607D8B")
        );

        // =========================================================
        // PHONE BUTTON
        // =========================================================

        Button phoneButton =
                new Button("☎  " + phoneNumber);

        phoneButton.setPrefWidth(
                190
        );

        phoneButton.setPrefHeight(
                40
        );

        phoneButton.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        14
                )
        );

        phoneButton.setTextFill(
                Color.web("#2878D4")
        );

        phoneButton.setStyle(
                "-fx-background-color: #E8F1FC;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        phoneButton.setOnAction(e -> {

            Alert contactAlert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            contactAlert.setTitle(
                    "Emergency Contact"
            );

            contactAlert.setHeaderText(
                    title
            );

            contactAlert.setContentText(
                    "Contact Number: " +
                    phoneNumber
            );

            contactAlert.showAndWait();
        });

        card.getChildren().addAll(
                iconLabel,
                titleLabel,
                descriptionLabel,
                phoneButton
        );

        return card;
    }

    // =============================================================
    // COLUMN HELPER
    // =============================================================

    private static class ColumnConstraintsHelper {

        public static void addColumns(
                GridPane grid) {

            javafx.scene.layout.ColumnConstraints column1 =
                    new javafx.scene.layout.ColumnConstraints();

            javafx.scene.layout.ColumnConstraints column2 =
                    new javafx.scene.layout.ColumnConstraints();

            column1.setPercentWidth(50);

            column2.setPercentWidth(50);

            grid.getColumnConstraints().addAll(
                    column1,
                    column2
            );
        }
    }
}