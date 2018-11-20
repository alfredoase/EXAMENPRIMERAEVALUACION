package com.example.user.quicktrade.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.quicktrade.R;
import com.example.user.quicktrade.model.Product;
import com.example.user.quicktrade.model.UserRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.user.quicktrade.MainActivity.signInstance;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etPrName, etPrSurnames, etPrEmail, etPrUsername, etPrPassword, etPrBirthday, etPrPhone;
    Button btEditInfo;
    DatabaseReference bbddProfile;

    String name;


    /**
     * Deafult method
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etPrName = findViewById(R.id.et_pr_name);
        etPrSurnames = findViewById(R.id.et_pr_surnames);
        etPrEmail = findViewById(R.id.et_pr_email);
        etPrUsername = findViewById(R.id.et_pr_username);
        etPrPassword = findViewById(R.id.et_pr_password);
        etPrBirthday = findViewById(R.id.et_pr_birthday);
        etPrPhone = findViewById(R.id.et_pr_phone);
        btEditInfo = findViewById(R.id.bt_editinfor);

        btEditInfo.setOnClickListener(this);

        name = etPrName.getText().toString();

        //Shows the user info
        ShowUserInfo();
    }

    /**
     * Method to detect the click
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == btEditInfo.getId()){
            String email = etPrEmail.getText().toString();
            String password = etPrPassword.getText().toString();

            EditUserFr(email, password);
            EditUserBD();
        }
    }

    /**
     * Method to show the user info
     */
    //Shows the user info in the edittexts
    public void ShowUserInfo(){
        //DATOS DE LA CONEXION
        String userID = signInstance.getUid();

        //REFERENCIA A LA BASE DE DATOS
        bbddProfile = FirebaseDatabase.getInstance().getReference("users");

        Query qShow = bbddProfile.orderByChild(getString(R.string.userId)).equalTo(userID);

        qShow.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    UserRegistration user = dataSnapshot1.getValue(UserRegistration.class);

                    String name = user.getUser_name();
                    String surnames = user.getUser_surnames();
                    String email = user.getUser_email();
                    String username = user.getUser_nickname();
                    String password = user.getUser_pass();
                    String birthday = user.getUser_birthday();
                    String phone = user.getUser_phone();

                    etPrName.setText(name);
                    etPrSurnames.setText(surnames);
                    etPrEmail.setText(email);
                    etPrUsername.setText(username);
                    etPrPassword.setText(password);
                    etPrBirthday.setText(birthday);
                    etPrPhone.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * Metho to edit the user info
     */
    //Edit the user info in the db
    private void EditUserBD(){
        //Conection info
        String userID = signInstance.getUid();
        Toast.makeText(this, "EDIT"+userID, Toast.LENGTH_SHORT).show();

        //Db reference
        bbddProfile = FirebaseDatabase.getInstance().getReference("users");
        Query qEdit = bbddProfile.orderByChild("userId").equalTo(userID);

        qEdit.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = etPrName.getText().toString();
                String surnames = etPrSurnames.getText().toString();
                String email = etPrEmail.getText().toString();
                String username = etPrUsername.getText().toString();
                String password = etPrPassword.getText().toString();
                String birthday = etPrBirthday.getText().toString();
                String phone = etPrPhone.getText().toString();

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    //Get the key and set the info on the edittext
                    String clave = dataSnapshot1.getKey();

                    bbddProfile.child(clave).child("user_name").setValue(etPrName.getText().toString());
                    bbddProfile.child(clave).child("user_surnames").setValue(surnames);
                    bbddProfile.child(clave).child("user_email").setValue(email);
                    bbddProfile.child(clave).child("user_nickname").setValue(username);
                    bbddProfile.child(clave).child("user_pass").setValue(password);
                    bbddProfile.child(clave).child("user_phone").setValue(birthday);
                    bbddProfile.child(clave).child("user_birthday").setValue(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * With this method updates the authentication credentials.
     *
     * @param email
     * @param password
     */
    //Update the authentication credentials
    private void EditUserFr(String email, final String password){
        FirebaseUser user = signInstance.getCurrentUser();

        user.updateEmail(email);
        user.updatePassword(password);
    }

}