package com.kreator.sameer.louie.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kreator.sameer.louie.AddReferalActivity;
import com.kreator.sameer.louie.R;

public class CustomerReferralFrag extends Fragment {

    public static CustomerReferralFrag newInstance(){
        CustomerReferralFrag fragment = new CustomerReferralFrag();
        return fragment;
    }

    ImageView addReferral;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_fragment_referral , container, false);
        addReferral = (ImageView)view.findViewById(R.id.customer_referral_addImage);
        addReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AddReferalActivity.class));
            }
        });
        return view;
    }

}
