package com.society.view.Resident_portal;

import com.google.cloud.firestore.Firestore;

import com.society.config.FirebaseConfig;
import com.society.dao.Resident_dao.PollDao;
import com.society.model.Resident_model.PollModel;
import com.society.model.Resident_model.PollVoteModel;

import javafx.application.Platform;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PollsSurveys {


    // =====================================================
    // VARIABLES
    // =====================================================

    private final Stage stage;

    private final String residentEmail;

    private final Firestore firestore;

    private final PollDao pollDao;

    private VBox pollContainer;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public PollsSurveys(
            Stage stage,
            String residentEmail
    ) {

        this.stage = stage;

        this.residentEmail =
                residentEmail == null
                        ? ""
                        : residentEmail
                                .trim()
                                .toLowerCase();

        this.firestore =
                FirebaseConfig.getFirestore();

        this.pollDao =
                new PollDao(
                        firestore
                );
    }


    // =====================================================
    // CREATE SCENE
    // =====================================================

    public Scene createScene(Stage stage2, String residentEmail2) {

        BorderPane root =
                new BorderPane();


        root.setStyle(
                "-fx-background-color: #e8ddd5;"
        );


        // =================================================
        // SIDEBAR
        // =================================================

        panel sidebar =
                new panel(stage, residentEmail);


        root.setLeft(
                sidebar.getSidebar()
        );


        // =================================================
        // MAIN
        // =================================================

        BorderPane main =
                new BorderPane();


        main.setTop(
                createHeader()
        );


        main.setCenter(
                createContent()
        );


        root.setCenter(
                main
        );


        // =================================================
        // SCENE
        // =================================================

        Scene scene =
                new Scene(
                        root,
                        1550,
                        850
                );


        // =================================================
        // LOAD FIRESTORE
        // =================================================

        loadActivePolls();


        return scene;
    }


    // =====================================================
    // HEADER
    // =====================================================

    private HBox createHeader() {

        HBox header =
                new HBox();


        header.setPadding(
                new Insets(
                        20,
                        30,
                        20,
                        35
                )
        );


        header.setAlignment(
                Pos.CENTER_LEFT
        );


        header.setStyle(
                "-fx-background-color: #4e342e;"
        );


        VBox titleBox =
                new VBox(3);


        Label title =
                new Label(
                        "Polls & Surveys"
                );


        title.setStyle(
                "-fx-text-fill: white;"
        );


        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        25
                )
        );


        Label subtitle =
                new Label(
                        "Resident Portal"
                );


        subtitle.setStyle(
                "-fx-text-fill: #eadfd9;"
        );


        subtitle.setFont(
                Font.font(
                        "System",
                        13
                )
        );


        titleBox.getChildren().addAll(
                title,
                subtitle
        );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        Label date =
                new Label(
                        java.time.LocalDate
                                .now()
                                .format(
                                        java.time.format
                                                .DateTimeFormatter
                                                .ofPattern(
                                                        "dd MMMM yyyy"
                                                )
                                )
                );


        date.setStyle(
                "-fx-text-fill: white;"
        );


        date.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );


        header.getChildren().addAll(
                titleBox,
                spacer,
                date
        );


        return header;
    }


    // =====================================================
    // CONTENT
    // =====================================================

    private VBox createContent() {

        VBox content =
                new VBox(15);


        content.setPadding(
                new Insets(
                        25,
                        30,
                        20,
                        35
                )
        );


        content.setStyle(
                "-fx-background-color: #e8ddd5;"
        );


        // =================================================
        // HEADING
        // =================================================

        VBox heading =
                new VBox(4);


        Label title =
                new Label(
                        "Community Polls"
                );


        title.setStyle(
                "-fx-text-fill: #263238;"
        );


        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        21
                )
        );


        Label subtitle =
                new Label(
                        "Share your opinion on society matters."
                );


        subtitle.setStyle(
                "-fx-text-fill: #8a7770;"
        );


        subtitle.setFont(
                Font.font(
                        "System",
                        12
                )
        );


        heading.getChildren().addAll(
                title,
                subtitle
        );


        // =================================================
        // TABS
        // =================================================

        HBox tabs =
                new HBox(25);


        Button active =
                createTabButton(
                        "Active Polls",
                        true
                );


        Button past =
                createTabButton(
                        "Past Polls",
                        false
                );


        tabs.getChildren().addAll(
                active,
                past
        );


        active.setOnAction(
                event -> {

                    setActiveTab(
                            active,
                            past
                    );

                    loadActivePolls();
                }
        );


        past.setOnAction(
                event -> {

                    setActiveTab(
                            past,
                            active
                    );

                    loadPastPolls();
                }
        );


        // =================================================
        // POLL CONTAINER
        // =================================================

        pollContainer =
                new VBox(15);


        pollContainer.setPadding(
                new Insets(
                        5,
                        5,
                        15,
                        0
                )
        );


        pollContainer.getChildren().add(
                createLoadingLabel()
        );


        // =================================================
        // SCROLL
        // =================================================

        ScrollPane scroll =
                new ScrollPane(
                        pollContainer
                );


        scroll.setFitToWidth(
                true
        );


        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );


        scroll.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;"
        );


        VBox.setVgrow(
                scroll,
                Priority.ALWAYS
        );


        content.getChildren().addAll(
                heading,
                tabs,
                scroll
        );


        return content;
    }


    // =====================================================
    // TAB BUTTON
    // =====================================================

    private Button createTabButton(
            String text,
            boolean selected
    ) {

        Button button =
                new Button(text);


        button.setPadding(
                new Insets(
                        0,
                        2,
                        6,
                        2
                )
        );


        button.setStyle(
                selected
                        ? activeTabStyle()
                        : inactiveTabStyle()
        );


        return button;
    }


    private String activeTabStyle() {

        return
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #4e342e;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 13px;" +
                "-fx-border-color: #4e342e;" +
                "-fx-border-width: 0 0 2 0;";
    }


    private String inactiveTabStyle() {

        return
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #78909c;" +
                "-fx-font-size: 13px;" +
                "-fx-border-color: transparent;";
    }


    private void setActiveTab(
            Button selected,
            Button other
    ) {

        selected.setStyle(
                activeTabStyle()
        );


        other.setStyle(
                inactiveTabStyle()
        );
    }


    // =====================================================
    // LOAD ACTIVE POLLS
    // =====================================================

    private void loadActivePolls() {

        if (residentEmail.isEmpty()) {

            showError(
                    "Resident email is missing."
            );

            return;
        }


        showLoading();


        Thread thread =
                new Thread(
                        () -> {

                            try {

                                List<PollModel> polls =
                                        pollDao
                                                .getActivePolls();


                                Platform.runLater(
                                        () -> {

                                            displayPolls(
                                                    polls
                                            );
                                        }
                                );


                            } catch (Exception e) {

                                e.printStackTrace();


                                Platform.runLater(
                                        () -> {

                                            showError(
                                                    "Unable to fetch polls from Firestore."
                                            );
                                        }
                                );
                            }
                        }
                );


        thread.setDaemon(true);

        thread.start();
    }


    // =====================================================
    // LOAD PAST POLLS
    // =====================================================

    private void loadPastPolls() {

        if (residentEmail.isEmpty()) {

            showError(
                    "Resident email is missing."
            );

            return;
        }


        showLoading();


        Thread thread =
                new Thread(
                        () -> {

                            try {

                                List<PollModel> polls =
                                        pollDao
                                                .getPastPolls();


                                Platform.runLater(
                                        () -> {

                                            displayPolls(
                                                    polls
                                            );
                                        }
                                );


                            } catch (Exception e) {

                                e.printStackTrace();


                                Platform.runLater(
                                        () -> {

                                            showError(
                                                    "Unable to fetch past polls."
                                            );
                                        }
                                );
                            }
                        }
                );


        thread.setDaemon(true);

        thread.start();
    }


    // =====================================================
    // LOADING
    // =====================================================

    private void showLoading() {

        pollContainer
                .getChildren()
                .clear();


        pollContainer
                .getChildren()
                .add(
                        createLoadingLabel()
                );
    }


    private Label createLoadingLabel() {

        Label label =
                new Label(
                        "Loading polls..."
                );


        label.setStyle(
                "-fx-text-fill: #78909c;"
        );


        label.setFont(
                Font.font(
                        "System",
                        13
                )
        );


        return label;
    }


    // =====================================================
    // DISPLAY POLLS
    // =====================================================

    private void displayPolls(
            List<PollModel> polls
    ) {

        pollContainer
                .getChildren()
                .clear();


        if (polls == null ||
                polls.isEmpty()) {

            VBox empty =
                    new VBox();


            empty.setAlignment(
                    Pos.CENTER
            );


            empty.setPrefHeight(
                    300
            );


            Label label =
                    new Label(
                            "No polls available."
                    );


            label.setStyle(
                    "-fx-text-fill: #78909c;"
            );


            label.setFont(
                    Font.font(
                            "System",
                            14
                    )
            );


            empty.getChildren().add(
                    label
            );


            pollContainer
                    .getChildren()
                    .add(
                            empty
                    );


            return;
        }


        for (PollModel poll :
                polls) {

            loadPollCard(
                    poll
            );
        }
    }


    // =====================================================
    // LOAD POLL CARD
    // =====================================================

    private void loadPollCard(
            PollModel poll
    ) {

        if (poll == null) {
            return;
        }

        // Show the card immediately. Vote state is loaded in the background.
        VBox temporary = new VBox();
        temporary.setPadding(new Insets(20));
        temporary.setStyle(cardStyle());
        temporary.getChildren().add(createLoadingLabel());
        pollContainer.getChildren().add(temporary);

        Thread thread = new Thread(() -> {
            PollVoteModel vote = null;

            try {
                vote = pollDao.getResidentVote(
                        poll.getId(),
                        residentEmail
                );
            } catch (Exception e) {
                System.out.println(
                        "Unable to load resident vote for poll "
                                + poll.getId()
                                + ": "
                                + e.getMessage()
                );
            }

            PollVoteModel finalVote = vote;

            Platform.runLater(() -> {
                int index = pollContainer.getChildren().indexOf(temporary);

                if (index < 0) {
                    return;
                }

                pollContainer.getChildren().set(
                        index,
                        createPollCard(poll, finalVote)
                );
            });
        });

        thread.setDaemon(true);
        thread.start();
    }


    // =====================================================
    // CREATE POLL CARD
    // =====================================================

    private VBox createPollCard(
            PollModel poll,
            PollVoteModel residentVote
    ) {

        VBox card =
                new VBox(12);


        card.setPadding(
                new Insets(
                        20,
                        22,
                        18,
                        22
                )
        );


        card.setStyle(
                cardStyle()
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );


        // =================================================
        // TOP ROW
        // =================================================

        HBox top =
                new HBox();


        top.setAlignment(
                Pos.CENTER_LEFT
        );


        Label status =
                new Label(
                        isPollActive(poll)
                                ? "Active"
                                : "Closed"
                );


        status.setPadding(
                new Insets(
                        5,
                        9,
                        5,
                        9
                )
        );


        status.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        11
                )
        );


        status.setStyle(
                "-fx-background-color: #f1e5df;" +
                "-fx-background-radius: 5;" +
                "-fx-text-fill: #4e342e;"
        );


        Label posted =
                new Label(
                        "Posted on "
                                + safeText(
                                        poll.getCreatedDate()
                                )
                );


        posted.setStyle(
                "-fx-text-fill: #78909c;"
        );


        posted.setFont(
                Font.font(
                        "System",
                        11
                )
        );


        HBox left =
                new HBox(
                        12,
                        status,
                        posted
                );


        left.setAlignment(
                Pos.CENTER_LEFT
        );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        Label ending =
                new Label(
                        isPollActive(poll)
                                ? "Ends on "
                                        + safeText(
                                                poll.getEndDate()
                                        )
                                : "Ended on "
                                        + safeText(
                                                poll.getEndDate()
                                        )
                );


        ending.setStyle(
                "-fx-text-fill: #78909c;"
        );


        ending.setFont(
                Font.font(
                        "System",
                        11
                )
        );


        top.getChildren().addAll(
                left,
                spacer,
                ending
        );


        // =================================================
        // QUESTION
        // =================================================

       Label question =
        new Label(
                safeText(
                        poll.getQuestion()
                )
        );


        question.setWrapText(
                true
        );

        question.setMaxWidth(
                Double.MAX_VALUE
        );


        question.setStyle(
                "-fx-text-fill: #263238;"
        );


        question.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );


        // =================================================
        // INSTRUCTION
        // =================================================

        boolean activePoll =
                isPollActive(poll);

        Label instruction =
                new Label(
                        activePoll
                                ? (residentVote == null
                                        ? "Choose one option"
                                        : "Your response has been recorded")
                                : "Poll results"
                );


        instruction.setStyle(
                "-fx-text-fill: #78909c;"
        );


        instruction.setFont(
                Font.font(
                        "System",
                        12
                )
        );


        card.getChildren().addAll(
                top,
                question,
                instruction
        );


        // =================================================
        // OPTIONS
        // =================================================

        List<String> options =
                poll.getOptions();


        if (options == null) {

            options =
                    new ArrayList<>();
        }


        ToggleGroup group =
                new ToggleGroup();


        for (String option :
                options) {

            if (option == null ||
                    option.trim().isEmpty()) {

                continue;
            }


            HBox row =
                    createOptionRow(
                            poll,
                            option,
                            residentVote,
                            group,
                            isPollActive(poll)
                    );


            card.getChildren().add(
                    row
            );
        }


        // =================================================
        // BOTTOM
        // =================================================

        HBox bottom =
                new HBox(10);

        bottom.setAlignment(
                Pos.CENTER_LEFT
        );


        // =================================================
        // ENDED POLL
        // =================================================

        if (!isPollActive(poll)) {

            Label total =
                    new Label(
                            "♧  "
                                    + poll.getTotalVotes()
                                    + " Total Votes"
                    );

            total.setStyle(
                    "-fx-text-fill: #78909c;"
            );

            total.setFont(
                    Font.font(
                            "System",
                            11
                    )
            );

            bottom.getChildren().add(
                    total
            );

        }

        // =================================================
        // ACTIVE POLL - USER HAS ALREADY VOTED
        // =================================================

        else if (residentVote != null) {

            Region bottomSpacer =
                    new Region();

            HBox.setHgrow(
                    bottomSpacer,
                    Priority.ALWAYS
            );


            Label voted =
                    new Label(
                            "Your response is recorded  ✓"
                    );

            voted.setPadding(
                    new Insets(
                            7,
                            13,
                            7,
                            13
                    )
            );

            voted.setFont(
                    Font.font(
                            "System",
                            FontWeight.BOLD,
                            11
                    )
            );

            voted.setStyle(
                    "-fx-background-color: #f1e8e4;" +
                    "-fx-background-radius: 8;" +
                    "-fx-text-fill: #4e342e;"
            );


            bottom.getChildren().addAll(
                    bottomSpacer,
                    voted
            );

        }

        // =================================================
        // ACTIVE POLL - NOT YET VOTED
        // =================================================

        else {

            Region bottomSpacer =
                    new Region();

            HBox.setHgrow(
                    bottomSpacer,
                    Priority.ALWAYS
            );


            Button vote =
                    new Button(
                            "Vote"
                    );

            vote.setPrefWidth(
                    88
            );

            vote.setPrefHeight(
                    34
            );

            vote.setStyle(
                    "-fx-background-color: #4e342e;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
            );


            vote.setOnAction(
                    event -> {

                        Toggle selected =
                                group.getSelectedToggle();


                        if (selected == null) {

                            showWarning(
                                    "Please select an option."
                            );

                            return;
                        }


                        RadioButton radio =
                                (RadioButton) selected;


                        Object optionData =
                                radio.getUserData();


                        String selectedOption =
                                optionData == null
                                        ? ""
                                        : optionData
                                                .toString()
                                                .trim();


                        System.out.println(
                                "=========================================="
                        );

                        System.out.println(
                                "VOTE BUTTON CLICKED"
                        );

                        System.out.println(
                                "Poll ID: "
                                        + poll.getId()
                        );

                        System.out.println(
                                "Resident: "
                                        + residentEmail
                        );

                        System.out.println(
                                "Selected option: "
                                        + selectedOption
                        );

                        System.out.println(
                                "=========================================="
                        );


                        if (selectedOption.isEmpty()) {

                            showWarning(
                                    "Please select an option."
                            );

                            return;
                        }


                        submitVote(
                                poll,
                                selectedOption
                        );
                    }
            );


            bottom.getChildren().addAll(
                    bottomSpacer,
                    vote
            );
        }


        card.getChildren().add(
                bottom
        );


        return card;
    }


    // =====================================================
    // OPTION ROW
    // =====================================================

    private HBox createOptionRow(
            PollModel poll,
            String option,
            PollVoteModel residentVote,
            ToggleGroup group,
            boolean active
    ) {

        /*
         * ACTIVE POLL
         * ------------
         * Show only the selectable option.
         * No percentages, vote counts, or progress bars.
         *
         * ENDED POLL
         * -----------
         * Show the result percentage and progress bar.
         */

        VBox content =
                new VBox(6);

        HBox top =
                new HBox(10);

        top.setAlignment(
                Pos.CENTER_LEFT
        );

        top.setPadding(
                new Insets(
                        8,
                        12,
                        8,
                        12
                )
        );

        top.setMaxWidth(
                Double.MAX_VALUE
        );

        RadioButton radio =
                new RadioButton();

        /*
         * The option text is stored on the radio button.
         * This is important because the visible option text
         * is rendered separately by the Label.
         */
        radio.setUserData(
                option
        );

        radio.setToggleGroup(
                group
        );

        radio.setDisable(
                !active ||
                residentVote != null
        );

        radio.setStyle(
                "-fx-accent: #5d4037;"
        );


        // =================================================
        // SHOW RESIDENT'S VOTE
        // =================================================

        if (residentVote != null &&
                residentVote.getSelectedOption() != null &&
                residentVote.getSelectedOption()
                        .equalsIgnoreCase(
                                option
                        )) {

            radio.setSelected(
                    true
            );
        }


        Label name =
                new Label(
                        option
                );

        name.setStyle(
                "-fx-text-fill: #3e302b;"
        );

        name.setMinWidth(
                120
        );

        name.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12
                )
        );

        /*
         * Let the resident click the option text itself.
         */
        name.setOnMouseClicked(
                event -> {

                    if (active &&
                            residentVote == null) {

                        radio.setSelected(
                                true
                        );
                    }
                }
        );


        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        top.setStyle(
                "-fx-background-color: #faf7f5;" +
                "-fx-background-radius: 8;"
        );


        // =================================================
        // ACTIVE POLL
        // =================================================

        if (active) {

            /*
             * IMPORTANT:
             * No percentage.
             * No vote count.
             * No progress bar.
             */
            top.getChildren().addAll(
                    radio,
                    name
            );

            HBox.setHgrow(
                    name,
                    Priority.ALWAYS
            );

            content.getChildren().add(
                    top
            );

        }

        // =================================================
        // ENDED POLL - SHOW RESULTS
        // =================================================

        else {

            long optionVotes =
                    getOptionVotes(
                            poll,
                            option
                    );

            long totalVotes =
                    poll.getTotalVotes();

            int percentage =
                    calculatePercentage(
                            optionVotes,
                            totalVotes
                    );


            Label percentageLabel =
                    new Label(
                            percentage
                                    + "% ("
                                    + optionVotes
                                    + " votes)"
                    );

            percentageLabel.setStyle(
                    "-fx-text-fill: #8d6e63;"
            );

            percentageLabel.setFont(
                    Font.font(
                            "System",
                            11
                    )
            );


            top.getChildren().addAll(
                    name,
                    spacer,
                    percentageLabel
            );


            // ---------------------------------------------
            // RESULT BAR
            // ---------------------------------------------

            StackPane background =
                    new StackPane();

            background.setPrefHeight(
                    4
            );

            background.setMaxHeight(
                    4
            );

            background.setStyle(
                    "-fx-background-color: #eee8e4;" +
                    "-fx-background-radius: 4;"
            );


            Region progress =
                    new Region();

            progress.setPrefHeight(
                    4
            );

            progress.setMaxHeight(
                    4
            );

            progress.setStyle(
                    "-fx-background-color: #8d6e63;" +
                    "-fx-background-radius: 4;"
            );


            background.getChildren().add(
                    progress
            );


            StackPane.setAlignment(
                    progress,
                    Pos.CENTER_LEFT
            );


            final double ratio =
                    percentage / 100.0;


            progress.prefWidthProperty().bind(
                    background.widthProperty()
                            .multiply(
                                    ratio
                            )
            );


            content.getChildren().addAll(
                    top,
                    background
            );
        }


        HBox row =
                new HBox(
                        content
                );

        HBox.setHgrow(
                content,
                Priority.ALWAYS
        );

        return row;
    }


    // =====================================================
    // GET OPTION VOTES
    // =====================================================

    private long getOptionVotes(
            PollModel poll,
            String option
    ) {

        Map<String, Long> counts =
                poll.getVoteCounts();


        if (counts == null) {
            return 0;
        }


        if (counts.containsKey(option)) {

            Long value =
                    counts.get(option);


            return value == null
                    ? 0
                    : value;
        }


        for (Map.Entry<String, Long> entry :
                counts.entrySet()) {

            if (entry.getKey() != null &&
                    entry.getKey()
                            .equalsIgnoreCase(
                                    option
                            )) {

                return entry.getValue() == null
                        ? 0
                        : entry.getValue();
            }
        }


        return 0;
    }


    // =====================================================
    // CALCULATE PERCENTAGE
    // =====================================================

    private int calculatePercentage(
            long optionVotes,
            long totalVotes
    ) {

        if (totalVotes <= 0) {

            return 0;
        }


        return (int)
                Math.round(
                        optionVotes * 100.0
                                / totalVotes
                );
    }


    // =====================================================
    // CHECK ACTIVE
    // =====================================================

    private boolean isPollActive(
            PollModel poll
    ) {

        if (poll == null) {
            return false;
        }

        String status = poll.getStatus();

        if (status != null &&
                !status.trim().isEmpty() &&
                !"ACTIVE".equalsIgnoreCase(status.trim())) {
            return false;
        }

        String endDate = poll.getEndDate();

        if (endDate == null ||
                endDate.trim().isEmpty()) {
            return "ACTIVE".equalsIgnoreCase(safeText(status));
        }

        try {
            java.time.LocalDate parsedEndDate =
                    java.time.LocalDate.parse(endDate.trim());

            return !java.time.LocalDate.now()
                    .isAfter(parsedEndDate);

        } catch (Exception e) {
            return false;
        }
    }


    // =====================================================
    // SUBMIT VOTE
    // =====================================================

    private void submitVote(
            PollModel poll,
            String selectedOption
    ) {

        String secretaryEmail =
                poll.getCreatedByEmail();


        if (secretaryEmail == null ||
                secretaryEmail.trim().isEmpty()) {

            showError(
                    "Secretary email is missing."
            );

            return;
        }


        Thread thread =
                new Thread(
                        () -> {

                            try {

                                pollDao.submitVote(
                                        secretaryEmail,
                                        poll.getId(),
                                        residentEmail,
                                        selectedOption
                                );


                                Platform.runLater(
                                        () -> {

                                            showSuccess(
                                                    "Your vote has been recorded."
                                            );


                                            loadActivePolls();
                                        }
                                );


                            } catch (Exception e) {

                                e.printStackTrace();


                                Platform.runLater(
                                        () -> {

                                            String message =
                                                    e.getMessage();


                                            if (message == null ||
                                                    message.trim()
                                                            .isEmpty()) {

                                                message =
                                                        "Unable to submit your vote.";
                                            }


                                            showError(
                                                    message
                                            );
                                        }
                                );
                            }
                        }
                );


        thread.setDaemon(true);

        thread.start();
    }


    // =====================================================
    // CARD STYLE
    // =====================================================

    private String cardStyle() {

        return
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #dfd5cf;" +
                "-fx-border-radius: 10;";
    }


    // =====================================================
    // SAFE TEXT
    // =====================================================

    private String safeText(
            String value
    ) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "-";
        }


        return value;
    }


    // =====================================================
    // WARNING
    // =====================================================

    private void showWarning(
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );


        alert.setTitle(
                "Society360"
        );


        alert.setHeaderText(
                null
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }


    // =====================================================
    // ERROR
    // =====================================================

    private void showError(
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );


        alert.setTitle(
                "Society360"
        );


        alert.setHeaderText(
                "Polls & Surveys"
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }


    // =====================================================
    // SUCCESS
    // =====================================================

    private void showSuccess(
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setTitle(
                "Society360"
        );


        alert.setHeaderText(
                "Vote Submitted"
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }

}