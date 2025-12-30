package konnekt.model.dao;

import konnekt.connection.DatabaseConnection;
import konnekt.model.pojo.OTPPojo;

import java.sql.*;

public class OTPDao {

    public boolean insertOtp(OTPPojo otp) {
        String sql = "INSERT INTO otp (otp, type, email) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, otp.getOtp());
            ps.setString(2, otp.getType());
            ps.setString(3, otp.getEmail());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateOtp(String email, String otpValue, String type) {
        String sql = "SELECT * FROM otp WHERE email=? AND otp=? AND type=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, otpValue);
            ps.setString(3, type);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp expiresAt = rs.getTimestamp("expires_at");
                return expiresAt != null && expiresAt.after(new Timestamp(System.currentTimeMillis()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
