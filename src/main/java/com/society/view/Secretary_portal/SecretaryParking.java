package com.society.view.Secretary_portal;

import com.society.controller.Secretary_Controller.SecretaryParkingController;
import com.society.model.Secretary_model.SecretaryParking_model.AssignedParking;
import com.society.model.Secretary_model.SecretaryParking_model.ParkingMember;
import com.society.model.Secretary_model.SecretaryParking_model.ParkingSlot;
import com.society.service.resident_service.SecretarySession;
import com.society.view.ScreenSize;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SecretaryParking {

    // ============================================================
    // CONTROLLER
    // ============================================================

    private final SecretaryParkingController controller;

    // ============================================================
    // SECRETARY
    // ============================================================

    private String secretaryEmail;
    private String societyName = "";

    // ============================================================
    // DATA
    // ============================================================

    private final List<ParkingSlot> parkingSlots =
            new ArrayList<>();

    private final List<ParkingMember> societyMembers =
            new ArrayList<>();

    private final List<AssignedParking> assignedParkings =
            new ArrayList<>();

    // ============================================================
    // UI
    // ============================================================

    private BorderPane root;

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    // ============================================================
    // BACKGROUND IMAGE
    // ============================================================

    private static final String BACKGROUND_IMAGE =
            "/background-Dashboard5.jpeg";

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public SecretaryParking() {

        secretaryEmail =
                SecretarySession.getLoggedInEmail();

        if (secretaryEmail == null ||
                secretaryEmail.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Secretary email cannot be empty. " +
                    "Please login again."
            );
        }

        secretaryEmail =
                secretaryEmail.trim().toLowerCase();

        controller =
                new SecretaryParkingController();

        System.out.println(
                "================================================"
        );

        System.out.println(
                "SecretaryParking opened"
        );

        System.out.println(
                "Secretary Email = " +
                        secretaryEmail
        );

        System.out.println(
                "================================================"
        );
    }

    // ============================================================
    // PARKING SCENE
    // ============================================================

    public Scene getParkingScene(Stage stage) {

        // --------------------------------------------------------
        // ROOT
        // --------------------------------------------------------

        root = new BorderPane();

        root.setStyle(
                "-fx-background-color: #E8DED7;"
        );

        // --------------------------------------------------------
        // COMMON SECRETARY SIDEBAR
        // --------------------------------------------------------

        SecretarySidebar sidebarObj =
                new SecretarySidebar();

        root.setLeft(
                sidebarObj.createSidebar(stage)
        );

        // --------------------------------------------------------
        // LOADING SCREEN
        // --------------------------------------------------------

        VBox loadingBox =
                new VBox(15);

        loadingBox.setAlignment(
                Pos.CENTER
        );

        loadingBox.setPadding(
                new Insets(30)
        );

        loadingBox.setStyle(
                "-fx-background-color: #E8DED7;"
        );

        Label loading =
                new Label(
                        "Loading parking data..."
                );

        loading.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18
                )
        );

        loading.setTextFill(
                Color.WHITE
        );

        ProgressIndicator progress =
                new ProgressIndicator();

        progress.setPrefSize(
                45,
                45
        );

        loadingBox.getChildren()
                .addAll(
                        progress,
                        loading
                );

        root.setCenter(
                loadingBox
        );

        // --------------------------------------------------------
        // SCENE
        // --------------------------------------------------------

        Scene scene =
                new Scene(
                        root,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        // --------------------------------------------------------
        // LOAD DATA
        // --------------------------------------------------------

        loadParkingData();

        return scene;
    }

    // ============================================================
    // LOAD PARKING DATA
    // ============================================================

    private void loadParkingData() {

        if (executor.isShutdown()) {

            System.out.println(
                    "Parking executor is already shut down."
            );

            return;
        }

        executor.submit(() -> {

            try {

                System.out.println(
                        "========== LOADING PARKING DATA =========="
                );

                // ------------------------------------------------
                // SOCIETY
                // ------------------------------------------------

                societyName =
                        controller.getSecretarySociety(
                                secretaryEmail
                        );

                if (societyName == null ||
                        societyName.trim().isEmpty()) {

                    throw new IllegalStateException(
                            "No society is assigned to secretary: "
                                    + secretaryEmail
                    );
                }

                societyName =
                        societyName.trim();

                System.out.println(
                        "Parking Society = "
                                + societyName
                );

                // ------------------------------------------------
                // MEMBERS
                // ------------------------------------------------

                List<ParkingMember> members =
                        controller.getSocietyMembers(
                                societyName
                        );

                if (members == null) {
                    members = new ArrayList<>();
                }

                // ------------------------------------------------
                // PARKING SLOTS
                // ------------------------------------------------

                List<ParkingSlot> slots =
                        controller.getParkingSlots(
                                societyName
                        );

                if (slots == null) {
                    slots = new ArrayList<>();
                }

                // ------------------------------------------------
                // ASSIGNED PARKING
                // ------------------------------------------------

                List<AssignedParking> assignments =
                        controller.getAssignedParking(
                                societyName
                        );

                if (assignments == null) {
                    assignments = new ArrayList<>();
                }

                System.out.println(
                        "Members     = "
                                + members.size()
                );

                System.out.println(
                        "Slots       = "
                                + slots.size()
                );

                System.out.println(
                        "Assignments = "
                                + assignments.size()
                );

                // ------------------------------------------------
                // UPDATE UI
                // ------------------------------------------------

                final List<ParkingMember> finalMembers =
                        members;

                final List<ParkingSlot> finalSlots =
                        slots;

                final List<AssignedParking> finalAssignments =
                        assignments;

                Platform.runLater(() -> {

                    societyMembers.clear();

                    societyMembers.addAll(
                            finalMembers
                    );

                    parkingSlots.clear();

                    parkingSlots.addAll(
                            finalSlots
                    );

                    assignedParkings.clear();

                    assignedParkings.addAll(
                            finalAssignments
                    );

                    if (root != null) {

                        root.setCenter(
                                createMainContent()
                        );
                    }
                });

                System.out.println(
                        "========== PARKING DATA LOADED =========="
                );

            } catch (Exception e) {

                e.printStackTrace();

                Platform.runLater(() ->
                        showWarning(
                                "Unable to load parking data.\n\n"
                                        + safeMessage(e)
                        )
                );
            }
        });
    }

    // ============================================================
    // MAIN CONTENT
    // ============================================================

    private VBox createMainContent() {

        VBox main =
                new VBox();

        main.setFillWidth(true);

        main.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        // ========================================================
        // BACKGROUND IMAGE
        // ========================================================

        try {

            if (getClass().getResource(
                    BACKGROUND_IMAGE
            ) != null) {

                Image backgroundImage =
                        new Image(
                                getClass()
                                        .getResource(
                                                BACKGROUND_IMAGE
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

                main.setBackground(
                        new Background(
                                bgImage
                        )
                );

            } else {

                System.err.println(
                        "Background image not found: "
                                + BACKGROUND_IMAGE
                );

                main.setStyle(
                        "-fx-background-color:#E8DED7;"
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "Background image could not be loaded: "
                            + e.getMessage()
            );

            main.setStyle(
                    "-fx-background-color:#E8DED7;"
            );
        }

        // ========================================================
        // HEADER
        // ========================================================

        VBox header =
                createHeader();

        // ========================================================
        // CONTENT SCROLL
        // ========================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setFitToWidth(true);

        scrollPane.setFitToHeight(false);

        scrollPane.setPannable(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;" +
                "-fx-border-color:transparent;" +
                "-fx-padding:0;"
        );

        VBox parkingContent =
                new VBox();

        parkingContent.setFillWidth(true);

        parkingContent.setPadding(
                new Insets(28)
        );

        parkingContent.setSpacing(25);

        // IMPORTANT:
        // Transparent so background image of main is visible
        parkingContent.setStyle(
                "-fx-background-color:transparent;"
        );

        parkingContent.getChildren()
                .addAll(
                        createOverviewSection(),
                        createParkingSlotsSection(),
                        createVisitorParkingSection(),
                        createAssignedParkingSection()
                );

        scrollPane.setContent(
                parkingContent
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        main.getChildren()
                .addAll(
                        header,
                        scrollPane
                );

        return main;
    }

    // ============================================================
    // HEADER - FIXED 80 HEIGHT
    // ============================================================

    private VBox createHeader() {

        VBox header =
                new VBox();

        header.setMinHeight(80);
        header.setPrefHeight(80);
        header.setMaxHeight(80);

        header.setPadding(
                new Insets(
                        10,
                        25,
                        10,
                        25
                )
        );

        header.setSpacing(2);

        header.setFillWidth(true);

        header.setStyle(
                "-fx-background-color: #4e342e;" +
                "-fx-border-color:#3E2723;" +
                "-fx-border-width:0 0 1 0;"
        );

        // --------------------------------------------------------
        // TITLE
        // --------------------------------------------------------

        Label title =
                new Label(
                        "Smart Parking"
                );

        title.setTextFill(
                Color.WHITE
        );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        24
                )
        );

        // --------------------------------------------------------
        // SUBTITLE
        // --------------------------------------------------------

        Label subtitle =
                new Label(
                        "Manage parking slots and resident/owner parking assignments"
                );

        subtitle.setTextFill(
                Color.WHITE
        );

        subtitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.NORMAL,
                        13
                )
        );

        subtitle.setWrapText(true);

        // --------------------------------------------------------
        // SOCIETY
        // --------------------------------------------------------

        Label societyLabel =
                new Label(
                        "Society: "
                                + safe(societyName)
                );

        societyLabel.setTextFill(
                Color.WHITE
        );

        societyLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13
                )
        );

        // --------------------------------------------------------
        // HEADER ROW
        // --------------------------------------------------------

        HBox headerTop =
                new HBox();

        headerTop.setAlignment(
                Pos.CENTER_LEFT
        );

        headerTop.setSpacing(15);

        headerTop.getChildren()
                .add(
                        title
                );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        headerTop.getChildren()
                .add(
                        spacer
                );

        headerTop.getChildren()
                .add(
                        societyLabel
                );

        header.getChildren()
                .addAll(
                        headerTop,
                        subtitle
                );

        return header;
    }

    // ============================================================
    // OVERVIEW
    // ============================================================

    private VBox createOverviewSection() {

        VBox section =
                new VBox();

        section.setSpacing(12);

        Label heading =
                new Label(
                        "Parking Overview"
                );

        heading.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22
                )
        );

        heading.setTextFill(
                Color.web("#18324A")
        );

        HBox cards =
                new HBox();

        cards.setSpacing(18);

        cards.setAlignment(
                Pos.CENTER_LEFT
        );

        int total =
                parkingSlots.size();

        int available =
                countSlots("Available");

        int occupied =
                countSlots("Occupied");

        int assigned =
                countSlots("Assigned");

        cards.getChildren()
                .addAll(

                        createOverviewCard(
                                "Total Slots",
                                String.valueOf(total)
                        ),

                        createOverviewCard(
                                "Available",
                                String.valueOf(available)
                        ),

                        createOverviewCard(
                                "Occupied",
                                String.valueOf(occupied)
                        ),

                        createOverviewCard(
                                "Assigned",
                                String.valueOf(assigned)
                        )
                );

        section.getChildren()
                .addAll(
                        heading,
                        cards
                );

        return section;
    }

    // ============================================================
    // COUNT SLOTS
    // ============================================================

    private int countSlots(
            String status) {

        int count = 0;

        for (ParkingSlot slot :
                parkingSlots) {

            if (slot == null) {
                continue;
            }

            if (slot.isVisitorSlot()) {
                continue;
            }

            if (slot.getStatus() != null &&
                    slot.getStatus()
                            .equalsIgnoreCase(status)) {

                count++;
            }
        }

        return count;
    }

    // ============================================================
    // COUNT VISITOR SLOTS
    // ============================================================

    private int countVisitorSlots() {

        int count = 0;

        for (ParkingSlot slot :
                parkingSlots) {

            if (slot != null &&
                    slot.isVisitorSlot()) {

                count++;
            }
        }

        return count;
    }

    // ============================================================
    // COUNT AVAILABLE VISITOR SLOTS
    // ============================================================

    private int countAvailableVisitorSlots() {

        int count = 0;

        for (ParkingSlot slot :
                parkingSlots) {

            if (slot != null &&
                    slot.isVisitorSlot() &&
                    slot.getStatus() != null &&
                    slot.getStatus()
                            .equalsIgnoreCase("Available")) {

                count++;
            }
        }

        return count;
    }

    // ============================================================
    // OVERVIEW CARD
    // ============================================================

    private VBox createOverviewCard(
            String title,
            String value) {

        VBox card =
                new VBox();

        card.setAlignment(
                Pos.CENTER
        );

        card.setSpacing(8);

        card.setPrefHeight(110);

        card.setPrefWidth(200);

        card.setMinWidth(170);

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#A7BBC5;" +
                "-fx-border-radius:10;"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        titleLabel.setTextFill(
                Color.web("#555555")
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        28
                )
        );

        valueLabel.setTextFill(
                Color.web("#18324A")
        );

        card.getChildren()
                .addAll(
                        titleLabel,
                        valueLabel
                );

        return card;
    }

    // ============================================================
    // PARKING SLOTS SECTION
    // ============================================================

    private VBox createParkingSlotsSection() {

        VBox section =
                new VBox();

        section.setSpacing(15);

        section.setPadding(
                new Insets(20)
        );

        section.setFillWidth(true);

        section.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:12;" +
                "-fx-border-color:#A7BBC5;" +
                "-fx-border-radius:12;"
        );

        HBox headingRow =
                new HBox();

        headingRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label(
                        "Manage Parking Slots"
                );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22
                )
        );

        title.setTextFill(
                Color.web("#18324A")
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button addSlotButton =
                new Button(
                        "+ Add Parking Slot"
                );

        addSlotButton.setPrefHeight(38);

        addSlotButton.setStyle(
                "-fx-background-color:#7898A2;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:6;"
        );

        addSlotButton.setOnAction(
                e -> showAddSlotDialog()
        );

        headingRow.getChildren()
                .addAll(
                        title,
                        spacer,
                        addSlotButton
                );

        GridPane slotGrid =
                new GridPane();

        slotGrid.setHgap(20);

        slotGrid.setVgap(20);

        int column = 0;

        int row = 0;

        for (ParkingSlot slot :
                parkingSlots) {

            if (slot == null) {
                continue;
            }

            if (slot.isVisitorSlot()) {
                continue;
            }

            VBox slotCard =
                    createParkingSlotCard(slot);

            slotGrid.add(
                    slotCard,
                    column,
                    row
            );

            column++;

            if (column == 4) {

                column = 0;
                row++;
            }
        }

        section.getChildren()
                .addAll(
                        headingRow,
                        slotGrid
                );

        return section;
    }

    // ============================================================
    // VISITOR PARKING SECTION
    // ============================================================

    private VBox createVisitorParkingSection() {

        VBox section =
                new VBox();

        section.setSpacing(15);

        section.setPadding(
                new Insets(20)
        );

        section.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:12;" +
                "-fx-border-color:#A7BBC5;" +
                "-fx-border-radius:12;"
        );

        HBox headingRow =
                new HBox();

        headingRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label(
                        "Visitor Parking"
                );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22
                )
        );

        title.setTextFill(
                Color.web("#18324A")
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        int visitorCount =
                countVisitorSlots();

        Button addVisitorButton =
                new Button(
                        "+ Add Visitor Slot"
                );

        addVisitorButton.setPrefHeight(38);

        addVisitorButton.setStyle(
                "-fx-background-color:#7898A2;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:6;"
        );

        addVisitorButton.setTooltip(
                new Tooltip(
                        "Add any number of extra visitor parking slots."
                )
        );

        addVisitorButton.setOnAction(
                e -> showAddVisitorSlotDialog()
        );

        headingRow.getChildren()
                .addAll(
                        title,
                        spacer,
                        addVisitorButton
                );

        Label info =
                new Label(
                        "Visitor slots: "
                                + visitorCount
                                + "     |     Available: "
                                + countAvailableVisitorSlots()
                                + "     |     You can add more slots anytime."
                );

        info.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        info.setTextFill(
                Color.web("#666666")
        );

        info.setWrapText(true);

        GridPane visitorGrid =
                new GridPane();

        visitorGrid.setHgap(20);
        visitorGrid.setVgap(20);

        int column = 0;
        int row = 0;

        for (ParkingSlot slot :
                parkingSlots) {

            if (slot == null ||
                    !slot.isVisitorSlot()) {

                continue;
            }

            VBox card =
                    createVisitorSlotCard(slot);

            visitorGrid.add(
                    card,
                    column,
                    row
            );

            column++;

            if (column == 4) {

                column = 0;
                row++;
            }
        }

        if (visitorCount == 0) {

            Label empty =
                    new Label(
                            "No visitor parking slots added yet.\n"
                                    + "Click '+ Add Visitor Slot' to create visitor slots."
                    );

            empty.setFont(
                    Font.font(
                            "Arial",
                            FontWeight.NORMAL,
                            14
                    )
            );

            empty.setTextFill(
                    Color.web("#777777")
            );

            empty.setWrapText(true);

            section.getChildren()
                    .addAll(
                            headingRow,
                            info,
                            empty
                    );

        } else {

            section.getChildren()
                    .addAll(
                            headingRow,
                            info,
                            visitorGrid
                    );
        }

        return section;
    }

    // ============================================================
    // NORMAL SLOT CARD
    // ============================================================

    private VBox createParkingSlotCard(
            ParkingSlot slot) {

        VBox card =
                new VBox();

        card.setAlignment(
                Pos.CENTER
        );

        card.setSpacing(8);

        card.setPrefWidth(210);
        card.setPrefHeight(145);

        String background;
        String textColor;

        String status =
                slot.getStatus() == null
                        ? "Unknown"
                        : slot.getStatus();

        if (status.equalsIgnoreCase("Available")) {

            background = "#A8D5A8";
            textColor = "#176B2C";

        } else if (status.equalsIgnoreCase("Occupied")) {

            background = "#E87575";
            textColor = "white";

        } else {

            background = "#E5C66B";
            textColor = "#5D4B00";
        }

        card.setStyle(
                "-fx-background-color:"
                        + background
                        + ";"
                        + "-fx-background-radius:10;"
        );

        Label slotNumber =
                new Label(
                        safe(slot.getSlotNumber())
                );

        slotNumber.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        19
                )
        );

        slotNumber.setTextFill(
                Color.web(textColor)
        );

        Label statusLabel =
                new Label(status);

        statusLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        statusLabel.setTextFill(
                Color.web(textColor)
        );

        Button manageButton =
                new Button("Manage");

        manageButton.setPrefHeight(28);

        manageButton.setStyle(
                "-fx-background-color:rgba(255,255,255,0.75);"
                        + "-fx-text-fill:#18324A;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:5;"
        );

        manageButton.setOnAction(
                e -> showSlotDetails(slot)
        );

        card.getChildren()
                .addAll(
                        slotNumber,
                        statusLabel,
                        manageButton
                );

        return card;
    }

    // ============================================================
    // VISITOR SLOT CARD
    // ============================================================

    private VBox createVisitorSlotCard(
            ParkingSlot slot) {

        VBox card =
                new VBox();

        card.setAlignment(
                Pos.CENTER
        );

        card.setSpacing(7);

        card.setPrefWidth(210);
        card.setPrefHeight(175);

        String status =
                slot.getStatus() == null
                        ? "Available"
                        : slot.getStatus();

        String background;
        String textColor;

        if (status.equalsIgnoreCase("Available")) {

            background = "#A8D5A8";
            textColor = "#176B2C";

        } else {

            background = "#E87575";
            textColor = "white";
        }

        card.setStyle(
                "-fx-background-color:"
                        + background
                        + ";"
                        + "-fx-background-radius:10;"
        );

        Label slotNumber =
                new Label(
                        safe(slot.getSlotNumber())
                );

        slotNumber.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        19
                )
        );

        slotNumber.setTextFill(
                Color.web(textColor)
        );

        Label visitorLabel =
                new Label("VISITOR");

        visitorLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12
                )
        );

        visitorLabel.setTextFill(
                Color.web(textColor)
        );

        Label statusLabel =
                new Label(status);

        statusLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        statusLabel.setTextFill(
                Color.web(textColor)
        );

        Button manageButton =
                new Button(
                        status.equalsIgnoreCase("Occupied")
                                ? "Manage / Release"
                                : "Assign Visitor"
                );

        manageButton.setPrefHeight(28);

        manageButton.setStyle(
                "-fx-background-color:rgba(255,255,255,0.80);"
                        + "-fx-text-fill:#18324A;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:5;"
        );

        manageButton.setOnAction(
                e -> showVisitorSlotDetails(slot)
        );

        card.getChildren()
                .addAll(
                        slotNumber,
                        visitorLabel,
                        statusLabel,
                        manageButton
                );

        return card;
    }

    // ============================================================
    // ADD NORMAL SLOT DIALOG
    // ============================================================

    private void showAddSlotDialog() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Add Parking Slot"
        );

        dialog.setHeaderText(
                "Create and Allocate New Parking Slot"
        );

        ButtonType allocateButtonType =
                new ButtonType(
                        "Allocate Slot",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        allocateButtonType,
                        ButtonType.CANCEL
                );

        VBox content =
                new VBox();

        content.setSpacing(12);
        content.setPadding(
                new Insets(15)
        );

        Label societyLabel =
                new Label(
                        "Society: "
                                + societyName
                );

        societyLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        societyLabel.setTextFill(
                Color.web("#18324A")
        );

        Label slotLabel =
                new Label(
                        "Parking Slot Name / Number"
                );

        slotLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        TextField slotField =
                new TextField();

        slotField.setPromptText(
                "Example: P-09"
        );

        Label memberHeading =
                new Label(
                        "Select Resident / Owner"
                );

        memberHeading.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        Label instruction =
                new Label(
                        "Select ONE member to allocate this parking slot."
                );

        instruction.setTextFill(
                Color.web("#666666")
        );

        VBox memberList =
                new VBox(8);

        memberList.setPadding(
                new Insets(8)
        );

        memberList.setStyle(
                "-fx-background-color:#F7F7F7;"
                        + "-fx-border-color:#D0D0D0;"
                        + "-fx-border-radius:6;"
                        + "-fx-background-radius:6;"
        );

        ScrollPane memberScroll =
                new ScrollPane(memberList);

        memberScroll.setFitToWidth(true);

        memberScroll.setPrefViewportHeight(260);

        List<CheckBox> checkBoxes =
                new ArrayList<>();

        for (ParkingMember member :
                societyMembers) {

            if (member == null) {
                continue;
            }

            HBox row =
                    new HBox(10);

            row.setAlignment(
                    Pos.CENTER_LEFT
            );

            row.setPadding(
                    new Insets(7)
            );

            CheckBox checkBox =
                    new CheckBox();

            Label name =
                    new Label(
                            safe(member.getName())
                    );

            name.setFont(
                    Font.font(
                            "Arial",
                            FontWeight.BOLD,
                            13
                    )
            );

            Label flat =
                    new Label(
                            "Flat: "
                                    + safe(member.getFlatNo())
                    );

            Label role =
                    new Label(
                            "("
                                    + safe(member.getRole())
                                    + ")"
                    );

            role.setTextFill(
                    Color.web("#7898A2")
            );

            Label email =
                    new Label(
                            safe(member.getEmail())
                    );

            email.setTextFill(
                    Color.web("#777777")
            );

            VBox memberInfo =
                    new VBox(2);

            HBox nameRow =
                    new HBox(8);

            nameRow.getChildren()
                    .addAll(
                            name,
                            role
                    );

            memberInfo.getChildren()
                    .addAll(
                            nameRow,
                            flat,
                            email
                    );

            row.getChildren()
                    .addAll(
                            checkBox,
                            memberInfo
                    );

            checkBox.setOnAction(e -> {

                if (checkBox.isSelected()) {

                    for (CheckBox other :
                            checkBoxes) {

                        if (other != checkBox) {

                            other.setSelected(
                                    false
                            );
                        }
                    }
                }
            });

            checkBoxes.add(
                    checkBox
            );

            memberList.getChildren()
                    .add(row);
        }

        if (societyMembers.isEmpty()) {

            Label noMembers =
                    new Label(
                            "No Residents or Owners found for society: "
                                    + societyName
                    );

            noMembers.setTextFill(
                    Color.web("#777777")
            );

            memberList.getChildren()
                    .add(noMembers);
        }

        Node allocateButton =
                dialog.getDialogPane()
                        .lookupButton(
                                allocateButtonType
                        );

        allocateButton.setDisable(true);

        Runnable validate = () -> {

            boolean slotEntered =
                    !slotField.getText()
                            .trim()
                            .isEmpty();

            boolean memberSelected =
                    checkBoxes.stream()
                            .anyMatch(
                                    CheckBox::isSelected
                            );

            allocateButton.setDisable(
                    !slotEntered
                            || !memberSelected
                            || societyMembers.isEmpty()
            );
        };

        slotField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) ->
                                validate.run()
                );

        for (CheckBox checkBox :
                checkBoxes) {

            checkBox.selectedProperty()
                    .addListener(
                            (obs, oldValue, newValue) ->
                                    validate.run()
                    );
        }

        content.getChildren()
                .addAll(
                        societyLabel,
                        slotLabel,
                        slotField,
                        memberHeading,
                        instruction,
                        memberScroll
                );

        dialog.getDialogPane()
                .setContent(content);

        dialog.setResultConverter(
                dialogButton -> {

                    if (dialogButton ==
                            allocateButtonType) {

                        String slotNumber =
                                slotField.getText()
                                        .trim();

                        if (slotNumber.isEmpty()) {

                            showWarning(
                                    "Please enter parking slot name/number."
                            );

                            return null;
                        }

                        if (slotNumber
                                .toUpperCase()
                                .startsWith("V-")) {

                            showWarning(
                                    "V- slots are reserved for visitor parking.\n\n"
                                            + "Please use '+ Add Visitor Slot'."
                            );

                            return null;
                        }

                        ParkingMember selectedMember =
                                null;

                        for (int i = 0;
                             i < checkBoxes.size();
                             i++) {

                            if (checkBoxes.get(i)
                                    .isSelected()) {

                                selectedMember =
                                        findSelectedMember(
                                                checkBoxes,
                                                i
                                        );

                                break;
                            }
                        }

                        if (selectedMember == null) {

                            for (int i = 0;
                                 i < checkBoxes.size()
                                         && i < societyMembers.size();
                                 i++) {

                                if (checkBoxes.get(i)
                                        .isSelected()) {

                                    selectedMember =
                                            societyMembers.get(i);

                                    break;
                                }
                            }
                        }

                        if (selectedMember == null) {

                            showWarning(
                                    "Please select one Resident or Owner."
                            );

                            return null;
                        }

                        System.out.println(
                                "=========================================="
                        );

                        System.out.println(
                                "ALLOCATE BUTTON CLICKED"
                        );

                        System.out.println(
                                "Secretary = "
                                        + secretaryEmail
                        );

                        System.out.println(
                                "Society = "
                                        + societyName
                        );

                        System.out.println(
                                "Slot = "
                                        + slotNumber
                        );

                        System.out.println(
                                "Member = "
                                        + selectedMember.getName()
                        );

                        System.out.println(
                                "Member Email = "
                                        + selectedMember.getEmail()
                        );

                        System.out.println(
                                "=========================================="
                        );

                        saveParking(
                                slotNumber,
                                selectedMember,
                                dialog
                        );

                        return null;
                    }

                    return dialogButton;
                }
        );

        dialog.showAndWait();
    }

    // ============================================================
    // FIND SELECTED MEMBER
    // ============================================================

    private ParkingMember findSelectedMember(
            List<CheckBox> checkBoxes,
            int selectedIndex) {

        if (selectedIndex < 0 ||
                selectedIndex >= checkBoxes.size()) {

            return null;
        }

        if (!checkBoxes.get(selectedIndex).isSelected()) {
            return null;
        }

        int currentIndex = 0;

        for (ParkingMember member :
                societyMembers) {

            if (member == null) {
                continue;
            }

            if (currentIndex == selectedIndex) {
                return member;
            }

            currentIndex++;
        }

        return null;
    }

    // ============================================================
    // SAVE NORMAL PARKING
    // ============================================================

    private void saveParking(
            String slotNumber,
            ParkingMember member,
            Dialog<ButtonType> dialog) {

        if (member == null) {

            showWarning(
                    "Please select a valid Resident or Owner."
            );

            return;
        }

        executor.submit(() -> {

            try {

                System.out.println(
                        ">>> saveParking() START"
                );

                controller.allocateParking(
                        secretaryEmail,
                        societyName,
                        slotNumber,
                        member
                );

                System.out.println(
                        ">>> controller.allocateParking() SUCCESS"
                );

                List<ParkingSlot> slots =
                        controller.getParkingSlots(
                                societyName
                        );

                List<AssignedParking> assignments =
                        controller.getAssignedParking(
                                societyName
                        );

                if (slots == null) {
                    slots = new ArrayList<>();
                }

                if (assignments == null) {
                    assignments = new ArrayList<>();
                }

                final List<ParkingSlot> finalSlots =
                        slots;

                final List<AssignedParking> finalAssignments =
                        assignments;

                Platform.runLater(() -> {

                    parkingSlots.clear();

                    parkingSlots.addAll(
                            finalSlots
                    );

                    assignedParkings.clear();

                    assignedParkings.addAll(
                            finalAssignments
                    );

                    if (dialog.isShowing()) {
                        dialog.close();
                    }

                    if (root != null) {

                        root.setCenter(
                                createMainContent()
                        );
                    }

                    showSuccess(
                            "Parking "
                                    + slotNumber
                                    + " assigned successfully to\n"
                                    + member.getName()
                                    + " - "
                                    + member.getFlatNo()
                                    + " ("
                                    + member.getRole()
                                    + ")"
                    );
                });

            } catch (Exception ex) {

                ex.printStackTrace();

                Platform.runLater(() ->
                        showWarning(
                                "Parking allocation failed.\n\n"
                                        + safeMessage(ex)
                        )
                );
            }
        });
    }

    // ============================================================
    // ADD VISITOR SLOT COUNT DIALOG
    // ============================================================

    private void showAddVisitorSlotDialog() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Add Visitor Parking Slots"
        );

        dialog.setHeaderText(
                "Add Extra Visitor Parking Slots"
        );

        ButtonType addButtonType =
                new ButtonType(
                        "Add Slots",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        addButtonType,
                        ButtonType.CANCEL
                );

        VBox content =
                new VBox(12);

        content.setPadding(
                new Insets(15)
        );

        Label societyLabel =
                new Label(
                        "Society: "
                                + societyName
                );

        societyLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        societyLabel.setTextFill(
                Color.web("#18324A")
        );

        int currentVisitorCount =
                countVisitorSlots();

        Label currentCountLabel =
                new Label(
                        "Currently added visitor slots: "
                                + currentVisitorCount
                );

        currentCountLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        currentCountLabel.setTextFill(
                Color.web("#555555")
        );

        Label information =
                new Label(
                        "Visitor parking slots are temporary slots.\n\n"
                                + "Enter how many EXTRA visitor slots you want to add.\n"
                                + "The system will automatically create the next available "
                                + "visitor slot numbers."
                );

        information.setWrapText(true);

        information.setTextFill(
                Color.web("#666666")
        );

        Label countLabel =
                new Label(
                        "How many extra visitor slots do you want to add?"
                );

        countLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        countLabel.setTextFill(
                Color.web("#18324A")
        );

        TextField countField =
                new TextField();

        countField.setPromptText(
                "Example: 5"
        );

        countField.setPrefWidth(
                250
        );

        countField.setTextFormatter(
                new TextFormatter<String>(
                        change -> {

                            String newText =
                                    change.getControlNewText();

                            if (newText.matches("\\d*")) {
                                return change;
                            }

                            return null;
                        }
                )
        );

        Label exampleLabel =
                new Label(
                        "Example: If you enter 5, five new visitor slots "
                                + "will be created automatically."
                );

        exampleLabel.setWrapText(true);

        exampleLabel.setTextFill(
                Color.web("#777777")
        );

        Label previewLabel =
                new Label(
                        "New total: "
                                + currentVisitorCount
                );

        previewLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        previewLabel.setTextFill(
                Color.web("#176B2C")
        );

        countField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {

                            String text =
                                    newValue == null
                                            ? ""
                                            : newValue.trim();

                            if (text.isEmpty()) {

                                previewLabel.setText(
                                        "New total: "
                                                + currentVisitorCount
                                );

                                return;
                            }

                            try {

                                long count =
                                        Long.parseLong(text);

                                long newTotal =
                                        currentVisitorCount
                                                + count;

                                previewLabel.setText(
                                        "New total: "
                                                + newTotal
                                );

                            } catch (NumberFormatException ex) {

                                previewLabel.setText(
                                        "New total: "
                                                + currentVisitorCount
                                );
                            }
                        }
                );

        content.getChildren()
                .addAll(
                        societyLabel,
                        currentCountLabel,
                        information,
                        countLabel,
                        countField,
                        exampleLabel,
                        previewLabel
                );

        dialog.getDialogPane()
                .setContent(content);

        Node addButton =
                dialog.getDialogPane()
                        .lookupButton(
                                addButtonType
                        );

        addButton.setDisable(true);

        countField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {

                            boolean valid =
                                    isPositiveInteger(
                                            newValue
                                    );

                            addButton.setDisable(
                                    !valid
                            );
                        }
                );

        dialog.setResultConverter(
                dialogButton -> {

                    if (dialogButton ==
                            addButtonType) {

                        String countText =
                                countField.getText()
                                        .trim();

                        if (!isPositiveInteger(
                                countText)) {

                            showWarning(
                                    "Please enter a valid positive number.\n\n"
                                            + "Example: 1, 2, 5, 10..."
                            );

                            return null;
                        }

                        int count;

                        try {

                            count =
                                    Integer.parseInt(
                                            countText
                                    );

                        } catch (NumberFormatException ex) {

                            showWarning(
                                    "The number entered is too large or invalid."
                            );

                            return null;
                        }

                        if (count <= 0) {

                            showWarning(
                                    "Please enter a number greater than 0."
                            );

                            return null;
                        }

                        System.out.println(
                                "=========================================="
                        );

                        System.out.println(
                                "ADD EXTRA VISITOR PARKING SLOTS"
                        );

                        System.out.println(
                                "Secretary = "
                                        + secretaryEmail
                        );

                        System.out.println(
                                "Society = "
                                        + societyName
                        );

                        System.out.println(
                                "Existing Visitor Slots = "
                                        + currentVisitorCount
                        );

                        System.out.println(
                                "Requested Extra Slots = "
                                        + count
                        );

                        System.out.println(
                                "=========================================="
                        );

                        createVisitorSlots(
                                count,
                                dialog
                        );

                        return null;
                    }

                    return dialogButton;
                }
        );

        dialog.showAndWait();
    }

    // ============================================================
    // VALIDATE POSITIVE INTEGER
    // ============================================================

    private boolean isPositiveInteger(
            String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return false;
        }

        try {

            long number =
                    Long.parseLong(
                            value.trim()
                    );

            return number > 0;

        } catch (NumberFormatException ex) {

            return false;
        }
    }

    // ============================================================
    // CREATE MULTIPLE VISITOR SLOTS
    // ============================================================

    private void createVisitorSlots(
            int count,
            Dialog<ButtonType> dialog) {

        if (count <= 0) {

            showWarning(
                    "Number of visitor slots must be greater than 0."
            );

            return;
        }

        executor.submit(() -> {

            int createdCount = 0;

            Exception lastException = null;

            try {

                System.out.println(
                        "=========================================="
                );

                System.out.println(
                        "CREATING MULTIPLE VISITOR SLOTS"
                );

                System.out.println(
                        "Requested Count = "
                                + count
                );

                System.out.println(
                        "Secretary = "
                                + secretaryEmail
                );

                System.out.println(
                        "Society = "
                                + societyName
                );

                System.out.println(
                        "=========================================="
                );

                for (int i = 0; i < count; i++) {

                    try {

                        System.out.println(
                                "Creating visitor slot "
                                        + (i + 1)
                                        + " of "
                                        + count
                        );

                        controller.createVisitorParkingSlot(
                                secretaryEmail,
                                societyName
                        );

                        createdCount++;

                        System.out.println(
                                "Visitor slot "
                                        + (i + 1)
                                        + " created successfully."
                        );

                    } catch (Exception ex) {

                        lastException = ex;

                        System.err.println(
                                "Visitor slot creation failed at "
                                        + (i + 1)
                                        + " of "
                                        + count
                        );

                        ex.printStackTrace();

                        break;
                    }
                }

                List<ParkingSlot> slots =
                        controller.getParkingSlots(
                                societyName
                        );

                List<AssignedParking> assignments =
                        controller.getAssignedParking(
                                societyName
                        );

                if (slots == null) {
                    slots = new ArrayList<>();
                }

                if (assignments == null) {
                    assignments = new ArrayList<>();
                }

                final List<ParkingSlot> finalSlots =
                        slots;

                final List<AssignedParking> finalAssignments =
                        assignments;

                final int finalCreatedCount =
                        createdCount;

                final Exception finalException =
                        lastException;

                Platform.runLater(() -> {

                    parkingSlots.clear();

                    parkingSlots.addAll(
                            finalSlots
                    );

                    assignedParkings.clear();

                    assignedParkings.addAll(
                            finalAssignments
                    );

                    if (dialog.isShowing()) {
                        dialog.close();
                    }

                    if (root != null) {

                        root.setCenter(
                                createMainContent()
                        );
                    }

                    if (finalCreatedCount == count) {

                        showSuccess(
                                finalCreatedCount
                                        + " visitor parking slot"
                                        + (finalCreatedCount == 1
                                        ? ""
                                        : "s")
                                        + " created successfully."
                        );

                    } else if (finalCreatedCount > 0) {

                        String errorMessage =
                                finalException == null
                                        ? ""
                                        : "\n\nReason: "
                                        + safeMessage(
                                        finalException
                                );

                        showWarning(
                                "Only "
                                        + finalCreatedCount
                                        + " of "
                                        + count
                                        + " requested visitor slots were created."
                                        + errorMessage
                        );

                    } else {

                        String errorMessage =
                                finalException == null
                                        ? ""
                                        : "\n\nReason: "
                                        + safeMessage(
                                        finalException
                                );

                        showWarning(
                                "No visitor parking slots were created."
                                        + errorMessage
                        );
                    }
                });

            } catch (Exception ex) {

                ex.printStackTrace();

                Platform.runLater(() -> {

                    if (dialog.isShowing()) {
                        dialog.close();
                    }

                    showWarning(
                            "Unable to create visitor parking slots.\n\n"
                                    + safeMessage(ex)
                    );
                });
            }
        });
    }

    // ============================================================
    // VISITOR SLOT DETAILS
    // ============================================================

    private void showVisitorSlotDetails(
            ParkingSlot slot) {

        if (slot == null) {

            showWarning(
                    "Invalid visitor parking slot."
            );

            return;
        }

        if (slot.getStatus() != null &&
                slot.getStatus()
                        .equalsIgnoreCase("Occupied")) {

            showOccupiedVisitorDialog(slot);

        } else {

            showAssignVisitorDialog(slot);
        }
    }

    // ============================================================
    // ASSIGN VISITOR DIALOG
    // ============================================================

    private void showAssignVisitorDialog(
            ParkingSlot slot) {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Assign Visitor Parking"
        );

        dialog.setHeaderText(
                "Assign "
                        + slot.getSlotNumber()
                        + " to Visitor"
        );

        ButtonType assignButtonType =
                new ButtonType(
                        "Assign Visitor",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        assignButtonType,
                        ButtonType.CANCEL
                );

        VBox content =
                new VBox(12);

        content.setPadding(
                new Insets(15)
        );

        Label societyLabel =
                new Label(
                        "Society: "
                                + societyName
                );

        societyLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        TextField visitorNameField =
                new TextField();

        visitorNameField.setPromptText(
                "Visitor name"
        );

        TextField vehicleField =
                new TextField();

        vehicleField.setPromptText(
                "Vehicle number"
        );

        Label residentHeading =
                new Label(
                        "Visiting Resident / Owner"
                );

        residentHeading.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        ComboBox<ParkingMember> residentCombo =
                new ComboBox<>();

        residentCombo.setMaxWidth(
                Double.MAX_VALUE
        );

        residentCombo.setItems(
                FXCollections.observableArrayList(
                        societyMembers
                )
        );

        residentCombo.setCellFactory(
                list -> new ListCell<ParkingMember>() {

                    @Override
                    protected void updateItem(
                            ParkingMember member,
                            boolean empty) {

                        super.updateItem(
                                member,
                                empty
                        );

                        if (empty ||
                                member == null) {

                            setText(null);

                        } else {

                            setText(
                                    safe(member.getName())
                                            + " - Flat "
                                            + safe(member.getFlatNo())
                                            + " ("
                                            + safe(member.getRole())
                                            + ")"
                            );
                        }
                    }
                }
        );

        residentCombo.setButtonCell(
                new ListCell<ParkingMember>() {

                    @Override
                    protected void updateItem(
                            ParkingMember member,
                            boolean empty) {

                        super.updateItem(
                                member,
                                empty
                        );

                        if (empty ||
                                member == null) {

                            setText(null);

                        } else {

                            setText(
                                    safe(member.getName())
                                            + " - Flat "
                                            + safe(member.getFlatNo())
                            );
                        }
                    }
                }
        );

        Label instruction =
                new Label(
                        "Select the Resident/Owner whose visitor is using this slot."
                );

        instruction.setWrapText(true);

        instruction.setTextFill(
                Color.web("#666666")
        );

        content.getChildren()
                .addAll(
                        societyLabel,
                        new Label("Visitor Name"),
                        visitorNameField,
                        new Label("Vehicle Number"),
                        vehicleField,
                        residentHeading,
                        residentCombo,
                        instruction
                );

        dialog.getDialogPane()
                .setContent(content);

        Node assignButton =
                dialog.getDialogPane()
                        .lookupButton(
                                assignButtonType
                        );

        assignButton.setDisable(true);

        Runnable validate = () -> {

            boolean visitorEntered =
                    !visitorNameField.getText()
                            .trim()
                            .isEmpty();

            boolean vehicleEntered =
                    !vehicleField.getText()
                            .trim()
                            .isEmpty();

            boolean residentSelected =
                    residentCombo.getValue() != null;

            assignButton.setDisable(
                    !visitorEntered
                            || !vehicleEntered
                            || !residentSelected
            );
        };

        visitorNameField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) ->
                                validate.run()
                );

        vehicleField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) ->
                                validate.run()
                );

        residentCombo.valueProperty()
                .addListener(
                        (obs, oldValue, newValue) ->
                                validate.run()
                );

        dialog.setResultConverter(
                dialogButton -> {

                    if (dialogButton ==
                            assignButtonType) {

                        ParkingMember resident =
                                residentCombo.getValue();

                        if (resident == null) {

                            showWarning(
                                    "Please select the visiting Resident/Owner."
                            );

                            return null;
                        }

                        String visitorName =
                                visitorNameField
                                        .getText()
                                        .trim();

                        String vehicleNumber =
                                vehicleField
                                        .getText()
                                        .trim();

                        assignVisitor(
                                slot,
                                visitorName,
                                vehicleNumber,
                                resident,
                                dialog
                        );

                        return null;
                    }

                    return dialogButton;
                }
        );

        dialog.showAndWait();
    }

    // ============================================================
    // ASSIGN VISITOR
    // ============================================================

    private void assignVisitor(
            ParkingSlot slot,
            String visitorName,
            String vehicleNumber,
            ParkingMember visitingResident,
            Dialog<ButtonType> dialog) {

        if (slot == null) {

            showWarning(
                    "Invalid visitor parking slot."
            );

            return;
        }

        if (visitingResident == null) {

            showWarning(
                    "Please select the visiting Resident/Owner."
            );

            return;
        }

        executor.submit(() -> {

            try {

                System.out.println(
                        "=========================================="
                );

                System.out.println(
                        "ASSIGN VISITOR PARKING"
                );

                System.out.println(
                        "Secretary = "
                                + secretaryEmail
                );

                System.out.println(
                        "Society = "
                                + societyName
                );

                System.out.println(
                        "Slot = "
                                + slot.getSlotNumber()
                );

                System.out.println(
                        "Visitor = "
                                + visitorName
                );

                System.out.println(
                        "Vehicle = "
                                + vehicleNumber
                );

                System.out.println(
                        "Visiting Resident = "
                                + visitingResident.getName()
                );

                System.out.println(
                        "Resident Email = "
                                + visitingResident.getEmail()
                );

                System.out.println(
                        "=========================================="
                );

                controller.assignVisitorParking(
                        secretaryEmail,
                        societyName,
                        slot.getSlotNumber(),
                        visitorName,
                        vehicleNumber,
                        visitingResident
                );

                List<ParkingSlot> slots =
                        controller.getParkingSlots(
                                societyName
                        );

                List<AssignedParking> assignments =
                        controller.getAssignedParking(
                                societyName
                        );

                if (slots == null) {
                    slots = new ArrayList<>();
                }

                if (assignments == null) {
                    assignments = new ArrayList<>();
                }

                final List<ParkingSlot> finalSlots =
                        slots;

                final List<AssignedParking> finalAssignments =
                        assignments;

                Platform.runLater(() -> {

                    parkingSlots.clear();

                    parkingSlots.addAll(
                            finalSlots
                    );

                    assignedParkings.clear();

                    assignedParkings.addAll(
                            finalAssignments
                    );

                    if (dialog.isShowing()) {
                        dialog.close();
                    }

                    if (root != null) {

                        root.setCenter(
                                createMainContent()
                        );
                    }

                    showSuccess(
                            "Visitor "
                                    + visitorName
                                    + " assigned to "
                                    + slot.getSlotNumber()
                                    + " successfully."
                    );
                });

            } catch (Exception ex) {

                ex.printStackTrace();

                Platform.runLater(() ->
                        showWarning(
                                "Visitor parking assignment failed.\n\n"
                                        + safeMessage(ex)
                        )
                );
            }
        });
    }

    // ============================================================
    // OCCUPIED VISITOR DETAILS
    // ============================================================

    private void showOccupiedVisitorDialog(
            ParkingSlot slot) {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Visitor Parking Details"
        );

        dialog.setHeaderText(
                slot.getSlotNumber()
                        + " - Visitor Parking"
        );

        ButtonType releaseButtonType =
                new ButtonType(
                        "Release Visitor Slot",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        releaseButtonType,
                        ButtonType.CANCEL
                );

        VBox content =
                new VBox(10);

        content.setPadding(
                new Insets(15)
        );

        Label loading =
                new Label(
                        "Loading visitor details..."
                );

        content.getChildren()
                .add(
                        loading
                );

        dialog.getDialogPane()
                .setContent(content);

        executor.submit(() -> {

            try {

                Map<String, Object> details =
                        controller.getVisitorSlotDetails(
                                societyName,
                                slot.getSlotNumber()
                        );

                Platform.runLater(() -> {

                    content.getChildren()
                            .clear();

                    content.getChildren()
                            .addAll(

                                    createDetailLabel(
                                            "Society",
                                            societyName
                                    ),

                                    createDetailLabel(
                                            "Slot",
                                            slot.getSlotNumber()
                                    ),

                                    createDetailLabel(
                                            "Status",
                                            safe(slot.getStatus())
                                    ),

                                    createDetailLabel(
                                            "Visitor",
                                            getMapString(
                                                    details,
                                                    "visitorName",
                                                    "Not available"
                                            )
                                    ),

                                    createDetailLabel(
                                            "Visitor Phone",
                                            getMapString(
                                                    details,
                                                    "visitorPhone",
                                                    "Not available"
                                            )
                                    ),

                                    createDetailLabel(
                                            "Vehicle",
                                            getMapString(
                                                    details,
                                                    "vehicleNumber",
                                                    "Not available"
                                            )
                                    ),

                                    createDetailLabel(
                                            "Visiting Resident",
                                            getMapString(
                                                    details,
                                                    "visitingResidentName",
                                                    "Not available"
                                            )
                                    ),

                                    createDetailLabel(
                                            "Resident Email",
                                            getMapString(
                                                    details,
                                                    "visitingResidentEmail",
                                                    "Not available"
                                            )
                                    ),

                                    createDetailLabel(
                                            "Flat",
                                            getMapString(
                                                    details,
                                                    "visitingResidentFlat",
                                                    "Not available"
                                            )
                                    )
                            );
                });

            } catch (Exception ex) {

                ex.printStackTrace();

                Platform.runLater(() -> {

                    content.getChildren()
                            .clear();

                    content.getChildren()
                            .add(
                                    new Label(
                                            "Unable to load visitor details.\n\n"
                                                    + safeMessage(ex)
                                    )
                            );
                });
            }
        });

        dialog.setResultConverter(
                dialogButton -> {

                    if (dialogButton ==
                            releaseButtonType) {

                        releaseVisitorSlot(
                                slot,
                                dialog
                        );

                        return null;
                    }

                    return dialogButton;
                }
        );

        dialog.showAndWait();
    }

    // ============================================================
    // RELEASE VISITOR SLOT
    // ============================================================

    private void releaseVisitorSlot(
            ParkingSlot slot,
            Dialog<ButtonType> dialog) {

        if (slot == null) {

            showWarning(
                    "Invalid visitor parking slot."
            );

            return;
        }

        executor.submit(() -> {

            try {

                System.out.println(
                        "=========================================="
                );

                System.out.println(
                        "RELEASE VISITOR PARKING"
                );

                System.out.println(
                        "Secretary = "
                                + secretaryEmail
                );

                System.out.println(
                        "Society = "
                                + societyName
                );

                System.out.println(
                        "Slot = "
                                + slot.getSlotNumber()
                );

                System.out.println(
                        "=========================================="
                );

                controller.releaseVisitorParking(
                        secretaryEmail,
                        societyName,
                        slot.getSlotNumber()
                );

                List<ParkingSlot> slots =
                        controller.getParkingSlots(
                                societyName
                        );

                List<AssignedParking> assignments =
                        controller.getAssignedParking(
                                societyName
                        );

                if (slots == null) {
                    slots = new ArrayList<>();
                }

                if (assignments == null) {
                    assignments = new ArrayList<>();
                }

                final List<ParkingSlot> finalSlots =
                        slots;

                final List<AssignedParking> finalAssignments =
                        assignments;

                Platform.runLater(() -> {

                    parkingSlots.clear();

                    parkingSlots.addAll(
                            finalSlots
                    );

                    assignedParkings.clear();

                    assignedParkings.addAll(
                            finalAssignments
                    );

                    if (dialog.isShowing()) {
                        dialog.close();
                    }

                    if (root != null) {

                        root.setCenter(
                                createMainContent()
                        );
                    }

                    showSuccess(
                            "Visitor parking slot "
                                    + slot.getSlotNumber()
                                    + " is now Available."
                    );
                });

            } catch (Exception ex) {

                ex.printStackTrace();

                Platform.runLater(() ->
                        showWarning(
                                "Unable to release visitor parking slot.\n\n"
                                        + safeMessage(ex)
                        )
                );
            }
        });
    }

    // ============================================================
    // ASSIGNED PARKING
    // ============================================================

    private VBox createAssignedParkingSection() {

        VBox section =
                new VBox();

        section.setSpacing(15);

        section.setPadding(
                new Insets(20)
        );

        section.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:12;"
                        + "-fx-border-color:#A7BBC5;"
                        + "-fx-border-radius:12;"
        );

        Label title =
                new Label(
                        "Assigned Parking"
                );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22
                )
        );

        title.setTextFill(
                Color.web("#18324A")
        );

        TableView<AssignedParking> table =
                new TableView<>();

        table.setPrefHeight(280);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        TableColumn<AssignedParking, String>
                memberColumn =
                new TableColumn<>(
                        "Resident / Owner"
                );

        memberColumn.setPrefWidth(190);

        memberColumn.setCellValueFactory(
                data ->
                        data.getValue()
                                .memberProperty()
        );

        TableColumn<AssignedParking, String>
                flatColumn =
                new TableColumn<>(
                        "Flat No."
                );

        flatColumn.setPrefWidth(100);

        flatColumn.setCellValueFactory(
                data ->
                        data.getValue()
                                .flatProperty()
        );

        TableColumn<AssignedParking, String>
                roleColumn =
                new TableColumn<>(
                        "Role"
                );

        roleColumn.setPrefWidth(100);

        roleColumn.setCellValueFactory(
                data ->
                        data.getValue()
                                .roleProperty()
        );

        TableColumn<AssignedParking, String>
                vehicleColumn =
                new TableColumn<>(
                        "Vehicle No."
                );

        vehicleColumn.setPrefWidth(140);

        vehicleColumn.setCellValueFactory(
                data ->
                        data.getValue()
                                .vehicleProperty()
        );

        TableColumn<AssignedParking, String>
                slotColumn =
                new TableColumn<>(
                        "Parking Slot"
                );

        slotColumn.setPrefWidth(130);

        slotColumn.setCellValueFactory(
                data ->
                        data.getValue()
                                .slotProperty()
        );

        TableColumn<AssignedParking, String>
                statusColumn =
                new TableColumn<>(
                        "Status"
                );

        statusColumn.setPrefWidth(120);

        statusColumn.setCellValueFactory(
                data ->
                        data.getValue()
                                .statusProperty()
        );

        table.getColumns()
                .addAll(
                        memberColumn,
                        flatColumn,
                        roleColumn,
                        vehicleColumn,
                        slotColumn,
                        statusColumn
                );

        table.setItems(
                FXCollections.observableArrayList(
                        assignedParkings
                )
        );

        section.getChildren()
                .addAll(
                        title,
                        table
                );

        return section;
    }

    // ============================================================
    // SLOT DETAILS
    // ============================================================

    private void showSlotDetails(
            ParkingSlot slot) {

        AssignedParking assignment =
                null;

        for (AssignedParking parking :
                assignedParkings) {

            if (parking == null) {
                continue;
            }

            if (parking.getSlot() != null &&
                    parking.getSlot()
                            .equalsIgnoreCase(
                                    slot.getSlotNumber()
                            )) {

                assignment = parking;

                break;
            }
        }

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Parking Slot Details"
        );

        alert.setHeaderText(
                slot.getSlotNumber()
        );

        if (assignment != null) {

            alert.setContentText(

                    "Society: "
                            + societyName

                            + "\n\nStatus: "
                            + safe(slot.getStatus())

                            + "\nMember: "
                            + safe(assignment.getMember())

                            + "\nFlat No.: "
                            + safe(assignment.getFlat())

                            + "\nRole: "
                            + safe(assignment.getRole())

                            + "\nVehicle No.: "
                            + (
                            assignment.getVehicle() == null ||
                                    assignment.getVehicle().isEmpty()
                                    ? "Not added"
                                    : assignment.getVehicle()
                    )
            );

        } else {

            alert.setContentText(

                    "Society: "
                            + societyName

                            + "\n\nStatus: "
                            + safe(slot.getStatus())

                            + "\nNo member assigned."
            );
        }

        alert.showAndWait();
    }

    // ============================================================
    // DETAIL LABEL
    // ============================================================

    private Label createDetailLabel(
            String title,
            String value) {

        Label label =
                new Label(
                        title
                                + ": "
                                + safe(value)
                );

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.NORMAL,
                        14
                )
        );

        label.setWrapText(true);

        return label;
    }

    // ============================================================
    // MAP STRING
    // ============================================================

    private String getMapString(
            Map<String, Object> map,
            String key,
            String defaultValue) {

        if (map == null) {
            return defaultValue;
        }

        Object value =
                map.get(key);

        if (value == null) {
            return defaultValue;
        }

        String result =
                String.valueOf(value)
                        .trim();

        if (result.isEmpty()) {
            return defaultValue;
        }

        return result;
    }

    // ============================================================
    // WARNING
    // ============================================================

    private void showWarning(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle(
                "Parking"
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }

    // ============================================================
    // SUCCESS
    // ============================================================

    private void showSuccess(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Parking"
        );

        alert.setHeaderText(
                "Success"
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }

    // ============================================================
    // SAFE STRING
    // ============================================================

    private String safe(
            String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "";
        }

        return value.trim();
    }

    // ============================================================
    // SAFE EXCEPTION MESSAGE
    // ============================================================

    private String safeMessage(
            Exception e) {

        if (e == null) {
            return "Unknown error.";
        }

        String message =
                e.getMessage();

        if (message == null ||
                message.trim().isEmpty()) {

            return e.getClass()
                    .getSimpleName();
        }

        return message;
    }

    // ============================================================
    // SHOW
    // ============================================================

    public static void show(
            Stage stage) {

        SecretaryParking parking =
                new SecretaryParking();

        Scene scene =
                parking.getParkingScene(stage);

        stage.setTitle(
                "Society360 - Smart Parking"
        );

        stage.setScene(scene);

        stage.show();
    }
}