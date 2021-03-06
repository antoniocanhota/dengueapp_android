package br.uff.antoniocanhota.dengueapp.android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private boolean isTablet() {
		return (getApplicationContext().getResources().getConfiguration().screenLayout
		& Configuration.SCREENLAYOUT_SIZE_MASK)
		>= Configuration.SCREENLAYOUT_SIZE_XLARGE;
		}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WakeUpServerTask wakeUpServerTask = new WakeUpServerTask(getApplicationContext());
        wakeUpServerTask.execute();
        
        if ( isTablet() )
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        
        setContentView(R.layout.activity_main);
                
        Button bt_publicar_denuncia = (Button)findViewById(R.id.bt_publicar_denuncia);
        Button bt_denuncias = (Button)findViewById(R.id.bt_denuncias);
        Button bt_minhas_denuncias = (Button)findViewById(R.id.bt_minhas_denuncias);
        //Button bt_dicas_prevencao = (Button)findViewById(R.id.bt_dicas_prevencao);
        Button bt_minhas_informacoes = (Button)findViewById(R.id.bt_minhas_informacoes);
        Button bt_sobre = (Button)findViewById(R.id.bt_sobre);
        
        bt_publicar_denuncia.setOnClickListener(new View.OnClickListener(){
			
			public void onClick(View arg0) {
				startActivity(new Intent("br.uff.antoniocanhota.dengueapp.android.PUBLICARDENUNCIAACTIVITY"));
			}			
        });
        
        bt_denuncias.setOnClickListener(new View.OnClickListener(){
        	
        	public void onClick(View arg0) {
        		startActivity(new Intent("br.uff.antoniocanhota.dengueapp.android.DENUNCIASACTIVITY"));
        	}			
        });
        
        bt_minhas_denuncias.setOnClickListener(new View.OnClickListener(){
			
			public void onClick(View arg0) {
				startActivity(new Intent("br.uff.antoniocanhota.dengueapp.android.MINHASDENUNCIASACTIVITY"));				
			}
        });
        
        /*bt_dicas_prevencao.setOnClickListener(new View.OnClickListener(){
        	
        	public void onClick(View arg0) {
        		startActivity(new Intent("br.uff.antoniocanhota.dengueapp.android.DICASPREVENCAOACTIVITY"));				
        	}
        });*/
        
        bt_minhas_informacoes.setOnClickListener(new View.OnClickListener(){
        	
        	public void onClick(View arg0) {
        		startActivity(new Intent("br.uff.antoniocanhota.dengueapp.android.MINHASINFORMACOESACTIVITY"));
        	}
        	
        });
        
        bt_sobre.setOnClickListener(new View.OnClickListener(){
        	
        	public void onClick(View arg0) {
        		startActivity(new Intent("br.uff.antoniocanhota.dengueapp.android.SOBREACTIVITY"));				
        	}
        });
        
    }

}
