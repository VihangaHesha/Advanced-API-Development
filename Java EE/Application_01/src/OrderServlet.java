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
                String discount = resultSetOrders.getString(4);
                String cash = resultSetOrders.getString(5);
                String balance = resultSetOrders.getString(6);

                JsonObjectBuilder orderBuilder = Json.createObjectBuilder();
                orderBuilder.add("orderId", orderId);
                orderBuilder.add("date", date);
                orderBuilder.add("customerId", customerId);
                orderBuilder.add("discount", discount);
                orderBuilder.add("cash", cash);
                orderBuilder.add("balance", balance);

                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orderDetail WHERE orderId = ?");
                preparedStatement.setString(1, orderId);
                ResultSet resultSetOrderDetail = preparedStatement.executeQuery();

                JsonArrayBuilder orderDetailsArray = Json.createArrayBuilder();
                while (resultSetOrderDetail.next()) {
                    String oId = resultSetOrderDetail.getString(1);
                    String iId = resultSetOrderDetail.getString(2);
                    String qty = resultSetOrderDetail.getString(3);

                    JsonObjectBuilder orderDetailBuilder = Json.createObjectBuilder();
                    orderDetailBuilder.add("orderId", oId);
                    orderDetailBuilder.add("itemCode", iId);
                    orderDetailBuilder.add("qty", qty);

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
        super.doPost(req, resp);
    }
}
