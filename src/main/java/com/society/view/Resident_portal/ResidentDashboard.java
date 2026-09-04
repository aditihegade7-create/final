package com.society.view.Resident_portal;

import com.society.controller.Resident_Controller.DashboardController;
import com.society.controller.welcome.UserController;
import com.society.dao.Welcome.UserDao;
import com.society.model.Resident_model.NoticeModel;
import com.society.model.Resident_model.PollModel;
import com.society.model.Welcome.User;
import com.society.view.ScreenSize;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Resident Dashboard.
 *
 * Existing navigation is preserved.
 * Dashboard values are loaded from Firestore through
 * the existing controller/DAO structure.
 */
public class ResidentDashboard {

    private static final ZoneId INDIA_ZONE =
            ZoneId.of("Asia/Kolkata");

    private Scene residentDashboardScene;

    private String residentName = "Resident";
    private String flatNo = "N/A";
    private String LognEmail = "";

    private Label totalBillsLabel;
    private Label maintenanceBillsLabel;
    private Label maintenanceBottomLabel;
    private Label complaintCountLabel;
    private Label visitorCountLabel;

    private VBox upcomingBox;
    private VBox noticeContent;
    private VBox pollContent;

    // =========================================================
    // GET DASHBOARD SCENE
    // =========================================================

    public Scene getResidentDashboardScene(Stage stage) {

        UserController userController =
                new UserController();

        String loggedInEmail =
                UserDao.getLoggedInEmail();

        this.LognEmail =
                loggedInEmail == null
                        ? ""
                        : loggedInEmail.trim().toLowerCase();

        User resident = null;

        if (!this.LognEmail.isEmpty()) {

            resident =
                    userController.getUserByEmail(
                            this.LognEmail
                    );
        }

        if (resident != null) {

            residentName =
                    safe(resident.getName());

            flatNo =
                    safe(resident.getFlatNo());

            if (residentName.isEmpty()) {
                residentName = "Resident";
            }

            if (flatNo.isEmpty()) {
                flatNo = "N/A";
            }
        }

        // =====================================================
        // SIDEBAR
        // =====================================================

        panel panelobj =
                new panel(
                        stage,
                        this.LognEmail
                );

        BorderPane root =
                new BorderPane();

        root.setLeft(
                panelobj.getSidebar()
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainContent =
                new VBox(20);

        mainContent.setPadding(
                new Insets(
                        25,
                        30,
                        25,
                        30
                )
        );

        mainContent.setStyle(
                "-fx-background-color: #e8ddd5;"
        );

        // =====================================================
        // HEADER
        // =====================================================

        HBox header =
                new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setStyle(
                "-fx-background-color: #4e342e;"
        );

        header.setPadding(
                new Insets(
                        0,
                        15,
                        0,
                        15
                )
        );

        VBox welcomeBox =
                new VBox(4);

        Label welcome =
                new Label(
                        getGreeting()
                                + ", "
                                + residentName
                                + " 👋"
                );

        welcome.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        25
                )
        );

        welcome.setTextFill(
                Color.WHITE
        );

        Label flat =
                new Label(
                        "Flat "
                                + flatNo
                                + getTowerText(resident)
                );

        flat.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        flat.setTextFill(
                Color.WHITE
        );

        welcomeBox.getChildren().addAll(
                welcome,
                flat
        );

        Region headerSpace =
                new Region();

        HBox.setHgrow(
                headerSpace,
                Priority.ALWAYS
        );

        LocalDate today =
                LocalDate.now(INDIA_ZONE);

        Label date =
                new Label(
                        today.format(
                                DateTimeFormatter.ofPattern(
                                        "dd MMMM yyyy"
                                )
                        )
                                + "\n"
                                + today.format(
                                DateTimeFormatter.ofPattern(
                                        "EEEE"
                                )
                        )
                );

        date.setTextFill(
                Color.WHITE
        );

        date.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        date.setAlignment(
                Pos.CENTER_RIGHT
        );

        header.getChildren().addAll(
                welcomeBox,
                headerSpace,
                date
        );

        // =====================================================
        // SUMMARY CARDS
        // =====================================================

        HBox summaryCards =
                new HBox(15);

        VBox totalBills =
                createSummaryCard(
                        "Total Bills",
                        "₹ 0",
                        "Loading..."
                );

        VBox maintenanceBills =
                createSummaryCard(
                        "Maintenance Bills",
                        "₹ 0",
                        "Loading..."
                );

        VBox complaints =
                createSummaryCard(
                        "Active Complaints",
                        "0",
                        "View Status"
                );

        VBox visitors =
                createSummaryCard(
                        "Visitors Today",
                        "0",
                        "View Log"
                );

        totalBillsLabel =
                getAmountLabel(totalBills);

        maintenanceBillsLabel =
                getAmountLabel(maintenanceBills);

        maintenanceBottomLabel =
                getBottomLabel(maintenanceBills);

        complaintCountLabel =
                getAmountLabel(complaints);

        visitorCountLabel =
                getAmountLabel(visitors);

        // =====================================================
        // SUMMARY CARD ACTIONS
        // =====================================================

        Button totalDetails =
                getBottomButton(totalBills);

        if (totalDetails != null) {

            totalDetails.setOnAction(
                    e -> openMyBills(stage)
            );
        }

        Button maintenanceDetails =
                getBottomButton(maintenanceBills);

        if (maintenanceDetails != null) {

            maintenanceDetails.setOnAction(
                    e -> openMyBills(stage)
            );
        }

        Button complaintDetails =
                getBottomButton(complaints);

        if (complaintDetails != null) {

            complaintDetails.setOnAction(
                    e -> openComplaints(stage)
            );
        }

        Button visitorDetails =
                getBottomButton(visitors);

        if (visitorDetails != null) {

            visitorDetails.setOnAction(
                    e -> openVisitors(stage)
            );
        }

        summaryCards.getChildren().addAll(
                totalBills,
                maintenanceBills,
                complaints,
                visitors
        );

        // =====================================================
        // QUICK ACTIONS
        // =====================================================

        Label quickTitle =
                createSectionTitle(
                        "Quick Actions"
                );

        GridPane quickActions =
                new GridPane();

        quickActions.setHgap(15);
        quickActions.setVgap(15);

        Button payMaintenance =
                createActionButton(
                        "Pay Maintenance",
                        "Secure Online Payment"
                );

        payMaintenance.setOnAction(
                e -> openMyBills(stage)
        );

        Button raiseComplaint =
                createActionButton(
                        "Raise Complaint",
                        "Report an issue"
                );

        raiseComplaint.setOnAction(
                e -> openComplaints(stage)
        );

        Button bookAmenity =
                createActionButton(
                        "Book Amenity",
                        "Hall, Court, Guest Room"
                );

        bookAmenity.setOnAction(e -> {

            AmenitiesBooking booking =
                    new AmenitiesBooking(
                            residentName,
                            flatNo,
                            this.LognEmail
                    );

            stage.setScene(
                    booking.getAminityScene(stage)
            );

            stage.show();
        });

        Button inviteVisitor =
                createActionButton(
                        "Invite Visitor",
                        "Pre-approve entry"
                );

        inviteVisitor.setOnAction(
                e -> openVisitors(stage)
        );

        quickActions.add(
                payMaintenance,
                0,
                0
        );

        quickActions.add(
                raiseComplaint,
                1,
                0
        );

        quickActions.add(
                bookAmenity,
                0,
                1
        );

        quickActions.add(
                inviteVisitor,
                1,
                1
        );

        // =====================================================
        // UPCOMING / DUE
        // =====================================================

        upcomingBox =
                new VBox(12);

        upcomingBox.setPadding(
                new Insets(18)
        );

        upcomingBox.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
        );

        Label upcomingTitle =
                new Label(
                        "Upcoming / Due"
                );

        upcomingTitle.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );

        upcomingTitle.setTextFill(
                Color.web("#263238")
        );

        upcomingBox.getChildren().add(
                upcomingTitle
        );

        addLoadingDueRows();

        // =====================================================
        // MIDDLE SECTION
        // =====================================================

        HBox middleSection =
                new HBox(20);

        VBox quickBox =
                new VBox(10);

        quickBox.setPadding(
                new Insets(18)
        );

        quickBox.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
        );

        quickBox.getChildren().addAll(
                quickTitle,
                quickActions
        );

        HBox.setHgrow(
                quickBox,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                upcomingBox,
                Priority.ALWAYS
        );

        middleSection.getChildren().addAll(
                quickBox,
                upcomingBox
        );

        // =====================================================
        // BOTTOM SECTION
        // =====================================================

        HBox bottomSection =
                new HBox(20);

        // =====================================================
        // NOTICES
        // =====================================================

        VBox noticesBox =
                new VBox(12);

        noticesBox.setPadding(
                new Insets(18)
        );

        noticesBox.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
        );

        Label noticesTitle =
                new Label(
                        "Society Notices"
                );

        noticesTitle.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );

        noticesTitle.setTextFill(
                Color.web("#263238")
        );

        noticeContent =
                new VBox(10);

        noticeContent.setFillWidth(true);

        Label noticeLoading =
                new Label(
                        "Loading notices..."
                );

        noticeLoading.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        noticeLoading.setTextFill(
                Color.web("#789098")
        );

        noticeContent.getChildren().add(
                noticeLoading
        );

        noticesBox.getChildren().addAll(
                noticesTitle,
                noticeContent
        );

        // =====================================================
        // POLL
        // =====================================================

        VBox pollBox =
                new VBox(12);

        pollBox.setPadding(
                new Insets(18)
        );

        pollBox.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
        );

        Label pollTitle =
                new Label(
                        "Community Poll"
                );

        pollTitle.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );

        pollTitle.setTextFill(
                Color.web("#263238")
        );

        pollContent =
                new VBox(10);

        Label pollLoading =
                new Label(
                        "Loading poll..."
                );

        pollLoading.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        pollLoading.setTextFill(
                Color.web("#789098")
        );

        pollContent.getChildren().add(
                pollLoading
        );

        pollBox.getChildren().addAll(
                pollTitle,
                pollContent
        );

        HBox.setHgrow(
                noticesBox,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                pollBox,
                Priority.ALWAYS
        );

        bottomSection.getChildren().addAll(
                noticesBox,
                pollBox
        );

        // =====================================================
        // ADD CONTENT
        // =====================================================

        mainContent.getChildren().addAll(
                summaryCards,
                middleSection,
                bottomSection
        );

        BorderPane mainArea =
                new BorderPane();

        mainArea.setTop(header);
        mainArea.setCenter(mainContent);

        root.setCenter(mainArea);

        // =====================================================
        // SCENE
        // =====================================================

        residentDashboardScene =
                new Scene(
                        root,
                        ScreenSize.getWidth(),
                        ScreenSize.getHeight()
                );

        // =====================================================
        // FETCH FIRESTORE DATA
        // =====================================================

        loadDashboardData();

        return residentDashboardScene;
    }

    // =========================================================
    // LOAD DASHBOARD DATA
    // =========================================================

    private void loadDashboardData() {

        if (this.LognEmail.isEmpty()) {

            Platform.runLater(() -> {

                setAmount(
                        complaintCountLabel,
                        "0"
                );

                setAmount(
                        visitorCountLabel,
                        "0"
                );

                showNoticeMessage(
                        "Unable to load dashboard data."
                );

                showPollMessage(
                        "Unable to load dashboard data."
                );

                showDueMessage(
                        "No resident session found."
                );
            });

            return;
        }

        final String email =
                this.LognEmail;

        Thread thread =
                new Thread(() -> {

                    try {

                        DashboardController controller =
                                new DashboardController();

                        DashboardController.DashboardData data =
                                controller.loadDashboardData(
                                        email
                                );

                        Platform.runLater(() ->
                                updateDashboard(data)
                        );

                    } catch (Exception e) {

                        e.printStackTrace();

                        Platform.runLater(() -> {

                            setAmount(
                                    complaintCountLabel,
                                    "0"
                            );

                            setAmount(
                                    visitorCountLabel,
                                    "0"
                            );

                            setAmount(
                                    totalBillsLabel,
                                    "₹ 0"
                            );

                            setAmount(
                                    maintenanceBillsLabel,
                                    "₹ 0"
                            );

                            setBottom(
                                    maintenanceBottomLabel,
                                    "Unable to load"
                            );

                            showNoticeMessage(
                                    "Unable to load society notices."
                            );

                            showPollMessage(
                                    "Unable to load community poll."
                            );

                            showDueMessage(
                                    "Unable to load due information."
                            );
                        });
                    }
                });

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // UPDATE DASHBOARD
    // =========================================================

    private void updateDashboard(
            DashboardController.DashboardData data) {

        if (data == null) {
            return;
        }

        // Complaints
        setAmount(
                complaintCountLabel,
                String.valueOf(
                        data.getActiveComplaints()
                )
        );

        // Visitors
        setAmount(
                visitorCountLabel,
                String.valueOf(
                        data.getVisitorsToday()
                )
        );

        // Bills
        setAmount(
                totalBillsLabel,
                formatMoney(
                        data.getTotalBills()
                )
        );

        setAmount(
                maintenanceBillsLabel,
                formatMoney(
                        data.getOutstandingMaintenance()
                )
        );

        String dueDate =
                data.getMaintenanceDueDate();

        if (dueDate == null
                || dueDate.trim().isEmpty()) {

            setBottom(
                    maintenanceBottomLabel,
                    "No outstanding due"
            );

        } else {

            setBottom(
                    maintenanceBottomLabel,
                    "Due: "
                            + formatDisplayDate(dueDate)
            );
        }

        updateUpcoming(
                data.getMaintenanceBills()
        );

        updateNotices(
                data.getNotices()
        );

        updatePoll(
                data.getPolls()
        );
    }

    // =========================================================
    // UPDATE UPCOMING
    // =========================================================

    private void updateUpcoming(
        List<Map<String, Object>> maintenanceList) {

    upcomingBox.getChildren().clear();

    Label title =
            new Label("Upcoming / Due");

    title.setFont(
            Font.font(
                    "System",
                    FontWeight.BOLD,
                    17
            )
    );

    title.setTextFill(
            Color.web("#263238")
    );

    upcomingBox.getChildren().add(title);

    if (maintenanceList == null
            || maintenanceList.isEmpty()) {

        addDueRow(
                "Maintenance Due",
                "No outstanding due"
        );

        addDueRow(
                "Water Bill",
                "Not configured"
        );

        addDueRow(
                "Electricity Bill",
                "Not configured"
        );

        return;
    }

    Map<String, Object> next =
            findNextOutstandingMaintenance(
                    maintenanceList
            );

    if (next != null) {

        String date =
                safe(
                        next.get("date")
                                == null
                                ? ""
                                : next.get("date").toString()
                );

        addDueRow(
                "Maintenance Due",
                formatDisplayDate(date)
        );

    } else {

        addDueRow(
                "Maintenance Due",
                "No outstanding due"
        );
    }

    addDueRow(
            "Water Bill",
            "Not configured"
    );

    addDueRow(
            "Electricity Bill",
            "Not configured"
    );
}
    // =========================================================
    // FIND NEXT OUTSTANDING MAINTENANCE
    // =========================================================

    private Map<String, Object> findNextOutstandingMaintenance(
        List<Map<String, Object>> maintenanceList) {

    Map<String, Object> selected = null;

    LocalDate selectedDate =
            LocalDate.MAX;

    for (Map<String, Object> maintenance
            : maintenanceList) {

        if (maintenance == null) {
            continue;
        }

        // -------------------------------------------------
        // GET STATUS FROM MAP
        // -------------------------------------------------

        Object statusObject =
                maintenance.get("status");

        String status =
                statusObject == null
                        ? ""
                        : statusObject.toString().trim()
                                .toLowerCase();

        // -------------------------------------------------
        // OUTSTANDING STATUS
        // -------------------------------------------------

        boolean outstanding =
                status.isEmpty()
                        || "pending".equals(status)
                        || "overdue".equals(status)
                        || "unpaid".equals(status)
                        || "open".equals(status);

        if (!outstanding) {
            continue;
        }

        // -------------------------------------------------
        // GET DATE FROM MAP
        // -------------------------------------------------

        Object dateObject =
                maintenance.get("date");

        String date =
                dateObject == null
                        ? ""
                        : dateObject.toString().trim();

        // -------------------------------------------------
        // NO DATE
        // -------------------------------------------------

        if (date.isEmpty()) {

            if (selected == null) {
                selected = maintenance;
            }

            continue;
        }

        // -------------------------------------------------
        // PARSE DATE
        // -------------------------------------------------

        try {

            LocalDate parsed =
                    LocalDate.parse(date);

            if (parsed.isBefore(selectedDate)) {

                selectedDate = parsed;
                selected = maintenance;
            }

        } catch (Exception ignored) {

            if (selected == null) {
                selected = maintenance;
            }
        }
    }

    return selected;
}

    // =========================================================
    // GET STRING FROM MAINTENANCE MAP
    // =========================================================

    private String getMapString(
            Map<String, Object> map,
            String key) {

        if (map == null || key == null) {
            return "";
        }

        Object value =
                map.get(key);

        if (value == null) {
            return "";
        }

        return value.toString().trim();
    }

    // =========================================================
    // UPDATE NOTICES
    // =========================================================

    private void updateNotices(
            List<NoticeModel> notices) {

        noticeContent.getChildren().clear();

        if (notices == null
                || notices.isEmpty()) {

            showNoticeMessage(
                    "No society notices available."
            );

            return;
        }

        int count =
                Math.min(
                        notices.size(),
                        3
                );

        for (int i = 0;
             i < count;
             i++) {

            NoticeModel notice =
                    notices.get(i);

            if (notice == null) {
                continue;
            }

            VBox card =
                    new VBox(4);

            card.setPadding(
                    new Insets(
                            7,
                            0,
                            7,
                            0
                    )
            );

            String titleText =
                    safe(
                            notice.getTitle()
                    );

            if (titleText.isEmpty()) {
                titleText =
                        "Society Notice";
            }

            Label title =
                    new Label(
                            "• "
                                    + titleText
                    );

            title.setFont(
                    Font.font(
                            "System",
                            FontWeight.BOLD,
                            13
                    )
            );

            title.setTextFill(
                    Color.web("#263238")
            );

            title.setWrapText(true);

            Label date =
                    new Label(
                            safe(
                                    notice.getDate()
                            )
                    );

            date.setFont(
                    Font.font(
                            "System",
                            11
                    )
            );

            date.setTextFill(
                    Color.web("#789098")
            );

            Label description =
                    new Label(
                            safe(
                                    notice.getDescription()
                            )
                    );

            description.setFont(
                    Font.font(
                            "System",
                            12
                    )
            );

            description.setTextFill(
                    Color.web("#607D8B")
            );

            description.setWrapText(true);

            card.getChildren().addAll(
                    title,
                    date,
                    description
            );

            String status =
                    safe(
                            notice.getStatus()
                    );

            if (!status.isEmpty()) {

                Label statusLabel =
                        new Label(
                                "Status: "
                                        + status
                        );

                statusLabel.setFont(
                        Font.font(
                                "System",
                                FontWeight.BOLD,
                                11
                        )
                );

                statusLabel.setTextFill(
                        Color.web("#789098")
                );

                card.getChildren().add(
                        statusLabel
                );
            }

            noticeContent.getChildren().add(
                    card
            );
        }
    }

    // =========================================================
    // UPDATE POLL
    // =========================================================

    private void updatePoll(
            List<PollModel> polls) {

        pollContent.getChildren().clear();

        if (polls == null
                || polls.isEmpty()) {

            showPollMessage(
                    "No active community poll."
            );

            return;
        }

        PollModel poll =
                polls.get(0);

        if (poll == null) {

            showPollMessage(
                    "No active community poll."
            );

            return;
        }

        Label question =
                new Label(
                        safe(
                                poll.getQuestion()
                        )
                );

        question.setWrapText(true);

        question.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        question.setTextFill(
                Color.web("#263238")
        );

        pollContent.getChildren().add(
                question
        );

        List<String> options =
                poll.getOptions();

        Map<String, Long> counts =
                poll.getVoteCounts();

        long totalVotes =
                poll.getTotalVotes();

        if (options == null
                || options.isEmpty()) {

            showPollMessage(
                    "Poll has no options."
            );

            return;
        }

        int displayCount =
                Math.min(
                        options.size(),
                        4
                );

        for (int i = 0;
             i < displayCount;
             i++) {

            String option =
                    safe(
                            options.get(i)
                    );

            if (option.isEmpty()) {
                continue;
            }

            long votes = 0;

            if (counts != null
                    && counts.containsKey(option)
                    && counts.get(option) != null) {

                votes =
                        counts.get(option);
            }

            double percentage =
                    totalVotes <= 0
                            ? 0.0
                            : (double) votes
                            / totalVotes;

            Label optionLabel =
                    new Label(
                            option
                                    + " ("
                                    + Math.round(
                                    percentage * 100
                            )
                                    + "%)"
                    );

            optionLabel.setFont(
                    Font.font(
                            "System",
                            FontWeight.BOLD,
                            12
                    )
            );

            ProgressBar bar =
                    new ProgressBar(
                            clampPercentage(
                                    percentage
                            )
                    );

            bar.setPrefWidth(250);

            pollContent.getChildren().addAll(
                    optionLabel,
                    bar
            );
        }

        Button openPoll =
                new Button(
                        "Open Polls & Surveys"
                );

        openPoll.setStyle(
                "-fx-background-color:#4e342e;"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:6;"
        );

        openPoll.setOnAction(
                e -> openPolls(stageForPoll())
        );

        pollContent.getChildren().add(
                openPoll
        );
    }

    // =========================================================
    // POLL STAGE
    // =========================================================

    private Stage stageForPoll() {

        if (residentDashboardScene == null) {
            return null;
        }

        return (Stage)
                residentDashboardScene
                        .getWindow();
    }

    // =========================================================
    // OPEN POLLS
    // =========================================================

    private void openPolls(Stage stage) {

        if (stage == null) {
            return;
        }

        PollsSurveys polls =
                new PollsSurveys(
                        stage,
                        this.LognEmail
                );

        stage.setScene(
                polls.createScene(
                        stage,
                        LognEmail
                )
        );

        stage.show();
    }

    // =========================================================
    // OPEN BILLS
    // =========================================================

    private void openMyBills(
            Stage stage) {

        if (stage == null
                || this.LognEmail.isEmpty()) {
            return;
        }

        Mybills bills =
                new Mybills(
                        this.LognEmail
                );

        stage.setScene(
                bills.getBillScene(stage)
        );

        stage.show();
    }

    // =========================================================
    // OPEN COMPLAINTS
    // =========================================================

    private void openComplaints(
            Stage stage) {

        if (stage == null) {
            return;
        }

        Complaint complaint =
                new Complaint();

        stage.setScene(
                complaint.getComplaintScene(
                        stage
                )
        );

        stage.show();
    }

    // =========================================================
    // OPEN VISITORS
    // =========================================================

    private void openVisitors(
            Stage stage) {

        if (stage == null) {
            return;
        }

        Visitor visitor =
                new Visitor();

        Scene visitorScene =
                visitor.getVisitorScene(
                        stage
                );

        // Visitor shows its own login/session alert when it cannot build a
        // scene. Do not replace the dashboard with a null scene.
        if (visitorScene == null) {
            return;
        }

        stage.setScene(
                visitorScene
        );

        stage.show();
    }

    // =========================================================
    // LOADING DUE ROWS
    // =========================================================

    private void addLoadingDueRows() {

        addDueRow(
                "Maintenance Due",
                "Loading..."
        );

        addDueRow(
                "Water Bill",
                "Loading..."
        );

        addDueRow(
                "Electricity Bill",
                "Loading..."
        );
    }

    // =========================================================
    // ADD DUE ROW
    // =========================================================

    private void addDueRow(
            String title,
            String value) {

        HBox row =
                new HBox();

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12
                )
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12
                )
        );

        valueLabel.setTextFill(
                Color.web("#607D8B")
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        row.getChildren().addAll(
                titleLabel,
                spacer,
                valueLabel
        );

        upcomingBox.getChildren().add(
                row
        );
    }

    // =========================================================
    // SHOW DUE MESSAGE
    // =========================================================

    private void showDueMessage(
            String message) {

        upcomingBox.getChildren().clear();

        Label title =
                new Label(
                        "Upcoming / Due"
                );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );

        title.setTextFill(
                Color.web("#263238")
        );

        Label messageLabel =
                new Label(message);

        messageLabel.setTextFill(
                Color.web("#789098")
        );

        upcomingBox.getChildren().addAll(
                title,
                messageLabel
        );
    }

    // =========================================================
    // SHOW NOTICE MESSAGE
    // =========================================================

    private void showNoticeMessage(
            String message) {

        noticeContent.getChildren().clear();

        Label label =
                new Label(message);

        label.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        label.setTextFill(
                Color.web("#789098")
        );

        label.setWrapText(true);

        noticeContent.getChildren().add(
                label
        );
    }

    // =========================================================
    // SHOW POLL MESSAGE
    // =========================================================

    private void showPollMessage(
            String message) {

        pollContent.getChildren().clear();

        Label label =
                new Label(message);

        label.setFont(
                Font.font(
                        "System",
                        13
                )
        );

        label.setTextFill(
                Color.web("#789098")
        );

        label.setWrapText(true);

        pollContent.getChildren().add(
                label
        );
    }

    // =========================================================
    // SUMMARY CARD
    // =========================================================

    private VBox createSummaryCard(
            String title,
            String amount,
            String bottomText) {

        VBox card =
                new VBox(7);

        card.setPadding(
                new Insets(16)
        );

        card.setPrefWidth(
                190
        );

        card.setPrefHeight(
                110
        );

        card.setStyle(
                "-fx-background-color:white;"
                        + "-fx-background-radius:10;"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        titleLabel.setTextFill(
                Color.web("#607D8B")
        );

        Label amountLabel =
                new Label(amount);

        amountLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        22
                )
        );

        amountLabel.setTextFill(
                Color.web("#263238")
        );

        Label bottomLabel =
                new Label(bottomText);

        bottomLabel.setFont(
                Font.font(
                        "System",
                        12
                )
        );

        bottomLabel.setTextFill(
                Color.web("#789098")
        );

        card.getChildren().addAll(
                titleLabel,
                amountLabel,
                bottomLabel
        );

        return card;
    }

    // =========================================================
    // GET AMOUNT LABEL
    // =========================================================

    private Label getAmountLabel(
            VBox card) {

        return (Label)
                card.getChildren().get(1);
    }

    // =========================================================
    // GET BOTTOM LABEL
    // =========================================================

    private Label getBottomLabel(
            VBox card) {

        return (Label)
                card.getChildren().get(2);
    }

    // =========================================================
    // GET BOTTOM BUTTON
    // =========================================================

    private Button getBottomButton(
            VBox card) {

        if (card.getChildren().size() < 3) {
            return null;
        }

        Label bottom =
                (Label)
                        card.getChildren().get(2);

        Button button =
                new Button();

        button.setGraphic(bottom);

        button.setStyle(
                "-fx-background-color:transparent;"
                        + "-fx-padding:0;"
        );

        card.getChildren().set(
                2,
                button
        );

        return button;
    }

    // =========================================================
    // SET AMOUNT
    // =========================================================

    private void setAmount(
            Label label,
            String value) {

        if (label != null) {

            label.setText(
                    value == null
                            ? ""
                            : value
            );
        }
    }

    // =========================================================
    // SET BOTTOM
    // =========================================================

    private void setBottom(
            Label label,
            String value) {

        if (label != null) {

            label.setText(
                    value == null
                            ? ""
                            : value
            );
        }
    }

    // =========================================================
    // SECTION TITLE
    // =========================================================

    private Label createSectionTitle(
            String text) {

        Label label =
                new Label(text);

        label.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );

        label.setTextFill(
                Color.web("#263238")
        );

        return label;
    }

    // =========================================================
    // ACTION BUTTON
    // =========================================================

    private Button createActionButton(
            String title,
            String subtitle) {

        Button button =
                new Button();

        button.setPrefSize(
                230,
                65
        );

        button.setAlignment(
                Pos.CENTER_LEFT
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13
                )
        );

        Label subtitleLabel =
                new Label(subtitle);

        subtitleLabel.setFont(
                Font.font(
                        "System",
                        11
                )
        );

        subtitleLabel.setTextFill(
                Color.GRAY
        );

        VBox content =
                new VBox(4);

        content.getChildren().addAll(
                titleLabel,
                subtitleLabel
        );

        button.setGraphic(
                content
        );

        button.setStyle(
                "-fx-background-color:#F5F7F8;"
                        + "-fx-border-color:#D5DDE0;"
                        + "-fx-border-radius:7;"
                        + "-fx-background-radius:7;"
        );

        return button;
    }

    // =========================================================
    // FORMAT MONEY
    // =========================================================

    private String formatMoney(
            double value) {

        if (Math.abs(
                value - Math.rint(value)
        ) < 0.000001) {

            return "₹ "
                    + String.format(
                    "%.0f",
                    value
            );
        }

        return "₹ "
                + String.format(
                "%.2f",
                value
        );
    }

    // =========================================================
    // FORMAT DATE
    // =========================================================

    private String formatDisplayDate(
            String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return "Not available";
        }

        String clean =
                value.trim();

        try {

            return LocalDate.parse(clean)
                    .format(
                            DateTimeFormatter.ofPattern(
                                    "dd MMM yyyy"
                            )
                    );

        } catch (Exception ignored) {

            return clean;
        }
    }

    // =========================================================
    // CLAMP PERCENTAGE
    // =========================================================

    private double clampPercentage(
            double value) {

        if (value < 0) {
            return 0;
        }

        if (value > 1) {
            return 1;
        }

        return value;
    }

    // =========================================================
    // GREETING
    // =========================================================

    private String getGreeting() {

        int hour =
                java.time.LocalTime.now(
                        INDIA_ZONE
                ).getHour();

        if (hour < 12) {
            return "Good Morning";
        }

        if (hour < 17) {
            return "Good Afternoon";
        }

        return "Good Evening";
    }

    // =========================================================
    // TOWER TEXT
    // =========================================================

    private String getTowerText(
            User resident) {

        if (resident == null) {
            return "";
        }

        String[] candidates = {

                readUserProperty(
                        resident,
                        "getTower"
                ),

                readUserProperty(
                        resident,
                        "getTowerNo"
                ),

                readUserProperty(
                        resident,
                        "getBuilding"
                ),

                readUserProperty(
                        resident,
                        "getBuildingName"
                )
        };

        for (String value : candidates) {

            if (value != null
                    && !value.trim().isEmpty()) {

                return " • "
                        + value.trim();
            }
        }

        return "";
    }

    // =========================================================
    // OPTIONAL USER PROPERTY
    // =========================================================

    private String readUserProperty(
            User resident,
            String methodName) {

        try {

            java.lang.reflect.Method method =
                    resident.getClass()
                            .getMethod(
                                    methodName
                            );

            Object value =
                    method.invoke(
                            resident
                    );

            return value == null
                    ? ""
                    : value.toString();

        } catch (Exception ignored) {

            return "";
        }
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safe(String value) {

        return value == null
                ? ""
                : value.trim();
    }
}
