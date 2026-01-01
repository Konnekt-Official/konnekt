package konnekt.component;

import konnekt.model.dao.UserDao;
import konnekt.model.pojo.UserPojo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChangePasswordPanel extends JPanel {

    private final UserPojo currentUser;
    private final UserDao userDao = new UserDao();

    private JPasswordField oldPass;
    private JPasswordField newPass;
    private JPasswordField confirmPass;

    public ChangePasswordPanel(UserPojo user) {
        this.currentUser = user;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        addField("Old Password", oldPass = new JPasswordField());
        addField("New Password", newPass = new JPasswordField());
        addField("Confirm Password", confirmPass = new JPasswordField());

        JButton changeBtn = new JButton("Change Password");
        changeBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        changeBtn.addActionListener(e -> changePassword());
        add(Box.createVerticalStrut(20));
        add(changeBtn);
    }

    private void addField(String label, JPasswordField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label + ":");
        lbl.setPreferredSize(new Dimension(120, 30));
        panel.add(lbl, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);

        panel.setBorder(new EmptyBorder(5, 0, 5, 0));
        add(panel);
        add(Box.createVerticalStrut(10));
    }

    private void changePassword() {
        String oldP = new String(oldPass.getPassword());
        String newP = new String(newPass.getPassword());
        String confP = new String(confirmPass.getPassword());

        if (!oldP.equals(currentUser.getPassword())) {
            JOptionPane.showMessageDialog(this, "Old password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newP.equals(confP)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser.setPassword(newP);
        boolean success = userDao.updateUser(currentUser);
        if (success) {
            JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to change password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
