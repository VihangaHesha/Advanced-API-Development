import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet (urlPatterns = "/student")
public class StudentServlet extends HttpServlet {
    private final List<StudentDTO> studentDTOList = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        studentDTOList.add(new StudentDTO(1,"Vihanga","heshanvihanga321@gmail.com",21));
        studentDTOList.add(new StudentDTO(2,"Kamal","KamalKumra@gmail.com",20));
        studentDTOList.add(new StudentDTO(3,"Malsha","MalshaSilva21@gmail.com",21));
        studentDTOList.add(new StudentDTO(4,"Vihanga","NimalFernando@gmail.com",19));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        resp.setContentType("application/json");

        StringBuilder studentJsonList = new StringBuilder("[");
        for (StudentDTO studentDTO : studentDTOList) {

            String studentJson = String.format("{\"id\": %d, \"name\": \"%s\", \"email\": \"%s\", \"age\": %d}",
                    studentDTO.getId(), studentDTO.getName(), studentDTO.getEmail(), studentDTO.getAge());

            studentJsonList.append(studentJson);

            if (studentDTOList.indexOf(studentDTO) != studentDTOList.size() - 1) {
                studentJsonList.append(",");
            }
        }
        studentJsonList.append("]");
        String stduentJsonListString = studentJsonList.toString();

        out.println(stduentJsonListString);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String ageString = req.getParameter("age");//Convert this into Integer

        int age,id;
        try{
            age = Integer.parseInt(ageString);
            id = studentDTOList.size() + 1;
        }catch (NumberFormatException e){
            System.out.println("Invalid Age!!");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid Age\"}");
            return;
        }

        StudentDTO studentDTO = new StudentDTO(id, name, email, age);
        studentDTOList.add(studentDTO);
    }
}