package org.rodriDev.projectealiments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ProductInfo> productInfos;
    private LayoutInflater inflater;

    public ProductAdapter(Context context, List<ProductInfo> productInfos) {
        this.inflater = LayoutInflater.from(context);
        this.productInfos = productInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductInfo productInfo = productInfos.get(position);

        // Construir la cadena con toda la información
        StringBuilder productInfoText = new StringBuilder();
        productInfoText.append("Nombre del producto: ").append(productInfo.getProductName()).append("\n");
        productInfoText.append("Marca: ").append(productInfo.getBrand()).append("\n");
        productInfoText.append("Categorías: ").append(productInfo.getCategories()).append("\n");
        productInfoText.append("Ingredientes: ").append(productInfo.getIngredients()).append("\n");



        holder.productInfoTextView.setText(productInfoText.toString());

        Picasso.get().load(productInfo.getImageUrl()).into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productInfoTextView;
        ImageView productImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productInfoTextView = itemView.findViewById(R.id.productNameTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
        }
    }
}

