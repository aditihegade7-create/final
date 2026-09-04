package com.society.view.Guard_portal;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.PauseTransition;

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

import javafx.scene.effect.DropShadow;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.view.ScreenSize;

import com.society.controller.Guard_Controller.EmergencyController;

import com.society.model.Guard_model.EmergencyModel;
import com.society.model.Welcome.User;
import com.society.dao.Welcome.UserDao;


public class EmergencySOS {

    private static boolean sosActive = false;


    // =====================================================
    // FIRESTORE / LOGGED-IN USER DATA
    // =====================================================

    private static final EmergencyController emergencyController =
            new EmergencyController();

    private static String loggedInEmail = "";
    private static String society = "";
    private static String activeEmergencyId = "";

    private static String emergencyType = "";
    private static String emergencyLocation = "";
    private static String emergencyDetails = "";
    private static String emergencyTime = "";

    // =====================================================
    // CREATE SCENE
    // =====================================================

    public static Scene createScene(
            Stage stage,
            String guardEmail) {

        loggedInEmail =
        guardEmail == null
                ? ""
                : guardEmail.trim().toLowerCase();

setLoggedInEmail(loggedInEmail);

// =====================================================
// RESTORE ACTIVE EMERGENCY FOR LOGGED-IN GUARD
// =====================================================

sosActive = false;
activeEmergencyId = "";

if (!loggedInEmail.isEmpty()) {

    List<EmergencyModel> activeEmergencies =
            emergencyController.getActiveEmergenciesByEmail(
                    loggedInEmail
            );

    if (activeEmergencies != null &&
            !activeEmergencies.isEmpty()) {

        EmergencyModel latestEmergency =
                activeEmergencies.get(
                        activeEmergencies.size() - 1
                );

        if (latestEmergency != null &&
                latestEmergency.getEmergencyId() != null &&
                !latestEmergency.getEmergencyId()
                        .trim()
                        .isEmpty()) {

            activeEmergencyId =
                    latestEmergency.getEmergencyId();

            sosActive = true;
        }
    }
}


BorderPane root =
        new BorderPane();


        // =====================================================
        // SIDEBAR
        // =====================================================

        GuardSidebar sidebar =
                new GuardSidebar(
                        stage,
                        "Emergency SOS",
                        loggedInEmail,
                        society
                );

        root.setLeft(
                sidebar.getSidebar()
        );


        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(
                        25,
                        40,
                        30,
                        40
                )
        );

        content.setFillWidth(
                true
        );

        content.setStyle(
                "-fx-background-color:#E8DDD5;"
        );


        // =====================================================
        // HEADER
        // =====================================================

        HBox header =
                new HBox();

        header.setPadding(
                new Insets(
                        25,
                        35,
                        25,
                        35
                )
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setStyle(
                "-fx-background-color:#4E342E;"
        );


        // =====================================================
        // HEADER TITLE
        // =====================================================

        VBox titleBox =
                new VBox(3);


        Label title =
                new Label(
                        "Emergency SOS"
                );

        title.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );


        Label description =
                new Label(
                        "Send an emergency alert to residents and the secretary."
                );

        description.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:white;"
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


        day.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );


        date.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#E6D8D3;"
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
        // EMERGENCY ALERT CARD
        // =====================================================

        VBox alertCard =
                new VBox();


        alertCard.setPadding(
                new Insets(
                        24,
                        28,
                        18,
                        28
                )
        );


        alertCard.setSpacing(
                13
        );


        alertCard.setMaxWidth(
                Double.MAX_VALUE
        );


        alertCard.setStyle(
                "-fx-background-color:#FFFDFC;" +
                "-fx-background-radius:16;" +
                "-fx-border-color:#D9CEC7;" +
                "-fx-border-radius:16;" +
                "-fx-border-width:1;"
        );


        alertCard.setEffect(
                new DropShadow(
                        10,
                        Color.rgb(
                                0,
                                0,
                                0,
                                0.07
                        )
                )
        );


        // =====================================================
        // ALERT HEADER
        // =====================================================

        VBox alertHeading =
                new VBox(4);


        Label alertTitle =
                new Label(
                        "Emergency Alert"
                );


        alertTitle.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#A83A3A;"
        );


        Label alertMessage =
                new Label(
                        "Press SOS to immediately notify residents and the secretary."
                );


        alertMessage.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#52606D;"
        );


        alertMessage.setWrapText(
                true
        );


        alertHeading.getChildren().addAll(
                alertTitle,
                alertMessage
        );


        // =====================================================
        // ALERT BODY
        // =====================================================

        HBox alertBody =
                new HBox(28);


        alertBody.setAlignment(
                Pos.CENTER
        );


        alertBody.setPadding(
                new Insets(
                        3,
                        20,
                        3,
                        20
                )
        );


        // =====================================================
        // SOS BUTTON AREA
        // =====================================================

        StackPane sosArea =
                new StackPane();


        sosArea.setPrefSize(
                230,
                175
        );


        // =====================================================
        // OUTER SOS RING
        // =====================================================

        Circle outerCircle =
                new Circle(
                        82
                );


        outerCircle.setFill(
                Color.web(
                        "#FFF8F8"
                )
        );


        outerCircle.setStroke(
                Color.web(
                        "#EFD6D6"
                )
        );


        outerCircle.setStrokeWidth(
                2
        );


        // =====================================================
        // SECOND SOS RING
        // =====================================================

        Circle middleCircle =
                new Circle(
                        70
                );


        middleCircle.setFill(
                Color.web(
                        "#FDEFEF"
                )
        );


        middleCircle.setStroke(
                Color.web(
                        "#F0D1D1"
                )
        );


        middleCircle.setStrokeWidth(
                2
        );


        // =====================================================
        // INNER SOS RING
        // =====================================================

        Circle innerCircle =
                new Circle(
                        61
                );


        innerCircle.setFill(
                Color.web(
                        "#FBE4E4"
                )
        );


        innerCircle.setStroke(
                Color.web(
                        "#F3D4D4"
                )
        );


        innerCircle.setStrokeWidth(
                1
        );


        // =====================================================
        // SOS BUTTON
        // =====================================================

        Button sendAlertButton =
                new Button(
                        "SOS"
                );


        sendAlertButton.setPrefWidth(
                120
        );


        sendAlertButton.setPrefHeight(
                120
        );


        sendAlertButton.setMinWidth(
                120
        );


        sendAlertButton.setMinHeight(
                120
        );


        sendAlertButton.setMaxWidth(
                120
        );


        sendAlertButton.setMaxHeight(
                120
        );


        sendAlertButton.setStyle(
                "-fx-background-color:#B83A3A;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:28px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:100px;" +
                "-fx-border-color:#EBAAAA;" +
                "-fx-border-width:4px;" +
                "-fx-border-radius:100px;" +
                "-fx-cursor:hand;"
        );


        sendAlertButton.setEffect(
                new DropShadow(
                        12,
                        Color.rgb(
                                184,
                                58,
                                58,
                                0.20
                        )
                )
        );


        // =====================================================
        // SOS HOVER + ANIMATION
        // =====================================================

        sendAlertButton.setOnMouseEntered(
                e -> {

                    sendAlertButton.setStyle(
                            "-fx-background-color:#9E3030;" +
                            "-fx-text-fill:white;" +
                            "-fx-font-size:28px;" +
                            "-fx-font-weight:bold;" +
                            "-fx-background-radius:100px;" +
                            "-fx-border-color:#E7A0A0;" +
                            "-fx-border-width:4px;" +
                            "-fx-border-radius:100px;" +
                            "-fx-cursor:hand;"
                    );

                    playHoverScale(
                            sendAlertButton,
                            1.06
                    );
                }
        );


        sendAlertButton.setOnMouseExited(
                e -> {

                    sendAlertButton.setStyle(
                            "-fx-background-color:#B83A3A;" +
                            "-fx-text-fill:white;" +
                            "-fx-font-size:28px;" +
                            "-fx-font-weight:bold;" +
                            "-fx-background-radius:100px;" +
                            "-fx-border-color:#EBAAAA;" +
                            "-fx-border-width:4px;" +
                            "-fx-border-radius:100px;" +
                            "-fx-cursor:hand;"
                    );

                    playHoverScale(
                            sendAlertButton,
                            1.0
                    );
                }
        );


        sosArea.getChildren().addAll(
                outerCircle,
                middleCircle,
                innerCircle,
                sendAlertButton
        );


        // =====================================================
        // SOS RING ANIMATION
        // =====================================================

        addSOSRingAnimation(
                outerCircle,
                middleCircle
        );


        // =====================================================
        // INFORMATION CARDS
        // =====================================================

        HBox infoCards =
                new HBox(11);


        infoCards.setAlignment(
                Pos.CENTER
        );


        VBox instantCard =
                createInfoCard(
                        "⚠",
                        "Instant Alert",
                        "Notifies all residents immediately.",
                        "#B83A3A"
                );


        VBox secureCard =
                createInfoCard(
                        "✓",
                        "Secure & Reliable",
                        "Emergency information is stored securely.",
                        "#315B45"
                );


        VBox responseCard =
                createInfoCard(
                        "●",
                        "Quick Response",
                        "Helps residents get assistance faster.",
                        "#7656A8"
                );


        infoCards.getChildren().addAll(
                instantCard,
                secureCard,
                responseCard
        );


        alertBody.getChildren().addAll(
                sosArea,
                infoCards
        );


        // =====================================================
        // STATUS
        // =====================================================

        HBox statusContainer =
                new HBox();


        statusContainer.setAlignment(
                Pos.CENTER
        );


        Label statusLabel =
                new Label();


        updateStatus(
                statusLabel
        );


        statusContainer.getChildren().add(
                statusLabel
        );


        alertCard.getChildren().addAll(
                alertHeading,
                alertBody,
                statusContainer
        );


        // =====================================================
        // EMERGENCY DETAILS CARD
        // =====================================================

        VBox detailsCard =
                new VBox();


        detailsCard.setPadding(
                new Insets(
                        21,
                        26,
                        18,
                        26
                )
        );


        detailsCard.setSpacing(
                14
        );


        detailsCard.setMaxWidth(
                Double.MAX_VALUE
        );


        detailsCard.setStyle(
                "-fx-background-color:#FFFDFC;" +
                "-fx-background-radius:16;" +
                "-fx-border-color:#D9CEC7;" +
                "-fx-border-radius:16;" +
                "-fx-border-width:1;"
        );


        detailsCard.setEffect(
                new DropShadow(
                        9,
                        Color.rgb(
                                0,
                                0,
                                0,
                                0.06
                        )
         ) );


        // =====================================================
        // DETAILS HEADER
        // =====================================================

        HBox detailsHeader =
                new HBox(12);


        detailsHeader.setAlignment(
                Pos.CENTER_LEFT
        );


        // =====================================================
        // CLIPBOARD ICON
        // =====================================================

        StackPane detailsIcon =
                new StackPane();


        Circle detailsCircle =
                new Circle(
                        21
                );


        detailsCircle.setFill(
                Color.web(
                        "#EAF2EC"
                )
        );


        Label clipboardIcon =
                new Label(
                        "▤"
                );


        clipboardIcon.setStyle(
                "-fx-font-size:17px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#315B45;"
        );


        detailsIcon.getChildren().addAll(
                detailsCircle,
                clipboardIcon
        );


        // =====================================================
        // DETAILS TITLE
        // =====================================================

        VBox detailsHeading =
                new VBox(3);


        Label detailsTitle =
                new Label(
                        "Emergency Details"
                );


        detailsTitle.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#171717;"
        );


        Label detailsSubtitle =
                new Label(
                        "These details will be included in the emergency alert."
                );


        detailsSubtitle.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#52606D;"
        );


        detailsHeading.getChildren().addAll(
                detailsTitle,
                detailsSubtitle
        );


        detailsHeader.getChildren().addAll(
                detailsIcon,
                detailsHeading
        );


        // =====================================================
        // FORM
        // =====================================================

        GridPane form =
                new GridPane();


        form.setHgap(
                22
        );


        form.setVgap(
                8
        );


        form.setMaxWidth(
                Double.MAX_VALUE
        );


        ColumnConstraints column1 =
                new ColumnConstraints();


        ColumnConstraints column2 =
                new ColumnConstraints();


        column1.setPercentWidth(
                50
        );


        column2.setPercentWidth(
                50
        );


        column1.setHgrow(
                Priority.ALWAYS
        );


        column2.setHgrow(
                Priority.ALWAYS
        );


        form.getColumnConstraints().addAll(
                column1,
                column2
        );


        // =====================================================
        // EMERGENCY TYPE
        // =====================================================

        Label typeLabel =
                createLabel(
                        "Emergency Type *"
                );


        ComboBox<String> typeBox =
                new ComboBox<>();


        typeBox.getItems().addAll(
                "Fire",
                "Medical Emergency",
                "Security Threat",
                "Accident",
                "Suspicious Activity",
                "Other"
        );


        typeBox.setPromptText(
                "Select emergency type"
        );


        styleComboBox(
                typeBox
        );


        // =====================================================
        // LOCATION
        // =====================================================

        Label locationLabel =
                createLabel(
                        "Location *"
                );


        TextField locationField =
                new TextField();


        locationField.setPromptText(
                "Example: Building A"
        );


        styleField(
                locationField
        );


        form.add(
                typeLabel,
                0,
                0
        );


        form.add(
                locationLabel,
                1,
                0
        );


        form.add(
                typeBox,
                0,
                1
        );


        form.add(
                locationField,
                1,
                1
        );


        // =====================================================
        // ADDITIONAL INFORMATION
        // =====================================================

        Label informationLabel =
                createLabel(
                        "Additional Information"
                );


        TextArea informationField =
                new TextArea();


        informationField.setPromptText(
                "Example: Smoke detected near the second floor..."
        );


        informationField.setPrefHeight(
                70
        );


        informationField.setMaxWidth(
                Double.MAX_VALUE
        );


        informationField.setWrapText(
                true
        );


        informationField.setStyle(
                "-fx-background-color:#FAF8F6;" +
                "-fx-background-radius:8;" +
                "-fx-border-color:#D8D0CB;" +
                "-fx-border-radius:8;" +
                "-fx-text-fill:#171717;" +
                "-fx-prompt-text-fill:#7C8984;" +
                "-fx-padding:10;"
        );


        form.add(
                informationLabel,
                0,
                2
        );


        form.add(
                informationField,
                0,
                3,
                2,
                1
        );


        // =====================================================
        // CLEAR BUTTON
        // =====================================================

        Button clearButton =
                new Button(
                        "Clear"
                );


        clearButton.setPrefWidth(
                100
        );


        clearButton.setPrefHeight(
                40
        );


        clearButton.setStyle(
                "-fx-background-color:white;" +
                "-fx-text-fill:#171717;" +
                "-fx-font-weight:bold;" +
                "-fx-border-color:#CFC7C2;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );


        // =====================================================
        // RESOLVE BUTTON
        // =====================================================

        Button resolveButton =
                new Button(
                        "✓  Resolve Alert"
                );


        resolveButton.setPrefWidth(
                150
        );


        resolveButton.setPrefHeight(
                40
        );


        resolveButton.setStyle(
                "-fx-background-color:#4E342E;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );


        // =====================================================
        // BUTTON HOVER + SCALE ANIMATION
        // =====================================================

        clearButton.setOnMouseEntered(
                e -> {

                    clearButton.setStyle(
                            "-fx-background-color:#F5F1EE;" +
                            "-fx-text-fill:#171717;" +
                            "-fx-font-weight:bold;" +
                            "-fx-border-color:#BEB4AE;" +
                            "-fx-border-radius:7;" +
                            "-fx-background-radius:7;" +
                            "-fx-cursor:hand;"
                    );

                    playHoverScale(
                            clearButton,
                            1.04
                    );
                }
        );


        clearButton.setOnMouseExited(
                e -> {

                    clearButton.setStyle(
                            "-fx-background-color:white;" +
                            "-fx-text-fill:#171717;" +
                            "-fx-font-weight:bold;" +
                            "-fx-border-color:#CFC7C2;" +
                            "-fx-border-radius:7;" +
                            "-fx-background-radius:7;" +
                            "-fx-cursor:hand;"
                    );

                    playHoverScale(
                            clearButton,
                            1.0
                    );
                }
        );


        resolveButton.setOnMouseEntered(
                e -> {

                    resolveButton.setStyle(
                            "-fx-background-color:#3E2723;" +
                            "-fx-text-fill:white;" +
                            "-fx-font-weight:bold;" +
                            "-fx-background-radius:7;" +
                            "-fx-cursor:hand;"
                    );

                    playHoverScale(
                            resolveButton,
                            1.04
                    );
                }
        );


        resolveButton.setOnMouseExited(
                e -> {

                    resolveButton.setStyle(
                            "-fx-background-color:#4E342E;" +
                            "-fx-text-fill:white;" +
                            "-fx-font-weight:bold;" +
                            "-fx-background-radius:7;" +
                            "-fx-cursor:hand;"
                    );

                    playHoverScale(
                            resolveButton,
                            1.0
                    );
                }
        );


        // =====================================================
        // FIELD ANIMATIONS
        // =====================================================

        addFieldAnimation(
                typeBox
        );


        addFieldAnimation(
                locationField
        );


        addFieldAnimation(
                informationField
        );


        // =====================================================
        // BOTTOM BUTTONS
        // =====================================================

        HBox bottomButtons =
                new HBox(
                        12,
                        clearButton,
                        resolveButton
                );


        bottomButtons.setAlignment(
                Pos.CENTER_RIGHT
        );


        detailsCard.getChildren().addAll(
                detailsHeader,
                form,
                bottomButtons
        );


        // =====================================================
        // SEND EMERGENCY ALERT
        // FUNCTIONALITY UNCHANGED
        // =====================================================

        sendAlertButton.setOnAction(e -> {

            if (sosActive) {

                playCardAnimation(
                        alertCard
                );

                showMessage(
                        "Alert Already Active",
                        "An emergency alert is already active."
                );

                return;
            }


            if (
                    typeBox.getValue() == null
                    ||
                    locationField.getText()
                            .trim()
                            .isEmpty()
            ) {

                playCardAnimation(
                        alertCard
                );

                showMessage(
                        "Missing Information",
                        "Please select the emergency type and enter the location."
                );

                return;
            }


            emergencyType =
                    typeBox.getValue();


            emergencyLocation =
                    locationField.getText()
                            .trim();


            emergencyDetails =
                    informationField.getText()
                            .trim();


            emergencyTime =
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "dd MMM yyyy, hh:mm a"
                                    )
                            );


            Alert confirmation =
                    new Alert(
                            Alert.AlertType.CONFIRMATION
                    );


            confirmation.setTitle(
                    "Confirm Emergency Alert"
            );


            confirmation.setHeaderText(
                    "Send Emergency Alert?"
            );


            confirmation.setContentText(
                    "Emergency: "
                            + emergencyType
                            + "\nLocation: "
                            + emergencyLocation
                            + "\n\nThis alert will be sent to residents and the secretary."
            );


            confirmation.showAndWait()
                    .ifPresent(response -> {

                        if (
                                response ==
                                        ButtonType.OK
                        ) {

                            if (loggedInEmail == null ||
                                    loggedInEmail.trim().isEmpty()) {

                                showMessage(
                                        "User Not Found",
                                        "Logged-in guard email is not available."
                                );

                                return;
                            }


                            if (society == null ||
                                    society.trim().isEmpty()) {

                                showMessage(
                                        "Society Not Found",
                                        "Society information could not be found for the logged-in guard."
                                );

                                return;
                            }


                            EmergencyModel savedEmergency =
                                    emergencyController.createEmergency(
                                            loggedInEmail,
                                            society,
                                            emergencyType,
                                            emergencyLocation,
                                            emergencyDetails,
                                            emergencyTime
                                    );


                            if (savedEmergency == null) {

                                sosActive = false;

                                updateStatus(
                                        statusLabel
                                );

                                playCardAnimation(
                                        alertCard
                                );

                                showMessage(
                                        "Emergency Alert Failed",
                                        "The emergency alert could not be saved to Firestore."
                                );

                                return;
                            }


                            activeEmergencyId =
                                    savedEmergency.getEmergencyId();


                            sosActive = true;


                            updateStatus(
                                    statusLabel
                            );


                            playSOSSuccessAnimation(
                                    sendAlertButton
                            );


                            playCardAnimation(
                                    alertCard
                            );


                            showMessage(
                                    "Emergency Alert Sent",
                                    "The emergency alert has been saved to Firestore successfully."
                            );
                        }
                    });
        });


        // =====================================================
        // RESOLVE EMERGENCY ALERT
        // FUNCTIONALITY UNCHANGED
        // =====================================================

        resolveButton.setOnAction(e -> {

            if (!sosActive ||
                    activeEmergencyId == null ||
                    activeEmergencyId.trim().isEmpty()) {

                playCardAnimation(
                        detailsCard
                );

                showMessage(
                        "No Active Alert",
                        "There is no active emergency alert to resolve."
                );

                return;
            }


            boolean resolved =
        emergencyController.resolveEmergency(
                loggedInEmail,
                activeEmergencyId
        );


            if (!resolved) {

                playCardAnimation(
                        detailsCard
                );

                showMessage(
                        "Resolve Failed",
                        "Unable to update the emergency alert in Firestore."
                );

                return;
            }


            sosActive = false;

            activeEmergencyId = "";


            updateStatus(
                    statusLabel
            );


            playCardAnimation(
                    detailsCard
            );


            showMessage(
                    "Alert Resolved",
                    "The emergency alert has been marked as resolved."
            );
        });


        // =====================================================
        // CLEAR BUTTON
        // FUNCTIONALITY UNCHANGED
        // =====================================================

        clearButton.setOnAction(e -> {

            typeBox.setValue(
                    null
            );


            locationField.clear();


            informationField.clear();


            playCardAnimation(
                    detailsCard
            );
        });


        // =====================================================
        // ADD CONTENT
        // =====================================================

        content.getChildren().addAll(
                alertCard,
                detailsCard
        );


        // =====================================================
        // MAIN AREA
        // =====================================================

        BorderPane mainarea =
                new BorderPane();


        mainarea.setTop(
                header
        );


        mainarea.setCenter(
                content
        );


        root.setCenter(
                mainarea
        );


        // =====================================================
        // PAGE ENTRANCE ANIMATION
        // =====================================================

        playPageAnimation(
                header,
                alertCard,
                detailsCard
        );


        // =====================================================
        // RETURN SCENE
        // =====================================================

        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }


    // =====================================================
    // PAGE ENTRANCE ANIMATION
    // SAME STYLE AS MANUAL VISITOR ENTRY
    // =====================================================

    private static void playPageAnimation(
            HBox header,
            VBox alertCard,
            VBox detailsCard) {

        header.setOpacity(0);

        alertCard.setOpacity(0);

        detailsCard.setOpacity(0);


        FadeTransition headerFade =
                new FadeTransition(
                        Duration.millis(600),
                        header
                );


        headerFade.setFromValue(0);

        headerFade.setToValue(1);


        TranslateTransition headerSlide =
                new TranslateTransition(
                        Duration.millis(600),
                        header
                );


        headerSlide.setFromY(-25);

        headerSlide.setToY(0);


        FadeTransition alertFade =
                new FadeTransition(
                        Duration.millis(700),
                        alertCard
                );


        alertFade.setFromValue(0);

        alertFade.setToValue(1);


        TranslateTransition alertSlide =
                new TranslateTransition(
                        Duration.millis(700),
                        alertCard
                );


        alertSlide.setFromY(30);

        alertSlide.setToY(0);


        FadeTransition detailsFade =
                new FadeTransition(
                        Duration.millis(750),
                        detailsCard
                );


        detailsFade.setFromValue(0);

        detailsFade.setToValue(1);


        TranslateTransition detailsSlide =
                new TranslateTransition(
                        Duration.millis(750),
                        detailsCard
                );


        detailsSlide.setFromY(30);

        detailsSlide.setToY(0);


        headerFade.play();

        headerSlide.play();


        alertFade.setDelay(
                Duration.millis(150)
        );


        alertSlide.setDelay(
                Duration.millis(150)
        );


        detailsFade.setDelay(
                Duration.millis(300)
        );


        detailsSlide.setDelay(
                Duration.millis(300)
        );


        alertFade.play();

        alertSlide.play();


        detailsFade.play();

        detailsSlide.play();
    }


    // =====================================================
    // BUTTON HOVER SCALE
    // =====================================================

    private static void playHoverScale(
            javafx.scene.Node node,
            double scaleValue) {

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(120),
                        node
                );


        scale.setToX(
                scaleValue
        );


        scale.setToY(
                scaleValue
        );


        scale.play();
    }


    // =====================================================
    // FIELD HOVER ANIMATION
    // =====================================================

    private static void addFieldAnimation(
            javafx.scene.Node field) {

        field.setOnMouseEntered(
                e -> {

                    ScaleTransition scale =
                            new ScaleTransition(
                                    Duration.millis(120),
                                    field
                            );


                    scale.setToX(
                            1.01
                    );


                    scale.setToY(
                            1.01
                    );


                    scale.play();
                }
        );


        field.setOnMouseExited(
                e -> {

                    ScaleTransition scale =
                            new ScaleTransition(
                                    Duration.millis(120),
                                    field
                            );


                    scale.setToX(
                            1
                    );


                    scale.setToY(
                            1
                    );


                    scale.play();
                }
        );
    }


    // =====================================================
    // CARD FEEDBACK ANIMATION
    // SAME STYLE AS MANUAL VISITOR ENTRY
    // =====================================================

    private static void playCardAnimation(
            javafx.scene.Node node) {

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(150),
                        node
                );


        scale.setFromX(
                1
        );


        scale.setFromY(
                1
        );


        scale.setToX(
                1.015
        );


        scale.setToY(
                1.015
        );


        scale.setAutoReverse(
                true
        );


        scale.setCycleCount(
                2
        );


        scale.play();
    }


    // =====================================================
    // SOS RING ANIMATION
    // =====================================================

    private static void addSOSRingAnimation(
            Circle outerCircle,
            Circle middleCircle) {

        ScaleTransition outerPulse =
                new ScaleTransition(
                        Duration.millis(1200),
                        outerCircle
                );


        outerPulse.setFromX(
                1
        );


        outerPulse.setFromY(
                1
        );


        outerPulse.setToX(
                1.08
        );


        outerPulse.setToY(
                1.08
        );


        outerPulse.setAutoReverse(
                true
        );


        outerPulse.setCycleCount(
                ScaleTransition.INDEFINITE
        );


        outerPulse.play();


        ScaleTransition middlePulse =
                new ScaleTransition(
                        Duration.millis(1000),
                        middleCircle
                );


        middlePulse.setFromX(
                1
        );


        middlePulse.setFromY(
                1
        );


        middlePulse.setToX(
                1.05
        );


        middlePulse.setToY(
                1.05
        );


        middlePulse.setAutoReverse(
                true
        );


        middlePulse.setCycleCount(
                ScaleTransition.INDEFINITE
        );


        middlePulse.play();
    }


    // =====================================================
    // SOS SUCCESS ANIMATION
    // =====================================================

    private static void playSOSSuccessAnimation(
            Button button) {

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(180),
                        button
                );


        scale.setFromX(
                1
        );


        scale.setFromY(
                1
        );


        scale.setToX(
                1.10
        );


        scale.setToY(
                1.10
        );


        scale.setAutoReverse(
                true
        );


        scale.setCycleCount(
                2
        );


        scale.play();
    }


    // =====================================================
    // INFORMATION CARD
    // UI ONLY
    // =====================================================

    private static VBox createInfoCard(
            String iconText,
            String titleText,
            String descriptionText,
            String iconColor
    ) {

        VBox card =
                new VBox(9);


        card.setPrefWidth(
                190
        );


        card.setMinWidth(
                175
        );


        card.setPrefHeight(
                105
        );


        card.setPadding(
                new Insets(14)
        );


        card.setStyle(
                "-fx-background-color:#FAF9F7;" +
                "-fx-background-radius:12;" +
                "-fx-border-color:#DED8D3;" +
                "-fx-border-radius:12;" +
                "-fx-border-width:1;"
        );


        // =====================================================
        // ICON + TITLE
        // =====================================================

        HBox top =
                new HBox(9);


        top.setAlignment(
                Pos.CENTER_LEFT
        );


        StackPane iconContainer =
                new StackPane();


        Circle iconCircle =
                new Circle(
                        18
                );


        iconCircle.setFill(
                Color.web(
                        lightenColor(
                                iconColor
                        )
                )
        );


        Label icon =
                new Label(
                        iconText
                );


        icon.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" +
                iconColor +
                ";"
        );


        iconContainer.getChildren().addAll(
                iconCircle,
                icon
        );


        Label cardTitle =
                new Label(
                        titleText
                );


        cardTitle.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#171717;"
        );


        top.getChildren().addAll(
                iconContainer,
                cardTitle
        );


        // =====================================================
        // DESCRIPTION
        // =====================================================

        Label description =
                new Label(
                        descriptionText
                );


        description.setWrapText(
                true
        );


        description.setStyle(
                "-fx-font-size:10px;" +
                "-fx-text-fill:#68756F;"
        );


        card.getChildren().addAll(
                top,
                description
        );


        // =====================================================
        // INFORMATION CARD HOVER ANIMATION
        // =====================================================

        card.setOnMouseEntered(
                e -> {

                    ScaleTransition scale =
                            new ScaleTransition(
                                    Duration.millis(120),
                                    card
                            );


                    scale.setToX(
                            1.03
                    );


                    scale.setToY(
                            1.03
                    );


                    scale.play();
                }
        );


        card.setOnMouseExited(
                e -> {

                    ScaleTransition scale =
                            new ScaleTransition(
                                    Duration.millis(120),
                                    card
                            );


                    scale.setToX(
                            1
                    );


                    scale.setToY(
                            1
                    );


                    scale.play();
                }
        );


        return card;
    }


    // =====================================================
    // LIGHTEN COLOR
    // =====================================================

    private static String lightenColor(
            String hex
    ) {

        Color color =
                Color.web(hex);


        double r =
                Math.min(
                        1,
                        color.getRed() + 0.80
                );


        double g =
                Math.min(
                        1,
                        color.getGreen() + 0.80
                );


        double b =
                Math.min(
                        1,
                        color.getBlue() + 0.80
                );


        return String.format(
                "#%02X%02X%02X",
                (int) (r * 255),
                (int) (g * 255),
                (int) (b * 255)
        );
    }


    // =====================================================
    // RESTORE ACTIVE EMERGENCY
    // =====================================================

    private static void loadActiveEmergencyForLoggedInGuard() {

        sosActive = false;
        activeEmergencyId = "";

        if (loggedInEmail == null ||
                loggedInEmail.trim().isEmpty()) {
            return;
        }

        try {
            List<EmergencyModel> emergencies =
                    emergencyController.getEmergenciesByEmail(
                            loggedInEmail
                    );

            if (emergencies == null || emergencies.isEmpty()) {
                return;
            }

            EmergencyModel latestActive = null;

            for (EmergencyModel emergency : emergencies) {

                if (emergency == null ||
                        !"ACTIVE".equalsIgnoreCase(
                                emergency.getStatus())) {
                    continue;
                }

                if (latestActive == null) {
                    latestActive = emergency;
                    continue;
                }

                if (emergency.getCreatedAt() != null &&
                        (latestActive.getCreatedAt() == null ||
                                emergency.getCreatedAt().after(
                                        latestActive.getCreatedAt()))) {
                    latestActive = emergency;
                }
            }

            if (latestActive != null &&
                    latestActive.getEmergencyId() != null &&
                    !latestActive.getEmergencyId().trim().isEmpty()) {

                activeEmergencyId =
                        latestActive.getEmergencyId();

                emergencyType =
                        latestActive.getType() == null
                                ? ""
                                : latestActive.getType();

                emergencyLocation =
                        latestActive.getLocation() == null
                                ? ""
                                : latestActive.getLocation();

                emergencyDetails =
                        latestActive.getDetails() == null
                                ? ""
                                : latestActive.getDetails();

                emergencyTime =
                        latestActive.getTime() == null
                                ? ""
                                : latestActive.getTime();

                sosActive = true;
            }

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to restore active emergency."
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // UPDATE STATUS
    // =====================================================

    private static void updateStatus(
            Label statusLabel
    ) {

        if (sosActive) {

            statusLabel.setText(
                    "  ALERT ACTIVE  "
            );


            statusLabel.setStyle(
                    "-fx-font-size:12px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-text-fill:#9E3F3F;" +
                    "-fx-background-color:#FCE5E5;" +
                    "-fx-background-radius:20;" +
                    "-fx-padding:7 14 7 14;"
            );

        } else {

            statusLabel.setText(
                    "  ●  No active emergency  "
            );


            statusLabel.setStyle(
                    "-fx-font-size:12px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-text-fill:#315B45;" +
                    "-fx-background-color:#E8F3EC;" +
                    "-fx-background-radius:20;" +
                    "-fx-padding:7 14 7 14;"
            );
        }
    }


    // =====================================================
    // CREATE LABEL
    // =====================================================

    private static Label createLabel(
            String text
    ) {

        Label label =
                new Label(
                        text
                );


        label.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#171717;"
        );


        return label;
    }


    // =====================================================
    // STYLE TEXT FIELD
    // =====================================================

    private static void styleField(
            TextField field
    ) {

        field.setMaxWidth(
                Double.MAX_VALUE
        );


        field.setPrefHeight(
                42
        );


        field.setStyle(
                "-fx-background-color:#FAF8F6;" +
                "-fx-background-radius:8;" +
                "-fx-border-color:#D8D0CB;" +
                "-fx-border-radius:8;" +
                "-fx-text-fill:#171717;" +
                "-fx-prompt-text-fill:#7C8984;" +
                "-fx-padding:0 12 0 12;"
        );
    }


    // =====================================================
    // STYLE COMBO BOX
    // =====================================================

    private static void styleComboBox(
            ComboBox<String> box
    ) {

        box.setMaxWidth(
                Double.MAX_VALUE
        );


        box.setPrefHeight(
                42
        );


        box.setStyle(
                "-fx-background-color:#FAF8F6;" +
                "-fx-background-radius:8;" +
                "-fx-border-color:#D8D0CB;" +
                "-fx-border-radius:8;" +
                "-fx-text-fill:#171717;" +
                "-fx-padding:0 5 0 5;"
        );
    }


    // =====================================================
    // SHOW MESSAGE
    // =====================================================

    private static void showMessage(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setTitle(
                title
        );


        alert.setHeaderText(
                null
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }


    // =====================================================
    // SET LOGGED-IN GUARD EMAIL
    // =====================================================
    //
    // Call this after successful guard login.
    // No separate session file is required.
    // =====================================================

    public static void setLoggedInEmail(
            String email) {

        if (email == null ||
                email.trim().isEmpty()) {

            loggedInEmail = "";
            society = "";

            System.out.println(
                    "Emergency SOS: logged-in email is empty."
            );

            return;
        }


        loggedInEmail =
                email.trim().toLowerCase();


        try {

            UserDao userDao =
                    new UserDao();


            User user =
                    userDao.getUserByEmail(
                            loggedInEmail
                    );


                        if (user != null) {

                society = user.getSociety();

                if (society == null || society.trim().isEmpty()) {

                        society = user.getSociety();
                }

                if (society == null) {

                        society = "";

                } else {

                        society = society.trim();
                }

                } else {

                society = "";
                }


            System.out.println(
                    "Emergency SOS User Loaded"
            );


            System.out.println(
                    "Email: " +
                            loggedInEmail
            );


            System.out.println(
                    "Society: " +
                            (society.isEmpty()
                                    ? "NOT FOUND"
                                    : society)
            );


            System.out.println(
                    "======================================"
            );


        } catch (Exception e) {

            society = "";


            System.out.println(
                    "ERROR: Unable to load guard society."
            );


            e.printStackTrace();
        }
    }


    // =====================================================
    // SOS STATUS
    // =====================================================

    public static boolean isSOSActive() {

        return sosActive;
    }
}