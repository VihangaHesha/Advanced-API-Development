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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String age = req.getParameter("age");

        if (id == null || id.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("\"{\"error\": \"Id is required!\"}");
        }
        try{
            StudentDTO studentDTO = findById(Integer.parseInt(id));
                if (studentDTO == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\": \"Student not found!\"}");
                }else {
                    studentDTO.setName(name);
                    studentDTO.setEmail(email);
                    studentDTO.setAge(Integer.parseInt(age));
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write("{\"message\": \"Student updated successfully\"}");
                }
            }catch (NumberFormatException e){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Invalid Id\"}");
            }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        if (id == null || id.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Id is required!\"}");
        } else {
            try {
                int idInteger = Integer.parseInt(id);
                StudentDTO studentDTO = findById(idInteger);
                if (studentDTO == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\": \"Student not found!\"}");
                } else {
                    studentDTOList.remove(studentDTO);
                    resp.getWriter().write("{ \"message\": \"Student Deleted Successfully\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{ \"error\": \"Invalid Id\"}");
            }
        }
    }

    private StudentDTO findById(int id) {
    for (StudentDTO studentDTO : studentDTOList){
        if (studentDTO.getId() == id){
            return studentDTO;
        }
    }
        return null;
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

        if (name.isEmpty() || name == null || email.isEmpty() || email == null || ageString == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid Input.Name,Emall and Age cannot be empty\"}");

        }else {
            StudentDTO studentDTO = new StudentDTO(id, name, email, age);
            studentDTOList.add(studentDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        }
    }
}