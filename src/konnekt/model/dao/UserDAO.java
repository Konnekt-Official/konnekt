package konnekt.model.dao;

import konnekt.connection.Database;
import konnekt.model.pojo.UserPojo;
import konnekt.utils.Password;

import java.sql.*;

public class UserDAO {

    public boolean addUser(UserPojo user) {
        String sql = "INSERT INTO user(full_name, username, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, Password.hashPassword(user.getPassword()));
            return ps.executeUpdate() > 0; // returns true if insert succeeds
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean login(String emailOrUsername, String password) {
        String sql = "SELECT * FROM users WHERE (email=? OR username=?) AND password=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, emailOrUsername);
            ps.setString(2, emailOrUsername);
            ps.setString(3, password); // hash in real projects
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if user exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean existsByUsername(String username) {
        String sql = "SELECT id FROM user WHERE username = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
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
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if a row exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}