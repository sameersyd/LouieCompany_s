package com.kreator.sameer.louie.Contractor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kreator.sameer.louie.AddReferalActivity;
import com.kreator.sameer.louie.Configs;
import com.kreator.sameer.louie.InviteCustomerActivity;
import com.kreator.sameer.louie.R;

public class ContractorCustomerFrag extends Fragment {

    public static ContractorCustomerFrag newInstance(){
        ContractorCustomerFrag fragment = new ContractorCustomerFrag();
        return fragment;
    }

    ImageView addCust;
    Dialog loadDialog;
    ListView listView;
    String firmKey;
    SwipeRefreshLayout refreshLayout;
    ContractorCustomerCustomadapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contractor_fragment_customer , container, false);

        addCust = (ImageView)view.findViewById(R.id.contractor_customer_addImage);
        addCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),InviteCustomerActivity.class));
            }
        });
        listView = (ListView)view.findViewById(R.id.contFrag_referral_listview);
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

        adapter = new ContractorCustomerCustomadapter(getContext());
        adapter.delegate = this;
        listView.setAdapter(adapter);

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

        return view;
    }

    //Fetch the firm key of the contractor, to get all firm members uid to get all contractors customers of the team
    public void mainFetch(){
        loadDialog.show();
        adapter.spacecrafts.clear();
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

    //Get the firm members using the firm key
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
                loadDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        });
    }

    //retrieve all the customers uid, every contractor has, and pass them to fetch their details
    public void retrieve(String contUid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.users)
                .child(contUid)
                .child(Configs.customers);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    fetchCustDetails(ds.getRef().getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Fetch details of customers
    public void fetchCustDetails(String custUid){
        final String idC = custUid;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.users)
                .child(custUid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child(Configs.users)
                        .child(idC);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        adapter.spacecrafts.add(new ContractorCustomersObject(
                                dataSnapshot.child(Configs.name).getValue().toString(),
                                dataSnapshot.child(Configs.phone).getValue().toString(),
                                dataSnapshot.child(Configs.email).getValue().toString(),
                                dataSnapshot.child(Configs.profile_image).getValue().toString(),
                                dataSnapshot.child(Configs.contractor_link_uid).getValue().toString(),
                                idC));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
