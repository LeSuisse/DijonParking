/**
 *      This file is part of Dijon Parking <http://code.google.com/p/dijon-parking/>
 *      
 *      Dijon Parking is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *      
 *      Dijon Parking is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *      
 *      You should have received a copy of the GNU General Public License
 *      along with Dijon Parking.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.dijonparking.gui;

import java.text.DecimalFormat;
import java.util.List;

import org.dijonparking.R;
import org.dijonparking.xml.Parking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListParkingAdapter extends ArrayAdapter<Parking>{
	
	private List<Parking> parking;
	LayoutInflater infla;

	public ListParkingAdapter(Context context, List<Parking> objects) {
		super(context, R.layout.dispoparking,objects);
		parking = objects;
		infla = LayoutInflater.from(getContext());
	}
	
	private static class ViewHolder {
		ImageView icone;
		TextView nomParking;
		TextView placeDispo;
		TextView distance;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView == null) {
			convertView = infla.inflate(R.layout.dispoparking, null);
			holder = new ViewHolder();
			holder.icone = (ImageView) convertView.findViewById(R.id.parking_dispo_icone);
			holder.nomParking = (TextView) convertView.findViewById(R.id.nom_parking);
			holder.placeDispo = (TextView) convertView.findViewById(R.id.nombre_place_dispo);
			holder.distance = (TextView) convertView.findViewById(R.id.distance_parking);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.nomParking.setText(parking.get(position).getNom());

		int placeLibreDispo = parking.get(position).getNbPlaceDispoHoraire();
		if (placeLibreDispo < 0) {
			holder.placeDispo.setText(getContext().getText(R.string.placelibreindispo));
		}
		else if (placeLibreDispo == 0 || placeLibreDispo == 1) {
			holder.placeDispo.setText(getContext().getText(R.string.placelibre)+" "+placeLibreDispo);
		}
		else {
			holder.placeDispo.setText(getContext().getText(R.string.placeslibres)+" "+placeLibreDispo);
		}
		
		//Calcul de l'image à afficher
		double ratioLibre = -1;
		if (parking.get(position).getNbPlaceDispoTotal() >= 0 && parking.get(position).getCapTotale() >= 0)
			ratioLibre = (double) parking.get(position).getNbPlaceDispoTotal() / (double) parking.get(position).getCapTotale();

		if (ratioLibre < 0)
			holder.icone.setImageResource(R.drawable.unpark);
		else if (ratioLibre < 0.2)
			holder.icone.setImageResource(R.drawable.redpark);
		else if (ratioLibre < 0.4)
			holder.icone.setImageResource(R.drawable.orangepark);
		else
			holder.icone.setImageResource(R.drawable.greenpark);
		
		//Affichage distance
		if (parking.get(position).getDistance() >= 0) {
			DecimalFormat df = new DecimalFormat("0.0");
			holder.distance.setText(df.format(((double)parking.get(position).getDistance())/1000) + " km");
		}
		
		return convertView;
		
	}

}
