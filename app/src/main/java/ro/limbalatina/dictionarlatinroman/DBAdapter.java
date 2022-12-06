package ro.limbalatina.dictionarlatinroman;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {

    // protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private final DataBaseHelper mDbHelper;

    public DBAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DBAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            // Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DBAdapter open() throws SQLException {
        mDbHelper.openDataBase();
        mDbHelper.close();
        mDb = mDbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Cursor queryData(String sql) {
        Cursor mCur = mDb.rawQuery(sql, null);
        if (mCur != null) {
            mCur.moveToNext();
        }
        return mCur;
    }

} // end class TestAdapter.
