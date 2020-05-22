package jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.controladors.ControladorPartida;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.R;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.adapters.AdapterCartes;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.adapters.DbAdapter;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.domini.Carta;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.domini.Jugador;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.domini.Partida;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.persistencia.ResultsBBDD;

public class MainActivity extends AppCompatActivity {
    private String nom;
    private int nParelles;
    private AdapterCartes adaper;
    private ResultsBBDD resultsBBDD;
    private Jugador jugador;
    private int currentIdPartida;

    private RecyclerView recyclerView;
    private TextView txtNom;
    private TextView txtScore;

    private boolean crearNovaPartida;
    private ControladorPartida controlador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        nom = intent.getStringExtra("nom");
        nParelles = Integer.parseInt(intent.getStringExtra("nparelles"));

        txtNom = findViewById(R.id.txt_nom);
        txtScore = findViewById(R.id.txt_score);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(getLayout(nParelles, false));
        } else {
            recyclerView.setLayoutManager(getLayout(nParelles, true));
        }

        DbAdapter mDbAdapter = DbAdapter.getInstance(this);
        mDbAdapter.open(); // obrir connexió
        resultsBBDD = new ResultsBBDD(mDbAdapter);

        if(savedInstanceState != null){
            int nParelles = savedInstanceState.getInt("savedNparelles");
            int resultat = savedInstanceState.getInt("savedResultat");
            ArrayList<String> cartesStrings = savedInstanceState.getStringArrayList("savedItems");
            ArrayList<Carta> cartes = new ArrayList<Carta>();
            int savedCount = savedInstanceState.getInt("savedCount");
            int savedContadorCount = savedInstanceState.getInt("savedControladorCount");
            String cartaRevertidaStrings = savedInstanceState.getString("savedCartesRevertides");

            for (String s : cartesStrings) {
                cartes.add(Carta.createCartaFromString(s));
            }

            Partida novaPartida = new Partida(nParelles, resultat, cartes, savedCount);
            crearNovaPartida = false;
            initJugador();
            jugador.addPartida(novaPartida);
            if (savedContadorCount == 1) {
                controlador = new ControladorPartida(jugador.getPartida(currentIdPartida), savedContadorCount, Carta.createCartaFromString(cartaRevertidaStrings));
                adaper = new AdapterCartes(this, controlador, 1, true);
            } else {
                controlador = new ControladorPartida(jugador.getPartida(currentIdPartida));
                adaper = new AdapterCartes(this, controlador, 0, false);
            }
        } else {
            crearNovaPartida = true;
            initJugador();
            controlador = new ControladorPartida(jugador.getPartida(currentIdPartida));
            adaper = new AdapterCartes(this, controlador, 0, false);
        }
        recyclerView.setAdapter(adaper);
        txtNom.setText(nom);
        txtScore.setText(jugador.getResultatPartidaActual());
    }

    private void initJugador() {
        ArrayList<Partida> partides = new ArrayList<>();
        jugador = new Jugador(nom, partides);
        if (!resultsBBDD.isResultatsEmpty()) {
            fillData(jugador);
        }
        if(crearNovaPartida) crearNovaPartida();
    }

    private void fillData(Jugador jugador) {
        Cursor results = resultsBBDD.getResultats();
        currentIdPartida = 0;
        for (results.moveToFirst(); !results.isAfterLast(); results.moveToNext()) {
            String partidaString = results.getString(1);
            String[] temp = partidaString.split("=");

            Partida partida = new Partida(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
            if (temp[0].equals(nom)) {
                jugador.addPartida(partida);
                currentIdPartida++;
            }
        }
    }

    private void crearNovaPartida() {
        Partida novaPartida = new Partida(nParelles, 0);
        novaPartida.initCartes(this.getResources().obtainTypedArray(R.array.cartesarray));
        jugador.addPartida(novaPartida);
    }

    private void guardarPartida(Partida partida) {
        resultsBBDD.addResultat(jugador.getNom(), partida);
    }

    public void changeResultat(){
        txtScore.setText(jugador.getResultatPartidaActual());
        adaper.notifyDataSetChanged();
    }

    public void jocFinalitzat() {
        resultsBBDD.addResultat(jugador.getNom(), jugador.getPartida(currentIdPartida));
        Toast.makeText(this, "Felicitats!! has completat el joc amb un resultat de "+jugador.getResultatPartidaActual(),
                Toast.LENGTH_LONG).show();

        seeResults();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_close) {
            AlertDialog diaBox = AskOption("close", "Tancar sessió"); // Alerta de confirmació per borrar
            diaBox.show();
        } else if (id == R.id.action_showresults) {
            AlertDialog diaBox = AskOption("resultat", "Veure resultats"); // Alerta de confirmació per borrar
            diaBox.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void seeResults(){
        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
        intent.putExtra("results", resultsBBDD.showResults());
        startActivity(intent);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putInt("savedNparelles", jugador.getPartida(currentIdPartida).getnParelles());
        outState.putInt("savedResultat", jugador.getPartida(currentIdPartida).getResultat());
        ArrayList<String> cartes = jugador.getPartida(currentIdPartida).toArrayString();
        outState.putStringArrayList("savedItems", cartes);
        outState.putInt("savedCount", jugador.getPartida(currentIdPartida).getCartesFound());
        outState.putInt("savedControladorCount" , controlador.getCount());
        if(controlador.getCount()==1) outState.putString("savedCartesRevertides", controlador.toStringCartesRevertides());
        super.onSaveInstanceState(outState);
    }


    private RecyclerView.LayoutManager getLayout(int nParelles, boolean horizontal) {
        RecyclerView.LayoutManager lm;
        int files = 0;
        switch (nParelles) {
            case 2:
                files = 2;
                break;
            case 3:
                files = (horizontal) ? 3 : 2;
                break;
            case 4:
                files = (horizontal) ? 4 : 3;
                break;
            case 5:
                files = (horizontal) ? 5 : 3;
                break;
            case 6:
                files = (horizontal) ? 5 : 4;
                break;
            case 7:
                files = (horizontal) ? 6 : 4;
                break;
            case 8:
                files = (horizontal) ? 6 : 5;
                break;
            default:
                lm = new GridLayoutManager(this, 2);
        }
        return lm = new GridLayoutManager(this, files);
    }

    @Override
    public void onBackPressed() {
        AlertDialog diaBox = AskOption("back", "Tancar partida"); // Alerta de confirmació per borrar
        diaBox.show();
    }

    private AlertDialog AskOption(final String tipus, String titol) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle(titol)
                .setMessage("Si cliques OK, es perdrà la partida actual")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (tipus.equals("resultat")) {
                            seeResults();
                        } else if(tipus.equals("close")){
                            finish();
                        } else {
                            MainActivity.super.onBackPressed();
                        }
                    }

                })

                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }
}
