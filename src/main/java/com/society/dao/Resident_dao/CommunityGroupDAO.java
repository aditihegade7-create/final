package com.society.dao.Resident_dao;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.config.FirebaseConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityGroupDAO {

    private static final String COMMUNITY_COLLECTION = "CommunityGroups";
    private static final String RESIDENT_COLLECTION = "Residents";

    private final Firestore db;

    public CommunityGroupDAO() {
        db = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // GROUP RECORD
    // =========================================================

    public static class GroupRecord {
        public String groupId;
        public String groupName;
        public String groupType;
        public int memberCount;
        public String description;
        public String createdByEmail;
        public String createdByName;
        public String createdByFlat;
        public boolean joined;

        public GroupRecord() {
        }

        public GroupRecord(
                String groupId,
                String groupName,
                String groupType,
                int memberCount,
                String description,
                String createdByEmail,
                String createdByName,
                String createdByFlat,
                boolean joined) {

            this.groupId = groupId;
            this.groupName = groupName;
            this.groupType = groupType;
            this.memberCount = memberCount;
            this.description = description;
            this.createdByEmail = createdByEmail;
            this.createdByName = createdByName;
            this.createdByFlat = createdByFlat;
            this.joined = joined;
        }
    }

    // =========================================================
    // MEMBER RECORD
    // =========================================================

    public static class MemberRecord {
        public String email;
        public String name;
        public String flatNo;
        public String role;

        public MemberRecord() {
        }

        public MemberRecord(
                String email,
                String name,
                String flatNo,
                String role) {

            this.email = email;
            this.name = name;
            this.flatNo = flatNo;
            this.role = role;
        }
    }

    // =========================================================
    // MESSAGE RECORD
    // =========================================================

    public static class MessageRecord {
        public String messageId;
        public String messageType;
        public String senderEmail;
        public String senderName;
        public String groupName;
        public String message;
        public String imageBase64;
        public String imageName;
        public Timestamp createdAt;

        public MessageRecord() {
        }

        public MessageRecord(
                String messageId,
                String messageType,
                String senderEmail,
                String senderName,
                String groupName,
                String message,
                String imageBase64,
                String imageName,
                Timestamp createdAt) {

            this.messageId = messageId;
            this.messageType = messageType;
            this.senderEmail = senderEmail;
            this.senderName = senderName;
            this.groupName = groupName;
            this.message = message;
            this.imageBase64 = imageBase64;
            this.imageName = imageName;
            this.createdAt = createdAt;
        }
    }

    // =========================================================
    // NORMALIZE EMAIL
    // =========================================================

    private String normalizeEmail(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Resident login email is required.");
        }
        return email.trim().toLowerCase();
    }

    // =========================================================
    // GET SOCIETY FROM RESIDENT LOGIN EMAIL
    //
    // Residents/{email} OR Residents document having email field
    // Field used here: society
    // =========================================================

    private String getSociety(String residentEmail) throws Exception {
        String email = normalizeEmail(residentEmail);

        // First try Residents/{email}
        DocumentSnapshot direct = db
                .collection(RESIDENT_COLLECTION)
                .document(email)
                .get()
                .get();

        if (direct.exists()) {
            String society = direct.getString("society");
            if (society != null && !society.trim().isEmpty()) {
                return society.trim();
            }
        }

        // If email is a field instead of document ID
        QuerySnapshot result = db
                .collection(RESIDENT_COLLECTION)
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .get();

        if (result.isEmpty()) {
            throw new Exception(
                    "Resident not found for login email: " + email);
        }

        DocumentSnapshot resident = result.getDocuments().get(0);
        String society = resident.getString("society");

        if (society == null || society.trim().isEmpty()) {
            throw new Exception(
                    "Society not found for resident: " + email);
        }

        return society.trim();
    }

    // =========================================================
    // GET RESIDENT NAME / FLAT FROM RESIDENTS
    // =========================================================

    private DocumentSnapshot getResidentDocument(String residentEmail) throws Exception {
        String email = normalizeEmail(residentEmail);

        // 1. Try document ID = login email.
        DocumentSnapshot direct = db
                .collection(RESIDENT_COLLECTION)
                .document(email)
                .get()
                .get();

        if (direct.exists()) {
            String directStoredEmail = direct.getString("email");
            String directName = direct.getString("name");
            if ((directStoredEmail != null
                    && directStoredEmail.trim().equalsIgnoreCase(email))
                    || (directName != null && !directName.trim().isEmpty())) {
                return direct;
            }
        }

        // 2. Try exact email field.
        QuerySnapshot result = db
                .collection(RESIDENT_COLLECTION)
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .get();

        if (!result.isEmpty()) {
            return result.getDocuments().get(0);
        }

        // 3. Final fallback: case-insensitive email comparison.
        QuerySnapshot allResidents = db
                .collection(RESIDENT_COLLECTION)
                .get()
                .get();

        for (DocumentSnapshot resident : allResidents.getDocuments()) {
            String storedEmail = resident.getString("email");
            if (storedEmail != null
                    && storedEmail.trim().equalsIgnoreCase(email)) {
                return resident;
            }
        }

        if (direct.exists()) {
            return direct;
        }

        throw new Exception(
                "Resident not found for login email: " + email);
    }

    private String getResidentName(String residentEmail) throws Exception {
        DocumentSnapshot resident = getResidentDocument(residentEmail);
        String name = resident.getString("name");

        if (name == null || name.trim().isEmpty()) {
            throw new Exception(
                    "Resident name not found for login email: " + residentEmail);
        }

        return name.trim();
    }

    private String getResidentFlat(String residentEmail) throws Exception {
        DocumentSnapshot resident = getResidentDocument(residentEmail);
        String flat = resident.getString("flatNo");
        return flat == null ? "" : flat.trim();
    }

    // =========================================================
    // CREATE GROUP
    // =========================================================

    public String createGroup(
            String residentEmail,
            String groupName,
            String groupType,
            String description,
            String residentName,
            String residentFlat) throws Exception {

        String email = normalizeEmail(residentEmail);

        if (groupName == null || groupName.trim().isEmpty()) {
            throw new Exception("Group name is required.");
        }

        String society = getSociety(email);
        String actualResidentName = getResidentName(email);
        String actualResidentFlat = getResidentFlat(email);

        CollectionReference groups = db
                .collection(COMMUNITY_COLLECTION)
                .document(society)
                .collection("groups");

        DocumentReference groupRef = groups.document();

        Map<String, Object> groupData = new HashMap<>();

        groupData.put("groupName", groupName.trim());
        groupData.put(
                "groupType",
                groupType == null || groupType.trim().isEmpty()
                        ? "Community Group"
                        : groupType.trim());
        groupData.put(
                "description",
                description == null ? "" : description.trim());

        // Exact field requested by the UI / existing Firestore data.
        groupData.put("createdByEmail", email);
        groupData.put(
                "createdByName",
                actualResidentName);
        groupData.put(
                "createdByFlat",
                actualResidentFlat);

        // Keep society on the group document too.
        groupData.put("societyName", society);

        groupData.put("createdAt", FieldValue.serverTimestamp());

        groupRef.set(groupData).get();

        // Creator is automatically a member.
        Map<String, Object> memberData = new HashMap<>();
        memberData.put("email", email);
        memberData.put(
                "name",
                actualResidentName);
        memberData.put(
                "flatNo",
                actualResidentFlat);
        memberData.put("role", "Admin");
        memberData.put("joinedAt", FieldValue.serverTimestamp());

        groupRef
                .collection("members")
                .document(email)
                .set(memberData)
                .get();

        return groupRef.getId();
    }

    // =========================================================
    // GET GROUPS FOR CURRENT RESIDENT
    //
    // Only CommunityGroups/{current resident society}/groups
    // is queried.
    // =========================================================

    public List<GroupRecord> getAllGroups(String residentEmail)
            throws Exception {

        String email = normalizeEmail(residentEmail);
        String society = getSociety(email);

        QuerySnapshot snapshot = db
                .collection(COMMUNITY_COLLECTION)
                .document(society)
                .collection("groups")
                .get()
                .get();

        List<GroupRecord> groups = new ArrayList<>();

        for (DocumentSnapshot document : snapshot.getDocuments()) {
            String savedSociety = document.getString("societyName");

            if (savedSociety != null
                    && !savedSociety.trim().isEmpty()
                    && !savedSociety.trim().equals(society)) {
                continue;
            }

            String groupId = document.getId();
            String groupName = document.getString("groupName");
            String groupType = document.getString("groupType");
            String description = document.getString("description");
            String createdByEmail = document.getString("createdByEmail");
            String createdByName = document.getString("createdByName");
            String createdByFlat = document.getString("createdByFlat");

            QuerySnapshot members = document
                    .getReference()
                    .collection("members")
                    .get()
                    .get();

            boolean joined = document
                    .getReference()
                    .collection("members")
                    .document(email)
                    .get()
                    .get()
                    .exists();

            groups.add(new GroupRecord(
                    groupId,
                    groupName,
                    groupType,
                    members.size(),
                    description,
                    createdByEmail,
                    createdByName,
                    createdByFlat,
                    joined));
        }

        return groups;
    }

    // =========================================================
    // JOIN GROUP
    // =========================================================

    public boolean joinGroup(
            String groupId,
            String createdByEmail,
            String residentEmail,
            String residentName,
            String residentFlat) throws Exception {

        if (groupId == null || groupId.trim().isEmpty()) {
            throw new Exception("Group ID is required.");
        }

        String email = normalizeEmail(residentEmail);
        String society = getSociety(email);
        String actualResidentName = getResidentName(email);
        String actualResidentFlat = getResidentFlat(email);

        // IMPORTANT:
        // The group is always resolved inside the current resident's
        // society. createdByEmail is not used to cross societies.
        DocumentReference groupRef = db
                .collection(COMMUNITY_COLLECTION)
                .document(society)
                .collection("groups")
                .document(groupId);

        DocumentSnapshot group = groupRef.get().get();

        if (!group.exists()) {
            throw new Exception(
                    "Community group not found in your society.");
        }

        String savedSociety = group.getString("societyName");
        if (savedSociety != null
                && !savedSociety.trim().isEmpty()
                && !savedSociety.trim().equals(society)) {
            throw new Exception(
                    "You cannot join a group from another society.");
        }

        DocumentReference memberRef = groupRef
                .collection("members")
                .document(email);

        if (memberRef.get().get().exists()) {
            return false;
        }

        Map<String, Object> memberData = new HashMap<>();
        memberData.put("email", email);
        memberData.put(
                "name",
                actualResidentName);
        memberData.put(
                "flatNo",
                actualResidentFlat);
        memberData.put("role", "Member");
        memberData.put("joinedAt", FieldValue.serverTimestamp());

        memberRef.set(memberData).get();

        return true;
    }

    // =========================================================
    // GET MEMBERS
    // =========================================================

    public List<MemberRecord> getMembers(
            String groupId,
            String createdByEmail) throws Exception {

        if (groupId == null || groupId.trim().isEmpty()) {
            throw new Exception("Group ID is required.");
        }

        String society = getSociety(createdByEmail);

        QuerySnapshot snapshot = db
                .collection(COMMUNITY_COLLECTION)
                .document(society)
                .collection("groups")
                .document(groupId)
                .collection("members")
                .get()
                .get();

        List<MemberRecord> members = new ArrayList<>();

        for (DocumentSnapshot document : snapshot.getDocuments()) {
            String memberEmail = document.getString("email");
            if (memberEmail == null || memberEmail.trim().isEmpty()) {
                memberEmail = document.getId();
            }

            String memberName = document.getString("name");
            String memberFlat = document.getString("flatNo");

            try {
                String residentName = getResidentName(memberEmail);
                String residentFlat = getResidentFlat(memberEmail);

                if (memberName == null || memberName.trim().isEmpty()
                        || !memberName.trim().equals(residentName)) {
                    memberName = residentName;
                    Map<String, Object> update = new HashMap<>();
                    update.put("name", residentName);
                    update.put("flatNo", residentFlat);
                    update.put("email", normalizeEmail(memberEmail));
                    document.getReference().update(update).get();
                }

                if (memberFlat == null || memberFlat.trim().isEmpty()) {
                    memberFlat = residentFlat;
                }
            } catch (Exception ignored) {
                // Keep stored member data if resident lookup is unavailable.
            }

            members.add(new MemberRecord(
                    memberEmail,
                    memberName,
                    memberFlat,
                    document.getString("role")));
        }

        return members;
    }

    // =========================================================
    // CHECK MEMBER
    // =========================================================

    public boolean isMember(
            String groupId,
            String createdByEmail,
            String residentEmail) throws Exception {

        String email = normalizeEmail(residentEmail);
        String society = getSociety(email);

        return db
                .collection(COMMUNITY_COLLECTION)
                .document(society)
                .collection("groups")
                .document(groupId)
                .collection("members")
                .document(email)
                .get()
                .get()
                .exists();
    }

    // =========================================================
    // SAVE TEXT MESSAGE
    // =========================================================

    public String saveTextMessage(
            String groupId,
            String residentEmail,
            String senderName,
            String message) throws Exception {

        String email = normalizeEmail(residentEmail);

        if (groupId == null || groupId.trim().isEmpty()) {
            throw new Exception("Group ID is required.");
        }

        if (message == null || message.trim().isEmpty()) {
            throw new Exception("Message cannot be empty.");
        }

        String society = getSociety(email);
        DocumentReference groupRef = getGroupForCurrentSociety(
                society,
                groupId);

        ensureMember(groupRef, email);
        String actualSenderName = getResidentName(email);
        DocumentReference messageRef = groupRef
                .collection("messages")
                .document();

        DocumentSnapshot groupSnapshot = groupRef.get().get();
        String actualGroupName = groupSnapshot.getString("groupName");

        Map<String, Object> data = new HashMap<>();
        data.put("messageType", "TEXT");
        data.put("senderEmail", email);
        data.put("senderName", actualSenderName);
        data.put("groupName", actualGroupName == null ? "" : actualGroupName);
        data.put("message", message.trim());
        data.put("imageBase64", "");
        data.put("imageName", "");
        data.put("createdAt", FieldValue.serverTimestamp());

        messageRef.set(data).get();

        return messageRef.getId();
    }

    // =========================================================
    // SAVE IMAGE MESSAGE
    // =========================================================

    public String saveImageMessage(
            String groupId,
            String residentEmail,
            String senderName,
            String imageBase64,
            String imageName) throws Exception {

        String email = normalizeEmail(residentEmail);

        if (groupId == null || groupId.trim().isEmpty()) {
            throw new Exception("Group ID is required.");
        }

        if (imageBase64 == null || imageBase64.trim().isEmpty()) {
            throw new Exception("Image data is empty.");
        }

        String society = getSociety(email);
        DocumentReference groupRef = getGroupForCurrentSociety(
                society,
                groupId);

        ensureMember(groupRef, email);
        String actualSenderName = getResidentName(email);
        DocumentReference messageRef = groupRef
                .collection("messages")
                .document();

        DocumentSnapshot groupSnapshot = groupRef.get().get();
        String actualGroupName = groupSnapshot.getString("groupName");

        Map<String, Object> data = new HashMap<>();
        data.put("messageType", "IMAGE");
        data.put("senderEmail", email);
        data.put("senderName", actualSenderName);
        data.put("groupName", actualGroupName == null ? "" : actualGroupName);
        data.put("message", "");
        data.put("imageBase64", imageBase64);
        data.put(
                "imageName",
                imageName == null ? "image.jpg" : imageName.trim());
        data.put("createdAt", FieldValue.serverTimestamp());

        messageRef.set(data).get();

        return messageRef.getId();
    }

    // =========================================================
    // GET CHAT MESSAGES
    // =========================================================

    public List<MessageRecord> getMessages(
            String groupId,
            String residentEmail) throws Exception {

        String email = normalizeEmail(residentEmail);

        if (groupId == null || groupId.trim().isEmpty()) {
            throw new Exception("Group ID is required.");
        }

        String society = getSociety(email);
        DocumentReference groupRef = getGroupForCurrentSociety(
                society,
                groupId);

        // Only members of this group can read its messages.
        ensureMember(groupRef, email);

        DocumentSnapshot groupSnapshot = groupRef.get().get();
        String groupName = groupSnapshot.getString("groupName");

        QuerySnapshot snapshot = groupRef
                .collection("messages")
                .get()
                .get();

        List<MessageRecord> messages = new ArrayList<>();

        for (DocumentSnapshot document : snapshot.getDocuments()) {
            String senderEmail = document.getString("senderEmail");
            String senderName = document.getString("senderName");

            // ALWAYS resolve the sender name from Residents using senderEmail.
            // This also repairs old message documents whose senderName is blank.
            if (senderEmail != null && !senderEmail.trim().isEmpty()) {
                try {
                    senderName = getResidentName(senderEmail);

                    if (senderName != null && !senderName.trim().isEmpty()
                            && (document.getString("senderName") == null
                            || document.getString("senderName").trim().isEmpty())) {
                        Map<String, Object> repair = new HashMap<>();
                        repair.put("senderName", senderName);
                        document.getReference().update(repair).get();
                    }
                } catch (Exception ignored) {
                    // Try the group member record if Residents lookup fails.
                    try {
                        DocumentSnapshot member = groupRef
                                .collection("members")
                                .document(senderEmail.trim().toLowerCase())
                                .get()
                                .get();
                        String memberName = member.getString("name");
                        if (memberName != null && !memberName.trim().isEmpty()) {
                            senderName = memberName.trim();
                        }
                    } catch (Exception ignoredMember) {
                        // Keep stored senderName.
                    }
                }
            }

            if (senderName == null || senderName.trim().isEmpty()) {
                senderName = "Resident";
            }

            String savedGroupName = document.getString("groupName");
            if (savedGroupName == null || savedGroupName.trim().isEmpty()) {
                savedGroupName = groupName == null ? "" : groupName;
            }

            Object messageObject = document.getData().get("message");
            String actualMessage = messageObject == null
                    ? ""
                    : String.valueOf(messageObject);

            String messageType = document.getString("messageType");
            if (messageType == null || messageType.trim().isEmpty()) {
                messageType = "TEXT";
            }

            messages.add(new MessageRecord(
                    document.getId(),
                    messageType,
                    senderEmail,
                    senderName,
                    savedGroupName,
                    actualMessage,
                    document.getString("imageBase64"),
                    document.getString("imageName"),
                    document.getTimestamp("createdAt")));
        }

        messages.sort(new Comparator<MessageRecord>() {
            @Override
            public int compare(MessageRecord a, MessageRecord b) {
                if (a.createdAt == null && b.createdAt == null) {
                    return 0;
                }
                if (a.createdAt == null) {
                    return 1;
                }
                if (b.createdAt == null) {
                    return -1;
                }
                return a.createdAt.compareTo(b.createdAt);
            }
        });

        return messages;
    }

    // =========================================================
    // INTERNAL GROUP VALIDATION
    // =========================================================

    private DocumentReference getGroupForCurrentSociety(
            String society,
            String groupId) throws Exception {

        DocumentReference groupRef = db
                .collection(COMMUNITY_COLLECTION)
                .document(society)
                .collection("groups")
                .document(groupId);

        if (!groupRef.get().get().exists()) {
            throw new Exception(
                    "Community group not found in your society.");
        }

        return groupRef;
    }

    private void ensureMember(
            DocumentReference groupRef,
            String email) throws Exception {

        boolean member = groupRef
                .collection("members")
                .document(email)
                .get()
                .get()
                .exists();

        if (!member) {
            QuerySnapshot members = groupRef
                    .collection("members")
                    .get()
                    .get();

            for (DocumentSnapshot memberDoc : members.getDocuments()) {
                String memberEmail = memberDoc.getString("email");
                if (memberEmail != null
                        && memberEmail.trim().equalsIgnoreCase(email)) {
                    member = true;
                    break;
                }
            }
        }

        if (!member) {
            throw new Exception(
                    "You must join this community group first.");
        }
    }
}
