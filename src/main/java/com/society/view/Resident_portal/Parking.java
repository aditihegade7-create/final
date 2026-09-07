package com.society.view.Resident_portal;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.society.view.ScreenSize;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class Parking {

    // Your assigned parking slot
    private final String myParkingSlot = "P-03";

    public Scene getParkingScene(Stage stage, String loginEmail) {

        BorderPane borderPane = new BorderPane();

        Image bgImage = new Image(
        getClass().getResource("/background-Dashboard5.jpeg").toExternalForm()
        );

        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
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

        borderPane.setBackground(
                new Background(backgroundImage)
        );

        // ==================================================
        // SIDEBAR
        // ==================================================

        panel panelObj = new panel(stage, myParkingSlot);


        
         

        // ==================================================
        // HEADER
        // ==================================================

        Label title = new Label("Smart Parking");

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );


        Label subtitle = new Label(
                "Check parking availability and monitor your assigned parking area"
        );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: white;"
        );


        VBox header = new VBox(5);

        header.getChildren().addAll(
                title,
                subtitle
        );

       // header.setPadding(new Insets(20));

        header.setStyle(
                "-fx-background-color: #4e342e;"
        );


        // ==================================================
        // MAIN CONTENT
        // ==================================================

        VBox mainContent = new VBox(25);

        mainContent.setPadding(
                new Insets(25)
        );

        
        mainContent.setStyle(
                 "-fx-background-color: transparent;"
        );
        FadeTransition pageFade = new FadeTransition(
        Duration.millis(700),
        mainContent
        );

        pageFade.setFromValue(0.0);
        pageFade.setToValue(1.0);
        pageFade.setCycleCount(1);

        pageFade.play();

       

        // ==================================================
        // MY PARKING CARD
        // ==================================================

        Label myParkingTitle =
                new Label("My Parking Area");

        myParkingTitle.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4e342e;"
        );


        Label mySlot =
                new Label("Assigned Parking: " + myParkingSlot);

        mySlot.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #37474f;"
        );


        Label myStatus =
                new Label("● AVAILABLE");

        myStatus.setStyle(
                "-fx-text-fill: green;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;"
        );


        VBox myParkingCard = new VBox(
                10,
                myParkingTitle,
                mySlot,
                myStatus
        );

        myParkingCard.setPadding(
                new Insets(20)
        );

        myParkingCard.setMaxWidth(
                Double.MAX_VALUE
        );

        myParkingCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #789098;" +
                "-fx-border-radius: 12;"
        );


        // ==================================================
        // PARKING AVAILABILITY CARD
        // ==================================================

        Label availabilityTitle =
                new Label("Parking Availability");

        availabilityTitle.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4e342e;"
        );


        // ==================================================
        // PARKING GRID
        // ==================================================

        GridPane parkingGrid =
                new GridPane();

        parkingGrid.setHgap(20);

        parkingGrid.setVgap(20);

        parkingGrid.setAlignment(
                Pos.CENTER
        );


        // P-01 to P-08

        addParkingSlot(
                parkingGrid,
                "P-01",
                0,
                0,
                false
        );

        addParkingSlot(
                parkingGrid,
                "P-02",
                1,
                0,
                true
        );

        addParkingSlot(
                parkingGrid,
                "P-03",
                2,
                0,
                false
        );

        addParkingSlot(
                parkingGrid,
                "P-04",
                3,
                0,
                false
        );


        addParkingSlot(
                parkingGrid,
                "P-05",
                0,
                1,
                true
        );

        addParkingSlot(
                parkingGrid,
                "P-06",
                1,
                1,
                false
        );

        addParkingSlot(
                parkingGrid,
                "P-07",
                2,
                1,
                true
        );

        addParkingSlot(
                parkingGrid,
                "P-08",
                3,
                1,
                false
        );


        // ==================================================
        // SIMULATION BUTTON
        // ==================================================

        Button simulateButton =
                new Button("🚗  Simulate Car Parking");

        simulateButton.setStyle(
                "-fx-background-color: #4e342e;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 10px 20px;" +
                "-fx-background-radius: 8px;"
        );


        // ==================================================
        // SIMULATION ACTION
        // ==================================================

        simulateButton.setOnAction(event -> {

            // Change status

            myStatus.setText("● OCCUPIED");

            myStatus.setStyle(
                    "-fx-text-fill: red;" +
                    "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;"
            );


            // Notification

            Alert alert =
                    new Alert(Alert.AlertType.WARNING);

            alert.setTitle(
                    "Smart Parking Alert"
            );

            alert.setHeaderText(
                    "Parking Alert!"
            );

            alert.setContentText(
                    "A vehicle has been detected in your parking area "
                    + myParkingSlot
                    + ".\n\nPlease check your parking slot."
            );

            alert.show();
        });


        // ==================================================
        // WHITE PARKING AREA
        // ==================================================

        VBox parkingArea =
                new VBox(20);

        parkingArea.setPadding(
                new Insets(25)
        );

        parkingArea.setAlignment(
                Pos.CENTER
        );

        parkingArea.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: #789098;" +
                "-fx-border-radius: 15;"
        );


        parkingArea.getChildren().addAll(
                availabilityTitle,
                parkingGrid,
                simulateButton
        );


        // ==================================================
        // ADD CONTENT
        // ==================================================

        mainContent.getChildren().addAll(
                myParkingCard,
                parkingArea
        );


        // ==================================================
        // SCROLL PANE
        // ==================================================

        ScrollPane scrollPane =
                new ScrollPane(mainContent);

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                 "-fx-background: transparent;" +
        "-fx-background-color: transparent;"
        );


// ==================================================
                // CENTER AREA
        // HEADER + CONTENT
        // ==================================================

        BorderPane centerPane =
                new BorderPane();

        
BorderPane mainarea = new BorderPane();
mainarea.setTop(header);
mainarea.setCenter(scrollPane);
header.setStyle("-fx-background-color: #b3adad");




        centerPane.setCenter(mainarea);


        // ==================================================
        // MAIN BORDERPANE
        // ==================================================

       // BorderPane borderPane =
        //        new BorderPane();

        // Sidebar

        borderPane.setLeft(
                panelObj.getSidebar()
        );


        // Center

        borderPane.setCenter(
                centerPane
        );


        // Entire background

       // borderPane.setStyle(
       //         "-fx-background-color: #b3adad;"
        //);


        // ==================================================
        // SCENE
        // ==================================================

        Scene scene = new Scene(
                borderPane,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );


        return scene;
    }


    // ==================================================
    // PARKING SLOT METHOD
    // ==================================================

    private void addParkingSlot(
            GridPane grid,
            String slotName,
            int column,
            int row,
            boolean occupied) {


        Label slot = new Label();


        // Size of parking slot

        slot.setPrefSize(
                250,
                150
        );


        slot.setAlignment(
                Pos.CENTER
        );


        // ==================================================
        // SLOT TEXT
        // ==================================================

        if (slotName.equals(myParkingSlot)) {

            slot.setText(
                    "⭐ " + slotName +
                    "\nMY PARKING"
            );

        } else {

            slot.setText(
                    slotName
            );
        }


        // ==================================================
        // OCCUPIED
        // ==================================================

        if (occupied) {

            slot.setStyle(
                    "-fx-background-color: #e57373;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 10;"
            );
        }


        // ==================================================
        // MY PARKING
        // ==================================================

        else if (
                slotName.equals(myParkingSlot)
        ) {

            slot.setStyle(
                    "-fx-background-color: #81c784;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #2e7d32;" +
                    "-fx-border-width: 3;" +
                    "-fx-border-radius: 10;"
            );
        }


        // ==================================================
        // AVAILABLE
        // ==================================================

        else {

            slot.setStyle(
                    "-fx-background-color: #a5d6a7;" +
                    "-fx-text-fill: #1b5e20;" +
                    "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 10;"
            );
        }


        // Add slot to grid

        grid.add(
                slot,
                column,
                row
        );
    }
}