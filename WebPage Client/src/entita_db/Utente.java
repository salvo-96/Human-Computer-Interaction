package entita_db;

public class Utente {
    private String mail;
    private String password;
    private String ruolo;
    private String nome;
    private String cognome;

    public Utente() {
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRuolo() {
        return this.ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public String toString() {
        return String.format("Utente [mail=%s, password=%s, ruolo=%s, nome=%s, cognome=%s]", this.mail, this.password, this.ruolo, this.nome, this.cognome);
    }
}
