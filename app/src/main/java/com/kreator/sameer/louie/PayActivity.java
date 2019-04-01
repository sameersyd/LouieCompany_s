package com.kreator.sameer.louie;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.kreator.sameer.louie.ViewContractor.ViewContractorActivity;

public class PayActivity extends AppCompatActivity {

    TextView custNameTxt,contactTxt,numberTxt,emailTxt,amountTxt,cashTxt;
    Button payBtn;
    EditText amountEdit;
    ImageView closeImg,profileImg;
    String custUid;
    String recEmail,recAmount,recDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            custUid = extras.getString("custUid");
        }

        custNameTxt = (TextView)findViewById(R.id.pay_nameTxt);
        contactTxt = (TextView)findViewById(R.id.pay_contactTxt);
        numberTxt = (TextView)findViewById(R.id.pay_numberTxt);
        emailTxt = (TextView)findViewById(R.id.pay_emailTxt);
        amountTxt = (TextView)findViewById(R.id.pay_amountTxt);
        payBtn = (Button)findViewById(R.id.pay_payBtn);
        closeImg = (ImageView)findViewById(R.id.pay_closeImg);
        profileImg = (ImageView)findViewById(R.id.pay_profilePic);
        amountEdit = (EditText)findViewById(R.id.pay_amountEdit);
        cashTxt = (TextView) findViewById(R.id.pay_cashTxt);

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        custNameTxt.setTypeface(myCustomFont_montserrat_bold);
        contactTxt.setTypeface(myCustomFont_montserrat_bold);
        numberTxt.setTypeface(myCustomFont_montserrat_regular);
        emailTxt.setTypeface(myCustomFont_montserrat_regular);
        amountTxt.setTypeface(myCustomFont_montserrat_regular);
        payBtn.setTypeface(myCustomFont_montserrat_regular);
        amountEdit.setTypeface(myCustomFont_montserrat_regular);
        cashTxt.setTypeface(myCustomFont_montserrat_regular);

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        payBtn.setEnabled(false);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Configs.users)
                .child(custUid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                custNameTxt.setText(dataSnapshot.child(Configs.name).getValue(String.class));
                numberTxt.setText(dataSnapshot.child(Configs.phone).getValue(String.class));
                emailTxt.setText(dataSnapshot.child(Configs.email).getValue(String.class));
                payBtn.setText("Pay "+dataSnapshot.child(Configs.name).getValue(String.class));

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                storageRef.child(dataSnapshot.child(Configs.profile_image).getValue(String.class)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(PayActivity.this).load(uri).into(profileImg);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(PayActivity.this, exception+"", Toast.LENGTH_SHORT).show();
                    }
                });

                if (dataSnapshot.child(Configs.payment_mode).getValue(String.class).equals(Configs.cash_type_payment)){

                    payBtn.setEnabled(false);
                    amountEdit.setVisibility(View.INVISIBLE);
                    amountTxt.setVisibility(View.INVISIBLE);
                    payBtn.setVisibility(View.INVISIBLE);
                    cashTxt.setVisibility(View.VISIBLE);

                }else if (dataSnapshot.child(Configs.payment_mode).getValue(String.class).equals(Configs.paypal_type_payment)){

                    amountEdit.setVisibility(View.VISIBLE);
                    amountTxt.setVisibility(View.VISIBLE);
                    payBtn.setVisibility(View.VISIBLE);
                    cashTxt.setVisibility(View.INVISIBLE);

                    recEmail = dataSnapshot.child(Configs.email).getValue(String.class);
                    recAmount = amountEdit.getText().toString();
                    recDesc = "Payment from "+getString(R.string.app_name);

                    payBtn.setEnabled(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PayActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountEdit.getText().toString().isEmpty() || amountEdit.getText().toString().equals("")){
                    Toast.makeText(PayActivity.this, "Enter amount!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if (Integer.parseInt(amountEdit.getText().toString()) < 1){
                        Toast.makeText(PayActivity.this, "Enter correct amount!", Toast.LENGTH_SHORT).show();
                        return;
                    }else if (Integer.parseInt(amountEdit.getText().toString()) > 100000){
                        Toast.makeText(PayActivity.this, "Amount too large!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?&amp;cmd=_xclick&amp;business="+recEmail+"&amp;currency_code=USD&amp;amount="+recAmount+"&amp;item_name="+recDesc));
                startActivity(browserIntent);
            }
        });

    }



}
