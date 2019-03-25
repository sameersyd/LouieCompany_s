package com.kreator.sameer.louie.Contractor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kreator.sameer.louie.AddReferalActivity;
import com.kreator.sameer.louie.InviteCustomerActivity;
import com.kreator.sameer.louie.R;

public class ContractorCustomerFrag extends Fragment {

    public static ContractorCustomerFrag newInstance(){
        ContractorCustomerFrag fragment = new ContractorCustomerFrag();
        return fragment;
    }

    ImageView addCust;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contractor_fragment_customer , container, false);
        addCust = (ImageView)view.findViewById(R.id.contractor_customer_addImage);
        addCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),InviteCustomerActivity.class));
            }
        });
        return view;
    }

}
