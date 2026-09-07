
package com.society.view.Resident_portal;

import com.society.dao.Resident_dao.CommunityGroupDAO;
import com.society.dao.Resident_dao.CommunityGroupDAO.GroupRecord;
import com.society.dao.Resident_dao.CommunityGroupDAO.MemberRecord;
import com.society.dao.Resident_dao.CommunityGroupDAO.MessageRecord;
import com.society.view.ScreenSize;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class Community {

    // =========================================================
    // COLORS
    // =========================================================

    private static final String BROWN = "#4e342e";
    private static final String LIGHT_BROWN = "#4a3730";
    private static final String BACKGROUND = "#e8ddd5";
    private static final String GREEN = "#1b481d";
    private static final String TEXT_DARK = "#263238";
    private static final String TEXT_GREY = "#789098";

    // =========================================================
    // UI VARIABLES
    // =========================================================

    private VBox groupList;
    private VBox chatMessages;

    private Label chatGroupName;
    private Label chatGroupMembers;

    private VBox aboutPanel;
    private TextField messageField;

    private GroupData selectedGroup;
    private ScheduledExecutorService chatRefreshExecutor;

    private final List<GroupData> groups = new ArrayList<>();

    // =========================================================
    // CURRENT USER
    // =========================================================

    private final String currentUserEmail;
    private final String currentUserName;
    private final String currentUserFlat;

    // =========================================================
    // DAO
    // =========================================================

    private final CommunityGroupDAO groupDAO;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Community(
            String loggedInEmail,
            String residentName,
            String residentFlat
    ) {

        if (loggedInEmail == null ||
                loggedInEmail.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Logged-in resident email not found."
            );
        }

        currentUserEmail =
                loggedInEmail.trim().toLowerCase();

        currentUserName =
                residentName == null
                        ? ""
                        : residentName.trim();

        currentUserFlat =
                residentFlat == null
                        ? ""
                        : residentFlat.trim();

        groupDAO = new CommunityGroupDAO();
    }

    // =========================================================
    // CONSTRUCTOR - EMAIL ONLY
    // =========================================================

    public Community(String loggedInEmail) {

        this(
                loggedInEmail,
                "",
                ""
        );
    }

    // =========================================================
    // COMMUNITY SCENE
    // =========================================================

    public Scene getCommunityScene(Stage stage) {

        BorderPane root = new BorderPane();

        // =====================================================
        // FULL SCREEN BACKGROUND IMAGE
        // =====================================================

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

        // =====================================================
        // SIDEBAR - LEFT
        // =====================================================

        try {

            panel panelObj =
                    new panel(
                            stage,
                            currentUserEmail
                    );

            if (panelObj.getSidebar() != null) {

                root.setLeft(
                        panelObj.getSidebar()
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        // =====================================================
        // HEADER - 80px
        // =====================================================

        HBox header = createHeader();

        header.setPrefHeight(80);
        header.setMinHeight(80);
        header.setMaxHeight(80);

        // =====================================================
        // COMMUNITY AREA
        // =====================================================

        BorderPane communityArea =
                new BorderPane();

        communityArea.setStyle(
                "-fx-background-color: transparent;"
        );

        VBox groupsPanel =
                createGroupsPanel(
                        stage,
                        communityArea
                );

        communityArea.setLeft(
                groupsPanel
        );

        communityArea.setCenter(
                createWelcomeScreen()
        );

        // =====================================================
        // ABOUT PANEL
        // =====================================================

        aboutPanel =
                createAboutPanel();

        aboutPanel.setVisible(false);
        aboutPanel.setManaged(false);

        communityArea.setRight(
                aboutPanel
        );

        // =====================================================
        // MAIN AREA
        // Header + Community content
        // =====================================================

        BorderPane mainArea =
                new BorderPane();

        mainArea.setStyle(
                "-fx-background-color:transparent; ;"
        );

        mainArea.setTop(
                header
        );

        mainArea.setCenter(
                communityArea
        );

        // Sidebar remains LEFT
        // Header is directly beside Sidebar

        root.setCenter(
                mainArea
        );

        // =====================================================
        // CONTENT FADE ANIMATION
        // Sidebar and Header remain static
        // =====================================================

        FadeTransition contentFade =
                new FadeTransition(
                        Duration.millis(700),
                        communityArea
                );

        contentFade.setFromValue(0.0);
        contentFade.setToValue(1.0);
        contentFade.setCycleCount(1);

        contentFade.play();

        // =====================================================
        // LOAD GROUPS
        // =====================================================

        loadGroupsFromFirestore(
                communityArea
        );

        // =====================================================
        // SCREEN SIZE
        // =====================================================

        double width = 1200;
        double height = 700;

        try {

            width = ScreenSize.getWidth();
            height = ScreenSize.getHeight();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return new Scene(
                root,
                width,
                height
        );
    }

    // =========================================================
    // LOAD GROUPS
    // =========================================================

    private void loadGroupsFromFirestore(
            BorderPane communityArea
    ) {

        Thread thread =
                new Thread(() -> {

                    try {

                        List<GroupRecord> records =
                                groupDAO.getAllGroups(
                                        currentUserEmail
                                );

                        Platform.runLater(() -> {

                            groups.clear();

                            if (records != null) {

                                for (GroupRecord record :
                                        records) {

                                    if (record == null) {
                                        continue;
                                    }

                                    GroupData group =
                                            new GroupData(
                                                    safeString(record.groupId),
                                                    safeString(record.groupName),
                                                    safeString(record.groupType),
                                                    String.valueOf(
                                                            record.memberCount
                                                    ),
                                                    safeString(record.description),
                                                    safeString(record.createdByEmail),
                                                    safeString(record.createdByName),
                                                    safeString(record.createdByFlat),
                                                    record.joined
                                            );

                                    groups.add(group);
                                }
                            }

                            refreshGroupList(
                                    communityArea
                            );
                        });

                    } catch (Exception e) {

                        e.printStackTrace();

                        Platform.runLater(() ->
                                showError(
                                        "Community Groups",
                                        "Unable to load groups",
                                        getExceptionMessage(e)
                                )
                        );
                    }

                });

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // HEADER
    // =========================================================

    private HBox createHeader() {

        HBox header = new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setPadding(
                new Insets(
                        18,
                        30,
                        18,
                        35
                )
        );

        header.setSpacing(20);

        header.setStyle(
                "-fx-background-color:#4e342e;" +
                        "-fx-border-color: #4e342e;" +
                        "-fx-border-width: 0 0 1 0;"
        );

        VBox titleBox = new VBox(3);

        Label title =
                new Label(
                        "Community Portal"
                );

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
                        "Connect, Share & Support Each Other"
                );

        subtitle.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        subtitle.setTextFill(
               (Color.WHITE)
        );

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        String initial = "?";

        if (currentUserName != null &&
                !currentUserName.trim().isEmpty()) {

            initial =
                    currentUserName
                            .trim()
                            .substring(0, 1)
                            .toUpperCase();
        }

        Label circle =
                new Label(initial);

        circle.setAlignment(Pos.CENTER);

        circle.setPrefSize(46, 46);
        circle.setMinSize(46, 46);
        circle.setMaxSize(46, 46);

        circle.setStyle(
                "-fx-background-color: " +
                        LIGHT_BROWN +
                        ";" +
                        "-fx-background-radius: 50;"
        );

        circle.setTextFill(Color.BLACK);

        circle.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        18
                )
        );

        VBox userBox = new VBox(2);

        Label userName =
                new Label(
                        currentUserName
                );

        userName.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        14
                )
        );

        Label flatNo =
                new Label(
                        currentUserFlat
                );

        flatNo.setTextFill(
                Color.web(TEXT_GREY)
        );

        userBox.getChildren().addAll(
                userName,
                flatNo
        );

        Label arrow =
                new Label("⌄");

        arrow.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        18
                )
        );

        header.getChildren().addAll(
                titleBox,
                spacer,
                circle,
                userBox,
                arrow
        );

        return header;
    }

    // =========================================================
    // GROUP PANEL
    // =========================================================

    private VBox createGroupsPanel(
            Stage stage,
            BorderPane communityArea
    ) {

        VBox panel = new VBox(15);

        panel.setPadding(
                new Insets(20)
        );

        panel.setPrefWidth(365);
        panel.setMinWidth(365);
        panel.setMaxWidth(365);

        panel.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e0d7d2;" +
                        "-fx-border-width: 0 1 0 0;"
        );

        HBox titleRow = new HBox();

        titleRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label(
                        "Community Groups"
                );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        20
                )
        );

        title.setTextFill(
                Color.web(TEXT_DARK)
        );

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button createGroup =
                new Button(
                        "+ Create Group"
                );

        createGroup.setStyle(
                "-fx-background-color: " +
                        BROWN +
                        ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6;"
        );

        createGroup.setOnAction(
                e -> showCreateGroupDialog(
                        stage,
                        communityArea
                )
        );

        titleRow.getChildren().addAll(
                title,
                spacer,
                createGroup
        );

        Label subtitle =
                new Label(
                        "Join groups and connect with residents"
                );

        subtitle.setTextFill(
                Color.web(TEXT_GREY)
        );

        TextField searchField =
                new TextField();

        searchField.setPromptText(
                "⌕  Search groups..."
        );

        searchField.setPrefHeight(38);

        searchField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ddd3ce;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );

        groupList =
                new VBox(8);

        ScrollPane scroll =
                new ScrollPane(groupList);

        scroll.setFitToWidth(true);

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

        searchField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {

                            refreshGroupList(
                                    communityArea,
                                    newValue
                            );
                        }
                );

        VBox newCommunity =
                new VBox(8);

        newCommunity.setPadding(
                new Insets(15)
        );

        newCommunity.setStyle(
                "-fx-background-color: #f0f5ff;" +
                        "-fx-background-radius: 8;"
        );

        Label newTitle =
                new Label(
                        "New to the community?"
                );

        newTitle.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        15
                )
        );

        Label newText =
                new Label(
                        "Join a group and start connecting " +
                                "with your neighbors."
                );

        newText.setWrapText(true);

        newText.setTextFill(
                Color.web("#607d8b")
        );

        Button explore =
                new Button(
                        "Explore Groups"
                );

        explore.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " +
                        BROWN +
                        ";" +
                        "-fx-text-fill: " +
                        BROWN +
                        ";" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-radius: 5;"
        );

        explore.setOnAction(
                e -> {

                    searchField.clear();

                    if (groupList != null) {
                        groupList.requestFocus();
                    }
                }
        );

        newCommunity.getChildren().addAll(
                newTitle,
                newText,
                explore
        );

        panel.getChildren().addAll(
                titleRow,
                subtitle,
                searchField,
                scroll,
                newCommunity
        );

        return panel;
    }

    // =========================================================
    // REFRESH GROUP LIST
    // =========================================================

    private void refreshGroupList(
            BorderPane communityArea
    ) {

        refreshGroupList(
                communityArea,
                ""
        );
    }

    private void refreshGroupList(
            BorderPane communityArea,
            String searchText
    ) {

        if (groupList == null) {
            return;
        }

        groupList
                .getChildren()
                .clear();

        String search =
                searchText == null
                        ? ""
                        : searchText
                                .toLowerCase()
                                .trim();

        for (GroupData group : groups) {

            if (group == null) {
                continue;
            }

            String name =
                    group.name == null
                            ? ""
                            : group.name.toLowerCase();

            if (name.contains(search)) {

                groupList
                        .getChildren()
                        .add(
                                createGroupItem(
                                        group,
                                        communityArea
                                )
                        );
            }
        }
    }

    // =========================================================
    // GROUP ITEM
    // =========================================================

    private VBox createGroupItem(
            GroupData group,
            BorderPane communityArea
    ) {

        VBox container = new VBox();
        container.setPadding(new Insets(8));
        container.setStyle(
                "-fx-background-color: #533131;" +
                "-fx-background-radius: 8;"
        );

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label(getGroupIcon(group.name));
        icon.setAlignment(Pos.CENTER);
        icon.setPrefSize(48, 48);
        icon.setMinSize(48, 48);
        icon.setMaxSize(48, 48);
        icon.setStyle(
                "-fx-background-color: " + GREEN + ";" +
                "-fx-background-radius: 50;"
        );
        icon.setTextFill(Color.WHITE);
        icon.setFont(Font.font("System", FontWeight.BOLD, 17));

        VBox info = new VBox(3);

        Label name = new Label(safeString(group.name));
        name.setFont(Font.font("System", FontWeight.BOLD, 14));
        name.setTextFill(Color.BLACK);

        Label details = new Label(
                safeString(group.type) +
                " • " +
                safeString(group.memberCount) +
                " Members"
        );
        details.setFont(Font.font("System", 12));
        details.setTextFill(Color.web(TEXT_GREY));

        info.getChildren().addAll(name, details);
        HBox.setHgrow(info, Priority.ALWAYS);

        boolean isCreator =
                group.createdByEmail != null &&
                group.createdByEmail.equalsIgnoreCase(
                        currentUserEmail
                );

        if (!isCreator) {

            Button joinButton = new Button(
                    group.joined ? "Joined" : "Join"
            );

            if (group.joined) {
                joinButton.setStyle(
                        "-fx-background-color: #e8f5e9;" +
                        "-fx-text-fill: #43a047;" +
                        "-fx-background-radius: 15;" +
                        "-fx-font-weight: bold;"
                );
            } else {
                joinButton.setStyle(
                        "-fx-background-color: " + BROWN + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-font-weight: bold;"
                );
            }

            joinButton.setOnAction(e -> {
                if (group.joined) {
                    openChat(group, communityArea);
                } else {
                    joinGroupInFirestore(group, communityArea);
                }
            });

            row.getChildren().addAll(
                    icon,
                    info,
                    joinButton
            );

        } else {

            row.getChildren().addAll(
                    icon,
                    info
            );
        }

        row.setOnMouseClicked(e -> {
            if (group.joined) {
                openChat(group, communityArea);
            }
        });

        container.getChildren().add(row);
        return container;
    }

    // =========================================================
    // JOIN GROUP
    // =========================================================

    private void joinGroupInFirestore(
            GroupData group,
            BorderPane communityArea
    ) {

        if (group == null) {
            return;
        }

        Thread thread =
                new Thread(() -> {

                    try {

                        boolean newlyJoined =
                                groupDAO.joinGroup(
                                        group.groupId,
                                        group.createdByEmail,
                                        currentUserEmail,
                                        currentUserName,
                                        currentUserFlat
                                );

                        Platform.runLater(() -> {

                            if (newlyJoined) {

                                group.joined = true;

                                boolean alreadyExists = false;

                                for (MemberData member :
                                        group.members) {

                                    if (member != null &&
                                            member.email != null &&
                                            member.email.equalsIgnoreCase(
                                                    currentUserEmail
                                            )) {

                                        alreadyExists = true;
                                        break;
                                    }
                                }

                                if (!alreadyExists) {

                                    group.members.add(
                                            new MemberData(
                                                    currentUserEmail,
                                                    currentUserName,
                                                    currentUserFlat,
                                                    "Member"
                                            )
                                    );
                                }

                                int count =
                                        parseMemberCount(
                                                group.memberCount
                                        );

                                group.memberCount =
                                        String.valueOf(
                                                count + 1
                                        );

                            } else {

                                group.joined = true;
                            }

                            refreshGroupList(
                                    communityArea
                            );

                            openChat(
                                    group,
                                    communityArea
                            );
                        });

                    } catch (Exception e) {

                        e.printStackTrace();

                        Platform.runLater(() ->
                                showError(
                                        "Join Group",
                                        "Unable to join group",
                                        getExceptionMessage(e)
                                )
                        );
                    }

                });

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // WELCOME SCREEN
    // =========================================================

    private VBox createWelcomeScreen() {

        VBox box = new VBox(15);

        box.setAlignment(
                Pos.CENTER
        );

        box.setPadding(
                new Insets(40)
        );

        box.setStyle(
                "-fx-background-color: transparent;"
        );

        Label icon =
                new Label("👥");

        icon.setFont(
                Font.font(
                        "System",
                        45
                )
        );

        Label title =
                new Label(
                        "Welcome to Community"
                );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        24
                )
        );

        title.setTextFill(
                Color.web(TEXT_DARK)
        );

        Label text =
                new Label(
                        "Join a community group to start " +
                                "connecting with your neighbors."
                );

        text.setTextFill(
                Color.web(TEXT_GREY)
        );

        text.setWrapText(true);

        box.getChildren().addAll(
                icon,
                title,
                text
        );

        return box;
    }

    // =========================================================
    // OPEN CHAT
    // =========================================================

    private void openChat(
            GroupData group,
            BorderPane communityArea
    ) {

        if (group == null) {
            return;
        }

        stopChatAutoRefresh();
        selectedGroup = group;

        if (aboutPanel != null) {
            aboutPanel.setVisible(false);
            aboutPanel.setManaged(false);
        }

        communityArea.setCenter(
                createChatPanel(group)
        );

        refreshGroupList(communityArea);
    }

    // =========================================================
    // CHAT PANEL
    // =========================================================

    private VBox createChatPanel(
            GroupData group
    ) {

        VBox chatPanel = new VBox();
        chatPanel.setStyle(
                "-fx-background-color: #d4c9c2;"
        );

        HBox chatHeader = new HBox(12);
        chatHeader.setAlignment(Pos.CENTER_LEFT);
        chatHeader.setPadding(
                new Insets(12, 18, 12, 18)
        );
        chatHeader.setStyle(
                "-fx-background-color: #4e342e;" +
                "-fx-border-color: #4e342e;" +
                "-fx-border-width: 0 0 1 0;"
        );

        Label groupIcon = new Label(
                getGroupIcon(group.name)
        );
        groupIcon.setAlignment(Pos.CENTER);
        groupIcon.setPrefSize(42, 42);
        groupIcon.setMinSize(42, 42);
        groupIcon.setMaxSize(42, 42);
        groupIcon.setStyle(
                "-fx-background-color: " + GREEN + ";" +
                "-fx-background-radius: 50;"
        );
        groupIcon.setTextFill(Color.WHITE);

        VBox titleBox = new VBox(2);

        chatGroupName = new Label(
                safeString(group.name)
        );
        chatGroupName.setFont(
                Font.font("System", FontWeight.BOLD, 17)
        );

        chatGroupMembers = new Label(
                safeString(group.type) +
                " • " +
                safeString(group.memberCount) +
                " Members"
        );
        chatGroupMembers.setTextFill(
                Color.web(TEXT_GREY)
        );

        titleBox.getChildren().addAll(
                chatGroupName,
                chatGroupMembers
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button aboutButton = new Button("ⓘ About");
        aboutButton.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #d7ccc6;" +
                "-fx-text-fill: " + BROWN + ";" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;"
        );
        aboutButton.setOnAction(
                e -> toggleAboutPanel(group)
        );

        Button moreButton = new Button("⋮");
        moreButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-font-size: 20;"
        );

        chatHeader.getChildren().addAll(
                groupIcon,
                titleBox,
                spacer,
                aboutButton,
                moreButton
        );

        HBox pinned = new HBox(8);
        pinned.setAlignment(Pos.CENTER_LEFT);
        pinned.setPadding(new Insets(8, 15, 8, 15));
        pinned.setStyle(
                "-fx-background-color: #fffaf7;" +
                "-fx-border-color: #050505;" +
                "-fx-border-width: 0 0 1 0;"
        );

        Label pin = new Label("📌");
        Label pinnedText = new Label(
                "Pinned: Society Sunday Cleanup Drive " +
                "this weekend at 7 AM. All are welcome!"
        );
        pinnedText.setWrapText(true);
        pinnedText.setTextFill(Color.web("#5d4037"));
        pinned.getChildren().addAll(pin, pinnedText);

        chatMessages = new VBox(12);
        chatMessages.setPadding(new Insets(18));
        chatMessages.setFillWidth(true);

        ScrollPane messageScroll =
                new ScrollPane(chatMessages);
        messageScroll.setFitToWidth(true);
        messageScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );
        messageScroll.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;"
        );
        VBox.setVgrow(messageScroll, Priority.ALWAYS);

        loadMessagesFromFirestore(group);
        startChatAutoRefresh(group);

        HBox inputArea = new HBox(8);
        inputArea.setPadding(new Insets(12));
        inputArea.setAlignment(Pos.CENTER);
        inputArea.setStyle(
                "-fx-background-color: #ffffff;" +
                "-fx-border-color: #fffaf7;" +
                "-fx-border-width: 1 0 0 0;"
        );

        Button imageButton = new Button("📎");
        imageButton.setPrefSize(42, 42);
        imageButton.setStyle(
                "-fx-background-color: #837d7a;" +
                "-fx-font-size: 18;" +
                "-fx-background-radius: 50;"
        );
        imageButton.setOnAction(e -> sendImage());

        messageField = new TextField();
        messageField.setPromptText("Type a message...");
        messageField.setPrefHeight(42);
        messageField.setStyle(
                "-fx-background-color: #f5f2f0;" +
                "-fx-background-radius: 22;" +
                "-fx-border-color: #ddd5d0;" +
                "-fx-border-radius: 22;" +
                "-fx-padding: 0 16 0 16;"
        );
        HBox.setHgrow(messageField, Priority.ALWAYS);

        Button sendButton = new Button("➤");
        sendButton.setPrefSize(45, 42);
        sendButton.setStyle(
                "-fx-background-color: " + BROWN + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18;" +
                "-fx-background-radius: 50;"
        );
        sendButton.setOnAction(e -> sendMessage());
        messageField.setOnAction(e -> sendMessage());

        inputArea.getChildren().addAll(
                imageButton,
                messageField,
                sendButton
        );

        chatPanel.getChildren().addAll(
                chatHeader,
                pinned,
                messageScroll,
                inputArea
        );

        return chatPanel;
    }

    // =========================================================
    // SEND MESSAGE
    // =========================================================

    private void sendMessage() {

        if (messageField == null || selectedGroup == null) {
            return;
        }

        String message = messageField.getText().trim();
        if (message.isEmpty()) {
            return;
        }

        final String textToSave = message;
        final GroupData targetGroup = selectedGroup;
        messageField.clear();

        Thread thread = new Thread(() -> {
            try {
                groupDAO.saveTextMessage(
                        targetGroup.groupId,
                        currentUserEmail,
                        currentUserName,
                        textToSave
                );

                Platform.runLater(() -> {
                    if (selectedGroup == targetGroup
                            && chatMessages != null) {
                        loadMessagesFromFirestore(targetGroup);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() ->
                        showError(
                                "Send Message",
                                "Unable to send message",
                                getExceptionMessage(e)
                        )
                );
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // OWN MESSAGE
    // =========================================================

    private void addOwnMessage(
            String sender,
            String message,
            String time
    ) {

        if (chatMessages == null) {
            return;
        }

        HBox row = new HBox();

        row.setAlignment(
                Pos.CENTER_RIGHT
        );

        VBox bubble = new VBox(4);

        bubble.setMaxWidth(430);

        bubble.setPadding(
                new Insets(
                        10,
                        14,
                        8,
                        14
                )
        );

        bubble.setStyle(
                "-fx-background-color: #2b4319;" +
                        "-fx-background-radius: 10 10 2 10;"
        );

        Label senderLabel =
                new Label(
                        safeString(sender)
                );

        senderLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12
                )
        );
        senderLabel.setTextFill(Color.BLACK);

        Label text =
                new Label(
                        message == null ? "" : message
                );

        text.setWrapText(true);

        text.setFont(
                Font.font(
                        "System",
                        14
                )
        );
        text.setTextFill(Color.BLACK);

        Label timeLabel =
                new Label(
                        safeString(time)
                );

        timeLabel.setTextFill(
                Color.web("#78909c")
        );

        timeLabel.setFont(
                Font.font(
                        "System",
                        10
                )
        );

        bubble.getChildren().addAll(
                senderLabel,
                text,
                timeLabel
        );

        row.getChildren().add(
                bubble
        );

        chatMessages.getChildren().add(row);
    }

    // =========================================================
    // RECEIVED MESSAGE
    // =========================================================

    private void addReceivedMessage(
            String sender,
            String message,
            String time
    ) {

        if (chatMessages == null) {
            return;
        }

        HBox row =
                new HBox(8);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        Label avatar =
                new Label(
                        getInitials(sender)
                );

        avatar.setAlignment(Pos.CENTER);

        avatar.setPrefSize(38, 38);
        avatar.setMinSize(38, 38);
        avatar.setMaxSize(38, 38);

        avatar.setStyle(
                "-fx-background-color: #78909c;" +
                        "-fx-background-radius: 50;"
        );

        avatar.setTextFill(Color.BEIGE);

        avatar.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        11
                )
        );

        VBox bubble =
                new VBox(4);

        bubble.setMaxWidth(430);

        bubble.setPadding(
                new Insets(
                        9,
                        13,
                        8,
                        13
                )
        );

        bubble.setStyle(
                "-fx-background-color: #8a6060;" +
                        "-fx-background-radius: 2 10 10 10;" +
                        "-fx-border-color: #907a6a;" +
                        "-fx-border-radius: 2 10 10 10;"
        );

        Label senderLabel =
                new Label(
                        safeString(sender)
                );

        senderLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12
                )
        );

        senderLabel.setTextFill(Color.BLACK);

        Label text =
                new Label(
                        message == null ? "" : message
                );

        text.setWrapText(true);

        text.setFont(
                Font.font(
                        "System",
                        14
                )
        );
        text.setTextFill(Color.BLACK);

        Label timeLabel =
                new Label(
                        safeString(time)
                );

        timeLabel.setTextFill(
                Color.web("#78909c")
        );

        timeLabel.setFont(
                Font.font(
                        "System",
                        10
                )
        );

        bubble.getChildren().addAll(
                senderLabel,
                text,
                timeLabel
        );

        row.getChildren().addAll(
                avatar,
                bubble
        );

        chatMessages.getChildren().add(row);
    }

    // =========================================================
    // SEND IMAGE
    // =========================================================

    private void sendImage() {

        if (selectedGroup == null
                || chatMessages == null
                || chatMessages.getScene() == null) {
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files",
                        "*.png",
                        "*.jpg",
                        "*.jpeg",
                        "*.gif",
                        "*.bmp"
                )
        );

        File file = fileChooser.showOpenDialog(
                chatMessages.getScene().getWindow()
        );

        if (file == null) {
            return;
        }

        final GroupData targetGroup = selectedGroup;

        Thread thread = new Thread(() -> {
            try {
                String base64 = encodeImageForFirestore(file);

                groupDAO.saveImageMessage(
                        targetGroup.groupId,
                        currentUserEmail,
                        currentUserName,
                        base64,
                        file.getName()
                );

                Platform.runLater(() -> {
                    if (selectedGroup == targetGroup
                            && chatMessages != null) {
                        loadMessagesFromFirestore(targetGroup);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() ->
                        showError(
                                "Image Error",
                                "Unable to send image",
                                getExceptionMessage(e)
                        )
                );
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // LOAD MESSAGES FROM FIRESTORE
    // =========================================================

    private boolean sameGroup(GroupData a, GroupData b) {
        if (a == null || b == null) {
            return false;
        }
        return safeString(a.groupId).equals(safeString(b.groupId));
    }

    private void startChatAutoRefresh(GroupData group) {
        stopChatAutoRefresh();

        if (group == null) {
            return;
        }

        chatRefreshExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "community-chat-refresh");
            t.setDaemon(true);
            return t;
        });

        chatRefreshExecutor.scheduleAtFixedRate(() -> {
            if (selectedGroup == null || !sameGroup(selectedGroup, group)) {
                return;
            }
            loadMessagesFromFirestore(group);
        }, 2, 2, TimeUnit.SECONDS);
    }

    private void stopChatAutoRefresh() {
        if (chatRefreshExecutor != null) {
            chatRefreshExecutor.shutdownNow();
            chatRefreshExecutor = null;
        }
    }

    private void loadMessagesFromFirestore(
            GroupData group
    ) {

        if (group == null) {
            return;
        }

        final String groupId = group.groupId;
        final String email = currentUserEmail;

        Thread thread = new Thread(() -> {
            try {
                List<MessageRecord> records =
                        groupDAO.getMessages(
                                groupId,
                                email
                        );

                Platform.runLater(() -> {

                    if (selectedGroup == null
                            || !sameGroup(selectedGroup, group)
                            || chatMessages == null) {
                        return;
                    }

                    chatMessages.getChildren().clear();

                    if (records == null) {
                        return;
                    }

                    for (MessageRecord record : records) {
                        if (record == null) {
                            continue;
                        }

                        if ((selectedGroup.name == null || selectedGroup.name.trim().isEmpty())
                                && record.groupName != null
                                && !record.groupName.trim().isEmpty()) {
                            selectedGroup.name = record.groupName;
                            if (chatGroupName != null) {
                                chatGroupName.setText(record.groupName);
                            }
                        }

                        boolean own =
                                record.senderEmail != null
                                && record.senderEmail.equalsIgnoreCase(
                                        currentUserEmail
                                );

                        String time =
                                formatMessageTime(record.createdAt);

                        if ("IMAGE".equalsIgnoreCase(
                                record.messageType)) {

                            if (own) {
                                addOwnImage(
                                        record.senderName,
                                        record.imageName,
                                        record.imageBase64,
                                        time
                                );
                            } else {
                                addReceivedImage(
                                        record.senderName,
                                        record.imageName,
                                        record.imageBase64,
                                        time
                                );
                            }

                        } else {

                            if (own) {
                                addOwnMessage(
                                        record.senderName,
                                        record.message,
                                        time
                                );
                            } else {
                                addReceivedMessage(
                                        record.senderName,
                                        record.message,
                                        time
                                );
                            }
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() ->
                        showError(
                                "Community Chat",
                                "Unable to load messages",
                                getExceptionMessage(e)
                        )
                );
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // OWN IMAGE
    // =========================================================

    private void addOwnImage(
            String sender,
            String imageName,
            String imageBase64,
            String time
    ) {

        if (chatMessages == null) {
            return;
        }

        try {
            byte[] bytes = Base64.getDecoder().decode(imageBase64);
            Image image = new Image(
                    new ByteArrayInputStream(bytes)
            );

            if (image.isError()) {
                throw new IllegalArgumentException(
                        "Stored image could not be decoded."
                );
            }

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(250);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);

            VBox bubble = new VBox(5);
            bubble.setPadding(new Insets(8));
            bubble.setStyle(
                    "-fx-background-color: #dcf8c6;" +
                    "-fx-background-radius: 10 10 2 10;"
            );

            Label senderLabel = new Label(
                    safeString(sender)
            );
            senderLabel.setFont(
                    Font.font("System", FontWeight.BOLD, 12)
            );
            senderLabel.setTextFill(
                    Color.web("#5d4037")
            );

            Label imageNameLabel = new Label(
                    safeString(imageName)
            );
            imageNameLabel.setFont(
                    Font.font("System", FontWeight.BOLD, 11)
            );
            imageNameLabel.setTextFill(
                    Color.web("#5d4037")
            );

            Label timeLabel = new Label(
                    safeString(time)
            );
            timeLabel.setTextFill(
                    Color.web("#78909c")
            );
            timeLabel.setFont(
                    Font.font("System", 10)
            );

            bubble.getChildren().addAll(
                    senderLabel,
                    imageView,
                    imageNameLabel,
                    timeLabel
            );

            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_RIGHT);
            row.getChildren().add(bubble);

            chatMessages.getChildren().add(row);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // RECEIVED IMAGE
    // =========================================================

    private void addReceivedImage(
            String sender,
            String imageName,
            String imageBase64,
            String time
    ) {

        if (chatMessages == null) {
            return;
        }

        try {
            byte[] bytes = Base64.getDecoder().decode(imageBase64);
            Image image = new Image(
                    new ByteArrayInputStream(bytes)
            );

            if (image.isError()) {
                throw new IllegalArgumentException(
                        "Stored image could not be decoded."
                );
            }

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(250);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);

            VBox bubble = new VBox(5);
            bubble.setPadding(new Insets(8));
            bubble.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-background-radius: 2 10 10 10;" +
                    "-fx-border-color: #8e7665;" +
                    "-fx-border-radius: 2 10 10 10;"
            );

            Label senderLabel = new Label(
                    safeString(sender)
            );
            senderLabel.setFont(
                    Font.font("System", FontWeight.BOLD, 12)
            );
            senderLabel.setTextFill(
                    Color.web("#5d4037")
            );

            Label imageNameLabel = new Label(
                    safeString(imageName)
            );
            imageNameLabel.setFont(
                    Font.font("System", FontWeight.BOLD, 11)
            );

            Label timeLabel = new Label(
                    safeString(time)
            );
            timeLabel.setTextFill(
                    Color.web("#78909c")
            );
            timeLabel.setFont(
                    Font.font("System", 10)
            );

            bubble.getChildren().addAll(
                    senderLabel,
                    imageView,
                    imageNameLabel,
                    timeLabel
            );

            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);

            Label avatar = new Label(
                    getInitials(sender)
            );
            avatar.setAlignment(Pos.CENTER);
            avatar.setPrefSize(38, 38);
            avatar.setMinSize(38, 38);
            avatar.setMaxSize(38, 38);
            avatar.setStyle(
                    "-fx-background-color: #78909c;" +
                    "-fx-background-radius: 50;"
            );
            avatar.setTextFill(Color.ALICEBLUE);
            avatar.setFont(
                    Font.font("System", FontWeight.BOLD, 11)
            );

            row.getChildren().addAll(
                    avatar,
                    bubble
            );

            chatMessages.getChildren().add(row);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // IMAGE ENCODING
    // =========================================================

    private String encodeImageForFirestore(
            File file
    ) throws Exception {

        BufferedImage original = ImageIO.read(file);

        if (original == null) {
            throw new IllegalArgumentException(
                    "Selected file is not a valid image."
            );
        }

        int maxDimension = 900;
        double scale = Math.min(
                1.0,
                (double) maxDimension /
                        Math.max(
                                original.getWidth(),
                                original.getHeight()
                        )
        );

        int width = Math.max(
                1,
                (int) Math.round(
                        original.getWidth() * scale
                )
        );

        int height = Math.max(
                1,
                (int) Math.round(
                        original.getHeight() * scale
                )
        );

        BufferedImage resized = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D graphics = resized.createGraphics();
        graphics.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );
        graphics.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );
        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        graphics.drawImage(
                original,
                0,
                0,
                width,
                height,
                null
        );
        graphics.dispose();

        float quality = 0.70f;
        byte[] bytes = null;

        for (int attempt = 0; attempt < 8; attempt++) {

            ByteArrayOutputStream output =
                    new ByteArrayOutputStream();

            Iterator<ImageWriter> writers =
                    ImageIO.getImageWritersByFormatName("jpg");

            if (!writers.hasNext()) {
                throw new IllegalStateException(
                        "JPEG writer is not available."
                );
            }

            ImageWriter writer = writers.next();

            try (ImageOutputStream imageOutput =
                         ImageIO.createImageOutputStream(output)) {

                writer.setOutput(imageOutput);

                ImageWriteParam params =
                        writer.getDefaultWriteParam();

                if (params.canWriteCompressed()) {
                    params.setCompressionMode(
                            ImageWriteParam.MODE_EXPLICIT
                    );
                    params.setCompressionQuality(quality);
                }

                writer.write(
                        null,
                        new IIOImage(resized, null, null),
                        params
                );

            } finally {
                writer.dispose();
            }

            bytes = output.toByteArray();

            if (bytes.length <= 600 * 1024) {
                break;
            }

            quality -= 0.08f;

            if (quality < 0.30f) {
                int newWidth =
                        Math.max(300, resized.getWidth() * 4 / 5);
                int newHeight =
                        Math.max(300, resized.getHeight() * 4 / 5);

                BufferedImage smaller =
                        new BufferedImage(
                                newWidth,
                                newHeight,
                                BufferedImage.TYPE_INT_RGB
                        );

                Graphics2D g = smaller.createGraphics();
                g.drawImage(
                        resized,
                        0,
                        0,
                        newWidth,
                        newHeight,
                        null
                );
                g.dispose();

                resized = smaller;
                quality = 0.60f;
            }
        }

        if (bytes == null || bytes.length > 600 * 1024) {
            throw new IllegalArgumentException(
                    "Image is too large. Please select a smaller image."
            );
        }

        return Base64.getEncoder().encodeToString(bytes);
    }

    // =========================================================
    // MESSAGE TIME
    // =========================================================

    private String formatMessageTime(
            com.google.cloud.Timestamp timestamp
    ) {

        if (timestamp == null) {
            return "Just now";
        }

        try {
            return new SimpleDateFormat(
                    "hh:mm a"
            ).format(timestamp.toDate());
        } catch (Exception e) {
            return "";
        }
    }

    // =========================================================
    // ABOUT PANEL
    // =========================================================

    private VBox createAboutPanel() {

        VBox panel =
                new VBox(15);

        panel.setPrefWidth(320);
        panel.setMinWidth(320);
        panel.setMaxWidth(320);

        panel.setPadding(
                new Insets(20)
        );

        panel.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e0d7d2;" +
                        "-fx-border-width: 0 0 0 1;"
        );

        return panel;
    }

    // =========================================================
    // TOGGLE ABOUT
    // =========================================================

    private void toggleAboutPanel(
            GroupData group
    ) {

        if (aboutPanel == null) {
            return;
        }

        if (aboutPanel.isVisible()) {

            aboutPanel.setVisible(false);
            aboutPanel.setManaged(false);

            return;
        }

        VBox content =
                buildAboutContent(group);

        aboutPanel
                .getChildren()
                .setAll(
                        content.getChildren()
                );

        aboutPanel.setVisible(true);
        aboutPanel.setManaged(true);
    }

    // =========================================================
    // ABOUT CONTENT
    // =========================================================

    private VBox buildAboutContent(
            GroupData group
    ) {

        VBox content =
                new VBox(15);

        Label title =
                new Label(
                        "About This Group"
                );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        20
                )
        );

        title.setTextFill(
                Color.web(TEXT_DARK)
        );

        Label description =
                new Label(
                        safeString(group.description)
                );

        description.setWrapText(true);

        description.setTextFill(
                Color.web(TEXT_GREY)
        );

        VBox groupInfo =
                new VBox(7);

        Label groupName =
                new Label(
                        safeString(group.name)
                );

        groupName.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        16
                )
        );

        Label type =
                new Label(
                        safeString(group.type)
                );

        type.setTextFill(
                Color.web(TEXT_GREY)
        );

        Label members =
                new Label(
                        safeString(group.memberCount) +
                                " Members"
                );

        members.setTextFill(
                Color.web(TEXT_GREY)
        );

        groupInfo.getChildren().addAll(
                groupName,
                type,
                members
        );

        Separator separator =
                new Separator();

        HBox memberHeader =
                new HBox();

        memberHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        Label memberTitle =
                new Label(
                        "Group Members"
                );

        memberTitle.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Label viewAll =
                new Label("View All");

        viewAll.setTextFill(
                Color.web(BROWN)
        );

        memberHeader.getChildren().addAll(
                memberTitle,
                spacer,
                viewAll
        );

        VBox memberList =
                new VBox(12);

        loadMembersFromFirestore(
                group,
                memberList
        );

        ScrollPane memberScroll =
                new ScrollPane(memberList);

        memberScroll.setFitToWidth(true);

        memberScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        memberScroll.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;"
        );

        VBox.setVgrow(
                memberScroll,
                Priority.ALWAYS
        );

        content.getChildren().addAll(
                title,
                description,
                groupInfo,
                separator,
                memberHeader,
                memberScroll
        );

        return content;
    }

    // =========================================================
    // LOAD MEMBERS
    // =========================================================

    private void loadMembersFromFirestore(
            GroupData group,
            VBox memberList
    ) {

        if (group == null ||
                memberList == null) {

            return;
        }

        Thread thread =
                new Thread(() -> {

                    try {

                        List<MemberRecord> records =
                                groupDAO.getMembers(
                                        group.groupId,
                                        group.createdByEmail
                                );

                        Platform.runLater(() -> {

                            memberList
                                    .getChildren()
                                    .clear();

                            group.members.clear();

                            if (records != null) {

                                for (MemberRecord record :
                                        records) {

                                    if (record == null) {
                                        continue;
                                    }

                                    MemberData member =
                                            new MemberData(
                                                    safeString(record.email),
                                                    safeString(record.name),
                                                    safeString(record.flatNo),
                                                    safeString(record.role)
                                            );

                                    group.members.add(member);

                                    memberList
                                            .getChildren()
                                            .add(
                                                    createMemberRow(member)
                                            );
                                }
                            }

                            if (!group.members.isEmpty()) {

                                group.memberCount =
                                        String.valueOf(
                                                group.members.size()
                                        );

                                if (chatGroupMembers != null &&
                                        selectedGroup == group) {

                                    chatGroupMembers.setText(
                                            safeString(group.type) +
                                                    " • " +
                                                    group.memberCount +
                                                    " Members"
                                    );
                                }
                            }
                        });

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                });

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // MEMBER ROW
    // =========================================================

    private HBox createMemberRow(
            MemberData member
    ) {

        HBox row =
                new HBox(10);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        Label avatar =
                new Label(
                        getInitials(member.name)
                );

        avatar.setAlignment(Pos.CENTER);

        avatar.setPrefSize(42, 42);
        avatar.setMinSize(42, 42);
        avatar.setMaxSize(42, 42);

        avatar.setStyle(
                "-fx-background-color: #78909c;" +
                        "-fx-background-radius: 50;"
        );

        avatar.setTextFill(Color.BEIGE);

        avatar.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        11
                )
        );

        VBox info =
                new VBox(2);

        Label name =
                new Label(
                        safeString(member.name)
                );

        name.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        Label role =
                new Label(
                        safeString(member.role)
                );

        role.setTextFill(
                Color.web(TEXT_GREY)
        );

        info.getChildren().addAll(
                name,
                role
        );

        HBox.setHgrow(
                info,
                Priority.ALWAYS
        );

        Label online =
                new Label("●");

        online.setTextFill(
                Color.web(GREEN)
        );

        row.getChildren().addAll(
                avatar,
                info,
                online
        );

        return row;
    }

    // =========================================================
    // CREATE GROUP DIALOG
    // =========================================================

    private void showCreateGroupDialog(
            Stage stage,
            BorderPane communityArea
    ) {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Create Community Group"
        );

        dialog.setHeaderText(
                "Create a new community group"
        );

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Group name"
        );

        ComboBox<String> typeBox =
                new ComboBox<>();

        typeBox.getItems().addAll(
                "Community Group",
                "Interest Group",
                "Activity Group",
                "Official Group"
        );

        typeBox.setValue(
                "Community Group"
        );

        TextArea description =
                new TextArea();

        description.setPromptText(
                "Group description"
        );

        description.setPrefRowCount(3);

        VBox box =
                new VBox(10);

        box.setPadding(
                new Insets(10)
        );

        box.getChildren().addAll(
                new Label("Group Name"),
                nameField,
                new Label("Group Type"),
                typeBox,
                new Label("Description"),
                description
        );

        dialog.getDialogPane()
                .setContent(box);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        ButtonType.CANCEL,
                        ButtonType.OK
                );

        dialog.showAndWait()
                .ifPresent(result -> {

                    if (result != ButtonType.OK) {
                        return;
                    }

                    String name =
                            nameField
                                    .getText()
                                    .trim();

                    if (name.isEmpty()) {

                        showWarning(
                                "Create Group",
                                "Group name required",
                                "Please enter a group name."
                        );

                        return;
                    }

                    String type =
                            typeBox.getValue();

                    if (type == null ||
                            type.trim().isEmpty()) {

                        type = "Community Group";
                    }

                    String desc =
                            description
                                    .getText()
                                    .trim();

                    createGroupInFirestore(
                            name,
                            type,
                            desc,
                            communityArea
                    );
                });
    }

    // =========================================================
    // CREATE GROUP
    // =========================================================

    private void createGroupInFirestore(
            String groupName,
            String groupType,
            String description,
            BorderPane communityArea
    ) {

        Thread thread =
                new Thread(() -> {

                    try {

                        groupDAO.createGroup(
                                currentUserEmail,
                                groupName,
                                groupType,
                                description,
                                currentUserName,
                                currentUserFlat
                        );

                        Platform.runLater(() -> {

                            showInformation(
                                    "Community Group",
                                    "Group Created Successfully",
                                    "Your community group has been created."
                            );

                            loadGroupsFromFirestore(
                                    communityArea
                            );
                        });

                    } catch (Exception e) {

                        e.printStackTrace();

                        Platform.runLater(() ->
                                showError(
                                        "Create Group",
                                        "Unable to create group",
                                        getExceptionMessage(e)
                                )
                        );
                    }

                });

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // GROUP ICON
    // =========================================================

    private String getGroupIcon(
            String groupName
    ) {

        if (groupName == null ||
                groupName.trim().isEmpty()) {

            return "♣";
        }

        String name =
                groupName.toLowerCase();

        if (name.contains("pet")) {
            return "🐾";
        }

        if (name.contains("fitness")) {
            return "F";
        }

        if (name.contains("book")) {
            return "B";
        }

        if (name.contains("parent")) {
            return "P";
        }

        if (name.contains("sport")) {
            return "⚽";
        }

        if (name.contains("music")) {
            return "♪";
        }

        return "♣";
    }

    // =========================================================
    // INITIALS
    // =========================================================

    private String getInitials(
            String name
    ) {

        if (name == null ||
                name.trim().isEmpty()) {

            return "?";
        }

        String[] parts =
                name.trim()
                        .split("\\s+");

        if (parts.length == 1) {

            String value =
                    parts[0];

            return value
                    .substring(
                            0,
                            Math.min(
                                    2,
                                    value.length()
                            )
                    )
                    .toUpperCase();
        }

        return (
                parts[0].charAt(0) +
                        "" +
                        parts[
                                parts.length - 1
                        ].charAt(0)
        ).toUpperCase();
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safeString(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }

    // =========================================================
    // MEMBER COUNT
    // =========================================================

    private int parseMemberCount(
            String value
    ) {

        try {

            return Integer.parseInt(
                    value == null
                            ? "0"
                            : value.trim()
            );

        } catch (Exception e) {

            return 0;
        }
    }

    // =========================================================
    // EXCEPTION MESSAGE
    // =========================================================

    private String getExceptionMessage(
            Exception e
    ) {

        if (e == null) {
            return "Unknown error occurred.";
        }

        String message =
                e.getMessage();

        if (message == null ||
                message.trim().isEmpty()) {

            return "Unknown error occurred.";
        }

        return message;
    }

    // =========================================================
    // ALERT - ERROR
    // =========================================================

    private void showError(
            String title,
            String header,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // =========================================================
    // ALERT - WARNING
    // =========================================================

    private void showWarning(
            String title,
            String header,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // =========================================================
    // ALERT - INFORMATION
    // =========================================================

    private void showInformation(
            String title,
            String header,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // =========================================================
    // GROUP DATA
    // =========================================================

    private static class GroupData {

        String groupId;
        String name;
        String type;
        String memberCount;
        String description;

        String createdByEmail;
        String createdByName;
        String createdByFlat;

        boolean joined;

        List<MemberData> members =
                new ArrayList<>();

        GroupData(
                String groupId,
                String name,
                String type,
                String memberCount,
                String description,
                String createdByEmail,
                String createdByName,
                String createdByFlat,
                boolean joined
        ) {

            this.groupId =
                    groupId;

            this.name =
                    name;

            this.type =
                    type;

            this.memberCount =
                    memberCount;

            this.description =
                    description;

            this.createdByEmail =
                    createdByEmail;

            this.createdByName =
                    createdByName;

            this.createdByFlat =
                    createdByFlat;

            this.joined =
                    joined;
        }
    }

    // =========================================================
    // MEMBER DATA
    // =========================================================

    private static class MemberData {

        String email;
        String name;
        String flatNo;
        String role;

        MemberData(
                String email,
                String name,
                String flatNo,
                String role
        ) {

            this.email = email;
            this.name = name;
            this.flatNo = flatNo;
            this.role = role;
        }
    }
}
