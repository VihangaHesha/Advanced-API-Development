import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;
@WebServlet (urlPatterns = "/orderDetails")
public class OrderDetailsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        JsonObjectBuilder response = Json.createObjectBuilder();

        try {
            // Parse request body
            JsonReader jsonReader = Json.createReader(req.getReader());
            JsonObject orderData = jsonReader.readObject();

            // Get connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = getConnection("jdbc:mysql://localhost:3306/Company",
                    "root",
                    "Banda@321");
            connection.setAutoCommit(false);

            try {
                // Insert order
                PreparedStatement orderStmt = connection.prepareStatement(
                        "INSERT INTO orders (orderId, date, customerId) VALUES (?, ?, ?)"
                );
                orderStmt.setString(1, orderData.getString("orderId"));
                orderStmt.setString(2, orderData.getString("date"));
                orderStmt.setString(3, orderData.getString("customerId"));
                orderStmt.executeUpdate();

                // Insert order details and update item quantities
                JsonArray items = orderData.getJsonArray("items");
                PreparedStatement detailStmt = connection.prepareStatement(
                        "INSERT INTO orderDetail (orderId, itemCode, qty,unitPrice) VALUES (?, ?, ?,?)"
                );
                PreparedStatement updateItemStmt = connection.prepareStatement(
                        "UPDATE item SET qty = qty - ? WHERE itemCode = ?"
                );

                for (JsonValue item : items) {
                    JsonObject orderDetail = item.asJsonObject();
                    String itemCode = orderDetail.getString("itemCode");
                    int qty = orderDetail.getInt("qty");

                    // Insert order detail
                    detailStmt.setString(1, orderData.getString("orderId"));
                    detailStmt.setString(2, itemCode);
                    detailStmt.setInt(3, qty);
                    detailStmt.executeUpdate();

                    // Update item quantity
                    updateItemStmt.setInt(1, qty);
                    updateItemStmt.setString(2, itemCode);
                    updateItemStmt.executeUpdate();
                }

                connection.commit();
                response.add("status", "success");
                response.add("message", "Order placed successfully");

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (Exception e) {
            response.add("status", "error");
            response.add("message", e.getMessage());
        }

        resp.getWriter().write(response.build().toString());
    }
}
