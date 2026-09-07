package com.society.view.Owner_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.controller.Owner_Controller.ResidentController;
import com.society.model.Owner_model.ResidentModel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RemoveTenant {

    private static final ResidentController residentController =
            new ResidentController();

    private static final String BROWN = "#4e342e";
    private static final String BACKGROUND = "#e8ddd5";
    private static final String CARD = "#F4F7F8";
    private static final String DARK_TEXT = "#102A43";
    private static final String SECONDARY_TEXT = "#546E7A";
    private static final String BORDER = "#CBD5D8";
    private static final String GREEN = "#4CAF50";

    public static Scene createScene(Stage stage, String ownerEmail) {

        BorderPane root = new BorderPane();

        // =====================================================
        // SIDEBAR - UNCHANGED
        // =====================================================

        OwnerSidebar sidebar =
                new OwnerSidebar(stage, ownerEmail);

        root.setLeft(sidebar.getSidebar());

        // =====================================================
        // MAIN CONTENT - SAME THEME
        // =====================================================

        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(30, 40, 30, 40));
        mainContent.setSpacing(20);
        mainContent.setAlignment(Pos.TOP_LEFT);
        mainContent.setStyle(
                "-fx-background-color:" + BACKGROUND + ";"
        );

        // =====================================================
        // HEADER - SAME AS CURRENT PAGE
        // =====================================================

        HBox header = new HBox();
        header.setPrefWidth(900);
        header.setPrefHeight(80);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color:" + BROWN + ";"
        );

        VBox headingBox = new VBox();
        headingBox.setSpacing(3);

        Label greeting = new Label("Remove Tenant");
        greeting.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#ffffff;"
        );

        Label description = new Label(
                "Remove an existing tenant from your property"
        );
        description.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#ffffff;"
        );

        headingBox.getChildren().addAll(
                greeting,
                description
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label day = new Label();
        Label date = new Label();

        LocalDate today = LocalDate.now();

        day.setText(today.format(
                DateTimeFormatter.ofPattern("EEEE")
        ));
        day.setStyle("-fx-text-fill:#ffffff;");

        date.setText(today.format(
                DateTimeFormatter.ofPattern("dd MMMM yyyy")
        ));
        date.setStyle("-fx-text-fill:#ffffff;");

        VBox dateBox = new VBox();
        dateBox.setSpacing(2);
        dateBox.setAlignment(Pos.CENTER_RIGHT);
        dateBox.getChildren().addAll(day, date);

        header.getChildren().addAll(
                headingBox,
                spacer,
                dateBox
        );

        // =====================================================
        // MAIN CARD
        // =====================================================

        VBox tenantCard = new VBox();
        tenantCard.setPadding(new Insets(25));
        tenantCard.setSpacing(20);
        tenantCard.setMaxWidth(1200);
        tenantCard.setStyle(
                "-fx-background-color:#ffffff;" +
                "-fx-background-radius:12;"
        );

        Label cardTitle = new Label("1. Select Tenant");
        cardTitle.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" + DARK_TEXT + ";"
        );

        Label cardSubtitle = new Label(
                "Choose an active tenant to continue"
        );
        cardSubtitle.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:" + SECONDARY_TEXT + ";"
        );

        VBox cardHeading = new VBox(
                4,
                cardTitle,
                cardSubtitle
        );

        // =====================================================
        // LEFT: ACTIVE TENANTS
        // =====================================================

        VBox tenantSelectionBox = new VBox();
        tenantSelectionBox.setSpacing(12);
        tenantSelectionBox.setPrefWidth(470);
        tenantSelectionBox.setMinWidth(400);

        Label activeTitle = new Label("Active Tenants");
        activeTitle.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" + DARK_TEXT + ";"
        );

        Label activeSubtitle = new Label(
                "Select a tenant to view their details"
        );
        activeSubtitle.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:" + SECONDARY_TEXT + ";"
        );

        VBox activeHeading = new VBox(
                3,
                activeTitle,
                activeSubtitle
        );

        ListView<ResidentModel> tenantList =
                new ListView<>();

        tenantList.setPrefHeight(300);
        tenantList.setMaxHeight(300);
        tenantList.setStyle(
                "-fx-background-color:#ffffff;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;"
        );

        tenantList.setCellFactory(list ->
                new ListCell<ResidentModel>() {

                    private final Label initials = new Label();
                    private final Label name = new Label();
                    private final Label email = new Label();
                    private final Label flat = new Label();
                    private final Label rent = new Label();
                    private final Label status = new Label("Active");

                    private final VBox middle = new VBox();
                    private final VBox right = new VBox();
                    private final HBox row = new HBox();

                    {
                        initials.setPrefSize(45, 45);
                        initials.setMinSize(45, 45);
                        initials.setMaxSize(45, 45);
                        initials.setAlignment(Pos.CENTER);
                        initials.setStyle(
                                "-fx-background-color:#ead8ce;" +
                                "-fx-background-radius:50;" +
                                "-fx-text-fill:" + BROWN + ";" +
                                "-fx-font-size:14px;" +
                                "-fx-font-weight:bold;"
                        );

                        name.setStyle(
                                "-fx-font-size:13px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-text-fill:" + DARK_TEXT + ";"
                        );

                        email.setStyle(
                                "-fx-font-size:10px;" +
                                "-fx-text-fill:" + SECONDARY_TEXT + ";"
                        );

                        middle.setSpacing(2);
                        middle.getChildren().addAll(name, email);

                        flat.setStyle(
                                "-fx-font-size:12px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-text-fill:" + DARK_TEXT + ";"
                        );

                        rent.setStyle(
                                "-fx-font-size:10px;" +
                                "-fx-text-fill:" + SECONDARY_TEXT + ";"
                        );

                        status.setStyle(
                                "-fx-background-color:#E7F4E8;" +
                                "-fx-background-radius:10;" +
                                "-fx-text-fill:" + GREEN + ";" +
                                "-fx-font-size:10px;" +
                                "-fx-padding:3 8 3 8;"
                        );

                        right.setSpacing(3);
                        right.setAlignment(Pos.CENTER_RIGHT);
                        right.getChildren().addAll(flat, rent, status);

                        row.setSpacing(12);
                        row.setAlignment(Pos.CENTER_LEFT);
                        row.setPadding(new Insets(10));

                        HBox.setHgrow(middle, Priority.ALWAYS);

                        row.getChildren().addAll(
                                initials,
                                middle,
                                right
                        );

                        setPrefHeight(78);
                    }

                    @Override
                    protected void updateItem(
                            ResidentModel tenant,
                            boolean empty) {

                        super.updateItem(tenant, empty);

                        if (empty || tenant == null) {
                            setGraphic(null);
                            setText(null);
                            return;
                        }

                        name.setText(safe(tenant.getName()));
                        email.setText(safe(tenant.getEmail()));
                        flat.setText(safe(tenant.getFlat()));

                        String rentValue =
                                safe(tenant.getMonthlyRent());

                        if (rentValue.isEmpty()) {
                            rent.setText("Rent not specified");
                        } else {
                            rent.setText("₹" + rentValue + " / month");
                        }

                        initials.setText(
                                getInitials(tenant.getName())
                        );

                        setGraphic(row);
                        updateSelectionStyle();
                    }

                    @Override
                    public void updateSelected(boolean selected) {
                        super.updateSelected(selected);
                        updateSelectionStyle();
                    }

                    private void updateSelectionStyle() {
                        if (isSelected()) {
                            setStyle(
                                    "-fx-background-color:#f7ebe4;" +
                                    "-fx-border-color:#b47b62;" +
                                    "-fx-border-width:1;"
                            );
                        } else {
                            setStyle(
                                    "-fx-background-color:#ffffff;"
                            );
                        }
                    }
                }
        );

        tenantSelectionBox.getChildren().addAll(
                activeHeading,
                tenantList
        );

        // =====================================================
        // RIGHT: SELECTED TENANT DETAILS
        // =====================================================

        VBox detailsBox = new VBox();
        detailsBox.setSpacing(15);
        detailsBox.setPrefWidth(650);
        detailsBox.setPadding(new Insets(20));
        detailsBox.setStyle(
                "-fx-background-color:" + CARD + ";" +
                "-fx-background-radius:10;"
        );

        Label detailsTitle = new Label("2. Tenant Details");
        detailsTitle.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" + DARK_TEXT + ";"
        );

        Label detailsSubtitle = new Label(
                "Review tenant details and provide move-out information"
        );
        detailsSubtitle.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:" + SECONDARY_TEXT + ";"
        );

        VBox detailsHeading = new VBox(
                4,
                detailsTitle,
                detailsSubtitle
        );

        GridPane form = new GridPane();
        form.setHgap(25);
        form.setVgap(10);

        Label nameLabel = createLabel("Full Name");
        TextField nameField = createReadOnlyField();

        Label rentLabel = createLabel("Monthly Rent");
        TextField rentField = createReadOnlyField();

        Label emailLabel = createLabel("Email");
        TextField emailField = createReadOnlyField();

        Label moveOutLabel = createLabel("Move-out Date *");
        DatePicker moveOutDate = new DatePicker();
        moveOutDate.setPromptText("Select move-out date");
        moveOutDate.setPrefWidth(270);
        moveOutDate.setPrefHeight(40);
        moveOutDate.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;"
        );

        Label flatLabel = createLabel("Flat / Unit");
        TextField flatField = createReadOnlyField();

        Label reasonLabel = createLabel("Reason *");
        TextField reasonField = createField("Enter reason");
        reasonField.setPrefWidth(270);

        Label moveInLabel = createLabel("Move-in Date");
        TextField moveInField = createReadOnlyField();

        form.add(nameLabel, 0, 0);
        form.add(rentLabel, 1, 0);
        form.add(nameField, 0, 1);
        form.add(rentField, 1, 1);

        form.add(emailLabel, 0, 2);
        form.add(moveOutLabel, 1, 2);
        form.add(emailField, 0, 3);
        form.add(moveOutDate, 1, 3);

        form.add(flatLabel, 0, 4);
        form.add(reasonLabel, 1, 4);
        form.add(flatField, 0, 5);
        form.add(reasonField, 1, 5);

        form.add(moveInLabel, 0, 6);
        form.add(moveInField, 0, 7);

        Button clearButton = new Button("Clear");
        Button removeButton = new Button("Remove Tenant");

        clearButton.setPrefWidth(110);
        clearButton.setPrefHeight(40);

        removeButton.setPrefWidth(140);
        removeButton.setPrefHeight(40);

        clearButton.setStyle(
                "-fx-background-color:#ffffff;" +
                "-fx-text-fill:" + BROWN + ";" +
                "-fx-font-weight:bold;" +
                "-fx-border-color:#8D6E63;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;"
        );

        removeButton.setStyle(
                "-fx-background-color:" + BROWN + ";" +
                "-fx-text-fill:#ffffff;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;"
        );

        HBox buttons = new HBox();
        buttons.setSpacing(12);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.getChildren().addAll(
                clearButton,
                removeButton
        );

        // =====================================================
        // INITIAL STATE
        // =====================================================

        moveOutDate.setDisable(true);
        reasonField.setDisable(true);
        removeButton.setDisable(true);

        // =====================================================
        // SELECT TENANT
        // =====================================================

        tenantList.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldTenant, selectedTenant) -> {

                    if (selectedTenant == null) {
                        clearTenantFields(
                                nameField,
                                emailField,
                                flatField,
                                moveInField,
                                rentField
                        );

                        moveOutDate.setDisable(true);
                        reasonField.setDisable(true);
                        removeButton.setDisable(true);
                        return;
                    }

                    nameField.setText(
                            safe(selectedTenant.getName())
                    );

                    emailField.setText(
                            safe(selectedTenant.getEmail())
                    );

                    flatField.setText(
                            safe(selectedTenant.getFlat())
                    );

                    moveInField.setText(
                            safe(selectedTenant.getMoveInDate())
                    );

                    String rentValue =
                            safe(selectedTenant.getMonthlyRent());

                    rentField.setText(
                            rentValue.isEmpty()
                                    ? ""
                                    : "₹" + rentValue
                    );

                    moveOutDate.setDisable(false);
                    reasonField.setDisable(false);
                    removeButton.setDisable(false);
                });

        // =====================================================
        // LOAD ACTIVE TENANTS
        // =====================================================

        loadActiveTenants(
                tenantList,
                ownerEmail
        );

        // =====================================================
        // CLEAR
        // =====================================================

        clearButton.setOnAction(event -> {

            tenantList.getSelectionModel()
                    .clearSelection();

            clearTenantFields(
                    nameField,
                    emailField,
                    flatField,
                    moveInField,
                    rentField
            );

            moveOutDate.setValue(null);
            reasonField.clear();

            moveOutDate.setDisable(true);
            reasonField.setDisable(true);
            removeButton.setDisable(true);
        });

        // =====================================================
        // REMOVE TENANT
        // =====================================================

        removeButton.setOnAction(event -> {

            try {

                ResidentModel selectedTenant =
                        tenantList.getSelectionModel()
                                .getSelectedItem();

                if (selectedTenant == null) {
                    showAlert(
                            Alert.AlertType.WARNING,
                            "No Tenant Selected",
                            "Please select an active tenant."
                    );
                    return;
                }

                if (moveOutDate.getValue() == null) {
                    showAlert(
                            Alert.AlertType.WARNING,
                            "Missing Information",
                            "Please select move-out date."
                    );
                    return;
                }

                LocalDate selectedMoveOutDate =
                        moveOutDate.getValue();

                if (selectedMoveOutDate.isBefore(LocalDate.now())) {
                    showAlert(
                            Alert.AlertType.WARNING,
                            "Invalid Date",
                            "Move-out date cannot be before today."
                    );
                    return;
                }

                String reason =
                        reasonField.getText().trim();

                if (reason.isEmpty()) {
                    showAlert(
                            Alert.AlertType.WARNING,
                            "Missing Information",
                            "Please enter the reason."
                    );
                    return;
                }

                String moveOutDateValue =
                        selectedMoveOutDate.format(
                                DateTimeFormatter.ofPattern(
                                        "dd MMMM yyyy"
                                )
                        );

                Alert confirmation =
                        new Alert(Alert.AlertType.CONFIRMATION);

                confirmation.setTitle("Remove Tenant");
                confirmation.setHeaderText(
                        "Confirm Tenant Removal"
                );
                confirmation.setContentText(
                        "Are you sure you want to remove "
                        + selectedTenant.getName()
                        + "?\n\n"
                        + "Flat / Unit: "
                        + selectedTenant.getFlat()
                        + "\n"
                        + "Move-out Date: "
                        + moveOutDateValue
                        + "\n\n"
                        + "The tenant will be marked as inactive "
                        + "and their rental history will be preserved."
                );

                java.util.Optional<ButtonType> result =
                        confirmation.showAndWait();

                if (result.isEmpty() ||
                        result.get() != ButtonType.OK) {
                    return;
                }

                boolean success =
                        residentController.removeTenant(
                                ownerEmail,
                                selectedTenant.getEmail(),
                                moveOutDateValue,
                                reason
                        );

                if (success) {

                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Tenant Removed",
                            selectedTenant.getName()
                            + " has been successfully removed.\n\n"
                            + "The tenant has been marked as inactive "
                            + "and their rental history has been preserved."
                    );

                    tenantList.getItems()
                            .remove(selectedTenant);

                    clearTenantFields(
                            nameField,
                            emailField,
                            flatField,
                            moveInField,
                            rentField
                    );

                    moveOutDate.setValue(null);
                    reasonField.clear();

                    moveOutDate.setDisable(true);
                    reasonField.setDisable(true);
                    removeButton.setDisable(true);

                } else {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Remove Failed",
                            "Tenant could not be removed.\n\n"
                            + "Please make sure the selected tenant "
                            + "is active and belongs to your account."
                    );
                }

            } catch (Exception ex) {

                ex.printStackTrace();

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Error while removing tenant:\n\n"
                        + ex.getMessage()
                );
            }
        });

        detailsBox.getChildren().addAll(
                detailsHeading,
                form,
                buttons
        );

        HBox selectionArea = new HBox();
        selectionArea.setSpacing(25);
        selectionArea.getChildren().addAll(
                tenantSelectionBox,
                detailsBox
        );

        HBox.setHgrow(detailsBox, Priority.ALWAYS);

        tenantCard.getChildren().addAll(
                cardHeading,
                selectionArea
        );

        // =====================================================
        // INFORMATION LABEL
        // =====================================================

        Label information = new Label(
                "The tenant will be marked as Inactive. " +
                "Their rental history will be preserved."
        );

        information.setPadding(new Insets(14));
        information.setMaxWidth(1200);
        information.setStyle(
                "-fx-background-color:#E8F1F2;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:#263238;"
        );

        mainContent.getChildren().addAll(
                header,
                tenantCard,
                information
        );

        root.setCenter(mainContent);

        return new Scene(
                root,
                1500,
                750
        );
    }

    // =========================================================
    // LOAD ACTIVE TENANTS
    // =========================================================

    private static void loadActiveTenants(
            ListView<ResidentModel> tenantList,
            String ownerEmail) {

        try {

            List<ResidentModel> tenants =
                    residentController.getActiveTenants(
                            ownerEmail
                    );

            tenantList.getItems().clear();

            if (tenants != null && !tenants.isEmpty()) {
                tenantList.getItems().addAll(tenants);
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Unable to Load Tenants",
                    "Active tenants could not be loaded.\n\n"
                    + ex.getMessage()
            );
        }
    }

    // =========================================================
    // CLEAR TENANT FIELDS
    // =========================================================

    private static void clearTenantFields(
            TextField nameField,
            TextField emailField,
            TextField flatField,
            TextField moveInField,
            TextField rentField) {

        nameField.clear();
        emailField.clear();
        flatField.clear();
        moveInField.clear();
        rentField.clear();
    }

    // =========================================================
    // CREATE LABEL
    // =========================================================

    private static Label createLabel(String text) {

        Label label = new Label(text);

        label.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#37474F;"
        );

        return label;
    }

    // =========================================================
    // CREATE FIELD
    // =========================================================

    private static TextField createField(String prompt) {

        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefWidth(270);
        field.setPrefHeight(40);

        field.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;"
        );

        return field;
    }

    // =========================================================
    // READ-ONLY FIELD
    // =========================================================

    private static TextField createReadOnlyField() {

        TextField field = createField("");
        field.setEditable(false);

        field.setStyle(
                "-fx-background-color:#edf1f2;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;" +
                "-fx-text-fill:#455A64;"
        );

        return field;
    }

    // =========================================================
    // INITIALS
    // =========================================================

    private static String getInitials(String name) {

        if (name == null || name.trim().isEmpty()) {
            return "?";
        }

        String[] parts = name.trim().split("\\s+");

        if (parts.length == 1) {
            return parts[0]
                    .substring(0, 1)
                    .toUpperCase();
        }

        return (
                parts[0].substring(0, 1)
                +
                parts[parts.length - 1]
                        .substring(0, 1)
        ).toUpperCase();
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private static String safe(String value) {
        return value == null ? "" : value;
    }

    // =========================================================
    // ALERT
    // =========================================================

    private static void showAlert(
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
