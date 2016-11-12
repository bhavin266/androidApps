package com.cs478.project.chicagohub;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class RestaurantActivity extends AppCompatActivity implements ListFragment.OnListFragmentInteractionListener {

    private FragmentManager mFragmentManager;
    private FrameLayout restuarantListLayout, restaurantImageLayout;
    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private final ImageFragment imageFragment = ImageFragment.newInstance(R.array.RestaurantsImages);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restuarantListLayout = (FrameLayout) findViewById(R.id.list_fragment_container);
        restaurantImageLayout = (FrameLayout) findViewById(R.id.image_fragment_container);
        Toolbar myToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setTitle("ChicagoHub > Restaurants");
        mFragmentManager = getFragmentManager();
        // Start a new Fragment transaction
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        //Passing restaurantList to ListFragment
        fragmentTransaction.add(R.id.list_fragment_container, ListFragment.newInstance(R.array.Restaurants));
        fragmentTransaction.commit();

        // For the back button press requirement specification
        mFragmentManager
                .addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        updateLayout();
                    }
                });
    }

    private void updateLayout() {
        // Check if image is being displayed
        if (!imageFragment.isAdded()) {
            restuarantListLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT));

        } else if (imageFragment.isAdded() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Show only list of restaurants
            restuarantListLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0, MATCH_PARENT));

            restaurantImageLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                    MATCH_PARENT));
        } else {
            // Make the List take 1/3 of the layout's width
            restuarantListLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 1f));

            // Let Image take 2/3's of the layout's width
            restaurantImageLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 2f));
        }
    }

    @Override
    public void onListSelection(int index) {
        // If the QuoteFragment has not been added, add it now
        if (!imageFragment.isAdded()) {
            // Start a new FragmentTransaction
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();

            // Add the Image fragment to the layout
            fragmentTransaction.add(R.id.image_fragment_container, imageFragment);

            // Add this FragmentTransaction to the backstack
            fragmentTransaction.addToBackStack(null);

            // Commit the FragmentTransaction
            fragmentTransaction.commit();

            // Force Android to execute the committed FragmentTransaction
            mFragmentManager.executePendingTransactions();
        }

        imageFragment.displayImage(index);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hotels, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (item.getItemId()) {
            case R.id.action_hotels:
                Intent intent = new Intent(this, HotelActivity.class);
                startActivity(intent);
                break;
            default:
                return false;
        }
        return true;

    }
}
