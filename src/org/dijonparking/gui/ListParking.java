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

import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.LoaderActionBarItem;
import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionBar;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.util.ArrayList;
import java.util.Comparator;

import org.dijonparking.R;
import org.dijonparking.util.StaticPreferences;
import org.dijonparking.xml.DownloaderAndParser;
import org.dijonparking.xml.Parking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ListParking extends GDListActivity  implements LocationListener {
	private ListView listView;
    private ListParkingAdapter adapter;
	private QuickActionBar mBar;
	private ArrayList<Parking> parkings;
	private LocationManager locationManager;
	private int positionListvClicked;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareQuickAction();
        listView = getListView();
        getActionBar().setType(greendroid.widget.ActionBar.Type.Empty);
        getActionBar().addItem(Type.Refresh);
        getActionBar().addItem(Type.Settings);
        getActionBar().addItem(Type.Help);
        
    	new DownloadAndParseTask(this).execute();
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				positionListvClicked = position;
				if (parkings.get(position).getLatitude() != 0 && parkings.get(position).getLongitude() != 0) {
					mBar.show(arg1);
				}
				else {
					startInfoActivity();
				}
			}
			});
    }
    
    @Override
    public void onResume() {
    	super.onResume();
        String prefTri = PreferenceManager.getDefaultSharedPreferences(this).getString("tri", "1");
        StaticPreferences.setTri(Integer.valueOf(prefTri));
       	startGps();
       	updateListView();
    }
    
    @Override
    public void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.listparking, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.refresh:
	    	new DownloadAndParseTask(this).execute();
			return true;
		case R.id.settings:
			startActivity(new Intent(this, Preferences.class));
			return true;
		case R.id.about:
            AlertDialog.Builder about = new AlertDialog.Builder(this);
            about.setTitle(R.string.about);
            View changes = getLayoutInflater().inflate(R.layout.about, null);
            ((TextView) changes.findViewById(R.id.version_about)).setText("Dijon Parking "+getText(R.string.version_name));
            ((TextView) changes.findViewById(R.id.description_about)).setText(getText(R.string.descriptionabout));
            about.setView(changes).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    }
    
    @Override
    public boolean onHandleActionBarItemClick (ActionBarItem item, int position) {
    	switch (position) {
		case 0:
			new DownloadAndParseTask(this).execute();
			((LoaderActionBarItem) item).setLoading(false);
			return true;
		case 1:
			startActivity(new Intent(this, Preferences.class));
			return true;
		case 2:
			AlertDialog.Builder help = new AlertDialog.Builder(this);
			help.setTitle(R.string.significationcouleurpoint);
			View changes = getLayoutInflater().inflate(R.layout.help, null);
			help.setView(changes).show();
			return true;
		default:
			return super.onHandleActionBarItemClick(item, position);
		}
    	
    }
    
	@Override
	public void onLocationChanged(Location location) {
		updateLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
    
	private class DownloadAndParseTask extends DownloaderAndParser {

		public DownloadAndParseTask(Context context) {
			super(context);
		}
		@Override
		protected void finalOperations(ArrayList<Parking> listParking) {
			//Évite d'avoir à recalculer la distance après une mise à jour
            if (parkings != null) {
                for(Parking park : parkings) {
                        int res = listParking.indexOf(park);
                        if (res != -1 && park.getDistance() >= 0)
                                listParking.get(res).setDistance(park.getDistance());
                }
            }
            parkings = listParking;
        
            updateListView();
		}
		
		@Override
		protected void restartTask() {
			new DownloadAndParseTask(getContext()).execute();
			
		}
		
	}
	
    private void updateListView() {
    	if (adapter == null) {
    		if (parkings != null) {
    			adapter = new ListParkingAdapter(this, parkings);
    			listView.setAdapter(adapter);
    		}
    	}
    	else {
    		adapter.sort(new Comparator<Parking>() {
				@Override
				public int compare(Parking object1, Parking object2) {
					return object1.compareTo(object2);
				}
			});
    	}
    }
    
    private void startGps() {
    	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	
    	Criteria criteria = new Criteria();
    	criteria.setAccuracy(Criteria.ACCURACY_FINE);
    	criteria.setAltitudeRequired(false);
    	criteria.setBearingRequired(false);
    	criteria.setCostAllowed(false);
    	criteria.setSpeedRequired(false);
    	
    	String bestProvider = locationManager.getBestProvider(criteria, false);
    	
    	locationManager.requestLocationUpdates(bestProvider, 30000, 40, this);
    }

    private void updateLocation(Location location) {
    	//Parcourt de liste de parking pour mettre à jour la distance
    	if (parkings != null)
    		for (Parking parking : parkings) {
    			parking.setDistance(location);
    		}
    	
    	updateListView();
    }
    
    private void startInfoActivity() {
		Intent it = new Intent(getApplicationContext(), InfoParking.class);
		it.putExtra("parking", parkings.get(positionListvClicked));
		startActivity(it);
    }
    
    private void startRouteActivity() {
    	//Not officially supported
    	String direction = "google.navigation:q="+parkings.get(positionListvClicked).getLatitude()+","+
    															parkings.get(positionListvClicked).getLongitude();
    	Intent it = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(direction));
    	startActivity(it);
    }
    
	private void prepareQuickAction() {
		mBar = new QuickActionBar(this);
		mBar.addQuickAction(new QuickAction(this, blackDrawable(android.R.drawable.ic_dialog_map), R.string.itineraire));
		mBar.addQuickAction(new QuickAction(this, blackDrawable(R.drawable.ic_dialog_info), R.string.info));
		
		mBar.setOnQuickActionClickListener(new OnQuickActionClickListener() {
			
			@Override
			public void onQuickActionClicked(QuickActionWidget widget, int position) {
				switch (position) {
				case 0:
					startRouteActivity();
					break;
				case 1:
					startInfoActivity();
					break;
				default:
					break;
				}
				
			}
		});
	}
	
	private Drawable blackDrawable(int drawableId) {
		Drawable d = getResources().getDrawable(drawableId);
		d.setColorFilter(new LightingColorFilter(Color.BLACK, Color.BLACK));
		return d;
	}
}