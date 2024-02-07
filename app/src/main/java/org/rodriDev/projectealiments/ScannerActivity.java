package org.rodriDev.projectealiments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        // Inicia el escáner de ZXing
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Procesa el resultado del escaneo
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // El código de barras fue escaneado exitosamente
                Log.d("ScannerActivity", "Código escaneado: " + result.getContents());

                // Aquí puedes manejar el resultado, por ejemplo, actualizar el RecyclerView
            } else {
                // El escaneo fue cancelado por el usuario
                Log.d("ScannerActivity", "Escaneo cancelado");
            }
            finish(); // Cierra la actividad después del escaneo
        }
    }
}
