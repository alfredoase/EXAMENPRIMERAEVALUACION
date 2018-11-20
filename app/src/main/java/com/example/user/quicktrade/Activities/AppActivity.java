package com.example.user.quicktrade.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.user.quicktrade.MainActivity;
import com.example.user.quicktrade.R;
import com.example.user.quicktrade.model.Product;
import com.example.user.quicktrade.model.ProductAdaptor;
import com.example.user.quicktrade.model.UserRegistration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.quicktrade.MainActivity.signInstance;

/**
 * In this class there are the logic to show the products with a recyclerview
 */
public class AppActivity extends AppCompatActivity implements View.OnClickListener {

    private Product p;
    private ArrayList<Product> products;
    private RecyclerView listProd;
    private ProductAdaptor adaptor;
    private EditText etSearch;
    private Button btSearch;
    private RadioGroup rgSearch;
    private RadioButton rbHome, rbTech, rbCar, rbUser, rbReset;
    private String cat_prod = "all";
    private String userID_ID;
    private LinearLayout layoutApp;

    private String userNameSearch = "";

    private Button btShare;

    private int REQUEST_CODE_RATEPRODUCT = 10;

    private Button btrateProductActivity;

    /**
     * onCreate(), it's a default method on all the activities, but in this, link the recyclerview
     * to our activity_app layout.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        //Add the recyclerview to the activity
        listProd = findViewById(R.id.rv_appActivity);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.HORIZONTAL);
        listProd.setLayoutManager(lim);

        //Finding the views to work with her
        etSearch = findViewById(R.id.et_search);
        btSearch = findViewById(R.id.bt_search);
        rgSearch = findViewById(R.id.rg_searchby);
        rbHome = findViewById(R.id.rb_home);
        rbTech = findViewById(R.id.rb_tech);
        rbCar = findViewById(R.id.rb_car);
        rbUser = findViewById(R.id.rb_user);
        rbReset = findViewById(R.id.rb_reset);
        btShare = findViewById(R.id.bt_share);
        btrateProductActivity = findViewById(R.id.bt_rateProductActivity);

        layoutApp = findViewById(R.id.ll_et);

        etSearch.setEnabled(false);

        btSearch.setOnClickListener(this);
        rbCar.setOnClickListener(this);
        rbTech.setOnClickListener(this);
        rbHome.setOnClickListener(this);
        rbUser.setOnClickListener(this);
        rbReset.setOnClickListener(this);
        btShare.setOnClickListener(this);
        btrateProductActivity.setOnClickListener(this);

        rbCar.setTextColor((Color.parseColor("#ffffff")));
        rbTech.setTextColor((Color.parseColor("#ffffff")));
        rbHome.setTextColor((Color.parseColor("#ffffff")));
        rbUser.setTextColor((Color.parseColor("#ffffff")));
        rbReset.setTextColor((Color.parseColor("#ffffff")));
        etSearch.setHintTextColor((Color.parseColor("#ffffff")));
        etSearch.setTextColor((Color.parseColor("#ffffff")));

        //Method settings
        Setting(lim);
    }

    /**
     * These method shows the products always the activity appear.
     */
    @Override
    protected void onStart() {
        //Show products method
        ShowProducts();

        super.onStart();
    }

    /**
     * The method that, add the OptionsMenu to the activity.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);

        return true;
    }

    /**
     * This method, have a view as a parameter to detect which item is pressed.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_search:
                String condicion = etSearch.getText().toString();
                ChangeQuery(condicion);
                break;
            case R.id.rb_car:
                condicion = "";

                etSearch.setEnabled(false);
                etSearch.setText("");

                ChangeQuery(condicion);
                break;
            case R.id.rb_home:
                condicion = "";

                etSearch.setEnabled(false);
                etSearch.setText("");

                ChangeQuery(condicion);
                break;
            case R.id.rb_tech:
                condicion = "";

                etSearch.setEnabled(false);
                etSearch.setText("");

                ChangeQuery(condicion);
                break;
            case R.id.rb_user:
                condicion = "";
                etSearch.setEnabled(true);
                ChangeQuery(condicion);
                break;
            case R.id.rb_reset:
                condicion = "";

                etSearch.setEnabled(false);
                etSearch.setText("");

                ChangeQuery(condicion);
                break;

            case R.id.bt_share:
                ShareApp();
                break;

            case R.id.bt_rateProductActivity:
                RateProduct();

            default:
                break;
        }
    }

    /**
     * In this method is detected which item is selected on the OptionsMenu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Depends which option calls open differents methods
        switch (item.getItemId()){
            case R.id.men_preferences:
                OpenSettings();
                return true;
            case R.id.men_profile:
                OpenProfile();
                return true;
            case R.id.men_signout:
                SignOut();
                return true;
            case R.id.men_addprod:
                OpenAddProduct();
                return true;
            default:
                return false;
        }
    }

    /**
     * Detects which key is pressed, and if the key back is pressed, show an alert.
     *
     * @param keyCode
     * @param event
     * @return
     */
    //METODO QUE AL DARLE AL PULSAR ATRAS NOS CONFIRMA SI QUEREMOS SALIR DE LA SESION Y NOS ENVIA A LA ACTIVITY DE LOGIN
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("Salir")
                    .setMessage("Estás seguro que deseas salir?")
                    .setNegativeButton(android.R.string.cancel, null)//sin listener
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            //Salir
                            SignOut();
                        }
                    })
                    .show();

            //If its true nothing happens
            return true;
        }
        //In this case, returns to the listener
        return super.onKeyDown(keyCode, event);
    }

    /**
     * This method open the preferences activity
     */
    public void OpenSettings(){
        Intent intentSettingsActivity = new Intent(this, SettingsActivity.class);

        startActivity(intentSettingsActivity);
        onPause();
    }

    /**
     * This method open the profile activity
     */
    public void OpenProfile(){
        Intent intentProfileActivity = new Intent(this, ProfileActivity.class);

        startActivity(intentProfileActivity);
        onPause();
    }

    /**
     * This method open the addproduct activity
     */
    public void OpenAddProduct(){
        Intent intentAddProductActivity = new Intent(this, AddProductActivity.class);

        startActivity(intentAddProductActivity);
        onPause();
    }

    /**
     * The method that signout the current user
     */
    public void SignOut(){
        Intent intentMainActivity = new Intent(this, MainActivity.class);

        startActivity(intentMainActivity);
        signInstance.signOut();
        finish();
    }


    /**
     * With this method, can change de query to search by filters.
     *
     * @param condicionQuery
     */
    private void ChangeQuery(String condicionQuery){
        int cat_prod_int = rgSearch.getCheckedRadioButtonId();

        if (cat_prod_int == rbTech.getId()){
            cat_prod = "Technology";
            ShowProducts();

            Toast.makeText(this, cat_prod, Toast.LENGTH_SHORT).show();

        }
        if(cat_prod_int == rbCar.getId()){
            cat_prod = "Car";
            ShowProducts();

            Toast.makeText(this, cat_prod, Toast.LENGTH_SHORT).show();

        }
        if(cat_prod_int == rbHome.getId()){
            cat_prod = "Home";
            ShowProducts();

            Toast.makeText(this, cat_prod, Toast.LENGTH_SHORT).show();

        }
        if(cat_prod_int == rbUser.getId()){
            cat_prod = "User";
            ShowProducts();

            Toast.makeText(this, cat_prod, Toast.LENGTH_SHORT).show();

        }
        if(cat_prod_int == rbReset.getId()){
            cat_prod = "all";
            ShowProducts();

            Toast.makeText(this, cat_prod, Toast.LENGTH_SHORT).show();
        }
        ShowProducts();
    }

    /**
     * On this method there are the logic tho show the products on the recyclerview and change the query
     */
    public void ShowProducts() {
        String userID = signInstance.getUid();

        //Database reference
        final DatabaseReference bbddProd = FirebaseDatabase.getInstance().getReference("products");
        final DatabaseReference bbddUser = FirebaseDatabase.getInstance().getReference("users");

        if(cat_prod.equals("all")){
            Query qSearch = bbddProd.orderByChild("products");
            qSearch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    products = new ArrayList<>();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        //Obtenemos la clave de la busqueda de arriba
                        Product userProd = dataSnapshot1.getValue(Product.class);

                        String userID = signInstance.getUid();
                        String userName = userProd.getUserName_prod();
                        String title = userProd.getTitulo_prod();
                        String price = userProd.getPrecio_prod();
                        String description = userProd.getDescr_prod();
                        String loc = userProd.getLugar_rec();
                        String cat = userProd.getCat_prod();
                        String prod_favorito = userProd.getFavorito();
                        String puntuacion = "0";

                        products.add(new Product(userID, userName, title, price, loc, description, cat, puntuacion, prod_favorito));
                    }

                    InitAdapter();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AppActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                }
            });
        }else if(cat_prod.equals("Technology")){
            Query qSearch = bbddProd.orderByChild("cat_prod").equalTo(cat_prod);
            qSearch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    products = new ArrayList<>();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        //Obtenemos la clave de la busqueda de arriba
                        Product userProd = dataSnapshot1.getValue(Product.class);

                        String userID = signInstance.getUid();
                        String userName = userProd.getUserName_prod();
                        String title = userProd.getTitulo_prod();
                        String price = userProd.getPrecio_prod();
                        String description = userProd.getDescr_prod();
                        String loc = userProd.getLugar_rec();
                        String cat = userProd.getCat_prod();
                        String prod_favorito = userProd.getFavorito();
                        String puntuacion = "0";

                        products.add(new Product(userID, userName, title, price, loc, description, cat, puntuacion, prod_favorito));
                    }

                    InitAdapter();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AppActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                }
            });
        }else if(cat_prod.equals("Car")){
            Query qSearch = bbddProd.orderByChild("cat_prod").equalTo(cat_prod);
            qSearch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    products = new ArrayList<>();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        //Obtenemos la clave de la busqueda de arriba
                        Product userProd = dataSnapshot1.getValue(Product.class);

                        String userID = signInstance.getUid();
                        String userName = userProd.getUserName_prod();
                        String title = userProd.getTitulo_prod();
                        String price = userProd.getPrecio_prod();
                        String description = userProd.getDescr_prod();
                        String loc = userProd.getLugar_rec();
                        String cat = userProd.getCat_prod();
                        String prod_favorito = userProd.getFavorito();
                        String puntuacion = "0";

                        products.add(new Product(userID, userName, title, price, loc, description, cat, puntuacion, prod_favorito));
                    }

                    InitAdapter();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AppActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                }
            });
        }else if(cat_prod.equals("Home")){
            Query qSearch = bbddProd.orderByChild("cat_prod").equalTo(cat_prod);
            qSearch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    products = new ArrayList<>();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        //Obtenemos la clave de la busqueda de arriba
                        Product userProd = dataSnapshot1.getValue(Product.class);

                        String userID = signInstance.getUid();
                        String userName = userProd.getUserName_prod();
                        String title = userProd.getTitulo_prod();
                        String price = userProd.getPrecio_prod();
                        String description = userProd.getDescr_prod();
                        String loc = userProd.getLugar_rec();
                        String cat = userProd.getCat_prod();
                        String prod_favorito = userProd.getFavorito();
                        String puntuacion = "0";

                        products.add(new Product(userID, userName, title, price, loc, description, cat, puntuacion, prod_favorito));
                    }

                    InitAdapter();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AppActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                }
            });
        }else if(cat_prod.equals("User")){
            Query qFindUserName = bbddUser.orderByChild(getString(R.string.userId)).equalTo(userID);
            qFindUserName.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        UserRegistration user = dataSnapshot1.getValue(UserRegistration.class);
                        userNameSearch = user.getUser_name();

                        Query qSearch = bbddProd.orderByChild("userName_prod").equalTo(etSearch.getText().toString());
                        qSearch.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                products = new ArrayList<>();

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    //Obtenemos la clave de la busqueda de arriba
                                    Product userProd = dataSnapshot1.getValue(Product.class);

                                    String userID = signInstance.getUid();
                                    String userName = userProd.getUserName_prod();
                                    String title = userProd.getTitulo_prod();
                                    String price = userProd.getPrecio_prod();
                                    String description = userProd.getDescr_prod();
                                    String loc = userProd.getLugar_rec();
                                    String cat = userProd.getCat_prod();
                                    String prod_favorito = userProd.getFavorito();
                                    String puntuacion = "0";

                                    products.add(new Product(userID, userName, title, price, loc, description, cat, puntuacion, prod_favorito));
                                }

                                InitAdapter();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(AppActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

        }
    }

    /**
     * Initialize the adaptor to show the products.
     */
    private void InitAdapter(){
        adaptor = new ProductAdaptor(products);
        listProd.setAdapter(adaptor);
    }

    private void Setting(LinearLayoutManager lim){
        //Get the infor about the preferences
        SharedPreferences datosPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String colorBack = datosPreferences.getString("set_back", "White");
        Boolean orienLim = datosPreferences.getBoolean("set_rows", true);

        if (colorBack.equals("White")){
            layoutApp.setBackgroundColor((Color.parseColor("#ffffff")));
        }else if (colorBack.equals("Black")){
            layoutApp.setBackgroundColor((Color.parseColor("#000000")));
        }

        if(orienLim == true){
            lim.setOrientation(LinearLayoutManager.VERTICAL);
        }else if(orienLim == false){
            lim.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
    }


    //EJERCICIO 2
    //
    //
    //
    //
    //
    //
    /**
     * This method allow share the app.
     */
    private void ShareApp() {

        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.shareText);

        if (shareIntent.resolveActivity(AppActivity.this.getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "" + R.string.titleShare));
        } else {
            Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
        }
    }

    //EJERCICIO 3
    //
    //
    //
    /**
     * Method that open the activity to rate the product.
     */
    private void RateProduct(){
        Intent rateProduct = new Intent(this, RateProductActivity.class);

        startActivityForResult(rateProduct, REQUEST_CODE_RATEPRODUCT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_RATEPRODUCT && resultCode == RESULT_OK){
            final String puntuationProduct = data.getStringExtra("puntuation");
            String titleProduct = data.getStringExtra("title");

            String prod_favorito = "";
            Toast.makeText(this, "PUNTUACION: "+puntuationProduct + " TITULO: "+ titleProduct, Toast.LENGTH_LONG).show();

            products.add(new Product("","","","","","","",puntuationProduct,prod_favorito));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
