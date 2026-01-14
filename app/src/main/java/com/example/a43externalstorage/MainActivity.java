package com.example.a43externalstorage;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    //sdcard/Android/data/com.example.a43externalstorage/files/testfile.txt

    EditText editText;
    Button write;
    Button read;
    Button saveToSD;
    private final String FILENAME = "testfile.txt";
    private final String RUTAFINAL = Environment.DIRECTORY_DOCUMENTS;
    private final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        read = findViewById(R.id.button2);
        write = findViewById(R.id.button);
        editText = findViewById(R.id.editTextTextMultiLine);
        saveToSD = findViewById(R.id.button3);

        write.setOnClickListener(v -> esribir());
        read.setOnClickListener(v -> leer());
        saveToSD.setOnClickListener(v -> guardarArchivoEnSD());

    }

    private void leer() {
        if (isExternalStorageReadable()) {
            checkStoragePermission();
            StringBuilder stringBuilder = new StringBuilder();

            try {
                File carpeta = getExternalFilesDir(RUTAFINAL);
                File textFile = new File(carpeta, FILENAME);
                FileInputStream fileInputStream = new FileInputStream(textFile);

                if (fileInputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String newLine;

                    while ((newLine = bufferedReader.readLine()) != null) {
                        stringBuilder.append(newLine).append("\n");
                    }

                    fileInputStream.close();
                }

                editText.setText(stringBuilder.toString());

            } catch (java.io.IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error reading file", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Cannot read External Storage", Toast.LENGTH_LONG).show();
        }

    }

    private void esribir() {
        if (isExternalStorageWritable()) {
            checkStoragePermission();
            try {
                File carpeta = getExternalFilesDir(RUTAFINAL);
                File textFile = new File(carpeta, FILENAME);
                FileOutputStream fileOutputStream = new FileOutputStream(textFile);
                fileOutputStream.write(editText.getText().toString().getBytes());
                fileOutputStream.close();
                Toast.makeText(this, "GUARDADO FICHERO EN sd CARD", Toast.LENGTH_LONG).show();
            } catch (java.io.IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error writing file", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Cannot write to External Storage", Toast.LENGTH_LONG).show();
        }

    }

    public void guardarArchivoEnSD() {
        // 1. Obtener la ruta de la "SD Card" (Almacenamiento externo propio de la app)
        // Esto crea la ruta: /sdcard/Android/data/tu.paquete/files/misFicheros
        File folder = new File(getExternalFilesDir(null), "misFicheros");

        // 2. Verificar si la carpeta existe, si no, crearla
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("Carpeta creada con éxito");
            } else {
                System.out.println("Error al crear la carpeta");
                return;
            }
        }

        // 3. Crear el objeto File para el archivo dentro de esa carpeta
        File file = new File(folder, "prueba.txt");

        //4. Recoger el contenido de la caja de texto
        String contenido = editText.getText().toString();
        // 5. Escribir el contenido en el archivo
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(contenido);

            Toast.makeText(this, "GUARDADO FICHERO EN sd CARD", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error writing file", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            // --- PARA API 33 HASTA API 36+ ---
            // Ya no se usa WRITE_EXTERNAL_STORAGE. Se piden permisos según el tipo de archivo.
            String[] permissions33 = {
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
            };

            if (!hasPermissions(permissions33)) {
                ActivityCompat.requestPermissions(this, permissions33, 101);
            }
        } else {
            // --- PARA VERSIONES ANTIGUAS (API 32 o inferior) ---
            int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
        }
    }

    // Método auxiliar para verificar múltiples permisos
    private boolean hasPermissions(String[] permissions) {
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}