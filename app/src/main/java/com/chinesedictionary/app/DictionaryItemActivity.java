package com.chinesedictionary.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class DictionaryItemActivity extends ActionBarActivity {

    String traditionalChineseChars;
    String simplifiedChineseChars;
    String pinyin;
    String yale;
    String meaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_item_activity);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the strings in the class declaration.
        Intent intent = getIntent();

        traditionalChineseChars = intent.getStringExtra("traditionalWords");
        TextView textViewTraditionalChars = (TextView) findViewById(R.id.traditionaltextView);
        textViewTraditionalChars.setText(traditionalChineseChars);

        simplifiedChineseChars = intent.getStringExtra("simplifiedWords");
        TextView textViewSimplifiedChars = (TextView) findViewById(R.id.simplifiedTextView);
        textViewSimplifiedChars.setText(simplifiedChineseChars);

        yale = intent.getStringExtra("yale");
        yale = yale.substring(6, yale.length());
        TextView textViewJyutping = (TextView) findViewById(R.id.yaleTextView);
        textViewJyutping.setText(yale);

        pinyin = intent.getStringExtra("pinyin");
        pinyin = pinyin.substring(8, pinyin.length());
        TextView textViewPinyin = (TextView) findViewById(R.id.pinyinTextView);
        textViewPinyin.setText(pinyin);


        meaning = intent.getStringExtra("meaning");
        meaning = meaning.substring(9, meaning.length());
        meaning = "• " + meaning;
        meaning = meaning.replaceAll("/","\n• ");
        TextView textViewMeaning = (TextView) findViewById(R.id.meaningTextView);
        textViewMeaning.setText(meaning);

    }


    public void copyTraditional(View v)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("traditional", traditionalChineseChars);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Traditional copied to Clipboard",
                Toast.LENGTH_LONG).show();
    }

    public void copySimplified(View v)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simplified", simplifiedChineseChars);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Simplified copied to Clipboard",
                Toast.LENGTH_LONG).show();
    }

    public void copyMeaning(View v)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("meaning", meaning);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Meaning copied to Clipboard",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.dictionary_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
