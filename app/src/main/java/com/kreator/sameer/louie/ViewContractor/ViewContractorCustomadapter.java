package com.kreator.sameer.louie.ViewContractor;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.kreator.sameer.louie.R;

import java.util.ArrayList;

public class ViewContractorCustomadapter extends BaseAdapter {

    Context c;
    ArrayList<ViewContractorObject> spacecrafts = new ArrayList<>();

    public ViewContractorCustomadapter(Context c) {
        this.c = c;
    }
    @Override
    public int getCount() {
        return spacecrafts.size();
    }
    @Override
    public Object getItem(int position) {
        return spacecrafts.get(position);      // Existing Code Modified To Display Recent Top
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView= LayoutInflater.from(c).inflate(R.layout.view_contractor_model,parent,false);

        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(c.getAssets(),"fonts/Montserrat-Regular.ttf");

        TextView nameTxt = (TextView)convertView.findViewById(R.id.viewCont_model_nameTxt);
        TextView noTxt = (TextView)convertView.findViewById(R.id.viewCont_model_numberTxt);
        TextView detailsTxt = (TextView)convertView.findViewById(R.id.viewCont_model_detailsTxt);

        nameTxt.setTypeface(myCustomFont_montserrat_regular);
        noTxt.setTypeface(myCustomFont_montserrat_regular);
        detailsTxt.setTypeface(myCustomFont_montserrat_regular);

        ViewContractorObject s = (ViewContractorObject) this.getItem(position);

        nameTxt.setText(s.getName());
        noTxt.setText(s.getNo());
        detailsTxt.setText(s.getEmail()+", "+s.getPhone());

        return convertView;
    }

}