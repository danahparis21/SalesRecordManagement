
package sales.record.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {

    private JButton productManagementButton, dataAnalyticsButton, logoutButton;

    public AdminDashboard() {
     
        setTitle("Admin Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(Color.WHITE);
        
        productManagementButton = new JButton("Product Management");
        productManagementButton.setBounds(200, 100, 200, 40);
        add(productManagementButton);

        
        dataAnalyticsButton = new JButton("Data Analytics");
        dataAnalyticsButton.setBounds(200, 150, 200, 40);
        add(dataAnalyticsButton);
        
        logoutButton = new JButton("Log Out");
        logoutButton.setBounds(200, 200, 200, 40);
        add(logoutButton);

        productManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProductManagement();  
            }
        });
        
        dataAnalyticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDataAnalytics();  
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();  
            }
        });
        
        setVisible(true);
    }
    
    private void logout() {
        dispose();
        new LoginForm().setVisible(true);  
    }


   
    public void openProductManagement() {
        ProductManagement productManagement = new ProductManagement();
        productManagement.setVisible(true);
        dispose();
    }

    
    public void openDataAnalytics() {
        
       DataAnalytics analyticsWindow = new DataAnalytics();
       analyticsWindow.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
        });
    }
}