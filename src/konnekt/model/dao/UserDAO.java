package konnekt.model.dao;

import konnekt.connection.DatabaseConnection;
import konnekt.model.pojo.UserPojo;

import java.sql.*;

public class UserDao {

    public boolean addUser(UserPojo user) {
        String sql = "INSERT INTO user(full_name, username, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            return ps.executeUpdate() > 0; // returns true if insert succeeds
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean login(String email, String password) {
        String sql = "SELECT * FROM user WHERE email=? AND password=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password); // hash in real projects
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if user exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean existsByUsername(String username) {
        String sql = "SELECT id FROM user WHERE username = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if a row exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT id FROM user WHERE email = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if a row exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}