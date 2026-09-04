package com.society.controller.Secretary_Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.society.dao.Secretary_dao.PollDao;
import com.society.model.Secretary_model.Poll;

public class PollController {

    // =========================================================
    // DAO
    // =========================================================

    private final PollDao pollDao;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PollController() {
        pollDao = new PollDao();
    }

    // =========================================================
    // CREATE POLL
    // =========================================================

    public boolean createPoll(
            String question,
            String type,
            String description,
            String endDate,
            String targetAudience,
            List<String> options) {

        try {

            if (question == null
                    || question.trim().isEmpty()) {
                return false;
            }

            if (type == null
                    || type.trim().isEmpty()) {
                return false;
            }

            if (endDate == null
                    || endDate.trim().isEmpty()) {
                return false;
            }

            if (options == null
                    || options.size() < 2) {
                return false;
            }

            // -------------------------------------------------
            // CLEAN OPTIONS
            // -------------------------------------------------

            List<String> cleanOptions = new ArrayList<>();

            for (String option : options) {

                if (option != null
                        && !option.trim().isEmpty()) {

                    cleanOptions.add(option.trim());
                }
            }

            if (cleanOptions.size() < 2) {
                return false;
            }

            // -------------------------------------------------
            // INITIAL VOTE COUNTS
            // -------------------------------------------------

            List<Long> voteCounts = new ArrayList<>();

            for (int i = 0;
                    i < cleanOptions.size();
                    i++) {

                voteCounts.add(0L);
            }

            // -------------------------------------------------
            // CREATE POLL OBJECT
            // -------------------------------------------------

            Poll poll = new Poll();

            poll.setQuestion(question.trim());

            poll.setType(type.trim());

            poll.setDescription(
                    description == null
                            ? ""
                            : description.trim()
            );

            poll.setCreatedDate(
                    LocalDate.now().toString()
            );

            poll.setEndDate(endDate.trim());

            poll.setTargetAudience(
                    targetAudience == null
                            ? "All Residents"
                            : targetAudience.trim()
            );

            poll.setStatus("ACTIVE");

            poll.setOptions(cleanOptions);

            poll.setVoteCounts(voteCounts);

            poll.setTotalVotes(0);

            // -------------------------------------------------
            // DAO
            // -------------------------------------------------

            return pollDao.addPoll(poll);

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET ALL
    // =========================================================

    public List<Poll> getAllPolls() {
        return pollDao.getAllPolls();
    }

    // =========================================================
    // GET ACTIVE
    // =========================================================

    public List<Poll> getActivePolls() {
        return pollDao.getActivePolls();
    }

    // =========================================================
    // GET SURVEYS
    // =========================================================

    public List<Poll> getSurveys() {
        return pollDao.getSurveys();
    }

    // =========================================================
    // GET HISTORY
    // =========================================================

    public List<Poll> getHistory() {
        return pollDao.getHistory();
    }

    // =========================================================
    // CLOSE
    // =========================================================

    public boolean closePoll(String pollId) {
        return pollDao.closePoll(pollId);
    }

    // =========================================================
    // DELETE
    // =========================================================

    public boolean deletePoll(String pollId) {
        return pollDao.deletePoll(pollId);
    }

    // =========================================================
    // VOTE
    // =========================================================

    public boolean addVote(
            String pollId,
            int optionIndex) {

        return pollDao.addVote(
                pollId,
                optionIndex
        );
    }
}