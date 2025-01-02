import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet (urlPatterns = "/lifecycle")
public class LifeCycleServlet extends HttpServlet {
    //The Servlet Initialized here
    public LifeCycleServlet(){
        System.out.println("Inside Constructor");
    }

    //This will be called after the init method according to the servlet
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Inside doGet");
    }

    //This brings the servlet features like req and resp handling to the servlet
    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("Inside init");
    }

    //This will be called when the server is stopped
    @Override
    public void destroy() {
        System.out.println("Inside destroy");
    }
}
