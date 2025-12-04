package cl.sarai.iot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseActivity extends AppCompatActivity {

    private EditText editNombre, editCriatura, editLugar;
    private Button btnEnviarFB, btnVerFirebase;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_firebase);

        // Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("ghibli_formulario");

        // UI
        editNombre = findViewById(R.id.editNombre);
        editCriatura = findViewById(R.id.editCriatura);
        editLugar = findViewById(R.id.editLugar);

        btnEnviarFB = findViewById(R.id.btnEnviarFB);
        btnVerFirebase = findViewById(R.id.btnVerFirebase);

        // Enviar
        btnEnviarFB.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String criatura = editCriatura.getText().toString().trim();
            String lugar = editLugar.getText().toString().trim();

            if (nombre.isEmpty() || criatura.isEmpty() || lugar.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos, viajero", Toast.LENGTH_SHORT).show();
                return;
            }

            GhibliData data = new GhibliData(nombre, criatura, lugar);

            String key = reference.push().getKey();
            reference.child(key).setValue(data);

            editNombre.setText("");
            editCriatura.setText("");
            editLugar.setText("");

            Toast.makeText(this, "El bosque guarda tu mensaje", Toast.LENGTH_SHORT).show();
        });

        // VerActivity
        btnVerFirebase.setOnClickListener(v -> {
            Intent intent = new Intent(FirebaseActivity.this, VerActivity.class);
            startActivity(intent);
        });
    }

    public static class GhibliData {
        public String nombre;
        public String criatura;
        public String lugar;

        public GhibliData() { }

        public GhibliData(String nombre, String criatura, String lugar) {
            this.nombre = nombre;
            this.criatura = criatura;
            this.lugar = lugar;
        }
    }
}
