package org.rodriDev.projectealiments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<ProductInfo> productInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistorialActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar la lista de información de productos
        productInfos = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productInfos);
        recyclerView.setAdapter(productAdapter);

        // Configurar el botón de escaneo
        Button scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia el lector de códigos de barras ZXing
                startScanner();
            }
        });
    }

    // Método para iniciar el escaneo de códigos de barras
    private void startScanner() {
        // Configura el lector de códigos de barras ZXing
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Escanea un código de barras");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    // Método para procesar el resultado del escaneo de códigos de barras
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Procesa el resultado del escaneo de códigos de barras
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String barcode = result.getContents();
            if (barcode != null && !barcode.isEmpty()) {
                // Realiza la llamada a la API en segundo plano
                new FetchProductInfoTask().execute(barcode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Clase interna para realizar la llamada a la API en segundo plano
    private class FetchProductInfoTask extends AsyncTask<String, Void, ProductInfo> {
        @Override
        protected ProductInfo doInBackground(String... barcodes) {
            String barcode = barcodes[0];
            String deviceLanguage = Locale.getDefault().getLanguage();
            String apiUrl = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json?lang=" + deviceLanguage;

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return parseProductInfo(result.toString());
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProductInfo productInfo) {
            if (productInfo != null) {
                // Actualizar la lista de productos y notificar al adaptador
                productInfos.add(productInfo);
                productAdapter.notifyDataSetChanged();
            } else {
                // Si hay un error al obtener información del producto
                productInfos.add(new ProductInfo("Error al obtener información del producto", "", "", "", "", "", "", "", ""));
                productAdapter.notifyDataSetChanged();
            }
        }
        private void saveProductToSharedPreferences(ProductInfo productInfo) {
            // Obtener la instancia de SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("MyProducts", Context.MODE_PRIVATE);

            // Obtener la lista existente de productos desde SharedPreferences
            String productListJson = sharedPreferences.getString("productList", "[]");
            Type productListType = new TypeToken<List<ProductInfo>>(){}.getType();
            List<ProductInfo> productList = new Gson().fromJson(productListJson, productListType);

            // Añadir el nuevo producto a la lista
            productList.add(productInfo);

            // Convertir la lista a formato JSON y guardarla en SharedPreferences
            String updatedProductListJson = new Gson().toJson(productList);
            sharedPreferences.edit().putString("productList", updatedProductListJson).apply();
        }

        private ProductInfo parseProductInfo(String json) {
            try {
                JSONObject jsonResult = new JSONObject(json);
                JSONObject product = jsonResult.optJSONObject("product");

                if (product != null) {
                    // Obtener los campos relevantes
                    String productName = product.optString("product_name", "Nombre del producto no disponible");
                    String brand = product.optString("brands", "Marca no disponible");
                    String categories = product.optString("categories", "Categorías no disponibles");
                    String ingredients = product.optString("ingredients_text", "Ingredientes no disponibles");
                    String allergens = product.optString("allergens", "Alérgenos no disponibles");
                    String countries = product.optString("countries", "Países no disponibles");
                    String additives = product.optString("additives_tags", "Aditivos no disponibles");
                    String labels = product.optString("labels_tags", "Etiquetas no disponibles");
                    String imageUrl = product.optString("image_front_url", "Imagen no disponible");

                    return new ProductInfo(productName, brand, categories, ingredients, allergens, countries, additives, labels, imageUrl);
                } else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
