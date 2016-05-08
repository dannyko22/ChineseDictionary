package com.chinesedictionary.app;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

/**
 * Created by Danny on 08/03/14.
 */
public class DictionaryData {
    //private variables
    int _id;
    String line;

    // constructor.  empty data.
    public DictionaryData(){
        this._id = -1;
        this.line = null;
    }

    // constructor
    public DictionaryData(int id, String phrase){
        this._id = id;
        this.line = phrase;
    }

    //get only the traditional chinese characters
    public String getTraditionalChineseChars(String searchKey) {
        int beginPosition = 0;
        int endPosition = line.indexOf("[") - 1;


        String chineseChars = line.substring(beginPosition, endPosition);
        chineseChars.trim();
        String[] words = {"", ""};
        words = chineseChars.split(" ");
        return words[0];
    }

    // get only the simplified characters.  return "" if nothing is found.
    public String getSimplifiedChineseChars(String searchKey) {
        int beginPosition = 0;
        int endPosition = line.indexOf("[") - 1;

        String chineseChars = line.substring(beginPosition, endPosition);
        chineseChars.trim();
        String[] words = {"", ""};
        words = chineseChars.split(" ");
        if (words.length == 1)
            return "";
        else
            return words[1];
    }

    // get the traditional and simplified characters together.
    public SpannableString getChineseChars(String searchKey)
    {
        int beginPosition = 0;
        int endPosition = line.indexOf("[")-1;
        searchKey = searchKey.trim();

        String chineseChars = line.substring(beginPosition, endPosition);
        chineseChars.trim();
        String[] words = {"",""};
        words = chineseChars.split(" ");

        if (words.length == 1)
        {
            String finalChineseChars = "Traditional: " + words[0];
            SpannableString characters = new SpannableString(finalChineseChars);
            int chineseCharLength = words[0].length() + 13;
            characters.setSpan(new ForegroundColorSpan(Color.BLUE), 13, chineseCharLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            //find highlight
            int beginSearchKeyPosition = finalChineseChars.indexOf(searchKey.trim(), 12);
            int endSearchKeyPosition = beginSearchKeyPosition + searchKey.length();

            // if search key is found, color the search key
            if (beginSearchKeyPosition != -1) {
                characters.setSpan(new BackgroundColorSpan(Color.YELLOW), beginSearchKeyPosition, endSearchKeyPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            return characters;
        }
        else
        {
            String traditionalChineseChars = "Traditional: " + words[0];
            String simplifiedChineseChars = " Simplified: " + words[1];
            SpannableString characters = new SpannableString(traditionalChineseChars + simplifiedChineseChars);
            int chineseTraditionalCharLength = words[0].length() + 13;
            int chineseSimplifiedCharLength = words[1].length() + chineseTraditionalCharLength+ 13;

            characters.setSpan(new ForegroundColorSpan(Color.BLUE), 13, chineseTraditionalCharLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            characters.setSpan(new ForegroundColorSpan(Color.RED), chineseTraditionalCharLength+13, chineseSimplifiedCharLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            //find first highlight
            int beginFirstSearchKeyPosition = traditionalChineseChars.indexOf(searchKey.trim(), 12);
            int endFirstSearchKeyPosition = beginFirstSearchKeyPosition + searchKey.length();

            // if search key is found, color the search key
            if (beginFirstSearchKeyPosition != -1) {
                characters.setSpan(new BackgroundColorSpan(Color.YELLOW), beginFirstSearchKeyPosition, endFirstSearchKeyPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //find second highlight in simplified
            int beginSecondSearchKeyPosition = simplifiedChineseChars.indexOf(searchKey.trim(), 12);
            int endSecondSearchKeyPosition = beginSecondSearchKeyPosition + searchKey.length();

            // if search key is found, color the search key
            if (beginSecondSearchKeyPosition != -1) {
                characters.setSpan(new BackgroundColorSpan(Color.YELLOW), chineseTraditionalCharLength+beginSecondSearchKeyPosition, chineseTraditionalCharLength+endSecondSearchKeyPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            return characters;
        }
    }


    public SpannableString getCantoneseTone(String searchKey)
    {
        int beginPosition = line.indexOf("[")+1;
        int endPosition = line.indexOf("]");
        searchKey = searchKey.trim();

        String cantoneseTone = new String("Yale: " + line.substring(beginPosition, endPosition));
        SpannableString cantoneseSpanString = new SpannableString(cantoneseTone);
        cantoneseSpanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, 5, 0);


        int beginSearchKeyPosition = cantoneseTone.toLowerCase().indexOf(searchKey.trim().toLowerCase(), 5);
        int endSearchKeyPosition = beginSearchKeyPosition + searchKey.length();

        // if search key is found, color the search key
        if (beginSearchKeyPosition != -1) {
            cantoneseSpanString.setSpan(new BackgroundColorSpan(Color.YELLOW), beginSearchKeyPosition, endSearchKeyPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return cantoneseSpanString;
    }

    public SpannableString getMandarinTone(String searchKey)
    {
        StringBuilder tempLine = new StringBuilder();
        searchKey = searchKey.trim();

        int beginPosition = line.indexOf("]")+3;
        tempLine.append(line.substring(beginPosition,line.toString().length()));

        int endPosition = tempLine.indexOf("]");

        String mandarinTone = new String("Pinyin: " + tempLine.substring(0, endPosition));
        SpannableString mandarinSpanString = new SpannableString(mandarinTone);
        mandarinSpanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, 7, 0);

        int beginSearchKeyPosition = mandarinTone.toLowerCase().indexOf(searchKey.trim().toLowerCase(), 7);
        int endSearchKeyPosition = beginSearchKeyPosition + searchKey.length();

        // if search key is found, color the search key
        if (beginSearchKeyPosition != -1) {
            mandarinSpanString.setSpan(new BackgroundColorSpan(Color.YELLOW), beginSearchKeyPosition, endSearchKeyPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return mandarinSpanString;

    }

    public SpannableString getDefinition(String searchKey)
    {
        searchKey = searchKey.trim();
        int beginPosition = line.indexOf("/")+1;
        int endPosition = line.length();
        String definition = new String("Meaning: " + line.substring(beginPosition, endPosition-1));
        SpannableString definitionSpanString = new SpannableString(definition);
        definitionSpanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, 8, 0);

        int beginSearchKeyPosition = definition.toLowerCase().indexOf(searchKey.trim().toLowerCase(), 8);
        int endSearchKeyPosition = beginSearchKeyPosition + searchKey.length();

        // if search key is found, color the search key
        if (beginSearchKeyPosition != -1) {
            definitionSpanString.setSpan(new BackgroundColorSpan(Color.YELLOW), beginSearchKeyPosition, endSearchKeyPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return definitionSpanString;
    }


    public SpannableString getDefinitionForSearch(String searchKey)
    {
        searchKey = searchKey.trim();
        int beginPosition = line.indexOf("/");
        int endPosition = line.length();
        String definition = new String(line.substring(beginPosition, endPosition));
        SpannableString definitionSpanString = new SpannableString(definition);
        definitionSpanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, 0, 0);

        return definitionSpanString;
    }


}
