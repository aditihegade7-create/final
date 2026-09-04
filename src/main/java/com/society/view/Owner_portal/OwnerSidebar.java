package com.society.view.Owner_portal;

import com.society.view.Welcome.LogInPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class OwnerSidebar {

    private VBox sidebar;

    // =========================================================
    // LOGGED-IN OWNER EMAIL
    // =========================================================

    private String ownerEmail;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public OwnerSidebar(
            Stage stage,
            String ownerEmail) {

        this.ownerEmail = ownerEmail;

        sidebar = new VBox();

        sidebar.setPadding(
                new Insets(20)
        );

        sidebar.setPrefWidth(280);

        sidebar.setPrefHeight(750);

        sidebar.setAlignment(
                Pos.TOP_LEFT
        );

        sidebar.setStyle(
                "-fx-background-color: #593a32;"
        );


        // =====================================================
        // LOGO
        // =====================================================

        Label logo =
                new Label("Society360");

        logo.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );


        // =====================================================
        // PORTAL
        // =====================================================

        Label portal =
                new Label("Owner Portal");

        portal.setTextFill(
                Color.LIGHTBLUE
        );


        // =====================================================
        // BUTTONS
        // =====================================================

        Button dashboard =
                new Button("Dashboard");

        Button addTenant =
                new Button("Add Tenant");

        Button removeTenant =
                new Button("Remove Tenant");

        Button payments =
                new Button("View Payments");

        Button rentalHistory =
                new Button("Rental History");

        Button ownerDocuments =
                new Button("Owner Document");

        Button profile =
                new Button("Profile");


        // =====================================================
        // BUTTON SIZE
        // =====================================================

        dashboard.setPrefSize(300, 40);
        addTenant.setPrefSize(300, 40);
        removeTenant.setPrefSize(300, 40);
        payments.setPrefSize(300, 40);
        rentalHistory.setPrefSize(300, 40);
        ownerDocuments.setPrefSize(300, 40);
        profile.setPrefSize(300, 40);


        sidebar.setSpacing(10);


        // =====================================================
        // BUTTON STYLE
        // =====================================================

        String buttonStyle =
                "-fx-background-color: #4e423e;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;";


        dashboard.setStyle(buttonStyle);
        addTenant.setStyle(buttonStyle);
        removeTenant.setStyle(buttonStyle);
        payments.setStyle(buttonStyle);
        rentalHistory.setStyle(buttonStyle);
        ownerDocuments.setStyle(buttonStyle);
        profile.setStyle(buttonStyle);


        // =====================================================
        // DASHBOARD
        // =====================================================

        dashboard.setOnAction(e -> {

            stage.setScene(
                    OwnerDashboard.createScene(
                            stage,
                            ownerEmail
                    )
            );
        });


        // =====================================================
        // ADD TENANT
        // =====================================================

        addTenant.setOnAction(e -> {

            stage.setScene(
                    AddTenant.createScene(
                            stage,
                            ownerEmail
                    )
            );
        });


        // =====================================================
        // REMOVE TENANT
        // =====================================================

        removeTenant.setOnAction(e -> {

            stage.setScene(
                    RemoveTenant.createScene(
                            stage,
                            ownerEmail
                    )
            );
        });

        // =====================================================
        // PAYMENTS
        // =====================================================

        payments.setOnAction(e -> {

            stage.setScene(
                    ViewPayments.createScene(
                            stage,
                            ownerEmail
                    )
            );
        });


        // =====================================================
        // RENTAL HISTORY
        // =====================================================

        rentalHistory.setOnAction(e -> {

            stage.setScene(
                    RentalHistory.createScene(
                            stage,
                            ownerEmail
                    )
            );
        });


        // =====================================================
        // OWNER DOCUMENTS
        // =====================================================

        ownerDocuments.setOnAction(e -> {

    stage.setScene(
            OwnerDocuments.createScene(
                    stage,
                    ownerEmail
            )
    );

    stage.show();
});

        // =====================================================
        // PROFILE
        // =====================================================

        profile.setOnAction(e -> {

            stage.setScene(
                    OwnerProfile.createScene(
                            stage,
                            ownerEmail
                    )
            );
        });


        // =====================================================
        // ADD EVERYTHING
        // =====================================================

        sidebar.getChildren().addAll(
                logo,
                portal,
                dashboard,
                addTenant,
                removeTenant,
                payments,
                rentalHistory,
                ownerDocuments,
                profile
        );
    }


    // =========================================================
    // GET SIDEBAR
    // =========================================================

    public VBox getSidebar() {

        return sidebar;
    }
}