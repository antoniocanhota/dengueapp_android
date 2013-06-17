package br.uff.antoniocanhota.dengueapp.android;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.conn.HttpHostConnectException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;

public class Webservice {

	// Para uso em produção:
	// public static final String WEBSERVICES =
	// "http://dengue.herokuapp.com/webservices/";
	// Para uso em desenvolvimento:
	//public static final String WEBSERVICES = "http://10.0.2.2:3000/webservices/";
	// "http://dengue.herokuapp.com/webservices/";
	// Para uso em desenvolvimento:
	public static final String WEBSERVICES = "http://10.1.1.2:3000/webservices/";
	public static final String WEBSERVICE_PUBLICAR_DENUNCIA = WEBSERVICES+"denuncias/publicar";
	public static final String WEBSERVICE_LISTAGEM_DE_DENUNCIAS = WEBSERVICES
			+ "denuncias.xml";
	public static final String WEBSERVICE_LISTAGEM_DE_DENUNCIAS_DO_USUARIO = WEBSERVICE_LISTAGEM_DE_DENUNCIAS
			+ "?identificador_do_android=";
	public static final String WEBSERVICE_REGISTRO_DO_DISPOSITIVO = WEBSERVICES
			+ "registro_do_dispositivo.xml?identificador_do_android=";
	public static final String WEBSERVICE_NOTIFY_EXCEPTION = WEBSERVICES
			+ "reportar_excecao";
	public static final String WEBSERVICE_SERVER_STATUS = WEBSERVICES + "status_do_servidor";

	private Context ctx;

	public Webservice(Context ctx) {
		this.ctx = ctx;
	}

	public List<Denuncia> getDenuncias() {		
		return getDenuncias(null);
	}

	public List<Denuncia> getDenuncias(String identificadorDoAndroid) {		
		List<Denuncia> denuncias = new ArrayList<Denuncia>();
		try {
			Document doc;

			if (identificadorDoAndroid == null) {
				doc = getDocument(WEBSERVICE_LISTAGEM_DE_DENUNCIAS);
			} else {
				doc = getDocument(WEBSERVICE_LISTAGEM_DE_DENUNCIAS_DO_USUARIO
						+ identificadorDoAndroid);
			}
			NodeList denunciasNodeList = doc.getElementsByTagName("denuncia");
			for (int i = 0; i < denunciasNodeList.getLength(); i++) {
				Node denunciaNode = denunciasNodeList.item(i);
				Element denunciaElement = (Element) denunciaNode;
				Denuncia d = new Denuncia();
				d.setId(Integer
						.valueOf(processarCampoXML(denunciaElement, "id")));
				d.setLatitude(Double.parseDouble(processarCampoXML(
						denunciaElement, "latitude")));
				d.setLongitude(Double.parseDouble(processarCampoXML(
						denunciaElement, "longitude")));
				d.setFotoUrl(processarCampoXML(denunciaElement, "url_imagem"));
				d.setDataEHora(processarCampoXML(denunciaElement, "data-e-hora"));
				denuncias.add(d);
			}
		} catch (Exception e) {
			Utilitarios.notifyExceptionToServer(e, ctx);
		}
		return denuncias;

	}

	public boolean postDenuncia(Denuncia denuncia) {		
		WebservicePost wsPost = new WebservicePost(
				WEBSERVICE_PUBLICAR_DENUNCIA, ctx);
		try {			
			wsPost.addParam("denuncia[foto]",
					Utilitarios.getFileBodyFromJpeg(denuncia.getFoto()));
			wsPost.addParam("denuncia[latitude]",
					Utilitarios.getStringBody(denuncia.getLatitudeStr()));
			wsPost.addParam("denuncia[longitude]",
					Utilitarios.getStringBody(denuncia.getLongitudeStr()));
			wsPost.addParam("dispositivo[identificador_do_android]",
					Utilitarios.getStringBody(Utilitarios.getAndroidID(ctx)));
		} catch (Exception eInternal) {
			Utilitarios.notifyExceptionToServer(eInternal, ctx);
		}
		return wsPost.send();
	}

	public Dispositivo getRegistroDoDispositivo() {
		Document doc = null;
		Dispositivo dispositivo = new Dispositivo();		
		try {
			doc = getDocument(WEBSERVICE_REGISTRO_DO_DISPOSITIVO+Utilitarios.getAndroidID(ctx));
			Element registroDoDispositivoElement = doc.getDocumentElement();			
			dispositivo.setCodigoDeAtivacao(processarCampoXML(registroDoDispositivoElement,"codigo_de_ativacao"));
			dispositivo.setEmailDoUsuarioAssociado(processarCampoXML(registroDoDispositivoElement,"usuario_associado"));
			dispositivo.setIdentificadorDoAndroid(processarCampoXML(registroDoDispositivoElement,"identificador_do_android"));
		} catch (Exception e) {
			Utilitarios.notifyExceptionToServer(e, ctx);
		}
		return dispositivo;
	}

//	public boolean cannotConnectToServer() {
//		boolean canConnect = false;
//		if (getServerStatus() == 200)
//			canConnect = true;
//		return !canConnect;
//	}
//	
//	public Integer getServerStatus() {
//		Integer result = null;
//		HttpPost post = new HttpPost(WEBSERVICE_SERVER_STATUS);		
//		HttpClient client = new DefaultHttpClient();
//		HttpResponse response;
//		try {
//			response = client.execute(post);
//			result = response.getStatusLine().getStatusCode();
//		} catch (HttpHostConnectException e) {
//			Utilitarios.showToast("O servidor da aplicação está fora do ar.", ctx);			
//		} catch (Exception e) {
//			Utilitarios.notifyExceptionToServer(e, ctx);
//		}
//		return result;
//	}

	public void postExceptionToServer(Exception e) {
		WebservicePost wsPost = new WebservicePost(WEBSERVICE_NOTIFY_EXCEPTION,
				ctx);
		try {
			wsPost.addParam("exception",
					Utilitarios.getStringBody(e.toString()));
			wsPost.addParam("trace", Utilitarios.getStringBody(Utilitarios
					.getExceptionStackTraceAsString(e)));
			wsPost.addParam("android_version", Utilitarios
					.getStringBody(Utilitarios.getAndroidVersionRelease()));
			wsPost.addParam("android_id",
					Utilitarios.getStringBody(Utilitarios.getAndroidID(ctx)));
			wsPost.addParam("manufacturer", Utilitarios
					.getStringBody(Utilitarios.getDeviceManufacturer()));
			wsPost.addParam("model",
					Utilitarios.getStringBody(Utilitarios.getDeviceModel()));
		} catch (Exception eInternal) {
			Utilitarios.notifyExceptionToServer(eInternal, ctx);
		}
		wsPost.send(false);
	}

	private Document getDocument(String url) {
		return getDocument(getInputStream(url));
	}

	private Document getDocument(InputStream inputStream) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(inputStream);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			Utilitarios.notifyExceptionToServer(e, this.ctx);			
		}
		return doc;
	}

	private InputStream getInputStream(String url) {
//		WebserviceGet wsGet = new WebserviceGet(ctx);
//		wsGet.execute(new String[] { url });
//		return wsGet.getInputStream();
		return null;
	}

	private static String processarCampoXML(Element objeto, String campo) {
		NodeList campoElmntLs = objeto.getElementsByTagName(campo);
		Element campoElmnt = (Element) campoElmntLs.item(0);
		NodeList campoNd = campoElmnt.getChildNodes();
		return ((Node) campoNd.item(0)).getNodeValue();
	}

	// private MultipartEntity getMultipartEntity(String url){
	// HttpClient client = new DefaultHttpClient();
	// HttpPost post = new HttpPost(url);
	// return new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	// }

}
