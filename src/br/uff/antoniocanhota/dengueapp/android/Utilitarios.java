package br.uff.antoniocanhota.dengueapp.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class Utilitarios {

	public static String getPhoneNumber(Context ctx){
		TelephonyManager tMgr =(TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
		return tMgr.getLine1Number();		   
	}
	
	public static String getAndroidID(Context ctx){
		String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID);
		return android_id;
	}
	
	public static String getDeviceID(Context ctx) {
	     String deviceID = null;
	     String serviceName = Context.TELEPHONY_SERVICE;
	     TelephonyManager m_telephonyManager = (TelephonyManager)ctx.getSystemService(serviceName);
	     int deviceType = m_telephonyManager.getPhoneType();
	     switch (deviceType) {
	           case (TelephonyManager.PHONE_TYPE_GSM):
	           break;
	           case (TelephonyManager.PHONE_TYPE_CDMA):
	           break;
	           case (TelephonyManager.PHONE_TYPE_NONE):
	           break;
	          default:
	         break;
	     }
	     deviceID = m_telephonyManager.getDeviceId();
	     return deviceID;
	}

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
			throw new IOException("N�o � uma conex�o HTTP");
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
