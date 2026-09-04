package com.society.view.Resident_portal;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.society.controller.ImageUploadController;
import com.society.controller.Resident_Controller.ProfileController;
import com.society.model.Resident_model.ProfileModel;
import com.society.view.ScreenSize;
import com.society.view.Welcome.LogInPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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

public class ProfilePage {

    // =========================================================
    // LOGGED-IN EMAIL
    // =========================================================
        
    private static String loggedInEmail = "";


    // =========================================================
    // SET LOGGED-IN EMAIL
    // =========================================================

    public static void setLoggedInEmail(
            String email) {

        if (email == null ||
                email.trim().isEmpty()) {

            loggedInEmail = "";

            return;
        }


        loggedInEmail =
                email.trim()
                        .toLowerCase();
    }


    // =========================================================
    // CREATE PROFILE SCENE
    // =========================================================

    public Scene getProfileScene(
        Stage stage, String residentEmail) {

    setLoggedInEmail(residentEmail);

    // =====================================================
    // CONTROLLER
    // =====================================================

    ProfileController controller =
            new ProfileController();


        // =====================================================
        // LOAD PROFILE
        // =====================================================

        ProfileModel profile =
                controller.getProfile(
                        loggedInEmail
                );


        // =====================================================
        // DEFAULT VALUES
        // =====================================================

        String nameText =
                "Resident";

        String emailText =
                loggedInEmail;

        String phoneText =
                "";

        String flatText =
                "";

        String wingText =
                "";

        String societyText =
                "";

        String residentTypeText =
                "Resident";

        String statusText =
                "Active";

        String profileImageUrl =
                "";


        // =====================================================
        // GET FIRESTORE VALUES
        // =====================================================

        if (profile != null) {

            if (profile.getName() != null &&
                    !profile.getName()
                            .trim()
                            .isEmpty()) {

                nameText =
                        profile.getName();
            }


            if (profile.getEmail() != null &&
                    !profile.getEmail()
                            .trim()
                            .isEmpty()) {

                emailText =
                        profile.getEmail();
            }


            if (profile.getPhone() != null) {

                phoneText =
                        profile.getPhone();
            }


            if (profile.getFlat() != null) {

                flatText =
                        profile.getFlat();
            }


            if (profile.getWing() != null) {

                wingText =
                        profile.getWing();
            }


            if (profile.getSocietyName() != null) {

                societyText =
                        profile.getSocietyName();
            }


            if (profile.getResidentType() != null &&
                    !profile.getResidentType()
                            .trim()
                            .isEmpty()) {

                residentTypeText =
                        profile.getResidentType();
            }


            if (profile.getStatus() != null &&
                    !profile.getStatus()
                            .trim()
                            .isEmpty()) {

                statusText =
                        profile.getStatus();
            }


            if (profile.getProfileImageUrl() != null) {

                profileImageUrl =
                        profile.getProfileImageUrl();
            }
        }


        // =====================================================
        // SIDEBAR
        // =====================================================

        panel panelobj =
                new panel(stage, residentEmail);


        // =====================================================
        // ROOT
        // =====================================================

        BorderPane root =
                new BorderPane();

        root.setLeft(
                panelobj.getSidebar()
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
                new VBox(2);

        dateBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        dateBox.getChildren().addAll(
                day,
                date
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
        // PHOTO
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


        loadProfileImage(
                photoContainer,
                profileImageUrl,
                nameText
        );


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
                "-fx-text-fill:#593a32;" +
                "-fx-font-weight:bold;" +
                "-fx-border-color:#593a32;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );


        // =====================================================
        // NAME
        // =====================================================

        Label nameLabel =
                new Label(
                        nameText
                );

        nameLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        21
                )
        );

        nameLabel.setTextFill(
                Color.web("#3E2723")
        );


        Label roleLabel =
                new Label(
                        residentTypeText
                );

        roleLabel.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        roleLabel.setTextFill(
                Color.web("#789098")
        );


        String flatDisplay =
                flatText;


        if (!wingText.isEmpty()) {

            flatDisplay +=
                    " • " + wingText;
        }


        Label flatLabel =
                new Label(
                        flatDisplay
                );

        flatLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        flatLabel.setTextFill(
                Color.web("#593a32")
        );


        Label statusLabel =
                new Label(
                        statusText
                );

        statusLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        statusLabel.setTextFill(
                Color.web("#315B45")
        );


        VBox identity =
                new VBox(5);

        identity.setAlignment(
                Pos.CENTER
        );

        identity.getChildren().addAll(
                nameLabel,
                roleLabel,
                flatLabel,
                statusLabel
        );


        Separator profileSeparator =
                new Separator();


        // =====================================================
        // EDIT BUTTON
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
                "-fx-background-color:#593a32;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );


        // =====================================================
        // SAVE BUTTON
        // =====================================================

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
                "-fx-background-color:#593a32;" +
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
                "-fx-text-fill:#593a32;" +
                "-fx-font-weight:bold;" +
                "-fx-border-color:#593a32;" +
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


        TextField nameField =
                createField(
                        nameText
                );


        TextField emailField =
                createField(
                        emailText
                );


        TextField phoneField =
                createField(
                        phoneText
                );


        Label flatValue =
                createValueLabel(
                        flatText
                );


        Label wingValue =
                createValueLabel(
                        wingText
                );


        HBox nameRow =
                createEditableInfoRow(
                        "Full Name",
                        nameField
                );


        HBox emailRow =
                createEditableInfoRow(
                        "Email",
                        emailField
                );


        HBox phoneRow =
                createEditableInfoRow(
                        "Phone Number",
                        phoneField
                );


        HBox flatRow =
                createInfoRow(
                        "Flat Number",
                        flatValue
                );


        HBox wingRow =
                createInfoRow(
                        "Wing",
                        wingValue
                );


        VBox personalSection =
                new VBox(7);

        personalSection.getChildren().addAll(
                personalTitle,
                personalSeparator,
                nameRow,
                emailRow,
                phoneRow,
                flatRow,
                wingRow
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


        Label accountStatusValue =
                createValueLabel(
                        statusText
                );


        Label accountTypeValue =
                createValueLabel(
                        residentTypeText
                );


        Label societyValue =
                createValueLabel(
                        societyText
                );


        HBox accountStatusRow =
                createInfoRow(
                        "Account Status",
                        accountStatusValue
                );


        HBox accountTypeRow =
                createInfoRow(
                        "Account Type",
                        accountTypeValue
                );


        HBox societyRow =
                createInfoRow(
                        "Society",
                        societyValue
                );


        VBox accountSection =
                new VBox(7);

        accountSection.getChildren().addAll(
                accountTitle,
                accountSeparator,
                accountStatusRow,
                accountTypeRow,
                societyRow
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
                "-fx-text-fill:#593a32;" +
                "-fx-border-color:#593a32;" +
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
                accountSection,
                securitySection
        );


        // =====================================================
        // EDITABLE FIELDS
        //
        // Email remains read-only.
        // Flat and Wing are read-only.
        // =====================================================

        nameField.setEditable(
                false
        );

        phoneField.setEditable(
                false
        );

        emailField.setEditable(
                false
        );


        // =====================================================
        // EDIT PROFILE
        // =====================================================

        editButton.setOnAction(e -> {

            nameField.setEditable(
                    true
            );

            phoneField.setEditable(
                    true
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
        });


        // =====================================================
        // SAVE PROFILE
        // =====================================================

        saveButton.setOnAction(e -> {

            String updatedName =
                    nameField.getText()
                            .trim();


            String updatedPhone =
                    phoneField.getText()
                            .trim();


            if (updatedName.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Profile",
                        "Name cannot be empty."
                );

                return;
            }


            if (updatedPhone.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Profile",
                        "Phone number cannot be empty."
                );

                return;
            }


            boolean updated =
                    controller.updateProfile(
                            loggedInEmail,
                            updatedName,
                            updatedPhone
                    );


            if (updated) {

                nameLabel.setText(
                        updatedName
                );


                nameField.setEditable(
                        false
                );

                phoneField.setEditable(
                        false
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


                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Profile",
                        "Profile updated successfully."
                );


            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Profile",
                        "Unable to update profile."
                );
            }
        });


        // =====================================================
        // CHANGE PHOTO
        // =====================================================

        changePhotoButton.setOnAction(e -> {

            FileChooser fileChooser =
                    new FileChooser();


            fileChooser.setTitle(
                    "Select Profile Photo"
            );


            fileChooser
                    .getExtensionFilters()
                    .add(
                            new FileChooser.ExtensionFilter(
                                    "Image Files",
                                    "*.png",
                                    "*.jpg",
                                    "*.jpeg",
                                    "*.webp"
                            )
                    );


            File file =
                    fileChooser.showOpenDialog(
                            stage
                    );


            if (file == null) {
                return;
            }


            try {

                ImageUploadController uploader =
                        new ImageUploadController();


                String imageUrl =
                        uploader.imageUpload(
                                file
                        );


                if (imageUrl == null ||
                        imageUrl.trim().isEmpty()) {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Profile Photo",
                            "Photo upload failed."
                    );

                    return;
                }


                boolean saved =
                        controller.updateProfileImage(
                                loggedInEmail,
                                imageUrl
                        );


                if (!saved) {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Profile Photo",
                            "Photo could not be saved."
                    );

                    return;
                }


                loadProfileImage(
                        photoContainer,
                        imageUrl,
                        updatedName(
                                nameField.getText()
                        )
                );


                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Profile Photo",
                        "Profile photo updated successfully."
                );


            } catch (Exception ex) {

                ex.printStackTrace();


                showAlert(
                        Alert.AlertType.ERROR,
                        "Profile Photo",
                        "Error updating profile photo."
                );
            }
        });


        // =====================================================
        // CHANGE PASSWORD
        // =====================================================

        changePasswordButton.setOnAction(e -> {

            showChangePasswordDialog(
                    stage,
                    controller
            );
        });


        // =====================================================
        // LOGOUT
        // =====================================================

        logoutButton.setOnAction(e -> {

            loggedInEmail =
                    "";


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


        // =====================================================
        // MAIN AREA
        // =====================================================

        mainArea.getChildren().addAll(
                header,
                contentArea
        );


        // =====================================================
        // ROOT
        // =====================================================

        root.setCenter(
                mainArea
        );


        // =====================================================
        // SCENE
        // =====================================================

        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }


    // =========================================================
    // LOAD PROFILE IMAGE
    // =========================================================

    private void loadProfileImage(
            StackPane container,
            String imageUrl,
            String name) {

        container
                .getChildren()
                .clear();


        Image image =
                null;


        // =====================================================
        // CLOUDINARY IMAGE
        // =====================================================

        if (imageUrl != null &&
                !imageUrl.trim().isEmpty()) {

            try {

                image =
                        new Image(
                                imageUrl,
                                115,
                                115,
                                false,
                                true,
                                true
                        );

            } catch (Exception e) {

                image = null;
            }
        }


        if (image != null &&
                !image.isError()) {

            ImageView imageView =
                    new ImageView(
                            image
                    );


            imageView.setFitWidth(
                    115
            );


            imageView.setFitHeight(
                    115
            );


            imageView.setPreserveRatio(
                    false
            );


            imageView.setSmooth(
                    true
            );


            Circle clip =
                    new Circle(
                            57.5,
                            57.5,
                            57.5
                    );


            imageView.setClip(
                    clip
            );


            container
                    .getChildren()
                    .add(
                            imageView
                    );


            return;
        }


        // =====================================================
        // FALLBACK INITIALS
        // =====================================================

        Circle circle =
                new Circle(
                        57.5,
                        Color.web("#789098")
                );


        String initials =
                getInitials(
                        name
                );


        Label initialsLabel =
                new Label(
                        initials
                );


        initialsLabel.setStyle(
                "-fx-text-fill:white;" +
                "-fx-font-size:34px;" +
                "-fx-font-weight:bold;"
        );


        container
                .getChildren()
                .addAll(
                        circle,
                        initialsLabel
                );
    }


    // =========================================================
    // GET INITIALS
    // =========================================================

    private String getInitials(
            String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            return "R";
        }


        String[] parts =
                name.trim()
                        .split("\\s+");


        if (parts.length == 1) {

            return parts[0]
                    .substring(
                            0,
                            1
                    )
                    .toUpperCase();
        }


        return (
                parts[0].substring(0, 1)
                        +
                parts[parts.length - 1]
                        .substring(0, 1)
        ).toUpperCase();
    }


    // =========================================================
    // UPDATED NAME
    // =========================================================

    private String updatedName(
            String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            return "Resident";
        }

        return name.trim();
    }


    // =========================================================
    // SECTION TITLE
    // =========================================================

    private Label createSectionTitle(
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
                Color.web("#3E2723")
        );


        return label;
    }


    // =========================================================
    // READ-ONLY VALUE
    // =========================================================

    private Label createValueLabel(
            String text) {

        Label label =
                new Label(
                        text == null
                                ? ""
                                : text
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

    private TextField createField(
            String text) {

        TextField field =
                new TextField(
                        text == null
                                ? ""
                                : text
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
    // READ-ONLY ROW
    // =========================================================

    private HBox createInfoRow(
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
                Color.web("#263238")
        );


        HBox row =
                new HBox(15);


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

    private HBox createEditableInfoRow(
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
                Color.web("#263238")
        );


        HBox row =
                new HBox(15);


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
    // CHANGE PASSWORD DIALOG
    // =========================================================

    private void showChangePasswordDialog(
            Stage owner,
            ProfileController controller) {

        Stage dialog =
                new Stage();


        dialog.setTitle(
                "Change Password"
        );


        dialog.initOwner(
                owner
        );


        VBox box =
                new VBox(12);


        box.setPadding(
                new Insets(25)
        );


        box.setPrefWidth(
                380
        );


        box.setStyle(
                "-fx-background-color:white;"
        );


        Label title =
                new Label(
                        "Change Password"
                );


        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        20
                )
        );


        title.setTextFill(
                Color.web("#3E2723")
        );


        PasswordField currentPassword =
                new PasswordField();


        currentPassword.setPromptText(
                "Current Password"
        );


        currentPassword.setPrefHeight(
                38
        );


        PasswordField newPassword =
                new PasswordField();


        newPassword.setPromptText(
                "New Password"
        );


        newPassword.setPrefHeight(
                38
        );


        PasswordField confirmPassword =
                new PasswordField();


        confirmPassword.setPromptText(
                "Confirm Password"
        );


        confirmPassword.setPrefHeight(
                38
        );


        Button cancelButton =
                new Button(
                        "Cancel"
                );


        cancelButton.setPrefWidth(
                110
        );


        cancelButton.setPrefHeight(
                38
        );


        Button changeButton =
                new Button(
                        "Change Password"
                );


        changeButton.setPrefWidth(
                145
        );


        changeButton.setPrefHeight(
                38
        );


        changeButton.setStyle(
                "-fx-background-color:#593a32;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;"
        );


        HBox buttons =
                new HBox(10);


        buttons.setAlignment(
                Pos.CENTER_RIGHT
        );


        buttons.getChildren().addAll(
                cancelButton,
                changeButton
        );


        box.getChildren().addAll(
                title,
                currentPassword,
                newPassword,
                confirmPassword,
                buttons
        );


        cancelButton.setOnAction(e ->
                dialog.close()
        );


        changeButton.setOnAction(e -> {

            String current =
                    currentPassword
                            .getText();


            String newPass =
                    newPassword
                            .getText();


            String confirm =
                    confirmPassword
                            .getText();


            if (current.isEmpty() ||
                    newPass.isEmpty() ||
                    confirm.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Change Password",
                        "Please fill all fields."
                );

                return;
            }


            if (!newPass.equals(confirm)) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Change Password",
                        "New passwords do not match."
                );

                return;
            }


            if (newPass.length() < 6) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Change Password",
                        "Password must contain at least 6 characters."
                );

                return;
            }


            boolean changed =
                    controller.changePassword(
                            loggedInEmail,
                            current,
                            newPass
                    );


            if (changed) {

                dialog.close();


                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Change Password",
                        "Password changed successfully."
                );


            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Change Password",
                        "Current password is incorrect or password could not be changed."
                );
            }
        });


        Scene scene =
                new Scene(
                        box
                );


        dialog.setScene(
                scene
        );


        dialog.showAndWait();
    }


    // =========================================================
    // ALERT
    // =========================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(
                        type
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

    public static String getLoggedInEmail() {
    return loggedInEmail;
}
}