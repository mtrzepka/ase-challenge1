package com.umkc.sg11.grocerybuddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.umkc.sg11.grocerybuddy.RecipeContract.RecipeEntry;

public class CurateRecipeActivity extends Activity {

	RecipeDbHelper mDbHelper = new RecipeDbHelper(this);
	SQLiteDatabase db;
	EditText editTitle;
	EditText editIngredients;
	EditText editDirections;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curate_recipe);
		editTitle = (EditText) findViewById(R.id.editTextTitle);
		editIngredients = (EditText) findViewById(R.id.editTextIngredients);
		editDirections = (EditText) findViewById(R.id.editTextDirections);
		db = mDbHelper.getWritableDatabase();
	}
	
	/** Called when the user clicks the Add Recipe button */
	public void insertRecipe(View view) {		
		String title = editTitle.getText().toString().trim();
		String ingredients = editIngredients.getText().toString().trim();
		String directions = editDirections.getText().toString().trim();
		
		if (title.isEmpty() || ingredients.isEmpty() || directions.isEmpty()) {
			createDialog("Input Error", "All of the fields are required!");
			return;
		}
		
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(RecipeEntry.COLUMN_NAME_TITLE, title);
		values.put(RecipeEntry.COLUMN_NAME_INGREDIENTS, ingredients);
		values.put(RecipeEntry.COLUMN_NAME_DIRECTIONS, directions);

		// Insert the new row, returning the primary key value of the new row
		try {
			db.insertOrThrow(RecipeEntry.TABLE_NAME, null, values);
		} catch(SQLException e) {
			if (e.getMessage().contains("unique")) {
				createDialog("Insert Error", "A Recipe with this name already exists");
			} else {
				createDialog("Insert Error", "Error saving Recipe!");
			}
			
			return;
		}
		
		System.out.println("Inserting Recipe");
		createDialog("Saved a Recipe", "Added " + title);
		clearRecipe(this.getCurrentFocus());
	}
	
	/** Called when the user clicks the Find Recipe button */
	public void findRecipe(View view) {
		String title = editTitle.getText().toString().trim();
		
		if (title.isEmpty()) {
			createDialog("Input Error", "Please provide a recipe to search for");
			return;
		}
		
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    RecipeEntry.COLUMN_NAME_TITLE,
		    RecipeEntry.COLUMN_NAME_INGREDIENTS,
		    RecipeEntry.COLUMN_NAME_DIRECTIONS,
		    };
		
		String selection = RecipeEntry.COLUMN_NAME_TITLE + "=?";
		String[] selectionArgs = {title};
		
		// How you want the results sorted in the resulting Cursor
		String sortOrder = RecipeEntry._ID;
		
		Cursor cursor = db.query(
			    RecipeEntry.TABLE_NAME,  // The table to query
			    projection,              // The columns to return
			    selection,               // The columns for the WHERE clause
			    selectionArgs,           // The values for the WHERE clause
			    null,                    // don't group the rows
			    null,                    // don't filter by row groups
			    sortOrder                // The sort order
			    );
		
		boolean found = cursor.moveToFirst();
		
		if (found) {
			String ingredients = cursor.getString(
			    cursor.getColumnIndexOrThrow(RecipeEntry.COLUMN_NAME_INGREDIENTS));
			String directions = cursor.getString(
				    cursor.getColumnIndexOrThrow(RecipeEntry.COLUMN_NAME_DIRECTIONS));
			
			editIngredients.setText(ingredients);
			editDirections.setText(directions);
		} else {
			clearRecipe(this.getCurrentFocus());
			editTitle.setText(title);
			createDialog("No Recipe Found", "Could not find " + title);
		}
	}
	
	/** Called when the user clicks the Delete Recipe button */
	public void deleteRecipe(View view) {
		String title = editTitle.getText().toString().trim();
		
		if (title.isEmpty()) {
			createDialog("Input Error", "Please provide a recipe to delete");
			return;
		}
		
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setTitle("Delete Recipe")
		.setMessage("Are you sure you want to delete " + title + "?");

		// Add the buttons
		builder.setPositiveButton("Ok", new DeleteListener(this.getCurrentFocus()));
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog
		           }
		       });

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		
		// 4. Display the Dialog
		dialog.show();
	}
	
	public void clearRecipe(View view) {
		editTitle.setText("");
		editIngredients.setText("");
		editDirections.setText("");
	}
	
	private void createDialog(String title, String message) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setTitle(title).setMessage(message).setPositiveButton("Ok", null);

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		
		// 4. Display the Dialog
		dialog.show();
	}
	
	private class DeleteListener implements OnClickListener {
		String title = editTitle.getText().toString().trim();
		String selection = RecipeEntry.COLUMN_NAME_TITLE + "=?";
		String[] selectionArgs = {title};
		View view;
		
		DeleteListener(View v) {
			view = v;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			int delCount = db.delete(RecipeEntry.TABLE_NAME, selection, selectionArgs);
			
			if (delCount == 0) {
				createDialog("Delete Error", "Could not delete " + title);
			} else {
				createDialog("Delete Successful", "Deleted " + title);
				clearRecipe(view);
			}
		}
	}
}
