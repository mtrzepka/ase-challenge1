package com.umkc.sg11.grocerybuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	/** Called when the user clicks the Find Grocery Store button */
	public void findStore(View view) {
		Intent intent = new Intent(this, FindGroceryActivity.class);
		startActivity(intent);
	}
	
	/** Called when the user clicks the Curate Recipes button */
	public void curateRecipes(View view) {
		Intent intent = new Intent(this, CurateRecipeActivity.class);
		startActivity(intent);
	}
}
