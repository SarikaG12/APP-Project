import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewStudents {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                 "jdbc:mysql://localhost:3306/studentdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                 "root",
                 "Ram@2001"
);


            String query = "SELECT * FROM students";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("------------------------------------------------------------");
            System.out.printf("%-5s %-15s %-10s %-25s %-12s %-8s %-10s%n",
                    "ID", "Name", "Roll", "Email", "Phone", "Gender", "Course");
            System.out.println("------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-5d %-15s %-10s %-25s %-12s %-8s %-10s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("roll"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("course"));
            }

            System.out.println("------------------------------------------------------------");
            con.close();

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
