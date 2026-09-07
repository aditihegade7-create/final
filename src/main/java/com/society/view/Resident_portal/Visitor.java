package com.society.view.Resident_portal;

import com.google.cloud.firestore.Firestore;
import com.society.controller.Resident_Controller.VisitorController;
import com.society.dao.Resident_dao.VisitorDAO;
import com.society.dao.Welcome.UserDao;
import com.society.model.Resident_model.VisitorModel;
import com.society.service.resident_service.ResidentSession;
import com.society.util.resident_util.QRCodeUtil;
import com.society.config.FirebaseConfig;
import com.society.view.ScreenSize;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Visitor {

    private VisitorController visitorController;

    public Scene getVisitorScene(Stage stage) {

        /*
         * =====================================================
         * GET CURRENTLY LOGGED-IN RESIDENT EMAIL
         * =====================================================
         */

        String loggedInEmail = ResidentSession.getLoggedInEmail();

        if (loggedInEmail == null || loggedInEmail.trim().isEmpty()) {
            loggedInEmail = UserDao.getLoggedInEmail();
        }

        /*
         * =====================================================
         * CHECK LOGIN SESSION
         * =====================================================
         */

        if (loggedInEmail == null ||
                loggedInEmail.trim().isEmpty()) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Login Required",
                    "Resident login email was not found."
            );

            return null;
        }

        final String residentEmail = loggedInEmail;

        /*
         * =====================================================
         * FIREBASE
         * =====================================================
         */

        Firestore firestore = FirebaseConfig.getFirestore();

        /*
         * =====================================================
         * DAO
         * =====================================================
         */

        VisitorDAO visitorDAO = new VisitorDAO(firestore);

        /*
         * =====================================================
         * CONTROLLER
         * =====================================================
         */

        visitorController = new VisitorController(visitorDAO);

        /*
         * =====================================================
         * RESIDENT SIDEBAR
         * =====================================================
         */

        panel panelobj = new panel(stage, residentEmail);

        BorderPane root = new BorderPane();

        /*
         * =====================================================
         * BACKGROUND IMAGE
         * =====================================================
         */

        URL imageURL =
                getClass().getResource(
                        "/background-Dashboard5.jpeg"
                );

        if (imageURL != null) {

            Image backgroundImage =
                    new Image(
                            imageURL.toExternalForm()
                    );

            BackgroundImage background =
                    new BackgroundImage(
                            backgroundImage,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            new BackgroundSize(
                                    100,
                                    100,
                                    true,
                                    true,
                                    false,
                                    true
                            )
                    );

            root.setBackground(
                    new Background(background)
            );

        } else {

            System.out.println(
                    "❌ Background image NOT FOUND: /background-Dashboard5.jpeg"
            );
        }

        /*
         * =====================================================
         * SIDEBAR
         * =====================================================
         */

        root.setLeft(panelobj.getSidebar());

        /*
         * =====================================================
         * MAIN CONTENT
         * =====================================================
         */

        VBox mainContent = new VBox(18);

        mainContent.setPadding(
                new Insets(
                        25,
                        35,
                        25,
                        35
                )
        );

        /*
         * Transparent so root background image is visible
         */

        mainContent.setStyle(
                "-fx-background-color: transparent;"
        );

        /*
         * =====================================================
         * HEADER
         * =====================================================
         */

        HBox header = new HBox();

        /*
         * HEADER HEIGHT = 80px
         */

        header.setPrefHeight(80);
        header.setMinHeight(80);
        header.setMaxHeight(80);

        header.setPadding(
                new Insets(
                        15,
                        25,
                        15,
                        25
                )
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        /*
         * HEADER REMAINS SOLID
         */

        header.setStyle(
                "-fx-background-color:#4e342e;"
        );

        /*
         * =====================================================
         * HEADER LEFT CONTENT
         * =====================================================
         */

        VBox headerText = new VBox(4);

        Label greeting = new Label(
                "Invite / Pre-Approve Visitor"
        );

        greeting.setStyle(
                "-fx-font-family:'Bodoni MT';" +
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        Label description = new Label(
                "Add visitor details to allow entry"
        );

        description.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:white;"
        );

        headerText.getChildren().addAll(
                greeting,
                description
        );

        /*
         * =====================================================
         * HEADER SPACER
         * =====================================================
         */

        Region headerSpacer = new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );

        /*
         * =====================================================
         * CURRENT DAY & DATE
         * =====================================================
         */

        LocalDate today = LocalDate.now();

        Label day = new Label();
        Label date = new Label();

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
                "-fx-text-fill:white;" +
                "-fx-font-size:13px;"
        );

        date.setStyle(
                "-fx-text-fill:white;" +
                "-fx-font-size:12px;"
        );

        /*
         * =====================================================
         * DATE BOX
         * =====================================================
         */

        VBox dateBox = new VBox(3);

        dateBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        dateBox.getChildren().addAll(
                day,
                date
        );

        /*
         * =====================================================
         * ADD HEADER CONTENT
         * =====================================================
         */

        header.getChildren().addAll(
                headerText,
                headerSpacer,
                dateBox
        );

        /*
         * =====================================================
         * FORM
         * =====================================================
         */

        GridPane form = new GridPane();

        form.setHgap(25);
        form.setVgap(15);

        ColumnConstraints col1 =
                new ColumnConstraints();

        col1.setPercentWidth(50);

        ColumnConstraints col2 =
                new ColumnConstraints();

        col2.setPercentWidth(50);

        form.getColumnConstraints()
                .addAll(
                        col1,
                        col2
                );

        /*
         * =====================================================
         * VISITOR NAME
         * =====================================================
         */

        Label visitorNameLabel =
                new Label("Visitor Name");

        TextField visitorName =
                new TextField();

        visitorName.setPromptText(
                "Enter full name"
        );

        visitorName.setPrefHeight(38);

        VBox visitorNameBox =
                new VBox(6);

        visitorNameBox.getChildren()
                .addAll(
                        visitorNameLabel,
                        visitorName
                );

        /*
         * =====================================================
         * PHONE
         * =====================================================
         */

        Label phoneLabel =
                new Label("Phone Number");

        TextField phoneNumber =
                new TextField();

        phoneNumber.setPromptText(
                "Enter mobile number"
        );

        phoneNumber.setPrefHeight(38);

        VBox phoneBox =
                new VBox(6);

        phoneBox.getChildren()
                .addAll(
                        phoneLabel,
                        phoneNumber
                );

        form.add(
                visitorNameBox,
                0,
                0
        );

        form.add(
                phoneBox,
                1,
                0
        );

        /*
         * =====================================================
         * PURPOSE
         * =====================================================
         */

        Label purposeLabel =
                new Label("Purpose of Visit");

        ComboBox<String> purpose =
                new ComboBox<>();

        purpose.setPromptText(
                "Select purpose"
        );

        purpose.getItems().addAll(
                "Personal Visit",
                "Family Visit",
                "Service Visit",
                "Delivery",
                "Other"
        );

        purpose.setMaxWidth(
                Double.MAX_VALUE
        );

        purpose.setPrefHeight(38);

        VBox purposeBox =
                new VBox(6);

        purposeBox.getChildren()
                .addAll(
                        purposeLabel,
                        purpose
                );

        /*
         * =====================================================
         * DATE
         * =====================================================
         */

        Label dateLabel =
                new Label("Visit Date");

        DatePicker visitDate =
                new DatePicker();

        visitDate.setPromptText(
                "Select date"
        );

        visitDate.setMaxWidth(
                Double.MAX_VALUE
        );

        visitDate.setPrefHeight(38);

        VBox dateBoxForm =
                new VBox(6);

        dateBoxForm.getChildren()
                .addAll(
                        dateLabel,
                        visitDate
                );

        form.add(
                purposeBox,
                0,
                1
        );

        form.add(
                dateBoxForm,
                1,
                1
        );

        /*
         * =====================================================
         * TIME
         * =====================================================
         */

        Label timeLabel =
                new Label("Visit Time");

        ComboBox<String> visitTime =
                new ComboBox<>();

        visitTime.setPromptText(
                "Select time"
        );

        visitTime.getItems().addAll(
                "08:00 AM",
                "09:00 AM",
                "10:00 AM",
                "11:00 AM",
                "12:00 PM",
                "01:00 PM",
                "02:00 PM",
                "03:00 PM",
                "04:00 PM",
                "05:00 PM",
                "06:00 PM",
                "07:00 PM",
                "08:00 PM"
        );

        visitTime.setMaxWidth(
                Double.MAX_VALUE
        );

        visitTime.setPrefHeight(38);

        VBox timeBox =
                new VBox(6);

        timeBox.getChildren()
                .addAll(
                        timeLabel,
                        visitTime
                );

        /*
         * =====================================================
         * FLAT
         * =====================================================
         */

        Label flatLabel =
                new Label("Flat Visit");

        ComboBox<String> flatVisit =
                new ComboBox<>();

        flatVisit.setPromptText(
                "Select flat to visit"
        );

        flatVisit.getItems().addAll(
                "A-101",
                "A-102",
                "A-103",
                "B-101",
                "B-102",
                "B-103"
        );

        flatVisit.setMaxWidth(
                Double.MAX_VALUE
        );

        flatVisit.setPrefHeight(38);

        VBox flatBox =
                new VBox(6);

        flatBox.getChildren()
                .addAll(
                        flatLabel,
                        flatVisit
                );

        form.add(
                timeBox,
                0,
                2
        );

        form.add(
                flatBox,
                1,
                2
        );

        /*
         * =====================================================
         * GATE
         * =====================================================
         */

        Label gateLabel =
                new Label("Gate Entry");

        ComboBox<String> gateEntry =
                new ComboBox<>();

        gateEntry.setPromptText(
                "Select Gate"
        );

        gateEntry.getItems().addAll(
                "Main Gate",
                "Gate 2",
                "Service Gate"
        );

        gateEntry.setMaxWidth(
                Double.MAX_VALUE
        );

        gateEntry.setPrefHeight(38);

        VBox gateBox =
                new VBox(6);

        gateBox.getChildren()
                .addAll(
                        gateLabel,
                        gateEntry
                );

        /*
         * =====================================================
         * VEHICLE
         * =====================================================
         */

        Label vehicleLabel =
                new Label(
                        "Vehicle Number (Optional)"
                );

        TextField vehicleNumber =
                new TextField();

        vehicleNumber.setPromptText(
                "Enter vehicle number"
        );

        vehicleNumber.setPrefHeight(38);

        VBox vehicleBox =
                new VBox(6);

        vehicleBox.getChildren()
                .addAll(
                        vehicleLabel,
                        vehicleNumber
                );

        form.add(
                gateBox,
                0,
                3
        );

        form.add(
                vehicleBox,
                1,
                3
        );

        /*
         * =====================================================
         * NOTE
         * =====================================================
         */

        Label note =
                new Label(
                        "Note: Visitor will receive a QR code / OTP for gate entry."
                );

        note.setPadding(
                new Insets(10)
        );

        note.setMaxWidth(
                Double.MAX_VALUE
        );

        note.setStyle(
                "-fx-background-color: #EAF2FF;" +
                "-fx-text-fill: #315B9A;" +
                "-fx-font-size: 12px;" +
                "-fx-background-radius: 5;"
        );

        /*
         * =====================================================
         * CLEAR BUTTON
         * =====================================================
         */

        Button clearBtn =
                new Button("Clear");

        clearBtn.setPrefWidth(90);
        clearBtn.setPrefHeight(35);

        clearBtn.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #D1D5DB;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;"
        );

        /*
         * =====================================================
         * VISITOR LIST
         * =====================================================
         */

        VBox visitorList =
                new VBox(8);

        /*
         * =====================================================
         * SEND INVITE BUTTON
         * =====================================================
         */

        Button sendInviteBtn =
                new Button("Send Invite");

        sendInviteBtn.setPrefWidth(120);
        sendInviteBtn.setPrefHeight(35);

        sendInviteBtn.setStyle(
                "-fx-background-color: #4e342e;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;"
        );

        /*
         * =====================================================
         * BUTTON CONTAINER
         * =====================================================
         */

        HBox buttons =
                new HBox(10);

        buttons.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttons.getChildren()
                .addAll(
                        clearBtn,
                        sendInviteBtn
                );

        /*
         * =====================================================
         * CLEAR ACTION
         * =====================================================
         */

        clearBtn.setOnAction(e -> {

            visitorName.clear();

            phoneNumber.clear();

            vehicleNumber.clear();

            purpose.setValue(null);

            visitDate.setValue(null);

            visitTime.setValue(null);

            flatVisit.setValue(null);

            gateEntry.setValue(null);
        });

        /*
         * =====================================================
         * SEND INVITE
         * =====================================================
         */

        sendInviteBtn.setOnAction(e -> {

            /*
             * =================================================
             * VALIDATION
             * =================================================
             */

            if (visitorName.getText()
                    .trim()
                    .isEmpty()

                    || phoneNumber.getText()
                            .trim()
                            .isEmpty()

                    || purpose.getValue() == null

                    || visitDate.getValue() == null

                    || visitTime.getValue() == null

                    || flatVisit.getValue() == null

                    || gateEntry.getValue() == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Missing Information",
                        "Please fill all required visitor details."
                );

                return;
            }

            /*
             * =================================================
             * PHONE VALIDATION
             * =================================================
             */

            if (!phoneNumber.getText()
                    .trim()
                    .matches("\\d{10}")) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Phone",
                        "Please enter a valid 10 digit phone number."
                );

                return;
            }

            sendInviteBtn.setDisable(true);

            /*
             * =================================================
             * FIRESTORE BACKGROUND THREAD
             * =================================================
             */

            Thread thread =
                    new Thread(() -> {

                        try {

                            /*
                             * =========================
                             * 1. SAVE VISITOR
                             * =========================
                             */

                            VisitorModel visitor =
                                    visitorController.sendInvite(

                                            residentEmail,

                                            visitorName.getText()
                                                    .trim(),

                                            phoneNumber.getText()
                                                    .trim(),

                                            purpose.getValue(),

                                            visitDate.getValue()
                                                    .toString(),

                                            visitTime.getValue(),

                                            flatVisit.getValue(),

                                            gateEntry.getValue(),

                                            vehicleNumber.getText()
                                                    .trim()
                                    );

                            /*
                             * =========================
                             * 2. CREATE QR DATA
                             * =========================
                             */

                            String qrData =
                                    "SOC360:"
                                            + visitor.getQrToken();

                            /*
                             * =========================
                             * 3. QR FILE NAME
                             * =========================
                             */

                            String safeName =
                                    visitor.getVisitorName()
                                            .replaceAll(
                                                    "[^a-zA-Z0-9]",
                                                    "_"
                                            );

                            String fileName =
                                    safeName
                                            + "_"
                                            + visitor.getId()
                                            + ".png";

                            /*
                             * =========================
                             * 4. GENERATE QR
                             * =========================
                             */

                            Path qrPath =
                                    QRCodeUtil.generateQRCode(
                                            qrData,
                                            fileName
                                    );

                            /*
                             * =========================
                             * 5. SHOW RESULT
                             * =========================
                             */

                            Platform.runLater(() -> {

                                sendInviteBtn
                                        .setDisable(false);

                                loadTodayVisitors(
                                        visitorList,
                                        residentEmail
                                );

                                showQRCode(
                                        visitor,
                                        qrPath
                                );
                            });

                        } catch (Exception ex) {

                            ex.printStackTrace();

                            Platform.runLater(() -> {

                                sendInviteBtn
                                        .setDisable(false);

                                showAlert(
                                        Alert.AlertType.ERROR,
                                        "Error",
                                        "Visitor could not be saved.\n\n"
                                                + ex.getMessage()
                                );
                            });
                        }
                    });

            thread.setDaemon(true);
            thread.start();
        });

        /*
         * =====================================================
         * TODAY'S VISITORS TITLE
         * =====================================================
         */

        Label todayTitle =
                new Label(
                        "Today's Invited Visitors"
                );

        todayTitle.setStyle(
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;"
        );

        /*
         * =====================================================
         * REFRESH BUTTON
         * =====================================================
         */

        Button refreshBtn =
                new Button("⟳");

        refreshBtn.setPrefWidth(40);
        refreshBtn.setPrefHeight(32);

        refreshBtn.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #D1D5DB;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-font-size: 18px;" +
                "-fx-text-fill: #172B4D;"
        );

        refreshBtn.setTooltip(
                new Tooltip(
                        "Refresh today's visitors"
                )
        );

        refreshBtn.setOnAction(e ->
                loadTodayVisitors(
                        visitorList,
                        residentEmail
                )
        );

        HBox todayHeader =
                new HBox(10);

        todayHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                todayTitle,
                Priority.ALWAYS
        );

        todayHeader.getChildren()
                .addAll(
                        todayTitle,
                        refreshBtn
                );

        /*
         * =====================================================
         * LOAD TODAY'S VISITORS
         * =====================================================
         */

        loadTodayVisitors(
                visitorList,
                residentEmail
        );

        /*
         * =====================================================
         * ADD FORM CONTENT
         *
         * IMPORTANT:
         * Header is NOT added here.
         * This keeps header directly attached
         * to sidebar.
         * =====================================================
         */

        mainContent.getChildren()
                .addAll(
                        form,
                        note,
                        buttons,
                        todayHeader,
                        visitorList
                );

        /*
         * =====================================================
         * MAIN AREA
         *
         * Header is placed directly on top.
         * MainContent remains below it with
         * its original padding.
         * =====================================================
         */

        BorderPane mainArea =
                new BorderPane();

        mainArea.setStyle(
                "-fx-background-color: transparent;"
        );

        /*
         * HEADER DIRECTLY ATTACHED TO SIDEBAR
         */

        mainArea.setTop(header);

        /*
         * FORM / CONTENT BELOW HEADER
         */

        mainArea.setCenter(mainContent);

        /*
         * =====================================================
         * ROOT CENTER
         * =====================================================
         */

        root.setCenter(
                mainArea
        );

        /*
         * =====================================================
         * SCENE
         * =====================================================
         */

        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }

    // =====================================================
    // SHOW QR CODE
    // =====================================================

    private void showQRCode(
            VisitorModel visitor,
            Path qrPath) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Visitor Invitation"
        );

        alert.setHeaderText(
                "Invitation Sent Successfully!"
        );

        Image image =
                new Image(
                        qrPath.toUri()
                                .toString()
                );

        ImageView imageView =
                new ImageView(image);

        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        Label visitorLabel =
                new Label(
                        "Visitor: "
                                + visitor.getVisitorName()
                );

        Label flatLabel =
                new Label(
                        "Flat: "
                                + visitor.getFlatNumber()
                );

        Label dateLabel =
                new Label(
                        "Date: "
                                + visitor.getVisitDate()
                );

        Label timeLabel =
                new Label(
                        "Time: "
                                + visitor.getVisitTime()
                );

        Label gateLabel =
                new Label(
                        "Gate: "
                                + visitor.getGate()
                );

        VBox content =
                new VBox(10);

        content.setAlignment(
                Pos.CENTER
        );

        content.getChildren()
                .addAll(
                        visitorLabel,
                        flatLabel,
                        dateLabel,
                        timeLabel,
                        gateLabel,
                        imageView
                );

        alert.getDialogPane()
                .setContent(content);

        /*
         * =====================================================
         * OK BUTTON
         * =====================================================
         */

        Button okButton =
                (Button) alert
                        .getDialogPane()
                        .lookupButton(
                                ButtonType.OK
                        );

        /*
         * =====================================================
         * OK ACTION
         * =====================================================
         */

        okButton.setOnAction(e -> {

            okButton.setDisable(true);

            /*
             * =================================================
             * CLOUDINARY UPLOAD
             * =================================================
             */

            Thread uploadThread =
                    new Thread(() -> {

                        try {

                            System.out.println(
                                    "================================"
                            );

                            System.out.println(
                                    "Uploading QR to Cloudinary..."
                            );

                            System.out.println(
                                    "Visitor ID: "
                                            + visitor.getId()
                            );

                            System.out.println(
                                    "QR Path: "
                                            + qrPath
                            );

                            /*
                             * =========================
                             * UPLOAD
                             * =========================
                             */

                            String cloudinaryUrl =
                                    QRCodeUtil
                                            .uploadQRCodeToCloudinary(
                                                    qrPath,
                                                    visitor.getId()
                                            );

                            System.out.println(
                                    "QR uploaded successfully!"
                            );

                            System.out.println(
                                    "Cloudinary URL: "
                                            + cloudinaryUrl
                            );

                            System.out.println(
                                    "================================"
                            );

                            Platform.runLater(() -> {

                                okButton
                                        .setDisable(false);

                                showAlert(
                                        Alert.AlertType.INFORMATION,
                                        "QR Uploaded",
                                        "QR code uploaded to Cloudinary successfully."
                                );
                            });

                        } catch (Exception ex) {

                            ex.printStackTrace();

                            Platform.runLater(() -> {

                                okButton
                                        .setDisable(false);

                                showAlert(
                                        Alert.AlertType.ERROR,
                                        "Cloudinary Upload Failed",
                                        "QR code could not be uploaded.\n\n"
                                                + ex.getMessage()
                                );
                            });
                        }
                    });

            uploadThread.setDaemon(true);
            uploadThread.start();
        });

        alert.showAndWait();
    }

    // =====================================================
    // LOAD TODAY'S VISITORS
    // =====================================================

    private void loadTodayVisitors(
            VBox visitorList,
            String loggedInEmail) {

        Thread thread =
                new Thread(() -> {

                    try {

                        /*
                         * =================================================
                         * ONLY CURRENT LOGGED-IN RESIDENT
                         * =================================================
                         */

                        List<VisitorModel> visitors =
                                visitorController
                                        .getTodayVisitors(
                                                loggedInEmail
                                        );

                        Platform.runLater(() -> {

                            visitorList
                                    .getChildren()
                                    .clear();

                            if (visitors == null
                                    || visitors.isEmpty()) {

                                Label emptyLabel =
                                        new Label(
                                                "No invited visitors for today."
                                        );

                                emptyLabel.setStyle(
                                        "-fx-text-fill: #777777;" +
                                        "-fx-font-size: 13px;" +
                                        "-fx-padding: 15;"
                                );

                                visitorList
                                        .getChildren()
                                        .add(emptyLabel);

                                return;
                            }

                            for (VisitorModel visitor :
                                    visitors) {

                                HBox row =
                                        createVisitorRow(
                                                visitor
                                                        .getVisitorName(),

                                                visitor
                                                        .getPurpose(),

                                                visitor
                                                        .getFlatNumber(),

                                                visitor
                                                        .getVisitTime(),

                                                visitor
                                                        .getStatus()
                                        );

                                visitorList
                                        .getChildren()
                                        .add(row);
                            }
                        });

                    } catch (Exception ex) {

                        ex.printStackTrace();

                        Platform.runLater(() -> {

                            showAlert(
                                    Alert.AlertType.ERROR,
                                    "Error",
                                    "Visitors could not be loaded.\n\n"
                                            + ex.getMessage()
                            );
                        });
                    }
                });

        thread.setDaemon(true);
        thread.start();
    }

    // =====================================================
    // VISITOR ROW
    // =====================================================

    private HBox createVisitorRow(
            String name,
            String purpose,
            String flat,
            String time,
            String status) {

        Label nameLabel =
                new Label(name);

        nameLabel.setStyle(
                "-fx-font-weight:bold;" +
                "-fx-font-size:13px;"
        );

        Label purposeLabel =
                new Label(purpose);

        Label flatLabel =
                new Label(flat);

        Label timeLabel =
                new Label(time);

        Label statusLabel =
                new Label(status);

        statusLabel.setStyle(
                "-fx-background-color: #DFF6E5;" +
                "-fx-text-fill: #16803C;" +
                "-fx-padding: 5 10 5 10;" +
                "-fx-background-radius: 12;"
        );

        HBox row =
                new HBox(20);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPadding(
                new Insets(12)
        );

        row.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #E5E7EB;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;"
        );

        HBox.setHgrow(
                nameLabel,
                Priority.ALWAYS
        );

        row.getChildren()
                .addAll(
                        nameLabel,
                        purposeLabel,
                        flatLabel,
                        timeLabel,
                        statusLabel
                );

        return row;
    }

    // =====================================================
    // ALERT
    // =====================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}