package com.kreator.sameer.louie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            DatabaseReference accSetupDb = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(Configs.accountsetup_done);
            try {
                accSetupDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(Boolean.class) == null || (dataSnapshot.getValue(Boolean.class) != true && dataSnapshot.getValue(Boolean.class) != false)){
                            Toast.makeText(SplashActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                            auth.signOut();
                            finish();
                            return;
                        }
                        if (dataSnapshot.getValue(Boolean.class)){
                            DatabaseReference typeBb = FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(Configs.account_type);
                            try {
                                typeBb.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue(String.class).equals(Configs.customer_type_account)){
                                            startActivity(new Intent(SplashActivity.this,CustomerActivity.class));
                                            finish();
                                        }else if (dataSnapshot.getValue(String.class).equals(Configs.contractor_type_account)) {
                                            startActivity(new Intent(SplashActivity.this,ContractorActivity.class));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }catch (Exception e){
                                Toast.makeText(SplashActivity.this, e+"", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            startActivity(new Intent(SplashActivity.this,AccountSetupActivity.class));
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }catch (Exception e){
                Toast.makeText(this, e+"", Toast.LENGTH_SHORT).show();
            }
        }else{
            startActivity(new Intent(this,LoginSignupActivity.class));
            finish();
        }

    }
}
