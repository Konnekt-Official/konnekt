package konnekt.model.dao;

import konnekt.connection.DatabaseConnection;
import konnekt.model.pojo.PostPojo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PostDao {

    // ---------- GET CONNECTION ----------
    public Connection getConnection() throws Exception {
        return DatabaseConnection.getConnection();
    }

    // ---------- ADD POST ----------
    public boolean addPost(PostPojo post) {
        String sql = "INSERT INTO post (user_id, content, likes) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getContent());
            ps.setInt(3, post.getLikes());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- GET ALL POSTS ----------
    public List<PostPojo> getAllPosts() {
        List<PostPojo> posts = new ArrayList<>();
        String sql = """
                SELECT p.id, p.user_id, p.content, p.likes, p.created_at,
                       u.full_name, u.username,
                       (SELECT COUNT(*) FROM comment c WHERE c.post_id = p.id) AS comment_count
                FROM post p
                JOIN user u ON p.user_id = u.id
                ORDER BY p.created_at DESC
                """;
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PostPojo post = new PostPojo();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setContent(rs.getString("content"));
                post.setLikes(rs.getInt("likes"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setFullName(rs.getString("full_name"));
                post.setUsername(rs.getString("username"));
                post.setCommentCount(rs.getInt("comment_count"));
                posts.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    // ---------- GET POST BY ID ----------
    public PostPojo getPostById(int postId) {
        String sql = """
                SELECT p.id, p.user_id, p.content, p.likes, p.created_at,
                       u.full_name, u.username,
                       (SELECT COUNT(*) FROM comment c WHERE c.post_id = p.id) AS comment_count
                FROM post p
                JOIN user u ON p.user_id = u.id
                WHERE p.id = ?
                """;
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PostPojo post = new PostPojo();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setContent(rs.getString("content"));
                post.setLikes(rs.getInt("likes"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setFullName(rs.getString("full_name"));
                post.setUsername(rs.getString("username"));
                post.setCommentCount(rs.getInt("comment_count"));
                return post;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------- GET POSTS BY USER ----------
    public List<PostPojo> getPostsByUser(int userId) {
        List<PostPojo> posts = new ArrayList<>();
        String sql = """
                SELECT p.id, p.user_id, p.content, p.likes, p.created_at,
                       u.full_name, u.username,
                       (SELECT COUNT(*) FROM comment c WHERE c.post_id = p.id) AS comment_count
                FROM post p
                JOIN user u ON p.user_id = u.id
                WHERE p.user_id = ?
                ORDER BY p.created_at DESC
                """;
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PostPojo post = new PostPojo();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setContent(rs.getString("content"));
                post.setLikes(rs.getInt("likes"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setFullName(rs.getString("full_name"));
                post.setUsername(rs.getString("username"));
                post.setCommentCount(rs.getInt("comment_count"));
                posts.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    // ---------- SEARCH POSTS ----------
    public List<PostPojo> searchPosts(String keyword) {
        List<PostPojo> posts = new ArrayList<>();
        String sql = """
                SELECT p.id, p.user_id, p.content, p.likes, p.created_at,
                       u.full_name, u.username,
                       (SELECT COUNT(*) FROM comment c WHERE c.post_id = p.id) AS comment_count
                FROM post p
                JOIN user u ON p.user_id = u.id
                WHERE p.content LIKE ?
                ORDER BY p.created_at DESC
                """;
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PostPojo post = new PostPojo();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setContent(rs.getString("content"));
                post.setLikes(rs.getInt("likes"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setFullName(rs.getString("full_name"));
                post.setUsername(rs.getString("username"));
                post.setCommentCount(rs.getInt("comment_count"));
                posts.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    // ---------- DELETE POST ----------
    public boolean deletePost(int postId) {
        String sql = "DELETE FROM post WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void incrementLike(int postId) {
        String sql = "UPDATE post SET likes = likes + 1 WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- COUNT POSTS ----------
    public int countPosts() {
        String sql = "SELECT COUNT(*) FROM post";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<String, Integer> getPostCount() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT DATE(created_at) d, COUNT(*) c FROM post GROUP BY d";

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("d"), rs.getInt("c"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, Integer> getPostCountBetween(LocalDate start, LocalDate end) {
        Map<String, Integer> map = new LinkedHashMap<>();

        String sql = """
        SELECT DATE(created_at) d, COUNT(*) c
        FROM post
        WHERE DATE(created_at) BETWEEN ? AND ?
        GROUP BY DATE(created_at)
        ORDER BY d
    """;

        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(start));
            ps.setDate(2, java.sql.Date.valueOf(end));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("d"), rs.getInt("c"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
