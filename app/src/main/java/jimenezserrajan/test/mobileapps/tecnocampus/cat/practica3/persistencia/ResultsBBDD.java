package jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.persistencia;

import android.database.Cursor;

import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.adapters.DbAdapter;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.domini.Partida;

public class ResultsBBDD {
    private DbAdapter mDbAdapter;

    public ResultsBBDD(DbAdapter mDbAdapter){
        this.mDbAdapter = mDbAdapter;
    }

    public void addResultat(String nomJugador, Partida partida){
        mDbAdapter.createTodo(nomJugador + "=" + partida.getResultat() + "=" + partida.getnParelles());
    }

    public Cursor getResultats(){
        return mDbAdapter.fetchAllTodos();
    }

    public boolean isResultatsEmpty(){
        return mDbAdapter.isEmpty();
    }

    public String showResults(){
        Cursor results = getResultats();
        String resultat = "";

        for(results.moveToFirst(); !results.isAfterLast(); results.moveToNext()){
            String partidaString = results.getString(1);
            String[] temp = partidaString.split("=");
            resultat += "Jugador: "+temp[0]+", ha aconseguit un resultat de "+temp[1]+" en "+temp[2]+" parella/es.\n\n";
        }
        return resultat;
    }
}
