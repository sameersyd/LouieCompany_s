package com.kreator.sameer.louie.Contractor;

import android.content.Context;
import android.graphics.Typeface;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kreator.sameer.louie.Configs;
import com.kreator.sameer.louie.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ContractorReferralsCustomadapter extends BaseAdapter {

    Context c;
    ContractorReferralFrag delegate;
    ArrayList<ContractorReferralsObject> spacecrafts = new ArrayList<>();
    ArrayList<ContractorReferralsObject> originalList = new ArrayList<>();

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

        Spinner statusSpin = (Spinner) convertView.findViewById(R.id.referralModel_statusUpdateSpin);
        ImageView submittedByImg = (ImageView)convertView.findViewById(R.id.referralModel_submittedByImg);
        Button payButton = (Button)convertView.findViewById(R.id.referralModel_payBtn);

        statusAdapter = ArrayAdapter.createFromResource(c, R.array.referral_status, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpin.setAdapter(statusAdapter);

        nameTxt.setTypeface(myCustomFont_montserrat_regular);
        phoneTxt.setTypeface(myCustomFont_montserrat_regular);
        emailTxt.setTypeface(myCustomFont_montserrat_regular);
        additionalTxt.setTypeface(myCustomFont_montserrat_regular);
        submittedByTxt.setTypeface(myCustomFont_montserrat_bold);
        statusTxt.setTypeface(myCustomFont_montserrat_regular);
        submittedPlain.setTypeface(myCustomFont_montserrat_regular);
        notesPlain.setTypeface(myCustomFont_montserrat_bold);
        payButton.setTypeface(myCustomFont_montserrat_regular);

        final ContractorReferralsObject s = (ContractorReferralsObject) this.getItem(position);

        nameTxt.setText(s.getReferral_name());
        phoneTxt.setText(s.getReferral_phone());
        emailTxt.setText(s.getReferral_email());
        additionalTxt.setText(s.getReferral_additional());
        statusTxt.setText(s.getReferral_status());
//        Glide.with(c).load(s.getProfileImg()).into(submittedByImg);

        if (s.getReferral_status().equals(Configs.referral_status_pending)){
            statusSpin.setSelection(0);
            payButton.setVisibility(View.GONE);
        }else if (s.getReferral_status().equals(Configs.referral_status_accepted)){
            statusSpin.setSelection(1);
            payButton.setVisibility(View.GONE);
        }else if (s.getReferral_status().equals(Configs.referral_status_rejected)){
            statusSpin.setSelection(2);
            payButton.setVisibility(View.GONE);
        }else if (s.getReferral_status().equals(Configs.referral_status_appointment_scheduled)){
            statusSpin.setSelection(3);
            payButton.setVisibility(View.GONE);
        }else if (s.getReferral_status().equals(Configs.referral_status_successful)){
            statusSpin.setSelection(4);
            payButton.setVisibility(View.VISIBLE);
        }else if (s.getReferral_status().equals(Configs.referral_status_payment_sent)){
            statusSpin.setSelection(5);
            payButton.setVisibility(View.VISIBLE);
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference img = ref
                .child(Configs.users)
                .child(s.getReferral_submitted_uid())
                .child(Configs.name);
        img.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                submittedByTxt.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        statusSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position){
//                    case 0:
//                        Log.v("*********&^*&^&%*",position+":0909");
//                        updateStatus(Configs.referral_status_pending);
//                        break;
//                    case 1:
//                        Log.v("*********&^*&^&%*",position+":0909");
//                        updateStatus(Configs.referral_status_accepted);
//                        break;
//                    case 2:
//                        Log.v("*********&^*&^&%*",position+":0909");
//                        updateStatus(Configs.referral_status_rejected);
//                        break;
//                    case 3:
//                        Log.v("*********&^*&^&%*",position+":0909");
//                        updateStatus(Configs.referral_status_appointment_scheduled);
//                        break;
//                    case 4:
//                        Log.v("*********&^*&^&%*",position+":0909");
//                        updateStatus(Configs.referral_status_successful);
//                        break;
//                    case 5:
//                        Log.v("*********&^*&^&%*",position+":0909");
//                        updateStatus(Configs.referral_status_payment_sent);
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        return convertView;
    }

    public void updateStatus(String status){
        HashMap map = new HashMap();
        map.put(Configs.referral_status,status);
        map.put(Configs.referral_updated_uid,FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference dbChang = FirebaseDatabase.getInstance().getReference();
        dbChang.child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(c, "Status Updated", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(c, e+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public ArrayList<ContractorReferralsObject> sortByVotes(ArrayList<ContractorReferralsObject> list){
//        Collections.sort(list,ageComparator);
//        return list;
//    }

//    public static Comparator<ContractorReferralsObject> ageComparator = new Comparator<ContractorReferralsObject>() {
//        @Override
//        public int compare(ContractorReferralsObject user1, ContractorReferralsObject user2) {
//            return (Integer.parseInt(user2.getAge()) < Integer.parseInt(user1.getAge()) ? -1 :
//                    (Integer.parseInt(user2.getAge()) == Integer.parseInt(user1.getAge()) ? 0 : 1));
//        }
//    };

//    public void update(ArrayList<ContractorReferralsObject> list){
//        originalList = spacecrafts;
//        spacecrafts.clear();
//        spacecrafts.addAll(sortByVotes(list));
//        notifyDataSetChanged();
//    }

}