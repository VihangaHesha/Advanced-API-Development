import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.sql.*;

@WebServlet (urlPatterns = "/orders")
public class OrderServlet extends HttpServlet {
    private Connection getCreatedConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/Company",
                "root",
                "Banda@321");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            Connection connection = getCreatedConnection();

            ResultSet resultSetOrders = connection.prepareStatement("SELECT * FROM orders").executeQuery();

            JsonArrayBuilder allOrders = Json.createArrayBuilder();

            while (resultSetOrders.next()) {
                String orderId = resultSetOrders.getString(1);
                String date = resultSetOrders.getString(2);
                String customerId = resultSetOrders.getString(3);

                JsonObjectBuilder orderBuilder = Json.createObjectBuilder();
                orderBuilder.add("orderId", orderId);
                orderBuilder.add("date", date);
                orderBuilder.add("customerId", customerId);

                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orderDetail WHERE orderId = ?");
                preparedStatement.setString(1, orderId);
                ResultSet resultSetOrderDetail = preparedStatement.executeQuery();

                JsonArrayBuilder orderDetailsArray = Json.createArrayBuilder();
                while (resultSetOrderDetail.next()) {
                    String oId = resultSetOrderDetail.getString(1);
                    String iId = resultSetOrderDetail.getString(2);
                    int qty = resultSetOrderDetail.getInt(3);
                    double unitPrice = resultSetOrderDetail.getDouble(4);

                    JsonObjectBuilder orderDetailBuilder = Json.createObjectBuilder();
                    orderDetailBuilder.add("orderId", oId);
                    orderDetailBuilder.add("itemCode", iId);
                    orderDetailBuilder.add("qty", qty);
                    orderDetailBuilder.add("unitPrice", unitPrice);

                    orderDetailsArray.add(orderDetailBuilder.build());
                }

                orderBuilder.add("orderDetails", orderDetailsArray.build());

                allOrders.add(orderBuilder.build());
            }

            resp.getWriter().write(allOrders.build().toString());

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String orderId = req.getParameter("orderId");
        Date date = Date.valueOf(req.getParameter("date"));
        String customerId = req.getParameter("customerId");

        if (orderId == null || date == null || customerId == null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\" : \"orderId, date and customerId are required\"}");
            return;
        }
        try {
            Connection connection = getCreatedConnection();
            connection.prepareStatement("INSERT INTO orders VALUES ('" + orderId + "', '" + date + "', '" + customerId + "')").executeUpdate();
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
