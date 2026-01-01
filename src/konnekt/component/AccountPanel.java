package konnekt.component;

import konnekt.model.dao.UserDao;
import konnekt.model.pojo.UserPojo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import konnekt.view.ChangePasswordView;
import konnekt.view.NavigatorView;

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
        setBackground(Color.WHITE);

        add(createHeader(), BorderLayout.NORTH);
        add(createForm(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        header.setOpaque(false);

        // Title only (no avatar)
        JLabel title = new JLabel("<html><b>Account Settings</b></html>");
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
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

        form.add(Box.createVerticalStrut(20));

        // Save Changes button (left-aligned)
        JButton saveBtn = new JButton("SAVE CHANGES");
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBackground(new Color(66, 133, 244)); // Google blue
        saveBtn.setFocusPainted(false);
        saveBtn.setFont(new Font("Verdana", Font.BOLD, 12));
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT); // align left
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        saveBtn.addActionListener(e -> saveChanges());
        form.add(saveBtn);

        form.add(Box.createVerticalStrut(10));

        // Change Password and Delete Account in same row, left-aligned
        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonsRow.setOpaque(false);

        JButton changePassBtn = new JButton("CHANGE PASSWORD");
        changePassBtn.setForeground(Color.WHITE);
        changePassBtn.setBackground(Color.BLACK);
        changePassBtn.setFocusPainted(false);
        changePassBtn.setFont(new Font("Verdana", Font.BOLD, 12));
        changePassBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        changePassBtn.addActionListener(e -> openChangePassword());
        buttonsRow.add(changePassBtn);

        JButton deleteBtn = new JButton("DELETE ACCOUNT");
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 53, 69)); // Bootstrap red
        deleteBtn.setFocusPainted(false);
        deleteBtn.setFont(new Font("Verdana", Font.BOLD, 12));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        deleteBtn.addActionListener(e -> deleteAccount());
        buttonsRow.add(deleteBtn);

        buttonsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(buttonsRow);

        return form;
    }

    private void saveChanges() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check for existing username/email
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

            // Refresh FeedPanel and ProfilePanel after changes
            NavigatorView.refreshProfile(currentUser.getId());
            NavigatorView.refreshInboxPanel(); // optional if relevant
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update account!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openChangePassword() {
       this.removeAll();
       new ChangePasswordView().setVisible(true);
    }

    private JPanel labeledField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label + ":");
        lbl.setPreferredSize(new Dimension(120, 30));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(lbl, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);

        panel.setBorder(new EmptyBorder(5, 0, 5, 0));
        return panel;
    }

    private void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete your account? This cannot be undone!",
                "Delete Account", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = userDao.deleteUser(currentUser.getId());
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Account deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
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
