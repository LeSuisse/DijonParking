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
package org.dijonparking.util;

/**
 * Sert uniquement à stocker certaines infos du PreferenceManager afin de pouvoir
 * y accéder sans avoir le Context
 *
 */
public class StaticPreferences {
	public final static int TRI_PROXIMITE = 0;
	public final static int TRI_NB_PLACES = 1;
	public final static int TRI_POURCENTAGE_PLACE = 2;

	private static int tri = 0;
	
	public static int getTri() {
		return tri;
	}
	
	public static void setTri(int prefTri) {
		tri = prefTri;
	}
}
