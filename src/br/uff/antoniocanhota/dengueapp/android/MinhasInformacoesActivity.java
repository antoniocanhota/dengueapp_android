package br.uff.antoniocanhota.dengueapp.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MinhasInformacoesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_informacoes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_minhas_informacoes, menu);
        return true;
    }
}
