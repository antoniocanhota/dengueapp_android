package br.uff.antoniocanhota.dengueapp.android;

public class Dispositivo {
	
	private String codigoDeAtivacao;
	private String identificadorDoAndroid;
	private String emailDoUsuarioAssociado;
	
	public Dispositivo(){
		
	}
	
	public Dispositivo(String codigoDeAtivacao, String identificadorDoAndroid, String emailDoUsuarioAssociado){
		this.setCodigoDeAtivacao(codigoDeAtivacao);
		this.setIdentificadorDoAndroid(identificadorDoAndroid);
		this.setEmailDoUsuarioAssociado(emailDoUsuarioAssociado);
	}

	public String getCodigoDeAtivacao() {
		return codigoDeAtivacao;
	}

	public void setCodigoDeAtivacao(String codigoDeAtivacao) {
		this.codigoDeAtivacao = codigoDeAtivacao;
	}

	public String getEmailDoUsuarioAssociado() {
		return emailDoUsuarioAssociado;
	}

	public void setEmailDoUsuarioAssociado(String emailDoUsuarioAssociado) {
		this.emailDoUsuarioAssociado = emailDoUsuarioAssociado;
	}

	public String getIdentificadorDoAndroid() {
		return identificadorDoAndroid;
	}

	public void setIdentificadorDoAndroid(String identificadorDoAndroid) {
		this.identificadorDoAndroid = identificadorDoAndroid;
	}
	
	public boolean isCadastrado(){
		return (this.codigoDeAtivacao != null);
	}
	
	public boolean isVinculadoAUmUsuario(){
		return (this.emailDoUsuarioAssociado != null);
	}

}
