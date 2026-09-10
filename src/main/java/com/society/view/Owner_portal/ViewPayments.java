package com.society.view.Owner_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.controller.Owner_Controller.PaymentController;
import com.society.model.Owner_model.PaymentModel;
import com.society.view.ScreenSize;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ViewPayments {

    private static final PaymentController paymentController =
            new PaymentController();

    // =========================================================
    // CREATE SCENE
    // =========================================================

    public static Scene createScene(
            Stage stage,
            String ownerEmail) {

        BorderPane root =
                new BorderPane();

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
        new Insets(0, 0, 25, 0)
);

mainContent.setSpacing(18);

mainContent.setAlignment(
        Pos.TOP_LEFT
);

mainContent.setStyle(
        "-fx-background-color: #e8ddd5;"
);
        // =====================================================
        // BACKGROUND IMAGE
        // =====================================================

        Image image =
                new Image(
                        ViewPayments.class
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
                new Background(
                        backgroundImage
                )
        );

        // =====================================================
        // HEADER
        // =====================================================

       

// =====================================================
// HEADER
// =====================================================

HBox header =
        new HBox();

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
                "Payments"
        );

greeting.setStyle(
        "-fx-font-family:'Bodoni MT';" +
        "-fx-font-size:24px;" +
        "-fx-font-weight:bold;" +
        "-fx-text-fill:white;"
);

Label description =
        new Label(
                "View all rental and maintenance payments"
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
        // PAYMENT TABLE
        // =====================================================

        TableView<PaymentModel> table =
                new TableView<>();

        table.setPrefHeight(500);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        // =====================================================
        // DATE
        // =====================================================

        TableColumn<PaymentModel, String>
                dateColumn =
                new TableColumn<>("Date");

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("date")
        );

        // =====================================================
        // TENANT
        // =====================================================

        TableColumn<PaymentModel, String>
                tenantColumn =
                new TableColumn<>("Tenant");

        tenantColumn.setCellValueFactory(
                new PropertyValueFactory<>("tenant")
        );

        // =====================================================
        // FLAT
        // =====================================================

        TableColumn<PaymentModel, String>
                flatColumn =
                new TableColumn<>("Flat / Unit");

        flatColumn.setCellValueFactory(
                new PropertyValueFactory<>("flat")
        );

        // =====================================================
        // PAYMENT FOR
        // =====================================================

        TableColumn<PaymentModel, String>
                paymentColumn =
                new TableColumn<>("For");

        paymentColumn.setCellValueFactory(
                new PropertyValueFactory<>("paymentFor")
        );

        // =====================================================
        // AMOUNT
        // =====================================================

        TableColumn<PaymentModel, String>
                amountColumn =
                new TableColumn<>("Amount");

        amountColumn.setCellValueFactory(
                new PropertyValueFactory<>("amount")
        );

        // =====================================================
        // STATUS
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

        List<PaymentModel> payments;

        try {

            payments =
                    paymentController.getOwnerPayments(
                            ownerEmail
                    );

        } catch (Exception e) {

            e.printStackTrace();

            payments =
                    FXCollections.observableArrayList();
        }

        ObservableList<PaymentModel>
                paymentList =
                FXCollections.observableArrayList(
                        payments
                );

        table.setItems(
                paymentList
        );

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
        // EMPTY TABLE MESSAGE
        // =====================================================

        if (payments.isEmpty()) {

            table.setPlaceholder(
                    new Label(
                            "No payment records found."
                    )
            );
        }

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

        HBox.setHgrow(
                received,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                pending,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                overdue,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                total,
                Priority.ALWAYS
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
        // ANIMATIONS
        // =====================================================

//   animateHeader(header);
        animateTable(table);
        animateSummary(summary);

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
    // CALCULATE STATUS TOTAL
    // =========================================================

    private static String calculateTotal(
            List<PaymentModel> payments,
            String requiredStatus) {

        double total = 0;

        for (PaymentModel payment : payments) {

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

        for (PaymentModel payment : payments) {

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
                "-fx-background-radius:8;" +
                "-fx-border-color:#D5DDE0;" +
                "-fx-border-radius:8;"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#4e342e;"
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

    // =========================================================
    // HEADER ANIMATION
    // =========================================================

  

    // =========================================================
    // TABLE ANIMATION
    // =========================================================

    private static void animateTable(
            TableView<PaymentModel> table) {

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
                Duration.millis(200)
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
                Duration.millis(200)
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

    // =========================================================
    // SUMMARY ANIMATION
    // =========================================================

    private static void animateSummary(
            HBox summary) {

        summary.setOpacity(0);
        summary.setTranslateY(20);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(600),
                        summary
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        fade.setDelay(
                Duration.millis(500)
        );

        TranslateTransition translate =
                new TranslateTransition(
                        Duration.millis(600),
                        summary
                );

        translate.setFromY(20);
        translate.setToY(0);

        translate.setDelay(
                Duration.millis(500)
        );

        new ParallelTransition(
                fade,
                translate
        ).play();

        // =====================================================
        // SUMMARY HOVER
        // =====================================================

        for (javafx.scene.Node node :
                summary.getChildren()) {

            node.setOnMouseEntered(e -> {

                ScaleTransition scale =
                        new ScaleTransition(
                                Duration.millis(150),
                                node
                        );

                scale.setToX(1.05);
                scale.setToY(1.05);

                scale.play();
            });

            node.setOnMouseExited(e -> {

                ScaleTransition scale =
                        new ScaleTransition(
                                Duration.millis(150),
                                node
                        );

                scale.setToX(1.0);
                scale.setToY(1.0);

                scale.play();
            });
        }
    }
}