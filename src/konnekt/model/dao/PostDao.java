package konnekt.model.dao;

import konnekt.model.pojo.PostPojo;
import konnekt.connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

    public boolean addPost(PostPojo post) {
        String sql = "INSERT INTO post (user_id, content, likes) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getContent());
            ps.setInt(3, post.getLikes());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<PostPojo> getAllPosts() {
        List<PostPojo> posts = new ArrayList<>();
        String sql = """
                    SELECT p.id, p.user_id, p.content, p.likes, p.created_at,
                    u.full_name, u.username,
                    COUNT(c.id) AS comment_count
                    FROM post p
                    JOIN user u ON p.user_id = u.id
                    LEFT JOIN comment c ON c.post_id = p.id
                    GROUP BY p.id, p.user_id, p.content, p.likes, p.created_at, u.full_name, u.username
                    ORDER BY p.created_at DESC
                    """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

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

    public PostPojo getPostById(int postId) {
        String sql = """
    SELECT p.id, p.user_id, p.content, p.likes, p.created_at,
           u.username, u.full_name,
           (SELECT COUNT(*) FROM comment c WHERE c.post_id = p.id) AS comment_count
    FROM post p
    JOIN user u ON p.user_id = u.id
    WHERE p.id = ?
    """;

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PostPojo p = new PostPojo();
                p.setId(rs.getInt("id"));
                p.setUserId(rs.getInt("user_id"));
                p.setContent(rs.getString("content"));
                p.setLikes(rs.getInt("likes"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setUsername(rs.getString("username"));
                p.setFullName(rs.getString("full_name"));
                p.setCommentCount(rs.getInt("comment_count"));
                return p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PostPojo> getPostsByUser(int userId) {
        List<PostPojo> posts = new ArrayList<>();
        String sql = "SELECT p.id, p.user_id, p.content, p.likes, p.created_at, "
                + "u.full_name, u.username "
                + "FROM post p "
                + "JOIN user u ON p.user_id = u.id "
                + "WHERE p.user_id = ? "
                + "ORDER BY p.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
                posts.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    public void likePost(int postId) {
        String sql = "UPDATE post SET likes = likes + 1 WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deletePost(int postId) {
        String sql = "DELETE FROM post WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<PostPojo> searchPosts(String keyword) {
        List<PostPojo> list = new ArrayList<>();
        String sql = """
        SELECT p.*,
               u.full_name,
               u.username,
               (SELECT COUNT(*) FROM post_like l WHERE l.post_id = p.id) AS like_count,
               (SELECT COUNT(*) FROM comment c WHERE c.post_id = p.id) AS comment_count
        FROM post p
        JOIN user u ON u.id = p.user_id
        WHERE p.content LIKE ?
        ORDER BY p.created_at DESC
    """;

        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PostPojo p = new PostPojo();
                p.setId(rs.getInt("id"));
                p.setUserId(rs.getInt("user_id"));
                p.setContent(rs.getString("content"));
                p.setFullName(rs.getString("full_name"));
                p.setUsername(rs.getString("username"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setLikes(rs.getInt("like_count"));
                p.setCommentCount(rs.getInt("comment_count"));

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
