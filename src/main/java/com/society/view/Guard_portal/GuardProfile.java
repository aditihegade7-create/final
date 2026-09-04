package com.society.view.Guard_portal;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.society.controller.ImageUploadController;
import com.society.controller.Guard_Controller.GuardController;
import com.society.model.Guard_model.GuardModel;
import com.society.view.ScreenSize;
import com.society.view.Welcome.LogInPage;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GuardProfile {

    private static Scene profileScene;

    private static final GuardController guardController =
            new GuardController();

    private static final ImageUploadController imageUploadController =
            new ImageUploadController();

    // =====================================================
    // CREATE SCENE
    // =====================================================

    public static Scene createScene(
            Stage stage,
            String guardEmail) {

        // =====================================================
        // VALIDATE GUARD EMAIL
        // =====================================================

        if (guardEmail == null ||
                guardEmail.trim().isEmpty()) {

            System.out.println(
                    "Guard email is missing."
            );

            return null;
        }

        // =====================================================
        // GET GUARD
        // =====================================================

        GuardModel guard =
                guardController.getGuard(
                        guardEmail
                );

        // =====================================================
        // GET SOCIETY
        // =====================================================

        String society = null;

        if (guard != null &&
                guard.getSociety() != null &&
                !guard.getSociety().trim().isEmpty()) {

            society =
                    guard.getSociety().trim();
        }

        // =====================================================
        // SIDEBAR
        // =====================================================

        GuardSidebar sidebar =
                new GuardSidebar(
                        stage,
                        "Profile",
                        guardEmail,
                        society
                );

        // =====================================================
        // ROOT
        // =====================================================

        BorderPane root =
                new BorderPane();

        root.setLeft(
                sidebar.getSidebar()
        );

        // =====================================================
        // MAIN AREA
        // =====================================================

        VBox mainArea =
                new VBox();

        mainArea.setStyle(
                "-fx-background-color:#e8ddd5;"
        );

        // =====================================================
        // HEADER
        // =====================================================

        HBox header =
                new HBox();

        header.setPrefHeight(
                105
        );

        header.setPadding(
                new Insets(
                        25,
                        40,
                        25,
                        30
                )
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setStyle(
                "-fx-background-color:#4e342e;"
        );

        VBox headerTitle =
                new VBox(3);

        Label headerMain =
                new Label(
                        "My Profile"
                );

        headerMain.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        24
                )
        );

        headerMain.setTextFill(
                Color.WHITE
        );

        Label headerSubtitle =
                new Label(
                        "Manage your personal and account information"
                );

        headerSubtitle.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        headerSubtitle.setTextFill(
                Color.WHITE
        );

        headerTitle.getChildren().addAll(
                headerMain,
                headerSubtitle
        );

        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );

        LocalDate today =
                LocalDate.now();

        Label day =
                new Label(
                        today.format(
                                DateTimeFormatter.ofPattern(
                                        "EEEE"
                                )
                        )
                );

        day.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        day.setTextFill(
                Color.WHITE
        );

        Label date =
                new Label(
                        today.format(
                                DateTimeFormatter.ofPattern(
                                        "dd MMMM yyyy"
                                )
                        )
                );

        date.setFont(
                Font.font(
                        "System",
                        12
                )
        );

        date.setTextFill(
                Color.WHITE
        );

        VBox dateBox =
                new VBox(
                        2,
                        day,
                        date
                );

        dateBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        header.getChildren().addAll(
                headerTitle,
                headerSpacer,
                dateBox
        );

        // =====================================================
        // CONTENT AREA
        // =====================================================

        VBox contentArea =
                new VBox(18);

        contentArea.setPadding(
                new Insets(
                        20,
                        35,
                        25,
                        35
                )
        );

        contentArea.setStyle(
                "-fx-background-color:#e8ddd5;"
        );

        // =====================================================
        // PROFILE CARD
        // =====================================================

        VBox profileCard =
                new VBox(16);

        profileCard.setPrefWidth(
                320
        );

        profileCard.setMinWidth(
                320
        );

        profileCard.setPrefHeight(
                545
        );

        profileCard.setPadding(
                new Insets(
                        28
                )
        );

        profileCard.setAlignment(
                Pos.TOP_CENTER
        );

        profileCard.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:14;" +
                "-fx-border-color:#D8CDC5;" +
                "-fx-border-radius:14;"
        );

        // =====================================================
        // PHOTO CONTAINER
        // =====================================================

        StackPane photoContainer =
                new StackPane();

        photoContainer.setPrefSize(
                115,
                115
        );

        photoContainer.setMaxSize(
                115,
                115
        );

        // =====================================================
        // IMAGE VIEW
        // =====================================================

        ImageView profileImageView =
                new ImageView();

        profileImageView.setFitWidth(
                115
        );

        profileImageView.setFitHeight(
                115
        );

        profileImageView.setPreserveRatio(
                false
        );

        profileImageView.setSmooth(
                true
        );

        Circle clip =
                new Circle(
                        57.5,
                        57.5,
                        57.5
                );

        profileImageView.setClip(
                clip
        );

        // =====================================================
        // LOAD SAVED CLOUDINARY IMAGE
        // =====================================================

        boolean imageLoaded =
                false;

        if (guard != null) {

            String imageUrl =
                    guard.getProfileImageUrl();

            if (imageUrl != null &&
                    !imageUrl.trim().isEmpty()) {

                try {

                    Image cloudinaryImage =
                            new Image(
                                    imageUrl,
                                    true
                            );

                    if (!cloudinaryImage.isError()) {

                        profileImageView.setImage(
                                cloudinaryImage
                        );

                        imageLoaded = true;
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }

        // =====================================================
        // DEFAULT IMAGE
        // =====================================================

        if (!imageLoaded) {

            try {

                var inputStream =
                        GuardProfile.class
                                .getResourceAsStream(
                                        "/images/guard_profile.png"
                                );

                if (inputStream != null) {

                    Image defaultImage =
                            new Image(
                                    inputStream
                            );

                    profileImageView.setImage(
                            defaultImage
                    );

                    imageLoaded = true;
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        // =====================================================
        // FINAL FALLBACK
        // =====================================================

        if (imageLoaded) {

            photoContainer
                    .getChildren()
                    .add(
                            profileImageView
                    );

        } else {

            Circle circle =
                    new Circle(
                            57.5,
                            Color.web(
                                    "#4e342e"
                            )
                    );

            String initialText = "?";

            if (guard != null &&
                    guard.getName() != null &&
                    !guard.getName().trim().isEmpty()) {

                initialText =
                        guard.getName().trim()
                                .substring(0, 1)
                                .toUpperCase();
            }

            Label initial =
                    new Label(
                            initialText
                    );

            initial.setStyle(
                    "-fx-text-fill:white;" +
                    "-fx-font-size:42px;" +
                    "-fx-font-weight:bold;"
            );

            photoContainer
                    .getChildren()
                    .addAll(
                            circle,
                            initial
                    );
        }

        // =====================================================
        // CHANGE PHOTO
        // =====================================================

        Button changePhotoButton =
                new Button(
                        "Change Photo"
                );

        changePhotoButton.setPrefWidth(
                165
        );

        changePhotoButton.setPrefHeight(
                35
        );

        changePhotoButton.setStyle(
                "-fx-background-color:white;" +
                "-fx-text-fill:#4e342e;" +
                "-fx-font-weight:bold;" +
                "-fx-border-color:#4e342e;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        // =====================================================
        // CHANGE PHOTO ACTION
        // =====================================================

        changePhotoButton.setOnAction(e -> {

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Select Profile Photo"
            );

            fileChooser.getExtensionFilters()
                    .add(
                            new FileChooser.ExtensionFilter(
                                    "Image Files",
                                    "*.png",
                                    "*.jpg",
                                    "*.jpeg"
                            )
                    );

            File file =
                    fileChooser.showOpenDialog(
                            stage
                    );

            if (file == null) {

                return;
            }

            // =================================================
            // UPLOAD TO CLOUDINARY
            // =================================================

            String imageUrl =
                    imageUploadController.imageUpload(
                            file
                    );

            if (imageUrl == null ||
                    imageUrl.trim().isEmpty()) {

                System.out.println(
                        "Cloudinary upload failed."
                );

                return;
            }

            // =================================================
            // SAVE URL TO FIRESTORE
            // =================================================

            boolean saved =
                    guardController.updateProfileImage(
                            guardEmail,
                            imageUrl
                    );

            if (!saved) {

                System.out.println(
                        "Could not save image URL."
                );

                return;
            }

            // =================================================
            // UPDATE IMAGE
            // =================================================

            Image newImage =
                    new Image(
                            imageUrl,
                            true
                    );

            profileImageView.setImage(
                    newImage
            );

            photoContainer
                    .getChildren()
                    .clear();

            photoContainer
                    .getChildren()
                    .add(
                            profileImageView
                    );

            // =================================================
            // PHOTO UPDATE ANIMATION
            // =================================================

            playPhotoAnimation(
                    photoContainer
            );

            System.out.println(
                    "Profile photo updated successfully."
            );
        });

        // =====================================================
        // PROFILE DATA
        // =====================================================

        String name =
                guard != null &&
                guard.getName() != null
                        ? guard.getName()
                        : "";

        String phone =
                guard != null &&
                guard.getPhone() != null
                        ? guard.getPhone()
                        : "";

        String email =
                guard != null &&
                guard.getEmail() != null
                        ? guard.getEmail()
                        : guardEmail;

        String guardId =
                guard != null &&
                guard.getGuardId() != null
                        ? guard.getGuardId()
                        : "";

        String status =
                guard != null &&
                guard.getStatus() != null
                        ? guard.getStatus()
                        : "";

        String gate =
                guard != null &&
                guard.getGate() != null
                        ? guard.getGate()
                        : "";

        String shift =
                guard != null &&
                guard.getShift() != null
                        ? guard.getShift()
                        : "";

        String joiningDate =
                guard != null &&
                guard.getJoiningDate() != null
                        ? guard.getJoiningDate()
                        : "";

        // =====================================================
        // IDENTITY
        // =====================================================

        Label nameLabel =
                new Label(
                        name
                );

        nameLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        21
                )
        );

        nameLabel.setTextFill(
                Color.web(
                        "#3E2723"
                )
        );

        Label roleLabel =
                new Label(
                        "Security Guard"
                );

        roleLabel.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        roleLabel.setTextFill(
                Color.web(
                        "#795548"
                )
        );

        Label guardIdLabel =
                new Label(
                        "Guard ID: " +
                        guardId
                );

        guardIdLabel.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        guardIdLabel.setTextFill(
                Color.web(
                        "#607D8B"
                )
        );

        Label statusLabel =
                new Label(
                        status
                );

        statusLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        statusLabel.setTextFill(
                Color.web(
                        "#315B45"
                )
        );

        VBox identity =
                new VBox(5);

        identity.setAlignment(
                Pos.CENTER
        );

        identity.getChildren().addAll(
                nameLabel,
                roleLabel,
                guardIdLabel,
                statusLabel
        );

        Separator profileSeparator =
                new Separator();

        // =====================================================
        // EDIT / SAVE
        // =====================================================

        Button editButton =
                new Button(
                        "Edit Profile"
                );

        editButton.setPrefWidth(
                165
        );

        editButton.setPrefHeight(
                40
        );

        editButton.setStyle(
                "-fx-background-color:#4e342e;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        Button saveButton =
                new Button(
                        "Save Changes"
                );

        saveButton.setPrefWidth(
                165
        );

        saveButton.setPrefHeight(
                40
        );

        saveButton.setVisible(
                false
        );

        saveButton.setManaged(
                false
        );

        saveButton.setStyle(
                "-fx-background-color:#4e342e;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        // =====================================================
        // LOGOUT
        // =====================================================

        Button logoutButton =
                new Button(
                        "Logout"
                );

        logoutButton.setPrefWidth(
                165
        );

        logoutButton.setPrefHeight(
                40
        );

        logoutButton.setStyle(
                "-fx-background-color:white;" +
                "-fx-text-fill:#4e342e;" +
                "-fx-font-weight:bold;" +
                "-fx-border-color:#4e342e;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        VBox profileButtons =
                new VBox(10);

        profileButtons.setAlignment(
                Pos.CENTER
        );

        profileButtons.getChildren().addAll(
                changePhotoButton,
                editButton,
                saveButton,
                logoutButton
        );

        profileCard.getChildren().addAll(
                photoContainer,
                identity,
                profileSeparator,
                profileButtons
        );

        // =====================================================
        // INFORMATION CARD
        // =====================================================

        VBox informationCard =
                new VBox(17);

        informationCard.setPrefWidth(
                790
        );

        informationCard.setMinWidth(
                790
        );

        informationCard.setPrefHeight(
                545
        );

        informationCard.setPadding(
                new Insets(
                        25
                )
        );

        informationCard.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:14;" +
                "-fx-border-color:#D8CDC5;" +
                "-fx-border-radius:14;"
        );

        // =====================================================
        // PERSONAL INFORMATION
        // =====================================================

        Label personalTitle =
                createSectionTitle(
                        "Personal Information"
                );

        Separator personalSeparator =
                new Separator();

        TextField nameField =
                createField(
                        name
                );

        TextField phoneField =
                createField(
                        phone
                );

        TextField emailField =
                createField(
                        email
                );

        // Email is the authenticated guard identifier and must not be edited.
        emailField.setEditable(false);

        Label guardIdValue =
                createValueLabel(
                        guardId
                );

        HBox nameRow =
                createEditableInfoRow(
                        "Full Name",
                        nameField
                );

        HBox phoneRow =
                createEditableInfoRow(
                        "Phone Number",
                        phoneField
                );

        HBox emailRow =
                createEditableInfoRow(
                        "Email",
                        emailField
                );

        HBox guardIdRow =
                createInfoRow(
                        "Guard ID",
                        guardIdValue
                );

        VBox personalSection =
                new VBox(7);

        personalSection.getChildren().addAll(
                personalTitle,
                personalSeparator,
                nameRow,
                phoneRow,
                emailRow,
                guardIdRow
        );

        // =====================================================
        // DUTY INFORMATION
        // =====================================================

        Label dutyTitle =
                createSectionTitle(
                        "Duty Information"
                );

        Separator dutySeparator =
                new Separator();

        Label gateValue =
                createValueLabel(
                        gate
                );

        Label shiftValue =
                createValueLabel(
                        shift
                );

        Label joiningValue =
                createValueLabel(
                        joiningDate
                );

        Label dutyStatusValue =
                createValueLabel(
                        status
                );

        HBox gateRow =
                createInfoRow(
                        "Assigned Gate",
                        gateValue
                );

        HBox shiftRow =
                createInfoRow(
                        "Current Shift",
                        shiftValue
                );

        HBox joiningRow =
                createInfoRow(
                        "Joining Date",
                        joiningValue
                );

        HBox dutyStatusRow =
                createInfoRow(
                        "Account Status",
                        dutyStatusValue
                );

        VBox dutySection =
                new VBox(7);

        dutySection.getChildren().addAll(
                dutyTitle,
                dutySeparator,
                gateRow,
                shiftRow,
                joiningRow,
                dutyStatusRow
        );

        // =====================================================
        // SECURITY
        // =====================================================

        Label securityTitle =
                createSectionTitle(
                        "Security"
                );

        Separator securitySeparator =
                new Separator();

        Label passwordLabel =
                new Label(
                        "Password"
                );

        passwordLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        passwordLabel.setTextFill(
                Color.web(
                        "#607D8B"
                )
        );

        Label passwordValue =
                new Label(
                        "••••••••••••"
                );

        passwordValue.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        passwordValue.setTextFill(
                Color.web(
                        "#263238"
                )
        );

        VBox passwordInfo =
                new VBox(
                        4,
                        passwordLabel,
                        passwordValue
                );

        Region securitySpacer =
                new Region();

        HBox.setHgrow(
                securitySpacer,
                Priority.ALWAYS
        );

        Button changePasswordButton =
                new Button(
                        "Change Password"
                );

        changePasswordButton.setPrefWidth(
                155
        );

        changePasswordButton.setPrefHeight(
                37
        );

        changePasswordButton.setStyle(
                "-fx-background-color:white;" +
                "-fx-text-fill:#4e342e;" +
                "-fx-border-color:#4e342e;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

        HBox securityRow =
                new HBox();

        securityRow.setAlignment(
                Pos.CENTER_LEFT
        );

        securityRow.getChildren().addAll(
                passwordInfo,
                securitySpacer,
                changePasswordButton
        );

        VBox securitySection =
                new VBox(7);

        securitySection.getChildren().addAll(
                securityTitle,
                securitySeparator,
                securityRow
        );

        informationCard.getChildren().addAll(
                personalSection,
                dutySection,
                securitySection
        );

        // =====================================================
        // EDITABLE FIELDS
        // =====================================================

        TextField[] editableFields = {
                nameField,
                phoneField
        };

        setFieldsEditable(
                false,
                editableFields
        );

        // =====================================================
        // FIELD HOVER ANIMATION
        // =====================================================

        addFieldAnimation(
                nameField
        );

        addFieldAnimation(
                phoneField
        );

        addFieldAnimation(
                emailField
        );

        // =====================================================
        // BUTTON ANIMATIONS
        // =====================================================

        addButtonAnimation(
                changePhotoButton
        );

        addButtonAnimation(
                editButton
        );

        addButtonAnimation(
                saveButton
        );

        addButtonAnimation(
                logoutButton
        );

        addButtonAnimation(
                changePasswordButton
        );

        // =====================================================
        // CARD HOVER ANIMATION
        // =====================================================

        addCardHoverAnimation(
                profileCard
        );

        addCardHoverAnimation(
                informationCard
        );

        // =====================================================
        // EDIT PROFILE
        // =====================================================

        editButton.setOnAction(e -> {

            setFieldsEditable(
                    true,
                    editableFields
            );

            editButton.setVisible(
                    false
            );

            editButton.setManaged(
                    false
            );

            saveButton.setVisible(
                    true
            );

            saveButton.setManaged(
                    true
            );

            playScaleAnimation(
                    informationCard
            );
        });

        // =====================================================
        // SAVE CHANGES
        // =====================================================

        saveButton.setOnAction(e -> {

            boolean updated =
                    guardController.updateGuard(
                            guardEmail,
                            nameField.getText(),
                            phoneField.getText(),
                            guardId,
                            gate,
                            shift,
                            joiningDate,
                            status
                    );

            if (updated) {

                nameLabel.setText(
                        nameField.getText()
                );

                setFieldsEditable(
                        false,
                        editableFields
                );

                editButton.setVisible(
                        true
                );

                editButton.setManaged(
                        true
                );

                saveButton.setVisible(
                        false
                );

                saveButton.setManaged(
                        false
                );

                playScaleAnimation(
                        informationCard
                );

                System.out.println(
                        "Guard profile updated."
                );

            } else {

                playScaleAnimation(
                        informationCard
                );

                System.out.println(
                        "Failed to update guard profile."
                );
            }
        });

        // =====================================================
        // CHANGE PASSWORD
        // =====================================================

        changePasswordButton.setOnAction(e -> {

            playScaleAnimation(
                    informationCard
            );

            System.out.println(
                    "Change Password clicked"
            );
        });

        // =====================================================
        // LOGOUT
        // =====================================================

        logoutButton.setOnAction(e -> {

            playScaleAnimation(
                    profileCard
            );

            LogInPage loginPage =
                    new LogInPage();

            stage.setScene(
                    loginPage.createScene(
                            stage
                    )
            );

            stage.show();
        });

        // =====================================================
        // PROFILE LAYOUT
        // =====================================================

        HBox profileLayout =
                new HBox(20);

        profileLayout.setAlignment(
                Pos.TOP_LEFT
        );

        profileLayout.getChildren().addAll(
                profileCard,
                informationCard
        );

        contentArea.getChildren().add(
                profileLayout
        );

        mainArea.getChildren().addAll(
                header,
                contentArea
        );

        root.setCenter(
                mainArea
        );

        // =====================================================
        // PAGE ENTRANCE ANIMATION
        // =====================================================

        playPageAnimation(
                header,
                profileCard,
                informationCard
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
    // PAGE ENTRANCE ANIMATION
    // =========================================================

    private static void playPageAnimation(
            HBox header,
            VBox profileCard,
            VBox informationCard) {

        // =====================================================
        // INITIAL STATE
        // =====================================================

        header.setOpacity(
                0
        );

        profileCard.setOpacity(
                0
        );

        informationCard.setOpacity(
                0
        );

        // =====================================================
        // HEADER FADE
        // =====================================================

        FadeTransition headerFade =
                new FadeTransition(
                        Duration.millis(600),
                        header
                );

        headerFade.setFromValue(
                0
        );

        headerFade.setToValue(
                1
        );

        // =====================================================
        // HEADER SLIDE
        // =====================================================

        TranslateTransition headerSlide =
                new TranslateTransition(
                        Duration.millis(600),
                        header
                );

        headerSlide.setFromY(
                -25
        );

        headerSlide.setToY(
                0
        );

        headerFade.play();

        headerSlide.play();

        // =====================================================
        // PROFILE CARD
        // =====================================================

        FadeTransition profileFade =
                new FadeTransition(
                        Duration.millis(700),
                        profileCard
                );

        profileFade.setFromValue(
                0
        );

        profileFade.setToValue(
                1
        );

        TranslateTransition profileSlide =
                new TranslateTransition(
                        Duration.millis(700),
                        profileCard
                );

        profileSlide.setFromY(
                30
        );

        profileSlide.setToY(
                0
        );

        profileFade.setDelay(
                Duration.millis(150)
        );

        profileSlide.setDelay(
                Duration.millis(150)
        );

        profileFade.play();

        profileSlide.play();

        // =====================================================
        // INFORMATION CARD
        // =====================================================

        FadeTransition informationFade =
                new FadeTransition(
                        Duration.millis(750),
                        informationCard
                );

        informationFade.setFromValue(
                0
        );

        informationFade.setToValue(
                1
        );

        TranslateTransition informationSlide =
                new TranslateTransition(
                        Duration.millis(750),
                        informationCard
                );

        informationSlide.setFromY(
                30
        );

        informationSlide.setToY(
                0
        );

        informationFade.setDelay(
                Duration.millis(250)
        );

        informationSlide.setDelay(
                Duration.millis(250)
        );

        informationFade.play();

        informationSlide.play();
    }

    // =========================================================
    // BUTTON HOVER ANIMATION
    // =========================================================

    private static void addButtonAnimation(
            Button button) {

        button.setOnMouseEntered(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            scale.setToX(
                    1.04
            );

            scale.setToY(
                    1.04
            );

            scale.play();
        });

        button.setOnMouseExited(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            scale.setToX(
                    1
            );

            scale.setToY(
                    1
            );

            scale.play();
        });
    }

    // =========================================================
    // CARD HOVER ANIMATION
    // =========================================================

    private static void addCardHoverAnimation(
            VBox card) {

        card.setOnMouseEntered(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(150),
                            card
                    );

            scale.setToX(
                    1.01
            );

            scale.setToY(
                    1.01
            );

            scale.play();
        });

        card.setOnMouseExited(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(150),
                            card
                    );

            scale.setToX(
                    1
            );

            scale.setToY(
                    1
            );

            scale.play();
        });
    }

    // =========================================================
    // FIELD HOVER ANIMATION
    // =========================================================

    private static void addFieldAnimation(
            javafx.scene.Node field) {

        field.setOnMouseEntered(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            field
                    );

            scale.setToX(
                    1.01
            );

            scale.setToY(
                    1.01
            );

            scale.play();
        });

        field.setOnMouseExited(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            field
                    );

            scale.setToX(
                    1
            );

            scale.setToY(
                    1
            );

            scale.play();
        });
    }

    // =========================================================
    // CARD FEEDBACK ANIMATION
    // =========================================================

    private static void playScaleAnimation(
            javafx.scene.Node node) {

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(150),
                        node
                );

        scale.setFromX(
                1
        );

        scale.setFromY(
                1
        );

        scale.setToX(
                1.015
        );

        scale.setToY(
                1.015
        );

        scale.setAutoReverse(
                true
        );

        scale.setCycleCount(
                2
        );

        scale.play();
    }

    // =========================================================
    // PHOTO ANIMATION
    // =========================================================

    private static void playPhotoAnimation(
            javafx.scene.Node node) {

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(350),
                        node
                );

        scale.setFromX(
                0.85
        );

        scale.setFromY(
                0.85
        );

        scale.setToX(
                1
        );

        scale.setToY(
                1
        );

        scale.play();
    }

    // =========================================================
    // SECTION TITLE
    // =========================================================

    private static Label createSectionTitle(
            String text) {

        Label label =
                new Label(
                        text
                );

        label.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );

        label.setTextFill(
                Color.web(
                        "#3E2723"
                )
        );

        return label;
    }

    // =========================================================
    // VALUE LABEL
    // =========================================================

    private static Label createValueLabel(
            String text) {

        Label label =
                new Label(
                        text
                );

        label.setPrefWidth(
                500
        );

        label.setMinWidth(
                500
        );

        label.setPrefHeight(
                34
        );

        label.setAlignment(
                Pos.CENTER_LEFT
        );

        label.setPadding(
                new Insets(
                        0,
                        10,
                        0,
                        10
                )
        );

        label.setStyle(
                "-fx-background-color:#F4F7F4;" +
                "-fx-background-radius:6;" +
                "-fx-border-color:#CBD5D8;" +
                "-fx-border-radius:6;" +
                "-fx-text-fill:#263238;" +
                "-fx-font-size:13px;"
        );

        return label;
    }

    // =========================================================
    // TEXT FIELD
    // =========================================================

    private static TextField createField(
            String text) {

        TextField field =
                new TextField(
                        text
                );

        field.setPrefWidth(
                500
        );

        field.setMinWidth(
                500
        );

        field.setPrefHeight(
                34
        );

        field.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#CBD5D8;" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;" +
                "-fx-text-fill:#263238;" +
                "-fx-font-size:13px;"
        );

        return field;
    }

    // =========================================================
    // READ ONLY ROW
    // =========================================================

    private static HBox createInfoRow(
            String labelText,
            Label value) {

        Label label =
                new Label(
                        labelText
                );

        label.setPrefWidth(
                120
        );

        label.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        label.setTextFill(
                Color.web(
                        "#263238"
                )
        );

        HBox row =
                new HBox(
                        15
                );

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.getChildren().addAll(
                label,
                value
        );

        return row;
    }

    // =========================================================
    // EDITABLE ROW
    // =========================================================

    private static HBox createEditableInfoRow(
            String labelText,
            TextField field) {

        Label label =
                new Label(
                        labelText
                );

        label.setPrefWidth(
                120
        );

        label.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        label.setTextFill(
                Color.web(
                        "#263238"
                )
        );

        HBox row =
                new HBox(
                        15
                );

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.getChildren().addAll(
                label,
                field
        );

        return row;
    }

    // =========================================================
    // EDITABLE FIELDS
    // =========================================================

    private static void setFieldsEditable(
            boolean editable,
            TextField[] fields) {

        for (
                TextField field :
                fields
        ) {

            field.setEditable(
                    editable
            );
        }
    }
}