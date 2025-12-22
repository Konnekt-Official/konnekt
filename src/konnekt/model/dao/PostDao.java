package konnekt.model.dao;

import konnekt.connection.DatabaseConnection;
import konnekt.model.pojo.PostPojo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

    public boolean addPost(PostPojo post) {
        String sql = "INSERT INTO post (user_id, content) VALUES (?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getContent());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PostPojo> getAllPosts() {
        List<PostPojo> posts = new ArrayList<>();
        String sql = "SELECT * FROM post ORDER BY created_at DESC";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PostPojo post = new PostPojo();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setContent(rs.getString("content"));
                post.setLikes(rs.getInt("likes"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public PostPojo getPostById(int postId) {
        String sql = "SELECT * FROM post WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PostPojo post = new PostPojo();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setContent(rs.getString("content"));
                post.setLikes(rs.getInt("likes"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                return post;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PostPojo> getPostsByUserId(int userId) {
        List<PostPojo> posts = new ArrayList<>();
        String sql = "SELECT * FROM post WHERE user_id = ? ORDER BY created_at DESC";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PostPojo post = new PostPojo();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setContent(rs.getString("content"));
                post.setLikes(rs.getInt("likes"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
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

    public boolean deletePost(int postId) {
        String sql = "DELETE FROM post WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, postId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
