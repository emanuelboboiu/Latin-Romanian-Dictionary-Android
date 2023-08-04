package ro.limbalatina.dictionarlatinroman;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Class started on Sunday, 31 May 2015, created by Emanuel Boboiu.
 * This is the most useful class to work with SQLite database.
 * */

public class DataBaseHelper extends SQLiteOpenHelper {
    // private static String TAG = "DataBaseHelper"; // Tag just for the LogCat
    // window
    // destination path (location) of our database on device
    private static String DB_PATH = "";
    private static final String DB_NAME = "dictionar.db"; // Database name
    private static final int DATABASE_VERSION = 8;
    private static final String SP_KEY_DB_VER = "dbVer";

    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        // Get the path to our DB:
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
        this.mContext = context;
        initialise();
    } // end constructor.

    /**
     * Initialises database. Creates database if doesn't exist. Deletes the
     * DataBase if version is different.
     */
    private void initialise() {
        if (checkDataBase()) {
            Settings set = new Settings(mContext);
            int dbVersion = set.getIntSettings(SP_KEY_DB_VER);
            if (DATABASE_VERSION != dbVersion) {
                File dbFile = mContext.getDatabasePath(DB_NAME);
                if (!dbFile.delete()) {
                    // Make a toast with a message:
                    GUITools.toast(mContext.getString(R.string.warning_db_cannot_be_deleted), 1000, mContext);
                } // end if file was not deleted.
            } // end if the versions are different.
        } // end if DataBase exits.
        if (!checkDataBase()) {
            try {
                createDataBase();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        } // end if DataBase doesn't exist.
    } // end initialise.

    public void createDataBase() throws IOException {
        // If database not exists copy it from the assets

        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                // Copy the database from assets:
                copyDataBase();
                // Log.e(TAG, "createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        } // end if DataBase doesn't exist.
    } // end createDatabase method.

    // Check that the database exists here: /data/data/your
    // package/databases/DatabaseName:
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH);
        // Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    } // end check if file exists, checkDataBase() method.

    // Copy the database from assets
    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
        Settings set = new Settings(mContext);
        set.saveIntSettings(SP_KEY_DB_VER, DATABASE_VERSION);
    } // end copy database into DB folder.

    // Open the database, so we can query it
    public void openDataBase() throws SQLException {
        String mPath = DB_PATH;
        // Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}// end DataBaseHelper.
