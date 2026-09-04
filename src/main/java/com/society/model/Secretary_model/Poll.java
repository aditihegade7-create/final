package com.society.model.Secretary_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poll {

    // =========================================================
    // FIELDS
    // =========================================================

    private String pollId;
    private String question;
    private String type;
    private String description;
    private String createdDate;
    private String endDate;
    private String targetAudience;
    private String status;
    private String createdByEmail;

    private List<String> options;
    private List<Long> voteCounts;

    private long totalVotes;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public Poll() {

        options = new ArrayList<>();
        voteCounts = new ArrayList<>();
        totalVotes = 0;
    }

    // =========================================================
    // PARAMETERIZED CONSTRUCTOR
    // =========================================================

    public Poll(
            String pollId,
            String question,
            String type,
            String description,
            String createdDate,
            String endDate,
            String targetAudience,
            String status,
            String createdByEmail,
            List<String> options,
            List<Long> voteCounts,
            long totalVotes) {

        this.pollId = pollId;
        this.question = question;
        this.type = type;
        this.description = description;
        this.createdDate = createdDate;
        this.endDate = endDate;
        this.targetAudience = targetAudience;
        this.status = status;
        this.createdByEmail = createdByEmail;

        this.options =
                options == null
                        ? new ArrayList<>()
                        : new ArrayList<>(options);

        this.voteCounts =
                voteCounts == null
                        ? new ArrayList<>()
                        : new ArrayList<>(voteCounts);

        this.totalVotes = totalVotes;
    }

    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public void setCreatedByEmail(String createdByEmail) {
        this.createdByEmail = createdByEmail;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {

        this.options =
                options == null
                        ? new ArrayList<>()
                        : new ArrayList<>(options);
    }

    public List<Long> getVoteCounts() {
        return voteCounts;
    }

    public void setVoteCounts(List<Long> voteCounts) {

        this.voteCounts =
                voteCounts == null
                        ? new ArrayList<>()
                        : new ArrayList<>(voteCounts);
    }

    public long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(long totalVotes) {
        this.totalVotes = totalVotes;
    }

    // =========================================================
    // FIRESTORE MAP
    // =========================================================

    public Map<String, Object> toMap() {

        Map<String, Object> map =
                new HashMap<>();

        map.put("pollId", pollId);
        map.put("question", question);
        map.put("type", type);
        map.put("description", description);
        map.put("createdDate", createdDate);
        map.put("endDate", endDate);
        map.put("targetAudience", targetAudience);
        map.put("status", status);
        map.put("createdByEmail", createdByEmail);

        map.put(
                "options",
                options == null
                        ? new ArrayList<>()
                        : options
        );

        map.put(
                "voteCounts",
                voteCounts == null
                        ? new ArrayList<>()
                        : voteCounts
        );

        map.put(
                "totalVotes",
                totalVotes
        );

        return map;
    }

    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "Poll{" +
                "pollId='" + pollId + '\'' +
                ", question='" + question + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", targetAudience='" + targetAudience + '\'' +
                ", status='" + status + '\'' +
                ", createdByEmail='" + createdByEmail + '\'' +
                ", options=" + options +
                ", voteCounts=" + voteCounts +
                ", totalVotes=" + totalVotes +
                '}';
    }
}