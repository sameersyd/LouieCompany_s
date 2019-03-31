package com.kreator.sameer.louie.Customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kreator.sameer.louie.Configs;
import com.kreator.sameer.louie.ViewContractor.ViewContractorActivity;
import com.kreator.sameer.louie.R;

import java.util.HashMap;

public class CustomerContractorFrag extends Fragment {

    public static CustomerContractorFrag newInstance(){
        CustomerContractorFrag fragment = new CustomerContractorFrag();
        return fragment;
    }

    TextView yourContTxt,contSimTxt,contNameTxt,viewProTxt,noContTxt,expTxt;
    Button suspendBtn,viewProfileBtn;
    RelativeLayout contView,noContView;
    String contUid,firmKey;
    boolean viewProfileBoo = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_fragment_contractor , container, false);

        yourContTxt = (TextView)view.findViewById(R.id.customer_contracter_yourContTxt);
        contSimTxt = (TextView)view.findViewById(R.id.customer_contracter_contSimTxt);
        contNameTxt = (TextView)view.findViewById(R.id.customer_contracter_contNameTxt);
        viewProfileBtn = (Button) view.findViewById(R.id.customer_contracter_contViewProfileBtn);
        suspendBtn = (Button)view.findViewById(R.id.customer_contracter_contSuspendBtn);
        viewProTxt = (TextView)view.findViewById(R.id.customer_contracter_contViewProfileTxt);
        noContTxt = (TextView)view.findViewById(R.id.customer_contracter_noContTxt);
        expTxt = (TextView)view.findViewById(R.id.customer_contracter_expTxt);
        contView = (RelativeLayout)view.findViewById(R.id.customer_contracter_contView);
        noContView = (RelativeLayout)view.findViewById(R.id.customer_contracter_noContView);

        noContTxt.setText("No Contractor\nYet :(");
        contView.setVisibility(View.GONE);
        suspendBtn.setVisibility(View.GONE);
        noContView.setVisibility(View.GONE);

        Typeface myCustomFont_montserrat_light = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Montserrat-Light.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Montserrat-Regular.ttf");
        yourContTxt.setTypeface(myCustomFont_montserrat_light);
        contSimTxt.setTypeface(myCustomFont_montserrat_light);
        noContTxt.setTypeface(myCustomFont_montserrat_light);
        expTxt.setTypeface(myCustomFont_montserrat_light);
        contNameTxt.setTypeface(myCustomFont_montserrat_light);
        viewProTxt.setTypeface(myCustomFont_montserrat_regular);
        suspendBtn.setTypeface(myCustomFont_montserrat_regular);

        initializeContViews();

        viewProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewProfileBoo) {
                    Intent intent = new Intent(getContext(), ViewContractorActivity.class);
                    intent.putExtra("firmKey", firmKey);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "Wait until we fetch data...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        suspendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(getContext()).setIcon(R.drawable.app_logo).setTitle(getString(R.string.app_name))
                        .setMessage("Are you sure you want to leave this contractor?")
                        .setPositiveButton("Suspend", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                suspendMethod();
                            }
                        }).setNegativeButton("No", null)
                        .create();

                dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getActivity().getColor(R.color.white_color));
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getActivity().getColor(R.color.white_color));
                        }
                    }
                });
                dialog.show();
            }
        });
        return view;
    }

    public void suspendMethod(){

        //remove customer uid from contractors, customer's list
        DatabaseReference dbs = FirebaseDatabase.getInstance().getReference();
        dbs.child(Configs.users)
                .child(contUid)
                .child(Configs.customers)
                .child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    HashMap hs = new HashMap();
                    hs.put(Configs.contractor_linked,false);

                    //set contractor_linked in customer to false
                    DatabaseReference dbs = FirebaseDatabase.getInstance().getReference();
                    dbs.child(Configs.users)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .updateChildren(hs).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                //remove contractor_link_uid from customer's child
                                DatabaseReference dbs = FirebaseDatabase.getInstance().getReference();
                                dbs.child(Configs.users)
                                        .child(Configs.contractor_link_uid)
                                        .child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getContext(), "Contractor Suspended", Toast.LENGTH_SHORT).show();
                                            initializeContViews();
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
    }

    public void initializeContViews(){
        //check whether customer linked to contractor

        viewProfileBoo = false;

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference img = ref
                .child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Configs.contractor_linked);
        img.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue(Boolean.class)){

                    contView.setVisibility(View.VISIBLE);
                    suspendBtn.setVisibility(View.VISIBLE);
                    noContView.setVisibility(View.GONE);

                    //Get contractor UID and set
                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference img = ref
                            .child(Configs.users)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(Configs.contractor_link_uid);
                    img.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            contUid = dataSnapshot.getValue(String.class);

                            //Get contractor name
                            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference img = ref
                                    .child(Configs.users)
                                    .child(contUid)
                                    .child(Configs.name);
                            img.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    contNameTxt.setText(dataSnapshot.getValue(String.class));

                                    //Get firm key
                                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference img = ref
                                            .child(Configs.users)
                                            .child(contUid)
                                            .child(Configs.firm_key);
                                    img.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            firmKey = dataSnapshot.getValue(String.class);
                                            viewProfileBoo = true;
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else if (!dataSnapshot.getValue(Boolean.class)){
                    contView.setVisibility(View.GONE);
                    suspendBtn.setVisibility(View.GONE);
                    noContView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
