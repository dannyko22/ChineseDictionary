package com.chinesedictionary.app;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.kobakei.ratethisapp.RateThisApp;


public class MainActivity extends AppCompatActivity {

    DataBaseHelper myDbHelper;
    ListView listView;
    ArrayList<DictionaryData> dictList;
    MyArrayAdapter myArrayAdapter;
    EditText searchTextBox;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDbHelper = new DataBaseHelper(this);

        //Get ListView from activity_main.xml
        listView = (ListView) findViewById(R.id.listView);
        dictList = new ArrayList<DictionaryData>();
        dictList.add(new DictionaryData(-1,"Search"));
        myArrayAdapter = new MyArrayAdapter(this,dictList, "");

        //setListAdapter
        listView.setAdapter(myArrayAdapter);

        initializeAdNetwork();

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;
        }

        searchTextBox = (EditText) findViewById(R.id.searchTextBox);
        searchTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                new searchDictAsyncTask().execute("");
            }
        });
        searchTextBox.requestFocus();

        // Monitor launch times and interval from installation
        RateThisApp.onStart(this);
        // If the criteria is satisfied, "Rate this app" dialog will be shown
        RateThisApp.Config config = new RateThisApp.Config(1,2);
        config.setUrl("market://details?id=com.chinesedictionary.app");
        RateThisApp.init(config);
        RateThisApp.showRateDialogIfNeeded(this);
    }

    private void initializeAdNetwork()
    {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
            }
        });
    }


    private class searchDictAsyncTask extends AsyncTask<String, Integer, String>
    {
        ArrayList<DictionaryData> dictData;

        //call to look up dictionary data
        //this method can't touch the UI
        @Override
        protected String doInBackground(String... s)
        {
            dictData = searchDictionary();
            final String searchText = searchTextBox.getText().toString().trim();


            if (searchText.matches("[a-zA-Z]+")) {
                Collections.sort(dictList, new Comparator<DictionaryData>(){
                    public int compare(DictionaryData s1, DictionaryData s2) {
                        String newSearchText = "/"+searchText+"/";
                        return s2.getDefinitionForSearch(searchText).toString().indexOf(newSearchText) - s1.getDefinitionForSearch(searchText).toString().indexOf(newSearchText);
                    }
                });
            }
            else
            {
                Collections.sort(dictList, new Comparator<DictionaryData>(){
                    public int compare(DictionaryData s1, DictionaryData s2) {
                        return s1.getTraditionalChineseChars(searchText).length() - s2.getTraditionalChineseChars(searchText).length();
                    }
                });
            }


            return "";
        }

        //calling this method to modify UI
        @Override
        protected void onPostExecute(String s) {
            // textview for results;
            TextView textViewResults = (TextView) findViewById(R.id.textViewResults);
            if (dictList.get(0)._id != -1) {
                textViewResults.setText(dictList.size() + " results");
            }
            else
            {
                textViewResults.setText("0 results");
            }

            myArrayAdapter = new MyArrayAdapter(MainActivity.this,dictList, searchTextBox.getText().toString());
            listView.setAdapter(myArrayAdapter);
            myArrayAdapter.notifyDataSetChanged();
            listView.invalidateViews();
        }

    }

    public ArrayList<DictionaryData> searchDictionary()
    {

        String searchText = searchTextBox.getText().toString();


        //dictList.clear();
        dictList = myDbHelper.getDictionaryData(searchText);
        return dictList;



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            aboutMenuItem();
        }
        else if (id == R.id.action_rate_me)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //Try Google play
            intent.setData(Uri.parse("market://details?id=com.chinesedictionary.app"));
            if (!MyStartActivity(intent)) {
                //Market (Google play) app seems not installed, let's try to open a webbrowser
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.chinesedictionary.app"));
                if (!MyStartActivity(intent)) {
                    //Well if this also fails, we have run out of options, inform the user.
                    Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void aboutMenuItem() {



        startActivity(new Intent(this,about_me.class));

    }

    private boolean MyStartActivity(Intent aIntent) {
        try
        {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }

    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
