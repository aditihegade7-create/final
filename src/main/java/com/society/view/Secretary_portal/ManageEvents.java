package com.society.view.Secretary_portal;

import java.time.LocalDate;
import java.util.ArrayList;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;

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
    // ALL EVENTS
    // =====================================================

    private List<Event> currentEvents = new ArrayList<>();

    // =====================================================
    // COLORS
    // =====================================================

    private static final String HEADER_COLOR = "#4e342e";
    private static final String PAGE_BACKGROUND = "#F7F7F7";

    // =====================================================
    // BACKGROUND IMAGE
    // =====================================================

    private static final String BACKGROUND_IMAGE =
            "/background-Dashboard5.jpeg";

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
                new VBox(0);

        mainvb.setPrefWidth(1220);

        mainvb.setMaxWidth(
                Double.MAX_VALUE
        );

        mainvb.setMaxHeight(
                Double.MAX_VALUE
        );

        mainvb.setStyle(
                "-fx-background-color:"
                        + PAGE_BACKGROUND
                        + ";"
        );

        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label(
                        "Manage Events"
                );

        title.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        Label subtitle =
                new Label(
                        "Create and manage society events"
                );

        subtitle.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:white;"
        );

        VBox titleBox =
                new VBox(4);

        titleBox.setAlignment(
                Pos.CENTER_LEFT
        );

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        // =================================================
        // ADD EVENT BUTTON
        // =================================================

        Button addEventBtn =
                new Button(
                        "+ Add Event"
                );

        addEventBtn.setPrefWidth(150);
        addEventBtn.setPrefHeight(40);

        addEventBtn.setStyle(
                "-fx-background-color:" +
                HEADER_COLOR +
                ";" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        addEventBtn.setOnAction(
                e -> openAddEventDialog()
        );

        // =================================================
        // REFRESH BUTTON
        // =================================================

        Button refreshBtn =
                new Button(
                        "↻ Refresh"
                );

        refreshBtn.setPrefWidth(100);
        refreshBtn.setPrefHeight(40);

        refreshBtn.setStyle(
                "-fx-background-color:white;" +
                "-fx-text-fill:" +
                HEADER_COLOR +
                ";" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        refreshBtn.setOnAction(
                e -> loadEvents()
        );

        // =================================================
        // HEADER
        // =================================================

        HBox header =
                new HBox(10);

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setPadding(
                new Insets(
                        15,
                        25,
                        15,
                        25
                )
        );

        header.setPrefHeight(80);
        header.setMinHeight(80);
        header.setMaxHeight(80);

        HBox.setHgrow(
                titleBox,
                Priority.ALWAYS
        );

        header.getChildren().addAll(
                titleBox,
                refreshBtn
        );

        header.setStyle(
                "-fx-background-color:" +
                HEADER_COLOR +
                ";"
        );

        // =================================================
        // CONTENT BOX
        // =================================================

        VBox contentBox =
                new VBox(15);

        contentBox.setPadding(
                new Insets(25)
        );

        contentBox.setMaxWidth(
                Double.MAX_VALUE
        );

        contentBox.setMaxHeight(
                Double.MAX_VALUE
        );

        VBox.setVgrow(
                contentBox,
                Priority.ALWAYS
        );

        // =================================================
        // BACKGROUND IMAGE
        // =================================================

        try {

            if (getClass().getResource(
                    BACKGROUND_IMAGE
            ) != null) {

                Image backgroundImage =
                        new Image(
                                getClass()
                                        .getResource(
                                                BACKGROUND_IMAGE
                                        )
                                        .toExternalForm()
                        );

                BackgroundImage bgImage =
                        new BackgroundImage(
                                backgroundImage,
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

                contentBox.setBackground(
                        new Background(bgImage)
                );

            } else {

                System.err.println(
                        "Background image not found: "
                                + BACKGROUND_IMAGE
                );

                contentBox.setStyle(
                        "-fx-background-color:"
                                + PAGE_BACKGROUND
                                + ";"
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "Background image could not be loaded: "
                            + e.getMessage()
            );

            contentBox.setStyle(
                    "-fx-background-color:"
                            + PAGE_BACKGROUND
                            + ";"
            );
        }

        // =================================================
        // SEARCH BAR
        // =================================================

        TextField searchField =
                new TextField();

        searchField.setPromptText(
                "Search event..."
        );

        searchField.setPrefHeight(40);
        searchField.setPrefWidth(750);
        searchField.setMaxWidth(750);

        searchField.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:8;" +
                "-fx-border-color:#DDDDDD;" +
                "-fx-border-radius:8;" +
                "-fx-padding:0 15 0 15;" +
                "-fx-font-size:13px;"
        );

        // =================================================
        // SEARCH + ADD EVENT ROW
        // =================================================

        HBox searchBox =
                new HBox(10);

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        searchBox.getChildren().addAll(
                searchField,
                addEventBtn
        );

        // =================================================
        // EVENT LIST
        // =================================================

        eventList =
                new VBox(15);

        eventList.setPadding(
                new Insets(
                        5,
                        0,
                        5,
                        0
                )
        );

        eventList.setMaxWidth(
                Double.MAX_VALUE
        );

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

        scrollPane.setMaxWidth(
                Double.MAX_VALUE
        );

        // INVISIBLE SCROLL BAR

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;" +
                "-fx-border-color:transparent;"
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        // =================================================
        // SEARCH EVENT
        // =================================================

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        searchEvents(
                                newValue
                        )
        );

        // =================================================
        // LOAD EVENTS
        // =================================================

        loadEvents();

        // =================================================
        // VIEW ALL EVENTS
        // =================================================

        Button viewAllBtn =
                new Button(
                        "View All Events"
                );

        viewAllBtn.setMaxWidth(
                Double.MAX_VALUE
        );

        viewAllBtn.setPrefHeight(40);

        viewAllBtn.setStyle(
                "-fx-background-color:" +
                HEADER_COLOR +
                ";" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        viewAllBtn.setOnAction(
                e -> openViewAllEventsDialog()
        );

        // =================================================
        // CONTENT
        // =================================================

        contentBox.getChildren().addAll(
                searchBox,
                scrollPane,
                viewAllBtn
        );

        // =================================================
        // MAIN CONTENT
        // =================================================

        mainvb.getChildren().addAll(
                header,
                contentBox
        );

        VBox.setVgrow(
                contentBox,
                Priority.ALWAYS
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

        mainRoot.setStyle(
                "-fx-background-color:"
                        + PAGE_BACKGROUND
                        + ";"
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

        rootStack.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        rootStack.getChildren().add(
                mainRoot
        );

        // =================================================
        // SCENE
        // =================================================

        manageEventsScene =
                new Scene(
                        rootStack,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        return manageEventsScene;
    }

    // =====================================================
    // LOAD EVENTS FROM FIRESTORE
    // =====================================================

    private void loadEvents() {

        if (eventList == null) {
            return;
        }

        eventList
                .getChildren()
                .clear();

        currentEvents.clear();

        try {

            List<Event> events =
                    eventController.getAllEvents();

            if (events == null ||
                    events.isEmpty()) {

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

            currentEvents.addAll(events);

            displayEvents(currentEvents);

        } catch (Exception e) {

            e.printStackTrace();

            Label errorLabel =
                    new Label(
                            "Unable to load events."
                    );

            errorLabel.setStyle(
                    "-fx-font-size:14px;" +
                    "-fx-text-fill:#B00020;"
            );

            eventList.getChildren().add(
                    errorLabel
            );
        }
    }

    // =====================================================
    // SEARCH EVENTS
    // =====================================================

    private void searchEvents(
            String searchText) {

        if (searchText == null) {
            searchText = "";
        }

        String search =
                searchText
                        .trim()
                        .toLowerCase();

        if (search.isEmpty()) {

            displayEvents(
                    currentEvents
            );

            return;
        }

        List<Event> filteredEvents =
                new ArrayList<>();

        for (Event event : currentEvents) {

            if (event == null) {
                continue;
            }

            String eventName =
                    safeValue(
                            event.getEventName()
                    ).toLowerCase();

            String date =
                    safeValue(
                            event.getDate()
                    ).toLowerCase();

            String time =
                    safeValue(
                            event.getTime()
                    ).toLowerCase();

            String venue =
                    safeValue(
                            event.getVenue()
                    ).toLowerCase();

            String status =
                    getEventStatus(
                            event.getDate()
                    ).toLowerCase();

            if (eventName.contains(search)
                    || date.contains(search)
                    || time.contains(search)
                    || venue.contains(search)
                    || status.contains(search)) {

                filteredEvents.add(
                        event
                );
            }
        }

        displayEvents(
                filteredEvents
        );
    }

    // =====================================================
    // DISPLAY EVENTS
    // =====================================================

    private void displayEvents(
            List<Event> events) {

        eventList
                .getChildren()
                .clear();

        if (events == null ||
                events.isEmpty()) {

            Label emptyLabel =
                    new Label(
                            "No matching events found."
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

            if (event == null) {
                continue;
            }

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
    }

    // =====================================================
    // AUTOMATIC EVENT STATUS
    // =====================================================

    private String getEventStatus(
            String date) {

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
                "-fx-background-color:" +
                "rgba(0,0,0,0.5);"
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
                new Label(
                        "Add New Event"
                );

        title.setFont(
                Font.font(
                        "Georgia",
                        FontWeight.BOLD,
                        22
                )
        );

        title.setStyle(
                "-fx-text-fill:" +
                HEADER_COLOR +
                ";"
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
                new Label(
                        "Event Name"
                );

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
                new Label(
                        "Date"
                );

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
                new Label(
                        "Time"
                );

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
                new Label(
                        "Venue"
                );

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
                new Button(
                        "Cancel"
                );

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
                new Button(
                        "Save Event"
                );

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
            // EVENT NAME
            // =============================================

            String eventName =
                    nameField.getValue();

            // =============================================
            // DATE
            // =============================================

            String date = "";

            if (dateField.getValue() != null) {

                date =
                        dateField
                                .getValue()
                                .toString();
            }

            // =============================================
            // TIME
            // =============================================

            String time =
                    timeField.getValue();

            // =============================================
            // VENUE
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
            // STATUS
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
                "-fx-background-color:" +
                "rgba(0,0,0,0.5);"
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
                new Label(
                        "All Events"
                );

        title.setFont(
                Font.font(
                        "Georgia",
                        FontWeight.BOLD,
                        22
                )
        );

        title.setStyle(
                "-fx-text-fill:" +
                HEADER_COLOR +
                ";"
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

        try {

            List<Event> events =
                    eventController.getAllEvents();

            if (events == null ||
                    events.isEmpty()) {

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

                    if (event == null) {
                        continue;
                    }

                    // =====================================
                    // AUTOMATIC STATUS
                    // =====================================

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

        } catch (Exception e) {

            e.printStackTrace();

            Label errorLabel =
                    new Label(
                            "Unable to load events."
                    );

            errorLabel.setStyle(
                    "-fx-font-size:14px;" +
                    "-fx-text-fill:#B00020;"
            );

            allEvents.getChildren().add(
                    errorLabel
            );
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

        popupScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        popupScroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        popupScroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;" +
                "-fx-border-color:transparent;"
        );

        // =================================================
        // CLOSE BUTTON
        // =================================================

        Button closeBottomBtn =
                new Button(
                        "Close"
                );

        closeBottomBtn.setPrefWidth(100);

        closeBottomBtn.setPrefHeight(40);

        closeBottomBtn.setStyle(
                "-fx-background-color:" +
                HEADER_COLOR +
                ";" +
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

        if (rootStack != null &&
                overlay != null) {

            rootStack.getChildren().remove(
                    overlay
            );
        }
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
                new Label(
                        safeValue(eventName)
                );

        name.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" +
                HEADER_COLOR +
                ";"
        );

        // =================================================
        // DETAILS
        // =================================================

        Label details =
                new Label(
                        "Date: "
                                + safeValue(date)
                                + "    |    "
                                + "Time: "
                                + safeValue(time)
                                + "    |    "
                                + "Venue: "
                                + safeValue(venue)
                );

        details.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#777777;"
        );

        // =================================================
        // STATUS
        // =================================================

        Label status =
                new Label(
                        safeValue(statusText)
                );

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

    // =====================================================
    // SAFE VALUE
    // =====================================================

    private String safeValue(
            String value) {

        return value == null ||
                value.trim().isEmpty()
                ? "-"
                : value;
    }
}