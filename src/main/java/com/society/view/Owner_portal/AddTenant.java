package com.society.view.Owner_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.society.controller.Owner_Controller.ResidentController;
import com.society.controller.welcome.UserController;
import com.society.model.Owner_model.ResidentModel;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

public class AddTenant {

        private static final ResidentController residentController = new ResidentController();

        private static final UserController userController = new UserController();

        public static Scene createScene(Stage stage, String ownerEmail) {

                BorderPane root = new BorderPane();

                OwnerSidebar sidebar = new OwnerSidebar(stage, ownerEmail);

                root.setLeft(
                                sidebar.getSidebar());

                // =====================================================
                // MAIN CONTENT
                // =====================================================
                VBox mainContent = new VBox();

                mainContent.setPadding(
                                new Insets(0, 0, 25, 0));

                mainContent.setSpacing(18);

                mainContent.setAlignment(
                                Pos.TOP_LEFT);

                mainContent.setStyle(
                                "-fx-background-color: #e8ddd5;");

                // =====================================================
                // HEADER
                // =====================================================

                HBox header = new HBox();

                header.setPrefHeight(80);
                header.setPadding(
                                new Insets(15, 25, 15, 25));
                header.setAlignment(Pos.CENTER_LEFT);

                header.setStyle(
                                "-fx-background-color:#4e342e;");

                VBox headingBox = new VBox();

                headingBox.setSpacing(3);

                Label greeting = new Label("Add Tenant");

                greeting.setStyle( "-fx-font-family:'Bodoni MT';" +
                                "-fx-font-size:24px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:#ffffff;");

                Label description = new Label(
                                "Add and manage tenant details for your property");

                description.setStyle( "-fx-font-family:'Bodoni MT';" +
                                "-fx-font-size:12px;" +
                                                "-fx-text-fill:#ffffff;");

                headingBox.getChildren().addAll(
                                greeting,
                                description);

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                Label day = new Label();

                Label date = new Label();

                LocalDate today = LocalDate.now();

                day.setText(
                                today.format(
                                                DateTimeFormatter.ofPattern("EEEE")));

                day.setStyle(
                                "-fx-text-fill:#ffffff;");

                date.setText(
                                today.format(
                                                DateTimeFormatter.ofPattern(
                                                                "dd MMMM yyyy")));

                date.setStyle(
                                "-fx-text-fill:#ffffff;");

                VBox dateBox = new VBox();

                dateBox.setSpacing(2);

                dateBox.getChildren().addAll(
                                day,
                                date);

                header.getChildren().addAll(
                                headingBox,
                                spacer,
                                dateBox);

                // =====================================================
                // FORM CARD
                // =====================================================

                VBox formCard = new VBox();

                formCard.setPadding(
                                new Insets(20, 25, 20, 25));

                formCard.setSpacing(15);
                formCard.setMaxWidth(Double.MAX_VALUE);

                formCard.setStyle(
                                "-fx-background-color:#efeff3;" +
                                                "-fx-background-radius:12;");

                // =====================================================
                // FORM HEADING
                // =====================================================

                Label formTitle = new Label(
                                "Tenant Information");

                formTitle.setStyle(
                                "-fx-font-size:18px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:#102A43;");

                Label formSubtitle = new Label(
                                "Please provide the required tenant details");

                formSubtitle.setStyle(
                                "-fx-font-size:12px;" +
                                                "-fx-text-fill:#546E7A;");

                VBox formHeading = new VBox(
                                3,
                                formTitle,
                                formSubtitle);

                // =====================================================
                // FORM
                // =====================================================

                GridPane form = new GridPane();

                form.setHgap(25);
                form.setVgap(10);

                // =====================================================
                // NAME
                // =====================================================

                Label nameLabel = createLabel("Full Name");

                TextField nameField = createField("Enter full name");

                // =====================================================
                // PHONE
                // =====================================================

                Label phoneLabel = createLabel("Phone Number");

                TextField phoneField = createField("Enter phone number");

                // =====================================================
                // EMAIL
                // =====================================================

                Label emailLabel = createLabel("Email");

                TextField emailField = createField("Enter email address");

                // =====================================================
                // FLAT
                // =====================================================

                Label flatLabel = createLabel("Flat / Unit Number");

                TextField flatField = createField("Enter flat number");

                // =====================================================
                // MOVE-IN DATE
                // =====================================================

                Label moveInLabel = createLabel("Move-in Date");

                DatePicker moveInDate = new DatePicker();

                moveInDate.setPromptText(
                                "Select date");

                moveInDate.setPrefWidth(
                                320);

                moveInDate.setPrefHeight(
                                38);

                // =====================================================
                // MONTHLY RENT
                // =====================================================

                Label rentLabel = createLabel("Monthly Rent");

                TextField rentField = createField("Enter monthly rent");

                // =====================================================
                // SECURITY DEPOSIT
                // =====================================================

                Label depositLabel = createLabel("Security Deposit");

                TextField depositField = createField(
                                "Enter security deposit");

                // =====================================================
                // ID PROOF
                // =====================================================

                Label idLabel = createLabel("ID Proof Number");

                TextField idField = createField(
                                "Enter ID proof number");

                // =====================================================
                // EMERGENCY CONTACT
                // =====================================================

                Label emergencyLabel = createLabel("Emergency Contact");

                TextField emergencyField = createField(
                                "Enter emergency contact");

                // =====================================================
                // PASSWORD
                // =====================================================

                Label passwordLabel = createLabel("Password");

                PasswordField passwordField = createPasswordField("Enter tenant password");

                // =====================================================
                // CONFIRM PASSWORD
                // =====================================================

                Label confirmPasswordLabel = createLabel("Confirm Password");

                PasswordField confirmPasswordField = createPasswordField("Confirm tenant password");

                // =====================================================
                // FORM LAYOUT
                // =====================================================

                form.add(
                                nameLabel,
                                0,
                                0);

                form.add(
                                phoneLabel,
                                1,
                                0);

                form.add(
                                nameField,
                                0,
                                1);

                form.add(
                                phoneField,
                                1,
                                1);

                form.add(
                                emailLabel,
                                0,
                                2);

                form.add(
                                flatLabel,
                                1,
                                2);

                form.add(
                                emailField,
                                0,
                                3);

                form.add(
                                flatField,
                                1,
                                3);

                form.add(
                                moveInLabel,
                                0,
                                4);

                form.add(
                                rentLabel,
                                1,
                                4);

                form.add(
                                moveInDate,
                                0,
                                5);

                form.add(
                                rentField,
                                1,
                                5);

                form.add(
                                depositLabel,
                                0,
                                6);

                form.add(
                                idLabel,
                                1,
                                6);

                form.add(
                                depositField,
                                0,
                                7);

                form.add(
                                idField,
                                1,
                                7);

                form.add(
                                emergencyLabel,
                                0,
                                8);

                form.add(
                                passwordLabel,
                                1,
                                8);

                form.add(
                                emergencyField,
                                0,
                                9);

                form.add(
                                passwordField,
                                1,
                                9);

                // =====================================================
                // REMARKS
                // =====================================================

                Label remarksLabel = createLabel("Remarks");

                TextArea remarksArea = new TextArea();

                remarksArea.setPromptText(
                                "Enter any additional information");

                remarksArea.setPrefHeight(
                                55);

                remarksArea.setWrapText(
                                true);

                remarksArea.setStyle(
                                "-fx-background-color:white;" +
                                                "-fx-border-color:#CBD5D8;" +
                                                "-fx-border-radius:6;" +
                                                "-fx-background-radius:6;");

                form.add(
                                confirmPasswordLabel,
                                0,
                                10);

                form.add(
                                confirmPasswordField,
                                0,
                                11);

                form.add(
                                remarksLabel,
                                1,
                                10);

                form.add(
                                remarksArea,
                                1,
                                11);

                // =====================================================
                // BUTTONS
                // =====================================================

                Button clearButton = new Button("Clear");

                Button saveButton = new Button("Save Tenant");

                clearButton.setPrefWidth(100);
                clearButton.setPrefHeight(38);

                saveButton.setPrefWidth(125);
                saveButton.setPrefHeight(38);

                clearButton.setStyle(
                                "-fx-background-color:#ffffff;" +
                                                "-fx-text-fill:#4e342e;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-background-radius:7;");

                saveButton.setStyle(
                                "-fx-background-color:#4e342e;" +
                                                "-fx-text-fill:#ffffff;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-background-radius:7;");

                HBox buttons = new HBox();

                buttons.setSpacing(12);

                buttons.setAlignment(
                                Pos.CENTER_RIGHT);

                buttons.getChildren().addAll(
                                clearButton,
                                saveButton);

                // =====================================================
                // CLEAR BUTTON
                // =====================================================

                clearButton.setOnAction(
                                event -> {

                                        nameField.clear();
                                        phoneField.clear();
                                        emailField.clear();
                                        flatField.clear();

                                        moveInDate.setValue(null);

                                        rentField.clear();
                                        depositField.clear();

                                        idField.clear();
                                        emergencyField.clear();
                                        passwordField.clear();
                                        confirmPasswordField.clear();

                                        remarksArea.clear();
                                });

                // =====================================================
                // SAVE TENANT
                // =====================================================

                saveButton.setOnAction(
                                event -> {

                                        System.out.println(
                                                        "SAVE TENANT BUTTON CLICKED");

                                        try {

                                                // =================================================
                                                // READ FORM VALUES
                                                // =================================================

                                                String tenantName = nameField.getText()
                                                                .trim();

                                                String tenantPhone = phoneField.getText()
                                                                .trim();

                                                String tenantEmail = emailField.getText()
                                                                .trim()
                                                                .toLowerCase();

                                                String tenantPassword = passwordField.getText();

                                                String tenantConfirmPassword = confirmPasswordField.getText();

                                                String tenantFlat = flatField.getText()
                                                                .trim();

                                                String tenantMonthlyRent = rentField.getText()
                                                                .trim();

                                                String tenantSecurityDeposit = depositField.getText()
                                                                .trim();

                                                String tenantIdProof = idField.getText()
                                                                .trim();

                                                String tenantEmergencyContact = emergencyField.getText()
                                                                .trim();

                                                String tenantRemarks = remarksArea.getText()
                                                                .trim();

                                                // =================================================
                                                // MOVE-IN DATE
                                                // =================================================

                                                String tenantMoveInDate = "";

                                                if (moveInDate.getValue() != null) {

                                                        tenantMoveInDate = moveInDate.getValue()
                                                                        .format(
                                                                                        DateTimeFormatter.ofPattern(
                                                                                                        "dd MMMM yyyy"));
                                                }

                                                // =================================================
                                                // VALIDATION
                                                // =================================================

                                                if (tenantName.isEmpty()) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Missing Information",
                                                                        "Please enter tenant name.");

                                                        return;
                                                }

                                                if (tenantPhone.isEmpty()) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Missing Information",
                                                                        "Please enter phone number.");

                                                        return;
                                                }

                                                if (tenantEmail.isEmpty()) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Missing Information",
                                                                        "Please enter tenant email.");

                                                        return;
                                                }

                                                if (!tenantEmail.contains("@") ||
                                                                !tenantEmail.contains(".")) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Invalid Email",
                                                                        "Please enter a valid email address.");

                                                        return;
                                                }

                                                if (tenantPassword == null ||
                                                                tenantPassword.isEmpty()) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Missing Information",
                                                                        "Please enter tenant password.");

                                                        return;
                                                }

                                                if (tenantPassword.length() < 6) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Invalid Password",
                                                                        "Password must contain at least 6 characters.");

                                                        return;
                                                }

                                                if (!tenantPassword.equals(
                                                                tenantConfirmPassword)) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Password Mismatch",
                                                                        "Password and confirm password do not match.");

                                                        return;
                                                }

                                                if (tenantFlat.isEmpty()) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Missing Information",
                                                                        "Please enter flat number.");

                                                        return;
                                                }

                                                if (tenantMoveInDate.isEmpty()) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Missing Information",
                                                                        "Please select move-in date.");

                                                        return;
                                                }

                                                if (tenantMonthlyRent.isEmpty()) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Missing Information",
                                                                        "Please enter monthly rent.");

                                                        return;
                                                }

                                                if (tenantSecurityDeposit.isEmpty()) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Missing Information",
                                                                        "Please enter security deposit.");

                                                        return;
                                                }

                                                if (tenantIdProof.isEmpty()) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Missing Information",
                                                                        "Please enter ID proof number.");

                                                        return;
                                                }

                                                if (tenantEmergencyContact.isEmpty()) {

                                                        showAlert(
                                                                        Alert.AlertType.WARNING,
                                                                        "Missing Information",
                                                                        "Please enter emergency contact.");

                                                        return;
                                                }

                                                // =================================================
                                                // CREATE RESIDENT MODEL
                                                // =================================================

                                                ResidentModel resident = new ResidentModel();

                                                resident.setEmail(
                                                                tenantEmail);

                                                resident.setName(
                                                                tenantName);

                                                resident.setPhone(
                                                                tenantPhone);

                                                resident.setDateOfBirth(
                                                                "");

                                                resident.setGender(
                                                                "");

                                                resident.setFlat(
                                                                tenantFlat);

                                                // =================================================
                                                // SOCIETY FROM LOGGED-IN OWNER
                                                // =================================================

                                                String normalizedOwnerEmail = ownerEmail == null
                                                                ? ""
                                                                : ownerEmail.trim().toLowerCase();

                                                ResidentModel owner = residentController.getOwnerByEmail(
                                                                normalizedOwnerEmail);

                                                if (owner == null ||
                                                                owner.getSociety() == null ||
                                                                owner.getSociety().trim().isEmpty()) {

                                                        showAlert(
                                                                        Alert.AlertType.ERROR,
                                                                        "Society Not Found",
                                                                        "The owner's society could not be found. " +
                                                                                        "Tenant was not saved.");

                                                        return;
                                                }

                                                resident.setSociety(
                                                                owner.getSociety().trim());

                                                // =================================================
                                                // RESIDENT TYPE
                                                // =================================================

                                                resident.setResidentType(
                                                                "Tenant");

                                                // =================================================
                                                // OWNER RELATIONSHIP
                                                // =================================================

                                                resident.setOwnerEmail(
                                                                normalizedOwnerEmail);

                                                resident.setPrimaryResidentEmail(
                                                                "");

                                                // =================================================
                                                // RENTAL INFORMATION
                                                // =================================================

                                                resident.setMoveInDate(
                                                                tenantMoveInDate);

                                                resident.setMoveOutDate(
                                                                "");

                                                resident.setMonthlyRent(
                                                                tenantMonthlyRent);

                                                resident.setSecurityDeposit(
                                                                tenantSecurityDeposit);

                                                // =================================================
                                                // OTHER INFORMATION
                                                // =================================================

                                                resident.setIdProof(
                                                                tenantIdProof);

                                                resident.setEmergencyContact(
                                                                tenantEmergencyContact);

                                                resident.setRemarks(
                                                                tenantRemarks);

                                                resident.setRemovalReason(
                                                                "");

                                                resident.setStatus(
                                                                "Active");

                                                // =================================================
                                                // DEBUG
                                                // =================================================

                                                System.out.println(
                                                                "------------------------------------");

                                                System.out.println(
                                                                "Saving Tenant");

                                                System.out.println(
                                                                "Tenant Email: "
                                                                                + tenantEmail);

                                                System.out.println(
                                                                "Tenant Name: "
                                                                                + tenantName);

                                                System.out.println(
                                                                "Tenant Flat: "
                                                                                + tenantFlat);

                                                System.out.println(
                                                                "Owner Email: "
                                                                                + ownerEmail);

                                                System.out.println(
                                                                "Resident Type: Tenant");

                                                System.out.println(
                                                                "------------------------------------");

                                                // =================================================
                                                // CREATE FIREBASE AUTHENTICATION ACCOUNT
                                                // =================================================

                                                System.out.println(
                                                                "Creating Firebase Authentication account...");

                                                boolean authCreated = userController.createFirebaseAccount(
                                                                tenantEmail,
                                                                tenantPassword);

                                                System.out.println(
                                                                "Firebase Authentication result: "
                                                                                + authCreated);

                                                if (!authCreated) {

                                                        showAlert(
                                                                        Alert.AlertType.ERROR,
                                                                        "Authentication Failed",
                                                                        "Tenant Firebase Authentication account could not be created.\n\n"
                                                                                        + "The tenant was not saved to Firestore.");

                                                        return;
                                                }

                                                // =================================================
                                                // SAVE TO FIRESTORE
                                                // =================================================

                                                boolean success = residentController.addResident(
                                                                resident);

                                                System.out.println(
                                                                "Firestore save result: "
                                                                                + success);

                                                // =================================================
                                                // SUCCESS
                                                // =================================================

                                                if (success) {

                                                        showAlert(
                                                                        Alert.AlertType.INFORMATION,
                                                                        "Tenant Added",
                                                                        "Tenant has been successfully added.\n\n"
                                                                                        + "Firebase login account has also been created.");

                                                        // Clear form

                                                        nameField.clear();

                                                        phoneField.clear();

                                                        emailField.clear();

                                                        flatField.clear();

                                                        moveInDate.setValue(
                                                                        null);

                                                        rentField.clear();

                                                        depositField.clear();

                                                        idField.clear();

                                                        emergencyField.clear();
                                                        passwordField.clear();
                                                        confirmPasswordField.clear();

                                                        remarksArea.clear();

                                                } else {

                                                        showAlert(
                                                                        Alert.AlertType.ERROR,
                                                                        "Save Failed",
                                                                        "Tenant could not be saved to Firestore.");
                                                }

                                        } catch (Exception ex) {

                                                System.out.println(
                                                                "====================================");

                                                System.out.println(
                                                                "ADD TENANT ERROR");

                                                System.out.println(
                                                                "====================================");

                                                ex.printStackTrace();

                                                showAlert(
                                                                Alert.AlertType.ERROR,
                                                                "Error",
                                                                "Error while saving tenant:\n\n"
                                                                                + ex.getMessage());
                                        }
                                });

                // =====================================================
                // ADD FORM TO CARD
                // =====================================================

                formCard.getChildren().addAll(
                                formHeading,
                                form,
                                buttons);

                // =====================================================
                // ADD CONTENT
                // =====================================================

                mainContent.getChildren().addAll(
                                header,
                                formCard);

                // =====================================================
                // MAIN AREA
                // =====================================================

                BorderPane mainArea = new BorderPane();

                mainArea.setCenter(
                                mainContent);

                root.setCenter(
                                mainArea);

                // =====================================================
                // RETURN SCENE
                // =====================================================

                Scene scene = new Scene(
                                root,
                                ScreenSize.getWidth(),
                                ScreenSize.getHeight());

                stage.setScene(
                                scene);

                stage.setMaximized(
                                true);

                return scene;
        }

        // return new Scene(
        // root,
        // 1500,
        // 750
        // );
        // }

        // =========================================================
        // CREATE LABEL
        // =========================================================

        private static Label createLabel(
                        String text) {

                Label label = new Label(text);

                label.setStyle(
                                "-fx-font-size:12px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:#37474F;");

                return label;
        }

        // =========================================================
        // CREATE PASSWORD FIELD
        // =========================================================

        private static PasswordField createPasswordField(
                        String prompt) {

                PasswordField field = new PasswordField();

                field.setPromptText(
                                prompt);

                field.setPrefWidth(
                                320);

                field.setPrefHeight(
                                38);

                field.setStyle(
                                "-fx-background-color:white;" +
                                                "-fx-border-color:#CBD5D8;" +
                                                "-fx-border-radius:6;" +
                                                "-fx-background-radius:6;");

                return field;
        }

        // =========================================================
        // CREATE TEXT FIELD
        // =========================================================

        private static TextField createField(
                        String prompt) {

                TextField field = new TextField();

                field.setPromptText(
                                prompt);

                field.setPrefWidth(
                                320);

                field.setPrefHeight(
                                38);

                field.setStyle(
                                "-fx-background-color:white;" +
                                                "-fx-border-color:#CBD5D8;" +
                                                "-fx-border-radius:6;" +
                                                "-fx-background-radius:6;");

                return field;
        }

        // =========================================================
        // ALERT
        // =========================================================

        private static void showAlert(
                        Alert.AlertType type,
                        String title,
                        String message) {

                Alert alert = new Alert(type);

                alert.setTitle(
                                title);

                alert.setHeaderText(
                                null);

                alert.setContentText(
                                message);

                alert.showAndWait();
        }
}