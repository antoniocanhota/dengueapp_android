package br.uff.antoniocanhota.dengueapp.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context ctx;
	private List<Denuncia> arrayDeDenuncias = new ArrayList<Denuncia>();
	
	public MyItemizedOverlay(Context context) {		
		super(boundCenterBottom(context.getResources().getDrawable(R.drawable.ic_marker)));
		ctx = context;
	}
	
	public void addOverlayItem(GeoPoint coordenada, Denuncia d) {
		arrayDeDenuncias.add(d);
		String titulo = "Denúncia #"+Integer.toString(d.getId());
		OverlayItem overlayItem = new OverlayItem(coordenada,titulo,null);
		overlayItem.setMarker(boundCenterBottom(ctx.getResources().getDrawable(R.drawable.ic_marker)));
		mOverlays.add(overlayItem);			
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {			
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index){
		Intent intent = new Intent("br.uff.antoniocanhota.dengueapp.android.SHOWDENUNCIAACTIVITY");
		Bundle bundle = new Bundle();
		Denuncia d = arrayDeDenuncias.get(index);
		bundle.putString("id",Integer.toString(d.getId()));
		bundle.putString("dataEHora", d.getdataEHora());
		bundle.putString("enderecoAproximado", "Não determinado");
		bundle.putString("urlDaImagem", d.getFotoUrl());
		bundle.putString("situacao", "Não determinado");
		intent.putExtras(bundle);
		ctx.startActivity(intent);
		return true;
	}
	
}

