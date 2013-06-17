package br.uff.antoniocanhota.dengueapp.android.webservices;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import br.uff.antoniocanhota.dengueapp.android.Denuncia;
import br.uff.antoniocanhota.dengueapp.android.MyItemizedOverlay;
import br.uff.antoniocanhota.dengueapp.android.Utilitarios;
import br.uff.antoniocanhota.dengueapp.android.Webservice;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class GetDenuncias extends AsyncTask<String, Void, List<Denuncia>> {

	private ProgressDialog progressDialog;
	private Context ctx;
	private List<Denuncia> denuncias;
	private MapView mapView;
	private String androidId;

	public GetDenuncias(Context ctx, MapView mapView) {
		this.ctx = ctx;		
		this.mapView = mapView;
		this.denuncias = new ArrayList<Denuncia>();
		this.androidId = null;
	}
	
	public GetDenuncias(Context ctx, MapView mapView, String androidId) {
		this(ctx,mapView);
		this.androidId = androidId;
	}

	@Override
	protected List<Denuncia> doInBackground(String... params) {
		try {
			Document doc;

			if (androidId == null){
				doc = WSUtilitarios.getDocument(
						Webservice.WEBSERVICE_LISTAGEM_DE_DENUNCIAS, this.ctx);
			}else{
				doc = WSUtilitarios.getDocument(
						Webservice.WEBSERVICE_LISTAGEM_DE_DENUNCIAS_DO_USUARIO+androidId, this.ctx);
			}			
			NodeList denunciasNodeList = doc.getElementsByTagName("denuncia");
			for (int i = 0; i < denunciasNodeList.getLength(); i++) {
				Node denunciaNode = denunciasNodeList.item(i);
				Element denunciaElement = (Element) denunciaNode;
				Denuncia d = new Denuncia();
				d.setId(Integer.valueOf(WSUtilitarios.processarCampoXML(
						denunciaElement, "id")));
				d.setLatitude(Double.parseDouble(WSUtilitarios
						.processarCampoXML(denunciaElement, "latitude")));
				d.setLongitude(Double.parseDouble(WSUtilitarios
						.processarCampoXML(denunciaElement, "longitude")));
				d.setFotoUrl(WSUtilitarios.processarCampoXML(denunciaElement,
						"url_imagem"));
				d.setDataEHora(WSUtilitarios.processarCampoXML(denunciaElement,
						"data-e-hora"));
				denuncias.add(d);
			}
		} catch (Exception e) {
			Utilitarios.notifyExceptionToServer(e, ctx);
		}		
		return denuncias;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(ctx);
		progressDialog.setMessage("Executando...");
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(List<Denuncia> denuncias) {
		generateMarkers();
		mapView.invalidate();
		progressDialog.dismiss();
	}

	private void generateMarkers() {
		// Criação da listagem de pontos das denúncias
		MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(ctx);

		// Iteração da lista de denúncias
		for (Denuncia denuncia : denuncias) {
			// Criação do ponto no mapa
			Double lat = Double.parseDouble(String.valueOf(denuncia
					.getLatitude()));
			Double lng = Double.parseDouble(String.valueOf(denuncia
					.getLongitude()));
			GeoPoint ponto = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

			// Adicionando o ponto à listagem de pontos
			itemizedOverlay.addOverlayItem(ponto, denuncia);
			mapView.getOverlays().add(itemizedOverlay);
		}

	}

}
