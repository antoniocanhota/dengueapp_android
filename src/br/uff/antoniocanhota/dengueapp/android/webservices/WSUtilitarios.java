package br.uff.antoniocanhota.dengueapp.android.webservices;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.conn.HttpHostConnectException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.ProgressDialog;
import android.content.Context;
import br.uff.antoniocanhota.dengueapp.android.Utilitarios;

public class WSUtilitarios {

	public static Document getDocument(InputStream inputStream, Context ctx) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(inputStream);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			Utilitarios.notifyExceptionToServer(e, ctx);			
		}
		return doc;
	}

	public static Document getDocument(String url, Context ctx) {
		return getDocument(getInputStream(url,ctx),ctx);
	}
	
	public static InputStream getInputStream(String url, Context ctx){
		InputStream inputStream = null;
		int response = -1;

		URL urlObject = null;
		try {
			urlObject = new URL(url);
			URLConnection conn = urlObject.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();				
			}
		} catch (HttpHostConnectException e) {
			Utilitarios.showToast("Não foi possível se conectar com o servidor da aplicação.", ctx);	
		} catch (ConnectException e) {
			Utilitarios.showToast("Não foi possível se conectar com o servidor da aplicação.", ctx);	
		} catch (Exception e) {
			Utilitarios.notifyExceptionToServer(e, ctx);
		}

		return inputStream;
	}
	
	public static String processarCampoXML(Element objeto, String campo) {
		NodeList campoElmntLs = objeto.getElementsByTagName(campo);
		Element campoElmnt = (Element) campoElmntLs.item(0);
		NodeList campoNd = campoElmnt.getChildNodes();
		if (campoNd.item(0) == null){
			return null;
		}else{
			return ((Node) campoNd.item(0)).getNodeValue();
		}		
	}
	
	public static void showProgressDialog(ProgressDialog progressDialog){		
		progressDialog.setMessage("Executando...");
		progressDialog.show();
	}
	
	
	
}
