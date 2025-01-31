package sales.record.management.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame {

    public static void main(String[] args) {
       
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }

    public Main() {
        
        setTitle("Sales record management system");
        setSize(1080, 608); 
        setLayout(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
        
        setLocationRelativeTo(null);

        // BACKGROUND IMAGE
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/groceryStore3.png"));
        if (i1.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Image failed to load!");
        }
        Image image = i1.getImage().getScaledInstance(1080, 608, Image.SCALE_SMOOTH);
        i1 = new ImageIcon(image);
        JLabel imageLabel = new JLabel(i1);
        imageLabel.setBounds(0, 0, 1080, 608);
        add(imageLabel);

        // START BUTTON
        JButton start = createButton("LOG IN", 350, 450);
        start.addActionListener(new ButtonActionListener("LOG IN"));
        imageLabel.add(start);

    }

    // BUTTON EFFECTS
    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 250, 40);
        button.setBackground(Color.decode("#005336")); 
        button.setForeground(Color.WHITE);
        button.setFont(new Font("serif", Font.PLAIN, 24));
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.decode("#73aa92")); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180)); 
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(30, 60, 90)); 
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237)); 
            }
        });

        return button;
    }

    // ACTION FOR BUTTONS
    private class ButtonActionListener implements ActionListener {
        private String buttonType;

        public ButtonActionListener(String buttonType) {
            this.buttonType = buttonType;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (buttonType) {
                case "LOG IN":
         
                    new LoginForm().setVisible(true);
                    dispose();
                    break;
                
            }
        }
    }
}
