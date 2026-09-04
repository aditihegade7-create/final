package com.society.dao.Resident_dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.model.Resident_model.PollModel;
import com.society.model.Resident_model.PollVoteModel;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollDao {

    // =====================================================
    // FIRESTORE STRUCTURE
    // =====================================================

    /*
     * ACTUAL FIRESTORE STRUCTURE:
     *
     * Polls
     *   └── secretaryEmail
     *       └── polls
     *           └── pollId
     *               ├── createdByEmail
     *               ├── createdDate
     *               ├── question
     *               ├── description
     *               ├── endDate
     *               ├── options
     *               ├── voteCounts
     *               ├── totalVotes
     *               ├── status
     *               └── ...
     *
     * Votes:
     *
     * Polls/{secretaryEmail}/polls/{pollId}/votes/{residentEmail}
     */

    private static final String COLLECTION = "Polls";
    private static final String POLL_SUB_COLLECTION = "polls";
    private static final String VOTE_COLLECTION = "votes";

    private final Firestore firestore;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public PollDao(Firestore firestore) {

        if (firestore == null) {
            throw new IllegalArgumentException(
                    "Firestore connection is null."
            );
        }

        this.firestore = firestore;
    }


    // =====================================================
    // GET ACTIVE POLLS
    // =====================================================

    public List<PollModel> getActivePolls() throws Exception {

        List<PollModel> activePolls = new ArrayList<>();

        System.out.println("==========================================");
        System.out.println("FETCHING ACTIVE POLLS FROM FIRESTORE");
        System.out.println("==========================================");

        System.out.println(
                "Project ID: "
                        + firestore.getOptions().getProjectId()
        );

        System.out.println(
                "Database ID: "
                        + firestore.getOptions().getDatabaseId()
        );

        System.out.println(
                "Collection Group: "
                        + POLL_SUB_COLLECTION
        );

        System.out.println("==========================================");

        /*
         * IMPORTANT:
         *
         * The Firestore parent documents under Polls may not exist as
         * actual documents. The poll documents do exist inside the
         * subcollection:
         *
         * Polls/{secretaryEmail}/polls/{pollId}
         *
         * Therefore we use a collection-group query instead of:
         *
         * firestore.collection("Polls").get()
         */

        QuerySnapshot pollSnapshot = firestore
                .collectionGroup(POLL_SUB_COLLECTION)
                .get()
                .get();

        System.out.println(
                "Poll documents found: "
                        + pollSnapshot.size()
        );

        LocalDate today = LocalDate.now();

        for (DocumentSnapshot pollDocument :
                pollSnapshot.getDocuments()) {

            try {

                String secretaryEmail =
                        getSecretaryEmailFromPollDocument(
                                pollDocument
                        );

                PollModel poll =
                        convertPoll(
                                pollDocument,
                                secretaryEmail
                        );

                if (poll == null) {
                    continue;
                }

                String status = poll.getStatus();
                String endDate = poll.getEndDate();

                System.out.println(
                        "Checking poll: "
                                + pollDocument.getId()
                                + " | Status: "
                                + status
                                + " | End Date: "
                                + endDate
                );

                // Only ACTIVE polls should be shown to residents.
                if (status == null ||
                        !"ACTIVE".equalsIgnoreCase(
                                status.trim()
                        )) {
                    continue;
                }

                // If there is no end date, an ACTIVE poll is valid.
                if (endDate == null ||
                        endDate.trim().isEmpty()) {

                    activePolls.add(poll);

                    System.out.println(
                            "ACTIVE POLL FOUND: "
                                    + poll.getQuestion()
                    );

                    continue;
                }

                LocalDate pollEndDate;

                try {

                    pollEndDate = LocalDate.parse(
                            endDate.trim()
                    );

                } catch (DateTimeParseException e) {

                    System.out.println(
                            "Invalid poll end date: "
                                    + endDate
                                    + " | Poll ID: "
                                    + pollDocument.getId()
                    );

                    continue;
                }

                // The poll remains active through its end date.
                if (!today.isAfter(pollEndDate)) {

                    activePolls.add(poll);

                    System.out.println(
                            "ACTIVE POLL FOUND: "
                                    + poll.getQuestion()
                    );
                }

            } catch (Exception e) {

                System.out.println(
                        "Error processing poll: "
                                + pollDocument.getId()
                );

                e.printStackTrace();
            }
        }

        System.out.println(
                "TOTAL ACTIVE POLLS FETCHED: "
                        + activePolls.size()
        );

        System.out.println(
                "=========================================="
        );

        return activePolls;
    }

    // =====================================================
    // GET PAST POLLS
    // =====================================================

    public List<PollModel> getPastPolls() throws Exception {

        List<PollModel> pastPolls = new ArrayList<>();

        System.out.println("==========================================");
        System.out.println("FETCHING PAST POLLS FROM FIRESTORE");
        System.out.println("Collection Group: " + POLL_SUB_COLLECTION);
        System.out.println("==========================================");

        QuerySnapshot pollSnapshot = firestore
                .collectionGroup(POLL_SUB_COLLECTION)
                .get()
                .get();

        System.out.println(
                "Poll documents found: "
                        + pollSnapshot.size()
        );

        LocalDate today = LocalDate.now();

        for (DocumentSnapshot pollDocument :
                pollSnapshot.getDocuments()) {

            try {

                String secretaryEmail =
                        getSecretaryEmailFromPollDocument(
                                pollDocument
                        );

                PollModel poll =
                        convertPoll(
                                pollDocument,
                                secretaryEmail
                        );

                if (poll == null) {
                    continue;
                }

                String endDate = poll.getEndDate();

                if (endDate == null ||
                        endDate.trim().isEmpty()) {
                    continue;
                }

                try {

                    LocalDate pollEndDate =
                            LocalDate.parse(
                                    endDate.trim()
                            );

                    if (today.isAfter(pollEndDate)) {

                        pastPolls.add(poll);

                        System.out.println(
                                "PAST POLL FOUND: "
                                        + poll.getQuestion()
                        );
                    }

                } catch (DateTimeParseException e) {

                    System.out.println(
                            "Invalid poll end date: "
                                    + endDate
                                    + " | Poll ID: "
                                    + pollDocument.getId()
                    );
                }

            } catch (Exception e) {

                System.out.println(
                        "Error processing past poll: "
                                + pollDocument.getId()
                );

                e.printStackTrace();
            }
        }

        System.out.println(
                "TOTAL PAST POLLS FETCHED: "
                        + pastPolls.size()
        );

        System.out.println(
                "=========================================="
        );

        return pastPolls;
    }


    // =====================================================
    // FIND POLL REFERENCE
    // =====================================================

    /*
     * Finds:
     *
     * Polls/{secretaryEmail}/polls/{pollId}
     *
     * without hardcoding the secretary email.
     */

    private DocumentReference findPollReference(
            String pollId)
            throws Exception {

        if (pollId == null ||
                pollId.trim().isEmpty()) {

            throw new Exception(
                    "Poll identifier is missing."
            );
        }

        String requestedPollId =
                pollId.trim();

        /*
         * Do not query Polls/{secretaryEmail} here because the parent
         * secretary documents may not exist. Search the actual poll
         * documents using a collection-group query.
         */
        QuerySnapshot pollSnapshot = firestore
                .collectionGroup(POLL_SUB_COLLECTION)
                .get()
                .get();

        for (DocumentSnapshot pollDocument :
                pollSnapshot.getDocuments()) {

            if (requestedPollId.equals(
                    pollDocument.getId()
            )) {

                return pollDocument.getReference();
            }
        }

        return null;
    }


    // =====================================================
    // GET SECRETARY EMAIL FROM POLL PATH
    // =====================================================

    private String getSecretaryEmailFromPollDocument(
            DocumentSnapshot pollDocument) {

        /*
         * Expected poll path:
         *
         * Polls/{secretaryEmail}/polls/{pollId}
         *
         * pollDocument.getReference()          -> poll document
         * getParent()                           -> polls collection
         * getParent().getParent()               -> secretary document
         */

        try {

            DocumentReference secretaryReference =
                    pollDocument
                            .getReference()
                            .getParent()
                            .getParent();

            if (secretaryReference != null) {
                return secretaryReference.getId();
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to determine secretary email for poll: "
                            + pollDocument.getId()
            );
        }

        return null;
    }


    // =====================================================
    // CONVERT POLL
    // =====================================================

    private PollModel convertPoll(
            DocumentSnapshot document,
            String secretaryEmail) {

        try {

            PollModel poll =
                    new PollModel();


            // -------------------------------------------------
            // POLL ID
            // -------------------------------------------------

            poll.setId(
                    document.getId()
            );

            poll.setPollId(
                    document.getId()
            );


            // -------------------------------------------------
            // CREATED BY EMAIL
            // -------------------------------------------------

            String createdByEmail =
                    document.getString(
                            "createdByEmail"
                    );


            if (createdByEmail == null ||
                    createdByEmail.trim().isEmpty()) {

                createdByEmail =
                        secretaryEmail;
            }


            poll.setCreatedByEmail(
                    createdByEmail.trim()
            );


            // -------------------------------------------------
            // CREATED DATE
            // -------------------------------------------------

            poll.setCreatedDate(
                    getString(
                            document,
                            "createdDate"
                    )
            );


            // -------------------------------------------------
            // QUESTION
            // -------------------------------------------------

            poll.setQuestion(
                    getString(
                            document,
                            "question"
                    )
            );


            // -------------------------------------------------
            // DESCRIPTION
            // -------------------------------------------------

            poll.setDescription(
                    getString(
                            document,
                            "description"
                    )
            );


            // -------------------------------------------------
            // END DATE
            // -------------------------------------------------

            poll.setEndDate(
                    getString(
                            document,
                            "endDate"
                    )
            );


            // -------------------------------------------------
            // TYPE
            // -------------------------------------------------

            poll.setType(
                    getString(
                            document,
                            "type"
                    )
            );


            // -------------------------------------------------
            // TARGET AUDIENCE
            // -------------------------------------------------

            poll.setTargetAudience(
                    getString(
                            document,
                            "targetAudience"
                    )
            );


            // -------------------------------------------------
            // STATUS
            // -------------------------------------------------

            poll.setStatus(
                    getString(
                            document,
                            "status"
                    )
            );


            // -------------------------------------------------
            // OPTIONS
            // -------------------------------------------------

            Object optionsObject =
                    document.get("options");


            List<String> options =
                    new ArrayList<>();


            if (optionsObject instanceof List<?>) {

                List<?> rawOptions =
                        (List<?>) optionsObject;


                for (Object option :
                        rawOptions) {

                    if (option == null) {
                        continue;
                    }


                    String value =
                            option.toString().trim();


                    if (!value.isEmpty()) {

                        options.add(value);
                    }
                }
            }


            poll.setOptions(options);


            // -------------------------------------------------
            // VOTE COUNTS
            // -------------------------------------------------

            Map<String, Long> voteCounts =
                    new HashMap<>();


            Object countsObject =
                    document.get("voteCounts");


            // =================================================
            // LIST
            // =================================================

            if (countsObject instanceof List<?>) {

                List<?> rawCounts =
                        (List<?>) countsObject;


                for (int i = 0;
                     i < rawCounts.size();
                     i++) {

                    String option;


                    if (i < options.size()) {

                        option =
                                options.get(i);

                    } else {

                        option =
                                "Option " + (i + 1);
                    }


                    long count = 0;


                    Object value =
                            rawCounts.get(i);


                    if (value instanceof Number) {

                        count =
                                ((Number) value)
                                        .longValue();

                    } else if (value != null) {

                        try {

                            count =
                                    Long.parseLong(
                                            value.toString()
                                    );

                        } catch (
                                NumberFormatException ignored) {

                            count = 0;
                        }
                    }


                    voteCounts.put(
                            option,
                            count
                    );
                }
            }


            // =================================================
            // MAP
            // =================================================

            else if (countsObject instanceof Map<?, ?>) {

                Map<?, ?> rawCounts =
                        (Map<?, ?>) countsObject;


                for (Map.Entry<?, ?> entry :
                        rawCounts.entrySet()) {

                    if (entry.getKey() == null) {
                        continue;
                    }


                    String option =
                            entry.getKey()
                                    .toString();


                    long count = 0;


                    Object value =
                            entry.getValue();


                    if (value instanceof Number) {

                        count =
                                ((Number) value)
                                        .longValue();

                    } else if (value != null) {

                        try {

                            count =
                                    Long.parseLong(
                                            value.toString()
                                    );

                        } catch (
                                NumberFormatException ignored) {

                            count = 0;
                        }
                    }


                    voteCounts.put(
                            option,
                            count
                    );
                }
            }


            poll.setVoteCounts(
                    voteCounts
            );


            // -------------------------------------------------
            // TOTAL VOTES
            // -------------------------------------------------

            Object totalObject =
                    document.get("totalVotes");


            long totalVotes = 0;


            if (totalObject instanceof Number) {

                totalVotes =
                        ((Number) totalObject)
                                .longValue();

            } else if (totalObject != null) {

                try {

                    totalVotes =
                            Long.parseLong(
                                    totalObject.toString()
                            );

                } catch (
                        NumberFormatException ignored) {

                    totalVotes = 0;
                }
            }


            poll.setTotalVotes(
                    totalVotes
            );


            // -------------------------------------------------
            // DEBUG
            // -------------------------------------------------

            System.out.println(
                    "Poll loaded successfully:"
            );

            System.out.println(
                    "  Secretary: "
                            + secretaryEmail
            );

            System.out.println(
                    "  ID: "
                            + poll.getId()
            );

            System.out.println(
                    "  Question: "
                            + poll.getQuestion()
            );

            System.out.println(
                    "  Created By: "
                            + poll.getCreatedByEmail()
            );

            System.out.println(
                    "  End Date: "
                            + poll.getEndDate()
            );

            System.out.println(
                    "  Status: "
                            + poll.getStatus()
            );

            System.out.println(
                    "  Options: "
                            + poll.getOptions()
            );

            System.out.println(
                    "  Vote Counts: "
                            + poll.getVoteCounts()
            );

            System.out.println(
                    "  Total Votes: "
                            + poll.getTotalVotes()
            );


            return poll;


        } catch (Exception e) {

            System.out.println(
                    "ERROR CONVERTING POLL: "
                            + document.getId()
            );

            e.printStackTrace();

            return null;
        }
    }


    // =====================================================
    // GET STRING
    // =====================================================

    private String getString(
            DocumentSnapshot document,
            String field) {

        Object value =
                document.get(field);


        if (value == null) {
            return "";
        }


        return value.toString().trim();
    }


    // =====================================================
    // GET RESIDENT VOTE
    // =====================================================

    /*
     * This version does NOT require secretaryEmail.
     *
     * It automatically searches:
     *
     * Polls/{secretaryEmail}/polls/{pollId}
     */

    public PollVoteModel getResidentVote(
            String pollId,
            String residentEmail)
            throws Exception {

        validateEmail(
                residentEmail,
                "Resident email is missing."
        );


        String resident =
                normalizeEmail(
                        residentEmail
                );


        DocumentReference pollReference =
                findPollReference(
                        pollId
                );


        if (pollReference == null) {

            throw new Exception(
                    "Poll does not exist."
            );
        }


        DocumentSnapshot voteDocument =
                pollReference
                        .collection(
                                VOTE_COLLECTION
                        )
                        .document(resident)
                        .get()
                        .get();


        if (!voteDocument.exists()) {

            return null;
        }


        return voteDocument.toObject(
                PollVoteModel.class
        );
    }


    // =====================================================
    // GET RESIDENT VOTE
    // BACKWARD COMPATIBILITY
    // =====================================================

    public PollVoteModel getResidentVote(
            String secretaryEmail,
            String pollId,
            String residentEmail)
            throws Exception {

        validateEmail(
                residentEmail,
                "Resident email is missing."
        );


        if (secretaryEmail == null ||
                secretaryEmail.trim().isEmpty()) {

            return getResidentVote(
                    pollId,
                    residentEmail
            );
        }


        String secretary =
                secretaryEmail.trim();


        String resident =
                normalizeEmail(
                        residentEmail
                );


        DocumentSnapshot voteDocument =
                firestore
                        .collection(COLLECTION)
                        .document(secretary)
                        .collection(POLL_SUB_COLLECTION)
                        .document(pollId.trim())
                        .collection(VOTE_COLLECTION)
                        .document(resident)
                        .get()
                        .get();


        if (!voteDocument.exists()) {

            return null;
        }


        return voteDocument.toObject(
                PollVoteModel.class
        );
    }


    // =====================================================
    // SUBMIT VOTE
    // =====================================================

    /*
     * This version automatically finds the secretary.
     *
     * Firestore:
     *
     * Polls/{secretaryEmail}/polls/{pollId}
     *
     * Polls/{secretaryEmail}/polls/{pollId}/votes/{residentEmail}
     */

    public void submitVote(
            String pollId,
            String residentEmail,
            String selectedOption)
            throws Exception {

        validateEmail(
                residentEmail,
                "Resident email is missing."
        );


        if (pollId == null ||
                pollId.trim().isEmpty()) {

            throw new Exception(
                    "Poll identifier is missing."
            );
        }


        if (selectedOption == null ||
                selectedOption.trim().isEmpty()) {

            throw new Exception(
                    "Please select an option."
            );
        }


        final String resident =
                normalizeEmail(
                        residentEmail
                );


        final String option =
                selectedOption.trim();


        final DocumentReference pollReference =
                findPollReference(
                        pollId
                );


        if (pollReference == null) {

            throw new Exception(
                    "Poll does not exist."
            );
        }


        firestore.runTransaction(
                transaction -> {


                    // -----------------------------------------
                    // VOTE REFERENCE
                    // -----------------------------------------

                    DocumentReference voteReference =
                            pollReference
                                    .collection(
                                            VOTE_COLLECTION
                                    )
                                    .document(resident);


                    // -----------------------------------------
                    // READ POLL
                    // -----------------------------------------

                    DocumentSnapshot pollDocument =
                            transaction
                                    .get(pollReference)
                                    .get();


                    if (!pollDocument.exists()) {

                        throw new RuntimeException(
                                "Poll does not exist."
                        );
                    }


                    // -----------------------------------------
                    // CHECK DUPLICATE VOTE
                    // -----------------------------------------

                    DocumentSnapshot voteDocument =
                            transaction
                                    .get(voteReference)
                                    .get();


                    if (voteDocument.exists()) {

                        throw new RuntimeException(
                                "You have already voted in this poll."
                        );
                    }


                    // -----------------------------------------
                    // CHECK STATUS
                    // -----------------------------------------

                    String status =
                            pollDocument.getString(
                                    "status"
                            );


                    if (status != null &&
                            !status.trim().isEmpty() &&
                            !"ACTIVE".equalsIgnoreCase(
                                    status.trim()
                            )) {

                        throw new RuntimeException(
                                "This poll is not active."
                        );
                    }


                    // -----------------------------------------
                    // CHECK END DATE
                    // -----------------------------------------

                    String endDate =
                            pollDocument.getString(
                                    "endDate"
                            );


                    if (endDate != null &&
                            !endDate.trim().isEmpty()) {

                        try {

                            LocalDate end =
                                    LocalDate.parse(
                                            endDate.trim()
                                    );


                            if (LocalDate.now()
                                    .isAfter(end)) {

                                throw new RuntimeException(
                                        "This poll has ended."
                                );
                            }

                        } catch (
                                DateTimeParseException e) {

                            throw new RuntimeException(
                                    "Invalid poll end date."
                            );
                        }
                    }


                    // -----------------------------------------
                    // READ OPTIONS
                    // -----------------------------------------

                    Object optionsObject =
                            pollDocument.get("options");


                    List<String> options =
                            new ArrayList<>();


                    if (optionsObject instanceof List<?>) {

                        for (Object value :
                                (List<?>) optionsObject) {

                            if (value != null) {

                                String optionValue =
                                        value.toString()
                                                .trim();


                                if (!optionValue.isEmpty()) {

                                    options.add(
                                            optionValue
                                    );
                                }
                            }
                        }
                    }


                    if (!options.contains(option)) {

                        throw new RuntimeException(
                                "Selected option is not valid."
                        );
                    }


                    // -----------------------------------------
                    // READ VOTE COUNTS
                    // -----------------------------------------

                    Object countsObject =
                            pollDocument.get(
                                    "voteCounts"
                            );


                    // =================================================
                    // CASE 1: LIST
                    // =================================================

                    if (countsObject instanceof List<?>) {

                        List<?> rawCounts =
                                (List<?>) countsObject;


                        List<Object> newCounts =
                                new ArrayList<>(
                                        rawCounts
                                );


                        int optionIndex =
                                options.indexOf(option);


                        while (newCounts.size()
                                < options.size()) {

                            newCounts.add(0L);
                        }


                        Object currentValue =
                                newCounts.get(
                                        optionIndex
                                );


                        long currentCount = 0;


                        if (currentValue instanceof Number) {

                            currentCount =
                                    ((Number) currentValue)
                                            .longValue();

                        } else if (currentValue != null) {

                            try {

                                currentCount =
                                        Long.parseLong(
                                                currentValue
                                                        .toString()
                                        );

                            } catch (
                                    NumberFormatException ignored) {

                                currentCount = 0;
                            }
                        }


                        newCounts.set(
                                optionIndex,
                                currentCount + 1
                        );


                        long totalVotes =
                                getLongValue(
                                        pollDocument,
                                        "totalVotes"
                                );


                        transaction.update(
                                pollReference,
                                "voteCounts",
                                newCounts,
                                "totalVotes",
                                totalVotes + 1
                        );
                    }


                    // =================================================
                    // CASE 2: MAP
                    // =================================================

                    else {

                        Map<String, Object> newCounts =
                                new HashMap<>();


                        if (countsObject instanceof Map<?, ?>) {

                            Map<?, ?> rawCounts =
                                    (Map<?, ?>) countsObject;


                            for (Map.Entry<?, ?> entry :
                                    rawCounts.entrySet()) {

                                if (entry.getKey() == null) {
                                    continue;
                                }


                                String key =
                                        entry.getKey()
                                                .toString();


                                long count =
                                        convertToLong(
                                                entry.getValue()
                                        );


                                newCounts.put(
                                        key,
                                        count
                                );
                            }
                        }


                        long currentCount = 0;


                        Object currentValue =
                                newCounts.get(option);


                        if (currentValue instanceof Number) {

                            currentCount =
                                    ((Number) currentValue)
                                            .longValue();
                        }


                        newCounts.put(
                                option,
                                currentCount + 1
                        );


                        long totalVotes =
                                getLongValue(
                                        pollDocument,
                                        "totalVotes"
                                );


                        transaction.update(
                                pollReference,
                                "voteCounts",
                                newCounts,
                                "totalVotes",
                                totalVotes + 1
                        );
                    }


                    // -----------------------------------------
                    // CREATE VOTE DOCUMENT
                    // -----------------------------------------

                    Map<String, Object> voteData =
                            new HashMap<>();


                    voteData.put(
                            "residentEmail",
                            resident
                    );


                    voteData.put(
                            "pollId",
                            pollId.trim()
                    );


                    voteData.put(
                            "selectedOption",
                            option
                    );


                    voteData.put(
                            "votedDate",
                            LocalDate.now().toString()
                    );


                    transaction.set(
                            voteReference,
                            voteData
                    );


                    return null;
                }
        );
    }


    // =====================================================
    // SUBMIT VOTE
    // BACKWARD COMPATIBILITY
    // =====================================================

    public void submitVote(
            String secretaryEmail,
            String pollId,
            String residentEmail,
            String selectedOption)
            throws Exception {

        /*
         * If secretaryEmail is supplied by the existing UI,
         * use it directly.
         */

        if (secretaryEmail != null &&
                !secretaryEmail.trim().isEmpty()) {

            submitVoteWithSecretary(
                    secretaryEmail,
                    pollId,
                    residentEmail,
                    selectedOption
            );

        } else {

            submitVote(
                    pollId,
                    residentEmail,
                    selectedOption
            );
        }
    }


    // =====================================================
    // SUBMIT VOTE USING SECRETARY EMAIL
    // =====================================================

    private void submitVoteWithSecretary(
            String secretaryEmail,
            String pollId,
            String residentEmail,
            String selectedOption)
            throws Exception {

        validateEmail(
                residentEmail,
                "Resident email is missing."
        );


        if (pollId == null ||
                pollId.trim().isEmpty()) {

            throw new Exception(
                    "Poll identifier is missing."
            );
        }


        if (selectedOption == null ||
                selectedOption.trim().isEmpty()) {

            throw new Exception(
                    "Please select an option."
            );
        }


        final String secretary =
                secretaryEmail.trim();

        final String pollIdFinal =
                pollId.trim();

        final String resident =
                normalizeEmail(
                        residentEmail
                );

        final String option =
                selectedOption.trim();


        final DocumentReference pollReference =
                firestore
                        .collection(COLLECTION)
                        .document(secretary)
                        .collection(POLL_SUB_COLLECTION)
                        .document(pollIdFinal);


        firestore.runTransaction(
                transaction -> {

                    DocumentReference voteReference =
                            pollReference
                                    .collection(
                                            VOTE_COLLECTION
                                    )
                                    .document(resident);


                    DocumentSnapshot pollDocument =
                            transaction
                                    .get(pollReference)
                                    .get();


                    if (!pollDocument.exists()) {

                        throw new RuntimeException(
                                "Poll does not exist."
                        );
                    }


                    DocumentSnapshot voteDocument =
                            transaction
                                    .get(voteReference)
                                    .get();


                    if (voteDocument.exists()) {

                        throw new RuntimeException(
                                "You have already voted in this poll."
                        );
                    }


                    String status =
                            pollDocument.getString(
                                    "status"
                            );


                    if (status != null &&
                            !status.trim().isEmpty() &&
                            !"ACTIVE".equalsIgnoreCase(
                                    status.trim()
                            )) {

                        throw new RuntimeException(
                                "This poll is not active."
                        );
                    }


                    String endDate =
                            pollDocument.getString(
                                    "endDate"
                            );


                    if (endDate != null &&
                            !endDate.trim().isEmpty()) {

                        try {

                            LocalDate end =
                                    LocalDate.parse(
                                            endDate.trim()
                                    );


                            if (LocalDate.now()
                                    .isAfter(end)) {

                                throw new RuntimeException(
                                        "This poll has ended."
                                );
                            }

                        } catch (
                                DateTimeParseException e) {

                            throw new RuntimeException(
                                    "Invalid poll end date."
                            );
                        }
                    }


                    Object optionsObject =
                            pollDocument.get("options");


                    List<String> options =
                            new ArrayList<>();


                    if (optionsObject instanceof List<?>) {

                        for (Object value :
                                (List<?>) optionsObject) {

                            if (value != null) {

                                String valueString =
                                        value.toString()
                                                .trim();


                                if (!valueString.isEmpty()) {

                                    options.add(
                                            valueString
                                    );
                                }
                            }
                        }
                    }


                    if (!options.contains(option)) {

                        throw new RuntimeException(
                                "Selected option is not valid."
                        );
                    }


                    Object countsObject =
                            pollDocument.get(
                                    "voteCounts"
                            );


                    if (countsObject instanceof List<?>) {

                        List<?> rawCounts =
                                (List<?>) countsObject;


                        List<Object> newCounts =
                                new ArrayList<>(
                                        rawCounts
                                );


                        int optionIndex =
                                options.indexOf(option);


                        while (newCounts.size()
                                < options.size()) {

                            newCounts.add(0L);
                        }


                        long currentCount =
                                convertToLong(
                                        newCounts.get(
                                                optionIndex
                                        )
                                );


                        newCounts.set(
                                optionIndex,
                                currentCount + 1
                        );


                        long totalVotes =
                                getLongValue(
                                        pollDocument,
                                        "totalVotes"
                                );


                        transaction.update(
                                pollReference,
                                "voteCounts",
                                newCounts,
                                "totalVotes",
                                totalVotes + 1
                        );


                    } else {

                        Map<String, Object> newCounts =
                                new HashMap<>();


                        if (countsObject instanceof Map<?, ?>) {

                            Map<?, ?> rawCounts =
                                    (Map<?, ?>) countsObject;


                            for (Map.Entry<?, ?> entry :
                                    rawCounts.entrySet()) {

                                if (entry.getKey() == null) {
                                    continue;
                                }


                                newCounts.put(
                                        entry.getKey().toString(),
                                        convertToLong(
                                                entry.getValue()
                                        )
                                );
                            }
                        }


                        long currentCount =
                                convertToLong(
                                        newCounts.get(option)
                                );


                        newCounts.put(
                                option,
                                currentCount + 1
                        );


                        long totalVotes =
                                getLongValue(
                                        pollDocument,
                                        "totalVotes"
                                );


                        transaction.update(
                                pollReference,
                                "voteCounts",
                                newCounts,
                                "totalVotes",
                                totalVotes + 1
                        );
                    }


                    Map<String, Object> voteData =
                            new HashMap<>();


                    voteData.put(
                            "residentEmail",
                            resident
                    );


                    voteData.put(
                            "pollId",
                            pollIdFinal
                    );


                    voteData.put(
                            "selectedOption",
                            option
                    );


                    voteData.put(
                            "votedDate",
                            LocalDate.now().toString()
                    );


                    transaction.set(
                            voteReference,
                            voteData
                    );


                    return null;
                }
        );
    }


    // =====================================================
    // GET LONG VALUE
    // =====================================================

    private long getLongValue(
            DocumentSnapshot document,
            String field) {

        return convertToLong(
                document.get(field)
        );
    }


    // =====================================================
    // CONVERT TO LONG
    // =====================================================

    private long convertToLong(
            Object value) {

        if (value == null) {
            return 0;
        }


        if (value instanceof Number) {

            return ((Number) value)
                    .longValue();
        }


        try {

            return Long.parseLong(
                    value.toString()
            );

        } catch (NumberFormatException e) {

            return 0;
        }
    }


    // =====================================================
    // EMAIL VALIDATION
    // =====================================================

    private void validateEmail(
            String email,
            String message)
            throws Exception {

        if (email == null ||
                email.trim().isEmpty()) {

            throw new Exception(
                    message
            );
        }
    }


    // =====================================================
    // NORMALIZE EMAIL
    // =====================================================

    private String normalizeEmail(
            String email) {

        return email
                .trim()
                .toLowerCase();
    }
}