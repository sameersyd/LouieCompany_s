package com.kreator.sameer.louie;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;

public class AddReferalActivity extends AppCompatActivity {

    TextView mainTxt,nameTxt,phoneTxt,emailTxt,additionalTxt;
    EditText nameEdit,phoneEdit,emailEdit,additionalEdit;
    ImageView closeImg,addReferralBtn;
    String contUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_referal);

        mainTxt = (TextView)findViewById(R.id.addreferal_mainTxt);
        nameTxt = (TextView)findViewById(R.id.addreferal_nameTxt);
        phoneTxt = (TextView)findViewById(R.id.addreferal_phoneTxt);
        emailTxt = (TextView)findViewById(R.id.addreferal_emailTxt);
        additionalTxt = (TextView)findViewById(R.id.addreferal_additionalTxt);
        nameEdit = (EditText)findViewById(R.id.addreferal_nameEdit);
        phoneEdit = (EditText)findViewById(R.id.addreferal_phoneEdit);
        emailEdit = (EditText)findViewById(R.id.addreferal_emailEdit);
        additionalEdit = (EditText)findViewById(R.id.addreferal_additionalEdit);
        addReferralBtn = (ImageView)findViewById(R.id.addreferal_addFloatImg);
        closeImg = (ImageView)findViewById(R.id.addreferal_closeImg);

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        mainTxt.setTypeface(myCustomFont_montserrat_bold);
        nameTxt.setTypeface(myCustomFont_montserrat_regular);
        phoneTxt.setTypeface(myCustomFont_montserrat_regular);
        emailTxt.setTypeface(myCustomFont_montserrat_regular);
        additionalTxt.setTypeface(myCustomFont_montserrat_regular);
        nameEdit.setTypeface(myCustomFont_montserrat_regular);
        phoneEdit.setTypeface(myCustomFont_montserrat_regular);
        emailEdit.setTypeface(myCustomFont_montserrat_regular);
        additionalEdit.setTypeface(myCustomFont_montserrat_regular);

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addReferralBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReferalMethod();
            }
        });

        //Get cont key
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference img = ref
                .child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Configs.contractor_link_uid);
        img.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contUid = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addReferalMethod(){

        if (nameEdit.getText().toString().isEmpty() || nameEdit.getText().toString().equals("")){
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }else if (phoneEdit.getText().toString().isEmpty() || phoneEdit.getText().toString().equals("")){
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }else if (emailEdit.getText().toString().isEmpty() || emailEdit.getText().toString().equals("")){
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference sendRef = FirebaseDatabase.getInstance().getReference().child(Configs.users).child(contUid).child(Configs.referrals);
        String refKey = sendRef.push().getKey();

        HashMap hashMap = new HashMap();
        hashMap.put(Configs.referral_name,nameEdit.getText().toString());
        hashMap.put(Configs.referral_phone,phoneEdit.getText().toString());
        hashMap.put(Configs.referral_email,emailEdit.getText().toString());
        if (additionalEdit.getText().toString().isEmpty() || additionalEdit.getText().toString().equals(""))
            hashMap.put(Configs.referral_additional,"Additional Notes Empty");
        else
            hashMap.put(Configs.referral_additional,additionalEdit.getText().toString());
        hashMap.put(Configs.referral_status,Configs.referral_status_pending);
        hashMap.put(Configs.referral_submitted_uid,FirebaseAuth.getInstance().getCurrentUser().getUid()+"");
        hashMap.put(Configs.referral_updated_uid,contUid);
        hashMap.put(Configs.referral_key,refKey);
        hashMap.put(Configs.referral_updated_timestamp,System.currentTimeMillis());
        hashMap.put(Configs.referral_sentToCont_uid,contUid);

        sendRef.child(refKey).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddReferalActivity.this, "Referral successfully sent", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddReferalActivity.this, e+"", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
