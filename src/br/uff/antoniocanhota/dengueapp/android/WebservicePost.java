package br.uff.antoniocanhota.dengueapp.android;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

public class WebservicePost {
	
	private String url;
	private MultipartEntity mpEntity;
	private Context ctx;
	
	WebservicePost(String url, Context ctx){
		this.url = url;
		this.mpEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		this.ctx = ctx;
	}
	
	public void addParam(String name, ContentBody contentBody){
		this.mpEntity.addPart(name, contentBody);
	}
	
	public void send(){
		send(true);
	}
	
	public void send(boolean notifyExceptionToServer){		
		HttpPost post = new HttpPost(this.url);
		post.setEntity(this.mpEntity);
		HttpClient client = new DefaultHttpClient();
		try {
			
			HttpResponse response = client.execute(post);
			response.toString();
		} catch (Exception e) {
			if (notifyExceptionToServer){
				Utilitarios.notifyExceptionToServer(e, ctx);}	
		}
		
	}

}
