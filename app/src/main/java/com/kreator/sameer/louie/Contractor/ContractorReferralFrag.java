package com.kreator.sameer.louie.Contractor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kreator.sameer.louie.AddReferalActivity;
import com.kreator.sameer.louie.R;

public class ContractorReferralFrag extends Fragment {

    public static ContractorReferralFrag newInstance(){
        ContractorReferralFrag fragment = new ContractorReferralFrag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contractor_fragment_referral , container, false);

        return view;
    }

}
