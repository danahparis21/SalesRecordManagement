package sales.record.management.system;

import com.toedter.calendar.JDateChooser;
import java.sql.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SalesRecord extends JFrame {
    private JTextField productIDField, quantityField, totalPriceField, productFilterField;
    private JButton addSaleButton, updateSaleButton, deleteSaleButton, filterButton, logoutButton;
    private JTable salesTable;
    private DefaultTableModel tableModel;
    private int loggedInSalespersonID;  
    private int selectedSalesID;  
    private JComboBox<String> dateFilterComboBox;
    private JDateChooser startDateChooser, endDateChooser;
    private JTextField salespersonFilterField, customerFilterField, productNameField, productPriceField;

    public SalesRecord(int salespersonID) {
        loggedInSalespersonID = salespersonID;  
        setTitle("Sales Record Management");
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Display Salesperson ID
        JLabel salespersonIDLabel = new JLabel("Salesperson ID: " + loggedInSalespersonID);
        salespersonIDLabel.setBounds(50, 20, 200, 25);
        add(salespersonIDLabel);

      
        JLabel productIDLabel = new JLabel("Product ID:");
        productIDLabel.setBounds(50, 50, 100, 25);
        add(productIDLabel);

        productIDField = new JTextField();
        productIDField.setBounds(150, 50, 200, 30);
        add(productIDField);
        
        JLabel productNameLabel = new JLabel("Product Name:");
        productNameLabel.setBounds(390, 50, 100, 25);
        add(productNameLabel);

        productNameField = new JTextField();
        productNameField.setBounds(480, 50, 200, 30);
        add(productNameField);
        
        JLabel productPriceLabel = new JLabel("Price (Php):");
        productPriceLabel.setBounds(390, 100, 100, 25);
        add(productPriceLabel);

        productPriceField = new JTextField();
        productPriceField.setBounds(480, 100, 200, 30);
        add(productPriceField);

        JLabel quantityLabel = new JLabel("Quantity Sold:");
        quantityLabel.setBounds(50, 100, 100, 25);
        add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(150, 100, 200, 30);
        add(quantityField);

        JLabel totalPriceLabel = new JLabel("Total Price:");
        totalPriceLabel.setBounds(50, 150, 100, 25);
        add(totalPriceLabel);

        totalPriceField = new JTextField();
        totalPriceField.setBounds(150, 150, 200, 30);
        totalPriceField.setEditable(false); // automatic calculated
        add(totalPriceField);

        addSaleButton = new JButton("Add Sale");
        addSaleButton.setBounds(450, 220, 180, 60);
        styleButton(addSaleButton);
        add(addSaleButton);

        // Buttons
        updateSaleButton = new JButton("Update Sale");
        updateSaleButton.setBounds(680, 220, 180, 60);
        updateSaleButton.setEnabled(false); 
        styleButton(updateSaleButton);
        add(updateSaleButton);

        deleteSaleButton = new JButton("Delete Sale");
        deleteSaleButton.setBounds(900, 220, 180, 60);
        deleteSaleButton.setEnabled(false); 
        styleButton(deleteSaleButton);
        add(deleteSaleButton);

        // Product Filter
        JLabel productFilterLabel = new JLabel("Filter by Product ID:");
        productFilterLabel.setBounds(700, 50, 150, 25);
        add(productFilterLabel);

        productFilterField = new JTextField();
        productFilterField.setBounds(850, 50, 200, 30);
        add(productFilterField);

        // Date Filter
        JLabel dateFilterLabel = new JLabel("Filter by Date:");
        dateFilterLabel.setBounds(700, 100, 100, 25);
        add(dateFilterLabel);

        String[] dateFilterOptions = {"All", "Today", "This Week", "This Month"};
        dateFilterComboBox = new JComboBox<>(dateFilterOptions);
        dateFilterComboBox.setBounds(850, 100, 200, 30);
        add(dateFilterComboBox);
        
        // Salesperson Filter
        JLabel salespersonLabel = new JLabel("Filter by Salesperson ID:");
        salespersonLabel.setBounds(700, 150, 150, 25);
        add(salespersonLabel);

        salespersonFilterField = new JTextField();
        salespersonFilterField.setBounds(850, 150, 200, 30);
        add(salespersonFilterField);

        
        filterButton = new JButton("Apply Filter");
        filterButton.setBounds(1080, 135, 150, 40);
        styleButton(filterButton);
        add(filterButton);
        
       
        logoutButton = new JButton("Logout");
        logoutButton.setBounds(1300, 10, 150, 30);
        add(logoutButton);

        
        // Table 
        tableModel = new DefaultTableModel(new String[]{"Sales ID", "Product ID", "Product Name", "Quantity Sold", "Total Price", "Sale Date", "Salesperson ID", "Product Price"}, 0);

        salesTable = new JTable(tableModel);
        salesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        salesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        salesTable.setRowHeight(30); 
        JScrollPane scrollPane = new JScrollPane(salesTable);
        scrollPane.setBounds(150, 350, 1200, 400);
        add(scrollPane);
        loadSalesData();  

        addSaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSalesRecord();
            }
        });
        
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();  
            }
        });

        updateSaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSalesRecord();
            }
        });

        deleteSaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSalesRecord();
            }
        });

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSalesData(); 
            }
        });

        salesTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = salesTable.getSelectedRow();
            if (selectedRow != -1) {
                
                selectedSalesID = (int) tableModel.getValueAt(selectedRow, 0);
                updateSaleButton.setEnabled(true);
                deleteSaleButton.setEnabled(true);

                
                productIDField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
                productNameField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
                quantityField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
                totalPriceField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
                productPriceField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 7)));
            }
        });
        
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

    private void addSalesRecord() {
        int productID = Integer.parseInt(productIDField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        Connection conn = Database.connect();
        double productPrice = 0.0;
        int currentStock = 0; 
        if (conn != null) {
            try {
                String sql = "SELECT price, stock FROM products WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, productID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    productPrice = rs.getDouble("price");
                    currentStock = rs.getInt("stock");  
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        
        if (quantity > currentStock) {
            JOptionPane.showMessageDialog(this, "Not enough stock available for this product.");
            return;
        }

        double totalPrice = productPrice * quantity;
        totalPriceField.setText(String.valueOf(totalPrice));

       
        try {
            String sql = "INSERT INTO sales (product_id, salesperson_id, quantity, total_price, sale_date) VALUES (?, ?, ?, ?, NOW())";
            Connection conn2 = Database.connect();
            PreparedStatement stmt = conn2.prepareStatement(sql);
            stmt.setInt(1, productID);
            stmt.setInt(2, loggedInSalespersonID);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, totalPrice);
            stmt.executeUpdate();

           
            String updateStockSql = "UPDATE products SET stock = stock - ? WHERE id = ?";
            PreparedStatement updateStmt = conn2.prepareStatement(updateStockSql);
            updateStmt.setInt(1, quantity);  
            updateStmt.setInt(2, productID);
            updateStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Sale added and stock updated successfully!");
            loadSalesData();  
        } catch (SQLException e) {
            e.printStackTrace();
        }
}
    


    private void updateSalesRecord() {
        int productID = Integer.parseInt(productIDField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        Connection conn = Database.connect();
        double productPrice = 0.0;
        int oldQuantity = 0; 

        if (conn != null) {
            try {
              
                String sql = "SELECT price FROM products WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, productID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    productPrice = rs.getDouble("price");
                }

               
                String selectOldQuantitySql = "SELECT quantity FROM sales WHERE id = ?";
                PreparedStatement stmt2 = conn.prepareStatement(selectOldQuantitySql);
                stmt2.setInt(1, selectedSalesID);  
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next()) {
                    oldQuantity = rs2.getInt("quantity");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        double totalPrice = productPrice * quantity;
        totalPriceField.setText(String.valueOf(totalPrice));

        try {
          
            String updateSaleSql = "UPDATE sales SET product_id = ?, quantity = ?, total_price = ? WHERE id = ?";
            Connection conn2 = Database.connect();
            PreparedStatement stmt = conn2.prepareStatement(updateSaleSql);
            stmt.setInt(1, productID);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, totalPrice);
            stmt.setInt(4, selectedSalesID);
            stmt.executeUpdate();

            // Adjust product stock
            String updateStockSql = "UPDATE products SET stock = stock - ? + ? WHERE id = ?";
            PreparedStatement stmt3 = conn2.prepareStatement(updateStockSql);
            stmt3.setInt(1, oldQuantity); 
            stmt3.setInt(2, quantity);    
            stmt3.setInt(3, productID);
            stmt3.executeUpdate();

            JOptionPane.showMessageDialog(this, "Sale updated successfully!");
            loadSalesData(); 

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteSalesRecord() {
        try {
           
            String selectSaleSql = "SELECT product_id, quantity FROM sales WHERE id = ?";
            Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(selectSaleSql);
            stmt.setInt(1, selectedSalesID);  
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int productID = rs.getInt("product_id");
                int quantitySold = rs.getInt("quantity");

               
                String updateStockSql = "UPDATE products SET stock = stock + ? WHERE id = ?";
                PreparedStatement stmt2 = conn.prepareStatement(updateStockSql);
                stmt2.setInt(1, quantitySold);  
                stmt2.setInt(2, productID);
                stmt2.executeUpdate();
            }

            // Delete sales record
            String deleteSaleSql = "DELETE FROM sales WHERE id = ?";
            PreparedStatement stmt3 = conn.prepareStatement(deleteSaleSql);
            stmt3.setInt(1, selectedSalesID);
            stmt3.executeUpdate();

            JOptionPane.showMessageDialog(this, "Sale deleted successfully!");
            loadSalesData();  

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadSalesData() {
    tableModel.setRowCount(0); 

    Connection conn = Database.connect();
    if (conn == null) {
        JOptionPane.showMessageDialog(this, "Database connection failed.");
        return;
    }

    try {
       
        StringBuilder sql = new StringBuilder("SELECT s.id, s.product_id, p.name AS product_name, p.price AS product_price, s.quantity, s.total_price, s.sale_date, s.salesperson_id " +
                                             "FROM sales s " +
                                             "JOIN products p ON s.product_id = p.id " +
                                             "WHERE 1=1");

     
        if (!productFilterField.getText().trim().isEmpty()) {
            sql.append(" AND s.product_id = ?");
        }


        if (!salespersonFilterField.getText().trim().isEmpty()) {
            sql.append(" AND s.salesperson_id = ?");
        }


        String selectedDateFilter = dateFilterComboBox.getSelectedItem().toString();
        if (!selectedDateFilter.equals("All")) {
            sql.append(" AND s.sale_date >= ? AND s.sale_date <= ?");
        }

        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        int paramIndex = 1;

        
        if (!productFilterField.getText().trim().isEmpty()) {
            stmt.setInt(paramIndex++, Integer.parseInt(productFilterField.getText().trim()));
        }

        if (!salespersonFilterField.getText().trim().isEmpty()) {
            stmt.setInt(paramIndex++, Integer.parseInt(salespersonFilterField.getText().trim()));
        }

        // Date filter 
        if (!selectedDateFilter.equals("All")) {
            LocalDate now = LocalDate.now();
            LocalDate startDate = null, endDate = now;

            switch (selectedDateFilter) {
                case "Today":
                    startDate = now;
                    break;
                case "This Week":
                    startDate = now.minusDays(now.getDayOfWeek().getValue() - 1);
                    break;
                case "This Month":
                    startDate = now.withDayOfMonth(1);
                    break;
            }

            stmt.setDate(paramIndex++, java.sql.Date.valueOf(startDate));
            stmt.setDate(paramIndex++, java.sql.Date.valueOf(endDate));
        }

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
           
            tableModel.addRow(new Object[]{
                rs.getInt("id"),                  // Sales ID
                rs.getInt("product_id"),          // Product ID
                rs.getString("product_name"),     // Product Name
                rs.getInt("quantity"),            // Quantity Sold
                rs.getDouble("total_price"),      // Total Price
                rs.getDate("sale_date"),          // Sale Date
                rs.getInt("salesperson_id"),      // Salesperson ID
                rs.getDouble("product_price")     // Product Price
            });

        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void logout() {
        
        dispose();

        
        new LoginForm().setVisible(true);  
    }

    public static void main(String[] args) {
        new SalesRecord(1).setVisible(true);  
    }
}
