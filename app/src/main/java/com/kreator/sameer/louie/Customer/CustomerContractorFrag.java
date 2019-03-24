package com.kreator.sameer.louie.Customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kreator.sameer.louie.R;

public class CustomerContractorFrag extends Fragment {

    public static CustomerContractorFrag newInstance(){
        CustomerContractorFrag fragment = new CustomerContractorFrag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_fragment_contractor , container, false);
        return view;
    }

}
