/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contactmanagementsystem.db;



import java.sql.*;

public class DBConnection {

    private static final String DB_URL = "jdbc:derby:ContactDB;create=true";
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL);  // No Class.forName needed!
                System.out.println("✅ Derby DB Connected Successfully!");
                createTables();
            } catch (SQLException e) {
                System.out.println("❌ Connection Failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return conn;
    }

    private static void createTables() {
        try (Statement stmt = conn.createStatement()) {

            String usersTable = "CREATE TABLE USERS ("
                    + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                    + "username VARCHAR(50) UNIQUE NOT NULL, "
                    + "password VARCHAR(50) NOT NULL)";

            String contactsTable = "CREATE TABLE CONTACTS ("
                    + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                    + "name VARCHAR(100) NOT NULL, "
                    + "phone VARCHAR(15), "
                    + "email VARCHAR(100), "
                    + "address VARCHAR(200))";

            try {
                stmt.execute(usersTable);
                System.out.println("✅ USERS table ready.");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) {
                    System.out.println("ℹ️ USERS table already exists.");
                } else {
                    e.printStackTrace();
                }
            }

            try {
                stmt.execute(contactsTable);
                System.out.println("✅ CONTACTS table ready.");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) {
                    System.out.println("ℹ️ CONTACTS table already exists.");
                } else {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
