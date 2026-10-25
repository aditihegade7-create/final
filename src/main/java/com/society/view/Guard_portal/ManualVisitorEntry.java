package com.society.view.Guard_portal;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.society.controller.Guard_Controller.VisitorController;
import com.society.controller.Guard_Controller.GuardController;
import com.society.model.Guard_model.GuardModel;
import com.society.model.Guard_model.VisitorModel;
import com.society.view.ScreenSize;
import com.society.view.Owner_portal.OwnerDashboard;

public class ManualVisitorEntry {

    private static String guardEmail;

    // Existing entry point kept for compatibility with other portal code.
    public static Scene createScene(Stage stage) {
        return createScene(stage, guardEmail);
    }

    public static Scene createScene(Stage stage, String guardEmail) {

        BorderPane root = new BorderPane();

         Image image = new Image(
                    OwnerDashboard.class
                            .getResource(
                                    "/background-Dashboard5.jpeg"
                            )
                            .toExternalForm()
            );

                BackgroundImage bgImage = new BackgroundImage(
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
                                                true));

                root.setBackground(
                                new Background(bgImage));

        // =====================================================
        // GET SOCIETY FROM LOGGED-IN GUARD
        // =====================================================

        String society = null;

        String normalizedGuardEmail =
                guardEmail == null
                        ? ""
                        : guardEmail.trim().toLowerCase();

        if (!normalizedGuardEmail.isEmpty()) {

            try {

                GuardModel guard =
                        new GuardController().getGuard(
                                normalizedGuardEmail
                        );

                if (guard != null) {
                    society = guard.getSociety();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // =====================================================
        // SIDEBAR
        // =====================================================

        GuardSidebar sidebar =
                new GuardSidebar(
                        stage,
                        "Manual Visitor Entry",
                        normalizedGuardEmail,
                        society
                );

        root.setLeft(sidebar.getSidebar());

        VBox content = new VBox();

        content.setPadding(
                new Insets(25, 40, 25, 40)
        );

        content.setSpacing(18);

        content.setFillWidth(true);

      

        // =====================================================
        // HEADER - FIXED
        // =====================================================

        HBox header = new HBox();
        header.setPrefHeight(80);
        header.setMinHeight(80);
        header.setMaxHeight(80);
        header.setPadding(
                new Insets(25, 40, 25, 40)
        );

        header.setStyle(
                "-fx-background-color: #4e342e;"
        );

        // Title + description
        VBox titleBox = new VBox(3);

        Label title =
                new Label("Manual Visitor Entry");

        title.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-family:'Bodoni MT';" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill: white;"
        );

        Label description =
                new Label(
                        "Enter visitor details to approve their entry."
                );

        description.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill: white;"
        );

        titleBox.getChildren().addAll(
                title,
                description
        );

        // Spacer pushes date to the right
        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        // Date
        Label day = new Label();
        Label date = new Label();

        LocalDate today = LocalDate.now();

        day.setText(
                today.format(
                        DateTimeFormatter.ofPattern("EEEE")
                )
        );

        date.setText(
                today.format(
                        DateTimeFormatter.ofPattern("dd MMMM yyyy")
                )
        );

        day.setTextFill(Color.WHITE);
        date.setTextFill(Color.WHITE);

        VBox dateBox = new VBox(3);

        dateBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        dateBox.getChildren().addAll(
                day,
                date
        );

        // Add everything to header
        header.getChildren().addAll(
                titleBox,
                spacer,
                dateBox
        );

        // Header must stay fixed
        header.setOpacity(1);
        header.setTranslateY(0);
        header.setTranslateX(0);

        // =====================================================
        // FORM CARD
        // =====================================================

        VBox formCard =
                new VBox();

        formCard.setPadding(
                new Insets(22, 28, 22, 28)
        );

        formCard.setSpacing(18);

        formCard.setMaxWidth(
                Double.MAX_VALUE
        );

        formCard.setStyle(
                "-fx-background-color: #E8F0E8;" +
                "-fx-background-radius: 12;"
        );

        Label formTitle =
                new Label("Visitor Information");

        formTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #090909;"
        );

        Label requiredNote =
                new Label("* Required fields");

        requiredNote.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #c90909;"
        );

        HBox formHeading =
                new HBox(
                        formTitle,
                        requiredNote
                );

        formHeading.setAlignment(
                Pos.CENTER_LEFT
        );

        formHeading.setSpacing(20);

        GridPane form =
                new GridPane();

        form.setHgap(35);

        form.setVgap(14);

        form.setMaxWidth(
                Double.MAX_VALUE
        );

        ColumnConstraints column1 =
                new ColumnConstraints();

        ColumnConstraints column2 =
                new ColumnConstraints();

        column1.setPercentWidth(50);

        column2.setPercentWidth(50);

        column1.setHgrow(
                Priority.ALWAYS
        );

        column2.setHgrow(
                Priority.ALWAYS
        );

        form.getColumnConstraints().addAll(
                column1,
                column2
        );

        Label nameLabel =
                createLabel("Full Name *");

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Enter visitor's full name"
        );

        styleField(nameField);

        Label phoneLabel =
                createLabel("Phone Number *");

        TextField phoneField =
                new TextField();

        phoneField.setPromptText(
                "Enter mobile number"
        );

        styleField(phoneField);

        Label purposeLabel =
                createLabel("Purpose of Visit *");

        ComboBox<String> purposeBox =
                new ComboBox<>();

        purposeBox.getItems().addAll(
                "Personal Visit",
                "Delivery",
                "Service",
                "Housekeeping",
                "Other"
        );

        purposeBox.setPromptText(
                "Select purpose"
        );

        styleComboBox(purposeBox);

        Label flatLabel =
                createLabel("Visiting Unit / Flat *");

        TextField flatField =
                new TextField();

        flatField.setPromptText(
                "Example: A-102"
        );

        styleField(flatField);

        Label remarksLabel =
                createLabel("Remarks");

        TextArea remarksField =
                new TextArea();

        remarksField.setPromptText(
                "Add any additional information..."
        );

        remarksField.setPrefHeight(70);

        remarksField.setMaxWidth(
                Double.MAX_VALUE
        );

        remarksField.setWrapText(true);

        remarksField.setStyle(
                "-fx-background-color: #F4F7F4;" +
                "-fx-background-radius: 6;" +
                "-fx-text-fill: #090909;" +
                "-fx-prompt-text-fill: #52606D;"
        );

        form.add(
                nameLabel,
                0, 0
        );

        form.add(
                phoneLabel,
                1, 0
        );

        form.add(
                nameField,
                0, 1
        );

        form.add(
                phoneField,
                1, 1
        );

        form.add(
                purposeLabel,
                0, 2
        );

        form.add(
                flatLabel,
                1, 2
        );

        form.add(
                purposeBox,
                0, 3
        );

        form.add(
                flatField,
                1, 3
        );

        form.add(
                remarksLabel,
                0, 4
        );

        form.add(
                remarksField,
                0, 5,
                2, 1
        );

        // =====================================================
        // BUTTONS
        // =====================================================

        Button clearButton =
                new Button("Clear");

        clearButton.setPrefWidth(110);

        clearButton.setPrefHeight(40);

        clearButton.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: #070707;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;"
        );

        Button submitButton =
                new Button("Submit & Approve");

        submitButton.setPrefWidth(165);

        submitButton.setPrefHeight(40);

        submitButton.setStyle(
                "-fx-background-color: #4e342e;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;"
        );

        HBox buttons =
                new HBox(
                        12,
                        clearButton,
                        submitButton
                );

        buttons.setAlignment(
                Pos.CENTER_RIGHT
        );

        // =====================================================
        // BUTTON POPUP ANIMATION
        // =====================================================

        addButtonPopupAnimation(clearButton);
        addButtonPopupAnimation(submitButton);

        // =====================================================
        // CLEAR BUTTON
        // =====================================================

        clearButton.setOnAction(e -> {

            nameField.clear();

            phoneField.clear();

            flatField.clear();

            remarksField.clear();

            purposeBox.setValue(null);
        });

        // =====================================================
        // SUBMIT BUTTON
        // =====================================================

        submitButton.setOnAction(e -> {

            // =====================================================
            // VALIDATION
            // =====================================================

            if (
                    nameField.getText()
                            .trim()
                            .isEmpty()

                            ||

                    phoneField.getText()
                            .trim()
                            .isEmpty()

                            ||

                    purposeBox.getValue() == null

                            ||

                    flatField.getText()
                            .trim()
                            .isEmpty()
            ) {

                showMessage(
                        "Missing Information",
                        "Please fill all required fields."
                );

                return;
            }

            // =====================================================
            // CREATE VISITOR MODEL
            // =====================================================

            VisitorModel visitor =
                    new VisitorModel();

            visitor.setName(
                    nameField.getText().trim()
            );

            visitor.setPhone(
                    phoneField.getText().trim()
            );

            visitor.setFlat(
                    flatField.getText().trim()
            );

            visitor.setPurpose(
                    purposeBox.getValue()
            );

            visitor.setRemarks(
                    remarksField.getText().trim()
            );

            visitor.setEntryTime(
                    LocalTime.now().format(
                            DateTimeFormatter.ofPattern(
                                    "hh:mm a"
                            )
                    )
            );

            visitor.setExitTime(null);

            visitor.setQrPass(null);

            visitor.setStatus("Inside");

            if (normalizedGuardEmail.isEmpty()) {

                showMessage(
                        "Guard Email Missing",
                        "Logged-in guard email is not available."
                );

                return;
            }

            visitor.setEmail(
                    normalizedGuardEmail
            );

            visitor.setGuardEmail(
                    normalizedGuardEmail
            );

            // Preserve guardId and get the society from the
            // authenticated guard record.
            String societyName = null;

            try {

                GuardModel guard =
                        new GuardController().getGuard(
                                normalizedGuardEmail
                        );

                if (guard != null) {

                    visitor.setGuardId(
                            guard.getGuardId()
                    );

                    societyName =
                            guard.getSociety();
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }

            // =====================================================
            // VALIDATE SOCIETY
            // =====================================================

            if (societyName == null ||
                    societyName.trim().isEmpty()) {

                showMessage(
                        "Society Not Found",
                        "Society information could not be found for this guard."
                );

                return;
            }

            visitor.setSociety(
                    societyName.trim()
            );

            // =====================================================
            // SAVE TO FIRESTORE
            // =====================================================

            VisitorController visitorController =
                    new VisitorController();

            boolean saved =
                    visitorController.addVisitor(
                            visitor,
                            societyName
                    );

            // =====================================================
            // RESULT
            // =====================================================

            if (saved) {

                showMessage(
                        "Visitor Approved",
                        nameField.getText()
                                .trim()
                                + " has been added successfully."
                );

                clearButton.fire();

                stage.setScene(
                        VisitorLog.createScene(
                                stage,
                                normalizedGuardEmail,
                                societyName
                        )
                );

            } else {

                showMessage(
                        "Error",
                        "Visitor could not be saved to Firestore."
                );
            }
        });

        formCard.getChildren().addAll(
                formHeading,
                form,
                buttons
        );

        content.getChildren().add(
                formCard
        );

        // =====================================================
        // FORM CARD POPUP
        // =====================================================

        addCardPopupAnimation(formCard);

        // =====================================================
        // MAIN AREA
        // =====================================================

        BorderPane mainarea =
                new BorderPane();

        // Header fixed at TOP
        mainarea.setTop(
                header
        );

        // Page content below header
        mainarea.setCenter(
                content
        );

        root.setCenter(
                mainarea
        );

        // =====================================================
        // DOWN SIDE -> UP PAGE ANIMATION
        // =====================================================

        playPageAnimation(content);

        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }

    // =========================================================
    // PAGE DOWN -> UP ANIMATION
    // =========================================================

    private static void playPageAnimation(
            VBox content) {

        content.setOpacity(0);

        content.setTranslateY(45);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(700),
                        content
                );

        fade.setFromValue(0);

        fade.setToValue(1);

        TranslateTransition slide =
                new TranslateTransition(
                        Duration.millis(700),
                        content
                );

        slide.setFromY(45);

        slide.setToY(0);

        fade.play();

        slide.play();
    }

    // =========================================================
    // BUTTON POPUP / HOVER ANIMATION
    // =========================================================

    private static void addButtonPopupAnimation(
            Button button) {

        button.setOnMouseEntered(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            scale.setToX(1.06);

            scale.setToY(1.06);

            scale.play();
        });

        button.setOnMouseExited(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            scale.setToX(1);

            scale.setToY(1);

            scale.play();
        });
    }

    // =========================================================
    // CARD POPUP ANIMATION
    // =========================================================

    private static void addCardPopupAnimation(
            VBox card) {

        card.setOpacity(0);

        card.setScaleX(0.88);

        card.setScaleY(0.88);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(550),
                        card
                );

        fade.setFromValue(0);

        fade.setToValue(1);

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(550),
                        card
                );

        scale.setFromX(0.88);

        scale.setFromY(0.88);

        scale.setToX(1);

        scale.setToY(1);

        fade.play();

        scale.play();
    }

    // =========================================================
    // LABEL
    // =========================================================

    private static Label createLabel(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #080808;"
        );

        return label;
    }

    // =========================================================
    // TEXT FIELD STYLE
    // =========================================================

    private static void styleField(
            TextField field) {

        field.setMaxWidth(
                Double.MAX_VALUE
        );

        field.setPrefHeight(40);

        field.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 6;" +
                "-fx-text-fill: #060606;" +
                "-fx-prompt-text-fill: #52606D;"
        );
    }

    // =========================================================
    // COMBO BOX STYLE
    // =========================================================

    private static void styleComboBox(
            ComboBox<String> box) {

        box.setMaxWidth(
                Double.MAX_VALUE
        );

        box.setPrefHeight(60);

        box.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 6;" +
                "-fx-text-fill: #080808;"
        );
    }

    // =========================================================
    // ALERT
    // =========================================================

    private static void showMessage(
            String title,
            String message) {

        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}