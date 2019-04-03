package com.kreator.sameer.louie.Contractor;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kreator.sameer.louie.Configs;
import com.kreator.sameer.louie.R;
import com.kreator.sameer.louie.ViewContractor.ViewContractorActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ContractorReferralsCustomadapter extends BaseAdapter {

    Context c;
    ContractorReferralFrag delegate;
    ArrayList<ContractorReferralsObject> spacecrafts = new ArrayList<>();

    ArrayAdapter<CharSequence> statusAdapter;

    public ContractorReferralsCustomadapter(Context c) {
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
            convertView= LayoutInflater.from(c).inflate(R.layout.cont_referrals_model,parent,false);

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(c.getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(c.getAssets(),"fonts/Montserrat-Regular.ttf");

        TextView nameTxt = (TextView)convertView.findViewById(R.id.referralModel_nameTxt);
        TextView phoneTxt = (TextView)convertView.findViewById(R.id.referralModel_phoneTxt);
        TextView emailTxt = (TextView)convertView.findViewById(R.id.referralModel_emailTxt);
        TextView additionalTxt = (TextView)convertView.findViewById(R.id.referralModel_notesTxt);
        final TextView submittedByTxt = (TextView)convertView.findViewById(R.id.referralModel_submittedByTxt);
        TextView statusTxt = (TextView)convertView.findViewById(R.id.referralModel_statusTxt);
        TextView submittedPlain = (TextView)convertView.findViewById(R.id.referralModel_submittedByPlainTxt);
        TextView notesPlain = (TextView)convertView.findViewById(R.id.referralModel_notesPlainTxt);
        final Spinner statusSpin = (Spinner) convertView.findViewById(R.id.referralModel_statusUpdateSpin);
        final ImageView submittedByImg = (ImageView)convertView.findViewById(R.id.referralModel_submittedByImg);
        Button updateStatusButton = (Button)convertView.findViewById(R.id.referralModel_updateStatusBtn);

        statusAdapter = ArrayAdapter.createFromResource(c, R.array.referral_status, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpin.setAdapter(statusAdapter);

        submittedByImg.setImageDrawable(null);

        nameTxt.setTypeface(myCustomFont_montserrat_regular);
        phoneTxt.setTypeface(myCustomFont_montserrat_regular);
        emailTxt.setTypeface(myCustomFont_montserrat_regular);
        additionalTxt.setTypeface(myCustomFont_montserrat_regular);
        submittedByTxt.setTypeface(myCustomFont_montserrat_bold);
        statusTxt.setTypeface(myCustomFont_montserrat_regular);
        submittedPlain.setTypeface(myCustomFont_montserrat_regular);
        notesPlain.setTypeface(myCustomFont_montserrat_bold);
        updateStatusButton.setTypeface(myCustomFont_montserrat_regular);

        final ContractorReferralsObject s = (ContractorReferralsObject) this.getItem(position);

        nameTxt.setText(s.getReferral_name());
        phoneTxt.setText(s.getReferral_phone());
        emailTxt.setText(s.getReferral_email());
        additionalTxt.setText(s.getReferral_additional());
        statusTxt.setText(s.getReferral_status());

        if (s.getReferral_status().equals(Configs.referral_status_pending)){
            statusSpin.setSelection(0);
        }else if (s.getReferral_status().equals(Configs.referral_status_accepted)){
            statusSpin.setSelection(1);
        }else if (s.getReferral_status().equals(Configs.referral_status_rejected)){
            statusSpin.setSelection(2);
        }else if (s.getReferral_status().equals(Configs.referral_status_appointment_scheduled)){
            statusSpin.setSelection(3);
        }else if (s.getReferral_status().equals(Configs.referral_status_successful)){
            statusSpin.setSelection(4);
        }else if (s.getReferral_status().equals(Configs.referral_status_payment_sent)){
            statusSpin.setSelection(5);
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference img = ref
                .child(Configs.users)
                .child(s.getReferral_submitted_uid());
        img.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                submittedByTxt.setText(dataSnapshot.child(Configs.name).getValue(String.class));
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                storageRef.child(dataSnapshot.child(Configs.profile_image).getValue(String.class)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(c).load(uri).into(submittedByImg);
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
                Toast.makeText(c, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        updateStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusSpin.getSelectedItem().toString().equals(Configs.referral_status_pending))
                    updateStatus(Configs.referral_status_pending,s.getReferral_sentToCont_uid(),s.getReferral_key());
                else if (statusSpin.getSelectedItem().toString().equals(Configs.referral_status_accepted))
                    updateStatus(Configs.referral_status_accepted,s.getReferral_sentToCont_uid(),s.getReferral_key());
                else if (statusSpin.getSelectedItem().toString().equals(Configs.referral_status_rejected))
                    updateStatus(Configs.referral_status_rejected,s.getReferral_sentToCont_uid(),s.getReferral_key());
                else if (statusSpin.getSelectedItem().toString().equals(Configs.referral_status_appointment_scheduled))
                    updateStatus(Configs.referral_status_appointment_scheduled,s.getReferral_sentToCont_uid(),s.getReferral_key());
                else if (statusSpin.getSelectedItem().toString().equals(Configs.referral_status_successful))
                    updateStatus(Configs.referral_status_successful,s.getReferral_sentToCont_uid(),s.getReferral_key());
                else if (statusSpin.getSelectedItem().toString().equals(Configs.referral_status_payment_sent))
                    updateStatus(Configs.referral_status_payment_sent,s.getReferral_sentToCont_uid(),s.getReferral_key());
            }
        });

        return convertView;
    }

    public void updateStatus(final String status, String contUid, String key){
        HashMap map = new HashMap();
        map.put(Configs.referral_status,status);
        map.put(Configs.referral_updated_uid,FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference dbChang = FirebaseDatabase.getInstance().getReference();
        dbChang.child(Configs.users)
                .child(contUid)
                .child(Configs.referrals)
                .child(key)
                .updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(c, "Status Updated", Toast.LENGTH_SHORT).show();
                    delegate.mainFetch();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(c, e+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

}