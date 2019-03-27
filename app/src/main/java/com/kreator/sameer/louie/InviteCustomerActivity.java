package com.kreator.sameer.louie;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class InviteCustomerActivity extends AppCompatActivity {

    TextView mainTxt,emailTxt;
    EditText emailEdit;
    Button inviteBtn;
    ImageView closeImg;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_customer);
        mAuth = FirebaseAuth.getInstance();

        mainTxt = (TextView)findViewById(R.id.inviteCustomer_mainTxt);
        emailTxt = (TextView)findViewById(R.id.inviteCustomer_emailTxt);
        emailEdit = (EditText)findViewById(R.id.inviteCustomer_emailEdit);
        inviteBtn = (Button)findViewById(R.id.inviteCustomer_inviteBtn);
        closeImg = (ImageView)findViewById(R.id.inviteCustomer_closeImg);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        mainTxt.setTypeface(myCustomFont_montserrat_bold);
        emailTxt.setTypeface(myCustomFont_montserrat_regular);
        emailEdit.setTypeface(myCustomFont_montserrat_regular);
        inviteBtn.setTypeface(myCustomFont_montserrat_regular);

        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEdit.getText().toString().isEmpty()||emailEdit.getText().toString().equals("")){
                    Toast.makeText(InviteCustomerActivity.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                isCheckEmail(emailEdit.getText().toString(),new OnEmailCheckListener(){
                    @Override
                    public void onSucess(boolean isRegistered) {

                        final Dialog loadDialog = new Dialog(InviteCustomerActivity.this);
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
                        animSelect = (LottieAnimationView)loadDialog.findViewById(R.id.loading_one);
                        loadDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        animSelect.setAnimation("blueline.json");
                        animSelect.playAnimation();
                        animSelect.loop(true);

                        Window window = loadDialog.getWindow();
                        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        loadDialog.show();

                        inviteBtn.setEnabled(false);

                        if(isRegistered){
                            //email is registered, get UID using email
                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                            Query query = rootRef.child("users").orderByChild("email").equalTo(emailEdit.getText().toString());
                            ValueEventListener valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()) {

                                        final String key = ds.getKey();

                                        //get account type of the user[customer/contractor]
                                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                        DatabaseReference img = ref
                                                .child(Configs.users)
                                                .child(key)
                                                .child(Configs.account_type);
                                        img.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                String accType = dataSnapshot.getValue(String.class)+"";

                                                //check account type [customer/contractor]
                                                if (accType.equals(Configs.customer_type_account)){

                                                    DatabaseReference linkCheck = ref
                                                            .child(Configs.users)
                                                            .child(key)
                                                            .child(Configs.contractor_linked);
                                                    linkCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.getValue(Boolean.class)){
                                                                Toast.makeText(InviteCustomerActivity.this, "Customer linked with some other contractor", Toast.LENGTH_SHORT).show();
                                                                inviteBtn.setEnabled(true);
                                                                loadDialog.dismiss();
                                                            }else if(!dataSnapshot.getValue(Boolean.class)){
                                                                //Customer account! Send invite
                                                                DatabaseReference dbs = FirebaseDatabase.getInstance().getReference();
                                                                dbs.child(Configs.users)
                                                                        .child(key)
                                                                        .child(Configs.contractor_invite)
                                                                        .child(FirebaseAuth.getInstance().getUid()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){
                                                                            Toast.makeText(InviteCustomerActivity.this, "Invite Sent", Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        } else {
                                                                            Toast.makeText(InviteCustomerActivity.this, "Failed to send invite!", Toast.LENGTH_SHORT).show();
                                                                            inviteBtn.setEnabled(true);
                                                                            loadDialog.dismiss();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                }else if (accType.equals(Configs.contractor_type_account)){     //Contractor account can't be sent invite
                                                    Toast.makeText(InviteCustomerActivity.this, "This is a contractor account!", Toast.LENGTH_SHORT).show();
                                                    inviteBtn.setEnabled(true);
                                                    loadDialog.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Toast.makeText(InviteCustomerActivity.this, "Failed to send invite!", Toast.LENGTH_SHORT).show();
                                                inviteBtn.setEnabled(true);
                                                loadDialog.dismiss();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(InviteCustomerActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    inviteBtn.setEnabled(true);
                                    loadDialog.dismiss();
                                }
                            };
                            query.addListenerForSingleValueEvent(valueEventListener);
                        } else {
                            final String[] contName = new String[1];
                            DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference imgs = refs
                                    .child(Configs.users)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(Configs.name);
                            imgs.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   contName[0] = dataSnapshot.getValue(String.class)+"";
                                   Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                           "mailto", emailEdit.getText().toString(), null));
                                   emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invite from "+getString(R.string.app_name));
                                   emailIntent.putExtra(Intent.EXTRA_TEXT, "You have been invited by your contractor - "+contName[0]+" in the "+getString(R.string.app_name)+". You can download the app and logon to your account by either signing up or logging in using this email address to accept or reject the invite.");
                                   startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                   finish();
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });
                        }
                    }
                });
            }
        });

    }

    public void isCheckEmail(final String email,final OnEmailCheckListener listener){

        mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                if (task.isSuccessful()){
                    boolean check = !task.getResult().getProviders().isEmpty();
                    listener.onSucess(check);
                }else {
                    Toast.makeText(InviteCustomerActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
