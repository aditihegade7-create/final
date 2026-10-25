package com.society.view.Secretary_portal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.dao.Welcome.UserDao;
import com.society.model.Welcome.User;
import com.society.view.ScreenSize;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


// =========================================================
// VIEW SOS
// =========================================================

public class ViewSos {

    // =========================================================
    // SCENE
    // =========================================================

    private Scene viewSosScene;

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore db =
            FirebaseConfig.getFirestore();

    // =========================================================
    // COLORS
    // =========================================================

    private static final String BACKGROUND = "#B3ADAD";
    private static final String DARK = "#4e342e";
    private static final String TEXT_DARK = "#333333";
    private static final String GREEN = "#123C36";
    private static final String BORDER = "#EEEEEE";
    private static final String WHITE = "#FFFFFF";
    private static final String GREY = "#4e342e";

    // =========================================================
    // BACKGROUND IMAGE
    // =========================================================

    private static final String BACKGROUND_IMAGE =
            "/background-Dashboard5.jpeg";

    // =========================================================
    // DATA
    // =========================================================

    private final List<SosAlertData> allAlerts =
            new ArrayList<>();

    // =========================================================
    // UI
    // =========================================================

    private VBox sosList;

    private Button activeBtn;
    private Button resolvedBtn;
    private Button allBtn;

    private Button refreshBtn;

    // =========================================================
    // CURRENT SOCIETY
    // =========================================================

    private String currentSociety = "";

    // =========================================================
    // CURRENT FILTER
    // =========================================================

    private String currentFilter = "ACTIVE";

    // =========================================================
    // CREATE SCENE
    // =========================================================

    public Scene createScene(Stage stage) {

        // =====================================================
        // GET LOGGED-IN SECRETARY SOCIETY
        // =====================================================

        loadCurrentSociety();

        // =====================================================
        // SIDEBAR
        // =====================================================

        SecretarySidebar sidebarObj =
                new SecretarySidebar();

        VBox sidebar =
                sidebarObj.createSidebar(stage);

        // =====================================================
        // RIGHT SIDE
        // HEADER + CONTENT
        // =====================================================

        VBox rightSide =
                new VBox();

        rightSide.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        rightSide.setStyle(
                "-fx-background-color:transparent;"
        );

        // =====================================================
        // HEADER
        // HEADER IS DIRECTLY ATTACHED WITH SIDEBAR
        // =====================================================

        HBox header =
                new HBox();

        header.setPrefHeight(80);
        header.setMinHeight(80);
        header.setMaxHeight(80);

        header.setPadding(
                new Insets(20)
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setStyle(
                "-fx-background-color: #4e342e;"
        );

        // =====================================================
        // GREETING
        // =====================================================

        Label greeting =
                new Label( "SOS Alert"
                      
                );

        greeting.setStyle( "-fx-font-family:'Bodoni MT';" +
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

         Label subtitle =
                new Label(
                        "View emergency alerts from residents and guards"
                );

        subtitle.setStyle( "-fx-font-family:'Bodoni MT';" +
                "-fx-font-size:13px;" +
                "-fx-text-fill:" + GREY + ";"
        );

        // =====================================================
        // HEADER SPACER
        // =====================================================

        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );

        // =====================================================
        // NOTIFICATION
        // =====================================================

        Label notification =
                new Label("🔔");

        notification.setStyle(
                "-fx-font-size:20px;" +
                "-fx-text-fill:white;"
        );

        // =====================================================
        // CURRENT DATE
        // =====================================================

        LocalDate today =
                LocalDate.now();

        Label day =
                new Label(
                        today.format(
                                DateTimeFormatter.ofPattern(
                                        "EEEE"
                                )
                        )
                );

        day.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:white;"
        );

        Label date =
                new Label(
                        today.format(
                                DateTimeFormatter.ofPattern(
                                        "dd MMMM yyyy"
                                )
                        )
                );

        date.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:white;"
        );

        // =====================================================
        // DATE BOX
        // =====================================================

        VBox dateBox =
                new VBox(
                        2,
                        day,
                        date
                );

        dateBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        // =====================================================
        // HEADER CONTENT
        // =====================================================

        header.getChildren().addAll(
                greeting,
                subtitle,
                headerSpacer,
                notification,
                dateBox
        );

        // =====================================================
        // ALERT CONTENT PANEL
        // =====================================================

        VBox mainvb =
                new VBox(20);

        mainvb.setPadding(
                new Insets(20, 20, 20, 20)
        );

        mainvb.setPrefWidth(1220);

        mainvb.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        mainvb.setStyle(
                "-fx-background-color:" + BACKGROUND + ";"
        );

        // =====================================================
        // HEADING
        // =====================================================


        // =====================================================
        // TITLE
        // =====================================================


        // =====================================================
        // SUBTITLE
        // =====================================================

       

        // =====================================================
        // TITLE BOX
        // =====================================================

        
       

        // =====================================================
        // FILTER BUTTONS
        // =====================================================

        activeBtn =
                new Button("Active (0)");

        resolvedBtn =
                new Button("Resolved (0)");

        allBtn =
                new Button("All Alerts (0)");

        activeBtn.setPrefWidth(140);
        activeBtn.setPrefHeight(40);

        resolvedBtn.setPrefWidth(140);
        resolvedBtn.setPrefHeight(40);

        allBtn.setPrefWidth(140);
        allBtn.setPrefHeight(40);

        // =====================================================
        // REFRESH BUTTON
        // =====================================================

        refreshBtn =
                new Button("↻");

        refreshBtn.setPrefWidth(45);
        refreshBtn.setPrefHeight(40);

        refreshBtn.setTooltip(
                new Tooltip("Refresh SOS Alerts")
        );

        refreshBtn.setStyle(
                "-fx-background-color:" + DARK + ";" +
                "-fx-text-fill:white;" +
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        // =====================================================
        // BUTTON STYLES
        // =====================================================

        String normalStyle =
                "-fx-background-color:transparent;" +
                "-fx-text-fill:" + GREY + ";" +
                "-fx-font-weight:bold;" +
                "-fx-font-size:12px;" +
                "-fx-cursor:hand;";

        String activeStyle =
                "-fx-background-color:transparent;" +
                "-fx-text-fill:" + GREEN + ";" +
                "-fx-font-weight:bold;" +
                "-fx-font-size:12px;" +
                "-fx-border-color:#0B4F4A;" +
                "-fx-border-width:0 0 2 0;" +
                "-fx-cursor:hand;";

        activeBtn.setStyle(activeStyle);
        resolvedBtn.setStyle(normalStyle);
        allBtn.setStyle(normalStyle);

        // =====================================================
        // TABS
        // =====================================================

        HBox tabs =
                new HBox(25);

        tabs.setAlignment(
                Pos.CENTER_LEFT
        );

        tabs.getChildren().addAll(
                activeBtn,
                resolvedBtn,
                allBtn
        );

        // =====================================================
        // FILTER AREA
        // =====================================================

        HBox filterArea =
                new HBox(10);

        filterArea.setAlignment(
                Pos.CENTER_LEFT
        );

        Region filterSpacer =
                new Region();

        HBox.setHgrow(
                filterSpacer,
                Priority.ALWAYS
        );

        filterArea.getChildren().addAll(
                tabs,
                filterSpacer,
                refreshBtn
        );

        // =====================================================
        // SOS LIST
        // =====================================================

        sosList =
                new VBox(15);

        sosList.setPadding(
                new Insets(5, 0, 5, 0)
        );

        sosList.setFillWidth(true);

        sosList.setStyle(
                "-fx-background-color:transparent;"
        );

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                sosList
        );

        scrollPane.setFitToWidth(true);

        scrollPane.setFitToHeight(false);

        scrollPane.setPrefHeight(500);

        scrollPane.setMaxHeight(
                Double.MAX_VALUE
        );

        // =====================================================
        // INVISIBLE SCROLLBARS
        // =====================================================

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setPannable(true);

        scrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;" +
                "-fx-border-color:transparent;" +
                "-fx-padding:0;"
        );

        // =====================================================
        // ACTIVE BUTTON
        // =====================================================

        activeBtn.setOnAction(e -> {

            currentFilter = "ACTIVE";

            activeBtn.setStyle(activeStyle);
            resolvedBtn.setStyle(normalStyle);
            allBtn.setStyle(normalStyle);

            displayAlerts(
                    currentFilter
            );
        });

        // =====================================================
        // RESOLVED BUTTON
        // =====================================================

        resolvedBtn.setOnAction(e -> {

            currentFilter = "RESOLVED";

            activeBtn.setStyle(normalStyle);
            resolvedBtn.setStyle(activeStyle);
            allBtn.setStyle(normalStyle);

            displayAlerts(
                    currentFilter
            );
        });

        // =====================================================
        // ALL BUTTON
        // =====================================================

        allBtn.setOnAction(e -> {

            currentFilter = "ALL";

            activeBtn.setStyle(normalStyle);
            resolvedBtn.setStyle(normalStyle);
            allBtn.setStyle(activeStyle);

            displayAlerts(
                    currentFilter
            );
        });

        // =====================================================
        // REFRESH
        // =====================================================

        refreshBtn.setOnAction(e -> {

            fetchEmergencyAlerts();

        });

        // =====================================================
        // VIEW ALL BUTTON
        // =====================================================

        Button viewAllBtn =
                new Button(
                        "View All SOS Alerts"
                );

        viewAllBtn.setPrefHeight(40);

        viewAllBtn.setMaxWidth(
                Double.MAX_VALUE
        );

        viewAllBtn.setStyle(
                "-fx-background-color:" + DARK + ";" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:7;" +
                "-fx-cursor:hand;"
        );

        viewAllBtn.setOnAction(e -> {

            currentFilter = "ALL";

            activeBtn.setStyle(normalStyle);
            resolvedBtn.setStyle(normalStyle);
            allBtn.setStyle(activeStyle);

            displayAlerts("ALL");
        });

        // =====================================================
        // ADD CONTENT TO MAIN PANEL
        // =====================================================

        mainvb.getChildren().addAll(
               
                filterArea,
                scrollPane,
                viewAllBtn
        );

        // =====================================================
        // CONTENT HOLDER
        // 12px GAP BETWEEN SIDEBAR AND ALERT PANEL
        // =====================================================

        HBox contentHolder =
                new HBox();

        contentHolder.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        contentHolder.setPadding(
                new Insets(0, 0, 0, 12)
        );

        contentHolder.setStyle(
                "-fx-background-color:transparent;"
        );

        contentHolder.getChildren().add(
                mainvb
        );

        HBox.setHgrow(
                mainvb,
                Priority.ALWAYS
        );

        VBox.setVgrow(
                contentHolder,
                Priority.ALWAYS
        );

        // =====================================================
        // RIGHT SIDE = HEADER + CONTENT
        // =====================================================

        rightSide.getChildren().addAll(
                header,
                contentHolder
        );

        VBox.setVgrow(
                contentHolder,
                Priority.ALWAYS
        );

        // =====================================================
        // ROOT
        // SIDEBAR + RIGHT SIDE
        // HEADER ATTACHED TO SIDEBAR
        // =====================================================

        HBox root =
                new HBox();

        root.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        root.setStyle(
                "-fx-background-color:transparent;"
        );

        root.getChildren().addAll(
                sidebar,
                rightSide
        );

        HBox.setHgrow(
                rightSide,
                Priority.ALWAYS
        );

        // =====================================================
        // SCENE
        // =====================================================

        viewSosScene =
                new Scene(
                        root,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        stage.setTitle(
                "Society360 - View SOS Alerts"
        );

        stage.setScene(
                viewSosScene
        );

        stage.show();

        // =====================================================
        // BACKGROUND IMAGE
        // ONLY ALERT CONTENT AREA
        // =====================================================

        try {

            if (getClass().getResource(
                    BACKGROUND_IMAGE
            ) != null) {

                Image backgroundImage =
                        new Image(
                                getClass()
                                        .getResource(
                                                BACKGROUND_IMAGE
                                        )
                                        .toExternalForm()
                        );

                BackgroundImage bgImage =
                        new BackgroundImage(
                                backgroundImage,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(
                                        100,
                                        100,
                                        true,
                                        true,
                                        false,
                                        true
                                )
                        );

                mainvb.setBackground(
                        new Background(
                                bgImage
                        )
                );

            } else {

                System.err.println(
                        "Background image not found: "
                                + BACKGROUND_IMAGE
                );

                mainvb.setStyle(
                        "-fx-background-color:"
                                + BACKGROUND
                                + ";"
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "Background image could not be loaded: "
                            + e.getMessage()
            );

            mainvb.setStyle(
                    "-fx-background-color:"
                            + BACKGROUND
                            + ";"
            );
        }

        // =====================================================
        // FETCH FIRESTORE
        // =====================================================

        fetchEmergencyAlerts();

        return viewSosScene;
    }


    // =========================================================
    // GET GREETING
    // =========================================================

    private String getGreeting() {

        int hour =
                LocalTime.now().getHour();

        if (hour >= 5 && hour < 12) {

            return "Good Morning";

        } else if (hour >= 12 && hour < 17) {

            return "Good Afternoon";

        } else if (hour >= 17 && hour < 21) {

            return "Good Evening";

        } else {

            return "Good Night";
        }
    }


    // =========================================================
    // LOAD CURRENT SOCIETY
    // =========================================================

    private void loadCurrentSociety() {

        try {

            String loggedEmail =
                    UserDao.getLoggedInEmail();

            if (loggedEmail == null ||
                    loggedEmail.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Logged-in email not found."
                );

                currentSociety = "";

                return;
            }

            UserDao dao =
                    new UserDao();

            User secretary =
                    dao.getUserByEmail(
                            loggedEmail
                    );

            if (secretary != null) {

                currentSociety =
                        safeString(
                                secretary.getSociety()
                        );
            }

            System.out.println(
                    "Logged-in Secretary = "
                    + loggedEmail
            );

            System.out.println(
                    "Secretary Society = "
                    + currentSociety
            );

        } catch (Exception e) {

            System.out.println(
                    "Error loading secretary society."
            );

            e.printStackTrace();

            currentSociety = "";
        }
    }


    // =========================================================
    // FETCH EMERGENCY ALERTS
    // =========================================================

    private void fetchEmergencyAlerts() {

        if (refreshBtn != null) {

            refreshBtn.setDisable(true);
        }

        new Thread(() -> {

            try {

                synchronized (allAlerts) {

                    allAlerts.clear();
                }

                if (currentSociety == null ||
                        currentSociety.trim().isEmpty()) {

                    Platform.runLater(() -> {

                        sosList.getChildren().clear();

                        Label error =
                                new Label(
                                        "Secretary society not found."
                                );

                        error.setStyle(
                                "-fx-font-size:14px;" +
                                "-fx-text-fill:#D9534F;" +
                                "-fx-font-weight:bold;"
                        );

                        sosList.getChildren().add(
                                error
                        );

                        if (refreshBtn != null) {

                            refreshBtn.setDisable(false);
                        }
                    });

                    return;
                }

                CollectionReference emergencyCollection =
                        db.collection(
                                "emergency_alerts"
                        );

                ApiFuture<QuerySnapshot> parentFuture =
                        emergencyCollection.get();

                QuerySnapshot parentSnapshot =
                        parentFuture.get();

                System.out.println(
                        "Total email documents = "
                        + parentSnapshot.size()
                );

                for (QueryDocumentSnapshot emailDocument :
                        parentSnapshot.getDocuments()) {

                    String parentEmail =
                            safeString(
                                    emailDocument.getString("email")
                            );

                    if (parentEmail.isEmpty()) {

                        parentEmail =
                                safeString(
                                        emailDocument.getId()
                                );
                    }

                    String parentSociety =
                            safeString(
                                    emailDocument.getString("society")
                            );

                    if (parentSociety.isEmpty()) {

                        System.out.println(
                                "Skipping email because society missing: "
                                + parentEmail
                        );

                        continue;
                    }

                    if (!parentSociety.equalsIgnoreCase(
                            currentSociety)) {

                        System.out.println(
                                "Skipping different society: "
                                + parentEmail
                                + " -> "
                                + parentSociety
                        );

                        continue;
                    }

                    System.out.println(
                            "Fetching SOS for email: "
                            + parentEmail
                    );

                    CollectionReference alertsCollection =
                            emailDocument.getReference()
                                    .collection("alert");

                    ApiFuture<QuerySnapshot> alertsFuture =
                            alertsCollection.get();

                    QuerySnapshot alertsSnapshot =
                            alertsFuture.get();

                    System.out.println(
                            "Alerts found for "
                            + parentEmail
                            + " = "
                            + alertsSnapshot.size()
                    );

                    for (QueryDocumentSnapshot alertDocument :
                            alertsSnapshot.getDocuments()) {

                        SosAlertData alert =
                                convertToAlert(
                                        alertDocument,
                                        parentEmail,
                                        parentSociety
                                );

                        if (alert == null) {

                            continue;
                        }

                        if (!alert.society.isEmpty() &&
                                !alert.society.equalsIgnoreCase(
                                        currentSociety
                                )) {

                            continue;
                        }

                        synchronized (allAlerts) {

                            allAlerts.add(
                                    alert
                            );
                        }
                    }
                }

                synchronized (allAlerts) {

                    allAlerts.sort(
                            Comparator.comparing(
                                    (SosAlertData a) ->
                                            a.createdAtMillis
                            ).reversed()
                    );
                }

                Platform.runLater(() -> {

                    updateCounts();

                    displayAlerts(
                            currentFilter
                    );

                    if (refreshBtn != null) {

                        refreshBtn.setDisable(false);
                    }
                });

                System.out.println(
                        "===================================="
                );

                System.out.println(
                        "CURRENT SOCIETY = "
                        + currentSociety
                );

                System.out.println(
                        "TOTAL SOS ALERTS FETCHED = "
                        + allAlerts.size()
                );

                System.out.println(
                        "===================================="
                );

            } catch (Exception e) {

                e.printStackTrace();

                Platform.runLater(() -> {

                    sosList.getChildren().clear();

                    Label error =
                            new Label(
                                    "Unable to fetch SOS alerts from Firestore."
                            );

                    error.setStyle(
                            "-fx-font-size:14px;" +
                            "-fx-text-fill:#D9534F;" +
                            "-fx-font-weight:bold;"
                    );

                    error.setWrapText(true);

                    sosList.getChildren().add(
                            error
                    );

                    if (refreshBtn != null) {

                        refreshBtn.setDisable(false);
                    }
                });
            }

        }).start();
    }


    // =========================================================
    // CONVERT FIRESTORE ALERT
    // =========================================================

    private SosAlertData convertToAlert(
            QueryDocumentSnapshot document,
            String parentEmail,
            String parentSociety
    ) {

        try {

            SosAlertData alert =
                    new SosAlertData();

            alert.alertId =
                    safeString(
                            document.getId()
                    );

            alert.email =
                    safeString(
                            parentEmail
                    );

            String alertSociety =
                    document.getString(
                            "society"
                    );

            if (alertSociety == null ||
                    alertSociety.trim().isEmpty()) {

                alertSociety =
                        parentSociety;
            }

            alert.society =
                    safeString(
                            alertSociety
                    );

            alert.type =
                    safeString(
                            document.getString(
                                    "type"
                            )
                    );

            alert.location =
                    safeString(
                            document.getString(
                                    "location"
                            )
                    );

            alert.details =
                    safeString(
                            document.getString(
                                    "details"
                            )
                    );

            alert.status =
                    safeString(
                            document.getString(
                                    "status"
                            )
                    );

            alert.time =
                    safeString(
                            document.getString(
                                    "time"
                            )
                    );

            Object createdAt =
                    document.get(
                            "createdAt"
                    );

            if (createdAt instanceof Timestamp) {

                Timestamp timestamp =
                        (Timestamp) createdAt;

                alert.createdAtMillis =
                        timestamp.toDate()
                                .getTime();

            } else if (createdAt instanceof Date) {

                Date date =
                        (Date) createdAt;

                alert.createdAtMillis =
                        date.getTime();

            } else {

                alert.createdAtMillis =
                        0;
            }

            return alert;

        } catch (Exception e) {

            System.out.println(
                    "Error converting SOS alert: "
                    + document.getId()
            );

            e.printStackTrace();

            return null;
        }
    }


    // =========================================================
    // UPDATE COUNTS
    // =========================================================

    private void updateCounts() {

        int activeCount = 0;

        int resolvedCount = 0;

        int totalCount;

        synchronized (allAlerts) {

            for (SosAlertData alert :
                    allAlerts) {

                if (isActive(
                        alert.status
                )) {

                    activeCount++;

                } else if (isResolved(
                        alert.status
                )) {

                    resolvedCount++;
                }
            }

            totalCount =
                    allAlerts.size();
        }

        activeBtn.setText(
                "Active ("
                + activeCount
                + ")"
        );

        resolvedBtn.setText(
                "Resolved ("
                + resolvedCount
                + ")"
        );

        allBtn.setText(
                "All Alerts ("
                + totalCount
                + ")"
        );
    }


    // =========================================================
    // DISPLAY ALERTS
    // =========================================================

    private void displayAlerts(
            String filter
    ) {

        sosList.getChildren().clear();

        List<SosAlertData> filtered =
                new ArrayList<>();

        synchronized (allAlerts) {

            for (SosAlertData alert :
                    allAlerts) {

                if ("ACTIVE".equalsIgnoreCase(
                        filter
                )) {

                    if (isActive(
                            alert.status
                    )) {

                        filtered.add(
                                alert
                        );
                    }

                } else if ("RESOLVED".equalsIgnoreCase(
                        filter
                )) {

                    if (isResolved(
                            alert.status
                    )) {

                        filtered.add(
                                alert
                        );

                    }

                } else {

                    filtered.add(
                            alert
                    );
                }
            }
        }

        if (filtered.isEmpty()) {

            Label noData =
                    new Label();

            if ("ACTIVE".equalsIgnoreCase(
                    filter
            )) {

                noData.setText(
                        "No active SOS alerts found for this society."
                );

            } else if ("RESOLVED".equalsIgnoreCase(
                    filter
            )) {

                noData.setText(
                        "No resolved SOS alerts found for this society."
                );

            } else {

                noData.setText(
                        "No SOS alerts found for this society."
                );
            }

            noData.setStyle(
                    "-fx-font-size:14px;" +
                    "-fx-text-fill:#666666;" +
                    "-fx-font-weight:bold;"
            );

            noData.setPadding(
                    new Insets(25)
            );

            sosList.getChildren().add(
                    noData
            );

            return;
        }

        for (SosAlertData alert :
                filtered) {

            VBox card =
                    createSosCard(
                            alert
                    );

            sosList.getChildren().add(
                    card
            );
        }
    }


    // =========================================================
    // CREATE SOS CARD
    // =========================================================

    private VBox createSosCard(
            SosAlertData alert
    ) {

        VBox sos =
                new VBox(10);

        sos.setPadding(
                new Insets(18)
        );

        sos.setMaxWidth(
                Double.MAX_VALUE
        );

        sos.setStyle(
                "-fx-background-color:rgba(255,255,255,0.96);" +
                "-fx-background-radius:10;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:10;"
        );

        // =====================================================
        // TOP ROW
        // =====================================================

        HBox topRow =
                new HBox();

        topRow.setAlignment(
                Pos.CENTER_LEFT
        );

        // =====================================================
        // EMERGENCY TYPE
        // =====================================================

        String emergencyType =
                alert.type.isEmpty()
                        ? "Emergency Alert"
                        : alert.type;

        Label typeLabel =
                new Label(
                        emergencyType
                );

        typeLabel.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:" + GREEN + ";"
        );

        // =====================================================
        // STATUS
        // =====================================================

        String statusText =
                alert.status.isEmpty()
                        ? "ACTIVE"
                        : alert.status;

        Label statusLabel =
                new Label(
                        statusText
                );

        if (isActive(
                statusText
        )) {

            statusLabel.setStyle(
                    "-fx-background-color:#FDE8E8;" +
                    "-fx-text-fill:#D9534F;" +
                    "-fx-font-size:10px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:5px 10px;" +
                    "-fx-background-radius:12;"
            );

        } else {

            statusLabel.setStyle(
                    "-fx-background-color:#E5F7EC;" +
                    "-fx-text-fill:#2E9D63;" +
                    "-fx-font-size:10px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:5px 10px;" +
                    "-fx-background-radius:12;"
            );
        }

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        topRow.getChildren().addAll(
                typeLabel,
                spacer,
                statusLabel
        );

        // =====================================================
        // DETAILS
        // =====================================================

        StringBuilder detailsText =
                new StringBuilder();

        if (!alert.location.isEmpty()) {

            detailsText.append(
                    "Location: "
            );

            detailsText.append(
                    alert.location
            );
        }

        if (!alert.time.isEmpty()) {

            if (detailsText.length() > 0) {

                detailsText.append(
                        "    |    "
                );
            }

            detailsText.append(
                    "Time: "
            );

            detailsText.append(
                    alert.time
            );
        }

        if (!alert.details.isEmpty()) {

            if (detailsText.length() > 0) {

                detailsText.append(
                        "    |    "
                );
            }

            detailsText.append(
                    "Details: "
            );

            detailsText.append(
                    alert.details
            );
        }

        Label details =
                new Label(
                        detailsText.toString()
                );

        details.setWrapText(
                true
        );

        details.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:" + GREY + ";"
        );

        // =====================================================
        // SOCIETY
        // =====================================================

        Label societyLabel =
                new Label(
                        "Society: "
                        + alert.society
                );

        societyLabel.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:" + GREY + ";"
        );

        // =====================================================
        // ADD TO CARD
        // =====================================================

        sos.getChildren().addAll(
                topRow,
                details,
                societyLabel
        );

        return sos;
    }


    // =========================================================
    // ACTIVE CHECK
    // =========================================================

    private boolean isActive(
            String status
    ) {

        if (status == null) {

            return false;
        }

        return status.equalsIgnoreCase(
                "ACTIVE"
        );
    }


    // =========================================================
    // RESOLVED CHECK
    // =========================================================

    private boolean isResolved(
            String status
    ) {

        if (status == null) {

            return false;
        }

        return status.equalsIgnoreCase(
                "RESOLVED"
        );
    }


    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safeString(
            String value
    ) {

        if (value == null) {

            return "";
        }

        return value.trim();
    }


    // =========================================================
    // SOS ALERT DATA
    // =========================================================

    private static class SosAlertData {

        String alertId = "";

        /*
         * Parent emergency_alerts document email.
         *
         * Used internally only.
         *
         * NEVER displayed in UI.
         */

        String email = "";

        String society = "";

        String type = "";

        String location = "";

        String details = "";

        String status = "";

        String time = "";

        long createdAtMillis = 0;
    }
}