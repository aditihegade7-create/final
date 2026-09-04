package com.society.view.Resident_portal;

import com.society.controller.Resident_Controller.AmenitiesController;
import com.society.model.Resident_model.Amenities;
import com.society.service.resident_service.RazorpayService;
import com.society.view.ScreenSize;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class AmenitiesBooking {

    private VBox cardsContainer;
    private VBox upcomingBookingsContainer;

    private final AmenitiesController amenitiesController;
    private final RazorpayService razorpay;

    private final String residentName;
    private final String flatNo;
    private final String loginEmail;

    /*
     * These are the application's available amenity time slots.
     * They are not resident/secretary-specific data.
     */
    private static final List<String> TIME_SLOTS = Arrays.asList(
            "08:00 AM - 10:00 AM",
            "10:00 AM - 12:00 PM",
            "12:00 PM - 02:00 PM",
            "02:00 PM - 04:00 PM",
            "04:00 PM - 06:00 PM",
            "06:00 PM - 08:00 PM"
    );

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

    public AmenitiesBooking(
            String residentName,
            String flatNo,
            String loginEmail) {

        this.residentName = safe(residentName);
        this.flatNo = safe(flatNo);
        this.loginEmail = normalizeEmail(loginEmail);

        /*
         * AmenitiesController resolves the secretary dynamically
         * from the logged-in resident.
         */
        this.amenitiesController =
                new AmenitiesController(this.loginEmail);

        this.razorpay = new RazorpayService();
    }

    public Scene getAminityScene(Stage stage) {

        panel panelobj = new panel(stage, loginEmail);

        BorderPane root = new BorderPane();

        root.setLeft(panelobj.getSidebar());

        BorderPane mainArea = new BorderPane();

        VBox heading = new VBox(3);
        heading.setPadding(new Insets(15, 20, 15, 20));
        heading.setStyle("-fx-background-color: white;");

        Label title = new Label("Book Amenity");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#4e342e"));

        Label subtitle = new Label("Reserve society amenities");
        subtitle.setFont(Font.font("System", 14));
        subtitle.setTextFill(Color.web("#757575"));

        heading.getChildren().addAll(title, subtitle);

        mainArea.setTop(heading);

        VBox content = new VBox(20);
        content.setPadding(new Insets(27, 38, 30, 38));
        content.setStyle("-fx-background-color: #e8ddd5;");

        cardsContainer = new VBox(15);

        loadAmenities();

        VBox upcomingSection = createUpcomingBooking();

        content.getChildren().addAll(
                cardsContainer,
                upcomingSection
        );

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );
        scrollPane.setStyle(
                "-fx-background: #e8ddd5;" +
                "-fx-background-color: #e8ddd5;"
        );

        mainArea.setCenter(scrollPane);

        root.setCenter(mainArea);

        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }

    private void loadAmenities() {

        cardsContainer.getChildren().clear();

        List<Amenities> amenities =
                amenitiesController.getAmenities();

        if (amenities == null || amenities.isEmpty()) {

            Label empty =
                    new Label("No amenities available.");

            empty.setFont(
                    Font.font(
                            "System",
                            FontWeight.BOLD,
                            16
                    )
            );

            cardsContainer.getChildren().add(empty);
            return;
        }

        GridPane grid = new GridPane();

        grid.setHgap(16);
        grid.setVgap(16);

        for (int i = 0; i < 3; i++) {

            ColumnConstraints column =
                    new ColumnConstraints();

            column.setPercentWidth(33.33);
            column.setHgrow(Priority.ALWAYS);

            grid.getColumnConstraints().add(column);
        }

        int row = 0;
        int column = 0;

        for (Amenities amenity : amenities) {

            VBox card =
                    createAmenityCard(amenity);

            grid.add(card, column, row);

            GridPane.setHgrow(
                    card,
                    Priority.ALWAYS
            );

            column++;

            if (column == 3) {
                column = 0;
                row++;
            }
        }

        cardsContainer.getChildren().add(grid);
    }

    private VBox createAmenityCard(Amenities amenity) {

        VBox card = new VBox(9);

        card.setPadding(new Insets(16));
        card.setMinHeight(148);
        card.setPrefHeight(148);
        card.setMaxHeight(170);

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8;"
        );

        Label name =
                new Label(
                        safe(amenity.getAmenityName())
                );

        name.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        16
                )
        );

        name.setTextFill(
                Color.web("#4e342e")
        );

        Label price =
                new Label(
                        formatPrice(
                                amenity.getPrice()
                        )
                );

        price.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        14
                )
        );

        price.setTextFill(
                Color.web("#263238")
        );

        Label description =
                new Label(
                        safe(amenity.getDescription())
                );

        description.setWrapText(true);

        description.setTextFill(
                Color.web("#789098")
        );

        description.setFont(
                Font.font("System", 13)
        );

        HBox bottomRow =
                new HBox(10);

        bottomRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label availability =
                new Label(
                        safe(amenity.getAvailability())
                );

        availability.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        availability.setTextFill(
                Color.web("#00843D")
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button bookButton =
                new Button("Book");

        bookButton.setPrefWidth(76);
        bookButton.setPrefHeight(30);

        bookButton.setStyle(
                "-fx-background-color: #4e342e;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;"
        );

        bookButton.setOnAction(
                e -> showBookingDialog(amenity)
        );

        bottomRow.getChildren().addAll(
                availability,
                spacer,
                bookButton
        );

        card.getChildren().addAll(
                name,
                price,
                description,
                bottomRow
        );

        return card;
    }

    private String formatPrice(String price) {

        if (price == null ||
                price.trim().isEmpty()) {

            return "₹ 0";
        }

        String number =
                price.replaceAll(
                        "[^0-9.]",
                        ""
                );

        return number.isEmpty()
                ? price
                : "₹ " + number;
    }

    private void showBookingDialog(
            Amenities amenity) {

        if (amenity == null ||
                isEmpty(amenity.getAmenityId())) {

            showError(
                    "This amenity could not be identified."
            );

            return;
        }

        VBox box =
                new VBox(15);

        box.setAlignment(
                Pos.CENTER
        );

        box.setPadding(
                new Insets(25)
        );

        box.setStyle(
                "-fx-background-color: #e8ddd5;"
        );

        Label title =
                new Label(
                        "Book " +
                        safe(amenity.getAmenityName())
                );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        22
                )
        );

        title.setTextFill(
                Color.web("#4e342e")
        );

        Label residentLabel =
                new Label(
                        "Resident: " +
                        residentName
                );

        residentLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        14
                )
        );

        Label flatLabel =
                new Label(
                        "Flat No: " +
                        flatNo
                );

        flatLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        14
                )
        );

        Label dateLabel =
                new Label(
                        "Select Date"
                );

        dateLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        14
                )
        );

        DatePicker datePicker =
                new DatePicker(
                        LocalDate.now()
                );

        datePicker.setPrefWidth(250);

        datePicker.setDayCellFactory(
                new Callback<DatePicker, DateCell>() {

                    @Override
                    public DateCell call(
                            DatePicker picker) {

                        return new DateCell() {

                            @Override
                            public void updateItem(
                                    LocalDate date,
                                    boolean empty) {

                                super.updateItem(
                                        date,
                                        empty
                                );

                                if (date != null &&
                                        date.isBefore(
                                                LocalDate.now()
                                        )) {

                                    setDisable(true);
                                }
                            }
                        };
                    }
                }
        );

        Label timeLabel =
                new Label(
                        "Select Time Slot"
                );

        timeLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        14
                )
        );

        ComboBox<String> timeSlot =
                new ComboBox<>();

        timeSlot.setPrefWidth(250);

        timeSlot.setPromptText(
                "Select time slot"
        );

        timeSlot.getItems().setAll(
                TIME_SLOTS
        );

        /*
         * These properties hold the latest Firestore availability
         * information for the selected date.
         */
        timeSlot.getProperties().put(
                "unavailableSlots",
                new HashSet<String>()
        );

        timeSlot.getProperties().put(
                "endedSlots",
                new HashSet<String>()
        );

        /*
         * Dropdown cell.
         *
         * No getSetProperty() helper is used.
         */
        timeSlot.setCellFactory(
                comboBox -> new ListCell<String>() {

                    @Override
                    protected void updateItem(
                            String item,
                            boolean empty) {

                        super.updateItem(
                                item,
                                empty
                        );

                        if (empty || item == null) {

                            setText(null);
                            setDisable(false);
                            setStyle("");

                            return;
                        }

                        Object unavailableValue =
                                comboBox.getProperties().get(
                                        "unavailableSlots"
                                );

                        Object endedValue =
                                comboBox.getProperties().get(
                                        "endedSlots"
                                );

                        Set<?> unavailable =
                                unavailableValue instanceof Set<?>
                                        ? (Set<?>) unavailableValue
                                        : java.util.Collections.emptySet();

                        Set<?> ended =
                                endedValue instanceof Set<?>
                                        ? (Set<?>) endedValue
                                        : java.util.Collections.emptySet();

                        if (unavailable.contains(item)) {

                            setText(
                                    item +
                                    "  (UNAVAILABLE)"
                            );

                            setDisable(true);

                            setStyle(
                                    "-fx-text-fill: #b02a37;"
                            );

                        } else if (ended.contains(item)) {

                            setText(
                                    item +
                                    "  (ENDED)"
                            );

                            setDisable(true);

                            setStyle(
                                    "-fx-text-fill: #757575;"
                            );

                        } else {

                            setText(item);

                            setDisable(false);

                            setStyle(
                                    "-fx-text-fill: #16823b;"
                            );
                        }
                    }
                }
        );

        /*
         * Selected-value display.
         */
        timeSlot.setButtonCell(
                new ListCell<String>() {

                    @Override
                    protected void updateItem(
                            String item,
                            boolean empty) {

                        super.updateItem(
                                item,
                                empty
                        );

                        if (empty || item == null) {

                            setText(null);
                            setStyle("");

                            return;
                        }

                        Object unavailableValue =
                                timeSlot.getProperties().get(
                                        "unavailableSlots"
                                );

                        Object endedValue =
                                timeSlot.getProperties().get(
                                        "endedSlots"
                                );

                        Set<?> unavailable =
                                unavailableValue instanceof Set<?>
                                        ? (Set<?>) unavailableValue
                                        : java.util.Collections.emptySet();

                        Set<?> ended =
                                endedValue instanceof Set<?>
                                        ? (Set<?>) endedValue
                                        : java.util.Collections.emptySet();

                        if (unavailable.contains(item)) {

                            setText(
                                    item +
                                    "  (UNAVAILABLE)"
                            );

                            setStyle(
                                    "-fx-text-fill: #b02a37;"
                            );

                        } else if (ended.contains(item)) {

                            setText(
                                    item +
                                    "  (ENDED)"
                            );

                            setStyle(
                                    "-fx-text-fill: #757575;"
                            );

                        } else {

                            setText(item);

                            setStyle(
                                    "-fx-text-fill: #16823b;"
                            );
                        }
                    }
                }
        );

        /*
         * Prevent selection of an unavailable or ended slot.
         */
        timeSlot.valueProperty().addListener(
                (obs, oldValue, newValue) -> {

                    if (newValue == null) {
                        return;
                    }

                    Object unavailableValue =
                            timeSlot.getProperties().get(
                                    "unavailableSlots"
                            );

                    Object endedValue =
                            timeSlot.getProperties().get(
                                    "endedSlots"
                            );

                    Set<?> unavailable =
                            unavailableValue instanceof Set<?>
                                    ? (Set<?>) unavailableValue
                                    : java.util.Collections.emptySet();

                    Set<?> ended =
                            endedValue instanceof Set<?>
                                    ? (Set<?>) endedValue
                                    : java.util.Collections.emptySet();

                    if (unavailable.contains(newValue)) {

                        timeSlot.getSelectionModel()
                                .clearSelection();

                        showError(
                                "This time slot is already booked and accepted by another resident."
                        );

                    } else if (ended.contains(newValue)) {

                        timeSlot.getSelectionModel()
                                .clearSelection();

                        showError(
                                "This time slot has already ended for today."
                        );
                    }
                }
        );

        /*
         * Refresh availability whenever the date changes.
         */
        datePicker.valueProperty().addListener(
                (obs, oldDate, newDate) ->
                        updateAvailableTimeSlots(
                                amenity,
                                newDate,
                                timeSlot
                        )
        );

        /*
         * Initial availability.
         */
        updateAvailableTimeSlots(
                amenity,
                datePicker.getValue(),
                timeSlot
        );

        Button paymentButton =
                new Button(
                        "Continue to Payment"
                );

        paymentButton.setStyle(
                "-fx-background-color: #4e342e;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 20;" +
                "-fx-background-radius: 6;"
        );

        Stage popup =
                new Stage();

        popup.setTitle(
                "Book Amenity"
        );

        popup.setScene(
                new Scene(
                        box,
                        400,
                        480
                )
        );

        paymentButton.setOnAction(e -> {

            LocalDate selectedDate =
                    datePicker.getValue();

            String selectedSlot =
                    timeSlot.getValue();

            if (selectedDate == null) {

                showError(
                        "Please select date."
                );

                return;
            }

            if (selectedDate.isBefore(
                    LocalDate.now())) {

                showError(
                        "Please select today or a future date."
                );

                return;
            }

            if (selectedSlot == null ||
                    selectedSlot.trim().isEmpty()) {

                showError(
                        "Please select time slot."
                );

                return;
            }

            Object unavailableValue =
                    timeSlot.getProperties().get(
                            "unavailableSlots"
                    );

            Object endedValue =
                    timeSlot.getProperties().get(
                            "endedSlots"
                    );

            Set<?> unavailable =
                    unavailableValue instanceof Set<?>
                            ? (Set<?>) unavailableValue
                            : java.util.Collections.emptySet();

            Set<?> ended =
                    endedValue instanceof Set<?>
                            ? (Set<?>) endedValue
                            : java.util.Collections.emptySet();

            if (unavailable.contains(
                    selectedSlot)) {

                timeSlot.getSelectionModel()
                        .clearSelection();

                showError(
                        "This time slot is already unavailable."
                );

                return;
            }

            if (ended.contains(selectedSlot)) {

                timeSlot.getSelectionModel()
                        .clearSelection();

                showError(
                        "This time slot has already ended."
                );

                return;
            }

            String[] times =
                    selectedSlot.split(" - ");

            if (times.length != 2) {

                showError(
                        "Invalid time slot selected."
                );

                return;
            }

            String startTime =
                    times[0].trim();

            String endTime =
                    times[1].trim();

            String bookingDate =
                    selectedDate.toString();

            /*
             * IMPORTANT:
             *
             * This is the first final Firestore availability check.
             * It prevents a stale UI from allowing an already accepted
             * slot to proceed to payment.
             */
            if (!amenitiesController.isSlotAvailable(
                    amenity,
                    bookingDate,
                    startTime,
                    endTime
            )) {

                updateAvailableTimeSlots(
                        amenity,
                        selectedDate,
                        timeSlot
                );

                timeSlot.getSelectionModel()
                        .clearSelection();

                showError(
                        "This time slot is no longer available. Please select another slot."
                );

                return;
            }

            popup.close();

            startPayment(
                    amenity,
                    bookingDate,
                    startTime,
                    endTime
            );
        });

        box.getChildren().addAll(
                title,
                residentLabel,
                flatLabel,
                dateLabel,
                datePicker,
                timeLabel,
                timeSlot,
                paymentButton
        );

        popup.show();
    }

    private void updateAvailableTimeSlots(
            Amenities amenity,
            LocalDate selectedDate,
            ComboBox<String> timeSlot) {

        if (amenity == null ||
                selectedDate == null ||
                timeSlot == null) {

            return;
        }

        Set<String> unavailableSlots =
                new HashSet<>();

        Set<String> endedSlots =
                new HashSet<>();

        try {

            String bookingDate =
                    selectedDate.toString();

            /*
             * Only ACCEPTED bookings are returned here.
             *
             * PENDING requests do not block the slot.
             * REJECTED requests do not block the slot.
             */
            Set<String> accepted =
                    amenitiesController
                            .getUnavailableAcceptedSlots(
                                    amenity,
                                    bookingDate
                            );

            if (accepted != null) {

                unavailableSlots.addAll(
                        accepted
                );
            }

            /*
             * A slot whose end time has passed today
             * cannot be newly booked.
             */
            if (selectedDate.isEqual(
                    LocalDate.now())) {

                LocalTime now =
                        LocalTime.now();

                for (String slot : TIME_SLOTS) {

                    String[] times =
                            slot.split(" - ");

                    if (times.length != 2) {
                        continue;
                    }

                    LocalTime end =
                            parseTime(times[1]);

                    if (end != null &&
                            !now.isBefore(end)) {

                        endedSlots.add(slot);
                    }
                }
            }

            /*
             * Store the latest values on the ComboBox.
             */
            timeSlot.getProperties().put(
                    "unavailableSlots",
                    unavailableSlots
            );

            timeSlot.getProperties().put(
                    "endedSlots",
                    endedSlots
            );

            /*
             * Re-create the item list so JavaFX redraws
             * the cells using the latest properties.
             */
            timeSlot.getItems().setAll(
                    TIME_SLOTS
            );

            timeSlot.getSelectionModel()
                    .clearSelection();

            if (unavailableSlots.isEmpty() &&
                    endedSlots.isEmpty()) {

                timeSlot.setPromptText(
                        "Select time slot"
                );

            } else {

                timeSlot.setPromptText(
                        "Select an available time slot"
                );
            }

            /*
             * No ComboBox.refresh() is used.
             */
            timeSlot.requestLayout();

        } catch (Exception e) {

            e.printStackTrace();

            timeSlot.getProperties().put(
                    "unavailableSlots",
                    new HashSet<String>()
            );

            timeSlot.getProperties().put(
                    "endedSlots",
                    new HashSet<String>()
            );

            timeSlot.getSelectionModel()
                    .clearSelection();
        }
    }

    private void startPayment(
            Amenities amenity,
            String bookingDate,
            String startTime,
            String endTime) {

        double amount;

        try {

            String price =
                    amenity.getPrice();

            if (price == null ||
                    price.trim().isEmpty()) {

                throw new NumberFormatException(
                        "Price is empty"
                );
            }

            String number =
                    price.replaceAll(
                            "[^0-9.]",
                            ""
                    );

            amount =
                    Double.parseDouble(number);

            if (amount <= 0) {

                throw new NumberFormatException(
                        "Price must be greater than zero"
                );
            }

        } catch (Exception e) {

            showError(
                    "Invalid amenity price: " +
                    safe(amenity.getPrice())
            );

            return;
        }

        Stage processingStage =
                createProcessingStage();

        processingStage.show();

        final double finalAmount =
                amount;

        Task<String> paymentTask =
                new Task<String>() {

                    @Override
                    protected String call()
                            throws Exception {

                        return razorpay
                                .createPaymentLink(
                                        finalAmount,
                                        safe(
                                                amenity.getAmenityName()
                                        ) +
                                        " Booking"
                                );
                    }
                };

        paymentTask.setOnSucceeded(e -> {

            processingStage.close();

            String paymentUrl =
                    paymentTask.getValue();

            if (paymentUrl == null ||
                    paymentUrl.trim().isEmpty()) {

                showError(
                        "Razorpay did not return a payment link."
                );

                return;
            }

            try {

                razorpay.openPaymentPage(
                        paymentUrl
                );

                showPaymentWaiting(
                        amenity,
                        bookingDate,
                        startTime,
                        endTime,
                        finalAmount
                );

            } catch (Exception ex) {

                ex.printStackTrace();

                showError(
                        "Unable to open Razorpay payment page."
                );
            }
        });

        paymentTask.setOnFailed(e -> {

            processingStage.close();

            Throwable error =
                    paymentTask.getException();

            if (error != null) {
                error.printStackTrace();
            }

            showError(
                    "Unable to create Razorpay payment."
            );
        });

        Thread thread =
                new Thread(paymentTask);

        thread.setDaemon(true);
        thread.start();
    }

    private Stage createProcessingStage() {

        VBox box =
                new VBox(18);

        box.setAlignment(
                Pos.CENTER
        );

        box.setPadding(
                new Insets(30)
        );

        box.setStyle(
                "-fx-background-color: #e8ddd5;"
        );

        Label title =
                new Label(
                        "Creating Payment..."
                );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        22
                )
        );

        title.setTextFill(
                Color.web("#4e342e")
        );

        ProgressIndicator progress =
                new ProgressIndicator();

        Label message =
                new Label(
                        "Please wait while we connect to Razorpay."
                );

        message.setWrapText(true);

        box.getChildren().addAll(
                title,
                progress,
                message
        );

        Stage stage =
                new Stage();

        stage.setTitle(
                "Payment"
        );

        stage.setScene(
                new Scene(
                        box,
                        400,
                        250
                )
        );

        stage.setResizable(false);

        return stage;
    }

    private void showPaymentWaiting(
            Amenities amenity,
            String bookingDate,
            String startTime,
            String endTime,
            double amount) {

        VBox box =
                new VBox(18);

        box.setAlignment(
                Pos.CENTER
        );

        box.setPadding(
                new Insets(30)
        );

        box.setStyle(
                "-fx-background-color: #e8ddd5;"
        );

        Label title =
                new Label(
                        "Payment Processing"
                );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        24
                )
        );

        title.setTextFill(
                Color.web("#4e342e")
        );

        ProgressIndicator progress =
                new ProgressIndicator();

        Label message =
                new Label(
                        "Razorpay payment page has been opened.\n\n" +
                        "Complete your payment there.\n" +
                        "After successful payment, come back here."
                );

        message.setWrapText(true);

        message.setAlignment(
                Pos.CENTER
        );

        Label amountLabel =
                new Label(
                        "Amount: ₹ " +
                        amount
                );

        amountLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        16
                )
        );

        Button completedButton =
                new Button(
                        "I Have Completed Payment"
                );

        completedButton.setStyle(
                "-fx-background-color: #4e342e;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12 25;" +
                "-fx-background-radius: 6;"
        );

        Button cancelButton =
                new Button("Cancel");

        HBox buttons =
                new HBox(
                        10,
                        completedButton,
                        cancelButton
                );

        buttons.setAlignment(
                Pos.CENTER
        );

        box.getChildren().addAll(
                title,
                progress,
                message,
                amountLabel,
                buttons
        );

        Stage stage =
                new Stage();

        stage.setTitle(
                "Razorpay Payment"
        );

        stage.setScene(
                new Scene(
                        box,
                        550,
                        420
                )
        );

        stage.setResizable(false);

        completedButton.setOnAction(e -> {

            stage.close();

            verifyPaymentAndSave(
                    amenity,
                    bookingDate,
                    startTime,
                    endTime,
                    amount
            );
        });

        cancelButton.setOnAction(e -> {

            stage.close();

            showPaymentFailed();
        });

        stage.show();
    }

    private void verifyPaymentAndSave(
            Amenities amenity,
            String bookingDate,
            String startTime,
            String endTime,
            double amount) {

        Alert alert =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        alert.setTitle(
                "Payment Verification"
        );

        alert.setHeaderText(
                "Was your payment successful?"
        );

        alert.setContentText(
                "Confirm only if Razorpay shows that your payment was successful."
        );

        alert.showAndWait().ifPresent(
                result -> {

                    if (result == ButtonType.OK) {

                        saveBooking(
                                amenity,
                                bookingDate,
                                startTime,
                                endTime,
                                amount
                        );

                    } else {

                        showPaymentFailed();
                    }
                }
        );
    }

    private void saveBooking(
            Amenities amenity,
            String bookingDate,
            String startTime,
            String endTime,
            double amount) {

        try {

            /*
             * SECOND final Firestore check.
             *
             * This protects against another accepted booking
             * appearing while the resident was on Razorpay.
             */
            if (!amenitiesController.isSlotAvailable(
                    amenity,
                    bookingDate,
                    startTime,
                    endTime
            )) {

                showError(
                        "Payment was completed, but this time slot has already been accepted by another resident. The booking was not created."
                );

                loadAmenities();
                loadUpcomingBookings();

                return;
            }

            /*
             * Booking is created as:
             *
             * paymentStatus = SUCCESS
             * bookingStatus = PENDING
             */
            String bookingId =
                    amenitiesController.saveBooking(
                            amenity,
                            bookingDate,
                            startTime,
                            endTime,
                            flatNo,
                            residentName,
                            loginEmail,
                            "PENDING",
                            "SUCCESS",
                            String.valueOf(amount)
                    );

            if (bookingId != null) {

                showPaymentSuccess(
                        bookingId
                );

                loadAmenities();
                loadUpcomingBookings();

            } else {

                showError(
                        "Payment completed but booking could not be saved because the slot is no longer available."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Error while saving booking."
            );
        }
    }

    private VBox createUpcomingBooking() {

        VBox section =
                new VBox(15);

        Label title =
                new Label(
                        "Upcoming Booking"
                );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        18
                )
        );

        title.setTextFill(
                Color.web("#263238")
        );

        upcomingBookingsContainer =
                new VBox(10);

        section.getChildren().addAll(
                title,
                upcomingBookingsContainer
        );

        loadUpcomingBookings();

        return section;
    }

    private void loadUpcomingBookings() {

        if (upcomingBookingsContainer == null) {
            return;
        }

        upcomingBookingsContainer
                .getChildren()
                .clear();

        if (isEmpty(loginEmail)) {

            Label error =
                    new Label(
                            "Unable to identify logged-in resident."
                    );

            error.setTextFill(
                    Color.web("#b02a37")
            );

            upcomingBookingsContainer
                    .getChildren()
                    .add(error);

            return;
        }

        List<Map<String, Object>> bookings =
                amenitiesController
                        .getResidentBookings(
                                loginEmail
                        );

        if (bookings == null ||
                bookings.isEmpty()) {

            Label empty =
                    new Label(
                            "You have no upcoming bookings."
                    );

            empty.setFont(
                    Font.font(
                            "System",
                            14
                    )
            );

            empty.setTextFill(
                    Color.web("#757575")
            );

            upcomingBookingsContainer
                    .getChildren()
                    .add(empty);

            return;
        }

        bookings.sort(
                Comparator.comparing(
                        booking ->
                                getValue(
                                        booking,
                                        "bookingDate"
                                )
                )
        );

        LocalDate today =
                LocalDate.now();

        boolean foundUpcoming =
                false;

        for (Map<String, Object> booking :
                bookings) {

            if (booking == null) {
                continue;
            }

            String bookingDate =
                    getValue(
                            booking,
                            "bookingDate"
                    );

            String bookingStatus =
                    getValue(
                            booking,
                            "bookingStatus"
                    );

            if (bookingStatus.isEmpty()) {
                bookingStatus = "PENDING";
            }

            if ("CANCELLED".equalsIgnoreCase(
                    bookingStatus)) {

                continue;
            }

            try {

                LocalDate date =
                        LocalDate.parse(
                                bookingDate
                        );

                if (date.isBefore(today)) {
                    continue;
                }

            } catch (Exception ignored) {
                /*
                 * Keep the booking visible if
                 * its date cannot be parsed.
                 */
            }

            upcomingBookingsContainer
                    .getChildren()
                    .add(
                            createBookingCard(
                                    booking
                            )
                    );

            foundUpcoming = true;
        }

        if (!foundUpcoming) {

            Label empty =
                    new Label(
                            "You have no upcoming bookings."
                    );

            empty.setFont(
                    Font.font(
                            "System",
                            14
                    )
            );

            empty.setTextFill(
                    Color.web("#757575")
            );

            upcomingBookingsContainer
                    .getChildren()
                    .add(empty);
        }
    }

    private VBox createBookingCard(
            Map<String, Object> booking) {

        VBox card =
                new VBox(8);

        card.setPadding(
                new Insets(18)
        );

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8;"
        );

        Label amenity =
                new Label(
                        getValue(
                                booking,
                                "amenityName"
                        )
                );

        amenity.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );

        amenity.setTextFill(
                Color.web("#4e342e")
        );

        Label idLabel =
                new Label(
                        "Booking ID: " +
                        getValue(
                                booking,
                                "bookingId"
                        )
                );

        idLabel.setFont(
                Font.font(
                        "System",
                        12
                )
        );

        idLabel.setTextFill(
                Color.web("#757575")
        );

        String bookingDate =
                getValue(
                        booking,
                        "bookingDate"
                );

        Label date =
                new Label(
                        "Date: " +
                        formatDate(
                                bookingDate
                        )
                );

        date.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        Label time =
                new Label(
                        "Time: " +
                        getValue(
                                booking,
                                "startTime"
                        ) +
                        " - " +
                        getValue(
                                booking,
                                "endTime"
                        )
                );

        time.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        Label flat =
                new Label(
                        "Flat: " +
                        getValue(
                                booking,
                                "flatNo"
                        )
                );

        flat.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        Label resident =
                new Label(
                        "Resident: " +
                        getValue(
                                booking,
                                "residentName"
                        )
                );

        resident.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        Label amount =
                new Label(
                        "Amount: ₹ " +
                        getValue(
                                booking,
                                "paymentAmount"
                        )
                );

        amount.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        String bookingStatus =
                getValue(
                        booking,
                        "bookingStatus"
                );

        if (bookingStatus.isEmpty()) {
            bookingStatus = "PENDING";
        }

        Label statusLabel =
                new Label(
                        bookingStatus.toUpperCase()
                );

        statusLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        statusLabel.setPadding(
                new Insets(
                        6,
                        14,
                        6,
                        14
                )
        );

        if ("ACCEPTED".equalsIgnoreCase(
                bookingStatus)) {

            statusLabel.setStyle(
                    "-fx-background-color: #d8f3df;" +
                    "-fx-text-fill: #16823b;" +
                    "-fx-background-radius: 15;"
            );

        } else if ("REJECTED".equalsIgnoreCase(
                bookingStatus)) {

            statusLabel.setStyle(
                    "-fx-background-color: #f8d7da;" +
                    "-fx-text-fill: #b02a37;" +
                    "-fx-background-radius: 15;"
            );

        } else if ("PROGRESS".equalsIgnoreCase(
                bookingStatus)) {

            statusLabel.setStyle(
                    "-fx-background-color: #dbeafe;" +
                    "-fx-text-fill: #1d4ed8;" +
                    "-fx-background-radius: 15;"
            );

        } else {

            statusLabel.setStyle(
                    "-fx-background-color: #fff3cd;" +
                    "-fx-text-fill: #856404;" +
                    "-fx-background-radius: 15;"
            );
        }

        HBox statusRow =
                new HBox();

        statusRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label statusText =
                new Label(
                        "Booking Status:"
                );

        statusText.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        14
                )
        );

        Region statusSpacer =
                new Region();

        HBox.setHgrow(
                statusSpacer,
                Priority.ALWAYS
        );

        statusRow.getChildren().addAll(
                statusText,
                statusSpacer,
                statusLabel
        );

        String paymentStatus =
                getValue(
                        booking,
                        "paymentStatus"
                );

        if (paymentStatus.isEmpty()) {
            paymentStatus = "UNKNOWN";
        }

        Label paymentLabel =
                new Label(
                        "Payment: " +
                        paymentStatus.toUpperCase()
                );

        paymentLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        if ("PAID".equalsIgnoreCase(
                paymentStatus)
                ||
                "SUCCESS".equalsIgnoreCase(
                        paymentStatus)) {

            paymentLabel.setTextFill(
                    Color.web("#16823b")
            );

        } else {

            paymentLabel.setTextFill(
                    Color.web("#856404")
            );
        }

        card.getChildren().addAll(
                amenity,
                idLabel,
                date,
                time,
                flat,
                resident,
                amount,
                statusRow,
                paymentLabel
        );

        return card;
    }

    private String getValue(
            Map<String, Object> data,
            String key) {

        if (data == null ||
                key == null) {

            return "";
        }

        Object value =
                data.get(key);

        return value == null
                ? ""
                : value.toString();
    }

    private String formatDate(
            String date) {

        try {

            LocalDate localDate =
                    LocalDate.parse(date);

            return localDate.format(
                    DateTimeFormatter.ofPattern(
                            "dd MMM yyyy"
                    )
            );

        } catch (Exception e) {

            return safe(date);
        }
    }

    private void showPaymentSuccess(
            String bookingId) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Booking Request Submitted"
        );

        alert.setHeaderText(
                "✓ Payment Successful"
        );

        alert.setContentText(
                "Your payment was successful and your amenity booking request has been sent to the Secretary.\n\n" +
                "Booking ID: " +
                bookingId +
                "\n" +
                "Booking Status: PENDING"
        );

        alert.showAndWait();
    }

    private void showPaymentFailed() {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle(
                "Payment"
        );

        alert.setHeaderText(
                "Payment Not Completed"
        );

        alert.setContentText(
                "Your amenity has not been booked."
        );

        alert.showAndWait();
    }

    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Error"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }

    private static LocalTime parseTime(
            String value) {

        try {

            return LocalTime.parse(
                    value.trim()
                            .toUpperCase(
                                    Locale.ENGLISH
                            ),
                    TIME_FORMATTER
            );

        } catch (Exception e) {

            return null;
        }
    }

    private static String safe(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private static String normalizeEmail(
            String value) {

        return value == null
                ? ""
                : value.trim()
                        .toLowerCase(
                                Locale.ENGLISH
                        );
    }

    private static boolean isEmpty(
            String value) {

        return value == null ||
                value.trim().isEmpty();
    }
}