package com.umkc.sg11.grocerybuddy;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class FindGroceryActivity extends Activity implements LocationListener {

	private WebView webView;
	private LocationManager locationManager;
	private String provider;
	int lat;
	int lng;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_grocery);
		
		webView = (WebView) findViewById(R.id.webViewFindGrocery);
		webView.getSettings().setJavaScriptEnabled(true);
		
		// Get the location manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the location provider -> use
	    // default
	    Criteria criteria = new Criteria();
	    List<String> providers = locationManager.getProviders(true);
	    
	    for(String provider : providers) {
	    	System.out.println("Provider available: " + provider);
	    }
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);

	    // Initialize the location fields
	    if (location != null) {
	      System.out.println("Provider " + provider + " has been selected.");
	      onLocationChanged(location);
	    } else {
	    	System.out.println("Location Unavailable");
	    }
	    
	    webView.loadUrl("https://maps.google.com/?q=Grocery+Store&center=" + lat + "," + lng);
	}

	@Override
	public void onLocationChanged(Location location) {
		lat = (int) (location.getLatitude());
	    lng = (int) (location.getLongitude());
	    System.out.println("Lat: " + lat + " Long: " + lng);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}
	
	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}
}
