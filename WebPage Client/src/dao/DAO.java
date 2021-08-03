package dao;

import entita_db.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {

    private static final String url = "jdbc:mysql://localhost:8889/iumtweb_project";
    private static final String user = "Andrea";
    private static final String password = "root";

    public static void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            System.out.println("Registrazione driver fallita. Errore: " + e.getMessage());
        }
    }

    public static List<Professore> get_all_professori() throws SQLException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        List<Professore> professori = null;
        try {
            professori = new ArrayList<Professore>();
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }

            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM professore");
            while (rs.next()) {
                Professore professore = new Professore();
                professore.setId(rs.getInt("id"));
                professore.setNome(rs.getString("nome"));
                professore.setCognome(rs.getString("cognome"));
                professori.add(professore);
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo get_all_prof. Errore: " + e.getMessage());
        } finally {
            rs.close();
            st.close();
            conn.close();
        }
        return professori;
    }

    public static Professore get_professore(String id_professore) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Professore professore = null;
        try {
            professore = new Professore();
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "SELECT * FROM professore WHERE id = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, id_professore);
            rs = st.executeQuery();
            if (rs.next()) {
                professore.setId(rs.getInt("id"));
                professore.setNome(rs.getString("nome"));
                professore.setCognome(rs.getString("cognome"));
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo get_professore. Errore: " + e.getMessage());
        } finally {
            rs.close();
            st.close();
            conn.close();
        }
        return professore;
    }

    public static List<Corso> get_all_corsi() throws SQLException {
        Connection conn = null;
        Statement st = null;
        List<Corso> out = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }

            st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM corso");
            while (rs.next()) {
                Corso corso = new Corso();
                corso.setTitolo(rs.getString("titolo"));
                out.add(corso);
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo get_all_corsi. Errore: " + e.getMessage());
        } finally {
            st.close();
            conn.close();
        }
        return out;
    }

    public static ArrayList<Ripetizione> get_all_ripetizioni() throws SQLException {
        Connection conn = null;
        Statement st = null;
        ArrayList<Ripetizione> out = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }

            st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ripetizione");
            while (rs.next()) {
                Ripetizione ripetizione = new Ripetizione();
                ripetizione.setId_professore(rs.getString("id_professore"));
                ripetizione.setData(rs.getString("data"));
                ripetizione.setOra(rs.getString("ora"));
                ripetizione.setTitolo_corso(rs.getString("titolo_corso"));
                ripetizione.setMail_utente(rs.getString("mail_utente"));
                out.add(ripetizione);
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo get_all_ripetizioni. Errore: " + e.getMessage());
        } finally {
            st.close();
            conn.close();
        }
        return out;
    }

    public static boolean add_utente(Utente utente) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        boolean inserito = false;
        int n = 0;
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "INSERT INTO utente (mail, password, ruolo, nome, cognome) VALUES (?, ?, ?, ?, ?);";
            st = conn.prepareStatement(sql);
            st.setString(1, utente.getMail());
            st.setString(2, utente.getPassword());
            st.setString(3, utente.getRuolo());
            st.setString(4, utente.getNome());
            st.setString(5, utente.getCognome());
            n = st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo add_utente. Errore: " + e.getMessage());
        } finally {
            if (n != 0) {
                inserito = true;
            }
            st.close();
            conn.close();
            return inserito;
        }
    }

    public static Utente get_utente(String email, String psw) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Utente u = new Utente();
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "SELECT * FROM utente WHERE utente.mail = ? AND utente.password = ?;";
            st = conn.prepareStatement(sql);
            st.setString(1, email);
            st.setString(2, psw);
            rs = st.executeQuery();
            if (rs.next()) {
                u.setMail(rs.getString("mail"));
                u.setPassword(rs.getString("password"));
                u.setRuolo(rs.getString("ruolo"));
                u.setNome(rs.getString("nome"));
                u.setCognome(rs.getString("cognome"));
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con metodo get_utente. Errore: " + e.getMessage());
        } finally {
            rs.close();
            st.close();
            conn.close();
            return u;
        }
    }

    public static List<Professore> get_professore_from_corso(String corso) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Professore> professori = null;
        try {
            professori = new ArrayList<Professore>();
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "SELECT professore.id, professore.nome, professore.cognome " +
                    "FROM professore, corso, insegna " +
                    "WHERE professore.id = insegna.id_professore " +
                    "AND corso.titolo = insegna.titolo_corso " +
                    "AND corso.titolo = ?;";
            st = conn.prepareStatement(sql);
            st.setString(1, corso);
            rs = st.executeQuery();
            while (rs.next()) {
                Professore professore = new Professore();
                professore.setId(rs.getInt("id"));
                professore.setNome(rs.getString("nome"));
                professore.setCognome(rs.getString("cognome"));
                professori.add(professore);
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo get_professore_from_corso. Errore: " + e.getMessage());
        } finally {
            rs.close();
            st.close();
            conn.close();
            return professori;
        }
    }

    public static List<Ripetizione> get_ripetizioni_from_giorno(String giorno) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Ripetizione> ripetizioni = null;
        try {
            ripetizioni = new ArrayList<Ripetizione>();
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "SELECT ora " +
                    "FROM ripetizione " +
                    "WHERE id_professore IS NULL " +
                    "AND titolo_corso IS NULL " +
                    "AND mail_utente IS NULL " +
                    "AND data = ?;";
            st = conn.prepareStatement(sql);
            st.setString(1, giorno);
            rs = st.executeQuery();
            while (rs.next()) {
                Ripetizione ripetizione = new Ripetizione();
                ripetizione.setOra(rs.getString("ora"));
                ripetizioni.add(ripetizione);
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo get_professore_from_corso. Errore: " + e.getMessage());
        } finally {
            rs.close();
            st.close();
            conn.close();
            return ripetizioni;
        }
    }

    public static boolean prenota(String titolo_corso, int id_professore, String data, String ora, String mail_utente) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean ret = false;
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql;
            sql = "SELECT * " +
                    "FROM ripetizione " +
                    "WHERE data = ? " +
                    "AND ora = ?;";
            st = conn.prepareStatement(sql);
            st.setString(1, data);
            st.setString(2, ora);
            rs = st.executeQuery();
            if (rs.next()) {
                if (rs.getString("mail_utente") == null && rs.getString("id_professore") == null && rs.getString("titolo_corso") == null) {
                    sql = "UPDATE ripetizione " +
                            "SET mail_utente = ?, id_professore = ?, titolo_corso = ? " +
                            "WHERE data = ? " +
                            "AND ora = ?;";
                    st = conn.prepareStatement(sql);
                    st.setString(1, mail_utente);
                    st.setInt(2, id_professore);
                    st.setString(3, titolo_corso);
                    st.setString(4, data);
                    st.setString(5, ora);
                    st.executeUpdate();
                    ret = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo prenota. Errore: " + e.getMessage());
        } finally {
            st.close();
            conn.close();
            rs.close();
            return ret;
        }
    }

    public static List<Ripetizione> get_ripetizione_completate(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Ripetizione> ripetizioni = null;
        try {
            ripetizioni = new ArrayList<Ripetizione>();
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "SELECT * " +
                    "FROM storico " +
                    "WHERE mail_utente = ? " +
                    "AND stato = ?;";
            st = conn.prepareStatement(sql);
            st.setString(1, email);
            st.setString(2, "completata");
            rs = st.executeQuery();
            while (rs.next()) {
                Ripetizione ripetizione = new Ripetizione();
                Professore professore = DAO.get_professore(rs.getString("id_professore"));
                ripetizione.setId_professore(professore.getNome() + " " + professore.getCognome());
                ripetizione.setOra(rs.getString("ora"));
                ripetizione.setData(rs.getString("data"));
                ripetizione.setMail_utente(rs.getString("mail_utente"));
                ripetizione.setTitolo_corso(rs.getString("titolo_corso"));
                ripetizioni.add(ripetizione);
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo get_ripetizione_completate. Errore: " + e.getMessage());
        } finally {
            rs.close();
            st.close();
            conn.close();
            return ripetizioni;
        }
    }

    public static List<Ripetizione> get_ripetizione_annullate(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Ripetizione> ripetizioni = null;
        try {
            ripetizioni = new ArrayList<Ripetizione>();
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "SELECT * " +
                    "FROM storico " +
                    "WHERE mail_utente = ? " +
                    "AND stato = ?;";
            st = conn.prepareStatement(sql);
            st.setString(1, email);
            st.setString(2, "annullata");
            rs = st.executeQuery();
            while (rs.next()) {
                Ripetizione ripetizione = new Ripetizione();
                Professore professore = DAO.get_professore(rs.getString("id_professore"));
                ripetizione.setId_professore(professore.getNome() + " " + professore.getCognome());
                ripetizione.setOra(rs.getString("ora"));
                ripetizione.setData(rs.getString("data"));
                ripetizione.setMail_utente(rs.getString("mail_utente"));
                ripetizione.setTitolo_corso(rs.getString("titolo_corso"));
                ripetizioni.add(ripetizione);
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo get_ripetizione_annullate. Errore: " + e.getMessage());
        } finally {
            rs.close();
            st.close();
            conn.close();
            return ripetizioni;
        }
    }

    public static List<Ripetizione> get_ripetizione_in_corso(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Ripetizione> ripetizioni = null;
        try {
            ripetizioni = new ArrayList<Ripetizione>();
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "SELECT * " +
                    "FROM ripetizione " +
                    "WHERE mail_utente = ?;";
            st = conn.prepareStatement(sql);
            st.setString(1, email);
            rs = st.executeQuery();
            while (rs.next()) {
                Ripetizione ripetizione = new Ripetizione();
                Professore professore = DAO.get_professore(rs.getString("id_professore"));
                ripetizione.setId_professore(professore.getNome() + " " + professore.getCognome());
                ripetizione.setOra(rs.getString("ora"));
                ripetizione.setData(rs.getString("data"));
                ripetizione.setMail_utente(rs.getString("mail_utente"));
                ripetizione.setTitolo_corso(rs.getString("titolo_corso"));
                ripetizioni.add(ripetizione);
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo get_ripetizione_in_corso. Errore: " + e.getMessage());
        } finally {
            rs.close();
            st.close();
            conn.close();
            return ripetizioni;
        }
    }

    public static Corso add_corso(String titolo_corso) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        Corso corso = new Corso();
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "INSERT INTO corso (titolo) VALUES (?);";
            st = conn.prepareStatement(sql);
            st.setString(1, titolo_corso);
            if (st.executeUpdate() != 0) {
                corso.setTitolo(titolo_corso);
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con metodo add_corso. Errore: " + e.getMessage());
        } finally {
            st.close();
            conn.close();
            return corso;
        }
    }

    public static Professore add_professore(String nome, String cognome) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        Professore professore = new Professore();
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "INSERT INTO professore (id, nome, cognome) VALUES (NULL, ?, ?);";
            st = conn.prepareStatement(sql);
            st.setString(1, nome);
            st.setString(2, cognome);
            if (st.executeUpdate() != 0) {
                professore.setNome(nome);
                professore.setCognome(cognome);
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo add_professore. Errore: " + e.getMessage());
        } finally {
            st.close();
            conn.close();
            return professore;
        }
    }

    public static boolean remove_prof(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        boolean rimosso = false;
        int n = 0;
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "DELETE FROM professore WHERE professore.id = ?;";
            st = conn.prepareStatement(sql);
            st.setInt(1, id);
            if (st.executeUpdate() != 0) {
                rimosso = true;
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo remove_prof. Errore: " + e.getMessage());
        } finally {
            st.close();
            conn.close();
            return rimosso;
        }
    }

    public static boolean remove_corso(String titolo) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        boolean rimosso = false;
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "DELETE FROM corso WHERE corso.titolo = ?;";
            st = conn.prepareStatement(sql);
            st.setString(1, titolo);
            if (st.executeUpdate() != 0) {
                rimosso = true;
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo remove_corso. Errore: " + e.getMessage());
        } finally {
            st.close();
            conn.close();
            return rimosso;
        }
    }

    public static boolean add_insegnamento(int id, String titolo) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        boolean aggiunto = false;
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "INSERT INTO insegna (id_professore, titolo_corso) VALUES (?, ?);";
            st = conn.prepareStatement(sql);
            st.setInt(1, id);
            st.setString(2, titolo);
            if (st.executeUpdate() != 0) {
                aggiunto = true;
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo add_insegnamento. Errore: " + e.getMessage());
        } finally {
            st.close();
            conn.close();
            return aggiunto;
        }
    }

    public static List<Corso> get_corso_from_insegna(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        List<Corso> out = new ArrayList<>();
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "SELECT titolo_corso FROM insegna WHERE id_professore = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                Corso corso = new Corso();
                corso.setTitolo(rs.getString("titolo_corso"));
                out.add(corso);
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo get_all_corsi. Errore: " + e.getMessage());
        } finally {
            rs.close();
            st.close();
            conn.close();
            return out;
        }
    }

    public static boolean remove_insegnamento(int id, String titolo) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        boolean rimosso = false;
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql = "DELETE FROM insegna WHERE id_professore = ? AND titolo_corso = ?;";
            st = conn.prepareStatement(sql);
            st.setInt(1, id);
            st.setString(2, titolo);
            if (st.executeUpdate() != 0) {
                rimosso = true;
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo remove_insegnamento. Errore: " + e.getMessage());
        } finally {
            st.close();
            conn.close();
            return rimosso;
        }
    }

    public static boolean disdici(String data, String ora) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean disdetta = false;
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql;
            sql = "SELECT * FROM ripetizione WHERE data = ? AND ora = ?;";
            st = conn.prepareStatement(sql);
            st.setString(1, data);
            st.setString(2, ora);
            rs = st.executeQuery();
            if (rs.next()) {
                sql = "INSERT INTO storico (id, data, ora, id_professore, titolo_corso, mail_utente, stato) VALUES (NULL, ?, ?, ?, ?, ?, ?);";
                st = conn.prepareStatement(sql);
                st.setString(1, data);
                st.setString(2, ora);
                st.setString(3, rs.getString("id_professore"));
                st.setString(4, rs.getString("titolo_corso"));
                st.setString(5, rs.getString("mail_utente"));
                st.setString(6, "annullata");
                st.executeUpdate();
                sql = "UPDATE ripetizione " +
                        "SET mail_utente = NULL, id_professore = NULL, titolo_corso = NULL " +
                        "WHERE data = ? " +
                        "AND ora = ?;";
                st = conn.prepareStatement(sql);
                st.setString(1, data);
                st.setString(2, ora);
                if (st.executeUpdate() != 0) {
                    disdetta = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo disdici. Errore: " + e.getMessage());
        } finally {
            rs.close();
            st.close();
            conn.close();
            return disdetta;
        }
    }

    public static boolean completata(String data, String ora) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean completata = false;
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connessione al DataBase riuscita.");
            }
            String sql;
            sql = "SELECT * FROM ripetizione WHERE data = ? AND ora = ?;";
            st = conn.prepareStatement(sql);
            st.setString(1, data);
            st.setString(2, ora);
            rs = st.executeQuery();
            if (rs.next()) {
                sql = "INSERT INTO storico (id, data, ora, id_professore, titolo_corso, mail_utente, stato) VALUES (NULL, ?, ?, ?, ?, ?, ?);";
                st = conn.prepareStatement(sql);
                st.setString(1, data);
                st.setString(2, ora);
                st.setString(3, rs.getString("id_professore"));
                st.setString(4, rs.getString("titolo_corso"));
                st.setString(5, rs.getString("mail_utente"));
                st.setString(6, "completata");
                st.executeUpdate();
                sql = "UPDATE ripetizione " +
                        "SET mail_utente = NULL, id_professore = NULL, titolo_corso = NULL " +
                        "WHERE data = ? " +
                        "AND ora = ?;";
                st = conn.prepareStatement(sql);
                st.setString(1, data);
                st.setString(2, ora);
                if (st.executeUpdate() != 0) {
                    completata = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Problema nel DAO con il metodo completata. Errore: " + e.getMessage());
        } finally {
            rs.close();
            st.close();
            conn.close();
            return completata;
        }
    }
}

