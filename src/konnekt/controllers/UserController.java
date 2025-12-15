/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package konnekt.controllers;

import konnekt.model.dao.UserDAO;
import konnekt.model.pojo.UserPojo;

/**
 *
 * @author Hp
 */
public class UserController {

    private final UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }

    public boolean registerUser(String fullName, String username, String email, String password) {
        if (fullName == null || fullName.isEmpty() ||
            username == null || username.isEmpty() ||
            email == null || email.isEmpty() ||
            password == null || password.isEmpty()) {
            return false;
        }

        // 2️⃣ Check if username or email exists
        if (userDAO.existsByUsername(username) || userDAO.existsByEmail(email)) {
            return false;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);

        UserPojo user = new UserPojo(0, fullName, username, email, hashedPassword);
        return userDAO.addUser(user);
    }

    public boolean isEmptyField(String fullName, String username, String email, String password) {
        return fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty();
    }

    public boolean usernameExists(String username) {
        return userDAO.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userDAO.existsByEmail(email);
    }
}
