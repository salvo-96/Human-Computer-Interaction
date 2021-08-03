package entita_db;

public class Insegna {

    private int id_professore;
    private String titolo_corso;

    public Insegna() {
    }

    public int getId_professore() {
        return this.id_professore;
    }

    public void setId_professore(int id_professore) {
        this.id_professore = id_professore;
    }

    public String getTitolo_corso() {
        return this.titolo_corso;
    }

    public void setTitolo_corso(String titolo_corso) {
        this.titolo_corso = titolo_corso;
    }

    @Override
    public String toString() {
        return String.format("Insegna [id_professore=%s, titolo_corso=%s]", this.id_professore, this.titolo_corso);
    }
}
