package konnekt.model.pojo;

import java.util.Date;

public class PostPojo {
    private int id;
    private int userId;
    private String username;
    private String profilePictureUrl;
    private String content;
    private String imageUrl;
    private int likes;
    private Date createdAt;

    public PostPojo() {}

    public PostPojo(int id, int userId, String username, String profilePictureUrl, String content, String imageUrl, int likes, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}