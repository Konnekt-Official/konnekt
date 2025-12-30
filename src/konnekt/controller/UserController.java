package konnekt.controller;

import konnekt.model.dao.UserDao;
import konnekt.model.pojo.UserPojo;
import konnekt.model.dao.OTPDao;
import konnekt.model.pojo.OTPPojo;
import konnekt.utils.PasswordUtils;
import konnekt.manager.SessionManager;
import konnekt.service.EmailService;
import konnekt.utils.OTPUtils;
import konnekt.view.RegisterView;
import konnekt.view.LoginView;
import konnekt.view.NavigatorView;

import javax.swing.JOptionPane;

public class UserController {

    private final UserDao userDao;
    private final OTPDao otpDao;

    public UserController() {
        this.userDao = new UserDao();
        this.otpDao = new OTPDao();
    }

    public void registerUser(RegisterView rv, String fullName, String username, String email, String password) {
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(rv, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userDao.existsByUsername(username)) {
            JOptionPane.showMessageDialog(rv, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(rv, "Invalid email!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userDao.existsByEmail(email)) {
            JOptionPane.showMessageDialog(rv, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generate OTP
        String otp = OTPUtils.generateOTP();
        OTPPojo otpPojo = new OTPPojo();
        otpPojo.setOtp(otp);
        otpPojo.setType("REGISTER_ACCOUNT");
        otpPojo.setEmail(email);
        otpDao.insertOtp(otpPojo);

        String body = "<html><body style='font-family: Arial, sans-serif; line-height:1.6;'>"
                + "<h2 style='color:#333;'>Hello, <b>" + fullName + "</b>!</h2>"
                + "<p>We received a request to register your account.</p>"
                + "<p>Your One-Time Password (OTP) is:</p>"
                + "<h2 style='color:#01FE49;'>" + otp + "</h2>"
                + "<p>This OTP is valid for the next <b>5 minutes</b> and can only be used once. For your security, please do not share this code with anyone.</p>"
                + "<p>If you did not request this, please ignore this email or contact support immediately.</p>"
                + "<h2><b>Best regards,</b><br>Bokshi</h2>"
                + "<hr style='border:none; border-top:1px solid #ccc;'/>"
                + "<p style='font-size:12px; color:#888;'>This is an automated message, please do not reply.</p>"
                + "<p style='font-size:12px; color:#888;'>© 2025 Konnekt. All rights reserved.</p>"
                + "</body></html>";

        new EmailService().sendEmail(email, "Account Registration OTP", body);

        String message = "Please enter the OTP sent to your email (" + email + ")" ;
        while (true) {
            String inputOTP = JOptionPane.showInputDialog(rv, message, "OTP Verification", JOptionPane.PLAIN_MESSAGE);
            if (inputOTP == null) {
                return;
            }
            inputOTP = inputOTP.trim();
            if (inputOTP.isEmpty()) {
                JOptionPane.showMessageDialog(rv, "OTP cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (otpDao.validateOtp(email, inputOTP, "REGISTER_ACCOUNT")) {
                String hashedPassword = PasswordUtils.hashPassword(password);

                // Create user
                UserPojo user = new UserPojo();
                user.setFullName(fullName);
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(hashedPassword);

                if (userDao.addUser(user)) {
                    JOptionPane.showMessageDialog(rv, "Account registered successfully!");
                    rv.dispose();
                    new LoginView().setVisible(true);
                    return;
                } else {
                    JOptionPane.showMessageDialog(rv, "Failed to register account!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(rv, "Invalid OTP!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void loginUser(LoginView lv, String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(lv, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);
        if (userDao.login(email, hashedPassword)) {
            SessionManager.login(email);
            lv.dispose();
            new NavigatorView().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(lv, "Login failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
