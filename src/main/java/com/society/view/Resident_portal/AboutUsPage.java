package com.society.view.Resident_portal;

import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class AboutUsPage {

    private final String residentEmail;

    

    private static final String BROWN = "#593A32";
    private static final String DARK_BROWN = "#4E342E";
    private static final String LIGHT_BG = "#F8F4F1";
    private static final String CARD_BG = "#FFFFFF";
    private static final String TEXT = "#2F2521";
    private static final String MUTED = "#766B66";
    private static final String BORDER = "#E4DAD5";
    private static final String ACCENT = "#A56A55";
    private static final String LIGHT_ACCENT = "#F1E5DF";

   
    private static final String HERO_IMAGE_URL =
            "https://res.cloudinary.com/wcnbam3e/image/upload/v1788784528/Society360logo.jpg";

    private static final String SHASHI_SIR_IMAGE_URL =
            "https://res.cloudinary.com/wcnbam3e/image/upload/v1788782805/Shashi_sir.jpg";
    private static final String TEAM_IMAGE_URL = "https://res.cloudinary.com/wcnbam3e/image/upload/v1788783493/Grouppic.jpg";
    private static final String CORE2WEB_LOGO_URL =
            "https://res.cloudinary.com/wcnbam3e/image/upload/v1788785479/c2wlogo.jpg";

    public AboutUsPage(String residentEmail) {
        this.residentEmail = residentEmail == null ? "" : residentEmail.trim();
    }

    public Scene getAboutUsScene(Stage stage, String residentEmail) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + LIGHT_BG + ";");

       
        panel sidebarPanel = new panel(stage, residentEmail);
        root.setLeft(sidebarPanel.getSidebar());

        VBox mainContent = new VBox();
        mainContent.setFillWidth(true);

        HBox header = createHeader(stage);

        VBox content = new VBox(25);
        content.setPadding(new Insets(25, 35, 40, 35));
        content.setMaxWidth(1250);

        StackPane contentWrapper = new StackPane(content);
        contentWrapper.setAlignment(Pos.TOP_CENTER);
        contentWrapper.setStyle("-fx-background-color: " + LIGHT_BG + ";");

        ScrollPane scrollPane = new ScrollPane(contentWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;");

        content.getChildren().addAll(
                createBackButton(stage),
                createHeroSection(),
                createMissionVisionSection(),
                createWhatWeDoSection(),
                createServicesSection(),
                createWhySociety360Section(),
                createCore2WebSection(),
                createRespectedSirSection(),
                createInstructorsSection(),
                createSuperMentorsSection(),
                createMentorsSection(),
                createTeamLeadSection(),
                createOurTeamSection(),
                createClosingSection(),
                createFooter()
        );

        mainContent.getChildren().addAll(header, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.setCenter(mainContent);

        return new Scene(
                root,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }

    // =========================================================
    // HEADER 
    // =========================================================
    private HBox createHeader(Stage stage) {

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setSpacing(15);
        header.setStyle("-fx-background-color: " + DARK_BROWN + ";");

        Label title = new Label("About Society360");
        title.setFont(Font.font("System", FontWeight.BOLD, 22));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Smart Society Management");
        subtitle.setFont(Font.font("System", 12));
        subtitle.setTextFill(Color.web("#E6D8D2"));

        VBox titleBox = new VBox(2);
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label pageLabel = new Label("ABOUT US");
        pageLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        pageLabel.setTextFill(Color.WHITE);

        header.getChildren().addAll(titleBox, spacer, pageLabel);
        return header;
    }

    // =========================================================
    // BACK BUTTON
    // =========================================================
    private HBox createBackButton(Stage stage) {

        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);

        Button back = new Button("←  Back to Dashboard");
        back.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + BROWN + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 5 0 5 0;"
        );

        back.setOnAction(event -> {
            ResidentDashboard dashboard = new ResidentDashboard();
            stage.setScene(dashboard.getResidentDashboardScene(stage));
            stage.show();
        });

        box.getChildren().add(back);
        return box;
    }

    // =========================================================
    // HERO
    // =========================================================
    private VBox createHeroSection() {

        VBox section = new VBox();
        section.setFillWidth(true);

        HBox hero = new HBox(25);
        hero.setAlignment(Pos.CENTER);
        hero.setFillHeight(true);
        hero.setPadding(new Insets(28));
        hero.setMaxWidth(Double.MAX_VALUE);
        hero.setMinHeight(300);
        hero.setPrefHeight(320);
        hero.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 18;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 18;"
        );

        VBox text = new VBox(10);
        text.setAlignment(Pos.CENTER_LEFT);
        text.setMinWidth(0);
        text.setPrefWidth(390);
        text.setMaxWidth(520);
        HBox.setHgrow(text, Priority.ALWAYS);

        Label small = new Label("WELCOME TO SOCIETY360");
        small.setFont(Font.font("System", FontWeight.BOLD, 12));
        small.setTextFill(Color.web(ACCENT));

        Label title = new Label("About Us");
        title.setFont(Font.font("System", FontWeight.BOLD, 38));
        title.setTextFill(Color.web(TEXT));

        Label tagline = new Label("Empowering Communities, Connecting People.");
        tagline.setWrapText(true);
        tagline.setFont(Font.font("System", FontWeight.BOLD, 18));
        tagline.setTextFill(Color.web(BROWN));

        Label description = new Label(
                "Society360 is a smart residential society management platform " +
                "designed to bring residents, security, management and society " +
                "services together in one connected ecosystem."
        );
        description.setWrapText(true);
        description.setMaxWidth(500);
        description.setFont(Font.font("System", 13));
        description.setTextFill(Color.web(MUTED));
        description.setLineSpacing(4);

        text.getChildren().addAll(small, title, tagline, description);

        StackPane illustration = createHeroImage();
        illustration.setMinWidth(260);
        illustration.setPrefWidth(300);
        illustration.setMaxWidth(320);
        illustration.setMinHeight(250);
        illustration.setPrefHeight(260);
        illustration.setMaxHeight(280);
        HBox.setHgrow(illustration, Priority.NEVER);

        hero.getChildren().addAll(text, illustration);
        section.getChildren().add(hero);
        return section;
    }

    // =========================================================
    // HERO IMAGE - CLOUDINARY
    // =========================================================
    private StackPane createHeroImage() {

        StackPane pane = new StackPane();
        pane.setAlignment(Pos.CENTER);
        pane.setMinSize(260, 250);
        pane.setPrefSize(300, 260);
        pane.setMaxSize(320, 280);
        pane.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 14;"
        );

        if (HERO_IMAGE_URL != null && !HERO_IMAGE_URL.trim().isEmpty()) {
            try {
                Image image = new Image(
                        HERO_IMAGE_URL.trim(),
                        240,
                        240,
                        true,
                        true
                );

                if (!image.isError()) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(240);
                    imageView.setFitHeight(240);
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);

                    pane.getChildren().add(imageView);
                    return pane;
                }

                System.out.println(
                        "Society360 hero image could not be loaded from Cloudinary: " +
                        image.getException()
                );

            } catch (Exception e) {
                System.out.println(
                        "Unable to load Society360 hero image from Cloudinary: " +
                        e.getMessage()
                );
            }
        }

        Label placeholder = new Label("Society360");
        placeholder.setFont(Font.font("System", FontWeight.BOLD, 28));
        placeholder.setTextFill(Color.web(BROWN));
        pane.getChildren().add(placeholder);
        return pane;
    }

    // =========================================================
    // MISSION / VISION
    // =========================================================
    private HBox createMissionVisionSection() {

        HBox section = new HBox(18);

        VBox mission = createLargeCard(
                "01", "Our Mission",
                "To simplify everyday society management by providing residents " +
                "and management with a reliable, transparent and easy-to-use digital platform."
        );

        VBox vision = createLargeCard(
                "02", "Our Vision",
                "To create smarter, safer and more connected residential communities " +
                "where technology makes society living easier for everyone."
        );

        HBox.setHgrow(mission, Priority.ALWAYS);
        HBox.setHgrow(vision, Priority.ALWAYS);
        section.getChildren().addAll(mission, vision);
        return section;
    }

    private VBox createLargeCard(String number, String title, String description) {

        VBox card = new VBox(12);
        card.setPadding(new Insets(22));
        card.setMinHeight(185);
        card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 15;"
        );

        Label numberLabel = new Label(number);
        numberLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        numberLabel.setTextFill(Color.web(ACCENT));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web(TEXT));

        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setFont(Font.font("System", 12));
        descriptionLabel.setTextFill(Color.web(MUTED));
        descriptionLabel.setLineSpacing(3);

        card.getChildren().addAll(numberLabel, titleLabel, descriptionLabel);
        return card;
    }

    // =========================================================
    // WHAT WE DO
    // =========================================================
    private VBox createWhatWeDoSection() {

        VBox section = new VBox(12);
        Label title = createSectionTitle("What We Do");
        Label subtitle = createSectionSubtitle(
                "One connected platform for everyday residential society operations."
        );

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        VBox residents = createFeatureCard("R", "Residents",
                "Easy access to society information, services and communication.");
        VBox management = createFeatureCard("M", "Management",
                "Organize society operations, notices, maintenance and requests.");
        VBox security = createFeatureCard("S", "Security",
                "Manage visitors and improve residential security.");
        VBox services = createFeatureCard("L", "Local Services",
                "Connect residents with useful services and opportunities.");

        grid.add(residents, 0, 0);
        grid.add(management, 1, 0);
        grid.add(security, 0, 1);
        grid.add(services, 1, 1);

        GridPane.setHgrow(residents, Priority.ALWAYS);
        GridPane.setHgrow(management, Priority.ALWAYS);
        GridPane.setHgrow(security, Priority.ALWAYS);
        GridPane.setHgrow(services, Priority.ALWAYS);

        section.getChildren().addAll(title, subtitle, grid);
        return section;
    }

    private VBox createFeatureCard(String icon, String title, String description) {

        VBox card = new VBox(8);
        card.setPadding(new Insets(18));
        card.setMinHeight(130);
        card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 12;"
        );

        Label iconLabel = new Label(icon);
        iconLabel.setAlignment(Pos.CENTER);
        iconLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        iconLabel.setTextFill(Color.WHITE);
        iconLabel.setStyle(
                "-fx-background-color: " + BROWN + ";" +
                "-fx-background-radius: 8;" +
                "-fx-min-width: 32;" +
                "-fx-min-height: 32;" +
                "-fx-max-width: 32;" +
                "-fx-max-height: 32;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web(TEXT));

        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setFont(Font.font("System", 11));
        descriptionLabel.setTextFill(Color.web(MUTED));

        card.getChildren().addAll(iconLabel, titleLabel, descriptionLabel);
        return card;
    }

    // =========================================================
    // SERVICES
    // =========================================================
    private VBox createServicesSection() {

        VBox section = new VBox(12);
        Label title = createSectionTitle("Our Services");
        Label subtitle = createSectionSubtitle(
                "Society360 brings essential society services together in one place."
        );

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        String[][] serviceData = {
                {"01", "Maintenance", "Manage maintenance payments and society dues."},
                {"02", "Visitor Management", "Track and manage visitor entries efficiently."},
                {"03", "Amenities", "Book society amenities with ease."},
                {"04", "Complaints", "Raise and track society complaints."},
                {"05", "Notices", "Stay updated with important society announcements."},
                {"06", "Emergency", "Quick access to emergency support and information."},
                {"07", "Documents", "Access important society documents."},
                {"08", "Community", "Stay connected with your society community."}
        };

        for (int i = 0; i < serviceData.length; i++) {
            VBox card = createServiceCard(
                    serviceData[i][0], serviceData[i][1], serviceData[i][2]
            );
            int column = i % 4;
            int row = i / 4;
            grid.add(card, column, row);
            GridPane.setHgrow(card, Priority.ALWAYS);
        }

        section.getChildren().addAll(title, subtitle, grid);
        return section;
    }

    private VBox createServiceCard(String number, String title, String description) {

        VBox card = new VBox(7);
        card.setPadding(new Insets(16));
        card.setMinHeight(120);
        card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 12;"
        );

        Label numberLabel = new Label(number);
        numberLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        numberLabel.setTextFill(Color.web(ACCENT));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        titleLabel.setTextFill(Color.web(TEXT));

        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setFont(Font.font("System", 10));
        descriptionLabel.setTextFill(Color.web(MUTED));

        card.getChildren().addAll(numberLabel, titleLabel, descriptionLabel);
        return card;
    }

    // =========================================================
    // WHY SOCIETY360
    // =========================================================
    private VBox createWhySociety360Section() {

        VBox section = new VBox(15);
        section.setPadding(new Insets(5, 0, 5, 0));

        Label title = createSectionTitle("Why Society360?");
        Label subtitle = createSectionSubtitle(
                "Designed around the people who make a community work."
        );

        HBox benefits = new HBox(15);
        benefits.setAlignment(Pos.CENTER);

        VBox secure = createBenefit("01", "Secure",
                "Designed with secure society operations in mind.");
        VBox simple = createBenefit("02", "Simple",
                "Clean and straightforward user experience.");
        VBox connected = createBenefit("03", "Connected",
                "Residents and society operations stay connected.");
        VBox transparent = createBenefit("04", "Transparent",
                "Better visibility into society activities.");

        benefits.getChildren().addAll(secure, simple, connected, transparent);

        HBox.setHgrow(secure, Priority.ALWAYS);
        HBox.setHgrow(simple, Priority.ALWAYS);
        HBox.setHgrow(connected, Priority.ALWAYS);
        HBox.setHgrow(transparent, Priority.ALWAYS);

        section.getChildren().addAll(title, subtitle, benefits);
        return section;
    }

    private VBox createBenefit(String number, String title, String description) {

        VBox card = new VBox(7);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(18));
        card.setMinHeight(145);
        card.setStyle(
                "-fx-background-color: " + LIGHT_ACCENT + ";" +
                "-fx-background-radius: 13;"
        );

        Label numberLabel = new Label(number);
        numberLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        numberLabel.setTextFill(Color.web(ACCENT));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        titleLabel.setTextFill(Color.web(TEXT));

        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setAlignment(Pos.CENTER);
        descriptionLabel.setTextAlignment(TextAlignment.CENTER);
        descriptionLabel.setFont(Font.font("System", 10));
        descriptionLabel.setTextFill(Color.web(MUTED));

        card.getChildren().addAll(numberLabel, titleLabel, descriptionLabel);
        return card;
    }

    // =========================================================
    // CORE2WEB
    // =========================================================
    private VBox createCore2WebSection() {

        VBox section = createSimpleSectionCard();
        section.setAlignment(Pos.CENTER);

        // Keep the Core2Web logo beside the text, matching the reference layout.
        HBox contentRow = new HBox(28);
        contentRow.setAlignment(Pos.CENTER);
        contentRow.setPadding(new Insets(10, 20, 10, 20));
        contentRow.setMaxWidth(1000);

        StackPane logo = createCore2WebLogo();

        VBox textBox = new VBox(8);
        textBox.setAlignment(Pos.CENTER_LEFT);
        textBox.setMaxWidth(700);

        Label title = createSectionTitle("A Special Thanks to Core2Web");
        title.setAlignment(Pos.CENTER_LEFT);
        title.setTextAlignment(TextAlignment.LEFT);

        Label subtitle = createSectionSubtitle(
                "Our Learning Partner & Technical Foundation"
        );
        subtitle.setAlignment(Pos.CENTER_LEFT);
        subtitle.setTextAlignment(TextAlignment.LEFT);

        Label text = new Label(
                "We are grateful to Core2Web for providing us with the learning " +
                "platform, technical guidance and foundation that helped us develop " +
                "our skills and complete this project."
        );
        text.setWrapText(true);
        text.setFont(Font.font("System", 12));
        text.setTextFill(Color.web(MUTED));
        text.setLineSpacing(3);
        text.setMaxWidth(700);
        text.setAlignment(Pos.CENTER_LEFT);
        text.setTextAlignment(TextAlignment.LEFT);

        Label thanks = new Label(
                "Thank you for helping us learn, grow and turn our ideas into a working solution."
        );
        thanks.setWrapText(true);
        thanks.setFont(Font.font("System", FontWeight.BOLD, 11));
        thanks.setTextFill(Color.web(TEXT));
        thanks.setAlignment(Pos.CENTER_LEFT);
        thanks.setTextAlignment(TextAlignment.LEFT);
        thanks.setMaxWidth(700);

        textBox.getChildren().addAll(title, subtitle, text, thanks);
        contentRow.getChildren().addAll(logo, textBox);

        section.getChildren().add(contentRow);
        return section;
    }

    private StackPane createCore2WebLogo() {

        StackPane container = new StackPane();
        container.setMinSize(135, 135);
        container.setPrefSize(135, 135);
        container.setMaxSize(135, 135);
        container.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 16;"
        );

        try {
            Image image = new Image(
                    CORE2WEB_LOGO_URL.trim(),
                    115,
                    115,
                    true,
                    true
            );

            if (!image.isError()) {
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(115);
                imageView.setFitHeight(115);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                container.getChildren().add(imageView);
                return container;
            }

            System.out.println(
                    "Core2Web logo could not be loaded from Cloudinary."
            );
        } catch (Exception e) {
            System.out.println(
                    "Unable to load Core2Web logo from Cloudinary: " + e.getMessage()
            );
        }

        Label fallback = new Label("Core2Web");
        fallback.setFont(Font.font("System", FontWeight.BOLD, 16));
        fallback.setTextFill(Color.web(BROWN));
        container.getChildren().add(fallback);
        return container;
    }

    // =========================================================
    // RESPECTED SIR - ONLY INDIVIDUAL PHOTO ON PAGE
    // =========================================================
    private VBox createRespectedSirSection() {

        VBox section = new VBox(12);
        Label title = createSectionTitle("A Special Thanks to Our Respected Sir");

        HBox card = new HBox(25);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20, 22, 20, 22));
        card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 15;"
        );

        StackPane photo = createCloudinaryImage(
                SHASHI_SIR_IMAGE_URL,
                140,
                "S"
        );

        VBox textBox = new VBox(7);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label name = new Label("Shashi Bagal");
        name.setFont(Font.font("System", FontWeight.BOLD, 18));
        name.setTextFill(Color.web(BROWN));

        Label role = new Label("Respected Sir");
        role.setFont(Font.font("System", FontWeight.BOLD, 11));
        role.setTextFill(Color.web(MUTED));

        Label description = new Label(
                "We sincerely thank Shashi Sir for his valuable guidance, " +
                "encouragement and support. His teaching, motivation and direction " +
                "have helped us learn, improve our technical skills and approach " +
                "our project with confidence."
        );
        description.setWrapText(true);
        description.setMaxWidth(700);
        description.setLineSpacing(3);
        description.setFont(Font.font("System", 12));
        description.setTextFill(Color.web(MUTED));

        Label finalThanks = new Label(
                "Thank you, Sir, for being an important part of our learning journey."
        );
        finalThanks.setWrapText(true);
        finalThanks.setFont(Font.font("System", FontWeight.BOLD, 11));
        finalThanks.setTextFill(Color.web(TEXT));

        textBox.getChildren().addAll(name, role, description, finalThanks);
        card.getChildren().addAll(photo, textBox);
        section.getChildren().addAll(title, card);
        return section;
    }

    // =========================================================
    // INSTRUCTORS
    // =========================================================
    private VBox createInstructorsSection() {

        VBox section = new VBox(12);
        Label title = createSectionTitle("Our Instructors");
        Label subtitle = createSectionSubtitle(
                "The people who strengthened our technical foundation and learning."
        );

        HBox names = new HBox(15);
        names.setAlignment(Pos.CENTER);

        VBox sachin = createNameCard("Sachin Patil", "Instructor");
        VBox pramod = createNameCard("Pramod Bansode", "Instructor");

        names.getChildren().addAll(sachin, pramod);
        HBox.setHgrow(sachin, Priority.ALWAYS);
        HBox.setHgrow(pramod, Priority.ALWAYS);

        section.getChildren().addAll(title, subtitle, names);
        return section;
    }

    private VBox createNameCard(String name, String role) {

        VBox card = new VBox(7);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(18));
        card.setMinHeight(125);
        card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-background-radius: 13;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 13;"
        );

        Circle iconCircle = new Circle(28, Color.web(LIGHT_ACCENT));

        String initialText =
                name == null || name.trim().isEmpty()
                        ? "?"
                        : name.trim().substring(0, 1).toUpperCase();

        Label initial = new Label(initialText);
        initial.setFont(Font.font("System", FontWeight.BOLD, 15));
        initial.setTextFill(Color.web(BROWN));

        StackPane icon = new StackPane(iconCircle, initial);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.web(TEXT));

        Label roleLabel = new Label(role);
        roleLabel.setFont(Font.font("System", 10));
        roleLabel.setTextFill(Color.web(MUTED));

        card.getChildren().addAll(
                icon,
                nameLabel,
                roleLabel
        );

        return card;
    }

// =========================================================
    // SUPER MENTORS
    // =========================================================
    private VBox createSuperMentorsSection() {

        VBox section = new VBox(12);
        Label title = createSectionTitle("Super Mentors");
        Label subtitle = createSectionSubtitle(
                "Their guidance and support helped us move forward with confidence."
        );

        HBox names = new HBox(15);
        names.setAlignment(Pos.CENTER);

        VBox shiv = createNameCard("Shiv Sir", "Super Mentor");
        VBox subodh = createNameCard("Subodh Sir", "Super Mentor");

        names.getChildren().addAll(shiv, subodh);
        HBox.setHgrow(shiv, Priority.ALWAYS);
        HBox.setHgrow(subodh, Priority.ALWAYS);

        section.getChildren().addAll(title, subtitle, names);
        return section;
    }

    // =========================================================
    // MENTORS
    // =========================================================
    private VBox createMentorsSection() {

        VBox section = new VBox(12);
        Label title = createSectionTitle("Mentors");
        Label subtitle = createSectionSubtitle(
                "Thank you for your continuous guidance, support and encouragement."
        );

        HBox names = new HBox(15);
        names.setAlignment(Pos.CENTER);

        VBox manasi = createNameCard("Manasi", "Mentor");
        VBox sumit = createNameCard("Sumit", "Mentor");
        VBox dhanashree = createNameCard("Dhanashree", "Mentor");

        names.getChildren().addAll(manasi, sumit, dhanashree);
        HBox.setHgrow(manasi, Priority.ALWAYS);
        HBox.setHgrow(sumit, Priority.ALWAYS);
        HBox.setHgrow(dhanashree, Priority.ALWAYS);

        section.getChildren().addAll(title, subtitle, names);
        return section;
    }

    // =========================================================
    // TEAM LEAD
    // =========================================================
    private VBox createTeamLeadSection() {

        VBox section = new VBox(12);
        Label title = createSectionTitle("Our Team Lead & Mentor");

        VBox card = createNameCard(
                "Shravani Khatate",
                "Team Lead & Mentor"
        );
        card.setMinHeight(100);

        section.getChildren().addAll(title, card);
        return section;
    }

    // =========================================================
    // OUR TEAM - ONE TEAM PHOTO + ALL NAMES IN ONE LINE
    // =========================================================
    private VBox createOurTeamSection() {

        VBox section = new VBox(12);

        Label title = createSectionTitle("Meet Our Team");

        Label subtitle = createSectionSubtitle(
                "The team behind Society360 — learning, building and growing together."
        );

        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 15;"
        );

        StackPane teamPhoto = createTeamPhoto();

        Label teamRole = new Label("Project Team");
        teamRole.setFont(Font.font("System", FontWeight.BOLD, 11));
        teamRole.setTextFill(Color.web(MUTED));

        HBox names = new HBox(10);
        names.setAlignment(Pos.CENTER);

        String[] teamMembers = {
                "Aditi Hegde",
                "Diya Wadhwa",
                "Vaishnavi Kapse",
                "Shrutika Gaikwad",
                "Snehal Sarode"
        };

        for (String teamMember : teamMembers) {

            Label member = new Label(teamMember);

            member.setAlignment(Pos.CENTER);
            member.setFont(
                    Font.font(
                            "System",
                            FontWeight.BOLD,
                            11
                    )
            );

            member.setTextFill(Color.web(TEXT));

            member.setPadding(
                    new Insets(9, 12, 9, 12)
            );

            member.setStyle(
                    "-fx-background-color: " + LIGHT_ACCENT + ";" +
                    "-fx-background-radius: 10;"
            );

            names.getChildren().add(member);
        }

        card.getChildren().addAll(
                teamPhoto,
                teamRole,
                names
        );

        section.getChildren().addAll(
                title,
                subtitle,
                card
        );

        return section;
    }

    private StackPane createTeamPhoto() {

        StackPane container = new StackPane();

        container.setMinSize(620, 230);
        container.setPrefSize(620, 230);
        container.setMaxSize(620, 230);

        container.setStyle(
                "-fx-background-color: " + LIGHT_ACCENT + ";" +
                "-fx-background-radius: 14;"
        );

        if (TEAM_IMAGE_URL != null &&
                !TEAM_IMAGE_URL.trim().isEmpty()) {

            try {

                Image image = new Image(
                        TEAM_IMAGE_URL.trim(),
                        620,
                        230,
                        true,
                        true
                );

                if (!image.isError()) {

                    ImageView imageView = new ImageView(image);

                    imageView.setFitWidth(620);
                    imageView.setFitHeight(230);
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);

                    Rectangle clip = new Rectangle(620, 230);
                    clip.setArcWidth(28);
                    clip.setArcHeight(28);
                    imageView.setClip(clip);

                    container.getChildren().add(imageView);

                    return container;
                }

            } catch (Exception e) {

                System.out.println(
                        "Unable to load team image from Cloudinary: "
                                + e.getMessage()
                );
            }
        }

        Label placeholder = new Label("Society360 Team");

        placeholder.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        22
                )
        );

        placeholder.setTextFill(Color.web(BROWN));

        container.getChildren().add(placeholder);

        return container;
    }

    // =========================================================
    // CLOUDINARY IMAGE 
    // =========================================================
    private StackPane createCloudinaryImage(
            String imageUrl,
            double size,
            String fallbackInitial
    ) {

        StackPane container = new StackPane();
        container.setMinSize(size, size);
        container.setPrefSize(size, size);
        container.setMaxSize(size, size);

        Circle background = new Circle(size / 2, Color.web(LIGHT_ACCENT));
        container.getChildren().add(background);

        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            try {
                Image image = new Image(
                        imageUrl.trim(), size, size, true, true, true
                );

                if (!image.isError()) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(size);
                    imageView.setFitHeight(size);
                    imageView.setPreserveRatio(false);
                    imageView.setSmooth(true);

                    Circle clip = new Circle(size / 2, size / 2, size / 2);
                    imageView.setClip(clip);

                    container.getChildren().add(imageView);
                    return container;
                }
            } catch (Exception e) {
                System.out.println(
                        "Unable to load Shashi Sir's image from Cloudinary: " + e.getMessage()
                );
            }
        }

        Label initial = new Label(fallbackInitial);
        initial.setFont(Font.font("System", FontWeight.BOLD, 34));
        initial.setTextFill(Color.WHITE);
        initial.setStyle(
                "-fx-background-color: " + BROWN + ";" +
                "-fx-background-radius: 100;" +
                "-fx-min-width: " + size + ";" +
                "-fx-min-height: " + size + ";" +
                "-fx-max-width: " + size + ";" +
                "-fx-max-height: " + size + ";" +
                "-fx-alignment: CENTER;"
        );

        container.getChildren().add(initial);
        return container;
    }

    // =========================================================
    // CLOSING
    // =========================================================
    private VBox createClosingSection() {

        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25, 20, 25, 20));
        box.setStyle(
                "-fx-background-color: " + BROWN + ";" +
                "-fx-background-radius: 16;"
        );

        Label quote = new Label("\"Together, we build better communities.\"");
        quote.setFont(Font.font("System", FontWeight.BOLD, 20));
        quote.setTextFill(Color.WHITE);

        Label text = new Label("Society360 — Smart Society Management");
        text.setFont(Font.font("System", 11));
        text.setTextFill(Color.web("#E7DAD5"));

        box.getChildren().addAll(quote, text);
        return box;
    }

    // =========================================================
    // FOOTER
    // =========================================================
    private VBox createFooter() {

        VBox footer = new VBox(5);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10, 0, 5, 0));

        Label copyright = new Label("© 2026 Society360. All Rights Reserved.");
        copyright.setFont(Font.font("System", 10));
        copyright.setTextFill(Color.web(MUTED));

        Label contact = new Label("Smart Society Management");
        contact.setFont(Font.font("System", 10));
        contact.setTextFill(Color.web(MUTED));

        footer.getChildren().addAll(copyright, contact);
        return footer;
    }

    // =========================================================
    // SECTION TITLE
    // =========================================================
    private Label createSectionTitle(String text) {

        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 22));
        label.setTextFill(Color.web(TEXT));
        return label;
    }

    // =========================================================
    // SECTION SUBTITLE
    // =========================================================
    private Label createSectionSubtitle(String text) {

        Label label = new Label(text);
        label.setFont(Font.font("System", 12));
        label.setTextFill(Color.web(MUTED));
        label.setWrapText(true);
        return label;
    }

    // =========================================================
    // SIMPLE CARD CONTAINER
    // =========================================================
    private VBox createSimpleSectionCard() {

        VBox section = new VBox(8);
        section.setPadding(new Insets(20, 22, 20, 22));
        section.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 15;"
        );
        return section;
    }
}
