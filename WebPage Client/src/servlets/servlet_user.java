package servlets;

import com.google.gson.Gson;
import dao.DAO;
import entita_db.Ripetizione;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "servlet_user", urlPatterns = "/servlet_user")
public class servlet_user extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (request.getParameter("operazione")) {
            case "prenota": {
                prenota(request, response);

            }
            break;
            case "get_ripetizioni_completate": {
                get_ripetizioni_completate(request, response);
            }
            break;
            case "get_ripetizioni_annullate": {
                get_ripetizioni_annullate(request, response);
            }
            break;
            case "get_ripetizioni_in_corso": {
                get_ripetizioni_in_corso(request, response);
            }
            break;
            case "disdici": {
                disdici(request, response);
            }
            break;
            case "completata": {
                completata(request, response);
            }
            break;
        }
    }

    private void prenota(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);//////////////////
            response.setContentType("text/html;charset=UTF-8");
        try {
            DAO.registerDriver();
            out.print(new Gson().toJson(DAO.prenota(
                    request.getParameter("titolo_corso"),
                    Integer.parseInt(request.getParameter("id_professore")),
                    request.getParameter("data"),
                    request.getParameter("ora"),
                    (String) session.getAttribute("email"))));
        } catch (SQLException e) {
            System.out.println("ERRORE nel metodo prenota nella servlet_utente: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    private void get_ripetizioni_completate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            DAO.registerDriver();
            out.print(new Gson().toJson(DAO.get_ripetizione_completate((String) request.getSession().getAttribute("email"))));
        } catch (SQLException e) {
            System.out.println("ERRORE nel metodo get_ripetizioni_completate nella servlet_utente: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    private void get_ripetizioni_annullate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            DAO.registerDriver();
            out.print(new Gson().toJson(DAO.get_ripetizione_annullate((String) request.getSession().getAttribute("email"))));
        } catch (SQLException e) {
            System.out.println("ERRORE nel metodo get_ripetizioni_completate nella servlet_utente: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    private void get_ripetizioni_in_corso(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            DAO.registerDriver();
            out.print(new Gson().toJson(DAO.get_ripetizione_in_corso((String) request.getSession().getAttribute("email"))));
        } catch (SQLException e) {
            System.out.println("ERRORE nel metodo get_ripetizioni_completate nella servlet_utente: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    private void disdici(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            DAO.registerDriver();
            out.print(new Gson().toJson(DAO.disdici(request.getParameter("data"), request.getParameter("ora"))));
        } catch (SQLException e) {
            System.out.println("ERRORE nel metodo disdici nella servlet_utente: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    private void completata(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            DAO.registerDriver();
            out.print(new Gson().toJson(DAO.completata(request.getParameter("data"), request.getParameter("ora"))));
        } catch (SQLException e) {
            System.out.println("ERRORE nel metodo completata nella servlet_utente: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }
}
