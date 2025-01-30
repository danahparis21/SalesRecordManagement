package sales.record.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ProductManagement extends JFrame {
    private JTextField productNameField, categoryField, priceField, stockQuantityField;
    private JButton addButton, updateButton, deleteButton, backButton;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public ProductManagement() {
        setTitle("Product Management");
        setSize(1920, 1080);  
        setLocationRelativeTo(null);  
        setLayout(null);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        
        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setBounds(600, 50, 120, 25);
        add(nameLabel);
        
        productNameField = new JTextField();
        productNameField.setBounds(750, 50, 200, 30);
        add(productNameField);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(600, 100, 120, 25);
        add(categoryLabel);
        
        categoryField = new JTextField();
        categoryField.setBounds(750, 100, 200, 30);
        add(categoryField);

        JLabel priceLabel = new JLabel("Price (Pesos):");
        priceLabel.setBounds(600, 150, 120, 25);
        add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(750, 150, 200, 30);
        add(priceField);

        JLabel stockLabel = new JLabel("Stock Quantity:");
        stockLabel.setBounds(600, 200, 120, 25);
        add(stockLabel);

        stockQuantityField = new JTextField();
        stockQuantityField.setBounds(750, 200, 200, 30);
        add(stockQuantityField);

        
        addButton = new JButton("Add Product");
        addButton.setBounds(400, 250, 150, 40);
        add(addButton);

        updateButton = new JButton("Update Product");
        updateButton.setBounds(600, 250, 150, 40);
        add(updateButton);

        deleteButton = new JButton("Delete Product");
        deleteButton.setBounds(800, 250, 150, 40);
        add(deleteButton);
        
        backButton = new JButton("Back");
        backButton.setBounds(1200, 20, 150, 30);
        add(backButton);
        
        JButton resetButton = new JButton("Reset Fields");
        resetButton.setBounds(1000, 250, 150, 40);
        add(resetButton);

        // Create JTable to display products
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Price", "Stock"}, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBounds(150, 320, 1200, 400); 
        add(scrollPane);
        
        productTable.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
        
            productNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            categoryField.setText((String) tableModel.getValueAt(selectedRow, 2));
            priceField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
            stockQuantityField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
        }
    }
});

      
        loadProductData();

        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                back();  
            }
        });
        
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();  
            }
        });
    }
    private void resetFields() {
        productNameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        stockQuantityField.setText("");
}


    private void loadProductData() {
        Connection conn = Database.connect();
        if (conn != null) {
            try {
                String sql = "SELECT * FROM products";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                
                tableModel.setRowCount(0);

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String category = rs.getString("category");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");

                    
                    tableModel.addRow(new Object[]{id, name, category, price, stock});
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addProduct() {
        String name = productNameField.getText();
        String category = categoryField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockQuantityField.getText());

        Connection conn = Database.connect();
        if (conn != null) {
            try {
                String sql = "INSERT INTO products (name, category, price, stock) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setString(2, category);
                stmt.setDouble(3, price);
                stmt.setInt(4, stock);

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Product added successfully!");
                    loadProductData();  // Refresh product data
                    resetFields();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.");
            return;
        }

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        String name = productNameField.getText();
        String category = categoryField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockQuantityField.getText());

        Connection conn = Database.connect();
        if (conn != null) {
            try {
                String sql = "UPDATE products SET name = ?, category = ?, price = ?, stock = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setString(2, category);
                stmt.setDouble(3, price);
                stmt.setInt(4, stock);
                stmt.setInt(5, productId);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Product updated successfully!");
                    loadProductData();  
                    resetFields();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
            return;
        }

        int productId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = Database.connect();
            if (conn != null) {
                try {
                    String sql = "DELETE FROM products WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, productId);

                    int rowsDeleted = stmt.executeUpdate();
                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                        loadProductData();  
                        resetFields();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void back() {
        
        dispose();
        new AdminDashboard().setVisible(true);  
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ProductManagement().setVisible(true);
            }
        });
    }
}
