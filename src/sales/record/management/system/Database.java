package sales.record.management.system;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3307/sales_management";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 
    
    public static Connection connect() {
        Connection conn = null;
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
        
    
    }
}
