package com.kreator.sameer.louie;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kreator.sameer.louie.Customer.CustomerAccountFrag;
import com.kreator.sameer.louie.Customer.CustomerContractorFrag;
import com.kreator.sameer.louie.Customer.CustomerReferralFrag;

public class CustomerActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    TextView contractorSelect,referralSelect,accountSelect,teamTxt;
    ImageView teamImg,teamCircularImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        final ViewPager viewPager = findViewById(R.id.customer_viewPager);
        contractorSelect = (TextView)findViewById(R.id.customer_contractorSelect);
        referralSelect = (TextView)findViewById(R.id.customer_referralSelect);
        accountSelect = (TextView)findViewById(R.id.customer_accountSelect);
        teamImg = (ImageView) findViewById(R.id.customer_mainImage);
        teamCircularImg = (ImageView) findViewById(R.id.customer_mainImageCircular);
        teamTxt = (TextView)findViewById(R.id.customer_mainTxt);

        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        contractorSelect.setTypeface(myCustomFont_montserrat_regular);
        referralSelect.setTypeface(myCustomFont_montserrat_regular);
        accountSelect.setTypeface(myCustomFont_montserrat_regular);
        teamTxt.setTypeface(myCustomFont_montserrat_regular);

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

        setLogo();

    }

    public void setLogo(){
        DatabaseReference rem = FirebaseDatabase.getInstance().getReference();
        rem.child(Configs.users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check whether user is linked to contractor
                if (dataSnapshot.child(Configs.contractor_linked).getValue(Boolean.class)){     //Yes! fetch contractor account to get firm key
                    DatabaseReference rem = FirebaseDatabase.getInstance().getReference();
                    rem.child(Configs.users)
                            .child(dataSnapshot.child(Configs.contractor_link_uid).getValue(String.class))
                            .addValueEventListener(new ValueEventListener() {     //fetching contractor account to get firm key
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    DatabaseReference rem = FirebaseDatabase.getInstance().getReference();
                                    rem.child(Configs.firms).child(dataSnapshot.child(Configs.firm_key).getValue(String.class)).addValueEventListener(new ValueEventListener() {        //fetch firm details from fetched firm key datasnapshot
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child(Configs.firm_logo_boo).getValue(Boolean.class)){         //check whether firm has logo

                                                teamCircularImg.setVisibility(View.VISIBLE);
                                                teamImg.setVisibility(View.INVISIBLE);
                                                teamTxt.setVisibility(View.INVISIBLE);

                                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                                storageRef.child(dataSnapshot.child(Configs.firm_logo).getValue(String.class)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        Glide.with(getApplicationContext()).load(uri).into(teamCircularImg);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        Toast.makeText(getApplicationContext(), exception+"", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }else {
                                                teamImg.setVisibility(View.INVISIBLE);
                                                teamTxt.setVisibility(View.VISIBLE);
                                                teamCircularImg.setVisibility(View.INVISIBLE);
                                                teamTxt.setText(dataSnapshot.child(Configs.firm_name).getValue(String.class));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Toast.makeText(CustomerActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(CustomerActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {
                    teamCircularImg.setVisibility(View.GONE);
                    teamImg.setVisibility(View.VISIBLE);
                    teamTxt.setVisibility(View.GONE);
                    teamImg.setImageDrawable(getDrawable(R.drawable.louie_logo));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
