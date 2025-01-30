package sales.record.management.system;

import java.sql.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public SalesRecord(int salespersonID) {
        loggedInSalespersonID = salespersonID;  
        setTitle("Sales Record Management");
        setSize(800, 600);
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
        totalPriceField.setEditable(false); // Not editable, it will be calculated
        add(totalPriceField);

        addSaleButton = new JButton("Add Sale");
        addSaleButton.setBounds(170, 200, 150, 40);
        add(addSaleButton);

        // Buttons
        updateSaleButton = new JButton("Update Sale");
        updateSaleButton.setBounds(50, 250, 150, 40);
        updateSaleButton.setEnabled(false); 
        add(updateSaleButton);

        deleteSaleButton = new JButton("Delete Sale");
        deleteSaleButton.setBounds(250, 250, 150, 40);
        deleteSaleButton.setEnabled(false); 
        add(deleteSaleButton);

        // Product Filter
        JLabel productFilterLabel = new JLabel("Filter by Product ID:");
        productFilterLabel.setBounds(400, 50, 150, 25);
        add(productFilterLabel);

        productFilterField = new JTextField();
        productFilterField.setBounds(550, 50, 200, 30);
        add(productFilterField);

        // Date Filter
        JLabel dateFilterLabel = new JLabel("Filter by Date:");
        dateFilterLabel.setBounds(400, 100, 100, 25);
        add(dateFilterLabel);

        String[] dateFilterOptions = {"All", "Today", "This Week", "This Month"};
        dateFilterComboBox = new JComboBox<>(dateFilterOptions);
        dateFilterComboBox.setBounds(550, 100, 200, 30);
        add(dateFilterComboBox);

        
        filterButton = new JButton("Apply Filter");
        filterButton.setBounds(550, 150, 150, 40);
        add(filterButton);
        
       
        logoutButton = new JButton("Logout");
        logoutButton.setBounds(600, 520, 150, 30);
        add(logoutButton);

        
        // Table 
        tableModel = new DefaultTableModel(new String[]{"Sales ID", "Product ID", "Quantity Sold", "Total Price", "Sale Date"}, 0);
        salesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(salesTable);
        scrollPane.setBounds(50, 300, 700, 200);
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
                quantityField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
                totalPriceField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
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
                    currentStock = rs.getInt("stock");  // Get the current stock of the product
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
            updateStmt.setInt(1, quantity);  // Subtract the sold quantity from the stock
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
                stmt2.setInt(1, selectedSalesID);  // selectedSalesID is the current sales record ID
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
            // Get product ID and quantity sold before deletion
            String selectSaleSql = "SELECT product_id, quantity FROM sales WHERE id = ?";
            Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(selectSaleSql);
            stmt.setInt(1, selectedSalesID);  // Get the selected sales record
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int productID = rs.getInt("product_id");
                int quantitySold = rs.getInt("quantity");

               
                String updateStockSql = "UPDATE products SET stock = stock + ? WHERE id = ?";
                PreparedStatement stmt2 = conn.prepareStatement(updateStockSql);
                stmt2.setInt(1, quantitySold);  // Increment stock by the sold quantity
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
        String productFilter = productFilterField.getText();
        String dateFilter = (String) dateFilterComboBox.getSelectedItem();
        String sql = "SELECT * FROM sales WHERE salesperson_id = ?";

        // Apply filters
        if (!productFilter.isEmpty()) {
            sql += " AND product_id = ?";
        }
        if (dateFilter.equals("Today")) {
            sql += " AND DATE(sale_date) = CURDATE()";
        } else if (dateFilter.equals("This Week")) {
            sql += " AND YEARWEEK(sale_date, 1) = YEARWEEK(CURDATE(), 1)";
        } else if (dateFilter.equals("This Month")) {
            sql += " AND MONTH(sale_date) = MONTH(CURDATE())";
        }

        // Clear the existing data in the table
        tableModel.setRowCount(0);

        
        try {
            Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, loggedInSalespersonID);

            if (!productFilter.isEmpty()) {
                stmt.setInt(2, Integer.parseInt(productFilter));  
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("total_price"),
                    rs.getDate("sale_date")
                };
                tableModel.addRow(row);
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
