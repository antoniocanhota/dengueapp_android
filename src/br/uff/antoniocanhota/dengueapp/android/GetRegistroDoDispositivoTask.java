package br.uff.antoniocanhota.dengueapp.android;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import br.uff.antoniocanhota.dengueapp.android.webservices.WSUtilitarios;

public class GetRegistroDoDispositivoTask extends AsyncTask<String, Void, Dispositivo>{

	private ProgressDialog progressDialog;
	private Context ctx;
	private TextView paragraph;
	private TextView labelEmphasis;	
	private Exception excp;
	//private Dispositivo dispositivo;
	
	public GetRegistroDoDispositivoTask(TextView paragraph,TextView labelEmphasis, Context ctx){
		this.ctx = ctx;
		this.paragraph = paragraph;
		this.labelEmphasis = labelEmphasis;
		this.excp = null;
	}
	
	@Override
	protected Dispositivo doInBackground(String... arg0) {
		Document doc = null;
		Dispositivo dispositivo = new Dispositivo();		
		try {
			doc = WSUtilitarios.getDocument(Webservice.WEBSERVICE_REGISTRO_DO_DISPOSITIVO+Utilitarios.getAndroidID(ctx),ctx);
			Element registroDoDispositivoElement = doc.getDocumentElement();			
			dispositivo.setCodigoDeAtivacao(WSUtilitarios.processarCampoXML(registroDoDispositivoElement,"codigo_de_ativacao"));
			dispositivo.setEmailDoUsuarioAssociado(WSUtilitarios.processarCampoXML(registroDoDispositivoElement,"usuario_associado"));
			dispositivo.setIdentificadorDoAndroid(WSUtilitarios.processarCampoXML(registroDoDispositivoElement,"identificador_do_android"));
		} catch (Exception e) {
			excp = e;			
		}
		return dispositivo;
	}
	
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(ctx);
		progressDialog.setMessage("Executando...");
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(Dispositivo dispositivo) {
		progressDialog.dismiss();
		if (excp == null){			
			Utilitarios.notifyExceptionToServer(excp, ctx);
		} else {
			
			if (dispositivo.isCadastrado() && !dispositivo.isVinculadoAUmUsuario()){
	        	paragraph.setText("Utilize o código de ativação abaixo para (i) se cadastrar no site dengue.herokuapp.com ou (ii) vincular este celular/tablet à sua conta já existente.");
	        	labelEmphasis.setText(dispositivo.getCodigoDeAtivacao());
	        } else if (dispositivo.isCadastrado()  && dispositivo.isVinculadoAUmUsuario()){
	        	paragraph.setText("Este celular/tablet (código de ativação '"+dispositivo.getCodigoDeAtivacao()+"') já está vinculado à conta de usuário '"+dispositivo.getEmailDoUsuarioAssociado()+"'.");        	
	        } else if (dispositivo.isCadastrado() && !dispositivo.isVinculadoAUmUsuario()){
	        	paragraph.setText("Nenhuma denúncia foi enviada a partir deste celular/tablet. Por isso, ainda não foi gerado o código de ativação.");        		        
	        } else {
	        	Utilitarios.showToast("Houve um erro ao processar a solicitação.", ctx);
	        }
		}		
		
	}

}
