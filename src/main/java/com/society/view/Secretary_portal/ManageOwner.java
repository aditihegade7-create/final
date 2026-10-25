package com.society.view.Secretary_portal;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.society.controller.Secretary_Controller.OwnerController;
import com.society.model.Secretary_model.Owner;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ManageOwner {

    private Scene ownerScene;
    private StackPane rootStack;
    private OwnerController ownerController;

    private VBox ownerList;
    private TextField search;

    private List<Owner> allOwners = new ArrayList<>();

    private static final String BACKGROUND_IMAGE =
            "/background-Dashboard5.jpeg";

    private static final String PAGE_BACKGROUND =
            "#b3adad";

    public Scene createScene(Stage stage) {

        ownerController = new OwnerController();

        /* ================= SIDEBAR ================= */

        SecretarySidebar sidebarObj = new SecretarySidebar();
        VBox sidebar = sidebarObj.createSidebar(stage);

        /* ================= MAIN ================= */

        VBox mainvb = new VBox(10);

        mainvb.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        /*
         * Header touches sidebar from left
         * and screen edge from right.
         */
        mainvb.setPadding(
                new Insets(0, 0, 20, 0)
        );

        mainvb.setSpacing(10);

        /* ================= HEADER ================= */

        HBox header = new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setPrefHeight(75);

        header.setMaxWidth(
                Double.MAX_VALUE
        );

        header.setStyle(
                "-fx-background-color: #4e342e;" +
                "-fx-padding:15 20 15 20;"
        );

        VBox greetingBox =
                new VBox(3);
Label greeting =
        new Label(
                "Manage Owner"
        );
        greeting.setStyle( "-fx-font-family:'Bodoni MT';" );

        greeting.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22
                )
        );

        greeting.setStyle(
                "-fx-text-fill:white;"
        );

        Label welcomeText =
        new Label(
                "Manage your society owners"
        );

        welcomeText.setStyle( "-fx-font-family:'Bodoni MT';" );

welcomeText.setFont(
        Font.font(
                "Arial",
                FontWeight.NORMAL,
                13
        )
);

welcomeText.setStyle(
        "-fx-text-fill:#D7CCC8;"
        );

        

        greetingBox.getChildren().addAll(
                greeting,
                welcomeText
        );

        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );

        VBox notificationBox =
                new VBox();

        notificationBox.setAlignment(
                Pos.CENTER
        );

        notificationBox.setPadding(
                new Insets(0, 15, 0, 10)
        );

        Label notification =
                new Label("🔔");

        notification.setFont(
                Font.font(22)
        );

        notification.setStyle(
                "-fx-text-fill:white;"
        );

        notification.setTooltip(
                new Tooltip("Notifications")
        );

        notificationBox.getChildren().add(
                notification
        );

        VBox dateBox =
                new VBox(2);

        dateBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        LocalDate today =
                LocalDate.now();

        String day =
                today.format(
                        DateTimeFormatter.ofPattern("EEEE")
                );

        String date =
                today.format(
                        DateTimeFormatter.ofPattern(
                                "dd MMMM yyyy"
                        )
                );

        Label dayLabel =
                new Label(day);

        dayLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13
                )
        );

        dayLabel.setStyle(
                "-fx-text-fill:white;"
        );

        Label dateLabel =
                new Label(date);

        dateLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.NORMAL,
                        12
                )
        );

        dateLabel.setStyle(
                "-fx-text-fill:#D7CCC8;"
        );

        dateBox.getChildren().addAll(
                dayLabel,
                dateLabel
        );

        header.getChildren().addAll(
                greetingBox,
                headerSpacer,
                notificationBox,
                dateBox
        );

        /* ================= PAGE TITLE ================= */

        

        /* ================= SEARCH BAR ================= */

        search =
                new TextField();

        search.setPromptText(
                "Search owner, flat, mobile, email, society..."
        );

        search.setPrefHeight(45);

        search.setMaxWidth(
                Double.MAX_VALUE
        );

        search.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#D7CCC8;" +
                "-fx-border-radius:10;" +
                "-fx-border-width:1;" +
                "-fx-padding:0 15 0 15;" +
                "-fx-font-size:14;"
        );

        Button addOwnerBtn =
                new Button(
                        "+ Add New Owner"
                );

        addOwnerBtn.setPrefHeight(45);

        addOwnerBtn.setStyle(
                "-fx-background-color:#6D4C41;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:10;" +
                "-fx-padding:0 18 0 18;" +
                "-fx-cursor:hand;"
        );

        Button refreshBtn =
                new Button("⟳");

        refreshBtn.setPrefSize(
                45,
                45
        );

        refreshBtn.setStyle(
                "-fx-background-color:#5D4037;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:20;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:10;" +
                "-fx-cursor:hand;"
        );

        refreshBtn.setTooltip(
                new Tooltip("Refresh")
        );

        HBox searchBox =
                new HBox(10);

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        /*
         * Left gap from sidebar.
         * Right gap from screen edge.
         */
        searchBox.setPadding(
                new Insets(0, 20, 0, 15)
        );

        /*
         * Search bar slightly lower.
         */
        VBox.setMargin(
                searchBox,
                new Insets(10, 0, 0, 0)
        );

        HBox.setHgrow(
                search,
                Priority.ALWAYS
        );

        searchBox.getChildren().addAll(
                search,
                addOwnerBtn,
                refreshBtn
        );

        /* ================= OWNER LIST ================= */

        ownerList =
                new VBox(15);

        ownerList.setFillWidth(
                true
        );

        ownerList.setPadding(
                new Insets(0, 20, 0, 15)
        );

        /* ================= INVISIBLE SCROLL PANE ================= */

        ScrollPane scrollPane =
                new ScrollPane(ownerList);

        scrollPane.setFitToWidth(
                true
        );

        scrollPane.setFitToHeight(
                false
        );

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setPannable(
                true
        );

        scrollPane.setMaxWidth(
                Double.MAX_VALUE
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

        /* ================= CONTENT AREA ================= */

        VBox contentArea =
                new VBox(5);

        contentArea.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        /*
         * Right side spacing for content.
         */
        contentArea.setPadding(
                new Insets(0, 0, 0, 0)
        );

        contentArea.getChildren().addAll(
               
                searchBox,
                scrollPane
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        /* ================= BACKGROUND IMAGE ================= */

        try {

            Image bgImage =
                    new Image(
                            getClass()
                                    .getResource(
                                            BACKGROUND_IMAGE
                                    )
                                    .toExternalForm()
                    );

            BackgroundImage backgroundImage =
                    new BackgroundImage(
                            bgImage,
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

            contentArea.setBackground(
                    new Background(
                            backgroundImage
                    )
            );

        } catch (Exception e) {

            contentArea.setStyle(
                    "-fx-background-color:" +
                    PAGE_BACKGROUND +
                    ";"
            );
        }

        /* ================= MAIN ROOT ================= */

        HBox mainRoot =
                new HBox();

        mainRoot.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        HBox.setHgrow(
                mainvb,
                Priority.ALWAYS
        );

        mainvb.getChildren().addAll(
                header,
                contentArea
        );

        VBox.setVgrow(
                contentArea,
                Priority.ALWAYS
        );

        mainRoot.getChildren().addAll(
                sidebar,
                mainvb
        );

        /* ================= ROOT STACK ================= */

        rootStack =
                new StackPane();

        rootStack.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        rootStack.getChildren().add(
                mainRoot
        );

        /* ================= BUTTON ACTIONS ================= */

        addOwnerBtn.setOnAction(
                e -> openAddOwnerDialog()
        );

        refreshBtn.setOnAction(e -> {

            search.clear();
            loadOwners();

        });

        search.textProperty().addListener(
                (obs, oldValue, newValue) ->
                        filterOwners(newValue)
        );

        /* ================= LOAD DATA ================= */

        loadOwners();

        /* ================= SCENE ================= */

        ownerScene =
                new Scene(
                        rootStack,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        return ownerScene;
    }

    /* =========================================================
                         LOAD OWNERS
       ========================================================= */

    private void loadOwners() {

        ownerList.getChildren().clear();

        allOwners =
                ownerController.getOwnersBySociety();

        if (allOwners == null) {
            allOwners = new ArrayList<>();
        }

        if (allOwners.isEmpty()) {

            showEmptyMessage(
                    "No owners found in your society."
            );

            return;
        }

        for (Owner owner : allOwners) {

            ownerList.getChildren().add(
                    createOwnerRow(owner)
            );
        }
    }

    /* =========================================================
                         FILTER OWNERS
       ========================================================= */

    private void filterOwners(
            String searchText
    ) {

        ownerList.getChildren().clear();

        String text =
                searchText == null
                        ? ""
                        : searchText
                                .trim()
                                .toLowerCase();

        if (text.isEmpty()) {

            for (Owner owner : allOwners) {

                ownerList.getChildren().add(
                        createOwnerRow(owner)
                );
            }

            return;
        }

        boolean found = false;

        for (Owner owner : allOwners) {

            String name =
                    safe(owner.getName())
                            .toLowerCase();

            String flat =
                    safe(owner.getFlat())
                            .toLowerCase();

            String phone =
                    safe(owner.getPhone())
                            .toLowerCase();

            String email =
                    safe(owner.getEmail())
                            .toLowerCase();

            String status =
                    safe(owner.getStatus())
                            .toLowerCase();

            String society =
                    safe(owner.getSociety())
                            .toLowerCase();

            if (
                    name.contains(text)
                    || flat.contains(text)
                    || phone.contains(text)
                    || email.contains(text)
                    || status.contains(text)
                    || society.contains(text)
            ) {

                ownerList.getChildren().add(
                        createOwnerRow(owner)
                );

                found = true;
            }
        }

        if (!found) {

            showEmptyMessage(
                    "No matching owners found."
            );
        }
    }

    /* =========================================================
                         OWNER ROW
       ========================================================= */

    private HBox createOwnerRow(
            Owner owner
    ) {

        HBox row =
                new HBox(18);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPrefHeight(90);

        row.setMinHeight(90);

        row.setMaxWidth(
                Double.MAX_VALUE
        );

        row.setPadding(
                new Insets(
                        12,
                        15,
                        12,
                        15
                )
        );

        row.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:12;" +
                "-fx-border-color:#E0D6D2;" +
                "-fx-border-radius:12;"
        );

        Label profile =
                new Label("👤");

        profile.setFont(
                Font.font(30)
        );

        /* ================= NAME ================= */

        VBox nameBox =
                new VBox(4);

        nameBox.setPrefWidth(210);
        nameBox.setMinWidth(210);

        Label name =
                new Label(
                        safe(owner.getName())
                );

        name.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16
                )
        );

        name.setStyle(
                "-fx-text-fill:#3E2723;"
        );

        Label email =
                new Label(
                        safe(owner.getEmail())
                );

        email.setFont(
                Font.font(
                        "Arial",
                        FontWeight.NORMAL,
                        12
                )
        );

        email.setStyle(
                "-fx-text-fill:#795548;"
        );

        nameBox.getChildren().addAll(
                name,
                email
        );

        /* ================= FLAT ================= */

        VBox flatBox =
                new VBox(3);

        flatBox.setPrefWidth(90);
        flatBox.setMinWidth(90);

        Label flatTitle =
                new Label("Flat");

        flatTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11
                )
        );

        flatTitle.setStyle(
                "-fx-text-fill:#8D6E63;"
        );

        Label flat =
                new Label(
                        safe(owner.getFlat())
                );

        flat.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13
                )
        );

        flat.setStyle(
                "-fx-text-fill:#3E2723;"
        );

        flatBox.getChildren().addAll(
                flatTitle,
                flat
        );

        /* ================= MOBILE ================= */

        VBox phoneBox =
                new VBox(3);

        phoneBox.setPrefWidth(130);
        phoneBox.setMinWidth(130);

        Label phoneTitle =
                new Label("Mobile");

        phoneTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11
                )
        );

        phoneTitle.setStyle(
                "-fx-text-fill:#8D6E63;"
        );

        Label phone =
                new Label(
                        safe(owner.getPhone())
                );

        phone.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13
                )
        );

        phone.setStyle(
                "-fx-text-fill:#3E2723;"
        );

        phoneBox.getChildren().addAll(
                phoneTitle,
                phone
        );

        /* ================= SOCIETY ================= */

        VBox societyBox =
                new VBox(3);

        societyBox.setPrefWidth(145);
        societyBox.setMinWidth(145);

        Label societyTitle =
                new Label("Society");

        societyTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11
                )
        );

        societyTitle.setStyle(
                "-fx-text-fill:#8D6E63;"
        );

        Label society =
                new Label(
                        safe(owner.getSociety())
                );

        society.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13
                )
        );

        society.setStyle(
                "-fx-text-fill:#3E2723;"
        );

        societyBox.getChildren().addAll(
                societyTitle,
                society
        );

        /* ================= STATUS ================= */

        VBox statusBox =
                new VBox(3);

        statusBox.setPrefWidth(90);
        statusBox.setMinWidth(90);

        Label statusTitle =
                new Label("Status");

        statusTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11
                )
        );

        statusTitle.setStyle(
                "-fx-text-fill:#8D6E63;"
        );

        Label status =
                new Label(
                        safe(owner.getStatus())
                );

        status.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13
                )
        );

        status.setStyle(
                "-fx-text-fill:#2E7D32;"
        );

        statusBox.getChildren().addAll(
                statusTitle,
                status
        );

        /*
         * Status slightly towards left.
         */
        Region spacer =
                new Region();

        spacer.setPrefWidth(5);
        spacer.setMinWidth(5);

        row.getChildren().addAll(
                profile,
                nameBox,
                flatBox,
                phoneBox,
                societyBox,
                spacer,
                statusBox
        );

        return row;
    }

    /* =========================================================
                         ADD OWNER DIALOG
       ========================================================= */

    private void openAddOwnerDialog() {

        StackPane overlay =
                new StackPane();

        overlay.setStyle(
                "-fx-background-color:" +
                "rgba(0,0,0,0.45);"
        );

        VBox formBox =
                new VBox(12);

        formBox.setPrefWidth(430);

        formBox.setMaxHeight(
                Math.min(
                        ScreenSize.getHeight() - 40,
                        600
                )
        );

        formBox.setPadding(
                new Insets(25)
        );

        formBox.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:15;"
        );

        Label formTitle =
                new Label(
                        "Add New Owner"
                );

        formTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22
                )
        );

        formTitle.setStyle(
                "-fx-text-fill:#3E2723;"
        );

        TextField societyField =
                new TextField();

        societyField.setPromptText(
                "Society"
        );

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Owner Name"
        );

        TextField flatField =
                new TextField();

        flatField.setPromptText(
                "Flat Number"
        );

        TextField phoneField =
                new TextField();

        phoneField.setPromptText(
                "Phone"
        );

        TextField emailField =
                new TextField();

        emailField.setPromptText(
                "Email"
        );

        TextField statusField =
                new TextField();

        statusField.setPromptText(
                "Status"
        );

        VBox societyBox =
                new VBox(
                        5,
                        createFormLabel("Society"),
                        societyField
                );

        VBox nameBox =
                new VBox(
                        5,
                        createFormLabel("Owner Name"),
                        nameField
                );

        VBox flatBox =
                new VBox(
                        5,
                        createFormLabel("Flat Number"),
                        flatField
                );

        VBox phoneBox =
                new VBox(
                        5,
                        createFormLabel("Phone"),
                        phoneField
                );

        VBox emailBox =
                new VBox(
                        5,
                        createFormLabel("Email"),
                        emailField
                );

        VBox statusBox =
                new VBox(
                        5,
                        createFormLabel("Status"),
                        statusField
                );

        Button saveBtn =
                new Button(
                        "Save Owner"
                );

        saveBtn.setPrefHeight(42);

        saveBtn.setMaxWidth(
                Double.MAX_VALUE
        );

        saveBtn.setStyle(
                "-fx-background-color:#6D4C41;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        Button cancelBtn =
                new Button(
                        "Cancel"
                );

        cancelBtn.setPrefHeight(42);

        cancelBtn.setMaxWidth(
                Double.MAX_VALUE
        );

        cancelBtn.setStyle(
                "-fx-background-color:#E0E0E0;" +
                "-fx-text-fill:#3E2723;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        saveBtn.setOnAction(e -> {

            String society =
                    societyField
                            .getText()
                            .trim();

            String name =
                    nameField
                            .getText()
                            .trim();

            String flat =
                    flatField
                            .getText()
                            .trim();

            String phone =
                    phoneField
                            .getText()
                            .trim();

            String email =
                    emailField
                            .getText()
                            .trim();

            String status =
                    statusField
                            .getText()
                            .trim();

            if (
                    society.isEmpty()
                    || name.isEmpty()
                    || flat.isEmpty()
                    || phone.isEmpty()
                    || email.isEmpty()
                    || status.isEmpty()
            ) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation",
                        "Please fill all fields."
                );

                return;
            }

            if (
                    !email.contains("@")
                    || !email.contains(".")
            ) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Email",
                        "Please enter a valid email address."
                );

                return;
            }

            boolean success =
                    ownerController.addOwner(
                            name,
                            flat,
                            phone,
                            email,
                            status,
                            society
                    );

            if (success) {

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Owner added successfully."
                );

                removeOverlay(overlay);

                societyField.clear();
                nameField.clear();
                flatField.clear();
                phoneField.clear();
                emailField.clear();
                statusField.clear();

                loadOwners();

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Unable to add owner. " +
                        "Email may already exist."
                );
            }
        });

        cancelBtn.setOnAction(
                e -> removeOverlay(overlay)
        );

        formBox.getChildren().addAll(
                formTitle,
                societyBox,
                nameBox,
                flatBox,
                phoneBox,
                emailBox,
                statusBox,
                saveBtn,
                cancelBtn
        );

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

    /* =========================================================
                         FORM LABEL
       ========================================================= */

    private Label createFormLabel(
            String text
    ) {

        Label label =
                new Label(text);

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12
                )
        );

        label.setStyle(
                "-fx-text-fill:#5D4037;"
        );

        return label;
    }

    /* =========================================================
                         REMOVE OVERLAY
       ========================================================= */

    private void removeOverlay(
            StackPane overlay
    ) {

        rootStack
                .getChildren()
                .remove(overlay);
    }

    /* =========================================================
                         EMPTY MESSAGE
       ========================================================= */

    private void showEmptyMessage(
            String message
    ) {

        Label label =
                new Label(message);

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        label.setStyle(
                "-fx-text-fill:#5D4037;"
        );

        label.setAlignment(
                Pos.CENTER
        );

        label.setMaxWidth(
                Double.MAX_VALUE
        );

        VBox.setMargin(
                label,
                new Insets(
                        20,
                        0,
                        0,
                        15
                )
        );

        ownerList.getChildren().add(
                label
        );
    }

    /* =========================================================
                         ALERT
       ========================================================= */

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

    /* =========================================================
                         SAFE STRING
       ========================================================= */

    private String safe(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }

    /* =========================================================
                         GREETING
       ========================================================= */

    private String getGreeting() {

        int hour =
                LocalTime.now().getHour();

        if (hour >= 5 && hour < 12) {

            return "Good Morning";

        } else if (hour >= 12 && hour < 17) {

            return "Good Afternoon";

        } else if (hour >= 17 && hour < 21) {

            return "Good Evening";

        } else {

            return "Good Night";
        }
    }
}