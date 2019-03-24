package com.kreator.sameer.louie;

import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kreator.sameer.louie.Customer.CustomerAccountFrag;
import com.kreator.sameer.louie.Customer.CustomerContractorFrag;
import com.kreator.sameer.louie.Customer.CustomerReferralFrag;

public class CustomerActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    TextView contractorSelect,referralSelect,accountSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        final ViewPager viewPager = findViewById(R.id.viewPager);
        contractorSelect = (TextView)findViewById(R.id.customer_contractorSelect);
        referralSelect = (TextView)findViewById(R.id.customer_referralSelect);
        accountSelect = (TextView)findViewById(R.id.customer_accountSelect);

        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        contractorSelect.setTypeface(myCustomFont_montserrat_regular);
        referralSelect.setTypeface(myCustomFont_montserrat_regular);
        accountSelect.setTypeface(myCustomFont_montserrat_regular);

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        //Setting initial position to one
        viewPager.setCurrentItem(1);
        contractorSelect.setBackground(getDrawable(R.drawable.accountsetup_unselected));
        referralSelect.setBackground(getDrawable(R.drawable.accountsetup_selected));
        accountSelect.setBackground(getDrawable(R.drawable.accountsetup_unselected));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            contractorSelect.setTextColor(getColor(R.color.accountsetup_unselect));
            referralSelect.setTextColor(getColor(R.color.white_color));
            accountSelect.setTextColor(getColor(R.color.accountsetup_unselect));
        }

        contractorSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0,true);
            }
        });
        referralSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1,true);
            }
        });
        accountSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2,true);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        contractorSelect.setBackground(getDrawable(R.drawable.accountsetup_selected));
                        referralSelect.setBackground(getDrawable(R.drawable.accountsetup_unselected));
                        accountSelect.setBackground(getDrawable(R.drawable.accountsetup_unselected));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            contractorSelect.setTextColor(getColor(R.color.white_color));
                            referralSelect.setTextColor(getColor(R.color.accountsetup_unselect));
                            accountSelect.setTextColor(getColor(R.color.accountsetup_unselect));
                        }
                        break;
                    case 1:
                        contractorSelect.setBackground(getDrawable(R.drawable.accountsetup_unselected));
                        referralSelect.setBackground(getDrawable(R.drawable.accountsetup_selected));
                        accountSelect.setBackground(getDrawable(R.drawable.accountsetup_unselected));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            contractorSelect.setTextColor(getColor(R.color.accountsetup_unselect));
                            referralSelect.setTextColor(getColor(R.color.white_color));
                            accountSelect.setTextColor(getColor(R.color.accountsetup_unselect));
                        }
                        break;
                    case 2:
                        contractorSelect.setBackground(getDrawable(R.drawable.accountsetup_unselected));
                        referralSelect.setBackground(getDrawable(R.drawable.accountsetup_unselected));
                        accountSelect.setBackground(getDrawable(R.drawable.accountsetup_selected));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            contractorSelect.setTextColor(getColor(R.color.accountsetup_unselect));
                            referralSelect.setTextColor(getColor(R.color.accountsetup_unselect));
                            accountSelect.setTextColor(getColor(R.color.white_color));
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return CustomerContractorFrag.newInstance();
                case 1:
                    return CustomerReferralFrag.newInstance();
                case 2:
                    return CustomerAccountFrag.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
