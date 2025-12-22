/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package konnekt.controller;

import konnekt.model.dao.UserDao;
import konnekt.model.pojo.UserPojo;
import konnekt.model.dao.OTPDao;
import konnekt.model.pojo.OTPPojo;
import konnekt.model.enums.OTPType;
import konnekt.utils.PasswordUtils;
import konnekt.manager.SessionManager;
import konnekt.service.EmailService;
import konnekt.utils.OTPUtils;
import konnekt.view.RegisterView;
import konnekt.view.LoginView;
import konnekt.view.FeedView;

import javax.swing.JOptionPane;

/**
 *
 * @author Hp
 */
public class UserController {

    private final UserDao userDao;
    private final OTPDao otpDao;

    public UserController() {
        this.userDao = new UserDao();
        this.otpDao = new OTPDao();
    }

    public void registerUser(RegisterView rv, String fullName, String username, String email, String password) {
        if (this.isEmptyField(fullName, username, email, password)) {
            JOptionPane.showMessageDialog(rv, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (this.usernameExists(username)) {
            JOptionPane.showMessageDialog(rv, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(rv, "Invalid email address!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (this.emailExists(email)) {
            JOptionPane.showMessageDialog(rv, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        

        String otp = OTPUtils.generateOTP();
        
        OTPPojo otpPojo = new OTPPojo();
        otpPojo.setOtp(otp);
        otpPojo.setType(OTPType.REGISTER_ACCOUNT);
        otpPojo.setEmail(email);
        otpDao.insertOtp(otpPojo);
                
        String body = "<html>"
                + "<body>"
                + "<p><b>Hello, </b>" + fullName + "</p>"
                + "<p>Your <b>One-Time Password (OTP)</b> for registering your account is:</p>"
                + "<h2 style='color:#01FE49;'>" + otp + "</h2>"
                + "<p>This OTP will expire in <b>5 minutes</b>.</p>"
                + "<p>If you did not request this, please ignore this email.</p>"
                + "<br>"
                + "<p><b>Best regards,</b><br>Konnekt Team</p>"
                + "</body>"
                + "</html>";
        new EmailService().sendEmail(email, "Account Register", body);

        String message = "An OTP was sent to your email: " + email + "\nPlease enter it below:";
        while (true) {
            String inputOTP = JOptionPane.showInputDialog(
                    rv,
                    message,
                    "OTP Verification",
                    JOptionPane.PLAIN_MESSAGE
            ).trim();

            if (inputOTP == null) {
                return;
            } else if (inputOTP.isEmpty()) {
                JOptionPane.showMessageDialog(rv, "OTP cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean valid = otpDao.validateOtp(email, inputOTP, "REGISTER_ACCOUNT");

                if (valid) {
                    // JOptionPane.showMessageDialog(rv, "OTP verified successfully!");

                    String hashedPassword = PasswordUtils.hashPassword(password);

                    UserPojo user = new UserPojo(0, fullName, username, email, hashedPassword);
                    if (userDao.addUser(user)) {
                        JOptionPane.showMessageDialog(rv, "Account registered successfully!");
                        
                        rv.dispose();
                        new LoginView().setVisible(true);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(rv, "Some error occured while registering the account!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(rv, "Invalid OTP!", "Error", JOptionPane.ERROR_MESSAGE);
                }
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

            // JOptionPane.showMessageDialog(lv, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

            lv.dispose();
            new FeedView().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(lv, "Login failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isEmptyField(String fullName, String username, String email, String password) {
        return fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty();
    }

    public boolean usernameExists(String username) {
        return userDao.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userDao.existsByEmail(email);
    }
}