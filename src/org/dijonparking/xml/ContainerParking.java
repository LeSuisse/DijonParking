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

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;


public class ContainerParking {
	private final static String URL_XML = "http://webservices.dijon.fr/parkings.xml";
	private final static String ENCODING = "ISO-8859-1";
	private final static String USER_AGENT = "Android Dijon Parking - Market Release";
	
	public static ArrayList<Parking> getParkings() throws Exception {
		//Obtention instance d'un parseur SAX
		SAXParser sp = SAXParserFactory.newInstance().newSAXParser();

		DefaultHandler handler = new ParkingHandler();
		// Récupération du fichier XML et parsing
		URLConnection urlC = (new URL(URL_XML)).openConnection();
		urlC.setRequestProperty("USER-AGENT", USER_AGENT);
		InputSource inSo = new InputSource(urlC.getInputStream());
		inSo.setEncoding(ENCODING);
		ArrayList<Parking> listParking = null;
		if (inSo != null) {
			sp.parse(inSo, handler);
			listParking = ((ParkingHandler) handler).getParkings();
		}

		return listParking;
		
	}

}
