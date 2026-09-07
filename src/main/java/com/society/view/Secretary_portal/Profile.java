package com.society.view.Secretary_portal;

import java.util.List;

import com.society.dao.Welcome.UserDao;
import com.society.model.Welcome.User;
import com.society.view.ScreenSize;
import com.society.view.Welcome.LogInPage;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Profile {

    private Scene profileScene;
    private User loggedInUser;

    private TextField nameField;
    private TextField emailField;
    private TextField phoneField;
    private TextField dobField;

    private ComboBox<String> genderComboBox;

    private TextField societyField;
    private TextField addressField;

    private Label viewNameValue;
    private Label viewEmailValue;
    private Label viewPhoneValue;
    private Label viewRoleValue;

    private Label viewSocietyValue;
    private Label viewMembersValue;
    private Label viewAddressValue;

    private Label profileInitialLabel;
    private Label profileNameLabel;
    private Label profileRoleLabel;
    private Label profileSocietyLabel;

    private Button editBtn;
    private Button saveBtn;

    private VBox personalView;
    private VBox personalEdit;

    private VBox societyView;
    private VBox societyEdit;

    private HBox saveBox;

    // =========================================================
    // CONSTRUCTORS
    // =========================================================

    public Profile() {
    }

    public Profile(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    // =========================================================
    // CREATE SCENE
    // =========================================================

    public Scene createScene(Stage stage) {

        String loggedEmail = UserDao.getLoggedInEmail();

        if (loggedInUser == null) {

            if (loggedEmail != null &&
                    !loggedEmail.trim().isEmpty()) {

                UserDao dao = new UserDao();

                loggedInUser =
                        dao.getUserByEmail(loggedEmail);
            }
        }

        loadLatestUserData();

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

        // =====================================================
        // HEADER
        // =====================================================

        HBox header = new HBox();

        header.setPrefHeight(80);
        header.setMinHeight(80);
        header.setMaxHeight(80);

        header.setPadding(
                new Insets(18, 25, 18, 25)
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setStyle(
                "-fx-background-color:#593a32;"
        );

        Label headerTitle =
                new Label("PROFILE");

        headerTitle.setStyle(
                "-fx-font-size:25px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );

        Label headerText =
                new Label("Secretary Profile");

        headerText.setStyle(
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        header.getChildren().addAll(
                headerTitle,
                headerSpacer,
                headerText
        );

        // =====================================================
        // MAIN CONTAINER
        // =====================================================

        VBox mainContainer =
                new VBox(10);

        mainContainer.setPadding(
                new Insets(20, 25, 20, 25)
        );

        mainContainer.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        mainContainer.setStyle(
                "-fx-background-color:#B3ADAD;"
        );

        // =====================================================
        // HEADING
        // =====================================================

        Label heading =
                new Label("PROFILE");

        heading.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#333333;"
        );

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label("My Profile");

        title.setStyle(
                "-fx-font-size:25px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#123C36;"
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        Label subtitle =
                new Label(
                        "Manage your personal and society information"
                );

        subtitle.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#777777;"
        );

        // =====================================================
        // PROFILE CARD
        // =====================================================

        HBox profileCard =
                new HBox(25);

        profileCard.setPadding(
                new Insets(30, 32, 30, 32)
        );

        profileCard.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        profileCard.setStyle(
                "-fx-background-color:white;" +
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

        profileCard.getChildren().addAll(
                leftProfile,
                rightProfile
        );

        VBox.setVgrow(
                profileCard,
                Priority.ALWAYS
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        mainContainer.getChildren().addAll(
                heading,
                title,
                subtitle,
                profileCard
        );

        VBox.setVgrow(
                mainContainer,
                Priority.ALWAYS
        );

        // =====================================================
        // CONTENT WRAPPER
        // =====================================================

        VBox contentWrapper =
                new VBox();

        contentWrapper.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        contentWrapper.setStyle(
                "-fx-background-color:#B3ADAD;"
        );

        contentWrapper.getChildren().addAll(
                header,
                mainContainer
        );

        VBox.setVgrow(
                mainContainer,
                Priority.ALWAYS
        );

        // =====================================================
        // ROOT
        // =====================================================

        BorderPane root =
                new BorderPane();

        root.setLeft(sidebar);
        root.setCenter(contentWrapper);

        root.setStyle(
                "-fx-background-color:#B3ADAD;"
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

        // =====================================================
        // DOWN → UP ANIMATION
        // =====================================================

        playProfileAnimation(
                heading,
                title,
                subtitle,
                profileCard
        );

        return profileScene;
    }

    // =========================================================
    // LEFT PROFILE SECTION
    // =========================================================

    private VBox createLeftProfileSection(Stage stage) {

        VBox box =
                new VBox(12);

        box.setAlignment(
                Pos.TOP_CENTER
        );

        box.setPadding(
                new Insets(2, 5, 2, 5)
        );

        // =====================================================
        // PROFILE INITIAL
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

        profileNameLabel.setWrapText(true);

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

        profileSocietyLabel.setWrapText(true);

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
                new Button("Edit Profile");

        editBtn.setPrefWidth(240);
        editBtn.setPrefHeight(46);

        editBtn.setStyle(
                "-fx-background-color:#593a32;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;"
        );

        addPopupAnimation(editBtn);

        editBtn.setOnAction(e ->
                enableEditing(true)
        );

        // =====================================================
        // LOGOUT BUTTON
        // =====================================================

        Button logoutBtn =
                new Button("Logout");

        logoutBtn.setPrefWidth(240);
        logoutBtn.setPrefHeight(46);

        logoutBtn.setStyle(
                "-fx-background-color:#D9534F;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;"
        );

        addPopupAnimation(logoutBtn);

        logoutBtn.setOnAction(e ->
                handleLogout(stage)
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
                new Insets(18, 0, 0, 0)
        );

        // =====================================================
        // SAVE BUTTON
        // =====================================================

        saveBtn =
                new Button("Save Changes");

        saveBtn.setPrefWidth(160);
        saveBtn.setPrefHeight(42);

        saveBtn.setStyle(
                "-fx-background-color:#2E7D32;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;"
        );

        addPopupAnimation(saveBtn);

        saveBtn.setOnAction(e ->
                saveProfile()
        );

        saveBox.getChildren().add(
                saveBtn
        );

        // =====================================================
        // ADD CONTENT
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
        // INITIAL VIEW MODE
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
                new Insets(20, 0, 22, 0)
        );

        VBox nameBox =
                createViewField("Full Name");

        viewNameValue =
                getValueLabel(getUserName());

        nameBox.getChildren().add(
                viewNameValue
        );

        VBox emailBox =
                createViewField("Email");

        viewEmailValue =
                getValueLabel(getUserEmail());

        emailBox.getChildren().add(
                viewEmailValue
        );

        VBox phoneBox =
                createViewField("Phone");

        viewPhoneValue =
                getValueLabel(getUserPhone());

        phoneBox.getChildren().add(
                viewPhoneValue
        );

        VBox roleBox =
                createViewField("Role");

        viewRoleValue =
                getValueLabel(getUserRole());

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
                new Insets(20, 0, 0, 0)
        );

        VBox societyBox =
                createViewField("Society");

        viewSocietyValue =
                getValueLabel(getUserSociety());

        societyBox.getChildren().add(
                viewSocietyValue
        );

        VBox membersBox =
                createViewField("Members");

        viewMembersValue =
                getValueLabel(getMemberCount());

        membersBox.getChildren().add(
                viewMembersValue
        );

        VBox addressBox =
                createViewField("Address");

        viewAddressValue =
                getValueLabel(getUserAddress());

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
                new Insets(18, 0, 18, 0)
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
                new Insets(18, 0, 0, 0)
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

        // =====================================================
        // VALIDATION
        // =====================================================

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

        // =====================================================
        // DAO
        // =====================================================

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

        // =====================================================
        // SUCCESS
        // =====================================================

        if (updated) {

            showSuccess(
                    "Profile Updated",
                    "Your profile has been updated successfully."
            );

            User latestUser =
                    dao.getUserByEmail(email);

            if (latestUser != null) {

                loggedInUser =
                        latestUser;
            }

            Stage stage =
                    (Stage) profileScene.getWindow();

            Scene newScene =
                    createScene(stage);

            stage.setScene(newScene);

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
    // LOAD LATEST USER DATA
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
                    dao.getUserByEmail(email);

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
    // USER GETTERS
    // =========================================================

    private String getUserName() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getName()
        );
    }

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

    private String getUserPhone() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getPhone()
        );
    }

    private String getUserDob() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getDob()
        );
    }

    private String getUserGender() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getGender()
        );
    }

    private String getUserSociety() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getSociety()
        );
    }

    private String getUserAddress() {

        if (loggedInUser == null) {
            return "";
        }

        return safeString(
                loggedInUser.getAddress()
        );
    }

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
    // VIEW FIELD
    // =========================================================

    private VBox createViewField(
            String labelText) {

        VBox box =
                new VBox(3);

        Label label =
                createFieldLabel(labelText);

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
            javafx.scene.Node field) {

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
    // BUTTON POPUP ANIMATION
    // =========================================================

    private void addPopupAnimation(
            Button button) {

        button.setOnMouseEntered(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            scale.setToX(1.05);
            scale.setToY(1.05);

            scale.play();
        });

        button.setOnMouseExited(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            scale.setToX(1.0);
            scale.setToY(1.0);

            scale.play();
        });

        button.setOnMousePressed(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(80),
                            button
                    );

            scale.setToX(0.97);
            scale.setToY(0.97);

            scale.play();
        });

        button.setOnMouseReleased(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(100),
                            button
                    );

            scale.setToX(1.05);
            scale.setToY(1.05);

            scale.play();
        });
    }

    // =========================================================
    // DOWN → UP PROFILE ANIMATION
    // =========================================================

    private void playProfileAnimation(
            javafx.scene.Node heading,
            javafx.scene.Node title,
            javafx.scene.Node subtitle,
            javafx.scene.Node profileCard) {

        // =====================================================
        // INITIAL POSITION
        // =====================================================

        heading.setOpacity(0);
        heading.setTranslateY(35);

        title.setOpacity(0);
        title.setTranslateY(40);

        subtitle.setOpacity(0);
        subtitle.setTranslateY(45);

        profileCard.setOpacity(0);
        profileCard.setTranslateY(55);

        // =====================================================
        // HEADING ANIMATION
        // =====================================================

        FadeTransition fadeHeading =
                new FadeTransition(
                        Duration.millis(350),
                        heading
                );

        fadeHeading.setFromValue(0);
        fadeHeading.setToValue(1);

        TranslateTransition moveHeading =
                new TranslateTransition(
                        Duration.millis(350),
                        heading
                );

        moveHeading.setFromY(35);
        moveHeading.setToY(0);

        ParallelTransition headingAnimation =
                new ParallelTransition(
                        fadeHeading,
                        moveHeading
                );

        // =====================================================
        // TITLE ANIMATION
        // =====================================================

        FadeTransition fadeTitle =
                new FadeTransition(
                        Duration.millis(400),
                        title
                );

        fadeTitle.setFromValue(0);
        fadeTitle.setToValue(1);

        TranslateTransition moveTitle =
                new TranslateTransition(
                        Duration.millis(400),
                        title
                );

        moveTitle.setFromY(40);
        moveTitle.setToY(0);

        ParallelTransition titleAnimation =
                new ParallelTransition(
                        fadeTitle,
                        moveTitle
                );

        // =====================================================
        // SUBTITLE ANIMATION
        // =====================================================

        FadeTransition fadeSubtitle =
                new FadeTransition(
                        Duration.millis(450),
                        subtitle
                );

        fadeSubtitle.setFromValue(0);
        fadeSubtitle.setToValue(1);

        TranslateTransition moveSubtitle =
                new TranslateTransition(
                        Duration.millis(450),
                        subtitle
                );

        moveSubtitle.setFromY(45);
        moveSubtitle.setToY(0);

        ParallelTransition subtitleAnimation =
                new ParallelTransition(
                        fadeSubtitle,
                        moveSubtitle
                );

        // =====================================================
        // PROFILE CARD ANIMATION
        // =====================================================

        FadeTransition fadeCard =
                new FadeTransition(
                        Duration.millis(550),
                        profileCard
                );

        fadeCard.setFromValue(0);
        fadeCard.setToValue(1);

        TranslateTransition moveCard =
                new TranslateTransition(
                        Duration.millis(550),
                        profileCard
                );

        moveCard.setFromY(55);
        moveCard.setToY(0);

        ParallelTransition cardAnimation =
                new ParallelTransition(
                        fadeCard,
                        moveCard
                );

        // =====================================================
        // DELAYS
        // =====================================================

        headingAnimation.setDelay(
                Duration.millis(80)
        );

        titleAnimation.setDelay(
                Duration.millis(160)
        );

        subtitleAnimation.setDelay(
                Duration.millis(240)
        );

        cardAnimation.setDelay(
                Duration.millis(320)
        );

        // =====================================================
        // PLAY
        // =====================================================

        headingAnimation.play();
        titleAnimation.play();
        subtitleAnimation.play();
        cardAnimation.play();
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
                    loginPage.createScene(stage);

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

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

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

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

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
                "-fx-background-color:#B3ADAD;"
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

        loginButton.setPrefWidth(140);
        loginButton.setPrefHeight(38);

        loginButton.setStyle(
                "-fx-background-color:#593a32;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;"
        );

        addPopupAnimation(loginButton);

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