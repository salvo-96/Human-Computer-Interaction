package servlets;

import com.google.gson.Gson;
import dao.DAO;
import entita_db.Utente;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "servlet_session", urlPatterns = "/servlet_session")
public class servlet_session extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
        switch (request.getParameter("submit")) {
            case "login": {
                login(request, response);
            }
            break;
            case "logout": {
                logout(request, response);
            }
            break;
            case "register": {
                register(request, response);
            }
            break;
            case "info": {
                info(request, response);
            }
            break;

        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ServletException {
        PrintWriter out = null;
        DAO.registerDriver();
        Utente user = new Utente();
        user.setRuolo(request.getParameter("ruolo"));
        user.setMail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        user.setNome(request.getParameter("nome"));
        user.setCognome(request.getParameter("cognome"));
        if (DAO.add_utente(user)) {
            HttpSession session = request.getSession();
            session.setAttribute("email", user.getMail());
            session.setAttribute("password", user.getPassword());
            String android = request.getParameter("android");
            if (android != null) {
                if (android.equals("true")) {
                    out = response.getWriter();
                    response.setContentType("text/html;charset=UTF-8");
                    out.println(session.getId());
                    DAO.registerDriver();
                    out.print(new Gson().toJson(DAO.get_utente(user.getMail(), user.getPassword())));
                    out.flush();
                    out.close();
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("http://localhost:8080/Progetto-IUM-TWEB/html/index_utente.html"));
            }
        } else {
            out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Utente già registrato');");
            out.println("window.location.href = \"http://localhost:8080/Progetto-IUM-TWEB/html/index.html\"");
            out.println("</script>");
            out.flush();
            out.close();
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        DAO.registerDriver();
        Utente user = DAO.get_utente(email, password);
        String android = request.getParameter("android");
        if (user.getMail() != null) {
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            session.setAttribute("password", password);
            if (user.getRuolo().equals("user")) {
                if (android != null) {
                    if (android.equals("true")) {
                        PrintWriter out = response.getWriter();
                        response.setContentType("text/html;charset=UTF-8");
                        out.println(session.getId());
                        DAO.registerDriver();
                        out.print(new Gson().toJson(DAO.get_utente(email, password)));
                        out.flush();
                        out.close();
                    }
                } else {
                    response.sendRedirect(response.encodeRedirectURL("http://localhost:8080/Progetto-IUM-TWEB/html/index_utente.html"));
                }
            } else {
                if (android != null) {
                    if (android.equals("true")) {
                        PrintWriter out = response.getWriter();
                        response.setContentType("text/html;charset=UTF-8");
                        out.println(session.getId());
                        DAO.registerDriver();
                        out.print(new Gson().toJson(DAO.get_utente(email, password)));
                        out.flush();
                        out.close();
                    }
                } else {
                    response.sendRedirect(response.encodeRedirectURL("http://localhost:8080/Progetto-IUM-TWEB/html/index_admin.html"));
                }
            }
        } else {
            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Email / Password ERRATI ');");
            out.println("window.location.href = \"http://localhost:8080/Progetto-IUM-TWEB/html/index.html\"");
            out.println("</script>");
            out.flush();
            out.close();
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        String android = request.getParameter("android");
        if (android != null) {
            if (android.equals("true")) {
                PrintWriter out = response.getWriter();
                response.setContentType("text/html;charset=UTF-8");
                out.print("null");
                out.flush();
                out.close();
            }
        } else {
            response.sendRedirect("http://localhost:8080/Progetto-IUM-TWEB/html/index.html");
        }
    }

    private void info(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        DAO.registerDriver();
        Utente user = DAO.get_utente((String) session.getAttribute("email"), (String) session.getAttribute("password"));
        out.print(new Gson().toJson(user));
        out.flush();
        out.close();
    }

}