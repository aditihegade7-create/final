package com.society.view.Resident_portal;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.config.FirebaseConfig;
import com.society.view.ScreenSize;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parking {

        // =========================================================
        // PARKING SLOT NAMES
        // =========================================================

        private static final String[] SLOT_NAMES = {
                        "P-01",
                        "P-02",
                        "P-03",
                        "P-04",
                        "P-05",
                        "P-06",
                        "P-07",
                        "P-08"
        };

        // =========================================================
        // LOGGED IN RESIDENT
        // =========================================================

        private String loggedInEmail = "";
        private String residentSociety = "";

        // =========================================================
        // MY PARKING ASSIGNMENTS
        // =========================================================

        private final List<ParkingAssignment> myParkingSlots = new ArrayList<>();

        // =========================================================
        // ALL PARKING ASSIGNMENTS
        // =========================================================

        private final Map<String, ParkingAssignment> slotAssignments = new HashMap<>();

        // =========================================================
        // CONSTRUCTORS
        // =========================================================

        public Parking() {
        }

        public Parking(String residentEmail) {
                this.loggedInEmail = normalizeEmail(residentEmail);
        }

        // =========================================================
        // GET PARKING SCENE
        // =========================================================

        // public Scene getParkingScene(Stage stage) {
        //         // return getParkingScene(stage, this.loggedInEmail);
        //         // }

        //         BorderPane borderPane = new BorderPane();

        //         Image bgImage = new Image(
        //                         getClass().getResource("/background-Dashboard5.jpeg").toExternalForm());

        //         BackgroundImage backgroundImage = new BackgroundImage(
        //                         bgImage,
        //                         BackgroundRepeat.NO_REPEAT,
        //                         BackgroundRepeat.NO_REPEAT,
        //                         BackgroundPosition.CENTER,
        //                         new BackgroundSize(
        //                                         100,
        //                                         100,
        //                                         true,
        //                                         true,
        //                                         false,
        //                                         true));

        //         borderPane.setBackground(
        //                         new Background(backgroundImage));
        // }

        // ==================================================
        // =========================================================
        // MAIN PARKING SCENE
        // =========================================================

        public Scene getParkingScene(Stage stage, String residentEmail) {

                // ---------------------------------------------------------
                // RESET OLD DATA
                // ---------------------------------------------------------

                resetParkingData();

                // ---------------------------------------------------------
                // SET LOGGED IN EMAIL
                // ---------------------------------------------------------

                if (residentEmail != null && !residentEmail.trim().isEmpty()) {
                        loggedInEmail = normalizeEmail(residentEmail);
                }

                System.out.println();
                System.out.println("================================================");
                System.out.println("             RESIDENT PARKING");
                System.out.println("================================================");
                System.out.println("Logged In Email : " + loggedInEmail);

                // ---------------------------------------------------------
                // FETCH FIRESTORE
                // ---------------------------------------------------------

                if (!loggedInEmail.isEmpty()) {
                        fetchParkingData();
                } else {
                        System.out.println("ERROR: Resident email is empty.");
                }

                // =========================================================
                // SIDEBAR
                // =========================================================

                panel panelObj = new panel(stage, loggedInEmail);

                // ==================================================
                // =========================================================
                // HEADER
                // =========================================================

                Label title = new Label("Smart Parking");

                title.setStyle(
                                "-fx-font-size:26px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:white;");

                Label subtitle;

                if (residentSociety.isEmpty()) {

                        subtitle = new Label("Check your assigned parking area");

                } else {

                        subtitle = new Label(
                                        "Society: " + residentSociety);
                }

                subtitle.setStyle(
                                "-fx-font-size:14px;" +
                                                "-fx-text-fill:white;");

                VBox header = new VBox(
                                5,
                                title,
                                subtitle);

                header.setPadding(
                                new Insets(15, 20, 15, 20));

                header.setStyle(
                                "-fx-background-color: #4e342e;"

                );

                // =========================================================
                // MAIN CONTENT
                // =========================================================

                VBox mainContent = new VBox(25);

                mainContent.setPadding(
                                new Insets(25));

                mainContent.setStyle(
                                "-fx-background-color: transparent;");
                FadeTransition pageFade = new FadeTransition(
                                Duration.millis(700),
                                mainContent);

                pageFade.setFromValue(0.0);
                pageFade.setToValue(1.0);
                pageFade.setCycleCount(1);

                pageFade.play();

                // ==================================================
                // "-fx-background-color:#e8ddd5;"
                // );

                // =========================================================
                // MY PARKING CARD
                // =========================================================

                Label myParkingTitle = new Label("My Parking Area");

                myParkingTitle.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #4e342e;"

                );

                Label mySlot = new Label(
                                buildMyParkingText());

                mySlot.setStyle(
                                "-fx-font-size:16px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:#37474f;");

                Label myStatus;

                if (myParkingSlots.isEmpty()) {

                        myStatus = new Label("● NOT ALLOCATED");

                        myStatus.setStyle(
                                        "-fx-text-fill:#757575;" +
                                                        "-fx-font-size:15px;" +
                                                        "-fx-font-weight:bold;");

                } else {

                        myStatus = new Label("● ASSIGNED");

                        myStatus.setStyle(
                                        "-fx-text-fill:green;" +
                                                        "-fx-font-size:15px;" +
                                                        "-fx-font-weight:bold;");
                }

                VBox myParkingCard = new VBox(
                                10,
                                myParkingTitle,
                                mySlot,
                                myStatus);

                myParkingCard.setPadding(
                                new Insets(20));

                myParkingCard.setMaxWidth(
                                Double.MAX_VALUE);

                myParkingCard.setStyle(
                                "-fx-background-color:white;" +
                                                "-fx-background-radius:12;" +
                                                "-fx-border-color:#789098;" +
                                                "-fx-border-radius:12;");

                // =========================================================
                // PARKING AVAILABILITY TITLE
                // =========================================================

                Label availabilityTitle = new Label("Parking Availability");

                availabilityTitle.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #4e342e;"

                );

                // =========================================================
                // PARKING GRID
                // =========================================================

                GridPane parkingGrid = new GridPane();

                parkingGrid.setHgap(20);
                parkingGrid.setVgap(20);
                parkingGrid.setAlignment(Pos.CENTER);

                // =========================================================
                // DISPLAY ALL SLOTS
                // =========================================================

                for (int i = 0; i < SLOT_NAMES.length; i++) {

                        addParkingSlot(
                                        parkingGrid,
                                        SLOT_NAMES[i],
                                        i % 4,
                                        i / 4);
                }

                // =========================================================
                // SIMULATE PARKING
                // =========================================================

                Button simulateButton = new Button(
                                "🚗  Simulate Car Parking");

                simulateButton.setStyle(
                                "-fx-background-color: #4e342e;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-padding: 10px 20px;" +
                                                "-fx-background-radius: 8px;"

                );

                simulateButton.setOnAction(event -> {

                        if (myParkingSlots.isEmpty()) {

                                showAlert(
                                                Alert.AlertType.INFORMATION,
                                                "Smart Parking",
                                                "No Parking Assigned",
                                                "You currently do not have an assigned parking slot.");

                                return;
                        }

                        myStatus.setText(
                                        "● OCCUPIED");

                        myStatus.setStyle(
                                        "-fx-text-fill:red;" +
                                                        "-fx-font-size:15px;" +
                                                        "-fx-font-weight:bold;");

                        showAlert(
                                        Alert.AlertType.WARNING,
                                        "Smart Parking Alert",
                                        "Parking Alert!",
                                        "A vehicle has been detected in your parking area "
                                                        + getMySlotNames()
                                                        + ".\n\nPlease check your parking slot.");
                });

                // =========================================================
                // PARKING AREA
                // =========================================================

                VBox parkingArea = new VBox(
                                20,
                                availabilityTitle,
                                parkingGrid,
                                simulateButton);

                parkingArea.setPadding(
                                new Insets(25));

                parkingArea.setAlignment(
                                Pos.CENTER);

                parkingArea.setStyle(
                                "-fx-background-color:white;" +
                                                "-fx-background-radius:15;" +
                                                "-fx-border-color:#789098;" +
                                                "-fx-border-radius:15;");

                mainContent.getChildren().addAll(
                                myParkingCard,
                                parkingArea);

                // =========================================================
                // SCROLL PANE
                // =========================================================

                ScrollPane scrollPane = new ScrollPane(mainContent);

                scrollPane.setFitToWidth(true);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scrollPane.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.setStyle(
                                "-fx-background: transparent;" +
                                                "-fx-background-color: transparent;");

                // =========================================================
                // CENTER
                // =========================================================

                BorderPane centerPane = new BorderPane();

                centerPane.setTop(header);
                centerPane.setCenter(scrollPane);

                // =========================================================
                // ROOT
                // =========================================================

                // centerPane.setCenter(mainarea);

                // ==================================================
                // MAIN BORDERPANE
                // ==================================================

                // BorderPane borderPane =
                // new BorderPane();
                BorderPane root = new BorderPane();

                root.setLeft(
                                panelObj.getSidebar());

                root.setCenter(
                                centerPane);

                // Entire background

                // borderPane.setStyle(
                // "-fx-background-color: #b3adad;"
                // );
                root.setStyle(
                                "-fx-background-color:#e8ddd5;");

                // =========================================================
                // SCENE
                // =========================================================

                return new Scene(
                                root,
                                ScreenSize.getWidth(),
                                ScreenSize.getHeight());
        }

        // =========================================================
        // FETCH PARKING DATA FROM FIRESTORE
        // =========================================================

        private void fetchParkingData() {

                try {

                        Firestore db = FirebaseConfig.getFirestore();

                        System.out.println();
                        System.out.println("-----------------------------------------------");
                        System.out.println("START PARKING FETCH");
                        System.out.println("Resident Email = " + loggedInEmail);
                        System.out.println("-----------------------------------------------");

                        // =====================================================
                        // STEP 1
                        // GET RESIDENT DOCUMENT
                        // =====================================================

                        DocumentSnapshot residentDocument = findResidentDocument(db);

                        if (residentDocument == null ||
                                        !residentDocument.exists()) {

                                System.out.println(
                                                "Resident document NOT FOUND: "
                                                                + loggedInEmail);

                                return;
                        }

                        System.out.println(
                                        "Resident document FOUND: "
                                                        + residentDocument.getId());

                        // =====================================================
                        // STEP 2
                        // GET SOCIETY
                        // =====================================================

                        residentSociety = firstNonEmpty(
                                        getStringValue(
                                                        residentDocument,
                                                        "society"),
                                        getStringValue(
                                                        residentDocument,
                                                        "societyName"),
                                        getStringValue(
                                                        residentDocument,
                                                        "Society"),
                                        getStringValue(
                                                        residentDocument,
                                                        "SocietyName"));

                        residentSociety = safe(residentSociety);

                        System.out.println(
                                        "Resident Society = "
                                                        + residentSociety);

                        if (residentSociety.isEmpty()) {

                                System.out.println(
                                                "ERROR: Resident society is empty.");

                                return;
                        }

                        // =====================================================
                        // STEP 3
                        // FIND ParkingSlots/{society}
                        // =====================================================

                        DocumentSnapshot societyDocument = findSocietyDocument(
                                        db,
                                        residentSociety);

                        if (societyDocument == null ||
                                        !societyDocument.exists()) {

                                System.out.println(
                                                "ParkingSlots society NOT FOUND: "
                                                                + residentSociety);

                                return;
                        }

                        System.out.println(
                                        "Parking society FOUND = "
                                                        + societyDocument.getId());

                        // =====================================================
                        // STEP 4
                        // READ SECRETARY COLLECTIONS
                        //
                        // ParkingSlots
                        // |
                        // +-- stanza
                        // |
                        // +-- aditi@gmail.com
                        // |
                        // +-- P-01
                        // +-- P-02
                        // +-- P-03
                        // =====================================================

                        Iterable<CollectionReference> secretaryCollections = societyDocument
                                        .getReference()
                                        .listCollections();

                        int secretaryCount = 0;
                        int slotCount = 0;

                        for (CollectionReference secretaryCollection : secretaryCollections) {

                                secretaryCount++;

                                String secretaryEmail = secretaryCollection.getId();

                                System.out.println(
                                                "Secretary Collection = "
                                                                + secretaryEmail);

                                // =================================================
                                // GET ALL SLOT DOCUMENTS
                                // =================================================

                                QuerySnapshot slots = secretaryCollection
                                                .get()
                                                .get();

                                for (DocumentSnapshot slotDocument : slots.getDocuments()) {

                                        slotCount++;

                                        String slotName = safe(slotDocument.getId());

                                        if (slotName.isEmpty()) {
                                                continue;
                                        }

                                        Map<String, Object> data = slotDocument.getData();

                                        if (data == null) {
                                                continue;
                                        }

                                        // =================================================
                                        // GET ALLOCATED EMAIL
                                        // =================================================

                                        String allocatedToEmail = firstNonEmpty(
                                                        objectString(
                                                                        data.get(
                                                                                        "allocatedToEmail")),
                                                        objectString(
                                                                        data.get(
                                                                                        "memberEmail")),
                                                        objectString(
                                                                        data.get(
                                                                                        "allocatedTo")),
                                                        objectString(
                                                                        data.get(
                                                                                        "email")));

                                        allocatedToEmail = normalizeEmail(
                                                        allocatedToEmail);

                                        // =================================================
                                        // GET NAME
                                        // =================================================

                                        String allocatedToName = firstNonEmpty(
                                                        objectString(
                                                                        data.get(
                                                                                        "allocatedToName")),
                                                        objectString(
                                                                        data.get(
                                                                                        "memberName")),
                                                        objectString(
                                                                        data.get(
                                                                                        "name")));

                                        // =================================================
                                        // GET FLAT
                                        // =================================================

                                        String flatNo = firstNonEmpty(
                                                        objectString(
                                                                        data.get(
                                                                                        "flatNo")),
                                                        objectString(
                                                                        data.get(
                                                                                        "flat")));

                                        // =================================================
                                        // GET MEMBER SOCIETY
                                        // =================================================

                                        String memberSociety = firstNonEmpty(
                                                        objectString(
                                                                        data.get(
                                                                                        "memberSociety")),
                                                        objectString(
                                                                        data.get(
                                                                                        "society")),
                                                        residentSociety);

                                        // =================================================
                                        // GET STATUS
                                        // =================================================

                                        String status = firstNonEmpty(
                                                        objectString(
                                                                        data.get(
                                                                                        "status")),
                                                        "Allocated");

                                        // =================================================
                                        // DEBUG
                                        // =================================================

                                        System.out.println();
                                        System.out.println(
                                                        "Slot              = "
                                                                        + slotName);

                                        System.out.println(
                                                        "allocatedToEmail  = "
                                                                        + allocatedToEmail);

                                        System.out.println(
                                                        "memberEmail       = "
                                                                        + objectString(
                                                                                        data.get(
                                                                                                        "memberEmail")));

                                        System.out.println(
                                                        "allocatedToName   = "
                                                                        + allocatedToName);

                                        System.out.println(
                                                        "flatNo            = "
                                                                        + flatNo);

                                        System.out.println(
                                                        "memberSociety     = "
                                                                        + memberSociety);

                                        System.out.println(
                                                        "LoggedInEmail     = "
                                                                        + loggedInEmail);

                                        // =================================================
                                        // CREATE ASSIGNMENT
                                        // =================================================

                                        ParkingAssignment assignment = new ParkingAssignment(
                                                        slotName,
                                                        allocatedToEmail,
                                                        allocatedToName,
                                                        flatNo,
                                                        status,
                                                        memberSociety);

                                        // =================================================
                                        // STORE SLOT
                                        // =================================================

                                        slotAssignments.put(
                                                        normalizeSlot(slotName),
                                                        assignment);

                                        // =================================================
                                        // CHECK CURRENT RESIDENT
                                        //
                                        // IMPORTANT:
                                        // EMAIL ONLY
                                        // =================================================

                                        if (sameEmail(
                                                        loggedInEmail,
                                                        allocatedToEmail)) {

                                                System.out.println(
                                                                "******** MY PARKING FOUND ********");

                                                System.out.println(
                                                                "Resident = "
                                                                                + loggedInEmail);

                                                System.out.println(
                                                                "Slot = "
                                                                                + slotName);

                                                if (!containsMySlot(slotName)) {

                                                        myParkingSlots.add(
                                                                        assignment);
                                                }
                                        }
                                }
                        }

                        // =====================================================
                        // FINAL RESULT
                        // =====================================================

                        System.out.println();
                        System.out.println("-----------------------------------------------");
                        System.out.println(
                                        "Secretary Collections = "
                                                        + secretaryCount);

                        System.out.println(
                                        "Total Slot Documents = "
                                                        + slotCount);

                        System.out.println(
                                        "My Parking Slots = "
                                                        + getMySlotNames());

                        System.out.println(
                                        "My Parking Count = "
                                                        + myParkingSlots.size());

                        System.out.println("-----------------------------------------------");

                        if (myParkingSlots.isEmpty()) {

                                System.out.println(
                                                "RESULT = NOT ALLOCATED");

                        } else {

                                System.out.println(
                                                "RESULT = PARKING ASSIGNED");
                        }

                        System.out.println(
                                        "================================================");

                } catch (Exception e) {

                        System.err.println();
                        System.err.println(
                                        "ERROR FETCHING PARKING DATA");

                        e.printStackTrace();

                        resetParkingData();
                }
        }

        // =========================================================
        // FIND RESIDENT DOCUMENT
        // =========================================================

        private DocumentSnapshot findResidentDocument(
                        Firestore db) throws Exception {

                // ---------------------------------------------------------
                // FIRST: Residents/{email}
                // ---------------------------------------------------------

                DocumentSnapshot document = db.collection("Residents")
                                .document(loggedInEmail)
                                .get()
                                .get();

                if (document.exists()) {
                        return document;
                }

                // ---------------------------------------------------------
                // SECOND: residents/{email}
                // ---------------------------------------------------------

                document = db.collection("residents")
                                .document(loggedInEmail)
                                .get()
                                .get();

                if (document.exists()) {
                        return document;
                }

                // ---------------------------------------------------------
                // THIRD: SEARCH Residents BY EMAIL FIELD
                // ---------------------------------------------------------

                QuerySnapshot residents = db.collection("Residents")
                                .get()
                                .get();

                for (DocumentSnapshot resident : residents.getDocuments()) {

                        String email = firstNonEmpty(
                                        getStringValue(
                                                        resident,
                                                        "email"),
                                        getStringValue(
                                                        resident,
                                                        "residentEmail"),
                                        resident.getId());

                        if (sameEmail(
                                        loggedInEmail,
                                        email)) {

                                return resident;
                        }
                }

                // ---------------------------------------------------------
                // FOURTH: SEARCH lowercase residents
                // ---------------------------------------------------------

                QuerySnapshot lowerResidents = db.collection("residents")
                                .get()
                                .get();

                for (DocumentSnapshot resident : lowerResidents.getDocuments()) {

                        String email = firstNonEmpty(
                                        getStringValue(
                                                        resident,
                                                        "email"),
                                        getStringValue(
                                                        resident,
                                                        "residentEmail"),
                                        resident.getId());

                        if (sameEmail(
                                        loggedInEmail,
                                        email)) {

                                return resident;
                        }
                }

                return null;
        }

        // =========================================================
        // FIND SOCIETY DOCUMENT
        // =========================================================

        private DocumentSnapshot findSocietyDocument(
                        Firestore db,
                        String society) throws Exception {

                // ---------------------------------------------------------
                // EXACT DOCUMENT
                // ---------------------------------------------------------

                DocumentSnapshot document = db.collection("ParkingSlots")
                                .document(society)
                                .get()
                                .get();

                if (document.exists()) {
                        return document;
                }

                // ---------------------------------------------------------
                // CASE-INSENSITIVE SEARCH
                // ---------------------------------------------------------

                QuerySnapshot societies = db.collection("ParkingSlots")
                                .get()
                                .get();

                for (DocumentSnapshot societyDocument : societies.getDocuments()) {

                        String storedSociety = firstNonEmpty(
                                        getStringValue(
                                                        societyDocument,
                                                        "society"),
                                        societyDocument.getId());

                        if (sameText(
                                        society,
                                        storedSociety)) {

                                return societyDocument;
                        }
                }

                return null;
        }

        // =========================================================
        // ADD SLOT TO UI
        // =========================================================

        private void addParkingSlot(
                        GridPane grid,
                        String slotName,
                        int column,
                        int row) {

                Label slot = new Label();

                slot.setPrefSize(
                                250,
                                150);

                slot.setAlignment(
                                Pos.CENTER);

                ParkingAssignment assignment = slotAssignments.get(
                                normalizeSlot(slotName));

                // =====================================================
                // CHECK MY SLOT
                // =====================================================

                boolean isMyParking = isMySlot(slotName);

                // =====================================================
                // CHECK OTHER PERSON
                // =====================================================

                boolean isAssignedToOther = assignment != null
                                && !isMyParking
                                && !getAssignmentEmail(
                                                assignment).isEmpty();

                // =====================================================
                // MY PARKING
                // =====================================================

                if (isMyParking) {

                        String flat = getMyFlatForSlot(
                                        slotName);

                        if (!flat.isEmpty()) {

                                slot.setText(
                                                "⭐ " + slotName
                                                                + "\nMY PARKING"
                                                                + "\nFlat: " + flat);

                        } else {

                                slot.setText(
                                                "⭐ " + slotName
                                                                + "\nMY PARKING");
                        }

                        slot.setStyle(
                                        "-fx-background-color:#81c784;" +
                                                        "-fx-text-fill:white;" +
                                                        "-fx-font-size:15px;" +
                                                        "-fx-font-weight:bold;" +
                                                        "-fx-background-radius:10;" +
                                                        "-fx-border-color:#2e7d32;" +
                                                        "-fx-border-width:3;" +
                                                        "-fx-border-radius:10;");
                }

                // =====================================================
                // ASSIGNED TO OTHER RESIDENT
                // =====================================================

                else if (isAssignedToOther) {

                        slot.setText(
                                        slotName
                                                        + "\nALLOCATED");

                        slot.setStyle(
                                        "-fx-background-color:#e57373;" +
                                                        "-fx-text-fill:white;" +
                                                        "-fx-font-size:15px;" +
                                                        "-fx-font-weight:bold;" +
                                                        "-fx-background-radius:10;");
                }

                // =====================================================
                // AVAILABLE
                // =====================================================

                else {

                        slot.setText(
                                        slotName
                                                        + "\nAVAILABLE");

                        slot.setStyle(
                                        "-fx-background-color:#a5d6a7;" +
                                                        "-fx-text-fill:#1b5e20;" +
                                                        "-fx-font-size:15px;" +
                                                        "-fx-font-weight:bold;" +
                                                        "-fx-background-radius:10;");
                }

                grid.add(
                                slot,
                                column,
                                row);
        }

        // =========================================================
        // CHECK MY SLOT
        // =========================================================

        private boolean isMySlot(
                        String slotName) {

                for (ParkingAssignment assignment : myParkingSlots) {

                        if (assignment == null) {
                                continue;
                        }

                        if (sameSlot(
                                        slotName,
                                        assignment.getSlot())) {

                                return true;
                        }
                }

                return false;
        }

        // =========================================================
        // CHECK DUPLICATE
        // =========================================================

        private boolean containsMySlot(
                        String slotName) {

                return isMySlot(slotName);
        }

        // =========================================================
        // GET MY FLAT
        // =========================================================

        private String getMyFlatForSlot(
                        String slotName) {

                for (ParkingAssignment assignment : myParkingSlots) {

                        if (assignment == null) {
                                continue;
                        }

                        if (sameSlot(
                                        slotName,
                                        assignment.getSlot())) {

                                return safe(
                                                assignment.getFlat());
                        }
                }

                return "";
        }

        // =========================================================
        // GET MY SLOT NAMES
        // =========================================================

        private String getMySlotNames() {

                List<String> slots = new ArrayList<>();

                for (ParkingAssignment assignment : myParkingSlots) {

                        if (assignment == null) {
                                continue;
                        }

                        String slot = safe(
                                        assignment.getSlot());

                        if (!slot.isEmpty()) {

                                slots.add(slot);
                        }
                }

                if (slots.isEmpty()) {

                        return "None";
                }

                return String.join(
                                ", ",
                                slots);
        }

        // =========================================================
        // BUILD MY PARKING TEXT
        // =========================================================

        private String buildMyParkingText() {

                if (myParkingSlots.isEmpty()) {

                        return "Assigned Parking: NOT ALLOCATED";
                }

                return "Assigned Parking: "
                                + getMySlotNames();
        }

        // =========================================================
        // GET ASSIGNMENT EMAIL
        // =========================================================

        private String getAssignmentEmail(
                        ParkingAssignment assignment) {

                if (assignment == null) {

                        return "";
                }

                return normalizeEmail(
                                assignment.getMemberEmail());
        }

        // =========================================================
        // GET STRING FIELD
        // =========================================================

        private String getStringValue(
                        DocumentSnapshot document,
                        String field) {

                if (document == null ||
                                field == null) {

                        return "";
                }

                Object value = document.get(field);

                return objectString(value);
        }

        // =========================================================
        // OBJECT TO STRING
        // =========================================================

        private String objectString(
                        Object value) {

                if (value == null) {

                        return "";
                }

                return String.valueOf(
                                value).trim();
        }

        // =========================================================
        // FIRST NON EMPTY
        // =========================================================

        private String firstNonEmpty(
                        String... values) {

                if (values == null) {

                        return "";
                }

                for (String value : values) {

                        if (value != null &&
                                        !value.trim().isEmpty()) {

                                return value.trim();
                        }
                }

                return "";
        }

        // =========================================================
        // NORMALIZE EMAIL
        // =========================================================

        private String normalizeEmail(
                        String email) {

                if (email == null) {

                        return "";
                }

                return email
                                .trim()
                                .toLowerCase();
        }

        // =========================================================
        // COMPARE EMAIL
        // =========================================================

        private boolean sameEmail(
                        String email1,
                        String email2) {

                String e1 = normalizeEmail(email1);

                String e2 = normalizeEmail(email2);

                return !e1.isEmpty()
                                && !e2.isEmpty()
                                && e1.equals(e2);
        }

        // =========================================================
        // NORMALIZE SLOT
        // =========================================================

        private String normalizeSlot(
                        String slot) {

                if (slot == null) {

                        return "";
                }

                return slot
                                .trim()
                                .toLowerCase();
        }

        // =========================================================
        // COMPARE SLOT
        // =========================================================

        private boolean sameSlot(
                        String slot1,
                        String slot2) {

                return normalizeSlot(slot1)
                                .equals(
                                                normalizeSlot(slot2));
        }

        // =========================================================
        // COMPARE TEXT
        // =========================================================

        private boolean sameText(
                        String text1,
                        String text2) {

                if (text1 == null ||
                                text2 == null) {

                        return false;
                }

                return text1
                                .trim()
                                .equalsIgnoreCase(
                                                text2.trim());
        }

        // =========================================================
        // SAFE STRING
        // =========================================================

        private String safe(
                        String value) {

                return value == null
                                ? ""
                                : value.trim();
        }

        // =========================================================
        // RESET DATA
        // =========================================================

        private void resetParkingData() {

                myParkingSlots.clear();

                slotAssignments.clear();

                residentSociety = "";
        }

        // =========================================================
        // ALERT
        // =========================================================

        private void showAlert(
                        Alert.AlertType type,
                        String title,
                        String header,
                        String message) {

                Alert alert = new Alert(type);

                alert.setTitle(title);
                alert.setHeaderText(header);
                alert.setContentText(message);

                alert.showAndWait();
        }

        // =========================================================
        // PARKING ASSIGNMENT MODEL
        // =========================================================

        public static class ParkingAssignment {

                private final String slot;
                private final String memberEmail;
                private final String memberName;
                private final String flat;
                private final String status;
                private final String society;

                public ParkingAssignment(
                                String slot,
                                String memberEmail,
                                String memberName,
                                String flat,
                                String status,
                                String society) {

                        this.slot = slot == null
                                        ? ""
                                        : slot.trim();

                        this.memberEmail = memberEmail == null
                                        ? ""
                                        : memberEmail.trim();

                        this.memberName = memberName == null
                                        ? ""
                                        : memberName.trim();

                        this.flat = flat == null
                                        ? ""
                                        : flat.trim();

                        this.status = status == null
                                        ? ""
                                        : status.trim();

                        this.society = society == null
                                        ? ""
                                        : society.trim();
                }

                public String getSlot() {
                        return slot;
                }

                public String getMemberEmail() {
                        return memberEmail;
                }

                public String getMemberName() {
                        return memberName;
                }

                public String getFlat() {
                        return flat;
                }

                public String getStatus() {
                        return status;
                }

                public String getSociety() {
                        return society;
                }
        }
}