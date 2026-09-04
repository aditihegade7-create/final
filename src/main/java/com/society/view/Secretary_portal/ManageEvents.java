package com.society.view.Secretary_portal;

import java.time.LocalDate;
import java.util.List;

import com.society.controller.Secretary_Controller.EventController;
import com.society.model.Secretary_model.Event;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ManageEvents {

    private Scene manageEventsScene;

    // =====================================================
    // ROOT STACK
    // =====================================================

    private StackPane rootStack;

    // =====================================================
    // CONTROLLER
    // =====================================================

    private EventController eventController;

    // =====================================================
    // EVENT LIST
    // =====================================================

    private VBox eventList;

    // =====================================================
    // CREATE SCENE
    // =====================================================

    public Scene createScene(Stage stage) {

        // =================================================
        // CONTROLLER
        // =================================================

        eventController = new EventController();

        // =================================================
        // SIDEBAR
        // =================================================

        SecretarySidebar sidebarObj =
                new SecretarySidebar();

        VBox sidebar =
                sidebarObj.createSidebar(stage);

        // =================================================
        // MAIN CONTENT
        // =================================================

        VBox mainvb =
                new VBox(20);

        mainvb.setPadding(
                new Insets(25)
        );

        mainvb.setPrefWidth(1220);

        mainvb.setStyle(
                "-fx-background-color:#b3adad;"
        );

        // =================================================
        // HEADING
        // =================================================

        Label heading =
                new Label("MANAGE EVENTS");

        heading.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#434141;"
        );

        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label("Manage Events");

        title.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:black;"
        );

        Label subtitle =
                new Label(
                        "Create and manage society events"
                );

        subtitle.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#777777;"
        );

        VBox titleBox =
                new VBox(5);

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        // =================================================
        // ADD EVENT BUTTON
        // =================================================

        Button addEventBtn =
                new Button("+ Add Event");

        addEventBtn.setPrefWidth(130);
        addEventBtn.setPrefHeight(40);

        addEventBtn.setStyle(
                "-fx-background-color:#434141;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        addEventBtn.setOnAction(
                e -> openAddEventDialog()
        );

        // =================================================
        // HEADER
        // =================================================

        HBox header =
                new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                titleBox,
                Priority.ALWAYS
        );

        header.getChildren().addAll(
                titleBox,
                addEventBtn
        );

        // =================================================
        // EVENT LIST
        // =================================================

        eventList =
                new VBox(15);

        eventList.setPadding(
                new Insets(5, 0, 5, 0)
        );

        // =================================================
        // LOAD EVENTS
        // =================================================

        loadEvents();

        // =================================================
        // SCROLL PANE
        // =================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                eventList
        );

        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(500);

        scrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;"
        );

        // =================================================
        // VIEW ALL EVENTS
        // =================================================

        Button viewAllBtn =
                new Button("View All Events");

        viewAllBtn.setPrefWidth(1180);

        viewAllBtn.setPrefHeight(40);

        viewAllBtn.setStyle(
                "-fx-background-color:#434141;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-border-color:#EEEEEE;" +
                "-fx-border-radius:7;" +
                "-fx-cursor:hand;"
        );

        viewAllBtn.setOnAction(
                e -> openViewAllEventsDialog()
        );

        // =================================================
        // MAIN CONTENT
        // =================================================

        mainvb.getChildren().addAll(
                heading,
                header,
                scrollPane,
                viewAllBtn
        );

        // =================================================
        // MAIN ROOT
        // =================================================

        HBox mainRoot =
                new HBox();

        mainRoot.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        mainRoot.getChildren().addAll(
                sidebar,
                mainvb
        );

        HBox.setHgrow(
                mainvb,
                Priority.ALWAYS
        );

        // =================================================
        // STACK PANE
        // =================================================

        rootStack =
                new StackPane();

        rootStack.getChildren().add(
                mainRoot
        );

        // =================================================
        // SCENE
        // =================================================

        Scene scene =
                new Scene(
                        rootStack,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        manageEventsScene = scene;

        return manageEventsScene;
    }

    // =====================================================
    // LOAD EVENTS FROM FIRESTORE
    // =====================================================

    private void loadEvents() {

        eventList.getChildren().clear();

        try {

            List<Event> events =
                    eventController.getAllEvents();

            if (events == null || events.isEmpty()) {

                Label emptyLabel =
                        new Label(
                                "No events found."
                        );

                emptyLabel.setStyle(
                        "-fx-font-size:14px;" +
                        "-fx-text-fill:#666666;"
                );

                eventList.getChildren().add(
                        emptyLabel
                );

                return;
            }

            for (Event event : events) {

                // =========================================
                // AUTOMATIC STATUS
                // =========================================

                String status =
                        getEventStatus(
                                event.getDate()
                        );

                VBox eventCard =
                        createEvent(
                                event.getEventName(),
                                event.getDate(),
                                event.getTime(),
                                event.getVenue(),
                                status
                        );

                eventList.getChildren().add(
                        eventCard
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            Label errorLabel =
                    new Label(
                            "Unable to load events."
                    );

            errorLabel.setStyle(
                    "-fx-font-size:14px;" +
                    "-fx-text-fill:red;"
            );

            eventList.getChildren().add(
                    errorLabel
            );
        }
    }

    // =====================================================
    // AUTOMATIC EVENT STATUS
    // =====================================================

    private String getEventStatus(String date) {

        try {

            LocalDate eventDate =
                    LocalDate.parse(date);

            LocalDate today =
                    LocalDate.now();

            if (eventDate.isBefore(today)) {

                return "Completed";

            } else {

                return "Upcoming";
            }

        } catch (Exception e) {

            return "Upcoming";
        }
    }

    // =====================================================
    // ADD EVENT POPUP
    // =====================================================

    private void openAddEventDialog() {

        // =================================================
        // OVERLAY
        // =================================================

        StackPane overlay =
                new StackPane();

        overlay.setStyle(
                "-fx-background-color:rgba(0,0,0,0.5);"
        );

        // =================================================
        // FORM BOX
        // =================================================

        VBox formBox =
                new VBox(12);

        formBox.setPadding(
                new Insets(25)
        );

        formBox.setMaxWidth(500);

        formBox.setMaxHeight(600);

        formBox.setStyle("""
            -fx-background-color:#ffffff;
            -fx-background-radius:20;
            -fx-effect:dropshadow(
                three-pass-box,
                rgba(0,0,0,0.3),
                20,
                0,
                0,
                5
            );
        """);

        // =================================================
        // HEADER
        // =================================================

        HBox headerRow =
                new HBox();

        headerRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label("Add New Event");

        title.setFont(
                Font.font(
                        "Georgia",
                        FontWeight.BOLD,
                        22
                )
        );

        title.setStyle(
                "-fx-text-fill:#123C36;"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button closeBtn =
                new Button("✕");

        closeBtn.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16
                )
        );

        closeBtn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#555555;" +
                "-fx-cursor:hand;"
        );

        closeBtn.setOnAction(
                e -> removeOverlay(overlay)
        );

        headerRow.getChildren().addAll(
                title,
                spacer,
                closeBtn
        );

        // =================================================
        // EVENT NAME
        // =================================================

        Label nameLabel =
                new Label("Event Name");

        ComboBox<String> nameField =
                new ComboBox<>();

        nameField.setPromptText(
                "Select event"
        );

        nameField.getItems().addAll(

                "Society Annual Meeting",

                "Children's Drawing Competition",

                "Yoga Session",

                "Cleanliness Drive",

                "Cultural Evening",

                "Tree Plantation Drive",

                "Festival Celebration",

                "Sports Day",

                "Health Check-up Camp",

                "Blood Donation Camp",

                "Society Picnic",

                "Senior Citizen Meet",

                "Children's Sports Competition",

                "Independence Day Celebration",

                "Republic Day Celebration",

                "Diwali Celebration",

                "Holi Celebration",

                "Ganesh Festival",

                "Navratri Celebration",

                "Christmas Celebration"
        );

        nameField.setPrefHeight(40);

        nameField.setMaxWidth(
                Double.MAX_VALUE
        );

        // =================================================
        // DATE
        // =================================================

        Label dateLabel =
                new Label("Date");

        DatePicker dateField =
                new DatePicker();

        dateField.setPromptText(
                "Select event date"
        );

        dateField.setPrefHeight(40);

        dateField.setMaxWidth(
                Double.MAX_VALUE
        );

        // =================================================
        // TIME
        // =================================================

        Label timeLabel =
                new Label("Time");

        ComboBox<String> timeField =
                new ComboBox<>();

        timeField.setPromptText(
                "Select event time"
        );

        timeField.getItems().addAll(

                "07:00 AM",
                "08:00 AM",
                "09:00 AM",
                "10:00 AM",
                "11:00 AM",
                "12:00 PM",
                "01:00 PM",
                "02:00 PM",
                "03:00 PM",
                "04:00 PM",
                "05:00 PM",
                "06:00 PM",
                "07:00 PM",
                "08:00 PM",
                "09:00 PM"
        );

        timeField.setPrefHeight(40);

        timeField.setMaxWidth(
                Double.MAX_VALUE
        );

        // =================================================
        // VENUE
        // =================================================

        Label venueLabel =
                new Label("Venue");

        ComboBox<String> venueField =
                new ComboBox<>();

        venueField.setPromptText(
                "Select venue"
        );

        venueField.getItems().addAll(

                "Community Hall",

                "Society Garden",

                "Garden Area",

                "Society Entrance",

                "Terrace",

                "Club House",

                "Children's Play Area",

                "Parking Area",

                "Society Ground"
        );

        venueField.setPrefHeight(40);

        venueField.setMaxWidth(
                Double.MAX_VALUE
        );

        // =================================================
        // CANCEL BUTTON
        // =================================================

        Button cancelBtn =
                new Button("Cancel");

        cancelBtn.setPrefWidth(100);

        cancelBtn.setPrefHeight(40);

        cancelBtn.setStyle(
                "-fx-background-color:#E5E7EB;" +
                "-fx-text-fill:#333333;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        // =================================================
        // SAVE BUTTON
        // =================================================

        Button saveBtn =
                new Button("Save Event");

        saveBtn.setPrefWidth(120);

        saveBtn.setPrefHeight(40);

        saveBtn.setStyle(
                "-fx-background-color:#2E9D63;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        // =================================================
        // BUTTON BOX
        // =================================================

        HBox buttonBox =
                new HBox(10);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.getChildren().addAll(
                cancelBtn,
                saveBtn
        );

        // =================================================
        // CANCEL ACTION
        // =================================================

        cancelBtn.setOnAction(
                e -> removeOverlay(overlay)
        );

        // =================================================
        // SAVE EVENT
        // =================================================

        saveBtn.setOnAction(e -> {

            // =============================================
            // GET EVENT NAME
            // =============================================

            String eventName =
                    nameField.getValue();

            // =============================================
            // GET DATE
            // =============================================

            String date = "";

            if (dateField.getValue() != null) {

                date =
                        dateField
                                .getValue()
                                .toString();
            }

            // =============================================
            // GET TIME
            // =============================================

            String time =
                    timeField.getValue();

            // =============================================
            // GET VENUE
            // =============================================

            String venue =
                    venueField.getValue();

            // =============================================
            // VALIDATION
            // =============================================

            if (eventName == null
                    || eventName.isEmpty()
                    || date.isEmpty()
                    || time == null
                    || time.isEmpty()
                    || venue == null
                    || venue.isEmpty()) {

                showAlert(
                        "Validation Error",
                        "Please select all event details."
                );

                return;
            }

            // =============================================
            // CALCULATE STATUS
            // =============================================

            String status =
                    getEventStatus(date);

            // =============================================
            // SAVE USING CONTROLLER
            // =============================================

            boolean success =
                    eventController.addEvent(
                            eventName,
                            date,
                            time,
                            venue,
                            status
                    );

            // =============================================
            // SUCCESS
            // =============================================

            if (success) {

                showAlert(
                        "Success",
                        "Event added successfully."
                );

                removeOverlay(
                        overlay
                );

                // =========================================
                // REFRESH EVENT LIST
                // =========================================

                loadEvents();

            } else {

                showAlert(
                        "Error",
                        "Failed to add event to Firestore."
                );
            }
        });

        // =================================================
        // FORM CONTENT
        // =================================================

        formBox.getChildren().addAll(

                headerRow,

                nameLabel,
                nameField,

                dateLabel,
                dateField,

                timeLabel,
                timeField,

                venueLabel,
                venueField,

                buttonBox
        );

        // =================================================
        // ADD FORM TO OVERLAY
        // =================================================

        overlay.getChildren().add(
                formBox
        );

        StackPane.setAlignment(
                formBox,
                Pos.CENTER
        );

        rootStack.getChildren().add(
                overlay
        );
    }

    // =====================================================
    // VIEW ALL EVENTS
    // =====================================================

    private void openViewAllEventsDialog() {

        StackPane overlay =
                new StackPane();

        overlay.setStyle(
                "-fx-background-color:rgba(0,0,0,0.5);"
        );

        VBox formBox =
                new VBox(15);

        formBox.setPadding(
                new Insets(25)
        );

        formBox.setMaxWidth(650);

        formBox.setMaxHeight(600);

        formBox.setStyle("""
            -fx-background-color:#ffffff;
            -fx-background-radius:20;
            -fx-effect:dropshadow(
                three-pass-box,
                rgba(0,0,0,0.3),
                20,
                0,
                0,
                5
            );
        """);

        // =================================================
        // HEADER
        // =================================================

        HBox headerRow =
                new HBox();

        headerRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label("All Events");

        title.setFont(
                Font.font(
                        "Georgia",
                        FontWeight.BOLD,
                        22
                )
        );

        title.setStyle(
                "-fx-text-fill:#123C36;"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button closeBtn =
                new Button("✕");

        closeBtn.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16
                )
        );

        closeBtn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#555555;" +
                "-fx-cursor:hand;"
        );

        closeBtn.setOnAction(
                e -> removeOverlay(overlay)
        );

        headerRow.getChildren().addAll(
                title,
                spacer,
                closeBtn
        );

        // =================================================
        // GET EVENTS FROM FIRESTORE
        // =================================================

        VBox allEvents =
                new VBox(12);

        List<Event> events =
                eventController.getAllEvents();

        if (events == null || events.isEmpty()) {

            Label emptyLabel =
                    new Label(
                            "No events found."
                    );

            emptyLabel.setStyle(
                    "-fx-font-size:14px;" +
                    "-fx-text-fill:#666666;"
            );

            allEvents.getChildren().add(
                    emptyLabel
            );

        } else {

            for (Event event : events) {

                // =========================================
                // AUTOMATIC STATUS
                // =========================================

                String status =
                        getEventStatus(
                                event.getDate()
                        );

                VBox eventCard =
                        createEvent(
                                event.getEventName(),
                                event.getDate(),
                                event.getTime(),
                                event.getVenue(),
                                status
                        );

                allEvents.getChildren().add(
                        eventCard
                );
            }
        }

        // =================================================
        // SCROLL
        // =================================================

        ScrollPane popupScroll =
                new ScrollPane();

        popupScroll.setContent(
                allEvents
        );

        popupScroll.setFitToWidth(true);

        popupScroll.setPrefHeight(430);

        popupScroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;"
        );

        // =================================================
        // CLOSE BUTTON
        // =================================================

        Button closeBottomBtn =
                new Button("Close");

        closeBottomBtn.setPrefWidth(100);

        closeBottomBtn.setPrefHeight(40);

        closeBottomBtn.setStyle(
                "-fx-background-color:#434141;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        closeBottomBtn.setOnAction(
                e -> removeOverlay(overlay)
        );

        HBox buttonBox =
                new HBox(
                        closeBottomBtn
                );

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        // =================================================
        // FORM CONTENT
        // =================================================

        formBox.getChildren().addAll(
                headerRow,
                popupScroll,
                buttonBox
        );

        // =================================================
        // ADD FORM TO OVERLAY
        // =================================================

        overlay.getChildren().add(
                formBox
        );

        StackPane.setAlignment(
                formBox,
                Pos.CENTER
        );

        rootStack.getChildren().add(
                overlay
        );
    }

    // =====================================================
    // REMOVE OVERLAY
    // =====================================================

    private void removeOverlay(
            StackPane overlay) {

        rootStack.getChildren().remove(
                overlay
        );
    }

    // =====================================================
    // ALERT
    // =====================================================

    private void showAlert(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    // =====================================================
    // CREATE EVENT CARD
    // =====================================================

    private VBox createEvent(
            String eventName,
            String date,
            String time,
            String venue,
            String statusText) {

        VBox event =
                new VBox(10);

        event.setPadding(
                new Insets(18)
        );

        event.setPrefHeight(105);

        event.setMaxWidth(1180);

        event.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#EEEEEE;" +
                "-fx-border-radius:10;"
        );

        // =================================================
        // EVENT NAME
        // =================================================

        Label name =
                new Label(eventName);

        name.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#123C36;"
        );

        // =================================================
        // DETAILS
        // =================================================

        Label details =
                new Label(
                        "Date: " + date +
                        "    |    " +
                        "Time: " + time +
                        "    |    " +
                        "Venue: " + venue
                );

        details.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#777777;"
        );

        // =================================================
        // STATUS
        // =================================================

        Label status =
                new Label(statusText);

        if ("Upcoming".equalsIgnoreCase(
                statusText)) {

            status.setStyle(
                    "-fx-background-color:#E5F7EC;" +
                    "-fx-text-fill:#2E9D63;" +
                    "-fx-font-size:10px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:5px 10px;" +
                    "-fx-background-radius:12;"
            );

        } else {

            status.setStyle(
                    "-fx-background-color:#EAF0F6;" +
                    "-fx-text-fill:#55708A;" +
                    "-fx-font-size:10px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:5px 10px;" +
                    "-fx-background-radius:12;"
            );
        }

        // =================================================
        // BOTTOM ROW
        // =================================================

        HBox bottom =
                new HBox();

        bottom.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                details,
                Priority.ALWAYS
        );

        bottom.getChildren().addAll(
                details,
                status
        );

        // =================================================
        // ADD TO EVENT CARD
        // =================================================

        event.getChildren().addAll(
                name,
                bottom
        );

        return event;
    }
}