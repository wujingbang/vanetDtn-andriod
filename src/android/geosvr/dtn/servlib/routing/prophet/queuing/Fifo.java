package android.geosvr.dtn.servlib.routing.prophet.queuing;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.geosvr.dtn.servlib.bundling.Bundle;
import android.geosvr.dtn.servlib.storage.BundleStore;
import android.util.Log;


public class Fifo extends ProphetQueuing {

	private static final String TAG = "Fifo";

	/**
	 * SQLiteImplementation object
	 */
	private SQLiteDatabase db = BundleStore.getImpt_sqlite_().getDb();

	@Override
	public int getLastBundle() {
		Cursor cursor = db.query("bundles", null, null, null, null, null,
				"id Asc", null);
		int fieldColumn = cursor.getColumnIndex("id");
		if (cursor == null) {
			Log.d(TAG, "Row not found!");
			return -1;
		}

		if (!cursor.moveToFirst()) {
			return -1;
		}

		int result = cursor.getInt(fieldColumn);
		cursor.close();

		return result;
	}
}
