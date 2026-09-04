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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.nio.file.Path;
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

                // The login flow stores the current user in UserDao.  Older
                // resident screens use ResidentSession, which is not populated
                // by every login entry point, so use the canonical login value
                // when that session is unavailable.
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
                                        "Resident login email was not found.");

                        return null;
                }

                final String residentEmail = loggedInEmail;

                /*
                 * =====================================================
                 * USE YOUR EXISTING FIREBASE CONFIG
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

                panel panelobj = new panel(stage, residentEmail);

                BorderPane root = new BorderPane();

                root.setLeft(
                                panelobj.getSidebar());

                VBox mainContent = new VBox(18);

                mainContent.setPadding(
                                new Insets(
                                                25,
                                                35,
                                                25,
                                                35));

                mainContent.setStyle(
                                "-fx-background-color: #e8ddd5;");

                // ================= TITLE =================

                Label title = new Label(
                                "Invite / Pre-Approve Visitor");

                title.setFont(
                                Font.font(
                                                "System",
                                                FontWeight.BOLD,
                                                24));

                title.setTextFill(
                                Color.web("#172B4D"));

                Label subtitle = new Label(
                                "Add visitor details to allow entry");

                subtitle.setFont(
                                Font.font(
                                                "System",
                                                13));

                subtitle.setTextFill(
                                Color.GRAY);

                VBox heading = new VBox(5);

                heading.getChildren().addAll(
                                title,
                                subtitle);

                // ================= FORM =================

                GridPane form = new GridPane();

                form.setHgap(25);
                form.setVgap(15);

                ColumnConstraints col1 = new ColumnConstraints();

                col1.setPercentWidth(50);

                ColumnConstraints col2 = new ColumnConstraints();

                col2.setPercentWidth(50);

                form.getColumnConstraints()
                                .addAll(
                                                col1,
                                                col2);

                // ================= NAME =================

                Label visitorNameLabel = new Label(
                                "Visitor Name");

                TextField visitorName = new TextField();

                visitorName.setPromptText(
                                "Enter full name");

                visitorName.setPrefHeight(38);

                VBox visitorNameBox = new VBox(6);

                visitorNameBox.getChildren()
                                .addAll(
                                                visitorNameLabel,
                                                visitorName);

                // ================= PHONE =================

                Label phoneLabel = new Label(
                                "Phone Number");

                TextField phoneNumber = new TextField();

                phoneNumber.setPromptText(
                                "Enter mobile number");

                phoneNumber.setPrefHeight(38);

                VBox phoneBox = new VBox(6);

                phoneBox.getChildren()
                                .addAll(
                                                phoneLabel,
                                                phoneNumber);

                form.add(
                                visitorNameBox,
                                0,
                                0);

                form.add(
                                phoneBox,
                                1,
                                0);

                // ================= PURPOSE =================

                Label purposeLabel = new Label(
                                "Purpose of Visit");

                ComboBox<String> purpose = new ComboBox<>();

                purpose.setPromptText(
                                "Select purpose");

                purpose.getItems().addAll(
                                "Personal Visit",
                                "Family Visit",
                                "Service Visit",
                                "Delivery",
                                "Other");

                purpose.setMaxWidth(
                                Double.MAX_VALUE);

                purpose.setPrefHeight(38);

                VBox purposeBox = new VBox(6);

                purposeBox.getChildren()
                                .addAll(
                                                purposeLabel,
                                                purpose);

                // ================= DATE =================

                Label dateLabel = new Label(
                                "Visit Date");

                DatePicker visitDate = new DatePicker();

                visitDate.setPromptText(
                                "Select date");

                visitDate.setMaxWidth(
                                Double.MAX_VALUE);

                visitDate.setPrefHeight(38);

                VBox dateBox = new VBox(6);

                dateBox.getChildren()
                                .addAll(
                                                dateLabel,
                                                visitDate);

                form.add(
                                purposeBox,
                                0,
                                1);

                form.add(
                                dateBox,
                                1,
                                1);

                // ================= TIME =================

                Label timeLabel = new Label(
                                "Visit Time");

                ComboBox<String> visitTime = new ComboBox<>();

                visitTime.setPromptText(
                                "Select time");

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
                                "08:00 PM");

                visitTime.setMaxWidth(
                                Double.MAX_VALUE);

                visitTime.setPrefHeight(38);

                VBox timeBox = new VBox(6);

                timeBox.getChildren()
                                .addAll(
                                                timeLabel,
                                                visitTime);

                // ================= FLAT =================

                Label flatLabel = new Label(
                                "Flat Visit");

                ComboBox<String> flatVisit = new ComboBox<>();

                flatVisit.setPromptText(
                                "Select flat to visit");

                flatVisit.getItems().addAll(
                                "A-101",
                                "A-102",
                                "A-103",
                                "B-101",
                                "B-102",
                                "B-103");

                flatVisit.setMaxWidth(
                                Double.MAX_VALUE);

                flatVisit.setPrefHeight(38);

                VBox flatBox = new VBox(6);

                flatBox.getChildren()
                                .addAll(
                                                flatLabel,
                                                flatVisit);

                form.add(
                                timeBox,
                                0,
                                2);

                form.add(
                                flatBox,
                                1,
                                2);

                // ================= GATE =================

                Label gateLabel = new Label(
                                "Gate Entry");

                ComboBox<String> gateEntry = new ComboBox<>();

                gateEntry.setPromptText(
                                "Select Gate");

                gateEntry.getItems().addAll(
                                "Main Gate",
                                "Gate 2",
                                "Service Gate");

                gateEntry.setMaxWidth(
                                Double.MAX_VALUE);

                gateEntry.setPrefHeight(38);

                VBox gateBox = new VBox(6);

                gateBox.getChildren()
                                .addAll(
                                                gateLabel,
                                                gateEntry);

                // ================= VEHICLE =================

                Label vehicleLabel = new Label(
                                "Vehicle Number (Optional)");

                TextField vehicleNumber = new TextField();

                vehicleNumber.setPromptText(
                                "Enter vehicle number");

                vehicleNumber.setPrefHeight(38);

                VBox vehicleBox = new VBox(6);

                vehicleBox.getChildren()
                                .addAll(
                                                vehicleLabel,
                                                vehicleNumber);

                form.add(
                                gateBox,
                                0,
                                3);

                form.add(
                                vehicleBox,
                                1,
                                3);

                // ================= NOTE =================

                Label note = new Label(
                                "Note: Visitor will receive a QR code / OTP for gate entry.");

                note.setPadding(
                                new Insets(10));

                note.setMaxWidth(
                                Double.MAX_VALUE);

                note.setStyle(
                                "-fx-background-color: #EAF2FF;" +
                                                "-fx-text-fill: #315B9A;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-background-radius: 5;");

                // ================= BUTTONS =================

                Button clearBtn = new Button("Clear");

                clearBtn.setPrefWidth(90);
                clearBtn.setPrefHeight(35);

                clearBtn.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #D1D5DB;" +
                                                "-fx-border-radius: 5;" +
                                                "-fx-background-radius: 5;");

                // ================= TODAY'S VISITORS LIST =================

                VBox visitorList = new VBox(8);

                Button sendInviteBtn = new Button("Send Invite");

                sendInviteBtn.setPrefWidth(120);
                sendInviteBtn.setPrefHeight(35);

                sendInviteBtn.setStyle(
                                "-fx-background-color: #4e342e;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 5;");

                HBox buttons = new HBox(10);

                buttons.setAlignment(
                                Pos.CENTER_RIGHT);

                buttons.getChildren()
                                .addAll(
                                                clearBtn,
                                                sendInviteBtn);

                // ================= CLEAR =================

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

                // ================= SEND INVITE =================

                sendInviteBtn.setOnAction(e -> {

                        // =========================
                        // VALIDATION
                        // =========================

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
                                                "Please fill all required visitor details.");

                                return;
                        }

                        // =========================
                        // PHONE VALIDATION
                        // =========================

                        if (!phoneNumber.getText()
                                        .trim()
                                        .matches("\\d{10}")) {

                                showAlert(
                                                Alert.AlertType.WARNING,
                                                "Invalid Phone",
                                                "Please enter a valid 10 digit phone number.");

                                return;
                        }

                        sendInviteBtn.setDisable(true);

                        /*
                         * =====================================================
                         * FIRESTORE WORK SHOULD NOT
                         * BLOCK JAVAFX UI THREAD.
                         * =====================================================
                         */

                        Thread thread = new Thread(() -> {

                                try {

                                        // =========================
                                        // 1. SAVE VISITOR
                                        // =========================

                                        VisitorModel visitor = visitorController.sendInvite(

                                                        // IMPORTANT:
                                                        // CURRENT LOGGED-IN RESIDENT
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
                                                                        .trim());

                                        // =========================
                                        // 2. CREATE QR DATA
                                        // =========================

                                        String qrData = "SOC360:"
                                                        + visitor.getQrToken();

                                        // =========================
                                        // 3. QR FILE NAME
                                        // =========================

                                        String safeName = visitor.getVisitorName()
                                                        .replaceAll(
                                                                        "[^a-zA-Z0-9]",
                                                                        "_");

                                        String fileName = safeName
                                                        + "_"
                                                        + visitor.getId()
                                                        + ".png";

                                        // =========================
                                        // 4. GENERATE QR
                                        // =========================

                                        Path qrPath = QRCodeUtil.generateQRCode(
                                                        qrData,
                                                        fileName);

                                        // =========================
                                        // 5. SHOW RESULT
                                        // =========================

                                        Platform.runLater(() -> {

                                                sendInviteBtn
                                                                .setDisable(false);

                                                // Refresh today's list after a successful save.
                                                loadTodayVisitors(
                                                                visitorList,
                                                                residentEmail);

                                                showQRCode(
                                                                visitor,
                                                                qrPath);
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
                                                                                + ex.getMessage());
                                        });
                                }
                        });

                        thread.setDaemon(true);

                        thread.start();
                });

                // ================= TODAY'S VISITORS =================

                Label todayTitle = new Label(
                                "Today's Invited Visitors");

                todayTitle.setFont(
                                Font.font(
                                                "System",
                                                FontWeight.BOLD,
                                                16));

                // ================= REFRESH BUTTON =================

                Button refreshBtn = new Button("⟳");
                refreshBtn.setPrefWidth(40);
                refreshBtn.setPrefHeight(32);
                refreshBtn.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #D1D5DB;" +
                                                "-fx-border-radius: 5;" +
                                                "-fx-background-radius: 5;" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-text-fill: #172B4D;");

                refreshBtn.setTooltip(
                                new Tooltip("Refresh today's visitors"));

                refreshBtn.setOnAction(e ->
                                loadTodayVisitors(
                                                visitorList,
                                                residentEmail));

                HBox todayHeader = new HBox(10);
                todayHeader.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(todayTitle, Priority.ALWAYS);

                todayHeader.getChildren().addAll(
                                todayTitle,
                                refreshBtn);

                // Load ONLY the logged-in resident's visitors when screen opens.
                loadTodayVisitors(
                                visitorList,
                                residentEmail);

                // ================= ADD EVERYTHING =================

                mainContent.getChildren()
                                .addAll(
                                                heading,
                                                form,
                                                note,
                                                buttons,
                                                todayHeader,
                                                visitorList);

                root.setCenter(
                                mainContent);

                return new Scene(
                                root,
                                ScreenSize.getWidth(),
                                ScreenSize.getHeight());
        }

        // =====================================================
        // SHOW QR CODE
        // =====================================================

        private void showQRCode(
                        VisitorModel visitor,
                        Path qrPath) {

                Alert alert = new Alert(
                                Alert.AlertType.INFORMATION);

                alert.setTitle(
                                "Visitor Invitation");

                alert.setHeaderText(
                                "Invitation Sent Successfully!");

                Image image = new Image(
                                qrPath.toUri().toString());

                ImageView imageView = new ImageView(image);

                imageView.setFitWidth(300);

                imageView.setFitHeight(300);

                imageView.setPreserveRatio(true);

                Label visitorLabel = new Label(
                                "Visitor: "
                                                + visitor.getVisitorName());

                Label flatLabel = new Label(
                                "Flat: "
                                                + visitor.getFlatNumber());

                Label dateLabel = new Label(
                                "Date: "
                                                + visitor.getVisitDate());

                Label timeLabel = new Label(
                                "Time: "
                                                + visitor.getVisitTime());

                Label gateLabel = new Label(
                                "Gate: "
                                                + visitor.getGate());

                VBox content = new VBox(10);

                content.setAlignment(
                                Pos.CENTER);

                content.getChildren()
                                .addAll(
                                                visitorLabel,
                                                flatLabel,
                                                dateLabel,
                                                timeLabel,
                                                gateLabel,
                                                imageView);

                alert.getDialogPane()
                                .setContent(content);

                // =====================================================
                // OK BUTTON
                // =====================================================

                Button okButton = (Button) alert
                                .getDialogPane()
                                .lookupButton(
                                                ButtonType.OK);

                // =====================================================
                // WHEN USER CLICKS OK
                // =====================================================

                okButton.setOnAction(e -> {

                        // =================================================
                        // DISABLE OK WHILE UPLOADING
                        // =================================================

                        okButton.setDisable(true);

                        // =================================================
                        // CLOUDINARY UPLOAD IN BACKGROUND
                        // =================================================

                        Thread uploadThread = new Thread(() -> {

                                try {

                                        System.out.println(
                                                        "================================");

                                        System.out.println(
                                                        "Uploading QR to Cloudinary...");

                                        System.out.println(
                                                        "Visitor ID: "
                                                                        + visitor.getId());

                                        System.out.println(
                                                        "QR Path: "
                                                                        + qrPath);

                                        // =================================
                                        // UPLOAD
                                        // =================================

                                        String cloudinaryUrl = QRCodeUtil
                                                        .uploadQRCodeToCloudinary(
                                                                        qrPath,
                                                                        visitor.getId());

                                        System.out.println(
                                                        "QR uploaded successfully!");

                                        System.out.println(
                                                        "Cloudinary URL: "
                                                                        + cloudinaryUrl);

                                        System.out.println(
                                                        "================================");

                                        Platform.runLater(() -> {

                                                okButton.setDisable(false);

                                                showAlert(
                                                                Alert.AlertType.INFORMATION,
                                                                "QR Uploaded",
                                                                "QR code uploaded to Cloudinary successfully.");
                                        });

                                } catch (Exception ex) {

                                        ex.printStackTrace();

                                        Platform.runLater(() -> {

                                                okButton.setDisable(false);

                                                showAlert(
                                                                Alert.AlertType.ERROR,
                                                                "Cloudinary Upload Failed",
                                                                "QR code could not be uploaded.\n\n"
                                                                                + ex.getMessage());
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

                Thread thread = new Thread(() -> {

                        try {

                                /*
                                 * =================================================
                                 * ONLY CURRENT LOGGED-IN RESIDENT
                                 * =================================================
                                 */
                                List<VisitorModel> visitors = visitorController
                                                .getTodayVisitors(
                                                                loggedInEmail);

                                Platform.runLater(() -> {

                                        visitorList
                                                        .getChildren()
                                                        .clear();

                                        if (visitors == null || visitors.isEmpty()) {

                                                Label emptyLabel =
                                                                new Label(
                                                                                "No invited visitors for today.");

                                                emptyLabel.setStyle(
                                                                "-fx-text-fill: #777777;" +
                                                                                "-fx-font-size: 13px;" +
                                                                                "-fx-padding: 15;");

                                                visitorList.getChildren()
                                                                .add(emptyLabel);

                                                return;
                                        }

                                        for (VisitorModel visitor : visitors) {

                                                HBox row = createVisitorRow(

                                                                visitor.getVisitorName(),

                                                                visitor.getPurpose(),

                                                                visitor.getFlatNumber(),

                                                                visitor.getVisitTime(),

                                                                visitor.getStatus());

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
                                                                        + ex.getMessage());
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

                Label nameLabel = new Label(name);

                nameLabel.setFont(
                                Font.font(
                                                "System",
                                                FontWeight.BOLD,
                                                13));

                Label purposeLabel = new Label(purpose);

                Label flatLabel = new Label(flat);

                Label timeLabel = new Label(time);

                Label statusLabel = new Label(status);

                statusLabel.setStyle(
                                "-fx-background-color: #DFF6E5;" +
                                                "-fx-text-fill: #16803C;" +
                                                "-fx-padding: 5 10 5 10;" +
                                                "-fx-background-radius: 12;");

                HBox row = new HBox(20);

                row.setAlignment(
                                Pos.CENTER_LEFT);

                row.setPadding(
                                new Insets(12));

                row.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #E5E7EB;" +
                                                "-fx-border-radius: 5;" +
                                                "-fx-background-radius: 5;");

                HBox.setHgrow(
                                nameLabel,
                                Priority.ALWAYS);

                row.getChildren()
                                .addAll(
                                                nameLabel,
                                                purposeLabel,
                                                flatLabel,
                                                timeLabel,
                                                statusLabel);

                return row;
        }

        // =====================================================
        // ALERT
        // =====================================================

        private void showAlert(
                        Alert.AlertType type,
                        String title,
                        String message) {

                Alert alert = new Alert(type);

                alert.setTitle(title);

                alert.setHeaderText(null);

                alert.setContentText(message);

                alert.showAndWait();
        }
}
