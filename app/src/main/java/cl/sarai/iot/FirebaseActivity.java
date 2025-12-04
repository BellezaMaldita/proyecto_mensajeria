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

        // 1. conexion a firebase con la conexion previamente definida para el proyecto
        firebaseDatabase = FirebaseDatabase.getInstance();

        // 2. se genera una referencia para luego buscar la info en firebase, en este caso ghibli_formulario sera la raiz
        reference = firebaseDatabase.getReference("ghibli_formulario");

        // 3. aca obtenemos todos los datos necesarios ingresados por el usuario en la pantalla de formulario
        editNombre = findViewById(R.id.editNombre);
        editCriatura = findViewById(R.id.editCriatura);
        editLugar = findViewById(R.id.editLugar);

        btnEnviarFB = findViewById(R.id.btnEnviarFB);
        btnVerFirebase = findViewById(R.id.btnVerFirebase);

        // 4. definimos la accion del boton enviar
        btnEnviarFB.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String criatura = editCriatura.getText().toString().trim();
            String lugar = editLugar.getText().toString().trim();

            // 5. validamos que no falten registros
            if (nombre.isEmpty() || criatura.isEmpty() || lugar.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos, viajero", Toast.LENGTH_SHORT).show();
                return;
            }

            // 6. cargamos nuestro objeto con la info, clase GhibliData
            GhibliData data = new GhibliData(nombre, criatura, lugar);

            String key = reference.push().getKey();
            // 7. aca se sube la info a firebase con key de referencia en nuestra raiz previamente definida ghibli_formulario
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
