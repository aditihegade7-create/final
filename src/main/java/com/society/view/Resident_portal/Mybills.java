
package com.society.view.Resident_portal;

import com.society.service.resident_service.RazorpayService;
import com.society.dao.Resident_dao.MaintenanceDAO;
import com.society.dao.Resident_dao.ElectricityDAO;
import com.society.view.ScreenSize;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private final MaintenanceDAO maintenanceDAO =
            new MaintenanceDAO();

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
                "-fx-background-color: #e8ddd5;"
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

        VBox heading =
                new VBox(5);

        heading.setPadding(
                new Insets(15, 20, 15, 20)
        );

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

        billTitle.setTextFill(Color.WHITE);

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
        maintenanceDAO.getMaintenanceByEmail(
                loggedInEmail
        );

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
                electricityDAO.getElectricityBillsByEmail(
                        loggedInEmail
                );

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
                            maintenanceDAO
                                    .updateMaintenanceStatus(
                                            loggedInEmail,
                                            selectedBill.getMonth(),
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
                            electricityDAO
                                    .updateElectricityStatusByBillId(
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
