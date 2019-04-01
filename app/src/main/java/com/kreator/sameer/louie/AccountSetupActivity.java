package com.kreator.sameer.louie;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AccountSetupActivity extends AppCompatActivity {

    TextView mainTxt,customerSelectBtn,contractorSelectBtn;
    ArrayAdapter<CharSequence> paymentAdapter;
    Spinner paymentMethod,pickFirmSpin;
    TextView paymentTxt;
    RelativeLayout customerLayout,contractorLayout;
    ImageView customerFloat;
    TextView contPickteamTxt,contOrTxt,contCreateTxt,contUploadTxt;
    EditText contContractorNameEdit;
    ImageView contLogoImg,contJoinFirm,contCreateFirm;

    FirebaseAuth auth;
    DatabaseReference db,dbFirms;

    public static ArrayList<String> firmKeysList = new ArrayList<>();
    public static ArrayList<String> firmsName = new ArrayList<>();
    ArrayAdapter<String> adapter;

    public static final String FB_STORAGE_PATH = "image/";
    public static final int REQUEST_CODE = 1234;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);
        auth = FirebaseAuth.getInstance();

        dbFirms = FirebaseDatabase.getInstance().getReference().child(Configs.firms);
        retrieveFirms();

        mainTxt = (TextView)findViewById(R.id.accountsetup_mainTxt);
        customerSelectBtn = (TextView)findViewById(R.id.accountsetup_customerSelectTxt);
        contractorSelectBtn = (TextView)findViewById(R.id.accountsetup_contractorSelectTxt);
        paymentTxt = (TextView)findViewById(R.id.accountsetup_customer_paymentMethodTxt);
        customerLayout = (RelativeLayout)findViewById(R.id.accountsetup_customerLayout);
        contractorLayout = (RelativeLayout)findViewById(R.id.accountsetup_contractorLayout);
        customerFloat = (ImageView)findViewById(R.id.accountsetup_customer_floatNxt);

        contPickteamTxt = (TextView)findViewById(R.id.accountsetup_contractor_pickTeamTxt);
        contOrTxt = (TextView)findViewById(R.id.accountsetup_contractor_orTxt);
        contCreateTxt = (TextView)findViewById(R.id.accountsetup_contractor_createTxt);
        contUploadTxt = (TextView)findViewById(R.id.accountsetup_contractor_uploadLogoTxt);
        contContractorNameEdit = (EditText)findViewById(R.id.accountsetup_contractor_nameEdit);
        contLogoImg = (ImageView)findViewById(R.id.accountsetup_contractor_uploadLogoImg);
        contJoinFirm = (ImageView)findViewById(R.id.accountsetup_contractor_pickFirmFloat);
        contCreateFirm = (ImageView)findViewById(R.id.accountsetup_contractor_newFirmFloat);
        pickFirmSpin = (Spinner)findViewById(R.id.accountsetup_contractor_pickTeamSpin);

        contLogoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBrowse_Click();
            }
        });
        customerFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCustomerAccount();
            }
        });
        contJoinFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContractorAccount_joinFirm(firmKeysList.get(pickFirmSpin.getSelectedItemPosition()));
            }
        });
        contCreateFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContractorAccount_createFirm();
            }
        });

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, firmsName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickFirmSpin.setAdapter(adapter);

        paymentMethod = (Spinner) findViewById(R.id.accountsetup_customer_paymentMethodSpin);
        paymentAdapter = ArrayAdapter.createFromResource(this, R.array.payment_methods, android.R.layout.simple_spinner_item);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethod.setAdapter(paymentAdapter);

        Typeface myCustomFont_montserrat_bold = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        Typeface myCustomFont_montserrat_regular = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        mainTxt.setTypeface(myCustomFont_montserrat_bold);
        customerSelectBtn.setTypeface(myCustomFont_montserrat_regular);
        contractorSelectBtn.setTypeface(myCustomFont_montserrat_regular);
        paymentTxt.setTypeface(myCustomFont_montserrat_regular);

        contPickteamTxt.setTypeface(myCustomFont_montserrat_regular);
        contOrTxt.setTypeface(myCustomFont_montserrat_regular);
        contCreateTxt.setTypeface(myCustomFont_montserrat_regular);
        contUploadTxt.setTypeface(myCustomFont_montserrat_regular);
        contContractorNameEdit.setTypeface(myCustomFont_montserrat_regular);

        customerSelectBtn.setBackground(getDrawable(R.drawable.accountsetup_selected));
        contractorSelectBtn.setBackground(getDrawable(R.drawable.accountsetup_unselected));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            customerSelectBtn.setTextColor(getColor(R.color.white_color));
            contractorSelectBtn.setTextColor(getColor(R.color.accountsetup_unselect));
        }
        customerLayout.setVisibility(View.VISIBLE);
        contractorLayout.setVisibility(View.GONE);

        customerSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerSelected();
            }
        });
        contractorSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contractorSelected();
            }
        });
    }

    public void customerSelected(){
        customerSelectBtn.setBackground(getDrawable(R.drawable.accountsetup_selected));
        contractorSelectBtn.setBackground(getDrawable(R.drawable.accountsetup_unselected));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            customerSelectBtn.setTextColor(getColor(R.color.white_color));
            contractorSelectBtn.setTextColor(getColor(R.color.accountsetup_unselect));
        }
        customerLayout.setVisibility(View.VISIBLE);
        contractorLayout.setVisibility(View.GONE);
    }

    public void contractorSelected(){
        customerSelectBtn.setBackground(getDrawable(R.drawable.accountsetup_unselected));
        contractorSelectBtn.setBackground(getDrawable(R.drawable.accountsetup_selected));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            customerSelectBtn.setTextColor(getColor(R.color.accountsetup_unselect));
            contractorSelectBtn.setTextColor(getColor(R.color.white_color));
        }
        customerLayout.setVisibility(View.GONE);
        contractorLayout.setVisibility(View.VISIBLE);
    }

    public void setCustomerAccount(){

        final Dialog loadDialog = new Dialog(AccountSetupActivity.this);
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

        HashMap hm = new HashMap();
        hm.put(Configs.accountsetup_done,true);
        hm.put(Configs.account_type,Configs.customer_type_account);
        if (paymentMethod.getSelectedItem().toString().equals("Cash"))
            hm.put(Configs.payment_mode,Configs.cash_type_payment);
        else if (paymentMethod.getSelectedItem().toString().equals("PayPal")) {
            hm.put(Configs.payment_mode, Configs.paypal_type_payment);
        }
        db = FirebaseDatabase.getInstance().getReference();
        db.child(Configs.users)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(AccountSetupActivity.this,SplashActivity.class));
                    finish();
                }else {
                    Toast.makeText(AccountSetupActivity.this, "Failed to set account type!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void setContractorAccount_joinFirm(final String firmKey){

        final Dialog loadDialog = new Dialog(AccountSetupActivity.this);
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
        animSelect = (LottieAnimationView) loadDialog.findViewById(R.id.loading_one);
        loadDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        animSelect.setAnimation("blueline.json");
        animSelect.playAnimation();
        animSelect.loop(true);

        Window window = loadDialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loadDialog.show();

        final DatabaseReference dbin = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference dbChang = FirebaseDatabase.getInstance().getReference();

        dbin.child(Configs.firms)
                .child(firmKey)
                .child("members")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            HashMap hsm = new HashMap();
                            hsm.put(Configs.accountsetup_done,true);
                            hsm.put(Configs.account_type,Configs.contractor_type_account);
                            hsm.put(Configs.firm_key,firmKey);
                            dbChang.child(Configs.users)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .updateChildren(hsm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(AccountSetupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AccountSetupActivity.this, SplashActivity.class));
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AccountSetupActivity.this, e+"", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(AccountSetupActivity.this, "Firm creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AccountSetupActivity.this, e + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setContractorAccount_createFirm(){
        if (contContractorNameEdit.getText().toString().equals("") || contContractorNameEdit.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter account name", Toast.LENGTH_SHORT).show();
            return;
        } else if (imgUri == null){
            Toast.makeText(this, "Select Logo", Toast.LENGTH_SHORT).show();
            return;
        }else {
            final Dialog loadDialog = new Dialog(AccountSetupActivity.this);
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
            animSelect = (LottieAnimationView) loadDialog.findViewById(R.id.loading_one);
            loadDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            animSelect.setAnimation("blueline.json");
            animSelect.playAnimation();
            animSelect.loop(true);

            Window window = loadDialog.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            loadDialog.show();

            final String saltString = getSaltString();
            final String fileName = FirebaseAuth.getInstance().getCurrentUser().getUid()+FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri);
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference ref = mStorageRef.child(fileName);
            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    HashMap s = new HashMap();
                    s.put("firm_name",contContractorNameEdit.getText().toString());
                    s.put("firm_key",saltString);
                    s.put("firm_logo",fileName);
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference dbin = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference dbChang = FirebaseDatabase.getInstance().getReference();

                    db.child(Configs.firms).child(saltString).setValue(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dbin.child(Configs.firms)
                                        .child(saltString)
                                        .child("members")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(true)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            HashMap hsm = new HashMap();
                                            hsm.put(Configs.accountsetup_done,true);
                                            hsm.put(Configs.account_type,Configs.contractor_type_account);
                                            hsm.put(Configs.firm_key,saltString);
                                            dbChang.child(Configs.users)
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .updateChildren(hsm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(AccountSetupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(AccountSetupActivity.this, SplashActivity.class));
                                                        finish();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AccountSetupActivity.this, e+"", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(AccountSetupActivity.this, "Firm creation failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AccountSetupActivity.this, e + "", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }

    }

    public void btnBrowse_Click(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUri = data.getData();

            try {
                Bitmap bmp = ImagePicker.getImageFromResult(this, resultCode, data);//your compressed bitmap here
                contLogoImg.setImageBitmap(bmp);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void retrieveFirms(){

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference imgs = db.child(Configs.firms);
        imgs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                firmKeysList.clear();
                firmsName.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){         //getting all firm keys
                    firmKeysList.add(ds.getRef().getKey()+"");
                }

                for (int i = 0; i < firmKeysList.size(); i++){
                    DatabaseReference dbs = db.child(Configs.firms).child(firmKeysList.get(i)).child("firm_name");
                    dbs.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    firmsName.add(dataSnapshot.getValue(String.class));
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
