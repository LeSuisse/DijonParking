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

import java.util.HashMap;

import android.location.Location;

public class Parking implements Comparable<Parking>{
	private String id;
	private String nom;
	private Integer capTotale;
	private Integer nbPlaceDispoTotal;
	private Integer nbPlaceDispoHoraire;
	private Integer nbPlaceDispoAbo;
	private HashMap<String, String> tarifs;
	private HashMap<String, String> tarifsAbo;
	private Integer distance;

	public Parking() {
		
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

	public Integer getCapTotale() {
		return capTotale;
	}

	public void setCapTotale(Integer capTotale) {
		this.capTotale = capTotale;
	}

	public Integer getNbPlaceDispoTotal() {
		return nbPlaceDispoTotal;
	}

	public void setNbPlaceDispoTotal(Integer nbPlaceDispoTotal) {
		this.nbPlaceDispoTotal = nbPlaceDispoTotal;
	}

	public Integer getNbPlaceDispoHoraire() {
		return nbPlaceDispoHoraire;
	}

	public void setNbPlaceDispoHoraire(Integer nbPlaceDispoHoraire) {
		this.nbPlaceDispoHoraire = nbPlaceDispoHoraire;
	}

	public Integer getNbPlaceDispoAbo() {
		return nbPlaceDispoAbo;
	}

	public void setNbPlaceDispoAbo(Integer nbPlaceDispoAbo) {
		this.nbPlaceDispoAbo = nbPlaceDispoAbo;
	}

	public HashMap<String, String> getTarifs() {
		return tarifs;
	}

	public void setTarifs(HashMap<String, String> tarifs) {
		this.tarifs = tarifs;
	}

	public HashMap<String, String> getTarifsAbo() {
		return tarifsAbo;
	}

	public void setTarifsAbo(HashMap<String, String> tarifsAbo) {
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

	public Integer getDistance() {
		return distance;
	}

	//Calcul de la distance séparant le parking de la position
	public void setDistance(Location currentLoc) {
		if (getLatitude() == 0 || getLongitude() == 0 || currentLoc == null)
			distance = null;
		else {
			Location parkingLoc = new Location("");
			parkingLoc.setLatitude(getLatitude());
			parkingLoc.setLongitude(getLongitude());
			distance = (int) parkingLoc.distanceTo(currentLoc);
		}
	}

	@Override
	public int compareTo(Parking another) {
		int res = 0;
		if (another.distance == null) 
			res = 1;
		else if (distance == null)
			res = -1;
		else
			res = distance - another.distance;
		return res;
	}
}
