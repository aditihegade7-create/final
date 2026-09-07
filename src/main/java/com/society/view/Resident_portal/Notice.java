// package com.society.view.Resident_portal;

// import com.society.controller.Resident_Controller.NoticeController;
// import com.society.model.Resident_model.NoticeModel;
// import com.society.view.ScreenSize;

// import javafx.application.Platform;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.ComboBox;
// import javafx.scene.control.Label;
// import javafx.scene.control.ScrollPane;
// import javafx.scene.control.TextField;
// import javafx.scene.image.Image;
// import javafx.scene.layout.Background;
// import javafx.scene.layout.BackgroundImage;
// import javafx.scene.layout.BackgroundPosition;
// import javafx.scene.layout.BackgroundRepeat;
// import javafx.scene.layout.BackgroundSize;
// import javafx.scene.layout.BorderPane;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.Priority;
// import javafx.scene.layout.VBox;
// import javafx.scene.paint.Color;
// import javafx.scene.text.Font;
// import javafx.scene.text.FontWeight;
// import javafx.stage.Stage;

// import java.util.ArrayList;
// import java.util.List;

// public class Notice {

//     // =========================================================
//     // ALL NOTICES
//     // =========================================================

//     private List<NoticeModel> allNotices =
//             new ArrayList<>();

//     // =========================================================
//     // NOTICE CONTROLLER
//     // =========================================================

//     private final NoticeController noticeController;

//     // =========================================================
//     // CONSTRUCTOR
//     // =========================================================

//     public Notice(String residentEmail) {

//         noticeController =
//                 new NoticeController();
//     }

//     // =========================================================
//     // SCENE
//     // =========================================================

//     public Scene getResidentbtScene(
//             Stage stage,
//             String residentEmail) {

//         // =====================================================
//         // ROOT
//         // =====================================================

//         BorderPane root =new BorderPane();

//         //         Image backgroundImage = new Image(
//         //         getClass()
//         //                 .getResource("/background-Dashboard5.jpeg")
//         //                 .toExternalForm()
//         // );

//         // BackgroundSize backgroundSize = new BackgroundSize(
//         //         100,
//         //         100,
//         //         true,
//         //         true,
//         //         false,
//         //         true
//         // );

//         // BackgroundImage bgImage = new BackgroundImage(
//         //         backgroundImage,
//         //         BackgroundRepeat.NO_REPEAT,
//         //         BackgroundRepeat.NO_REPEAT,
//         //         BackgroundPosition.CENTER,
//         //         backgroundSize
//         // );

//         // root.setBackground(
//         //         new Background(bgImage)
//         // );

//         // =====================================================
//         // SIDEBAR
//         // =====================================================

//         panel panelobj = new panel(stage,residentEmail );

//         root.setLeft(panelobj.getSidebar());

//         // =====================================================
//         // MAIN CONTENT
//         // =====================================================

      

//         // =====================================================
//         // HEADING
//         // =====================================================

//          BorderPane mainArea = new BorderPane();

//          VBox heading =new VBox(3);

//         heading.setPadding(
//                 new Insets(15,20, 15, 20)
//         );

//         heading.setStyle(
//                 "-fx-background-color: #4e342e;"
//         );

//         Label title =new Label("Notices");

//         title.setFont(
//                 Font.font(
//                         "System",
//                         FontWeight.BOLD,
//                         28
//                 )
//         );

//         title.setTextFill(
//                 Color.WHITE
//         );

//         Label subtitle =new Label(
//                         "Important announcements from society management"
//                 );

//         subtitle.setFont(
//                 Font.font(
//                         "System",
//                         14
//                 )
//         );

//         subtitle.setTextFill(Color.web("#ffffff"));
//         //subtitle.setTextFill(Color.web( #4e342e));

       

//         heading.getChildren().addAll(
//                 title,
//                 subtitle
//         );

//          mainArea.setTop(heading);

//          VBox mainContent =new VBox(20);

//         mainContent.setPadding(new Insets(27, 38, 30, 38)
//         );

//         mainContent.setStyle(
//                 "-fx-background-color: #e8ddd5;"
// );
        

//         // =====================================================
//         // FILTER
//         // =====================================================

//         HBox filterBox =
//                 new HBox(10);

//         filterBox.setAlignment(
//                 Pos.CENTER_LEFT
//         );

//         ComboBox<String> category =
//                 new ComboBox<>();

//         category.setPromptText(
//                 "Category"
//         );

//         category.getItems().addAll(
//                 "All",
//                 "Maintenance",
//                 "Security",
//                 "Events",
//                 "General"
//         );

//         category.setPrefWidth(
//                 180
//         );

//         TextField search =new TextField();

//         search.setPromptText(
//                 "Search notices..."
//         );

//         search.setPrefWidth(
//                 250
//         );

//         filterBox.getChildren().addAll(
//                 category,
//                 search
//         );

//         // =====================================================
//         // NOTICE CONTAINER
//         // =====================================================

//         VBox noticeContainer =
//                 new VBox(15);

//         noticeContainer.setPadding(
//                 new Insets(10)
//         );

//         noticeContainer.setFillWidth(
//                 true
//         );

//         noticeContainer.setStyle(
//                 "-fx-background-color: #e8ddd5;"
//         );

//         // =====================================================
//         // SCROLL PANE
//         // =====================================================

//         ScrollPane scrollPane =
//                 new ScrollPane();

//         scrollPane.setContent(
//                 noticeContainer
//         );

//         scrollPane.setFitToWidth(
//                 true
//         );

//         scrollPane.setFitToHeight(
//                 false
//         );

//         scrollPane.setHbarPolicy(
//                 ScrollPane.ScrollBarPolicy.NEVER
//         );

//         scrollPane.setVbarPolicy(
//                 ScrollPane.ScrollBarPolicy.AS_NEEDED
//         );

//         scrollPane.setMaxWidth(
//                 Double.MAX_VALUE
//         );

//         scrollPane.setMaxHeight(
//                 Double.MAX_VALUE
//         );

//         scrollPane.setStyle(
//                 "-fx-background-color: #e8ddd5;"
//         );

//         // =====================================================
//         // VERTICAL GROW
//         // =====================================================

//         VBox.setVgrow(
//                 scrollPane,
//                 Priority.ALWAYS
//         );

//         // =====================================================
//         // ADD TO MAIN CONTENT
//         // =====================================================

//         mainContent.getChildren().addAll(
//                // heading,
//                 filterBox,
//                 scrollPane
//         );

//         // =====================================================
//         // CENTER
//         // =====================================================

//         root.setCenter(
//                 mainArea
//         );

//         // =====================================================
//         // FETCH NOTICES
//         // =====================================================

//         fetchNotices(
//                 noticeContainer,
//                 residentEmail
//         );

//         // =====================================================
//         // SCENE
//         // =====================================================

//         Scene scene =
//                 new Scene(
//                         root,
//                         ScreenSize.getWidth(),
//                         ScreenSize.getHeight()
//                 );

//         return scene;
//     }

//     // =========================================================
//     // FETCH NOTICES
//     // =========================================================

//     private void fetchNotices(
//             VBox noticeContainer,
//             String residentEmail) {

//         Thread thread =
//                 new Thread(() -> {

//                     try {

//                         // =================================================
//                         // START LOG
//                         // =================================================

//                         System.out.println(
//                                 "========================================"
//                         );

//                         System.out.println(
//                                 "RESIDENT NOTICE FETCH STARTED"
//                         );

//                         System.out.println(
//                                 "Logged-in Resident Email : "
//                                         + residentEmail
//                         );

//                         System.out.println(
//                                 "========================================"
//                         );

//                         // =================================================
//                         // VALIDATE RESIDENT EMAIL
//                         // =================================================

//                         if (residentEmail == null
//                                 || residentEmail.trim().isEmpty()) {

//                             throw new IllegalArgumentException(
//                                     "Resident email is empty."
//                             );
//                         }

//                         String email =
//                                 residentEmail.trim();

//                         // =================================================
//                         // FETCH THROUGH CONTROLLER
//                         // =================================================

//                         List<NoticeModel> fetchedNotices =
//                                 noticeController
//                                         .getNoticesForResident(
//                                                 email
//                                         );

//                         // =================================================
//                         // LOG RESULT
//                         // =================================================

//                         System.out.println(
//                                 "========================================"
//                         );

//                         System.out.println(
//                                 "TOTAL NOTICES FETCHED : "
//                                         + fetchedNotices.size()
//                         );

//                         System.out.println(
//                                 "========================================"
//                         );

//                         // =================================================
//                         // JAVAFX APPLICATION THREAD
//                         // =================================================

//                         Platform.runLater(() -> {

//                             System.out.println(
//                                     "Updating JavaFX Notice UI..."
//                             );

//                             // =============================================
//                             // CLEAR OLD DATA
//                             // =============================================

//                             allNotices.clear();

//                             // =============================================
//                             // ADD NEW DATA
//                             // =============================================

//                             allNotices.addAll(
//                                     fetchedNotices
//                             );

//                             // =============================================
//                             // CLEAR CONTAINER
//                             // =============================================

//                             noticeContainer
//                                     .getChildren()
//                                     .clear();

//                             // =============================================
//                             // NO NOTICES
//                             // =============================================

//                             if (fetchedNotices.isEmpty()) {

//                                 Label noNotice =
//                                         new Label(
//                                                 "No notices available"
//                                         );

//                                 noNotice.setFont(
//                                         Font.font(
//                                                 "System",
//                                                 15
//                                         )
//                                 );

//                                 noNotice.setTextFill(
//                                         Color.web(
//                                                 "#607D8B"
//                                         )
//                                 );

//                                 noticeContainer
//                                         .getChildren()
//                                         .add(
//                                                 noNotice
//                                         );

//                             } else {

//                                 // =========================================
//                                 // CREATE NOTICE BOXES
//                                 // =========================================

//                                 for (
//                                         NoticeModel notice :
//                                         fetchedNotices
//                                 ) {

//                                     VBox box =
//                                             createNoticeBox(
//                                                     notice
//                                             );

//                                     noticeContainer
//                                             .getChildren()
//                                             .add(
//                                                     box
//                                             );
//                                 }
//                             }

//                             // =============================================
//                             // LOG UI COUNT
//                             // =============================================

//                             System.out.println(
//                                     "Notice boxes added = "
//                                             + noticeContainer
//                                             .getChildren()
//                                             .size()
//                             );

//                             // =============================================
//                             // FORCE LAYOUT
//                             // =============================================

//                             noticeContainer.applyCss();

//                             noticeContainer.layout();
//                         });

//                     } catch (Exception e) {

//                         // =================================================
//                         // ERROR LOG
//                         // =================================================

//                         System.out.println(
//                                 "========================================"
//                         );

//                         System.out.println(
//                                 "ERROR WHILE FETCHING NOTICES"
//                         );

//                         System.out.println(
//                                 "========================================"
//                         );

//                         e.printStackTrace();

//                         // =================================================
//                         // SHOW ERROR IN JAVAFX
//                         // =================================================

//                         Platform.runLater(() -> {

//                             noticeContainer
//                                     .getChildren()
//                                     .clear();

//                             Label error =
//                                     new Label(
//                                             "Error loading notices"
//                                     );

//                             error.setTextFill(
//                                     Color.RED
//                             );

//                             noticeContainer
//                                     .getChildren()
//                                     .add(
//                                             error
//                                     );
//                         });
//                     }
//                 });

//         // =========================================================
//         // DAEMON THREAD
//         // =========================================================

//         thread.setDaemon(true);

//         thread.start();
//     }

//     // =========================================================
//     // CREATE NOTICE BOX
//     // =========================================================

//     private VBox createNoticeBox(
//             NoticeModel notice) {

//         VBox box =
//                 new VBox(12);

//         box.setPadding(
//                 new Insets(20)
//         );

//         box.setMinHeight(
//                 130
//         );

//         box.setMaxWidth(
//                 Double.MAX_VALUE
//         );

//         box.setStyle(
//                 "-fx-background-color: white;" +
//                 "-fx-background-radius: 10;" +
//                 "-fx-border-color: #d0d0d0;" +
//                 "-fx-border-radius: 10;"
//         );

//         // =====================================================
//         // TITLE
//         // =====================================================

//         Label titleLabel =
//                 new Label(
//                         notice.getTitle() == null
//                                 ? ""
//                                 : notice.getTitle()
//                 );

//         titleLabel.setFont(
//                 Font.font(
//                         "System",
//                         FontWeight.BOLD,
//                         18
//                 )
//         );

//         titleLabel.setTextFill(
//                 Color.web("#263238")
//         );

//         // =====================================================
//         // STATUS
//         // =====================================================

//         Label statusLabel =
//                 new Label(
//                         notice.getStatus() == null
//                                 ? ""
//                                 : notice.getStatus()
//                 );

//         statusLabel.setFont(
//                 Font.font(
//                         "System",
//                         FontWeight.BOLD,
//                         12
//                 )
//         );

//         statusLabel.setTextFill(
//                 Color.WHITE
//         );

//         statusLabel.setPadding(
//                 new Insets(
//                         5,
//                         12,
//                         5,
//                         12
//                 )
//         );

//         statusLabel.setStyle(
//                 "-fx-background-color: #2e7d32;" +
//                 "-fx-background-radius: 15;"
//         );

//         // =====================================================
//         // TOP ROW
//         // =====================================================

//         HBox topRow =
//                 new HBox(10);

//         topRow.setAlignment(
//                 Pos.CENTER_LEFT
//         );

//         topRow.getChildren().addAll(
//                 titleLabel,
//                 statusLabel
//         );

//         // =====================================================
//         // DATE
//         // =====================================================

//         Label dateLabel =
//                 new Label(
//                         "Date • "
//                                 + (
//                                 notice.getDate() == null
//                                         ? ""
//                                         : notice.getDate()
//                         )
//                 );

//         dateLabel.setFont(
//                 Font.font(
//                         "System",
//                         13
//                 )
//         );

//         dateLabel.setTextFill(
//                 Color.web("#607D8B")
//         );

//         // =====================================================
//         // DESCRIPTION
//         // =====================================================

//         Label descriptionLabel =
//                 new Label(
//                         notice.getDescription() == null
//                                 ? ""
//                                 : notice.getDescription()
//                 );

//         descriptionLabel.setWrapText(
//                 true
//         );

//         descriptionLabel.setMaxWidth(
//                 Double.MAX_VALUE
//         );

//         descriptionLabel.setFont(
//                 Font.font(
//                         "System",
//                         14
//                 )
//         );

//         descriptionLabel.setTextFill(
//                 Color.web("#455A64")
//         );

//         // =====================================================
//         // ADD TO BOX
//         // =====================================================

//         box.getChildren().addAll(
//                 topRow,
//                 dateLabel,
//                 descriptionLabel
//         );

//         return box;
//     }
// }

package com.society.view.Resident_portal;

import com.society.controller.Resident_Controller.NoticeController;
import com.society.model.Resident_model.NoticeModel;
import com.society.view.ScreenSize;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Notice {

    // =========================================================
    // ALL NOTICES
    // =========================================================

    private List<NoticeModel> allNotices =
            new ArrayList<>();

    // =========================================================
    // NOTICE CONTROLLER
    // =========================================================

    private final NoticeController noticeController;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Notice(String residentEmail) {

        noticeController =
                new NoticeController();
    }

    // =========================================================
    // SCENE
    // =========================================================

    public Scene getResidentbtScene(
            Stage stage,
            String residentEmail) {

        // =====================================================
        // ROOT
        // =====================================================

        BorderPane root =
                new BorderPane();

        // =====================================================
        // SIDEBAR
        // =====================================================

        panel panelobj =
                new panel(
                        stage,
                        residentEmail
                );

        root.setLeft(
                panelobj.getSidebar()
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainContent =
                new VBox(20);

        mainContent.setPadding(
                new Insets(30)
        );

        mainContent.setStyle(
                "-fx-background-color: #e8ddd5;"
        );

        // =====================================================
        // HEADING
        // =====================================================

        Label title =
                new Label("Notices");

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        28
                )
        );

        title.setTextFill(
                Color.WHITE
        );

        Label subtitle =
                new Label(
                        "Important announcements from society management"
                );

        subtitle.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        subtitle.setTextFill(
                Color.WHITE
        );

        VBox heading =
                new VBox(5);

        heading.setPadding(
                new Insets(15)
        );

        heading.setMaxWidth(
                Double.MAX_VALUE
        );

        heading.setStyle(
                "-fx-background-color: #4e342e;"
        );

        heading.getChildren().addAll(
                title,
                subtitle
        );

        // =====================================================
        // FILTER
        // =====================================================

        HBox filterBox =
                new HBox(10);

        filterBox.setAlignment(
                Pos.CENTER_LEFT
        );

        ComboBox<String> category =
                new ComboBox<>();

        category.setPromptText(
                "Category"
        );

        category.getItems().addAll(
                "All",
                "Maintenance",
                "Security",
                "Events",
                "General"
        );

        category.setPrefWidth(
                180
        );

        TextField search =
                new TextField();

        search.setPromptText(
                "Search notices..."
        );

        search.setPrefWidth(
                250
        );

        filterBox.getChildren().addAll(
                category,
                search
        );

        // =====================================================
        // NOTICE CONTAINER
        // =====================================================

        VBox noticeContainer =
                new VBox(15);

        noticeContainer.setPadding(
                new Insets(10)
        );

        noticeContainer.setFillWidth(
                true
        );

        noticeContainer.setStyle(
                "-fx-background-color: #e8ddd5;"
        );

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                noticeContainer
        );

        scrollPane.setFitToWidth(
                true
        );

        scrollPane.setFitToHeight(
                false
        );

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setMaxWidth(
                Double.MAX_VALUE
        );

        scrollPane.setMaxHeight(
                Double.MAX_VALUE
        );

        scrollPane.setStyle(
                "-fx-background-color: #e8ddd5;"
        );

        // =====================================================
        // VERTICAL GROW
        // =====================================================

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        // =====================================================
        // ADD TO MAIN CONTENT
        // =====================================================

        mainContent.getChildren().addAll(
                heading,
                filterBox,
                scrollPane
        );

        // =====================================================
        // CENTER
        // =====================================================

        root.setCenter(
                mainContent
        );

        // =====================================================
        // FETCH NOTICES
        // =====================================================

        fetchNotices(
                noticeContainer,
                residentEmail
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

        return scene;
    }

    // =========================================================
    // FETCH NOTICES
    // =========================================================

    private void fetchNotices(
            VBox noticeContainer,
            String residentEmail) {

        Thread thread =
                new Thread(() -> {

                    try {

                        // =================================================
                        // START LOG
                        // =================================================

                        System.out.println(
                                "========================================"
                        );

                        System.out.println(
                                "RESIDENT NOTICE FETCH STARTED"
                        );

                        System.out.println(
                                "Logged-in Resident Email : "
                                        + residentEmail
                        );

                        System.out.println(
                                "========================================"
                        );

                        // =================================================
                        // VALIDATE RESIDENT EMAIL
                        // =================================================

                        if (residentEmail == null
                                || residentEmail.trim().isEmpty()) {

                            throw new IllegalArgumentException(
                                    "Resident email is empty."
                            );
                        }

                        String email =
                                residentEmail.trim();

                        // =================================================
                        // FETCH THROUGH CONTROLLER
                        // =================================================

                        List<NoticeModel> fetchedNotices =
                                noticeController
                                        .getNoticesForResident(
                                                email
                                        );

                        // =================================================
                        // LOG RESULT
                        // =================================================

                        System.out.println(
                                "========================================"
                        );

                        System.out.println(
                                "TOTAL NOTICES FETCHED : "
                                        + fetchedNotices.size()
                        );

                        System.out.println(
                                "========================================"
                        );

                        // =================================================
                        // JAVAFX APPLICATION THREAD
                        // =================================================

                        Platform.runLater(() -> {

                            System.out.println(
                                    "Updating JavaFX Notice UI..."
                            );

                            // =============================================
                            // CLEAR OLD DATA
                            // =============================================

                            allNotices.clear();

                            // =============================================
                            // ADD NEW DATA
                            // =============================================

                            allNotices.addAll(
                                    fetchedNotices
                            );

                            // =============================================
                            // CLEAR CONTAINER
                            // =============================================

                            noticeContainer
                                    .getChildren()
                                    .clear();

                            // =============================================
                            // NO NOTICES
                            // =============================================

                            if (fetchedNotices.isEmpty()) {

                                Label noNotice =
                                        new Label(
                                                "No notices available"
                                        );

                                noNotice.setFont(
                                        Font.font(
                                                "System",
                                                15
                                        )
                                );

                                noNotice.setTextFill(
                                        Color.web(
                                                "#607D8B"
                                        )
                                );

                                noticeContainer
                                        .getChildren()
                                        .add(
                                                noNotice
                                        );

                            } else {

                                // =========================================
                                // CREATE NOTICE BOXES
                                // =========================================

                                for (
                                        NoticeModel notice :
                                        fetchedNotices
                                ) {

                                    VBox box =
                                            createNoticeBox(
                                                    notice
                                            );

                                    noticeContainer
                                            .getChildren()
                                            .add(
                                                    box
                                            );
                                }
                            }

                            // =============================================
                            // LOG UI COUNT
                            // =============================================

                            System.out.println(
                                    "Notice boxes added = "
                                            + noticeContainer
                                            .getChildren()
                                            .size()
                            );

                            // =============================================
                            // FORCE LAYOUT
                            // =============================================

                            noticeContainer.applyCss();

                            noticeContainer.layout();
                        });

                    } catch (Exception e) {

                        // =================================================
                        // ERROR LOG
                        // =================================================

                        System.out.println(
                                "========================================"
                        );

                        System.out.println(
                                "ERROR WHILE FETCHING NOTICES"
                        );

                        System.out.println(
                                "========================================"
                        );

                        e.printStackTrace();

                        // =================================================
                        // SHOW ERROR IN JAVAFX
                        // =================================================

                        Platform.runLater(() -> {

                            noticeContainer
                                    .getChildren()
                                    .clear();

                            Label error =
                                    new Label(
                                            "Error loading notices"
                                    );

                            error.setTextFill(
                                    Color.RED
                            );

                            noticeContainer
                                    .getChildren()
                                    .add(
                                            error
                                    );
                        });
                    }
                });

        // =========================================================
        // DAEMON THREAD
        // =========================================================

        thread.setDaemon(true);

        thread.start();
    }

    // =========================================================
    // CREATE NOTICE BOX
    // =========================================================

    private VBox createNoticeBox(
            NoticeModel notice) {

        VBox box =
                new VBox(12);

        box.setPadding(
                new Insets(20)
        );

        box.setMinHeight(
                130
        );

        box.setMaxWidth(
                Double.MAX_VALUE
        );

        box.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #d0d0d0;" +
                "-fx-border-radius: 10;"
        );

        // =====================================================
        // TITLE
        // =====================================================

        Label titleLabel =
                new Label(
                        notice.getTitle() == null
                                ? ""
                                : notice.getTitle()
                );

        titleLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        18
                )
        );

        titleLabel.setTextFill(
                Color.web("#263238")
        );

        // =====================================================
        // STATUS
        // =====================================================

        Label statusLabel =
                new Label(
                        notice.getStatus() == null
                                ? ""
                                : notice.getStatus()
                );

        statusLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12
                )
        );

        statusLabel.setTextFill(
                Color.WHITE
        );

        statusLabel.setPadding(
                new Insets(
                        5,
                        12,
                        5,
                        12
                )
        );

        statusLabel.setStyle(
                "-fx-background-color: #2e7d32;" +
                "-fx-background-radius: 15;"
        );

        // =====================================================
        // TOP ROW
        // =====================================================

        HBox topRow =
                new HBox(10);

        topRow.setAlignment(
                Pos.CENTER_LEFT
        );

        topRow.getChildren().addAll(
                titleLabel,
                statusLabel
        );

        // =====================================================
        // DATE
        // =====================================================

        Label dateLabel =
                new Label(
                        "Date • "
                                + (
                                notice.getDate() == null
                                        ? ""
                                        : notice.getDate()
                        )
                );

        dateLabel.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        dateLabel.setTextFill(
                Color.web("#607D8B")
        );

        // =====================================================
        // DESCRIPTION
        // =====================================================

        Label descriptionLabel =
                new Label(
                        notice.getDescription() == null
                                ? ""
                                : notice.getDescription()
                );

        descriptionLabel.setWrapText(
                true
        );

        descriptionLabel.setMaxWidth(
                Double.MAX_VALUE
        );

        descriptionLabel.setFont(
                Font.font(
                        "System",
                        14
                )
        );

        descriptionLabel.setTextFill(
                Color.web("#455A64")
        );

        // =====================================================
        // ADD TO BOX
        // =====================================================

        box.getChildren().addAll(
                topRow,
                dateLabel,
                descriptionLabel
        );

        return box;
    }
}