
// package com.society.view.Resident_portal;

// import java.io.File;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
// import java.util.List;

// import com.google.cloud.firestore.Firestore;

// import com.society.config.FirebaseConfig;
// import com.society.controller.Resident_Controller.ComplaintController;
// import com.society.dao.Resident_dao.ComplaintDAO;
// import com.society.dao.Welcome.UserDao;
// import com.society.model.Resident_model.ComplaintModel;
// import com.society.model.Welcome.User;
// import com.society.view.ScreenSize;

// import javafx.animation.FadeTransition;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;

// import javafx.scene.Scene;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Button;
// import javafx.scene.control.ComboBox;
// import javafx.scene.control.DatePicker;
// import javafx.scene.control.Label;
// import javafx.scene.control.ScrollPane;
// import javafx.scene.control.TextArea;
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
// import javafx.scene.layout.Region;
// import javafx.scene.layout.VBox;

// import javafx.scene.paint.Color;

// import javafx.stage.FileChooser;
// import javafx.stage.Stage;
// import javafx.util.Duration;


// public class Complaint {

//     private Firestore firestore;

//     private ComplaintDAO complaintDAO;
//     private ComplaintController complaintController;

//     private String currentEmail;
//     private String currentFlat;

//     private VBox complaintsContainer;


//     public Complaint() {

//         firestore = FirebaseConfig.getFirestore();

//         complaintDAO = new ComplaintDAO(firestore);

//         complaintController = new ComplaintController(complaintDAO);

//         currentEmail = UserDao.getLoggedInEmail();

//         loadCurrentFlat();
//     }


//     // ---------------------------------------------------------
//     // LOAD CURRENT USER FLAT
//     // ---------------------------------------------------------

//     private void loadCurrentFlat() {

//         try {

//             UserDao userDao = new UserDao();

//             User user = userDao.getUserByEmail(currentEmail);

//             if (user != null) {

//                 currentFlat = user.getFlatNo();

//             }

//         } catch (Exception e) {

//             e.printStackTrace();
//         }
//     }


//     // ---------------------------------------------------------
//     // COMPLAINT SCENE
//     // ---------------------------------------------------------

//     public Scene getComplaintScene(Stage stage) {


//         // =====================================================
//         // ROOT
//         // =====================================================

//         BorderPane root = new BorderPane();

//         // =====================================================
//         // FULL SCREEN BACKGROUND IMAGE
//         // =====================================================

//         Image backgroundImage = new Image(
//                 getClass()
//                         .getResource("/background-Dashboard5.jpeg")
//                         .toExternalForm()
//         );

//         BackgroundSize backgroundSize = new BackgroundSize(
//                 100,
//                 100,
//                 true,
//                 true,
//                 false,
//                 true
//         );

//         BackgroundImage bgImage = new BackgroundImage(
//                 backgroundImage,
//                 BackgroundRepeat.NO_REPEAT,
//                 BackgroundRepeat.NO_REPEAT,
//                 BackgroundPosition.CENTER,
//                 backgroundSize
//         );

//         root.setBackground(new Background(bgImage));


//         // =====================================================
//         // RESIDENT SIDEBAR
//         // =====================================================

//        panel sidebar = new panel(stage, currentEmail);

// root.setLeft(sidebar.getSidebar());

//         // =====================================================
//         // MAIN CENTER AREA
//         // =====================================================

//         BorderPane mainArea = new BorderPane();


//         // =====================================================
//         // HEADER
//         // =====================================================

//         HBox header = new HBox();

//         header.setPrefHeight(80);

//         header.setPadding(
//                 new Insets(15, 25, 15, 25)
//         );

//         header.setAlignment(Pos.CENTER_LEFT);

//         header.setStyle(
//                 "-fx-background-color:#4e342e;"
//         );


//         // -----------------------------------------------------
//         // HEADER TEXT
//         // -----------------------------------------------------

//         VBox headerText = new VBox(4);


//         Label greeting = new Label(
//                 "Complaints"
//         );

//         greeting.setStyle(
//                 "-fx-font-family:'Bodoni MT';" +
//                 "-fx-font-size:24px;" +
//                 "-fx-font-weight:bold;" +
//                 "-fx-text-fill:white;"
//         );


//         Label description = new Label(
//                 "Report and track your complaints"
//         );

//         description.setStyle(
//                 "-fx-font-size:14px;" +
//                 "-fx-text-fill:white;"
//         );


//         headerText.getChildren().addAll(
//                 greeting,
//                 description
//         );


//         // -----------------------------------------------------
//         // HEADER SPACER
//         // -----------------------------------------------------

//         Region headerSpacer = new Region();

//         HBox.setHgrow(
//                 headerSpacer,
//                 Priority.ALWAYS
//         );


//         // -----------------------------------------------------
//         // DATE
//         // -----------------------------------------------------

//         LocalDate today = LocalDate.now();


//         Label day = new Label();

//         day.setText(
//                 today.format(
//                         DateTimeFormatter.ofPattern("EEEE")
//                 )
//         );

//         day.setStyle(
//                 "-fx-text-fill:white;" +
//                 "-fx-font-size:13px;"
//         );


//         Label date = new Label();

//         date.setText(
//                 today.format(
//                         DateTimeFormatter.ofPattern("dd MMMM yyyy")
//                 )
//         );

//         date.setStyle(
//                 "-fx-text-fill:white;" +
//                 "-fx-font-size:12px;"
//         );


//         VBox dateBox = new VBox(3);

//         dateBox.setAlignment(
//                 Pos.CENTER_RIGHT
//         );

//         dateBox.getChildren().addAll(
//                 day,
//                 date
//         );


//         header.getChildren().addAll(
//                 headerText,
//                 headerSpacer,
//                 dateBox
//         );


//         // Header animation intentionally disabled
//         // animateHeader(header);


//         // =====================================================
//         // CONTENT AREA
//         // =====================================================

//         HBox contentArea = new HBox(25);

//         contentArea.setPadding(
//                 new Insets(25, 35, 25, 35)
//         );

//         contentArea.setStyle(
//                "-fx-background-color:transparent;"
//         );


//         // =====================================================
//         // LEFT FORM
//         // =====================================================

//         VBox formBox = new VBox(15);

//         formBox.setPrefWidth(500);

//         formBox.setMaxWidth(500);

//         formBox.setPadding(
//                 new Insets(25)
//         );

//         formBox.setStyle(
//                 "-fx-background-color:#f8f3ee;" +
//                 "-fx-background-radius:12;" +
//                 "-fx-border-radius:12;"
//         );


//         // -----------------------------------------------------
//         // FORM TITLE
//         // -----------------------------------------------------

//         Label formTitle = new Label(
//                 "Raise a Complaint"
//         );

//         formTitle.setStyle(
//                 "-fx-font-family:'Bodoni MT';" +
//                 "-fx-font-size:24px;" +
//                 "-fx-font-weight:bold;" +
//                 "-fx-text-fill:#3e2723;"
//         );


//         Label formSubtitle = new Label(
//                 "Report an issue to the society management"
//         );

//         formSubtitle.setStyle(
//                 "-fx-font-size:14px;" +
//                 "-fx-text-fill:#6d4c41;"
//         );


//         // =====================================================
//         // CATEGORY
//         // =====================================================

//         Label categoryLabel = new Label(
//                 "Complaint Category"
//         );

//         categoryLabel.setStyle(
//                 "-fx-font-weight:bold;" +
//                 "-fx-text-fill:#3e2723;"
//         );


//         ComboBox<String> categoryCombo =
//                 new ComboBox<>();

//         categoryCombo.setPromptText(
//                 "Select Category"
//         );

//         categoryCombo.getItems().addAll(
//                 "Maintenance",
//                 "Water",
//                 "Electricity",
//                 "Security",
//                 "Cleanliness",
//                 "Parking",
//                 "Noise",
//                 "Other"
//         );

//         categoryCombo.setMaxWidth(
//                 Double.MAX_VALUE
//         );


//         // =====================================================
//         // TITLE
//         // =====================================================

//         Label titleLabel = new Label(
//                 "Complaint Title"
//         );

//         titleLabel.setStyle(
//                 "-fx-font-weight:bold;" +
//                 "-fx-text-fill:#3e2723;"
//         );


//         TextField titleField = new TextField();

//         titleField.setPromptText(
//                 "Enter complaint title"
//         );

//         titleField.setMaxWidth(
//                 Double.MAX_VALUE
//         );


//         // =====================================================
//         // DESCRIPTION
//         // =====================================================

//         Label descriptionLabel = new Label(
//                 "Description"
//         );

//         descriptionLabel.setStyle(
//                 "-fx-font-weight:bold;" +
//                 "-fx-text-fill:#3e2723;"
//         );


//         TextArea descriptionArea =
//                 new TextArea();

//         descriptionArea.setPromptText(
//                 "Describe your complaint..."
//         );

//         descriptionArea.setPrefRowCount(5);

//         descriptionArea.setWrapText(true);

//         descriptionArea.setMaxWidth(
//                 Double.MAX_VALUE
//         );


//         // =====================================================
//         // IMAGE
//         // =====================================================

//         Label imageLabel = new Label(
//                 "Attach Image"
//         );

//         imageLabel.setStyle(
//                 "-fx-font-weight:bold;" +
//                 "-fx-text-fill:#3e2723;"
//         );


//         Label selectedFileLabel =
//                 new Label(
//                         "No image selected"
//                 );

//         selectedFileLabel.setStyle(
//                 "-fx-text-fill:#6d4c41;"
//         );


//         Button chooseImageButton =
//                 new Button(
//                         "Choose Image"
//                 );

//         chooseImageButton.setStyle(
//                 "-fx-background-color:#d7ccc8;" +
//                 "-fx-text-fill:#3e2723;" +
//                 "-fx-font-weight:bold;" +
//                 "-fx-background-radius:7;"
//         );


//         final File[] selectedFile =
//                 new File[1];


//         chooseImageButton.setOnAction(e -> {

//             FileChooser fileChooser =
//                     new FileChooser();

//             fileChooser.setTitle(
//                     "Select Complaint Image"
//             );


//             fileChooser.getExtensionFilters()
//                     .add(
//                             new FileChooser.ExtensionFilter(
//                                     "Image Files",
//                                     "*.png",
//                                     "*.jpg",
//                                     "*.jpeg"
//                             )
//                     );


//             File file =
//                     fileChooser.showOpenDialog(stage);


//             if (file != null) {

//                 selectedFile[0] = file;

//                 selectedFileLabel.setText(
//                         file.getName()
//                 );
//             }
//         });


//         HBox imageBox = new HBox(10);

//         imageBox.setAlignment(
//                 Pos.CENTER_LEFT
//         );

//         imageBox.getChildren().addAll(
//                 chooseImageButton,
//                 selectedFileLabel
//         );


//         // =====================================================
//         // PREFERRED DATE
//         // =====================================================

//         Label preferredDateLabel =
//                 new Label(
//                         "Preferred Date"
//                 );

//         preferredDateLabel.setStyle(
//                 "-fx-font-weight:bold;" +
//                 "-fx-text-fill:#3e2723;"
//         );


//         DatePicker preferredDate =
//                 new DatePicker();

//         preferredDate.setValue(
//                 LocalDate.now()
//         );

//         preferredDate.setMaxWidth(
//                 Double.MAX_VALUE
//         );


//         // =====================================================
//         // BUTTONS
//         // =====================================================

//         Button clearButton =
//                 new Button(
//                         "Clear"
//                 );

//         clearButton.setStyle(
//                 "-fx-background-color:#d7ccc8;" +
//                 "-fx-text-fill:#3e2723;" +
//                 "-fx-font-weight:bold;" +
//                 "-fx-background-radius:7;" +
//                 "-fx-padding:10 25;"
//         );


//         Button submitButton =
//                 new Button(
//                         "Submit Complaint"
//                 );

//         submitButton.setStyle(
//                 "-fx-background-color:#4e342e;" +
//                 "-fx-text-fill:white;" +
//                 "-fx-font-weight:bold;" +
//                 "-fx-background-radius:7;" +
//                 "-fx-padding:10 25;"
//         );


//         HBox buttonBox = new HBox(12);

//         buttonBox.setAlignment(
//                 Pos.CENTER_RIGHT
//         );

//         buttonBox.getChildren().addAll(
//                 clearButton,
//                 submitButton
//         );


//         // =====================================================
//         // CLEAR BUTTON
//         // =====================================================

//         clearButton.setOnAction(e -> {

//             categoryCombo.setValue(null);

//             titleField.clear();

//             descriptionArea.clear();

//             preferredDate.setValue(
//                     LocalDate.now()
//             );

//             selectedFile[0] = null;

//             selectedFileLabel.setText(
//                     "No image selected"
//             );
//         });


//         // =====================================================
//         // SUBMIT BUTTON
//         // =====================================================

//         submitButton.setOnAction(e -> {

//             try {

//                 String category =
//                         categoryCombo.getValue();

//                String complaintTitle = titleField.getText();

// String complaintDescription = descriptionArea.getText();

// LocalDate preferredDateValue = preferredDate.getValue();

//                 // -------------------------------------------------
//                 // VALIDATION
//                 // -------------------------------------------------

//                 if (category == null ||
//                         category.isEmpty()) {

//                     showAlert(
//                             Alert.AlertType.WARNING,
//                             "Validation",
//                             "Please select complaint category."
//                     );

//                     return;
//                 }


//                 if (complaintTitle == null ||
//         complaintTitle.trim().isEmpty()) {
//                     showAlert(
//                             Alert.AlertType.WARNING,
//                             "Validation",
//                             "Please enter complaint title."
//                     );

//                     return;
//                 }


//                 if (description == null ||
//                         complaintDescription.trim().isEmpty()) {

//                     showAlert(
//                             Alert.AlertType.WARNING,
//                             "Validation",
//                             "Please enter complaint description."
//                     );

//                     return;
//                 }


//                 // -------------------------------------------------
//                 // SUBMIT
//                 // -------------------------------------------------

//                 complaintController.submitComplaint(
//                         currentEmail,
//                         currentFlat,
//                         category,
//                         complaintTitle,
//                         description,
//                         selectedFile[0],
//                         preferredDateValue
//                 );


//                 showAlert(
//                         Alert.AlertType.INFORMATION,
//                         "Success",
//                         "Complaint submitted successfully."
//                 );


//                 clearButton.fire();


//                 loadComplaints();


//             } catch (Exception ex) {

//                 ex.printStackTrace();


//                 showAlert(
//                         Alert.AlertType.ERROR,
//                         "Error",
//                         "Unable to submit complaint.\n"
//                                 + ex.getMessage()
//                 );
//             }
//         });


//         // =====================================================
//         // ADD FORM CONTROLS
//         // =====================================================

//         formBox.getChildren().addAll(

//                 formTitle,

//                 formSubtitle,

//                 categoryLabel,
//                 categoryCombo,

//                 titleLabel,
//                 titleField,

//                 descriptionLabel,
//                 descriptionArea,

//                 imageLabel,
//                 imageBox,

//                 preferredDateLabel,
//                 preferredDate,

//                 buttonBox
//         );


//         // =====================================================
//         // RIGHT SIDE - MY COMPLAINTS
//         // =====================================================

//         VBox complaintsBox =
//                 new VBox(12);

//         complaintsBox.setPrefWidth(430);

//         complaintsBox.setMaxWidth(430);

//         complaintsBox.setPadding(
//                 new Insets(25)
//         );

//         complaintsBox.setStyle(
//                 "-fx-background-color:#f8f3ee;" +
//                 "-fx-background-radius:12;" +
//                 "-fx-border-radius:12;"
//         );


//         Label complaintsTitle =
//                 new Label(
//                         "My Complaints"
//                 );

//         complaintsTitle.setStyle(
//                 "-fx-font-family:'Bodoni MT';" +
//                 "-fx-font-size:24px;" +
//                 "-fx-font-weight:bold;" +
//                 "-fx-text-fill:#3e2723;"
//         );


//         Label complaintsSubtitle =
//                 new Label(
//                         "Track the status of your complaints"
//                 );

//         complaintsSubtitle.setStyle(
//                 "-fx-font-size:14px;" +
//                 "-fx-text-fill:#6d4c41;"
//         );


//         // =====================================================
//         // COMPLAINTS CONTAINER
//         // =====================================================

//         complaintsContainer =
//                 new VBox(12);

//         complaintsContainer.setPadding(
//                 new Insets(5)
//         );


//         ScrollPane complaintsScroll =
//                 new ScrollPane(
//                         complaintsContainer
//                 );

//         complaintsScroll.setFitToWidth(true);

//         complaintsScroll.setPannable(true);

//         complaintsScroll.setHbarPolicy(
//                 ScrollPane.ScrollBarPolicy.NEVER
//         );

//         complaintsScroll.setVbarPolicy(
//                 ScrollPane.ScrollBarPolicy.AS_NEEDED
//         );

//         complaintsScroll.setStyle(
//                 "-fx-background-color:transparent;" +
//                 "-fx-background:transparent;"
//         );


//         VBox.setVgrow(
//                 complaintsScroll,
//                 Priority.ALWAYS
//         );


//         complaintsBox.getChildren().addAll(

//                 complaintsTitle,

//                 complaintsSubtitle,

//                 complaintsScroll
//         );


//        // =====================================================
//         // ADD LEFT + RIGHT
//         // =====================================================

//         contentArea.getChildren().addAll(
//                 formBox,
//                 complaintsBox
//         );

//         // =====================================================
//         // CONTENT ANIMATION
//         // Sidebar and Header remain static
//         // =====================================================

//         FadeTransition contentFade =
//                 new FadeTransition(
//                         Duration.millis(700),
//                         contentArea
//                 );

//         contentFade.setFromValue(0.0);
//         contentFade.setToValue(1.0);
//         contentFade.setCycleCount(1);

//         // =====================================================
//         // MAIN AREA
//         // =====================================================

//         mainArea.setTop(header);
//         mainArea.setCenter(contentArea);

//         // =====================================================
//         // ROOT CENTER
//         // =====================================================

//         root.setCenter(mainArea);

//         // Start content animation
//         contentFade.play();

//         // =====================================================
//         // LOAD COMPLAINTS
//         // =====================================================

//         loadComplaints();


//         // =====================================================
//         // SCENE
//         // =====================================================

//         Scene scene =
//                 new Scene(
//                         root,
//                         ScreenSize.WIDTH,
//                         ScreenSize.HEIGHT
//                 );


//         return scene;
//     }


//     // =========================================================
//     // LOAD COMPLAINTS
//     // =========================================================

//     private void loadComplaints() {

//         if (complaintsContainer == null) {

//             return;
//         }


//         complaintsContainer.getChildren().clear();


//         try {

//             List<ComplaintModel> complaints =
//                     complaintController.getComplaintsByEmail(
//                             currentEmail
//                     );


//             if (complaints == null ||
//                     complaints.isEmpty()) {

//                 Label emptyLabel =
//                         new Label(
//                                 "No complaints found."
//                         );

//                 emptyLabel.setStyle(
//                         "-fx-text-fill:#6d4c41;" +
//                         "-fx-font-size:14px;"
//                 );

//                 complaintsContainer
//                         .getChildren()
//                         .add(emptyLabel);

//                 return;
//             }


//             for (ComplaintModel complaint :
//                     complaints) {

//                 complaintsContainer
//                         .getChildren()
//                         .add(
//                                 createComplaintCard(
//                                         complaint
//                                 )
//                         );
//             }


//         } catch (Exception e) {

//             e.printStackTrace();


//             Label errorLabel =
//                     new Label(
//                             "Unable to load complaints."
//                     );

//             errorLabel.setStyle(
//                     "-fx-text-fill:#b71c1c;" +
//                     "-fx-font-size:14px;"
//             );


//             complaintsContainer
//                     .getChildren()
//                     .add(errorLabel);
//         }
//     }


//     // =========================================================
//     // COMPLAINT CARD
//     // =========================================================

//     private VBox createComplaintCard(
//             ComplaintModel complaint) {


//         VBox card =
//                 new VBox(8);


//         card.setPadding(
//                 new Insets(15)
//         );


//         card.setStyle(
//                 "-fx-background-color:white;" +
//                 "-fx-background-radius:10;" +
//                 "-fx-border-color:#d7ccc8;" +
//                 "-fx-border-radius:10;"
//         );


//         // -----------------------------------------------------
//         // TITLE
//         // -----------------------------------------------------

//         Label title =
//                 new Label(
//                         complaint.getTitle()
//                 );


//         title.setStyle(
//                 "-fx-font-size:17px;" +
//                 "-fx-font-weight:bold;" +
//                 "-fx-text-fill:#3e2723;"
//         );


//         // -----------------------------------------------------
//         // CATEGORY
//         // -----------------------------------------------------

//         Label category =
//                 new Label(
//                         "Category: "
//                                 + complaint.getCategory()
//                 );


//         category.setStyle(
//                 "-fx-font-size:13px;" +
//                 "-fx-text-fill:#6d4c41;"
//         );


//         // -----------------------------------------------------
//         // DESCRIPTION
//         // -----------------------------------------------------

//         Label description =
//                 new Label(
//                         complaint.getDescription()
//                 );


//         description.setWrapText(true);


//         description.setStyle(
//                 "-fx-font-size:13px;" +
//                 "-fx-text-fill:#3e2723;"
//         );


//         // -----------------------------------------------------
//         // STATUS
//         // -----------------------------------------------------

//         Label status =
//                 new Label(
//                         "Status: "
//                                 + complaint.getStatus()
//                 );


//         status.setStyle(
//                 "-fx-font-size:13px;" +
//                 "-fx-font-weight:bold;" +
//                 "-fx-text-fill:#4e342e;"
//         );


//         card.getChildren().addAll(

//                 title,

//                 category,

//                 description,

//                 status
//         );


//         return card;
//     }


//     // =========================================================
//     // ALERT
//     // =========================================================

//     private void showAlert(
//             Alert.AlertType type,
//             String title,
//             String message) {


//         Alert alert =
//                 new Alert(type);


//         alert.setTitle(title);

//         alert.setHeaderText(null);

//         alert.setContentText(message);

//         alert.showAndWait();
//     }
// }

package com.society.view.Resident_portal;

import com.google.cloud.firestore.Firestore;
import com.society.config.FirebaseConfig;
import com.society.controller.Resident_Controller.ComplaintController;
import com.society.dao.Resident_dao.ComplaintDAO;
import com.society.dao.Welcome.UserDao;
import com.society.model.Resident_model.ComplaintModel;
import com.society.model.Welcome.User;
import com.society.view.ScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Complaint {

    // =========================================================
    // CONTROLLER
    // =========================================================

    private final ComplaintController complaintController;

    // =========================================================
    // CURRENT LOGGED-IN EMAIL
    // =========================================================

    private String currentEmail;

    // =========================================================
    // CURRENT FLAT
    // =========================================================

    private String currentFlatNumber = "";

    // =========================================================
    // SELECTED IMAGE
    // =========================================================

    private File selectedFile;

    // =========================================================
    // COMPLAINT CONTAINER
    // =========================================================

    private VBox complaintsContainer;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Complaint() {

        Firestore firestore = FirebaseConfig.getFirestore();

        ComplaintDAO complaintDAO =
                new ComplaintDAO(firestore);

        complaintController =
                new ComplaintController(complaintDAO);

        // Get logged-in email
        currentEmail =
                UserDao.getLoggedInEmail();

        System.out.println(
                "Complaint Page Email: " + currentEmail
        );

        // Get logged-in user's flat
        loadCurrentFlat();
    }

    // =========================================================
    // LOAD CURRENT FLAT
    // =========================================================

    private void loadCurrentFlat() {

        try {

            if (currentEmail == null
                    || currentEmail.trim().isEmpty()) {

                System.out.println(
                        "No logged-in email."
                );

                return;
            }

            UserDao userDao = new UserDao();

            User user =
                    userDao.getUserByEmail(currentEmail);

            if (user != null) {

                currentFlatNumber =
                        user.getFlatNo();

                if (currentFlatNumber == null) {
                    currentFlatNumber = "";
                }

                System.out.println(
                        "Current Flat: "
                                + currentFlatNumber
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // CREATE SCENE
    // =========================================================

    public Scene getComplaintScene(Stage stage) {

        // =====================================================
        // SIDEBAR
        // =====================================================

        panel panelobj = new panel(stage, currentEmail);

        // =====================================================
        // ROOT
        // =====================================================

        BorderPane root = new BorderPane();

        root.setLeft(
                panelobj.getSidebar()
        );

        // =====================================================
        // MAIN AREA
        // =====================================================

        HBox mainArea = new HBox(25);

        mainArea.setPadding(
                new Insets(25, 35, 25, 35)
        );

        mainArea.setStyle(
                "-fx-background-color:#e8ddd5;"
        );

        // =====================================================
        // LEFT FORM
        // =====================================================

        VBox formBox = new VBox(15);

        formBox.setPrefWidth(500);

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label("Raise a Complaint");

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        24
                )
        );

        title.setTextFill(
                Color.web("#172B4D")
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        Label subtitle =
                new Label(
                        "Report an issue to the society management"
                );

        subtitle.setTextFill(Color.GRAY);

        subtitle.setFont(
                Font.font("System", 13)
        );

        // =====================================================
        // CATEGORY
        // =====================================================

        Label categoryLabel =
                new Label("Complaint Category");

        ComboBox<String> category =
                new ComboBox<>();

        category.setPromptText(
                "Select Category"
        );

        category.getItems().addAll(
                "Maintenance",
                "Water Supply",
                "Electricity",
                "Security",
                "Cleanliness",
                "Lift",
                "Parking",
                "Other"
        );

        category.setMaxWidth(
                Double.MAX_VALUE
        );

        category.setPrefHeight(38);

        // =====================================================
        // COMPLAINT TITLE
        // =====================================================

        Label complaintTitleLabel =
                new Label("Complaint Title");

        TextField complaintTitle =
                new TextField();

        complaintTitle.setPromptText(
                "Brief title of your complaint"
        );

        complaintTitle.setPrefHeight(38);

        // =====================================================
        // DESCRIPTION
        // =====================================================

        Label descriptionLabel =
                new Label("Description");

        TextArea description =
                new TextArea();

        description.setPromptText(
                "Describe your issue in detail"
        );

        description.setPrefHeight(120);

        description.setWrapText(true);

        // =====================================================
        // IMAGE
        // =====================================================

        Label imageLabel =
                new Label("Upload Image (Optional)");

        Button chooseFile =
                new Button("Choose File");

        Label fileName =
                new Label("No file chosen");

        fileName.setTextFill(Color.GRAY);

        HBox fileBox =
                new HBox(10);

        fileBox.setAlignment(
                Pos.CENTER_LEFT
        );

        fileBox.getChildren().addAll(
                chooseFile,
                fileName
        );

        // =====================================================
        // FILE CHOOSER
        // =====================================================

        chooseFile.setOnAction(e -> {

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Select Complaint Image"
            );

            fileChooser
                    .getExtensionFilters()
                    .add(
                            new FileChooser.ExtensionFilter(
                                    "Image Files",
                                    "*.png",
                                    "*.jpg",
                                    "*.jpeg"
                            )
                    );

            selectedFile =
                    fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {

                fileName.setText(
                        selectedFile.getName()
                );
            }
        });

        // =====================================================
        // DATE
        // =====================================================

        Label dateLabel =
                new Label("Preferred Date");

        DatePicker preferredDate =
                new DatePicker();

        preferredDate.setPromptText(
                "Select date"
        );

        preferredDate.setMaxWidth(
                Double.MAX_VALUE
        );

        preferredDate.setPrefHeight(38);

        // =====================================================
        // BUTTONS
        // =====================================================

        Button clearButton =
                new Button("Clear");

        clearButton.setPrefWidth(90);
        clearButton.setPrefHeight(35);

        clearButton.setStyle(
                "-fx-background-color:white;"
                        + "-fx-border-color:#D1D5DB;"
                        + "-fx-border-radius:5;"
                        + "-fx-background-radius:5;"
        );

        Button submitButton =
                new Button("Submit Complaint");

        submitButton.setPrefWidth(145);
        submitButton.setPrefHeight(35);

        submitButton.setStyle(
                "-fx-background-color:#0B4F8A;"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:5;"
        );

        HBox buttonBox =
                new HBox(10);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.getChildren().addAll(
                clearButton,
                submitButton
        );

        // =====================================================
        // CLEAR BUTTON
        // =====================================================

        clearButton.setOnAction(e -> {

            category.setValue(null);

            complaintTitle.clear();

            description.clear();

            preferredDate.setValue(null);

            selectedFile = null;

            fileName.setText(
                    "No file chosen"
            );
        });

        // =====================================================
        // SUBMIT BUTTON
        // =====================================================

        submitButton.setOnAction(e -> {

            // =================================================
            // GET CURRENT LOGIN EMAIL
            // =================================================

            currentEmail =
                    UserDao.getLoggedInEmail();

            if (currentEmail == null
                    || currentEmail.trim().isEmpty()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Login Required",
                        "No logged-in user was found."
                );

                return;
            }

            // =================================================
            // GET CURRENT FLAT AGAIN
            // =================================================

            loadCurrentFlat();

            // =================================================
            // VALIDATION
            // =================================================

            if (category.getValue() == null
                    || complaintTitle.getText()
                    .trim()
                    .isEmpty()
                    || description.getText()
                    .trim()
                    .isEmpty()
                    || preferredDate.getValue() == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Missing Information",
                        "Please fill all required fields."
                );

                return;
            }

            // =================================================
            // SAVE COMPLAINT
            // =================================================

            try {

                String date =
                        preferredDate
                                .getValue()
                                .toString();

                String imageName = "";

                if (selectedFile != null) {

                    imageName =
                            selectedFile.getName();
                }

                ComplaintModel complaint =
                        complaintController.submitComplaint(

                                currentEmail,

                                currentFlatNumber,

                                category.getValue(),

                                complaintTitle
                                        .getText()
                                        .trim(),

                                description
                                        .getText()
                                        .trim(),

                                imageName,

                                date
                        );

                if (complaint == null) {

                    throw new Exception(
                            "Complaint could not be saved."
                    );
                }

                System.out.println(
                        "Complaint saved with ID: "
                                + complaint.getId()
                );

                // =================================================
                // SUCCESS
                // =================================================

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Complaint Submitted",
                        "Your complaint has been submitted successfully."
                );

                // =================================================
                // CLEAR FORM
                // =================================================

                category.setValue(null);

                complaintTitle.clear();

                description.clear();

                preferredDate.setValue(null);

                selectedFile = null;

                fileName.setText(
                        "No file chosen"
                );

                // =================================================
                // REFRESH COMPLAINTS
                // =================================================

                loadMyComplaints(
                        complaintsContainer
                );

            } catch (Exception ex) {

                ex.printStackTrace();

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed to save complaint:\n"
                                + ex.getMessage()
                );
            }
        });

        // =====================================================
        // ADD FORM CONTROLS
        // =====================================================

        formBox.getChildren().addAll(

                title,

                subtitle,

                categoryLabel,

                category,

                complaintTitleLabel,

                complaintTitle,

                descriptionLabel,

                description,

                imageLabel,

                fileBox,

                dateLabel,

                preferredDate,

                buttonBox
        );

        // =====================================================
        // RIGHT SIDE
        // =====================================================

        VBox complaintsBox =
                new VBox(15);

        complaintsBox.setPrefWidth(430);

        // =====================================================
        // MY COMPLAINTS TITLE
        // =====================================================

        Label myComplaints =
                new Label("My Complaints");

        myComplaints.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        18
                )
        );

        Label viewAll =
                new Label(
                        "View all your raised complaints"
                );

        viewAll.setTextFill(Color.GRAY);

        // =====================================================
        // COMPLAINTS CONTAINER
        // =====================================================

        complaintsContainer =
                new VBox(15);

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane(
                        complaintsContainer
                );

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color:transparent;"
        );

        scrollPane.setPrefHeight(600);

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        complaintsBox.getChildren().addAll(

                myComplaints,

                viewAll,

                scrollPane
        );

        // =====================================================
        // ADD BOTH SIDES
        // =====================================================

        HBox.setHgrow(
                formBox,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                complaintsBox,
                Priority.ALWAYS
        );

        mainArea.getChildren().addAll(
                formBox,
                complaintsBox
        );

        // =====================================================
        // PAGE TITLE
        // =====================================================

        Label newlLabel =
                new Label("Complaints");

        newlLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        24
                )
        );

        newlLabel.setTextFill(
                Color.web("#172B4D")
        );

        newlLabel.setPadding(
                new Insets(15, 35, 15, 35)
        );

        newlLabel.setStyle(
                "-fx-background-color:#765252;"
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        BorderPane mainarea2 =
                new BorderPane();

        mainarea2.setTop(newlLabel);

        mainarea2.setCenter(mainArea);

        root.setCenter(mainarea2);

        // =====================================================
        // LOAD FIRESTORE COMPLAINTS
        // =====================================================

        loadMyComplaints(
                complaintsContainer
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
    // LOAD MY COMPLAINTS
    // =========================================================

    private void loadMyComplaints(
            VBox container) {

        if (container == null) {
            return;
        }

        try {

            String email =
                    UserDao.getLoggedInEmail();

            if (email == null
                    || email.trim().isEmpty()) {

                container.getChildren().clear();

                Label label =
                        new Label(
                                "No logged-in user."
                        );

                label.setTextFill(Color.GRAY);

                container.getChildren().add(label);

                return;
            }

            System.out.println(
                    "Fetching complaints for: "
                            + email
            );

            List<ComplaintModel> complaints =
                    complaintController
                            .getMyComplaints(email);

            container.getChildren().clear();

            // =================================================
            // NO COMPLAINTS
            // =================================================

            if (complaints == null
                    || complaints.isEmpty()) {

                Label noComplaints =
                        new Label(
                                "You have not raised any complaints yet."
                        );

                noComplaints.setTextFill(
                        Color.GRAY
                );

                noComplaints.setFont(
                        Font.font(
                                "System",
                                14
                        )
                );

                container.getChildren().add(
                        noComplaints
                );

                return;
            }

            // =================================================
            // CREATE CARDS
            // =================================================

            for (ComplaintModel complaint :
                    complaints) {

                if (complaint != null) {

                    VBox card =
                            createComplaintCard(
                                    complaint
                            );

                    container.getChildren().add(card);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            container.getChildren().clear();

            Label error =
                    new Label(
                            "Unable to load complaints."
                    );

            error.setTextFill(Color.RED);

            container.getChildren().add(error);
        }
    }

    // =========================================================
    // CREATE COMPLAINT CARD
    // =========================================================

    private VBox createComplaintCard(
            ComplaintModel complaint) {

        VBox card =
                new VBox(8);

        card.setPadding(
                new Insets(15)
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setStyle(
                "-fx-background-color:white;"
                        + "-fx-border-color:#E5E7EB;"
                        + "-fx-border-radius:7;"
                        + "-fx-background-radius:7;"
        );

        // =====================================================
        // TITLE
        // =====================================================

        String titleText =
                complaint.getTitle();

        if (titleText == null
                || titleText.trim().isEmpty()) {

            titleText =
                    "Untitled Complaint";
        }

        Label titleLabel =
                new Label(titleText);

        titleLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        14
                )
        );

        titleLabel.setWrapText(true);

        // =====================================================
        // CATEGORY
        // =====================================================

        Label categoryLabel =
                new Label(
                        "Category: "
                                + safe(
                                        complaint.getCategory()
                                )
                );

        categoryLabel.setTextFill(
                Color.web("#555555")
        );

        // =====================================================
        // EMAIL
        // =====================================================

        Label emailLabel =
                new Label(
                        "Email: "
                                + safe(
                                        complaint.getEmail()
                                )
                );

        emailLabel.setTextFill(Color.GRAY);

        emailLabel.setWrapText(true);

        // =====================================================
        // FLAT + DATE
        // =====================================================

        String flat =
                safe(
                        complaint.getFlatNumber()
                );

        String date =
                formatDate(
                        complaint.getCreatedAt()
                );

        Label details =
                new Label(
                        "Flat: "
                                + flat
                                + "        "
                                + date
                );

        details.setTextFill(Color.GRAY);

        // =====================================================
        // STATUS
        // =====================================================

        String status =
                safe(
                        complaint.getStatus()
                );

        if (status.isEmpty()) {
            status = "PENDING";
        }

        String background;
        String textColor;

        if (status.equalsIgnoreCase("RESOLVED")) {

            background = "#DFF6E5";
            textColor = "#16803C";

        } else if (
                status.equalsIgnoreCase("CLOSED")) {

            background = "#EEF0F3";
            textColor = "#6B7280";

        } else {

            background = "#FFF0D6";
            textColor = "#D97706";
        }

        Label statusLabel =
                new Label(status);

        statusLabel.setStyle(
                "-fx-background-color:"
                        + background
                        + ";"
                        + "-fx-text-fill:"
                        + textColor
                        + ";"
                        + "-fx-padding:5 10 5 10;"
                        + "-fx-background-radius:12;"
        );

        // =====================================================
        // BOTTOM
        // =====================================================

        HBox bottom =
                new HBox();

        bottom.setAlignment(
                Pos.CENTER_LEFT
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        bottom.getChildren().addAll(
                details,
                spacer,
                statusLabel
        );

        // =====================================================
        // ADD TO CARD
        // =====================================================

        card.getChildren().addAll(

                titleLabel,

                categoryLabel,

                emailLabel,

                bottom
        );

        return card;
    }

    // =========================================================
    // FORMAT DATE
    // =========================================================

    private String formatDate(Date date) {

        if (date == null) {
            return "Date unavailable";
        }

        SimpleDateFormat formatter =
                new SimpleDateFormat(
                        "dd MMM yyyy"
                );

        return formatter.format(date);
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safe(String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return "";
        }

        return value;
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

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}
