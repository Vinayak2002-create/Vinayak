import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ResultsServlet")
public class ResultsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Voting Results</title></head><body>");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Load the MySQL JDBC driver
        	Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
        	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/votingsystem", "root", "Msvinayak@2002");

            // Prepare the SQL statement
            pst = conn.prepareStatement("SELECT * FROM candidates");

            // Execute the query
            rs = pst.executeQuery();

            // Display results in HTML format
            out.println("<h2>Voting Results</h2>");
            out.println("<table border='1'><tr><th>Candidate Name</th><th>Votes Received</th></tr>");
            while (rs.next()) {
                String candidateName = rs.getString("name");
                int votesReceived = rs.getInt("votes");
                out.println("<tr><td>" + candidateName + "</td><td>" + votesReceived + "</td></tr>");
            }
            out.println("</table>");

        } catch (ClassNotFoundException | SQLException e) {
            out.println("Error: " + e.getMessage());
        } finally {
            // Close all resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                out.println("Error closing database resources: " + e.getMessage());
            }
        }

        out.println("</body></html>");
        out.close();
    }
}
