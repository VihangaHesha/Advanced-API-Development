import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet (urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    private final List<CustomerDTO> customerDTOList = new ArrayList<>();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");

        if (id == null || name == null || address == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid Input. ID Or Name Or Address cannot be empty\"}");
        }else {
        /*CustomerDTO customerDTO = new CustomerDTO(id, name, address);
        customerDTOList.add(customerDTO);*/

            try {
                Connection connection = getCreatedConnection();

                PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer VALUES (?, ?, ?)");
                pstm.setString(1, id);
                pstm.setString(2, name);
                pstm.setString(3, address);
                pstm.executeUpdate();

                resp.setStatus(HttpServletResponse.SC_CREATED);

            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private Connection getCreatedConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/Company",
                "root",
                "Banda@321");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {

        String id = req.getParameter("id");
       try{
           if (id == null) {
               resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
               resp.getWriter().write("{\"error\": \"Invalid Input. ID cannot be empty\"}");
           } else {
               CustomerDTO customerDTO = findById(id);
               if (customerDTO == null) {
                   resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
               } else {
                   try {
                       Connection connection = getCreatedConnection();
                       PreparedStatement pstm = connection.prepareStatement("DELETE FROM Customer WHERE id = ?");
                       pstm.setString(1, id);
                       pstm.executeUpdate();
                       resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                   }catch (SQLException | ClassNotFoundException e) {
                       throw new RuntimeException(e);
                   }
               }
           }
       }catch (IOException e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");

        try{
        CustomerDTO customerDTO = findById(id);
        if (customerDTO == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            Connection connection = getCreatedConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE Customer SET name = ?, address = ? WHERE id = ?");
            pstm.setString(1, name);
            pstm.setString(2, address);
            pstm.setString(3, id);
            pstm.executeUpdate();
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\": \"Customer updated successfully\"}");
        }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private CustomerDTO findById(String id) {
        for (CustomerDTO customerDTO : customerDTOList) {
            if (customerDTO.getId().equals(id)) {
                return customerDTO;
            }
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       resp.setContentType("application/json");
       customerDTOList.clear();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Company",
                    "root",
                    "Banda@321");

            ResultSet resultSet = connection.prepareStatement("SELECT * FROM Customer").executeQuery();

            //create a json array
            JsonArrayBuilder customerArrays = Json.createArrayBuilder();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);

                //create a single jason obj
                JsonObjectBuilder customer = Json.createObjectBuilder();
                customer.add("id", id);
                customer.add("name", name);
                customer.add("address", address);
                customerArrays.add(customer);

                System.out.println("ID: " + id + "| Name: " + name + "| Address: " + address);

            }

            resp.getWriter().write(customerArrays.build().toString());


            /*StringBuilder customerJsonList = new StringBuilder("[");
            for (CustomerDTO customerDTO : customerDTOList) {

                String customerJson = String.format("{\"id\":\"%s\", \"name\":\"%s\", \"address\":\"%s\"}",
                        customerDTO.getId(),
                        customerDTO.getName(),
                        customerDTO.getAddress());

                customerJsonList.append(customerJson);

                if (customerDTOList.indexOf(customerDTO) != customerDTOList.size()-1) {
                    customerJsonList.append(",");
                }
            }
            customerJsonList.append("]");
            String customerJson = customerJsonList.toString();
            PrintWriter out = resp.getWriter();
            out.println(customerJson);*/


        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
}