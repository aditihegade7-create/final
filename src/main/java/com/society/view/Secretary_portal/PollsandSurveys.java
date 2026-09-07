package com.society.view.Secretary_portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.society.controller.Secretary_Controller.PollController;
import com.society.model.Secretary_model.Poll;
import com.society.view.ScreenSize;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PollsandSurveys {

    private Scene pollsScene;
    private PollController pollController;
    private VBox pollList;
    private ComboBox<String> filterCombo;

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    public Scene createScene(Stage stage) {

        pollController = new PollController();

        // ================= SIDEBAR =================
        SecretarySidebar sidebarObj = new SecretarySidebar();
        VBox sidebar = sidebarObj.createSidebar(stage);

        sidebar.setPrefWidth(280);
        sidebar.setMinWidth(280);
        sidebar.setMaxWidth(280);

        // ================= MAIN CONTENT =================
        VBox main = new VBox(15);
        main.setPadding(new Insets(22, 25, 20, 25));
        main.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        main.setStyle("-fx-background-color:#E8DDD3;");

        // =========================================================
        // TOP GLOBAL HEADER
        // =========================================================
        HBox topHeader = new HBox();

        topHeader.setPrefHeight(80);
        topHeader.setMinHeight(80);
        topHeader.setMaxHeight(80);

        topHeader.setPadding(new Insets(18, 25, 18, 25));
        topHeader.setAlignment(Pos.CENTER_LEFT);

        topHeader.setStyle(
                "-fx-background-color:#593a32;"
        );

        Label topHeaderTitle = new Label("POLLS & SURVEYS");

        topHeaderTitle.setStyle(
                "-fx-font-size:25px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        Region topHeaderSpacer = new Region();
        HBox.setHgrow(topHeaderSpacer, Priority.ALWAYS);

        Label topHeaderText = new Label("Secretary Polls & Surveys");

        topHeaderText.setStyle(
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        topHeader.getChildren().addAll(
                topHeaderTitle,
                topHeaderSpacer,
                topHeaderText
        );

        // =========================================================
        // PAGE HEADER
        // =========================================================
        VBox headingBox = new VBox(4);

        Label title = new Label("Polls & Surveys");

        title.setStyle(
                "-fx-font-size:26px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#24456D;"
        );

        Label subtitle = new Label(
                "Create polls, collect opinions and manage society surveys"
        );

        subtitle.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#666666;"
        );

        headingBox.getChildren().addAll(title, subtitle);

        // =========================================================
        // CREATE NEW POLL BUTTON
        // =========================================================
        Button createButton = new Button("+ Create New Poll");

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

        addPopupAnimation(createButton);

        createButton.setOnAction(e -> openCreatePollPopup());

        HBox pageHeader = new HBox();

        pageHeader.setAlignment(Pos.CENTER_LEFT);
        pageHeader.setSpacing(15);

        HBox.setHgrow(headingBox, Priority.ALWAYS);

        pageHeader.getChildren().addAll(
                headingBox,
                createButton
        );

        // =========================================================
        // STATISTICS
        // =========================================================
        HBox statistics = new HBox(15);

        statistics.setAlignment(Pos.CENTER_LEFT);

        Label activeValue = new Label("0");
        Label surveyValue = new Label("0");
        Label closedValue = new Label("0");

        statistics.getChildren().addAll(
                createStatCard("Active Polls", activeValue),
                createStatCard("Surveys", surveyValue),
                createStatCard("Closed", closedValue)
        );

        // =========================================================
        // LIST HEADER
        // =========================================================
        Label listTitle = new Label("Polls & Surveys");

        listTitle.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#333333;"
        );

        filterCombo = new ComboBox<>();

        filterCombo.getItems().addAll(
                "All",
                "Active",
                "Surveys",
                "Closed"
        );

        filterCombo.setValue("All");

        filterCombo.setPrefWidth(120);
        filterCombo.setPrefHeight(35);

        filterCombo.setOnAction(e -> loadPolls());

        Button refreshButton = new Button("⟳ Refresh");

        refreshButton.setPrefWidth(110);
        refreshButton.setPrefHeight(35);

        refreshButton.setStyle(
                "-fx-background-color:#56342B;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        addPopupAnimation(refreshButton);

        refreshButton.setOnAction(e -> {
            loadPolls();
            updateStatistics(
                    activeValue,
                    surveyValue,
                    closedValue
            );
        });

        HBox listHeader = new HBox(12);

        listHeader.setAlignment(Pos.CENTER_LEFT);

        Region listSpacer = new Region();
        HBox.setHgrow(listSpacer, Priority.ALWAYS);

        listHeader.getChildren().addAll(
                listTitle,
                listSpacer,
                filterCombo,
                refreshButton
        );

        // =========================================================
        // SEPARATOR
        // =========================================================
        Separator separator = new Separator();

        // =========================================================
        // POLL LIST
        // =========================================================
        pollList = new VBox(12);

        pollList.setPadding(new Insets(5));
        pollList.setFillWidth(true);

        // =========================================================
        // SCROLL PANE
        // =========================================================
        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setContent(pollList);

        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setMaxHeight(Double.MAX_VALUE);

        scrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background-insets:0;"
        );

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // =========================================================
        // FOOTER
        // =========================================================
        Label footer = new Label(
                "Society360 • Polls & Surveys Management"
        );

        footer.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#777777;"
        );

        HBox footerBox = new HBox(footer);

        footerBox.setAlignment(Pos.CENTER);

        // =========================================================
        // ADD CONTENT TO MAIN
        // =========================================================
        main.getChildren().addAll(
                pageHeader,
                statistics,
                listHeader,
                separator,
                scrollPane,
                footerBox
        );

        // =========================================================
        // CONTENT WRAPPER
        // =========================================================
        VBox contentWrapper = new VBox();

        contentWrapper.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        contentWrapper.setStyle(
                "-fx-background-color:#E8DDD3;"
        );

        contentWrapper.getChildren().addAll(
                topHeader,
                main
        );

        VBox.setVgrow(main, Priority.ALWAYS);

        // =========================================================
        // ROOT
        // =========================================================
        BorderPane root = new BorderPane();

        root.setLeft(sidebar);
        root.setCenter(contentWrapper);

        root.setStyle(
                "-fx-background-color:#E8DDD3;"
        );

        // =========================================================
        // SCENE
        // =========================================================
        pollsScene = new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );

        // =========================================================
        // INITIAL DATA
        // =========================================================
        loadPolls();

        updateStatistics(
                activeValue,
                surveyValue,
                closedValue
        );

        // =========================================================
        // DOWN → UP ANIMATION
        // =========================================================
        animateContent(pageHeader, 30, 80);
        animateContent(statistics, 40, 160);
        animateContent(listHeader, 45, 240);
        animateContent(separator, 50, 300);
        animateContent(scrollPane, 55, 360);
        animateContent(footerBox, 60, 420);

        return pollsScene;
    }

    // =============================================================
    // STAT CARD
    // =============================================================
    private VBox createStatCard(
            String titleText,
            Label valueLabel
    ) {

        VBox card = new VBox(5);

        card.setPrefWidth(180);
        card.setMinWidth(180);
        card.setMaxWidth(180);

        card.setPrefHeight(80);

        card.setPadding(new Insets(12));

        card.setAlignment(Pos.CENTER_LEFT);

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#DDDDDD;" +
                "-fx-border-radius:10;"
        );

        Label titleLabel = new Label(titleText);

        titleLabel.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#777777;"
        );

        valueLabel.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#24456D;"
        );

        card.getChildren().addAll(
                titleLabel,
                valueLabel
        );

        return card;
    }

    // =============================================================
    // UPDATE STATISTICS
    // =============================================================
    private void updateStatistics(
            Label activeValue,
            Label surveyValue,
            Label closedValue
    ) {

        try {

            int active =
                    pollController.getActivePolls().size();

            int surveys =
                    pollController.getSurveys().size();

            int closed =
                    pollController.getHistory().size();

            activeValue.setText(String.valueOf(active));
            surveyValue.setText(String.valueOf(surveys));
            closedValue.setText(String.valueOf(closed));

        } catch (Exception e) {

            activeValue.setText("0");
            surveyValue.setText("0");
            closedValue.setText("0");
        }
    }

    // =============================================================
    // LOAD POLLS
    // =============================================================
    private void loadPolls() {

        pollList.getChildren().clear();

        try {

            List<Poll> polls =
                    pollController.getAllPolls();

            String selectedFilter =
                    filterCombo.getValue();

            if (selectedFilter == null) {
                selectedFilter = "All";
            }

            List<Poll> filteredPolls =
                    new ArrayList<>();

            for (Poll poll : polls) {

                if (matchesFilter(
                        poll,
                        selectedFilter
                )) {

                    filteredPolls.add(poll);
                }
            }

            if (filteredPolls.isEmpty()) {

                showNoPolls();

            } else {

                for (Poll poll : filteredPolls) {

                    VBox card =
                            createPollCard(poll);

                    pollList.getChildren().add(card);
                }
            }

        } catch (Exception e) {

            showNoPolls();
        }
    }

    // =============================================================
    // FILTER
    // =============================================================
    private boolean matchesFilter(
            Poll poll,
            String filter
    ) {

        if ("All".equals(filter)) {
            return true;
        }

        if ("Active".equals(filter)) {

            return "ACTIVE".equalsIgnoreCase(
                    safe(poll.getStatus())
            );
        }

        if ("Surveys".equals(filter)) {

            return "Survey".equalsIgnoreCase(
                    safe(poll.getType())
            );
        }

        if ("Closed".equals(filter)) {

            return "CLOSED".equalsIgnoreCase(
                    safe(poll.getStatus())
            );
        }

        return true;
    }

    // =============================================================
    // CREATE POLL CARD
    // =============================================================
    private VBox createPollCard(Poll poll) {

        VBox card = new VBox(10);

        card.setPadding(new Insets(15));

        card.setMaxWidth(Double.MAX_VALUE);

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#DDDDDD;" +
                "-fx-border-radius:10;"
        );

        // ---------------------------------------------------------
        // QUESTION
        // ---------------------------------------------------------
        Label question =
                new Label(safe(poll.getQuestion()));

        question.setWrapText(true);

        question.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#24456D;"
        );

        // ---------------------------------------------------------
        // TYPE + STATUS
        // ---------------------------------------------------------
        HBox tags = new HBox(8);

        Label typeLabel =
                new Label(safe(poll.getType()));

        typeLabel.setStyle(
                "-fx-background-color:#E8DDD3;" +
                "-fx-text-fill:#56342B;" +
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:5 10 5 10;" +
                "-fx-background-radius:12;"
        );

        Label statusLabel =
                new Label(safe(poll.getStatus()));

        statusLabel.setStyle(
                "-fx-background-color:#EEE6E2;" +
                "-fx-text-fill:#56342B;" +
                "-fx-font-size:12px;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:5 10 5 10;" +
                "-fx-background-radius:12;"
        );

        tags.getChildren().addAll(
                typeLabel,
                statusLabel
        );

        // ---------------------------------------------------------
        // DESCRIPTION
        // ---------------------------------------------------------
        Label description =
                new Label(safe(poll.getDescription()));

        description.setWrapText(true);

        description.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#666666;"
        );

        // ---------------------------------------------------------
        // DETAILS
        // ---------------------------------------------------------
        HBox details = new HBox(18);

        details.setAlignment(Pos.CENTER_LEFT);

        Label createdLabel = new Label(
                "Created: " +
                formatDate(poll.getCreatedDate())
        );

        Label endLabel = new Label(
                "Ends: " +
                formatDate(poll.getEndDate())
        );

        Label audienceLabel = new Label(
                "Audience: " +
                safe(poll.getTargetAudience())
        );

        Label votesLabel = new Label(
                "Votes: " +
                String.valueOf(poll.getTotalVotes())
        );

        String detailStyle =
                "-fx-font-size:12px;" +
                "-fx-text-fill:#777777;";

        createdLabel.setStyle(detailStyle);
        endLabel.setStyle(detailStyle);
        audienceLabel.setStyle(detailStyle);
        votesLabel.setStyle(detailStyle);

        details.getChildren().addAll(
                createdLabel,
                endLabel,
                audienceLabel,
                votesLabel
        );

        // ---------------------------------------------------------
        // OPTIONS
        // ---------------------------------------------------------
        VBox optionsBox = new VBox(5);

        if (poll.getOptions() != null) {

            for (String option : poll.getOptions()) {

                Label optionLabel =
                        new Label(
                                "• " +
                                safe(option)
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

        // ---------------------------------------------------------
        // ACTION BUTTONS
        // ---------------------------------------------------------
        HBox actionBox = new HBox(10);

        actionBox.setAlignment(Pos.CENTER_RIGHT);

        Button closeButton =
                new Button("Close Poll");

        closeButton.setPrefWidth(105);
        closeButton.setPrefHeight(35);

        closeButton.setStyle(
                "-fx-background-color:#56342B;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        addPopupAnimation(closeButton);

        boolean isActive =
                "ACTIVE".equalsIgnoreCase(
                        safe(poll.getStatus())
                );

        closeButton.setDisable(!isActive);

        closeButton.setOnAction(e -> {

            try {

                pollController.closePoll(
                        poll.getPollId()
                );

                loadPolls();

            } catch (Exception ex) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Unable to close poll."
                );
            }
        });

        Button deleteButton =
                new Button("Delete");

        deleteButton.setPrefWidth(90);
        deleteButton.setPrefHeight(35);

        deleteButton.setStyle(
                "-fx-background-color:#8B4A3A;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        addPopupAnimation(deleteButton);

        deleteButton.setOnAction(e -> {

            try {

                pollController.deletePoll(
                        poll.getPollId()
                );

                loadPolls();

            } catch (Exception ex) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Unable to delete poll."
                );
            }
        });

        actionBox.getChildren().addAll(
                closeButton,
                deleteButton
        );

        card.getChildren().addAll(
                question,
                tags,
                description,
                details,
                optionsBox,
                actionBox
        );

        return card;
    }

    // =============================================================
    // CREATE POLL POPUP
    // =============================================================
    private void openCreatePollPopup() {

        StackPane root =
                (StackPane) pollsScene.getRoot();

        StackPane overlay = new StackPane();

        overlay.setStyle(
                "-fx-background-color:rgba(0,0,0,0.40);"
        );

        VBox popup = new VBox(12);

        popup.setPadding(new Insets(22));

        popup.setPrefWidth(500);
        popup.setPrefHeight(650);

        popup.setMaxWidth(500);
        popup.setMaxHeight(650);

        popup.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:15;" +
                "-fx-border-color:#DDDDDD;" +
                "-fx-border-radius:15;"
        );

        // =========================================================
        // POPUP TITLE
        // =========================================================
        Label popupTitle =
                new Label("Create New Poll");

        popupTitle.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#24456D;"
        );

        Label popupSubtitle =
                new Label(
                        "Create a poll or survey for society members."
                );

        popupSubtitle.setWrapText(true);

        popupSubtitle.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#777777;"
        );

        // =========================================================
        // TYPE
        // =========================================================
        Label typeLabel =
                createFormLabel("Type");

        ComboBox<String> typeCombo =
                new ComboBox<>();

        typeCombo.getItems().addAll(
                "Poll",
                "Survey"
        );

        typeCombo.setValue("Poll");

        typeCombo.setPrefWidth(450);

        // =========================================================
        // QUESTION
        // =========================================================
        Label questionLabel =
                createFormLabel("Question");

        TextField questionField =
                new TextField();

        questionField.setPromptText(
                "Enter your question"
        );

        questionField.setPrefHeight(38);

        // =========================================================
        // DESCRIPTION
        // =========================================================
        Label descriptionLabel =
                createFormLabel("Description");

        TextArea descriptionArea =
                new TextArea();

        descriptionArea.setPromptText(
                "Enter poll description"
        );

        descriptionArea.setPrefRowCount(3);

        descriptionArea.setWrapText(true);

        // =========================================================
        // OPTIONS
        // =========================================================
        Label optionsLabel =
                createFormLabel("Options");

        TextField option1 =
                new TextField();

        option1.setPromptText("Option 1");

        TextField option2 =
                new TextField();

        option2.setPromptText("Option 2");

        TextField option3 =
                new TextField();

        option3.setPromptText("Option 3 (optional)");

        TextField option4 =
                new TextField();

        option4.setPromptText("Option 4 (optional)");

        // =========================================================
        // END DATE
        // =========================================================
        Label endDateLabel =
                createFormLabel("End Date");

        DatePicker endDatePicker =
                new DatePicker(
                        LocalDate.now().plusDays(7)
                );

        endDatePicker.setPrefWidth(450);

        // =========================================================
        // AUDIENCE
        // =========================================================
        Label audienceLabel =
                createFormLabel("Audience");

        ComboBox<String> audienceCombo =
                new ComboBox<>();

        audienceCombo.getItems().addAll(
                "All Residents",
                "Owners",
                "Tenants",
                "All Society Members"
        );

        audienceCombo.setValue(
                "All Society Members"
        );

        audienceCombo.setPrefWidth(450);

        // =========================================================
        // BUTTONS
        // =========================================================
        HBox buttonBox = new HBox(10);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        Button popupCancelButton =
                new Button("Cancel");

        popupCancelButton.setPrefWidth(100);
        popupCancelButton.setPrefHeight(38);

        popupCancelButton.setStyle(
                "-fx-background-color:#DDDDDD;" +
                "-fx-text-fill:#333333;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        addPopupAnimation(popupCancelButton);

        Button popupCreateButton =
                new Button("Create Poll");

        popupCreateButton.setPrefWidth(120);
        popupCreateButton.setPrefHeight(38);

        popupCreateButton.setStyle(
                "-fx-background-color:#56342B;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        addPopupAnimation(popupCreateButton);

        buttonBox.getChildren().addAll(
                popupCancelButton,
                popupCreateButton
        );

        // =========================================================
        // POPUP CONTENT
        // =========================================================
        VBox form = new VBox(10);

        form.getChildren().addAll(
                popupTitle,
                popupSubtitle,

                typeLabel,
                typeCombo,

                questionLabel,
                questionField,

                descriptionLabel,
                descriptionArea,

                optionsLabel,
                option1,
                option2,
                option3,
                option4,

                endDateLabel,
                endDatePicker,

                audienceLabel,
                audienceCombo,

                buttonBox
        );

        ScrollPane popupScroll =
                new ScrollPane(form);

        popupScroll.setFitToWidth(true);

        popupScroll.setPrefWidth(520);
        popupScroll.setPrefHeight(680);

        popupScroll.setStyle(
                "-fx-background-color:transparent;"
        );

        overlay.getChildren().add(
                popupScroll
        );

        StackPane.setAlignment(
                popupScroll,
                Pos.CENTER
        );

        root.getChildren().add(overlay);

        // =========================================================
        // POPUP ANIMATION
        // =========================================================
        popupScroll.setOpacity(0);
        popupScroll.setTranslateY(30);
        popupScroll.setScaleX(0.95);
        popupScroll.setScaleY(0.95);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(250),
                        popupScroll
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition translate =
                new TranslateTransition(
                        Duration.millis(300),
                        popupScroll
                );

        translate.setFromY(30);
        translate.setToY(0);

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(300),
                        popupScroll
                );

        scale.setFromX(0.95);
        scale.setFromY(0.95);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition popupAnimation =
                new ParallelTransition(
                        fade,
                        translate,
                        scale
                );

        popupAnimation.play();

        // =========================================================
        // CANCEL
        // =========================================================
        popupCancelButton.setOnAction(e ->
                root.getChildren().remove(overlay)
        );

        // =========================================================
        // CREATE
        // =========================================================
        popupCreateButton.setOnAction(e -> {

            String type =
                    typeCombo.getValue();

            String question =
                    questionField.getText();

            String description =
                    descriptionArea.getText();

            LocalDate endDate =
                    endDatePicker.getValue();

            String audience =
                    audienceCombo.getValue();

            if (type == null ||
                    type.trim().isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation",
                        "Please select poll type."
                );

                return;
            }

            if (question == null ||
                    question.trim().isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation",
                        "Please enter a question."
                );

                return;
            }

            List<String> options =
                    new ArrayList<>();

            if (!option1.getText().trim().isEmpty()) {

                options.add(
                        option1.getText().trim()
                );
            }

            if (!option2.getText().trim().isEmpty()) {

                options.add(
                        option2.getText().trim()
                );
            }

            if (!option3.getText().trim().isEmpty()) {

                options.add(
                        option3.getText().trim()
                );
            }

            if (!option4.getText().trim().isEmpty()) {

                options.add(
                        option4.getText().trim()
                );
            }

            if (options.size() < 2) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation",
                        "Please enter at least two options."
                );

                return;
            }

            if (endDate == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation",
                        "Please select an end date."
                );

                return;
            }

            if (endDate.isBefore(LocalDate.now())) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation",
                        "End date cannot be before today."
                );

                return;
            }

            if (audience == null ||
                    audience.trim().isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation",
                        "Please select audience."
                );

                return;
            }

            try {

                pollController.createPoll(
                        question.trim(),
                        type,
                        description == null
                                ? ""
                                : description.trim(),
                        endDate.toString(),
                        audience,
                        options
                );

                root.getChildren().remove(
                        overlay
                );

                loadPolls();

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Poll created successfully."
                );

            } catch (Exception ex) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Unable to create poll."
                );
            }
        });
    }

    // =============================================================
    // FORM LABEL
    // =============================================================
    private Label createFormLabel(
            String text
    ) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#444444;"
        );

        return label;
    }

    // =============================================================
    // NO POLLS
    // =============================================================
    private void showNoPolls() {

        VBox box = new VBox(8);

        box.setAlignment(
                Pos.CENTER
        );

        box.setPadding(
                new Insets(50)
        );

        Label icon =
                new Label("📋");

        icon.setStyle(
                "-fx-font-size:35px;"
        );

        Label message =
                new Label(
                        "No polls or surveys found."
                );

        message.setStyle(
                "-fx-font-size:16px;" +
                "-fx-text-fill:#777777;"
        );

        box.getChildren().addAll(
                icon,
                message
        );

        pollList.getChildren().add(box);
    }

    // =============================================================
    // FORMAT DATE
    // =============================================================
    private String formatDate(
            Object date
    ) {

        if (date == null) {
            return "-";
        }

        try {

            if (date instanceof LocalDate) {

                return dateFormatter.format(
                        (LocalDate) date
                );
            }

            String value =
                    date.toString();

            try {

                LocalDate parsed =
                        LocalDate.parse(value);

                return dateFormatter.format(
                        parsed
                );

            } catch (Exception ignored) {

                return value;
            }

        } catch (Exception e) {

            return "-";
        }
    }

    // =============================================================
    // SAFE STRING
    // =============================================================
    private String safe(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }

    // =============================================================
    // ALERT
    // =============================================================
    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert =
                new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // =============================================================
    // DOWN → UP + FADE ANIMATION
    // =============================================================
    private void animateContent(
            javafx.scene.Node node,
            double fromY,
            double delayMillis
    ) {

        node.setOpacity(0);
        node.setTranslateY(fromY);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(500),
                        node
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition translate =
                new TranslateTransition(
                        Duration.millis(500),
                        node
                );

        translate.setFromY(fromY);
        translate.setToY(0);

        ParallelTransition animation =
                new ParallelTransition(
                        fade,
                        translate
                );

        animation.setDelay(
                Duration.millis(delayMillis)
        );

        animation.play();
    }

    // =============================================================
    // BUTTON POPUP / SCALE ANIMATION
    // =============================================================
    private void addPopupAnimation(
            Button button
    ) {

        ScaleTransition scaleUp =
                new ScaleTransition(
                        Duration.millis(120),
                        button
                );

        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);

        ScaleTransition scaleDown =
                new ScaleTransition(
                        Duration.millis(120),
                        button
                );

        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        ScaleTransition press =
                new ScaleTransition(
                        Duration.millis(80),
                        button
                );

        press.setToX(0.97);
        press.setToY(0.97);

        button.setOnMouseEntered(e -> {
            scaleDown.stop();
            scaleUp.playFromStart();
        });

        button.setOnMouseExited(e -> {
            scaleUp.stop();
            scaleDown.playFromStart();
        });

        button.setOnMousePressed(e -> {
            press.stop();
            press.playFromStart();
        });

        button.setOnMouseReleased(e -> {

            if (button.isHover()) {

                scaleUp.playFromStart();

            } else {

                scaleDown.playFromStart();
            }
        });
    }
}