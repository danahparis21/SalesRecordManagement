package sales.record.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ProductManagement extends JFrame {
    private JTextField productNameField, categoryField, priceField, stockQuantityField;
    private JTextField searchNameField, searchIdField, searchCategoryField;
    private JButton addButton, updateButton, deleteButton, backButton;
    private JButton searchButton;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public ProductManagement() {
        setTitle("Product Management");
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.decode("#FAFAFA")); // Light gray background

        // Modern font
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);

        // Product Fields
        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setBounds(400, 50, 120, 25);
        add(nameLabel);

        productNameField = new JTextField();
        productNameField.setBounds(550, 50, 200, 30);
        productNameField.setBorder(BorderFactory.createLineBorder(Color.decode("#e1e1e1"), 2));
        add(productNameField);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(labelFont);
        categoryLabel.setBounds(400, 100, 120, 25);
        add(categoryLabel);

        categoryField = new JTextField();
        categoryField.setBounds(550, 100, 200, 30);
        categoryField.setBorder(BorderFactory.createLineBorder(Color.decode("#e1e1e1"), 2));
        add(categoryField);

        JLabel priceLabel = new JLabel("Price (Pesos):");
        priceLabel.setFont(labelFont);
        priceLabel.setBounds(400, 150, 120, 25);
        add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(550, 150, 200, 30);
        priceField.setBorder(BorderFactory.createLineBorder(Color.decode("#e1e1e1"), 2));
        add(priceField);

        JLabel stockLabel = new JLabel("Stock Quantity:");
        stockLabel.setFont(labelFont);
        stockLabel.setBounds(400, 200, 120, 25);
        add(stockLabel);

        stockQuantityField = new JTextField();
        stockQuantityField.setBounds(550, 200, 200, 30);
        stockQuantityField.setBorder(BorderFactory.createLineBorder(Color.decode("#e1e1e1"), 2));
        add(stockQuantityField);

        // Buttons
        addButton = new JButton("Add Product");
        styleButton(addButton);
        addButton.setBounds(800, 50, 150, 40);
        add(addButton);

        updateButton = new JButton("Update Product");
        styleButton(updateButton);
        updateButton.setBounds(800, 100, 150, 40);
        add(updateButton);

        deleteButton = new JButton("Delete Product");
        styleButton(deleteButton);
        deleteButton.setBounds(800, 150, 150, 40);
        add(deleteButton);

        JButton resetButton = new JButton("Reset Fields");
        styleButton(resetButton);
        resetButton.setBounds(800, 200, 150, 40);
        add(resetButton);

        
        JLabel searchLabel = new JLabel("Search by");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        searchLabel.setForeground(Color.decode("#ea4343"));
        searchLabel.setBounds(280, 280, 120, 30);
        add(searchLabel);

        JLabel searchNameLabel = new JLabel("Name:");
        searchNameLabel.setFont(labelFont);
        searchNameLabel.setBounds(400, 280, 60, 30);
        add(searchNameLabel);

        searchNameField = new JTextField();
        searchNameField.setBounds(460, 280, 120, 30);
        add(searchNameField);

        JLabel searchIdLabel = new JLabel("ID:");
        searchIdLabel.setFont(labelFont);
        searchIdLabel.setBounds(600, 280, 60, 30);
        add(searchIdLabel);

        searchIdField = new JTextField();
        searchIdField.setBounds(640, 280, 120, 30);
        add(searchIdField);

        JLabel searchCategoryLabel = new JLabel("Category:");
        searchCategoryLabel.setFont(labelFont);
        searchCategoryLabel.setBounds(780, 280, 80, 30);
        add(searchCategoryLabel);

        searchCategoryField = new JTextField();
        searchCategoryField.setBounds(860, 280, 120, 30);
        add(searchCategoryField);

        searchButton = new JButton("Search");
        styleButton(searchButton);
        searchButton.setBounds(1000, 280, 100, 30);
        add(searchButton);

        backButton = new JButton("Back");
        styleButton(backButton);
        backButton.setBounds(1200, 20, 150, 30);
        add(backButton);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Price", "Stock"}, 0);
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        productTable.setRowHeight(30);  
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

        // Action Listeners
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        backButton.addActionListener(e -> back());
        resetButton.addActionListener(e -> resetFields());
        searchButton.addActionListener(e -> searchProducts());
    }

    
    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(Color.decode("#4CAF50"));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setPreferredSize(new Dimension(150, 40));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#e1e1e1")),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Hover effect
        Color defaultBackground = button.getBackground();
        Color hoverBackground = Color.decode("#45a049"); 

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBackground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultBackground);
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

    private void searchProducts() {
        String name = searchNameField.getText();
        String id = searchIdField.getText();
        String category = searchCategoryField.getText();

        Connection conn = Database.connect();
        if (conn != null) {
            try {
                StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");

                if (!name.isEmpty()) {
                    sql.append(" AND name LIKE ?");
                }
                if (!id.isEmpty()) {
                    sql.append(" AND id = ?");
                }
                if (!category.isEmpty()) {
                    sql.append(" AND category LIKE ?");
                }

                PreparedStatement stmt = conn.prepareStatement(sql.toString());

                int index = 1;
                if (!name.isEmpty()) {
                    stmt.setString(index++, "%" + name + "%");
                }
                if (!id.isEmpty()) {
                    stmt.setInt(index++, Integer.parseInt(id));
                }
                if (!category.isEmpty()) {
                    stmt.setString(index++, "%" + category + "%");
                }

                ResultSet rs = stmt.executeQuery();

                tableModel.setRowCount(0); 
                while (rs.next()) {
                    int productId = rs.getInt("id");
                    String productName = rs.getString("name");
                    String productCategory = rs.getString("category");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");

                    tableModel.addRow(new Object[]{productId, productName, productCategory, price, stock});
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
