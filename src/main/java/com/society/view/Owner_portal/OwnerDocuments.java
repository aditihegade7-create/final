package com.society.view.Owner_portal;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.society.controller.ImageUploadController;
import com.society.controller.Owner_Controller.OwnerDocumentController;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * =========================================================
 * OWNER DOCUMENTS
 * =========================================================
 *
 * Handles:
 *
 * 1. Select PDF documents
 * 2. Upload PDFs to Cloudinary
 * 3. Save Cloudinary URLs in Firestore
 * 4. Load previously uploaded documents
 *
 * Firestore:
 *
 * OwnerDocuments
 *      └── ownerEmail
 *             ├── email
 *             ├── aadhaarUrl
 *             ├── panUrl
 *             ├── ownershipProofUrl
 *             └── addressProofUrl
 *
 */
public class OwnerDocuments {


    // =========================================================
    // CONTROLLERS
    // =========================================================

    private static final OwnerDocumentController
            documentController =
                    new OwnerDocumentController();


    private static final ImageUploadController
            imageUploadController =
                    new ImageUploadController();


    // =========================================================
    // SELECTED FILES
    // =========================================================

    private static File aadhaarFile;

    private static File panFile;

    private static File ownershipFile;

    private static File addressFile;


    // =========================================================
    // CREATE SCENE
    // =========================================================

    public static Scene createScene(
            Stage stage,
            String ownerEmail) {


        final String finalOwnerEmail =
                ownerEmail == null
                        ? ""
                        : ownerEmail
                                .trim()
                                .toLowerCase();


        // =====================================================
        // RESET SELECTED FILES
        // =====================================================

        aadhaarFile = null;
        panFile = null;
        ownershipFile = null;
        addressFile = null;


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
        // MAIN CONTENT
        // =====================================================

        VBox mainContent =
                new VBox(20);

        mainContent.setPadding(
                new Insets(
                        0,
                        40,
                        30,
                        40
                )
        );

        mainContent.setStyle(
                "-fx-background-color:#e8ddd5;"
        );


        // =====================================================
        // HEADER
        // =====================================================

        HBox header =
                new HBox();

        header.setPrefHeight(
                100
        );

        header.setPadding(
                new Insets(20)
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setStyle(
                "-fx-background-color:#4e342e;"
        );


        VBox headerText =
                new VBox(4);


        Label greeting =
                new Label(
                        "Owner Documents"
                );

        greeting.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );


        Label description =
                new Label(
                        "Upload and manage your documents"
                );

        description.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:white;"
        );


        headerText.getChildren().addAll(
                greeting,
                description
        );


        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );


        Label day =
                new Label();

        Label date =
                new Label();


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
        // DOCUMENT CARD
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


        Label cardTitle =
                new Label(
                        "Required Documents"
                );

        cardTitle.setStyle(
                "-fx-font-size:19px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#333333;"
        );


        Label cardDescription =
                new Label(
                        "Upload documents in PDF format"
                );

        cardDescription.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#888888;"
        );


        VBox cardHeading =
                new VBox(4);

        cardHeading.getChildren().addAll(
                cardTitle,
                cardDescription
        );


        // =====================================================
        // EXISTING DOCUMENTS
        // =====================================================

        Map<String, String> existingDocuments =
                documentController
                        .getOwnerDocuments(
                                finalOwnerEmail
                        );


        // =====================================================
        // DOCUMENT ROWS
        // =====================================================

        HBox aadhaar =
                createDocumentRow(
                        stage,
                        "Aadhaar Card",
                        "Identity Proof",
                        existingDocuments.get(
                                "aadhaarUrl"
                        ),
                        "aadhaar"
                );


        HBox pan =
                createDocumentRow(
                        stage,
                        "PAN Card",
                        "Identity Proof",
                        existingDocuments.get(
                                "panUrl"
                        ),
                        "pan"
                );


        HBox ownership =
                createDocumentRow(
                        stage,
                        "Ownership Proof",
                        "Property Document",
                        existingDocuments.get(
                                "ownershipProofUrl"
                        ),
                        "ownership"
                );


        HBox address =
                createDocumentRow(
                        stage,
                        "Address Proof",
                        "Address Document",
                        existingDocuments.get(
                                "addressProofUrl"
                        ),
                        "address"
                );


        documentCard.getChildren().addAll(
                cardHeading,
                aadhaar,
                pan,
                ownership,
                address
        );


        // =====================================================
        // SAVE BUTTON
        // =====================================================

        HBox buttonBox =
                new HBox();

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );


        Button saveButton =
                new Button(
                        "Save Documents"
                );

        saveButton.setPrefWidth(
                170
        );

        saveButton.setPrefHeight(
                42
        );

        saveButton.setStyle(
                "-fx-background-color:#4e342e;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );


        // =====================================================
        // SAVE ACTION
        // =====================================================

        saveButton.setOnAction(e -> {

            if (finalOwnerEmail.isEmpty()) {

                showError(
                        "Owner Error",
                        "Owner email is not available."
                );

                return;
            }


            // =================================================
            // CHECK WHETHER ANY FILE WAS SELECTED
            // =================================================

            if (aadhaarFile == null &&
                    panFile == null &&
                    ownershipFile == null &&
                    addressFile == null) {

                showWarning(
                        "No Documents Selected",
                        "Please select at least one PDF document."
                );

                return;
            }


            // =================================================
            // UPLOAD FILES
            // =================================================

            String aadhaarUrl =
                    existingDocuments.get(
                            "aadhaarUrl"
                    );

            String panUrl =
                    existingDocuments.get(
                            "panUrl"
                    );

            String ownershipUrl =
                    existingDocuments.get(
                            "ownershipProofUrl"
                    );

            String addressUrl =
                    existingDocuments.get(
                            "addressProofUrl"
                    );


            // =================================================
            // AADHAAR
            // =================================================

            if (aadhaarFile != null) {

                aadhaarUrl =
                        uploadDocument(
                                aadhaarFile
                        );

                if (aadhaarUrl == null) {

                    showError(
                            "Upload Failed",
                            "Aadhaar Card could not be uploaded."
                    );

                    return;
                }
            }


            // =================================================
            // PAN
            // =================================================

            if (panFile != null) {

                panUrl =
                        uploadDocument(
                                panFile
                        );

                if (panUrl == null) {

                    showError(
                            "Upload Failed",
                            "PAN Card could not be uploaded."
                    );

                    return;
                }
            }


            // =================================================
            // OWNERSHIP
            // =================================================

            if (ownershipFile != null) {

                ownershipUrl =
                        uploadDocument(
                                ownershipFile
                        );

                if (ownershipUrl == null) {

                    showError(
                            "Upload Failed",
                            "Ownership Proof could not be uploaded."
                    );

                    return;
                }
            }


            // =================================================
            // ADDRESS
            // =================================================

            if (addressFile != null) {

                addressUrl =
                        uploadDocument(
                                addressFile
                        );

                if (addressUrl == null) {

                    showError(
                            "Upload Failed",
                            "Address Proof could not be uploaded."
                    );

                    return;
                }
            }


            // =================================================
            // SAVE URLS TO FIRESTORE
            // =================================================

            boolean saved =
                    documentController
                            .saveOwnerDocuments(
                                    finalOwnerEmail,
                                    aadhaarUrl,
                                    panUrl,
                                    ownershipUrl,
                                    addressUrl
                            );


            if (!saved) {

                showError(
                        "Save Failed",
                        "Documents were uploaded but could not be saved to Firestore."
                );

                return;
            }


            // =================================================
            // SUCCESS
            // =================================================

            showInformation(
                    "Documents Saved",
                    "Your documents have been saved successfully."
            );


            // =================================================
            // RELOAD PAGE
            // =================================================

            OwnerDocuments.createScene(
        stage,
        ownerEmail
);

            stage.show();
        });


        buttonBox.getChildren().add(
                saveButton
        );


        // =====================================================
        // MAIN CONTENT
        // =====================================================

        mainContent.getChildren().addAll(
                header,
                documentCard,
                buttonBox
        );


        root.setCenter(
                mainContent
        );


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
    // DOCUMENT ROW
    // =========================================================

    private static HBox createDocumentRow(
            Stage stage,
            String documentName,
            String documentType,
            String existingUrl,
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
        // DOCUMENT NAME
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
        // FILE NAME / STATUS
        // =====================================================

        String initialText;


        if (existingUrl != null &&
                !existingUrl.trim().isEmpty()) {

            initialText =
                    "Document uploaded";

        } else {

            initialText =
                    "No file selected";
        }


        Label fileName =
                new Label(
                        initialText
                );


        if (existingUrl != null &&
                !existingUrl.trim().isEmpty()) {

            fileName.setStyle(
                    "-fx-font-size:12px;" +
                    "-fx-text-fill:#2E7D32;" +
                    "-fx-font-weight:bold;"
            );

        } else {

            fileName.setStyle(
                    "-fx-font-size:12px;" +
                    "-fx-text-fill:#999999;"
            );
        }


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
        // CHOOSE BUTTON
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


        // =====================================================
        // CHOOSE FILE ACTION
        // =====================================================

        chooseButton.setOnAction(e -> {

            FileChooser fileChooser =
                    new FileChooser();


            fileChooser.setTitle(
                    "Select " + documentName
            );


            fileChooser
                    .getExtensionFilters()
                    .add(
                            new FileChooser.ExtensionFilter(
                                    "PDF Files",
                                    "*.pdf"
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
            // STORE SELECTED FILE
            // =================================================

            switch (documentKey) {

                case "aadhaar":

                    aadhaarFile =
                            file;

                    break;


                case "pan":

                    panFile =
                            file;

                    break;


                case "ownership":

                    ownershipFile =
                            file;

                    break;


                case "address":

                    addressFile =
                            file;

                    break;
            }


            // =================================================
            // UPDATE UI
            // =================================================

            fileName.setText(
                    file.getName()
            );

            fileName.setStyle(
                    "-fx-font-size:12px;" +
                    "-fx-text-fill:#4e3425;" +
                    "-fx-font-weight:bold;"
            );
        });


        row.getChildren().addAll(
                icon,
                information,
                fileName,
                spacer,
                chooseButton
        );


        return row;
    }


    // =========================================================
    // CLOUDINARY UPLOAD
    // =========================================================

    private static String uploadDocument(
            File file) {

        try {

            System.out.println(
                    "Uploading document: "
                            + file.getName()
            );


            String url =
                    imageUploadController
                            .imageUpload(
                                    file
                            );


            if (url == null ||
                    url.trim().isEmpty()) {

                return null;
            }


            System.out.println(
                    "Document uploaded successfully."
            );


            return url;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
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