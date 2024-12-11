import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("<html>" +
                "<head>" +
                "<title>Test Application</title>" +
                "<style>" +
                "p{" +
                "color:red;" +
                "background-color:Yellow;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>Hello HTTP Servlet Test </h1>" +
                "<p>This is an example page for HTTP Servlet.This page showcases some random stuff that came to my mind while doing this work</p>" +
                "</body>" +
                "</html>");
    }
}
