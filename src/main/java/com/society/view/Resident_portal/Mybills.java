
package com.society.view.Resident_portal;

import com.society.service.resident_service.RazorpayService;
import com.society.dao.Resident_dao.ElectricityDAO;
import com.society.view.ScreenSize;
import com.society.config.FirebaseConfig;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class Mybills {

    // =========================================================
    // LOGGED-IN USER EMAIL
    // =========================================================
    /*
     * IMPORTANT:
     *
     * This email MUST be the email of the currently logged-in
     * resident.
     *
     * No hard-coded resident email is used here.
     */
    private final String loggedInEmail;

    public Mybills(String loggedInEmail) {

        if (loggedInEmail == null) {
            this.loggedInEmail = "";
        } else {
            this.loggedInEmail = loggedInEmail.trim();
        }
    }

    // =========================================================
    // DAO
    // =========================================================
    private final ElectricityDAO electricityDAO =
            new ElectricityDAO();

    private final RazorpayService razorpayService =
            new RazorpayService();

    // =========================================================
    // GET BILL SCENE
    // =========================================================
    public Scene getBillScene(Stage stage) {

        // =====================================================
        // SIDEBAR
        // =====================================================
        panel panelobj =
                new panel(stage, loggedInEmail);

        BorderPane root =
                new BorderPane();


                Image backgroundImage = new Image(
        getClass()
                .getResource("/background-Dashboard5.jpeg")
                .toExternalForm()
        );

        BackgroundSize backgroundSize = new BackgroundSize(
                100,
                100,
                true,
                true,
                false,
                true
        );

        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize
        );

        root.setBackground(
                new Background(bgImage)
        );
                

        root.setLeft(
                panelobj.getSidebar()
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================
        VBox mainContent =
                new VBox(20);

        mainContent.setPadding(
                new Insets(30, 40, 30, 40)
        );

        mainContent.setStyle(
                "-fx-background-color: transparent;"

        );

        // =====================================================
        // TITLE
        // =====================================================
        Label title =
                new Label("My Bills");

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        28
                )
        );

        title.setTextFill(Color.WHITE);

        Label subtitle =
                new Label(
                        "View your society bills and payment status"
                );

        subtitle.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        subtitle.setTextFill(Color.WHITE);

        VBox heading =new VBox(3);

heading.setPadding(new Insets(15, 20, 15, 20));

        heading.getChildren().addAll(
                title,
                subtitle
        );

        heading.setStyle(
                "-fx-background-color: #4e342e;"
        );

        // =====================================================
        // SUMMARY CARDS
        // =====================================================
        HBox summaryCards =
                new HBox(20);

        VBox totalDue =
                createSummaryCard(
                        "Total Bills",
                        "₹ 0",
                        "Pending amount"
                );

        VBox maintenanceDue =
                createSummaryCard(
                        "Maintenance Bills",
                        "₹ 0",
                        "Loading..."
                );

        VBox electricityDue =
                createSummaryCard(
                        "Electricity Bill",
                        "₹ 0",
                        "Loading..."
                );

        summaryCards.getChildren().addAll(
                totalDue,
                maintenanceDue,
                electricityDue
        );

        // =====================================================
        // BILL TITLE
        // =====================================================
        Label billTitle =
                new Label("Bill History");

        billTitle.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        19
                )
        );

        // billTitle.setTextFill(Color.web(" #4E342E"));
        billTitle.setTextFill(Color.web("#4E342E"));

        // =====================================================
        // REFRESH BUTTON
        // =====================================================
        Button refreshButton =
                new Button("↻");

        refreshButton.setPrefWidth(45);
        refreshButton.setPrefHeight(35);

        refreshButton.setStyle(
                "-fx-background-color: #4e342e;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 20;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6;"
        );

        // =====================================================
        // ADD ELECTRICITY BUTTON
        // =====================================================
        Button addElectricityButton =
                new Button("＋ Add Electricity Bill");

        addElectricityButton.setPrefHeight(35);

        addElectricityButton.setStyle(
                "-fx-background-color: #4e342e;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6;"
        );

        // =====================================================
        // BILL TITLE BOX
        // =====================================================
        HBox billTitleBox =
                new HBox();

        billTitleBox.setAlignment(
                Pos.CENTER_LEFT
        );

        billTitleBox.setSpacing(15);

        billTitleBox.getChildren().addAll(
                billTitle,
                refreshButton,
                addElectricityButton
        );

        // =====================================================
        // TABLE
        // =====================================================
        TableView<Bill> table =
                new TableView<>();

        table.setPrefHeight(300);

        table.setStyle(
                "-fx-background-color: white;"
        );

        // =====================================================
        // BILL TYPE COLUMN
        // =====================================================
        TableColumn<Bill, String> typeColumn =
                new TableColumn<>("Bill Type");

        typeColumn.setCellValueFactory(
                data ->
                        data.getValue()
                                .typeProperty()
        );

        // =====================================================
        // MONTH COLUMN
        // =====================================================
        TableColumn<Bill, String> monthColumn =
                new TableColumn<>("Month");

        monthColumn.setCellValueFactory(
                data ->
                        data.getValue()
                                .monthProperty()
        );

        // =====================================================
        // AMOUNT COLUMN
        // =====================================================
        TableColumn<Bill, String> amountColumn =
                new TableColumn<>("Amount");

        amountColumn.setCellValueFactory(
                data ->
                        data.getValue()
                                .amountProperty()
        );

        // =====================================================
        // DUE DATE COLUMN
        // =====================================================
        TableColumn<Bill, String> dueDateColumn =
                new TableColumn<>("Due Date");

        dueDateColumn.setCellValueFactory(
                data ->
                        data.getValue()
                                .dueDateProperty()
        );

        // =====================================================
        // STATUS COLUMN
        // =====================================================
        TableColumn<Bill, String> statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(
                data ->
                        data.getValue()
                                .statusProperty()
        );

        // =====================================================
        // ADD COLUMNS
        // =====================================================
        table.getColumns().addAll(
                typeColumn,
                monthColumn,
                amountColumn,
                dueDateColumn,
                statusColumn
        );

        // =====================================================
        // TOTAL VARIABLES
        // =====================================================
        double maintenancePendingAmount = 0;
        double electricityPendingAmount = 0;

        // =====================================================
        // FETCH MAINTENANCE
        // =====================================================
        /*
         * IMPORTANT:
         *
         * Only the currently logged-in resident's email is passed.
         *
         * Expected Firestore path:
         *
         * maintenance
         *     └── loggedInEmail
         *          ├── email
         *          ├── residentName
         *          ├── flatNo
         *          ├── amount
         *          ├── date
         *          ├── month
         *          └── status
         */
        // =========================================================
// FETCH ALL MAINTENANCE BILLS
// =========================================================

List<Map<String, Object>> maintenanceBills =
        fetchMaintenanceByEmail(loggedInEmail);

// =========================================================
// MAINTENANCE DATA
// =========================================================

if (maintenanceBills != null
        && !maintenanceBills.isEmpty()) {

    for (Map<String, Object> maintenanceData :
            maintenanceBills) {

        String amount =
                getValue(
                        maintenanceData,
                        "amount"
                );

        String date =
                getValue(
                        maintenanceData,
                        "date"
                );

        String month =
                getValue(
                        maintenanceData,
                        "month"
                );

        String status =
                getValue(
                        maintenanceData,
                        "status"
                ).trim();

        String billId =
                getValue(
                        maintenanceData,
                        "billId"
                );

        // If billId is missing, use month
        if (billId.isEmpty()) {
            billId = month;
        }

        String displayAmount =
                amount.startsWith("₹")
                        ? amount
                        : "₹ " + amount;

        // =====================================================
        // ADD MAINTENANCE BILL TO TABLE
        //
        // PAID + PENDING BOTH WILL BE SHOWN
        // =====================================================

        table.getItems().add(
                new Bill(
                        "Maintenance",
                        month,
                        displayAmount,
                        date,
                        status,
                        billId
                )
        );

        // =====================================================
        // CALCULATE MAINTENANCE PENDING AMOUNT
        // =====================================================

        if (!isPaid(status)) {

            maintenancePendingAmount +=
                    convertAmountToDouble(
                            amount
                    );
        }
    }

    // =========================================================
    // MAINTENANCE CARD
    // =========================================================

    Label maintenanceAmountLabel =
            (Label) maintenanceDue
                    .getChildren()
                    .get(1);

    Label maintenanceDescriptionLabel =
            (Label) maintenanceDue
                    .getChildren()
                    .get(2);

    if (maintenancePendingAmount > 0) {

        maintenanceAmountLabel.setText(
                "₹ " +
                        formatAmount(
                                maintenancePendingAmount
                        )
        );

        maintenanceDescriptionLabel.setText(
                "Pending"
        );

    } else {

        maintenanceAmountLabel.setText(
                "₹ 0"
        );

        maintenanceDescriptionLabel.setText(
                "All Paid"
        );
    }

} else {

    // =========================================================
    // NO MAINTENANCE BILL
    // =========================================================

    Label maintenanceAmountLabel =
            (Label) maintenanceDue
                    .getChildren()
                    .get(1);

    maintenanceAmountLabel.setText(
            "₹ 0"
    );

    Label maintenanceDescriptionLabel =
            (Label) maintenanceDue
                    .getChildren()
                    .get(2);

    maintenanceDescriptionLabel.setText(
            "No maintenance record"
    );
}
        // =====================================================
        // FETCH ALL ELECTRICITY BILLS
        // =====================================================
        /*
         * IMPORTANT:
         *
         * Only loggedInEmail is passed.
         *
         * Expected Firestore path:
         *
         * electricity
         *     └── loggedInEmail
         *          ├── email
         *          ├── residentName
         *          ├── flatNo
         *          └── bills
         *               ├── July 2026
         *               ├── August 2026
         *               └── September 2026
         */
        List<Map<String, Object>> electricityBills =
                fetchElectricityByEmail(loggedInEmail);

        // =====================================================
        // ELECTRICITY DATA
        // =====================================================
        if (electricityBills != null
                && !electricityBills.isEmpty()) {

            for (Map<String, Object> electricityData :
                    electricityBills) {

                String amount =
                        getValue(
                                electricityData,
                                "amount"
                        );

                String date =
                        getValue(
                                electricityData,
                                "date"
                        );

                String month =
                        getValue(
                                electricityData,
                                "month"
                        );

                String status =
                        getValue(
                                electricityData,
                                "status"
                        ).trim();

                String billId =
                        getValue(
                                electricityData,
                                "billId"
                        );

                /*
                 * If billId is missing,
                 * month is used as billId.
                 *
                 * This matches:
                 *
                 * bills/
                 *     └── July 2026
                 */
                if (billId.isEmpty()) {
                    billId = month;
                }

                String displayAmount =
                        amount.startsWith("₹")
                                ? amount
                                : "₹ " + amount;

                // =================================================
                // ADD EVERY ELECTRICITY BILL TO TABLE
                //
                // PAID + PENDING BOTH WILL BE SHOWN.
                // =================================================
                table.getItems().add(
                        new Bill(
                                "Electricity",
                                month,
                                displayAmount,
                                date,
                                status,
                                billId
                        )
                );

                // =================================================
                // CALCULATE ELECTRICITY PENDING
                // =================================================
                if (!isPaid(status)) {

                    electricityPendingAmount +=
                            convertAmountToDouble(
                                    amount
                            );
                }
            }

            // =====================================================
            // ELECTRICITY CARD
            // =====================================================
            Label electricityAmountLabel =
                    (Label) electricityDue
                            .getChildren()
                            .get(1);

            Label electricityDescriptionLabel =
                    (Label) electricityDue
                            .getChildren()
                            .get(2);

            if (electricityPendingAmount > 0) {

                electricityAmountLabel.setText(
                        "₹ " +
                                formatAmount(
                                        electricityPendingAmount
                                )
                );

                electricityDescriptionLabel.setText(
                        "Pending"
                );

            } else {

                electricityAmountLabel.setText(
                        "₹ 0"
                );

                electricityDescriptionLabel.setText(
                        "All Paid"
                );
            }

        } else {

            Label electricityAmountLabel =
                    (Label) electricityDue
                            .getChildren()
                            .get(1);

            electricityAmountLabel.setText(
                    "₹ 0"
            );

            Label electricityDescriptionLabel =
                    (Label) electricityDue
                            .getChildren()
                            .get(2);

            electricityDescriptionLabel.setText(
                    "No electricity bill"
            );
        }

        // =====================================================
        // TOTAL DUE
        // =====================================================
        double totalPendingAmount =
                maintenancePendingAmount
                        + electricityPendingAmount;

        Label totalAmountLabel =
                (Label) totalDue
                        .getChildren()
                        .get(1);

        totalAmountLabel.setText(
                "₹ " +
                        formatAmount(
                                totalPendingAmount
                        )
        );

        Label totalDescriptionLabel =
                (Label) totalDue
                        .getChildren()
                        .get(2);

        if (totalPendingAmount > 0) {

            totalDescriptionLabel.setText(
                    "Pending"
            );

        } else {

            totalDescriptionLabel.setText(
                    "No pending amount"
            );
        }

        // =====================================================
        // TABLE PLACEHOLDER
        // =====================================================
        if (table.getItems().isEmpty()) {

            table.setPlaceholder(
                    new Label(
                            "No bills found"
                    )
            );
        }

        // =====================================================
        // REFRESH BUTTON
        // =====================================================
        refreshButton.setOnAction(e -> {

            /*
             * IMPORTANT:
             *
             * Pass the SAME loggedInEmail.
             *
             * Never use new Mybills() here because that
             * would lose the resident email.
             */
            stage.setScene(
                    new Mybills(
                            loggedInEmail
                    ).getBillScene(stage)
            );
        });

        // =====================================================
        // ADD ELECTRICITY BILL BUTTON
        // =====================================================
        addElectricityButton.setOnAction(e -> {

            showAddElectricityBillDialog(stage);
        });

        // =====================================================
        // PAY BUTTON
        // =====================================================
        Button payButton =
                new Button("Pay Selected Bill");

        payButton.setPrefHeight(40);
        payButton.setPrefWidth(160);

        payButton.setStyle(
                "-fx-background-color: #4e342e;" +
                        "-fx-text-fill: #f3e5e2;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6;"
        );

        // =====================================================
        // PAY BUTTON ACTION
        // =====================================================
        payButton.setOnAction(e -> {

            Bill selectedBill =
                    table.getSelectionModel()
                            .getSelectedItem();

            // =================================================
            // NO BILL SELECTED
            // =================================================
            if (selectedBill == null) {

                Alert alert =
                        new Alert(
                                Alert.AlertType.WARNING
                        );

                alert.setTitle(
                        "No Bill Selected"
                );

                alert.setHeaderText(null);

                alert.setContentText(
                        "Please select a bill first."
                );

                alert.showAndWait();

                return;
            }

            // =================================================
            // CHECK ALREADY PAID
            // =================================================
            if (isPaid(
                    selectedBill.getStatus()
            )) {

                Alert alert =
                        new Alert(
                                Alert.AlertType.INFORMATION
                        );

                alert.setTitle(
                        "Already Paid"
                );

                alert.setHeaderText(null);

                alert.setContentText(
                        "This bill has already been paid."
                );

                alert.showAndWait();

                return;
            }

            try {

                // =================================================
                // GET BILL AMOUNT
                // =================================================
                double amount =
                        convertAmountToDouble(
                                selectedBill.getAmount()
                        );

                if (amount <= 0) {

                    Alert alert =
                            new Alert(
                                    Alert.AlertType.ERROR
                            );

                    alert.setTitle(
                            "Invalid Amount"
                    );

                    alert.setHeaderText(null);

                    alert.setContentText(
                            "Invalid bill amount."
                    );

                    alert.showAndWait();

                    return;
                }

                // =================================================
                // CONFIRMATION
                // =================================================
                Alert confirmation =
                        new Alert(
                                Alert.AlertType.CONFIRMATION
                        );

                confirmation.setTitle(
                        "Confirm Payment"
                );

                confirmation.setHeaderText(
                        "Pay " +
                                selectedBill.getType() +
                                " Bill"
                );

                confirmation.setContentText(
                        "Bill Type: "
                                + selectedBill.getType()
                                + "\nMonth: "
                                + selectedBill.getMonth()
                                + "\nAmount: ₹ "
                                + formatAmount(amount)
                                + "\n\n"
                                + "Continue to Razorpay?"
                );

                ButtonType result =
                        confirmation.showAndWait()
                                .orElse(
                                        ButtonType.CANCEL
                                );

                if (result != ButtonType.OK) {
                    return;
                }

                // =================================================
                // CREATE PAYMENT LINK
                // =================================================
                String description =
                        "Society360 "
                                + selectedBill.getType()
                                + " Bill - "
                                + selectedBill.getMonth();

                String paymentUrl =
                        razorpayService.createPaymentLink(
                                amount,
                                description
                        );

                System.out.println(
                        "Payment URL = "
                                + paymentUrl
                );

                // =================================================
                // OPEN RAZORPAY
                // =================================================
                razorpayService.openPaymentPage(
                        paymentUrl
                );

                // =================================================
                // PAYMENT MESSAGE
                // =================================================
                Alert alert =
                        new Alert(
                                Alert.AlertType.INFORMATION
                        );

                alert.setTitle(
                        "Razorpay Payment"
                );

                alert.setHeaderText(
                        "Payment Page Opened"
                );

                alert.setContentText(
                        "Razorpay payment page has been "
                                + "opened in your browser.\n\n"
                                + "Please complete the payment there."
                );

                alert.showAndWait();

                // =================================================
                // UPDATE CORRECT BILL
                // =================================================
                boolean updated = false;

                // =================================================
                // MAINTENANCE PAYMENT
                // =================================================
                if (selectedBill.getType()
                        .equalsIgnoreCase(
                                "Maintenance"
                        )) {

                    /*
                     * Maintenance path:
                     *
                     * maintenance/
                     *     loggedInEmail
                     *
                     * The email is always used.
                     */
                    updated =
                            updateMaintenanceStatus(
                                    loggedInEmail,
                                    selectedBill.getBillId(),
                                    "Paid"
                            );

                }

                // =================================================
                // ELECTRICITY PAYMENT
                // =================================================
                else if (selectedBill.getType()
                        .equalsIgnoreCase(
                                "Electricity"
                        )) {

                    /*
                     * Exact path:
                     *
                     * electricity/
                     *     loggedInEmail/
                     *         bills/
                     *             billId
                     *
                     * The billId is the month when
                     * month-based document IDs are used.
                     */
                    updated =
                            updateElectricityStatus(
                                    loggedInEmail,
                                    selectedBill.getBillId(),
                                    "Paid"
                            );
                }

                // =================================================
                // IF UPDATED
                // =================================================
                if (updated) {

                    selectedBill.setStatus(
                            "Paid"
                    );

                    Alert paidAlert =
                            new Alert(
                                    Alert.AlertType.INFORMATION
                            );

                    paidAlert.setTitle(
                            "Payment Successful"
                    );

                    paidAlert.setHeaderText(
                            "Bill Paid Successfully"
                    );

                    paidAlert.setContentText(
                            selectedBill.getType()
                                    + " bill for "
                                    + selectedBill.getMonth()
                                    + " has been marked as Paid."
                                    + "\n\n"
                                    + "The paid amount will no longer "
                                    + "be included in Total Due."
                    );

                    paidAlert.showAndWait();

                    /*
                     * IMPORTANT:
                     *
                     * Keep the same logged-in email
                     * after refresh.
                     */
                    stage.setScene(
                            new Mybills(
                                    loggedInEmail
                            ).getBillScene(stage)
                    );

                } else {

                    Alert updateAlert =
                            new Alert(
                                    Alert.AlertType.WARNING
                            );

                    updateAlert.setTitle(
                            "Payment Completed"
                    );

                    updateAlert.setHeaderText(
                            "Payment Done"
                    );

                    updateAlert.setContentText(
                            "Payment was completed, but the bill "
                                    + "status could not be updated "
                                    + "in Firestore."
                    );

                    updateAlert.showAndWait();
                }

            } catch (Exception ex) {

                ex.printStackTrace();

                Alert alert =
                        new Alert(
                                Alert.AlertType.ERROR
                        );

                alert.setTitle(
                        "Payment Error"
                );

                alert.setHeaderText(
                        "Unable to Start Payment"
                );

                alert.setContentText(
                        ex.getMessage() != null
                                ? ex.getMessage()
                                : "Something went wrong while starting payment."
                );

                alert.showAndWait();
            }
        });

        // =====================================================
        // BUTTON BOX
        // =====================================================
        HBox buttonBox =
                new HBox();

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.getChildren().add(
                payButton
        );

        // =====================================================
        // ADD CONTENT
        // =====================================================
        mainContent.getChildren().addAll(
                heading,
                summaryCards,
                billTitleBox,
                table,
                buttonBox
        );

        // =====================================================
        // CENTER AREA
        // =====================================================
        BorderPane mainarea =
                new BorderPane();

                mainarea.setStyle(
                 "-fx-background-color: transparent;"
);

        mainarea.setCenter(
                mainContent
        );

        root.setCenter(
                mainarea
        );

        // =====================================================
        // RETURN SCENE
        // =====================================================
        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }

    // =========================================================
    // ADD ELECTRICITY BILL DIALOG
    // =========================================================
    private void showAddElectricityBillDialog(
            Stage stage
    ) {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Add Electricity Bill"
        );

        dialog.setHeaderText(
                "Enter Electricity Bill Details"
        );

        // =====================================================
        // FORM
        // =====================================================
        GridPane grid =
                new GridPane();

        grid.setHgap(12);
        grid.setVgap(12);

        grid.setPadding(
                new Insets(20)
        );

        // =====================================================
        // AMOUNT
        // =====================================================
        Label amountLabel =
                new Label("Amount:");

        TextField amountField =
                new TextField();

        amountField.setPromptText(
                "1000"
        );

        // =====================================================
        // DATE
        // =====================================================
        Label dateLabel =
                new Label("Date:");

        TextField dateField =
                new TextField();

        dateField.setPromptText(
                "21-07-2026"
        );

        // =====================================================
        // EMAIL
        // =====================================================
        Label emailLabel =
                new Label("Email:");

        /*
         * IMPORTANT:
         *
         * Email is automatically taken from loggedInEmail.
         *
         * User cannot change it.
         */
        TextField emailField =
                new TextField(
                        loggedInEmail
                );

        emailField.setEditable(false);

        // =====================================================
        // FLAT NUMBER
        // =====================================================
        Label flatNoLabel =
                new Label("Flat No:");

        TextField flatNoField =
                new TextField();

        flatNoField.setPromptText(
                "A-204"
        );

        // =====================================================
        // MONTH
        // =====================================================
        Label monthLabel =
                new Label("Month:");

        TextField monthField =
                new TextField();

        monthField.setPromptText(
                "July 2026"
        );

        // =====================================================
        // RESIDENT NAME
        // =====================================================
        Label residentNameLabel =
                new Label("Resident Name:");

        TextField residentNameField =
                new TextField();

        residentNameField.setPromptText(
                "Vaishnavai"
        );

        // =====================================================
        // STATUS
        // =====================================================
        Label statusLabel =
                new Label("Status:");

        ComboBox<String> statusBox =
                new ComboBox<>();

        statusBox.getItems().addAll(
                "Pending",
                "Paid"
        );

        statusBox.setValue(
                "Pending"
        );

        // =====================================================
        // ADD TO GRID
        // =====================================================
        grid.add(
                amountLabel,
                0,
                0
        );

        grid.add(
                amountField,
                1,
                0
        );

        grid.add(
                dateLabel,
                0,
                1
        );

        grid.add(
                dateField,
                1,
                1
        );

        grid.add(
                emailLabel,
                0,
                2
        );

        grid.add(
                emailField,
                1,
                2
        );

        grid.add(
                flatNoLabel,
                0,
                3
        );

        grid.add(
                flatNoField,
                1,
                3
        );

        grid.add(
                monthLabel,
                0,
                4
        );

        grid.add(
                monthField,
                1,
                4
        );

        grid.add(
                residentNameLabel,
                0,
                5
        );

        grid.add(
                residentNameField,
                1,
                5
        );

        grid.add(
                statusLabel,
                0,
                6
        );

        grid.add(
                statusBox,
                1,
                6
        );

        dialog.getDialogPane()
                .setContent(grid);

        // =====================================================
        // BUTTONS
        // =====================================================
        ButtonType saveButtonType =
                new ButtonType(
                        "Save Bill",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        saveButtonType,
                        ButtonType.CANCEL
                );

        // =====================================================
        // SAVE BUTTON
        // =====================================================
        Button saveButton =
                (Button) dialog.getDialogPane()
                        .lookupButton(
                                saveButtonType
                        );

        saveButton.addEventFilter(
                javafx.event.ActionEvent.ACTION,
                event -> {

                    String amount =
                            amountField
                                    .getText()
                                    .trim();

                    String date =
                            dateField
                                    .getText()
                                    .trim();

                    /*
                     * IMPORTANT:
                     *
                     * Do NOT take email from TextField.
                     *
                     * Always use loggedInEmail.
                     */
                    String email =
                            loggedInEmail.trim();

                    String flatNo =
                            flatNoField
                                    .getText()
                                    .trim();

                    String month =
                            monthField
                                    .getText()
                                    .trim();

                    String residentName =
                            residentNameField
                                    .getText()
                                    .trim();

                    String status =
                            statusBox
                                    .getValue()
                                    .trim();

                    // =========================================
                    // VALIDATION
                    // =========================================
                    if (
                            amount.isEmpty()
                                    ||
                            date.isEmpty()
                                    ||
                            email.isEmpty()
                                    ||
                            flatNo.isEmpty()
                                    ||
                            month.isEmpty()
                                    ||
                            residentName.isEmpty()
                    ) {

                        Alert alert =
                                new Alert(
                                        Alert.AlertType.WARNING
                                );

                        alert.setTitle(
                                "Missing Information"
                        );

                        alert.setHeaderText(null);

                        alert.setContentText(
                                "Please fill all electricity bill fields."
                        );

                        alert.showAndWait();

                        event.consume();

                        return;
                    }

                    // =========================================
                    // VALIDATE AMOUNT
                    // =========================================
                    double numericAmount =
                            convertAmountToDouble(
                                    amount
                            );

                    if (numericAmount <= 0) {

                        Alert alert =
                                new Alert(
                                        Alert.AlertType.WARNING
                                );

                        alert.setTitle(
                                "Invalid Amount"
                        );

                        alert.setHeaderText(null);

                        alert.setContentText(
                                "Please enter a valid electricity bill amount."
                        );

                        alert.showAndWait();

                        event.consume();

                        return;
                    }

                    // =========================================
                    // SAVE TO FIRESTORE
                    // =========================================
                    /*
                     * DAO MUST save this under:
                     *
                     * electricity/
                     *     loggedInEmail/
                     *         email
                     *         residentName
                     *         flatNo
                     *         bills/
                     *             month
                     *
                     * Example:
                     *
                     * electricity/
                     *     resident1@gmail.com/
                     *         email = resident1@gmail.com
                     *         residentName = Resident 1
                     *         flatNo = A-101
                     *         bills/
                     *             July 2026/
                     *                 amount = 1000
                     *                 date = 21-07-2026
                     *                 month = July 2026
                     *                 status = Pending
                     */
                    boolean saved =
                            electricityDAO.addElectricityBill(
                                    email,
                                    residentName,
                                    flatNo,
                                    amount,
                                    date,
                                    month,
                                    status
                            );

                    if (!saved) {

                        Alert alert =
                                new Alert(
                                        Alert.AlertType.ERROR
                                );

                        alert.setTitle(
                                "Save Failed"
                        );

                        alert.setHeaderText(null);

                        alert.setContentText(
                                "Electricity bill could not be saved to Firestore."
                        );

                        alert.showAndWait();

                        event.consume();

                        return;
                    }

                    // =========================================
                    // SUCCESS
                    // =========================================
                    Alert alert =
                            new Alert(
                                    Alert.AlertType.INFORMATION
                            );

                    alert.setTitle(
                            "Success"
                    );

                    alert.setHeaderText(
                            "Electricity Bill Added"
                    );

                    alert.setContentText(
                            "Electricity bill has been saved successfully."
                    );

                    alert.showAndWait();

                    // =========================================
                    // REFRESH UI
                    // =========================================
                    /*
                     * IMPORTANT:
                     *
                     * Keep loggedInEmail.
                     */
                    stage.setScene(
                            new Mybills(
                                    loggedInEmail
                            ).getBillScene(stage)
                    );
                }
        );

        dialog.showAndWait();
    }

    // =========================================================
    // FETCH ELECTRICITY BILLS - ONLY LOGGED-IN RESIDENT
    // =========================================================
    private List<Map<String, Object>> fetchElectricityByEmail(
            String email
    ) {

        List<Map<String, Object>> bills =
                new java.util.ArrayList<>();

        if (email == null || email.trim().isEmpty()) {
            return bills;
        }

        try {

            Firestore db = FirebaseConfig.getFirestore();
            String currentEmail = email.trim();

            // ONLY this resident's parent document is opened.
            DocumentSnapshot parentDocument =
                    db.collection("electricity")
                            .document(currentEmail)
                            .get()
                            .get();

            // Support capital E collection name if needed.
            if (!parentDocument.exists()) {
                parentDocument =
                        db.collection("Electricity")
                                .document(currentEmail)
                                .get()
                                .get();
            }

            if (!parentDocument.exists()) {
                System.out.println(
                        "Electricity document not found for: "
                                + currentEmail
                );
                return bills;
            }

            // Extra protection: parent email must match logged-in email.
            String storedEmail = parentDocument.getString("email");

            if (storedEmail != null
                    && !storedEmail.trim().isEmpty()
                    && !storedEmail.trim().equalsIgnoreCase(currentEmail)) {
                return bills;
            }

            // ONLY this resident's bills subcollection is read.
            QuerySnapshot snapshot =
                    parentDocument.getReference()
                            .collection("bills")
                            .get()
                            .get();

            for (DocumentSnapshot billDocument : snapshot.getDocuments()) {

                Map<String, Object> data = billDocument.getData();

                if (data == null) {
                    continue;
                }

                data = new java.util.HashMap<>(data);

                Object billIdObject = data.get("billId");

                String billId;

                if (billIdObject != null
                        && !String.valueOf(billIdObject).trim().isEmpty()) {
                    billId = String.valueOf(billIdObject).trim();
                } else {
                    billId = billDocument.getId();
                }

                data.put("billId", billId);

                if (!data.containsKey("status")
                        || data.get("status") == null
                        || String.valueOf(data.get("status")).trim().isEmpty()) {
                    data.put("status", "Pending");
                }

                bills.add(data);
            }

        } catch (Exception e) {

            e.printStackTrace();

            System.err.println(
                    "Error fetching electricity for: " + email
            );
        }

        return bills;
    }

    // =========================================================
    // UPDATE ELECTRICITY STATUS - ONLY LOGGED-IN RESIDENT
    // =========================================================
    private boolean updateElectricityStatus(
            String email,
            String billId,
            String newStatus
    ) {

        if (email == null || email.trim().isEmpty()
                || billId == null || billId.trim().isEmpty()) {
            return false;
        }

        try {

            Firestore db = FirebaseConfig.getFirestore();
            String currentEmail = email.trim();

            DocumentSnapshot parentDocument =
                    db.collection("electricity")
                            .document(currentEmail)
                            .get()
                            .get();

            if (!parentDocument.exists()) {
                parentDocument =
                        db.collection("Electricity")
                                .document(currentEmail)
                                .get()
                                .get();
            }

            if (!parentDocument.exists()) {
                return false;
            }

            String storedEmail = parentDocument.getString("email");

            if (storedEmail != null
                    && !storedEmail.trim().isEmpty()
                    && !storedEmail.trim().equalsIgnoreCase(currentEmail)) {
                return false;
            }

            CollectionReference billsReference =
                    parentDocument.getReference().collection("bills");

            // First: Firestore document ID.
            DocumentSnapshot directDocument =
                    billsReference.document(billId.trim()).get().get();

            if (directDocument.exists()) {
                directDocument.getReference()
                        .update("status", newStatus)
                        .get();
                return true;
            }

            // Second: billId field.
            QuerySnapshot matched =
                    billsReference
                            .whereEqualTo("billId", billId.trim())
                            .get()
                            .get();

            if (!matched.isEmpty()) {
                matched.getDocuments().get(0)
                        .getReference()
                        .update("status", newStatus)
                        .get();
                return true;
            }

            // Older month-based document IDs are already covered by
            // the first case because the month is the document ID.

        } catch (Exception e) {

            e.printStackTrace();

            System.err.println(
                    "Error updating electricity status for: "
                            + email + " / " + billId
            );
        }

        return false;
    }

    // =========================================================
    // FETCH MAINTENANCE BILLS - DYNAMIC EMAIL + SOCIETY
    // =========================================================
    /*
     * Existing Firestore structure is NOT changed.
     *
     * Maintenance
     *   ├── aditi@gmail.com
     *   │     ├── email
     *   │     ├── society = "stanza"
     *   │     └── records
     *   │           └── recordId
     *   │                 ├── amount
     *   │                 ├── date
     *   │                 ├── maintenanceId
     *   │                 ├── month
     *   │                 ├── society
     *   │                 └── status
     *   ├── raghav@gmail.com
     *   │     └── ...
     *   └── ...
     *
     * Important:
     * The logged-in resident does NOT need to already have a
     * Maintenance/{email} document. We find the resident's society
     * dynamically, then scan existing Maintenance parent documents
     * and fetch records belonging to that society.
     */
    private List<Map<String, Object>> fetchMaintenanceByEmail(
            String email
    ) {

        List<Map<String, Object>> bills =
                new java.util.ArrayList<>();

        if (email == null || email.trim().isEmpty()) {
            return bills;
        }

        try {

            Firestore db = FirebaseConfig.getFirestore();
            String currentEmail = email.trim();

            // -----------------------------------------------------
            // 1. GET LOGGED-IN RESIDENT
            // -----------------------------------------------------
            DocumentSnapshot residentDocument =
                    db.collection("Residents")
                            .document(currentEmail)
                            .get()
                            .get();

            // Support lowercase collection name too.
            if (!residentDocument.exists()) {
                residentDocument =
                        db.collection("residents")
                                .document(currentEmail)
                                .get()
                                .get();
            }

            if (!residentDocument.exists()) {
                System.out.println(
                        "Resident document not found for: "
                                + currentEmail
                );
                return bills;
            }

            // -----------------------------------------------------
            // 2. GET SOCIETY DYNAMICALLY
            // -----------------------------------------------------
            String residentSociety =
                    residentDocument.getString("society");

            // Support societyName if your resident document uses it.
            if (residentSociety == null ||
                    residentSociety.trim().isEmpty()) {
                residentSociety =
                        residentDocument.getString("societyName");
            }

            if (residentSociety == null ||
                    residentSociety.trim().isEmpty()) {
                System.out.println(
                        "Society not found for resident: "
                                + currentEmail
                );
                return bills;
            }

            residentSociety = residentSociety.trim();

            System.out.println(
                    "Logged-in Email = " + currentEmail
                            + " | Society = " + residentSociety
            );

            // -----------------------------------------------------
            // 3. FETCH ALL MAINTENANCE PARENT DOCUMENTS
            // -----------------------------------------------------
            QuerySnapshot maintenanceSnapshot =
                    db.collection("Maintenance")
                            .get()
                            .get();

            // Support lowercase collection name too.
            if (maintenanceSnapshot.isEmpty()) {
                maintenanceSnapshot =
                        db.collection("maintenance")
                                .get()
                                .get();
            }

            // -----------------------------------------------------
            // 4. SEARCH EACH MAINTENANCE PARENT
            // -----------------------------------------------------
            for (DocumentSnapshot parentDocument :
                    maintenanceSnapshot.getDocuments()) {

                Map<String, Object> parentData =
                        parentDocument.getData();

                if (parentData == null) {
                    continue;
                }

                String parentSociety =
                        String.valueOf(
                                parentData.getOrDefault(
                                        "society", ""
                                )
                        ).trim();

                // Only same society is allowed.
                if (!residentSociety.equalsIgnoreCase(parentSociety)) {
                    continue;
                }

                // -------------------------------------------------
                // 5. FETCH RECORDS SUBCOLLECTION
                // -------------------------------------------------
                CollectionReference recordsReference =
                        parentDocument.getReference()
                                .collection("records");

                QuerySnapshot recordsSnapshot =
                        recordsReference.get()
                                .get();

                for (DocumentSnapshot recordDocument :
                        recordsSnapshot.getDocuments()) {

                    Map<String, Object> data =
                            recordDocument.getData();

                    if (data == null) {
                        continue;
                    }

                    data = new java.util.HashMap<>(data);

                    // Final society safety check on each record.
                    String recordSociety =
                            String.valueOf(
                                    data.getOrDefault(
                                            "society",
                                            parentSociety
                                    )
                            ).trim();

                    if (!residentSociety.equalsIgnoreCase(recordSociety)) {
                        continue;
                    }

                    // maintenanceId is preferred; otherwise use record ID.
                    Object maintenanceIdObject =
                            data.get("maintenanceId");

                    String billId;

                    if (maintenanceIdObject != null &&
                            !String.valueOf(maintenanceIdObject)
                                    .trim().isEmpty()) {
                        billId = String.valueOf(
                                maintenanceIdObject
                        ).trim();
                    } else {
                        billId = recordDocument.getId();
                    }

                    data.put("billId", billId);

                    if (!data.containsKey("status") ||
                            data.get("status") == null ||
                            String.valueOf(data.get("status"))
                                    .trim().isEmpty()) {
                        data.put("status", "Pending");
                    }

                    bills.add(data);

                    System.out.println(
                            "Fetched Maintenance -> "
                                    + "Parent: " + parentDocument.getId()
                                    + " | Month: " + data.get("month")
                                    + " | Amount: " + data.get("amount")
                                    + " | Status: " + data.get("status")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(
                    "ERROR while fetching maintenance for: " + email
            );
        }

        return bills;
    }

    // =========================================================
    // UPDATE MAINTENANCE STATUS
    // =========================================================
    /*
     * The bill may belong to any Maintenance/{email} document of
     * the logged-in resident's society, so we search the same-society
     * parent documents and update the matching record.
     */
    private boolean updateMaintenanceStatus(
            String email,
            String billId,
            String newStatus
    ) {

        if (email == null || email.trim().isEmpty() ||
                billId == null || billId.trim().isEmpty()) {
            return false;
        }

        try {

            Firestore db = FirebaseConfig.getFirestore();
            String currentEmail = email.trim();

            // Get logged-in resident society.
            DocumentSnapshot residentDocument =
                    db.collection("Residents")
                            .document(currentEmail)
                            .get()
                            .get();

            if (!residentDocument.exists()) {
                residentDocument =
                        db.collection("residents")
                                .document(currentEmail)
                                .get()
                                .get();
            }

            if (!residentDocument.exists()) {
                return false;
            }

            String residentSociety =
                    residentDocument.getString("society");

            if (residentSociety == null ||
                    residentSociety.trim().isEmpty()) {
                residentSociety =
                        residentDocument.getString("societyName");
            }

            if (residentSociety == null ||
                    residentSociety.trim().isEmpty()) {
                return false;
            }

            residentSociety = residentSociety.trim();

            QuerySnapshot maintenanceSnapshot =
                    db.collection("Maintenance")
                            .get()
                            .get();

            if (maintenanceSnapshot.isEmpty()) {
                maintenanceSnapshot =
                        db.collection("maintenance")
                                .get()
                                .get();
            }

            for (DocumentSnapshot parentDocument :
                    maintenanceSnapshot.getDocuments()) {

                String parentSociety =
                        parentDocument.getString("society");

                if (parentSociety == null ||
                        !residentSociety.equalsIgnoreCase(
                                parentSociety.trim()
                        )) {
                    continue;
                }

                CollectionReference recordsReference =
                        parentDocument.getReference()
                                .collection("records");

                // First: billId equals Firestore record ID.
                DocumentSnapshot directDocument =
                        recordsReference.document(billId.trim())
                                .get()
                                .get();

                if (directDocument.exists()) {
                    directDocument.getReference()
                            .update("status", newStatus)
                            .get();
                    return true;
                }

                // Second: billId equals maintenanceId field.
                QuerySnapshot matched =
                        recordsReference
                                .whereEqualTo(
                                        "maintenanceId",
                                        billId.trim()
                                )
                                .get()
                                .get();

                if (!matched.isEmpty()) {
                    matched.getDocuments().get(0)
                            .getReference()
                            .update("status", newStatus)
                            .get();
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(
                    "Error updating maintenance status for: "
                            + email + " / " + billId
            );
        }

        return false;
    }

    // =========================================================
    // SAFE GET VALUE
    // =========================================================
    private String getValue(
            Map<String, Object> data,
            String field
    ) {

        Object value =
                data.get(field);

        if (value == null) {
            return "";
        }

        return String.valueOf(value);
    }

    // =========================================================
    // CHECK PAID STATUS
    // =========================================================
    private boolean isPaid(
            String status
    ) {

        return status != null
                && status.trim()
                .equalsIgnoreCase("Paid");
    }

    // =========================================================
    // CONVERT AMOUNT
    // =========================================================
    private double convertAmountToDouble(
            String amount
    ) {

        try {

            if (amount == null
                    || amount.trim().isEmpty()) {

                return 0;
            }

            String cleanAmount =
                    amount
                            .replace("₹", "")
                            .replace(",", "")
                            .trim();

            return Double.parseDouble(
                    cleanAmount
            );

        } catch (Exception e) {

            return 0;
        }
    }

    // =========================================================
    // FORMAT AMOUNT
    // =========================================================
    private String formatAmount(
            double amount
    ) {

        if (amount == (long) amount) {

            return String.valueOf(
                    (long) amount
            );
        }

        return String.format(
                "%.2f",
                amount
        );
    }

    // =========================================================
    // SUMMARY CARD
    // =========================================================
    private VBox createSummaryCard(
            String heading,
            String amount,
            String description
    ) {

        VBox card =
                new VBox(8);

        card.setPadding(
                new Insets(15)
        );

        card.setPrefWidth(220);
        card.setPrefHeight(105);

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;"
        );

        Label headingLabel =
                new Label(heading);

        headingLabel.setTextFill(
                Color.web("#546E7A")
        );

        headingLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        Label amountLabel =
                new Label(amount);

        amountLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        22
                )
        );

        amountLabel.setTextFill(
                Color.web("#37474F")
        );

        Label descriptionLabel =
                new Label(description);

        descriptionLabel.setTextFill(
                Color.GRAY
        );

        card.getChildren().addAll(
                headingLabel,
                amountLabel,
                descriptionLabel
        );

        return card;
    }

    // =========================================================
    // BILL CLASS
    // =========================================================
    public static class Bill {

        private final SimpleStringProperty type;
        private final SimpleStringProperty month;
        private final SimpleStringProperty amount;
        private final SimpleStringProperty dueDate;
        private final SimpleStringProperty status;
        private final SimpleStringProperty billId;

        public Bill(
                String type,
                String month,
                String amount,
                String dueDate,
                String status,
                String billId
        ) {

            this.type =
                    new SimpleStringProperty(
                            type
                    );

            this.month =
                    new SimpleStringProperty(
                            month
                    );

            this.amount =
                    new SimpleStringProperty(
                            amount
                    );

            this.dueDate =
                    new SimpleStringProperty(
                            dueDate
                    );

            this.status =
                    new SimpleStringProperty(
                            status
                    );

            this.billId =
                    new SimpleStringProperty(
                            billId
                    );
        }

        // =====================================================
        // TYPE
        // =====================================================
        public StringProperty typeProperty() {

            return type;
        }

        // =====================================================
        // MONTH
        // =====================================================
        public StringProperty monthProperty() {

            return month;
        }

        // =====================================================
        // AMOUNT
        // =====================================================
        public StringProperty amountProperty() {

            return amount;
        }

        // =====================================================
        // DUE DATE
        // =====================================================
        public StringProperty dueDateProperty() {

            return dueDate;
        }

        // =====================================================
        // STATUS
        // =====================================================
        public StringProperty statusProperty() {

            return status;
        }

        // =====================================================
        // GET TYPE
        // =====================================================
        public String getType() {

            return type.get();
        }

        // =====================================================
        // GET MONTH
        // =====================================================
        public String getMonth() {

            return month.get();
        }

        // =====================================================
        // GET AMOUNT
        // =====================================================
        public String getAmount() {

            return amount.get();
        }

        // =====================================================
        // GET DUE DATE
        // =====================================================
        public String getDueDate() {

            return dueDate.get();
        }

        // =====================================================
        // GET STATUS
        // =====================================================
        public String getStatus() {

            return status.get();
        }

        // =====================================================
        // GET BILL ID
        // =====================================================
        public String getBillId() {

            return billId.get();
        }

        // =====================================================
        // UPDATE STATUS
        // =====================================================
        public void setStatus(
                String status
        ) {

            this.status.set(
                    status
            );
        }
    }
}
