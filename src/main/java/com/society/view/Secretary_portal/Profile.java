package com.society.view.Secretary_portal;

import java.util.List;

import com.society.dao.Welcome.UserDao;
import com.society.model.Welcome.User;
import com.society.view.ScreenSize;
import com.society.view.Welcome.LogInPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// =========================================================
// PROFILE
// =========================================================

public class Profile {

    // =========================================================
    // SCENE
    // =========================================================

    private Scene profileScene;

    // =========================================================
    // LOGGED IN USER
    // =========================================================

    private User loggedInUser;

    // =========================================================
    // EDIT FIELDS
    // =========================================================

    private TextField nameField;
    private TextField emailField;
    private TextField phoneField;
    private TextField dobField;

    private ComboBox<String> genderComboBox;

    private TextField societyField;
    private TextField addressField;

    // =========================================================
    // VIEW LABELS
    // =========================================================

    private Label viewNameValue;
    private Label viewEmailValue;
    private Label viewPhoneValue;
    private Label viewRoleValue;

    private Label viewSocietyValue;
    private Label viewMembersValue;
    private Label viewAddressValue;

    // =========================================================
    // LEFT PROFILE LABELS
    // =========================================================

    private Label profileInitialLabel;
    private Label profileNameLabel;
    private Label profileRoleLabel;
    private Label profileSocietyLabel;

    // =========================================================
    // BUTTONS
    // =========================================================

    private Button editBtn;
    private Button saveBtn;

    // =========================================================
    // IMPORTANT UI CONTAINERS
    // =========================================================

    private VBox personalView;
    private VBox personalEdit;

    private VBox societyView;
    private VBox societyEdit;

    private HBox saveBox;

    // =========================================================
    // COLORS
    // =========================================================

    private static final String PAGE_BG = "#F7F7F7";
    private static final String HEADER_BG = "#4e342e";
    private static final String PRIMARY = "#4e342e";

    // =========================================================
    // BACKGROUND IMAGE
    // =========================================================

    private static final String BACKGROUND_IMAGE =
            "/background-Dashboard5.jpeg";

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public Profile() {
    }

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Profile(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    // =========================================================
    // CREATE SCENE
    // =========================================================

    public Scene createScene(Stage stage) {

        // =====================================================
        // STEP 1: GET LOGGED-IN EMAIL
        // =====================================================

        String loggedEmail =
                UserDao.getLoggedInEmail();

        // =====================================================
        // STEP 2: IF USER OBJECT NULL, FETCH USING EMAIL
        // =====================================================

        if (loggedInUser == null) {

            if (loggedEmail != null &&
                    !loggedEmail.trim().isEmpty()) {

                UserDao dao =
                        new UserDao();

                loggedInUser =
                        dao.getUserByEmail(loggedEmail);
            }
        }

        // =====================================================
        // STEP 3: ALWAYS LOAD LATEST FIRESTORE DATA
        // =====================================================

        loadLatestUserData();

        // =====================================================
        // STEP 4: USER NOT FOUND
        // =====================================================

        if (loggedInUser == null) {

            showError(
                    "Profile Error",
                    "Logged-in secretary information not found."
            );

            return createEmptyScene(stage);
        }

        // =====================================================
        // SIDEBAR
        // =====================================================

        SecretarySidebar sidebarObj =
                new SecretarySidebar(loggedInUser);

        VBox sidebar =
                sidebarObj.createSidebar(stage);

        sidebar.setPrefWidth(280);
        sidebar.setMinWidth(280);
        sidebar.setMaxWidth(280);

        // =====================================================
        // MAIN CONTAINER
        // =====================================================

        VBox mainContainer =
                new VBox();

        mainContainer.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        // =====================================================
        // HEADER
        // =====================================================

        Label title =
                new Label("My Profile");

        title.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        Label subtitle =
                new Label(
                        "Manage your personal and society information"
                );

        subtitle.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:white;"
        );

        VBox headingBox =
                new VBox(3);

        headingBox.setAlignment(
                Pos.CENTER_LEFT
        );

        headingBox.getChildren().addAll(
                title,
                subtitle
        );

        // =====================================================
        // HEADER
        // =====================================================

        HBox header =
                new HBox();

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

        // FIXED HEADER HEIGHT
        header.setPrefHeight(80);
        header.setMinHeight(80);
        header.setMaxHeight(80);

        header.setMaxWidth(
                Double.MAX_VALUE
        );

        header.setStyle(
                "-fx-background-color:" +
                HEADER_BG + ";"
        );

        HBox.setHgrow(
                headingBox,
                Priority.ALWAYS
        );

        header.getChildren().add(
                headingBox
        );

        // =====================================================
        // CONTENT AREA
        // =====================================================

        VBox content =
                new VBox();

        content.setPadding(
                new Insets(
                        20,
                        25,
                        25,
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

        // =====================================================
        // PROFILE CARD
        // =====================================================

        HBox profileCard =
                new HBox(25);

        profileCard.setPadding(
                new Insets(
                        30,
                        32,
                        30,
                        32
                )
        );

        profileCard.setMaxWidth(
                Double.MAX_VALUE
        );

        profileCard.setStyle(
                "-fx-background-color:rgba(255,255,255,0.96);" +
                "-fx-background-radius:12;" +
                "-fx-border-color:#E2E2E2;" +
                "-fx-border-radius:12;"
        );

        // =====================================================
        // LEFT PROFILE
        // =====================================================

        VBox leftProfile =
                createLeftProfileSection(stage);

        leftProfile.setPrefWidth(300);
        leftProfile.setMinWidth(280);
        leftProfile.setMaxWidth(330);

        // =====================================================
        // RIGHT PROFILE
        // =====================================================

        VBox rightProfile =
                createRightProfileSection();

        HBox.setHgrow(
                rightProfile,
                Priority.ALWAYS
        );

        // =====================================================
        // ADD TO CARD
        // =====================================================

        profileCard.getChildren().addAll(
                leftProfile,
                rightProfile
        );

        // =====================================================
        // INVISIBLE SCROLL PANE
        // =====================================================

        ScrollPane profileScrollPane =
                new ScrollPane();

        profileScrollPane.setContent(
                profileCard
        );

        profileScrollPane.setFitToWidth(
                true
        );

        profileScrollPane.setFitToHeight(
                false
        );

        // =====================================================
        // INVISIBLE SCROLLBARS
        // =====================================================

        profileScrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        profileScrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        // =====================================================
        // TRANSPARENT SCROLLPANE
        // =====================================================

        profileScrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;" +
                "-fx-border-color:transparent;" +
                "-fx-padding:0;"
        );

        // =====================================================
        // SCROLLING ENABLED
        // =====================================================

        profileScrollPane.setPannable(
                true
        );

        VBox.setVgrow(
                profileScrollPane,
                Priority.ALWAYS
        );

        // =====================================================
        // CONTENT ADD
        // =====================================================

        content.getChildren().add(
                profileScrollPane
        );

        // =====================================================
        // BACKGROUND IMAGE
        // =====================================================

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
                        "-fx-background-color:" +
                        PAGE_BG + ";"
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "Background image could not be loaded: "
                            + e.getMessage()
            );

            content.setStyle(
                    "-fx-background-color:" +
                    PAGE_BG + ";"
            );
        }

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        mainContainer.getChildren().addAll(
                header,
                content
        );

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        // =====================================================
        // ROOT
        // =====================================================

        HBox pageRoot =
                new HBox();

        pageRoot.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        pageRoot.getChildren().addAll(
                sidebar,
                mainContainer
        );

        HBox.setHgrow(
                mainContainer,
                Priority.ALWAYS
        );

        // =====================================================
        // STACK ROOT
        // =====================================================

        StackPane root =
                new StackPane();

        root.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        root.getChildren().add(
                pageRoot
        );

        // =====================================================
        // SCENE
        // =====================================================

        profileScene =
                new Scene(
                        root,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        return profileScene;
    }

    // =========================================================
    // LEFT PROFILE SECTION
    // =========================================================

    private VBox createLeftProfileSection(
            Stage stage) {

        VBox box =
                new VBox(12);

        box.setAlignment(
                Pos.TOP_CENTER
        );

        box.setPadding(
                new Insets(2, 5, 2, 5)
        );

        // =====================================================
        // INITIAL
        // =====================================================

        profileInitialLabel =
                new Label(
                        getInitial()
                );

        profileInitialLabel.setPrefSize(
                300,
                145
        );

        profileInitialLabel.setMinSize(
                300,
                145
        );

        profileInitialLabel.setMaxSize(
                300,
                145
        );

        profileInitialLabel.setAlignment(
                Pos.CENTER
        );

        profileInitialLabel.setStyle(
                "-fx-background-color:#E5F7EC;" +
                "-fx-background-radius:90;" +
                "-fx-font-size:46px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#123C36;"
        );

        // =====================================================
        // NAME
        // =====================================================

        profileNameLabel =
                new Label(
                        getUserName()
                );

        profileNameLabel.setWrapText(
                true
        );

        profileNameLabel.setAlignment(
                Pos.CENTER
        );

        profileNameLabel.setMaxWidth(
                290
        );

        profileNameLabel.setStyle(
                "-fx-font-size:25px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#123C36;"
        );

        // =====================================================
        // ROLE
        // =====================================================

        profileRoleLabel =
                new Label(
                        getUserRole()
                );

        profileRoleLabel.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#888888;"
        );

        // =====================================================
        // SOCIETY
        // =====================================================

        profileSocietyLabel =
                new Label(
                        getUserSociety()
                );

        profileSocietyLabel.setWrapText(
                true
        );

        profileSocietyLabel.setAlignment(
                Pos.CENTER
        );

        profileSocietyLabel.setMaxWidth(
                280
        );

        profileSocietyLabel.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#444444;"
        );

        // =====================================================
        // SEPARATOR
        // =====================================================

        Region separator =
                new Region();

        separator.setPrefHeight(1);

        separator.setMaxWidth(270);

        separator.setStyle(
                "-fx-background-color:#D8D8D8;"
        );

        // =====================================================
        // BUTTON BOX
        // =====================================================

        VBox buttonBox =
                new VBox(10);

        buttonBox.setAlignment(
                Pos.CENTER
        );

        buttonBox.setPadding(
                new Insets(8, 0, 0, 0)
        );

        // =====================================================
        // EDIT BUTTON
        // =====================================================

        editBtn =
                new Button(
                        "Edit Profile"
                );

        editBtn.setPrefWidth(
                240
        );

        editBtn.setPrefHeight(
                46
        );

        editBtn.setStyle(
                "-fx-background-color:" + PRIMARY + ";" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        editBtn.setOnAction(
                e -> enableEditing(true)
        );

        // =====================================================
        // LOGOUT
        // =====================================================

        Button logoutBtn =
                new Button(
                        "Logout"
                );

        logoutBtn.setPrefWidth(
                240
        );

        logoutBtn.setPrefHeight(
                46
        );

        logoutBtn.setStyle(
                "-fx-background-color:#D9534F;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        logoutBtn.setOnAction(
                e -> handleLogout(stage)
        );

        buttonBox.getChildren().addAll(
                editBtn,
                logoutBtn
        );

        box.getChildren().addAll(
                profileInitialLabel,
                profileNameLabel,
                profileRoleLabel,
                profileSocietyLabel,
                separator,
                buttonBox
        );

        return box;
    }

    // =========================================================
    // RIGHT PROFILE SECTION
    // =========================================================

    private VBox createRightProfileSection() {

        VBox mainBox =
                new VBox(0);

        mainBox.setPadding(
                new Insets(2, 0, 2, 0)
        );

        mainBox.setMaxWidth(
                Double.MAX_VALUE
        );

        // =====================================================
        // PERSONAL TITLE
        // =====================================================

        Label personalTitle =
                new Label(
                        "Personal Information"
                );

        personalTitle.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#123C36;"
        );

        Region personalSeparator =
                createSeparator();

        personalView =
                createPersonalView();

        personalEdit =
                createPersonalEdit();

        // =====================================================
        // SOCIETY TITLE
        // =====================================================

        Label societyTitle =
                new Label(
                        "Society Information"
                );

        societyTitle.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#123C36;"
        );

        Region societySeparator =
                createSeparator();

        societyView =
                createSocietyView();

        societyEdit =
                createSocietyEdit();

        // =====================================================
        // SAVE BOX
        // =====================================================

        saveBox =
                new HBox();

        saveBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        saveBox.setPadding(
                new Insets(
                        18,
                        0,
                        0,
                        0
                )
        );

        saveBtn =
                new Button(
                        "Save Changes"
                );

        saveBtn.setPrefWidth(
                160
        );

        saveBtn.setPrefHeight(
                42
        );

        saveBtn.setStyle(
                "-fx-background-color:#2E7D32;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        saveBtn.setOnAction(
                e -> saveProfile()
        );

        saveBox.getChildren().add(
                saveBtn
        );

        // =====================================================
        // ADD
        // =====================================================

        mainBox.getChildren().addAll(

                personalTitle,
                personalSeparator,

                personalView,
                personalEdit,

                societyTitle,
                societySeparator,

                societyView,
                societyEdit,

                saveBox
        );

        // =====================================================
        // INITIAL VIEW
        // =====================================================

        personalView.setVisible(true);
        personalView.setManaged(true);

        personalEdit.setVisible(false);
        personalEdit.setManaged(false);

        societyView.setVisible(true);
        societyView.setManaged(true);

        societyEdit.setVisible(false);
        societyEdit.setManaged(false);

        saveBox.setVisible(false);
        saveBox.setManaged(false);

        return mainBox;
    }

    // =========================================================
    // PERSONAL VIEW
    // =========================================================

    private VBox createPersonalView() {

        VBox box =
                new VBox(14);

        box.setPadding(
                new Insets(
                        20,
                        0,
                        22,
                        0
                )
        );

        VBox nameBox =
                createViewField("Full Name");

        viewNameValue =
                getValueLabel(
                        getUserName()
                );

        nameBox.getChildren().add(
                viewNameValue
        );

        VBox emailBox =
                createViewField("Email");

        viewEmailValue =
                getValueLabel(
                        getUserEmail()
                );

        emailBox.getChildren().add(
                viewEmailValue
        );

        VBox phoneBox =
                createViewField("Phone");

        viewPhoneValue =
                getValueLabel(
                        getUserPhone()
                );

        phoneBox.getChildren().add(
                viewPhoneValue
        );

        VBox roleBox =
                createViewField("Role");

        viewRoleValue =
                getValueLabel(
                        getUserRole()
                );

        roleBox.getChildren().add(
                viewRoleValue
        );

        box.getChildren().addAll(
                nameBox,
                emailBox,
                phoneBox,
                roleBox
        );

        return box;
    }

    // =========================================================
    // SOCIETY VIEW
    // =========================================================

    private VBox createSocietyView() {

        VBox box =
                new VBox(14);

        box.setPadding(
                new Insets(
                        20,
                        0,
                        0,
                        0
                )
        );

        VBox societyBox =
                createViewField("Society");

        viewSocietyValue =
                getValueLabel(
                        getUserSociety()
                );

        societyBox.getChildren().add(
                viewSocietyValue
        );

        VBox membersBox =
                createViewField("Members");

        viewMembersValue =
                getValueLabel(
                        getMemberCount()
                );

        membersBox.getChildren().add(
                viewMembersValue
        );

        VBox addressBox =
                createViewField("Address");

        viewAddressValue =
                getValueLabel(
                        getUserAddress()
                );

        addressBox.getChildren().add(
                viewAddressValue
        );

        box.getChildren().addAll(
                societyBox,
                membersBox,
                addressBox
        );

        return box;
    }

    // =========================================================
    // PERSONAL EDIT
    // =========================================================

    private VBox createPersonalEdit() {

        VBox box =
                new VBox(10);

        box.setPadding(
                new Insets(
                        18,
                        0,
                        18,
                        0
                )
        );

        Label nameLabel =
                createFieldLabel("Full Name");

        nameField =
                new TextField(
                        getUserName()
                );

        styleTextField(nameField);

        Label emailLabel =
                createFieldLabel("Email");

        emailField =
                new TextField(
                        getUserEmail()
                );

        emailField.setEditable(false);
        emailField.setPrefHeight(40);
        emailField.setMaxWidth(
                Double.MAX_VALUE
        );

        emailField.setStyle(
                "-fx-background-color:#EEEEEE;" +
                "-fx-border-color:#DDDDDD;" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;" +
                "-fx-padding:8;"
        );

        Label phoneLabel =
                createFieldLabel("Mobile Number");

        phoneField =
                new TextField(
                        getUserPhone()
                );

        styleTextField(phoneField);

        Label dobLabel =
                createFieldLabel("Date of Birth");

        dobField =
                new TextField(
                        getUserDob()
                );

        styleTextField(dobField);

        Label genderLabel =
                createFieldLabel("Gender");

        genderComboBox =
                new ComboBox<>();

        genderComboBox.getItems().addAll(
                "Male",
                "Female",
                "Other"
        );

        String gender =
                getUserGender();

        if (gender != null &&
                !gender.trim().isEmpty()) {

            genderComboBox.setValue(
                    gender
            );
        }

        genderComboBox.setPrefHeight(40);

        genderComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        genderComboBox.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#DDDDDD;" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;"
        );

        // =====================================================
        // ROW 1
        // =====================================================

        VBox nameBox =
                createFieldBox(
                        nameLabel,
                        nameField
                );

        VBox emailBox =
                createFieldBox(
                        emailLabel,
                        emailField
                );

        HBox row1 =
                new HBox(15);

        HBox.setHgrow(
                nameBox,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                emailBox,
                Priority.ALWAYS
        );

        row1.getChildren().addAll(
                nameBox,
                emailBox
        );

        // =====================================================
        // ROW 2
        // =====================================================

        VBox phoneBox =
                createFieldBox(
                        phoneLabel,
                        phoneField
                );

        VBox dobBox =
                createFieldBox(
                        dobLabel,
                        dobField
                );

        HBox row2 =
                new HBox(15);

        HBox.setHgrow(
                phoneBox,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                dobBox,
                Priority.ALWAYS
        );

        row2.getChildren().addAll(
                phoneBox,
                dobBox
        );

        // =====================================================
        // ROW 3
        // =====================================================

        VBox genderBox =
                createFieldBox(
                        genderLabel,
                        genderComboBox
                );

        HBox.setHgrow(
                genderBox,
                Priority.ALWAYS
        );

        box.getChildren().addAll(
                row1,
                row2,
                genderBox
        );

        return box;
    }

    // =========================================================
    // SOCIETY EDIT
    // =========================================================

    private VBox createSocietyEdit() {

        VBox box =
                new VBox(10);

        box.setPadding(
                new Insets(
                        18,
                        0,
                        0,
                        0
                )
        );

        Label societyLabel =
                createFieldLabel("Society");

        societyField =
                new TextField(
                        getUserSociety()
                );

        societyField.setEditable(false);
        societyField.setPrefHeight(40);
        societyField.setMaxWidth(
                Double.MAX_VALUE
        );

        societyField.setStyle(
                "-fx-background-color:#EEEEEE;" +
                "-fx-border-color:#DDDDDD;" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;" +
                "-fx-padding:8;"
        );

        Label addressLabel =
                createFieldLabel("Address");

        addressField =
                new TextField(
                        getUserAddress()
                );

        styleTextField(addressField);

        box.getChildren().addAll(

                createFieldBox(
                        societyLabel,
                        societyField
                ),

                createFieldBox(
                        addressLabel,
                        addressField
                )
        );

        return box;
    }

    // =========================================================
    // ENABLE EDITING
    // =========================================================

    private void enableEditing(
            boolean enable) {

        if (nameField == null ||
                personalView == null ||
                personalEdit == null ||
                societyView == null ||
                societyEdit == null ||
                saveBox == null) {

            return;
        }

        nameField.setEditable(enable);
        phoneField.setEditable(enable);
        dobField.setEditable(enable);
        addressField.setEditable(enable);

        genderComboBox.setDisable(!enable);

        emailField.setEditable(false);
        societyField.setEditable(false);

        personalView.setVisible(!enable);
        personalView.setManaged(!enable);

        societyView.setVisible(!enable);
        societyView.setManaged(!enable);

        personalEdit.setVisible(enable);
        personalEdit.setManaged(enable);

        societyEdit.setVisible(enable);
        societyEdit.setManaged(enable);

        saveBox.setVisible(enable);
        saveBox.setManaged(enable);

        editBtn.setVisible(!enable);
        editBtn.setManaged(!enable);
    }

    // =========================================================
    // SAVE PROFILE
    // =========================================================

    private void saveProfile() {

        if (loggedInUser == null) {

            showError(
                    "Profile Error",
                    "Logged-in user information not found."
            );

            return;
        }

        String name =
                safeString(
                        nameField.getText()
                );

        String phone =
                safeString(
                        phoneField.getText()
                );

        String dob =
                safeString(
                        dobField.getText()
                );

        String gender =
                safeString(
                        genderComboBox.getValue()
                );

        String address =
                safeString(
                        addressField.getText()
                );

        String email =
                safeString(
                        emailField.getText()
                );

        String society =
                safeString(
                        societyField.getText()
                );

        if (email.isEmpty()) {

            showError(
                    "Profile Error",
                    "Logged-in email was not found."
            );

            return;
        }

        if (society.isEmpty()) {

            showError(
                    "Profile Error",
                    "Society information was not found."
            );

            return;
        }

        if (name.isEmpty()) {

            showError(
                    "Validation Error",
                    "Please enter your full name."
            );

            return;
        }

        if (!phone.isEmpty() &&
                !phone.matches("\\d{10}")) {

            showError(
                    "Validation Error",
                    "Mobile number must contain exactly 10 digits."
            );

            return;
        }

        UserDao dao =
                new UserDao();

        boolean updated =
                dao.updateSecretaryProfile(

                        email,
                        society,
                        name,
                        phone,
                        dob,
                        gender,
                        address
                );

        if (updated) {

            showSuccess(
                    "Profile Updated",
                    "Your profile has been updated successfully."
            );

            User latestUser =
                    dao.getUserByEmail(
                            email
                    );

            if (latestUser != null) {

                loggedInUser =
                        latestUser;
            }

            Stage stage =
                    (Stage) profileScene
                            .getWindow();

            Scene newScene =
                    createScene(stage);

            stage.setScene(
                    newScene
            );

            stage.show();

        } else {

            showError(
                    "Update Failed",
                    "Profile could not be updated. " +
                    "Please verify your society information."
            );
        }
    }

    // =========================================================
    // LOAD LATEST FIRESTORE DATA
    // =========================================================

    private void loadLatestUserData() {

        try {

            String email =
                    UserDao.getLoggedInEmail();

            if (email == null ||
                    email.trim().isEmpty()) {

                if (loggedInUser != null) {

                    email =
                            loggedInUser.getEmail();
                }
            }

            if (email == null ||
                    email.trim().isEmpty()) {

                return;
            }

            UserDao dao =
                    new UserDao();

            User latestUser =
                    dao.getUserByEmail(
                            email
                    );

            if (latestUser != null) {

                loggedInUser =
                        latestUser;
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to load latest profile data."
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // MEMBER COUNT
    // =========================================================

    private String getMemberCount() {

        try {

            String society =
                    getUserSociety();

            if (society == null ||
                    society.trim().isEmpty()) {

                return "0";
            }

            UserDao dao =
                    new UserDao();

            List<User> residents =
                    dao.getUsers("resident");

            if (residents == null) {

                return "0";
            }

            int count = 0;

            for (User resident : residents) {

                if (resident == null) {
                    continue;
                }

                String residentSociety =
                        safeString(
                                resident.getSociety()
                        );

                if (residentSociety.equalsIgnoreCase(
                        society
                )) {

                    count++;
                }
            }

            return String.valueOf(count);

        } catch (Exception e) {

            System.out.println(
                    "Unable to calculate member count."
            );

            e.printStackTrace();

            return "0";
        }
    }

    // =========================================================
    // USER NAME
    // =========================================================

    private String getUserName() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getName()
        );
    }

    // =========================================================
    // USER EMAIL
    // =========================================================

    private String getUserEmail() {

        if (loggedInUser == null) {
            return "";
        }

        String email =
                safeString(
                        loggedInUser.getEmail()
                );

        if (email.isEmpty()) {

            email =
                    safeString(
                            UserDao.getLoggedInEmail()
                    );
        }

        return email;
    }

    // =========================================================
    // USER PHONE
    // =========================================================

    private String getUserPhone() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getPhone()
        );
    }

    // =========================================================
    // USER DOB
    // =========================================================

    private String getUserDob() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getDob()
        );
    }

    // =========================================================
    // USER GENDER
    // =========================================================

    private String getUserGender() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getGender()
        );
    }

    // =========================================================
    // USER SOCIETY
    // =========================================================

    private String getUserSociety() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getSociety()
        );
    }

    // =========================================================
    // USER ADDRESS
    // =========================================================

    private String getUserAddress() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getAddress()
        );
    }

    // =========================================================
    // USER ROLE
    // =========================================================

    private String getUserRole() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getRole()
        );
    }

    // =========================================================
    // INITIAL
    // =========================================================

    private String getInitial() {

        String name =
                getUserName();

        if (name.isEmpty()) {

            return "";
        }

        return name
                .substring(0, 1)
                .toUpperCase();
    }

    // =========================================================
    // CREATE VIEW FIELD
    // =========================================================

    private VBox createViewField(
            String labelText) {

        VBox box =
                new VBox(3);

        Label label =
                createFieldLabel(
                        labelText
                );

        box.getChildren().add(
                label
        );

        return box;
    }

    // =========================================================
    // VALUE LABEL
    // =========================================================

    private Label getValueLabel(
            String value) {

        Label label =
                new Label(
                        value == null
                                ? ""
                                : value
                );

        label.setWrapText(true);

        label.setMaxWidth(
                Double.MAX_VALUE
        );

        label.setStyle(
                "-fx-font-size:15px;" +
                "-fx-text-fill:#555555;"
        );

        return label;
    }

    // =========================================================
    // FIELD BOX
    // =========================================================

    private VBox createFieldBox(
            Label label,
            Node field) {

        VBox box =
                new VBox(5);

        box.setMaxWidth(
                Double.MAX_VALUE
        );

        box.getChildren().addAll(
                label,
                field
        );

        return box;
    }

    // =========================================================
    // FIELD LABEL
    // =========================================================

    private Label createFieldLabel(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#666666;"
        );

        return label;
    }

    // =========================================================
    // TEXT FIELD STYLE
    // =========================================================

    private void styleTextField(
            TextField field) {

        field.setPrefHeight(40);

        field.setMaxWidth(
                Double.MAX_VALUE
        );

        field.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#DDDDDD;" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;" +
                "-fx-padding:8;"
        );
    }

    // =========================================================
    // SEPARATOR
    // =========================================================

    private Region createSeparator() {

        Region separator =
                new Region();

        separator.setPrefHeight(1);

        separator.setMaxWidth(
                Double.MAX_VALUE
        );

        separator.setStyle(
                "-fx-background-color:#D8D8D8;"
        );

        return separator;
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safeString(
            String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void handleLogout(
            Stage stage) {

        try {

            UserDao.clearLoggedInUser();

            LogInPage loginPage =
                    new LogInPage();

            Scene loginScene =
                    loginPage.createScene(
                            stage
                    );

            stage.setScene(
                    loginScene
            );

            stage.setMaximized(
                    true
            );

            stage.show();

        } catch (Exception e) {

            System.out.println(
                    "Logout error:"
            );

            e.printStackTrace();

            showError(
                    "Logout Error",
                    "Unable to logout."
            );
        }
    }

    // =========================================================
    // SUCCESS ALERT
    // =========================================================

    private void showSuccess(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

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

    // =========================================================
    // ERROR ALERT
    // =========================================================

    private void showError(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

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

    // =========================================================
    // EMPTY SCENE
    // =========================================================

    private Scene createEmptyScene(
            Stage stage) {

        VBox box =
                new VBox(15);

        box.setAlignment(
                Pos.CENTER
        );

        box.setStyle(
                "-fx-background-color:" +
                PAGE_BG + ";"
        );

        Label label =
                new Label(
                        "Profile information not available."
                );

        label.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;"
        );

        Button loginButton =
                new Button(
                        "Go to Login"
                );

        loginButton.setPrefWidth(
                140
        );

        loginButton.setPrefHeight(
                38
        );

        loginButton.setStyle(
                "-fx-background-color:" +
                PRIMARY + ";" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        loginButton.setOnAction(e -> {

            UserDao.clearLoggedInUser();

            LogInPage loginPage =
                    new LogInPage();

            stage.setScene(
                    loginPage.createScene(stage)
            );

            stage.setMaximized(
                    true
            );

            stage.show();

        });

        box.getChildren().addAll(
                label,
                loginButton
        );

        return new Scene(
                box,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }
}