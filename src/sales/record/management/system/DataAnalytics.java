package sales.record.management.system;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.TextAnchor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataAnalytics extends JFrame {
    private JTextField startDateField, endDateField;
    private JLabel resultLabel;
    private JButton calculateButton,  nowStartButton,  nowEndButton, backButton;
    private JTextArea topProductsArea, topSalespersonArea, lowStockArea;  

    private JButton dailyButton, weeklyButton, monthlyButton;
    private JPanel chartPanel;
    private Connection conn;

    public DataAnalytics() {
        setTitle("Sales Dashboard");
        setSize(1920, 1080);  // Increased size for chart space
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Existing components
        JLabel startLabel = new JLabel("Start Date (YYYY-MM-DD):");
        startLabel.setBounds(20, 20, 180, 25);
        add(startLabel);

        startDateField = new JTextField();
        startDateField.setBounds(200, 20, 150, 25);
        add(startDateField);

        nowStartButton = new JButton("Now");
        nowStartButton.setBounds(360, 20, 80, 25);
        add(nowStartButton);

        JLabel endLabel = new JLabel("End Date (YYYY-MM-DD):");
        endLabel.setBounds(20, 60, 180, 25);
        add(endLabel);

        endDateField = new JTextField();
        endDateField.setBounds(200, 60, 150, 25);
        add(endDateField);

        nowEndButton = new JButton("Now");
        nowEndButton.setBounds(360, 60, 80, 25);
        add(nowEndButton);

        calculateButton = new JButton("Calculate Revenue");
        calculateButton.setBounds(140, 100, 200, 30);
        add(calculateButton);

        resultLabel = new JLabel("Total Revenue: Php 0.00");
        resultLabel.setBounds(140, 140, 250, 25);
        add(resultLabel);

        JLabel topProductsLabel = new JLabel("Top-Selling Products:");
        topProductsLabel.setBounds(20, 180, 200, 25);
        add(topProductsLabel);

        topProductsArea = new JTextArea();
        topProductsArea.setBounds(20, 210, 550, 100);
        topProductsArea.setEditable(false);
        add(topProductsArea);

        JLabel topSalespersonLabel = new JLabel("Top Salesperson:");
        topSalespersonLabel.setBounds(20, 320, 200, 25);
        add(topSalespersonLabel);

        topSalespersonArea = new JTextArea();
        topSalespersonArea.setBounds(20, 350, 550, 50);
        topSalespersonArea.setEditable(false);
        add(topSalespersonArea);

        backButton = new JButton("Back");
        backButton.setBounds(1200, 20, 150, 30);
        add(backButton);
        
        // Add buttons for daily, weekly, monthly sales trends
        dailyButton = new JButton("Daily Sales");
        dailyButton.setBounds(20, 420, 120, 30);
        add(dailyButton);

        weeklyButton = new JButton("Weekly Sales");
        weeklyButton.setBounds(160, 420, 120, 30);
        add(weeklyButton);

        monthlyButton = new JButton("Monthly Sales");
        monthlyButton.setBounds(300, 420, 120, 30);
        add(monthlyButton);

        // Panel for displaying chart
        chartPanel = new JPanel();
        chartPanel.setBounds(20, 460, 740, 300);
        add(chartPanel);

        // Inventory Insights Section
        JLabel lowStockLabel = new JLabel("Low Stock Products:");
        lowStockLabel.setBounds(650, 170, 200, 25);
        add(lowStockLabel);

        lowStockArea = new JTextArea();
        lowStockArea.setBounds(800, 170, 550, 100);  
        lowStockArea.setEditable(false);
        lowStockArea.setBackground(Color.WHITE);  
        lowStockArea.setForeground(Color.RED);  // Set text color to white
        lowStockArea.setFont(new Font("Arial", Font.BOLD, 14));  // Make text bold
        add(lowStockArea);

        // Establish database connection
        conn = Database.connect();
        displayLowStockProducts(); 

        nowStartButton.addActionListener(e -> startDateField.setText(getCurrentDate()));
        nowEndButton.addActionListener(e -> endDateField.setText(getCurrentDate()));

        calculateButton.addActionListener(e -> {
            String startDate = startDateField.getText();
            String endDate = endDateField.getText();
            double revenue = getTotalSalesRevenue(startDate, endDate);
            resultLabel.setText("Total Revenue: Php " + revenue);
            displayTopSellingProducts(startDate, endDate);
            displayTopSalesperson(startDate, endDate);
            displayLowStockProducts();  // Display low stock products
        });

        // Add action listeners for buttons
        dailyButton.addActionListener(e -> updateSalesTrend("daily"));
        weeklyButton.addActionListener(e -> updateSalesTrend("weekly"));
        monthlyButton.addActionListener(e -> updateSalesTrend("monthly"));
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                back();  
            }
        });
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public double getTotalSalesRevenue(String startDate, String endDate) {
        double totalRevenue = 0;
        String query = "SELECT SUM(total_price) FROM sales WHERE sale_date BETWEEN ? AND ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalRevenue = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRevenue;
    }

    public void displayTopSellingProducts(String startDate, String endDate) {
        String query = "SELECT p.name, SUM(s.quantity) AS total_sold " +
                       "FROM sales s JOIN products p ON s.product_id = p.id " +
                       "WHERE s.sale_date BETWEEN ? AND ? " +
                       "GROUP BY p.name " +
                       "ORDER BY total_sold DESC " +
                       "LIMIT 5";

        StringBuilder result = new StringBuilder();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.append(rs.getString("name")).append(" - Sold: ")
                      .append(rs.getInt("total_sold")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        topProductsArea.setText(result.toString());
    }

    public void displayTopSalesperson(String startDate, String endDate) {
        String query = "SELECT u.name AS salesperson_name, SUM(s.total_price) AS total_sales " +
                       "FROM sales s " +
                       "JOIN users u ON s.salesperson_id = u.id " +
                       "WHERE s.sale_date BETWEEN ? AND ? " +
                       "GROUP BY u.name " +
                       "ORDER BY total_sales DESC " +
                       "LIMIT 1";

        StringBuilder result = new StringBuilder();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result.append("Name: ").append(rs.getString("salesperson_name"))
                      .append("\nTotal Sales: Php ")
                      .append(rs.getDouble("total_sales"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        topSalespersonArea.setText(result.toString());
    }

    // New method to display low stock products
    public void displayLowStockProducts() {
        String query = "SELECT name, stock FROM products WHERE stock < 10";  

        StringBuilder result = new StringBuilder();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.append(rs.getString("name")).append(" - Stock: ")
                      .append(rs.getInt("stock")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        lowStockArea.setText(result.toString());
    }

    
        public void updateSalesTrend(String filter) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            String query = "";
            String category = "Total Sales";
            String dateField = "sale_date";
            String dateFormat = "";

            switch (filter) {
                case "daily":
                    query = "SELECT DATE(sale_date) AS day, SUM(total_price) AS total_sales FROM sales GROUP BY DATE(sale_date)";
                    break;
                case "weekly":
                    query = "SELECT YEARWEEK(sale_date) AS week, SUM(total_price) AS total_sales FROM sales GROUP BY YEARWEEK(sale_date)";
                    break;
                case "monthly":
                    query = "SELECT MONTH(sale_date) AS month, SUM(total_price) AS total_sales FROM sales GROUP BY MONTH(sale_date)";
                    break;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                ResultSet rs = pstmt.executeQuery();

                // Check if data is available
                if (!rs.isBeforeFirst()) {
                    System.out.println("No data returned from the query.");
                }

                while (rs.next()) {
                    String dateLabel = rs.getString(1);  // Day, Week, or Month depending on filter
                    double totalSales = rs.getDouble("total_sales");
                    dataset.addValue(totalSales, category, dateLabel);
                    System.out.println("Date: " + dateLabel + ", Total Sales: " + totalSales); // Debug output
                }

                // Create the chart
                JFreeChart chart = ChartFactory.createBarChart(
                        "Sales Trend (" + filter + ")",
                        "Date",
                        "Total Sales",
                        dataset
                );

                // Get the plot and renderer
                CategoryPlot plot = chart.getCategoryPlot();
                BarRenderer renderer = (BarRenderer) plot.getRenderer();

                // Create and configure item label generator
                StandardCategoryItemLabelGenerator itemLabelGenerator = new StandardCategoryItemLabelGenerator();
                renderer.setDefaultItemLabelGenerator(itemLabelGenerator);

                // Enable item labels
                renderer.setDefaultItemLabelsVisible(true);

                // Customize item label font and color
                renderer.setDefaultItemLabelFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
                renderer.setDefaultItemLabelPaint(java.awt.Color.BLACK);

                // Update the chart panel
                chartPanel.removeAll();
                chartPanel.add(new ChartPanel(chart));
                chartPanel.revalidate();
                chartPanel.repaint();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        private void back() {
        
        dispose();
        new AdminDashboard().setVisible(true);  
    }


    public static void main(String[] args) {
        new DataAnalytics().setVisible(true);
    }
}
