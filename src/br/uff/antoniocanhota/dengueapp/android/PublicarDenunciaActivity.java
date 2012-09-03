package br.uff.antoniocanhota.dengueapp.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PublicarDenunciaActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_denuncia);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_publicar_denuncia, menu);
        return true;
    }
}
