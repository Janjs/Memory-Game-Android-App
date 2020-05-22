package jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.controladors;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.domini.Carta;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.domini.Partida;

public class ControladorPartida {
    private Partida partida;
    private Carta[] cartesRevertides = new Carta[2];

    private int count = 0;

    public ControladorPartida(Partida partida) {
        this.partida = partida;
    }

    public ControladorPartida(Partida partida, int savedContadorCount, Carta cartaRevertida) {
        this.partida = partida;
        count = savedContadorCount;
        cartesRevertides[0] = cartaRevertida;
    }

    public ArrayList<Carta> getCartes(){
        return partida.getCartas();
    }

    public boolean revertirCarta(Carta carta) {
        boolean checkIguals = false;
        carta.flipImagesCarta();

        if(count==0){
            cartesRevertides[0] = carta;
            count++;
        } else {
            cartesRevertides[1] = carta;
            count = 0;
            checkIguals = true;
        }

        carta.revelar();

        return checkIguals;
    }

    public boolean checkCartesIguals() {
        if(cartesRevertides[0].getId() == cartesRevertides[1].getId()){
            imagesFound();
            partida.parellaBona();
            return true;
        } else {
            cartesRevertides[0].flipImagesCarta();
            cartesRevertides[1].flipImagesCarta();
            partida.parellaErronea(cartesRevertides[1].getRevelat());
            return false;
        }
    }

    private void imagesFound() {
        for(Carta carta: getCartes()){
            if(cartesRevertides[0].getId()==carta.getId()){
                carta.setFound(true);
            }
        }
    }

    public boolean checkJocCompletat(){
        partida.addCartaFound();
        if(partida.getCartesFound() == partida.getnParelles()) return true;
        else return false;
    }

    public String toStringCartesRevertides(){
        return cartesRevertides[0].toString();
    }

    public Partida getPartida(){
        return partida;
    }

    public int getCount() {
        return count;
    }
}

