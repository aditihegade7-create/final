
package com.society.view.Owner_portal;

import java.io.File;

import com.society.controller.ImageUploadController;
import com.society.controller.Owner_Controller.ResidentController;
import com.society.model.Owner_model.ResidentModel;
import com.society.view.ScreenSize;
import com.society.view.Welcome.LogInPage;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

/**
 * =========================================================
 * OWNER PROFILE
 * =========================================================
 *
 * Handles:
 *
 * 1. Fetch owner information from Firestore
 * 2. Display owner information
 * 3. Display Cloudinary profile image
 * 4. Change profile photo
 * 5. Edit profile information
 * 6. Save edited information to Firestore
 * 7. Cancel editing
 * 8. Logout
 *
 * UI + Animation added without changing backend functionality.
 *
 */
public class OwnerProfile {

    // =========================================================
    // CONTROLLER
    // =========================================================

    private static final ResidentController residentController =
            new ResidentController();

    // =========================================================
    // CREATE PROFILE SCENE
    // =========================================================

    public static Scene createScene(
            Stage stage,
            String ownerEmail) {

        // =====================================================
        // NORMALIZE EMAIL
        // =====================================================

        final String finalOwnerEmail =
                ownerEmail == null
                        ? ""
                        : ownerEmail.trim().toLowerCase();

        // =====================================================
        // ROOT
        // =====================================================

        BorderPane root = new BorderPane();

        // =====================================================
        // SIDEBAR
        // =====================================================

        OwnerSidebar sidebar =
                new OwnerSidebar(
                        stage,
                        finalOwnerEmail
                );

        root.setLeft(
                sidebar.getSidebar()
        );

        // =====================================================
        // LOAD OWNER FROM FIRESTORE
        // =====================================================

        ResidentModel owner = null;

        if (!finalOwnerEmail.isEmpty()) {

            owner =
                    residentController.getOwnerByEmail(
                            finalOwnerEmail
                    );
        }

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainContent =
                new VBox(20);

        mainContent.setPadding(
                new Insets(
                        30,
                        40,
                        30,
                        40
                )
        );

        mainContent.setSpacing(20);

        mainContent.setAlignment(
                Pos.TOP_LEFT
        );

        // =====================================================
        // BACKGROUND IMAGE
        // =====================================================

        try {

            Image image =
                    new Image(
                            OwnerProfile.class
                                    .getResource(
                                            "/background-Dashboard5.jpeg"
                                    )
                                    .toExternalForm()
                    );

            BackgroundImage backgroundImage =
                    new BackgroundImage(
                            image,
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

            mainContent.setBackground(
                    new Background(
                            backgroundImage
                    )
            );

        } catch (Exception e) {

            mainContent.setStyle(
                    "-fx-background-color:#e8ddd5;"
            );
        }

        // =====================================================
        // HEADER
        // =====================================================

        // =====================================================
// HEADER
// =====================================================

HBox header =
        new HBox();

header.setPrefHeight(80);

header.setPadding(
        new Insets(
                15,
                25,
                15,
                25
        )
);

header.setAlignment(
        Pos.CENTER_LEFT
);

header.setStyle(
        "-fx-background-color:#4e342e;"
);

// =====================================================
// HEADER TEXT
// =====================================================

VBox headerText =
        new VBox(4);

Label greeting =
        new Label(
                "Owner Profile"
        );

greeting.setStyle(
        "-fx-font-family:'Bodoni MT';" +
        "-fx-font-size:24px;" +
        "-fx-font-weight:bold;" +
        "-fx-text-fill:white;"
);

Label description =
        new Label(
                "View and manage owner details"
        );

description.setStyle(
        "-fx-font-size:14px;" +
        "-fx-text-fill:white;"
);

headerText.getChildren().addAll(
        greeting,
        description
);

// =====================================================
// HEADER SPACER
// =====================================================

Region headerSpacer =
        new Region();

HBox.setHgrow(
        headerSpacer,
        Priority.ALWAYS
);

// =====================================================
// DATE
// =====================================================

Label day =
        new Label();

Label date =
        new Label();

java.time.LocalDate today =
        java.time.LocalDate.now();

day.setText(
        today.format(
                java.time.format.DateTimeFormatter
                        .ofPattern("EEEE")
        )
);

date.setText(
        today.format(
                java.time.format.DateTimeFormatter
                        .ofPattern("dd MMMM yyyy")
        )
);

day.setStyle(
        "-fx-text-fill:white;" +
        "-fx-font-size:13px;"
);

date.setStyle(
        "-fx-text-fill:white;" +
        "-fx-font-size:12px;"
);

VBox dateBox =
        new VBox(3);

dateBox.setAlignment(
        Pos.CENTER_RIGHT
);

dateBox.getChildren().addAll(
        day,
        date
);

// =====================================================
// HEADER CHILDREN
// =====================================================

header.getChildren().addAll(
        headerText,
        headerSpacer,
        dateBox
);

        // =====================================================
        // PAGE HEADING
        // =====================================================

        Label heading =
                new Label(
                        "PROFILE"
                );

        heading.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );

        heading.setTextFill(
                Color.web("#593a32")
        );

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label(
                        "Owner Profile"
                );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        27
                )
        );

        title.setTextFill(
                Color.web("#3E2723")
        );

        Label subtitle =
                new Label(
                        "View and manage your personal details"
                );

        subtitle.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        subtitle.setTextFill(
                Color.web("#795548")
        );

        VBox titleBox =
                new VBox(
                        4
                );

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        // =====================================================
        // OWNER DATA DEFAULT VALUES
        // =====================================================

        String nameText =
                "Owner Name";

        String emailText =
                finalOwnerEmail;

        String phoneText =
                "-";

        String flatText =
                "-";

        String societyText =
                "-";

        String statusText =
                "Active";

        String accountTypeText =
                "Owner";

        String ownerIdText =
                "";

        String profileImageUrl =
                null;

        // =====================================================
        // LOAD OWNER DATA
        // =====================================================

        if (owner != null) {

            if (owner.getName() != null &&
                    !owner.getName().trim().isEmpty()) {

                nameText =
                        owner.getName().trim();
            }

            if (owner.getEmail() != null &&
                    !owner.getEmail().trim().isEmpty()) {

                emailText =
                        owner.getEmail()
                                .trim()
                                .toLowerCase();
            }

            if (owner.getPhone() != null &&
                    !owner.getPhone().trim().isEmpty()) {

                phoneText =
                        owner.getPhone().trim();
            }

            if (owner.getFlat() != null &&
                    !owner.getFlat().trim().isEmpty()) {

                flatText =
                        owner.getFlat().trim();
            }

            if (owner.getSociety() != null &&
                    !owner.getSociety().trim().isEmpty()) {

                societyText =
                        owner.getSociety().trim();
            }

            if (owner.getStatus() != null &&
                    !owner.getStatus().trim().isEmpty()) {

                statusText =
                        owner.getStatus().trim();
            }

            if (owner.getResidentType() != null &&
                    !owner.getResidentType().trim().isEmpty()) {

                accountTypeText =
                        owner.getResidentType().trim();
            }

            if (owner.getOwnerId() != null &&
                    !owner.getOwnerId().trim().isEmpty()) {

                ownerIdText =
                        owner.getOwnerId().trim();
            }

            if (owner.getProfileImageUrl() != null &&
                    !owner.getProfileImageUrl()
                            .trim()
                            .isEmpty()) {

                profileImageUrl =
                        owner.getProfileImageUrl().trim();
            }
        }

        // =====================================================
        // FINAL VALUES FOR LAMBDA
        // =====================================================

        final String finalNameText =
                nameText;

        final String finalEmailText =
                emailText;

        final String finalPhoneText =
                phoneText;

        final String finalFlatText =
                flatText;

        final String finalSocietyText =
                societyText;

        // =====================================================
        // LEFT PROFILE CARD
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
                new Insets(28)
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
        // PROFILE PHOTO CONTAINER
        // =====================================================

        StackPane photoContainer =
                new StackPane();

        photoContainer.setPrefSize(
                135,
                135
        );

        photoContainer.setMaxSize(
                135,
                135
        );

        Circle photoBackground =
                new Circle(
                        67.5,
                        Color.web("#EFE4DC")
                );

        photoContainer.getChildren().add(
                photoBackground
        );

        // =====================================================
        // LOAD PROFILE IMAGE
        // =====================================================

        Image profileImage =
                null;

        try {

            if (profileImageUrl != null &&
                    !profileImageUrl.trim().isEmpty()) {

                profileImage =
                        new Image(
                                profileImageUrl,
                                135,
                                135,
                                false,
                                true,
                                true
                        );
            }

        } catch (Exception e) {

            profileImage =
                    null;
        }

        // =====================================================
        // LOCAL FALLBACK IMAGE
        // =====================================================

        if (profileImage == null ||
                profileImage.isError()) {

            try {

                if (OwnerProfile.class
                        .getResourceAsStream(
                                "/images/owner_profile.png"
                        ) != null) {

                    profileImage =
                            new Image(
                                    OwnerProfile.class
                                            .getResourceAsStream(
                                                    "/images/owner_profile.png"
                                            )
                            );
                }

            } catch (Exception e) {

                profileImage =
                        null;
            }
        }

        // =====================================================
        // DISPLAY IMAGE
        // =====================================================

        if (profileImage != null &&
                !profileImage.isError()) {

            ImageView imageView =
                    new ImageView(
                            profileImage
                    );

            imageView.setFitWidth(
                    135
            );

            imageView.setFitHeight(
                    135
            );

            imageView.setPreserveRatio(
                    false
            );

            imageView.setSmooth(
                    true
            );

            Circle clip =
                    new Circle(
                            67.5,
                            67.5,
                            67.5
                    );

            imageView.setClip(
                    clip
            );

            photoContainer.getChildren().add(
                    imageView
            );

        } else {

            String initialsText =
                    getInitials(
                            finalNameText
                    );

            Label initials =
                    new Label(
                            initialsText
                    );

            initials.setFont(
                    Font.font(
                            "System",
                            FontWeight.BOLD,
                            36
                    )
            );

            initials.setTextFill(
                    Color.WHITE
            );

            initials.setAlignment(
                    Pos.CENTER
            );

            initials.setPrefSize(
                    135,
                    135
            );

            initials.setStyle(
                    "-fx-background-color:#789098;" +
                    "-fx-background-radius:70;"
            );

            photoContainer.getChildren().add(
                    initials
            );
        }

        // =====================================================
        // CHANGE PHOTO BUTTON
        // =====================================================

        Button changePhotoButton =
                new Button(
                        "Change Photo"
                );

        changePhotoButton.setPrefWidth(
                220
        );

        changePhotoButton.setPrefHeight(
                36
        );

        changePhotoButton.setStyle(
                "-fx-background-color:white;" +
                "-fx-text-fill:#593a32;" +
                "-fx-border-color:#593a32;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

        // =====================================================
        // CHANGE PHOTO ACTION
        // =====================================================

        changePhotoButton.setOnAction(e -> {

            if (finalOwnerEmail.isEmpty()) {

                showError(
                        "Profile Error",
                        "Owner email is not available."
                );

                return;
            }

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Select Profile Photo"
            );

            FileChooser.ExtensionFilter imageFilter =
                    new FileChooser.ExtensionFilter(
                            "Image Files",
                            "*.png",
                            "*.jpg",
                            "*.jpeg",
                            "*.webp"
                    );

            fileChooser
                    .getExtensionFilters()
                    .add(
                            imageFilter
                    );

            File file =
                    fileChooser.showOpenDialog(
                            stage
                    );

            if (file == null) {
                return;
            }

            try {

                // =================================================
                // UPLOAD TO CLOUDINARY
                // =================================================

                ImageUploadController uploader =
                        new ImageUploadController();

                String cloudinaryUrl =
                        uploader.imageUpload(
                                file
                        );

                if (cloudinaryUrl == null ||
                        cloudinaryUrl.trim().isEmpty()) {

                    showError(
                            "Upload Failed",
                            "Profile photo could not be uploaded."
                    );

                    return;
                }

                // =================================================
                // SAVE CLOUDINARY URL TO FIRESTORE
                // =================================================

                boolean saved =
                        residentController
                                .updateOwnerProfileImage(
                                        finalOwnerEmail,
                                        cloudinaryUrl
                                );

                if (!saved) {

                    showError(
                            "Save Failed",
                            "Photo uploaded, but the profile image could not be saved."
                    );

                    return;
                }

                showInformation(
                        "Profile Photo Updated",
                        "Your profile photo has been updated successfully."
                );

                // =================================================
                // RELOAD PROFILE
                // =================================================

                stage.setScene(
                        OwnerProfile.createScene(
                                stage,
                                finalOwnerEmail
                        )
                );

                stage.show();

            } catch (Exception ex) {

                ex.printStackTrace();

                showError(
                        "Photo Upload Error",
                        "Unable to update your profile photo.\n\n"
                                + ex.getMessage()
                );
            }
        });

        // =====================================================
        // OWNER NAME
        // =====================================================

        Label name =
                new Label(
                        finalNameText
                );

        name.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        22
                )
        );

        name.setTextFill(
                Color.web("#3E2723")
        );

        // =====================================================
        // ROLE
        // =====================================================

        Label role =
                new Label(
                        "Owner"
                );

        role.setFont(
                Font.font(
                        "System",
                        FontWeight.NORMAL,
                        14
                )
        );

        role.setTextFill(
                Color.web("#789098")
        );

        // =====================================================
        // SOCIETY
        // =====================================================

        Label society =
                new Label(
                        societyText.equals("-")
                                ? "Society not available"
                                : societyText
                );

        society.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        14
                )
        );

        society.setTextFill(
                Color.web("#593a32")
        );

        // =====================================================
        // FLAT
        // =====================================================

        Label flat =
                new Label(
                        flatText.equals("-")
                                ? "Flat not available"
                                : "Flat " + flatText
                );

        flat.setFont(
                Font.font(
                        "System",
                        FontWeight.NORMAL,
                        13
                )
        );

        flat.setTextFill(
                Color.web("#795548")
        );

        // =====================================================
        // IDENTITY
        // =====================================================

        VBox identity =
                new VBox(5);

        identity.setAlignment(
                Pos.CENTER
        );

        identity.getChildren().addAll(
                name,
                role,
                society,
                flat
        );

        // =====================================================
        // PROFILE SEPARATOR
        // =====================================================

        Separator profileSeparator =
                new Separator();

        // =====================================================
        // EDIT PROFILE BUTTON
        // =====================================================

        Button editProfile =
                new Button(
                        "Edit Profile"
                );

        editProfile.setPrefWidth(
                220
        );

        editProfile.setPrefHeight(
                40
        );

        editProfile.setStyle(
                "-fx-background-color:#593a32;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        // =====================================================
        // EDIT PROFILE ACTION
        // =====================================================

        editProfile.setOnAction(e -> {

            stage.setScene(
                    createEditScene(
                            stage,
                            finalEmailText
                    )
            );

            stage.show();
        });

        // =====================================================
        // LOGOUT BUTTON
        // =====================================================

        Button logoutButton =
                new Button(
                        "Logout"
                );

        logoutButton.setPrefWidth(
                220
        );

        logoutButton.setPrefHeight(
                40
        );

        logoutButton.setStyle(
                "-fx-background-color:#F4E2DF;" +
                "-fx-text-fill:#B0443E;" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        // =====================================================
        // LOGOUT ACTION
        // =====================================================

        logoutButton.setOnAction(e -> {

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
        // PROFILE BUTTONS
        // =====================================================

        VBox profileButtons =
                new VBox(10);

        profileButtons.setAlignment(
                Pos.CENTER
        );

        profileButtons.getChildren().addAll(
                changePhotoButton,
                editProfile,
                logoutButton
        );

        // =====================================================
        // PROFILE CARD
        // =====================================================

        profileCard.getChildren().addAll(
                photoContainer,
                identity,
                profileSeparator,
                profileButtons
        );

        // =====================================================
        // RIGHT INFORMATION CARD
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
                new Insets(25)
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

        HBox nameRow =
                createInfoRow(
                        "Full Name",
                        finalNameText
                );

        HBox phoneRow =
                createInfoRow(
                        "Phone Number",
                        finalPhoneText
                );

        HBox emailRow =
                createInfoRow(
                        "Email",
                        finalEmailText
                );

        HBox flatRow =
                createInfoRow(
                        "Flat / Unit",
                        finalFlatText
                );

        VBox personalSection =
                new VBox(7);

        personalSection.getChildren().addAll(
                personalTitle,
                personalSeparator,
                nameRow,
                phoneRow,
                emailRow,
                flatRow
        );

        // =====================================================
        // ACCOUNT INFORMATION
        // =====================================================

        Label accountTitle =
                createSectionTitle(
                        "Account Information"
                );

        Separator accountSeparator =
                new Separator();

        HBox statusRow =
                createInfoRow(
                        "Account Status",
                        statusText
                );

        HBox accountTypeRow =
                createInfoRow(
                        "Account Type",
                        accountTypeText
                );

        HBox societyRow =
                createInfoRow(
                        "Society",
                        finalSocietyText
                );

        HBox ownerIdRow =
                createInfoRow(
                        "Owner ID",
                        ownerIdText
                );

        VBox accountSection =
                new VBox(7);

        accountSection.getChildren().addAll(
                accountTitle,
                accountSeparator,
                statusRow,
                accountTypeRow,
                societyRow,
                ownerIdRow
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
                Color.web("#607D8B")
        );

        Label passwordValue =
                new Label(
                        "••••••••••••"
                );

        passwordValue.setFont(
                Font.font(
                        "System",
                        FontWeight.NORMAL,
                        14
                )
        );

        passwordValue.setTextFill(
                Color.web("#263238")
        );

        VBox passwordInfo =
                new VBox(4);

        passwordInfo.getChildren().addAll(
                passwordLabel,
                passwordValue
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
                "-fx-text-fill:#593a32;" +
                "-fx-border-color:#593a32;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

        changePasswordButton.setOnAction(e -> {

            showInformation(
                    "Change Password",
                    "Password change should be handled through Firebase Authentication."
            );
        });

        HBox securityContent =
                new HBox();

        securityContent.setAlignment(
                Pos.CENTER_LEFT
        );

        Region securitySpacer =
                new Region();

        HBox.setHgrow(
                securitySpacer,
                Priority.ALWAYS
        );

        securityContent.getChildren().addAll(
                passwordInfo,
                securitySpacer,
                changePasswordButton
        );

        VBox securitySection =
                new VBox(7);

        securitySection.getChildren().addAll(
                securityTitle,
                securitySeparator,
                securityContent
        );

        // =====================================================
        // INFORMATION CARD
        // =====================================================

        informationCard.getChildren().addAll(
                personalSection,
                accountSection,
                securitySection
        );

        // =====================================================
        // PROFILE CONTENT
        // =====================================================

        HBox profileContent =
                new HBox(22);

        profileContent.setAlignment(
                Pos.TOP_LEFT
        );

        profileContent.getChildren().addAll(
                profileCard,
                informationCard
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        mainContent.getChildren().addAll(
                heading,
                titleBox,
                profileContent
        );

        root.setCenter(
                mainContent
        );

        // =====================================================
        // PAGE ANIMATIONS
        // =====================================================

      
        animateProfileCard(
                profileCard
        );

        animateInformationCard(
                informationCard
        );

        animateButton(
                changePhotoButton
        );

        animateButton(
                editProfile
        );

        animateButton(
                logoutButton
        );

        animateButton(
                changePasswordButton
        );

        // =====================================================
        // MAIN AREA
        // =====================================================

        BorderPane mainarea =
                new BorderPane();

        mainarea.setTop(
                header
        );

        mainarea.setCenter(
                mainContent
        );

        root.setCenter(
                mainarea
        );

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        root,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        stage.setScene(
                scene
        );

        stage.setMaximized(
                true
        );

        return scene;
    }

    // =========================================================
    // EDIT PROFILE SCENE
    // =========================================================

    private static Scene createEditScene(
            Stage stage,
            String ownerEmail) {

        // =====================================================
        // NORMALIZE EMAIL
        // =====================================================

        final String finalOwnerEmail =
                ownerEmail == null
                        ? ""
                        : ownerEmail.trim().toLowerCase();

        // =====================================================
        // ROOT
        // =====================================================

        BorderPane root =
                new BorderPane();

        // =====================================================
        // SIDEBAR
        // =====================================================

        OwnerSidebar sidebar =
                new OwnerSidebar(
                        stage,
                        finalOwnerEmail
                );

        root.setLeft(
                sidebar.getSidebar()
        );

        // =====================================================
        // LOAD CURRENT OWNER DATA
        // =====================================================

        ResidentModel owner =
                null;

        if (!finalOwnerEmail.isEmpty()) {

            owner =
                    residentController.getOwnerByEmail(
                            finalOwnerEmail
                    );
        }

        // =====================================================
        // CURRENT VALUES
        // =====================================================

        String currentName =
                "";

        String currentPhone =
                "";

        String currentFlat =
                "";

        String currentSociety =
                "";

        if (owner != null) {

            if (owner.getName() != null) {

                currentName =
                        owner.getName().trim();
            }

            if (owner.getPhone() != null) {

                currentPhone =
                        owner.getPhone().trim();
            }

            if (owner.getFlat() != null) {

                currentFlat =
                        owner.getFlat().trim();
            }

            if (owner.getSociety() != null) {

                currentSociety =
                        owner.getSociety().trim();
            }
        }

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainContent =
                new VBox(18);

        mainContent.setPadding(
                new Insets(
                        25,
                        35,
                        25,
                        35
                )
        );

        // =====================================================
        // BACKGROUND
        // =====================================================

        try {

            Image image =
                    new Image(
                            OwnerProfile.class
                                    .getResource(
                                            "/background-Dashboard5.jpeg"
                                    )
                                    .toExternalForm()
                    );

            BackgroundImage backgroundImage =
                    new BackgroundImage(
                            image,
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

            mainContent.setBackground(
                    new Background(
                            backgroundImage
                    )
            );

        } catch (Exception e) {

            mainContent.setStyle(
                    "-fx-background-color:#e8ddd5;"
            );
        }

        // =====================================================
        // HEADING
        // =====================================================

        Label heading =
                new Label(
                        "PROFILE"
                );

        heading.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );

        heading.setTextFill(
                Color.web("#593a32")
        );

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label(
                        "Edit Profile"
                );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        26
                )
        );

        title.setTextFill(
                Color.web("#3E2723")
        );

        Label subtitle =
                new Label(
                        "Update your personal and account information"
                );

        subtitle.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        subtitle.setTextFill(
                Color.web("#795548")
        );

        VBox titleBox =
                new VBox(
                        4
                );

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        // =====================================================
        // EDIT CARD
        // =====================================================

        VBox editCard =
                new VBox(20);

        editCard.setPadding(
                new Insets(30)
        );

        editCard.setMaxWidth(
                900
        );

        editCard.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:14;" +
                "-fx-border-color:#D8CDC5;" +
                "-fx-border-radius:14;"
        );

        // =====================================================
        // SECTION TITLE
        // =====================================================

        Label personalTitle =
                createSectionTitle(
                        "Personal Information"
                );

        Separator separator =
                new Separator();

        // =====================================================
        // TEXT FIELDS
        // =====================================================

        TextField nameField =
                new TextField(
                        cleanFieldValue(
                                currentName
                        )
                );

        TextField phoneField =
                new TextField(
                        cleanFieldValue(
                                currentPhone
                        )
                );

        TextField flatField =
                new TextField(
                        cleanFieldValue(
                                currentFlat
                        )
                );

        TextField societyField =
                new TextField(
                        cleanFieldValue(
                                currentSociety
                        )
                );

        TextField emailField =
                new TextField(
                        finalOwnerEmail
                );

        // =====================================================
        // EMAIL CANNOT BE CHANGED
        // =====================================================

        emailField.setEditable(
                false
        );

        emailField.setFocusTraversable(
                false
        );

        // =====================================================
        // FIELD HEIGHTS
        // =====================================================

        nameField.setPrefHeight(40);
        phoneField.setPrefHeight(40);
        flatField.setPrefHeight(40);
        societyField.setPrefHeight(40);
        emailField.setPrefHeight(40);

        // =====================================================
        // FIELD STYLE
        // =====================================================

        String fieldStyle =
                "-fx-background-color:white;" +
                "-fx-border-color:#CBD5D8;" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;";

        nameField.setStyle(fieldStyle);
        phoneField.setStyle(fieldStyle);
        flatField.setStyle(fieldStyle);
        societyField.setStyle(fieldStyle);
        emailField.setStyle(fieldStyle);

        // =====================================================
        // GRID
        // =====================================================

        GridPane grid =
                new GridPane();

        grid.setHgap(25);
        grid.setVgap(18);

        grid.add(
                createEditLabel(
                        "Full Name"
                ),
                0,
                0
        );

        grid.add(
                nameField,
                1,
                0
        );

        grid.add(
                createEditLabel(
                        "Phone Number"
                ),
                0,
                1
        );

        grid.add(
                phoneField,
                1,
                1
        );

        grid.add(
                createEditLabel(
                        "Flat / Unit"
                ),
                0,
                2
        );

        grid.add(
                flatField,
                1,
                2
        );

        grid.add(
                createEditLabel(
                        "Society"
                ),
                0,
                3
        );

        grid.add(
                societyField,
                1,
                3
        );

        grid.add(
                createEditLabel(
                        "Email"
                ),
                0,
                4
        );

        grid.add(
                emailField,
                1,
                4
        );

        // =====================================================
        // BUTTONS
        // =====================================================

        HBox buttons =
                new HBox(12);

        buttons.setAlignment(
                Pos.CENTER_RIGHT
        );

        Button cancelButton =
                new Button(
                        "Cancel"
                );

        cancelButton.setPrefWidth(
                130
        );

        cancelButton.setPrefHeight(
                40
        );

        cancelButton.setStyle(
                "-fx-background-color:white;" +
                "-fx-text-fill:#593a32;" +
                "-fx-border-color:#593a32;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

        Button saveButton =
                new Button(
                        "Save Changes"
                );

        saveButton.setPrefWidth(
                150
        );

        saveButton.setPrefHeight(
                40
        );

        saveButton.setStyle(
                "-fx-background-color:#593a32;" +
                "-fx-text-fill:white;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

        // =====================================================
        // CANCEL
        // =====================================================

        cancelButton.setOnAction(e -> {

            stage.setScene(
                    OwnerProfile.createScene(
                            stage,
                            finalOwnerEmail
                    )
            );

            stage.show();
        });

        // =====================================================
        // SAVE CHANGES
        // =====================================================

        saveButton.setOnAction(e -> {

            String newName =
                    nameField.getText() == null
                            ? ""
                            : nameField.getText().trim();

            String newPhone =
                    phoneField.getText() == null
                            ? ""
                            : phoneField.getText().trim();

            String newFlat =
                    flatField.getText() == null
                            ? ""
                            : flatField.getText().trim();

            String newSociety =
                    societyField.getText() == null
                            ? ""
                            : societyField.getText().trim();

            // =================================================
            // EMAIL CHECK
            // =================================================

            if (finalOwnerEmail.isEmpty()) {

                showError(
                        "Profile Error",
                        "Owner email is not available."
                );

                return;
            }

            // =================================================
            // NAME VALIDATION
            // =================================================

            if (newName.isEmpty()) {

                showWarning(
                        "Invalid Name",
                        "Full Name cannot be empty."
                );

                return;
            }

            // =================================================
            // PHONE VALIDATION
            // =================================================

            if (!newPhone.isEmpty() &&
                    !newPhone.matches(
                            "[0-9+()\\- ]{7,15}"
                    )) {

                showWarning(
                        "Invalid Phone Number",
                        "Please enter a valid phone number."
                );

                return;
            }

            // =================================================
            // UPDATE FIRESTORE
            // =================================================

            boolean success =
                    residentController.updateOwnerProfile(
                            finalOwnerEmail,
                            newName,
                            newPhone,
                            newFlat,
                            newSociety
                    );

            // =================================================
            // SUCCESS
            // =================================================

            if (success) {

                showInformation(
                        "Profile Updated",
                        "Your profile has been updated successfully."
                );

                stage.setScene(
                        OwnerProfile.createScene(
                                stage,
                                finalOwnerEmail
                        )
                );

                stage.show();

            } else {

                showError(
                        "Update Failed",
                        "Unable to update your profile."
                );
            }
        });

        buttons.getChildren().addAll(
                cancelButton,
                saveButton
        );

        // =====================================================
        // EDIT CARD
        // =====================================================

        editCard.getChildren().addAll(
                personalTitle,
                separator,
                grid,
                buttons
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        mainContent.getChildren().addAll(
                heading,
                titleBox,
                editCard
        );

        root.setCenter(
                mainContent
        );

        // =====================================================
        // EDIT PAGE ANIMATION
        // =====================================================

        animateEditCard(
                editCard
        );

        animateButton(
                cancelButton
        );

        animateButton(
                saveButton
        );

        // =====================================================
        // MAIN AREA
        // =====================================================

        BorderPane mainarea =
                new BorderPane();

        mainarea.setCenter(
                mainContent
        );

        root.setCenter(
                mainarea
        );

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        root,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        stage.setScene(
                scene
        );

        stage.setMaximized(
                true
        );

        return scene;
    }

    // =========================================================
    // EDIT LABEL
    // =========================================================

    private static Label createEditLabel(
            String text) {

        Label label =
                new Label(
                        text
                );

        label.setPrefWidth(
                140
        );

        label.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        label.setTextFill(
                Color.web("#607D8B")
        );

        return label;
    }

    // =========================================================
    // CLEAN FIELD VALUE
    // =========================================================

    private static String cleanFieldValue(
            String value) {

        if (value == null ||
                value.trim().isEmpty() ||
                value.trim().equals("-")) {

            return "";
        }

        return value.trim();
    }

    // =========================================================
    // CREATE SECTION TITLE
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
                Color.web("#593a32")
        );

        return label;
    }

    // =========================================================
    // CREATE INFORMATION ROW
    // =========================================================

    private static HBox createInfoRow(
            String labelText,
            String valueText) {

        Label label =
                new Label(
                        labelText
                );

        label.setPrefWidth(
                145
        );

        label.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12
                )
        );

        label.setTextFill(
                Color.web("#789098")
        );

        Label value =
                new Label(
                        valueText == null ||
                        valueText.trim().isEmpty()
                                ? "-"
                                : valueText
                );

        value.setFont(
                Font.font(
                        "System",
                        FontWeight.NORMAL,
                        14
                )
        );

        value.setTextFill(
                Color.web("#263238")
        );

        HBox row =
                new HBox(15);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPadding(
                new Insets(
                        4,
                        0,
                        4,
                        0
                )
        );

        row.getChildren().addAll(
                label,
                value
        );

        return row;
    }

    // =========================================================
    // GET INITIALS
    // =========================================================

    private static String getInitials(
            String fullName) {

        if (fullName == null ||
                fullName.trim().isEmpty()) {

            return "ON";
        }

        String[] parts =
                fullName
                        .trim()
                        .split("\\s+");

        if (parts.length == 1) {

            return parts[0]
                    .substring(
                            0,
                            Math.min(
                                    2,
                                    parts[0].length()
                            )
                    )
                    .toUpperCase();
        }

        return (
                parts[0].substring(0, 1) +
                parts[parts.length - 1]
                        .substring(0, 1)
        ).toUpperCase();
    }

    // =========================================================
    // HEADER ANIMATION
    // =========================================================


    

    // =========================================================
    // PROFILE CARD ANIMATION
    // =========================================================

    private static void animateProfileCard(
            VBox profileCard) {

        profileCard.setOpacity(
                0
        );

        profileCard.setTranslateY(
                35
        );

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(700),
                        profileCard
                );

        fade.setFromValue(
                0
        );

        fade.setToValue(
                1
        );

        fade.setDelay(
                Duration.millis(250)
        );

        TranslateTransition slide =
                new TranslateTransition(
                        Duration.millis(700),
                        profileCard
                );

        slide.setFromY(
                35
        );

        slide.setToY(
                0
        );

        slide.setDelay(
                Duration.millis(250)
        );

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(700),
                        profileCard
                );

        scale.setFromX(
                0.96
        );

        scale.setFromY(
                0.96
        );

        scale.setToX(
                1
        );

        scale.setToY(
                1
        );

        scale.setDelay(
                Duration.millis(250)
        );

        fade.play();
        slide.play();
        scale.play();
    }

    // =========================================================
    // INFORMATION CARD ANIMATION
    // =========================================================

    private static void animateInformationCard(
            VBox informationCard) {

        informationCard.setOpacity(
                0
        );

        informationCard.setTranslateY(
                35
        );

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(700),
                        informationCard
                );

        fade.setFromValue(
                0
        );

        fade.setToValue(
                1
        );

        fade.setDelay(
                Duration.millis(400)
        );

        TranslateTransition slide =
                new TranslateTransition(
                        Duration.millis(700),
                        informationCard
                );

        slide.setFromY(
                35
        );

        slide.setToY(
                0
        );

        slide.setDelay(
                Duration.millis(400)
        );

        fade.play();
        slide.play();
    }

    // =========================================================
    // EDIT CARD ANIMATION
    // =========================================================

    private static void animateEditCard(
            VBox editCard) {

        editCard.setOpacity(
                0
        );

        editCard.setTranslateY(
                35
        );

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(700),
                        editCard
                );

        fade.setFromValue(
                0
        );

        fade.setToValue(
                1
        );

        fade.setDelay(
                Duration.millis(250)
        );

        TranslateTransition slide =
                new TranslateTransition(
                        Duration.millis(700),
                        editCard
                );

        slide.setFromY(
                35
        );

        slide.setToY(
                0
        );

        slide.setDelay(
                Duration.millis(250)
        );

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(700),
                        editCard
                );

        scale.setFromX(
                0.97
        );

        scale.setFromY(
                0.97
        );

        scale.setToX(
                1
        );

        scale.setToY(
                1
        );

        scale.setDelay(
                Duration.millis(250)
        );

        fade.play();
        slide.play();
        scale.play();
    }

    // =========================================================
    // BUTTON HOVER ANIMATION
    // =========================================================

    private static void animateButton(
            Button button) {

        button.setOnMouseEntered(e -> {

            if (!button.isDisabled()) {

                ScaleTransition scale =
                        new ScaleTransition(
                                Duration.millis(120),
                                button
                        );

                scale.setToX(
                        1.05
                );

                scale.setToY(
                        1.05
                );

                scale.play();
            }
        });

        button.setOnMouseExited(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            scale.setToX(
                    1.0
            );

            scale.setToY(
                    1.0
            );

            scale.play();
        });
    }

    // =========================================================
    // INFORMATION ALERT
    // =========================================================

    private static void showInformation(
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
    // WARNING ALERT
    // =========================================================

    private static void showWarning(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
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

    private static void showError(
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
}

