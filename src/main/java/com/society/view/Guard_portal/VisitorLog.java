package com.society.view.Guard_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.society.controller.Guard_Controller.VisitorController;
import com.society.model.Guard_model.VisitorModel;
import com.society.view.ScreenSize;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class VisitorLog {

    // =====================================================
    // SHARED VISITOR LIST
    // =====================================================

    public static ObservableList<Visitor> visitors =
            FXCollections.observableArrayList();

    // =====================================================
    // CREATE SCENE
    // =====================================================

    // Existing entry point kept for compatibility with other portal code.
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

        mainContent.setStyle(
                "-fx-background-color: #e8ddd5;"
        );

        // =====================================================
        // HEADER
        // =====================================================

        HBox header = new HBox();

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
                new javafx.scene.control.Tooltip("Refresh visitors")
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

        // Give the QR image enough vertical space.
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
        // QR PASS - DISPLAY QR IMAGE FROM qrToken
        // =====================================================
        //
        // Firestore stores qrToken.
        // VisitorDao maps qrToken -> qrPass.
        // The QR image is generated from that token.
        //
        // No new Firestore field is required.
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

        // =====================================================
        // ADMIT EXPECTED VISITOR
        // =====================================================

        admitVisitorButton.setOnAction(e -> {

            Visitor selectedVisitor =
                    visitorTable
                            .getSelectionModel()
                            .getSelectedItem();

            // No visitor selected
            if (selectedVisitor == null) {

                showMessage(
                        "No Visitor Selected",
                        "Please select an expected visitor first."
                );

                return;
            }

            // Only APPROVED visitors can be admitted
            if (!"INVITED".equals(
                    selectedVisitor.getStatus())) {

                showMessage(
                        "Cannot Admit Visitor",
                        "Only APPROVED visitors can be admitted."
                );

                return;
            }

            // Check Firestore document ID
            if (selectedVisitor.getVisitorId() == null
                    || selectedVisitor.getVisitorId().isEmpty()) {

                showMessage(
                        "Error",
                        "Visitor ID is missing."
                );

                return;
            }

            // Update Firestore using the actual logged-in guard email
            boolean updated =
                    visitorController.admitVisitor(
                            selectedVisitor.getVisitorId(),
                            guardEmail
                    );

            if (updated) {

                /*
                 * Reload from Firestore after admission.
                 *
                 * This is important because Firestore creates the actual
                 * entryTime. Reloading ensures the table displays that
                 * exact value instead of leaving Entry Time blank.
                 */
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
                    selectedVisitor.getStatus()
            )) {

                showMessage(
                        "Already Checked Out",
                        "This visitor has already checked out."
                );

                return;
            }

            // A visitor must be inside before being checked out.
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

            /*
             * IMPORTANT:
             *
             * The previous code incorrectly called admitVisitor()
             * here. That changed a visitor back to Inside.
             *
             * Use checkOutVisitor() so Firestore receives:
             *
             * status    = Checked Out
             * exitTime  = current time
             */
            boolean updated =
                    visitorController.checkOutVisitor(
                            selectedVisitor.getVisitorId()
                    );

            if (updated) {

                /*
                 * Reload from Firestore so the newly generated
                 * exit/entry values and status are displayed.
                 */
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

            // Empty search = show everything
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

            // Summary remains based on all visitors.
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
        // SCENE
        // =====================================================

        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
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

        // Keep this too if other existing code uses it.
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