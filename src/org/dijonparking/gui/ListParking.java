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

import java.util.ArrayList;
import java.util.Collections;

import org.dijonparking.R;
import org.dijonparking.xml.ContainerParking;
import org.dijonparking.xml.Parking;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListParking extends ListActivity  implements LocationListener {
	private ListView listView;
	private ArrayList<Parking> parkings;
	private LocationManager locationManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listparking);
        startGps();
        listView = getListView();
        
        new DownloadAndParse().execute();
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent it = new Intent(getApplicationContext(), InfoParking.class);
				it.putExtra("parking", parkings.get(position));
				startActivity(it);
				
			}
			});
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	startGps();
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
			new DownloadAndParse().execute();
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
    
    private class DownloadAndParse extends AsyncTask<Void, Void, ArrayList<Parking>> {
    	private ProgressDialog dialog;
    	private boolean error = false;
    	
    	@Override
    	protected void onPreExecute() {
    		dialog = new ProgressDialog(ListParking.this);
    		dialog.setMessage(getText(R.string.loading));
    		dialog.show();
    	}
    	
		@Override
		protected ArrayList<Parking> doInBackground(Void... params) {
			ArrayList<Parking> listParking = null;
			try {
				listParking = ContainerParking.getParkings();
			}
			catch (Exception e) {
				e.printStackTrace();
				error = true;
			}
			return listParking;
		}
    	
		@Override
		protected void onPostExecute(ArrayList<Parking> listParking) {
			dialog.dismiss();
			if (error) {
				internalError();
			}
			else {
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
		}
    }

    private void internalError() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(ListParking.this);
    	builder.setMessage(getText(R.string.internalerror))
    		   .setCancelable(false)
    		   .setPositiveButton(getText(R.string.retry), new DialogInterface.OnClickListener() {
    		@Override
			public void onClick(DialogInterface dialog, int id) {
    			new DownloadAndParse().execute();
    		}
    	})
    	.setNegativeButton(getText(R.string.quit), new DialogInterface.OnClickListener() {
    		@Override
			public void onClick(DialogInterface dialog, int id) {
    			finish();
    		}
    	});
    	builder.show();
    }
    
    private void updateListView() {
    	if (listView.getAdapter() == null )
    		listView.setAdapter(new ListParkingAdapter(this, parkings));
    	else
    		((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
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
    	for (Parking parking : parkings) {
    		parking.setDistance(location);
    	}
    	Collections.sort(parkings);
    }
    
	@Override
	public void onLocationChanged(Location location) {
		updateLocation(location);
		updateListView();
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
}