package servlets;

import com.google.gson.Gson;
import dao.DAO;
import entita_db.Corso;
import entita_db.Professore;
import entita_db.Ripetizione;
import entita_db.Utente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "servlet_admin", urlPatterns = "/servlet_admin")
public class servlet_admin extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            processRequest(request, response);
        } catch (SQLException e) {
            System.out.println("Errore nel metodo doPost della servlet_admin" + e.getMessage());
            System.out.println("StackTrace: \n");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            processRequest(request, response);
        } catch (SQLException e) {
            System.out.println("Errore nel metodo doGet della servlet_admin" + e.getMessage());
            System.out.println("StackTrace: \n");
            e.printStackTrace();
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        switch (request.getParameter("operazione")) {
            case "add_corso": {
                add_corso(request, response);
            }
            break;
            case "add_professore": {
                add_professore(request, response);
            }
            break;
            case "remove_professore": {
                remove_professore(request, response);
            }
            break;
            case "remove_corso": {
                remove_corso(request, response);
            }
            break;
            case "add_insegnamento": {
                add_insegnamento(request, response);
            }
            break;
            case "get_corso_from_insegna": {
                get_corso_from_insegna(request, response);
            }
            break;
            case "remove_insegnamento": {
                remove_insegnamento(request, response);
            }
            break;
        }
    }

    private void add_corso(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        DAO.registerDriver();
        out.print(new Gson().toJson(DAO.add_corso(request.getParameter("titolo_corso"))));
        out.flush();
        out.close();
    }

    private void remove_corso(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        DAO.registerDriver();
        out.print(new Gson().toJson(DAO.remove_corso(request.getParameter("titolo"))));
        out.flush();
        out.close();
    }

    private void add_professore(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        DAO.registerDriver();
        out.print(new Gson().toJson(DAO.add_professore(request.getParameter("nome"), request.getParameter("cognome"))));
        out.flush();
        out.close();
    }

    private void remove_professore(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        DAO.registerDriver();
        out.print(new Gson().toJson(DAO.remove_prof(Integer.parseInt(request.getParameter("id")))));
        out.flush();
        out.close();
    }

    private void add_insegnamento(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        DAO.registerDriver();
        out.print(new Gson().toJson(DAO.add_insegnamento(Integer.parseInt(request.getParameter("id")), request.getParameter("titolo"))));
        out.flush();
        out.close();
    }

    private void get_corso_from_insegna(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        DAO.registerDriver();
        out.print(new Gson().toJson(DAO.get_corso_from_insegna(Integer.parseInt(request.getParameter("id")))));
        out.flush();
        out.close();
    }

    private void remove_insegnamento(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        DAO.registerDriver();
        out.print(new Gson().toJson(DAO.remove_insegnamento(Integer.parseInt(request.getParameter("id")), request.getParameter("titolo"))));
        out.flush();
        out.close();
    }
}
