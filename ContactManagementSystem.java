/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package contactmanagementsystem;

import contactmanagementsystem.db.DBConnection;
import contactmanagementsystem.ui.LoginFrame;
import javax.swing.SwingUtilities;

/**
 *

 */
public class ContactManagementSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DBConnection.getConnection(); // Initialize DB
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

}
