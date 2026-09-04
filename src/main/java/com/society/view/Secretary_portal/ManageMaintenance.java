package com.society.view.Secretary_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.controller.Secretary_Controller.MaintenanceController;
import com.society.model.Secretary_model.Maintenance;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManageMaintenance {

    // =========================================================
    // SCENE
    // =========================================================

    private Scene manageMaintenanceScene;

    // =========================================================
    // ROOT
    // =========================================================

    private StackPane rootStack;

    // =========================================================
    // CONTROLLER
    // =========================================================

    private MaintenanceController maintenanceController;

    // =========================================================
    // MAINTENANCE LIST
    // =========================================================

    private VBox maintenanceList;

    // =========================================================
    // CURRENT STATUS
    // =========================================================

    private String currentStatus = "Pending";

    // =========================================================
    // CREATE SCENE
    // =========================================================

    public Scene createScene(Stage stage) {

        maintenanceController = new MaintenanceController();

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
                new VBox(20);

        mainvb.setPadding(
                new Insets(25)
        );

        mainvb.setPrefWidth(1220);

        mainvb.setMaxWidth(
                Double.MAX_VALUE
        );

        mainvb.setMaxHeight(
                Double.MAX_VALUE
        );

        mainvb.setStyle(
                "-fx-background-color:#b3adad;"
        );

        // =====================================================
        // HEADING
        // =====================================================

        Label heading =
                new Label(
                        "MANAGE MAINTENANCE"
                );

        heading.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#434141;"
        );

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label(
                        "Manage Maintenance"
                );

        title.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:black;"
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        Label subtitle =
                new Label(
                        "View and manage maintenance records of your society"
                );

        subtitle.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#777777;"
        );

        VBox titleBox =
                new VBox(5);

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        // =====================================================
        // ADD BUTTON
        // =====================================================

        Button addMaintenanceBtn =
                new Button(
                        "+ Add Maintenance"
                );

        addMaintenanceBtn.setPrefWidth(160);
        addMaintenanceBtn.setPrefHeight(40);

        addMaintenanceBtn.setStyle(
                "-fx-background-color:#434141;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        addMaintenanceBtn.setOnAction(
                e -> openAddMaintenanceDialog()
        );

        // =====================================================
        // REFRESH BUTTON
        // =====================================================

        Button refreshBtn =
                new Button(
                        "⟳ Refresh"
                );

        refreshBtn.setPrefWidth(100);
        refreshBtn.setPrefHeight(40);

        refreshBtn.setStyle(
                "-fx-background-color:#ffffff;" +
                "-fx-text-fill:#434141;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-border-color:#cccccc;" +
                "-fx-border-radius:7;" +
                "-fx-cursor:hand;"
        );

        refreshBtn.setOnAction(
                e -> refreshCurrentMaintenance()
        );

        // =====================================================
        // HEADER
        // =====================================================

        HBox maintenanceHeader =
                new HBox(10);

        maintenanceHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                titleBox,
                Priority.ALWAYS
        );

        maintenanceHeader.getChildren().addAll(
                titleBox,
                refreshBtn,
                addMaintenanceBtn
        );

        // =====================================================
        // STATUS BUTTONS
        // =====================================================

        Button pendingBtn =
                new Button("Pending");

        Button paidBtn =
                new Button("Paid");

        Button overdueBtn =
                new Button("Overdue");

        pendingBtn.setPrefWidth(150);
        paidBtn.setPrefWidth(150);
        overdueBtn.setPrefWidth(150);

        pendingBtn.setPrefHeight(40);
        paidBtn.setPrefHeight(40);
        overdueBtn.setPrefHeight(40);

        // =====================================================
        // TAB STYLES
        // =====================================================

        String normalStyle =
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#777777;" +
                "-fx-font-weight:bold;" +
                "-fx-font-size:12px;" +
                "-fx-cursor:hand;";

        String activeStyle =
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#123C36;" +
                "-fx-font-weight:bold;" +
                "-fx-font-size:12px;" +
                "-fx-border-color:#0B4F4A;" +
                "-fx-border-width:0 0 2 0;" +
                "-fx-cursor:hand;";

        pendingBtn.setStyle(activeStyle);
        paidBtn.setStyle(normalStyle);
        overdueBtn.setStyle(normalStyle);

        // =====================================================
        // TABS
        // =====================================================

        HBox tabs =
                new HBox(25);

        tabs.setAlignment(
                Pos.CENTER_LEFT
        );

        tabs.getChildren().addAll(
                pendingBtn,
                paidBtn,
                overdueBtn
        );

        // =====================================================
        // MAINTENANCE LIST
        // =====================================================

        maintenanceList =
                new VBox(15);

        maintenanceList.setPadding(
                new Insets(5, 0, 5, 0)
        );

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                maintenanceList
        );

        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(450);

        scrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;"
        );

        // =====================================================
        // DEFAULT LOAD
        // =====================================================

        currentStatus = "Pending";

        loadMaintenanceByStatus(
                currentStatus
        );

        // =====================================================
        // PENDING
        // =====================================================

        pendingBtn.setOnAction(e -> {

            currentStatus = "Pending";

            loadMaintenanceByStatus(
                    currentStatus
            );

            pendingBtn.setStyle(activeStyle);
            paidBtn.setStyle(normalStyle);
            overdueBtn.setStyle(normalStyle);
        });

        // =====================================================
        // PAID
        // =====================================================

        paidBtn.setOnAction(e -> {

            currentStatus = "Paid";

            loadMaintenanceByStatus(
                    currentStatus
            );

            pendingBtn.setStyle(normalStyle);
            paidBtn.setStyle(activeStyle);
            overdueBtn.setStyle(normalStyle);
        });

        // =====================================================
        // OVERDUE
        // =====================================================

        overdueBtn.setOnAction(e -> {

            currentStatus = "Overdue";

            loadMaintenanceByStatus(
                    currentStatus
            );

            pendingBtn.setStyle(normalStyle);
            paidBtn.setStyle(normalStyle);
            overdueBtn.setStyle(activeStyle);
        });

        // =====================================================
        // VIEW ALL
        // =====================================================

        Button viewAllBtn =
                new Button(
                        "View All Maintenance"
                );

        viewAllBtn.setMaxWidth(
                Double.MAX_VALUE
        );

        viewAllBtn.setPrefHeight(40);

        viewAllBtn.setStyle(
                "-fx-background-color:#434141;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        viewAllBtn.setOnAction(
                e -> openViewAllMaintenanceDialog()
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        mainvb.getChildren().addAll(
                heading,
                maintenanceHeader,
                tabs,
                scrollPane,
                viewAllBtn
        );

        // =====================================================
        // ROOT
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
        // STACK
        // =====================================================

        rootStack =
                new StackPane();

        rootStack.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        rootStack.getChildren().add(
                mainRoot
        );

        // =====================================================
        // SCENE
        // =====================================================

        manageMaintenanceScene =
                new Scene(
                        rootStack,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        return manageMaintenanceScene;
    }

    // =========================================================
    // REFRESH CURRENT LIST
    // =========================================================

    private void refreshCurrentMaintenance() {

        if (currentStatus == null ||
                currentStatus.trim().isEmpty()) {

            currentStatus = "Pending";
        }

        loadMaintenanceByStatus(
                currentStatus
        );
    }

    // =========================================================
    // LOAD MAINTENANCE BY STATUS
    // =========================================================

    private void loadMaintenanceByStatus(
            String requiredStatus) {

        if (maintenanceList == null) {
            return;
        }

        maintenanceList
                .getChildren()
                .clear();

        try {

            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "FETCHING MAINTENANCE FROM FIRESTORE"
            );

            System.out.println(
                    "Required Status = "
                            + requiredStatus
            );

            // =================================================
            // FETCH FROM CONTROLLER
            // =================================================

            List<Maintenance> data =
                    maintenanceController
                            .getAllMaintenance();

            System.out.println(
                    "Total Maintenance Fetched = "
                            + (data == null
                            ? 0
                            : data.size())
            );

            System.out.println(
                    "=============================================="
            );

            boolean found = false;

            if (data != null) {

                for (Maintenance maintenance :
                        data) {

                    if (maintenance == null) {
                        continue;
                    }

                    String status =
                            maintenance.getStatus();

                    if (status == null) {
                        continue;
                    }

                    if (status
                            .trim()
                            .equalsIgnoreCase(
                                    requiredStatus.trim()
                            )) {

                        VBox card =
                                createMaintenance(
                                        maintenance
                                );

                        maintenanceList
                                .getChildren()
                                .add(card);

                        found = true;
                    }
                }
            }

            // =================================================
            // EMPTY
            // =================================================

            if (!found) {

                Label emptyLabel =
                        new Label(
                                "No "
                                        + requiredStatus
                                        + " maintenance records found."
                        );

                emptyLabel.setStyle(
                        "-fx-font-size:16px;" +
                        "-fx-text-fill:#555555;"
                );

                maintenanceList
                        .getChildren()
                        .add(
                                emptyLabel
                        );
            }

        } catch (Exception e) {

            e.printStackTrace();

            Label error =
                    new Label(
                            "Error loading maintenance records."
                    );

            error.setStyle(
                    "-fx-font-size:14px;" +
                    "-fx-text-fill:#B00020;"
            );

            maintenanceList
                    .getChildren()
                    .add(
                            error
                    );
        }
    }

    // =========================================================
    // ADD MAINTENANCE DIALOG
    // =========================================================

    private void openAddMaintenanceDialog() {

        StackPane overlay =
                new StackPane();

        overlay.setStyle(
                "-fx-background-color:rgba(0,0,0,0.5);"
        );

        VBox formBox =
                new VBox(12);

        formBox.setPadding(
                new Insets(25)
        );

        formBox.setMaxWidth(430);

        formBox.setMaxHeight(520);

        formBox.setStyle("""
                -fx-background-color:#ffffff;
                -fx-background-radius:20;
                -fx-effect:dropshadow(
                    three-pass-box,
                    rgba(0,0,0,0.3),
                    20,
                    0,
                    0,
                    5
                );
                """);

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
                        "Add Maintenance"
                );

        popupTitle.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
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

        closeBtn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#555555;" +
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

        closeBtn.setOnAction(
                e -> removeOverlay(overlay)
        );

        headerRow.getChildren().addAll(
                popupTitle,
                spacer,
                closeBtn
        );

        // =====================================================
        // INFO
        // =====================================================

        Label infoLabel =
                new Label(
                        "Maintenance will be added to all residents\n"
                                + "belonging to the logged-in Secretary's society."
                );

        infoLabel.setWrapText(true);

        infoLabel.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#555555;" +
                "-fx-background-color:#F3F4F6;" +
                "-fx-padding:10px;" +
                "-fx-background-radius:8;"
        );

        // =====================================================
        // PAYMENT
        // =====================================================

        Label paymentLabel =
                new Label("Payment");

        paymentLabel.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#555555;"
        );

        TextField paymentField =
                new TextField();

        paymentField.setPromptText(
                "Enter maintenance amount"
        );

        paymentField.setPrefHeight(40);

        // =====================================================
        // MONTH
        // =====================================================

        Label monthLabel =
                new Label("Month");

        monthLabel.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#555555;"
        );

        TextField monthField =
                new TextField();

        monthField.setPromptText(
                "Select date"
        );

        monthField.setPrefHeight(40);

        monthField.setEditable(false);

        // =====================================================
        // DATE
        // =====================================================

        Label dateLabel =
                new Label("Date");

        dateLabel.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#555555;"
        );

        DatePicker datePicker =
                new DatePicker();

        datePicker.setPrefHeight(40);

        datePicker.setMaxWidth(
                Double.MAX_VALUE
        );

        // =====================================================
        // FORMATTERS
        // =====================================================

        DateTimeFormatter monthFormatter =
                DateTimeFormatter.ofPattern(
                        "MMMM yyyy"
                );

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern(
                        "dd-MM-yyyy"
                );

        // =====================================================
        // DATE ACTION
        // =====================================================

        datePicker.setOnAction(e -> {

            LocalDate selectedDate =
                    datePicker.getValue();

            if (selectedDate != null) {

                monthField.setText(
                        selectedDate.format(
                                monthFormatter
                        )
                );
            }
        });

        // =====================================================
        // STATUS
        // =====================================================

        Label statusLabel =
                new Label("Status");

        statusLabel.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#555555;"
        );

        ComboBox<String> statusComboBox =
                new ComboBox<>();

        statusComboBox.getItems().addAll(
                "Pending",
                "Paid",
                "Overdue"
        );

        statusComboBox.setValue(
                "Pending"
        );

        statusComboBox.setPrefHeight(40);

        statusComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        // =====================================================
        // BUTTONS
        // =====================================================

        Button cancelButton =
                new Button("Cancel");

        cancelButton.setPrefWidth(100);
        cancelButton.setPrefHeight(40);

        cancelButton.setStyle(
                "-fx-background-color:#E5E7EB;" +
                "-fx-text-fill:#333333;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        Button saveButton =
                new Button("Save");

        saveButton.setPrefWidth(120);
        saveButton.setPrefHeight(40);

        saveButton.setStyle(
                "-fx-background-color:#2E9D63;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        HBox buttonBox =
                new HBox(10);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.getChildren().addAll(
                cancelButton,
                saveButton
        );

        // =====================================================
        // FORM
        // =====================================================

        formBox.getChildren().addAll(

                headerRow,

                infoLabel,

                paymentLabel,
                paymentField,

                monthLabel,
                monthField,

                dateLabel,
                datePicker,

                statusLabel,
                statusComboBox,

                buttonBox
        );

        // =====================================================
        // CANCEL
        // =====================================================

        cancelButton.setOnAction(
                e -> removeOverlay(overlay)
        );

        // =====================================================
        // SAVE
        // =====================================================

        saveButton.setOnAction(e -> {

            String amount =
                    paymentField
                            .getText()
                            .trim();

            String month =
                    monthField
                            .getText()
                            .trim();

            String status =
                    statusComboBox
                            .getValue();

            LocalDate selectedDate =
                    datePicker.getValue();

            // =================================================
            // VALIDATION
            // =================================================

            if (amount.isEmpty()) {

                showMessage(
                        "Please enter maintenance payment."
                );

                return;
            }

            try {

                double payment =
                        Double.parseDouble(
                                amount
                        );

                if (payment <= 0) {

                    showMessage(
                            "Payment must be greater than 0."
                    );

                    return;
                }

            } catch (NumberFormatException ex) {

                showMessage(
                        "Please enter a valid payment amount."
                );

                return;
            }

            if (selectedDate == null) {

                showMessage(
                        "Please select a date."
                );

                return;
            }

            if (month.isEmpty()) {

                showMessage(
                        "Please select a date."
                );

                return;
            }

            if (status == null ||
                    status.trim().isEmpty()) {

                showMessage(
                        "Please select status."
                );

                return;
            }

            String date =
                    selectedDate.format(
                            dateFormatter
                    );

            // =================================================
            // SAVE TO FIRESTORE
            // =================================================

            try {

                saveButton.setDisable(true);

                boolean success =
                        maintenanceController
                                .addMaintenanceToAllResidents(
                                        amount,
                                        month,
                                        date,
                                        status
                                );

                // =================================================
                // SUCCESS
                // =================================================

                if (success) {

                    /*
                     * IMPORTANT:
                     * Maintenance Firestore मध्ये save झाल्यावर
                     * लगेच पुन्हा getAllMaintenance() call होतो.
                     */

                    removeOverlay(
                            overlay
                    );

                    // =================================================
                    // REFRESH FROM FIRESTORE
                    // =================================================

                    currentStatus =
                            status.trim();

                    loadMaintenanceByStatus(
                            currentStatus
                    );

                    showMessage(
                            "Maintenance added successfully.\n\n"
                                    + "The latest maintenance records have been fetched from Firestore."
                    );

                } else {

                    saveButton.setDisable(false);

                    showMessage(
                            "Maintenance could not be added.\n\n"
                                    + "Please check Secretary society and Residents society data."
                    );
                }

            } catch (Exception ex) {

                ex.printStackTrace();

                saveButton.setDisable(false);

                showMessage(
                        "Error while adding maintenance:\n"
                                + ex.getMessage()
                );
            }
        });

        // =====================================================
        // ADD OVERLAY
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
    }

    // =========================================================
    // VIEW ALL MAINTENANCE
    // =========================================================

    private void openViewAllMaintenanceDialog() {

        StackPane overlay =
                new StackPane();

        overlay.setStyle(
                "-fx-background-color:rgba(0,0,0,0.5);"
        );

        VBox popup =
                new VBox(15);

        popup.setPadding(
                new Insets(25)
        );

        popup.setMaxWidth(650);

        popup.setMaxHeight(550);

        popup.setStyle("""
                -fx-background-color:#ffffff;
                -fx-background-radius:20;
                -fx-effect:dropshadow(
                    three-pass-box,
                    rgba(0,0,0,0.3),
                    20,
                    0,
                    0,
                    5
                );
                """);

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
                        "All Maintenance"
                );

        popupTitle.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#123C36;"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button closeTopBtn =
                new Button("✕");

        closeTopBtn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#555555;" +
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

        closeTopBtn.setOnAction(
                e -> removeOverlay(overlay)
        );

        headerRow.getChildren().addAll(
                popupTitle,
                spacer,
                closeTopBtn
        );

        // =====================================================
        // DATA
        // =====================================================

        VBox allMaintenance =
                new VBox(12);

        allMaintenance.setPadding(
                new Insets(5)
        );

        try {

            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "FETCHING ALL MAINTENANCE FROM FIRESTORE"
            );

            List<Maintenance> data =
                    maintenanceController
                            .getAllMaintenance();

            System.out.println(
                    "Fetched Records = "
                            + (data == null
                            ? 0
                            : data.size())
            );

            System.out.println(
                    "=============================================="
            );

            if (data != null &&
                    !data.isEmpty()) {

                for (Maintenance maintenance :
                        data) {

                    if (maintenance == null) {
                        continue;
                    }

                    allMaintenance
                            .getChildren()
                            .add(
                                    createMaintenance(
                                            maintenance
                                    )
                            );
                }

            } else {

                Label noData =
                        new Label(
                                "No maintenance records found for your society."
                        );

                noData.setStyle(
                        "-fx-font-size:15px;" +
                        "-fx-text-fill:#555555;"
                );

                allMaintenance
                        .getChildren()
                        .add(
                                noData
                        );
            }

        } catch (Exception e) {

            e.printStackTrace();

            Label error =
                    new Label(
                            "Error loading maintenance records."
                    );

            error.setStyle(
                    "-fx-text-fill:#B00020;"
            );

            allMaintenance
                    .getChildren()
                    .add(
                            error
                    );
        }

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane popupScroll =
                new ScrollPane();

        popupScroll.setContent(
                allMaintenance
        );

        popupScroll.setFitToWidth(true);

        popupScroll.setPrefHeight(400);

        popupScroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;"
        );

        // =====================================================
        // CLOSE
        // =====================================================

        Button closeBtn =
                new Button("Close");

        closeBtn.setPrefWidth(100);
        closeBtn.setPrefHeight(40);

        closeBtn.setStyle(
                "-fx-background-color:#434141;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        closeBtn.setOnAction(
                e -> removeOverlay(overlay)
        );

        HBox buttonBox =
                new HBox(closeBtn);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        popup.getChildren().addAll(
                headerRow,
                popupScroll,
                buttonBox
        );

        overlay.getChildren().add(
                popup
        );

        StackPane.setAlignment(
                popup,
                Pos.CENTER
        );

        rootStack.getChildren().add(
                overlay
        );
    }

    // =========================================================
    // MAINTENANCE CARD
    // =========================================================

    private VBox createMaintenance(
            Maintenance maintenance) {

        VBox card =
                new VBox(10);

        card.setPadding(
                new Insets(18)
        );

        card.setMaxWidth(1180);

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#EEEEEE;" +
                "-fx-border-radius:10;"
        );

        // =====================================================
        // PAYMENT
        // =====================================================

        Label amount =
                new Label(
                        "Payment: ₹"
                                + safeValue(
                                        maintenance.getAmount()
                                )
                );

        amount.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#123C36;"
        );

        // =====================================================
        // DETAILS
        // =====================================================

        Label details =
                new Label(
                        "Month: "
                                + safeValue(
                                        maintenance.getMonth()
                                )
                                + "    |    Date: "
                                + safeValue(
                                        maintenance.getDate()
                                )
                );

        details.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#777777;"
        );

        // =====================================================
        // STATUS
        // =====================================================

        String statusText =
                maintenance.getStatus();

        String statusBackground =
                "#FFF0D9";

        String statusColor =
                "#C47A20";

        if (statusText != null &&
                statusText.equalsIgnoreCase(
                        "Paid"
                )) {

            statusBackground =
                    "#E5F7EC";

            statusColor =
                    "#2E9D63";

        } else if (statusText != null &&
                statusText.equalsIgnoreCase(
                        "Overdue"
                )) {

            statusBackground =
                    "#FDE8E8";

            statusColor =
                    "#D9534F";
        }

        Label status =
                new Label(
                        safeValue(
                                statusText
                        )
                );

        status.setStyle(
                "-fx-background-color:"
                        + statusBackground
                        + ";" +
                "-fx-text-fill:"
                        + statusColor
                        + ";" +
                "-fx-font-size:10px;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:5px 10px;" +
                "-fx-background-radius:12;"
        );

        // =====================================================
        // BOTTOM
        // =====================================================

        HBox bottom =
                new HBox();

        bottom.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                details,
                Priority.ALWAYS
        );

        bottom.getChildren().addAll(
                details,
                status
        );

        // =====================================================
        // CARD
        // =====================================================

        card.getChildren().addAll(
                amount,
                bottom
        );

        return card;
    }

    // =========================================================
    // REMOVE OVERLAY
    // =========================================================

    private void removeOverlay(
            StackPane overlay) {

        if (rootStack != null &&
                overlay != null) {

            rootStack
                    .getChildren()
                    .remove(
                            overlay
                    );
        }
    }

    // =========================================================
    // SAFE VALUE
    // =========================================================

    private String safeValue(
            String value) {

        return value == null ||
                value.trim().isEmpty()
                ? "-"
                : value;
    }

    // =========================================================
    // MESSAGE
    // =========================================================

    private void showMessage(
            String message) {

        StackPane messageOverlay =
                new StackPane();

        messageOverlay.setStyle(
                "-fx-background-color:rgba(0,0,0,0.35);"
        );

        VBox box =
                new VBox(15);

        box.setPadding(
                new Insets(25)
        );

        box.setAlignment(
                Pos.CENTER
        );

        box.setMaxWidth(380);

        box.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:15;"
        );

        Label messageLabel =
                new Label(message);

        messageLabel.setWrapText(true);

        messageLabel.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#333333;"
        );

        Button okBtn =
                new Button("OK");

        okBtn.setPrefWidth(80);
        okBtn.setPrefHeight(35);

        okBtn.setStyle(
                "-fx-background-color:#434141;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        okBtn.setOnAction(
                e -> rootStack
                        .getChildren()
                        .remove(
                                messageOverlay
                        )
        );

        box.getChildren().addAll(
                messageLabel,
                okBtn
        );

        messageOverlay.getChildren().add(
                box
        );

        StackPane.setAlignment(
                box,
                Pos.CENTER
        );

        rootStack
                .getChildren()
                .add(
                        messageOverlay
                );
    }
}