
package com.society.view.Owner_portal;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OwnerSidebar {

    private VBox sidebar;

    // =========================================================
    // LOGGED-IN OWNER EMAIL
    // =========================================================

    private String ownerEmail;

    // =========================================================
    // CURRENT ACTIVE BUTTON
    // =========================================================

    private Button activeButton;

    // =========================================================
    // REMEMBER ACTIVE PAGE
    // =========================================================

    private static String activePage = "";

    // =========================================================
    // SIDEBAR ANIMATION
    // =========================================================

    private static boolean sidebarAnimationDone = false;

    // =========================================================
    // COLORS
    // =========================================================

    private final String NORMAL_BG = "#4e342e";
    private final String NORMAL_TEXT = "white";

    private final String ACTIVE_BG = "#e7c3bb";
    private final String ACTIVE_TEXT = "#4e342e";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public OwnerSidebar(
            Stage stage,
            String ownerEmail) {

        this.ownerEmail = ownerEmail;

        // =====================================================
        // SIDEBAR
        // =====================================================

        sidebar = new VBox();

        sidebar.setPadding(
                new Insets(20)
        );

        sidebar.setPrefWidth(
                280
        );

        sidebar.setMinWidth(
                280
        );

        sidebar.setPrefHeight(
                750
        );

        sidebar.setAlignment(
                Pos.TOP_LEFT
        );

        sidebar.setSpacing(
                21
        );

        sidebar.setStyle(
                "-fx-background-color: #4e342e;"
        );

        // =====================================================
        // LOGO
        // =====================================================

        Label logo =
                new Label(
                        "Society360"
                );

        logo.setAlignment(
                Pos.CENTER
        );

        logo.setStyle(
                "-fx-font-family:'Bodoni MT';" +
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        // =====================================================
        // PORTAL
        // =====================================================

        Label portal =
                new Label(
                        "Owner Panel"
                );

        portal.setAlignment(
                Pos.CENTER
        );

        portal.setTextFill(
                Color.LIGHTBLUE
        );

        portal.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;"
        );

        // =====================================================
        // BUTTONS
        // =====================================================

        Button dashboard =
                new Button(
                        "🏠 Dashboard"
                );

        Button addTenant =
                new Button(
                        "👤+ Add Tenant"
                );

        Button removeTenant =
                new Button(
                        "👤− Remove Tenant"
                );

        Button payments =
                new Button(
                        "💳 View Payments"
                );

        Button rentalHistory =
                new Button(
                        "🕐 Rental History"
                );

        Button ownerDocuments =
                new Button(
                        "📄 Owner Document"
                );

        Button profile =
                new Button(
                        "👤 Profile"
                );

        // =====================================================
        // BUTTON SIZE
        // =====================================================

        dashboard.setPrefSize(
                250,
                40
        );

        addTenant.setPrefSize(
                250,
                40
        );

        removeTenant.setPrefSize(
                250,
                40
        );

        payments.setPrefSize(
                250,
                40
        );

        rentalHistory.setPrefSize(
                250,
                40
        );

        ownerDocuments.setPrefSize(
                250,
                40
        );

        profile.setPrefSize(
                250,
                40
        );

        // =====================================================
        // NORMAL STYLE
        // =====================================================

        setNormalStyle(
                dashboard
        );

        setNormalStyle(
                addTenant
        );

        setNormalStyle(
                removeTenant
        );

        setNormalStyle(
                payments
        );

        setNormalStyle(
                rentalHistory
        );

        setNormalStyle(
                ownerDocuments
        );

        setNormalStyle(
                profile
        );

        // =====================================================
        // DASHBOARD
        // =====================================================

        dashboard.setOnAction(e -> {

            setActiveButton(
                    dashboard
            );

            stage.setScene(
                    OwnerDashboard.createScene(
                            stage,
                            ownerEmail
                    )
            );

            stage.show();
        });

        // =====================================================
        // ADD TENANT
        // =====================================================

        addTenant.setOnAction(e -> {

            setActiveButton(
                    addTenant
            );

            stage.setScene(
                    AddTenant.createScene(
                            stage,
                            ownerEmail
                    )
            );

            stage.show();
        });

        // =====================================================
        // REMOVE TENANT
        // =====================================================

        removeTenant.setOnAction(e -> {

            setActiveButton(
                    removeTenant
            );

            stage.setScene(
                    RemoveTenant.createScene(
                            stage,
                            ownerEmail
                    )
            );

            stage.show();
        });

        // =====================================================
        // PAYMENTS
        // =====================================================

        payments.setOnAction(e -> {

            setActiveButton(
                    payments
            );

            stage.setScene(
                    ViewPayments.createScene(
                            stage,
                            ownerEmail
                    )
            );

            stage.show();
        });

        // =====================================================
        // RENTAL HISTORY
        // =====================================================

        rentalHistory.setOnAction(e -> {

            setActiveButton(
                    rentalHistory
            );

            stage.setScene(
                    RentalHistory.createScene(
                            stage,
                            ownerEmail
                    )
            );

            stage.show();
        });

        // =====================================================
        // OWNER DOCUMENTS
        // =====================================================

        ownerDocuments.setOnAction(e -> {

            setActiveButton(
                    ownerDocuments
            );

            stage.setScene(
                    OwnerDocuments.createScene(
                            stage,
                            ownerEmail
                    )
            );

            stage.show();
        });

        // =====================================================
        // PROFILE
        // =====================================================

        profile.setOnAction(e -> {

            setActiveButton(
                    profile
            );

            stage.setScene(
                    OwnerProfile.createScene(
                            stage,
                            ownerEmail
                    )
            );

            stage.show();
        });

        // =====================================================
        // ADD BUTTONS TO SIDEBAR
        // =====================================================

        sidebar.getChildren().addAll(

                logo,

                portal,

                dashboard,

                addTenant,

                removeTenant,

                payments,

                rentalHistory,

                ownerDocuments,

                profile

        );

        // =====================================================
        // RESTORE PREVIOUS ACTIVE BUTTON
        // =====================================================

        if (activePage.equals(
                "🏠 Dashboard")) {

            activeButton =
                    dashboard;

            setActiveStyle(
                    dashboard
            );

        } else if (activePage.equals(
                "👤+ Add Tenant")) {

            activeButton =
                    addTenant;

            setActiveStyle(
                    addTenant
            );

        } else if (activePage.equals(
                "👤− Remove Tenant")) {

            activeButton =
                    removeTenant;

            setActiveStyle(
                    removeTenant
            );

        } else if (activePage.equals(
                "💳 View Payments")) {

            activeButton =
                    payments;

            setActiveStyle(
                    payments
            );

        } else if (activePage.equals(
                "🕐 Rental History")) {

            activeButton =
                    rentalHistory;

            setActiveStyle(
                    rentalHistory
            );

        } else if (activePage.equals(
                "📄 Owner Document")) {

            activeButton =
                    ownerDocuments;

            setActiveStyle(
                    ownerDocuments
            );

        } else if (activePage.equals(
                "👤 Profile")) {

            activeButton =
                    profile;

            setActiveStyle(
                    profile
            );
        }

        // =====================================================
        // HOVER / SCALE ANIMATION
        // =====================================================

        animateButton(
                dashboard
        );

        animateButton(
                addTenant
        );

        animateButton(
                removeTenant
        );

        animateButton(
                payments
        );

        animateButton(
                rentalHistory
        );

        animateButton(
                ownerDocuments
        );

        animateButton(
                profile
        );

        // =====================================================
        // SIDEBAR ENTRY ANIMATION
        // =====================================================

        if (!sidebarAnimationDone) {

            animateButtonEntry(
                    dashboard,
                    100
            );

            animateButtonEntry(
                    addTenant,
                    180
            );

            animateButtonEntry(
                    removeTenant,
                    260
            );

            animateButtonEntry(
                    payments,
                    340
            );

            animateButtonEntry(
                    rentalHistory,
                    420
            );

            animateButtonEntry(
                    ownerDocuments,
                    500
            );

            animateButtonEntry(
                    profile,
                    580
            );

            sidebarAnimationDone = true;

        } else {

            // =================================================
            // NEXT PAGE → BUTTONS DIRECTLY VISIBLE
            // =================================================

            dashboard.setOpacity(1);
            addTenant.setOpacity(1);
            removeTenant.setOpacity(1);
            payments.setOpacity(1);
            rentalHistory.setOpacity(1);
            ownerDocuments.setOpacity(1);
            profile.setOpacity(1);
        }
    }

    // =========================================================
    // GET SIDEBAR
    // =========================================================

    public VBox getSidebar() {

        return sidebar;
    }

    // =========================================================
    // NORMAL BUTTON STYLE
    // =========================================================

    private void setNormalStyle(
            Button button) {

        button.setStyle(

                "-fx-background-color:"
                        + NORMAL_BG + ";" +

                "-fx-text-fill:"
                        + NORMAL_TEXT + ";" +

                "-fx-font-size:16px;" +

                "-fx-font-weight:normal;" +

                "-fx-alignment:CENTER-LEFT;" +

                "-fx-background-radius:7;" +

                "-fx-cursor:hand;"
        );
    }

    // =========================================================
    // ACTIVE BUTTON STYLE
    // =========================================================

    private void setActiveStyle(
            Button button) {

        button.setStyle(

                "-fx-background-color:"
                        + ACTIVE_BG + ";" +

                "-fx-text-fill:"
                        + ACTIVE_TEXT + ";" +

                "-fx-font-size:16px;" +

                "-fx-font-weight:bold;" +

                "-fx-alignment:CENTER-LEFT;" +

                "-fx-background-radius:7;" +

                "-fx-cursor:hand;"
        );
    }

    // =========================================================
    // SET ACTIVE BUTTON
    // =========================================================

    private void setActiveButton(
            Button button) {

        // =====================================================
        // PREVIOUS ACTIVE BUTTON → NORMAL
        // =====================================================

        if (activeButton != null) {

            setNormalStyle(
                    activeButton
            );
        }

        // =====================================================
        // NEW ACTIVE BUTTON
        // =====================================================

        activeButton =
                button;

        setActiveStyle(
                activeButton
        );

        // =====================================================
        // REMEMBER PAGE
        // =====================================================

        activePage =
                button.getText();
    }

    // =========================================================
    // BUTTON HOVER + SCALE ANIMATION
    // =========================================================

    private void animateButton(
            Button button) {

        button.setOnMouseEntered(e -> {

            if (!button.isDisabled()) {

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

                // =================================================
                // ACTIVE BUTTON
                // =================================================

                if (button ==
                        activeButton) {

                    setActiveStyle(
                            button
                    );

                } else {

                    // =================================================
                    // HOVER STYLE
                    // =================================================

                    button.setStyle(

                            "-fx-background-color:#e7c3bb;" +

                            "-fx-text-fill:#4e342e;" +

                            "-fx-font-size:16px;" +

                            "-fx-font-weight:bold;" +

                            "-fx-background-radius:7;" +

                            "-fx-alignment:CENTER-LEFT;" +

                            "-fx-cursor:hand;"
                    );
                }
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

            // =====================================================
            // RESTORE STYLE
            // =====================================================

            if (button ==
                    activeButton) {

                setActiveStyle(
                        button
                );

            } else {

                setNormalStyle(
                        button
                );
            }
        });
    }

    // =========================================================
    // BUTTON ENTRY FADE ANIMATION
    // =========================================================

    private void animateButtonEntry(
            Button button,
            int delay) {

        button.setOpacity(
                0
        );

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(400),
                        button
                );

        fade.setFromValue(
                0
        );

        fade.setToValue(
                1
        );

        fade.setDelay(
                Duration.millis(delay)
        );

        fade.play();
    }
}

