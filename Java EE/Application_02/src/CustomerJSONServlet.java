import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet (urlPatterns = "/customerJson")
public class CustomerJSONServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM customer").executeQuery();

            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);

                JsonObjectBuilder customer = Json.createObjectBuilder();

                customer.add("id", id);
                customer.add("name", name);
                customer.add("address", address);
                arrayBuilder.add(customer.build());
            }

            //we use data as attribute as a best practice!!!
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("data", arrayBuilder);
            response.add("status", HttpServletResponse.SC_OK);
            response.add("message", "Success");

            JsonObject json =  response.build();
            resp.setContentType("application/json");
            resp.getWriter().print(json);
        }catch (Exception e){
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("data", "");
            response.add("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.add("message", e.getMessage());
            JsonObject json =  response.build();
            resp.setContentType("application/json");
            resp.getWriter().print(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            Connection connection = dataSource.getConnection();
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonObject = reader.readObject();

            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String address = jsonObject.getString("address");

            System.out.println(id + " " + name + " " + address);

            //save on DB
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO customer VALUES(?,?,?)");
            pstm.setString(1, id);
            pstm.setString(2, name);
            pstm.setString(3, address);
            pstm.executeUpdate();
            resp.setStatus(HttpServletResponse.SC_CREATED);

            //send the proper response
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("data", jsonObject);
            response.add("status", HttpServletResponse.SC_CREATED);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            response.add("message", "Success");
            resp.setContentType("application/json");
            resp.getWriter().print(response.build());

        } catch (RuntimeException | SQLException e) {
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("data", "");
            error.add("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            error.add("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject json =  error.build();
            resp.setContentType("application/json");
            resp.getWriter().print(json);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Connection connection = dataSource.getConnection();
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonObject = reader.readObject();

            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String address = jsonObject.getString("address");

            PreparedStatement pstm = connection.prepareStatement("UPDATE customer SET name = ?, address = ? WHERE id = ?");
            pstm.setString(1, name);
            pstm.setString(2, address);
            pstm.setString(3, id);
            pstm.executeUpdate();
            resp.setStatus(HttpServletResponse.SC_CREATED);

            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("data", jsonObject);
            response.add("status", HttpServletResponse.SC_CREATED);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            response.add("message", "Success");
            resp.setContentType("application/json");
            resp.getWriter().print(response.build());

        } catch (RuntimeException | SQLException e) {
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("data", "");
            error.add("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            error.add("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject json =  error.build();
            resp.setContentType("application/json");
            resp.getWriter().print(json);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Connection connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}