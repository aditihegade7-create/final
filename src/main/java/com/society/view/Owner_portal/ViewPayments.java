package com.society.view.Owner_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.controller.Owner_Controller.PaymentController;
import com.society.model.Owner_model.PaymentModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

public class ViewPayments {

    private static final PaymentController paymentController =
            new PaymentController();


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


        VBox vb =
                new VBox();


        Label greeting =
                new Label("Payments");

        greeting.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#ffffff;"
        );


        Label description =
                new Label(
                        "View all rental and maintenance payment"
                );

        description.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#ffffff;"
        );


        vb.getChildren().addAll(
                greeting,
                description
        );


        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


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


        VBox vb1 =
                new VBox();

        vb1.getChildren().addAll(
                day,
                date
        );


        header.getChildren().addAll(
                vb,
                spacer,
                vb1
        );


        mainContent.getChildren().add(
                header
        );


        // =====================================================
        // PAYMENT TABLE
        // =====================================================

        TableView<PaymentModel> table =
                new TableView<>();

        table.setPrefHeight(500);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );


        // =====================================================
        // DATE COLUMN
        // =====================================================

        TableColumn<PaymentModel, String>
                dateColumn =
                new TableColumn<>("Date");

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("date")
        );


        // =====================================================
        // TENANT COLUMN
        // =====================================================

        TableColumn<PaymentModel, String>
                tenantColumn =
                new TableColumn<>("Tenant");

        tenantColumn.setCellValueFactory(
                new PropertyValueFactory<>("tenant")
        );


        // =====================================================
        // FLAT COLUMN
        // =====================================================

        TableColumn<PaymentModel, String>
                flatColumn =
                new TableColumn<>("Flat / Unit");

        flatColumn.setCellValueFactory(
                new PropertyValueFactory<>("flat")
        );


        // =====================================================
        // PAYMENT FOR COLUMN
        // =====================================================

        TableColumn<PaymentModel, String>
                paymentColumn =
                new TableColumn<>("For");

        paymentColumn.setCellValueFactory(
                new PropertyValueFactory<>("paymentFor")
        );


        // =====================================================
        // AMOUNT COLUMN
        // =====================================================

        TableColumn<PaymentModel, String>
                amountColumn =
                new TableColumn<>("Amount");

        amountColumn.setCellValueFactory(
                new PropertyValueFactory<>("amount")
        );


        // =====================================================
        // STATUS COLUMN
        // =====================================================

        TableColumn<PaymentModel, String>
                statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );


        table.getColumns().addAll(
                dateColumn,
                tenantColumn,
                flatColumn,
                paymentColumn,
                amountColumn,
                statusColumn
        );


        // =====================================================
        // LOAD FIRESTORE PAYMENTS
        // =====================================================

        List<PaymentModel> payments =
                paymentController.getOwnerPayments(
                        ownerEmail
                );


        ObservableList<PaymentModel>
                paymentList =
                FXCollections.observableArrayList(
                        payments
                );


        table.setItems(
                paymentList
        );


        table.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#D5DDE0;"
        );


        mainContent.getChildren().add(
                table
        );


        // =====================================================
        // SUMMARY
        // =====================================================

        HBox summary =
                new HBox();

        summary.setSpacing(15);


        VBox received =
                createSummaryBox(
                        "Total Received",
                        calculateTotal(
                                payments,
                                "Received"
                        )
                );


        VBox pending =
                createSummaryBox(
                        "Pending",
                        calculateTotal(
                                payments,
                                "Pending"
                        )
                );


        VBox overdue =
                createSummaryBox(
                        "Overdue",
                        calculateTotal(
                                payments,
                                "Overdue"
                        )
                );


        VBox total =
                createSummaryBox(
                        "Total",
                        calculateAllTotal(
                                payments
                        )
                );


        summary.getChildren().addAll(
                received,
                pending,
                overdue,
                total
        );


        mainContent.getChildren().add(
                summary
        );


        // =====================================================
        // MAIN AREA
        // =====================================================

        BorderPane mainarea =
                new BorderPane();

        mainarea.setCenter(
                mainContent
        );


        root.setCenter(
                mainarea
        );


        // =====================================================
        // SCENE
        // =====================================================

        return new Scene(
                root,
                1500,
                750
        );
    }


    // =========================================================
    // CALCULATE STATUS TOTAL
    // =========================================================

    private static String calculateTotal(
            List<PaymentModel> payments,
            String requiredStatus) {

        double total = 0;


        for (PaymentModel payment :
                payments) {

            if (payment.getStatus() != null &&
                    payment.getStatus()
                            .equalsIgnoreCase(
                                    requiredStatus
                            )) {

                total += parseAmount(
                        payment.getAmount()
                );
            }
        }


        return formatAmount(total);
    }


    // =========================================================
    // CALCULATE ALL TOTAL
    // =========================================================

    private static String calculateAllTotal(
            List<PaymentModel> payments) {

        double total = 0;


        for (PaymentModel payment :
                payments) {

            total += parseAmount(
                    payment.getAmount()
            );
        }


        return formatAmount(total);
    }


    // =========================================================
    // PARSE AMOUNT
    // =========================================================

    private static double parseAmount(
            String amount) {

        if (amount == null ||
                amount.trim().isEmpty()) {

            return 0;
        }


        try {

            String cleanAmount =
                    amount
                            .replace("₹", "")
                            .replace(",", "")
                            .trim();


            return Double.parseDouble(
                    cleanAmount
            );

        } catch (NumberFormatException e) {

            return 0;
        }
    }


    // =========================================================
    // FORMAT AMOUNT
    // =========================================================

    private static String formatAmount(
            double amount) {

        return String.format(
                "₹%,.0f",
                amount
        );
    }


    // =========================================================
    // SUMMARY BOX
    // =========================================================

    private static VBox createSummaryBox(
            String title,
            String value) {

        VBox box =
                new VBox();

        box.setSpacing(5);

        box.setPadding(
                new Insets(15)
        );

        box.setPrefWidth(200);

        box.setStyle(
                "-fx-background-color:#F4F7F8;" +
                "-fx-background-radius:8;"
        );


        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#546E7A;"
        );


        Label valueLabel =
                new Label(value);

        valueLabel.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#102A43;"
        );


        box.getChildren().addAll(
                titleLabel,
                valueLabel
        );


        return box;
    }
}