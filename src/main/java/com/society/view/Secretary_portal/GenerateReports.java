package com.society.view.Secretary_portal;

import java.util.ArrayList;
import java.util.List;

import com.society.controller.Secretary_Controller.ReportController;
import com.society.dao.Welcome.UserDao;
import com.society.model.Secretary_model.Report;
import com.society.model.Welcome.User;
import com.society.view.ScreenSize;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * GenerateReports
 *
 * Secretary Daily Reports page.
 *
 * Data flow:
 *
 * Secretary login
 *       ↓
 * ReportController
 *       ↓
 * Secretary society
 *       ↓
 * ReportDao
 *       ↓
 * Reports collection
 *       +
 * visitors/{email}/visitor_records/*
 *       ↓
 * Report list
 *       ↓
 * JavaFX table
 */
public class GenerateReports {

    // =========================================================
    // COLORS
    // =========================================================

    private static final String BACKGROUND =
            "#F7F7F7";

    private static final String DARK =
            "#3D322E";

    private static final String TEXT =
            "#292525";

    private static final String SECONDARY =
            "#666666";

    private static final String GREEN =
            "#47785B";

    private static final String BORDER =
            "#E1E1E1";

    // =========================================================
    // CONTROLLER
    // =========================================================

    private final ReportController reportController;

    // =========================================================
    // TABLE
    // =========================================================

    private final TableView<Report> reportTable =
            new TableView<>();

    private final ObservableList<Report> reportList =
            FXCollections.observableArrayList();

    // =========================================================
    // CURRENT FILTER
    // =========================================================

    private String currentFilter =
            "All";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public GenerateReports() {

        reportController =
                new ReportController();
    }

    // =========================================================
    // CREATE SCENE
    // =========================================================

    public Scene createScene(
            Stage stage) {

        // =====================================================
        // LOGIN CHECK
        // =====================================================

        String loggedInEmail =
                UserDao.getLoggedInEmail();

        if (isEmpty(loggedInEmail)) {

            showError(
                    "Session Error",
                    "No logged-in secretary was found."
            );

            return new Scene(
                    new VBox(),
                    ScreenSize.getWidth(),
                    ScreenSize.getHeight()
            );
        }

        // =====================================================
        // SECRETARY
        // =====================================================

        User secretary =
                reportController
                        .getLoggedInSecretary();

        if (secretary == null) {

            showError(
                    "Secretary Error",
                    "Secretary profile could not be found."
            );

            return new Scene(
                    new VBox(),
                    ScreenSize.getWidth(),
                    ScreenSize.getHeight()
            );
        }

        // =====================================================
        // SOCIETY
        // =====================================================

        String society =
                secretary.getSociety();

        if (isEmpty(society)) {

            showError(
                    "Society Error",
                    "Society information could not be found."
            );

            return new Scene(
                    new VBox(),
                    ScreenSize.getWidth(),
                    ScreenSize.getHeight()
            );
        }

        // =====================================================
        // SIDEBAR
        // =====================================================

        SecretarySidebar sidebarObj =
                new SecretarySidebar();

        VBox sidebar =
                sidebarObj.createSidebar(
                        stage
                );

        // =====================================================
        // MAIN
        // =====================================================

        BorderPane main =
                new BorderPane();

        main.setStyle(
                "-fx-background-color:"
                        + BACKGROUND
                        + ";"
        );

        // =====================================================
        // HEADER
        // =====================================================

        main.setTop(
                createHeader(
                        secretary
                )
        );

        // =====================================================
        // CONTENT
        // =====================================================

        main.setCenter(
                createContent(
                        stage,
                        society
                )
        );

        // =====================================================
        // BODY
        // =====================================================

        HBox body =
                new HBox();

        body.setStyle(
                "-fx-background-color:"
                        + BACKGROUND
                        + ";"
        );

        body.getChildren().addAll(
                sidebar,
                main
        );

        HBox.setHgrow(
                main,
                Priority.ALWAYS
        );

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane scroll =
                new ScrollPane();

        scroll.setContent(
                body
        );

        scroll.setFitToWidth(
                true
        );

        scroll.setFitToHeight(
                true
        );

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scroll.setStyle(
                "-fx-background-color:"
                        + BACKGROUND
                        + ";"
                        + "-fx-border-color:transparent;"
        );

        // =====================================================
        // LOAD
        // =====================================================

        loadReports();

        // =====================================================
        // SCENE
        // =====================================================

        return new Scene(
                scroll,
                ScreenSize.getWidth(),
                ScreenSize.getHeight()
        );
    }

    // =========================================================
    // HEADER
    // =========================================================

    private VBox createHeader(
            User secretary) {

        VBox header =
                new VBox();

        header.setPadding(
                new Insets(
                        22,
                        30,
                        18,
                        30
                )
        );

        header.setSpacing(
                5
        );

        header.setStyle(
                "-fx-background-color:white;"
                        + "-fx-border-color:"
                        + BORDER
                        + ";"
                        + "-fx-border-width:0 0 1 0;"
        );

        Label title =
                new Label(
                        "Daily Reports"
                );

        title.setStyle(
                "-fx-font-size:26px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:"
                        + DARK
                        + ";"
        );

        Label subtitle =
                new Label(
                        "Generate and review daily society activity reports"
                );

        subtitle.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-text-fill:"
                        + SECONDARY
                        + ";"
        );

        Label secretaryLabel =
                new Label(
                        "Secretary: "
                                + safe(
                                        secretary.getName()
                                )
                                + "    |    Society: "
                                + safe(
                                        secretary.getSociety()
                                )
                );

        secretaryLabel.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-text-fill:"
                        + SECONDARY
                        + ";"
        );

        header.getChildren().addAll(
                title,
                subtitle,
                secretaryLabel
        );

        return header;
    }

    // =========================================================
    // CONTENT
    // =========================================================

    private VBox createContent(
            Stage stage,
            String society) {

        VBox content =
                new VBox(
                        18
                );

        content.setPadding(
                new Insets(
                        25
                )
        );

        content.setFillWidth(
                true
        );

        // =====================================================
        // SOCIETY CARD
        // =====================================================

        VBox societyCard =
                new VBox(
                        5
                );

        societyCard.setPadding(
                new Insets(
                        18
                )
        );

        societyCard.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:10;"
                        + "-fx-border-color:"
                        + BORDER
                        + ";"
                        + "-fx-border-radius:10;"
        );

        Label societyTitle =
                new Label(
                        "Society"
                );

        societyTitle.setStyle(
                "-fx-font-size:13px;"
                        + "-fx-text-fill:"
                        + SECONDARY
                        + ";"
        );

        Label societyValue =
                new Label(
                        society
                );

        societyValue.setStyle(
                "-fx-font-size:19px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:"
                        + TEXT
                        + ";"
        );

        societyCard.getChildren().addAll(
                societyTitle,
                societyValue
        );

        // =====================================================
        // CATEGORY BUTTONS
        // =====================================================

        HBox categoryBox =
                new HBox(
                        10
                );

        categoryBox.setAlignment(
                Pos.CENTER_LEFT
        );

        Button allButton =
                createCategoryButton(
                        "All"
                );

        Button securityButton =
                createCategoryButton(
                        "Security"
                );

        Button visitorButton =
                createCategoryButton(
                        "Visitor"
                );

        Button parkingButton =
                createCategoryButton(
                        "Parking"
                );

        Button incidentButton =
                createCategoryButton(
                        "Incident"
                );

        Button otherButton =
                createCategoryButton(
                        "Other"
                );

        allButton.setOnAction(
                e -> {
                    currentFilter = "All";
                    filterTable(
                            currentFilter
                    );
                }
        );

        securityButton.setOnAction(
                e -> {
                    currentFilter = "Security";
                    filterTable(
                            currentFilter
                    );
                }
        );

        visitorButton.setOnAction(
                e -> {
                    currentFilter = "Visitor";
                    filterTable(
                            currentFilter
                    );
                }
        );

        parkingButton.setOnAction(
                e -> {
                    currentFilter = "Parking";
                    filterTable(
                            currentFilter
                    );
                }
        );

        incidentButton.setOnAction(
                e -> {
                    currentFilter = "Incident";
                    filterTable(
                            currentFilter
                    );
                }
        );

        otherButton.setOnAction(
                e -> {
                    currentFilter = "Other";
                    filterTable(
                            currentFilter
                    );
                }
        );

        categoryBox.getChildren().addAll(
                allButton,
                securityButton,
                visitorButton,
                parkingButton,
                incidentButton,
                otherButton
        );

        // =====================================================
        // REFRESH
        // =====================================================

        Button refreshButton =
                new Button(
                        "⟳ Refresh"
                );

        refreshButton.setPrefHeight(
                38
        );

        refreshButton.setStyle(
                "-fx-background-color:white;"
                        + "-fx-text-fill:"
                        + DARK
                        + ";"
                        + "-fx-font-weight:bold;"
                        + "-fx-border-color:"
                        + BORDER
                        + ";"
                        + "-fx-border-radius:7;"
                        + "-fx-background-radius:7;"
                        + "-fx-cursor:hand;"
        );

        refreshButton.setOnAction(
                e -> loadReports()
        );

        HBox filterRow =
                new HBox(
                        10
                );

        filterRow.setAlignment(
                Pos.CENTER_LEFT
        );

        filterRow.getChildren().addAll(
                categoryBox,
                refreshButton
        );

        // =====================================================
        // TABLE CARD
        // =====================================================

        VBox tableCard =
                new VBox(
                        12
                );

        tableCard.setPadding(
                new Insets(
                        18
                )
        );

        tableCard.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:10;"
                        + "-fx-border-color:"
                        + BORDER
                        + ";"
                        + "-fx-border-radius:10;"
        );

        Label tableTitle =
                new Label(
                        "Recent Reports"
                );

        tableTitle.setStyle(
                "-fx-font-size:19px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:"
                        + TEXT
                        + ";"
        );

        configureTable(
                stage
        );

        VBox.setVgrow(
                reportTable,
                Priority.ALWAYS
        );

        tableCard.getChildren().addAll(
                tableTitle,
                new Separator(),
                reportTable
        );

        VBox.setVgrow(
                tableCard,
                Priority.ALWAYS
        );

        content.getChildren().addAll(
                societyCard,
                filterRow,
                tableCard
        );

        return content;
    }

    // =========================================================
    // CATEGORY BUTTON
    // =========================================================

    private Button createCategoryButton(
            String text) {

        Button button =
                new Button(
                        text
                );

        button.setPrefHeight(
                38
        );

        button.setPrefWidth(
                105
        );

        button.setStyle(
                "-fx-background-color:"
                        + DARK
                        + ";"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:7;"
                        + "-fx-cursor:hand;"
        );

        return button;
    }

    // =========================================================
    // CONFIGURE TABLE
    // =========================================================

    private void configureTable(
            Stage owner) {

        reportTable.setItems(
                reportList
        );

        reportTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        reportTable.setPlaceholder(
                new Label(
                        "No reports found."
                )
        );

        // =====================================================
        // TYPE
        // =====================================================

        TableColumn<Report, String> typeColumn =
                new TableColumn<>(
                        "Report Type"
                );

        typeColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                safe(
                                        data.getValue()
                                                .getType()
                                )
                        )
        );

        // =====================================================
        // SOURCE
        // =====================================================

        TableColumn<Report, String> sourceColumn =
                new TableColumn<>(
                        "Source"
                );

        sourceColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                safe(
                                        data.getValue()
                                                .getSource()
                                )
                        )
        );

        // =====================================================
        // TITLE
        // =====================================================

        TableColumn<Report, String> titleColumn =
                new TableColumn<>(
                        "Title"
                );

        titleColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                safe(
                                        data.getValue()
                                                .getTitle()
                                )
                        )
        );

        // =====================================================
        // SUBMITTED BY
        // =====================================================

        TableColumn<Report, String> submittedColumn =
                new TableColumn<>(
                        "Submitted By"
                );

        submittedColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                safe(
                                        data.getValue()
                                                .getSubmittedBy()
                                )
                        )
        );

        // =====================================================
        // DATE
        // =====================================================

        TableColumn<Report, String> dateColumn =
                new TableColumn<>(
                        "Date & Time"
                );

        dateColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                safe(
                                        data.getValue()
                                                .getDate()
                                )
                        )
        );

        // =====================================================
        // STATUS
        // =====================================================

        TableColumn<Report, String> statusColumn =
                new TableColumn<>(
                        "Status"
                );

        statusColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                safe(
                                        data.getValue()
                                                .getStatus()
                                )
                        )
        );

        // =====================================================
        // VIEW
        // =====================================================

        TableColumn<Report, Void> viewColumn =
                new TableColumn<>(
                        "View"
                );

        viewColumn.setCellFactory(
                column ->
                        new TableCell<Report, Void>() {

                            private final Button viewButton =
                                    new Button(
                                            "View"
                                    );

                            {
                                viewButton.setStyle(
                                        "-fx-background-color:"
                                                + DARK
                                                + ";"
                                                + "-fx-text-fill:white;"
                                                + "-fx-font-weight:bold;"
                                                + "-fx-background-radius:6;"
                                                + "-fx-cursor:hand;"
                                );

                                viewButton.setOnAction(
                                        event -> {

                                            int index =
                                                    getIndex();

                                            if (index < 0
                                                    || index >=
                                                    getTableView()
                                                            .getItems()
                                                            .size()) {

                                                return;
                                            }

                                            Report report =
                                                    getTableView()
                                                            .getItems()
                                                            .get(
                                                                    index
                                                            );

                                            showReportDetails(
                                                    report,
                                                    owner
                                            );
                                        }
                                );
                            }

                            @Override
                            protected void updateItem(
                                    Void item,
                                    boolean empty) {

                                super.updateItem(
                                        item,
                                        empty
                                );

                                if (empty) {

                                    setGraphic(
                                            null
                                    );

                                } else {

                                    setGraphic(
                                            viewButton
                                    );

                                    setAlignment(
                                            Pos.CENTER
                                    );
                                }
                            }
                        }
        );

        // =====================================================
        // WIDTH
        // =====================================================

        typeColumn.setPrefWidth(
                130
        );

        sourceColumn.setPrefWidth(
                150
        );

        titleColumn.setPrefWidth(
                190
        );

        submittedColumn.setPrefWidth(
                180
        );

        dateColumn.setPrefWidth(
                170
        );

        statusColumn.setPrefWidth(
                110
        );

        viewColumn.setPrefWidth(
                90
        );

        // =====================================================
        // ADD
        // =====================================================

        reportTable.getColumns().clear();

        reportTable.getColumns().addAll(
                typeColumn,
                sourceColumn,
                titleColumn,
                submittedColumn,
                dateColumn,
                statusColumn,
                viewColumn
        );
    }

    // =========================================================
    // LOAD REPORTS
    // =========================================================

    private void loadReports() {

        Task<List<Report>> task =
                new Task<>() {

                    @Override
                    protected List<Report> call()
                            throws Exception {

                        return reportController
                                .getAllReports();
                    }
                };

        reportTable.setPlaceholder(
                new Label(
                        "Loading reports..."
                )
        );

        task.setOnSucceeded(
                event -> {

                    List<Report> reports =
                            task.getValue();

                    if (reports == null) {

                        reports =
                                new ArrayList<>();
                    }

                    reportList.clear();

                    reportList.addAll(
                            reports
                    );

                    currentFilter =
                            "All";

                    reportTable.setItems(
                            reportList
                    );

                    reportTable.setPlaceholder(
                            new Label(
                                    "No reports found for this society."
                            )
                    );

                    System.out.println(
                            "========================================"
                    );

                    System.out.println(
                            "REPORTS FETCHED SUCCESSFULLY"
                    );

                    System.out.println(
                            "Total Reports : "
                                    + reports.size()
                    );

                    System.out.println(
                            "Logged-in Email : "
                                    + reportController
                                    .getLoggedInEmail()
                    );

                    System.out.println(
                            "Secretary Society : "
                                    + reportController
                                    .getSocietyName()
                    );

                    System.out.println(
                            "========================================"
                    );
                }
        );

        task.setOnFailed(
                event -> {

                    reportList.clear();

                    reportTable.setItems(
                            reportList
                    );

                    reportTable.setPlaceholder(
                            new Label(
                                    "Unable to load reports."
                            )
                    );

                    Throwable error =
                            task.getException();

                    System.err.println(
                            "========================================"
                    );

                    System.err.println(
                            "REPORT FETCH FAILED"
                    );

                    if (error != null) {

                        error.printStackTrace();
                    }

                    System.err.println(
                            "========================================"
                    );

                    showError(
                            "Reports Error",
                            "Unable to load reports."
                    );
                }
        );

        Thread thread =
                new Thread(
                        task
                );

        thread.setDaemon(
                true
        );

        thread.start();
    }

    // =========================================================
    // FILTER TABLE
    // =========================================================

    private void filterTable(
            String filter) {

        if (filter == null
                || filter.equalsIgnoreCase(
                        "All"
                )) {

            reportTable.setItems(
                    reportList
            );

            return;
        }

        ObservableList<Report> filtered =
                FXCollections.observableArrayList();

        for (Report report :
                reportList) {

            if (report == null) {
                continue;
            }

            String type =
                    safe(
                            report.getType()
                    );

            String source =
                    safe(
                            report.getSource()
                    );

            if (matchesFilter(
                    type,
                    source,
                    filter
            )) {

                filtered.add(
                        report
                );
            }
        }

        reportTable.setItems(
                filtered
        );
    }

    // =========================================================
    // MATCH FILTER
    // =========================================================

    private boolean matchesFilter(
            String type,
            String source,
            String filter) {

        String typeLower =
                safe(type)
                        .toLowerCase();

        String sourceLower =
                safe(source)
                        .toLowerCase();

        String filterLower =
                safe(filter)
                        .toLowerCase();

        // =====================================================
        // DIRECT MATCH
        // =====================================================

        if (typeLower.contains(
                filterLower
        )) {

            return true;
        }

        if (sourceLower.contains(
                filterLower
        )) {

            return true;
        }

        // =====================================================
        // SECURITY
        // =====================================================

        if (filter.equalsIgnoreCase(
                "Security"
        )) {

            return sourceLower.contains(
                    "guard"
            )
                    || sourceLower.contains(
                    "security"
            )
                    || typeLower.contains(
                    "guard"
            )
                    || typeLower.contains(
                    "security"
            );
        }

        // =====================================================
        // VISITOR
        // =====================================================

        if (filter.equalsIgnoreCase(
                "Visitor"
        )) {

            return sourceLower.contains(
                    "visitor"
            )
                    || typeLower.contains(
                    "visitor"
            )
                    || typeLower.contains(
                    "visitors"
            );
        }

        // =====================================================
        // PARKING
        // =====================================================

        if (filter.equalsIgnoreCase(
                "Parking"
        )) {

            return sourceLower.contains(
                    "parking"
            )
                    || typeLower.contains(
                    "parking"
            );
        }

        // =====================================================
        // INCIDENT
        // =====================================================

        if (filter.equalsIgnoreCase(
                "Incident"
        )) {

            return sourceLower.contains(
                    "complaint"
            )
                    || sourceLower.contains(
                    "sos"
            )
                    || sourceLower.contains(
                    "emergency"
            )
                    || sourceLower.contains(
                    "incident"
            )
                    || typeLower.contains(
                    "complaint"
            )
                    || typeLower.contains(
                    "sos"
            )
                    || typeLower.contains(
                    "emergency"
            )
                    || typeLower.contains(
                    "incident"
            );
        }

        // =====================================================
        // OTHER
        // =====================================================

        if (filter.equalsIgnoreCase(
                "Other"
        )) {

            return !sourceLower.contains(
                    "guard"
            )
                    && !sourceLower.contains(
                    "security"
            )
                    && !sourceLower.contains(
                    "visitor"
            )
                    && !typeLower.contains(
                    "visitor"
            )
                    && !sourceLower.contains(
                    "parking"
            )
                    && !typeLower.contains(
                    "parking"
            )
                    && !sourceLower.contains(
                    "complaint"
            )
                    && !typeLower.contains(
                    "complaint"
            )
                    && !sourceLower.contains(
                    "incident"
            )
                    && !typeLower.contains(
                    "incident"
            );
        }

        return false;
    }

    // =========================================================
    // REPORT DETAILS
    // =========================================================

    private void showReportDetails(
            Report report,
            Stage owner) {

        if (report == null) {
            return;
        }

        Stage dialog =
                new Stage();

        dialog.initOwner(
                owner
        );

        dialog.initModality(
                Modality.APPLICATION_MODAL
        );

        dialog.setTitle(
                "Report Details"
        );

        VBox root =
                new VBox(
                        14
                );

        root.setPadding(
                new Insets(
                        25
                )
        );

        root.setPrefWidth(
                650
        );

        root.setPrefHeight(
                650
        );

        root.setStyle(
                "-fx-background-color:white;"
        );

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label(
                        safe(
                                report.getTitle()
                        )
                );

        title.setStyle(
                "-fx-font-size:22px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:"
                        + DARK
                        + ";"
        );

        // =====================================================
        // BASIC INFORMATION
        // =====================================================

        VBox information =
                new VBox(
                        8
                );

        addInfoRow(
                information,
                "Report Type",
                report.getType()
        );

        addInfoRow(
                information,
                "Source",
                report.getSource()
        );

        addInfoRow(
                information,
                "Submitted By",
                report.getSubmittedBy()
        );

        addInfoRow(
                information,
                "Email",
                report.getEmail()
        );

        addInfoRow(
                information,
                "Society",
                report.getSocietyName()
        );

        addInfoRow(
                information,
                "Society ID",
                report.getSocietyId()
        );

        addInfoRow(
                information,
                "Date & Time",
                report.getDate()
        );

        addInfoRow(
                information,
                "Status",
                report.getStatus()
        );

        // =====================================================
        // DETAILS
        // =====================================================

        Label detailsTitle =
                new Label(
                        "Details"
                );

        detailsTitle.setStyle(
                "-fx-font-size:16px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:"
                        + TEXT
                        + ";"
        );

        TextArea detailsArea =
                new TextArea(
                        safe(
                                report.getDetails()
                        )
                );

        detailsArea.setEditable(
                false
        );

        detailsArea.setWrapText(
                true
        );

        detailsArea.setPrefHeight(
                360
        );

        detailsArea.setStyle(
                "-fx-control-inner-background:#FAFAFA;"
                        + "-fx-font-size:13px;"
                        + "-fx-text-fill:"
                        + TEXT
                        + ";"
        );

        VBox.setVgrow(
                detailsArea,
                Priority.ALWAYS
        );

        // =====================================================
        // CLOSE
        // =====================================================

        Button close =
                new Button(
                        "Close"
                );

        close.setPrefWidth(
                100
        );

        close.setPrefHeight(
                38
        );

        close.setStyle(
                "-fx-background-color:"
                        + DARK
                        + ";"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:7;"
                        + "-fx-cursor:hand;"
        );

        close.setOnAction(
                event ->
                        dialog.close()
        );

        HBox closeBox =
                new HBox(
                        close
                );

        closeBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        root.getChildren().addAll(
                title,
                new Separator(),
                information,
                new Separator(),
                detailsTitle,
                detailsArea,
                closeBox
        );

        Scene scene =
                new Scene(
                        root
                );

        dialog.setScene(
                scene
        );

        dialog.setResizable(
                false
        );

        dialog.showAndWait();
    }

    // =========================================================
    // INFO ROW
    // =========================================================

    private void addInfoRow(
            VBox parent,
            String labelText,
            String valueText) {

        HBox row =
                new HBox(
                        10
                );

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        Label label =
                new Label(
                        labelText + ":"
                );

        label.setPrefWidth(
                120
        );

        label.setStyle(
                "-fx-font-weight:bold;"
                        + "-fx-text-fill:"
                        + SECONDARY
                        + ";"
        );

        Label value =
                new Label(
                        safe(
                                valueText
                        )
                );

        value.setWrapText(
                true
        );

        value.setStyle(
                "-fx-text-fill:"
                        + TEXT
                        + ";"
        );

        HBox.setHgrow(
                value,
                Priority.ALWAYS
        );

        row.getChildren().addAll(
                label,
                value
        );

        parent.getChildren().add(
                row
        );
    }

    // =========================================================
    // SAFE
    // =========================================================

    private String safe(
            String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    // =========================================================
    // EMPTY
    // =========================================================

    private boolean isEmpty(
            String value) {

        return value == null
                || value.trim().isEmpty();
    }

    // =========================================================
    // ERROR
    // =========================================================

    private void showError(
            String title,
            String message) {

        Runnable action =
                () -> {

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
                };

        if (Platform.isFxApplicationThread()) {

            action.run();

        } else {

            Platform.runLater(
                    action
            );
        }
    }
}