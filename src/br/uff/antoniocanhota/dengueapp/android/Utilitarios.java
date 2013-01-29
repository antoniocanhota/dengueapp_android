package br.uff.antoniocanhota.dengueapp.android;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

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
	
	public static String getDeviceIDCoded(Context ctx){
		int device_id = Integer.parseInt(Utilitarios.getDeviceID(ctx));
		int codigo_de_ativacao = Integer.parseInt(Utilitarios.getCodigoAtivacao(ctx));
		return Integer.toString(device_id + codigo_de_ativacao);
	}
	
	public static String getPhoneNumberCoded(Context ctx){
		int phone_number = Integer.parseInt(Utilitarios.getPhoneNumber(ctx));
		int codigo_de_ativacao = Integer.parseInt(Utilitarios.getCodigoAtivacao(ctx));
		return Integer.toString(phone_number + codigo_de_ativacao);
	}
	
	public static String getCodigoAtivacao(Context ctx) {
		String FILENAME = "codigo_ativacao_dengueapp";	
		String codigo_de_ativacao = "";
		
		try{
			FileInputStream fin = ctx.openFileInput(FILENAME);				
			DataInputStream in = new DataInputStream(fin);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			codigo_de_ativacao = br.readLine();
			in.close();												
		}
		catch (FileNotFoundException eIO) {
			Random randomGenerator = new Random();
			codigo_de_ativacao = Integer.toString(randomGenerator.nextInt(1000000));
			try{
				FileOutputStream fos = ctx.openFileOutput(FILENAME,Context.MODE_PRIVATE);
				fos.write(codigo_de_ativacao.getBytes());
				fos.close();
				return codigo_de_ativacao;
			}			
			catch (Exception ei){
				ei.printStackTrace();
			}			
		} 
		catch (Exception e){
			e.printStackTrace();
		}			
	
		return codigo_de_ativacao;
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
