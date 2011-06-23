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

import greendroid.app.GDMapActivity;

import org.dijonparking.R;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class Map extends GDMapActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.map);
        
        final MapView map = (MapView) findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        map.getController().setZoom(12);
        //Carter centrer sur Dijon au dméarrage
        final GeoPoint initPoint = new GeoPoint(47318575, 5038309);
        map.getController().setCenter(initPoint);
        
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}