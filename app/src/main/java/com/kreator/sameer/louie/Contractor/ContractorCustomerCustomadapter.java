package com.kreator.sameer.louie.Contractor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kreator.sameer.louie.PayActivity;
import com.kreator.sameer.louie.R;
import com.kreator.sameer.louie.ViewContractor.ViewContractorActivity;

import java.util.ArrayList;

public class ContractorCustomerCustomadapter extends BaseAdapter {

    Context c;
    ContractorCustomerFrag delegate;
    ArrayList<ContractorCustomersObject> spacecrafts = new ArrayList<>();

    public ContractorCustomerCustomadapter(Context c) {
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
            convertView= LayoutInflater.from(c).inflate(R.layout.cont_customerslist_model,parent,false);

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(c.getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(c.getAssets(),"fonts/Montserrat-Regular.ttf");

        TextView nameTxt = (TextView)convertView.findViewById(R.id.contCustList_nameTxt);
        TextView phoneTxt = (TextView)convertView.findViewById(R.id.contCustList_phoneTxt);
        TextView emailTxt = (TextView)convertView.findViewById(R.id.contCustList_emailTxt);

        final ImageView profileImg = (ImageView)convertView.findViewById(R.id.contCustList_profileImg);
        Button payButton = (Button)convertView.findViewById(R.id.contCustList_payBtn);

        nameTxt.setTypeface(myCustomFont_montserrat_bold);
        phoneTxt.setTypeface(myCustomFont_montserrat_regular);
        emailTxt.setTypeface(myCustomFont_montserrat_regular);
        payButton.setTypeface(myCustomFont_montserrat_regular);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c,PayActivity.class);
                intent.putExtra("custUid",spacecrafts.get(spacecrafts.size() - position - 1).getCustomer_uid());
                c.startActivity(intent);
            }
        });

        ContractorCustomersObject s = (ContractorCustomersObject) this.getItem(position);

        nameTxt.setText(s.getName());
        phoneTxt.setText(s.getPhone());
        emailTxt.setText(s.getEmail());

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child(s.getProfilePic()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

        return convertView;
    }

}