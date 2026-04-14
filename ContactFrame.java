/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contactmanagementsystem.ui;


import contactmanagementsystem.db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.sql.*;

public class ContactFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, phoneField, emailField, addressField, searchField;
    private String loggedInUser;

    public ContactFrame(String username) {
        this.loggedInUser = username;
        setTitle("📇 Contact Manager - Welcome " + username);
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---- TOP: Search Bar ----
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(245, 245, 250));
        topPanel.add(new JLabel("🔍 Search:"));
        searchField = new JTextField(20);
        topPanel.add(searchField);
        JButton searchBtn = new JButton("Search");
        JButton showAllBtn = new JButton("Show All");
        topPanel.add(searchBtn);
        topPanel.add(showAllBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setForeground(Color.RED);
        topPanel.add(Box.createHorizontalStrut(300));
        topPanel.add(logoutBtn);

        // ---- CENTER: Table ----
        String[] cols = {"ID", "Name", "Phone", "Email", "Address"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        JScrollPane scrollPane = new JScrollPane(table);

        // ---- RIGHT: Form Panel ----
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 250));
        formPanel.setPreferredSize(new Dimension(250, 0));
        formPanel.setBorder(BorderFactory.createTitledBorder("Contact Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        String[] fieldLabels = {"Name *", "Phone", "Email", "Address"};
        JTextField[] fields = {
            nameField = new JTextField(15),
            phoneField = new JTextField(15),
            emailField = new JTextField(15),
            addressField = new JTextField(15)
        };

        for (int i = 0; i < fieldLabels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i * 2;
            formPanel.add(new JLabel(fieldLabels[i]), gbc);
            gbc.gridy = i * 2 + 1;
            formPanel.add(fields[i], gbc);
        }

        // Buttons
        JButton addBtn = new JButton("➕ Add");
        JButton updateBtn = new JButton("✏️ Update");
        JButton deleteBtn = new JButton("🗑️ Delete");
        JButton clearBtn = new JButton("🔄 Clear");

        addBtn.setBackground(new Color(60, 179, 113));
        addBtn.setForeground(Color.WHITE);
        updateBtn.setBackground(new Color(70, 130, 180));
        updateBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 80, 60));
        deleteBtn.setForeground(Color.WHITE);

        gbc.gridwidth = 1;
        gbc.gridy = 9;
        gbc.gridx = 0;
        formPanel.add(addBtn, gbc);
        gbc.gridx = 1;
        formPanel.add(updateBtn, gbc);
        gbc.gridy = 10;
        gbc.gridx = 0;
        formPanel.add(deleteBtn, gbc);
        gbc.gridx = 1;
        formPanel.add(clearBtn, gbc);

        // ---- Layout ----
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.EAST);

        // ---- Table row click -> fill form ----
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                nameField.setText((String) tableModel.getValueAt(row, 1));
                phoneField.setText((String) tableModel.getValueAt(row, 2));
                emailField.setText((String) tableModel.getValueAt(row, 3));
                addressField.setText((String) tableModel.getValueAt(row, 4));
            }
        });

        // ---- Button Actions ----
        addBtn.addActionListener(e -> addContact());
        updateBtn.addActionListener(e -> updateContact());
        deleteBtn.addActionListener(e -> deleteContact());
        clearBtn.addActionListener(e -> clearForm());
        searchBtn.addActionListener(e -> searchContacts());
        showAllBtn.addActionListener(e -> loadContacts());
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        loadContacts();
    }

    private void loadContacts() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM CONTACTS ORDER BY id");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("name"),
                    rs.getString("phone"), rs.getString("email"), rs.getString("address")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addContact() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required!");
            return;
        }
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO CONTACTS (name, phone, email, address) VALUES (?,?,?,?)");
            ps.setString(1, name);
            ps.setString(2, phoneField.getText().trim());
            ps.setString(3, emailField.getText().trim());
            ps.setString(4, addressField.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Contact added!");
            clearForm();
            loadContacts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateContact() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a contact to update!");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required!");
            return;
        }
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE CONTACTS SET name=?, phone=?, email=?, address=? WHERE id=?");
            ps.setString(1, name);
            ps.setString(2, phoneField.getText().trim());
            ps.setString(3, emailField.getText().trim());
            ps.setString(4, addressField.getText().trim());
            ps.setInt(5, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Contact updated!");
            clearForm();
            loadContacts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteContact() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a contact to delete!");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this contact?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM CONTACTS WHERE id=?");
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "🗑️ Contact deleted!");
                clearForm();
                loadContacts();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void searchContacts() {
        String keyword = "%" + searchField.getText().trim() + "%";
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM CONTACTS WHERE name LIKE ? OR phone LIKE ? OR email LIKE ?");
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("name"),
                    rs.getString("phone"), rs.getString("email"), rs.getString("address")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        table.clearSelection();
    }
}
