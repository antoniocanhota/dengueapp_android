package br.uff.antoniocanhota.dengueapp.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Webservice {

	// Para uso em produção:
	public static final String WEBSERVICES = "http://dengue.herokuapp.com/webservices/";
	// Para uso em desenvolvimento:
	// public static final String WEBSERVICES =
	// "http://dengue.herokuapp.com/webservices/";
	public static final String WEBSERVICE_PUBLICAR_DENUNCIA = "denuncias/publicar";
	public static final String WEBSERVICE_LISTAGEM_DE_DENUNCIAS = WEBSERVICES
			+ "denuncias.xml";
	public static final String WEBSERVICE_LISTAGEM_DE_DENUNCIAS_DO_USUARIO = WEBSERVICE_LISTAGEM_DE_DENUNCIAS
			+ "?identificador_do_android=";
	public static final String WEBSERVICE_CODIGO_DE_ATIVACAO = "codigo_de_ativacao.xml";

	public static List<Denuncia> getDenuncias() throws IOException {
		return getDenuncias(null);
	}

	public static List<Denuncia> getDenuncias(String identificadorDoAndroid) throws IOException {
		Document doc;
		List<Denuncia> denuncias = new ArrayList<Denuncia>();
		if (identificadorDoAndroid == null) {
			doc = getDocument(WEBSERVICE_LISTAGEM_DE_DENUNCIAS);			
		} else {
			doc = getDocument(WEBSERVICE_LISTAGEM_DE_DENUNCIAS_DO_USUARIO+identificadorDoAndroid);			
		}
		NodeList denunciasNodeList = doc.getElementsByTagName("denuncia");
		for (int i = 0; i < denunciasNodeList.getLength(); i++){
			Node denunciaNode = denunciasNodeList.item(i);
			Element denunciaElement = (Element) denunciaNode;
			Denuncia d = new Denuncia();
			d.setId(Integer.valueOf(processarCampoXML(denunciaElement,"id")));
			d.setLatitude(Double.parseDouble(processarCampoXML(denunciaElement,"latitude")));
			d.setLongitude(Double.parseDouble(processarCampoXML(denunciaElement,"longitude")));
			d.setFotoUrl(processarCampoXML(denunciaElement, "url_imagem"));
			d.setDataEHora(processarCampoXML(denunciaElement, "data-e-hora"));
			denuncias.add(d);
		}
		return denuncias;
		
	}

	public static int postDenuncia(Denuncia denuncia) {
		return 1;
	}

	public static String getCodigoDeAtivacao() throws IOException {
		Document doc = getDocument(WEBSERVICE_CODIGO_DE_ATIVACAO);
		String codigoDeAtivacao = doc.getDocumentElement().getNodeValue();
		return codigoDeAtivacao;
	}

	public static boolean isComunicavelComOServidor() {
		return true;
	}
	
	public static void postExceptionToServer(Exception e){
		
	}
	
	private static Document getDocument(String url) throws IOException{
		return getDocument(getInputStream(url));
	}
	
	private static Document getDocument(InputStream inputStream){
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(inputStream);	
			doc.getDocumentElement().normalize();
		} catch(Exception e){
			e.printStackTrace();
		}
		return doc;
	}
	
	private static InputStream getInputStream(String url) throws IOException{
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}								
		
		return inputStream;
		
	}
	
	private static String processarCampoXML(Element objeto, String campo){
		NodeList campoElmntLs = objeto.getElementsByTagName(campo);
		Element campoElmnt = (Element) campoElmntLs.item(0);
		NodeList campoNd = campoElmnt.getChildNodes();
		return ((Node) campoNd.item(0)).getNodeValue();
	}

}
