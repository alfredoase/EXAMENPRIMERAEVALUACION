package com.example.user.quicktrade.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.quicktrade.Activities.ProductActivity;
import com.example.user.quicktrade.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

import java.util.List;

import static com.example.user.quicktrade.MainActivity.signInstance;


/**
 * This method implements the recycler view and controls all the data of products.
 */
public class ProductAdaptor extends RecyclerView.Adapter<ProductAdaptor.ProductViewHolder>{


    public static String TitleProduct = "";

    private List<Product> products;
    public ProductAdaptor (List<Product> products){
        this.products = products;
    }

    @Override
    public ProductAdaptor.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product,parent,false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductAdaptor.ProductViewHolder holder, int position) {
        Product prod = products.get(position);

        holder.tvTitleProd.setText(prod.getTitulo_prod());
        holder.tvDescriptionProd.setText(prod.getDescr_prod());
        holder.tvPriceProd.setText(prod.getPrecio_prod());
        holder.tvCategoryProd.setText(prod.getCat_prod());
        holder.tvUserProd.setText(prod.getUserName_prod());
        holder.tvProdPuntuation.setText(prod.getProd_puntuation());

        if(signInstance.getUid() == prod.getUserID_prod()){
            holder.tvProdFav.setText(prod.getFavorito());
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    /**
     * On this method there are the views and others methods to work with the product data
     */
    public class ProductViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView tvTitleProd;
        private TextView tvDescriptionProd;
        private TextView tvPriceProd;
        private ImageView ivImageProd;
        private TextView tvCategoryProd;
        private TextView tvUserProd;
        private TextView tvProdPuntuation;
        private TextView tvProdFav;

        public ProductViewHolder(View itemView) {
            super(itemView);

            tvTitleProd = itemView.findViewById(R.id.tv_titleprod);
            tvDescriptionProd = itemView.findViewById(R.id.tv_descriptionprod);
            tvPriceProd = itemView.findViewById(R.id.tv_priceprod);
            ivImageProd = itemView.findViewById(R.id.iv_imageprod);
            tvCategoryProd = itemView.findViewById(R.id.tv_catprod);
            tvUserProd = itemView.findViewById(R.id.tv_userprod);
            tvProdPuntuation = itemView.findViewById(R.id.tv_puntuation);
            tvProdFav = itemView.findViewById(R.id.tv_favorito);

            itemView.setOnClickListener(this);
        }

        /**
         * Method that open the selected product
         * @param v
         */
        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            int position = getAdapterPosition();
            Product prod = products.get(position);

            Intent intentProduct = new Intent(v.getContext(), ProductActivity.class);
            context.startActivity(intentProduct);
            TitleProduct = prod.getTitulo_prod();
        }
    }
}
