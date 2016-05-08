package com.chinesedictionary.app;


import java.util.ArrayList;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Danny on 11/03/14.
 */
public class MyArrayAdapter extends ArrayAdapter<DictionaryData> {
    private final Context context;
    private ArrayList<DictionaryData> itemsArrayList;
    protected String searchKey;

    public MyArrayAdapter(Context context, ArrayList<DictionaryData> itemsArrayList, String search) {

        super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.searchKey = search;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        LinearLayout rowView = (LinearLayout) inflater.inflate(R.layout.row, parent, false);

        // 3. Get the two text view from the rowView
        TextView textViewChineseChars = (TextView) rowView.findViewById(R.id.textViewChineseChars);
        TextView textViewCanto = (TextView) rowView.findViewById(R.id.textViewCanto);
        TextView textViewMandarin = (TextView) rowView.findViewById(R.id.textViewMandarin);
        TextView textViewDefinition = (TextView) rowView.findViewById(R.id.textViewDefinition);

        if (itemsArrayList.get(0)._id != -1) {
            // 4. Set the text for textView
            textViewChineseChars.setText(itemsArrayList.get(position).getChineseChars(searchKey));
            textViewCanto.setText(itemsArrayList.get(position).getCantoneseTone(searchKey));
            textViewMandarin.setText(itemsArrayList.get(position).getMandarinTone(searchKey));
            textViewDefinition.setText(itemsArrayList.get(position).getDefinition(searchKey));
        } else {
            textViewChineseChars.setText("");
            textViewCanto.setText("");
            textViewMandarin.setText("");
            textViewDefinition.setText("");
        }

        // setup click listener for each row
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (itemsArrayList.get(0)._id != -1) {
                    Intent i = new Intent(context, DictionaryItemActivity.class);
                    i.putExtra("traditionalWords", itemsArrayList.get(position).getTraditionalChineseChars(searchKey));
                    i.putExtra("simplifiedWords", itemsArrayList.get(position).getSimplifiedChineseChars(searchKey));
                    i.putExtra("yale", itemsArrayList.get(position).getCantoneseTone(searchKey).toString());
                    i.putExtra("pinyin", itemsArrayList.get(position).getMandarinTone(searchKey).toString());
                    i.putExtra("meaning", itemsArrayList.get(position).getDefinition(searchKey).toString());

                    context.startActivity(i);
                }

            }

        });




        // 5. return rowView
        return rowView;
    }
}


