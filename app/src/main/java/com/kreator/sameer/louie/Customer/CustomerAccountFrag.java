package com.kreator.sameer.louie.Customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.kreator.sameer.louie.AccountSetupActivity;
import com.kreator.sameer.louie.Configs;
import com.kreator.sameer.louie.R;
import com.kreator.sameer.louie.SplashActivity;
import com.kreator.sameer.louie.EditProfileActivity;

public class CustomerAccountFrag extends Fragment {

    public static CustomerAccountFrag newInstance(){
        CustomerAccountFrag fragment = new CustomerAccountFrag();
        return fragment;
    }

    TextView accountSetupTxt,editProfileTxt,logoutTxt;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_fragment_account , container, false);
        auth = FirebaseAuth.getInstance();

        accountSetupTxt = (TextView)view.findViewById(R.id.customer_account_accountSetupTxt);
        editProfileTxt = (TextView)view.findViewById(R.id.customer_account_editProfileTxt);
        logoutTxt = (TextView)view.findViewById(R.id.customer_account_logoutTxt);

        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(view.getContext().getAssets(),"fonts/Montserrat-Regular.ttf");
        accountSetupTxt.setTypeface(myCustomFont_montserrat_regular);
        editProfileTxt.setTypeface(myCustomFont_montserrat_regular);
        logoutTxt.setTypeface(myCustomFont_montserrat_regular);

        accountSetupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AccountSetupActivity.class);
                intent.putExtra("editAccountSetup",true);
                intent.putExtra("account_type",Configs.customer_type_account);
                startActivity(intent);
                getActivity().finish();
            }
        });

        editProfileTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),EditProfileActivity.class));
            }
        });

        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setIcon(R.drawable.app_logo).setTitle(getString(R.string.app_name))
                        .setMessage("Are you sure you want to logout!")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                auth.signOut();
                                startActivity(new Intent(getContext(),SplashActivity.class));
                                getActivity().finish();
                            }
                        }).setNegativeButton("No", null).show();
            }
        });

        return view;
    }

}
