
package com.society.view.Owner_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.controller.Secretary_Controller.ReportController;
import com.society.dao.Resident_dao.Resident;
import com.society.view.ScreenSize;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
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
import javafx.util.Duration;

public class RemoveTenant {

    // =========================================================
    // COLORS
    // =========================================================

    private static final String BROWN = "#4e342e";
    private static final String BACKGROUND = "#e8ddd5";
    private static final String CARD = "#F4F7F8";
    private static final String DARK_TEXT = "#102A43";
    private static final String SECONDARY_TEXT = "#546E7A";
    private static final String BORDER = "#CBD5D8";
    private static final String GREEN = "#4CAF50";

    // =========================================================
    // CREATE SCENE
    // =========================================================

    public static Scene createScene(Stage stage, String ownerEmail) {

        BorderPane root = new BorderPane();

        // =====================================================
        // SIDEBAR - LEFT
        // =====================================================

        OwnerSidebar sidebar = new OwnerSidebar(stage, ownerEmail);
        root.setLeft(sidebar.getSidebar());

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainContent = new VBox();

        mainContent.setPadding(
                new Insets(25, 35, 25, 35)
        );

        mainContent.setSpacing(15);

        mainContent.setAlignment(Pos.TOP_LEFT);

        mainContent.setStyle(
                "-fx-background-color: " + BACKGROUND + ";"
        );

        // =====================================================
        // HEADER
        // =====================================================

       // =====================================================
// HEADER
// =====================================================

HBox header = new HBox();

header.setPrefHeight(80);

header.setPadding(
        new Insets(15, 25, 15, 25)
);

header.setAlignment(
        Pos.CENTER_LEFT
);

header.setStyle(
        "-fx-background-color:#4e342e;"
);

// =====================================================
// HEADER TEXT
// =====================================================

VBox headerText =
        new VBox(4);

Label title =
        new Label(
                "Remove Tenant"
        );

title.setStyle(
        "-fx-font-family:'Bodoni MT';" +
        "-fx-font-size:24px;" +
        "-fx-font-weight:bold;" +
        "-fx-text-fill:white;"
);

Label subtitle =
        new Label(
                "Remove an active tenant from your property"
        );

subtitle.setStyle(
        "-fx-font-size:14px;" +
        "-fx-text-fill:white;"
);

headerText.getChildren().addAll(
        title,
        subtitle
);

// =====================================================
// SPACER
// =====================================================

Region headerSpacer =
        new Region();

HBox.setHgrow(
        headerSpacer,
        Priority.ALWAYS
);

// =====================================================
// DATE
// =====================================================

LocalDate today =
        LocalDate.now();

Label day =
        new Label();

Label date =
        new Label();

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

VBox dateBox =
        new VBox(3);

dateBox.setAlignment(
        Pos.CENTER_RIGHT
);

dateBox.getChildren().addAll(
        day,
        date
);

// =====================================================
// ADD HEADER COMPONENTS
// =====================================================

header.getChildren().addAll(
        headerText,
        headerSpacer,
        dateBox
);

        mainContent.getChildren().add(header);

        // =====================================================
        // TENANT CARD
        // =====================================================

        VBox tenantCard = new VBox(15);

        tenantCard.setPadding(
                new Insets(20)
        );

        tenantCard.setStyle(
                "-fx-background-color: " + CARD + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 12;"
        );

        Label cardTitle = new Label(
                "Active Tenants"
        );

        cardTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + DARK_TEXT + ";"
        );

        // =====================================================
        // TENANT LIST
        // =====================================================

        ListView<Resident> tenantList =
                new ListView<>();

        tenantList.setPrefHeight(250);

        tenantList.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: " + BORDER + ";"
        );

        tenantList.setCellFactory(
                list -> new ListCell<Resident>() {

                    @Override
                    protected void updateItem(
                            Resident tenant,
                            boolean empty) {

                        super.updateItem(
                                tenant,
                                empty
                        );

                        if (empty || tenant == null) {

                            setText(null);
                            setGraphic(null);

                            return;
                        }

                        VBox box = new VBox(4);

                        box.setPadding(
                                new Insets(10)
                        );

                        Label name = new Label(
                                safe(tenant.getName())
                        );

                        name.setStyle(
                                "-fx-font-size: 15px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: " + DARK_TEXT + ";"
                        );

                        Label email = new Label(
                                String.valueOf(tenant.getEmail())
                        );

                        email.setStyle(
                                "-fx-font-size: 12px;" +
                                "-fx-text-fill: " + SECONDARY_TEXT + ";"
                        );

                        Label flat = new Label(
                                "Flat: " +
                                String.valueOf(tenant.getFlatNumber())
                        );

                        flat.setStyle(
                                "-fx-font-size: 12px;" +
                                "-fx-text-fill: " + SECONDARY_TEXT + ";"
                        );

                        box.getChildren().addAll(
                                name,
                                email,
                                flat
                        );

                        setGraphic(box);
                    }
                }
        );

        tenantCard.getChildren().addAll(
                cardTitle,
                tenantList
        );

        // =====================================================
        // DETAILS CARD
        // =====================================================

        VBox detailsCard = new VBox(15);

        detailsCard.setPadding(
                new Insets(20)
        );

        detailsCard.setStyle(
                "-fx-background-color: " + CARD + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 12;"
        );

        Label detailsTitle = new Label(
                "Tenant Details"
        );

        detailsTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + DARK_TEXT + ";"
        );

        // =====================================================
        // GRID
        // =====================================================

        GridPane grid = new GridPane();

        grid.setHgap(15);
        grid.setVgap(12);

        TextField nameField =
                createReadOnlyField();

        TextField rentField =
                createReadOnlyField();

        TextField emailField =
                createReadOnlyField();

        TextField flatField =
                createReadOnlyField();

        TextField moveInField =
                createReadOnlyField();

        DatePicker moveOutDate =
                new DatePicker();

        moveOutDate.setMaxWidth(
                Double.MAX_VALUE
        );

        TextField reasonField =
                new TextField();

        reasonField.setPromptText(
                "Enter reason for removal"
        );

        String fieldStyle =
                "-fx-background-color: white;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;";

        moveOutDate.setStyle(
                fieldStyle
        );

        reasonField.setStyle(
                fieldStyle
        );

        // =====================================================
        // ADD FIELDS TO GRID
        // =====================================================

        grid.add(
                createLabel("Tenant Name"),
                0,
                0
        );

        grid.add(
                nameField,
                1,
                0
        );

        grid.add(
                createLabel("Monthly Rent"),
                0,
                1
        );

        grid.add(
                rentField,
                1,
                1
        );

        grid.add(
                createLabel("Email"),
                0,
                2
        );

        grid.add(
                emailField,
                1,
                2
        );

        grid.add(
                createLabel("Flat / Unit"),
                0,
                3
        );

        grid.add(
                flatField,
                1,
                3
        );

        grid.add(
                createLabel("Move-in Date"),
                0,
                4
        );

        grid.add(
                moveInField,
                1,
                4
        );

        grid.add(
                createLabel("Move-out Date"),
                0,
                5
        );

        grid.add(
                moveOutDate,
                1,
                5
        );

        grid.add(
                createLabel("Reason"),
                0,
                6
        );

        grid.add(
                reasonField,
                1,
                6
        );

        // =====================================================
        // HORIZONTAL GROW
        // =====================================================

        GridPane.setHgrow(
                nameField,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                rentField,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                emailField,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                flatField,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                moveInField,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                moveOutDate,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                reasonField,
                Priority.ALWAYS
        );

        // =====================================================
        // BUTTONS
        // =====================================================

        Button clearButton =
                new Button("Clear");

        Button removeButton =
                new Button("Remove Tenant");

        clearButton.setStyle(
                "-fx-background-color: #D7CCC8;" +
                "-fx-text-fill: " + DARK_TEXT + ";" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 20;"
        );

        removeButton.setStyle(
                "-fx-background-color: " + BROWN + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 20;"
        );

        HBox buttons = new HBox(12);

        buttons.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttons.getChildren().addAll(
                clearButton,
                removeButton
        );

        detailsCard.getChildren().addAll(
                detailsTitle,
                grid,
                buttons
        );

        // =====================================================
        // INFORMATION LABEL
        // =====================================================

        Label information = new Label(
                "The tenant will be marked as Inactive and " +
                "their rental history will be preserved."
        );

        information.setWrapText(true);

        information.setStyle(
                "-fx-background-color: #E8F5E9;" +
                "-fx-text-fill: " + GREEN + ";" +
                "-fx-padding: 12;" +
                "-fx-background-radius: 8;"
        );

        // =====================================================
        // MAIN CARDS CONTAINER
        // =====================================================

        HBox cards = new HBox(20);

        HBox.setHgrow(
                tenantCard,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                detailsCard,
                Priority.ALWAYS
        );

        cards.getChildren().addAll(
                tenantCard,
                detailsCard
        );

        mainContent.getChildren().addAll(
                cards,
                information
        );

        // =====================================================
        // INITIAL STATE
        // =====================================================

        nameField.setDisable(true);
        rentField.setDisable(true);
        emailField.setDisable(true);
        flatField.setDisable(true);
        moveInField.setDisable(true);

        moveOutDate.setDisable(true);
        reasonField.setDisable(true);

        clearButton.setDisable(true);
        removeButton.setDisable(true);

        // =====================================================
        // LOAD ACTIVE TENANTS
        // =====================================================

        loadActiveTenants(
                tenantList,
                ownerEmail
        );

        // =====================================================
        // SELECT TENANT
        // =====================================================

        tenantList.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, oldTenant, selectedTenant) -> {

                            if (selectedTenant == null) {

                                clearTenantFields(
                                        nameField,
                                        rentField,
                                        emailField,
                                        flatField,
                                        moveInField,
                                        moveOutDate,
                                        reasonField
                                );

                                moveOutDate.setDisable(true);
                                reasonField.setDisable(true);

                                clearButton.setDisable(true);
                                removeButton.setDisable(true);

                                return;
                            }

                            // -----------------------------
                            // DISPLAY TENANT DETAILS
                            // -----------------------------

                          nameField.setText(
        String.valueOf(selectedTenant.getName())
);

rentField.setText(
        String.valueOf(selectedTenant.getMonthlyRent())
);

emailField.setText(
        String.valueOf(selectedTenant.getEmail())
);

flatField.setText(
        String.valueOf(selectedTenant.getFlatNumber())
);

moveInField.setText(
        String.valueOf(selectedTenant.getMoveInDate())
);

                            // -----------------------------
                            // ENABLE CONTROLS
                            // -----------------------------

                            moveOutDate.setDisable(false);
                            reasonField.setDisable(false);

                            clearButton.setDisable(false);
                            removeButton.setDisable(false);
                        }
                );

        // =====================================================
        // CLEAR BUTTON
        // =====================================================

        clearButton.setOnAction(e -> {

            tenantList.getSelectionModel()
                    .clearSelection();

            clearTenantFields(
                    nameField,
                    rentField,
                    emailField,
                    flatField,
                    moveInField,
                    moveOutDate,
                    reasonField
            );

            moveOutDate.setDisable(true);
            reasonField.setDisable(true);

            clearButton.setDisable(true);
            removeButton.setDisable(true);
        });

        // =====================================================
        // REMOVE TENANT BUTTON
        // =====================================================

        removeButton.setOnAction(e -> {

            Resident selectedTenant =
                    tenantList
                            .getSelectionModel()
                            .getSelectedItem();

            // -------------------------------------------------
            // CHECK TENANT SELECTION
            // -------------------------------------------------

            if (selectedTenant == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "No Tenant Selected",
                        "Please select a tenant first."
                );

                return;
            }

            // -------------------------------------------------
            // CHECK MOVE-OUT DATE
            // -------------------------------------------------

            LocalDate moveOutDateValue =
                    moveOutDate.getValue();

            if (moveOutDateValue == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Move-out Date Required",
                        "Please select a move-out date."
                );

                return;
            }

            // -------------------------------------------------
            // CHECK DATE
            // -------------------------------------------------

            if (moveOutDateValue.isBefore(
                    LocalDate.now())) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Date",
                        "Move-out date cannot be before today."
                );

                return;
            }

            // -------------------------------------------------
            // CHECK REASON
            // -------------------------------------------------

            String reason =
                    reasonField
                            .getText()
                            .trim();

            if (reason.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Reason Required",
                        "Please enter a reason for removing the tenant."
                );

                return;
            }

            // -------------------------------------------------
            // CONFIRMATION
            // -------------------------------------------------

            Alert confirmation =
                    new Alert(
                            Alert.AlertType.CONFIRMATION
                    );

            confirmation.setTitle(
                    "Confirm Removal"
            );

            confirmation.setHeaderText(
                    "Remove " +
                    selectedTenant.getName() +
                    "?"
            );

            confirmation.setContentText(
                    "The tenant will be marked as inactive."
            );

            confirmation.showAndWait()
                    .ifPresent(response -> {

                        if (response ==
                                ButtonType.OK) {

                            try {

                                // ---------------------------------
                                // REMOVE FROM DATABASE
                                // ---------------------------------

                                boolean success =
                                        ReportController.removeTenant(
                                                ownerEmail,
                                                selectedTenant.getEmail(),
                                                moveOutDateValue,
                                                reason
                                        );

                                // ---------------------------------
                                // SUCCESS
                                // ---------------------------------

                                if (success) {

                                    showAlert(
                                            Alert.AlertType.INFORMATION,
                                            "Success",
                                            "Tenant removed successfully."
                                    );

                                    tenantList
                                            .getItems()
                                            .remove(
                                                    selectedTenant
                                            );

                                    clearTenantFields(
                                            nameField,
                                            rentField,
                                            emailField,
                                            flatField,
                                            moveInField,
                                            moveOutDate,
                                            reasonField
                                    );

                                    moveOutDate.setDisable(true);
                                    reasonField.setDisable(true);

                                    clearButton.setDisable(true);
                                    removeButton.setDisable(true);

                                }

                                // ---------------------------------
                                // FAILED
                                // ---------------------------------

                                else {

                                    showAlert(
                                            Alert.AlertType.ERROR,
                                            "Failed",
                                            "Unable to remove tenant."
                                    );
                                }

                            } catch (Exception ex) {

                                ex.printStackTrace();

                                showAlert(
                                        Alert.AlertType.ERROR,
                                        "Error",
                                        "An error occurred while removing tenant."
                                );
                            }
                        }
                    });
        });

        // =====================================================
        // ANIMATIONS
        // =====================================================

     //   animateHeader(header);

        animateTenantCard(
                tenantCard
        );

        animateDetailsCard(
                detailsCard
        );

        animateInformation(
                information
        );

        animateButton(
                clearButton
        );

        animateButton(
                removeButton
        );

        // =====================================================
        // ROOT
        // =====================================================

        root.setCenter(
                mainContent
        );

        Scene scene =
                new Scene(
                        root,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        stage.setScene(scene);

        stage.setMaximized(true);

        return scene;
    }

    // =========================================================
    // LOAD ACTIVE TENANTS
    // =========================================================

    private static void loadActiveTenants(
            ListView<Resident> tenantList,
            String ownerEmail) {

        try {

            List<Resident> tenants =
                    ReportController.getActiveTenants(
                            ownerEmail
                    );

            tenantList.getItems().setAll(
                    tenants
            );

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Unable to load active tenants."
            );
        }
    }

    // =========================================================
    // CLEAR FIELDS
    // =========================================================

    private static void clearTenantFields(
            TextField nameField,
            TextField rentField,
            TextField emailField,
            TextField flatField,
            TextField moveInField,
            DatePicker moveOutDate,
            TextField reasonField) {

        nameField.clear();

        rentField.clear();

        emailField.clear();

        flatField.clear();

        moveInField.clear();

        moveOutDate.setValue(null);

        reasonField.clear();
    }

    // =========================================================
    // CREATE LABEL
    // =========================================================

    private static Label createLabel(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + DARK_TEXT + ";"
        );

        return label;
    }

    // =========================================================
    // CREATE READ ONLY FIELD
    // =========================================================

    private static TextField createReadOnlyField() {

        TextField field =
                new TextField();

        field.setEditable(false);

        field.setStyle(
                "-fx-background-color: #EDEDED;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;"
        );

        return field;
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private static String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }

    // =========================================================
    // HEADER ANIMATION
    // =========================================================

  
       
       

    // =========================================================
    // TENANT CARD ANIMATION
    // =========================================================

    private static void animateTenantCard(
            VBox card) {

        card.setOpacity(0);

        card.setTranslateY(25);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(700),
                        card
                );

        fade.setFromValue(0);

        fade.setToValue(1);

        fade.setDelay(
                Duration.millis(200)
        );

        TranslateTransition translate =
                new TranslateTransition(
                        Duration.millis(700),
                        card
                );

        translate.setFromY(25);

        translate.setToY(0);

        translate.setDelay(
                Duration.millis(200)
        );

        ParallelTransition animation =
                new ParallelTransition(
                        fade,
                        translate
                );

        animation.play();
    }

    // =========================================================
    // DETAILS CARD ANIMATION
    // =========================================================

    private static void animateDetailsCard(
            VBox card) {

        card.setOpacity(0);

        card.setTranslateY(25);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(700),
                        card
                );

        fade.setFromValue(0);

        fade.setToValue(1);

        fade.setDelay(
                Duration.millis(300)
        );

        TranslateTransition translate =
                new TranslateTransition(
                        Duration.millis(700),
                        card
                );

        translate.setFromY(25);

        translate.setToY(0);

        translate.setDelay(
                Duration.millis(300)
        );

        ParallelTransition animation =
                new ParallelTransition(
                        fade,
                        translate
                );

        animation.play();
    }

    // =========================================================
    // INFORMATION ANIMATION
    // =========================================================

    private static void animateInformation(
            Label label) {

        label.setOpacity(0);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(600),
                        label
                );

        fade.setFromValue(0);

        fade.setToValue(1);

        fade.setDelay(
                Duration.millis(500)
        );

        fade.play();
    }

    // =========================================================
    // BUTTON ANIMATION
    // =========================================================

    private static void animateButton(
            Button button) {

        button.setOnMouseEntered(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            scale.setToX(1.05);

            scale.setToY(1.05);

            scale.play();
        });

        button.setOnMouseExited(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            scale.setToX(1.0);

            scale.setToY(1.0);

            scale.play();
        });

        button.setOnMousePressed(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(80),
                            button
                    );

            scale.setToX(0.97);

            scale.setToY(0.97);

            scale.play();
        });

        button.setOnMouseReleased(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(80),
                            button
                    );

            scale.setToX(1.05);

            scale.setToY(1.05);

            scale.play();
        });
    }

    // =========================================================
    // SHOW ALERT
    // =========================================================

    private static void showAlert(
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

