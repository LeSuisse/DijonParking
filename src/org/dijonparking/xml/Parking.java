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

public class Parking {
	private String id;
	private String nom;
	private Integer capTotale;
	private Integer nbPlaceDispoTotal;
	private Integer nbPlaceDispoHoraire;
	private Integer nbPlaceDispoAbo;
	private HashMap<String, String> tarifs;
	private HashMap<String, String> tarifsAbo;

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
}
