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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<ProductInfo> historyProducts;
    private LayoutInflater inflater;

    public HistoryAdapter(Context context, List<ProductInfo> historyProducts) {
        this.inflater = LayoutInflater.from(context);
        this.historyProducts = historyProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductInfo historyProduct = historyProducts.get(position);

        // Construir la cadena con toda la información
        StringBuilder productInfoText = new StringBuilder();
        productInfoText.append("Nombre del producto: ").append(historyProduct.getProductName()).append("\n");
        productInfoText.append("Marca: ").append(historyProduct.getBrand()).append("\n");
        productInfoText.append("Categorías: ").append(historyProduct.getCategories()).append("\n");
        productInfoText.append("Ingredientes: ").append(historyProduct.getIngredients()).append("\n");

        holder.productInfoTextView.setText(productInfoText.toString());

        Picasso.get().load(historyProduct.getImageUrl()).into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        return historyProducts.size();
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
