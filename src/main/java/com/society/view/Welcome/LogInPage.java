package com.society.view.Welcome;

import com.society.dao.Welcome.UserDao;
import com.society.model.Welcome.User;
import com.society.service.resident_service.ResidentSession;
import com.society.service.resident_service.SecretarySession;
import com.society.view.ScreenSize;

import com.society.view.Guard_portal.GuardDashboard;
import com.society.view.Owner_portal.OwnerDashboard;
import com.society.view.Resident_portal.ResidentDashboard;
import com.society.view.Secretary_portal.SecretaryDashboard;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LogInPage {

    // ================= COLORS =================

    private static final String DARK_BROWN = "#4E342E";
    private static final String BROWN = "#4E342E";
    private static final String PRIMARY_BROWN = "#4e342e";
    private static final String LIGHT_BROWN = "#D7CCC8";
    private static final String TEXT = "#3E2723";
    private static final String WHITE = "#FFFFFF";

    // =========================================================
    // SHOW LOGIN PAGE
    // =========================================================

    public Scene createScene(Stage stage) {

        // MAIN ROOT

        StackPane root = new StackPane();

        //root.setStyle(
                //"-fx-background-color: linear-gradient(to right, #E8DDD5, #bfb1a7);"
        //);

        Image bgImage = new Image(
                getClass()
                        .getResource("/background-Society360.jpeg")
                        .toExternalForm()
        );

        ImageView bgView = new ImageView(bgImage);

        bgView.setPreserveRatio(false);
        bgView.fitWidthProperty().bind(root.widthProperty());
        bgView.fitHeightProperty().bind(root.heightProperty());

        root.getChildren().add(bgView);

        // =====================================================
        // LEFT IMAGE + LOGIN CARD
        // =====================================================
// LEFT IMAGE PANEL
        VBox leftPanel = createLeftPanel();

        StackPane.setAlignment(
                leftPanel,
                Pos.CENTER_LEFT
        );

        StackPane.setMargin(
                leftPanel,
                new Insets(
                        20,
                        20,
                        20,
                        50
                )
        );

        // LOGIN CARD

        VBox loginCard =createLoginCard(stage);

        loginCard.setPrefWidth(500);
        loginCard.setMinWidth(450);
        loginCard.setMaxWidth(450);

        loginCard.setPrefHeight(600);
        loginCard.setMaxHeight(600);
         // RIGHT SIDE

        StackPane.setAlignment(
                loginCard,
                Pos.CENTER_RIGHT
        );

        StackPane.setMargin(
                loginCard,
                new Insets(
                        20,
                        100,
                        20,
                        20
                )
        );

        // ADD LEFT IMAGE + LOGIN CARD

        root.getChildren().addAll(
                leftPanel,
                loginCard
        );

        // LEFT PANEL ANIMATION
        leftPanel.setOpacity(0);
        leftPanel.setTranslateX(-80);

        FadeTransition leftFade =
                new FadeTransition(
                        Duration.millis(700),
                        leftPanel
                );

        leftFade.setFromValue(0);
        leftFade.setToValue(1);

        TranslateTransition leftSlide =
                new TranslateTransition(
                        Duration.millis(700),
                        leftPanel
                );

        leftSlide.setFromX(-80);
        leftSlide.setToX(0);

        ParallelTransition leftAnimation =
                new ParallelTransition(
                        leftFade,
                        leftSlide
                );

        leftAnimation.play();

        // LOGIN CARD ANIMATION
        loginCard.setOpacity(0);
        loginCard.setTranslateX(80);

        FadeTransition cardFade =
                new FadeTransition(
                        Duration.millis(700),
                        loginCard
                );

        cardFade.setFromValue(0);
        cardFade.setToValue(1);

        TranslateTransition cardSlide =
                new TranslateTransition(
                        Duration.millis(700),
                        loginCard
                );

        cardSlide.setFromX(80);
        cardSlide.setToX(0);

        ParallelTransition cardAnimation =
                new ParallelTransition(
                        cardFade,
                        cardSlide
                );

        cardAnimation.play();
        

        // =====================================================
        // SCENE
        // =====================================================

        Scene loginScene = new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );

        stage.setTitle("Society360 - Login");

        stage.setMinWidth(900);
        stage.setMinHeight(600);

        return loginScene;
    }

    // =========================================================
    // LEFT IMAGE PANEL
    // =========================================================

    private VBox createLeftPanel() {

        VBox panel = new VBox();

        panel.setPrefWidth(550);
        panel.setPrefHeight(500);

        panel.setMinWidth(550);
        panel.setMinHeight(500);

        panel.setMaxWidth(540);
        panel.setMaxHeight(500);

        panel.setAlignment(Pos.CENTER);

        panel.setPadding(new Insets(0));

        panel.setStyle(
                "-fx-background-color: " + DARK_BROWN + ";"
        );

        // LOAD IMAGE

        Image image = new Image(
                getClass().getResourceAsStream("/image.png")
        );

        ImageView imageView = new ImageView(image);

        // IMAGE SIZE

        imageView.setFitWidth(520);
        imageView.setFitHeight(480);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        // IMAGE CONTAINER

        StackPane imageContainer = new StackPane(imageView);

        imageContainer.setPrefWidth(600);
        imageContainer.setPrefHeight(700);

        imageContainer.setMinWidth(600);
        imageContainer.setMinHeight(700);

        imageContainer.setMaxWidth(600);
        imageContainer.setMaxHeight(700);

        // ADD IMAGE

        panel.getChildren().add(imageContainer);

        return panel;
    }

    // =========================================================
    // LOGIN CARD
    // =========================================================

    private VBox createLoginCard(Stage stage) {

        VBox card = new VBox(20);

        card.setMaxWidth(450);

        card.setPadding(
                new Insets(
                        45,
                        45,
                        40,
                        45
                )
        );

        card.setStyle(
                "-fx-background-color: #F8F3EE;"
                        + "-fx-background-radius: 15;"
                        + "-fx-border-color: #c7c3bd;"
                        + "-fx-border-radius: 15;"
                        + "-fx-border-width: 1.5;"
                        + "-fx-effect: dropshadow("
                        + "gaussian,"
                        + "rgba(78,52,46,0.18),"
                        + "20,"
                        + "0,"
                        + "0,"
                        + "8);"
        );

        // =====================================================
        // TITLE
        // =====================================================

        Label title = new Label("Welcome Back! 👋");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        28
                )
        );

        title.setTextFill(Color.web(TEXT));

        Label subtitle = new Label(
                " Login to continue to Society360"
        );

        subtitle.setFont(
                Font.font(
                        "Arial",
                        14
                )
        );

        subtitle.setTextFill(Color.web(BROWN));

        VBox titleBox = new VBox(
                6,
                title,
                subtitle
        );

        titleBox.setOpacity(0);
        titleBox.setTranslateY(15);

        FadeTransition titleFade =
                new FadeTransition(
                        Duration.millis(500),
                        titleBox
                );

        titleFade.setFromValue(0);
        titleFade.setToValue(1);

        TranslateTransition titleSlide =
                new TranslateTransition(
                        Duration.millis(500),
                        titleBox
                );

        titleSlide.setFromY(15);
        titleSlide.setToY(0);

        ParallelTransition titleAnimation =
                new ParallelTransition(
                        titleFade,
                        titleSlide
                );

        titleAnimation.play();

        // =====================================================
        // EMAIL
        // =====================================================

        TextField username = new TextField();

        username.setPromptText("Email");

        username.setPrefHeight(42);

        username.setStyle(inputStyle1());

        // EMAIL FOCUS ANIMATION

        username.focusedProperty().addListener(
                (obs, oldValue, newValue) -> {

                ScaleTransition scale =
                        new ScaleTransition(
                                Duration.millis(120),
                                username
                        );

                if (newValue) {
                        scale.setToX(1.02);
                        scale.setToY(1.02);
                } else {
                        scale.setToX(1.0);
                        scale.setToY(1.0);
                }

                scale.play();
                }
        );

        // =====================================================
        // PASSWORD
        // =====================================================

        PasswordField password = new PasswordField();

        password.setPromptText("Password");

        password.setPrefHeight(42);

        password.setStyle(inputStyle1());

        // PASSWORD FOCUS ANIMATION

        password.focusedProperty().addListener(
                (obs, oldValue, newValue) -> {

                ScaleTransition scale =
                        new ScaleTransition(
                                Duration.millis(120),
                                password
                        );

                if (newValue) {
                        scale.setToX(1.02);
                        scale.setToY(1.02);
                } else {
                        scale.setToX(1.0);
                        scale.setToY(1.0);
                }

                scale.play();
                }
        );

        // =====================================================
        // REMEMBER ME
        // =====================================================

        CheckBox remember = new CheckBox("Remember me");

        remember.setTextFill(Color.web(TEXT));

        // =====================================================
        // FORGOT PASSWORD
        // =====================================================

        Hyperlink forgot = new Hyperlink("Forgot Password?");

        forgot.setTextFill(Color.web(PRIMARY_BROWN));

        // FORGOT PASSWORD HOVER ANIMATION

        forgot.setOnMouseEntered(event -> {

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(120),
                        forgot
                );

        scale.setToX(1.08);
        scale.setToY(1.08);

        scale.play();
        });

        forgot.setOnMouseExited(event -> {

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(120),
                        forgot
                );

        scale.setToX(1.0);
        scale.setToY(1.0);

        scale.play();
        });

        forgot.setOnAction(event -> {

            TextInputDialog emailDialog =
                    new TextInputDialog();

            emailDialog.setTitle("Forgot Password");

            emailDialog.setHeaderText(
                    "Reset your password"
            );

            emailDialog.setContentText(
                    "Enter your Email:"
            );

            emailDialog.showAndWait().ifPresent(email -> {

                if (email.trim().isEmpty()) {

                    showAlert1(
                            Alert.AlertType.WARNING,
                            "Forgot Password",
                            "Please enter your Email."
                    );

                    return;
                }

                // Temporary OTP for testing

                String otp = "123456";

                TextInputDialog otpDialog =
                        new TextInputDialog();

                otpDialog.setTitle("OTP Verification");

                otpDialog.setHeaderText(
                        "OTP has been sent to " + email
                );

                otpDialog.setContentText(
                        "Enter OTP:"
                );

                otpDialog.showAndWait().ifPresent(inputOtp -> {

                    if (inputOtp.equals(otp)) {

                        showResetPasswordDialog(
                                stage,
                                email
                        );

                    } else {

                        showAlert1(
                                Alert.AlertType.ERROR,
                                "OTP Verification",
                                "Invalid OTP. Please try again."
                        );
                    }
                });
            });
        });

        // =====================================================
        // REMEMBER ROW
        // =====================================================

        HBox rememberRow = new HBox();

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        rememberRow.getChildren().addAll(
                remember,
                spacer,
                forgot
        );

        rememberRow.setAlignment(
                Pos.CENTER_LEFT
        );

        // =====================================================
        // LOGIN BUTTON
        // =====================================================

        Button loginButton =
                new Button("Login →");

        loginButton.setPrefHeight(52);

        loginButton.setMaxWidth(
                Double.MAX_VALUE
        );

        loginButton.setStyle(
                primaryButtonStyle1()
        );

        // LOGIN BUTTON HOVER ANIMATION

        loginButton.setOnMouseEntered(event -> {

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(120),
                        loginButton
                );

        scale.setToX(1.04);
        scale.setToY(1.04);

        scale.play();
        });

        loginButton.setOnMouseExited(event -> {

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(120),
                        loginButton
                );

        scale.setToX(1.0);
        scale.setToY(1.0);

        scale.play();
        });

        loginButton.setOnAction(event -> {

                ScaleTransition clickAnimation =
                new ScaleTransition(
                        Duration.millis(80),
                        loginButton
                );

                clickAnimation.setToX(0.96);
                clickAnimation.setToY(0.96);

                clickAnimation.setAutoReverse(true);
                clickAnimation.setCycleCount(2);

                clickAnimation.play();

            String email =
                    username.getText().trim().toLowerCase();

            String passwordText =
                    password.getText();

            // =================================================
            // CHECK EMPTY FIELDS
            // =================================================

            if (email.isEmpty()
                    || passwordText.isEmpty()) {

                showAlert1(
                        Alert.AlertType.WARNING,
                        "Login",
                        "Please enter your Email and Password."
                );

                return;
            }

            if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

                showAlert1(
                        Alert.AlertType.WARNING,
                        "Login",
                        "Please enter a valid email address."
                );

                return;
            }

            UserDao dao = new UserDao();

            // ================================================
            // STEP 1: FIREBASE AUTHENTICATION
            // ================================================

            boolean authenticated =
                    dao.authenticateUser(
                            email,
                            passwordText
                    );

            if (!authenticated) {

                UserDao.clearLoggedInUser();
                ResidentSession.clearSession();

                showAlert1(
                        Alert.AlertType.ERROR,
                        "Login Failed",
                        "Invalid Email or Password."
                );

                return;
            }

            // ================================================
            // STEP 2: GET USER ROLE
            // ================================================

            User user = dao.getUserByEmail(email);

            String role = user == null ? null : user.getRole();

            if (role == null || role.trim().isEmpty()) {

                UserDao.clearLoggedInUser();
                ResidentSession.clearSession();

                showAlert1(
                        Alert.AlertType.ERROR,
                        "Login Error",
                        "User profile not found."
                );

                return;
            }

            role = role.trim();
            UserDao.setLoggedInEmail(email);
            UserDao.setLoggedInRole(role);

                if (role.equalsIgnoreCase("resident")) {
        ResidentSession.setLoggedInEmail(email);
        } else if (role.equalsIgnoreCase("secretary")) {
        SecretarySession.setLoggedInEmail(email);
        } else {
        ResidentSession.clearSession();
        }

            // ================================================
            // STEP 3: OPEN DASHBOARD
            // ================================================

            if (role.equalsIgnoreCase("resident")) {

                ResidentDashboard dashboard =
                        new ResidentDashboard();

                stage.setScene(
                        dashboard.getResidentDashboardScene(
                                stage
                        )
                );

            } else if (
                    role.equalsIgnoreCase("owner")
            ) {

                stage.setScene(
                        OwnerDashboard.createScene(stage, email)
                );

            } else if (
                    role.equalsIgnoreCase("secretary")
            ) {

                SecretaryDashboard dashboard =
                        new SecretaryDashboard(user);

                stage.setScene(
                        dashboard.createScene(stage)
                );

            } else if (
                    role.equalsIgnoreCase("security")
                            || role.equalsIgnoreCase("guard")
            ) {

                stage.setScene(
                        GuardDashboard.createScene(stage, email)
                );

            } else {

                UserDao.clearLoggedInUser();
                ResidentSession.clearSession();

                showAlert1(
                        Alert.AlertType.ERROR,
                        "Login Error",
                        "Unknown user role: " + role
                );
            }
        });

        // =====================================================
        // NEW ACCOUNT
        // =====================================================

        Label account =
                new Label("Don't have an account?");

        account.setTextFill(
                Color.web(TEXT)
        );

        Hyperlink signup =
                new Hyperlink("Sign up");

        signup.setTextFill(
                Color.web(PRIMARY_BROWN)
        );

        // SIGN UP HOVER ANIMATION

        signup.setOnMouseEntered(event -> {

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(120),
                        signup
                );

        scale.setToX(1.08);
        scale.setToY(1.08);

        scale.play();
        });

        signup.setOnMouseExited(event -> {

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(120),
                        signup
                );

        scale.setToX(1.0);
        scale.setToY(1.0);

        scale.play();
        });

        signup.setOnAction(event -> {

            SigninPage signupPage =
                    new SigninPage();

            stage.setScene(
                    signupPage.createScene(stage)
            );
        });

        HBox signupRow =
                new HBox(
                        4,
                        account,
                        signup
                );

        signupRow.setAlignment(
                Pos.CENTER
        );

        // =====================================================
        // ADD TO CARD
        // =====================================================

        card.getChildren().addAll(
                titleBox,
                new Separator(),
                username,
                password,
                rememberRow,
                loginButton,
                signupRow
        );

        // =====================================================
        // FORM ELEMENT ENTRY ANIMATION
        // =====================================================

        animateNode(username, 200);
        animateNode(password, 300);
        animateNode(rememberRow, 400);
        animateNode(loginButton, 500);
        animateNode(signupRow, 600);

        return card;
    }

    // =========================================================
    // INPUT STYLE
    // =========================================================

    private String inputStyle1() {

        return
                "-fx-background-color: " + WHITE + ";"
                        + "-fx-background-radius: 10;"
                        + "-fx-border-color: " + LIGHT_BROWN + ";"
                        + "-fx-border-radius: 10;"
                        + "-fx-border-width: 1;"
                        + "-fx-padding: 0 15;"
                        + "-fx-font-size: 14px;"
                        + "-fx-text-fill: " + TEXT + ";"
                        + "-fx-prompt-text-fill: #9E8E8E;";
    }

    // =========================================================
    // BUTTON STYLE
    // =========================================================

    private String primaryButtonStyle1() {

        return
                "-fx-background-color: " + PRIMARY_BROWN + ";"
                        + "-fx-background-radius: 10;"
                        + "-fx-text-fill: " + WHITE + ";"
                        + "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;";
    }

    // =========================================================
    // RESET PASSWORD DIALOG
    // =========================================================

    private void showResetPasswordDialog(
            Stage stage,
            String email
    ) {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle("Reset Password");

        dialog.setHeaderText(
                "Create a new password"
        );

        ButtonType resetButton =
                new ButtonType(
                        "Reset Password"
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        resetButton,
                        ButtonType.CANCEL
                );

        VBox box =
                new VBox(12);

        box.setPadding(
                new Insets(20)
        );

        Label emailLabel =
                new Label("Email: " + email);

        PasswordField newPassword =
                new PasswordField();

        newPassword.setPromptText(
                "Enter new password"
        );

        newPassword.setPrefHeight(40);

        newPassword.setStyle(
                inputStyle1()
        );

        PasswordField confirmPassword =
                new PasswordField();

        confirmPassword.setPromptText(
                "Confirm new password"
        );

        confirmPassword.setPrefHeight(40);

        confirmPassword.setStyle(
                inputStyle1()
        );

        box.getChildren().addAll(
                emailLabel,
                newPassword,
                confirmPassword
        );

        dialog.getDialogPane()
                .setContent(box);

        dialog.setResultConverter(button -> {

            if (button == resetButton) {

                String password =
                        newPassword.getText();

                String confirm =
                        confirmPassword.getText();

                if (password.isEmpty()
                        || confirm.isEmpty()) {

                    showAlert1(
                            Alert.AlertType.WARNING,
                            "Reset Password",
                            "Please enter both passwords."
                    );

                    return null;
                }

                if (!password.equals(confirm)) {

                    showAlert1(
                            Alert.AlertType.ERROR,
                            "Reset Password",
                            "Passwords do not match."
                    );

                    return null;
                }

                if (password.length() < 6) {

                    showAlert1(
                            Alert.AlertType.WARNING,
                            "Reset Password",
                            "Password must contain at least 6 characters."
                    );

                    return null;
                }

                showAlert1(
                        Alert.AlertType.INFORMATION,
                        "Password Reset",
                        "Password reset successfully!"
                );
            }

            return button;
        });

        dialog.showAndWait();
    }

    // =========================================================
        // FORM ELEMENT ANIMATION
        // =========================================================

        private void animateNode(
                javafx.scene.Node node,
                int delay
        ) {

        node.setOpacity(0);
        node.setTranslateY(15);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(450),
                        node
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide =
                new TranslateTransition(
                        Duration.millis(450),
                        node
                );

        slide.setFromY(15);
        slide.setToY(0);

        ParallelTransition animation =
                new ParallelTransition(
                        fade,
                        slide
                );

        animation.setDelay(
                Duration.millis(delay)
        );

        animation.play();
        }

    // =========================================================
    // ALERT
    // =========================================================

    private void showAlert1(
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
}
