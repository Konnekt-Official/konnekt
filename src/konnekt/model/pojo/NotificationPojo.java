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

    // getters & setters
}