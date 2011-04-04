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
package org.dijonparking.xml;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Parking implements Comparable<Parking>, Parcelable{
	private String id;
	private String nom;
	private int capTotale = -1;
	private int nbPlaceDispoTotal = -1;
	private int nbPlaceDispoHoraire = -1;
	private int nbPlaceDispoAbo = -1;
	private Bundle tarifs;
	private Bundle tarifsAbo;
	private int distance = -1;

	public Parking() {
		
	}
	
	protected Parking(Parcel in) {
		id = in.readString();
		nom = in.readString();
		capTotale = in.readInt();
		nbPlaceDispoTotal = in.readInt();
		nbPlaceDispoHoraire = in.readInt();
		nbPlaceDispoAbo = in.readInt();
		tarifs = in.readBundle();
		tarifsAbo = in.readBundle();
		distance = in.readInt();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getCapTotale() {
		return capTotale;
	}

	public void setCapTotale(int capTotale) {
		this.capTotale = capTotale;
	}

	public int getNbPlaceDispoTotal() {
		return nbPlaceDispoTotal;
	}

	public void setNbPlaceDispoTotal(int nbPlaceDispoTotal) {
		this.nbPlaceDispoTotal = nbPlaceDispoTotal;
	}

	public int getNbPlaceDispoHoraire() {
		return nbPlaceDispoHoraire;
	}

	public void setNbPlaceDispoHoraire(int nbPlaceDispoHoraire) {
		this.nbPlaceDispoHoraire = nbPlaceDispoHoraire;
	}

	public int getNbPlaceDispoAbo() {
		return nbPlaceDispoAbo;
	}

	public void setNbPlaceDispoAbo(int nbPlaceDispoAbo) {
		this.nbPlaceDispoAbo = nbPlaceDispoAbo;
	}

	public Bundle getTarifs() {
		return tarifs;
	}

	public void setTarifs(Bundle tarifs) {
		this.tarifs = tarifs;
	}

	public Bundle getTarifsAbo() {
		return tarifsAbo;
	}

	public void setTarifsAbo(Bundle tarifsAbo) {
		this.tarifsAbo = tarifsAbo;

	}

	//Q&D
	public double getLatitude() {
		double res = 0;
		
		if (id.equals("CLEMENCEAU"))
			res = 47.329111;
		else if (id.equals("CONDORCET"))
			res = 47.319671;
		else if (id.equals("CONSERVATOIRE"))
			res = 47.327281;
		else if (id.equals("DARCY"))
			res = 47.323347;
		else if (id.equals("DAUPHINE"))
			res = 47.321591;
		else if (id.equals("GRANGIER"))
			res = 47.323178;
		else if (id.equals("MALRAUX"))
			res = 47.327281;
		else if (id.equals("SAINTE_ANNE"))
			res = 47.318575;
		else if (id.equals("TIVOLI"))
			res = 47.316581;
		else if (id.equals("TREMOUILLE"))
			res = 47.325631;
		
		return res;
	}

	//Q&D
	public double getLongitude() {
		double res = 0;
		
		if (id.equals("CLEMENCEAU"))
			res = 5.05137;
		else if (id.equals("CONDORCET"))
			res= 5.033303;
		else if (id.equals("CONSERVATOIRE"))
			res = 5.049644;
		else if (id.equals("DARCY"))
			res = 5.03369;
		else if (id.equals("DAUPHINE"))
			res = 5.037532;
		else if (id.equals("GRANGIER"))
			res = 5.037631;
		else if (id.equals("MALRAUX"))
			res = 5.049644;
		else if (id.equals("SAINTE_ANNE"))
			res = 5.038309;
		else if (id.equals("TIVOLI"))
			res = 5.033679;
		else if (id.equals("TREMOUILLE"))
			res = 5.040326;
		
		return res;
	}

	public int getDistance() {
		return distance;
	}

	//Calcul de la distance séparant le parking de la position
	public void setDistance(Location currentLoc) {
		if (getLatitude() == 0 || getLongitude() == 0 || currentLoc == null)
			distance = -1;
		else {
			Location parkingLoc = new Location("");
			parkingLoc.setLatitude(getLatitude());
			parkingLoc.setLongitude(getLongitude());
			distance = (int) parkingLoc.distanceTo(currentLoc);
		}
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(Parking another) {
		int res = 0;
		if (another.distance < 0) 
			res = 1;
		else if (distance < 0)
			res = -1;
		else
			res = distance - another.distance;
		return res;
	}

	@Override
	public boolean equals(Object o) {
		boolean res = false;
		if (this == o)
			res = true;
		else if (o instanceof Parking) {
			Parking other = (Parking) o;
			if (id.equals(other.id)) {
				res = true;
			}
		}
		return res;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(nom);
		dest.writeInt(capTotale);
		dest.writeInt(nbPlaceDispoTotal);
		dest.writeInt(nbPlaceDispoHoraire);
		dest.writeInt(nbPlaceDispoAbo);
		dest.writeBundle(tarifs);
		dest.writeBundle(tarifsAbo);
		dest.writeInt(distance);
	}
	
	public static final Parcelable.Creator<Parking> CREATOR = new Parcelable.Creator<Parking>() {

		@Override
		public Parking createFromParcel(Parcel source) {
			return new Parking(source);
		}

		@Override
		public Parking[] newArray(int size) {
			return new Parking[size];
		}
	};
}
