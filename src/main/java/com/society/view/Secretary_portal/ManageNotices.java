package com.society.view.Secretary_portal;

import java.util.ArrayList;
import java.util.List;

import com.society.controller.Secretary_Controller.NoticeController;
import com.society.model.Secretary_model.Notice;
import com.society.model.Welcome.User;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
import javafx.stage.Stage;

public class ManageNotices {

    // ============================================================
    // SCENE
    // ============================================================

    private Scene manageNoticesScene;

    // ============================================================
    // LOGGED-IN USER
    // ============================================================

    private User loggedInUser;

    // ============================================================
    // CONTROLLER
    // ============================================================

    private NoticeController noticeController;

    // ============================================================
    // ROOT STACK
    // ============================================================

    private StackPane rootStack;

    // ============================================================
    // NOTICE LIST
    // ============================================================

    private VBox noticeList;

    // ============================================================
    // CURRENT NOTICES
    // ============================================================

    private List<Notice> currentNotices =
            new ArrayList<>();

    // ============================================================
    // SEARCH FIELD
    // ============================================================

    private TextField searchField;

    // ============================================================
    // COLORS
    // ============================================================

    private static final String HEADER_COLOR =
            "#4e342e";

    private static final String PAGE_BACKGROUND =
            "#F7F7F7";

    // ============================================================
    // BACKGROUND IMAGE
    // ============================================================

    private static final String BACKGROUND_IMAGE =
            "/background-Dashboard5.jpeg";

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public ManageNotices(User loggedInUser) {

        this.loggedInUser = loggedInUser;

        System.out.println(
                "======================================"
        );

        System.out.println(
                "ManageNotices opened"
        );

        if (loggedInUser != null) {

            System.out.println(
                    "Logged-in Email: "
                            + loggedInUser.getEmail()
            );
        }

        System.out.println(
                "======================================"
        );
    }

    // ============================================================
    // GET EMAIL
    // ============================================================

    private String getUserEmail() {

        if (loggedInUser == null) {
            return null;
        }

        if (loggedInUser.getEmail() == null) {
            return null;
        }

        String email =
                loggedInUser.getEmail()
                        .trim()
                        .toLowerCase();

        if (email.isEmpty()) {
            return null;
        }

        return email;
    }

    // ============================================================
    // CREATE SCENE
    // ============================================================

    public Scene createScene(Stage stage) {

        // ========================================================
        // CHECK USER
        // ========================================================

        if (loggedInUser == null) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "User Error",
                    "Logged-in user information is missing."
            );

            return null;
        }

        if (getUserEmail() == null) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Email Error",
                    "Logged-in user's email is not available."
            );

            return null;
        }

        System.out.println(
                "ManageNotices Email: "
                        + getUserEmail()
        );

        // ========================================================
        // CONTROLLER
        // ========================================================

        noticeController =
                new NoticeController();

        // ========================================================
        // ROOT STACK
        // ========================================================

        rootStack =
                new StackPane();

        // ========================================================
        // SIDEBAR
        // DO NOT CHANGE
        // ========================================================

        SecretarySidebar sidebarObj =
                new SecretarySidebar(loggedInUser);

        VBox sidebar =
                sidebarObj.createSidebar(stage);

        // ========================================================
        // MAIN CONTENT
        // ========================================================

        VBox mainvb =
                new VBox(0);

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

        HBox.setHgrow(
                mainvb,
                Priority.ALWAYS
        );

        // ========================================================
        // HEADER TITLE
        // ========================================================

        Label title =
                new Label(
                        "Manage Notices"
                );

        title.setStyle( "-fx-font-family:'Bodoni MT';" +
                "-fx-font-size:20px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:white;"
        );

        Label subtitle =
                new Label(
                        "Create, edit and manage society notices"
                );

        subtitle.setStyle( "-fx-font-family:'Bodoni MT';" +
                "-fx-font-size:13px;"
                        + "-fx-text-fill:white;"
        );

        VBox headingBox =
                new VBox(4);

        headingBox.setAlignment(
                Pos.CENTER_LEFT
        );

        headingBox.getChildren().addAll(
                title,
                subtitle
        );

        // ========================================================
        // REFRESH BUTTON
        // ========================================================

        Button refreshBtn =
                new Button(
                        "↻ Refresh"
                );

        refreshBtn.setPrefWidth(105);

        refreshBtn.setPrefHeight(40);

        refreshBtn.setMinWidth(105);

        refreshBtn.setMinHeight(40);

        refreshBtn.setMaxWidth(105);

        refreshBtn.setMaxHeight(40);

        refreshBtn.setStyle(
                "-fx-background-color:white;"
                        + "-fx-text-fill:"
                        + HEADER_COLOR
                        + ";"
                        + "-fx-font-size:14px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:7;"
                        + "-fx-cursor:hand;"
        );

        // ========================================================
        // HEADER
        // ========================================================

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

        header.setMaxWidth(
                Double.MAX_VALUE
        );

        header.setStyle(
                "-fx-background-color:"
                        + HEADER_COLOR
                        + ";"
        );

        HBox.setHgrow(
                headingBox,
                Priority.ALWAYS
        );

        header.getChildren().addAll(
                headingBox,
                refreshBtn
        );

        // ========================================================
        // CONTENT
        // ========================================================

        VBox content =
                new VBox(15);

        content.setPadding(
                new Insets(
                        20,
                        25,
                        20,
                        25
                )
        );

        content.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        // ========================================================
        // BACKGROUND IMAGE
        // ========================================================

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

                content.setBackground(
                        new Background(
                                bgImage
                        )
                );

            } else {

                System.err.println(
                        "Background image not found: "
                                + BACKGROUND_IMAGE
                );

                content.setStyle(
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

            content.setStyle(
                    "-fx-background-color:"
                            + PAGE_BACKGROUND
                            + ";"
            );
        }

        // ========================================================
        // PAGE HEADING
        // ========================================================

        Label pageHeading =
                new Label(
                        "MANAGE NOTICES"
                );

        pageHeading.setStyle(
                "-fx-font-size:18px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#434141;"
        );

        // ========================================================
        // SEARCH + ADD NOTICE
        // ========================================================

        HBox searchBox =
                new HBox(10);

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        searchBox.setMaxWidth(
                Double.MAX_VALUE
        );

        // ========================================================
        // SEARCH FIELD
        // ========================================================

        searchField =
                new TextField();

        searchField.setPromptText(
                "Search notice..."
        );

        searchField.setPrefHeight(
                45
        );

        searchField.setPrefWidth(
                650
        );

        searchField.setMaxWidth(
                650
        );

        searchField.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:8;"
                        + "-fx-border-color:#E1E5E8;"
                        + "-fx-border-radius:8;"
                        + "-fx-font-size:14px;"
                        + "-fx-padding:0 15 0 15;"
        );

        // ========================================================
        // ADD NOTICE BUTTON
        // RIGHT SIDE OF SEARCH BAR
        // ========================================================

        Button addNoticeBtn =
                new Button(
                        "+ Add Notice"
                );

        addNoticeBtn.setPrefWidth(
                150
        );

        addNoticeBtn.setPrefHeight(
                45
        );

        addNoticeBtn.setStyle(
                "-fx-background-color:"
                        + HEADER_COLOR
                        + ";"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-font-size:13px;"
                        + "-fx-background-radius:8;"
                        + "-fx-cursor:hand;"
        );

        // ========================================================
        // SEARCH LISTENER
        // ========================================================

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    filterNotices(
                            newValue
                    );
                }
        );

        // ========================================================
        // SEARCH BOX CONTENT
        // ========================================================

        searchBox.getChildren().addAll(
                searchField,
                addNoticeBtn
        );

        // ========================================================
        // NOTICE LIST
        // ========================================================

        noticeList =
                new VBox(15);

        noticeList.setPadding(
                new Insets(5)
        );

        noticeList.setFillWidth(
                true
        );

        noticeList.setMaxWidth(
                Double.MAX_VALUE
        );

        // ========================================================
        // NOTICE SCROLL PANE
        // ========================================================

        ScrollPane noticeScrollPane =
                new ScrollPane();

        noticeScrollPane.setContent(
                noticeList
        );

        noticeScrollPane.setFitToWidth(
                true
        );

        noticeScrollPane.setFitToHeight(
                false
        );

        // ========================================================
        // INVISIBLE SCROLLBARS
        // ========================================================

        noticeScrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        noticeScrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        noticeScrollPane.setStyle(
                "-fx-background-color:transparent;"
                        + "-fx-background:transparent;"
                        + "-fx-border-color:transparent;"
        );

        VBox.setVgrow(
                noticeScrollPane,
                Priority.ALWAYS
        );

        // ========================================================
        // VIEW ALL BUTTON
        // ========================================================

        Button viewAllBtn =
                new Button(
                        "View All Notices"
                );

        viewAllBtn.setMaxWidth(
                Double.MAX_VALUE
        );

        viewAllBtn.setPrefHeight(
                40
        );

        viewAllBtn.setStyle(
                "-fx-background-color:"
                        + HEADER_COLOR
                        + ";"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:7;"
                        + "-fx-border-color:#EEEEEE;"
                        + "-fx-border-radius:7;"
                        + "-fx-cursor:hand;"
        );

        // ========================================================
        // CONTENT
        // ========================================================

        content.getChildren().addAll(
                pageHeading,
                searchBox,
                noticeScrollPane,
                viewAllBtn
        );

        // ========================================================
        // MAIN
        // ========================================================

        mainvb.getChildren().addAll(
                header,
                content
        );

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        // ========================================================
        // PAGE ROOT
        // ========================================================

        HBox pageRoot =
                new HBox();

        pageRoot.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        pageRoot.setStyle(
                "-fx-background-color:"
                        + HEADER_COLOR
                        + ";"
        );

        pageRoot.getChildren().addAll(
                sidebar,
                mainvb
        );

        HBox.setHgrow(
                mainvb,
                Priority.ALWAYS
        );

        // ========================================================
        // ROOT STACK
        // ========================================================

        rootStack.getChildren().add(
                pageRoot
        );

        // ========================================================
        // LOAD NOTICES
        // ========================================================

        loadNotices();

        // ========================================================
        // ADD NOTICE ACTION
        // ========================================================

        addNoticeBtn.setOnAction(
                e -> openAddNoticePopup()
        );

        // ========================================================
        // REFRESH ACTION
        // ========================================================

        refreshBtn.setOnAction(
                e -> {

                    if (searchField != null) {
                        searchField.clear();
                    }

                    System.out.println(
                            "Refreshing notices..."
                    );

                    loadNotices();
                }
        );

        // ========================================================
        // VIEW ALL ACTION
        // ========================================================

        viewAllBtn.setOnAction(
                e -> openAllNoticesPopup()
        );

        // ========================================================
        // SCENE
        // ========================================================

        manageNoticesScene =
                new Scene(
                        rootStack,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        return manageNoticesScene;
    }

    // ============================================================
    // LOAD NOTICES
    // EMAIL BASED FETCH
    // ============================================================

    private void loadNotices() {

        if (noticeList == null) {
            return;
        }

        noticeList
                .getChildren()
                .clear();

        currentNotices.clear();

        String email =
                getUserEmail();

        if (email == null) {

            Label errorLabel =
                    new Label(
                            "User email is not available."
                    );

            errorLabel.setStyle(
                    "-fx-font-size:15px;"
                            + "-fx-text-fill:#D9534F;"
            );

            noticeList.getChildren().add(
                    errorLabel
            );

            return;
        }

        try {

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "Fetching notices using email:"
            );

            System.out.println(
                    email
            );

            // ====================================================
            // FETCH USING EMAIL
            // ====================================================

            List<Notice> notices =
                    noticeController
                            .getNoticesBySenderEmail(
                                    email
                            );

            // ====================================================
            // NO DATA
            // ====================================================

            if (notices == null
                    || notices.isEmpty()) {

                Label emptyLabel =
                        new Label(
                                "No notices found."
                        );

                emptyLabel.setStyle(
                        "-fx-font-size:16px;"
                                + "-fx-text-fill:#555555;"
                );

                noticeList.getChildren().add(
                        emptyLabel
                );

                System.out.println(
                        "No notices found for: "
                                + email
                );

                return;
            }

            // ====================================================
            // STORE DATA FOR SEARCH
            // ====================================================

            currentNotices.addAll(
                    notices
            );

            // ====================================================
            // DISPLAY DATA
            // ====================================================

            System.out.println(
                    "Notices fetched: "
                            + notices.size()
            );

            displayNotices(
                    currentNotices
            );

            System.out.println(
                    "Notices displayed successfully."
            );

            System.out.println(
                    "======================================"
            );

        } catch (Exception e) {

            e.printStackTrace();

            Label errorLabel =
                    new Label(
                            "Unable to load notices."
                    );

            errorLabel.setStyle(
                    "-fx-font-size:15px;"
                            + "-fx-text-fill:#D9534F;"
            );

            noticeList.getChildren().add(
                    errorLabel
            );
        }
    }

    // ============================================================
    // FILTER NOTICES
    // ============================================================

    private void filterNotices(
            String searchText) {

        if (noticeList == null) {
            return;
        }

        String query =
                searchText == null
                        ? ""
                        : searchText
                                .trim()
                                .toLowerCase();

        List<Notice> filtered =
                new ArrayList<>();

        for (Notice notice :
                currentNotices) {

            if (notice == null) {
                continue;
            }

            String title =
                    safeText(
                            notice.getTitle()
                    ).toLowerCase();

            String description =
                    safeText(
                            notice.getDescription()
                    ).toLowerCase();

            String date =
                    safeText(
                            notice.getDate()
                    ).toLowerCase();

            String status =
                    safeText(
                            notice.getStatus()
                    ).toLowerCase();

            boolean matches =
                    query.isEmpty()
                            || title.contains(query)
                            || description.contains(query)
                            || date.contains(query)
                            || status.contains(query);

            if (matches) {

                filtered.add(
                        notice
                );
            }
        }

        displayNotices(
                filtered
        );
    }

    // ============================================================
    // DISPLAY NOTICES
    // ============================================================

    private void displayNotices(
            List<Notice> notices) {

        noticeList
                .getChildren()
                .clear();

        if (notices == null
                || notices.isEmpty()) {

            Label noNotice =
                    new Label(
                            "No matching notices found."
                    );

            noNotice.setStyle(
                    "-fx-font-size:16px;"
                            + "-fx-text-fill:#555555;"
            );

            noticeList.getChildren().add(
                    noNotice
            );

            return;
        }

        for (Notice notice :
                notices) {

            if (notice == null) {
                continue;
            }

            VBox noticeBox =
                    createNotice(
                            notice.getTitle(),
                            notice.getDescription(),
                            notice.getDate(),
                            notice.getStatus()
                    );

            noticeList.getChildren().add(
                    noticeBox
            );
        }
    }

    // ============================================================
    // CREATE NOTICE CARD
    // ============================================================

    private VBox createNotice(
            String noticeTitleText,
            String noticeDescriptionText,
            String noticeDateText,
            String statusText
    ) {

        VBox notice =
                new VBox(8);

        notice.setPadding(
                new Insets(20)
        );

        notice.setMaxWidth(
                Double.MAX_VALUE
        );

        notice.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:8;"
                        + "-fx-border-color:#EEEEEE;"
                        + "-fx-border-radius:8;"
        );

        // ========================================================
        // TITLE
        // ========================================================

        Label noticeTitle =
                new Label(
                        "▣  "
                                + safeText(
                                        noticeTitleText
                                )
                );

        noticeTitle.setWrapText(
                true
        );

        noticeTitle.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#123C36;"
        );

        // ========================================================
        // DESCRIPTION
        // ========================================================

        Label noticeText =
                new Label(
                        safeText(
                                noticeDescriptionText
                        )
                );

        noticeText.setWrapText(
                true
        );

        noticeText.setStyle(
                "-fx-font-size:11px;"
                        + "-fx-text-fill:#777777;"
        );

        // ========================================================
        // DATE
        // ========================================================

        Label noticeDate =
                new Label(
                        safeText(
                                noticeDateText
                        )
                );

        noticeDate.setStyle(
                "-fx-font-size:10px;"
                        + "-fx-text-fill:#777777;"
        );

        // ========================================================
        // STATUS
        // ========================================================

        Label status =
                new Label(
                        safeText(
                                statusText
                        )
                );

        status.setStyle(
                "-fx-background-color:"
                        + HEADER_COLOR
                        + ";"
                        + "-fx-text-fill:white;"
                        + "-fx-font-size:10px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-padding:5px 9px;"
                        + "-fx-background-radius:12;"
        );

        // ========================================================
        // BOTTOM
        // ========================================================

        HBox bottom =
                new HBox(10);

        bottom.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                noticeDate,
                Priority.ALWAYS
        );

        bottom.getChildren().addAll(
                noticeDate,
                status
        );

        // ========================================================
        // ADD
        // ========================================================

        notice.getChildren().addAll(
                noticeTitle,
                noticeText,
                bottom
        );

        return notice;
    }

    // ============================================================
    // ADD NOTICE POPUP
    // ============================================================

    private void openAddNoticePopup() {

        // ========================================================
        // EMAIL CHECK
        // ========================================================

        String email =
                getUserEmail();

        if (email == null) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Email Error",
                    "Logged-in user's email is not available."
            );

            return;
        }

        // ========================================================
        // OVERLAY
        // ========================================================

        StackPane overlay =
                new StackPane();

        overlay.setStyle(
                "-fx-background-color:"
                        + "rgba(0,0,0,0.35);"
        );

        // ========================================================
        // POPUP
        // ========================================================

        VBox popup =
                new VBox(10);

        popup.setPadding(
                new Insets(20)
        );

        popup.setPrefWidth(
                450
        );

        popup.setPrefHeight(
                430
        );

        popup.setMaxWidth(
                450
        );

        popup.setMaxHeight(
                430
        );

        popup.setMinWidth(
                450
        );

        popup.setMinHeight(
                430
        );

        popup.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:15;"
        );

        // ========================================================
        // POPUP TITLE
        // ========================================================

        Label popupTitle =
                new Label(
                        "Add New Notice"
                );

        popupTitle.setStyle(
                "-fx-font-size:22px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:"
                        + HEADER_COLOR
                        + ";"
        );

        // ========================================================
        // TITLE
        // ========================================================

        Label titleLabel =
                new Label(
                        "Notice Title"
                );

        titleLabel.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-font-weight:bold;"
        );

        TextField titleField =
                new TextField();

        titleField.setPromptText(
                "Enter notice title"
        );

        titleField.setPrefHeight(
                38
        );

        // ========================================================
        // DESCRIPTION
        // ========================================================

        Label descriptionLabel =
                new Label(
                        "Notice Description"
                );

        descriptionLabel.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-font-weight:bold;"
        );

        TextField descriptionField =
                new TextField();

        descriptionField.setPromptText(
                "Enter notice description"
        );

        descriptionField.setPrefHeight(
                38
        );

        // ========================================================
        // DATE
        // ========================================================

        Label dateLabel =
                new Label(
                        "Notice Date"
                );

        dateLabel.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-font-weight:bold;"
        );

        TextField dateField =
                new TextField();

        dateField.setPromptText(
                "Enter date (YYYY-MM-DD)"
        );

        dateField.setPrefHeight(
                38
        );

        // ========================================================
        // STATUS
        // ========================================================

        Label statusLabel =
                new Label(
                        "Status"
                );

        statusLabel.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-font-weight:bold;"
        );

        TextField statusField =
                new TextField();

        statusField.setPromptText(
                "Enter status"
        );

        statusField.setPrefHeight(
                38
        );

        // ========================================================
        // CANCEL
        // ========================================================

        Button cancelBtn =
                new Button(
                        "Cancel"
                );

        cancelBtn.setPrefWidth(
                100
        );

        cancelBtn.setPrefHeight(
                38
        );

        cancelBtn.setStyle(
                "-fx-background-color:#E5E7EB;"
                        + "-fx-text-fill:#333333;"
                        + "-fx-background-radius:8;"
                        + "-fx-cursor:hand;"
        );

        // ========================================================
        // SAVE
        // ========================================================

        Button saveBtn =
                new Button(
                        "Save Notice"
                );

        saveBtn.setPrefWidth(
                130
        );

        saveBtn.setPrefHeight(
                38
        );

        saveBtn.setStyle(
                "-fx-background-color:"
                        + HEADER_COLOR
                        + ";"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:8;"
                        + "-fx-cursor:hand;"
        );

        // ========================================================
        // BUTTON BOX
        // ========================================================

        HBox buttonBox =
                new HBox(10);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.getChildren().addAll(
                cancelBtn,
                saveBtn
        );

        // ========================================================
        // POPUP CONTENT
        // ========================================================

        popup.getChildren().addAll(

                popupTitle,

                titleLabel,
                titleField,

                descriptionLabel,
                descriptionField,

                dateLabel,
                dateField,

                statusLabel,
                statusField,

                buttonBox
        );

        // ========================================================
        // ADD POPUP
        // ========================================================

        overlay.getChildren().add(
                popup
        );

        StackPane.setAlignment(
                popup,
                Pos.CENTER
        );

        rootStack.getChildren().add(
                overlay
        );

        // ========================================================
        // CANCEL
        // ========================================================

        cancelBtn.setOnAction(
                e ->
                        rootStack
                                .getChildren()
                                .remove(
                                        overlay
                                )
        );

        // ========================================================
        // SAVE
        // ========================================================

        saveBtn.setOnAction(
                e -> {

                    String noticeTitle =
                            titleField
                                    .getText()
                                    .trim();

                    String description =
                            descriptionField
                                    .getText()
                                    .trim();

                    String date =
                            dateField
                                    .getText()
                                    .trim();

                    String status =
                            statusField
                                    .getText()
                                    .trim();

                    // ============================================
                    // VALIDATION
                    // ============================================

                    if (noticeTitle.isEmpty()
                            || description.isEmpty()
                            || date.isEmpty()
                            || status.isEmpty()) {

                        showAlert(
                                Alert.AlertType.WARNING,
                                "Validation Error",
                                "Please fill all fields."
                        );

                        return;
                    }

                    // ============================================
                    // EMAIL
                    // ============================================

                    String senderEmail =
                            getUserEmail();

                    if (senderEmail == null) {

                        showAlert(
                                Alert.AlertType.ERROR,
                                "Email Error",
                                "User email is not available."
                        );

                        return;
                    }

                    // ============================================
                    // SAVE
                    // ============================================

                    try {

                        System.out.println(
                                "======================================"
                        );

                        System.out.println(
                                "Saving Notice"
                        );

                        System.out.println(
                                "Email: "
                                        + senderEmail
                        );

                        System.out.println(
                                "Title: "
                                        + noticeTitle
                        );

                        System.out.println(
                                "Description: "
                                        + description
                        );

                        System.out.println(
                                "Date: "
                                        + date
                        );

                        System.out.println(
                                "Status: "
                                        + status
                        );

                        System.out.println(
                                "======================================"
                        );

                        // =========================================
                        // EMAIL PASSED TO CONTROLLER
                        // =========================================

                        boolean success =
                                noticeController.addNotice(
                                        noticeTitle,
                                        description,
                                        date,
                                        status,
                                        senderEmail
                                );

                        if (success) {

                            rootStack
                                    .getChildren()
                                    .remove(
                                            overlay
                                    );

                            if (searchField != null) {
                                searchField.clear();
                            }

                            loadNotices();

                            showAlert(
                                    Alert.AlertType.INFORMATION,
                                    "Success",
                                    "Notice saved successfully!"
                            );

                        } else {

                            showAlert(
                                    Alert.AlertType.ERROR,
                                    "Error",
                                    "Failed to save notice to Firestore."
                            );
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();

                        showAlert(
                                Alert.AlertType.ERROR,
                                "Firestore Error",
                                "Unable to save notice.\n\n"
                                        + ex.getMessage()
                        );
                    }
                }
        );

        titleField.requestFocus();
    }

    // ============================================================
    // VIEW ALL NOTICES
    // ============================================================

    private void openAllNoticesPopup() {

        StackPane overlay =
                new StackPane();

        overlay.setStyle(
                "-fx-background-color:"
                        + "rgba(0,0,0,0.35);"
        );

        // ========================================================
        // POPUP
        // ========================================================

        VBox popup =
                new VBox(15);

        popup.setPadding(
                new Insets(25)
        );

        popup.setPrefWidth(
                600
        );

        popup.setPrefHeight(
                500
        );

        popup.setMaxWidth(
                600
        );

        popup.setMaxHeight(
                500
        );

        popup.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:15;"
        );

        // ========================================================
        // TITLE
        // ========================================================

        Label popupTitle =
                new Label(
                        "All Notices"
                );

        popupTitle.setStyle(
                "-fx-font-size:24px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:"
                        + HEADER_COLOR
                        + ";"
        );

        // ========================================================
        // NOTICE LIST
        // ========================================================

        VBox allNoticeList =
                new VBox(12);

        String email =
                getUserEmail();

        List<Notice> notices =
                noticeController
                        .getNoticesBySenderEmail(
                                email
                        );

        if (notices == null
                || notices.isEmpty()) {

            Label noNotice =
                    new Label(
                            "No notices found."
                    );

            noNotice.setStyle(
                    "-fx-font-size:14px;"
                            + "-fx-text-fill:#777777;"
            );

            allNoticeList.getChildren().add(
                    noNotice
            );

        } else {

            for (Notice notice :
                    notices) {

                VBox noticeBox =
                        createPopupNotice(
                                notice.getTitle(),
                                notice.getDescription(),
                                notice.getDate(),
                                notice.getStatus()
                        );

                allNoticeList.getChildren().add(
                        noticeBox
                );
            }
        }

        // ========================================================
        // SCROLL
        // ========================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                allNoticeList
        );

        scrollPane.setFitToWidth(
                true
        );

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setPrefHeight(
                370
        );

        scrollPane.setStyle(
                "-fx-background-color:transparent;"
                        + "-fx-background:transparent;"
                        + "-fx-border-color:transparent;"
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        // ========================================================
        // CLOSE BUTTON
        // ========================================================

        Button closeBtn =
                new Button(
                        "Close"
                );

        closeBtn.setPrefWidth(
                100
        );

        closeBtn.setPrefHeight(
                40
        );

        closeBtn.setStyle(
                "-fx-background-color:"
                        + HEADER_COLOR
                        + ";"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:8;"
                        + "-fx-cursor:hand;"
        );

        HBox buttonBox =
                new HBox(
                        closeBtn
                );

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        // ========================================================
        // POPUP CONTENT
        // ========================================================

        popup.getChildren().addAll(
                popupTitle,
                scrollPane,
                buttonBox
        );

        // ========================================================
        // ADD POPUP
        // ========================================================

        overlay.getChildren().add(
                popup
        );

        StackPane.setAlignment(
                popup,
                Pos.CENTER
        );

        rootStack.getChildren().add(
                overlay
        );

        // ========================================================
        // CLOSE
        // ========================================================

        closeBtn.setOnAction(
                e ->
                        rootStack
                                .getChildren()
                                .remove(
                                        overlay
                                )
        );
    }

    // ============================================================
    // POPUP NOTICE
    // ============================================================

    private VBox createPopupNotice(
            String titleText,
            String descriptionText,
            String dateText,
            String statusText
    ) {

        VBox box =
                new VBox(7);

        box.setPadding(
                new Insets(15)
        );

        box.setMaxWidth(
                Double.MAX_VALUE
        );

        box.setStyle(
                "-fx-background-color:#F4F7F6;"
                        + "-fx-background-radius:10;"
        );

        // ========================================================
        // TITLE
        // ========================================================

        Label title =
                new Label(
                        "▣  "
                                + safeText(
                                        titleText
                                )
                );

        title.setWrapText(
                true
        );

        title.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#123C36;"
        );

        // ========================================================
        // DESCRIPTION
        // ========================================================

        Label description =
                new Label(
                        safeText(
                                descriptionText
                        )
                );

        description.setWrapText(
                true
        );

        description.setStyle(
                "-fx-font-size:12px;"
                        + "-fx-text-fill:#555555;"
        );

        // ========================================================
        // DATE
        // ========================================================

        Label date =
                new Label(
                        safeText(
                                dateText
                        )
                );

        date.setStyle(
                "-fx-font-size:11px;"
                        + "-fx-text-fill:#777777;"
        );

        // ========================================================
        // STATUS
        // ========================================================

        Label status =
                new Label(
                        safeText(
                                statusText
                        )
                );

        status.setStyle(
                "-fx-background-color:"
                        + HEADER_COLOR
                        + ";"
                        + "-fx-text-fill:white;"
                        + "-fx-font-size:10px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-padding:5px 9px;"
                        + "-fx-background-radius:12;"
        );

        // ========================================================
        // BOTTOM
        // ========================================================

        HBox bottom =
                new HBox(10);

        bottom.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                date,
                Priority.ALWAYS
        );

        bottom.getChildren().addAll(
                date,
                status
        );

        box.getChildren().addAll(
                title,
                description,
                bottom
        );

        return box;
    }

    // ============================================================
    // SAFE TEXT
    // ============================================================

    private String safeText(
            String text) {

        if (text == null) {
            return "";
        }

        return text;
    }

    // ============================================================
    // ALERT
    // ============================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert =
                new Alert(type);

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