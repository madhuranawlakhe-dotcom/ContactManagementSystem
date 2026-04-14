/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contactmanagementsystem.ui;

/**
 *
 * @author lalit
 */
import contactmanagementsystem.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignupFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField, confirmPasswordField;

    public SignupFrame() {
        setTitle("Contact Manager - Sign Up");
        setSize(420, 320);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 50, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("📋 Create Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        String[] labels = {"Username:", "Password:", "Confirm Password:"};
        JComponent[] fields = {
            usernameField = new JTextField(15),
            passwordField = new JPasswordField(15),
            confirmPasswordField = new JPasswordField(15)
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(Color.WHITE);
            panel.add(lbl, gbc);
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        JButton signupBtn = new JButton("Create Account");
        signupBtn.setBackground(new Color(60, 179, 113));
        signupBtn.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(signupBtn, gbc);

        JButton backBtn = new JButton("Back to Login");
        backBtn.setBackground(new Color(150, 150, 150));
        backBtn.setForeground(Color.WHITE);
        gbc.gridy = 5;
        panel.add(backBtn, gbc);

        signupBtn.addActionListener(e -> signup());
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void signup() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirm = new String(confirmPasswordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO USERS (username, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Account created! Please login.");
            new LoginFrame().setVisible(true);
            dispose();
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "❌ Username already exists!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
