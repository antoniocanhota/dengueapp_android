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
import br.uff.antoniocanhota.dengueapp.android.Utilitarios;
import br.uff.antoniocanhota.dengueapp.android.Webservice;

public class GetDenuncias extends AsyncTask<String, Void, List<Denuncia>>{

	private ProgressDialog progressDialog;
	private Context ctx;
	private List<Denuncia> denuncias;
	
	public GetDenuncias(Context ctx, List<Denuncia> denuncias) {
		this.ctx = ctx;
		Utilitarios.getAndroidID(this.ctx);
		this.denuncias= denuncias;
	}

	@Override
	protected List<Denuncia> doInBackground(String... params) {		
		try {
			Document doc;

			doc = WSUtilitarios.getDocument(Webservice.WEBSERVICE_LISTAGEM_DE_DENUNCIAS,this.ctx);			
			NodeList denunciasNodeList = doc.getElementsByTagName("denuncia");
			for (int i = 0; i < denunciasNodeList.getLength(); i++) {
				Node denunciaNode = denunciasNodeList.item(i);
				Element denunciaElement = (Element) denunciaNode;
				Denuncia d = new Denuncia();
				d.setId(Integer
						.valueOf(WSUtilitarios.processarCampoXML(denunciaElement, "id")));
				d.setLatitude(Double.parseDouble(WSUtilitarios.processarCampoXML(
						denunciaElement, "latitude")));
				d.setLongitude(Double.parseDouble(WSUtilitarios.processarCampoXML(
						denunciaElement, "longitude")));
				d.setFotoUrl(WSUtilitarios.processarCampoXML(denunciaElement, "url_imagem"));
				d.setDataEHora(WSUtilitarios.processarCampoXML(denunciaElement, "data-e-hora"));
				denuncias.add(d);
			}
		} catch (Exception e) {
			Utilitarios.notifyExceptionToServer(e, ctx);
		}
		return denuncias;
	}

	@Override
	protected void onPreExecute(){
		progressDialog = new ProgressDialog(ctx);
		progressDialog.setMessage("Executando...");		
		progressDialog.show();
	}
	
	@Override
    protected void onPostExecute(List<Denuncia> denuncias) {        
        progressDialog.dismiss();
    }

	
	
}
