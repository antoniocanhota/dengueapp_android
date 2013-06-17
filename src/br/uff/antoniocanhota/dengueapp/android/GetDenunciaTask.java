package br.uff.antoniocanhota.dengueapp.android;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import br.uff.antoniocanhota.dengueapp.android.webservices.WSUtilitarios;

public class GetDenunciaTask extends AsyncTask<String, Void, Denuncia> {

	private ProgressDialog progressDialog;
	private Context ctx;
	private Denuncia denuncia;
	private Bundle bundle;
	
	ImageView foto;
    TextView id; 
    TextView publicadaEm;
    TextView enderecoAproximado;
    TextView situacao;
	
	public GetDenunciaTask(Bundle bundle, TextView id, TextView publicadaEm, TextView enderecoAproximado, TextView situacao, ImageView foto, Context ctx){
		this.ctx = ctx;	
		this.bundle = bundle;
		this.denuncia = new Denuncia();
		
		this.foto = foto;
	    this.id = id; 
	    this.publicadaEm = publicadaEm;
	    this.enderecoAproximado = enderecoAproximado;
	    this.situacao = situacao;
	}
	
	@Override
	protected Denuncia doInBackground(String... params) {
		Bitmap fotoTmp = Utilitarios.getBitmapFromUrl(bundle.getString("urlDaImagem"),ctx);
		denuncia.setFoto(fotoTmp);
		denuncia.setId(Integer.valueOf(bundle.getString("id")));
		denuncia.setDataEHora(bundle.getString("dataEHora"));
		denuncia.setEnderecoAproximado(bundle.getString("enderecoAproximado"));
		denuncia.setSituacao(bundle.getString("situacao"));		     	
		return null;
	}
	
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(ctx);
		progressDialog.setMessage("Executando...");
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(Denuncia denuncia) {
		fillForm();
		progressDialog.dismiss();
	}
	
	private void fillForm(){		
    	foto.setImageBitmap(denuncia.getFoto());    	
    	id.setText("Denúncia #"+denuncia.getId()); 
        publicadaEm.setText(denuncia.getdataEHora());
        enderecoAproximado.setText(denuncia.getEnderecoAproximado());
        situacao.setText(denuncia.getSituacao());    	
	}

}
