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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class DataAnalytics extends JFrame {
    private JTextField startDateField, endDateField;
    private JLabel resultLabel;
    private JButton calculateButton,  nowStartButton,  nowEndButton, backButton;
    private JTextArea topProductsArea, topSalespersonArea, lowStockArea;  
    private JDateChooser startDateChooser, endDateChooser;


    private JButton dailyButton, weeklyButton, monthlyButton, printButton;
    private JPanel chartPanel;
    private Connection conn;

    public DataAnalytics() {
        setTitle("Sales Dashboard");
        setSize(1920, 1080);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        
       /* // BACKGROUND IMAGE
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/bgdata.png"));
        JLabel image = new JLabel(i1);
        image.setBounds(0, 0, 1920, 1080); 
        add(image);
        */
        
        // TITLE HEADER
        JLabel titleLabel = new JLabel("Data Analytics Dashboard");
        titleLabel.setBounds(90, 20, 400, 30);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        add(titleLabel);

        // TOTAL Date LABEL
        JPanel datePanel = new JPanel(); 
        datePanel.setBounds(5, 70, 500, 100); 
        datePanel.setBackground(Color.WHITE);  
        datePanel.setLayout(null);  

        // Set rounded corners (keeping border color white so it blends with the background)
        datePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));  

        // Start Date Label
        JLabel startLabel = new JLabel("Start Date (YYYY-MM-DD):");
        startLabel.setBounds(10, 10, 180, 25);
        startLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        datePanel.add(startLabel);

        // JDateChooser for start date
        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        startDateChooser.setBounds(180, 10, 150, 25);  // Adjust placement and size
        datePanel.add(startDateChooser);

        // End Date Label
        JLabel endLabel = new JLabel("End Date (YYYY-MM-DD):");
        endLabel.setBounds(10, 50, 180, 25);
        endLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        datePanel.add(endLabel);

        // JDateChooser for end date
        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        endDateChooser.setBounds(180, 50, 150, 25);  
        datePanel.add(endDateChooser);

  
        calculateButton = new JButton("Calculate Revenue");
        calculateButton.setBounds(340, 50, 150, 30); 
        datePanel.add(calculateButton);

        
        add(datePanel);

        // TOTAL REVENUE LABEL
        JPanel revenuePanel = new JPanel(); 
        revenuePanel.setBounds(5, 170, 500, 50); 
        revenuePanel.setBackground(Color.decode("#4161ef")); 
        revenuePanel.setLayout(new BorderLayout());  
        
        revenuePanel.setBorder(BorderFactory.createLineBorder(Color.decode("#4161ef"), 10));  
      
        resultLabel = new JLabel("Total Revenue: ₱ 0.00");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));  
        resultLabel.setForeground(Color.WHITE);
        revenuePanel.add(resultLabel, BorderLayout.CENTER); 
        
        add(revenuePanel);
 

        // TOP SELLING PRODUCTS PANEL
        JPanel topProductsPanel = new JPanel(); 
        topProductsPanel.setBounds(5, 225, 500, 180);  
        topProductsPanel.setBackground(Color.decode("#1cbeca"));  
        topProductsPanel.setLayout(new BorderLayout());  

        
        topProductsPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#1cbeca"), 10));  
        
        JLabel topProductsLabel = new JLabel("Top-Selling Products:");
        topProductsLabel.setFont(new Font("Segoe UI", Font.BOLD, 20)); 
        topProductsLabel.setForeground(Color.WHITE);
        topProductsPanel.add(topProductsLabel, BorderLayout.NORTH);  
        
        topProductsArea = new JTextArea();
        topProductsArea.setEditable(false);  
        topProductsArea.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
        topProductsArea.setBackground(Color.WHITE);  
        topProductsArea.setForeground(Color.BLACK);  
        topProductsPanel.add(topProductsArea, BorderLayout.CENTER); 
        
        
        add(topProductsPanel);

        // TOP SALESPERSON PANEL
        JPanel topSalespersonPanel = new JPanel(); 
        topSalespersonPanel.setBounds(5, 410, 500, 130);  
        topSalespersonPanel.setBackground(Color.decode("#a2cfef"));  
        topSalespersonPanel.setLayout(new BorderLayout());  

        
        topSalespersonPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#a2cfef"), 10));  

       
        JLabel topSalespersonLabel = new JLabel("Top Salesperson:");
        topSalespersonLabel.setFont(new Font("Segoe UI", Font.BOLD, 20)); 
        topSalespersonLabel.setForeground(Color.WHITE);
        topSalespersonPanel.add(topSalespersonLabel, BorderLayout.NORTH); 

        
        topSalespersonArea = new JTextArea();
        topSalespersonArea.setEditable(false); 
        topSalespersonArea.setFont(new Font("Segoe UI", Font.PLAIN, 18));  
        topSalespersonArea.setBackground(Color.WHITE);  
        topSalespersonArea.setForeground(Color.BLACK);  
        topSalespersonPanel.add(topSalespersonArea, BorderLayout.CENTER);  
        
        add(topSalespersonPanel);
        
         // LOW STOCK PRODUCTS PANEL
        JPanel lowStockPanel = new JPanel(); 
        lowStockPanel.setBounds(5, 545, 500, 120);  
        lowStockPanel.setBackground(Color.decode("#e56868")); 
        lowStockPanel.setLayout(new BorderLayout());  
        
        lowStockPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#e56868"), 10));  
        
        JLabel lowStockLabel = new JLabel("Low Stock Products:");
        lowStockLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));  
        lowStockLabel.setForeground(Color.WHITE); 
        lowStockPanel.add(lowStockLabel, BorderLayout.NORTH);  
        
        lowStockArea = new JTextArea();
        lowStockArea.setEditable(false);  
        lowStockArea.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
        lowStockArea.setBackground(Color.decode("#e56868"));  
        lowStockArea.setForeground(Color.WHITE); 
        lowStockPanel.add(lowStockArea, BorderLayout.CENTER);  
        
        
        add(lowStockPanel);
        

        backButton = new JButton("Back");
        backButton.setBounds(50, 750, 150, 30);
        styleButton(backButton, Color.decode("#87ccfc"), Color.WHITE, Color.decode("#335ed9"));  
        add(backButton);
        
        printButton = new JButton("Print");
        printButton.setBounds(210, 750, 150, 30);
        styleButton(printButton, Color.decode("#90d8b8"), Color.WHITE, Color.decode("#73aa92"));  
        add(printButton);

        dailyButton = new JButton("Daily Sales");
        dailyButton.setBounds(20, 670, 150, 50);
        styleButton(dailyButton, Color.decode("#4161ef"), Color.WHITE, Color.decode("#335ed9"));  
        add(dailyButton);

        weeklyButton = new JButton("Weekly Sales");
        weeklyButton.setBounds(180, 670, 150, 50);
        styleButton(weeklyButton, Color.decode("#4161ef"), Color.WHITE, Color.decode("#335ed9"));  
        add(weeklyButton);

        monthlyButton = new JButton("Monthly Sales");
        monthlyButton.setBounds(340, 670, 150, 50);
        styleButton(monthlyButton, Color.decode("#4161ef"), Color.WHITE, Color.decode("#335ed9"));  
        add(monthlyButton);

        // Panel for displaying chart
        chartPanel = new JPanel();
        chartPanel.setBounds(510, 10, 1000, 1000);
        add(chartPanel);

       

        // Establish database connection
        conn = Database.connect();
        displayLowStockProducts(); 

       

        calculateButton.addActionListener(e -> {
            
            Date startDate = startDateChooser.getDate(); 
            Date endDate = endDateChooser.getDate();    

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = (startDate != null) ? sdf.format(startDate) : null; 
            String endDateStr = (endDate != null) ? sdf.format(endDate) : null;     

            if (startDateStr != null && endDateStr != null) {
                
                double revenue = getTotalSalesRevenue(startDateStr, endDateStr);
                resultLabel.setText("Total Revenue: ₱ " + revenue);
                displayTopSellingProducts(startDateStr, endDateStr);
                displayTopSalesperson(startDateStr, endDateStr);
                displayLowStockProducts();  
            } else {
                
                JOptionPane.showMessageDialog(this, "Please select both start and end dates.");
            }
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
        
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenerateReport();  
            }
        });
    }
    
    public void styleButton(JButton button, Color bgColor, Color textColor, Color hoverBgColor) {
        button.setBackground(bgColor);  
        button.setForeground(textColor);  
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));  
        button.setFocusPainted(false); 
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
        button.setBorderPainted(false); 
        button.setOpaque(true);  
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));  

        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverBgColor);  
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);  
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

    public void GenerateReport() {
        // Get the start and end dates
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = (startDate != null) ? sdf.format(startDate) : null;
        String endDateStr = (endDate != null) ? sdf.format(endDate) : null;

        if (startDateStr == null || endDateStr == null) {
            JOptionPane.showMessageDialog(this, "Please select both start and end dates.");
            return;
        }

        double revenue = getTotalSalesRevenue(startDateStr, endDateStr);

      
        StringBuilder topSellingProducts = new StringBuilder();
        displayTopSellingProductsForReport(startDateStr, endDateStr, topSellingProducts);

      
        StringBuilder topSalesperson = new StringBuilder();
        displayTopSalespersonForReport(startDateStr, endDateStr, topSalesperson);

        StringBuilder lowStockProducts = new StringBuilder();
        displayLowStockProductsForReport(lowStockProducts);

        new GenerateReport(revenue, topSellingProducts.toString(), 0, lowStockProducts.toString(),
                           0, topSalesperson.toString(), 0);  
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
                      .append("\nTotal Sales: ₱ ")
                      .append(rs.getDouble("total_sales"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        topSalespersonArea.setText(result.toString());
    }

   
    public void displayLowStockProducts() {
        String query = "SELECT name, stock FROM products WHERE stock < 10";  
        StringBuilder result = new StringBuilder();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) { 
                result.append("No products are low in stock.");
            } else {
                while (rs.next()) {
                    result.append(rs.getString("name")).append(" - Stock: ")
                          .append(rs.getInt("stock")).append("\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result.append("Error retrieving low-stock products.");
        }

        lowStockArea.setText(result.toString());
    }

    
    public void updateSalesTrend(String filter) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String query = "";

        String todayFormatted = new SimpleDateFormat("MMM dd").format(new Date());
        System.out.println("Today's date formatted: " + todayFormatted);  

        switch (filter) {
            case "daily":
                query = "SELECT DATE_FORMAT(DATE(sale_date), '%b %d') AS day, SUM(total_price) AS total_sales " +
                        "FROM sales WHERE DATE(sale_date) BETWEEN CURDATE() - INTERVAL 6 DAY AND CURDATE() " +  
                        "GROUP BY DATE(sale_date) ORDER BY DATE(sale_date)";
                break;
            case "weekly":
                query = "SELECT DATE_FORMAT(sale_date, 'Week %u, %Y') AS week, SUM(total_price) AS total_sales " +
                        "FROM sales WHERE sale_date >= CURDATE() - INTERVAL 1 MONTH " + 
                        "GROUP BY YEARWEEK(sale_date) ORDER BY YEARWEEK(sale_date)";
                break;
            case "monthly":
                query = "SELECT DATE_FORMAT(sale_date, '%b %Y') AS month, SUM(total_price) AS total_sales " +
                        "FROM sales WHERE sale_date >= CURDATE() - INTERVAL 1 YEAR " + 
                        "GROUP BY YEAR(sale_date), MONTH(sale_date) ORDER BY YEAR(sale_date), MONTH(sale_date)";
                break;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No data returned from the query.");
            }

            dataset.clear();

            boolean todaySalesFound = false;

            while (rs.next()) {
                String dateLabel = rs.getString(1);  
                double totalSales = rs.getDouble("total_sales");

                System.out.println("Date: " + dateLabel + ", Total Sales: ₱" + String.format("%,.2f", totalSales));

                if (dateLabel.equalsIgnoreCase(todayFormatted)) {
                    todaySalesFound = true;
                }

                dataset.addValue(totalSales, dateLabel, "Total Sales");

                System.out.println("Current dataset contents: ");
                for (int i = 0; i < dataset.getRowCount(); i++) {
                    System.out.println("Row " + i + ": " + dataset.getRowKey(i));
                }
            }
            if (!todaySalesFound) {
                dataset.addValue(0, todayFormatted, "Total Sales");
                System.out.println("No sales data for today. Adding bar for today with 0 sales.");
            }
            // Create the chart
            JFreeChart chart = ChartFactory.createBarChart(
                    "Sales Trend (" + filter + ")",
                    "Date",
                    "Total Sales (₱)",
                    dataset
            );
          
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.white);  

            BarRenderer renderer = (BarRenderer) plot.getRenderer();

            renderer.setSeriesPaint(0, new Color(93, 173, 226)); 
            renderer.setItemMargin(0.05); 
            renderer.setMaximumBarWidth(0.10);

            // Show labels 
            StandardCategoryItemLabelGenerator labelGenerator = new StandardCategoryItemLabelGenerator("₱{2}", new DecimalFormat("#,###.00"));
            renderer.setDefaultItemLabelGenerator(labelGenerator);
            renderer.setDefaultItemLabelsVisible(true);
            renderer.setDefaultItemLabelFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
            renderer.setDefaultItemLabelPaint(Color.BLACK);


            chart.getCategoryPlot().setRangePannable(true); 

           
            chartPanel.setPreferredSize(new java.awt.Dimension(600, 350));  
            chartPanel.removeAll();
            chartPanel.add(new ChartPanel(chart));
            chartPanel.revalidate();  
            chartPanel.repaint();     

            add(chartPanel);  
            revalidate();  

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void back() {
        
        dispose();
        new AdminDashboard().setVisible(true);  
    }
    
    private void displayTopSellingProductsForReport(String startDate, String endDate, StringBuilder result) {
        String query = "SELECT p.name, SUM(s.quantity) AS total_sold " +
                       "FROM sales s JOIN products p ON s.product_id = p.id " +
                       "WHERE s.sale_date BETWEEN ? AND ? " +
                       "GROUP BY p.name " +
                       "ORDER BY total_sold DESC " +
                       "LIMIT 5";

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
    }

    private void displayTopSalespersonForReport(String startDate, String endDate, StringBuilder result) {
        String query = "SELECT u.name AS salesperson_name, SUM(s.total_price) AS total_sales " +
                       "FROM sales s " +
                       "JOIN users u ON s.salesperson_id = u.id " +
                       "WHERE s.sale_date BETWEEN ? AND ? " +
                       "GROUP BY u.name " +
                       "ORDER BY total_sales DESC " +
                       "LIMIT 1";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result.append(rs.getString("salesperson_name")).append(" - Total Sales: ")
                      .append(rs.getDouble("total_sales")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void displayLowStockProductsForReport(StringBuilder result) {
         String query = "SELECT name, stock FROM products WHERE stock < 10";  
         
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.append(rs.getString("name")).append(" - Stock: ")
                      .append(rs.getInt("stock")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    


    public static void main(String[] args) {
        new DataAnalytics().setVisible(true);
    }
}
