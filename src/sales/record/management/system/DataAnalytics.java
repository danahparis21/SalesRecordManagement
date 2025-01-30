package sales.record.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

class DataAnalytics extends JFrame {
    
    // Database connection
    private Connection conn;

    public DataAnalytics() {
        setTitle("Sales Analytics");
        setSize(1920, 1800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
      
        getContentPane().setBackground(Color.WHITE);

        conn = Database.connect();

 
        JTabbedPane tabbedPane = new JTabbedPane();
        


        // Total Sales Revenue
        JPanel totalSalesPanel = new JPanel();
        totalSalesPanel.add(new JLabel("Total Sales Revenue: " + getTotalSalesRevenue("2024-01-01", "2024-12-31")));
        tabbedPane.addTab("Total Sales", totalSalesPanel);

        //  Top-Selling Products
        JPanel topSellingProductsPanel = new JPanel();
        topSellingProductsPanel.add(new JLabel("Top-Selling Products: " + getTopSellingProducts()));
        tabbedPane.addTab("Top Products", topSellingProductsPanel);

        // Sales Trends 
        JPanel salesTrendsPanel = createSalesTrendChart("2024-01-01", "2024-12-31");
        tabbedPane.addTab("Sales Trends", salesTrendsPanel);
        
        salesTrendsPanel.setVisible(true); 
        

        // Salesperson Performance
        JPanel salespersonPerformancePanel = new JPanel();
        salespersonPerformancePanel.add(new JLabel("Salesperson Performance: " + getSalespersonPerformance()));
        tabbedPane.addTab("Salesperson Performance", salespersonPerformancePanel);
        

        // Inventory Insights (Low Stock)
        JPanel inventoryInsightsPanel = new JPanel();
        inventoryInsightsPanel.add(new JLabel("Low Stock Products: " + getLowStockProducts()));
        tabbedPane.addTab("Inventory Insights", inventoryInsightsPanel);
        


        
        add(tabbedPane);
    }

    // calculate total sales revenue for a specific date range
    public double getTotalSalesRevenue(String startDate, String endDate) {
        double totalRevenue = 0;
        String query = "SELECT SUM(total_price) FROM sales WHERE sale_date BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalRevenue = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }

    // top-selling products 
    public String getTopSellingProducts() {
        StringBuilder products = new StringBuilder();
        String query = "SELECT product_id, SUM(quantity) AS total_sold FROM sales GROUP BY product_id ORDER BY total_sold DESC LIMIT 5";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.append("Product ID: ").append(rs.getInt("product_id"))
                        .append(", Quantity Sold: ").append(rs.getInt("total_sold")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products.toString();
    }

    //  sales trend chart 
    public JPanel createSalesTrendChart(String startDate, String endDate) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String query = "SELECT sale_date, SUM(total_price) AS total_sales FROM sales WHERE sale_date BETWEEN ? AND ? GROUP BY sale_date";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String saleDate = rs.getString("sale_date");
                double totalSales = rs.getDouble("total_sales");
                dataset.addValue(totalSales, "Sales", saleDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Sales Trend",
                "Date", "Revenue",
                dataset
        );
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.add(chartPanel, BorderLayout.CENTER);

        return chartContainer;
    }


    //  track salesperson performance
    public String getSalespersonPerformance() {
        StringBuilder performance = new StringBuilder();
        String query = "SELECT salesperson_id, SUM(total_price) AS total_sales FROM sales GROUP BY salesperson_id";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                performance.append("Salesperson ID: ").append(rs.getInt("salesperson_id"))
                        .append(", Total Sales: ").append(rs.getDouble("total_sales")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return performance.toString();
    }

    //low stock products
    public String getLowStockProducts() {
        StringBuilder lowStockProducts = new StringBuilder();
        String query = "SELECT id, name, stock FROM products WHERE stock < 10";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lowStockProducts.append("Product ID: ").append(rs.getInt("id"))
                .append(", Product Name: ").append(rs.getString("name"))
                .append(", Stock: ").append(rs.getInt("stock")).append("\n");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lowStockProducts.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DataAnalytics().setVisible(true);
            }
        });
    }
}
