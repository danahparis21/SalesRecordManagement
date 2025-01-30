
package sales.record.management.system;
import java.sql.Connection;


public class Main {
    public static void main(String[] args) {
        Connection conn = Database.connect();
        if (conn != null) {
            System.out.println("Successfully connected to the database.");
        }
    }
}
