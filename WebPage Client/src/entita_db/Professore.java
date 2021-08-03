package entita_db;

import java.io.Serializable;

public class Professore implements Serializable {

    private int id;
    private String nome;
    private String cognome;

    public Professore() {
    }


    public static void main(String[] args){

        Professore mario = new Professore();

        mario.setId(12);

        mario.setNome("Orazio");

        mario.setCognome("Bia");


    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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
        return String.format("Professore [id=%d, nome=%s, cognome=%s]", this.id, this.nome, this.cognome);
    }
}
