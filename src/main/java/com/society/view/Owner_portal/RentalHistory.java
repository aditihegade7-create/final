package com.society.view.Owner_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.controller.Secretary_Controller.ReportController;
import com.society.model.Owner_model.ResidentModel;
//import com.society.model.Resident_model.ResidentModel;
import com.society.view.ScreenSize;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RentalHistory {

    private static final ReportController residentController =
            new ReportController();

    // =========================================================
    // CREATE SCENE
    // =========================================================

    public static Scene createScene(
            Stage stage,
            String ownerEmail) {

        BorderPane root = new BorderPane();

        // =====================================================
        // SIDEBAR - LEFT
        // =====================================================

        OwnerSidebar sidebar =
                new OwnerSidebar(
                        stage,
                        ownerEmail
                );

        root.setLeft(
                sidebar.getSidebar()
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainContent = new VBox();

        mainContent.setPadding(
                new Insets(25, 35, 25, 35)
        );

        mainContent.setSpacing(15);
        mainContent.setAlignment(Pos.TOP_LEFT);

        // =====================================================
        // BACKGROUND IMAGE
        // =====================================================

        Image image =
                new Image(
                        RentalHistory.class
                                .getResource(
                                        "/background-Dashboard5.jpeg"
                                )
                                .toExternalForm()
                );

        BackgroundImage backgroundImage =
                new BackgroundImage(
                        image,
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

        mainContent.setBackground(
                new Background(backgroundImage)
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

Label greeting =
        new Label(
                "Rental History"
        );

greeting.setStyle(
        "-fx-font-family:'Bodoni MT';" +
        "-fx-font-size:24px;" +
        "-fx-font-weight:bold;" +
        "-fx-text-fill:white;"
);

Label description =
        new Label(
                "View rental history of tenants"
        );

description.setStyle(
        "-fx-font-size:14px;" +
        "-fx-text-fill:white;"
);

headerText.getChildren().addAll(
        greeting,
        description
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

mainContent.getChildren().add(
        header
);
    
    
        // =====================================================
        // TABLE
        // =====================================================

        TableView<ResidentModel> table =
                new TableView<>();

        table.setPrefHeight(550);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        // =====================================================
        // TENANT NAME
        // =====================================================

        TableColumn<ResidentModel, String>
                tenantColumn =
                new TableColumn<>("Tenant Name");

        tenantColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        // =====================================================
        // FLAT
        // =====================================================

        TableColumn<ResidentModel, String>
                flatColumn =
                new TableColumn<>("Flat / Unit");

        flatColumn.setCellValueFactory(
                new PropertyValueFactory<>("flat")
        );

        // =====================================================
        // MOVE-IN
        // =====================================================

        TableColumn<ResidentModel, String>
                moveInColumn =
                new TableColumn<>("Move-in Date");

        moveInColumn.setCellValueFactory(
                new PropertyValueFactory<>("moveInDate")
        );

        // =====================================================
        // MOVE-OUT
        // =====================================================

        TableColumn<ResidentModel, String>
                moveOutColumn =
                new TableColumn<>("Move-out Date");

        moveOutColumn.setCellValueFactory(
                new PropertyValueFactory<>("moveOutDate")
        );

        // =====================================================
        // RENT
        // =====================================================

        TableColumn<ResidentModel, String>
                rentColumn =
                new TableColumn<>("Rent (Monthly)");

        rentColumn.setCellValueFactory(
                new PropertyValueFactory<>("monthlyRent")
        );

        // =====================================================
        // STATUS
        // =====================================================

        TableColumn<ResidentModel, String>
                statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );

        table.getColumns().addAll(
                tenantColumn,
                flatColumn,
                moveInColumn,
                moveOutColumn,
                rentColumn,
                statusColumn
        );

        // =====================================================
        // LOAD FIRESTORE RENTAL HISTORY
        // =====================================================


        table.setItems(FXCollections.observableArrayList());
        //TableView<ResidentModel> table = new TableView<>();
       /*  try {

            List<ResidentModel> rentals =
                    residentController.getRentalHistory(
                            ownerEmail
                    );

            table.setItems(
                    FXCollections.observableArrayList(
                            rentals
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            table.setPlaceholder(
                    new Label(
                            "Unable to load rental history."
                    )
            );
        }*/

        // =====================================================
        // TABLE STYLE
        // =====================================================

        table.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#D5DDE0;" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;"
        );

        mainContent.getChildren().add(
                table
        );

        // =====================================================
        // EMPTY MESSAGE
        // =====================================================

        if (table.getItems().isEmpty()) {

            Label emptyLabel =
                    new Label(
                            "No rental history found."
                    );

            emptyLabel.setStyle(
                    "-fx-font-size:14px;" +
                    "-fx-text-fill:#546E7A;"
            );

            mainContent.getChildren().add(
                    emptyLabel
            );
        }

        // =====================================================
        // ANIMATIONS
        // =====================================================

     //   animateHeader(header);
        animateTable(table);

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
    // HEADER ANIMATION
    // =========================================================

   

    // =========================================================
    // TABLE ANIMATION
    // =========================================================

    private static void animateTable(
            TableView<ResidentModel> table) {

        table.setOpacity(0);

        table.setScaleX(0.97);
        table.setScaleY(0.97);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(700),
                        table
                );

        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(
                Duration.millis(250)
        );

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(700),
                        table
                );

        scale.setFromX(0.97);
        scale.setFromY(0.97);

        scale.setToX(1.0);
        scale.setToY(1.0);

        scale.setDelay(
                Duration.millis(250)
        );

        new ParallelTransition(
                fade,
                scale
        ).play();

        // =====================================================
        // TABLE HOVER
        // =====================================================

        table.setOnMouseEntered(e -> {

            ScaleTransition hover =
                    new ScaleTransition(
                            Duration.millis(150),
                            table
                    );

            hover.setToX(1.005);
            hover.setToY(1.005);

            hover.play();
        });

        table.setOnMouseExited(e -> {

            ScaleTransition hover =
                    new ScaleTransition(
                            Duration.millis(150),
                            table
                    );

            hover.setToX(1.0);
            hover.setToY(1.0);

            hover.play();
        });
    }
}