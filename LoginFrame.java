/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginpage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.io.*;
import java.util.function.Consumer;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;
    private final Consumer<String> onLoginSuccess;

    /**
     *
     * @param onLoginSuccess
     */
    public LoginFrame(Consumer<String> onLoginSuccess) {
    this.onLoginSuccess = onLoginSuccess;
        setTitle("User Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        userPanel.add(usernameLabel);
        userPanel.add(usernameField);

        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        passPanel.add(passwordLabel);
        passPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton = new JButton("Login");
        buttonPanel.add(loginButton);

        JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createAccountButton = new JButton("Create Account");
        createPanel.add(createAccountButton);

        mainPanel.add(userPanel);
        mainPanel.add(passPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(createPanel);
        add(mainPanel);

        loginButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Please enter both username and password.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Login successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                if (onLoginSuccess != null) {
                    onLoginSuccess.accept(username);
                }
                
                LoginFrame.this.dispose();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Invalid username or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        createAccountButton.addActionListener((ActionEvent e) -> {
            createNewAccount();
        });
    }

    private boolean authenticateUser(String username, String password) {
        String filePath = "users.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String fileUsername = parts[0].trim();
                    String filePassword = parts[1].trim();
                    if (username.equals(fileUsername) && password.equals(filePassword)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error reading users.txt. Please ensure the file exists.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return false;
    }

    private void createNewAccount() {
        String newUsername = JOptionPane.showInputDialog(this, "Enter new username:");
        if (newUsername == null || newUsername.trim().isEmpty()) return;
        newUsername = newUsername.trim();

        String newPassword = JOptionPane.showInputDialog(this, "Enter new password:");
        if (newPassword == null || newPassword.trim().isEmpty()) return;
        newPassword = newPassword.trim();

        if (userExists(newUsername)) {
            JOptionPane.showMessageDialog(this,
                    "Username already exists. Please choose another.",
                    "Account Creation Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String filePath = "users.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(newUsername + "," + newPassword);
            bw.newLine();
            JOptionPane.showMessageDialog(this,
                    "Account created successfully! You can now log in.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error writing to users.txt. Account creation failed.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean userExists(String username) {
        String filePath = "users.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    String fileUsername = parts[0].trim();
                    if (username.equals(fileUsername)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            // Ignore if file doesn't exist yet
        }
        return false;
    }


    public static void main(String[] args) {
        // Ensure UI runs on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Launch the login frame and define what happens on successful login
            LoginFrame loginFrame = new LoginFrame((username) -> {
                // Placeholder for launching the main application after login
                JOptionPane.showMessageDialog(null, "Welcome! Application starting...");
            });
            loginFrame.setVisible(true);
        });
    }
}
