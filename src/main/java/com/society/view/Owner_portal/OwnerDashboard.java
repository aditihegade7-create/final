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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class OwnerDashboard {

    // ============================================================
    // SOCIETY360 THEME
    // ============================================================

    private static final String BUTTON_COLOR = "#4E342E";
    private static final String BACKGROUND_COLOR = "#E8DDD5";
    private static final String SIDEBAR_COLOR = "#593A32";

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

        HBox header =
                createHeader();

        // ========================================================
        // CONTENT
        // ========================================================

        VBox content =
                new VBox();

        content.setSpacing(18);

        content.setPadding(
                new Insets(20, 28, 20, 28)
        );

        content.setStyle(
                "-fx-background-color: " +
                BACKGROUND_COLOR +
                ";"
        );

        // ========================================================
        // STAT CARDS
        // ========================================================

        HBox cards =
                new HBox();

        cards.setSpacing(16);

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

        HBox information =
                new HBox();

        information.setSpacing(18);

        VBox incomeOverview =
                createIncomeOverview(data);

        VBox recentActivity =
                createRecentActivity(
                        data.recentPayments
                );

        HBox.setHgrow(
                incomeOverview,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                recentActivity,
                Priority.ALWAYS
        );

        information.getChildren().addAll(
                incomeOverview,
                recentActivity
        );

        // ========================================================
        // NOTICE
        // ========================================================

        /*
         * Notice remains unchanged for now because no NoticeDao /
         * NoticeModel was supplied with the current Owner Portal
         * files. We will connect this in the Notice step.
         */
        HBox notice =
                createNotice();

        // ========================================================
        // CONTENT
        // ========================================================

        content.getChildren().addAll(
                cards,
                information,
                notice
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
        // SCENE
        // ========================================================

        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }

    // ============================================================
    // HEADER
    // ============================================================

    private static HBox createHeader() {

        HBox header = new HBox();

        header.setPrefHeight(100);
        header.setMinHeight(100);
        header.setMaxHeight(100);

        header.setPadding(
                new Insets(20, 28, 20, 28)
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        /*
         * Main Society360 header color
         */
        header.setStyle(
                "-fx-background-color: " +
                BUTTON_COLOR +
                ";"
        );

        // ========================================================
        // LEFT SIDE
        // ========================================================

        VBox greetingBox = new VBox();

        greetingBox.setSpacing(4);

        Label greeting =
                new Label(
                        "Good Morning, Owner 👋"
                );

        greeting.setStyle(
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );

        Label subtitle =
                new Label(
                        "Here's what's happening in your society today."
                );

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #E9DED8;"
        );

        greetingBox.getChildren().addAll(
                greeting,
                subtitle
        );

        // ========================================================
        // SPACER
        // ========================================================

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        // ========================================================
        // NOTIFICATION
        // ========================================================

        StackPane notification =
                createNotification();

        // ========================================================
        // DATE
        // ========================================================

        VBox dateBox =
                createHeaderDate();

        header.getChildren().addAll(
                greetingBox,
                spacer,
                notification,
                dateBox
        );

        return header;
    }

    // ============================================================
    // HEADER NOTIFICATION
    // ============================================================

    private static StackPane createNotification() {

        StackPane box =
                new StackPane();

        box.setPrefSize(46, 46);
        box.setMinSize(46, 46);
        box.setMaxSize(46, 46);

        box.setStyle(
                "-fx-background-color: rgba(255,255,255,0.12);" +
                "-fx-background-radius: 23;" +
                "-fx-border-color: rgba(255,255,255,0.25);" +
                "-fx-border-radius: 23;"
        );

        Label bell =
                new Label("♧");

        bell.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-text-fill: white;"
        );

        Label count =
                new Label("3");

        count.setPrefSize(18, 18);
        count.setMinSize(18, 18);
        count.setMaxSize(18, 18);

        count.setAlignment(
                Pos.CENTER
        );

        count.setStyle(
                "-fx-background-color: #D85A50;" +
                "-fx-background-radius: 9;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 9px;" +
                "-fx-font-weight: bold;"
        );

        StackPane.setAlignment(
                count,
                Pos.TOP_RIGHT
        );

        StackPane.setMargin(
                count,
                new Insets(-2, -2, 0, 0)
        );

        box.getChildren().addAll(
                bell,
                count
        );

        HBox.setMargin(
                box,
                new Insets(0, 14, 0, 0)
        );

        return box;
    }

    // ============================================================
    // HEADER DATE
    // ============================================================

    private static VBox createHeaderDate() {

        LocalDate today =
                LocalDate.now();

        String day =
                today.format(
                        DateTimeFormatter.ofPattern("EEEE")
                );

        String date =
                today.format(
                        DateTimeFormatter.ofPattern(
                                "dd MMMM yyyy"
                        )
                );

        VBox box =
                new VBox();

        box.setSpacing(2);

        box.setAlignment(
                Pos.CENTER_LEFT
        );

        box.setPadding(
                new Insets(8, 15, 8, 15)
        );

        box.setPrefWidth(175);
        box.setMinWidth(175);
        box.setMaxWidth(175);

        box.setPrefHeight(58);

        box.setStyle(
                "-fx-background-color: rgba(255,255,255,0.10);" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(255,255,255,0.20);" +
                "-fx-border-radius: 10;"
        );

        Label dayLabel =
                new Label(day);

        dayLabel.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );

        Label dateLabel =
                new Label(date);

        dateLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #E9DED8;"
        );

        box.getChildren().addAll(
                dayLabel,
                dateLabel
        );

        return box;
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

        VBox card =
                new VBox();

        card.setSpacing(4);

        card.setPadding(
                new Insets(14, 16, 14, 16)
        );

        card.setMinHeight(120);
        card.setPrefHeight(120);

        card.setStyle(
                "-fx-background-color: " +
                CARD_COLOR +
                ";" +
                "-fx-background-radius: 13;" +
                "-fx-border-color: " +
                BORDER_COLOR +
                ";" +
                "-fx-border-radius: 13;" +
                "-fx-effect: dropshadow(gaussian, rgba(80,50,40,0.08), 8, 0, 0, 2);"
        );

        HBox row =
                new HBox();

        row.setSpacing(12);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        // --------------------------------------------------------
        // ICON
        // --------------------------------------------------------

        StackPane iconBox =
                new StackPane();

        iconBox.setPrefSize(50, 50);
        iconBox.setMinSize(50, 50);
        iconBox.setMaxSize(50, 50);

        iconBox.setStyle(
                "-fx-background-color: " +
                iconBackground +
                ";" +
                "-fx-background-radius: 25;"
        );

        Label iconLabel =
                new Label(icon);

        iconLabel.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
                iconColor +
                ";"
        );

        iconBox.getChildren().add(
                iconLabel
        );

        // --------------------------------------------------------
        // TEXT
        // --------------------------------------------------------

        VBox text =
                new VBox();

        text.setSpacing(2);

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: " +
                SECONDARY_TEXT +
                ";"
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
                NAVY_TEXT +
                ";"
        );

        Label subtitleLabel =
                new Label(subtitle);

        subtitleLabel.setStyle(
                "-fx-font-size: 10px;" +
                "-fx-text-fill: " +
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

        card.getChildren().add(
                row
        );

        return card;
    }

    // ============================================================
    // INCOME OVERVIEW
    // ============================================================

    private static VBox createIncomeOverview(
            DashboardData data) {

        VBox box =
                new VBox();

        box.setSpacing(12);

        box.setPadding(
                new Insets(19)
        );

        box.setMinHeight(330);
        box.setPrefHeight(330);

        box.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: " +
                BORDER_COLOR +
                ";" +
                "-fx-border-radius: 14;" +
                "-fx-effect: dropshadow(gaussian, rgba(80,50,40,0.08), 8, 0, 0, 2);"
        );

        // --------------------------------------------------------
        // TITLE
        // --------------------------------------------------------

        HBox heading =
                new HBox();

        heading.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label(
                        "Income Overview"
                );

        title.setStyle(
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
                NAVY_TEXT +
                ";"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button filter =
                new Button(
                        "This Month  ˅"
                );

        filter.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: " +
                BORDER_COLOR +
                ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: " +
                BUTTON_COLOR +
                ";" +
                "-fx-font-size: 11px;" +
                "-fx-padding: 7 12 7 12;" +
                "-fx-cursor: hand;"
        );

        heading.getChildren().addAll(
                title,
                spacer,
                filter
        );

        // --------------------------------------------------------
        // PIE CHART
        // --------------------------------------------------------

        double totalIncome =
                data.rentIncome +
                data.maintenanceIncome +
                data.otherIncome;

        /*
         * PieChart does not behave well with three zero values.
         * Give it one tiny invisible-ish slice only when there
         * is no income, while the center still shows ₹0.
         */
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

        PieChart chart =
                new PieChart();

        chart.getData().addAll(
                rent,
                maintenance,
                other
        );

        chart.setLabelsVisible(false);
        chart.setLegendVisible(false);
        chart.setAnimated(false);

        chart.setPrefSize(
                190,
                190
        );

        chart.setMinSize(
                190,
                190
        );

        chart.applyCss();

        if (rent.getNode() != null) {
            rent.getNode().setStyle(
                    "-fx-pie-color: " +
                    BUTTON_COLOR +
                    ";"
            );
        }

        if (maintenance.getNode() != null) {
            maintenance.getNode().setStyle(
                    "-fx-pie-color: " +
                    GREEN +
                    ";"
            );
        }

        if (other.getNode() != null) {
            other.getNode().setStyle(
                    "-fx-pie-color: " +
                    GOLD +
                    ";"
            );
        }

        // --------------------------------------------------------
        // DONUT CENTER
        // --------------------------------------------------------

        Circle center =
                new Circle(50);

        center.setStyle(
                "-fx-fill: white;"
        );

        Label total =
                new Label(
                        formatAmount(totalIncome)
                );

        total.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
                NAVY_TEXT +
                ";"
        );

        Label totalText =
                new Label(
                        "Total Income"
                );

        totalText.setStyle(
                "-fx-font-size: 10px;" +
                "-fx-text-fill: " +
                SECONDARY_TEXT +
                ";"
        );

        VBox centerContent =
                new VBox(
                        2,
                        total,
                        totalText
                );

        centerContent.setAlignment(
                Pos.CENTER
        );

        StackPane chartContainer =
                new StackPane(
                        chart,
                        center,
                        centerContent
                );

        chartContainer.setPrefSize(
                200,
                200
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

        content.setSpacing(20);

        content.setAlignment(
                Pos.CENTER_LEFT
        );

        content.getChildren().addAll(
                chartContainer,
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

        HBox row =
                new HBox();

        row.setSpacing(7);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        Circle dot =
                new Circle(5.5);

        dot.setStyle(
                "-fx-fill: " +
                color +
                ";"
        );

        Label nameLabel =
                new Label(name);

        nameLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #344054;"
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
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
                NAVY_TEXT +
                ";"
        );

        Label percentageLabel =
                new Label(percentage);

        percentageLabel.setStyle(
                "-fx-font-size: 10px;" +
                "-fx-text-fill: #6B9D79;"
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

        VBox box =
                new VBox();

        box.setSpacing(10);

        box.setPadding(
                new Insets(19)
        );

        box.setMinHeight(330);
        box.setPrefHeight(330);

        box.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: " +
                BORDER_COLOR +
                ";" +
                "-fx-border-radius: 14;" +
                "-fx-effect: dropshadow(gaussian, rgba(80,50,40,0.08), 8, 0, 0, 2);"
        );

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
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
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
                "-fx-background-color: white;" +
                "-fx-border-color: " +
                BORDER_COLOR +
                ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: " +
                BUTTON_COLOR +
                ";" +
                "-fx-font-size: 11px;" +
                "-fx-padding: 7 12 7 12;" +
                "-fx-cursor: hand;"
        );

        heading.getChildren().addAll(
                title,
                spacer,
                viewAll
        );

        VBox activityList =
                new VBox();

        activityList.setSpacing(0);

        int count = 0;

        for (PaymentModel payment : payments) {

            if (count >= 3) {
                break;
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
                    "-fx-font-size: 11px;" +
                    "-fx-text-fill: " +
                    SECONDARY_TEXT +
                    ";"
            );

            VBox.setMargin(
                    empty,
                    new Insets(20, 0, 0, 0)
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
                "-fx-background-color: " +
                iconBackground +
                ";" +
                "-fx-background-radius: 11;"
        );

        Label iconLabel =
                new Label(icon);

        iconLabel.setStyle(
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
                iconColor +
                ";"
        );

        iconBox.getChildren().add(
                iconLabel
        );

        // --------------------------------------------------------
        // INFORMATION
        // --------------------------------------------------------

        VBox info =
                new VBox();

        info.setSpacing(2);

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
                NAVY_TEXT +
                ";"
        );

        Label dateLabel =
                new Label(date);

        dateLabel.setStyle(
                "-fx-font-size: 10px;" +
                "-fx-text-fill: " +
                SECONDARY_TEXT +
                ";"
        );

        info.getChildren().addAll(
                titleLabel,
                dateLabel
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
        // AMOUNT
        // --------------------------------------------------------

        Label amountLabel =
                new Label(amount);

        String amountColor;

        if (amount.startsWith("+")) {
            amountColor = GREEN;
        }
        else if (amount.startsWith("-")) {
            amountColor = NAVY_TEXT;
        }
        else {
            amountColor = RED;
        }

        amountLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
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
                "-fx-background-color: #EEE6E1;"
        );

        return divider;
    }

    // ============================================================
    // CREATE ACTIVITY FROM FIRESTORE PAYMENT
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

            if (paymentFor.contains("maintenance")) {
                title = "Maintenance Received";
                icon = "₹";
                iconBackground = "#EDF5E9";
                iconColor = GREEN;
            }
            else if (paymentFor.contains("rent")) {
                title = "Payment Received";
                icon = "₹";
                iconBackground = "#EDF5E9";
                iconColor = GREEN;
            }
            else {
                title = "Payment Received";
                icon = "₹";
                iconBackground = "#EDF5E9";
                iconColor = GREEN;
            }

            amountPrefix = "+ ";

        } else if (status.equals("pending")) {

            if (paymentFor.contains("maintenance")) {
                title = "Maintenance Due";
            }
            else if (paymentFor.contains("rent")) {
                title = "Rent Due";
            }
            else {
                title = "Payment Due";
            }

            icon = "▣";
            iconBackground = "#FBF0E5";
            iconColor = "#C98545";

        } else if (status.equals("overdue")) {

            title = paymentFor.contains("rent")
                    ? "Rent Overdue"
                    : "Payment Overdue";

            icon = "!";
            iconBackground = "#F8E9E7";
            iconColor = RED;

        } else {

            title = "Payment Activity";
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
            amount = amountPrefix + amount;
        }

        return createActivity(
                icon,
                title,
                safeString(payment.getDate()),
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
                ownerEmail.trim().toLowerCase();

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

            // ----------------------------------------------------
            // ACTIVE TENANTS
            // ----------------------------------------------------

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

            // ----------------------------------------------------
            // PAYMENT CALCULATIONS
            // ----------------------------------------------------

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
                // CURRENT MONTH RECEIVED INCOME
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
                // PENDING AMOUNT
                // ------------------------------------------------

                if (status.equals("pending")) {

                    data.pendingAmount += amount;

                    String tenantEmail =
                            safeString(
                                    payment.getTenantEmail()
                            );

                    if (!tenantEmail.isEmpty()) {

                        data.pendingTenantEmails
                                .add(
                                        tenantEmail
                                                .toLowerCase()
                                );
                    }

                    // --------------------------------------------
                    // UPCOMING DUE
                    // --------------------------------------------

                    if (paymentDate != null &&
                            paymentDate
                                    .getYear()
                                    == today.getYear() &&
                            paymentDate
                                    .getMonthValue()
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

            // ----------------------------------------------------
            // RECENT ACTIVITY
            // ----------------------------------------------------

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
    // LOWERCASE STRING
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
                new Insets(13, 18, 13, 18)
        );

        notice.setMinHeight(78);
        notice.setPrefHeight(78);

        notice.setStyle(
                "-fx-background-color: #F7F1ED;" +
                "-fx-background-radius: 13;" +
                "-fx-border-color: #DCCFC7;" +
                "-fx-border-radius: 13;"
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
                "-fx-background-color: " +
                BUTTON_COLOR +
                ";" +
                "-fx-background-radius: 23;"
        );

        Label icon =
                new Label("!");

        icon.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
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
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
                NAVY_TEXT +
                ";"
        );

        Label text =
                new Label(
                        "Water tank cleaning on 15 May 2025 " +
                        "from 9:00 AM to 1:00 PM."
                );

        text.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #5F6368;"
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
                "-fx-background-color: " +
                BUTTON_COLOR +
                ";" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );

        notice.getChildren().addAll(
                iconBox,
                info,
                spacer,
                viewNotices
        );

        return notice;
    }
}