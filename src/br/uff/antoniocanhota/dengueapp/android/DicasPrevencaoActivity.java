package br.uff.antoniocanhota.dengueapp.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DicasPrevencaoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicas_prevencao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dicas_prevencao, menu);
        return true;
    }
}
