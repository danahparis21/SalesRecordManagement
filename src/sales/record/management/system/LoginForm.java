package sales.record.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private String loggedInRole;  
    private int loggedInUserID;   // Store the user's ID (for admin or salesperson)

    public LoginForm() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

      
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Log In");

        getContentPane().setBackground(Color.WHITE);

       
        JLabel titleLabel = new JLabel("Log In");
        titleLabel.setBounds(150, 20, 100, 30);
        titleLabel.setFont(new Font("Segeo UI", Font.BOLD, 22));
        add(titleLabel);

    
        JLabel user = new JLabel("Username");
        user.setBounds(40, 70, 100, 30);
        add(user);

        usernameField.setBounds(150, 70, 150, 30);
        add(usernameField);

        
        JLabel pass = new JLabel("Password");
        pass.setBounds(40, 120, 100, 30);
        add(pass);

        passwordField.setBounds(150, 120, 150, 30);
        add(passwordField);

       
        loginButton.setBounds(150, 170, 100, 30);
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.white);
        add(loginButton);

  
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (validateLogin(username, password)) {

                    // JOptionPane.showMessageDialog(null, "Login Successful");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                }
            }
        });
    }

    //admin - adminpassword
    //salesperson - salespassword
    private boolean validateLogin(String username, String password) {

        Connection conn = Database.connect();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Database connection failed");
            return false;
        }

        try {
            String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                loggedInUserID = rs.getInt("id");
                loggedInRole = rs.getString("role");

                String role = rs.getString("role");

                if (role.equals("admin")) {
                    JOptionPane.showMessageDialog(null, "Welcome Admin!");
                    AdminDashboard adminDashboard = new AdminDashboard();
                    adminDashboard.setVisible(true);
                    dispose();

                } else if (role.equals("salesperson")) {
                    JOptionPane.showMessageDialog(null, "Welcome Salesperson!");
                    SalesRecord salesRecord = new SalesRecord(loggedInUserID);
                    salesRecord.setVisible(true);
                    dispose();
                }

                return true;

            } else {
              
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
      
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}
