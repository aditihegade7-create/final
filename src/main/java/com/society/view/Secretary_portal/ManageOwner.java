package com.society.view.Secretary_portal;

import java.util.ArrayList;
import java.util.List;

import com.society.controller.Secretary_Controller.OwnerController;
import com.society.model.Secretary_model.Owner;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ManageOwner {

    // =====================================================
    // SCENE
    // =====================================================

    private Scene ownerScene;

    // =====================================================
    // ROOT
    // =====================================================

    private StackPane rootStack;

    // =====================================================
    // CONTROLLER
    // =====================================================

    private OwnerController ownerController;

    // =====================================================
    // OWNER LIST
    // =====================================================

    private VBox ownerList;

    // =====================================================
    // SEARCH
    // =====================================================

    private TextField search;

    // =====================================================
    // OWNER CACHE
    // =====================================================

    private List<Owner> allOwners =
            new ArrayList<>();

    // =====================================================
    // CREATE SCENE
    // =====================================================

    public Scene createScene(Stage stage) {

        // =====================================================
        // CONTROLLER
        // =====================================================

        ownerController =
                new OwnerController();

        // =====================================================
        // SIDEBAR
        // =====================================================

        SecretarySidebar sidebarObj =
                new SecretarySidebar();

        VBox sidebar =
                sidebarObj.createSidebar(stage);

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainvb =
                new VBox(10);

        mainvb.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        mainvb.setPadding(
                new Insets(20)
        );

        mainvb.setSpacing(10);

        mainvb.setStyle(
                "-fx-background-color:#b3adad;"
        );

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label("Manage Owners");

        title.setStyle(
                "-fx-font-size:28px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:black;"
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        Label subtitle =
                new Label(
                        "View and manage owners of your society"
                );

        subtitle.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#777777;"
        );

        // =====================================================
        // SEARCH
        // =====================================================

        search =
                new TextField();

        search.setPromptText(
                "Search owner, flat, mobile, email, society..."
        );

        search.setPrefHeight(45);

        search.setPrefWidth(600);

        search.setMaxWidth(
                Double.MAX_VALUE
        );

        search.setStyle(
                "-fx-background-color:#F8F9FA;" +
                "-fx-border-color:#E1E5E8;" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-font-size:14px;"
        );

        HBox.setHgrow(
                search,
                Priority.ALWAYS
        );

        // =====================================================
        // ADD OWNER BUTTON
        // =====================================================

        Button addOwnerBtn =
                new Button("+ Add New Owner");

        addOwnerBtn.setPrefWidth(180);

        addOwnerBtn.setPrefHeight(45);

        addOwnerBtn.setStyle(
                "-fx-background-color:#434141;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        // =====================================================
        // REFRESH BUTTON
        // =====================================================

        Button refreshBtn =
                new Button("⟳");

        refreshBtn.setPrefWidth(45);
        refreshBtn.setPrefHeight(45);

        refreshBtn.setMinWidth(45);
        refreshBtn.setMinHeight(45);

        refreshBtn.setMaxWidth(45);
        refreshBtn.setMaxHeight(45);

        refreshBtn.setStyle(
                "-fx-background-color:#56342B;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;" +
                "-fx-padding:0;"
        );

        refreshBtn.setTooltip(
                new Tooltip("Refresh")
        );

        // =====================================================
        // SEARCH BOX
        // =====================================================

        HBox searchBox =
                new HBox(12);

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        searchBox.setMaxWidth(
                Double.MAX_VALUE
        );

        searchBox.getChildren().addAll(
                search,
                addOwnerBtn,
                refreshBtn
        );

        // =====================================================
        // OWNER LIST
        // =====================================================

        ownerList =
                new VBox(15);

        ownerList.setPadding(
                new Insets(15, 0, 20, 0)
        );

        ownerList.setFillWidth(true);

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                ownerList
        );

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:transparent;"
        );

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        // =====================================================
        // MAIN ROOT
        // =====================================================

        HBox mainRoot =
                new HBox();

        mainRoot.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        mainRoot.getChildren().addAll(
                sidebar,
                mainvb
        );

        mainRoot.setStyle(
                "-fx-background-color:#434141;"
        );

        HBox.setHgrow(
                mainvb,
                Priority.ALWAYS
        );

        // =====================================================
        // ROOT STACKPANE
        // =====================================================

        rootStack =
                new StackPane();

        rootStack.getChildren().add(
                mainRoot
        );

        // =====================================================
        // ADD OWNER
        // =====================================================

        addOwnerBtn.setOnAction(
                e -> openAddOwnerDialog()
        );

        // =====================================================
        // REFRESH
        // =====================================================

        refreshBtn.setOnAction(e -> {

            search.clear();

            loadOwners();

            System.out.println(
                    "Owner data refreshed."
            );
        });

        // =====================================================
        // SEARCH
        // =====================================================

        search.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    filterOwners(newValue);
                }
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        mainvb.getChildren().addAll(
                title,
                subtitle,
                searchBox,
                scrollPane
        );

        // =====================================================
        // LOAD DATA
        // =====================================================

        loadOwners();

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        rootStack,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        ownerScene = scene;

        return ownerScene;
    }

    // =====================================================
    // LOAD OWNERS
    //
    // IMPORTANT:
    // Only logged-in Secretary's society owners
    // =====================================================

    private void loadOwners() {

        ownerList.getChildren().clear();

        try {

            /*
             * DO NOT USE:
             *
             * ownerController.getAllOwners()
             *
             * because that returns owners from
             * every society.
             */

            allOwners =
                    ownerController
                            .getOwnersBySociety();

            if (allOwners == null) {

                allOwners =
                        new ArrayList<>();
            }

            System.out.println(
                    "Owners of logged-in Secretary's society: "
                            + allOwners.size()
            );

            if (allOwners.isEmpty()) {

                showEmptyMessage(
                        "No owners found in your society."
                );

                return;
            }

            for (Owner owner : allOwners) {

                if (owner == null) {
                    continue;
                }

                ownerList.getChildren().add(
                        createOwnerRow(owner)
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            showEmptyMessage(
                    "Unable to load owners."
            );
        }
    }

    // =====================================================
    // FILTER OWNERS
    //
    // Local search on already loaded society owners
    // =====================================================

    private void filterOwners(
            String searchText) {

        ownerList.getChildren().clear();

        String text =
                searchText == null
                        ? ""
                        : searchText
                                .toLowerCase()
                                .trim();

        // =====================================================
        // EMPTY SEARCH
        // =====================================================

        if (text.isEmpty()) {

            if (allOwners == null ||
                    allOwners.isEmpty()) {

                showEmptyMessage(
                        "No owners found in your society."
                );

                return;
            }

            for (Owner owner : allOwners) {

                if (owner == null) {
                    continue;
                }

                ownerList.getChildren().add(
                        createOwnerRow(owner)
                );
            }

            return;
        }

        // =====================================================
        // SEARCH CACHE
        // =====================================================

        boolean found = false;

        if (allOwners != null) {

            for (Owner owner : allOwners) {

                if (owner == null) {
                    continue;
                }

                String name =
                        safe(owner.getName())
                                .toLowerCase();

                String flat =
                        safe(owner.getFlat())
                                .toLowerCase();

                String mobile =
                        safe(owner.getMobile())
                                .toLowerCase();

                String email =
                        safe(owner.getEmail())
                                .toLowerCase();

                String status =
                        safe(owner.getStatus())
                                .toLowerCase();

                String society =
                        safe(owner.getSociety())
                                .toLowerCase();

                if (name.contains(text)
                        || flat.contains(text)
                        || mobile.contains(text)
                        || email.contains(text)
                        || status.contains(text)
                        || society.contains(text)) {

                    ownerList.getChildren().add(
                            createOwnerRow(owner)
                    );

                    found = true;
                }
            }
        }

        // =====================================================
        // NOTHING FOUND
        // =====================================================

        if (!found) {

            showEmptyMessage(
                    "No matching owners found."
            );
        }
    }

    // =====================================================
    // CREATE OWNER ROW
    // =====================================================

    private HBox createOwnerRow(
            Owner owner) {

        HBox ownerRow =
                new HBox(10);

        ownerRow.setPrefHeight(90);

        ownerRow.setMaxWidth(
                Double.MAX_VALUE
        );

        ownerRow.setAlignment(
                Pos.CENTER_LEFT
        );

        ownerRow.setPadding(
                new Insets(15)
        );

        ownerRow.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;"
        );

        // =====================================================
        // PROFILE
        // =====================================================

        Label profile =
                new Label("👤");

        profile.setPrefWidth(45);
        profile.setPrefHeight(45);

        profile.setAlignment(
                Pos.CENTER
        );

        profile.setStyle(
                "-fx-background-color:#E5E7EB;" +
                "-fx-background-radius:50%;" +
                "-fx-font-size:21px;"
        );

        // =====================================================
        // OWNER DETAILS
        // =====================================================

        VBox ownerDetails =
                new VBox(3);

        ownerDetails.setPrefWidth(220);

        Label name =
                new Label(
                        safe(owner.getName())
                );

        name.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#123C36;"
        );

        Label email =
                new Label(
                        safe(owner.getEmail())
                );

        email.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#666666;"
        );

        ownerDetails.getChildren().addAll(
                name,
                email
        );

        // =====================================================
        // FLAT
        // =====================================================

        VBox flatBox =
                new VBox(2);

        Label flatTitle =
                new Label("Flat");

        flatTitle.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#777777;"
        );

        Label flat =
                new Label(
                        safe(owner.getFlat())
                );

        flat.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#555555;"
        );

        flatBox.getChildren().addAll(
                flatTitle,
                flat
        );

        flatBox.setPrefWidth(100);

        // =====================================================
        // MOBILE
        // =====================================================

        VBox mobileBox =
                new VBox(2);

        Label mobileTitle =
                new Label("Mobile");

        mobileTitle.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#777777;"
        );

        Label mobile =
                new Label(
                        safe(owner.getMobile())
                );

        mobile.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#555555;"
        );

        mobileBox.getChildren().addAll(
                mobileTitle,
                mobile
        );

        mobileBox.setPrefWidth(150);

        // =====================================================
        // SOCIETY
        // =====================================================

        VBox societyBox =
                new VBox(2);

        Label societyTitle =
                new Label("Society");

        societyTitle.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#777777;"
        );

        Label society =
                new Label(
                        safe(owner.getSociety())
                );

        society.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#56342B;"
        );

        societyBox.getChildren().addAll(
                societyTitle,
                society
        );

        societyBox.setPrefWidth(180);

        // =====================================================
        // STATUS
        // =====================================================

        VBox statusBox =
                new VBox(2);

        Label statusTitle =
                new Label("Status");

        statusTitle.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#777777;"
        );

        Label status =
                new Label(
                        safe(owner.getStatus())
                );

        status.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#2E9D63;"
        );

        statusBox.getChildren().addAll(
                statusTitle,
                status
        );

        statusBox.setPrefWidth(100);

        // =====================================================
        // ADD TO ROW
        // =====================================================

        ownerRow.getChildren().addAll(
                profile,
                ownerDetails,
                flatBox,
                mobileBox,
                societyBox,
                statusBox
        );

        return ownerRow;
    }

    // =====================================================
    // ADD OWNER POPUP
    // =====================================================

    private void openAddOwnerDialog() {

        StackPane overlay =
                new StackPane();

        overlay.setStyle(
                "-fx-background-color:rgba(0,0,0,0.5);"
        );

        // =====================================================
        // FORM BOX
        // =====================================================

        VBox formBox =
                new VBox(7);

        formBox.setPadding(
                new Insets(18)
        );

        formBox.setPrefWidth(430);

        formBox.setMaxWidth(430);

        formBox.setMaxHeight(
                Math.min(
                        ScreenSize.getHeight() - 40,
                        600
                )
        );

        formBox.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:15;" +
                "-fx-effect:dropshadow(" +
                "gaussian, rgba(0,0,0,0.3)," +
                "20,0.2,0,5);"
        );

        // =====================================================
        // HEADER
        // =====================================================

        HBox headerRow =
                new HBox();

        headerRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label popupTitle =
                new Label(
                        "Add New Owner"
                );

        popupTitle.setFont(
                Font.font(
                        "Georgia",
                        FontWeight.BOLD,
                        21
                )
        );

        popupTitle.setStyle(
                "-fx-text-fill:#123C36;"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button closeBtn =
                new Button("✕");

        closeBtn.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        closeBtn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#555555;" +
                "-fx-cursor:hand;"
        );

        headerRow.getChildren().addAll(
                popupTitle,
                spacer,
                closeBtn
        );

        // =====================================================
        // SOCIETY
        // =====================================================

        Label societyLabel =
                createFormLabel(
                        "Society Name"
                );

        TextField societyField =
                new TextField();

        societyField.setPromptText(
                "Enter society name"
        );

        societyField.setPrefHeight(35);

        // =====================================================
        // OWNER NAME
        // =====================================================

        Label nameLabel =
                createFormLabel(
                        "Owner Name"
                );

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Enter owner name"
        );

        nameField.setPrefHeight(35);

        // =====================================================
        // FLAT
        // =====================================================

        Label flatLabel =
                createFormLabel(
                        "Flat Number"
                );

        TextField flatField =
                new TextField();

        flatField.setPromptText(
                "Enter flat number"
        );

        flatField.setPrefHeight(35);

        // =====================================================
        // MOBILE
        // =====================================================

        Label mobileLabel =
                createFormLabel(
                        "Mobile Number"
                );

        TextField mobileField =
                new TextField();

        mobileField.setPromptText(
                "Enter mobile number"
        );

        mobileField.setPrefHeight(35);

        // =====================================================
        // EMAIL
        // =====================================================

        Label emailLabel =
                createFormLabel(
                        "Email"
                );

        TextField emailField =
                new TextField();

        emailField.setPromptText(
                "Enter email"
        );

        emailField.setPrefHeight(35);

        // =====================================================
        // STATUS
        // =====================================================

        Label statusLabel =
                createFormLabel(
                        "Status"
                );

        TextField statusField =
                new TextField();

        statusField.setPromptText(
                "Active / Inactive"
        );

        statusField.setPrefHeight(35);

        // =====================================================
        // BUTTONS
        // =====================================================

        Button cancelBtn =
                new Button("Cancel");

        cancelBtn.setPrefWidth(100);

        cancelBtn.setPrefHeight(38);

        cancelBtn.setStyle(
                "-fx-background-color:#E5E7EB;" +
                "-fx-text-fill:#333333;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        Button saveBtn =
                new Button("Save Owner");

        saveBtn.setPrefWidth(130);

        saveBtn.setPrefHeight(38);

        saveBtn.setStyle(
                "-fx-background-color:#2E9D63;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        HBox buttonBox =
                new HBox(10);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.getChildren().addAll(
                cancelBtn,
                saveBtn
        );

        // =====================================================
        // FORM
        // =====================================================

        formBox.getChildren().addAll(

                headerRow,

                societyLabel,
                societyField,

                nameLabel,
                nameField,

                flatLabel,
                flatField,

                mobileLabel,
                mobileField,

                emailLabel,
                emailField,

                statusLabel,
                statusField,

                buttonBox
        );

        // =====================================================
        // OVERLAY
        // =====================================================

        overlay.getChildren().add(
                formBox
        );

        StackPane.setAlignment(
                formBox,
                Pos.CENTER
        );

        rootStack.getChildren().add(
                overlay
        );

        // =====================================================
        // CLOSE
        // =====================================================

        closeBtn.setOnAction(
                e -> removeOverlay(overlay)
        );

        // =====================================================
        // CANCEL
        // =====================================================

        cancelBtn.setOnAction(
                e -> removeOverlay(overlay)
        );

        // =====================================================
        // SAVE OWNER
        // =====================================================

        saveBtn.setOnAction(e -> {

            String society =
                    societyField
                            .getText()
                            .trim();

            String name =
                    nameField
                            .getText()
                            .trim();

            String flat =
                    flatField
                            .getText()
                            .trim();

            String mobile =
                    mobileField
                            .getText()
                            .trim();

            String email =
                    emailField
                            .getText()
                            .trim()
                            .toLowerCase();

            String status =
                    statusField
                            .getText()
                            .trim();

            // =================================================
            // VALIDATION
            // =================================================

            if (society.isEmpty()
                    || name.isEmpty()
                    || flat.isEmpty()
                    || mobile.isEmpty()
                    || email.isEmpty()
                    || status.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation Error",
                        "Please fill all fields."
                );

                return;
            }

            // =================================================
            // EMAIL VALIDATION
            // =================================================

            if (!email.contains("@")
                    || !email.contains(".")) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Email",
                        "Please enter a valid email address."
                );

                return;
            }

            // =================================================
            // ADD OWNER
            // =================================================

            boolean success =
                    ownerController.addOwner(
                            name,
                            flat,
                            mobile,
                            email,
                            status,
                            society
                    );

            // =================================================
            // SUCCESS
            // =================================================

            if (success) {

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Owner saved successfully!"
                );

                removeOverlay(
                        overlay
                );

                societyField.clear();
                nameField.clear();
                flatField.clear();
                mobileField.clear();
                emailField.clear();
                statusField.clear();

                // -------------------------------------------------
                // Reload only logged-in Secretary's society
                // -------------------------------------------------

                loadOwners();

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed to save owner.\n"
                                + "This email may already exist."
                );
            }
        });
    }

    // =====================================================
    // FORM LABEL
    // =====================================================

    private Label createFormLabel(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#333333;" +
                "-fx-font-size:13px;"
        );

        return label;
    }

    // =====================================================
    // REMOVE OVERLAY
    // =====================================================

    private void removeOverlay(
            StackPane overlay) {

        if (rootStack != null) {

            rootStack.getChildren()
                    .remove(overlay);
        }
    }

    // =====================================================
    // EMPTY MESSAGE
    // =====================================================

    private void showEmptyMessage(
            String message) {

        ownerList.getChildren().clear();

        Label emptyLabel =
                new Label(message);

        emptyLabel.setStyle(
                "-fx-font-size:16px;" +
                "-fx-text-fill:#555555;"
        );

        ownerList.getChildren().add(
                emptyLabel
        );
    }

    // =====================================================
    // ALERT
    // =====================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }
}