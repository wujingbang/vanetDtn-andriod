package android.geosvr.dtn.servlib.routing.prophet.queuing;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.geosvr.dtn.servlib.bundling.Bundle;
import android.geosvr.dtn.servlib.storage.BundleStore;
import android.util.Log;

public class Mofo extends ProphetQueuing {

	private static final String TAG = "ForwardedTimes";

	/**
	 * SQLiteImplementation object
	 */
	private SQLiteDatabase db = BundleStore.getImpt_sqlite_().getDb();

	public int getLastBundle() {
		Cursor cursor = db.query("bundles", null, null, null, null, null,
				"forwarded_times Desc, id Desc", null);
		int forwardColumn = cursor.getColumnIndex("forwarded_times");
		int fieldColumn = cursor.getColumnIndex("id");
		if (cursor == null) {
			Log.d(TAG, "Row not found!");
			return -1;
		}

		if (!cursor.moveToFirst()) {
			return -1;
		}

		Log.i("Queuing", "Deleting bundle ft: " + cursor.getInt(forwardColumn));
		int result = cursor.getInt(fieldColumn);
		cursor.close();

		return result;
	}
}