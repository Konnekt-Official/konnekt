package konnekt.model.pojo;

import java.sql.Timestamp;
import konnekt.model.enums.OTPType;

public class OTPPojo {

    private int id;
    private String otp;
    private OTPType type;
    private String email;
    private Timestamp createdAt;
    private Timestamp expiresAt;

    public OTPPojo() {}

    public OTPPojo(int id, String otp, OTPType type, String email, Timestamp createdAt, Timestamp expiresAt) {
        this.id = id;
        this.otp = otp;
        this.type = type;
        this.email = email;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public OTPType getType() {
        return type;
    }

    public void setType(OTPType type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return new Timestamp(System.currentTimeMillis()).after(expiresAt);
    }

    @Override
    public String toString() {
        return "OTPPojo{" +
                "id=" + id +
                ", otp='" + otp + '\'' +
                ", type=" + type +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
