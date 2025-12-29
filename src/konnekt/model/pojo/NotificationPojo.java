package konnekt.model.pojo;

import java.sql.Timestamp;

public class NotificationPojo {

    private int id;
    private int userId;
    private Integer senderId;

    private String senderFullName;
    private String senderUsername;

    private String type; // LIKE, COMMENT, FOLLOW
    private Integer referenceId;

    private boolean isRead;
    private Timestamp createdAt;

    // -------- getters & setters --------

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Integer getSenderId() { return senderId; }
    public void setSenderId(Integer senderId) { this.senderId = senderId; }

    public String getSenderFullName() { return senderFullName; }
    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public String getSenderUsername() { return senderUsername; }
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getReferenceId() { return referenceId; }
    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
