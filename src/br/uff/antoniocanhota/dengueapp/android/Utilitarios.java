package br.uff.antoniocanhota.dengueapp.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.provider.Settings.Secure;
import android.widget.Toast;

public class Utilitarios {

	// public static String getPhoneNumber(Context ctx){
	// TelephonyManager tMgr
	// =(TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
	// return tMgr.getLine1Number();
	// }

	public static String getAndroidVersionRelease(){	
		return android.os.Build.VERSION.RELEASE;
	}
	
	public static String getDeviceManufacturer(){
		return android.os.Build.MANUFACTURER;
	}
	
	public static String getDeviceModel(){
		return android.os.Build.MODEL;
	}
	
	public static String getAndroidID(Context ctx) {
		String android_id = Secure.getString(ctx.getContentResolver(),
				Secure.ANDROID_ID);
		return android_id;
	}

	public static void showToastIOException(Context ctx) {
		Toast.makeText(ctx, "Houve um erro de conexão. Tente novamente.",
				Toast.LENGTH_LONG).show();
	}
	
	public static void showToast(String message, Context ctx) {
		Toast.makeText(ctx, message,
				Toast.LENGTH_LONG).show();
	}

	public static void showToastException(Context ctx) {
		Toast.makeText(ctx, "Desculpe, mas não é possível continuar a ação solicitada. Houve um erro da aplicação.", Toast.LENGTH_LONG)
				.show();
	}
	
	public static void notifyExceptionToServer(Exception e, Context ctx){
		e.printStackTrace();
		Webservice webservice = new Webservice(ctx);
		webservice.postExceptionToServer(e);		
		showToastException(ctx);
	}
	
	public static StringBody getStringBody(String string) throws UnsupportedEncodingException{
		return new StringBody(string,Charset.forName("UTF-8"));
	}
	
	public static FileBody getFileBodyFromJpeg(File file){
		return new FileBody(file,"image/jpeg");
	}
	
	public static File getFileFromBitmap(Bitmap bitmap, String fileName, Context ctx){
		File result = null;
		try{
			// create a file to write bitmap data
			result = new File(ctx.getCacheDir(), fileName);
			result.createNewFile();
			// Convert bitmap to byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
			byte[] bitmapdata = bos.toByteArray();
			// write the bytes in file
			FileOutputStream fos = new FileOutputStream(result);
			fos.write(bitmapdata);
			fos.flush();
			fos.close();
		}catch(Exception e){
			Utilitarios.notifyExceptionToServer(e, ctx);
		}
		return result;
	}
	
	public static String getExceptionStackTraceAsString(Exception e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}	
	
	// public static String getDeviceID(Context ctx) {
	// String deviceID = null;
	// String serviceName = Context.TELEPHONY_SERVICE;
	// TelephonyManager m_telephonyManager =
	// (TelephonyManager)ctx.getSystemService(serviceName);
	// int deviceType = m_telephonyManager.getPhoneType();
	// switch (deviceType) {
	// case (TelephonyManager.PHONE_TYPE_GSM):
	// break;
	// case (TelephonyManager.PHONE_TYPE_CDMA):
	// break;
	// case (TelephonyManager.PHONE_TYPE_NONE):
	// break;
	// default:
	// break;
	// }
	// deviceID = m_telephonyManager.getDeviceId();
	// return deviceID;
	// }

	// public static String getDeviceIDCoded(Context ctx){
	// BigDecimal device_id = new BigDecimal(Utilitarios.getDeviceID(ctx));
	// BigDecimal codigo_de_ativacao = new
	// BigDecimal(Utilitarios.getCodigoAtivacao(ctx));
	// BigDecimal device_id_coded = device_id.add(codigo_de_ativacao);
	// return device_id_coded.toString();
	// }

	// public static String getPhoneNumberCoded(Context ctx){
	// String real_phone_number = Utilitarios.getPhoneNumber(ctx);
	// BigDecimal phone_number = new BigDecimal("0");
	// if (real_phone_number == null){
	// phone_number = new BigDecimal(real_phone_number);
	// }
	// BigDecimal codigo_de_ativacao = new
	// BigDecimal(Utilitarios.getCodigoAtivacao(ctx));
	// BigDecimal phone_number_coded = phone_number.add(codigo_de_ativacao);
	// return phone_number_coded.toString();
	// }

	// public static String getCodigoAtivacao(Context ctx) {
	// String FILENAME = "codigo_ativacao_dengueapp";
	// String codigo_de_ativacao = "";
	//
	// try{
	// FileInputStream fin = ctx.openFileInput(FILENAME);
	// DataInputStream in = new DataInputStream(fin);
	// BufferedReader br = new BufferedReader(new InputStreamReader(in));
	// codigo_de_ativacao = br.readLine();
	// in.close();
	// }
	// catch (FileNotFoundException eIO) {
	// Random randomGenerator = new Random();
	// codigo_de_ativacao = Integer.toString(randomGenerator.nextInt(1000000));
	// try{
	// FileOutputStream fos = ctx.openFileOutput(FILENAME,Context.MODE_PRIVATE);
	// fos.write(codigo_de_ativacao.getBytes());
	// fos.close();
	// return codigo_de_ativacao;
	// }
	// catch (Exception ei){
	// ei.printStackTrace();
	// }
	// }
	// catch (Exception e){
	// e.printStackTrace();
	// }
	//
	// return codigo_de_ativacao;
	// }

	// public static String processarCampoXML(Element objeto, String campo){
	// NodeList campoElmntLs = objeto.getElementsByTagName(campo);
	// Element campoElmnt = (Element) campoElmntLs.item(0);
	// NodeList campoNd = campoElmnt.getChildNodes();
	// return ((Node) campoNd.item(0)).getNodeValue();
	// }

	// public static InputStream OpenHttpConnection(String urlString) throws
	// IOException {
	// InputStream in = null;
	// int response = -1;
	//
	// URL url = new URL(urlString);
	// URLConnection conn = url.openConnection();
	//
	// if (!(conn instanceof HttpURLConnection))
	// throw new IOException("N�o � uma conex�o HTTP");
	// try{
	// HttpURLConnection httpConn = (HttpURLConnection) conn;
	// httpConn.setAllowUserInteraction(false);
	// httpConn.setInstanceFollowRedirects(true);
	// httpConn.setRequestMethod("GET");
	// httpConn.connect();
	// response = httpConn.getResponseCode();
	// if (response == HttpURLConnection.HTTP_OK) {
	// in = httpConn.getInputStream();
	// }
	// }catch (Exception ex){
	// throw new IOException("Erro ao conectar");
	// }
	//
	// return in;
	// }

}
