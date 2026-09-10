package com.society.view.Guard_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.controller.Guard_Controller.VisitorController;
import com.society.model.Guard_model.VisitorModel;
import com.society.view.ScreenSize;
import com.society.view.Owner_portal.OwnerDashboard;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VisitorLog {

    // =====================================================
    // SHARED VISITOR LIST
    // =====================================================

    public static ObservableList<Visitor> visitors =
            FXCollections.observableArrayList();

    // =====================================================
    // CREATE SCENE
    // =====================================================

    public static Scene createScene(Stage stage) {
        return createScene(stage, null, null);
    }

    public static Scene createScene(Stage stage, String guardEmail) {
        return createScene(stage, guardEmail, null);
    }

    public static Scene createScene(
            Stage stage,
            String guardEmail,
            String society) {

        BorderPane root = new BorderPane();

         Image image = new Image(
                    OwnerDashboard.class
                            .getResource(
                                    "/background-Dashboard5.jpeg"
                            )
                            .toExternalForm()
            );

                BackgroundImage bgImage = new BackgroundImage(
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
                                                true));

                root.setBackground(
                                new Background(bgImage));

        // =====================================================
        // SIDEBAR
        // =====================================================

        GuardSidebar sidebar =
                new GuardSidebar(
                        stage,
                        "Visitor Log",
                        guardEmail,
                        society
                );

        root.setLeft(sidebar.getSidebar());

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainContent = new VBox();

        mainContent.setPadding(
                new Insets(25, 40, 25, 40)
        );

        mainContent.setSpacing(18);

       

        // =====================================================
        // HEADER
        // =====================================================

        HBox header = new HBox();
         header.setPrefHeight(80);
        header.setMinHeight(80);
        header.setMaxHeight(80);

        header.setPadding(
                new Insets(25, 35, 25, 35)
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setStyle(
                "-fx-background-color: #4e342e;"
        );

        VBox titleBox = new VBox(3);

        Label title =
                new Label("Visitor Log");

        title.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-family:'Bodoni MT';" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        Label description =
                new Label(
                        "Monitor visitor entries and exits at the society gate."
                );

        description.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:white;"
        );

        titleBox.getChildren().addAll(
                title,
                description
        );

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Label day = new Label();
        Label date = new Label();

        LocalDate today =
                LocalDate.now();

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

        day.setTextFill(Color.WHITE);
        date.setTextFill(Color.WHITE);

        VBox dateBox = new VBox(3);

        dateBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        dateBox.getChildren().addAll(
                day,
                date
        );

        header.getChildren().addAll(
                titleBox,
                spacer,
                dateBox
        );

        // =====================================================
        // SUMMARY CARDS
        // =====================================================

        GridPane summaryGrid =
                new GridPane();

        summaryGrid.setHgap(20);

        summaryGrid.setAlignment(
                Pos.CENTER_LEFT
        );

        Label totalValue = new Label();
        Label insideValue = new Label();
        Label checkedOutValue = new Label();
        Label deliveryValue = new Label();

        VBox totalCard =
                createSummaryCard(
                        "Total Visitors",
                        totalValue
                );

        VBox insideCard =
                createSummaryCard(
                        "Currently Inside",
                        insideValue
                );

        VBox checkedOutCard =
                createSummaryCard(
                        "Checked Out",
                        checkedOutValue
                );

        VBox deliveryCard =
                createSummaryCard(
                        "Deliveries",
                        deliveryValue
                );

        summaryGrid.add(
                totalCard,
                0,
                0
        );

        summaryGrid.add(
                insideCard,
                1,
                0
        );

        summaryGrid.add(
                checkedOutCard,
                2,
                0
        );

        summaryGrid.add(
                deliveryCard,
                3,
                0
        );

        // =====================================================
        // SEARCH
        // =====================================================

        Label searchTitle =
                new Label("Search Visitors");

        searchTitle.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#070707;"
        );

        TextField searchField =
                new TextField();

        searchField.setPromptText(
                "Name, phone number or flat number..."
        );

        searchField.setPrefWidth(430);

        searchField.setPrefHeight(38);

        searchField.setPadding(
                new Insets(0, 42, 0, 12)
        );

        searchField.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:6;" +
                "-fx-text-fill:#080808;" +
                "-fx-prompt-text-fill:#7A7A7A;"
        );

        Button searchButton =
                new Button("Search");

        searchButton.setPrefWidth(100);

        searchButton.setPrefHeight(38);

        searchButton.setStyle(
                "-fx-background-color:#4e342e;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:6;"
        );

        Button refreshButton =
                new Button("\u21BB");

        refreshButton.setPrefWidth(34);

        refreshButton.setPrefHeight(30);

        refreshButton.setTooltip(
                new javafx.scene.control.Tooltip(
                        "Refresh visitors"
                )
        );

        refreshButton.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#4e342e;" +
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:6;" +
                "-fx-padding:0;" +
                "-fx-cursor:hand;"
        );

        refreshButton.setOnMouseEntered(e ->
                refreshButton.setStyle(
                        "-fx-background-color:#F1ECE8;" +
                        "-fx-text-fill:#4e342e;" +
                        "-fx-font-size:20px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:6;" +
                        "-fx-padding:0;" +
                        "-fx-cursor:hand;"
                )
        );

        refreshButton.setOnMouseExited(e ->
                refreshButton.setStyle(
                        "-fx-background-color:transparent;" +
                        "-fx-text-fill:#4e342e;" +
                        "-fx-font-size:20px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:6;" +
                        "-fx-padding:0;" +
                        "-fx-cursor:hand;"
                )
        );

        StackPane searchFieldContainer =
                new StackPane(searchField);

        StackPane.setAlignment(
                refreshButton,
                Pos.CENTER_RIGHT
        );

        searchFieldContainer.getChildren().add(
                refreshButton
        );

        HBox searchBox =
                new HBox(
                        10,
                        searchFieldContainer,
                        searchButton
                );

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox searchSection =
                new VBox(
                        8,
                        searchTitle,
                        searchBox
                );

        // =====================================================
        // VISITOR TABLE
        // =====================================================

        TableView<Visitor> visitorTable =
                new TableView<>();

        visitorTable.setPrefHeight(330);

        visitorTable.setFixedCellSize(70);

        visitorTable.setStyle(
                "-fx-background-color:#E8F0E8;" +
                "-fx-background-radius:8;"
        );

        TableColumn<Visitor, String> nameColumn =
                new TableColumn<>("Visitor Name");

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        nameColumn.setPrefWidth(190);

        TableColumn<Visitor, String> phoneColumn =
                new TableColumn<>("Phone");

        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("phone")
        );

        phoneColumn.setPrefWidth(140);

        TableColumn<Visitor, String> flatColumn =
                new TableColumn<>("Flat");

        flatColumn.setCellValueFactory(
                new PropertyValueFactory<>("flat")
        );

        flatColumn.setPrefWidth(100);

        TableColumn<Visitor, String> purposeColumn =
                new TableColumn<>("Purpose");

        purposeColumn.setCellValueFactory(
                new PropertyValueFactory<>("purpose")
        );

        purposeColumn.setPrefWidth(170);

        TableColumn<Visitor, String> entryColumn =
                new TableColumn<>("Entry Time");

        entryColumn.setCellValueFactory(
                new PropertyValueFactory<>("entryTime")
        );

        entryColumn.setPrefWidth(120);

        TableColumn<Visitor, String> qrPassColumn =
                new TableColumn<>("QR Pass");

        qrPassColumn.setCellValueFactory(
                new PropertyValueFactory<>("qrPass")
        );

        qrPassColumn.setPrefWidth(120);

        // =====================================================
        // QR PASS - DISPLAY QR IMAGE
        // =====================================================

        qrPassColumn.setCellFactory(column ->
                new TableCell<Visitor, String>() {

                    private final ImageView qrImageView =
                            new ImageView();

                    {
                        qrImageView.setFitWidth(58);
                        qrImageView.setFitHeight(58);
                        qrImageView.setPreserveRatio(true);
                        qrImageView.setSmooth(true);
                    }

                    @Override
                    protected void updateItem(
                            String qrToken,
                            boolean empty) {

                        super.updateItem(
                                qrToken,
                                empty
                        );

                        if (empty
                                || qrToken == null
                                || qrToken.trim().isEmpty()) {

                            setGraphic(null);
                            setText(null);

                            return;
                        }

                        try {

                            String encodedToken =
                                    java.net.URLEncoder.encode(
                                            qrToken.trim(),
                                            java.nio.charset.StandardCharsets.UTF_8
                                    );

                            String qrUrl =
                                    "https://api.qrserver.com/v1/create-qr-code/"
                                    + "?size=100x100"
                                    + "&data="
                                    + encodedToken;

                            Image qrImage =
                                    new Image(
                                            qrUrl,
                                            58,
                                            58,
                                            true,
                                            true,
                                            true
                                    );

                            qrImageView.setImage(
                                    qrImage
                            );

                            setGraphic(
                                    qrImageView
                            );

                            setText(null);

                        } catch (Exception ex) {

                            setGraphic(null);
                            setText("QR");
                        }
                    }
                }
        );

        TableColumn<Visitor, String> statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );

        statusColumn.setPrefWidth(130);

        nameColumn.setStyle(
                "-fx-alignment:CENTER;"
        );

        phoneColumn.setStyle(
                "-fx-alignment:CENTER;"
        );

        flatColumn.setStyle(
                "-fx-alignment:CENTER;"
        );

        purposeColumn.setStyle(
                "-fx-alignment:CENTER;"
        );

        entryColumn.setStyle(
                "-fx-alignment:CENTER;"
        );

        qrPassColumn.setStyle(
                "-fx-alignment:CENTER;"
        );

        statusColumn.setStyle(
                "-fx-alignment:CENTER;"
        );

        visitorTable.getColumns().addAll(
                nameColumn,
                phoneColumn,
                flatColumn,
                purposeColumn,
                entryColumn,
                qrPassColumn,
                statusColumn
        );

        // =====================================================
        // FIRESTORE CONTROLLER
        // =====================================================

        VisitorController visitorController =
                new VisitorController();

        // =====================================================
        // INITIAL FIRESTORE LOAD
        // =====================================================

        loadVisitorsFromFirestore(
                visitorController
        );

        visitorTable.setItems(
                visitors
        );

        updateSummary(
                visitors,
                totalValue,
                insideValue,
                checkedOutValue,
                deliveryValue
        );

        // =====================================================
        // ADD VISITOR
        // =====================================================

        Button addVisitorButton =
                new Button("Add Visitor");

        addVisitorButton.setPrefWidth(140);

        addVisitorButton.setPrefHeight(40);

        addVisitorButton.setStyle(
                "-fx-background-color:#4e342e;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:6;"
        );

        addVisitorButton.setOnAction(e -> {

            stage.setScene(
                    ManualVisitorEntry.createScene(
                            stage,
                            guardEmail
                    )
            );

        });

        // =====================================================
        // ADMIT VISITOR
        // =====================================================

        Button admitVisitorButton =
                new Button("Admit Visitor");

        admitVisitorButton.setPrefWidth(140);

        admitVisitorButton.setPrefHeight(40);

        admitVisitorButton.setStyle(
                "-fx-background-color:#4e342e;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:6;"
        );

        admitVisitorButton.setOnAction(e -> {

            Visitor selectedVisitor =
                    visitorTable
                            .getSelectionModel()
                            .getSelectedItem();

            if (selectedVisitor == null) {

                showMessage(
                        "No Visitor Selected",
                        "Please select an expected visitor first."
                );

                return;
            }

            if (!"INVITED".equals(
                    selectedVisitor.getStatus())) {

                showMessage(
                        "Cannot Admit Visitor",
                        "Only APPROVED visitors can be admitted."
                );

                return;
            }

            if (selectedVisitor.getVisitorId() == null
                    || selectedVisitor.getVisitorId().isEmpty()) {

                showMessage(
                        "Error",
                        "Visitor ID is missing."
                );

                return;
            }

            boolean updated =
                    visitorController.admitVisitor(
                            selectedVisitor.getVisitorId(),
                            guardEmail
                    );

            if (updated) {

                loadVisitorsFromFirestore(
                        visitorController
                );

                visitorTable.setItems(
                        visitors
                );

                updateSummary(
                        visitors,
                        totalValue,
                        insideValue,
                        checkedOutValue,
                        deliveryValue
                );

                showMessage(
                        "Visitor Admitted",
                        selectedVisitor.getName()
                                + " is now inside."
                );

            } else {

                showMessage(
                        "Admission Failed",
                        "Unable to update visitor in Firestore."
                );
            }

        });

        // =====================================================
        // CHECK OUT
        // =====================================================

        Button checkOutButton =
                new Button("Check Out");

        checkOutButton.setPrefWidth(130);

        checkOutButton.setPrefHeight(40);

        checkOutButton.setStyle(
                "-fx-background-color:#E8F0E8;" +
                "-fx-text-fill:#060606;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:6;"
        );

        checkOutButton.setOnAction(e -> {

            Visitor selectedVisitor =
                    visitorTable
                            .getSelectionModel()
                            .getSelectedItem();

            if (selectedVisitor == null) {

                showMessage(
                        "No Visitor Selected",
                        "Please select a visitor first."
                );

                return;
            }

            if ("Checked Out".equals(
                    selectedVisitor.getStatus())) {

                showMessage(
                        "Already Checked Out",
                        "This visitor has already checked out."
                );

                return;
            }

            String currentStatus =
                    selectedVisitor.getStatus();

            if (currentStatus == null ||
                    !"Inside".equalsIgnoreCase(
                            currentStatus.trim()
                    )) {

                showMessage(
                        "Cannot Check Out",
                        "Only visitors who are currently inside can be checked out."
                );

                return;
            }

            if (selectedVisitor.getVisitorId() == null
                    || selectedVisitor.getVisitorId().isEmpty()) {

                showMessage(
                        "Error",
                        "Visitor ID is missing. Cannot update Firestore."
                );

                return;
            }

            boolean updated =
                    visitorController.checkOutVisitor(
                            selectedVisitor.getVisitorId()
                    );

            if (updated) {

                loadVisitorsFromFirestore(
                        visitorController
                );

                visitorTable.setItems(
                        visitors
                );

                updateSummary(
                        visitors,
                        totalValue,
                        insideValue,
                        checkedOutValue,
                        deliveryValue
                );

                showMessage(
                        "Visitor Checked Out",
                        selectedVisitor.getName()
                                + " has been checked out."
                );

            } else {

                showMessage(
                        "Checkout Failed",
                        "Unable to update visitor in Firestore."
                );
            }

        });

        // =====================================================
        // ACTION BUTTONS
        // =====================================================

        HBox actionButtons =
                new HBox(
                        12,
                        addVisitorButton,
                        admitVisitorButton,
                        checkOutButton
                );

        actionButtons.setAlignment(
                Pos.CENTER_RIGHT
        );

        // =====================================================
        // SEARCH BUTTON
        // =====================================================

        searchButton.setOnAction(e -> {

            String search =
                    searchField.getText()
                            .trim()
                            .toLowerCase();

            if (search.isEmpty()) {

                visitorTable.setItems(
                        visitors
                );

                updateSummary(
                        visitors,
                        totalValue,
                        insideValue,
                        checkedOutValue,
                        deliveryValue
                );

                return;
            }

            ObservableList<Visitor> filtered =
                    FXCollections.observableArrayList();

            for (Visitor visitor : visitors) {

                String name =
                        visitor.getName() == null
                                ? ""
                                : visitor.getName()
                                        .toLowerCase();

                String phone =
                        visitor.getPhone() == null
                                ? ""
                                : visitor.getPhone()
                                        .toLowerCase();

                String flat =
                        visitor.getFlat() == null
                                ? ""
                                : visitor.getFlat()
                                        .toLowerCase();

                String purpose =
                        visitor.getPurpose() == null
                                ? ""
                                : visitor.getPurpose()
                                        .toLowerCase();

                if (name.contains(search)
                        || phone.contains(search)
                        || flat.contains(search)
                        || purpose.contains(search)) {

                    filtered.add(visitor);
                }
            }

            visitorTable.setItems(
                    filtered
            );

            updateSummary(
                    visitors,
                    totalValue,
                    insideValue,
                    checkedOutValue,
                    deliveryValue
            );

        });

        // =====================================================
        // REFRESH BUTTON
        // =====================================================

        refreshButton.setOnAction(e -> {

            searchField.clear();

            loadVisitorsFromFirestore(
                    visitorController
            );

            visitorTable.setItems(
                    visitors
            );

            updateSummary(
                    visitors,
                    totalValue,
                    insideValue,
                    checkedOutValue,
                    deliveryValue
            );

        });

        // =====================================================
        // ADD CONTENT
        // =====================================================

        mainContent.getChildren().addAll(
                summaryGrid,
                searchSection,
                visitorTable,
                actionButtons
        );

        // =====================================================
        // MAIN AREA
        // =====================================================

        BorderPane mainarea =
                new BorderPane();

        mainarea.setTop(
                header
        );

        mainarea.setCenter(
                mainContent
        );

        root.setCenter(
                mainarea
        );

        // =====================================================
        // BUTTON POPUP ANIMATION
        // =====================================================

        addButtonPopupAnimation(
                searchButton
        );

        addButtonPopupAnimation(
                refreshButton
        );

        addButtonPopupAnimation(
                addVisitorButton
        );

        addButtonPopupAnimation(
                admitVisitorButton
        );

        addButtonPopupAnimation(
                checkOutButton
        );

        // =====================================================
        // SUMMARY CARD POPUP ANIMATION
        // =====================================================

        addCardPopupAnimation(
                totalCard,
                0
        );

        addCardPopupAnimation(
                insideCard,
                80
        );

        addCardPopupAnimation(
                checkedOutCard,
                160
        );

        addCardPopupAnimation(
                deliveryCard,
                240
        );

        // =====================================================
        // HEADER FIXED
        // =====================================================

        header.setOpacity(1);
        header.setTranslateY(0);

        // =====================================================
        // PAGE DOWN -> UP ANIMATION
        // HEADER NOT INCLUDED
        // =====================================================

        playPageAnimation(
                mainContent
        );

        // =====================================================
        // SCENE
        // =====================================================

        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }

    // =====================================================
    // PAGE DOWN -> UP ANIMATION
    // =====================================================

    private static void playPageAnimation(
            VBox mainContent) {

        mainContent.setOpacity(0);

        mainContent.setTranslateY(45);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(700),
                        mainContent
                );

        fade.setFromValue(0);

        fade.setToValue(1);

        TranslateTransition slide =
                new TranslateTransition(
                        Duration.millis(700),
                        mainContent
                );

        slide.setFromY(45);

        slide.setToY(0);

        fade.play();

        slide.play();
    }

    // =====================================================
    // BUTTON POPUP ANIMATION
    // =====================================================

    private static void addButtonPopupAnimation(
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
    }

    // =====================================================
    // CARD POPUP ANIMATION
    // =====================================================

    private static void addCardPopupAnimation(
            VBox card,
            int delay) {

        card.setOpacity(0);

        card.setScaleX(0.85);

        card.setScaleY(0.85);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(450),
                        card
                );

        fade.setFromValue(0);

        fade.setToValue(1);

        fade.setDelay(
                Duration.millis(delay)
        );

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(450),
                        card
                );

        scale.setFromX(0.85);

        scale.setFromY(0.85);

        scale.setToX(1);

        scale.setToY(1);

        scale.setDelay(
                Duration.millis(delay)
        );

        fade.play();

        scale.play();
    }

    // =====================================================
    // LOAD VISITORS FROM FIRESTORE
    // =====================================================

    private static void loadVisitorsFromFirestore(
            VisitorController visitorController) {

        List<VisitorModel> firestoreVisitors =
                visitorController.getAllVisitors();

        visitors.clear();

        for (VisitorModel visitor :
                firestoreVisitors) {

            Visitor uiVisitor =
                    new Visitor(
                            visitor.getName(),
                            visitor.getPhone(),
                            visitor.getFlat(),
                            visitor.getPurpose(),
                            visitor.getRemarks(),
                            visitor.getEntryTime(),
                            visitor.getQrPass(),
                            visitor.getStatus()
                    );

            uiVisitor.setVisitorId(
                    visitor.getVisitorId()
            );

            visitors.add(
                    uiVisitor
            );
        }
    }

    // =====================================================
    // SUMMARY CARD
    // =====================================================

    private static VBox createSummaryCard(
            String title,
            Label value) {

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#080808;"
        );

        value.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#080808;"
        );

        VBox card =
                new VBox(
                        8,
                        titleLabel,
                        value
                );

        card.setPrefWidth(250);

        card.setPrefHeight(80);

        card.setPadding(
                new Insets(15)
        );

        card.setStyle(
                "-fx-background-color:#E8F0E8;" +
                "-fx-background-radius:10;"
        );

        return card;
    }

    // =====================================================
    // UPDATE SUMMARY
    // =====================================================

    private static void updateSummary(
            ObservableList<Visitor> visitors,
            Label total,
            Label inside,
            Label checkedOut,
            Label deliveries) {

        int totalCount =
                visitors.size();

        int insideCount = 0;

        int checkedOutCount = 0;

        int deliveryCount = 0;

        for (Visitor visitor :
                visitors) {

            if ("Inside".equals(
                    visitor.getStatus())) {

                insideCount++;
            }

            if ("Checked Out".equals(
                    visitor.getStatus())) {

                checkedOutCount++;
            }

            if ("Delivery".equals(
                    visitor.getPurpose())) {

                deliveryCount++;
            }
        }

        total.setText(
                String.valueOf(
                        totalCount
                )
        );

        inside.setText(
                String.valueOf(
                        insideCount
                )
        );

        checkedOut.setText(
                String.valueOf(
                        checkedOutCount
                )
        );

        deliveries.setText(
                String.valueOf(
                        deliveryCount
                )
        );
    }

    // =====================================================
    // VISITOR MODEL FOR TABLE
    // =====================================================

    public static class Visitor {

        private String visitorId;

        private String name;

        private String phone;

        private String flat;

        private String purpose;

        private String remarks;

        private String entryTime;

        private String qrPass;

        private String status;

        // =================================================
        // 8-ARGUMENT CONSTRUCTOR
        // =================================================

        public Visitor(
                String name,
                String phone,
                String flat,
                String purpose,
                String remarks,
                String entryTime,
                String qrPass,
                String status) {

            this.name = name;

            this.phone = phone;

            this.flat = flat;

            this.purpose = purpose;

            this.remarks = remarks;

            this.entryTime = entryTime;

            this.qrPass = qrPass;

            this.status = status;
        }

        // =================================================
        // 7-ARGUMENT CONSTRUCTOR
        // =================================================

        public Visitor(
                String name,
                String phone,
                String flat,
                String purpose,
                String remarks,
                String entryTime,
                String status) {

            this.name = name;

            this.phone = phone;

            this.flat = flat;

            this.purpose = purpose;

            this.remarks = remarks;

            this.entryTime = entryTime;

            this.status = status;
        }

        // =================================================
        // GETTERS
        // =================================================

        public String getVisitorId() {

            return visitorId;
        }

        public String getName() {

            return name;
        }

        public String getPhone() {

            return phone;
        }

        public String getFlat() {

            return flat;
        }

        public String getPurpose() {

            return purpose;
        }

        public String getRemarks() {

            return remarks;
        }

        public String getEntryTime() {

            return entryTime;
        }

        public String getQrPass() {

            return qrPass;
        }

        public String getQRPass() {

            return qrPass;
        }

        public String getStatus() {

            return status;
        }

        // =================================================
        // SETTERS
        // =================================================

        public void setVisitorId(
                String visitorId) {

            this.visitorId =
                    visitorId;
        }

        public void setQrPass(
                String qrPass) {

            this.qrPass =
                    qrPass;
        }

        public void setStatus(
                String status) {

            this.status =
                    status;
        }
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

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}