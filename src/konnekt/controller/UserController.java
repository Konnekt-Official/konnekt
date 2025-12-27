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

        String body = "<html><body>"
                + "<p>Hello, <b>" + fullName + "</b></p>"
                + "<p>Your OTP is:</p><h2 style='color:#01FE49;'>" + otp + "</h2>"
                + "<p>Expires in 5 minutes.</p></body></html>";
        new EmailService().sendEmail(email, "Account Registration OTP", body);

        String message = "An OTP has been sent to " + email + ". Please enter it below:";
        while (true) {
            String inputOTP = JOptionPane.showInputDialog(rv, message, "OTP Verification", JOptionPane.PLAIN_MESSAGE);
            if (inputOTP == null) return;
            inputOTP = inputOTP.trim();
            if (inputOTP.isEmpty()) {
                JOptionPane.showMessageDialog(rv, "OTP cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (otpDao.validateOtp(email, inputOTP, "REGISTER_ACCOUNT")) {
                String hashedPassword = PasswordUtils.hashPassword(password);

                // Default images
                String defaultProfilePicture = "/images/default-profile.png";
                String defaultBannerPicture = "/images/default-banner.png";

                // Create user
                UserPojo user = new UserPojo();
                user.setFullName(fullName);
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(hashedPassword);
                user.setProfileUrl(defaultProfilePicture);
                user.setBannerUrl(defaultBannerPicture);

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