package com.society.view.Secretary_portal;

import java.util.List;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.dao.Secretary_dao.GuardDao;
import com.society.dao.Welcome.UserDao;
import com.society.controller.Secretary_Controller.GuardController;
import com.society.model.Secretary_model.Guard;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ManageGuard {

    // =====================================================
    // SCENE
    // =====================================================

    private Scene guard;

    // =====================================================
    // ROOT
    // =====================================================

    private StackPane rootStack;

    // =====================================================
    // CONTROLLER
    // =====================================================

    private GuardController guardController;

    // =====================================================
    // GUARD LIST
    // =====================================================

    private VBox guardList;

    // =====================================================
    // SEARCH
    // =====================================================

    private TextField search;

    // =====================================================
    // CURRENT SOCIETY
    // =====================================================

    private String currentSociety = "";

    // =====================================================
    // FIRESTORE
    // =====================================================

    private final Firestore db =
            FirebaseConfig.getFirestore();

    // =====================================================
    // SECRETARY COLLECTION
    // =====================================================

    private static final String SECRETARY_COLLECTION =
            "Secretaries";

    // =====================================================
    // CREATE SCENE
    // =====================================================

    public Scene createScene(Stage stage) {

        // =====================================================
        // CONTROLLER
        // =====================================================

        guardController =
                new GuardController();

        // =====================================================
        // GET CURRENT SECRETARY SOCIETY
        // =====================================================

        currentSociety =
                getCurrentSociety();

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "MANAGE GUARD"
        );

        System.out.println(
                "Current Society : "
                        + currentSociety
        );

        System.out.println(
                "=========================================="
        );

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
                new Label(
                        "Manage Guards"
                );

        title.setStyle(
                "-fx-font-size:28px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:black;"
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        Label subtitle =
                new Label(
                        "View and manage guards of your society"
                );

        subtitle.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-text-fill:#777777;"
        );

        // =====================================================
        // SEARCH
        // =====================================================

        search =
                new TextField();

        search.setPromptText(
                "Search guard, mobile, shift..."
        );

        search.setPrefHeight(45);

        search.setPrefWidth(600);

        search.setMaxWidth(
                Double.MAX_VALUE
        );

        search.setStyle(
                "-fx-background-color:#F8F9FA;"
                        + "-fx-border-color:#E1E5E8;"
                        + "-fx-border-radius:8;"
                        + "-fx-background-radius:8;"
                        + "-fx-font-size:14px;"
        );

        HBox.setHgrow(
                search,
                Priority.ALWAYS
        );

        // =====================================================
        // ADD GUARD BUTTON
        // =====================================================

        Button addGuardBtn =
                new Button(
                        "+ Add New Guard"
                );

        addGuardBtn.setPrefWidth(180);

        addGuardBtn.setPrefHeight(45);

        addGuardBtn.setStyle(
                "-fx-background-color:#434141;"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:8;"
                        + "-fx-cursor:hand;"
        );

        // =====================================================
        // REFRESH BUTTON
        // =====================================================

        Button refreshBtn =
                new Button("⟳");

        refreshBtn.setPrefWidth(45);

        refreshBtn.setPrefHeight(45);

        refreshBtn.setMinWidth(45);

        refreshBtn.setMinHeight(45);

        refreshBtn.setMaxWidth(45);

        refreshBtn.setMaxHeight(45);

        refreshBtn.setStyle(
                "-fx-background-color:#56342B;"
                        + "-fx-text-fill:white;"
                        + "-fx-font-size:24px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:8;"
                        + "-fx-cursor:hand;"
                        + "-fx-padding:0;"
        );

        Tooltip refreshTooltip =
                new Tooltip(
                        "Refresh"
                );

        refreshBtn.setTooltip(
                refreshTooltip
        );

        // =====================================================
        // SEARCH BOX
        // =====================================================

        HBox searchBox =
                new HBox(12);

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        searchBox.setMaxWidth(
                Double.MAX_VALUE
        );

        searchBox.getChildren().addAll(
                search,
                addGuardBtn,
                refreshBtn
        );

        // =====================================================
        // GUARD LIST
        // =====================================================

        guardList =
                new VBox(15);

        guardList.setPadding(
                new Insets(
                        15,
                        0,
                        20,
                        0
                )
        );

        guardList.setFillWidth(true);

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                guardList
        );

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color:transparent;"
                        + "-fx-border-color:transparent;"
        );

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        // =====================================================
        // MAIN ROOT
        // =====================================================

        HBox mainRoot =
                new HBox();

        mainRoot.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        mainRoot.getChildren().addAll(
                sidebar,
                mainvb
        );

        mainRoot.setStyle(
                "-fx-background-color:#434141;"
        );

        HBox.setHgrow(
                mainvb,
                Priority.ALWAYS
        );

        // =====================================================
        // ROOT STACKPANE
        // =====================================================

        rootStack =
                new StackPane();

        rootStack.getChildren().add(
                mainRoot
        );

        // =====================================================
        // ADD GUARD
        // =====================================================

        addGuardBtn.setOnAction(
                e -> openAddGuardDialog()
        );

        // =====================================================
        // REFRESH
        // =====================================================

        refreshBtn.setOnAction(e -> {

            search.clear();

            // Get latest society
            currentSociety =
                    getCurrentSociety();

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "REFRESHING GUARDS"
            );

            System.out.println(
                    "Current Society : "
                            + currentSociety
            );

            System.out.println(
                    "=========================================="
            );

            loadGuards();
        });

        // =====================================================
        // SEARCH
        // =====================================================

        search.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    filterGuards(
                            newValue
                    );
                }
        );

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
        // LOAD CURRENT SOCIETY GUARDS
        // =====================================================

        loadGuards();

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        rootStack,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        guard = scene;

        return guard;
    }

    // =====================================================
    // GET CURRENT SOCIETY
    // =====================================================
    //
    // LOGIN EMAIL
    //      ↓
    // UserDao.getLoggedInEmail()
    //      ↓
    // Secretaries collection
    //      ↓
    // email field
    //      ↓
    // society field
    //
    // =====================================================

    private String getCurrentSociety() {

        try {

            // =================================================
            // GET LOGGED-IN EMAIL
            // =================================================

            String loggedInEmail =
                    UserDao.getLoggedInEmail();

            if (loggedInEmail == null
                    || loggedInEmail.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Logged-in email is not available."
                );

                return "";
            }

            loggedInEmail =
                    loggedInEmail
                            .trim()
                            .toLowerCase();

            System.out.println(
                    "Logged-in Secretary Email : "
                            + loggedInEmail
            );

            // =================================================
            // QUERY SECRETARIES
            // =================================================

            QuerySnapshot snapshot =
                    db.collection(
                            SECRETARY_COLLECTION
                    )
                            .whereEqualTo(
                                    "email",
                                    loggedInEmail
                            )
                            .limit(1)
                            .get()
                            .get();

            // =================================================
            // CHECK RESULT
            // =================================================

            if (snapshot.isEmpty()) {

                System.out.println(
                        "ERROR: Secretary not found."
                );

                System.out.println(
                        "Email : "
                                + loggedInEmail
                );

                return "";
            }

            // =================================================
            // GET SECRETARY DOCUMENT
            // =================================================

            DocumentSnapshot document =
                    snapshot
                            .getDocuments()
                            .get(0);

            // =================================================
            // GET SOCIETY
            // =================================================

            String society =
                    document.getString(
                            "society"
                    );

            if (society == null
                    || society.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Secretary society is empty."
                );

                System.out.println(
                        "Secretary Email : "
                                + loggedInEmail
                );

                return "";
            }

            society =
                    society.trim();

            // =================================================
            // DEBUG
            // =================================================

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "SECRETARY FOUND"
            );

            System.out.println(
                    "Email   : "
                            + loggedInEmail
            );

            System.out.println(
                    "Society : "
                            + society
            );

            System.out.println(
                    "=========================================="
            );

            return society;

        } catch (Exception e) {

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "ERROR: getCurrentSociety()"
            );

            System.out.println(
                    "=========================================="
            );

            e.printStackTrace();

            return "";
        }
    }

    // =====================================================
    // LOAD GUARDS
    // =====================================================

    private void loadGuards() {

        guardList.getChildren().clear();

        // =====================================================
        // SOCIETY VALIDATION
        // =====================================================

        if (currentSociety == null
                || currentSociety.trim().isEmpty()) {

            Label errorLabel =
                    new Label(
                            "Society information not found."
                    );

            errorLabel.setStyle(
                    "-fx-font-size:16px;"
                            + "-fx-text-fill:#b00020;"
            );

            guardList.getChildren().add(
                    errorLabel
            );

            System.out.println(
                    "ERROR: Current society is not available."
            );

            return;
        }

        // =====================================================
        // FETCH ONLY CURRENT SOCIETY
        // =====================================================

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "LOADING GUARDS"
        );

        System.out.println(
                "Society : "
                        + currentSociety
        );

        System.out.println(
                "=========================================="
        );

        List<Guard> guards =
                guardController.getGuardsBySociety(
                        currentSociety
                );

        // =====================================================
        // EMPTY
        // =====================================================

        if (guards == null
                || guards.isEmpty()) {

            Label emptyLabel =
                    new Label(
                            "No guards found for "
                                    + currentSociety
                    );

            emptyLabel.setStyle(
                    "-fx-font-size:16px;"
                            + "-fx-text-fill:#555555;"
            );

            guardList.getChildren().add(
                    emptyLabel
            );

            return;
        }

        // =====================================================
        // DISPLAY
        // =====================================================

        for (Guard guard : guards) {

            if (guard == null) {
                continue;
            }

            guardList.getChildren().add(
                    createGuardRow(
                            guard
                    )
            );
        }
    }

    // =====================================================
    // FILTER GUARDS
    // =====================================================

    private void filterGuards(
            String searchText) {

        guardList.getChildren().clear();

        String text =
                searchText == null
                        ? ""
                        : searchText
                                .toLowerCase()
                                .trim();

        // =====================================================
        // SOCIETY VALIDATION
        // =====================================================

        if (currentSociety == null
                || currentSociety.trim().isEmpty()) {

            Label label =
                    new Label(
                            "Society information not found."
                    );

            label.setStyle(
                    "-fx-font-size:16px;"
                            + "-fx-text-fill:#b00020;"
            );

            guardList.getChildren().add(
                    label
            );

            return;
        }

        // =====================================================
        // FETCH ONLY CURRENT SOCIETY
        // =====================================================

        List<Guard> guards =
                guardController.getGuardsBySociety(
                        currentSociety
                );

        boolean found = false;

        if (guards != null) {

            for (Guard guard : guards) {

                if (guard == null) {
                    continue;
                }

                String name =
                        safe(
                                guard.getName()
                        )
                                .toLowerCase();

                String mobile =
                        safe(
                                guard.getMobile()
                        )
                                .toLowerCase();

                String shift =
                        safe(
                                guard.getShift()
                        )
                                .toLowerCase();

                String status =
                        safe(
                                guard.getStatus()
                        )
                                .toLowerCase();

                String gate =
                        safe(
                                guard.getAssignedGate()
                        )
                                .toLowerCase();

                String email =
                        safe(
                                guard.getEmail()
                        )
                                .toLowerCase();

                if (name.contains(text)
                        || mobile.contains(text)
                        || shift.contains(text)
                        || status.contains(text)
                        || gate.contains(text)
                        || email.contains(text)) {

                    guardList.getChildren().add(
                            createGuardRow(
                                    guard
                            )
                    );

                    found = true;
                }
            }
        }

        // =====================================================
        // NO RESULT
        // =====================================================

        if (!found) {

            Label emptyLabel =
                    new Label(
                            "No matching guards found."
                    );

            emptyLabel.setStyle(
                    "-fx-font-size:16px;"
                            + "-fx-text-fill:#555555;"
            );

            guardList.getChildren().add(
                    emptyLabel
            );
        }
    }

    // =====================================================
    // CREATE GUARD ROW
    // =====================================================

    private HBox createGuardRow(
            Guard guard) {

        HBox guardRow =
                new HBox(10);

        guardRow.setPrefHeight(85);

        guardRow.setMaxWidth(
                Double.MAX_VALUE
        );

        guardRow.setAlignment(
                Pos.CENTER_LEFT
        );

        guardRow.setPadding(
                new Insets(15)
        );

        guardRow.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:10;"
        );

        // =====================================================
        // PROFILE
        // =====================================================

        Label profile =
                new Label("👤");

        profile.setPrefWidth(45);

        profile.setPrefHeight(45);

        profile.setAlignment(
                Pos.CENTER
        );

        profile.setStyle(
                "-fx-background-color:#E5E7EB;"
                        + "-fx-background-radius:50%;"
                        + "-fx-font-size:21px;"
        );

        // =====================================================
        // NAME
        // =====================================================

        Label name =
                new Label(
                        safe(
                                guard.getName()
                        )
                );

        name.setPrefWidth(150);

        name.setStyle(
                "-fx-font-size:15px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#123C36;"
        );

        // =====================================================
        // MOBILE
        // =====================================================

        Label mobile =
                new Label(
                        "Mobile: "
                                + safe(
                                        guard.getMobile()
                                )
                );

        mobile.setPrefWidth(155);

        mobile.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-text-fill:#555555;"
        );

        // =====================================================
        // SHIFT
        // =====================================================

        Label shiftLabel =
                new Label("Shift");

        shiftLabel.setStyle(
                "-fx-font-size:11px;"
                        + "-fx-text-fill:#777777;"
        );

        ComboBox<String> shiftCombo =
                new ComboBox<>();

        shiftCombo.getItems().addAll(
                "Morning",
                "Evening",
                "Night"
        );

        String currentShift =
                safe(
                        guard.getShift()
                );

        if (currentShift.isEmpty()) {
            currentShift = "Morning";
        }

        if (!shiftCombo.getItems()
                .contains(currentShift)) {

            shiftCombo.getItems()
                    .add(currentShift);
        }

        shiftCombo.setValue(
                currentShift
        );

        shiftCombo.setPrefWidth(125);

        shiftCombo.setPrefHeight(35);

        VBox shiftBox =
                new VBox(2);

        shiftBox.setAlignment(
                Pos.CENTER_LEFT
        );

        shiftBox.getChildren().addAll(
                shiftLabel,
                shiftCombo
        );

        // =====================================================
        // STATUS
        // =====================================================

        Label statusLabel =
                new Label("Status");

        statusLabel.setStyle(
                "-fx-font-size:11px;"
                        + "-fx-text-fill:#777777;"
        );

        ComboBox<String> statusCombo =
                new ComboBox<>();

        statusCombo.getItems().addAll(
                "Active",
                "Inactive"
        );

        String currentStatus =
                safe(
                        guard.getStatus()
                );

        if ("Inactive".equalsIgnoreCase(
                currentStatus)) {

            statusCombo.setValue(
                    "Inactive"
            );

        } else {

            statusCombo.setValue(
                    "Active"
            );
        }

        statusCombo.setPrefWidth(105);

        statusCombo.setPrefHeight(35);

        VBox statusBox =
                new VBox(2);

        statusBox.setAlignment(
                Pos.CENTER_LEFT
        );

        statusBox.getChildren().addAll(
                statusLabel,
                statusCombo
        );

        // =====================================================
        // GATE
        // =====================================================

        Label gateLabel =
                new Label(
                        "Assigned Gate"
                );

        gateLabel.setStyle(
                "-fx-font-size:11px;"
                        + "-fx-text-fill:#777777;"
        );

        ComboBox<String> gateCombo =
                new ComboBox<>();

        gateCombo.getItems().addAll(
                "Main Gate",
                "Back Gate"
        );

        String currentGate =
                safe(
                        guard.getAssignedGate()
                );

        if ("Back Gate".equalsIgnoreCase(
                currentGate)) {

            gateCombo.setValue(
                    "Back Gate"
            );

        } else {

            gateCombo.setValue(
                    "Main Gate"
            );
        }

        gateCombo.setPrefWidth(120);

        gateCombo.setPrefHeight(35);

        VBox gateBox =
                new VBox(2);

        gateBox.setAlignment(
                Pos.CENTER_LEFT
        );

        gateBox.getChildren().addAll(
                gateLabel,
                gateCombo
        );

        // =====================================================
        // SHIFT CHANGE
        // =====================================================

        shiftCombo.setOnAction(e -> {

            String newShift =
                    shiftCombo.getValue();

            if (newShift == null) {
                return;
            }

            updateGuard(
                    guard,
                    newShift,
                    statusCombo.getValue(),
                    gateCombo.getValue()
            );
        });

        // =====================================================
        // STATUS CHANGE
        // =====================================================

        statusCombo.setOnAction(e -> {

            String newStatus =
                    statusCombo.getValue();

            if (newStatus == null) {
                return;
            }

            updateGuard(
                    guard,
                    shiftCombo.getValue(),
                    newStatus,
                    gateCombo.getValue()
            );
        });

        // =====================================================
        // GATE CHANGE
        // =====================================================

        gateCombo.setOnAction(e -> {

            String newGate =
                    gateCombo.getValue();

            if (newGate == null) {
                return;
            }

            updateGuard(
                    guard,
                    shiftCombo.getValue(),
                    statusCombo.getValue(),
                    newGate
            );
        });

        // =====================================================
        // ADD TO ROW
        // =====================================================

        guardRow.getChildren().addAll(

                profile,

                name,

                mobile,

                shiftBox,

                statusBox,

                gateBox
        );

        return guardRow;
    }

    // =====================================================
    // UPDATE GUARD
    // =====================================================

    private void updateGuard(
            Guard guard,
            String shift,
            String status,
            String gate) {

        try {

            if (guard == null) {
                return;
            }

            // =================================================
            // IMPORTANT
            // USE FIRESTORE DOCUMENT ID
            // NOT EMAIL
            // =================================================

            String guardId =
                    safe(
                            guard.getId()
                    ).trim();

            if (guardId.isEmpty()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Update Failed",
                        "Guard document ID is missing."
                );

                return;
            }

            // =================================================
            // VALIDATION
            // =================================================

            if (shift == null
                    || shift.trim().isEmpty()
                    || status == null
                    || status.trim().isEmpty()
                    || gate == null
                    || gate.trim().isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation Error",
                        "Guard update fields are required."
                );

                return;
            }

            // =================================================
            // UPDATE
            // =================================================

            boolean updated =
                    guardController.updateGuard(
                            guardId,
                            shift,
                            status,
                            gate
                    );

            // =================================================
            // SUCCESS
            // =================================================

            if (updated) {

                guard.setShift(
                        shift
                );

                guard.setStatus(
                        status
                );

                guard.setAssignedGate(
                        gate
                );

                System.out.println(
                        "Guard updated successfully."
                );

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Update Failed",
                        "Guard details could not be updated."
                );

                loadGuards();
            }

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Something went wrong while updating guard."
            );

            loadGuards();
        }
    }

    // =====================================================
    // ADD GUARD POPUP
    // =====================================================

    private void openAddGuardDialog() {

        // =====================================================
        // GET LATEST SOCIETY
        // =====================================================

        currentSociety =
                getCurrentSociety();

        // =====================================================
        // CHECK SOCIETY
        // =====================================================

        if (currentSociety == null
                || currentSociety.trim().isEmpty()) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Society Not Found",
                    "Current society information is not available."
            );

            return;
        }

        // =====================================================
        // OVERLAY
        // =====================================================

        StackPane overlay =
                new StackPane();

        overlay.setStyle(
                "-fx-background-color:rgba(0,0,0,0.5);"
        );

        // =====================================================
        // FORM BOX
        // =====================================================

        VBox formBox =
                new VBox(7);

        formBox.setPadding(
                new Insets(18)
        );

        formBox.setPrefWidth(430);

        formBox.setMaxWidth(430);

        formBox.setMaxHeight(540);

        formBox.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:15;"
                        + "-fx-effect:dropshadow("
                        + "gaussian, rgba(0,0,0,0.3),"
                        + "20,0.2,0,5);"
        );

        // =====================================================
        // HEADER
        // =====================================================

        HBox headerRow =
                new HBox();

        headerRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label popupTitle =
                new Label(
                        "Add New Guard"
                );

        popupTitle.setFont(
                Font.font(
                        "Georgia",
                        FontWeight.BOLD,
                        21
                )
        );

        popupTitle.setStyle(
                "-fx-text-fill:#123C36;"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button closeBtn =
                new Button("✕");

        closeBtn.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        closeBtn.setStyle(
                "-fx-background-color:transparent;"
                        + "-fx-text-fill:#555555;"
                        + "-fx-cursor:hand;"
        );

        headerRow.getChildren().addAll(
                popupTitle,
                spacer,
                closeBtn
        );

        // =====================================================
        // NAME
        // =====================================================

        Label nameLabel =
                createFormLabel(
                        "Guard Name"
                );

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Enter guard name"
        );

        nameField.setPrefHeight(35);

        // =====================================================
        // MOBILE
        // =====================================================

        Label mobileLabel =
                createFormLabel(
                        "Mobile Number"
                );

        TextField mobileField =
                new TextField();

        mobileField.setPromptText(
                "Enter mobile number"
        );

        mobileField.setPrefHeight(35);

        // =====================================================
        // SHIFT
        // =====================================================

        Label shiftLabel =
                createFormLabel(
                        "Shift"
                );

        ComboBox<String> shiftCombo =
                new ComboBox<>();

        shiftCombo.getItems().addAll(
                "Morning",
                "Evening",
                "Night"
        );

        shiftCombo.setValue(
                "Morning"
        );

        shiftCombo.setPrefHeight(35);

        shiftCombo.setMaxWidth(
                Double.MAX_VALUE
        );

        // =====================================================
        // EMAIL
        // =====================================================

        Label emailLabel =
                createFormLabel(
                        "Email"
                );

        TextField emailField =
                new TextField();

        emailField.setPromptText(
                "Enter email"
        );

        emailField.setPrefHeight(35);

        // =====================================================
        // STATUS
        // =====================================================

        Label statusLabel =
                createFormLabel(
                        "Status"
                );

        ComboBox<String> statusCombo =
                new ComboBox<>();

        statusCombo.getItems().addAll(
                "Active",
                "Inactive"
        );

        statusCombo.setValue(
                "Active"
        );

        statusCombo.setPrefHeight(35);

        statusCombo.setMaxWidth(
                Double.MAX_VALUE
        );

        // =====================================================
        // ASSIGNED GATE
        // =====================================================

        Label gateLabel =
                createFormLabel(
                        "Assigned Gate"
                );

        ComboBox<String> gateCombo =
                new ComboBox<>();

        gateCombo.getItems().addAll(
                "Main Gate",
                "Back Gate"
        );

        gateCombo.setValue(
                "Main Gate"
        );

        gateCombo.setPrefHeight(35);

        gateCombo.setMaxWidth(
                Double.MAX_VALUE
        );

        // =====================================================
        // SOCIETY
        // =====================================================

        Label societyLabel =
                createFormLabel(
                        "Society"
                );

        TextField societyField =
                new TextField(
                        currentSociety
                );

        societyField.setPrefHeight(35);

        societyField.setEditable(false);

        societyField.setFocusTraversable(false);

        societyField.setStyle(
                "-fx-background-color:#EEEEEE;"
                        + "-fx-text-fill:#555555;"
        );

        // =====================================================
        // CANCEL
        // =====================================================

        Button cancelBtn =
                new Button(
                        "Cancel"
                );

        cancelBtn.setPrefWidth(100);

        cancelBtn.setPrefHeight(38);

        cancelBtn.setStyle(
                "-fx-background-color:#E5E7EB;"
                        + "-fx-text-fill:#333333;"
                        + "-fx-background-radius:8;"
                        + "-fx-cursor:hand;"
        );

        // =====================================================
        // SAVE
        // =====================================================

        Button saveBtn =
                new Button(
                        "Save Guard"
                );

        saveBtn.setPrefWidth(130);

        saveBtn.setPrefHeight(38);

        saveBtn.setStyle(
                "-fx-background-color:#2E9D63;"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:8;"
                        + "-fx-cursor:hand;"
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
                new Insets(
                        5,
                        0,
                        0,
                        0
                )
        );

        buttonBox.getChildren().addAll(
                cancelBtn,
                saveBtn
        );

        // =====================================================
        // FORM CONTENT
        // =====================================================

        formBox.getChildren().addAll(

                headerRow,

                nameLabel,
                nameField,

                mobileLabel,
                mobileField,

                shiftLabel,
                shiftCombo,

                emailLabel,
                emailField,

                statusLabel,
                statusCombo,

                gateLabel,
                gateCombo,

                societyLabel,
                societyField,

                buttonBox
        );

        // =====================================================
        // ADD FORM TO OVERLAY
        // =====================================================

        overlay.getChildren().add(
                formBox
        );

        StackPane.setAlignment(
                formBox,
                Pos.CENTER
        );

        rootStack.getChildren().add(
                overlay
        );

        // =====================================================
        // CLOSE
        // =====================================================

        closeBtn.setOnAction(
                e -> removeOverlay(
                        overlay
                )
        );

        // =====================================================
        // CANCEL
        // =====================================================

        cancelBtn.setOnAction(
                e -> removeOverlay(
                        overlay
                )
        );

        // =====================================================
        // SAVE
        // =====================================================

        saveBtn.setOnAction(e -> {

            String name =
                    nameField.getText()
                            .trim();

            String mobile =
                    mobileField.getText()
                            .trim();

            String shift =
                    shiftCombo.getValue();

            String email =
                    emailField.getText()
                            .trim();

            String status =
                    statusCombo.getValue();

            String gate =
                    gateCombo.getValue();

            // =================================================
            // IMPORTANT
            // SOCIETY ALWAYS COMES FROM LOGGED-IN SECRETARY
            // =================================================

            String society =
                    currentSociety;

            // =================================================
            // VALIDATION
            // =================================================

            if (name.isEmpty()
                    || mobile.isEmpty()
                    || shift == null
                    || email.isEmpty()
                    || status == null
                    || gate == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation Error",
                        "Please fill all fields."
                );

                return;
            }

            // =================================================
            // SOCIETY VALIDATION
            // =================================================

            if (society == null
                    || society.trim().isEmpty()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Society Error",
                        "Current society could not be determined."
                );

                return;
            }

            // =================================================
            // SAVE GUARD
            // =================================================

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "ADDING NEW GUARD"
            );

            System.out.println(
                    "Name    : "
                            + name
            );

            System.out.println(
                    "Email   : "
                            + email
            );

            System.out.println(
                    "Society : "
                            + society
            );

            System.out.println(
                    "=========================================="
            );

            boolean success =
                    guardController.addGuard(
                            name,
                            mobile,
                            shift,
                            email,
                            status,
                            gate,
                            society
                    );

            // =================================================
            // SUCCESS
            // =================================================

            if (success) {

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Guard saved successfully!"
                );

                removeOverlay(
                        overlay
                );

                // =================================================
                // CLEAR
                // =================================================

                nameField.clear();

                mobileField.clear();

                emailField.clear();

                shiftCombo.setValue(
                        "Morning"
                );

                statusCombo.setValue(
                        "Active"
                );

                gateCombo.setValue(
                        "Main Gate"
                );

                // =================================================
                // REFRESH CURRENT SOCIETY
                // =================================================

                loadGuards();

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed to save guard."
                );
            }
        });
    }

    // =====================================================
    // FORM LABEL
    // =====================================================

    private Label createFormLabel(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-weight:bold;"
                        + "-fx-text-fill:#333333;"
                        + "-fx-font-size:13px;"
        );

        return label;
    }

    // =====================================================
    // REMOVE OVERLAY
    // =====================================================

    private void removeOverlay(
            StackPane overlay) {

        if (rootStack != null) {

            rootStack.getChildren()
                    .remove(
                            overlay
                    );
        }
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

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }
}