package jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.R;

public class ResultsActivity extends AppCompatActivity {
    private String resultats;
    private TextView tvResultats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        resultats = intent.getStringExtra("results");

        tvResultats = findViewById(R.id.txt_resultats_resultats);
        tvResultats.setText(resultats);
    }
}
