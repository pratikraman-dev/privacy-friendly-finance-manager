/*
 This file is part of Privacy Friendly App Finance Manager.

 Privacy Friendly App Finance Manager is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly App Finance Manager is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly App Finance Manager. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlyfinance.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David Meiborg
 * @version 20180710
 *
 * This class defines the structure of our database for the Categories.
 */

public class CategorySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 15;

    /**
     * Use the following pattern for the name of the database
     * PF_[Name of the app]_DB
     */
    public static final String DATABASE_NAME = "PF_FinanceManager_DB_Categories";

    //Names of table in the database
    private static final String TABLE_SAMPLEDATA = "CategoryData";

    //Names of columns in the databases in this example we only use one table
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "categoryName";

    public CategorySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /**
         * Create the table sample data on the first start
         * Be careful with the final line of the query and the SQL syntax that is used in the String.
         */
        String CREATE_SAMPLEDATA_TABLE = "CREATE TABLE " + TABLE_SAMPLEDATA +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_SAMPLEDATA_TABLE);

        String categoryName = MainActivity.getDefaultCategory();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,categoryName);
        sqLiteDatabase.insert(TABLE_SAMPLEDATA,null,values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SAMPLEDATA);

        onCreate(sqLiteDatabase);
    }


    /**
     * Adds a single sampleData to our Table
     * As no ID is provided and KEY_ID is autoincremented (see line 50)
     * the last available key of the table is taken and incremented by 1
     * @param sampleData data that will be added
     */
    public void addSampleData(CategoryDataType sampleData) {
        SQLiteDatabase database = this.getWritableDatabase();

        //To adjust this class for your own data, please add your values here.
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,sampleData.getCategoryName());

        database.insert(TABLE_SAMPLEDATA, null, values);
        database.close();
    }


    /**
     * This method returns all data from the DB as a list
     * This could be used for instance to fill a recyclerView
     * @return A list of all available sampleData in the Database
     */
    public List<CategoryDataType> getAllSampleData() {
        List<CategoryDataType> sampleDataList = new ArrayList<CategoryDataType>();

        String selectQuery = "SELECT  * FROM " + TABLE_SAMPLEDATA;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        CategoryDataType sampleData = null;

        if (cursor.moveToFirst()) {
            do {
                //To adjust this class for your own data, please add your values here.
                //be careful to use the right get-method to get the data from the cursor
                sampleData = new CategoryDataType();
                sampleData.setID(Integer.parseInt(cursor.getString(0)));
                sampleData.setCategoryName(cursor.getString(1));

                sampleDataList.add(sampleData);
            } while (cursor.moveToNext());
        }

        return sampleDataList;
    }

    /**
     * Updates a database entry.
     * @param sampleData
     * @return actually makes the update
     */
    public void updateSampleData(CategoryDataType sampleData) {
        SQLiteDatabase database = this.getWritableDatabase();

        //To adjust this class for your own data, please add your values here.
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,sampleData.getCategoryName());

        database.update(TABLE_SAMPLEDATA, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sampleData.getID()) });

        database.close();

    }
    /**
     * Deletes sampleData from the DB
     * This method takes the sampleData and extracts its key to build the delete-query
     * @param sampleData that will be deleted
     */
    public void deleteSampleData(CategoryDataType sampleData) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_SAMPLEDATA, KEY_ID + " = ?",
                new String[] { Integer.toString(sampleData.getID()) });
        //always close the DB after deletion of single entries
        database.close();
    }

    /**
     * deletes all sampleData from the table.
     * This could be used in case of a reset of the app.
     */
    public void deleteAllSampleData() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from "+ TABLE_SAMPLEDATA);
    }


}
