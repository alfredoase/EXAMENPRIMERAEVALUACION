package com.example.user.quicktrade.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.quicktrade.R;
import com.google.firebase.database.DatabaseReference;

public class RateProductActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etSetPuntuation;
    private TextView tvTitle;

    private Button btsetPuntuation;

    private DatabaseReference bbdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_product);

        etSetPuntuation = findViewById(R.id.et_puntuation);
        tvTitle = findViewById(R.id.tv_rateproduct_title);
        btsetPuntuation = findViewById(R.id.bt_rateProduct);

        btsetPuntuation.setOnClickListener(this);

        tvTitle.setText("Prod 1 Alf");
        tvTitle.setText("Prod 1 Alf");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_rateProduct:
                SetPuntuation();
                break;
        }
    }

    /**
     * Method that rate the product
     */
    private void SetPuntuation(){
        String stringPuntuation = etSetPuntuation.getText().toString();
        String stringTitle = tvTitle.getText().toString();

        Intent intentPuntuation = getIntent();

        intentPuntuation.putExtra("puntuation", stringPuntuation);
        intentPuntuation.putExtra("title", stringTitle);

        setResult(RESULT_OK, intentPuntuation);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return true;
    }
}
