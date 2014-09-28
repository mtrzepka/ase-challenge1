package com.umkc.sg11.grocerybuddy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.umkc.sg11.grocerybuddy.RecipeContract.RecipeEntry;

public class RecipeDbHelper extends SQLiteOpenHelper {
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_RECIPES =
	    "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
	    RecipeEntry._ID + " INTEGER PRIMARY KEY," +
	    RecipeEntry.COLUMN_NAME_TITLE + TEXT_TYPE + " UNIQUE" + COMMA_SEP +
	    RecipeEntry.COLUMN_NAME_INGREDIENTS + TEXT_TYPE + COMMA_SEP +
	    RecipeEntry.COLUMN_NAME_DIRECTIONS + TEXT_TYPE +
	    " )";

	private static final String SQL_DELETE_RECIPES =
	    "DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME;
	
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Recipe.db";

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RECIPES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // first version
    }
}
