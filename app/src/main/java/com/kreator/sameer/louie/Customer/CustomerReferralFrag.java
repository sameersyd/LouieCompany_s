package com.kreator.sameer.louie.Customer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kreator.sameer.louie.AccountSetupActivity;
import com.kreator.sameer.louie.AddReferalActivity;
import com.kreator.sameer.louie.Configs;
import com.kreator.sameer.louie.Contractor.ContractorCustomerCustomadapter;
import com.kreator.sameer.louie.R;
import com.kreator.sameer.louie.SplashActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerReferralFrag extends Fragment {

    public static CustomerReferralFrag newInstance(){
        CustomerReferralFrag fragment = new CustomerReferralFrag();
        return fragment;
    }

    ImageView addReferral;
    TextView noContTxt,expTxt,inviteNameTxt,inviteIgnoreTxt;
    RelativeLayout noContView,contInviteView;
    Button inviteAcceptBtn;
    boolean contlinked;

    Dialog loadDialog;
    ListView listView;
    SwipeRefreshLayout refreshLayout;
    CustomerReferralCustomadapter adapter;

    DatabaseReference db;
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

        listView = (ListView)view.findViewById(R.id.customer_referral_listview);
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.customer_referral_swipeRefreshLayout);

        loadDialog = new Dialog(getContext());
        loadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadDialog.setContentView(R.layout.loading_one);
        loadDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    loadDialog.dismiss();
                }
                return true;
            }
        });
        LottieAnimationView animSelect;
        animSelect = (LottieAnimationView) loadDialog.findViewById(R.id.loading_one);
        loadDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        animSelect.setAnimation("blueline.json");
        animSelect.playAnimation();
        animSelect.loop(true);

        Window window = loadDialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        adapter = new CustomerReferralCustomadapter(getContext());
        adapter.delegate = this;
        listView.setAdapter(adapter);

        noContTxt.setText("No Contractor\nYet :(");

        inviteAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept_invite();
            }
        });
        inviteIgnoreTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ignoreInvite();
            }
        });
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

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                referralAndInvites();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },1500);
            }
        });

        //For swipeRefreshListener not activating while listview swiped down(Going up)
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (listView == null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                refreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        referralAndInvites();

        return view;
    }

    //Get customer's linked contractor uid
    public void mainFetch(){
        listView.setVisibility(View.VISIBLE);
        adapter.spacecrafts.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Configs.contractor_link_uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.spacecrafts.clear();
                fetchReferrals(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        });
    }

    //Fetch referrals sent by this customer
    public void fetchReferrals(String contUid){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.users)
                .child(contUid)
                .child(Configs.referrals);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                adapter.spacecrafts.clear();

                adapter.notifyDataSetChanged();

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    if (ds.child(Configs.referral_submitted_uid).getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){      //Check whether current referral was sent by this customer

                        final String name = ds.child(Configs.referral_name).getValue(String.class);
                        final String phone = ds.child(Configs.referral_phone).getValue(String.class);
                        final String email = ds.child(Configs.referral_email).getValue(String.class);
                        final String status = ds.child(Configs.referral_status).getValue(String.class);

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                                .child(Configs.users)
                                .child(ds.child(Configs.referral_updated_uid).getValue(String.class));
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                adapter.spacecrafts.add(new CustomerReferralObject(
                                        name,
                                        phone,
                                        email,
                                        status,
                                        dataSnapshot.child(Configs.name).getValue(String.class),
                                        dataSnapshot.child(Configs.profile_image).getValue(String.class)
                                ));
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        });
    }

    public void accept_invite(){

        db = FirebaseDatabase.getInstance().getReference();

        HashMap hm = new HashMap();
        hm.put(Configs.contractor_linked,true);
        hm.put(Configs.contractor_link_uid,contractorsInvitesList.get(0));

        //change contractor link status, updating contractor UID in customer account
        db.child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    //enter customer UID in contractors, customer child
                    db.child(Configs.users)
                            .child(contractorsInvitesList.get(0))
                            .child(Configs.customers)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                //remove all contractors invite request from customers invite list as it is accepted
                                db.child(Configs.users)
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(Configs.contractor_invite)
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getContext(), "Invite accepted", Toast.LENGTH_SHORT).show();
                                            addReferral.setVisibility(View.VISIBLE);
                                            noContView.setVisibility(View.GONE);
                                            contInviteView.setVisibility(View.GONE);
                                            referralAndInvites();
                                        }else{
                                            Toast.makeText(getContext(), task.getException()+"", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else {
                                Toast.makeText(getContext(), task.getException()+"", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getActivity(), task.getException()+"", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void ignoreInvite(){
        FirebaseDatabase.getInstance().getReference()
                .child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Configs.contractor_invite)
                .child(contractorsInvitesList.get(0))
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(getContext(), "Request Declined", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), task.getException()+"", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void referralAndInvites(){
        noContView.setVisibility(View.GONE);
        contInviteView.setVisibility(View.GONE);
        addReferral.setVisibility(View.GONE);

        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
        DatabaseReference imgs = refs
                .child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Configs.contractor_linked);
        imgs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contlinked = dataSnapshot.getValue(Boolean.class);
                if (contlinked){
                    //contractor linked; Show referral adding button and referral links
                    addReferral.setVisibility(View.VISIBLE);
                    noContView.setVisibility(View.GONE);
                    contInviteView.setVisibility(View.GONE);
                    mainFetch();
                }else if (!contlinked){
                    listView.setVisibility(View.GONE);
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
                                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else{
                                //contractor invites not available; show no contractor yet :( view
                                noContView.setVisibility(View.VISIBLE);
                                contInviteView.setVisibility(View.GONE);
                                addReferral.setVisibility(View.GONE);
                                listView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
