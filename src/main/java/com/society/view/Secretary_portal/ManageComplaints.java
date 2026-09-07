package com.society.view.Secretary_portal;

import java.util.List;

import com.society.controller.Secretary_Controller.ComplaintController;
import com.society.model.Secretary_model.Complaint;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManageComplaints {

    private Scene manageComplaintsScene;

    private ComplaintController complaintController;

    // Current selected filter
    private String currentFilter = "ALL";

    // =====================================================
    // COLORS
    // =====================================================

    private static final String HEADER_COLOR =
            "#4E342E";

    private static final String PAGE_BACKGROUND =
            "#F7F7F7";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ManageComplaints() {

        complaintController =
                new ComplaintController();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "MANAGE COMPLAINTS"
        );

        System.out.println(
                "ComplaintController initialized"
        );

        System.out.println(
                "Society-wise complaint filtering enabled"
        );

        System.out.println(
                "======================================"
        );
    }

    // =====================================================
    // CREATE SCENE
    // =====================================================

    public Scene createScene(Stage stage) {

        // =================================================
        // SIDEBAR
        // =================================================

        SecretarySidebar sidebarObj =
                new SecretarySidebar();

        VBox sidebar =
                sidebarObj.createSidebar(stage);

        // =================================================
        // MAIN CONTAINER
        // =================================================

        VBox mainvb =
                new VBox(0);

        mainvb.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        /*
         * No unnecessary gap on left/right.
         * Header touches the edges.
         */
        mainvb.setPadding(
                new Insets(
                        0,
                        0,
                        20,
                        0
                )
        );

        mainvb.setPrefWidth(1220);

        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label(
                        "Manage Complaints"
                );

        title.setStyle(
                "-fx-font-size:20px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:white;"
        );

        // =================================================
        // SUBTITLE
        // =================================================

        Label subtitle =
                new Label(
                        "View and manage complaints submitted by residents of your society"
                );

        subtitle.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-text-fill:white;"
        );

        // =================================================
        // TITLE BOX
        // =================================================

        VBox titleBox =
                new VBox(5);

        titleBox.setAlignment(
                Pos.CENTER_LEFT
        );

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        HBox.setHgrow(
                titleBox,
                Priority.ALWAYS
        );

        // =================================================
        // REFRESH BUTTON
        // =================================================

        Button refreshBtn =
                new Button(
                        "↻ Refresh"
                );

        refreshBtn.setPrefWidth(105);

        refreshBtn.setPrefHeight(40);

        refreshBtn.setMinWidth(105);

        refreshBtn.setMinHeight(40);

        refreshBtn.setMaxWidth(105);

        refreshBtn.setMaxHeight(40);

        refreshBtn.setTooltip(
                new Tooltip(
                        "Refresh complaints"
                )
        );

        refreshBtn.setStyle(
                "-fx-background-color: #FFFFFF;"
                        + "-fx-text-fill:"
                        + HEADER_COLOR
                        + ";"
                        + "-fx-font-size:14px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:7;"
                        + "-fx-border-color:#FFFFFF;"
                        + "-fx-border-radius:7;"
                        + "-fx-cursor:hand;"
        );

        // =================================================
        // HEADER
        // HEIGHT = 80px
        // =================================================

        HBox complaintHeader =
                new HBox(10);

        complaintHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        complaintHeader.setPrefHeight(80);

        complaintHeader.setMinHeight(80);

        complaintHeader.setMaxHeight(80);

        complaintHeader.setMaxWidth(
                Double.MAX_VALUE
        );

        complaintHeader.setPadding(
                new Insets(
                        15,
                        20,
                        15,
                        20
                )
        );

        complaintHeader.setStyle(
                "-fx-background-color:"
                        + HEADER_COLOR
                        + ";"
        );

        // =================================================
        // HEADER CONTENT
        // =================================================

        complaintHeader.getChildren().addAll(
                titleBox,
                refreshBtn
        );

        // =================================================
        // CONTENT AREA
        // =================================================

        VBox contentArea =
                new VBox(15);

        contentArea.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        /*
         * Content has padding so that complaint cards
         * and tabs do not touch the sidebar.
         */
        contentArea.setPadding(
                new Insets(
                        20,
                        25,
                        0,
                        25
                )
        );

        // =================================================
        // BACKGROUND IMAGE
        // =================================================

        try {

            Image backgroundImage =
                    new Image(
                            getClass()
                                    .getResource(
                                            "/background-Dashboard5.jpeg"
                                    )
                                    .toExternalForm()
                    );

            BackgroundImage bgImage =
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

            contentArea.setBackground(
                    new Background(bgImage)
            );

        } catch (Exception e) {

            System.err.println(
                    "Background image could not be loaded: "
                            + e.getMessage()
            );

            contentArea.setStyle(
                    "-fx-background-color:"
                            + PAGE_BACKGROUND
                            + ";"
            );
        }

        // =================================================
        // SOCIETY INFO
        // =================================================

        Label societyInfo =
                new Label(
                        "Complaints from: Your Society Residents"
                );

        societyInfo.setStyle(
                "-fx-font-size:12px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#123C36;"
                        + "-fx-background-color:rgba(255,255,255,0.80);"
                        + "-fx-padding:8 12 8 12;"
                        + "-fx-background-radius:6;"
        );

        // =================================================
        // STATUS BUTTONS
        // =================================================

        Button allBtn =
                new Button("All");

        Button progressBtn =
                new Button("In Progress");

        Button resolvedBtn =
                new Button("Resolved");

        Button closedBtn =
                new Button("Closed");

        allBtn.setPrefWidth(130);

        progressBtn.setPrefWidth(150);

        resolvedBtn.setPrefWidth(130);

        closedBtn.setPrefWidth(130);

        allBtn.setPrefHeight(40);

        progressBtn.setPrefHeight(40);

        resolvedBtn.setPrefHeight(40);

        closedBtn.setPrefHeight(40);

        // =================================================
        // TAB STYLES
        // =================================================

        String normalStyle =
                "-fx-background-color:transparent;"
                        + "-fx-text-fill:#777777;"
                        + "-fx-font-weight:bold;"
                        + "-fx-font-size:12px;";

        String activeStyle =
                "-fx-background-color:transparent;"
                        + "-fx-text-fill:#3E2723;"
                        + "-fx-font-weight:bold;"
                        + "-fx-font-size:12px;"
                        + "-fx-border-color:#3E2723;"
                        + "-fx-border-width:0 0 2 0;";

        allBtn.setStyle(
                activeStyle
        );

        progressBtn.setStyle(
                normalStyle
        );

        resolvedBtn.setStyle(
                normalStyle
        );

        closedBtn.setStyle(
                normalStyle
        );

        HBox tabs =
                new HBox(20);

        tabs.setAlignment(
                Pos.CENTER_LEFT
        );

        tabs.getChildren().addAll(
                allBtn,
                progressBtn,
                resolvedBtn,
                closedBtn
        );

        // =================================================
        // COMPLAINT LIST
        // =================================================

        VBox complaintList =
                new VBox(15);

        complaintList.setPadding(
                new Insets(
                        5,
                        0,
                        5,
                        0
                )
        );

        // =================================================
        // INITIAL FETCH
        // =================================================

        loadComplaintsFromFirestore(
                complaintList
        );

        // =================================================
        // SCROLL PANE
        // =================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                complaintList
        );

        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(480);

        /*
         * Completely invisible scrollbars.
         * Scrolling still works using mouse wheel.
         */
        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setStyle(
                "-fx-background-color:transparent;"
                        + "-fx-background:transparent;"
                        + "-fx-border-color:transparent;"
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        // =================================================
        // ALL BUTTON
        // =================================================

        allBtn.setOnAction(e -> {

            currentFilter =
                    "ALL";

            System.out.println(
                    "Selected filter = ALL"
            );

            loadComplaintsFromFirestore(
                    complaintList
            );

            setActiveTab(
                    allBtn,
                    progressBtn,
                    resolvedBtn,
                    closedBtn,
                    activeStyle,
                    normalStyle
            );
        });

        // =================================================
        // IN PROGRESS BUTTON
        // =================================================

        progressBtn.setOnAction(e -> {

            currentFilter =
                    "IN PROGRESS";

            System.out.println(
                    "Selected filter = IN PROGRESS"
            );

            loadComplaintsFromFirestore(
                    complaintList
            );

            setActiveTab(
                    progressBtn,
                    allBtn,
                    resolvedBtn,
                    closedBtn,
                    activeStyle,
                    normalStyle
            );
        });

        // =================================================
        // RESOLVED BUTTON
        // =================================================

        resolvedBtn.setOnAction(e -> {

            currentFilter =
                    "RESOLVED";

            System.out.println(
                    "Selected filter = RESOLVED"
            );

            loadComplaintsFromFirestore(
                    complaintList
            );

            setActiveTab(
                    resolvedBtn,
                    allBtn,
                    progressBtn,
                    closedBtn,
                    activeStyle,
                    normalStyle
            );
        });

        // =================================================
        // CLOSED BUTTON
        // =================================================

        closedBtn.setOnAction(e -> {

            currentFilter =
                    "CLOSED";

            System.out.println(
                    "Selected filter = CLOSED"
            );

            loadComplaintsFromFirestore(
                    complaintList
            );

            setActiveTab(
                    closedBtn,
                    allBtn,
                    progressBtn,
                    resolvedBtn,
                    activeStyle,
                    normalStyle
            );
        });

        // =================================================
        // REFRESH BUTTON
        // =================================================

        refreshBtn.setOnAction(e -> {

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "REFRESHING COMPLAINTS"
            );

            System.out.println(
                    "Current Filter = "
                            + currentFilter
            );

            System.out.println(
                    "Society-wise filtering = ENABLED"
            );

            System.out.println(
                    "======================================"
            );

            loadComplaintsFromFirestore(
                    complaintList
            );

            // ---------------------------------------------
            // Restore active tab
            // ---------------------------------------------

            if (currentFilter.equals("ALL")) {

                setActiveTab(
                        allBtn,
                        progressBtn,
                        resolvedBtn,
                        closedBtn,
                        activeStyle,
                        normalStyle
                );

            } else if (
                    currentFilter.equals(
                            "IN PROGRESS"
                    )) {

                setActiveTab(
                        progressBtn,
                        allBtn,
                        resolvedBtn,
                        closedBtn,
                        activeStyle,
                        normalStyle
                );

            } else if (
                    currentFilter.equals(
                            "RESOLVED"
                    )) {

                setActiveTab(
                        resolvedBtn,
                        allBtn,
                        progressBtn,
                        closedBtn,
                        activeStyle,
                        normalStyle
                );

            } else if (
                    currentFilter.equals(
                            "CLOSED"
                    )) {

                setActiveTab(
                        closedBtn,
                        allBtn,
                        progressBtn,
                        resolvedBtn,
                        activeStyle,
                        normalStyle
                );
            }

            System.out.println(
                    "Complaints refreshed."
            );
        });

        // =================================================
        // VIEW ALL BUTTON
        // =================================================

        Button viewAllBtn =
                new Button(
                        "View All Complaints"
                );

        viewAllBtn.setMaxWidth(
                Double.MAX_VALUE
        );

        viewAllBtn.setPrefHeight(40);

        viewAllBtn.setStyle(
                "-fx-background-color:"
                        + HEADER_COLOR
                        + ";"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:7;"
                        + "-fx-cursor:hand;"
        );

        viewAllBtn.setOnAction(
                e -> showAllComplaintsPopup()
        );

        // =================================================
        // MAIN CONTENT
        // =================================================

        contentArea.getChildren().addAll(
                societyInfo,
                tabs,
                scrollPane,
                viewAllBtn
        );

        VBox.setVgrow(
                contentArea,
                Priority.ALWAYS
        );

        // =================================================
        // MAIN CONTAINER CONTENT
        // =================================================

        mainvb.getChildren().addAll(
                complaintHeader,
                contentArea
        );

        VBox.setVgrow(
                contentArea,
                Priority.ALWAYS
        );

        // =================================================
        // ROOT
        // =================================================

        HBox root =
                new HBox();

        root.getChildren().addAll(
                sidebar,
                mainvb
        );

        root.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        root.setStyle(
                "-fx-background-color:"
                        + PAGE_BACKGROUND
                        + ";"
        );

        HBox.setHgrow(
                mainvb,
                Priority.ALWAYS
        );

        // =================================================
        // SCENE
        // =================================================

        Scene scene =
                new Scene(
                        root,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        manageComplaintsScene =
                scene;

        return manageComplaintsScene;
    }

    // =====================================================
    // LOAD COMPLAINTS FROM FIRESTORE
    // =====================================================

    private void loadComplaintsFromFirestore(
            VBox complaintList) {

        try {

            List<Complaint> complaints;

            // -------------------------------------------------
            // ALL
            // -------------------------------------------------

            if (currentFilter.equals("ALL")) {

                complaints =
                        complaintController
                                .getAllComplaints();

            }

            // -------------------------------------------------
            // FILTERED BY STATUS
            // -------------------------------------------------

            else {

                complaints =
                        complaintController
                                .getAllComplaintsByStatus(
                                        currentFilter
                                );
            }

            System.out.println(
                    "--------------------------------------"
            );

            System.out.println(
                    "COMPLAINT FETCH RESULT"
            );

            System.out.println(
                    "Filter = "
                            + currentFilter
            );

            System.out.println(
                    "Complaints received by UI = "
                            +
                            (
                                    complaints == null
                                            ? "NULL"
                                            : complaints.size()
                            )
            );

            System.out.println(
                    "--------------------------------------"
            );

            loadComplaints(
                    complaintList,
                    complaints
            );

        } catch (Exception e) {

            System.err.println(
                    "ERROR LOADING COMPLAINTS IN UI"
            );

            e.printStackTrace();

            complaintList.getChildren().clear();

            Label error =
                    new Label(
                            "Error loading complaints. Check console."
                    );

            error.setStyle(
                    "-fx-font-size:14px;"
                            + "-fx-text-fill:#B00020;"
                            + "-fx-font-weight:bold;"
            );

            complaintList.getChildren().add(
                    error
            );
        }
    }

    // =====================================================
    // LOAD COMPLAINTS
    // =====================================================

    private void loadComplaints(
            VBox complaintList,
            List<Complaint> complaints) {

        complaintList.getChildren().clear();

        // -------------------------------------------------
        // NULL
        // -------------------------------------------------

        if (complaints == null) {

            Label error =
                    new Label(
                            "Unable to fetch complaints from Firestore."
                    );

            error.setStyle(
                    "-fx-font-size:14px;"
                            + "-fx-text-fill:#B00020;"
                            + "-fx-font-weight:bold;"
            );

            complaintList.getChildren().add(
                    error
            );

            return;
        }

        // -------------------------------------------------
        // EMPTY
        // -------------------------------------------------

        if (complaints.isEmpty()) {

            Label noData =
                    new Label(
                            "No complaints found for your society."
                    );

            noData.setStyle(
                    "-fx-font-size:14px;"
                            + "-fx-text-fill:#777777;"
                            + "-fx-background-color:rgba(255,255,255,0.80);"
                            + "-fx-padding:10;"
            );

            complaintList.getChildren().add(
                    noData
            );

            return;
        }

        // -------------------------------------------------
        // ADD CARDS
        // -------------------------------------------------

        int count = 0;

        for (Complaint complaint :
                complaints) {

            if (complaint == null) {
                continue;
            }

            complaintList.getChildren().add(
                    createComplaint(
                            complaint
                    )
            );

            count++;
        }

        System.out.println(
                "Complaint cards displayed = "
                        + count
        );
    }

    // =====================================================
    // CREATE COMPLAINT CARD
    // =====================================================

    private VBox createComplaint(
            Complaint complaint) {

        VBox card =
                new VBox(8);

        card.setPadding(
                new Insets(18)
        );

        card.setMaxWidth(1180);

        card.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:10;"
                        + "-fx-border-color:#EEEEEE;"
                        + "-fx-border-radius:10;"
        );

        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label(
                        safeValue(
                                complaint.getTitle()
                        )
                );

        title.setStyle(
                "-fx-font-size:15px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#123C36;"
        );

        // =================================================
        // EMAIL
        // =================================================

        Label email =
                new Label(
                        "Email: "
                                +
                                safeValue(
                                        complaint.getEmail()
                                )
                );

        email.setStyle(
                "-fx-font-size:11px;"
                        + "-fx-text-fill:#555555;"
        );

        // =================================================
        // SOCIETY
        // =================================================

        Label society =
                new Label(
                        "Society: "
                                +
                                safeValue(
                                        complaint.getSociety()
                                )
                );

        society.setStyle(
                "-fx-font-size:11px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#123C36;"
        );

        // =================================================
        // CATEGORY
        // =================================================

        Label category =
                new Label(
                        "Category: "
                                +
                                safeValue(
                                        complaint.getCategory()
                                )
                );

        category.setStyle(
                "-fx-font-size:11px;"
                        + "-fx-text-fill:#555555;"
        );

        // =================================================
        // DESCRIPTION
        // =================================================

        Label description =
                new Label(
                        "Description: "
                                +
                                safeValue(
                                        complaint.getDescription()
                                )
                );

        description.setWrapText(true);

        description.setMaxWidth(1050);

        description.setStyle(
                "-fx-font-size:11px;"
                        + "-fx-text-fill:#777777;"
        );

        // =================================================
        // DETAILS
        // =================================================

        Label details =
                new Label(
                        "Flat: "
                                +
                                safeValue(
                                        complaint.getFlatNumber()
                                )
                                +
                                "    |    Preferred Date: "
                                +
                                safeValue(
                                        complaint.getPreferredDate()
                                )
                );

        details.setStyle(
                "-fx-font-size:11px;"
                        + "-fx-text-fill:#777777;"
        );

        // =================================================
        // STATUS LABEL
        // =================================================

        Label statusLabel =
                new Label(
                        "Status:"
                );

        statusLabel.setStyle(
                "-fx-font-size:11px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#555555;"
        );

        // =================================================
        // STATUS COMBO
        // =================================================

        ComboBox<String> statusCombo =
                new ComboBox<>();

        statusCombo.getItems().addAll(
                "IN PROGRESS",
                "RESOLVED",
                "CLOSED"
        );

        String initialStatus =
                normalizeStatus(
                        complaint.getStatus()
                );

        statusCombo.setValue(
                initialStatus
        );

        statusCombo.setPrefWidth(150);

        statusCombo.setPrefHeight(32);

        statusCombo.setStyle(
                "-fx-font-size:11px;"
                        + "-fx-font-weight:bold;"
        );

        // =================================================
        // STATUS CHANGE
        // =================================================

        statusCombo.setOnAction(e -> {

            String selectedStatus =
                    statusCombo.getValue();

            if (selectedStatus == null) {
                return;
            }

            String complaintEmail =
                    complaint.getEmail();

            String complaintId =
                    complaint.getId();

            // -------------------------------------------------
            // VALIDATE EMAIL
            // -------------------------------------------------

            if (complaintEmail == null ||
                    complaintEmail.trim().isEmpty()) {

                showErrorAlert(
                        "Complaint email is missing."
                );

                statusCombo.setValue(
                        normalizeStatus(
                                complaint.getStatus()
                        )
                );

                return;
            }

            // -------------------------------------------------
            // VALIDATE ID
            // -------------------------------------------------

            if (complaintId == null ||
                    complaintId.trim().isEmpty()) {

                showErrorAlert(
                        "Complaint ID is missing."
                );

                statusCombo.setValue(
                        normalizeStatus(
                                complaint.getStatus()
                        )
                );

                return;
            }

            String oldStatus =
                    normalizeStatus(
                            complaint.getStatus()
                    );

            // -------------------------------------------------
            // SAME STATUS
            // -------------------------------------------------

            if (oldStatus.equalsIgnoreCase(
                    selectedStatus
            )) {

                return;
            }

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "COMPLAINT STATUS CHANGE"
            );

            System.out.println(
                    "Resident Email = "
                            + complaintEmail
            );

            System.out.println(
                    "Complaint ID = "
                            + complaintId
            );

            System.out.println(
                    "Complaint Society = "
                            + safeValue(
                                    complaint.getSociety()
                            )
            );

            System.out.println(
                    "Old Status = "
                            + oldStatus
            );

            System.out.println(
                    "New Status = "
                            + selectedStatus
            );

            System.out.println(
                    "======================================"
            );

            // -------------------------------------------------
            // UPDATE FIRESTORE
            // -------------------------------------------------

            boolean success =
                    complaintController
                            .updateComplaintStatus(
                                    complaintEmail,
                                    complaintId,
                                    selectedStatus
                            );

            // -------------------------------------------------
            // SUCCESS
            // -------------------------------------------------

            if (success) {

                complaint.setStatus(
                        selectedStatus
                );

                statusCombo.setValue(
                        selectedStatus
                );

                showSuccessAlert(
                        "Complaint status changed to "
                                + selectedStatus
                );

            }

            // -------------------------------------------------
            // FAILURE
            // -------------------------------------------------

            else {

                statusCombo.setValue(
                        oldStatus
                );

                complaint.setStatus(
                        oldStatus
                );

                showErrorAlert(
                        "Failed to update complaint status in Firestore."
                );
            }
        });

        // =================================================
        // STATUS BOX
        // =================================================

        HBox statusBox =
                new HBox(8);

        statusBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        statusBox.getChildren().addAll(
                statusLabel,
                statusCombo
        );

        // =================================================
        // BOTTOM
        // =================================================

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
                statusBox
        );

        // =================================================
        // CARD CONTENT
        // =================================================

        card.getChildren().addAll(
                title,
                email,
                society,
                category,
                description,
                bottom
        );

        return card;
    }

    // =====================================================
    // SET ACTIVE TAB
    // =====================================================

    private void setActiveTab(
            Button active,
            Button b1,
            Button b2,
            Button b3,
            String activeStyle,
            String normalStyle) {

        active.setStyle(
                activeStyle
        );

        b1.setStyle(
                normalStyle
        );

        b2.setStyle(
                normalStyle
        );

        b3.setStyle(
                normalStyle
        );
    }

    // =====================================================
    // NORMALIZE STATUS
    // =====================================================

    private String normalizeStatus(
            String status) {

        if (status == null ||
                status.trim().isEmpty()) {

            return "IN PROGRESS";
        }

        String value =
                status.trim()
                        .toUpperCase();

        switch (value) {

            case "OPEN":
                return "IN PROGRESS";

            case "IN PROGRESS":
                return "IN PROGRESS";

            case "RESOLVED":
                return "RESOLVED";

            case "CLOSED":
                return "CLOSED";

            default:
                return "IN PROGRESS";
        }
    }

    // =====================================================
    // SUCCESS ALERT
    // =====================================================

    private void showSuccessAlert(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Complaint Updated"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        alert.show();
    }

    // =====================================================
    // ERROR ALERT
    // =====================================================

    private void showErrorAlert(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Complaint Error"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        alert.show();
    }

    // =====================================================
    // VIEW ALL COMPLAINTS POPUP
    // =====================================================

    private void showAllComplaintsPopup() {

        try {

            /*
             * IMPORTANT:
             *
             * getAllComplaints() already performs
             * society-wise filtering.
             *
             * Therefore this popup will also show
             * ONLY complaints from the logged-in
             * Secretary's society.
             */

            List<Complaint> complaints =
                    complaintController
                            .getAllComplaints();

            Stage popupStage =
                    new Stage();

            VBox popup =
                    new VBox(15);

            popup.setPadding(
                    new Insets(25)
            );

            popup.setPrefWidth(800);

            popup.setPrefHeight(650);

            popup.setStyle(
                    "-fx-background-color:white;"
            );

            // =================================================
            // TITLE
            // =================================================

            Label popupTitle =
                    new Label(
                            "All Resident Complaints"
                    );

            popupTitle.setStyle(
                    "-fx-font-size:24px;"
                            + "-fx-font-weight:bold;"
                            + "-fx-text-fill:#123C36;"
            );

            // =================================================
            // SOCIETY INFO
            // =================================================

            Label popupSociety =
                    new Label(
                            "Complaints from: Your Society Residents"
                    );

            popupSociety.setStyle(
                    "-fx-font-size:12px;"
                            + "-fx-font-weight:bold;"
                            + "-fx-text-fill:#777777;"
            );

            // =================================================
            // LIST
            // =================================================

            VBox allComplaints =
                    new VBox(12);

            if (complaints == null ||
                    complaints.isEmpty()) {

                Label noData =
                        new Label(
                                "No complaints found for your society."
                        );

                noData.setStyle(
                        "-fx-font-size:14px;"
                                + "-fx-text-fill:#777777;"
                );

                allComplaints.getChildren().add(
                        noData
                );

            } else {

                for (Complaint complaint :
                        complaints) {

                    if (complaint != null) {

                        allComplaints.getChildren().add(
                                createComplaint(
                                        complaint
                                )
                        );
                    }
                }
            }

            // =================================================
            // SCROLL
            // =================================================

            ScrollPane scroll =
                    new ScrollPane();

            scroll.setContent(
                    allComplaints
            );

            scroll.setFitToWidth(true);

            scroll.setPrefHeight(500);

            // =================================================
            // CLOSE
            // =================================================

            Button closeBtn =
                    new Button(
                            "Close"
                    );

            closeBtn.setPrefWidth(100);

            closeBtn.setPrefHeight(40);

            closeBtn.setStyle(
                    "-fx-background-color:"
                            + HEADER_COLOR
                            + ";"
                            + "-fx-text-fill:white;"
                            + "-fx-font-weight:bold;"
                            + "-fx-background-radius:8;"
                            + "-fx-cursor:hand;"
            );

            closeBtn.setOnAction(
                    e -> popupStage.close()
            );

            HBox buttonBox =
                    new HBox(
                            closeBtn
                    );

            buttonBox.setAlignment(
                    Pos.CENTER_RIGHT
            );

            // =================================================
            // POPUP CONTENT
            // =================================================

            popup.getChildren().addAll(
                    popupTitle,
                    popupSociety,
                    scroll,
                    buttonBox
            );

            Scene popupScene =
                    new Scene(
                            popup
                    );

            popupStage.setTitle(
                    "All Resident Complaints"
            );

            popupStage.setScene(
                    popupScene
            );

            popupStage.setResizable(
                    false
            );

            popupStage.show();

        } catch (Exception e) {

            System.err.println(
                    "ERROR OPENING COMPLAINT POPUP"
            );

            e.printStackTrace();

            showErrorAlert(
                    "Unable to load complaints."
            );
        }
    }

    // =====================================================
    // SAFE VALUE
    // =====================================================

    private String safeValue(
            String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "-";
        }

        return value;
    }
}