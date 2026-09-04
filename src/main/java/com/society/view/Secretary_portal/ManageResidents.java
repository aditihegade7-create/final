package com.society.view.Secretary_portal;

import java.util.ArrayList;
import java.util.List;

import com.society.controller.Secretary_Controller.ResidentController;
import com.society.model.Secretary_model.Resident;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ManageResidents {

    // =========================================================
    // SCENE
    // =========================================================

    private Scene Resident;

    // =========================================================
    // CURRENT RESIDENTS
    //
    // This list contains ONLY the logged-in Secretary's
    // society residents.
    // =========================================================

    private List<Resident> currentResidents =
            new ArrayList<>();

    // =========================================================
    // CREATE SCENE
    // =========================================================

    public Scene createScene(javafx.stage.Stage stage) {

        // =====================================================
        // CONTROLLER
        // =====================================================

        ResidentController residentController =
                new ResidentController();

        // =====================================================
        // SIDEBAR
        // =====================================================

        SecretarySidebar sidebarObj =
                new SecretarySidebar();

        VBox sidebar =
                sidebarObj.createSidebar(stage);

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainvb =
                new VBox(10);

        mainvb.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        mainvb.setPadding(
                new Insets(20)
        );

        mainvb.setSpacing(10);

        mainvb.setStyle(
                "-fx-background-color:#b3adad;"
        );

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label("Manage Residents");

        title.setStyle(
                "-fx-font-size:28px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:black;"
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        Label subtitle =
                new Label(
                        "View and manage residents of your society"
                );

        subtitle.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#777777;"
        );

        // =====================================================
        // SEARCH FIELD
        // =====================================================

        TextField search =
                new TextField();

        search.setPromptText(
                "Search resident, flat no., phone, email..."
        );

        search.setPrefHeight(45);

        search.setPrefWidth(750);

        search.setStyle(
                "-fx-background-color:#F8F9FA;" +
                "-fx-border-color:#E1E5E8;" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-font-size:14px;"
        );

        HBox.setHgrow(
                search,
                Priority.ALWAYS
        );

        // =====================================================
        // REFRESH BUTTON
        // =====================================================

        Button refreshBtn =
                new Button("⟳");

        refreshBtn.setPrefWidth(50);

        refreshBtn.setPrefHeight(45);

        refreshBtn.setStyle(
                "-fx-background-color:#434141;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        refreshBtn.setTooltip(
                new Tooltip(
                        "Refresh resident data"
                )
        );

        // =====================================================
        // ADD RESIDENT BUTTON
        // =====================================================

        Button addResidentBtn =
                new Button(
                        "+ Add New Resident"
                );

        addResidentBtn.setPrefWidth(200);

        addResidentBtn.setPrefHeight(45);

        addResidentBtn.setStyle(
                "-fx-background-color:#434141;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        // =====================================================
        // SEARCH BOX
        // =====================================================

        HBox searchBox =
                new HBox(10);

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        searchBox.getChildren().addAll(
                search,
                refreshBtn,
                addResidentBtn
        );

        // =====================================================
        // RESIDENT LIST
        // =====================================================

        VBox residentList =
                new VBox(20);

        residentList.setPadding(
                new Insets(20, 0, 20, 0)
        );

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                residentList
        );

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color:transparent;"
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        // =====================================================
        // INITIAL LOAD
        //
        // IMPORTANT:
        // getResidentsBySociety()
        //
        // NOT getAllResidents()
        // =====================================================

        loadResidents(
                residentController,
                residentList
        );

        // =====================================================
        // REFRESH
        // =====================================================

        refreshBtn.setOnAction(e -> {

            search.clear();

            loadResidents(
                    residentController,
                    residentList
            );
        });

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        mainvb.getChildren().addAll(
                title,
                subtitle,
                searchBox,
                scrollPane
        );

        // =====================================================
        // ROOT
        // =====================================================

        HBox root =
                new HBox();

        root.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        root.getChildren().addAll(
                sidebar,
                mainvb
        );

        root.setStyle(
                "-fx-background-color:#434141;"
        );

        HBox.setHgrow(
                mainvb,
                Priority.ALWAYS
        );

        // =====================================================
        // STACK PANE
        // =====================================================

        StackPane stackPane =
                new StackPane();

        stackPane.getChildren().add(
                root
        );

        // =====================================================
        // POPUP
        // =====================================================

        VBox popup =
                new VBox(12);

        popup.setPadding(
                new Insets(25)
        );

        popup.setAlignment(
                Pos.TOP_LEFT
        );

        popup.setPrefWidth(430);

        popup.setMaxWidth(430);

        popup.setPrefHeight(520);

        popup.setMaxHeight(520);

        popup.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:15;" +
                "-fx-effect:dropshadow(" +
                "gaussian," +
                "rgba(0,0,0,0.30)," +
                "20," +
                "0.2," +
                "0," +
                "5);"
        );

        // =====================================================
        // POPUP TITLE
        // =====================================================

        Label popupTitle =
                new Label(
                        "Add New Resident"
                );

        popupTitle.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#123C36;"
        );

        // =====================================================
        // NAME
        // =====================================================

        Label nameLabel =
                new Label(
                        "Resident Name"
                );

        nameLabel.setStyle(
                "-fx-font-weight:bold;"
        );

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Enter resident name"
        );

        nameField.setPrefHeight(40);

        // =====================================================
        // FLAT
        // =====================================================

        Label flatLabel =
                new Label(
                        "Flat Number"
                );

        flatLabel.setStyle(
                "-fx-font-weight:bold;"
        );

        TextField flatField =
                new TextField();

        flatField.setPromptText(
                "Enter flat number"
        );

        flatField.setPrefHeight(40);

        // =====================================================
        // MOBILE
        // =====================================================

        Label mobileLabel =
                new Label(
                        "Mobile Number"
                );

        mobileLabel.setStyle(
                "-fx-font-weight:bold;"
        );

        TextField mobileField =
                new TextField();

        mobileField.setPromptText(
                "Enter mobile number"
        );

        mobileField.setPrefHeight(40);

        // =====================================================
        // EMAIL
        // =====================================================

        Label emailLabel =
                new Label(
                        "Email"
                );

        emailLabel.setStyle(
                "-fx-font-weight:bold;"
        );

        TextField emailField =
                new TextField();

        emailField.setPromptText(
                "Enter email"
        );

        emailField.setPrefHeight(40);

        // =====================================================
        // STATUS
        // =====================================================

        Label statusLabel =
                new Label(
                        "Status"
                );

        statusLabel.setStyle(
                "-fx-font-weight:bold;"
        );

        TextField statusField =
                new TextField();

        statusField.setPromptText(
                "Enter status (Owner / Tenant)"
        );

        statusField.setPrefHeight(40);

        // =====================================================
        // CANCEL BUTTON
        // =====================================================

        Button cancelBtn =
                new Button(
                        "Cancel"
                );

        cancelBtn.setPrefWidth(100);

        cancelBtn.setPrefHeight(40);

        cancelBtn.setStyle(
                "-fx-background-color:#E5E7EB;" +
                "-fx-text-fill:#333333;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        // =====================================================
        // SAVE BUTTON
        // =====================================================

        Button saveBtn =
                new Button(
                        "Save Resident"
                );

        saveBtn.setPrefWidth(140);

        saveBtn.setPrefHeight(40);

        saveBtn.setStyle(
                "-fx-background-color:#2E9D63;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        // =====================================================
        // BUTTON BOX
        // =====================================================

        HBox buttonBox =
                new HBox(10);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.setPadding(
                new Insets(10, 0, 0, 0)
        );

        buttonBox.getChildren().addAll(
                cancelBtn,
                saveBtn
        );

        // =====================================================
        // POPUP CONTENT
        // =====================================================

        popup.getChildren().addAll(

                popupTitle,

                nameLabel,
                nameField,

                flatLabel,
                flatField,

                mobileLabel,
                mobileField,

                emailLabel,
                emailField,

                statusLabel,
                statusField,

                buttonBox
        );

        // =====================================================
        // OVERLAY
        // =====================================================

        VBox overlay =
                new VBox();

        overlay.setStyle(
                "-fx-background-color:" +
                "rgba(0,0,0,0.35);"
        );

        // =====================================================
        // POPUP CONTAINER
        // =====================================================

        StackPane popupContainer =
                new StackPane();

        popupContainer.setAlignment(
                Pos.CENTER
        );

        popupContainer.getChildren().add(
                popup
        );

        // =====================================================
        // POPUP LAYER
        // =====================================================

        StackPane popupLayer =
                new StackPane();

        popupLayer.setPickOnBounds(true);

        popupLayer.getChildren().addAll(
                overlay,
                popupContainer
        );

        popupLayer.setVisible(false);

        stackPane.getChildren().add(
                popupLayer
        );

        // =====================================================
        // ADD RESIDENT
        // =====================================================

        addResidentBtn.setOnAction(e -> {

            popupLayer.setVisible(true);

            nameField.requestFocus();
        });

        // =====================================================
        // CANCEL
        // =====================================================

        cancelBtn.setOnAction(e -> {

            popupLayer.setVisible(false);

            clearFields(
                    nameField,
                    flatField,
                    mobileField,
                    emailField,
                    statusField
            );
        });

        // =====================================================
        // SAVE RESIDENT
        // =====================================================

        saveBtn.setOnAction(e -> {

            String name =
                    cleanValue(
                            nameField.getText()
                    );

            String flat =
                    cleanValue(
                            flatField.getText()
                    );

            String mobile =
                    cleanValue(
                            mobileField.getText()
                    );

            String email =
                    cleanEmail(
                            emailField.getText()
                    );

            String status =
                    cleanValue(
                            statusField.getText()
                    );

            // =================================================
            // VALIDATION
            // =================================================

            if (name.isEmpty()
                    || flat.isEmpty()
                    || mobile.isEmpty()
                    || email.isEmpty()
                    || status.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation Error",
                        "Please fill all fields."
                );

                return;
            }

            // =================================================
            // EMAIL VALIDATION
            // =================================================

            if (!isValidEmail(email)) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Email",
                        "Please enter a valid email address."
                );

                return;
            }

            // =================================================
            // CHECK SECRETARY SOCIETY
            //
            // This is an extra safety check before saving.
            // =================================================

            String secretarySociety =
                    residentController
                            .getLoggedInSecretarySociety();

            if (secretarySociety == null
                    || secretarySociety.trim().isEmpty()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Society Error",
                        "Unable to identify your society."
                );

                return;
            }

            // =================================================
            // SAVE THROUGH CONTROLLER
            //
            // Controller automatically assigns society.
            // =================================================

            boolean success =
                    residentController.addResident(
                            name,
                            flat,
                            mobile,
                            email,
                            status
                    );

            // =================================================
            // SUCCESS
            // =================================================

            if (success) {

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Resident saved successfully!"
                                + "\n\nSociety: "
                                + secretarySociety
                );

                popupLayer.setVisible(false);

                clearFields(
                        nameField,
                        flatField,
                        mobileField,
                        emailField,
                        statusField
                );

                // =================================================
                // RELOAD ONLY CURRENT SECRETARY SOCIETY
                // =================================================

                loadResidents(
                        residentController,
                        residentList
                );

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed to save resident."
                );
            }
        });

        // =====================================================
        // SEARCH
        //
        // IMPORTANT:
        // Search currentResidents only.
        //
        // currentResidents already contains ONLY the
        // logged-in Secretary's society residents.
        // =====================================================

        search.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    String searchText =
                            newValue == null
                                    ? ""
                                    : newValue
                                            .toLowerCase()
                                            .trim();

                    displayResidents(
                            currentResidents,
                            searchText,
                            residentList
                    );
                }
        );

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        stackPane,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        Resident = scene;

        return Resident;
    }

    // =========================================================
    // LOAD RESIDENTS
    //
    // VERY IMPORTANT:
    //
    // getResidentsBySociety()
    //
    // NOT:
    //
    // getAllResidents()
    // =========================================================

    private void loadResidents(
            ResidentController residentController,
            VBox residentList
    ) {

        // =====================================================
        // CLEAR OLD UI
        // =====================================================

        residentList
                .getChildren()
                .clear();

        // =====================================================
        // CLEAR OLD LIST
        // =====================================================

        currentResidents.clear();

        // =====================================================
        // GET ONLY LOGGED-IN SECRETARY SOCIETY RESIDENTS
        // =====================================================

        List<Resident> residents =
                residentController
                        .getResidentsBySociety();

        // =====================================================
        // NULL CHECK
        // =====================================================

        if (residents == null) {

            residents =
                    new ArrayList<>();
        }

        // =====================================================
        // EXTRA UI-SIDE SAFETY FILTER
        //
        // Even if DAO somehow returns wrong society,
        // we verify it again here.
        // =====================================================

        String secretarySociety =
                residentController
                        .getLoggedInSecretarySociety();

        if (secretarySociety == null
                || secretarySociety.trim().isEmpty()) {

            Label errorLabel =
                    new Label(
                            "Unable to identify your society."
                    );

            errorLabel.setStyle(
                    "-fx-font-size:16px;" +
                    "-fx-text-fill:#D32F2F;"
            );

            residentList
                    .getChildren()
                    .add(
                            errorLabel
                    );

            return;
        }

        secretarySociety =
                secretarySociety.trim();

        // =====================================================
        // FINAL SOCIETY FILTER
        // =====================================================

        for (Resident resident : residents) {

            if (resident == null) {

                continue;
            }

            String residentSociety =
                    resident.getSociety();

            if (residentSociety == null
                    || residentSociety.trim().isEmpty()) {

                System.out.println(
                        "UI BLOCKED: Society missing for "
                                + resident.getEmail()
                );

                continue;
            }

            residentSociety =
                    residentSociety.trim();

            // =================================================
            // EXACT SOCIETY CHECK
            // =================================================

            if (!residentSociety.equalsIgnoreCase(
                    secretarySociety
            )) {

                System.out.println(
                        "UI BLOCKED: "
                                + resident.getEmail()
                                + " | Society = "
                                + residentSociety
                                + " | Required = "
                                + secretarySociety
                );

                continue;
            }

            // =================================================
            // ADD TO CURRENT LIST
            // =================================================

            currentResidents.add(
                    resident
            );
        }

        // =====================================================
        // DISPLAY
        // =====================================================

        displayResidents(
                currentResidents,
                "",
                residentList
        );

        // =====================================================
        // DEBUG
        // =====================================================

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "MANAGE RESIDENTS UI"
        );

        System.out.println(
                "Secretary Society = ["
                        + secretarySociety
                        + "]"
        );

        System.out.println(
                "Residents Loaded = "
                        + currentResidents.size()
        );

        System.out.println(
                "=========================================="
        );
    }

    // =========================================================
    // DISPLAY RESIDENTS
    //
    // Used for both normal display and search.
    // =========================================================

    private void displayResidents(
            List<Resident> residents,
            String searchText,
            VBox residentList
    ) {

        // =====================================================
        // CLEAR UI
        // =====================================================

        residentList
                .getChildren()
                .clear();

        // =====================================================
        // NULL / EMPTY
        // =====================================================

        if (residents == null
                || residents.isEmpty()) {

            Label emptyLabel =
                    new Label(
                            searchText.isEmpty()
                                    ? "No residents found."
                                    : "No matching residents found."
                    );

            emptyLabel.setStyle(
                    "-fx-font-size:16px;" +
                    "-fx-text-fill:#555555;"
            );

            residentList
                    .getChildren()
                    .add(
                            emptyLabel
                    );

            return;
        }

        // =====================================================
        // NORMALIZE SEARCH
        // =====================================================

        String query =
                searchText == null
                        ? ""
                        : searchText
                                .toLowerCase()
                                .trim();

        int matchCount = 0;

        // =====================================================
        // LOOP
        // =====================================================

        for (Resident resident : residents) {

            if (resident == null) {

                continue;
            }

            // =================================================
            // GET VALUES
            // =================================================

            String name =
                    safeLower(
                            resident.getName()
                    );

            String flat =
                    safeLower(
                            resident.getFlat()
                    );

            String mobile =
                    safeLower(
                            resident.getMobile()
                    );

            String email =
                    safeLower(
                            resident.getEmail()
                    );

            String status =
                    safeLower(
                            resident.getStatus()
                    );

            String society =
                    safeLower(
                            resident.getSociety()
                    );

            // =================================================
            // SEARCH
            //
            // Society is also included in search, but because
            // currentResidents is already society-filtered,
            // another society cannot appear.
            // =================================================

            boolean matches =
                    query.isEmpty()
                            || name.contains(query)
                            || flat.contains(query)
                            || mobile.contains(query)
                            || email.contains(query)
                            || status.contains(query)
                            || society.contains(query);

            // =================================================
            // ADD MATCH
            // =================================================

            if (matches) {

                HBox row =
                        createResidentRow(
                                resident.getName(),
                                resident.getFlat(),
                                resident.getMobile(),
                                resident.getStatus()
                        );

                residentList
                        .getChildren()
                        .add(
                                row
                        );

                matchCount++;
            }
        }

        // =====================================================
        // NO SEARCH RESULT
        // =====================================================

        if (matchCount == 0) {

            Label noResult =
                    new Label(
                            "No matching residents found."
                    );

            noResult.setStyle(
                    "-fx-font-size:16px;" +
                    "-fx-text-fill:#555555;"
            );

            residentList
                    .getChildren()
                    .add(
                            noResult
                    );
        }
    }

    // =========================================================
    // SAFE LOWERCASE
    // =========================================================

    private String safeLower(
            String value
    ) {

        if (value == null) {

            return "";
        }

        return value
                .trim()
                .toLowerCase();
    }

    // =========================================================
    // CLEAR FIELDS
    // =========================================================

    private void clearFields(
            TextField nameField,
            TextField flatField,
            TextField mobileField,
            TextField emailField,
            TextField statusField
    ) {

        nameField.clear();

        flatField.clear();

        mobileField.clear();

        emailField.clear();

        statusField.clear();
    }

    // =========================================================
    // CLEAN VALUE
    // =========================================================

    private String cleanValue(
            String value
    ) {

        if (value == null) {

            return "";
        }

        return value.trim();
    }

    // =========================================================
    // CLEAN EMAIL
    // =========================================================

    private String cleanEmail(
            String value
    ) {

        if (value == null) {

            return "";
        }

        return value
                .trim()
                .toLowerCase();
    }

    // =========================================================
    // EMAIL VALIDATION
    // =========================================================

    private boolean isValidEmail(
            String email
    ) {

        return email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );
    }

    // =========================================================
    // ALERT
    // =========================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert =
                new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    // =========================================================
    // CREATE RESIDENT ROW
    // =========================================================

    private HBox createResidentRow(
            String residentName,
            String flatNumber,
            String mobile,
            String statusText
    ) {

        HBox residentRow =
                new HBox(5);

        residentRow.setPrefWidth(1000);

        residentRow.setMaxWidth(1000);

        residentRow.setPrefHeight(70);

        residentRow.setAlignment(
                Pos.CENTER_LEFT
        );

        residentRow.setPadding(
                new Insets(20)
        );

        residentRow.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;"
        );

        // =====================================================
        // PROFILE
        // =====================================================

        Label profile =
                new Label("👤");

        profile.setPrefWidth(50);

        profile.setPrefHeight(50);

        profile.setAlignment(
                Pos.CENTER
        );

        profile.setStyle(
                "-fx-background-color:#E5E7EB;" +
                "-fx-background-radius:50%;" +
                "-fx-font-size:22px;"
        );

        // =====================================================
        // NAME
        // =====================================================

        Label name =
                new Label(
                        residentName == null
                                ? ""
                                : residentName
                );

        name.setPrefWidth(200);

        name.setStyle(
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#123C36;"
        );

        // =====================================================
        // FLAT
        // =====================================================

        Label flat =
                new Label(
                        "Flat: "
                                +
                                (
                                    flatNumber == null
                                            ? ""
                                            : flatNumber
                                )
                );

        flat.setPrefWidth(150);

        flat.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#555555;"
        );

        // =====================================================
        // MOBILE
        // =====================================================

        Label mobileLabel =
                new Label(
                        "Mobile: "
                                +
                                (
                                    mobile == null
                                            ? ""
                                            : mobile
                                )
                );

        mobileLabel.setPrefWidth(220);

        mobileLabel.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#555555;"
        );

        // =====================================================
        // STATUS
        // =====================================================

        Label status =
                new Label(
                        statusText == null
                                ? ""
                                : statusText
                );

        status.setPrefWidth(100);

        status.setStyle(
                "-fx-text-fill:#2E9D63;" +
                "-fx-font-weight:bold;"
        );

        // =====================================================
        // ADD CHILDREN
        // =====================================================

        residentRow.getChildren().addAll(
                profile,
                name,
                flat,
                mobileLabel,
                status
        );

        return residentRow;
    }
}