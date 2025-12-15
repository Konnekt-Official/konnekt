/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package konnekt.controllers;

import konnekt.model.dao.UserDao;
import konnekt.model.pojo.UserPojo;
import konnekt.utils.Password;
import konnekt.view.RegisterView;

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

        String otp = JOptionPane.showInputDialog(
                rv, // parent frame
                "Enter OTP:", // message
                "OTP Verification", // title
                JOptionPane.PLAIN_MESSAGE
        );

        if (otp == null) {
            // User pressed Cancel or closed dialog
        } else if (otp.trim().isEmpty()) {
            JOptionPane.showMessageDialog(rv, "OTP cannot be empty");
        } else {
            
        }

        String hashedPassword = Password.hashPassword(password);

        UserPojo user = new UserPojo(0, fullName, username, email, hashedPassword);
        userDao.addUser(user);
    }
    
    public boolean loginUser(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        
        String hashedPassword = Password.hashPassword(password);
        
        return userDao.login(email, hashedPassword);
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
