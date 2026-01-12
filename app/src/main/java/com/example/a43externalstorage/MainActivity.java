package com.example.a43externalstorage;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button buttonW, buttonR;

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

        editText = findViewById(R.id.editTextTextMultiLine);
        buttonR = findViewById(R.id.button);
        buttonW = findViewById(R.id.button2);

        buttonW.setOnClickListener(v -> write());
        buttonR.setOnClickListener(v -> read());
    }

    private void write() {
        // Implementation for writing to external storage
    }

    private void read() {
        // Implementation for reading from external storage
    }

    //Hace falta en api36
    public boolean isExternalStorageWritable() {
       String state = Environment.getExternalStorageState();
       return Environment.MEDIA_MOUNTED.equals(state);
    }

    //Hace falta en api36
    public boolean isExternalStorageReadable() {
         String state = Environment.getExternalStorageState();
         return Environment.MEDIA_MOUNTED.equals(state) ||
                  Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}