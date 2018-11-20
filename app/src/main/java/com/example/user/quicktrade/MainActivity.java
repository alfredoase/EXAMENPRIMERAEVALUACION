package com.example.user.quicktrade;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.quicktrade.Activities.AppActivity;
import com.example.user.quicktrade.Activities.SignUpActivity;
import com.example.user.quicktrade.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_username, et_password;
    private Button bt_login;
    private LinearLayout ll_signup;
    private TextView tv_signup;

    //VARIABLE DE LA AUTENTICACION DE FIREBASE
    public static FirebaseAuth signInstance;

    /**
     * Default method of activities
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buscamos las vistas del activity para poder trabajar con ellas
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        bt_login = findViewById(R.id.bt_login);
        ll_signup = findViewById(R.id.ll_signup);
        tv_signup = findViewById(R.id.tv_signup);


        //Añadimos listener a dos vistas para poder trabajar con ellas
        bt_login.setOnClickListener(this);
        ll_signup.setOnClickListener(this);

        //
        //
        //EJERCICIO 1
        //
        //
        //Default message to give the welcome, when apps open.
        ShowWelcome();
    }

    /**
     * Detects the view selected, and do differents operations.
     *
     * @param v
     */
    //En este metodo lo que hacemos es obtener las ids y realizar los metodos necesarios
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == bt_login.getId()){
                //METODO PARA ABRIR LA APP UNA VEZ LOGUEADO
                String email = et_username.getText().toString();
                String password = et_password.getText().toString();

                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Fill the field with your credentials!", Toast.LENGTH_LONG).show();
                }else {
                    SignUser(email, password);
                }
        }
        if(id == ll_signup.getId()){
            //METODO PARA ABRIR LA ACTIVIDAD DE REGISTRO
            OpenSignUpActivity();

        }
    }

    /**
     * Open the signup activity.
     */
    //METODO PARA ABRIR LA ACTIVIDAD DE REGISTRO
    private void OpenSignUpActivity (){
        Intent intentSignUpActivity = new Intent(this, SignUpActivity.class);

        startActivity(intentSignUpActivity);
    }

    /**
     * The method that allow login in the app.
     *
     * @param email
     * @param password
     */
    //METODO DE LOGUEO
    private void SignUser(String email, String password){
        signInstance = FirebaseAuth.getInstance();

            signInstance.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intentAppActivity = new Intent(MainActivity.this, AppActivity.class);
                                startActivity(intentAppActivity);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "You need login, have an account?", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

    }

    private void ShowWelcome(){
        Toast.makeText(this, R.string.welcome_message, Toast.LENGTH_LONG).show();
    }
}
