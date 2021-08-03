package entita_db;

import java.io.Serializable;

public class Corso implements Serializable {

    private String titolo;

    public Corso() {
    }

    public String getTitolo() {
        return this.titolo;
    }

    public void setTitolo(String titolo) {

        this.titolo = titolo;
    }

    @Override
    public String toString() {
        return String.format("Corso [titolo=%s]", this.titolo);
    }


}
