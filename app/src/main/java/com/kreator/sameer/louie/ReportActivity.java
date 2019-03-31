package com.kreator.sameer.louie;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ReportActivity extends AppCompatActivity {

    String firmKey;
    EditText descEdit;
    TextView mainTxt,descTxt;
    Button submitBtn;
    ImageView closeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            firmKey = extras.getString("firmKey");
        }

        descTxt = (TextView)findViewById(R.id.report_describeTxt);
        mainTxt = (TextView)findViewById(R.id.report_mainTxt);
        descEdit = (EditText)findViewById(R.id.report_descEdit);
        submitBtn = (Button)findViewById(R.id.report_submitBtn);
        closeImg = (ImageView)findViewById(R.id.report_closeImg);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        mainTxt.setTypeface(myCustomFont_montserrat_bold);
        descTxt.setTypeface(myCustomFont_montserrat_regular);
        descEdit.setTypeface(myCustomFont_montserrat_regular);
        submitBtn.setTypeface(myCustomFont_montserrat_regular);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport();
            }
        });

    }

    public void submitReport(){

        if (descEdit.getText().toString().equals("") || descEdit.getText().toString().isEmpty()){
            Toast.makeText(this, "Describe your issue", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference dbin = FirebaseDatabase.getInstance().getReference();
        DatabaseReference sendRef = FirebaseDatabase.getInstance().getReference().child(Configs.report).child(firmKey).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        String refKey = sendRef.push().getKey();

        HashMap hsm = new HashMap();
        hsm.put(Configs.report_by_uid,FirebaseAuth.getInstance().getCurrentUser().getUid());
        hsm.put(Configs.report_description,descEdit.getText().toString());
        hsm.put(Configs.report_key,refKey);

        dbin.child(Configs.report)
                .child(firmKey)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(refKey)
                .updateChildren(hsm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ReportActivity.this, "Team reported!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReportActivity.this, e+"", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
