package br.uff.antoniocanhota.dengueapp.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utilitarios {

	public static String processarCampoXML(Element objeto, String campo){
		NodeList campoElmntLs = objeto.getElementsByTagName(campo);
		Element campoElmnt = (Element) campoElmntLs.item(0);
		NodeList campoNd = campoElmnt.getChildNodes();
		return ((Node) campoNd.item(0)).getNodeValue();
	}
	
	public static InputStream OpenHttpConnection(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;
		
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		
		if (!(conn instanceof HttpURLConnection)) 
			throw new IOException("N‹o Ž uma conex‹o HTTP");
		try{
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		}catch (Exception ex){
			throw new IOException("Erro ao conectar");
		}
		
		return in;
	}
	
}
