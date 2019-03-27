package com.kreator.sameer.louie.Contractor;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kreator.sameer.louie.R;

import java.util.ArrayList;

public class ContractorCustomerCustomadapter extends BaseAdapter {

    Context c;
    ContractorCustomerFrag delegate;
    ArrayList<ContractorCustomersObject> spacecrafts = new ArrayList<>();

    public ContractorCustomerCustomadapter(Context c) {
        this.c = c;
    }
    @Override
    public int getCount() {
        return spacecrafts.size();
    }
    @Override
    public Object getItem(int position) {
        return spacecrafts.get(spacecrafts.size() - position - 1);      // Existing Code Modified To Display Recent Top
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView= LayoutInflater.from(c).inflate(R.layout.cont_customerslist_model,parent,false);

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(c.getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(c.getAssets(),"fonts/Montserrat-Regular.ttf");

        TextView nameTxt = (TextView)convertView.findViewById(R.id.contCustList_nameTxt);
        TextView phoneTxt = (TextView)convertView.findViewById(R.id.contCustList_phoneTxt);
        TextView emailTxt = (TextView)convertView.findViewById(R.id.contCustList_emailTxt);

        ImageView profileImg = (ImageView)convertView.findViewById(R.id.contCustList_profileImg);
        Button payButton = (Button)convertView.findViewById(R.id.contCustList_payBtn);

        nameTxt.setTypeface(myCustomFont_montserrat_bold);
        phoneTxt.setTypeface(myCustomFont_montserrat_regular);
        emailTxt.setTypeface(myCustomFont_montserrat_regular);
        payButton.setTypeface(myCustomFont_montserrat_regular);

        ContractorCustomersObject s = (ContractorCustomersObject) this.getItem(position);

        nameTxt.setText(s.getName());
        phoneTxt.setText(s.getPhone());
        emailTxt.setText(s.getEmail());
//        Glide.with(c).load(s.getProfileImg()).into(submittedByImg);

        return convertView;
    }

}