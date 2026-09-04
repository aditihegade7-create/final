package com.society.view.Owner_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.controller.Owner_Controller.ResidentController;
import com.society.model.Owner_model.ResidentModel;

import javafx.collections.FXCollections;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;


public class RentalHistory {

    // =========================================================
    // CONTROLLER
    // =========================================================

    private static final ResidentController residentController =
            new ResidentController();


    // =========================================================
    // CREATE SCENE
    // =========================================================

    public static Scene createScene(Stage stage, String ownerEmail) {

        BorderPane root =
                new BorderPane();


        // =====================================================
        // SIDEBAR
        // =====================================================

        OwnerSidebar sidebar =
                new OwnerSidebar(stage, ownerEmail);

        root.setLeft(
                sidebar.getSidebar()
        );


        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainContent =
                new VBox();

        mainContent.setPadding(
                new Insets(25, 35, 25, 35)
        );

        mainContent.setSpacing(15);

        mainContent.setAlignment(
                Pos.TOP_LEFT
        );

        mainContent.setStyle(
                "-fx-background-color:#e8ddd5;"
        );


        // =====================================================
        // HEADER
        // =====================================================

        HBox header =
                new HBox();

        header.setPrefWidth(900);

        header.setPrefHeight(80);

        header.setPadding(
                new Insets(20)
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

        VBox heading =
                new VBox();

        heading.setSpacing(3);


        Label greeting =
                new Label(
                        "Rental History"
                );

        greeting.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#ffffff;"
        );


        Label description =
                new Label(
                        "View rental history of tenants"
                );

        description.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#ffffff;"
        );


        heading.getChildren().addAll(
                greeting,
                description
        );


        // =====================================================
        // HEADER SPACER
        // =====================================================

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        // =====================================================
        // CURRENT DATE
        // =====================================================

        Label day =
                new Label();

        Label date =
                new Label();


        LocalDate today =
                LocalDate.now();


        day.setText(
                today.format(
                        DateTimeFormatter.ofPattern(
                                "EEEE"
                        )
                )
        );

        day.setStyle(
                "-fx-text-fill:#ffffff;"
        );


        date.setText(
                today.format(
                        DateTimeFormatter.ofPattern(
                                "dd MMMM yyyy"
                        )
                )
        );

        date.setStyle(
                "-fx-text-fill:#ffffff;"
        );


        VBox dateBox =
                new VBox();

        dateBox.setSpacing(2);

        dateBox.getChildren().addAll(
                day,
                date
        );


        header.getChildren().addAll(
                heading,
                spacer,
                dateBox
        );


        // =====================================================
        // TABLE
        // =====================================================

        TableView<ResidentModel> table =
                new TableView<>();


        table.setPrefHeight(
                550
        );


        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );


        // =====================================================
        // TENANT NAME
        // =====================================================

        TableColumn<ResidentModel, String>
                tenantColumn =
                new TableColumn<>(
                        "Tenant Name"
                );


        tenantColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "name"
                )
        );


        // =====================================================
        // FLAT
        // =====================================================

        TableColumn<ResidentModel, String>
                flatColumn =
                new TableColumn<>(
                        "Flat / Unit"
                );


        flatColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "flat"
                )
        );


        // =====================================================
        // MOVE-IN DATE
        // =====================================================

        TableColumn<ResidentModel, String>
                moveInColumn =
                new TableColumn<>(
                        "Move-in Date"
                );


        moveInColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "moveInDate"
                )
        );


        // =====================================================
        // MOVE-OUT DATE
        // =====================================================

        TableColumn<ResidentModel, String>
                moveOutColumn =
                new TableColumn<>(
                        "Move-out Date"
                );


        moveOutColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "moveOutDate"
                )
        );


        // =====================================================
        // MONTHLY RENT
        // =====================================================

        TableColumn<ResidentModel, String>
                rentColumn =
                new TableColumn<>(
                        "Rent (Monthly)"
                );


        rentColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "monthlyRent"
                )
        );


        // =====================================================
        // STATUS
        // =====================================================

        TableColumn<ResidentModel, String>
                statusColumn =
                new TableColumn<>(
                        "Status"
                );


        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "status"
                )
        );


        // =====================================================
        // ADD COLUMNS
        // =====================================================

        table.getColumns().addAll(
                tenantColumn,
                flatColumn,
                moveInColumn,
                moveOutColumn,
                rentColumn,
                statusColumn
        );


        
System.out.println(
        "RENTAL HISTORY OWNER EMAIL = "
                + ownerEmail
);

System.out.println(
        "===================================="
);
        // =====================================================
        // LOAD RENTAL HISTORY FROM FIRESTORE
        // =====================================================

        List<ResidentModel> rentals =
                residentController.getRentalHistory(
                        ownerEmail
                );


        // =====================================================
        // SET TABLE DATA
        // =====================================================

        table.setItems(
                FXCollections.observableArrayList(
                        rentals
                )
        );


        // =====================================================
        // TABLE STYLE
        // =====================================================

        table.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#D5DDE0;"
        );


        // =====================================================
        // EMPTY MESSAGE
        // =====================================================

        if (rentals.isEmpty()) {

            Label emptyLabel =
                    new Label(
                            "No rental history found."
                    );


            emptyLabel.setStyle(
                    "-fx-font-size:13px;" +
                    "-fx-text-fill:#546E7A;"
            );


            mainContent.getChildren().addAll(
                    header,
                    table,
                    emptyLabel
            );

        } else {

            mainContent.getChildren().addAll(
                    header,
                    table
            );
        }


        // =====================================================
        // MAIN AREA
        // =====================================================

        root.setCenter(
                mainContent
        );


        // =====================================================
        // RETURN SCENE
        // =====================================================

        return new Scene(
                root,
                1500,
                750
        );
    }
}