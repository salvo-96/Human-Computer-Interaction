package entita_db;

import java.io.Serializable;

public class Ripetizione implements Serializable {

    private String id_professore;
    private String data;
    private String ora;
    private String titolo_corso;
    private String mail_utente;

    public Ripetizione() {
    }

    public String getOra() {
        return this.ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getId_professore() {
        return this.id_professore;
    }

    public String getData() {
        return this.data;
    }

    public String getTitolo_corso() {
        return this.titolo_corso;
    }

    public String getMail_utente() {
        return this.mail_utente;
    }

    public void setId_professore(String id_professore) {
        this.id_professore = id_professore;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTitolo_corso(String titolo_corso) {
        this.titolo_corso = titolo_corso;
    }

    public void setMail_utente(String mail_utente) {
        this.mail_utente = mail_utente;
    }
}
