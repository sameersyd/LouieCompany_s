package com.kreator.sameer.louie.Customer;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kreator.sameer.louie.Configs;
import com.kreator.sameer.louie.Contractor.ContractorCustomerFrag;
import com.kreator.sameer.louie.Contractor.ContractorCustomersObject;
import com.kreator.sameer.louie.R;
import com.kreator.sameer.louie.ViewContractor.ViewContractorActivity;

import java.util.ArrayList;

public class CustomerReferralCustomadapter extends BaseAdapter {

    Context c;
    CustomerReferralFrag delegate;
    ArrayList<CustomerReferralObject> spacecrafts = new ArrayList<>();

    public CustomerReferralCustomadapter(Context c) {
        this.c = c;
    }
    @Override
    public int getCount() {
        return spacecrafts.size();
    }
    @Override
    public Object getItem(int position) {
        return spacecrafts.get(spacecrafts.size() - position - 1);      // Existing Code Modified To Display Recent Top
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView= LayoutInflater.from(c).inflate(R.layout.cust_myreferrals_model,parent,false);

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(c.getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(c.getAssets(),"fonts/Montserrat-Regular.ttf");

        TextView nameTxt = (TextView)convertView.findViewById(R.id.custMyReferrals_nameTxt);
        TextView phoneTxt = (TextView)convertView.findViewById(R.id.custMyReferrals_phoneTxt);
        TextView emailTxt = (TextView)convertView.findViewById(R.id.custMyReferrals_emailTxt);
        final TextView updatedNameTxt = (TextView)convertView.findViewById(R.id.custMyReferrals_updatedByTxt);
        TextView statusTxt = (TextView)convertView.findViewById(R.id.custMyReferrals_statusTxt);
        TextView updatedPlainTxt = (TextView)convertView.findViewById(R.id.custMyReferrals_statusUpdatedByPlainTxt);
        final ImageView profileImg = (ImageView)convertView.findViewById(R.id.custMyReferrals_statusUpdatedByImg);

        nameTxt.setTypeface(myCustomFont_montserrat_regular);
        phoneTxt.setTypeface(myCustomFont_montserrat_regular);
        emailTxt.setTypeface(myCustomFont_montserrat_regular);
        statusTxt.setTypeface(myCustomFont_montserrat_regular);
        updatedPlainTxt.setTypeface(myCustomFont_montserrat_regular);
        updatedNameTxt.setTypeface(myCustomFont_montserrat_bold);

        profileImg.setImageDrawable(null);

        final CustomerReferralObject s = (CustomerReferralObject) this.getItem(position);

        nameTxt.setText(s.getName());
        phoneTxt.setText(s.getPhone());
        emailTxt.setText(s.getEmail());
        statusTxt.setText(s.getStatus());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.users)
                .child(s.getUpdated_uid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updatedNameTxt.setText(dataSnapshot.child(Configs.name).getValue(String.class));
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                storageRef.child(dataSnapshot.child(Configs.profile_image).getValue(String.class)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(c).load(uri).into(profileImg);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(c, exception+"", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return convertView;
    }

}