import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
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

            ResultSet resultSet = connection.prepareStatement("SELECT * FROM item").executeQuery();

            JsonArrayBuilder allItems = Json.createArrayBuilder();

            while (resultSet.next()) {
                String itemCode = resultSet.getString(1);
                String name = resultSet.getString(2);
                String qty = resultSet.getString(3);
                String unitPrice = resultSet.getString(4);

                JsonObjectBuilder itemBuilder = Json.createObjectBuilder();
                itemBuilder.add("itemCode", itemCode);
                itemBuilder.add("name", name);
                itemBuilder.add("qty", qty);
                itemBuilder.add("unitPrice", unitPrice);
                allItems.add(itemBuilder.build());
            }

            resp.getWriter().write(allItems.build().toString());

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String itemCode = req.getParameter("itemCode");
        String name = req.getParameter("name");
        String qty = req.getParameter("qty");
        String unitPrice = req.getParameter("unitPrice");

        if (itemCode == null || name == null || qty == null || unitPrice == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\" : \"itemCode,name, qty and unitPrice are required\"}");
            return;
        }

        try {
            Connection connection = getCreatedConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO item VALUES (?, ?, ?, ?)");

            preparedStatement.setString(1, itemCode);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, Integer.parseInt(qty));
            preparedStatement.setBigDecimal(4, new BigDecimal(unitPrice));

            preparedStatement.executeUpdate();

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String itemCode = req.getParameter("itemCode");
        String name = req.getParameter("name");
        String qty = req.getParameter("qty");
        String unitPrice = req.getParameter("unitPrice");

        if (itemCode == null || name == null || qty == null || unitPrice == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\" : \"itemCode,name, qty and unitPrice are required\"}");
            return;
        }

        try {
            Connection connection = getCreatedConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE item SET name = ?, qty = ?, unitPrice = ? WHERE itemCode = ?");

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, Integer.parseInt(qty));
            preparedStatement.setBigDecimal(3, new BigDecimal(unitPrice));
            preparedStatement.setString(4, itemCode);

            preparedStatement.executeUpdate();

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String itemCode = req.getParameter("itemCode");

        if (itemCode == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\" : \"itemCode required\"}");
            return;
        }

        try {
            Connection connection = getCreatedConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM item WHERE itemCode = ?");

            preparedStatement.setString(1, itemCode);

            preparedStatement.executeUpdate();

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
