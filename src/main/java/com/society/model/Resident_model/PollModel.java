package com.society.model.Resident_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollModel {

    private String id;
    private String pollId;

    private String createdByEmail;
    private String createdDate;

    private String question;
    private String description;

    private String endDate;
    private String type;
    private String targetAudience;
    private String status;

    private List<String> options;
    private Map<String, Long> voteCounts;

    private long totalVotes;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public PollModel() {

        options = new ArrayList<>();
        voteCounts = new HashMap<>();

        totalVotes = 0;
    }

    // =====================================================
    // ID
    // =====================================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    // =====================================================
    // CREATED BY
    // =====================================================

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public void setCreatedByEmail(String createdByEmail) {
        this.createdByEmail = createdByEmail;
    }

    // =====================================================
    // CREATED DATE
    // =====================================================

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    // =====================================================
    // QUESTION
    // =====================================================

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    // =====================================================
    // DESCRIPTION
    // =====================================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // =====================================================
    // END DATE
    // =====================================================

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    // =====================================================
    // TYPE
    // =====================================================

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // =====================================================
    // TARGET AUDIENCE
    // =====================================================

    public String getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    // =====================================================
    // STATUS
    // =====================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // =====================================================
    // OPTIONS
    // =====================================================

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {

        this.options =
                options != null
                        ? options
                        : new ArrayList<>();
    }

    // =====================================================
    // VOTE COUNTS
    // =====================================================

    public Map<String, Long> getVoteCounts() {
        return voteCounts;
    }

    public void setVoteCounts(Map<String, Long> voteCounts) {

        this.voteCounts =
                voteCounts != null
                        ? voteCounts
                        : new HashMap<>();
    }

    // =====================================================
    // TOTAL VOTES
    // =====================================================

    public long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(long totalVotes) {
        this.totalVotes = totalVotes;
    }
}