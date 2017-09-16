package com.example.android.miwok;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wen-zhao on 9/2/2017.
 */

public class WordAdapter extends  ArrayAdapter<Word> {
    private int colorActivityBackground;
    //constructor ArrayAdapter(Context context, int resource, T[] objects)
    public WordAdapter(Activity context, ArrayList<Word> words, int colorActivityBackground){
        super(context, 0, words);
        this.colorActivityBackground = colorActivityBackground;
    }
    @NonNull
    @Override
    public View getView(int position,  View convertView,ViewGroup parent) {
        View listItemView = convertView;
        //if there is no reusable view
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Word currentWord = getItem(position);
        //ContextCompat: Helper for accessing features in Context.
        //ContextCompat.getColor(context, id) Returns a color associated with a particular resource ID
        //getContext() Returns the context the view is running in, through which it can access the current theme, resources, etc.
        int color = ContextCompat.getColor(getContext(),colorActivityBackground);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.list_item_text_view);
                nameTextView.setText(currentWord.getMiwokTranslation());
        nameTextView.setBackgroundColor(color);

        TextView nameTextView2 = (TextView) listItemView.findViewById(R.id.list_item_text_view2);
        nameTextView2.setText(currentWord.getDefautTranslation());
        nameTextView2.setBackgroundColor(color);

        ImageView icon = (ImageView)listItemView.findViewById(R.id.play_icon);
        icon.setBackgroundColor(color);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image_id);
        if (currentWord.getImageResourceId() != -1){
            imageView.setImageResource(currentWord.getImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        }else{
           imageView.setVisibility(View.GONE);

        }
        return listItemView;
    }

}
