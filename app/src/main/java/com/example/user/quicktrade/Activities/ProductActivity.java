package com.example.user.quicktrade.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.user.quicktrade.R;
import com.example.user.quicktrade.model.Product;
import com.example.user.quicktrade.model.UserRegistration;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.user.quicktrade.MainActivity.signInstance;
import static com.example.user.quicktrade.model.ProductAdaptor.TitleProduct;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaration of variables to work with they
    DatabaseReference bbddProfile;
    EditText etProdTitle;
    EditText etProdDescr;
    NumberPicker npProdPrice;
    RadioGroup rbProdCat;
    EditText etProdLoc;
    RadioButton rbprodtech, rbprodcar, rbprodhome;
    Button btprodEdit, btprodDelete, btAñadirFav;

    /**
     * Default method of activities
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        rbProdCat = findViewById(R.id.rbprodcat);
        etProdTitle = findViewById(R.id.etprodname);
        etProdDescr = findViewById(R.id.etproddescription);
        npProdPrice = findViewById(R.id.npprodprice);
        etProdLoc = findViewById(R.id.etprodlocation);
        rbprodtech = findViewById(R.id.rbprodtech);
        rbprodhome = findViewById(R.id.rbprodhome);
        rbprodcar = findViewById(R.id.rbprodcar);
        btprodEdit = findViewById(R.id.btprod_ed);
        btprodDelete = findViewById(R.id.btprod_del);
        btAñadirFav = findViewById(R.id.btprod_fav);

        btprodEdit.setVisibility(View.INVISIBLE);
        btprodDelete.setVisibility(View.INVISIBLE);
        btAñadirFav.setVisibility(View.INVISIBLE);

        npProdPrice.setMinValue(0);
        npProdPrice.setMaxValue(5000);

        btprodEdit.setOnClickListener(this);
        btprodDelete.setOnClickListener(this);
        btAñadirFav.setOnClickListener(this);
    }

    /**
     * These method shows the product info always the activity appear.
     */
    @Override
    protected void onStart() {
        ShowProductInfo();

        super.onStart();
    }

    /**
     * These method show the product info on the views of the activity.
     */
    public void ShowProductInfo(){
        //DataBase reference
        bbddProfile = FirebaseDatabase.getInstance().getReference("products");

        Query qShowProd = bbddProfile.orderByChild("titulo_prod").equalTo(TitleProduct);

        qShowProd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Bucle to extract all the information about the user
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Product prod = dataSnapshot1.getValue(Product.class);

                    String titulo_prod = prod.getTitulo_prod();
                    String descr_prod = prod.getDescr_prod();
                    String precio_prod = prod.getPrecio_prod();
                    String cat_prod = prod.getCat_prod();
                    String lugar_rec = prod.getLugar_rec();
                    String prod_id = prod.getUserID_prod();

                    String userID = signInstance.getUid();

                    //If the User ID match with the UserID of the product, shows edition buttons
                    if(prod_id.equals(userID)){
                        btprodEdit.setVisibility(View.VISIBLE);
                        btprodDelete.setVisibility(View.VISIBLE);
                        btAñadirFav.setVisibility(View.VISIBLE);
                    }

                    etProdTitle.setText(titulo_prod);
                    etProdDescr.setText(descr_prod);
                    npProdPrice.setValue(Integer.valueOf(precio_prod));
                    etProdLoc.setText(lugar_rec);

                    if(cat_prod == "Home"){
                        rbProdCat.setId(rbprodtech.getId());
                    }
                    if(cat_prod == "Technology"){
                        rbProdCat.setId(rbprodhome.getId());
                    }
                    if(cat_prod == "Car"){
                        rbProdCat.setId(rbprodcar.getId());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Detects the button pressed.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == btprodEdit.getId()){
            EditProduct();
        }

        if(v.getId() == btprodDelete.getId()){
            DeleteProduct();
        }

        if(v.getId() == btAñadirFav.getId()){
            AddFavProduct();
        }
    }

    /**
     * Method to edit the product.
     */
    //Edit the product
    public void EditProduct(){
        String userID = signInstance.getUid();

        //Database reference
        bbddProfile = FirebaseDatabase.getInstance().getReference("products");
        //Query to edit the product
        Query qEdit = bbddProfile.orderByChild("titulo_prod").equalTo(TitleProduct);

        qEdit.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String tit_prod = etProdTitle.getText().toString();
                String pric_prod = Integer.toString(npProdPrice.getValue());
                String loc_rec = etProdLoc.getText().toString();
                String descr_prod = etProdDescr.getText().toString();
                String cat_prod = "";

                int cat_prod_int = rbProdCat.getCheckedRadioButtonId();

                if (cat_prod_int == rbprodtech.getId()){
                    cat_prod = "Technology";
                }else if(cat_prod_int == rbprodcar.getId()){
                    cat_prod = "Car";
                }else{
                    cat_prod = "Home";
                }
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    //Get the key
                    String clave = dataSnapshot1.getKey();

                    bbddProfile.child(clave).child("titulo_prod").setValue(tit_prod);
                    bbddProfile.child(clave).child("precio_prod").setValue(pric_prod);
                    bbddProfile.child(clave).child("descr_prod").setValue(descr_prod);
                    bbddProfile.child(clave).child("cat_prod").setValue(cat_prod);
                    bbddProfile.child(clave).child("lugar_rec").setValue(loc_rec);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method to delete the product.
     */
    //Delete
    public void DeleteProduct(){
        //Database reference
        bbddProfile = FirebaseDatabase.getInstance().getReference("products");
        Query qEdit = bbddProfile.orderByChild("titulo_prod").equalTo(TitleProduct);

        qEdit.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    //Get the key
                    String clave = dataSnapshot1.getKey();
                    DatabaseReference refProd = bbddProfile.child(clave);

                    refProd.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method to delete the product.
     */
    //Delete
    public void AddFavProduct(){
        //Database reference
        bbddProfile = FirebaseDatabase.getInstance().getReference("products");
        Query qEdit = bbddProfile.orderByChild("titulo_prod").equalTo(TitleProduct);

        qEdit.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    //Get the key
                    String clave = dataSnapshot1.getKey();
                    bbddProfile.child(clave).child("favorito").setValue("Favorito");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
