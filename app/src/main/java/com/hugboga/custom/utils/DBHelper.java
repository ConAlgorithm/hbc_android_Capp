package com.hugboga.custom.utils;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hugboga.custom.constants.ResourcesConstants;
import com.hugboga.custom.data.bean.AirPort;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by admin on 2015/7/21.
 */
public class DBHelper extends SQLiteOpenHelper {


    //The Android's default system path of your application database.
    public  static String DB_PATH = "/data/data/com.hugboga.custom/databases/";

    public static String DB_NAME = "hbcv2.2.7.db";


    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /*public DbUtils getDbUtils() {
        int dbVersion = new SharedPre(myContext).getIntValue(SharedPre.RESOURCES_DB_VERSION, ResourcesConstants.RESOURCES_DB_VERSION_DEFAULT);
        MLog.e("dbVersion=" + dbVersion);
        DbUtils db = DbUtils.create(myContext, DB_PATH, DB_NAME,dbVersion,null);
        MLog.e("dbVersion2=" + db.getDatabase().getVersion());
        db.configDebug(Config.DEBUG_ENABLE);
        return db;
    }*/

    public DbManager getDbManager(){
        DbManager db = x.getDb(DaoConfig.getInstance(myContext));
        return db;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase(int dbVersion) throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                deleteOldDb();
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    /**
     * 删除老数据库
     */
    public void deleteOldDb(){
        File fileDir = new File(DB_PATH);
        if(fileDir.exists() && fileDir.isDirectory()){
            File[] files = fileDir.listFiles();
            for(File file:files){
                String fileName = file.getName();
                if(fileName.startsWith("hbc") && !fileName.equals(DB_NAME) && (fileName.endsWith(".db") || fileName.endsWith(".db-journal"))){
                    file.delete();
                }
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase() {

        boolean checkDB = false;
//        DbUtils db = getDbUtils();
        DbManager db = getDbManager();
        try {
            db.getDatabase();
//            checkDB = db.tableIsExist(AirPort.class);
            checkDB = db.getTable(AirPort.class).tableIsExist();
            MLog.e("checkDataBase " + checkDB);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return checkDB;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    public void copyDataBase() throws IOException {
MLog.e("copyDataBase");
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public static boolean copyFile(File file,File newFile){
        boolean flag = false;
        try {
            InputStream myInput = new FileInputStream(file);
            OutputStream myOutput = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int length;
            try {
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                //Close the streams
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            }finally {
                myOutput.flush();
                myOutput.close();
                myInput.close();
                flag = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}