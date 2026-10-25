package com.society.view.Resident_portal;

import com.society.dao.Welcome.UserDao;
import com.society.model.Welcome.User;
import com.society.view.Welcome.LogInPage;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class panel {

        // ---------------------------------------------------------
        // SIDEBAR
        // ---------------------------------------------------------

        private VBox sidebar;
        private ScrollPane sidebarScroll;

        // Active button
        private Button activeButton;

        // Remember active page
        private static String activePage = "";

        // Sidebar animation only once
        private static boolean sidebarAnimationDone = false;

        // ---------------------------------------------------------
        // COLORS
        // ---------------------------------------------------------

        private final String NORMAL_BG = "#4e342e";
        private final String NORMAL_TEXT = "white";

        private final String ACTIVE_BG = "#e7c3bb";
        private final String ACTIVE_TEXT = "#4e342e";

        // ---------------------------------------------------------
        // CONSTRUCTOR
        // ---------------------------------------------------------

        public panel(Stage stage, String residentEmail) {

                createSidebar(stage, residentEmail);
        }

        // =========================================================
        // CREATE SIDEBAR
        // =========================================================

        private void createSidebar(Stage stage, String residentEmail) {

                sidebar = new VBox();

                sidebar.setPadding(new Insets(20));

                sidebar.setPrefWidth(280);
                sidebar.setMinWidth(280);
                sidebar.setMaxWidth(280);

                sidebar.setPrefHeight(Region.USE_COMPUTED_SIZE);

                sidebar.setAlignment(Pos.TOP_LEFT);

                sidebar.setSpacing(14);

                // Important - sidebar stays at left/top
                sidebar.setTranslateX(0);
                sidebar.setTranslateY(0);

                sidebar.setStyle(
                                "-fx-background-color: #4e342e;");

                // =====================================================
                // LOGO
                // =====================================================

                Label logo = new Label("Society360");

                logo.setLineSpacing(10);

                logo.setAlignment(Pos.CENTER_LEFT);

                logo.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-family:'Bodoni MT';" +
                                                "-fx-font-size:22px;" +
                                                "-fx-font-weight:bold;");

                // =====================================================
                // PANEL LABEL
                // =====================================================

                Label panel = new Label("Resident Panel");

                panel.setStyle(
                                "-fx-text-fill: lightgray;" +
                                                "-fx-font-size:14px;" +
                                                "-fx-padding:5px;");

                // =====================================================
                // DASHBOARD
                // =====================================================

                Button dashboardBtn = createButton("🏠 Dashboard");

                dashboardBtn.setOnAction(event -> {

                        setActiveButton(dashboardBtn);

                        ResidentDashboard residentDashboard = new ResidentDashboard();

                        Scene scene = residentDashboard.getResidentDashboardScene(stage);

                        navigate(stage, scene);
                });

                // =====================================================
                // MY BILLS
                // =====================================================

                Button ownersBtn = createButton("💳 My Bills");

                ownersBtn.setOnAction(event -> {

                        setActiveButton(ownersBtn);

                        String loginEmail = UserDao.getLoggedInEmail();

                        if (loginEmail == null ||
                                        loginEmail.trim().isEmpty()) {

                                System.out.println(
                                                "No logged-in resident found.");

                                return;
                        }

                        loginEmail = loginEmail.trim();

                        Mybills bills = new Mybills(loginEmail);

                        navigate(
                                        stage,
                                        bills.getBillScene(stage));
                });

                // =====================================================
                // VISITORS
                // =====================================================

                Button guardsBtn = createButton("👥 Visitors");

                guardsBtn.setOnAction(event -> {

                        setActiveButton(guardsBtn);

                        Visitor visitor = new Visitor();

                        Scene visitorScene = visitor.getVisitorScene(stage);

                        if (visitorScene == null) {
                                return;
                        }

                        navigate(stage, visitorScene);
                });

                // =====================================================
                // AMENITIES BOOKING
                // =====================================================

                Button noticesBtn = createButton("📅 Amenities Booking");

                noticesBtn.setOnAction(event -> {

                        setActiveButton(noticesBtn);

                        String loginEmail = UserDao.getLoggedInEmail();

                        if (loginEmail == null ||
                                        loginEmail.trim().isEmpty()) {

                                System.out.println(
                                                "No logged-in resident found.");

                                return;
                        }

                        loginEmail = loginEmail.trim();

                        UserDao dao = new UserDao();

                        User resident = dao.getUserByEmail(loginEmail);

                        if (resident == null) {

                                System.out.println(
                                                "Resident profile not found for: "
                                                                + loginEmail);

                                return;
                        }

                        String residentName = resident.getName();

                        String flatNo = resident.getFlatNo();

                        if (residentName == null) {
                                residentName = "";
                        }

                        if (flatNo == null) {
                                flatNo = "";
                        }

                        AmenitiesBooking amenitiesBooking = new AmenitiesBooking(
                                        residentName,
                                        flatNo,
                                        loginEmail);

                        navigate(
                                        stage,
                                        amenitiesBooking.getAminityScene(stage));
                });

                // =====================================================
                // NOTICE
                // =====================================================

                Button complaintsBtn = createButton("📢 Notice");

                complaintsBtn.setOnAction(event -> {

                        setActiveButton(complaintsBtn);

                        Notice community = new Notice(residentEmail);

                        navigate(
                                        stage,
                                        community.getResidentbtScene(
                                                        stage,
                                                        residentEmail));
                });

                // =====================================================
                // COMPLAINTS
                // =====================================================

                Button paymentsBtn = createButton("❗ Complaints");

                paymentsBtn.setOnAction(event -> {

                        setActiveButton(paymentsBtn);

                        Complaint complaint = new Complaint();

                        navigate(
                                        stage,
                                        complaint.getComplaintScene(stage));
                });

                // =====================================================
                // COMMUNITY
                // =====================================================

                Button cumunityButton = createButton("👥 Community");

                cumunityButton.setOnAction(event -> {

                        setActiveButton(cumunityButton);

                        Community community = new Community(residentEmail);

                        navigate(
                                        stage,
                                        community.getCommunityScene(stage));
                });

                // =====================================================
                // EMERGENCY SOS
                // =====================================================

                Button sosBtn = createButton("🆘 Emergency SOS");

                sosBtn.setOnAction(event -> {

                        setActiveButton(sosBtn);

                        Emergency emergency = new Emergency();

                        navigate(
                                        stage,
                                        emergency.getEmergencyScene(
                                                        stage,
                                                        residentEmail));
                });

                // =====================================================
                // POLLS & SURVEYS
                // =====================================================

                Button eventsBtn = createButton("📊 Polls & Surveys");

                eventsBtn.setOnAction(event -> {

                        setActiveButton(eventsBtn);

                        PollsSurveys polls = new PollsSurveys(
                                        stage,
                                        residentEmail);

                        navigate(
                                        stage,
                                        polls.createScene(
                                                        stage,
                                                        residentEmail));
                });

                // =====================================================
                // DOCUMENTS
                // =====================================================

                Button reportsBtn = createButton("📄 Documents");

                reportsBtn.setOnAction(event -> {

                        setActiveButton(reportsBtn);

                        Document document = new Document();

                        navigate(
                                        stage,
                                        document.getDocumentScene(
                                                        stage,
                                                        residentEmail));
                });

                // =====================================================
                // PROFILE
                // =====================================================

                Button profileBtn = createButton("👤 Profile");

                profileBtn.setOnAction(event -> {

                        setActiveButton(profileBtn);

                        ProfilePage profil = new ProfilePage();

                        navigate(
                                        stage,
                                        profil.getProfileScene(
                                                        stage,
                                                        residentEmail));
                });

                // =====================================================
                // PARKING
                // =====================================================

                Button Parkingbtn = createButton("🚗 Parking");

                setActiveButton(Parkingbtn);

                //Parking parking = new Parking();

                // navigate(
                //                 stage,
                //                 parking.getParkingScene(stage));

                // ---------------------------------------------------------
                // PARKING
                // ---------------------------------------------------------
                Parkingbtn.setStyle(
                                "-fx-background-color: #4e342e;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-alignment: CENTER-LEFT;" +
                                                "-fx-pref-width: 240px;" +
                                                "-fx-pref-height: 40px;");

                Parkingbtn.setOnAction(event -> {

                        // Get currently logged-in resident email
                        String loginEmail = UserDao.getLoggedInEmail();

                        // Check login email
                        if (loginEmail == null ||
                                        loginEmail.trim().isEmpty()) {

                                System.out.println(
                                                "ERROR: No logged-in resident email found.");

                                return;
                        }

                        // Normalize email
                        loginEmail = loginEmail.trim().toLowerCase();

                        System.out.println(
                                        "Opening Parking for: "
                                                        + loginEmail);

                        // Create Parking object with logged-in email
                        Parking parking = new Parking(loginEmail);

                        // IMPORTANT:
                        // Pass email to Parking page
                        Scene parkingScene = parking.getParkingScene(
                                        stage,
                                        loginEmail);

                        stage.setScene(
                                        parkingScene);

                        stage.show();
                });

                Button aboutUsPage = createButton("👤 AboutUs");

                aboutUsPage.setOnAction(event -> {

                        setActiveButton(aboutUsPage);

                        AboutUsPage profil = new AboutUsPage(residentEmail);

                        navigate(
                                        stage,
                                        profil.getAboutUsScene(
                                                        stage,
                                                        residentEmail));
                });

                // =====================================================
                // ADD ALL COMPONENTS
                // =====================================================

                sidebar.getChildren().addAll(

                                logo,
                                panel,

                                dashboardBtn,
                                ownersBtn,
                                guardsBtn,
                                noticesBtn,
                                complaintsBtn,
                                paymentsBtn,
                                cumunityButton,
                                sosBtn,
                                eventsBtn,
                                reportsBtn,
                                profileBtn,
                                Parkingbtn,
                                aboutUsPage
                // logoutBtn
                );

                // =====================================================
                // RESTORE ACTIVE BUTTON
                // =====================================================

                restoreActiveButton(
                                dashboardBtn,
                                ownersBtn,
                                guardsBtn,
                                noticesBtn,
                                complaintsBtn,
                                paymentsBtn,
                                cumunityButton,
                                sosBtn,
                                eventsBtn,
                                reportsBtn,
                                profileBtn,
                                Parkingbtn,
                                aboutUsPage
                // logoutBtn
                );

                // =====================================================
                // BUTTON HOVER ANIMATION
                // =====================================================

                animateButton(dashboardBtn);
                animateButton(ownersBtn);
                animateButton(guardsBtn);
                animateButton(noticesBtn);
                animateButton(complaintsBtn);
                animateButton(paymentsBtn);
                animateButton(cumunityButton);
                animateButton(sosBtn);
                animateButton(eventsBtn);
                animateButton(reportsBtn);
                animateButton(profileBtn);
                animateButton(aboutUsPage);
                

                // animateButton(logoutBtn);

                // =====================================================
                // SCROLL PANE
                // =====================================================

                sidebarScroll = new ScrollPane();

                sidebarScroll.setContent(sidebar);

                sidebarScroll.setPrefWidth(280);
                sidebarScroll.setMinWidth(280);
                sidebarScroll.setMaxWidth(280);

                sidebarScroll.setFitToWidth(true);
                sidebarScroll.setFitToHeight(false);

                sidebarScroll.setPannable(true);

                // Hide horizontal scrollbar
                sidebarScroll.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                // Hide vertical scrollbar
                sidebarScroll.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                sidebarScroll.setStyle(
                                "-fx-background-color:#4e342e;" +
                                                "-fx-border-color:transparent;");

                // =====================================================
                // SIDEBAR ENTRY ANIMATION
                // =====================================================

                if (!sidebarAnimationDone) {

                        animateButtonEntry(dashboardBtn, 100);
                        animateButtonEntry(ownersBtn, 180);
                        animateButtonEntry(guardsBtn, 260);
                        animateButtonEntry(noticesBtn, 340);
                        animateButtonEntry(complaintsBtn, 420);
                        animateButtonEntry(paymentsBtn, 500);
                        animateButtonEntry(cumunityButton, 580);
                        animateButtonEntry(sosBtn, 660);
                        animateButtonEntry(eventsBtn, 740);
                        animateButtonEntry(reportsBtn, 820);
                        animateButtonEntry(profileBtn, 900);
                        animateButtonEntry(Parkingbtn, 980);
                        animateButtonEntry(aboutUsPage, 980);
                        // animateButtonEntry(logoutBtn, 1060);

                        sidebarAnimationDone = true;

                } else {

                        dashboardBtn.setOpacity(1);
                        ownersBtn.setOpacity(1);
                        guardsBtn.setOpacity(1);
                        noticesBtn.setOpacity(1);
                        complaintsBtn.setOpacity(1);
                        paymentsBtn.setOpacity(1);
                        cumunityButton.setOpacity(1);
                        sosBtn.setOpacity(1);
                        eventsBtn.setOpacity(1);
                        reportsBtn.setOpacity(1);
                        profileBtn.setOpacity(1);
                        Parkingbtn.setOpacity(1);
                        // logoutBtn.setOpacity(1);

                        dashboardBtn.setTranslateX(0);
                        ownersBtn.setTranslateX(0);
                        guardsBtn.setTranslateX(0);
                        noticesBtn.setTranslateX(0);
                        complaintsBtn.setTranslateX(0);
                        paymentsBtn.setTranslateX(0);
                        cumunityButton.setTranslateX(0);
                        sosBtn.setTranslateX(0);
                        eventsBtn.setTranslateX(0);
                        reportsBtn.setTranslateX(0);
                        profileBtn.setTranslateX(0);
                        Parkingbtn.setTranslateX(0);
                        // logoutBtn.setTranslateX(0);
                }
        }

        // =========================================================
        // CREATE BUTTON
        // =========================================================

        private Button createButton(String text) {

                Button button = new Button(text);

                button.setPrefWidth(250);
                button.setMinWidth(250);
                button.setMaxWidth(250);

                button.setPrefHeight(40);
                button.setMinHeight(40);
                button.setMaxHeight(40);

                button.setAlignment(Pos.CENTER_LEFT);

                setNormalStyle(button);

                return button;
        }

        // =========================================================
        // NORMAL BUTTON STYLE
        // =========================================================

        private void setNormalStyle(Button button) {

                button.setStyle(
                                "-fx-background-color:" + NORMAL_BG + ";" +
                                                "-fx-text-fill:" + NORMAL_TEXT + ";" +
                                                "-fx-font-size:16px;" +
                                                "-fx-alignment:CENTER-LEFT;" +
                                                "-fx-background-radius:7;");
        }

        // =========================================================
        // ACTIVE BUTTON STYLE
        // =========================================================

        private void setActiveStyle(Button button) {

                button.setStyle(
                                "-fx-background-color:" + ACTIVE_BG + ";" +
                                                "-fx-text-fill:" + ACTIVE_TEXT + ";" +
                                                "-fx-font-size:16px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-alignment:CENTER-LEFT;" +
                                                "-fx-background-radius:7;");
        }

        // =========================================================
        // SET ACTIVE BUTTON
        // =========================================================

        private void setActiveButton(Button button) {

                if (activeButton != null &&
                                activeButton != button) {

                        setNormalStyle(activeButton);
                }

                activeButton = button;

                setActiveStyle(button);

                activePage = button.getText();
        }

        // =========================================================
        // RESTORE ACTIVE BUTTON
        // =========================================================

        private void restoreActiveButton(Button... buttons) {

                if (activePage == null ||
                                activePage.isEmpty()) {

                        setActiveButton(buttons[0]);
                        return;
                }

                for (Button button : buttons) {

                        if (button.getText().equals(activePage)) {

                                activeButton = button;

                                setActiveStyle(button);

                                return;
                        }
                }

                setActiveButton(buttons[0]);
        }

        // =========================================================
        // HOVER + SCALE ANIMATION
        // =========================================================

        private void animateButton(Button button) {

                button.setOnMouseEntered(event -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(120),
                                        button);

                        scale.setToX(1.04);
                        scale.setToY(1.04);

                        scale.play();

                        if (button == activeButton) {

                                setActiveStyle(button);

                        } else {

                                setActiveStyle(button);
                        }
                });

                button.setOnMouseExited(event -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(120),
                                        button);

                        scale.setToX(1.0);
                        scale.setToY(1.0);

                        scale.play();

                        if (button == activeButton) {

                                setActiveStyle(button);

                        } else {

                                setNormalStyle(button);
                        }
                });
        }

        // =========================================================
        // BUTTON ENTRY ANIMATION
        // =========================================================

        private void animateButtonEntry(
                        Button button,
                        int delay) {

                button.setOpacity(0);

                button.setTranslateX(-20);

                FadeTransition fade = new FadeTransition(
                                Duration.millis(400),
                                button);

                fade.setFromValue(0);
                fade.setToValue(1);

                TranslateTransition slide = new TranslateTransition(
                                Duration.millis(400),
                                button);

                slide.setFromX(-20);
                slide.setToX(0);

                ParallelTransition animation = new ParallelTransition(
                                fade,
                                slide);

                animation.setDelay(
                                Duration.millis(delay));

                animation.play();
        }

        // =========================================================
        // NAVIGATION
        // =========================================================

        private void navigate(
                        Stage stage,
                        Scene newScene) {

                if (newScene == null) {
                        return;
                }

                // Keep sidebar on LEFT
                if (newScene.getRoot() instanceof BorderPane) {

                        BorderPane newRoot = (BorderPane) newScene.getRoot();

                        newRoot.setLeft(sidebarScroll);
                }

                // Keep sidebar fixed
                sidebarScroll.setTranslateX(0);
                sidebarScroll.setTranslateY(0);
                sidebarScroll.setOpacity(1);

                stage.setScene(newScene);

                stage.show();
        }

        // =========================================================
        // GET SIDEBAR
        // =========================================================

        public ScrollPane getSidebar() {

                return sidebarScroll;
        }
}