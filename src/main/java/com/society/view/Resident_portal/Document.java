
package com.society.view.Resident_portal;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.society.config.FirebaseConfig;
import com.society.controller.ImageUploadController;
import com.society.service.resident_service.ResidentSession;
import com.society.view.ScreenSize;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Document {

    // =========================================================
    // TEMPORARY SELECTED FILES
    // =========================================================

    private File aadhaarFile;
    private File panFile;
    private File ownershipFile;
    private File addressFile;

    // =========================================================
    // DOCUMENT SCENE
    // =========================================================

    public Scene getDocumentScene(Stage stage, String residentEmail) {

        // =====================================================
        // PANEL
        // =====================================================

        panel panelobj = new panel(stage, residentEmail);

        // =====================================================
        // ROOT
        // =====================================================

        BorderPane root = new BorderPane();

        // =====================================================
        // BACKGROUND IMAGE
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
        // SIDEBAR - STATIC LEFT
        // =====================================================

        root.setLeft(
                panelobj.getSidebar()
        );

        // =====================================================
        // MAIN AREA
        // =====================================================

        BorderPane mainArea = new BorderPane();

        mainArea.setStyle(
                "-fx-background-color:transparent;"
        );

        // =====================================================
        // HEADER
        // HEADER IS DIRECTLY ATTACHED TO SIDEBAR
        // =====================================================

        HBox header = new HBox();

        header.setPrefHeight(80);
        header.setMinHeight(80);
        header.setMaxHeight(80);

        header.setPadding(
                new Insets(
                        15,
                        30,
                        15,
                        30
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

        VBox headerText = new VBox(4);

        Label greeting =
                new Label(
                        "Resident Documents"
                );

        greeting.setStyle( "-fx-font-family:'Bodoni MT';" +
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        Label description =
                new Label(
                        "Upload and manage your documents"
                );

        description.setStyle( "-fx-font-family:'Bodoni MT';" +
                "-fx-font-size:12px;" +
                "-fx-text-fill:white;"
        );

        headerText.getChildren().addAll(
                greeting,
                description
        );

        // =====================================================
        // HEADER SPACER
        // =====================================================

        Region headerSpacer = new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );

        // =====================================================
        // DATE
        // =====================================================

        Label day = new Label();
        Label date = new Label();

        LocalDate today =
                LocalDate.now();

        day.setText(
                today.format(
                        DateTimeFormatter.ofPattern(
                                "EEEE"
                        )
                )
        );

        date.setText(
                today.format(
                        DateTimeFormatter.ofPattern(
                                "dd MMMM yyyy"
                        )
                )
        );

        day.setStyle(
                "-fx-text-fill:white;"
        );

        date.setStyle(
                "-fx-text-fill:white;"
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

        header.getChildren().addAll(
                headerText,
                headerSpacer,
                dateBox
        );

        // =====================================================
        // IMPORTANT
        // HEADER DIRECTLY IN MAIN AREA
        // NO LEFT/RIGHT PADDING
        // =====================================================

        mainArea.setTop(
                header
        );

        // =====================================================
        // DOCUMENT CONTENT
        // =====================================================

        VBox mainContent = new VBox(20);

        mainContent.setPadding(
                new Insets(
                        20,
                        40,
                        30,
                        40
                )
        );

        mainContent.setStyle(
                "-fx-background-color:transparent;"
        );

        // =====================================================
        // REQUIRED DOCUMENTS CARD
        // =====================================================

        VBox documentCard =
                new VBox(18);

        documentCard.setPadding(
                new Insets(25)
        );

        documentCard.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:15;" +
                "-fx-border-color:#E5E7EB;" +
                "-fx-border-radius:15;"
        );

        // =====================================================
        // CARD TITLE
        // =====================================================

        Label cardTitle =
                new Label(
                        "Required Documents"
                );

        cardTitle.setStyle(
                "-fx-font-size:19px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#4e342e;"
        );

        Label cardDescription =
                new Label(
                        "Upload documents in PDF format"
                );

        cardDescription.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#4e342e;"
        );

        VBox cardHeading =
                new VBox(4);

        cardHeading.getChildren().addAll(
                cardTitle,
                cardDescription
        );

        // =====================================================
        // DOCUMENT ROWS
        // =====================================================

        HBox aadhaar =
                createDocumentRow(
                        stage,
                        "Aadhaar Card",
                        "Identity Proof",
                        "aadhaar"
                );

        HBox pan =
                createDocumentRow(
                        stage,
                        "PAN Card",
                        "Identity Proof",
                        "pan"
                );

        HBox ownership =
                createDocumentRow(
                        stage,
                        "Ownership Proof",
                        "Property Document",
                        "ownership"
                );

        HBox address =
                createDocumentRow(
                        stage,
                        "Address Proof",
                        "Address Document",
                        "address"
                );

        // =====================================================
        // ADD DOCUMENTS
        // =====================================================

        documentCard.getChildren().addAll(
                cardHeading,
                aadhaar,
                pan,
                ownership,
                address
        );

        // =====================================================
        // ANIMATED CONTENT
        // =====================================================

        VBox animatedContent =
                new VBox(20);

        animatedContent.getChildren().add(
                documentCard
        );

        // =====================================================
        // DOCUMENT CONTENT
        // =====================================================

        mainContent.getChildren().add(
                animatedContent
        );

        // =====================================================
        // SET DOCUMENT CONTENT BELOW HEADER
        // =====================================================

        mainArea.setCenter(
                mainContent
        );

        // =====================================================
        // SET MAIN AREA IN ROOT
        // =====================================================

        root.setCenter(
                mainArea
        );

        // =====================================================
        // FADE ANIMATION
        // ONLY DOCUMENT CARD
        // =====================================================

        FadeTransition documentFade =
                new FadeTransition(
                        Duration.millis(700),
                        animatedContent
                );

        documentFade.setFromValue(0.0);
        documentFade.setToValue(1.0);
        documentFade.setCycleCount(1);
        documentFade.play();

        // =====================================================
        // FETCH EXISTING DOCUMENTS
        // =====================================================

        loadExistingDocuments();

        // =====================================================
        // RETURN SCENE
        // =====================================================

        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }

    // =========================================================
    // BUTTON ANIMATION
    // =========================================================

    private void addButtonAnimation(Button button) {

        // Hover
        button.setOnMouseEntered(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(150),
                            button
                    );

            scale.setToX(1.07);
            scale.setToY(1.07);
            scale.play();
        });

        // Mouse Exit
        button.setOnMouseExited(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(150),
                            button
                    );

            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        // Mouse Press
        button.setOnMousePressed(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(80),
                            button
                    );

            scale.setToX(0.94);
            scale.setToY(0.94);
            scale.play();
        });

        // Mouse Release
        button.setOnMouseReleased(e -> {

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(100),
                            button
                    );

            scale.setToX(1.07);
            scale.setToY(1.07);
            scale.play();
        });
    }

    // =========================================================
    // CREATE DOCUMENT ROW
    // =========================================================

    private HBox createDocumentRow(
            Stage stage,
            String documentName,
            String documentType,
            String documentKey) {

        HBox row =
                new HBox(15);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPadding(
                new Insets(15)
        );

        row.setStyle(
                "-fx-background-color:#F8FAFC;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#E8EBEF;" +
                "-fx-border-radius:10;"
        );

        // =====================================================
        // ICON
        // =====================================================

        Label icon =
                new Label("📄");

        icon.setStyle(
                "-fx-font-size:25px;"
        );

        // =====================================================
        // NAME
        // =====================================================

        Label name =
                new Label(
                        documentName
                );

        name.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#333333;"
        );

        Label type =
                new Label(
                        documentType
                );

        type.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#888888;"
        );

        VBox information =
                new VBox(3);

        information.setPrefWidth(
                220
        );

        information.getChildren().addAll(
                name,
                type
        );

        // =====================================================
        // FILE NAME
        // =====================================================

        Label fileName =
                new Label(
                        "No file selected"
                );

        fileName.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#999999;"
        );

        // =====================================================
        // SPACER
        // =====================================================

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        // =====================================================
        // CHOOSE FILE
        // =====================================================

        Button chooseButton =
                new Button(
                        "Choose File"
                );

        chooseButton.setPrefWidth(
                110
        );

        chooseButton.setPrefHeight(
                34
        );

        chooseButton.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#4e342e;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-text-fill:#4e342e;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

        addButtonAnimation(chooseButton);

        // =====================================================
        // PREVIEW BUTTON
        // =====================================================

        Button previewButton =
                new Button(
                        "Preview"
                );

        previewButton.setPrefWidth(
                75
        );

        previewButton.setPrefHeight(
                34
        );

        previewButton.setStyle(
                "-fx-background-color:white;" +
                "-fx-text-fill:#4e342e;" +
                "-fx-border-color:#4e342e;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

        addButtonAnimation(previewButton);

        // =====================================================
        // SAVE BUTTON
        // =====================================================

        Button saveButton =
                new Button(
                        "Save"
                );

        saveButton.setPrefWidth(
                70
        );

        saveButton.setPrefHeight(
                34
        );

        saveButton.setStyle(
                "-fx-background-color:#4e342e;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        addButtonAnimation(saveButton);

        // =====================================================
        // REMOVE BUTTON
        // =====================================================

        Button removeButton =
                new Button(
                        "Remove"
                );

        removeButton.setPrefWidth(
                80
        );

        removeButton.setPrefHeight(
                34
        );

        removeButton.setStyle(
                "-fx-background-color:#b71c1c;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        addButtonAnimation(removeButton);

        // =====================================================
        // CHOOSE FILE ACTION
        // =====================================================

        chooseButton.setOnAction(e -> {

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Select " + documentName
            );

            fileChooser.getExtensionFilters()
                    .add(
                            new FileChooser.ExtensionFilter(
                                    "PDF Files",
                                    "*.pdf"
                            )
                    );

            File selectedFile =
                    fileChooser.showOpenDialog(
                            stage
                    );

            if (selectedFile == null) {
                return;
            }

            setSelectedFile(
                    documentKey,
                    selectedFile
            );

            fileName.setText(
                    selectedFile.getName()
            );

            fileName.setStyle(
                    "-fx-font-size:12px;" +
                    "-fx-text-fill:#4e3425;" +
                    "-fx-font-weight:bold;"
            );
        });

        // =====================================================
        // PREVIEW ACTION
        // =====================================================

        previewButton.setOnAction(e -> {

            File selectedFile =
                    getSelectedFile(
                            documentKey
                    );

            if (selectedFile == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "No File Selected",
                        "Please select a PDF file first."
                );

                return;
            }

            try {

                if (Desktop.isDesktopSupported()) {

                    Desktop.getDesktop().open(
                            selectedFile
                    );

                } else {

                    showAlert(
                            Alert.AlertType.WARNING,
                            "Preview Not Supported",
                            "Your system does not support PDF preview."
                    );
                }

            } catch (IOException ex) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Preview Error",
                        "Unable to preview the document."
                );
            }
        });

        // =====================================================
        // SAVE ACTION
        // =====================================================

        saveButton.setOnAction(e -> {

            File selectedFile =
                    getSelectedFile(
                            documentKey
                    );

            if (selectedFile == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "No File Selected",
                        "Please select a PDF file first."
                );

                return;
            }

            // =================================================
            // CHECK LOGGED-IN EMAIL
            // =================================================

            String email =
                    ResidentSession.getLoggedInEmail();

            if (email == null ||
                    email.trim().isEmpty()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Login Required",
                        "Logged-in resident email was not found."
                );

                return;
            }

            // =================================================
            // DISABLE SAVE BUTTON
            // =================================================

            saveButton.setDisable(true);

            saveButton.setText(
                    "Uploading..."
            );

            try {

                // =============================================
                // STEP 1: CLOUDINARY UPLOAD
                // =============================================

                String uploadedUrl =
                        uploadDocument(
                                selectedFile
                        );

                if (uploadedUrl == null ||
                        uploadedUrl.trim().isEmpty()) {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Upload Failed",
                            "Document upload failed.\n\n"
                            + "Please check your internet connection "
                            + "and Cloudinary configuration."
                    );

                    return;
                }

                // =============================================
                // STEP 2: FIRESTORE SAVE
                // =============================================

                boolean saved =
                        saveDocumentToFirestore(
                                email,
                                documentKey,
                                documentType,
                                documentName,
                                uploadedUrl
                        );

                if (!saved) {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Firestore Error",
                            "Document was uploaded to Cloudinary "
                            + "but could not be saved in Firestore."
                    );

                    return;
                }

                // =============================================
                // SUCCESS
                // =============================================

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Document Saved",
                        documentName
                        + " uploaded successfully.\n\n"
                        + "Document saved for:\n"
                        + email
                        + "\n\n"
                        + "Cloudinary URL:\n"
                        + uploadedUrl
                );

            } finally {

                saveButton.setDisable(false);

                saveButton.setText(
                        "Save"
                );
            }
        });

        // =====================================================
        // REMOVE ACTION
        // =====================================================

        removeButton.setOnAction(e -> {

            File selectedFile =
                    getSelectedFile(
                            documentKey
                    );

            if (selectedFile == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "No Document",
                        "There is no document to remove."
                );

                return;
            }

            Alert confirmation =
                    new Alert(
                            Alert.AlertType.CONFIRMATION
                    );

            confirmation.setTitle(
                    "Remove Document"
            );

            confirmation.setHeaderText(
                    "Remove " + documentName
            );

            confirmation.setContentText(
                    "Are you sure you want to remove this document?"
            );

            if (confirmation.showAndWait()
                    .orElse(ButtonType.CANCEL)
                    == ButtonType.OK) {

                setSelectedFile(
                        documentKey,
                        null
                );

                fileName.setText(
                        "No file selected"
                );

                fileName.setStyle(
                        "-fx-font-size:12px;" +
                        "-fx-text-fill:#999999;"
                );

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Document Removed",
                        documentName +
                        " removed successfully."
                );
            }
        });

        // =====================================================
        // ADD CONTROLS
        // =====================================================

        row.getChildren().addAll(
                icon,
                information,
                fileName,
                spacer,
                chooseButton,
                previewButton,
                saveButton,
                removeButton
        );

        return row;
    }

    // =========================================================
    // GET SELECTED FILE
    // =========================================================

    private File getSelectedFile(
            String documentKey) {

        switch (documentKey) {

            case "aadhaar":
                return aadhaarFile;

            case "pan":
                return panFile;

            case "ownership":
                return ownershipFile;

            case "address":
                return addressFile;

            default:
                return null;
        }
    }

    // =========================================================
    // SET SELECTED FILE
    // =========================================================

    private void setSelectedFile(
            String documentKey,
            File file) {

        switch (documentKey) {

            case "aadhaar":
                aadhaarFile = file;
                break;

            case "pan":
                panFile = file;
                break;

            case "ownership":
                ownershipFile = file;
                break;

            case "address":
                addressFile = file;
                break;
        }
    }

    // =========================================================
    // CLOUDINARY UPLOAD
    // =========================================================

    private static String uploadDocument(
            File file) {

        if (file == null ||
                !file.exists() ||
                !file.isFile()) {

            System.out.println(
                    "Invalid document file."
            );

            return null;
        }

        try {

            System.out.println(
                    "Uploading document: "
                    + file.getName()
            );

            String url =
                    ImageUploadController.imageUpload(
                            file
                    );

            if (url == null ||
                    url.trim().isEmpty()) {

                System.out.println(
                        "Cloudinary returned an empty URL."
                );

                return null;
            }

            System.out.println(
                    "Document uploaded successfully."
            );

            System.out.println(
                    "Cloudinary URL: "
                    + url
            );

            return url;

        } catch (Exception e) {

            System.err.println(
                    "Document upload failed."
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // SAVE DOCUMENT TO FIRESTORE
    // =========================================================

    private boolean saveDocumentToFirestore(
            String email,
            String documentKey,
            String documentType,
            String documentName,
            String cloudinaryUrl) {

        try {

            Firestore db =
                    FirebaseConfig.getFirestore();

            DocumentReference documentReference =
                    db.collection("documents")
                            .document(email)
                            .collection("documents")
                            .document(documentKey);

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "email",
                    email
            );

            data.put(
                    "documentType",
                    documentType
            );

            data.put(
                    "documentName",
                    documentName
            );

            data.put(
                    "cloudinaryUrl",
                    cloudinaryUrl
            );

            data.put(
                    "uploadedAt",
                    System.currentTimeMillis()
            );

            documentReference.set(
                    data
            ).get();

            System.out.println(
                    "Document saved to Firestore."
            );

            System.out.println(
                    "Resident Email: "
                    + email
            );

            System.out.println(
                    "Document Type: "
                    + documentType
            );

            return true;

        } catch (Exception e) {

            System.err.println(
                    "Firestore document save failed."
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // LOAD EXISTING DOCUMENTS
    // =========================================================

    private void loadExistingDocuments() {

        String email =
                ResidentSession.getLoggedInEmail();

        if (email == null ||
                email.trim().isEmpty()) {

            System.out.println(
                    "No logged-in resident email."
            );

            return;
        }

        try {

            Firestore db =
                    FirebaseConfig.getFirestore();

            String[] documentKeys = {
                    "aadhaar",
                    "pan",
                    "ownership",
                    "address"
            };

            for (String documentKey : documentKeys) {

                DocumentSnapshot snapshot =
                        db.collection("documents")
                                .document(email)
                                .collection("documents")
                                .document(documentKey)
                                .get()
                                .get();

                if (snapshot.exists()) {

                    System.out.println(
                            "Document found: "
                            + documentKey
                    );

                    System.out.println(
                            "Cloudinary URL: "
                            + snapshot.getString(
                                    "cloudinaryUrl"
                            )
                    );

                } else {

                    System.out.println(
                            "No document found: "
                            + documentKey
                    );
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Error fetching resident documents."
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // ALERT
    // =========================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(type);

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

