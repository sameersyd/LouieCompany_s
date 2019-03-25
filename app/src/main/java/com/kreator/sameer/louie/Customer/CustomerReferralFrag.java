package com.kreator.sameer.louie.Customer;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kreator.sameer.louie.AddReferalActivity;
import com.kreator.sameer.louie.Configs;
import com.kreator.sameer.louie.R;

import java.util.ArrayList;

public class CustomerReferralFrag extends Fragment {

    public static CustomerReferralFrag newInstance(){
        CustomerReferralFrag fragment = new CustomerReferralFrag();
        return fragment;
    }

    ImageView addReferral;
    TextView noContTxt,expTxt,inviteNameTxt,inviteIgnoreTxt;
    RelativeLayout noContView,contInviteView;
    Button inviteAcceptBtn;
    public static ArrayList<String> contractorsInvitesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_fragment_referral , container, false);

        noContTxt = (TextView)view.findViewById(R.id.customer_referral_noContTxt);
        expTxt = (TextView)view.findViewById(R.id.customer_referral_expTxt);
        addReferral = (ImageView)view.findViewById(R.id.customer_referral_addImage);
        noContView = (RelativeLayout)view.findViewById(R.id.customer_referral_noContView);
        contInviteView = (RelativeLayout)view.findViewById(R.id.customer_referral_contInviteView);
        inviteNameTxt = (TextView)view.findViewById(R.id.customer_referral_contInvNameTxt);
        inviteIgnoreTxt = (TextView)view.findViewById(R.id.customer_referral_contInvIgnoreTxt);
        inviteAcceptBtn = (Button)view.findViewById(R.id.customer_referral_contInvAcceptBtn);

        noContTxt.setText("No Contractor\nYet :(");
        addReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AddReferalActivity.class));
            }
        });
        Typeface myCustomFont_montserrat_light = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Montserrat-Light.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Montserrat-Regular.ttf");
        inviteIgnoreTxt.setTypeface(myCustomFont_montserrat_regular);
        noContTxt.setTypeface(myCustomFont_montserrat_light);
        inviteNameTxt.setTypeface(myCustomFont_montserrat_light);
        inviteAcceptBtn.setTypeface(myCustomFont_montserrat_regular);
        expTxt.setTypeface(myCustomFont_montserrat_light);

        referralAndInvites();

        return view;
    }

    public void referralAndInvites(){
        noContView.setVisibility(View.GONE);
        contInviteView.setVisibility(View.GONE);
        addReferral.setVisibility(View.GONE);

        final boolean[] contlinked = new boolean[1];
        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
        DatabaseReference imgs = refs
                .child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Configs.contractor_linked);
        imgs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contlinked[0] = dataSnapshot.getValue(Boolean.class);
                if (contlinked[0]){
                    //contractor linked; Show referral adding button and referral links
                    addReferral.setVisibility(View.VISIBLE);
                    noContView.setVisibility(View.GONE);
                    contInviteView.setVisibility(View.GONE);
                }else if (!contlinked[0]){
                    addReferral.setVisibility(View.GONE);
                    //contractor not linked; check for invites
                    final DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference imgs = refs
                            .child(Configs.users)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(Configs.contractor_invite);
                    imgs.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //contractor invites available; show accept or decline button

                                contractorsInvitesList.clear();

                                for (DataSnapshot ds : dataSnapshot.getChildren()){         //getting all contractors invite's UID
                                    contractorsInvitesList.add(ds.getRef().getKey()+"");
                                }

                                DatabaseReference inviteCont = refs
                                        .child(Configs.users)
                                        .child(contractorsInvitesList.get(0))
                                        .child(Configs.name);
                                inviteCont.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        inviteNameTxt.setText("You have an invite from "+dataSnapshot.getValue(String.class));
                                        noContView.setVisibility(View.GONE);
                                        contInviteView.setVisibility(View.VISIBLE);
                                        addReferral.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }else{
                                //contractor invites not available; show no contractor yet :( view
                                noContView.setVisibility(View.VISIBLE);
                                contInviteView.setVisibility(View.GONE);
                                addReferral.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
