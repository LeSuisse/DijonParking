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
import greendroid.graphics.drawable.DrawableStateSet;
import greendroid.graphics.drawable.MapPinDrawable;

import java.util.ArrayList;

import org.dijonparking.R;
import org.dijonparking.xml.Parking;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class Map extends GDMapActivity {
	private static final int[] PRESSED_STATE = {android.R.attr.state_pressed};
	private static final int E6 = (int) Math.pow(10,6);
	
	private MapView map;
	
	private ArrayList<Parking> parkings;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.map);
        
        map = (MapView) findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        map.getController().setZoom(12);
        //Carte centrée sur Dijon au démarrage
        map.getController().setCenter(new GeoPoint(47318575, 5038309));
        
        parkings = (ArrayList<Parking>) getIntent().getExtras().get("parkings");
        
        updateMapPin();
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private void updateMapPin() {
		final Resources r = getResources();
		
		for (Parking parking : parkings) {
			double ratio = -1;
			
			if (parking.getNbPlaceDispoTotal() >= 0 && parking.getCapTotale() >= 0)
				ratio = (double) parking.getNbPlaceDispoTotal() / (double) parking.getCapTotale();
			
			BasicItemizedOverlay itemizedOverlay = new BasicItemizedOverlay(new MapPinDrawable(r, getColorStateList(ratio), getColorStateList(ratio)));
			final GeoPoint pos = new GeoPoint((int) (parking.getLatitude()*E6), (int) (parking.getLongitude()*E6));

			itemizedOverlay.addOverlay(new OverlayItem(pos, parking.getNom(), null));
			
			map.getOverlays().add(itemizedOverlay);
		}
	}
	
	private class BasicItemizedOverlay extends ItemizedOverlay<OverlayItem> {

        private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

        public BasicItemizedOverlay(Drawable defaultMarker) {
            super(boundCenterBottom(defaultMarker));
        }

        public void addOverlay(OverlayItem overlay) {
            mOverlays.add(overlay);
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
        protected boolean onTap(int index) {
            return true;
        }

    }
	
	private ColorStateList getColorStateList(double ratio) {
		int[][] states = new int[2][];
        int[] colors = new int[2];

        final int color = getColor(ratio);

        states[0] = PRESSED_STATE;
        colors[0] = addRGB(color, -50);

        states[1] = DrawableStateSet.EMPTY_STATE_SET;
        colors[1] = color;

        return new ColorStateList(states, colors);
	}
	
	private static final int getColor(double ratio) {
		int r, g, b;
		
		if (ratio < 0)
			r = g = b = 171;
		else if (ratio < 0.2) {
			r = 222;
			g = 0;
			b = 56;
		}
		else if (ratio < 0.4) {
			r = 255;
			g = 140;
			b = 0;
		}
		else {
			r = 113;
			g = 211;
			b = 0;
		}
		
		return Color.rgb(r, g, b);
	}

	private static int addRGB(int color, int amount) {
        int r = constrain(Color.red(color) + amount, 0, 255);
        int g = constrain(Color.green(color) + amount, 0, 255);
        int b = constrain(Color.blue(color) + amount, 0, 255);
        return Color.rgb(r, g, b);
    }
	
	private static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }
}