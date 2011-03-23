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

import org.dijonparking.R;
import org.dijonparking.xml.ContainerParking;
import org.dijonparking.xml.Parking;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ListParking extends ListActivity {
	private ListView listView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listparking);
        
        listView = getListView();
        
        new DownloadAndParse().execute();

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
            ((TextView) changes.findViewById(R.id.version_about)).setText("Dijon Parking 0.0.1");
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
			else
				updateListView(listParking);
		}
    }

    private void updateListView(ArrayList<Parking> park) {
    	listView.setAdapter(new ListParkingAdapter(this, park));
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
}