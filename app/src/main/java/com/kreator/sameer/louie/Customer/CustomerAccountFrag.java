package com.kreator.sameer.louie.Customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kreator.sameer.louie.R;

public class CustomerAccountFrag extends Fragment {

    public static CustomerAccountFrag newInstance(){
        CustomerAccountFrag fragment = new CustomerAccountFrag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_fragment_account , container, false);
        return view;
    }

}
