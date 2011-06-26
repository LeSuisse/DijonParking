package org.dijonparking.gui;

import greendroid.app.GDExpandableListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.LoaderActionBarItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import org.dijonparking.R;
import org.dijonparking.xml.DownloaderAndParser;
import org.dijonparking.xml.Parking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class InfoParking extends GDExpandableListActivity {

	private Parking parking;
	private TreeMap<Float, String> tarifs = null;
	private TreeMap<Float, String> tarifsAbo = null;
	private final static DecimalFormat df = new DecimalFormat("0.00");
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infoparking);
		
		getActionBar().addItem(Type.Refresh);
		getActionBar().addItem(Type.Export);
		
		parking = getIntent().getExtras().getParcelable("parking");
		
		updateParking();
	}
	
	public boolean onHandleActionBarItemClick (ActionBarItem item, int position) {
		switch (position) {
		case 0:
			new DownloadAndParseTask(this).execute();
			((LoaderActionBarItem) item).setLoading(false);
			return true;
		case 1:
			String direction = "google.navigation:q="+parking.getLatitude()+","+parking.getLongitude();
			Intent it = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(direction));
			startActivity(it);
			return true;
		default:
			return super.onHandleActionBarItemClick(item, position);
		}
	}
	
	private class DownloadAndParseTask extends DownloaderAndParser {

		public DownloadAndParseTask(Context context) {
			super(context);
		}

		@Override
		protected void finalOperations(ArrayList<Parking> listParking) {
			Iterator<Parking> it = listParking.iterator();
			Parking park;
			boolean trouve = false;
			while (it.hasNext() && !trouve) {
				park = it.next();
				if (park.getId().equals(parking.getId())) {
					parking = park;
					trouve = true;
					updateParking();
				}
			}
		}

		@Override
		protected void restartTask() {
			new DownloadAndParseTask(getContext()).execute();
			
		}
		
	}
	
	private void updateParking() {
		//Affichage icone disponibilité parking
		double ratioLibre = -1;
		ImageView iconeDispo = (ImageView) findViewById(R.id.parking_dispo_icone);
		if (parking.getNbPlaceDispoTotal() >= 0 && parking.getCapTotale() >= 0)
			ratioLibre = (double) parking.getNbPlaceDispoTotal() / (double) parking.getCapTotale();
		if (ratioLibre < 0)
			iconeDispo.setImageResource(R.drawable.unpark);
		else if (ratioLibre < 0.2)
			iconeDispo.setImageResource(R.drawable.redpark);
		else if (ratioLibre < 0.4)
			iconeDispo.setImageResource(R.drawable.orangepark);
		else
			iconeDispo.setImageResource(R.drawable.greenpark);
		
		//Affichage nom du parking
		((TextView) findViewById(R.id.nom_parking)).setText(parking.getNom());
		
		String res = null;
		//Affichage nombre de place libre
		if (parking.getNbPlaceDispoHoraire() < 0)
			res = getString(R.string.placelibreindispo);
		else if (parking.getNbPlaceDispoHoraire() == 0 || parking.getNbPlaceDispoHoraire() == 1)
			res = getString(R.string.placelibre)+" "+parking.getNbPlaceDispoHoraire();
		else
			res = getString(R.string.placeslibres)+" "+parking.getNbPlaceDispoHoraire();
		((TextView) findViewById(R.id.nombre_place_dispo)).setText(res);
		
		//Affichage nombre place libre abonné
		if (parking.getNbPlaceDispoAbo() < 0)
			res = getString(R.string.placelibreaboindispo);
		else if (parking.getNbPlaceDispoAbo() == 0 || parking.getNbPlaceDispoAbo() == 1)
			res = getString(R.string.placelibreabo)+" "+parking.getNbPlaceDispoAbo();
		else
			res = getString(R.string.placeslibresabo)+" "+parking.getNbPlaceDispoAbo();
		((TextView) findViewById(R.id.nombre_place_dispo_abo)).setText(res);
		
		//Affichage capacité totale
		if (parking.getCapTotale() < 0)
			res = getString(R.string.captotaleindispo);
		else
			res = getString(R.string.captotale, parking.getCapTotale());
		((TextView) findViewById(R.id.capacite_totale)).setText(res);
		
		//Tri en fonction des tarifs
		sortTarifs();
		
		//Affichage des tarifs
		SimpleExpandableListAdapter exAdapt = new SimpleExpandableListAdapter(
				this,
				groupList(),
				android.R.layout.simple_expandable_list_item_1,
				new String[] {"type"},
				new int[] {android.R.id.text1},
				childList(),
				R.layout.simpleitem,
				new String[] { "montant", "duree" },
				new int[] { android.R.id.text1, android.R.id.text2 }
		);
		setListAdapter(exAdapt);
	}
	
	private void sortTarifs() {
		if (parking.getTarifs() != null) {
			tarifs = new TreeMap<Float, String>();
			for (String key : parking.getTarifs().keySet()) {
				try {
					tarifs.put(Float.parseFloat(key), (String) parking.getTarifs().get(key));
				}
				catch (Exception e) {}
			}
		}
		if (parking.getTarifsAbo() != null) {
			tarifsAbo = new TreeMap<Float, String>();
			for (String key : parking.getTarifsAbo().keySet()) {
				try {
					tarifsAbo.put(Float.parseFloat(key), (String) parking.getTarifsAbo().get(key));
				}
				catch (Exception e) {}
			}
		}
	}

	/**
	 * 
	 * @return List contenant une Map pour chaque groupe de la expandableList
	 */
	private ArrayList<HashMap<String, String>> groupList() {
		ArrayList<HashMap<String, String>> groupList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> m;
		if (tarifs.size() != 0) {
			m = new HashMap<String, String>();
			m.put("type", getString(R.string.tarifshoraires));
			groupList.add(m);
		}
		if (tarifsAbo.size() != 0) {
			m = new HashMap<String, String>();
			m.put("type", getString(R.string.tarifsabo));
			groupList.add(m);
		}
		return groupList;
		
	}

	private ArrayList<ArrayList<HashMap<String, String>>> childList() {
		ArrayList<ArrayList<HashMap<String, String>>> childList =new ArrayList<ArrayList<HashMap<String,String>>>();
		ArrayList<HashMap<String, String>> cont;
		if (tarifs.size() != 0) {
			cont = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> child;
			for (Float key : tarifs.keySet()) {
				child = new HashMap<String, String>();
				child.put("montant", df.format(key)+" €");
				child.put("duree", tarifs.get(key));
				cont.add(child);
			}
			childList.add(cont);
		}
		if (tarifsAbo.size() != 0) {
			cont = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> child;
			for (Float key : tarifsAbo.keySet()) {
				child = new HashMap<String, String>();
				child.put("montant", df.format(key)+" €");
				child.put("duree", tarifsAbo.get(key));
				cont.add(child);
			}
			childList.add(cont);
		}
		
		return childList;
	}
}
