package com.kreator.sameer.louie.Contractor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.kreator.sameer.louie.R;
import com.kreator.sameer.louie.SplashActivity;

public class ContractorAccountFrag extends Fragment {

    public static ContractorAccountFrag newInstance(){
        ContractorAccountFrag fragment = new ContractorAccountFrag();
        return fragment;
    }

    TextView logoutTxt;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contractor_fragment_account , container, false);
        auth = FirebaseAuth.getInstance();
        logoutTxt = (TextView)view.findViewById(R.id.contractor_account_logoutTxt);
        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getContext(),SplashActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }

}
