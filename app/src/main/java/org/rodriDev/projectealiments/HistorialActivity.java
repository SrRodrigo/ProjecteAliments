package org.rodriDev.projectealiments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private List<ProductInfo> historyProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Obtener la lista de productos del historial desde SharedPreferences
        historyProducts = loadProductsFromSharedPreferences();

        // Configurar el RecyclerView
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryAdapter(this, historyProducts);
        historyRecyclerView.setAdapter(historyAdapter);
    }

    // Método para cargar la lista de productos desde SharedPreferences
    private List<ProductInfo> loadProductsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyProducts", Context.MODE_PRIVATE);
        String productListJson = sharedPreferences.getString("productList", "[]");
        Type productListType = new TypeToken<List<ProductInfo>>(){}.getType();
        return new Gson().fromJson(productListJson, productListType);
    }
}
