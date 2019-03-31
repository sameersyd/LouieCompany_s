package com.kreator.sameer.louie.Customer;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kreator.sameer.louie.Contractor.ContractorCustomerFrag;
import com.kreator.sameer.louie.Contractor.ContractorCustomersObject;
import com.kreator.sameer.louie.R;

import java.util.ArrayList;

public class CustomerReferralCustomadapter extends BaseAdapter {

    Context c;
    CustomerReferralFrag delegate;
    ArrayList<CustomerReferralObject> spacecrafts = new ArrayList<>();

    public CustomerReferralCustomadapter(Context c) {
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
            convertView= LayoutInflater.from(c).inflate(R.layout.cust_myreferrals_model,parent,false);

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(c.getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(c.getAssets(),"fonts/Montserrat-Regular.ttf");

        TextView nameTxt = (TextView)convertView.findViewById(R.id.custMyReferrals_nameTxt);
        TextView phoneTxt = (TextView)convertView.findViewById(R.id.custMyReferrals_phoneTxt);
        TextView emailTxt = (TextView)convertView.findViewById(R.id.custMyReferrals_emailTxt);
        TextView updatedNameTxt = (TextView)convertView.findViewById(R.id.custMyReferrals_updatedByTxt);
        TextView statusTxt = (TextView)convertView.findViewById(R.id.custMyReferrals_statusTxt);
        TextView updatedPlainTxt = (TextView)convertView.findViewById(R.id.custMyReferrals_statusUpdatedByPlainTxt);
        ImageView profileImg = (ImageView)convertView.findViewById(R.id.contCustList_profileImg);

        nameTxt.setTypeface(myCustomFont_montserrat_regular);
        phoneTxt.setTypeface(myCustomFont_montserrat_regular);
        emailTxt.setTypeface(myCustomFont_montserrat_regular);
        statusTxt.setTypeface(myCustomFont_montserrat_regular);
        updatedPlainTxt.setTypeface(myCustomFont_montserrat_regular);
        updatedNameTxt.setTypeface(myCustomFont_montserrat_bold);

        CustomerReferralObject s = (CustomerReferralObject) this.getItem(position);

        nameTxt.setText(s.getName());
        phoneTxt.setText(s.getPhone());
        emailTxt.setText(s.getEmail());
        updatedNameTxt.setText(s.getUpdated_name());
        statusTxt.setText(s.getStatus());
//        Glide.with(c).load(s.getProfileImg()).into(submittedByImg);

        return convertView;
    }

}