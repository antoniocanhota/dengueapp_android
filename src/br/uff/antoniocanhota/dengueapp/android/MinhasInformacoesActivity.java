package br.uff.antoniocanhota.dengueapp.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MinhasInformacoesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_informacoes);        
        TextView paragraph = (TextView) findViewById(R.id.paragraph);
        TextView labelEmphasis = (TextView) findViewById(R.id.labelEmphasis);
        
        Webservice webservice = new Webservice(getApplicationContext());
        Dispositivo dispositivo = webservice.getRegistroDoDispositivo();
        
        if (dispositivo.isCadastrado() && !dispositivo.isVinculadoAUmUsuario()){
        	paragraph.setText("Utilize o código de ativação abaixo para (i) se cadastrar no site dengue.herokuapp.com ou (ii) vincular este celular/tablet à sua conta já existente.");
        	labelEmphasis.setText(dispositivo.getCodigoDeAtivacao());
        } else if (dispositivo.isCadastrado()  && dispositivo.isVinculadoAUmUsuario()){
        	paragraph.setText("Este celular/tablet (código de ativação '"+dispositivo.getCodigoDeAtivacao()+"') já está vinculado à conta de usuário '"+dispositivo.getEmailDoUsuarioAssociado()+"'.");        	
        } else if (dispositivo.isCadastrado() && !dispositivo.isVinculadoAUmUsuario()){
        	paragraph.setText("Nenhuma denúncia foi enviada a partir deste celular/tablet. Por isso, ainda não foi gerado o código de ativação.");        	
        }       
		               
    }

}
