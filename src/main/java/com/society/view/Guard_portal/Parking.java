package com.society.view.Guard_portal;

import javafx.application.Platform;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.society.controller.Guard_Controller.ParkingController;
import com.society.model.Guard_model.ParkingModel;
import com.society.view.ScreenSize;

public class Parking {

    // =====================================================
    // PARKING SPOT
    // =====================================================

    static class ParkingSpot {

        String spotNumber;
        boolean occupied;

        String vehicleNumber;
        String ownerName;
        String vehicleType;

        String parkingId;

        ParkingSpot(String spotNumber) {

            this.spotNumber = spotNumber;

            this.occupied = false;

            this.vehicleNumber = "";
            this.ownerName = "";
            this.vehicleType = "";

            this.parkingId = null;
        }
    }

    // =====================================================
    // CREATE FRESH PARKING SPOTS
    // =====================================================

    private static ArrayList<ParkingSpot>
    createParkingSpots() {

        ArrayList<ParkingSpot> spots =
                new ArrayList<>();

        // -----------------------------
        // ZONE A
        // -----------------------------

        spots.add(
                new ParkingSpot("A-01")
        );

        spots.add(
                new ParkingSpot("A-02")
        );

        spots.add(
                new ParkingSpot("A-03")
        );

        spots.add(
                new ParkingSpot("A-04")
        );

        // -----------------------------
        // ZONE B
        // -----------------------------

        spots.add(
                new ParkingSpot("B-01")
        );

        spots.add(
                new ParkingSpot("B-02")
        );

        spots.add(
                new ParkingSpot("B-03")
        );

        spots.add(
                new ParkingSpot("B-04")
        );

        // -----------------------------
        // ZONE C
        // -----------------------------

        spots.add(
                new ParkingSpot("C-01")
        );

        spots.add(
                new ParkingSpot("C-02")
        );

        spots.add(
                new ParkingSpot("C-03")
        );

        spots.add(
                new ParkingSpot("C-04")
        );

        return spots;
    }


    // =====================================================
    // MAIN SCENE
    // =====================================================

    public static Scene createScene(
            Stage stage,
            String guardEmail,
            String society) {

        // =================================================
        // NORMALIZE USER INFORMATION
        // =================================================

        final String normalizedGuardEmail =
                guardEmail == null
                        ? null
                        : guardEmail
                                .trim()
                                .toLowerCase();

        final String normalizedSociety =
                society == null
                        ? null
                        : society.trim();

        // =================================================
        // FRESH PARKING STATE
        // =================================================

        ArrayList<ParkingSpot> parkingSpots =
                createParkingSpots();

        ParkingController parkingController =
                new ParkingController();

        // =================================================
        // LOAD FIRESTORE DATA
        // =================================================

        loadParkingFromFirestore(
                parkingController,
                normalizedGuardEmail,
                parkingSpots
        );

        // =================================================
        // ROOT
        // =================================================

        BorderPane root =
                new BorderPane();

        // =================================================
        // SIDEBAR
        // KEEP EXISTING SIDEBAR
        // =================================================

        GuardSidebar sidebar =
                new GuardSidebar(
                        stage,
                        "Parking",
                        normalizedGuardEmail,
                        normalizedSociety
                );

        root.setLeft(
                sidebar.getSidebar()
        );

        // =================================================
        // MAIN AREA
        // =================================================

        BorderPane mainArea =
                new BorderPane();

        // =================================================
        // HEADER
        // =================================================

        HBox header =
                createHeader();

        mainArea.setTop(
                header
        );

        // =================================================
        // SCROLL CONTENT
        // =================================================

        VBox content =
                new VBox(18);

        content.setPadding(
                new Insets(
                        24,
                        32,
                        28,
                        32
                )
        );

        content.setFillWidth(true);

        content.setStyle(
                "-fx-background-color:#E8DDD5;"
        );

        // =================================================
        // SUMMARY
        // =================================================

        HBox summary =
                createSummary(
                        parkingSpots
                );

        // =================================================
        // PARKING CARD
        // =================================================

        VBox parkingCard =
                createParkingCard(
                        parkingSpots,
                        parkingController,
                        normalizedGuardEmail
                );

        // =================================================
        // VEHICLE ENTRY CARD
        // =================================================

        VBox entryCard =
                createEntryCard(
                        stage,
                        parkingSpots,
                        parkingController,
                        normalizedGuardEmail,
                        normalizedSociety
                );

        // =================================================
        // ADD CONTENT
        // =================================================

        content.getChildren().addAll(
                summary,
                parkingCard,
                entryCard
        );

        // =================================================
        // SCROLL PANE
        // =================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                content
        );

        scrollPane.setFitToWidth(true);

        scrollPane.setFitToHeight(false);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setPannable(true);

        scrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;" +
                "-fx-border-color:transparent;"
        );

        // =================================================
        // HIDE SCROLLBAR VISUALLY
        //
        // Scrolling still works with mouse wheel / trackpad.
        // =================================================

        Platform.runLater(() -> {

            scrollPane.lookupAll(
                    ".scroll-bar"
            ).forEach(node -> {

                node.setOpacity(0);
                node.setMouseTransparent(false);
            });
        });

        // =================================================
        // CENTER
        // =================================================

        mainArea.setCenter(
                scrollPane
        );

        root.setCenter(
                mainArea
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

        return scene;
    }

    // =====================================================
    // HEADER
    // =====================================================

    private static HBox createHeader() {

        HBox header =
                new HBox();

        header.setPadding(
                new Insets(
                        22,
                        32,
                        22,
                        32
                )
        );

        header.setMinHeight(100);
        header.setPrefHeight(100);

        header.setStyle(
                "-fx-background-color:#4E342E;"
        );

        // =================================================
        // TITLE
        // =================================================

        VBox titleBox =
                new VBox(4);

        Label title =
                new Label(
                        "Parking / Vehicle Entry"
                );

        title.setStyle(
                "-fx-font-size:25px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        Label description =
                new Label(
                        "Manage parking availability and vehicle entry."
                );

        description.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:white;"
        );

        titleBox.getChildren().addAll(
                title,
                description
        );

        // =================================================
        // SPACER
        // =================================================

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        // =================================================
        // DATE
        // =================================================

        LocalDate today =
                LocalDate.now();

        Label day =
                new Label(
                        today.format(
                                DateTimeFormatter.ofPattern(
                                        "EEEE"
                                )
                        )
                );

        Label date =
                new Label(
                        today.format(
                                DateTimeFormatter.ofPattern(
                                        "dd MMMM yyyy"
                                )
                        )
                );

        day.setTextFill(
                Color.WHITE
        );

        date.setTextFill(
                Color.WHITE
        );

        day.setStyle(
                "-fx-font-size:14px;"
        );

        date.setStyle(
                "-fx-font-size:14px;"
        );

        VBox dateBox =
                new VBox(4);

        dateBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        dateBox.getChildren().addAll(
                day,
                date
        );

        // =================================================
        // HEADER CONTENT
        // =================================================

        header.getChildren().addAll(
                titleBox,
                spacer,
                dateBox
        );

        return header;
    }

    // =====================================================
    // PARKING CARD
    // =====================================================

    private static VBox createParkingCard(
            ArrayList<ParkingSpot> parkingSpots,
            ParkingController parkingController,
            String guardEmail) {

        VBox card =
                new VBox(16);

        card.setPadding(
                new Insets(20)
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setStyle(
                "-fx-background-color:#E8F0E8;" +
                "-fx-background-radius:14;"
        );

        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label(
                        "Parking Availability"
                );

        title.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#111111;"
        );

        Label description =
                new Label(
                        "Select an occupied spot to view vehicle details or release the spot."
                );

        description.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#59636E;"
        );

        // =================================================
        // LEGEND
        // =================================================

        HBox legend =
                new HBox(20);

        legend.setAlignment(
                Pos.CENTER_LEFT
        );

        legend.getChildren().addAll(

                createLegendItem(
                        "#A9D6B5",
                        "Available"
                ),

                createLegendItem(
                        "#C96B6B",
                        "Occupied"
                )
        );

        // =================================================
        // ZONES
        // =================================================

        VBox zones =
                new VBox(14);

        zones.getChildren().add(
                createZone(
                        "ZONE A",
                        parkingSpots,
                        0,
                        4,
                        parkingController,
                        guardEmail
                )
        );

        zones.getChildren().add(
                createZone(
                        "ZONE B",
                        parkingSpots,
                        4,
                        8,
                        parkingController,
                        guardEmail
                )
        );

        zones.getChildren().add(
                createZone(
                        "ZONE C",
                        parkingSpots,
                        8,
                        12,
                        parkingController,
                        guardEmail
                )
        );

        card.getChildren().addAll(
                title,
                description,
                legend,
                zones
        );

        return card;
    }

    // =====================================================
    // LEGEND
    // =====================================================

    private static HBox createLegendItem(
            String color,
            String text) {

        HBox box =
                new HBox(7);

        box.setAlignment(
                Pos.CENTER_LEFT
        );

        Region indicator =
                new Region();

        indicator.setPrefSize(
                12,
                12
        );

        indicator.setMinSize(
                12,
                12
        );

        indicator.setStyle(
                "-fx-background-color:" +
                color +
                ";" +
                "-fx-background-radius:4;"
        );

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#555555;"
        );

        box.getChildren().addAll(
                indicator,
                label
        );

        return box;
    }

    // =====================================================
    // ZONE
    // =====================================================

    private static VBox createZone(
            String zoneName,
            ArrayList<ParkingSpot> parkingSpots,
            int start,
            int end,
            ParkingController parkingController,
            String guardEmail) {

        VBox zone =
                new VBox(7);

        Label zoneLabel =
                new Label(zoneName);

        zoneLabel.setStyle(
                "-fx-font-size:11px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#5D4037;"
        );

        // =================================================
        // FOUR SPOTS IN ONE ROW
        // =================================================

        GridPane grid =
                new GridPane();

        grid.setHgap(10);
        grid.setVgap(8);

        for (int i = start; i < end; i++) {

            ParkingSpot spot =
                    parkingSpots.get(i);

            Button button =
                    createSpotButton(
                            spot,
                            parkingController,
                            guardEmail
                    );

            grid.add(
                    button,
                    i - start,
                    0
            );
        }

        zone.getChildren().addAll(
                zoneLabel,
                grid
        );

        return zone;
    }

    // =====================================================
    // PARKING SPOT BUTTON
    // =====================================================

    private static Button createSpotButton(
            ParkingSpot spot,
            ParkingController parkingController,
            String guardEmail) {

        Button button =
                new Button();

        // =================================================
        // PROPORTIONAL SIZE
        // =================================================

        button.setPrefWidth(155);
        button.setMinWidth(135);

        button.setPrefHeight(62);
        button.setMinHeight(58);

        button.setMaxHeight(65);

        updateSpotButton(
                button,
                spot
        );

        // =================================================
        // ACTION
        // =================================================

        button.setOnAction(e -> {

            // =================================================
            // AVAILABLE
            // =================================================

            if (!spot.occupied) {

                showMessage(
                        "Available Spot",
                        "Parking spot "
                                + spot.spotNumber
                                + " is available."
                );

                return;
            }

            // =================================================
            // OCCUPIED
            // =================================================

            Alert alert =
                    new Alert(
                            Alert.AlertType.CONFIRMATION
                    );

            alert.setTitle(
                    "Occupied Parking"
            );

            alert.setHeaderText(
                    "Parking Spot "
                            + spot.spotNumber
            );

            alert.setContentText(
                    "Vehicle: "
                            + spot.vehicleNumber
                            + "\nOwner: "
                            + spot.ownerName
                            + "\nType: "
                            + spot.vehicleType
                            + "\n\n"
                            + "Do you want to release this parking spot?"
            );

            ButtonType releaseButton =
                    new ButtonType(
                            "Release Parking"
                    );

            ButtonType cancelButton =
                    new ButtonType(
                            "Cancel",
                            ButtonBar.ButtonData.CANCEL_CLOSE
                    );

            alert.getButtonTypes().setAll(
                    releaseButton,
                    cancelButton
            );

            Optional<ButtonType> result =
                    alert.showAndWait();

            // =================================================
            // RELEASE
            // =================================================

            if (result.isPresent()
                    && result.get() == releaseButton) {

                try {

                    ParkingModel parking =
                            parkingController.getParkingBySpot(
                                    spot.spotNumber,
                                    guardEmail
                            );

                    if (parking == null) {

                        showMessage(
                                "Error",
                                "Active parking record was not found."
                        );

                        return;
                    }

                    boolean released =
                            parkingController.releaseParking(
                                    parking.getParkingId()
                            );

                    if (!released) {

                        showMessage(
                                "Error",
                                "Unable to release parking in Firestore."
                        );

                        return;
                    }

                    // =================================================
                    // LOCAL STATE
                    // =================================================

                    spot.occupied = false;

                    spot.vehicleNumber = "";

                    spot.ownerName = "";

                    spot.vehicleType = "";

                    spot.parkingId = null;

                    updateSpotButton(
                            button,
                            spot
                    );

                    showMessage(
                            "Parking Released",
                            "Parking spot "
                                    + spot.spotNumber
                                    + " is now available."
                    );

                } catch (Exception ex) {

                    ex.printStackTrace();

                    showMessage(
                            "Error",
                            "Unable to release parking."
                    );
                }
            }
        });

        return button;
    }

    // =====================================================
    // UPDATE SPOT UI
    // =====================================================

    private static void updateSpotButton(
            Button button,
            ParkingSpot spot) {

        if (spot.occupied) {

            button.setText(
                    spot.spotNumber
                            + "\n"
                            + "OCCUPIED"
            );

            button.setStyle(
                    "-fx-background-color:#C96B6B;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-size:12px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-background-radius:9;" +
                    "-fx-border-radius:9;" +
                    "-fx-border-color:#B25555;" +
                    "-fx-border-width:1;"
            );

        } else {

            button.setText(
                    spot.spotNumber
                            + "\n"
                            + "AVAILABLE"
            );

            button.setStyle(
                    "-fx-background-color:#A9D6B5;" +
                    "-fx-text-fill:#183A2D;" +
                    "-fx-font-size:12px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-background-radius:9;" +
                    "-fx-border-radius:9;" +
                    "-fx-border-color:#82B892;" +
                    "-fx-border-width:1;"
            );
        }
    }

    // =====================================================
    // VEHICLE ENTRY CARD
    // =====================================================

    private static VBox createEntryCard(
            Stage stage,
            ArrayList<ParkingSpot> parkingSpots,
            ParkingController parkingController,
            String guardEmail,
            String society) {

        VBox card =
                new VBox(14);

        card.setPadding(
                new Insets(20)
        );

        card.setStyle(
                "-fx-background-color:#E8F0E8;" +
                "-fx-background-radius:14;"
        );

        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label(
                        "Vehicle Entry"
                );

        title.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#111111;"
        );

        // =================================================
        // FIELDS
        // =================================================

        Label vehicleLabel =
                createLabel(
                        "Vehicle Number *"
                );

        TextField vehicleField =
                new TextField();

        vehicleField.setPromptText(
                "Example: MH12AB1234"
        );

        styleField(
                vehicleField
        );

        Label ownerLabel =
                createLabel(
                        "Owner / Resident Name *"
                );

        TextField ownerField =
                new TextField();

        ownerField.setPromptText(
                "Enter name"
        );

        styleField(
                ownerField
        );

        Label typeLabel =
                createLabel(
                        "Vehicle Type *"
                );

        ComboBox<String> vehicleTypeBox =
                new ComboBox<>();

        vehicleTypeBox.getItems().addAll(
                "Car",
                "Bike",
                "Scooter",
                "Other"
        );

        vehicleTypeBox.setPromptText(
                "Select vehicle type"
        );

        styleComboBox(
                vehicleTypeBox
        );

        Label selectedLabel =
                createLabel(
                        "Assigned Parking Spot"
                );

        Label selectedSpot =
                new Label(
                        "Not assigned"
                );

        selectedSpot.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#5D4037;"
        );

        // =================================================
        // FIELD GRID
        // =================================================

        GridPane grid =
                new GridPane();

        grid.setHgap(22);
        grid.setVgap(8);

        ColumnConstraints c1 =
                new ColumnConstraints();

        ColumnConstraints c2 =
                new ColumnConstraints();

        c1.setPercentWidth(50);
        c2.setPercentWidth(50);

        c1.setHgrow(
                Priority.ALWAYS
        );

        c2.setHgrow(
                Priority.ALWAYS
        );

        grid.getColumnConstraints().addAll(
                c1,
                c2
        );

        grid.add(
                vehicleLabel,
                0,
                0
        );

        grid.add(
                ownerLabel,
                1,
                0
        );

        grid.add(
                vehicleField,
                0,
                1
        );

        grid.add(
                ownerField,
                1,
                1
        );

        grid.add(
                typeLabel,
                0,
                2
        );

        grid.add(
                selectedLabel,
                1,
                2
        );

        grid.add(
                vehicleTypeBox,
                0,
                3
        );

        grid.add(
                selectedSpot,
                1,
                3
        );

        // =================================================
        // BUTTONS
        // =================================================

        Button findSpotButton =
                new Button(
                        "Find Available Spot"
                );

        findSpotButton.setPrefWidth(
                175
        );

        findSpotButton.setPrefHeight(
                39
        );

        findSpotButton.setStyle(
                "-fx-background-color:#4E342E;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;"
        );

        Button parkButton =
                new Button(
                        "Park Vehicle"
                );

        parkButton.setPrefWidth(
                145
        );

        parkButton.setPrefHeight(
                39
        );

        parkButton.setStyle(
                "-fx-background-color:#4E342E;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;"
        );

        Button clearButton =
                new Button(
                        "Clear"
                );

        clearButton.setPrefWidth(
                90
        );

        clearButton.setPrefHeight(
                39
        );

        clearButton.setStyle(
                "-fx-background-color:white;" +
                "-fx-text-fill:#4E342E;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-border-color:#D4C7BF;" +
                "-fx-border-radius:7;"
        );

        HBox buttons =
                new HBox(
                        10,
                        findSpotButton,
                        parkButton,
                        clearButton
                );

        buttons.setAlignment(
                Pos.CENTER_RIGHT
        );

        // =================================================
        // FIND SPOT
        // =================================================

        findSpotButton.setOnAction(e -> {

            ParkingSpot availableSpot =
                    findAvailableSpot(
                            parkingSpots
                    );

            if (availableSpot == null) {

                showMessage(
                        "Parking Full",
                        "There are no available parking spots."
                );

                selectedSpot.setText(
                        "No spot available"
                );

                return;
            }

            selectedSpot.setText(
                    availableSpot.spotNumber
            );
        });

        // =================================================
        // PARK VEHICLE
        // =================================================

        parkButton.setOnAction(e -> {

            // =================================================
            // VALIDATION
            // =================================================

            if (vehicleField.getText()
                    .trim()
                    .isEmpty()
                    ||
                    ownerField.getText()
                            .trim()
                            .isEmpty()
                    ||
                    vehicleTypeBox.getValue() == null) {

                showMessage(
                        "Missing Information",
                        "Please enter vehicle number, owner name and vehicle type."
                );

                return;
            }

            // =================================================
            // FIND SELECTED SPOT
            // =================================================

            ParkingSpot spot =
                    findSpotByNumber(
                            parkingSpots,
                            selectedSpot.getText()
                    );

            if (spot == null) {

                showMessage(
                        "Parking Spot Required",
                        "Please find an available parking spot first."
                );

                return;
            }

            // =================================================
            // CHECK OCCUPANCY
            // =================================================

            if (spot.occupied) {

                showMessage(
                        "Spot Occupied",
                        "This parking spot is already occupied."
                );

                selectedSpot.setText(
                        "Not assigned"
                );

                return;
            }

            // =================================================
            // SOCIETY VALIDATION
            // =================================================

            if (society == null ||
                    society.trim().isEmpty()) {

                showMessage(
                        "Society Information Missing",
                        "The logged-in guard's society could not be determined."
                );

                return;
            }

            // =================================================
            // EMAIL VALIDATION
            // =================================================

            if (guardEmail == null ||
                    guardEmail.trim().isEmpty()) {

                showMessage(
                        "Guard Information Missing",
                        "The logged-in guard could not be identified."
                );

                return;
            }

            // =================================================
            // VEHICLE DATA
            // =================================================

            String vehicleNumber =
                    vehicleField.getText()
                            .trim()
                            .toUpperCase();

            String ownerName =
                    ownerField.getText()
                            .trim();

            String vehicleType =
                    vehicleTypeBox.getValue();

            // =================================================
            // SAVE TO FIRESTORE
            // =================================================

            boolean saved =
                    parkingController.saveParking(
                            guardEmail,
                            society,
                            spot.spotNumber,
                            vehicleNumber,
                            ownerName,
                            vehicleType
                    );

            if (!saved) {

                showMessage(
                        "Parking Failed",
                        "This parking spot may already be occupied or the information could not be saved."
                );

                return;
            }

            // =================================================
            // LOCAL UPDATE
            // =================================================

            spot.occupied = true;

            spot.vehicleNumber =
                    vehicleNumber;

            spot.ownerName =
                    ownerName;

            spot.vehicleType =
                    vehicleType;

            // =================================================
            // SUCCESS
            // =================================================

            showMessage(
                    "Parking Allotted",
                    "Vehicle allotted to parking spot "
                            + spot.spotNumber
                            + "."
            );

            // =================================================
            // CLEAR FORM
            // =================================================

            vehicleField.clear();

            ownerField.clear();

            vehicleTypeBox.setValue(
                    null
            );

            selectedSpot.setText(
                    "Not assigned"
            );

            // =================================================
            // REFRESH
            // =================================================

            stage.setScene(
                    createScene(
                            stage,
                            guardEmail,
                            society
                    )
            );
        });

        // =================================================
        // CLEAR
        // =================================================

        clearButton.setOnAction(e -> {

            vehicleField.clear();

            ownerField.clear();

            vehicleTypeBox.setValue(
                    null
            );

            selectedSpot.setText(
                    "Not assigned"
            );
        });

        card.getChildren().addAll(
                title,
                grid,
                buttons
        );

        return card;
    }

    // =====================================================
    // LOAD PARKING
    // =====================================================

    private static void loadParkingFromFirestore(
            ParkingController parkingController,
            String guardEmail,
            ArrayList<ParkingSpot> parkingSpots) {

        try {

            if (guardEmail == null ||
                    guardEmail.trim().isEmpty()) {

                return;
            }

            List<ParkingModel> firestoreParking =
                    parkingController.getAllParking(
                            guardEmail
                    );

            for (ParkingModel parking :
                    firestoreParking) {

                if (parking == null) {
                    continue;
                }

                ParkingSpot spot =
                        findSpotByNumber(
                                parkingSpots,
                                parking.getSpotNumber()
                        );

                if (spot == null) {
                    continue;
                }

                spot.occupied =
                        parking.isOccupied();

                spot.parkingId =
                        parking.getParkingId();

                spot.vehicleNumber =
                        parking.getVehicleNumber() != null
                                ? parking.getVehicleNumber()
                                : "";

                spot.ownerName =
                        parking.getOwnerName() != null
                                ? parking.getOwnerName()
                                : "";

                spot.vehicleType =
                        parking.getVehicleType() != null
                                ? parking.getVehicleType()
                                : "";
            }

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to load parking from Firestore."
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // FIND AVAILABLE SPOT
    // =====================================================

    private static ParkingSpot findAvailableSpot(
            ArrayList<ParkingSpot> parkingSpots) {

        for (ParkingSpot spot :
                parkingSpots) {

            if (!spot.occupied) {

                return spot;
            }
        }

        return null;
    }

    // =====================================================
    // FIND SPOT BY NUMBER
    // =====================================================

    private static ParkingSpot findSpotByNumber(
            ArrayList<ParkingSpot> parkingSpots,
            String spotNumber) {

        if (spotNumber == null ||
                spotNumber.trim().isEmpty()) {

            return null;
        }

        for (ParkingSpot spot :
                parkingSpots) {

            if (spot.spotNumber.equals(
                    spotNumber.trim()
            )) {

                return spot;
            }
        }

        return null;
    }

    // =====================================================
    // SUMMARY
    // =====================================================

    private static HBox createSummary(
            ArrayList<ParkingSpot> parkingSpots) {

        int total =
                parkingSpots.size();

        int occupied = 0;

        for (ParkingSpot spot :
                parkingSpots) {

            if (spot.occupied) {

                occupied++;
            }
        }

        int available =
                total - occupied;

        HBox summary =
                new HBox(14);

        summary.setAlignment(
                Pos.CENTER_LEFT
        );

        summary.getChildren().addAll(

                createSummaryCard(
                        "TOTAL SPOTS",
                        String.valueOf(total)
                ),

                createSummaryCard(
                        "AVAILABLE",
                        String.valueOf(available)
                ),

                createSummaryCard(
                        "OCCUPIED",
                        String.valueOf(occupied)
                )
        );

        return summary;
    }

    // =====================================================
    // SUMMARY CARD
    // =====================================================

    private static VBox createSummaryCard(
            String title,
            String value) {

        VBox card =
                new VBox(3);

        card.setPrefWidth(
                170
        );

        card.setPrefHeight(
                68
        );

        card.setPadding(
                new Insets(
                        9,
                        13,
                        9,
                        13
                )
        );

        card.setAlignment(
                Pos.CENTER_LEFT
        );

        card.setStyle(
                "-fx-background-color:#E8F0E8;" +
                "-fx-background-radius:10;"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size:10px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#65706A;"
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setStyle(
                "-fx-font-size:21px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#4E342E;"
        );

        card.getChildren().addAll(
                titleLabel,
                valueLabel
        );

        return card;
    }

    // =====================================================
    // FORM LABEL
    // =====================================================

    private static Label createLabel(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size:11px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#333333;"
        );

        return label;
    }

    // =====================================================
    // TEXT FIELD
    // =====================================================

    private static void styleField(
            TextField field) {

        field.setMaxWidth(
                Double.MAX_VALUE
        );

        field.setPrefHeight(
                39
        );

        field.setStyle(
                "-fx-background-color:#F4F7F4;" +
                "-fx-background-radius:7;" +
                "-fx-text-fill:#111111;" +
                "-fx-prompt-text-fill:#6B7280;" +
                "-fx-border-color:#D5DDD5;" +
                "-fx-border-radius:7;"
        );
    }

    // =====================================================
    // COMBO BOX
    // =====================================================

    private static void styleComboBox(
            ComboBox<String> box) {

        box.setMaxWidth(
                Double.MAX_VALUE
        );

        box.setPrefHeight(
                39
        );

        box.setStyle(
                "-fx-background-color:#F4F7F4;" +
                "-fx-background-radius:7;" +
                "-fx-text-fill:#111111;" +
                "-fx-border-color:#D5DDD5;" +
                "-fx-border-radius:7;"
        );
    }

    // =====================================================
    // MESSAGE
    // =====================================================

    private static void showMessage(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}