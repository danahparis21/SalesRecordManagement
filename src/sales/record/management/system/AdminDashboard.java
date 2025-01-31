package sales.record.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminDashboard extends JFrame {

    private JButton productManagementButton, dataAnalyticsButton, logoutButton;

    public AdminDashboard() {
     
        setTitle("Admin Dashboard");
        setSize(1080, 608);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(Color.WHITE);
        
        // BACKGROUND IMAGE
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/adminBG.png"));
        if (i1.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Image failed to load!");
        }
        Image image = i1.getImage().getScaledInstance(1080, 608, Image.SCALE_SMOOTH);
        i1 = new ImageIcon(image);
        JLabel imageLabel = new JLabel(i1);
        imageLabel.setBounds(0, 0, 1080, 608);
        add(imageLabel);
        
        productManagementButton = createButton("Product Management", 400, 150, new Color(0, 83, 54));
        imageLabel.add(productManagementButton);

        dataAnalyticsButton = createButton("Data Analytics", 400, 250, new Color(0, 83, 54));
        imageLabel.add(dataAnalyticsButton);
        
      
        logoutButton = createButton("Log Out", 400, 350, new Color(234, 67, 67)); // #ea4343 color
        imageLabel.add(logoutButton);

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

    private JButton createButton(String text, int x, int y, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 300, 80);
        button.setBackground(backgroundColor); 
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover Effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(backgroundColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor); 
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(backgroundColor.darker().darker()); 
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(backgroundColor.darker()); 
            }
        });

      
        button.setBorder(BorderFactory.createLineBorder(backgroundColor, 1, true));
        button.setOpaque(true);

        return button;
    }

    public void openProductManagement() {
        ProductManagement productManagement = new ProductManagement();
        productManagement.setVisible(true);
        dispose();
    }

    public void openDataAnalytics() {
       dispose();
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
