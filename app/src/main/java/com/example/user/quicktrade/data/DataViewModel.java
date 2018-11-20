/*
package com.example.user.quicktrade.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.user.quicktrade.Activities.AppActivity;
import com.example.user.quicktrade.R;
import com.example.user.quicktrade.model.Product;
import com.example.user.quicktrade.model.ProductAdaptor;
import com.example.user.quicktrade.model.UserRegistration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.quicktrade.MainActivity.signInstance;

public class DataViewModel extends ViewModel{
    private MutableLiveData<List<Product>> mDatabase = null;
    private Product p;
    private ArrayList<Product> products;
    private RecyclerView listProd;
    private ProductAdaptor adaptor;

    public MutableLiveData<List<Product>> getData(){
        if(mDatabase == null){
            mDatabase = new MutableLiveData<List<Product>>();
            //Here, I will launch the AsyncTask
            AsyncDataLoader task =  new AsyncDataLoader();
            //task.execute(SERVER_HTTP_REQUEST_ADDRESS);
        }
        return mDatabase;
    }

    public class AsyncDataLoader extends AsyncTask<String, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(String... strings) {
            String userID = signInstance.getUid();

            //Database reference
            final DatabaseReference bbddProd = FirebaseDatabase.getInstance().getReference("products");
            final DatabaseReference bbddUser = FirebaseDatabase.getInstance().getReference("users");

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

                        products.add(new Product(userID, userName, title, price, loc, description, cat));
                    }

                    InitAdapter();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DataViewModel.this, "ERROR", Toast.LENGTH_LONG).show();
                }
            });
        }

        return products2;
    }

        @Override
        protected void onPostExecute(List<Product> productsDatabaseEntries) {
            super.onPostExecute(productsDatabaseEntries);
            mDatabase.setValue(productsDatabaseEntries);
        }
    }

    private void InitAdapter(){
        adaptor = new ProductAdaptor(products);
        listProd.setAdapter(adaptor);
    }
}*/
