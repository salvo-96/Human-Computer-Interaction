package servlets;

import com.google.gson.Gson;
import dao.DAO;
import entita_db.Corso;
import entita_db.Professore;
import entita_db.Ripetizione;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "servlet_index", urlPatterns = "/servlet_index")

public class servlet_index extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        switch (request.getParameter("operazione")) {
            case "get_all_professori": {
                get_all_professori(request, response);
            }
            break;
            case "get_all_corsi": {
                get_all_corsi(request, response);
            }
            break;
            case "get_all_ripetizioni": {
                get_all_ripetizioni(request, response);
            }
            break;
            case "get_professore_from_corso": {
                get_professore_from_corso(request, response);
            }
            break;
            case "get_ripetizioni_from_giorno": {
                get_ripetizioni_from_giorno(request, response);
            }
            break;
            default: {
                System.out.println("Caso non riconosciuto dallo switch del metodo porcess_request della servlet_index");
            }
            break;
        }
    }


    private void get_all_professori(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            DAO.registerDriver();
            out.print(new Gson().toJson(DAO.get_all_professori()));
        } catch (SQLException e) {
            System.out.println("ERRORE nel metodo_get_all_professori della servlet_index: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    private void get_all_corsi(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            DAO.registerDriver();
            out.print(new Gson().toJson(DAO.get_all_corsi()));
        } catch (SQLException e) {
            System.out.println("ERRORE nel metodo get_all_corsi della servlet_index: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    private void get_all_ripetizioni(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            DAO.registerDriver();
            out.print(new Gson().toJson(DAO.get_all_ripetizioni()));
        } catch (SQLException e) {
            System.out.println("ERRORE nel metodo get_all_ripetizion nella servlet_index: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    private void get_professore_from_corso(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            DAO.registerDriver();
            out.print(new Gson().toJson(DAO.get_professore_from_corso(request.getParameter("titolo_corso"))));
        } catch (SQLException e) {
            System.out.println("ERRORE nel metodo get_professore_from_corso nella servlet_index: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    private void get_ripetizioni_from_giorno(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            DAO.registerDriver();
            out.print(new Gson().toJson(DAO.get_ripetizioni_from_giorno(request.getParameter("giorno"))));
        } catch (SQLException e) {
            System.out.println("ERRORE nel metodo get_ripetizioni_from_giorno nella servlet_index: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }
}