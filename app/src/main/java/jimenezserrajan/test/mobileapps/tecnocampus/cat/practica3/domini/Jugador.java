package jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.domini;

import java.util.ArrayList;

public class Jugador {
    String nom;
    ArrayList<Partida> partides;


    public Jugador(String nom, ArrayList<Partida> partides) {
        this.nom = nom;
        this.partides = partides;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Partida> getPartides() {
        return partides;
    }

    public void setPartides(ArrayList<Partida> partides) {
        this.partides = partides;
    }

    public Partida getPartida(int id){
        return partides.get(id);
    }

    public void addPartida(Partida partida){
        partides.add(partida);
    }

    public String getResultatPartidaActual(){ return String.valueOf(partides.get(partides.size()-1).getResultat()); }
}

