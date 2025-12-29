package konnekt.model.pojo;

import java.sql.Timestamp;

public class FollowPojo {
    private int id;
    private int followerId;
    private int followingId;
    private Timestamp createdAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFollowerId() { return followerId; }
    public void setFollowerId(int followerId) { this.followerId = followerId; }

    public int getFollowingId() { return followingId; }
    public void setFollowingId(int followingId) { this.followingId = followingId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
