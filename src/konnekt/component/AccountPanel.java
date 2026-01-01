package konnekt.component;

import konnekt.model.dao.UserDao;
import konnekt.model.pojo.UserPojo;
import konnekt.utils.AvatarUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AccountPanel extends JPanel {

    private final UserDao userDao = new UserDao();
    private final UserPojo currentUser;

    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;

    public AccountPanel(UserPojo user) {
        this.currentUser = user;

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createForm(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        header.setOpaque(false);

        // Avatar
        JLabel avatar = AvatarUtil.avatar(60);
        header.add(avatar);

        // Title
        JLabel title = new JLabel("<html><b>Account Settings</b></html>");
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        header.add(title);

        return header;
    }

    private JPanel createForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        form.add(Box.createVerticalStrut(20));

        // Full Name
        fullNameField = new JTextField(currentUser.getFullName());
        form.add(labeledField("Full Name", fullNameField));

        // Username
        usernameField = new JTextField(currentUser.getUsername());
        form.add(labeledField("Username", usernameField));

        // Email
        emailField = new JTextField(currentUser.getEmail());
        form.add(labeledField("Email", emailField));

        // Buttons panel
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setOpaque(false);
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> saveChanges());
        buttons.add(saveBtn);

        // Change Password link (slightly left)
        JButton changePassBtn = new JButton("Change Password");
        changePassBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        changePassBtn.setForeground(new Color(66, 133, 244));
        changePassBtn.setBorderPainted(false);
        changePassBtn.setContentAreaFilled(false);
        changePassBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        changePassBtn.setMargin(new Insets(0, 5, 0, 0)); // shift a bit left
        changePassBtn.addActionListener(e -> openChangePassword());
        buttons.add(Box.createVerticalStrut(10));
        buttons.add(changePassBtn);

        // Delete Account
        JButton deleteBtn = new JButton("Delete Account");
        deleteBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteBtn.setForeground(Color.RED);
        deleteBtn.addActionListener(e -> deleteAccount());
        buttons.add(Box.createVerticalStrut(10));
        buttons.add(deleteBtn);

        form.add(Box.createVerticalStrut(20));
        form.add(buttons);

        return form;
    }

    private JPanel labeledField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label + ":");
        lbl.setPreferredSize(new Dimension(100, 30));
        panel.add(lbl, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);

        panel.setBorder(new EmptyBorder(5, 0, 5, 0));
        return panel;
    }

    private void saveChanges() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if username/email already exists (for other users)
        UserPojo existingUser = userDao.getUserByEmail(email);
        if (existingUser != null && existingUser.getId() != currentUser.getId()) {
            JOptionPane.showMessageDialog(this, "Email already taken!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        existingUser = userDao.getUserByUsername(username);
        if (existingUser != null && existingUser.getId() != currentUser.getId()) {
            JOptionPane.showMessageDialog(this, "Username already taken!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update user
        currentUser.setFullName(fullName);
        currentUser.setUsername(username);
        currentUser.setEmail(email);

        boolean success = userDao.updateUser(currentUser);
        if (success) {
            JOptionPane.showMessageDialog(this, "Account updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update account!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openChangePassword() {
        // Replace panel with ChangePasswordPanel
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.getContentPane().removeAll();
        topFrame.getContentPane().add(new ChangePasswordPanel(currentUser));
        topFrame.revalidate();
        topFrame.repaint();
    }

    private void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete your account? This cannot be undone!",
                "Delete Account", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = userDao.deleteUser(currentUser.getId());
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Account deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                // Go back to login view
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                topFrame.getContentPane().removeAll();
                topFrame.getContentPane().add(new konnekt.view.LoginView());
                topFrame.revalidate();
                topFrame.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete account!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
