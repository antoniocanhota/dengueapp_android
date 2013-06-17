package br.uff.antoniocanhota.dengueapp.android;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class PostDenunciaTask extends AsyncTask<String, Void, Integer> {

	private ProgressDialog progressDialog;
	private Context ctx;
	private Denuncia denuncia;	
	private Integer httpStatus;
	private WebservicePost wsPost;
	private PublicarDenunciaActivity activity;
	private int result;
	private Exception excpt;
	
	public PostDenunciaTask(Denuncia denuncia, Context ctx, PublicarDenunciaActivity activity){
		this.ctx = ctx;
		this.denuncia = denuncia;	
		this.activity = activity;
	}
	
	@Override
	protected Integer doInBackground(String... arg0) {
		result = 0;
		buildPost();
		
		HttpPost post = new HttpPost(wsPost.getUrl());

		HttpClient client = new DefaultHttpClient();
		try {
			post.setEntity(wsPost.getMultiPartEntity());
			HttpResponse response = client.execute(post);
			this.httpStatus = response.getStatusLine().getStatusCode();
			if (httpStatus.intValue() != 201) {
				result = -1;
			} else {
				result = 1;
			}
		} catch (HttpHostConnectException e) {
			result = -2;
		} catch (Exception e) {
			result = -3;
			excpt = e;		
		}
		return result;
	
	}

	public void buildPost() {
		wsPost = new WebservicePost(
				Webservice.WEBSERVICE_PUBLICAR_DENUNCIA, ctx);
		try {			
			wsPost.addParam("denuncia[foto]",
					Utilitarios.getBitmapBodyFromJpeg(denuncia.getFoto(),ctx));
			wsPost.addParam("denuncia[latitude]",
					Utilitarios.getStringBody(denuncia.getLatitudeStr()));
			wsPost.addParam("denuncia[longitude]",
					Utilitarios.getStringBody(denuncia.getLongitudeStr()));
			wsPost.addParam("dispositivo[identificador_do_android]",
					Utilitarios.getStringBody(Utilitarios.getAndroidID(ctx)));
		} catch (Exception eInternal) {
			Utilitarios.notifyExceptionToServer(eInternal, ctx);
		}		
	}
	
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(ctx);
		progressDialog.setMessage("Enviando...");
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(Integer result) {
		progressDialog.dismiss();
		activity.finish();
		switch(result){
			case 1:
				Utilitarios.showToast("Denúncia enviada com sucesso.", ctx);				
				break;
			case -1:
				Utilitarios.showToastException(ctx);
				break;
			case -2:
				Utilitarios.showToast("O servidor da aplicação está fora do ar.",ctx);
				break;
			case -3:
				Utilitarios.notifyExceptionToServer(excpt, ctx);	
				break;
		}
		activity.finish();

	}
	
	
}
