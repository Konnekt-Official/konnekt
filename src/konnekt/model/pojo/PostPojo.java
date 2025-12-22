package konnekt.model.pojo;

import java.sql.Timestamp;

public class PostPojo {

    private int id;
    private int userId;
    private String content;
    private int likes;
    private Timestamp createdAt;

    public PostPojo() {}

    public PostPojo(int id, int userId, String content, int likes, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.likes = likes;
        this.createdAt = createdAt;
    }

    public int getId() { 
        return id; 
    }

    public void setId(int id) { 
        this.id = id; 
    }

    public int getUserId() { 
        return userId; 
    }

    public void setUserId(int userId) { 
        this.userId = userId; 
    }

    public String getContent() { 
        return content; 
    }

    public void setContent(String content) { 
        this.content = content; 
    }

    public int getLikes() { 
        return likes; 
    }

    public void setLikes(int likes) { 
        this.likes = likes; 
    }

    public Timestamp getCreatedAt() { 
        return createdAt; 
    }

    public void setCreatedAt(Timestamp createdAt) { 
        this.createdAt = createdAt; 
    }
}