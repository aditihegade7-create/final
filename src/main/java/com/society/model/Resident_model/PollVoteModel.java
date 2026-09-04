package com.society.model.Resident_model;

public class PollVoteModel {

    private String residentEmail;
    private String selectedOption;
    private String votedAt;

    public PollVoteModel() {
    }

    public PollVoteModel(
            String residentEmail,
            String selectedOption,
            String votedAt) {

        this.residentEmail = residentEmail;
        this.selectedOption = selectedOption;
        this.votedAt = votedAt;
    }

    public String getResidentEmail() {
        return residentEmail;
    }

    public void setResidentEmail(String residentEmail) {
        this.residentEmail = residentEmail;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getVotedAt() {
        return votedAt;
    }

    public void setVotedAt(String votedAt) {
        this.votedAt = votedAt;
    }
}