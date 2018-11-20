package com.example.user.quicktrade.Activities;

import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.quicktrade.R;
import com.example.user.quicktrade.model.Product;
import com.example.user.quicktrade.model.UserRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth authInstance;
    private EditText etEmail, etPassword, etName, etSurname, etUsername, etBirthday, etPhone;
    private Button btSignup;

    DatabaseReference bbddUser;

    UserRegistration userD;

    String email;
    String password;
    String user_name;
    String user_surnames;
    String user_email;
    String user_nickname;
    String user_pass;
    String user_birthday;
    String user_phone;
    String user_product;

    /**
     * Default method of activities
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etBirthday = findViewById(R.id.et_birthday);
        etName = findViewById(R.id.et_name);
        etSurname = findViewById(R.id.et_surnames);
        etUsername = findViewById(R.id.et_username);
        etPhone = findViewById(R.id.et_phone);

        btSignup = findViewById(R.id.bt_signup);

        btSignup.setOnClickListener(this);

        bbddUser = FirebaseDatabase.getInstance().getReference("users");
    }


    /**
     * In this method collect the information to add an user and if there are all data register call the method to register it.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_signup){
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            user_name = etName.getText().toString();
            user_surnames = etSurname.getText().toString();
            user_email = etEmail.getText().toString();
            user_nickname = etUsername.getText().toString();
            user_pass = etPassword.getText().toString();
            user_birthday = etBirthday.getText().toString();
            user_phone = etPhone.getText().toString();
            user_product = null;

            //With these if check if the strings are empty.
            if( (!email.isEmpty()) && (!password.isEmpty()) && (!user_name.isEmpty())){
                if((!user_surnames.isEmpty()) && (!user_email.isEmpty()) && (!user_nickname.isEmpty()) && (!user_pass.isEmpty())){
                    if(!user_birthday.isEmpty() && (!user_phone.isEmpty())){
                        AddUser(email, password);
                    }
                }
            }

        }
    }

    /**
     * Method to register the user
     *
     * @param email
     * @param password
     */
    private void AddUser(String email, String password){
        //AuthInstace from FireBase
        authInstance = FirebaseAuth.getInstance();

        authInstance.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = authInstance.getCurrentUser();

                            Toast.makeText(SignUpActivity.this, "Authentication correct.", Toast.LENGTH_SHORT).show();

                            //ID from user
                            String userId = authInstance.getUid();

                            //Instance of the User class
                            userD = new UserRegistration(userId, user_name, user_surnames, user_email, user_nickname, user_pass, user_birthday, user_phone);
                            String clave = bbddUser.push().getKey();

                            //Insert in the db te user
                            bbddUser.child(clave).setValue(userD);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
