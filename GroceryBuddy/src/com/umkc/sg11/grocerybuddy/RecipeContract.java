package com.umkc.sg11.grocerybuddy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class RecipeContract {
	// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
	RecipeContract() {}
	
	/* Inner class that defines the table contents */
	public static abstract class RecipeEntry implements BaseColumns {
		public static final String TABLE_NAME = "recipes";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_INGREDIENTS = "ingredients";
		public static final String COLUMN_NAME_DIRECTIONS = "directions";
	}
}
