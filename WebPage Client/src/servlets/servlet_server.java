package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "servlet_server", urlPatterns = "/servlet_server")
public class servlet_server extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        switch (request.getParameter("servlet")) {
            case "index": {
                getServletContext().getNamedDispatcher("servlet_index").forward(request, response);
            }
            break;
            case "admin": {
                getServletContext().getNamedDispatcher("servlet_admin").forward(request, response);
            }
            break;
            case "session": {
                getServletContext().getNamedDispatcher("servlet_session").forward(request, response);
            }
            break;
            case "user": {
                getServletContext().getNamedDispatcher("servlet_user").forward(request, response);
            }
            break;
            case "render_index": {
                PrintWriter out = response.getWriter();
                response.setContentType("text/html;charset=UTF-8");
                out.print("http://localhost:8080/Progetto-IUM-TWEB/html/index.html");
                out.flush();
                out.close();
            }
            break;
            default: {
                System.out.println("Caso non riconosciuto dallo switch del metodo processRequest della servler_server");
            }
            break;
        }
    }
}
