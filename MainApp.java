/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.SwingUtilities;
import loginpage.LoginFrame;
import dsa_ecommerce.ProductTreeGUI;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame((username) -> {
                SwingUtilities.invokeLater(() -> {
                    ProductTreeGUI productTreeGUI = new ProductTreeGUI(username);
                    productTreeGUI.setVisible(true);
                });
            });
            loginFrame.setVisible(true);
        });
    }
}

