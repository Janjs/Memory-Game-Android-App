package jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.R;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.adapters.DbAdapter;
import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.persistencia.ResultsBBDD;

public class LoginActivity extends AppCompatActivity {

    private EditText nParelles;
    private EditText nom;
    private Button btnLogin;
    private Button btnResultats;
    private ResultsBBDD resultsBBDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nParelles = findViewById(R.id.etxt_parelles);
        nom = findViewById(R.id.etxt_nom);
        btnLogin = findViewById(R.id.btn_login);
        btnResultats = findViewById(R.id.btn_resultats);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nom.getText().toString().isEmpty() ||
                        nParelles.getText().toString().isEmpty() ||
                        !android.text.TextUtils.isDigitsOnly(nParelles.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "El nom o el nº de parelles no pot ser buit.",
                            Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(nParelles.getText().toString()) > 8 || Integer.parseInt(nParelles.getText().toString()) < 2) {
                    Toast.makeText(getApplicationContext(), "Màxim 8 parelles, mínim 2",
                            Toast.LENGTH_SHORT).show();
                } else if (nom.getText().toString().contains("=") || nom.getText().toString().contains(",")) {
                    Toast.makeText(getApplicationContext(), "Caracters invàlids",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("nom", nom.getText().toString());
                    intent.putExtra("nparelles", nParelles.getText().toString());
                    startActivity(intent);
                }
            }
        });

        DbAdapter mDbAdapter = DbAdapter.getInstance(this);
        mDbAdapter.open(); // obrir connexió
        resultsBBDD = new ResultsBBDD(mDbAdapter);

        btnResultats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResultsActivity.class);
                intent.putExtra("results", resultsBBDD.showResults());
                startActivity(intent);
            }
        });
    }
}
