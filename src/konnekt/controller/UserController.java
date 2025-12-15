/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package konnekt.controller;

import konnekt.model.dao.UserDao;
import konnekt.model.pojo.UserPojo;
import konnekt.model.dao.OTPDao;
import konnekt.utils.Password;
import konnekt.manager.Session;
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

    public UserController() {
        this.userDao = new UserDao();
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

        // send mail
        String message = "An OTP was sent to your email: " + email + "\nPlease enter it below:";
        String otp = JOptionPane.showInputDialog(
                rv,
                message,
                "OTP Verification",
                JOptionPane.PLAIN_MESSAGE
        ).trim();

        if (otp == null) {
            // User pressed Cancel or closed dialog
        } else if (otp.isEmpty()) {
            JOptionPane.showMessageDialog(rv, "OTP cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            boolean valid = new OTPDao().validateOtp(email, otp, "REGISTER_ACCOUNT");

            if (valid) {
                JOptionPane.showMessageDialog(rv, "OTP verified successfully!");
                
                String hashedPassword = Password.hashPassword(password);

                UserPojo user = new UserPojo(0, fullName, username, email, hashedPassword);
                if (userDao.addUser(user)) {
                    JOptionPane.showMessageDialog(rv, "Account registered successfully!");
                } else {
                    JOptionPane.showMessageDialog(rv, "Some error occured while registering the account!", "Error", JOptionPane.ERROR_MESSAGE);
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

        String hashedPassword = Password.hashPassword(password);
        if (userDao.login(email, hashedPassword)) {
            Session.login(email);
            
            JOptionPane.showMessageDialog(lv, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
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
