package com.society.view.Secretary_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.society.controller.Secretary_Controller.PollController;
import com.society.model.Secretary_model.Poll;
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
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PollsandSurveys {

    // =========================================================
    // SCENE
    // =========================================================

    private Scene pollsScene;

    // =========================================================
    // CONTROLLER
    // =========================================================

    private PollController pollController;

    // =========================================================
    // POLL LIST
    // =========================================================

    private VBox pollList;

    // =========================================================
    // FILTER
    // =========================================================

    private ComboBox<String> filterCombo;

    // =========================================================
    // DATE FORMAT
    // =========================================================

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    // =========================================================
    // CREATE SCENE
    // =========================================================

    public Scene createScene(Stage stage) {

        pollController =
                new PollController();

        // =====================================================
        // SIDEBAR
        // =====================================================

        SecretarySidebar sidebarObj =
                new SecretarySidebar();

        VBox sidebar =
                sidebarObj.createSidebar(stage);

        sidebar.setPrefWidth(280);
        sidebar.setMinWidth(280);
        sidebar.setMaxWidth(280);

        // =====================================================
        // MAIN
        // =====================================================

        VBox main =
                new VBox(15);

        main.setPadding(
                new Insets(
                        22,
                        25,
                        20,
                        25
                )
        );

        main.setStyle(
                "-fx-background-color:#E8DDD3;"
        );

        main.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        // =====================================================
        // HEADER
        // =====================================================

        Label heading =
                new Label(
                        "Polls & Surveys"
                );

        heading.setStyle(
                "-fx-font-size:26px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#24456D;"
        );

        Label subtitle =
                new Label(
                        "Create polls, collect opinions and manage society surveys"
                );

        subtitle.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#666666;"
        );

        VBox headingBox =
                new VBox(4);

        headingBox.getChildren().addAll(
                heading,
                subtitle
        );

        // =====================================================
        // CREATE BUTTON
        // =====================================================

        Button createButton =
                new Button(
                        "+ Create New Poll"
                );

        createButton.setPrefWidth(165);
        createButton.setPrefHeight(40);

        createButton.setStyle(
                "-fx-background-color:#56342B;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-font-size:14px;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        /*
         * IMPORTANT:
         *
         * This opens an overlay inside the current Scene.
         * No new Stage is created.
         */

        createButton.setOnAction(
                e -> openCreatePollPopup()
        );

        HBox header =
                new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                headingBox,
                Priority.ALWAYS
        );

        header.getChildren().addAll(
                headingBox,
                createButton
        );

        // =====================================================
        // STATISTICS
        // =====================================================

        HBox statistics =
                new HBox(12);

        Label activeValue =
                new Label("0");

        Label surveyValue =
                new Label("0");

        Label closedValue =
                new Label("0");

        VBox activeCard =
                createStatCard(
                        "Active Polls",
                        activeValue
                );

        VBox surveyCard =
                createStatCard(
                        "Surveys",
                        surveyValue
                );

        VBox closedCard =
                createStatCard(
                        "Closed",
                        closedValue
                );

        statistics.getChildren().addAll(
                activeCard,
                surveyCard,
                closedCard
        );

        // =====================================================
        // SECTION HEADER
        // =====================================================

        Label listTitle =
                new Label(
                        "Polls & Surveys"
                );

        listTitle.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#333333;"
        );

        filterCombo =
                new ComboBox<>();

        filterCombo.getItems().addAll(
                "All",
                "Active",
                "Surveys",
                "Closed"
        );

        filterCombo.setValue(
                "All"
        );

        filterCombo.setPrefWidth(120);
        filterCombo.setPrefHeight(35);

        filterCombo.setOnAction(
                e -> loadPolls()
        );

        Button refreshButton =
                new Button("⟳ Refresh");

        refreshButton.setPrefWidth(110);
        refreshButton.setPrefHeight(35);

        refreshButton.setStyle(
                "-fx-background-color:#56342B;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        refreshButton.setOnAction(
                e -> {

                    loadPolls();

                    updateStatistics(
                            activeValue,
                            surveyValue,
                            closedValue
                    );
                }
        );

        HBox listHeader =
                new HBox(10);

        listHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                listTitle,
                Priority.ALWAYS
        );

        listHeader.getChildren().addAll(
                listTitle,
                filterCombo,
                refreshButton
        );

        // =====================================================
        // SEPARATOR
        // =====================================================

        Separator separator =
                new Separator();

        // =====================================================
        // POLL LIST
        // =====================================================

        pollList =
                new VBox(12);

        pollList.setPadding(
                new Insets(5)
        );

        pollList.setFillWidth(true);

        pollList.setMaxWidth(
                Double.MAX_VALUE
        );

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                pollList
        );

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setMaxHeight(
                Double.MAX_VALUE
        );

        scrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;"
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        // =====================================================
        // FOOTER
        // =====================================================

        Label footer =
                new Label(
                        "Poll and survey data is synchronized with Firestore."
                );

        footer.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#777777;"
        );

        HBox footerBox =
                new HBox();

        footerBox.setAlignment(
                Pos.CENTER
        );

        footerBox.getChildren().add(
                footer
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        main.getChildren().addAll(
                header,
                statistics,
                listHeader,
                separator,
                scrollPane,
                footerBox
        );

        // =====================================================
        // ROOT
        // =====================================================

        HBox mainRoot =
                new HBox();

        mainRoot.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        mainRoot.getChildren().addAll(
                sidebar,
                main
        );

        HBox.setHgrow(
                main,
                Priority.ALWAYS
        );

        StackPane root =
                new StackPane();

        root.getChildren().add(
                mainRoot
        );

        // =====================================================
        // SCENE
        // =====================================================

        pollsScene =
                new Scene(
                        root,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        // =====================================================
        // INITIAL LOAD
        // =====================================================

        loadPolls();

        updateStatistics(
                activeValue,
                surveyValue,
                closedValue
        );

        return pollsScene;
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private VBox createStatCard(
            String title,
            Label value) {

        VBox card =
                new VBox(5);

        card.setPadding(
                new Insets(15)
        );

        card.setPrefWidth(180);
        card.setPrefHeight(80);

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#DDDDDD;" +
                "-fx-border-radius:10;"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#777777;"
        );

        value.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#24456D;"
        );

        card.getChildren().addAll(
                titleLabel,
                value
        );

        return card;
    }

    // =========================================================
    // UPDATE STATISTICS
    // =========================================================

    private void updateStatistics(
            Label activeValue,
            Label surveyValue,
            Label closedValue) {

        try {

            activeValue.setText(
                    String.valueOf(
                            pollController
                                    .getActivePolls()
                                    .size()
                    )
            );

            surveyValue.setText(
                    String.valueOf(
                            pollController
                                    .getSurveys()
                                    .size()
                    )
            );

            closedValue.setText(
                    String.valueOf(
                            pollController
                                    .getHistory()
                                    .size()
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // LOAD POLLS
    // =========================================================

    private void loadPolls() {

        if (pollList == null
                || pollController == null) {

            return;
        }

        pollList.getChildren().clear();

        List<Poll> polls =
                pollController.getAllPolls();

        String filter =
                filterCombo == null
                        ? "All"
                        : filterCombo.getValue();

        if (polls == null
                || polls.isEmpty()) {

            showNoPolls(
                    "No polls or surveys found."
            );

            return;
        }

        int count = 0;

        for (Poll poll : polls) {

            if (poll == null) {
                continue;
            }

            if (!matchesFilter(
                    poll,
                    filter)) {

                continue;
            }

            pollList.getChildren().add(
                    createPollCard(poll)
            );

            count++;
        }

        if (count == 0) {

            showNoPolls(
                    "No records found for this filter."
            );
        }
    }

    // =========================================================
    // FILTER
    // =========================================================

    private boolean matchesFilter(
            Poll poll,
            String filter) {

        if (filter == null
                || filter.equals("All")) {

            return true;
        }

        if (filter.equals("Active")) {

            return poll.getStatus() != null
                    && poll.getStatus()
                    .equalsIgnoreCase("ACTIVE");
        }

        if (filter.equals("Surveys")) {

            return poll.getType() != null
                    && poll.getType()
                    .equalsIgnoreCase("Survey");
        }

        if (filter.equals("Closed")) {

            return poll.getStatus() != null
                    && poll.getStatus()
                    .equalsIgnoreCase("CLOSED");
        }

        return true;
    }

    // =========================================================
    // POLL CARD
    // =========================================================

    private VBox createPollCard(
            Poll poll) {

        VBox card =
                new VBox(10);

        card.setPadding(
                new Insets(15)
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#DDDDDD;" +
                "-fx-border-radius:10;"
        );

        // =====================================================
        // TOP
        // =====================================================

        Label question =
                new Label(
                        safe(poll.getQuestion())
                );

        question.setWrapText(true);

        question.setStyle(
                "-fx-font-size:17px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#18385E;"
        );

        Label type =
                new Label(
                        safe(poll.getType())
                );

        type.setStyle(
                "-fx-background-color:#E8EEF7;" +
                "-fx-text-fill:#24456D;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:5px 10px;" +
                "-fx-background-radius:12;"
        );

        Label status =
                new Label(
                        safe(poll.getStatus())
                );

        if ("ACTIVE".equalsIgnoreCase(
                poll.getStatus())) {

            status.setStyle(
                    "-fx-background-color:#DDF4E5;" +
                    "-fx-text-fill:#218838;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:5px 10px;" +
                    "-fx-background-radius:12;"
            );

        } else {

            status.setStyle(
                    "-fx-background-color:#F8D7DA;" +
                    "-fx-text-fill:#B02A37;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:5px 10px;" +
                    "-fx-background-radius:12;"
            );
        }

        HBox titleRow =
                new HBox(10);

        titleRow.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                question,
                Priority.ALWAYS
        );

        titleRow.getChildren().addAll(
                question,
                type,
                status
        );

        // =====================================================
        // DESCRIPTION
        // =====================================================

        Label description =
                new Label(
                        safe(poll.getDescription())
                );

        description.setWrapText(true);

        description.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#666666;"
        );

        // =====================================================
        // DETAILS
        // =====================================================

        Label date =
                new Label(
                        "Created: "
                                + formatDate(
                                poll.getCreatedDate()
                        )
                );

        Label endDate =
                new Label(
                        "Ends: "
                                + formatDate(
                                poll.getEndDate()
                        )
                );

        Label audience =
                new Label(
                        "Audience: "
                                + safe(
                                poll.getTargetAudience()
                        )
                );

        Label votes =
                new Label(
                        "Total Votes: "
                                + poll.getTotalVotes()
                );

        for (Label label :
                List.of(
                        date,
                        endDate,
                        audience,
                        votes)) {

            label.setStyle(
                    "-fx-font-size:12px;" +
                    "-fx-text-fill:#777777;"
            );
        }

        HBox details =
                new HBox(20);

        details.getChildren().addAll(
                date,
                endDate,
                audience,
                votes
        );

        // =====================================================
        // OPTIONS
        // =====================================================

        VBox optionsBox =
                new VBox(5);

        if (poll.getOptions() != null) {

            for (int i = 0;
                    i < poll.getOptions().size();
                    i++) {

                String option =
                        poll.getOptions().get(i);

                long count = 0;

                if (poll.getVoteCounts() != null
                        && i < poll.getVoteCounts().size()) {

                    count =
                            poll.getVoteCounts()
                                    .get(i);
                }

                Label optionLabel =
                        new Label(
                                "• "
                                        + safe(option)
                                        + "   ("
                                        + count
                                        + ")"
                        );

                optionLabel.setStyle(
                        "-fx-font-size:13px;" +
                        "-fx-text-fill:#444444;"
                );

                optionsBox.getChildren().add(
                        optionLabel
                );
            }
        }

        // =====================================================
        // ACTIONS
        // =====================================================

        Button closeButton =
                new Button("Close Poll");

        closeButton.setPrefWidth(100);
        closeButton.setPrefHeight(32);

        closeButton.setStyle(
                "-fx-background-color:#856404;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:6;"
        );

        Button deleteButton =
                new Button("Delete");

        deleteButton.setPrefWidth(85);
        deleteButton.setPrefHeight(32);

        deleteButton.setStyle(
                "-fx-background-color:#DC3545;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:6;"
        );

        if (!"ACTIVE".equalsIgnoreCase(
                poll.getStatus())) {

            closeButton.setDisable(true);
        }

        closeButton.setOnAction(
                e -> {

                    boolean success =
                            pollController.closePoll(
                                    poll.getPollId()
                            );

                    if (success) {

                        showAlert(
                                Alert.AlertType.INFORMATION,
                                "Poll Closed",
                                "Poll has been closed successfully."
                        );

                        loadPolls();

                    } else {

                        showAlert(
                                Alert.AlertType.ERROR,
                                "Error",
                                "Unable to close poll."
                        );
                    }
                }
        );

        deleteButton.setOnAction(
                e -> {

                    boolean success =
                            pollController.deletePoll(
                                    poll.getPollId()
                            );

                    if (success) {

                        showAlert(
                                Alert.AlertType.INFORMATION,
                                "Deleted",
                                "Poll deleted successfully."
                        );

                        loadPolls();

                    } else {

                        showAlert(
                                Alert.AlertType.ERROR,
                                "Error",
                                "Unable to delete poll."
                        );
                    }
                }
        );

        HBox actions =
                new HBox(8);

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        actions.getChildren().addAll(
                closeButton,
                deleteButton
        );

        card.getChildren().addAll(
                titleRow,
                description,
                details,
                optionsBox,
                actions
        );

        return card;
    }

    // =========================================================
    // CREATE POLL POPUP
    // =========================================================

    private void openCreatePollPopup() {

        if (pollsScene == null) {

            return;
        }

        // =====================================================
        // OVERLAY
        // =====================================================

        StackPane overlay =
                new StackPane();

        overlay.setPickOnBounds(true);

        overlay.setStyle(
                "-fx-background-color:rgba(0,0,0,0.40);"
        );

        // =====================================================
        // POPUP
        // =====================================================

        VBox popup =
                new VBox(9);

        popup.setPadding(
                new Insets(20)
        );

        popup.setPrefWidth(500);
        popup.setMaxWidth(500);

        popup.setPrefHeight(650);
        popup.setMaxHeight(650);

        popup.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:15;" +
                "-fx-border-color:#DDDDDD;" +
                "-fx-border-radius:15;"
        );

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label(
                        "Create New Poll"
                );

        title.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#24456D;"
        );

        Label subtitle =
                new Label(
                        "Create a poll or survey for society residents"
                );

        subtitle.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#777777;"
        );

        // =====================================================
        // TYPE
        // =====================================================

        Label typeLabel =
                createFormLabel(
                        "Poll Type"
                );

        ComboBox<String> typeCombo =
                new ComboBox<>();

        typeCombo.getItems().addAll(
                "Poll",
                "Survey"
        );

        typeCombo.setValue(
                "Poll"
        );

        typeCombo.setMaxWidth(
                Double.MAX_VALUE
        );

        typeCombo.setPrefHeight(35);

        // =====================================================
        // QUESTION
        // =====================================================

        Label questionLabel =
                createFormLabel(
                        "Question"
                );

        TextField questionField =
                new TextField();

        questionField.setPromptText(
                "Enter your question"
        );

        questionField.setPrefHeight(35);

        // =====================================================
        // DESCRIPTION
        // =====================================================

        Label descriptionLabel =
                createFormLabel(
                        "Description"
                );

        TextArea descriptionArea =
                new TextArea();

        descriptionArea.setPromptText(
                "Enter description"
        );

        descriptionArea.setPrefRowCount(2);

        descriptionArea.setWrapText(true);

        // =====================================================
        // OPTION 1
        // =====================================================

        Label option1Label =
                createFormLabel(
                        "Option 1"
                );

        TextField option1Field =
                new TextField();

        option1Field.setPromptText(
                "Example: Yes"
        );

        option1Field.setPrefHeight(35);

        // =====================================================
        // OPTION 2
        // =====================================================

        Label option2Label =
                createFormLabel(
                        "Option 2"
                );

        TextField option2Field =
                new TextField();

        option2Field.setPromptText(
                "Example: No"
        );

        option2Field.setPrefHeight(35);

        // =====================================================
        // OPTION 3
        // =====================================================

        Label option3Label =
                createFormLabel(
                        "Option 3"
                );

        TextField option3Field =
                new TextField();

        option3Field.setPromptText(
                "Optional"
        );

        option3Field.setPrefHeight(35);

        // =====================================================
        // OPTION 4
        // =====================================================

        Label option4Label =
                createFormLabel(
                        "Option 4"
                );

        TextField option4Field =
                new TextField();

        option4Field.setPromptText(
                "Optional"
        );

        option4Field.setPrefHeight(35);

        // =====================================================
        // END DATE
        // =====================================================

        Label endDateLabel =
                createFormLabel(
                        "End Date"
                );

        DatePicker endDatePicker =
                new DatePicker();

        endDatePicker.setValue(
                LocalDate.now().plusDays(7)
        );

        endDatePicker.setPrefHeight(35);

        endDatePicker.setMaxWidth(
                Double.MAX_VALUE
        );

        // =====================================================
        // TARGET AUDIENCE
        // =====================================================

        Label audienceLabel =
                createFormLabel(
                        "Target Audience"
                );

        ComboBox<String> audienceCombo =
                new ComboBox<>();

        audienceCombo.getItems().addAll(
                "All Residents",
                "Owners",
                "Tenants",
                "All Society Members"
        );

        audienceCombo.setValue(
                "All Residents"
        );

        audienceCombo.setPrefHeight(35);

        audienceCombo.setMaxWidth(
                Double.MAX_VALUE
        );

        // =====================================================
        // BUTTONS
        // =====================================================

        Button cancelButton =
                new Button("Cancel");

        cancelButton.setPrefWidth(100);
        cancelButton.setPrefHeight(38);

        cancelButton.setStyle(
                "-fx-background-color:#E5E7EB;" +
                "-fx-text-fill:#333333;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        Button createButton =
                new Button("Create Poll");

        createButton.setPrefWidth(120);
        createButton.setPrefHeight(38);

        createButton.setStyle(
                "-fx-background-color:#56342B;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        HBox buttonBox =
                new HBox(10);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.getChildren().addAll(
                cancelButton,
                createButton
        );

        // =====================================================
        // POPUP CONTENT
        // =====================================================

        popup.getChildren().addAll(

                title,

                subtitle,

                typeLabel,
                typeCombo,

                questionLabel,
                questionField,

                descriptionLabel,
                descriptionArea,

                option1Label,
                option1Field,

                option2Label,
                option2Field,

                option3Label,
                option3Field,

                option4Label,
                option4Field,

                endDateLabel,
                endDatePicker,

                audienceLabel,
                audienceCombo,

                buttonBox
        );

        // =====================================================
        // POPUP SCROLL
        // =====================================================

        ScrollPane popupScroll =
                new ScrollPane();

        popupScroll.setContent(
                popup
        );

        popupScroll.setFitToWidth(true);

        popupScroll.setPrefWidth(520);
        popupScroll.setPrefHeight(680);

        popupScroll.setMaxWidth(520);
        popupScroll.setMaxHeight(680);

        popupScroll.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:transparent;"
        );

        // =====================================================
        // ADD TO OVERLAY
        // =====================================================

        overlay.getChildren().add(
                popupScroll
        );

        StackPane.setAlignment(
                popupScroll,
                Pos.CENTER
        );

        StackPane root =
                (StackPane) pollsScene.getRoot();

        root.getChildren().add(
                overlay
        );

        // =====================================================
        // CANCEL
        // =====================================================

        cancelButton.setOnAction(
                e -> root.getChildren().remove(
                        overlay
                )
        );

        // =====================================================
        // CREATE
        // =====================================================

        createButton.setOnAction(e -> {

            String type =
                    typeCombo.getValue();

            String question =
                    questionField
                            .getText()
                            .trim();

            String description =
                    descriptionArea
                            .getText()
                            .trim();

            String option1 =
                    option1Field
                            .getText()
                            .trim();

            String option2 =
                    option2Field
                            .getText()
                            .trim();

            String option3 =
                    option3Field
                            .getText()
                            .trim();

            String option4 =
                    option4Field
                            .getText()
                            .trim();

            LocalDate endDate =
                    endDatePicker.getValue();

            String audience =
                    audienceCombo.getValue();

            // =================================================
            // VALIDATION
            // =================================================

            if (type == null
                    || question.isEmpty()
                    || option1.isEmpty()
                    || option2.isEmpty()
                    || endDate == null
                    || audience == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Missing Information",
                        "Please fill all required fields."
                );

                return;
            }

            if (endDate.isBefore(
                    LocalDate.now())) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Date",
                        "End date cannot be before today."
                );

                return;
            }

            List<String> options =
                    new ArrayList<>();

            options.add(option1);
            options.add(option2);

            if (!option3.isEmpty()) {

                options.add(option3);
            }

            if (!option4.isEmpty()) {

                options.add(option4);
            }

            // =================================================
            // SAVE
            // =================================================

            boolean saved =
                    pollController.createPoll(
                            question,
                            type,
                            description,
                            endDate.toString(),
                            audience,
                            options
                    );

            if (saved) {

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Poll created and saved to Firestore successfully."
                );

                // CLOSE POPUP
                root.getChildren().remove(
                        overlay
                );

                // REFRESH UI FROM FIRESTORE
                loadPolls();

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Save Failed",
                        "Unable to save poll. Please check logged-in email and Firestore connection."
                );
            }
        });
    }

    // =========================================================
    // FORM LABEL
    // =========================================================

    private Label createFormLabel(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#333333;"
        );

        return label;
    }

    // =========================================================
    // NO POLLS
    // =========================================================

    private void showNoPolls(
            String message) {

        Label label =
                new Label(message);

        label.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#777777;" +
                "-fx-padding:20;"
        );

        pollList.getChildren().add(
                label
        );
    }

    // =========================================================
    // FORMAT DATE
    // =========================================================

    private String formatDate(
            String date) {

        try {

            if (date == null
                    || date.trim().isEmpty()) {

                return "";
            }

            return LocalDate
                    .parse(date)
                    .format(dateFormatter);

        } catch (Exception e) {

            return safe(date);
        }
    }

    // =========================================================
    // SAFE
    // =========================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }

    // =========================================================
    // ALERT
    // =========================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}