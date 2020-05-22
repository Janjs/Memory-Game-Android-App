package jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.domini;

import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.R;

public class Partida {
    private int nParelles;
    private int resultat;
    private ArrayList<Carta> cartas = new ArrayList<>();
    private ArrayList<Integer> imagesIds = new ArrayList<>();

    private int cartesFound = 0;

    public Partida(int nParelles, int resultat) {
        this.nParelles = nParelles;
        this.resultat = resultat;
    }

    public Partida(int nParelles, int resultat, ArrayList<Carta> cartas, int cartesFound) {
        this.nParelles = nParelles;
        this.resultat = resultat;
        this.cartas = cartas;
        this.cartesFound = cartesFound;
    }

    public int getnParelles() {
        return nParelles;
    }

    public void setnParelles(int nParelles) {
        this.nParelles = nParelles;
    }

    public int getResultat() {
        return resultat;
    }

    public void setResultat(int resultat) {
        this.resultat = resultat;
    }

    public void parellaBona(){
        resultat += 20;
    }

    public void parellaErronea(int copsRevelat){
        resultat -= (5 * copsRevelat);
    }

    public ArrayList<Carta> getCartas(){
        return cartas;
    }

    public void initCartes(TypedArray imgs) {
        for(int i=0; i<nParelles; i++) {
            int rndInt = 0;
            do {
                rndInt = new Random().nextInt(imgs.length());
            } while (imagesIds.contains(rndInt));
            imagesIds.add(rndInt);
            int resID = imgs.getResourceId(rndInt, 0);

            cartas.add(new Carta(i, 0, resID, R.drawable.cardback, false));
            cartas.add(new Carta(i, 0, resID, R.drawable.cardback, false));
        }
        Collections.shuffle(cartas);
    }

    public Carta getCartaById(int id) throws Exception {
        for(Carta carta: cartas){
            if(carta.getId()==id){
                return carta;
            }
        }
        throw new Exception();
    }

    public int getCartesFound() {
        return cartesFound;
    }

    public void addCartaFound() {
        this.cartesFound++;
    }

    public ArrayList<String> toArrayString() {
        ArrayList<String> cartesString = new ArrayList<String>();
        for(Carta s: cartas){
            cartesString.add(s.toString());
        }
        return cartesString;
    }
}
