package konnekt.model.dao;

import konnekt.connection.DatabaseConnection;
import konnekt.model.pojo.PostPojo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

    public boolean addPost(PostPojo post) {
        String sql = "INSERT INTO post(user_id, content, image_url) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getContent());
            ps.setString(3, post.getImageUrl());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PostPojo> getAllPosts() {
        String sql = "SELECT p.id, p.user_id, p.content, p.image_url, p.likes, p.created_at, " +
                     "u.username, u.profile_picture_url " +
                     "FROM post p JOIN user u ON p.user_id = u.id " +
                     "ORDER BY p.created_at DESC";
        List<PostPojo> posts = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PostPojo post = new PostPojo();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setContent(rs.getString("content"));
                post.setImageUrl(rs.getString("image_url"));
                post.setLikes(rs.getInt("likes"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setUsername(rs.getString("username"));
                post.setProfilePictureUrl(rs.getString("profile_picture_url"));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public boolean likePost(int postId) {
        String sql = "UPDATE post SET likes = likes + 1 WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, postId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
