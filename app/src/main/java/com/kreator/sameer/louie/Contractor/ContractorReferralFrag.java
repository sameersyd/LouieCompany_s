package com.kreator.sameer.louie.Contractor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import com.kreator.sameer.louie.R;

import java.util.ArrayList;

public class ContractorReferralFrag extends Fragment {

    public static ContractorReferralFrag newInstance(){
        ContractorReferralFrag fragment = new ContractorReferralFrag();
        return fragment;
    }

    ListView listview;
    String firmKey;
    SwipeRefreshLayout refreshLayout;
    ContractorReferralsCustomadapter adapter;
    Dialog loadDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contractor_fragment_referral , container, false);

        listview = (ListView)view.findViewById(R.id.contFrag_referral_listview);
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.contFrag_referral_swipeRefreshLayout);

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

        adapter = new ContractorReferralsCustomadapter(getContext());
        adapter.delegate = this;
        listview.setAdapter(adapter);

        mainFetch();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainFetch();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },1500);
            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (listview == null || listview.getChildCount() == 0) ? 0 : listview.getChildAt(0).getTop();
                refreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        return view;
    }

    public void mainFetch(){

        loadDialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Configs.firm_key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.spacecrafts.clear();
                firmKey = dataSnapshot.getValue(String.class);
                fetchData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        });
    }

    public void fetchData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.firms)
                .child(firmKey)
                .child("members");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    retrieve(ds.getRef().getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        });
    }

    public void retrieve(String contUid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.users)
                .child(contUid)
                .child(Configs.referrals);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    adapter.spacecrafts.add(new ContractorReferralsObject(
                            ds.child(Configs.referral_name).getValue().toString(),
                            ds.child(Configs.referral_phone).getValue().toString(),
                            ds.child(Configs.referral_email).getValue().toString(),
                            ds.child(Configs.referral_additional).getValue().toString(),
                            ds.child(Configs.referral_status).getValue().toString(),
                            ds.child(Configs.referral_key).getValue().toString(),
                            ds.child(Configs.referral_updated_timestamp).getValue().toString(),
                            ds.child(Configs.referral_submitted_uid).getValue().toString(),
                            ds.child(Configs.referral_updated_uid).getValue().toString(),
                            ds.child(Configs.referral_sentToCont_uid).getValue().toString()));
                    adapter.notifyDataSetChanged();
                }
                loadDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        });

    }

}
