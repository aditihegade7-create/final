package com.society.view.Owner_portal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.society.controller.Owner_Controller.PaymentController;
import com.society.controller.Owner_Controller.ResidentController;
import com.society.model.Owner_model.PaymentModel;
import com.society.model.Owner_model.ResidentModel;
import com.society.view.ScreenSize;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OwnerDashboard {

    // ============================================================
    // THEME
    // ============================================================

    private static final String BUTTON_COLOR = "#4E342E";
    private static final String BACKGROUND_COLOR = "#E8DDD5";
    private static final String CARD_COLOR = "#FFFFFF";

    private static final String NAVY_TEXT = "#102A43";
    private static final String SECONDARY_TEXT = "#667085";
    private static final String LIGHT_TEXT = "#98A2B3";
    private static final String BORDER_COLOR = "#D8CCC4";

    private static final String GREEN = "#6D9E63";
    private static final String GOLD = "#D5A84B";
    private static final String RED = "#B9574D";
    private static final String PURPLE = "#7667B5";

    // ============================================================
    // SCENE
    // ============================================================

    public static Scene createScene(Stage stage) {
        return createScene(stage, null);
    }

    public static Scene createScene(
            Stage stage,
            String ownerEmail) {

        BorderPane root = new BorderPane();

        // ========================================================
        // LOAD DASHBOARD DATA
        // ========================================================

        DashboardData data =
                loadDashboardData(ownerEmail);

        // ========================================================
        // SIDEBAR
        // ========================================================

        OwnerSidebar sidebar =
                new OwnerSidebar(stage, ownerEmail);

        root.setLeft(sidebar.getSidebar());

        // ========================================================
        // MAIN AREA
        // ========================================================

        VBox mainArea = new VBox();

        mainArea.setSpacing(0);

        mainArea.setStyle(
                "-fx-background-color: " +
                BACKGROUND_COLOR +
                ";"
        );

        // ========================================================
        // HEADER
        // ========================================================

        HBox header = createHeader();

        // ========================================================
        // CONTENT
        // ========================================================

        VBox content = new VBox();

        content.setSpacing(18);

        content.setPadding(
                new Insets(25, 35, 25, 35)
        );

        content.setAlignment(Pos.TOP_LEFT);

        // ========================================================
        // BACKGROUND IMAGE
        // ========================================================

        try {

            Image image = new Image(
                    OwnerDashboard.class
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

            content.setBackground(
                    new Background(backgroundImage)
            );

        } catch (Exception e) {

            System.out.println(
                    "Background image not found."
            );

            content.setStyle(
                    "-fx-background-color: " +
                    BACKGROUND_COLOR +
                    ";"
            );
        }

        // ========================================================
        // STAT CARDS
        // ========================================================

        HBox cards = new HBox();

        cards.setSpacing(15);

        VBox totalTenants =
                createStatCard(
                        "Total Tenants",
                        String.format(
                                Locale.US,
                                "%02d",
                                data.activeTenantCount
                        ),
                        "Active Tenants",
                        "♙",
                        "#F0EAFB",
                        PURPLE
                );

        VBox rentReceived =
                createStatCard(
                        "Rent Received",
                        formatAmount(
                                data.rentReceived
                        ),
                        "This Month",
                        "₹",
                        "#EDF5E9",
                        GREEN
                );

        VBox pendingAmount =
                createStatCard(
                        "Pending Amount",
                        formatAmount(
                                data.pendingAmount
                        ),
                        "From " +
                        data.pendingTenantCount +
                        " Tenants",
                        "▣",
                        "#FBF0E5",
                        "#C98545"
                );

        VBox upcomingDue =
                createStatCard(
                        "Upcoming Due",
                        String.format(
                                Locale.US,
                                "%02d",
                                data.upcomingDueCount
                        ),
                        "Due This Month",
                        "▣",
                        "#F8E9E7",
                        RED
                );

        cards.getChildren().addAll(
                totalTenants,
                rentReceived,
                pendingAmount,
                upcomingDue
        );

        HBox.setHgrow(
                totalTenants,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                rentReceived,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                pendingAmount,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                upcomingDue,
                Priority.ALWAYS
        );

        // ========================================================
        // INFORMATION SECTION
        // ========================================================

        HBox information = new HBox();

        information.setSpacing(20);

        VBox incomeOverview =
                createIncomeOverview(data);

        VBox activity =
                createRecentActivity(
                        data.recentPayments
                );

        HBox.setHgrow(
                incomeOverview,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                activity,
                Priority.ALWAYS
        );

        information.getChildren().addAll(
                incomeOverview,
                activity
        );

        // ========================================================
        // NOTICE
        // ========================================================

        HBox noticeBox =
                createNotice();

        // ========================================================
        // ADD CONTENT
        // ========================================================

        content.getChildren().addAll(
                cards,
                information,
                noticeBox
        );

        // ========================================================
        // MAIN AREA
        // ========================================================

        mainArea.getChildren().addAll(
                header,
                content
        );

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        root.setCenter(mainArea);

        // ========================================================
        // ANIMATIONS
        // ========================================================

        animateCard(
                totalTenants,
                0
        );

        animateCard(
                rentReceived,
                100
        );

        animateCard(
                pendingAmount,
                200
        );

        animateCard(
                upcomingDue,
                300
        );

        animateBox(
                incomeOverview,
                400
        );

        animateBox(
                activity,
                500
        );

        animateBox(
                noticeBox,
                600
        );

      //  animateHeader(header);

        // ========================================================
        // SCENE
        // ========================================================

        Scene scene =
                new Scene(
                        root,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        return scene;
    }

    // ============================================================
    // HEADER
    // ============================================================

    private static HBox createHeader() {

        HBox header = new HBox();

        header.setPrefHeight(80);
        header.setMinHeight(80);
        header.setMaxHeight(80);

        header.setPadding(
                new Insets(15, 25, 15, 25)
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setStyle(
                "-fx-background-color: " +
                BUTTON_COLOR +
                ";"
        );

        // --------------------------------------------------------
        // LEFT SIDE
        // --------------------------------------------------------

        VBox v1 = new VBox(4);

        Label greeting =
                new Label(
                        "Good Morning, Owner 👋"
                );

        greeting.setStyle(
                "-fx-font-family:'Bodoni MT';" +
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#ffffff;"
        );

        Label subtitle =
                new Label(
                        "This is your dashboard"
                );

        subtitle.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#ffffff;"
        );

        v1.getChildren().addAll(
                greeting,
                subtitle
        );

        // --------------------------------------------------------
        // SPACER
        // --------------------------------------------------------

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        // --------------------------------------------------------
        // DATE
        // --------------------------------------------------------

        LocalDate today =
                LocalDate.now();

        String day =
                today.format(
                        DateTimeFormatter.ofPattern(
                                "EEEE"
                        )
                );

        String date =
                today.format(
                        DateTimeFormatter.ofPattern(
                                "dd MMMM yyyy"
                        )
                );

        VBox dateBox = new VBox();

        dateBox.setSpacing(2);

        dateBox.setAlignment(
                Pos.CENTER_LEFT
        );

        dateBox.setPadding(
                new Insets(8, 15, 8, 15)
        );

        dateBox.setStyle(
                "-fx-background-color:rgba(255,255,255,0.10);" +
                "-fx-background-radius:10;" +
                "-fx-border-color:rgba(255,255,255,0.20);" +
                "-fx-border-radius:10;"
        );

        Label dayLabel =
                new Label(day);

        dayLabel.setStyle(
                "-fx-font-size:11px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        Label dateLabel =
                new Label(date);

        dateLabel.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#E9DED8;"
        );

        dateBox.getChildren().addAll(
                dayLabel,
                dateLabel
        );

        header.getChildren().addAll(
                v1,
                spacer,
                dateBox
        );

        return header;
    }

    // ============================================================
    // STAT CARD
    // ============================================================

    private static VBox createStatCard(
            String title,
            String value,
            String subtitle,
            String icon,
            String iconBackground,
            String iconColor) {

        VBox card = new VBox();

        card.setSpacing(4);

        card.setPadding(
                new Insets(14, 16, 14, 16)
        );

        card.setMinHeight(120);
        card.setPrefHeight(120);

        card.setStyle(
                "-fx-background-color:" +
                CARD_COLOR +
                ";" +
                "-fx-background-radius:13;" +
                "-fx-border-color:" +
                BORDER_COLOR +
                ";" +
                "-fx-border-radius:13;" +
                "-fx-effect:dropshadow(gaussian, rgba(80,50,40,0.08), 8, 0, 0, 2);"
        );

        HBox row = new HBox();

        row.setSpacing(12);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        // --------------------------------------------------------
        // ICON
        // --------------------------------------------------------

        StackPane iconBox =
                new StackPane();

        iconBox.setPrefSize(
                50,
                50
        );

        iconBox.setMinSize(
                50,
                50
        );

        iconBox.setMaxSize(
                50,
                50
        );

        iconBox.setStyle(
                "-fx-background-color:" +
                iconBackground +
                ";" +
                "-fx-background-radius:25;"
        );

        Label iconLabel =
                new Label(icon);

        iconLabel.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" +
                iconColor +
                ";"
        );

        iconBox.getChildren().add(
                iconLabel
        );

        // --------------------------------------------------------
        // TEXT
        // --------------------------------------------------------

        VBox text = new VBox();

        text.setSpacing(2);

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:" +
                SECONDARY_TEXT +
                ";"
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" +
                NAVY_TEXT +
                ";"
        );

        Label subtitleLabel =
                new Label(subtitle);

        subtitleLabel.setStyle(
                "-fx-font-size:10px;" +
                "-fx-text-fill:" +
                LIGHT_TEXT +
                ";"
        );

        text.getChildren().addAll(
                titleLabel,
                valueLabel,
                subtitleLabel
        );

        row.getChildren().addAll(
                iconBox,
                text
        );

        card.getChildren().add(row);

        // Hover animation
        card.setOnMouseEntered(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(150),
                            card
                    );

            scale.setToX(1.03);
            scale.setToY(1.03);

            scale.play();
        });

        card.setOnMouseExited(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(150),
                            card
                    );

            scale.setToX(1);
            scale.setToY(1);

            scale.play();
        });

        return card;
    }

    // ============================================================
    // INCOME OVERVIEW
    // ============================================================

    private static VBox createIncomeOverview(
            DashboardData data) {

        VBox box = new VBox();

        box.setSpacing(12);

        box.setPadding(
                new Insets(19)
        );

        box.setMinHeight(330);
        box.setPrefHeight(330);

        box.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:14;" +
                "-fx-border-color:" +
                BORDER_COLOR +
                ";" +
                "-fx-border-radius:14;" +
                "-fx-effect:dropshadow(gaussian, rgba(80,50,40,0.08), 8, 0, 0, 2);"
        );

        // --------------------------------------------------------
        // HEADING
        // --------------------------------------------------------

        Label heading =
                new Label(
                        "Income Overview"
                );

        heading.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" +
                NAVY_TEXT +
                ";"
        );

        // --------------------------------------------------------
        // TOTAL INCOME
        // --------------------------------------------------------

        double totalIncome =
                data.rentIncome +
                data.maintenanceIncome +
                data.otherIncome;

        double rentValue =
                data.rentIncome;

        double maintenanceValue =
                data.maintenanceIncome;

        double otherValue =
                data.otherIncome;

        if (totalIncome <= 0) {

            rentValue = 1;
            maintenanceValue = 0;
            otherValue = 0;
        }

        // --------------------------------------------------------
        // PIE CHART
        // --------------------------------------------------------

        PieChart.Data rent =
                new PieChart.Data(
                        "Rent",
                        rentValue
                );

        PieChart.Data maintenance =
                new PieChart.Data(
                        "Maintenance",
                        maintenanceValue
                );

        PieChart.Data other =
                new PieChart.Data(
                        "Other Charges",
                        otherValue
                );

        PieChart pieChart =
                new PieChart();

        pieChart.getData().addAll(
                rent,
                maintenance,
                other
        );

        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(false);
        pieChart.setAnimated(false);

        pieChart.setPrefSize(
                180,
                180
        );

        pieChart.setMinSize(
                180,
                180
        );

        pieChart.applyCss();

        if (rent.getNode() != null) {

            rent.getNode().setStyle(
                    "-fx-pie-color:" +
                    BUTTON_COLOR +
                    ";"
            );
        }

        if (maintenance.getNode() != null) {

            maintenance.getNode().setStyle(
                    "-fx-pie-color:" +
                    GREEN +
                    ";"
            );
        }

        if (other.getNode() != null) {

            other.getNode().setStyle(
                    "-fx-pie-color:" +
                    GOLD +
                    ";"
            );
        }

        // --------------------------------------------------------
        // DONUT CENTER
        // --------------------------------------------------------

        Circle centerCircle =
                new Circle(48);

        centerCircle.setStyle(
                "-fx-fill:white;"
        );

        Label totalAmount =
                new Label(
                        formatAmount(totalIncome)
                );

        totalAmount.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" +
                NAVY_TEXT +
                ";"
        );

        Label totalText =
                new Label(
                        "Total Income"
                );

        totalText.setStyle(
                "-fx-font-size:10px;" +
                "-fx-text-fill:" +
                SECONDARY_TEXT +
                ";"
        );

        VBox centerText =
                new VBox(
                        1,
                        totalAmount,
                        totalText
                );

        centerText.setAlignment(
                Pos.CENTER
        );

        StackPane chart =
                new StackPane(
                        pieChart,
                        centerCircle,
                        centerText
                );

        chart.setPrefSize(
                190,
                190
        );

        // --------------------------------------------------------
        // DETAILS
        // --------------------------------------------------------

        VBox details =
                new VBox();

        details.setSpacing(17);

        details.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox rentRow =
                createIncomeRow(
                        "Rent",
                        formatAmount(
                                data.rentIncome
                        ),
                        percentage(
                                data.rentIncome,
                                totalIncome
                        ),
                        BUTTON_COLOR
                );

        HBox maintenanceRow =
                createIncomeRow(
                        "Maintenance",
                        formatAmount(
                                data.maintenanceIncome
                        ),
                        percentage(
                                data.maintenanceIncome,
                                totalIncome
                        ),
                        GREEN
                );

        HBox otherRow =
                createIncomeRow(
                        "Other Charges",
                        formatAmount(
                                data.otherIncome
                        ),
                        percentage(
                                data.otherIncome,
                                totalIncome
                        ),
                        GOLD
                );

        details.getChildren().addAll(
                rentRow,
                maintenanceRow,
                otherRow
        );

        HBox content =
                new HBox();

        content.setSpacing(15);

        content.setAlignment(
                Pos.CENTER_LEFT
        );

        content.getChildren().addAll(
                chart,
                details
        );

        box.getChildren().addAll(
                heading,
                content
        );

        return box;
    }

    // ============================================================
    // INCOME ROW
    // ============================================================

    private static HBox createIncomeRow(
            String name,
            String amount,
            String percentage,
            String color) {

        Circle dot =
                new Circle(6);

        dot.setStyle(
                "-fx-fill:" +
                color +
                ";"
        );

        Label nameLabel =
                new Label(name);

        nameLabel.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#37474F;"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Label amountLabel =
                new Label(amount);

        amountLabel.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" +
                NAVY_TEXT +
                ";"
        );

        Label percentageLabel =
                new Label(percentage);

        percentageLabel.setStyle(
                "-fx-font-size:10px;" +
                "-fx-text-fill:#6B9D79;"
        );

        HBox row =
                new HBox();

        row.setSpacing(8);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPrefWidth(250);

        row.getChildren().addAll(
                dot,
                nameLabel,
                spacer,
                amountLabel,
                percentageLabel
        );

        return row;
    }

    // ============================================================
    // RECENT ACTIVITY
    // ============================================================

    private static VBox createRecentActivity(
            List<PaymentModel> payments) {

        VBox box = new VBox();

        box.setSpacing(10);

        box.setPadding(
                new Insets(19)
        );

        box.setMinHeight(330);
        box.setPrefHeight(330);

        box.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:14;" +
                "-fx-border-color:" +
                BORDER_COLOR +
                ";" +
                "-fx-border-radius:14;" +
                "-fx-effect:dropshadow(gaussian, rgba(80,50,40,0.08), 8, 0, 0, 2);"
        );

        // --------------------------------------------------------
        // HEADING
        // --------------------------------------------------------

        HBox heading =
                new HBox();

        heading.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label(
                        "Recent Activity"
                );

        title.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" +
                NAVY_TEXT +
                ";"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button viewAll =
                new Button(
                        "View All"
                );

        viewAll.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:" +
                BORDER_COLOR +
                ";" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:" +
                BUTTON_COLOR +
                ";" +
                "-fx-font-size:11px;" +
                "-fx-padding:7 12 7 12;" +
                "-fx-cursor:hand;"
        );

        addButtonAnimation(viewAll);

        heading.getChildren().addAll(
                title,
                spacer,
                viewAll
        );

        // --------------------------------------------------------
        // ACTIVITY LIST
        // --------------------------------------------------------

        VBox activityList =
                new VBox();

        activityList.setSpacing(0);

        int count = 0;

        for (PaymentModel payment :
                payments) {

            if (count >= 3) {
                break;
            }

            if (payment == null) {
                continue;
            }

            activityList.getChildren().add(
                    createActivityFromPayment(
                            payment
                    )
            );

            count++;

            if (count < 3) {

                activityList.getChildren().add(
                        createDivider()
                );
            }
        }

        if (count == 0) {

            Label empty =
                    new Label(
                            "No payment activity yet."
                    );

            empty.setStyle(
                    "-fx-font-size:12px;" +
                    "-fx-text-fill:" +
                    SECONDARY_TEXT +
                    ";"
            );

            VBox.setMargin(
                    empty,
                    new Insets(
                            20,
                            0,
                            0,
                            0
                    )
            );

            activityList.getChildren().add(
                    empty
            );
        }

        box.getChildren().addAll(
                heading,
                activityList
        );

        return box;
    }

    // ============================================================
    // ACTIVITY
    // ============================================================

    private static HBox createActivity(
            String icon,
            String title,
            String date,
            String amount,
            String iconBackground,
            String iconColor) {

        HBox row =
                new HBox();

        row.setSpacing(11);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPadding(
                new Insets(7, 0, 7, 0)
        );

        // --------------------------------------------------------
        // ICON
        // --------------------------------------------------------

        StackPane iconBox =
                new StackPane();

        iconBox.setPrefSize(
                44,
                44
        );

        iconBox.setMinSize(
                44,
                44
        );

        iconBox.setMaxSize(
                44,
                44
        );

        iconBox.setStyle(
                "-fx-background-color:" +
                iconBackground +
                ";" +
                "-fx-background-radius:11;"
        );

        Label iconLabel =
                new Label(icon);

        iconLabel.setStyle(
                "-fx-font-size:17px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" +
                iconColor +
                ";"
        );

        iconBox.getChildren().add(
                iconLabel
        );

        // --------------------------------------------------------
        // INFO
        // --------------------------------------------------------

        VBox info =
                new VBox();

        info.setSpacing(2);

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" +
                NAVY_TEXT +
                ";"
        );

        Label dateLabel =
                new Label(date);

        dateLabel.setStyle(
                "-fx-font-size:10px;" +
                "-fx-text-fill:" +
                SECONDARY_TEXT +
                ";"
        );

        info.getChildren().addAll(
                titleLabel,
                dateLabel
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Label amountLabel =
                new Label(amount);

        String amountColor;

        if (amount.startsWith("+")) {

            amountColor = GREEN;

        } else if (amount.startsWith("-")) {

            amountColor = NAVY_TEXT;

        } else {

            amountColor = RED;
        }

        amountLabel.setStyle(
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" +
                amountColor +
                ";"
        );

        row.getChildren().addAll(
                iconBox,
                info,
                spacer,
                amountLabel
        );

        return row;
    }

    // ============================================================
    // DIVIDER
    // ============================================================

    private static Region createDivider() {

        Region divider =
                new Region();

        divider.setPrefHeight(1);
        divider.setMinHeight(1);
        divider.setMaxHeight(1);

        divider.setStyle(
                "-fx-background-color:#EEE6E1;"
        );

        return divider;
    }

    // ============================================================
    // CREATE ACTIVITY FROM PAYMENT
    // ============================================================

    private static HBox createActivityFromPayment(
            PaymentModel payment) {

        String paymentFor =
                safeLower(
                        payment.getPaymentFor()
                );

        String status =
                safeLower(
                        payment.getStatus()
                );

        String title;

        String icon;

        String iconBackground;

        String iconColor;

        String amountPrefix = "";

        if (status.equals("received")) {

            if (paymentFor.contains(
                    "maintenance")) {

                title =
                        "Maintenance Received";

            } else if (
                    paymentFor.contains("rent")) {

                title =
                        "Payment Received";

            } else {

                title =
                        "Payment Received";
            }

            icon = "₹";

            iconBackground = "#EDF5E9";

            iconColor = GREEN;

            amountPrefix = "+ ";

        } else if (status.equals("pending")) {

            if (paymentFor.contains(
                    "maintenance")) {

                title =
                        "Maintenance Due";

            } else if (
                    paymentFor.contains("rent")) {

                title =
                        "Rent Due";

            } else {

                title =
                        "Payment Due";
            }

            icon = "▣";

            iconBackground = "#FBF0E5";

            iconColor = "#C98545";

        } else if (status.equals("overdue")) {

            title =
                    paymentFor.contains("rent")
                            ? "Rent Overdue"
                            : "Payment Overdue";

            icon = "!";

            iconBackground = "#F8E9E7";

            iconColor = RED;

        } else {

            title =
                    "Payment Activity";

            icon = "₹";

            iconBackground = "#F0EAFB";

            iconColor = PURPLE;
        }

        String amount =
                formatAmount(
                        parseAmount(
                                payment.getAmount()
                        )
                );

        if (!amountPrefix.isEmpty()) {

            amount =
                    amountPrefix +
                    amount;
        }

        return createActivity(
                icon,
                title,
                safeString(
                        payment.getDate()
                ),
                amount,
                iconBackground,
                iconColor
        );
    }

    // ============================================================
    // LOAD DASHBOARD DATA
    // ============================================================

    private static DashboardData loadDashboardData(
            String ownerEmail) {

        DashboardData data =
                new DashboardData();

        if (ownerEmail == null ||
                ownerEmail.trim().isEmpty()) {

            System.out.println(
                    "OwnerDashboard: owner email is missing."
            );

            return data;
        }

        String normalizedEmail =
                ownerEmail
                        .trim()
                        .toLowerCase();

        try {

            ResidentController residentController =
                    new ResidentController();

            PaymentController paymentController =
                    new PaymentController();

            List<ResidentModel> residents =
                    residentController
                            .getResidentsByOwnerEmail(
                                    normalizedEmail
                            );

            List<PaymentModel> payments =
                    paymentController
                            .getOwnerPayments(
                                    normalizedEmail
                            );

            LocalDate today =
                    LocalDate.now();

            // ====================================================
            // ACTIVE TENANTS
            // ====================================================

            for (ResidentModel resident :
                    residents) {

                if (resident == null) {
                    continue;
                }

                boolean isTenant =
                        "Tenant".equalsIgnoreCase(
                                safeString(
                                        resident.getResidentType()
                                )
                        );

                boolean isActive =
                        !"Inactive".equalsIgnoreCase(
                                safeString(
                                        resident.getStatus()
                                )
                        );

                if (isTenant && isActive) {

                    data.activeTenantCount++;
                }
            }

            // ====================================================
            // PAYMENTS
            // ====================================================

            for (PaymentModel payment :
                    payments) {

                if (payment == null) {
                    continue;
                }

                double amount =
                        parseAmount(
                                payment.getAmount()
                        );

                String status =
                        safeLower(
                                payment.getStatus()
                        );

                String paymentFor =
                        safeLower(
                                payment.getPaymentFor()
                        );

                LocalDate paymentDate =
                        parsePaymentDate(
                                payment.getDate()
                        );

                // ------------------------------------------------
                // CURRENT MONTH RECEIVED
                // ------------------------------------------------

                if (status.equals("received") &&
                        paymentDate != null &&
                        paymentDate.getYear()
                                == today.getYear() &&
                        paymentDate.getMonthValue()
                                == today.getMonthValue()) {

                    if (paymentFor.contains("rent")) {

                        data.rentReceived += amount;

                        data.rentIncome += amount;

                    } else if (
                            paymentFor.contains(
                                    "maintenance"
                            )) {

                        data.maintenanceIncome +=
                                amount;

                    } else {

                        data.otherIncome +=
                                amount;
                    }
                }

                // ------------------------------------------------
                // PENDING
                // ------------------------------------------------

                if (status.equals("pending")) {

                    data.pendingAmount += amount;

                    String tenantEmail =
                            safeString(
                                    payment.getTenantEmail()
                            );

                    if (!tenantEmail.isEmpty()) {

                        data.pendingTenantEmails.add(
                                tenantEmail
                                        .toLowerCase()
                        );
                    }

                    // --------------------------------------------
                    // UPCOMING DUE
                    // --------------------------------------------

                    if (paymentDate != null &&
                            paymentDate.getYear()
                                    == today.getYear() &&
                            paymentDate.getMonthValue()
                                    == today.getMonthValue() &&
                            !paymentDate.isBefore(
                                    today
                            )) {

                        data.upcomingDueCount++;
                    }
                }
            }

            data.pendingTenantCount =
                    data.pendingTenantEmails.size();

            // ====================================================
            // RECENT ACTIVITY
            // ====================================================

            List<PaymentModel> sortedPayments =
                    new ArrayList<>(
                            payments
                    );

            sortedPayments.sort(
                    Comparator.comparing(
                            (PaymentModel p) ->
                                    parsePaymentDate(
                                            p == null
                                                    ? null
                                                    : p.getDate()
                                    ),
                            Comparator.nullsLast(
                                    Comparator.reverseOrder()
                            )
                    )
            );

            data.recentPayments =
                    sortedPayments;

            // ====================================================
            // DEBUG
            // ====================================================

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Owner dashboard data loaded."
            );

            System.out.println(
                    "Owner Email: " +
                    normalizedEmail
            );

            System.out.println(
                    "Active Tenants: " +
                    data.activeTenantCount
            );

            System.out.println(
                    "Rent Received This Month: " +
                    formatAmount(
                            data.rentReceived
                    )
            );

            System.out.println(
                    "Pending Amount: " +
                    formatAmount(
                            data.pendingAmount
                    )
            );

            System.out.println(
                    "Upcoming Due: " +
                    data.upcomingDueCount
            );

            System.out.println(
                    "===================================="
            );

        } catch (Exception e) {

            System.out.println(
                    "OwnerDashboard data loading error: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }

        return data;
    }

    // ============================================================
    // PARSE PAYMENT DATE
    // ============================================================

    private static LocalDate parsePaymentDate(
            String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return null;
        }

        String date =
                value.trim();

        DateTimeFormatter[] dateTimeFormats = {

                DateTimeFormatter.ofPattern(
                        "d MMMM yyyy, h:mm a",
                        Locale.ENGLISH
                ),

                DateTimeFormatter.ofPattern(
                        "dd MMMM yyyy, hh:mm a",
                        Locale.ENGLISH
                ),

                DateTimeFormatter.ofPattern(
                        "d MMM yyyy, h:mm a",
                        Locale.ENGLISH
                ),

                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy, hh:mm a",
                        Locale.ENGLISH
                )
        };

        for (DateTimeFormatter formatter :
                dateTimeFormats) {

            try {

                return LocalDateTime
                        .parse(
                                date,
                                formatter
                        )
                        .toLocalDate();

            } catch (DateTimeParseException ignored) {
            }
        }

        DateTimeFormatter[] dateFormats = {

                DateTimeFormatter.ofPattern(
                        "d MMMM yyyy",
                        Locale.ENGLISH
                ),

                DateTimeFormatter.ofPattern(
                        "dd MMMM yyyy",
                        Locale.ENGLISH
                ),

                DateTimeFormatter.ofPattern(
                        "d MMM yyyy",
                        Locale.ENGLISH
                ),

                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy",
                        Locale.ENGLISH
                ),

                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                ),

                DateTimeFormatter.ofPattern(
                        "d/M/yyyy"
                ),

                DateTimeFormatter.ofPattern(
                        "dd-MM-yyyy"
                ),

                DateTimeFormatter.ofPattern(
                        "d-M-yyyy"
                ),

                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd"
                )
        };

        for (DateTimeFormatter formatter :
                dateFormats) {

            try {

                return LocalDate.parse(
                        date,
                        formatter
                );

            } catch (DateTimeParseException ignored) {
            }
        }

        return null;
    }

    // ============================================================
    // PARSE AMOUNT
    // ============================================================

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
                            .replace("+", "")
                            .trim();

            return Double.parseDouble(
                    cleanAmount
            );

        } catch (NumberFormatException e) {

            System.out.println(
                    "Unable to parse amount: " +
                    amount
            );

            return 0;
        }
    }

    // ============================================================
    // FORMAT AMOUNT
    // ============================================================

    private static String formatAmount(
            double amount) {

        return String.format(
                Locale.US,
                "₹%,.0f",
                amount
        );
    }

    // ============================================================
    // PERCENTAGE
    // ============================================================

    private static String percentage(
            double value,
            double total) {

        if (total <= 0) {

            return "0.0%";
        }

        return String.format(
                Locale.US,
                "%.1f%%",
                (value / total) * 100
        );
    }

    // ============================================================
    // SAFE STRING
    // ============================================================

    private static String safeString(
            String value) {

        return value == null
                ? ""
                : value;
    }

    // ============================================================
    // SAFE LOWERCASE
    // ============================================================

    private static String safeLower(
            String value) {

        return safeString(value)
                .trim()
                .toLowerCase();
    }

    // ============================================================
    // DASHBOARD DATA
    // ============================================================

    private static class DashboardData {

        int activeTenantCount;

        double rentReceived;

        double pendingAmount;

        int pendingTenantCount;

        int upcomingDueCount;

        double rentIncome;

        double maintenanceIncome;

        double otherIncome;

        List<String> pendingTenantEmails =
                new ArrayList<>();

        List<PaymentModel> recentPayments =
                new ArrayList<>();
    }

    // ============================================================
    // NOTICE
    // ============================================================

    private static HBox createNotice() {

        HBox notice =
                new HBox();

        notice.setSpacing(14);

        notice.setAlignment(
                Pos.CENTER_LEFT
        );

        notice.setPadding(
                new Insets(
                        13,
                        18,
                        13,
                        18
                )
        );

        notice.setMinHeight(78);
        notice.setPrefHeight(78);

        notice.setStyle(
                "-fx-background-color:#F7F1ED;" +
                "-fx-background-radius:13;" +
                "-fx-border-color:#DCCFC7;" +
                "-fx-border-radius:13;"
        );

        // --------------------------------------------------------
        // ICON
        // --------------------------------------------------------

        StackPane iconBox =
                new StackPane();

        iconBox.setPrefSize(
                45,
                45
        );

        iconBox.setMinSize(
                45,
                45
        );

        iconBox.setMaxSize(
                45,
                45
        );

        iconBox.setStyle(
                "-fx-background-color:" +
                BUTTON_COLOR +
                ";" +
                "-fx-background-radius:23;"
        );

        Label icon =
                new Label("!");

        icon.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        iconBox.getChildren().add(
                icon
        );

        // --------------------------------------------------------
        // INFORMATION
        // --------------------------------------------------------

        VBox info =
                new VBox();

        info.setSpacing(2);

        Label heading =
                new Label("Notice");

        heading.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" +
                NAVY_TEXT +
                ";"
        );

        Label text =
                new Label(
                        "Water tank cleaning on 15 May 2025 " +
                        "from 9:00 AM to 1:00 PM."
                );

        text.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#5F6368;"
        );

        info.getChildren().addAll(
                heading,
                text
        );

        // --------------------------------------------------------
        // SPACER
        // --------------------------------------------------------

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        // --------------------------------------------------------
        // BUTTON
        // --------------------------------------------------------

        Button viewNotices =
                new Button(
                        "View All Notices   ›"
                );

        viewNotices.setPrefWidth(
                155
        );

        viewNotices.setPrefHeight(
                36
        );

        viewNotices.setStyle(
                "-fx-background-color:" +
                BUTTON_COLOR +
                ";" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:11px;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

        addButtonAnimation(viewNotices);

        notice.getChildren().addAll(
                iconBox,
                info,
                spacer,
                viewNotices
        );

        return notice;
    }

    // ============================================================
    // CARD ANIMATION
    // ============================================================

    private static void animateCard(
            VBox card,
            int delay) {

        card.setOpacity(0);

        card.setTranslateY(30);

        card.setScaleX(0.95);

        card.setScaleY(0.95);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(500),
                        card
                );

        fade.setFromValue(0);

        fade.setToValue(1);

        fade.setDelay(
                Duration.millis(delay)
        );

        TranslateTransition slide =
                new TranslateTransition(
                        Duration.millis(500),
                        card
                );

        slide.setFromY(30);

        slide.setToY(0);

        slide.setDelay(
                Duration.millis(delay)
        );

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(350),
                        card
                );

        scale.setFromX(0.95);

        scale.setFromY(0.95);

        scale.setToX(1);

        scale.setToY(1);

        scale.setDelay(
                Duration.millis(delay)
        );

        fade.play();

        slide.play();

        scale.play();
    }

    // ============================================================
    // BOX ANIMATION
    // ============================================================

    private static void animateBox(
        Node box,
        int delay) {

    box.setOpacity(0);
    box.setTranslateY(25);

    FadeTransition fade =
            new FadeTransition(
                    Duration.millis(600),
                    box
            );

    fade.setFromValue(0);
    fade.setToValue(1);
    fade.setDelay(
            Duration.millis(delay)
    );

    TranslateTransition slide =
            new TranslateTransition(
                    Duration.millis(600),
                    box
            );

    slide.setFromY(25);
    slide.setToY(0);
    slide.setDelay(
            Duration.millis(delay)
    );

    fade.play();
    slide.play();
}

    // ============================================================
    // HEADER ANIMATION
    // ============================================================

   
    // ============================================================
    // BUTTON ANIMATION
    // ============================================================

    private static void addButtonAnimation(
            Button button) {

        button.setOnMouseEntered(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            scale.setToX(1.06);

            scale.setToY(1.06);

            scale.play();
        });

        button.setOnMouseExited(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            scale.setToX(1);

            scale.setToY(1);

            scale.play();
        });

        button.setOnMousePressed(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(80),
                            button
                    );

            scale.setToX(0.96);

            scale.setToY(0.96);

            scale.play();
        });

        button.setOnMouseReleased(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(80),
                            button
                    );

            scale.setToX(1.06);

            scale.setToY(1.06);

            scale.play();
        });
    }
}