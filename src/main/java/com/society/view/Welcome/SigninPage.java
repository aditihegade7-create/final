package com.society.view.Welcome;

import com.society.controller.welcome.UserController;
import com.society.dao.Welcome.UserDao;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SigninPage {

        private Scene signinScene;

        private final UserController controller = new UserController();

        private static final String DARK_BROWN = "#4E342E";

        // =========================================================
        // RESIDENT FIELDS
        // =========================================================

        private TextField residentFlatNo;
        private TextField residentAadhar;
        private TextField residentSociety;
        private TextField residentOwnerName;

        // =========================================================
        // OWNER FIELDS
        // =========================================================

        private TextField ownerFlatNo;
        private TextField ownerAadhar;
        private TextField ownerAddress;
        private TextField ownerSociety;

        // =========================================================
        // GUARD FIELDS
        // =========================================================

        private DatePicker joiningDate;
        private TextField guardAadhar;
        private TextField guardSociety;

        // =========================================================
        // SECRETARY FIELDS
        // =========================================================

        private TextField secretaryAadhar;
        private TextField secretarySociety;

        // =====================================================
        // CREATE SCENE
        // =====================================================

        public Scene createScene(Stage stage) {

                // =================================================
                // ROOT
                // =================================================

                StackPane root = new StackPane();

                root.setStyle(
                                "-fx-background-color:" +
                                                "linear-gradient(to right,#D7CCC8,#bfb1a7);");

                // =================================================
                // BACKGROUND IMAGE
                // =================================================

                Image bgImage = new Image(getClass().getResource("/Background-Society360.jpeg")
                                .toExternalForm());

                ImageView bgView = new ImageView(bgImage);

                bgView.setPreserveRatio(false);
                bgView.setSmooth(true);

                bgView.fitWidthProperty().bind(
                                root.widthProperty());

                bgView.fitHeightProperty().bind(
                                root.heightProperty());

                // Background first
                root.getChildren().add(bgView);

                // =================================================
                // LEFT PANEL
                // =================================================

                VBox leftPanel = createLeftPanel();

                StackPane.setAlignment(
                                leftPanel,
                                Pos.CENTER_LEFT);

                StackPane.setMargin(
                                leftPanel,
                                new Insets(
                                                20,
                                                20,
                                                20,
                                                50));

                // =================================================
                // SIGNUP CARD
                // =================================================

                VBox signupCard = new VBox(15);

                signupCard.setPrefWidth(400);
                signupCard.setMinWidth(400);
                signupCard.setMaxWidth(400);

                signupCard.setPrefHeight(640);
                signupCard.setMaxHeight(640);

                signupCard.setPadding(
                                new Insets(20));

                signupCard.setAlignment(
                                Pos.TOP_CENTER);

                signupCard.setStyle(
                                "-fx-background-color: #F8F3EE;" +
                                                "-fx-border-color: #c7c3bd;" +
                                                "-fx-border-radius: 15;" +
                                                "-fx-border-width: 1.5;" +
                                                "-fx-background-radius: 15;");

                // =================================================
                // TITLE
                // =================================================

                Label createTitle = new Label("Create Account");

                createTitle.setStyle(
                                "-fx-font-size:26px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:#333333;" +
                                                "-fx-underline:true;");

                Label createSubtitle = new Label(
                                "Join Society360 and simplify your life");

                createSubtitle.setStyle(
                                "-fx-font-size:13px;" +
                                                "-fx-text-fill:#535050;"
                // "-fx-underline:true;"
                );

                // =================================================
                // ROLE SELECTION
                // =================================================

                Label roleLabel = new Label("I am a");

                roleLabel.setStyle(
                                "-fx-font-size:13px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:#555555;" +
                                                "-fx-underline:true;");

                RadioButton owner = new RadioButton("Owner");

                RadioButton resident = new RadioButton("Resident");

                RadioButton secretary = new RadioButton("Secretary");

                RadioButton guard = new RadioButton("Guard");

                String roleStyle = "-fx-font-size:16px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-text-fill:#4e342e;";

                owner.setStyle(roleStyle);
                resident.setStyle(roleStyle);
                secretary.setStyle(roleStyle);
                guard.setStyle(roleStyle);

                ToggleGroup roleGroup = new ToggleGroup();

                owner.setToggleGroup(roleGroup);
                resident.setToggleGroup(roleGroup);
                secretary.setToggleGroup(roleGroup);
                guard.setToggleGroup(roleGroup);

                VBox roles = new VBox(25);

                roles.setAlignment(
                                Pos.CENTER_LEFT);

                roles.getChildren().addAll(
                                owner,
                                resident,
                                secretary,
                                guard);

                // =================================================
                // ROLE RADIO BUTTON ANIMATION
                // =================================================

                addRadioAnimation(owner);
                addRadioAnimation(resident);
                addRadioAnimation(secretary);
                addRadioAnimation(guard);

                // =================================================
                // BACK BUTTON
                // =================================================

                Button backToLoginBtn = new Button("← Back");

                backToLoginBtn.setPrefWidth(100);
                backToLoginBtn.setPrefHeight(40);

                backToLoginBtn.setStyle(
                                "-fx-background-color:transparent;" +
                                                "-fx-text-fill:#4e342e;" +
                                                "-fx-font-size:14px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-cursor:hand;");

                // =================================================
                // BACK BUTTON HOVER ANIMATION
                // =================================================

                backToLoginBtn.setOnMouseEntered(event -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(120),
                                        backToLoginBtn);

                        scale.setToX(1.08);
                        scale.setToY(1.08);

                        scale.play();
                });

                backToLoginBtn.setOnMouseExited(event -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(120),
                                        backToLoginBtn);

                        scale.setToX(1.0);
                        scale.setToY(1.0);

                        scale.play();
                });

                backToLoginBtn.setOnAction(event -> {

                        LogInPage loginPage = new LogInPage();

                        stage.setScene(
                                        loginPage.createScene(stage));

                        stage.show();
                });

                // =================================================
                // CONTINUE BUTTON
                // =================================================

                Button continueBtn = new Button("Continue");

                continueBtn.setPrefWidth(330);
                continueBtn.setPrefHeight(45);

                continueBtn.setStyle(
                                "-fx-background-color:#4e342e;" +
                                                "-fx-text-fill:white;" +
                                                "-fx-font-size:15px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-background-radius:8;");
                // =================================================
                // CONTINUE BUTTON HOVER ANIMATION
                // =================================================

                continueBtn.setOnMouseEntered(event -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(120),
                                        continueBtn);

                        scale.setToX(1.04);
                        scale.setToY(1.04);

                        scale.play();
                });

                continueBtn.setOnMouseExited(event -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(120),
                                        continueBtn);

                        scale.setToX(1.0);
                        scale.setToY(1.0);

                        scale.play();
                });

                // =================================================
                // DETAILS BOX
                // =================================================

                VBox detailsBox = new VBox(12);

                detailsBox.setAlignment(
                                Pos.TOP_CENTER);

                detailsBox.setVisible(false);
                detailsBox.setManaged(false);

                // =================================================
                // CONTINUE ACTION
                // =================================================

                continueBtn.setOnAction(event -> {

                        ScaleTransition click = new ScaleTransition(
                                        Duration.millis(80),
                                        continueBtn);

                        click.setToX(0.96);
                        click.setToY(0.96);

                        click.setAutoReverse(true);
                        click.setCycleCount(2);

                        click.play();

                        Toggle selected = roleGroup.getSelectedToggle();

                        if (selected == null) {

                                showAlert(
                                                "Role Required",
                                                "Please select your role first.");

                                return;
                        }

                        String role = ((RadioButton) selected).getText();

                        createRoleForm(
                                        role,
                                        detailsBox,
                                        stage);

                        detailsBox.setVisible(true);
                        detailsBox.setManaged(true);

                        roleLabel.setVisible(false);
                        roleLabel.setManaged(false);

                        roles.setVisible(false);
                        roles.setManaged(false);

                        continueBtn.setVisible(false);
                        continueBtn.setManaged(false);

                        // =================================================
                        // DETAILS BOX ENTRY ANIMATION
                        // =================================================

                        detailsBox.setOpacity(0);
                        detailsBox.setTranslateY(25);

                        FadeTransition detailsFade = new FadeTransition(
                                        Duration.millis(500),
                                        detailsBox);

                        detailsFade.setFromValue(0);
                        detailsFade.setToValue(1);

                        TranslateTransition detailsSlide = new TranslateTransition(
                                        Duration.millis(500),
                                        detailsBox);

                        detailsSlide.setFromY(25);
                        detailsSlide.setToY(0);

                        ParallelTransition detailsAnimation = new ParallelTransition(
                                        detailsFade,
                                        detailsSlide);

                        detailsAnimation.play();
                });

                // =================================================
                // ADD CONTENT
                // =================================================

                signupCard.getChildren().addAll(
                                backToLoginBtn,
                                createTitle,
                                createSubtitle,
                                roleLabel,
                                roles,
                                continueBtn,
                                detailsBox);

                // =================================================
                // SIGNUP CARD POSITION
                // =================================================

                StackPane.setAlignment(
                                signupCard,
                                Pos.CENTER_RIGHT);

                StackPane.setMargin(
                                signupCard,
                                new Insets(
                                                20,
                                                100,
                                                20,
                                                20));

                // =================================================
                // ADD TO ROOT
                // =================================================

                root.getChildren().addAll(
                                leftPanel,
                                signupCard);
                // =================================================
                // PAGE ENTRY ANIMATION
                // =================================================

                // LEFT PANEL
                leftPanel.setOpacity(0);
                leftPanel.setTranslateX(-80);

                FadeTransition leftFade = new FadeTransition(
                                Duration.millis(700),
                                leftPanel);

                leftFade.setFromValue(0);
                leftFade.setToValue(1);

                TranslateTransition leftSlide = new TranslateTransition(
                                Duration.millis(700),
                                leftPanel);

                leftSlide.setFromX(-80);
                leftSlide.setToX(0);

                ParallelTransition leftAnimation = new ParallelTransition(
                                leftFade,
                                leftSlide);

                leftAnimation.play();

                // SIGNUP CARD
                signupCard.setOpacity(0);
                signupCard.setTranslateX(80);

                FadeTransition cardFade = new FadeTransition(
                                Duration.millis(700),
                                signupCard);

                cardFade.setFromValue(0);
                cardFade.setToValue(1);

                TranslateTransition cardSlide = new TranslateTransition(
                                Duration.millis(700),
                                signupCard);

                cardSlide.setFromX(80);
                cardSlide.setToX(0);

                ParallelTransition cardAnimation = new ParallelTransition(
                                cardFade,
                                cardSlide);

                cardAnimation.play();

                // =================================================
                // SCENE
                // =================================================

                signinScene = new Scene(
                                root,
                                ScreenSize.getWidth(),
                                ScreenSize.getHeight());

                stage.setTitle("Society360 - Sign Up");

                stage.setMinWidth(900);
                stage.setMinHeight(600);

                return signinScene;
        }

        // =====================================================
        // ROLE FORM
        // =====================================================

        private void createRoleForm(
                        String role,
                        VBox detailsBox,
                        Stage stage) {

                detailsBox.getChildren().clear();

                // =================================================
                // ROLE TITLE
                // =================================================

                Label roleTitle = new Label(role + " Details");

                roleTitle.setStyle(
                                "-fx-font-size:20px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:#4e342e;" +
                                                "-fx-underline:true;");

                // =================================================
                // COMMON FIELDS
                // =================================================

                TextField nameField = createTextField("Full Name");

                TextField phoneField = createTextField("Phone Number");

                DatePicker dob = new DatePicker();

                dob.setPromptText(
                                "Date of Birth");

                dob.setPrefHeight(40);
                dob.setMaxWidth(350);

                dob.focusedProperty().addListener(
                                (obs, oldValue, newValue) -> {

                                        ScaleTransition scale = new ScaleTransition(
                                                        Duration.millis(120),
                                                        dob);

                                        if (newValue) {
                                                scale.setToX(1.02);
                                                scale.setToY(1.02);
                                        } else {
                                                scale.setToX(1.0);
                                                scale.setToY(1.0);
                                        }

                                        scale.play();
                                });

                TextField emailField = createTextField("Email Address");

                ComboBox<String> gender = new ComboBox<>();

                gender.getItems().addAll(
                                "Male",
                                "Female",
                                "Other");

                gender.setPromptText("Gender");

                gender.setPrefHeight(40);
                gender.setMaxWidth(350);

                gender.focusedProperty().addListener(
                                (obs, oldValue, newValue) -> {

                                        ScaleTransition scale = new ScaleTransition(
                                                        Duration.millis(120),
                                                        gender);

                                        if (newValue) {
                                                scale.setToX(1.02);
                                                scale.setToY(1.02);
                                        } else {
                                                scale.setToX(1.0);
                                                scale.setToY(1.0);
                                        }

                                        scale.play();
                                });

                PasswordField password = new PasswordField();

                password.setPromptText(
                                "Create Password");

                password.setPrefHeight(40);
                password.setMaxWidth(350);

                PasswordField confirmPassword = new PasswordField();

                confirmPassword.setPromptText(
                                "Confirm Password");

                // PASSWORD ANIMATION

                addPasswordAnimation(password);
                addPasswordAnimation(confirmPassword);

                confirmPassword.setPrefHeight(40);
                confirmPassword.setMaxWidth(350);

                detailsBox.getChildren().addAll(
                                roleTitle,
                                nameField,
                                phoneField,
                                dob,
                                emailField,
                                gender);

                // // =================================================
                // // RESIDENT
                // // =================================================

                // if (role.equalsIgnoreCase("Resident")) {

                // TextField residentFlatNo = createTextField1(
                // "Flat Number");

                // TextField aadhar = createTextField1(
                // "Aadhar Number");

                // TextField society = createTextField1(
                // "Society Name");

                // Label residentTypeLabel = new Label("Resident Type");

                // residentTypeLabel.setStyle(
                // "-fx-font-size:13px;" +
                // "-fx-font-weight:bold;" +
                // "-fx-text-fill:#3d3d3d;");

                // RadioButton ownerOption = new RadioButton("Owner");

                // RadioButton residentOption = new RadioButton("Resident");

                // ToggleGroup residentTypeGroup = new ToggleGroup();

                // ownerOption.setToggleGroup(
                // residentTypeGroup);

                // residentOption.setToggleGroup(
                // residentTypeGroup);

                // HBox residentTypeBox = new HBox(20);

                // residentTypeBox.setAlignment(
                // Pos.CENTER_LEFT);

                // residentTypeBox.getChildren().addAll(
                // ownerOption,
                // residentOption);

                // TextField ownerName = createTextField1(
                // "Owner Name");

                // ownerName.setVisible(false);
                // ownerName.setManaged(false);

                // residentOption.setOnAction(e -> {

                // ownerName.setVisible(true);
                // ownerName.setManaged(true);
                // });

                // ownerOption.setOnAction(e -> {

                // ownerName.setVisible(false);
                // ownerName.setManaged(false);
                // });

                // detailsBox.getChildren().addAll(
                // residentFlatNo,
                // password,
                // confirmPassword,
                // aadhar,
                // society,
                // residentTypeLabel,
                // residentTypeBox,
                // ownerName);
                // }

                // // =================================================
                // // OWNER
                // // =================================================

                // else if (role.equalsIgnoreCase("Owner")) {

                // TextField residentFlatNo = createTextField1(
                // "Flat Number");

                // TextField aadhar = createTextField1(
                // "Aadhar Number");

                // TextField address = createTextField1(
                // "Permanent Address");

                // TextField society = createTextField1(
                // "Society Name");

                // detailsBox.getChildren().addAll(
                // residentFlatNo,
                // password,
                // confirmPassword,
                // aadhar,
                // address,
                // society);
                // }

                // // =================================================
                // // GUARD
                // // =================================================

                // else if (role.equalsIgnoreCase("Guard")) {

                // DatePicker joiningDate = new DatePicker();

                // joiningDate.setPromptText(
                // "Joining Date");

                // joiningDate.setPrefHeight(40);
                // joiningDate.setMaxWidth(350);

                // TextField aadhar = createTextField1(
                // "Aadhar Number");

                // TextField society = createTextField1(
                // "Society Name");

                // detailsBox.getChildren().addAll(
                // password,
                // confirmPassword,
                // joiningDate,
                // aadhar,
                // society);
                // }

                // // =================================================
                // // SECRETARY
                // // =================================================

                // else if (role.equalsIgnoreCase("Secretary")) {

                // TextField aadhar = createTextField1(
                // "Aadhar Number");

                // TextField society = createTextField1(
                // "Society Name");

                // detailsBox.getChildren().addAll(
                // password,
                // confirmPassword,
                // aadhar,
                // society);
                // }

                // // =================================================
                // // TERMS
                // // =================================================

                // CheckBox terms = new CheckBox(
                // "I agree to the Terms & Conditions and Privacy Policy");

                // terms.setStyle(
                // "-fx-font-size:11px;" +
                // "-fx-text-fill:#777777;");

                // // =================================================
                // // TERMS CHECKBOX ANIMATION
                // // =================================================

                // terms.setOnMouseEntered(event -> {

                // ScaleTransition scale = new ScaleTransition(
                // Duration.millis(100),
                // terms);

                // scale.setToX(1.03);
                // scale.setToY(1.03);

                // scale.play();
                // });

                // terms.setOnMouseExited(event -> {

                // ScaleTransition scale = new ScaleTransition(
                // Duration.millis(100),
                // terms);

                // scale.setToX(1.0);
                // scale.setToY(1.0);

                // scale.play();
                // });

                // terms.setOnAction(e -> {

                // if (terms.isSelected()) {

                // Alert termsAlert = new Alert(
                // Alert.AlertType.INFORMATION);

                // termsAlert.setTitle(
                // "Terms & Conditions");

                // termsAlert.setHeaderText(
                // "Society360 - Terms & Conditions");

                // termsAlert.setContentText(
                // "By creating an account on Society360, "
                // + "you agree to:\n\n"
                // + "• Provide correct and valid personal information.\n"
                // + "• Keep your password and account details secure.\n"
                // + "• Use the Society360 system only for genuine purposes.\n"
                // + "• Respect the privacy of other society members.\n"
                // + "• Your information will be used only for society management.\n\n"
                // + "By continuing, you agree to the Terms & Conditions "
                // + "and Privacy Policy.");

                // termsAlert.showAndWait();
                // }
                // });

                // // =================================================
                // // SIGN UP BUTTON
                // // =================================================

                // Button signupBtn = new Button("Sign Up");

                // signupBtn.setPrefWidth(350);
                // signupBtn.setPrefHeight(45);

                // signupBtn.setStyle(
                // "-fx-background-color:#4e342e;" +
                // "-fx-text-fill:white;" +
                // "-fx-font-size:15px;" +
                // "-fx-font-weight:bold;" +
                // "-fx-background-radius:8;");

                // // =================================================
                // // SIGN UP BUTTON HOVER ANIMATION
                // // =================================================

                // signupBtn.setOnMouseEntered(event -> {

                // ScaleTransition scale = new ScaleTransition(
                // Duration.millis(120),
                // signupBtn);

                // scale.setToX(1.04);
                // scale.setToY(1.04);

                // scale.play();
                // });

                // signupBtn.setOnMouseExited(event -> {

                // ScaleTransition scale = new ScaleTransition(
                // Duration.millis(120),
                // signupBtn);

                // scale.setToX(1.0);
                // scale.setToY(1.0);

                // scale.play();
                // });

                // // signupBtn.setOnAction(event -> {

                // // ScaleTransition click = new ScaleTransition(
                // // Duration.millis(80),
                // // signupBtn);

                // // click.setToX(0.96);
                // // click.setToY(0.96);

                // // click.setAutoReverse(true);
                // // click.setCycleCount(2);

                // // click.play();

                // // // =============================================
                // // // TERMS VALIDATION
                // // // =============================================

                // // if (!terms.isSelected()) {

                // // showAlert(
                // // "Terms Required",
                // // "Please accept the Terms & Conditions.");

                // // return;
                // // }

                // // // =============================================
                // // // ROLE
                // // // =============================================

                // // String selectedRole = role;

                // // // =============================================
                // // // GET FORM DATA
                // // // =============================================

                // // String name = nameField.getText().trim();

                // // String email = emailField.getText().trim();

                // // String pass = password.getText();

                // // String confirmPass = confirmPassword.getText();

                // // // =============================================
                // // // BASIC VALIDATION
                // // // =============================================

                // // if (name.isEmpty()
                // // || email.isEmpty()
                // // || pass.isEmpty()
                // // || confirmPass.isEmpty()) {

                // // showAlert(
                // // "Missing Details",
                // // "Please fill all required fields.");

                // // return;
                // // }

                // // // =============================================
                // // // PASSWORD VALIDATION
                // // // =============================================

                // // if (!pass.equals(confirmPass)) {

                // // showAlert(
                // // "Password Error",
                // // "Password and Confirm Password do not match.");

                // // return;
                // // }

                // // if (pass.length() < 6) {

                // // showAlert(
                // // "Password Error",
                // // "Password must contain at least 6 characters.");

                // // return;
                // // }

                // // // =============================================
                // // // SIGN UP + FIRESTORE SAVE
                // // // =============================================

                // // boolean saved = controller.addUser(
                // // name,
                // // email,
                // // pass,
                // // selectedRole);

                // // if (!saved) {

                // // showAlert(
                // // "Registration Failed",
                // // "Unable to create account or save user information.");

                // // return;
                // // }

                // // // =============================================
                // // // SUCCESS
                // // // =============================================

                // // showAlert(
                // // "Sign Up Successful",
                // // "Your account has been created successfully!");

                // // // =============================================
                // // // CLEAR FIELDS
                // // // =============================================

                // // nameField.clear();
                // // emailField.clear();
                // // password.clear();
                // // confirmPassword.clear();

                // // terms.setSelected(false);

                // // // =============================================
                // // // GO TO LOGIN
                // // // =============================================

                // // LogInPage loginPage = new LogInPage();

                // // stage.setScene(
                // // loginPage.createScene(stage));

                // // stage.show();
                // // });

                // signupBtn.setOnAction(event -> {

                // // =================================================
                // // TERMS
                // // =================================================

                // if (!terms.isSelected()) {

                // showAlert(
                // "Terms Required",
                // "Please accept the Terms & Conditions.");

                // return;
                // }

                // // =================================================
                // // COMMON DATA
                // // =================================================

                // String name = nameField.getText().trim();

                // String phone = phoneField.getText().trim();

                // String email = emailField.getText()
                // .trim()
                // .toLowerCase();

                // String pass = password.getText();

                // String confirmPass = confirmPassword.getText();

                // String dobValue = "";

                // if (dob.getValue() != null) {

                // dobValue = dob.getValue().toString();
                // }

                // String genderValue = "";

                // if (gender.getValue() != null) {

                // genderValue = gender.getValue();
                // }

                // // =================================================
                // // COMMON VALIDATION
                // // =================================================

                // if (name.isEmpty()
                // || phone.isEmpty()
                // || email.isEmpty()
                // || pass.isEmpty()
                // || confirmPass.isEmpty()) {

                // showAlert(
                // "Missing Details",
                // "Please fill all required fields.");

                // return;
                // }

                // // =================================================
                // // EMAIL VALIDATION
                // // =================================================

                // if (!email.matches(
                // "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

                // showAlert(
                // "Invalid Email",
                // "Please enter a valid email address.");

                // return;
                // }

                // // =================================================
                // // PHONE VALIDATION
                // // =================================================

                // if (!phone.matches("\\d{10}")) {

                // showAlert(
                // "Invalid Phone",
                // "Phone number must contain exactly 10 digits.");

                // return;
                // }

                // // =================================================
                // // PASSWORD
                // // =================================================

                // if (!pass.equals(confirmPass)) {

                // showAlert(
                // "Password Error",
                // "Password and Confirm Password do not match.");

                // return;
                // }

                // if (pass.length() < 6) {

                // showAlert(
                // "Password Error",
                // "Password must contain at least 6 characters.");

                // return;
                // }

                // // =================================================
                // // VARIABLES
                // // =================================================

                // String flatNo = "";
                // String aadhar = "";
                // String society = "";
                // String ownerName = "";
                // String address = "";
                // String joiningDateValue = "";

                // // =================================================
                // // RESIDENT DATA
                // // =================================================

                // if (role.equalsIgnoreCase("Resident")) {

                // flatNo = residentFlatNo
                // .getText()
                // .trim();

                // aadhar = residentAadhar
                // .getText()
                // .trim();

                // society = residentSociety
                // .getText()
                // .trim();

                // ownerName = residentOwnerName
                // .getText()
                // .trim();

                // if (flatNo.isEmpty()
                // || aadhar.isEmpty()
                // || society.isEmpty()) {

                // showAlert(
                // "Missing Details",
                // "Please fill Flat Number, Aadhar Number and Society Name.");

                // return;
                // }

                // if (!aadhar.matches("\\d{12}")) {

                // showAlert(
                // "Invalid Aadhar",
                // "Aadhar number must contain exactly 12 digits.");

                // return;
                // }
                // }

                // // =================================================
                // // OWNER DATA
                // // =================================================

                // else if (role.equalsIgnoreCase("Owner")) {

                // flatNo = ownerFlatNo
                // .getText()
                // .trim();

                // aadhar = ownerAadhar
                // .getText()
                // .trim();

                // address = ownerAddress
                // .getText()
                // .trim();

                // society = ownerSociety
                // .getText()
                // .trim();

                // if (flatNo.isEmpty()
                // || aadhar.isEmpty()
                // || address.isEmpty()
                // || society.isEmpty()) {

                // showAlert(
                // "Missing Details",
                // "Please fill all Owner details.");

                // return;
                // }

                // if (!aadhar.matches("\\d{12}")) {

                // showAlert(
                // "Invalid Aadhar",
                // "Aadhar number must contain exactly 12 digits.");

                // return;
                // }
                // }

                // // =================================================
                // // GUARD DATA
                // // =================================================

                // else if (role.equalsIgnoreCase("Guard")) {

                // aadhar = guardAadhar
                // .getText()
                // .trim();

                // society = guardSociety
                // .getText()
                // .trim();

                // if (joiningDate.getValue() != null) {

                // joiningDateValue = joiningDate
                // .getValue()
                // .toString();
                // }

                // if (aadhar.isEmpty()
                // || society.isEmpty()
                // || joiningDateValue.isEmpty()) {

                // showAlert(
                // "Missing Details",
                // "Please fill all Guard details.");

                // return;
                // }

                // if (!aadhar.matches("\\d{12}")) {

                // showAlert(
                // "Invalid Aadhar",
                // "Aadhar number must contain exactly 12 digits.");

                // return;
                // }
                // }

                // // =================================================
                // // SECRETARY DATA
                // // =================================================

                // else if (role.equalsIgnoreCase("Secretary")) {

                // aadhar = secretaryAadhar
                // .getText()
                // .trim();

                // society = secretarySociety
                // .getText()
                // .trim();

                // if (aadhar.isEmpty()
                // || society.isEmpty()) {

                // showAlert(
                // "Missing Details",
                // "Please fill all Secretary details.");

                // return;
                // }

                // if (!aadhar.matches("\\d{12}")) {

                // showAlert(
                // "Invalid Aadhar",
                // "Aadhar number must contain exactly 12 digits.");

                // return;
                // }
                // }

                // // =================================================
                // // SAVE USER
                // // =================================================

                // boolean saved = controller.addUser(
                // name,
                // email,
                // pass,
                // role,
                // phone,
                // dobValue,
                // genderValue,
                // flatNo,
                // aadhar,
                // society,
                // ownerName,
                // address,
                // joiningDateValue);

                // // =================================================
                // // FAILED
                // // =================================================

                // if (!saved) {

                // showAlert(
                // "Registration Failed",
                // "Unable to create account or save user information.");

                // return;
                // }

                // // =================================================
                // // SUCCESS
                // // =================================================

                // showAlert(
                // "Sign Up Successful",
                // "Your account has been created successfully!");

                // // =================================================
                // // GO TO LOGIN
                // // =================================================

                // LogInPage loginPage = new LogInPage();

                // stage.setScene(
                // loginPage.createScene(stage));

                // stage.show();
                // });

                // // =================================================
                // // LOGIN
                // // =================================================

                // Label accountText = new Label("Already have an account?");

                // accountText.setStyle(
                // "-fx-text-fill:#666666;" +
                // "-fx-font-size:12px;");

                // Button loginBtn = new Button("Login");

                // loginBtn.setStyle(
                // "-fx-background-color:transparent;" +
                // "-fx-text-fill:#5f331e;" +
                // "-fx-font-weight:bold;" +
                // "-fx-font-size:12px;");

                // // =================================================
                // // LOGIN BUTTON HOVER ANIMATION
                // // =================================================

                // loginBtn.setOnMouseEntered(event -> {

                // ScaleTransition scale = new ScaleTransition(
                // Duration.millis(120),
                // loginBtn);

                // scale.setToX(1.08);
                // scale.setToY(1.08);

                // scale.play();
                // });

                // loginBtn.setOnMouseExited(event -> {

                // ScaleTransition scale = new ScaleTransition(
                // Duration.millis(120),
                // loginBtn);

                // scale.setToX(1.0);
                // scale.setToY(1.0);

                // scale.play();
                // });

                // loginBtn.setOnAction(event -> {

                // LogInPage loginPage = new LogInPage();

                // stage.setScene(
                // loginPage.createScene(stage));

                // stage.show();
                // });

                // HBox loginBox = new HBox(5);

                // loginBox.setAlignment(
                // Pos.CENTER);

                // loginBox.getChildren().addAll(
                // accountText,
                // loginBtn);

                // // =================================================
                // // ADD FINAL CONTROLS
                // // =================================================

                // detailsBox.getChildren().addAll(
                // terms,
                // signupBtn,
                // loginBox);

                // =====================================================
                // RESIDENT
                // =====================================================

                if (role.equalsIgnoreCase("Resident")) {

                        residentFlatNo = createTextField(
                                        "Flat Number");

                        residentAadhar = createTextField(
                                        "Aadhar Number");

                        residentSociety = createTextField(
                                        "Society Name");

                        residentOwnerName = createTextField(
                                        "Owner Name");

                        Label residentTypeLabel = new Label(
                                        "Resident Type");

                        residentTypeLabel.setStyle(
                                        "-fx-font-size:13px;"
                                                        + "-fx-font-weight:bold;"
                                                        + "-fx-text-fill:#3d3d3d;");

                        RadioButton ownerOption = new RadioButton("Owner");

                        RadioButton residentOption = new RadioButton("Resident");

                        ToggleGroup residentTypeGroup = new ToggleGroup();

                        ownerOption.setToggleGroup(
                                        residentTypeGroup);

                        residentOption.setToggleGroup(
                                        residentTypeGroup);

                        HBox residentTypeBox = new HBox(20);

                        residentTypeBox.setAlignment(
                                        Pos.CENTER_LEFT);

                        residentTypeBox.getChildren().addAll(
                                        ownerOption,
                                        residentOption);

                        residentOwnerName.setVisible(false);
                        residentOwnerName.setManaged(false);

                        residentOption.setOnAction(e -> {

                                residentOwnerName.setVisible(true);
                                residentOwnerName.setManaged(true);
                        });

                        ownerOption.setOnAction(e -> {

                                residentOwnerName.setVisible(false);
                                residentOwnerName.setManaged(false);
                                residentOwnerName.clear();
                        });

                        detailsBox.getChildren().addAll(
                                        residentFlatNo,
                                        password,
                                        confirmPassword,
                                        residentAadhar,
                                        residentSociety,
                                        residentTypeLabel,
                                        residentTypeBox,
                                        residentOwnerName);
                }

                // =====================================================
                // OWNER
                // =====================================================

                else if (role.equalsIgnoreCase("Owner")) {

                        ownerFlatNo = createTextField(
                                        "Flat Number");

                        ownerAadhar = createTextField(
                                        "Aadhar Number");

                        ownerAddress = createTextField(
                                        "Permanent Address");

                        ownerSociety = createTextField(
                                        "Society Name");

                        detailsBox.getChildren().addAll(
                                        ownerFlatNo,
                                        password,
                                        confirmPassword,
                                        ownerAadhar,
                                        ownerAddress,
                                        ownerSociety);
                }

                // =====================================================
                // GUARD
                // =====================================================

                else if (role.equalsIgnoreCase("Guard")) {

                        joiningDate = new DatePicker();

                        joiningDate.setPromptText(
                                        "Joining Date");

                        joiningDate.setPrefHeight(40);
                        joiningDate.setMaxWidth(350);

                        guardAadhar = createTextField(
                                        "Aadhar Number");

                        guardSociety = createTextField(
                                        "Society Name");

                        detailsBox.getChildren().addAll(
                                        password,
                                        confirmPassword,
                                        joiningDate,
                                        guardAadhar,
                                        guardSociety);
                }

                // =====================================================
                // SECRETARY
                // =====================================================

                else if (role.equalsIgnoreCase("Secretary")) {

                        secretaryAadhar = createTextField(
                                        "Aadhar Number");

                        secretarySociety = createTextField(
                                        "Society Name");

                        detailsBox.getChildren().addAll(
                                        password,
                                        confirmPassword,
                                        secretaryAadhar,
                                        secretarySociety);
                }

                // =====================================================
                // TERMS
                // =====================================================

                CheckBox terms = new CheckBox(
                                "I agree to the Terms & Conditions and Privacy Policy");

                terms.setStyle(
                                "-fx-font-size:11px;"
                                                + "-fx-text-fill:#777777;");

                // =====================================================
                // SIGNUP BUTTON
                // =====================================================

                Button signupBtn = new Button("Sign Up");

                signupBtn.setPrefWidth(350);
                signupBtn.setPrefHeight(45);

                signupBtn.setStyle(
                                "-fx-background-color:#4e342e;"
                                                + "-fx-text-fill:white;"
                                                + "-fx-font-size:15px;"
                                                + "-fx-font-weight:bold;"
                                                + "-fx-background-radius:8;");

                // =====================================================
                // SIGNUP ACTION
                // =====================================================

                signupBtn.setOnAction(event -> {

                        // =================================================
                        // TERMS
                        // =================================================

                        if (!terms.isSelected()) {

                                showAlert(
                                                "Terms Required",
                                                "Please accept the Terms & Conditions.");

                                return;
                        }

                        // =================================================
                        // COMMON DATA
                        // =================================================

                        String name = nameField.getText().trim();

                        String phone = phoneField.getText().trim();

                        String email = emailField.getText()
                                        .trim()
                                        .toLowerCase();

                        String pass = password.getText();

                        String confirmPass = confirmPassword.getText();

                        String dobValue = "";

                        if (dob.getValue() != null) {

                                dobValue = dob.getValue().toString();
                        }

                        String genderValue = "";

                        if (gender.getValue() != null) {

                                genderValue = gender.getValue();
                        }

                        // =================================================
                        // COMMON VALIDATION
                        // =================================================

                        if (name.isEmpty()
                                        || phone.isEmpty()
                                        || email.isEmpty()
                                        || pass.isEmpty()
                                        || confirmPass.isEmpty()) {

                                showAlert(
                                                "Missing Details",
                                                "Please fill all required fields.");

                                return;
                        }

                        // =================================================
                        // EMAIL VALIDATION
                        // =================================================

                        if (!email.matches(
                                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

                                showAlert(
                                                "Invalid Email",
                                                "Please enter a valid email address.");

                                return;
                        }

                        // =================================================
                        // PHONE VALIDATION
                        // =================================================

                        if (!phone.matches("\\d{10}")) {

                                showAlert(
                                                "Invalid Phone",
                                                "Phone number must contain exactly 10 digits.");

                                return;
                        }

                        // =================================================
                        // PASSWORD
                        // =================================================

                        if (!pass.equals(confirmPass)) {

                                showAlert(
                                                "Password Error",
                                                "Password and Confirm Password do not match.");

                                return;
                        }

                        if (pass.length() < 6) {

                                showAlert(
                                                "Password Error",
                                                "Password must contain at least 6 characters.");

                                return;
                        }

                        // =================================================
                        // VARIABLES
                        // =================================================

                        String flatNo = "";
                        String aadhar = "";
                        String society = "";
                        String ownerName = "";
                        String address = "";
                        String joiningDateValue = "";

                        // =================================================
                        // RESIDENT DATA
                        // =================================================

                        if (role.equalsIgnoreCase("Resident")) {

                                flatNo = residentFlatNo
                                                .getText()
                                                .trim();

                                aadhar = residentAadhar
                                                .getText()
                                                .trim();

                                society = residentSociety
                                                .getText()
                                                .trim();

                                ownerName = residentOwnerName
                                                .getText()
                                                .trim();

                                if (flatNo.isEmpty()
                                                || aadhar.isEmpty()
                                                || society.isEmpty()) {

                                        showAlert(
                                                        "Missing Details",
                                                        "Please fill Flat Number, Aadhar Number and Society Name.");

                                        return;
                                }

                                if (!aadhar.matches("\\d{12}")) {

                                        showAlert(
                                                        "Invalid Aadhar",
                                                        "Aadhar number must contain exactly 12 digits.");

                                        return;
                                }
                        }

                        // =================================================
                        // OWNER DATA
                        // =================================================

                        else if (role.equalsIgnoreCase("Owner")) {

                                flatNo = ownerFlatNo
                                                .getText()
                                                .trim();

                                aadhar = ownerAadhar
                                                .getText()
                                                .trim();

                                address = ownerAddress
                                                .getText()
                                                .trim();

                                society = ownerSociety
                                                .getText()
                                                .trim();

                                if (flatNo.isEmpty()
                                                || aadhar.isEmpty()
                                                || address.isEmpty()
                                                || society.isEmpty()) {

                                        showAlert(
                                                        "Missing Details",
                                                        "Please fill all Owner details.");

                                        return;
                                }

                                if (!aadhar.matches("\\d{12}")) {

                                        showAlert(
                                                        "Invalid Aadhar",
                                                        "Aadhar number must contain exactly 12 digits.");

                                        return;
                                }
                        }

                        // =================================================
                        // GUARD DATA
                        // =================================================

                        else if (role.equalsIgnoreCase("Guard")) {

                                aadhar = guardAadhar
                                                .getText()
                                                .trim();

                                society = guardSociety
                                                .getText()
                                                .trim();

                                if (joiningDate.getValue() != null) {

                                        joiningDateValue = joiningDate
                                                        .getValue()
                                                        .toString();
                                }

                                if (aadhar.isEmpty()
                                                || society.isEmpty()
                                                || joiningDateValue.isEmpty()) {

                                        showAlert(
                                                        "Missing Details",
                                                        "Please fill all Guard details.");

                                        return;
                                }

                                if (!aadhar.matches("\\d{12}")) {

                                        showAlert(
                                                        "Invalid Aadhar",
                                                        "Aadhar number must contain exactly 12 digits.");

                                        return;
                                }
                        }

                        // =================================================
                        // SECRETARY DATA
                        // =================================================

                        else if (role.equalsIgnoreCase("Secretary")) {

                                aadhar = secretaryAadhar
                                                .getText()
                                                .trim();

                                society = secretarySociety
                                                .getText()
                                                .trim();

                                if (aadhar.isEmpty()
                                                || society.isEmpty()) {

                                        showAlert(
                                                        "Missing Details",
                                                        "Please fill all Secretary details.");

                                        return;
                                }

                                if (!aadhar.matches("\\d{12}")) {

                                        showAlert(
                                                        "Invalid Aadhar",
                                                        "Aadhar number must contain exactly 12 digits.");

                                        return;
                                }
                        }

                        // =================================================
                        // SAVE USER
                        // =================================================

                        boolean saved = controller.addUser(
                                        name,
                                        email,
                                        pass,
                                        role,
                                        phone,
                                        dobValue,
                                        genderValue,
                                        flatNo,
                                        aadhar,
                                        society,
                                        ownerName,
                                        address,
                                        joiningDateValue);

                        // =================================================
                        // FAILED
                        // =================================================

                        if (!saved) {

                                showAlert(
                                                "Registration Failed",
                                                "Unable to create account or save user information.");

                                return;
                        }

                        // =================================================
                        // SUCCESS
                        // =================================================

                        showAlert(
                                        "Sign Up Successful",
                                        "Your account has been created successfully!");

                        // =================================================
                        // GO TO LOGIN
                        // =================================================

                        LogInPage loginPage = new LogInPage();

                        stage.setScene(
                                        loginPage.createScene(stage));

                        stage.show();
                });

                // =====================================================
                // LOGIN TEXT
                // =====================================================

                Label accountText = new Label(
                                "Already have an account?");

                accountText.setStyle(
                                "-fx-text-fill:#666666;"
                                                + "-fx-font-size:12px;");

                Button loginBtn = new Button("Login");

                loginBtn.setStyle(
                                "-fx-background-color:transparent;"
                                                + "-fx-text-fill:#5f331e;"
                                                + "-fx-font-weight:bold;"
                                                + "-fx-font-size:12px;");

                loginBtn.setOnAction(event -> {

                        LogInPage loginPage = new LogInPage();

                        stage.setScene(
                                        loginPage.createScene(stage));

                        stage.show();
                });

                HBox loginBox = new HBox(5);

                loginBox.setAlignment(
                                Pos.CENTER);

                loginBox.getChildren().addAll(
                                accountText,
                                loginBtn);

                detailsBox.getChildren().addAll(
                                terms,
                                signupBtn,
                                loginBox);
                // =================================================
                // FORM ELEMENTS ENTRY ANIMATION
                // =================================================

                for (int i = 0; i < detailsBox.getChildren().size(); i++) {

                        javafx.scene.Node node = detailsBox.getChildren().get(i);

                        node.setOpacity(0);
                        node.setTranslateY(15);

                        FadeTransition fade = new FadeTransition(
                                        Duration.millis(350),
                                        node);

                        fade.setFromValue(0);
                        fade.setToValue(1);

                        TranslateTransition slide = new TranslateTransition(
                                        Duration.millis(350),
                                        node);

                        slide.setFromY(15);
                        slide.setToY(0);

                        ParallelTransition animation = new ParallelTransition(
                                        fade,
                                        slide);

                        animation.setDelay(
                                        Duration.millis(i * 80));

                        animation.play();
                }
        }

        // =====================================================
        // RADIO BUTTON HOVER ANIMATION
        // =====================================================

        private void addRadioAnimation(
                        RadioButton radio) {

                radio.setOnMouseEntered(event -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(120),
                                        radio);

                        scale.setToX(1.05);
                        scale.setToY(1.05);

                        scale.play();
                });

                radio.setOnMouseExited(event -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(120),
                                        radio);

                        scale.setToX(1.0);
                        scale.setToY(1.0);

                        scale.play();
                });
        }

        // =====================================================
        // TEXT FIELD
        // =====================================================

        private TextField createTextField(
                        String prompt) {

                TextField field = new TextField();

                field.setPromptText(prompt);

                field.setPrefHeight(40);
                field.setMaxWidth(350);

                field.setStyle(
                                "-fx-background-color:#FFFDF9;" +
                                                "-fx-border-color:#bdbab7;" +
                                                "-fx-border-radius:8;" +
                                                "-fx-background-radius:8;" +
                                                "-fx-font-size:14px;");

                // =================================================
                // TEXT FIELD FOCUS ANIMATION
                // =================================================

                field.focusedProperty().addListener(
                                (obs, oldValue, newValue) -> {

                                        ScaleTransition scale = new ScaleTransition(
                                                        Duration.millis(120),
                                                        field);

                                        if (newValue) {

                                                scale.setToX(1.02);
                                                scale.setToY(1.02);

                                        } else {

                                                scale.setToX(1.0);
                                                scale.setToY(1.0);
                                        }

                                        scale.play();
                                });

                return field;
        }

        // =====================================================
        // PASSWORD FIELD ANIMATION
        // =====================================================

        private void addPasswordAnimation(
                        PasswordField field) {

                field.focusedProperty().addListener(
                                (obs, oldValue, newValue) -> {

                                        ScaleTransition scale = new ScaleTransition(
                                                        Duration.millis(120),
                                                        field);

                                        if (newValue) {

                                                scale.setToX(1.02);
                                                scale.setToY(1.02);

                                        } else {

                                                scale.setToX(1.0);
                                                scale.setToY(1.0);
                                        }

                                        scale.play();
                                });
        }

        // =====================================================
        // ALERT
        // =====================================================

        private void showAlert(
                        String title,
                        String message) {

                Alert alert = new Alert(Alert.AlertType.WARNING);

                alert.setTitle(title);

                alert.setHeaderText(null);

                alert.setContentText(message);

                alert.showAndWait();
        }

        // =====================================================
        // LEFT IMAGE PANEL
        // =====================================================

        private VBox createLeftPanel() {

                VBox panel = new VBox();

                panel.setPrefWidth(550);
                panel.setPrefHeight(500);

                panel.setMinWidth(550);
                panel.setMinHeight(500);

                panel.setMaxWidth(540);
                panel.setMaxHeight(500);

                panel.setAlignment(
                                Pos.CENTER);

                panel.setPadding(
                                new Insets(0));

                panel.setStyle(
                                "-fx-background-color:" +
                                                DARK_BROWN + ";");

                // =================================================
                // LOAD IMAGE
                // =================================================

                Image image = new Image(getClass().getResourceAsStream("/image.png"));

                ImageView imageView = new ImageView(image);

                imageView.setFitWidth(520);

                imageView.setFitHeight(480);

                imageView.setPreserveRatio(false);

                imageView.setSmooth(true);

                // =================================================
                // IMAGE CONTAINER
                // =================================================

                StackPane imageContainer = new StackPane(imageView);

                imageContainer.setPrefWidth(600);
                imageContainer.setPrefHeight(700);

                imageContainer.setMinWidth(600);
                imageContainer.setMinHeight(700);

                imageContainer.setMaxWidth(600);
                imageContainer.setMaxHeight(700);

                panel.getChildren().add(
                                imageContainer);

                return panel;
        }
}