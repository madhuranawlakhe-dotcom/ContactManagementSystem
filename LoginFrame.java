/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contactmanagementsystem.ui;


import contactmanagementsystem.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Contact Manager - Login");
        setSize(400, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("🔐 Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(loginBtn, gbc);

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBackground(new Color(60, 179, 113));
        signupBtn.setForeground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(signupBtn, gbc);

        loginBtn.addActionListener(e -> login());
        signupBtn.addActionListener(e -> {
            new SignupFrame().setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM USERS WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "✅ Welcome, " + username + "!");
                new ContactFrame(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid credentials!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
