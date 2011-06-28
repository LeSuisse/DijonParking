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

import java.io.IOException;
import java.util.ArrayList;

import org.dijonparking.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public abstract class DownloaderAndParser extends AsyncTask<Void, Void, ArrayList<Parking>> {
	private static enum TypeErreur {NETWORK, NULL, MISC};
	
	private Context context;
	private ProgressDialog dialog;
	private TypeErreur erreur;

	public DownloaderAndParser(Context context) {
		super();
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(context);
		dialog.setMessage(context.getText(R.string.loading));
		dialog.show();
	}
	
	@Override
	protected ArrayList<Parking> doInBackground(Void... params) {
		ArrayList<Parking> listParking = null;
		try {
			listParking = ContainerParking.getParkings();
		}
		catch (IOException e) {
			erreur = TypeErreur.NETWORK;
		}
		catch (NullPointerException e) {
			erreur = TypeErreur.NULL;
		}
		catch (Exception e) {
			erreur = TypeErreur.MISC;
		}
		return listParking;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Parking> listParking) {
		dialog.dismiss();
		if (listParking == null) {
			switch (erreur) {
			case NETWORK:
				alertDialogError(context.getText(R.string.network_error));
				break;
			case NULL:
				alertDialogError(context.getText(R.string.nullerror));
				break;
			default:
				alertDialogError(context.getText(R.string.internalerror));
				break;
			}
		}
		else {
			finalOperations(listParking);
		}
	}

	private void alertDialogError(CharSequence mess) {
		Log.i("Dijon Parking", "IE");
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setMessage(mess)
    		   .setCancelable(false)
    		   .setPositiveButton(context.getText(R.string.retry), new DialogInterface.OnClickListener() {
    		@Override
			public void onClick(DialogInterface dialog, int id) {
    			restartTask();
    		}
    	})
    	.setNegativeButton(context.getText(R.string.quit), new DialogInterface.OnClickListener() {
    		@Override
			public void onClick(DialogInterface dialog, int id) {
    			((Activity) context).finish();
    		}
    	});
    	builder.show();
	}
	
	public Context getContext() {
		return context;
	}

	protected abstract void finalOperations(ArrayList<Parking> listParking);
	protected abstract void restartTask();

}