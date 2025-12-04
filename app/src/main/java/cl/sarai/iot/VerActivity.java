package cl.sarai.iot;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerActivity extends AppCompatActivity {

    private TextView textVerDatos;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver);

        textVerDatos = findViewById(R.id.textVerDatos);

        // Referencia al mismo nodo usado en FirebaseActivity
        reference = FirebaseDatabase.getInstance().getReference("ghibli_formulario");

        cargarDatos();
    }

    private void cargarDatos() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                StringBuilder builder = new StringBuilder();

                for (DataSnapshot hijo : snapshot.getChildren()) {

                    String nombre = hijo.child("nombre").getValue(String.class);
                    String criatura = hijo.child("criatura").getValue(String.class);
                    String lugar = hijo.child("lugar").getValue(String.class);

                    builder.append("Nombre: ").append(nombre).append("\n");
                    builder.append("Criatura favorita: ").append(criatura).append("\n");
                    builder.append("Lugar soñado: ").append(lugar).append("\n");
                    builder.append("----------------------------\n");
                }

                textVerDatos.setText(builder.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                textVerDatos.setText("No se pudieron cargar los datos del bosque");
            }
        });
    }
}
