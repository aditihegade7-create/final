package com.society.dao.Secretary_dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.dao.Welcome.UserDao;
import com.society.model.Secretary_model.Poll;
import com.society.model.Welcome.User;

public class PollDao {

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore firestore;

    // =========================================================
    // COLLECTION
    // =========================================================

    private static final String COLLECTION_NAME = "Polls";

    private static final String SUB_COLLECTION = "polls";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PollDao() {

        firestore = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // GET LOGGED-IN SECRETARY
    // =========================================================

    private User getLoggedInSecretary() {

        try {

            UserDao userDao = new UserDao();

            User secretary =
                    userDao.getLoggedInSecretary();

            if (secretary == null) {

                System.out.println(
                        "No logged-in secretary found."
                );

                return null;
            }

            return secretary;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // GET SECRETARY EMAIL
    // =========================================================

    private String getLoggedInEmail() {

        User secretary =
                getLoggedInSecretary();

        if (secretary == null) {
            return null;
        }

        String email =
                secretary.getEmail();

        if (email == null
                || email.trim().isEmpty()) {

            return null;
        }

        return email.trim().toLowerCase();
    }

    // =========================================================
    // GET SECRETARY SOCIETY
    // =========================================================

    private String getLoggedInSociety() {

        User secretary =
                getLoggedInSecretary();

        if (secretary == null) {
            return null;
        }

        String society =
                secretary.getSociety();

        if (society == null
                || society.trim().isEmpty()) {

            return null;
        }

        return society.trim();
    }

    // =========================================================
    // ADD POLL
    // =========================================================

    public boolean addPoll(Poll poll) {

        try {

            // -------------------------------------------------
            // GET LOGGED-IN EMAIL
            // -------------------------------------------------

            String email =
                    getLoggedInEmail();

            if (email == null) {

                System.out.println(
                        "Cannot create poll. "
                        + "Secretary email not found."
                );

                return false;
            }

            // -------------------------------------------------
            // GET SOCIETY
            // -------------------------------------------------

            String society =
                    getLoggedInSociety();

            if (society == null) {

                System.out.println(
                        "Cannot create poll. "
                        + "Secretary society not found."
                );

                return false;
            }

            // -------------------------------------------------
            // PREPARE DATA
            // -------------------------------------------------

            Map<String, Object> pollData =
                    new HashMap<>();

            pollData.put(
                    "question",
                    poll.getQuestion()
            );

            pollData.put(
                    "type",
                    poll.getType()
            );

            pollData.put(
                    "description",
                    poll.getDescription()
            );

            pollData.put(
                    "createdDate",
                    poll.getCreatedDate()
            );

            pollData.put(
                    "endDate",
                    poll.getEndDate()
            );

            pollData.put(
                    "targetAudience",
                    poll.getTargetAudience()
            );

            pollData.put(
                    "status",
                    poll.getStatus()
            );

            pollData.put(
                    "options",
                    poll.getOptions()
            );

            pollData.put(
                    "voteCounts",
                    poll.getVoteCounts()
            );

            pollData.put(
                    "totalVotes",
                    poll.getTotalVotes()
            );

            // -------------------------------------------------
            // IMPORTANT IDENTITY DATA
            // -------------------------------------------------

            pollData.put(
                    "email",
                    email
            );

            pollData.put(
                    "society",
                    society
            );

            pollData.put(
                    "timestamp",
                    System.currentTimeMillis()
            );

            // -------------------------------------------------
            // FIRESTORE
            //
            // Polls
            //    └── email
            //          └── polls
            //                └── auto generated poll ID
            //
            // -------------------------------------------------

            ApiFuture<DocumentReference> future =
                    firestore
                            .collection(COLLECTION_NAME)
                            .document(email)
                            .collection(SUB_COLLECTION)
                            .add(pollData);

            DocumentReference document =
                    future.get();

            System.out.println(
                    "Poll created successfully."
            );

            System.out.println(
                    "Poll ID : "
                            + document.getId()
            );

            System.out.println(
                    "Email   : "
                            + email
            );

            System.out.println(
                    "Society : "
                            + society
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error while adding poll:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET ALL POLLS
    // =========================================================

    public List<Poll> getAllPolls() {

        List<Poll> pollList =
                new ArrayList<>();

        try {

            String email =
                    getLoggedInEmail();

            if (email == null) {
                return pollList;
            }

            // -------------------------------------------------
            // FETCH USING EMAIL
            // -------------------------------------------------

            ApiFuture<QuerySnapshot> future =
                    firestore
                            .collection(COLLECTION_NAME)
                            .document(email)
                            .collection(SUB_COLLECTION)
                            .get();

            QuerySnapshot snapshot =
                    future.get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Poll poll =
                        document.toObject(Poll.class);

                if (poll != null) {

                    pollList.add(poll);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return pollList;
    }

    // =========================================================
    // GET ACTIVE POLLS
    // =========================================================

    public List<Poll> getActivePolls() {

        List<Poll> activePolls =
                new ArrayList<>();

        try {

            String email =
                    getLoggedInEmail();

            if (email == null) {
                return activePolls;
            }

            ApiFuture<QuerySnapshot> future =
                    firestore
                            .collection(COLLECTION_NAME)
                            .document(email)
                            .collection(SUB_COLLECTION)
                            .get();

            QuerySnapshot snapshot =
                    future.get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Poll poll =
                        document.toObject(Poll.class);

                if (poll != null
                        && "ACTIVE".equalsIgnoreCase(
                                poll.getStatus())) {

                    activePolls.add(poll);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return activePolls;
    }

    // =========================================================
    // GET SURVEYS
    // =========================================================

    public List<Poll> getSurveys() {

        List<Poll> surveys =
                new ArrayList<>();

        try {

            String email =
                    getLoggedInEmail();

            if (email == null) {
                return surveys;
            }

            ApiFuture<QuerySnapshot> future =
                    firestore
                            .collection(COLLECTION_NAME)
                            .document(email)
                            .collection(SUB_COLLECTION)
                            .get();

            QuerySnapshot snapshot =
                    future.get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Poll poll =
                        document.toObject(Poll.class);

                if (poll != null
                        && poll.getType() != null
                        && poll.getType()
                                .equalsIgnoreCase("SURVEY")) {

                    surveys.add(poll);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return surveys;
    }

    // =========================================================
    // GET HISTORY
    // =========================================================

    public List<Poll> getHistory() {

        List<Poll> history =
                new ArrayList<>();

        try {

            String email =
                    getLoggedInEmail();

            if (email == null) {
                return history;
            }

            ApiFuture<QuerySnapshot> future =
                    firestore
                            .collection(COLLECTION_NAME)
                            .document(email)
                            .collection(SUB_COLLECTION)
                            .get();

            QuerySnapshot snapshot =
                    future.get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Poll poll =
                        document.toObject(Poll.class);

                if (poll != null
                        && poll.getStatus() != null
                        && poll.getStatus()
                                .equalsIgnoreCase("CLOSED")) {

                    history.add(poll);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return history;
    }

    // =========================================================
    // CLOSE POLL
    // =========================================================

    public boolean closePoll(String pollId) {

        try {

            String email =
                    getLoggedInEmail();

            if (email == null
                    || pollId == null
                    || pollId.trim().isEmpty()) {

                return false;
            }

            DocumentReference pollRef =
                    firestore
                            .collection(COLLECTION_NAME)
                            .document(email)
                            .collection(SUB_COLLECTION)
                            .document(pollId);

            pollRef.update(
                    "status",
                    "CLOSED"
            ).get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // DELETE POLL
    // =========================================================

    public boolean deletePoll(String pollId) {

        try {

            String email =
                    getLoggedInEmail();

            if (email == null
                    || pollId == null
                    || pollId.trim().isEmpty()) {

                return false;
            }

            firestore
                    .collection(COLLECTION_NAME)
                    .document(email)
                    .collection(SUB_COLLECTION)
                    .document(pollId)
                    .delete()
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // ADD VOTE
    // =========================================================

    public boolean addVote(
            String pollId,
            int optionIndex) {

        try {

            String email =
                    getLoggedInEmail();

            if (email == null
                    || pollId == null
                    || pollId.trim().isEmpty()) {

                return false;
            }

            DocumentReference pollRef =
                    firestore
                            .collection(COLLECTION_NAME)
                            .document(email)
                            .collection(SUB_COLLECTION)
                            .document(pollId);

            DocumentSnapshot document =
                    pollRef.get().get();

            if (!document.exists()) {
                return false;
            }

            Poll poll =
                    document.toObject(Poll.class);

            if (poll == null) {
                return false;
            }

            // -------------------------------------------------
            // CHECK OPTION INDEX
            // -------------------------------------------------

            List<String> options =
                    poll.getOptions();

            List<Long> voteCounts =
                    poll.getVoteCounts();

            if (options == null
                    || voteCounts == null) {

                return false;
            }

            if (optionIndex < 0
                    || optionIndex >= options.size()) {

                return false;
            }

            if (optionIndex >= voteCounts.size()) {

                return false;
            }

            // -------------------------------------------------
            // INCREASE VOTE
            // -------------------------------------------------

            Long currentCount =
                    voteCounts.get(optionIndex);

            if (currentCount == null) {
                currentCount = 0L;
            }

            voteCounts.set(
                    optionIndex,
                    currentCount + 1
            );

            // -------------------------------------------------
            // TOTAL VOTES
            // -------------------------------------------------

            long totalVotes =
                    poll.getTotalVotes();

            totalVotes++;

            // -------------------------------------------------
            // UPDATE FIRESTORE
            // -------------------------------------------------

            pollRef.update(
                    "voteCounts",
                    voteCounts,
                    "totalVotes",
                    totalVotes
            ).get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}