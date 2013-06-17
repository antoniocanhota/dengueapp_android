package br.uff.antoniocanhota.dengueapp.android;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;

public class WakeUpServerTask extends AsyncTask<String, Void, Integer> {

	private Exception e;
	private Context ctx;
	
	public WakeUpServerTask(Context ctx){
		this.ctx = ctx;
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		Integer result = 0;
		HttpPost post = new HttpPost(Webservice.WEBSERVICE_SERVER_STATUS);		
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = client.execute(post);
			result = response.getStatusLine().getStatusCode();
		} catch (HttpHostConnectException e) {		
			result = 0;					
		} catch (Exception e) {
			result = -1;
			this.e = e;			
		}		
		return result;
	}
	
	protected void onPostExecute(Integer result) {		
		switch(result){
			case 0:
				Utilitarios.showToast("O servidor da aplicação está fora do ar.", ctx);						
				break;
			case -1:
				Utilitarios.notifyExceptionToServer(e, ctx);
				break;
			case 200:													
				//do nothing
				break;			
		}		
	}

}
