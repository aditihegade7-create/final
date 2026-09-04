package com.society.model.Resident_model;

import java.util.ArrayList;
import java.util.List;

public class CommunityGroupModel {

    private String groupId;
    private String groupName;
    private String groupType;
    private String description;

    private String createdByEmail;
    private String createdByName;
    private String createdByFlat;

    private long memberCount;
    private String createdAt;

    private boolean joined;

    private List<Member> members;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public CommunityGroupModel() {
        this.members = new ArrayList<>();
        this.memberCount = 0;
        this.joined = false;
    }

    // =========================================================
    // FULL CONSTRUCTOR
    // =========================================================

    public CommunityGroupModel(
            String groupId,
            String groupName,
            String groupType,
            String description,
            String createdByEmail,
            String createdByName,
            String createdByFlat,
            long memberCount,
            String createdAt
    ) {

        this.groupId = groupId;
        this.groupName = groupName;
        this.groupType = groupType;
        this.description = description;

        this.createdByEmail = createdByEmail;
        this.createdByName = createdByName;
        this.createdByFlat = createdByFlat;

        this.memberCount = memberCount;
        this.createdAt = createdAt;

        this.joined = false;
        this.members = new ArrayList<>();
    }

    // =========================================================
    // GETTERS / SETTERS
    // =========================================================

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public void setCreatedByEmail(String createdByEmail) {
        this.createdByEmail = createdByEmail;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedByFlat() {
        return createdByFlat;
    }

    public void setCreatedByFlat(String createdByFlat) {
        this.createdByFlat = createdByFlat;
    }

    public long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(long memberCount) {
        this.memberCount = memberCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public List<Member> getMembers() {

        if (members == null) {
            members = new ArrayList<>();
        }

        return members;
    }

    public void setMembers(List<Member> members) {

        if (members == null) {
            this.members = new ArrayList<>();
        } else {
            this.members = members;
        }
    }

    public void addMember(Member member) {

        if (members == null) {
            members = new ArrayList<>();
        }

        if (member != null) {
            members.add(member);
        }
    }

    public void removeMember(Member member) {

        if (members != null) {
            members.remove(member);
        }
    }

    // =========================================================
    // MEMBER MODEL
    // =========================================================

    public static class Member {

        private String email;
        private String name;
        private String flatNo;
        private String role;
        private String joinedAt;

        public Member() {
        }

        public Member(
                String email,
                String name,
                String flatNo,
                String role,
                String joinedAt
        ) {

            this.email = email;
            this.name = name;
            this.flatNo = flatNo;
            this.role = role;
            this.joinedAt = joinedAt;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFlatNo() {
            return flatNo;
        }

        public void setFlatNo(String flatNo) {
            this.flatNo = flatNo;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getJoinedAt() {
            return joinedAt;
        }

        public void setJoinedAt(String joinedAt) {
            this.joinedAt = joinedAt;
        }
    }
}
