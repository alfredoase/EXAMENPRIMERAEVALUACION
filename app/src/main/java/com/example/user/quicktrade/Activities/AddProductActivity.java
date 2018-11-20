package com.example.user.quicktrade.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.user.quicktrade.R;
import com.example.user.quicktrade.model.Product;
import com.example.user.quicktrade.model.UserRegistration;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

import static com.example.user.quicktrade.MainActivity.signInstance;
import static com.example.user.quicktrade.model.ProductAdaptor.TitleProduct;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference bbddProd;
    DatabaseReference bbddName;

    RadioGroup cat;
    RadioButton rb_tech, rb_car, rb_home;

    EditText name, description, location;
    NumberPicker precio;

    Button bt_add;

    ImageView imgProd;

    private int REQUEST_IMAGE_CAPTURE = 1;

    //userName variable.
    //public static String userName;

    int pruebaI = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //Cogemos la referencia de la base de datos para realizar las operaciones
        bbddProd = FirebaseDatabase.getInstance().getReference("products");

        name = findViewById(R.id.et_prod_name);
        description = findViewById(R.id.et_prod_description);
        location = findViewById(R.id.et_prod_location);
        cat = findViewById(R.id.ev_prod_cat);
        precio = findViewById(R.id.np_prod_price);
        bt_add = findViewById(R.id.bt_prod_addprod);
        rb_car = findViewById(R.id.rb_car);
        rb_tech = findViewById(R.id.rb_tech);
        rb_home = findViewById(R.id.rb_home);
        imgProd = findViewById(R.id.iv_setimage);

        //VALUES OF PRICE
        precio.setMinValue(0);
        precio.setMaxValue(5000);

        bt_add.setOnClickListener(this);
        imgProd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == imgProd.getId()){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        if(v.getId() == R.id.bt_prod_addprod){
            String titulo_prod = name.getText().toString();
            String precio_prod = Integer.toString(precio.getValue());
            String lugar_rec = location.getText().toString();
            String descr_prod = description.getText().toString();
            String cat_prod = "";
            String prod_favorito = "";

            int cat_prod_int = cat.getCheckedRadioButtonId();

            if (cat_prod_int == rb_tech.getId()){
                cat_prod = "Technology";
            }else if(cat_prod_int == rb_car.getId()){
                cat_prod = "Car";
            }else{
                cat_prod = "Home";
            }

            if(!TextUtils.isEmpty(titulo_prod)&&!TextUtils.isEmpty(precio_prod)){
                if(!TextUtils.isEmpty(lugar_rec)&&!TextUtils.isEmpty(descr_prod)&&!TextUtils.isEmpty(cat_prod)){
                    final String userNameS = "";

                    //UserID to the product
                    String userID_prod = signInstance.getUid();
                    String prod_puntuation = "0";

                    Product prod = new Product(userID_prod, userNameS, titulo_prod, precio_prod, lugar_rec, descr_prod, cat_prod, prod_puntuation, prod_favorito);

                    String clave = bbddProd.push().getKey();

                    bbddProd.child(clave).setValue(prod);

                    Toast.makeText(this, "Product added correctly!", Toast.LENGTH_SHORT).show();

                    //OBTENER EL USUARIO
                    DatabaseReference bbddProd = FirebaseDatabase.getInstance().getReference("users");
                    Query qShow = bbddProd.orderByChild(getString(R.string.userId)).equalTo(userID_prod);
                    qShow.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                UserRegistration user = dataSnapshot1.getValue(UserRegistration.class);
                                final String userName = user.getUser_name();

                                String titulo_prod = name.getText().toString();

                                final DatabaseReference bbddProd2 = FirebaseDatabase.getInstance().getReference("products");
                                Query qEdit = bbddProd2.orderByChild("titulo_prod").equalTo(titulo_prod);

                                qEdit.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                            //Get the key
                                            String clave = dataSnapshot1.getKey();

                                            bbddProd2.child(clave).child("userName_prod").setValue(userName);
                                        }
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
            }else{
                Toast.makeText(this, "Fail adding product!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgProd.setImageBitmap(imageBitmap);
            encodeBitmapAndSaveToFirebase(imageBitmap);
        }
    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("products").child("imageUrl");
        ref.setValue(imageEncoded);
    }
}
