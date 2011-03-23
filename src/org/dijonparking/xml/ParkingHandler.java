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

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ParkingHandler extends DefaultHandler {
	//Tags XLM
	private final String PARKING = "parking";
	private final String ID = "code";
	private final String NOM = "nom";
	private final String CAP_TOTALE = "capaciteTotale";
	private final String NB_PLACE_DISPO_TOTAL = "nbPlaceDispo";
	private final String NB_PLACE_DISPO_HORAIRE = "nbPlaceDispoHoraire";
	private final String NB_PLACE_DISPO_ABONNE = "nbPlaceDispoAbonne";
	private final String TARIFS = "tarifParkings";
	private final String TARIFS_HORAIRE = "TarifParking";
	private final String DUREE = "duree";
	private final String MONTANT = "montant";
	private final String TARIFS_ABO = "tarifsAbonnements";
	private final String TARIFS_HORAIRE_ABO = "TarifsAbonnement";
	private final String DUREE_ABO = "abonnement";
	private final String MONTANT_ABO = "tarif";
	
	//ArrayList des parkings contenus dans le fichier XML	
	private ArrayList<Parking> arlParkings;
	//Contenu entre balises XML
	private String valeurCourante;
	//Parking courant
	private Parking parkingCourant;
	//Stockage info tarifs
	private String duree;
	private String montant;
	private HashMap<String, String> tarifs;
	
	@Override
	public void startDocument() {
		arlParkings = new ArrayList<Parking>(11);
	}
	
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) {
		if (localName.equalsIgnoreCase(PARKING)) {
			parkingCourant = new Parking();
		}
		else if (localName.equalsIgnoreCase(TARIFS)) {
			tarifs = new HashMap<String, String>();
		}
		else if (localName.equalsIgnoreCase(TARIFS_ABO)) {
			tarifs = new HashMap<String, String>();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String name) {
		if (localName.equalsIgnoreCase(ID)) {
			parkingCourant.setId(valeurCourante);
		}
		else if (localName.equalsIgnoreCase(NOM)) {
			parkingCourant.setNom(valeurCourante);
		}
		else if (localName.equalsIgnoreCase(CAP_TOTALE)) {
			parkingCourant.setCapTotale(Integer.parseInt(valeurCourante));
		}
		else if (localName.equalsIgnoreCase(NB_PLACE_DISPO_TOTAL)) {
			parkingCourant.setNbPlaceDispoTotal(Integer.parseInt(valeurCourante));
		}
		else if (localName.equalsIgnoreCase(NB_PLACE_DISPO_HORAIRE)) {
			parkingCourant.setNbPlaceDispoHoraire(Integer.parseInt(valeurCourante));
		}
		else if (localName.equalsIgnoreCase(NB_PLACE_DISPO_ABONNE)) {
			parkingCourant.setNbPlaceDispoAbo(Integer.parseInt(valeurCourante));
		}
		else if (localName.equalsIgnoreCase(TARIFS)) {
			parkingCourant.setTarifs(tarifs);
		}
		else if (localName.equalsIgnoreCase(TARIFS_HORAIRE)) {
			tarifs.put(duree, montant);
		}
		else if (localName.equalsIgnoreCase(DUREE)) {
			duree = valeurCourante;
		}
		else if (localName.equalsIgnoreCase(MONTANT)) {
			montant = valeurCourante;
		}
		else if (localName.equalsIgnoreCase(TARIFS_ABO)) {
			parkingCourant.setTarifsAbo(tarifs);
		}
		else if (localName.equalsIgnoreCase(TARIFS_HORAIRE_ABO)) {
			tarifs.put(duree, montant);
		}
		else if (localName.equalsIgnoreCase(DUREE_ABO)) {
			duree = valeurCourante;
		}
		else if (localName.equalsIgnoreCase(MONTANT_ABO)) {
			montant = valeurCourante;
		}
		else if (localName.equalsIgnoreCase(PARKING)) {
			arlParkings.add(parkingCourant);
		}
		
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
		valeurCourante = new String(ch, start, length);
	}
	
	public ArrayList<Parking> getParkings() {
		//Fix pour le parking Saint Anne
		Parking saintanne = null;
		Parking sainteanne = null;
		for(Parking park : arlParkings) {
			if (park.getId().equalsIgnoreCase("SAINTE_ANNE"))
				sainteanne = park;
			else if (park.getId().equalsIgnoreCase("SAINT-ANNE"))
				saintanne = park;
		}
		if (saintanne != null && sainteanne != null) {
			sainteanne.setNom(saintanne.getNom());
			sainteanne.setCapTotale(saintanne.getCapTotale());
			sainteanne.setTarifsAbo(saintanne.getTarifsAbo());
			arlParkings.remove(saintanne);
		}
		
		return arlParkings;
		
	}

}