package cl.sarai.iot;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttActivity extends AppCompatActivity {

    private EditText editMensaje;
    private Button btnEnviar;
    private TextView textRecibidos;

    private MqttClient mqttClient;

    private final String SERVER_URI = "tcp://broker.hivemq.com:1883";
    private final String TOPIC = "/sailormoon/sarai/mensajes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mqtt);

        editMensaje = findViewById(R.id.editMensajeMQTT);
        btnEnviar = findViewById(R.id.btnEnviarMQTT);
        textRecibidos = findViewById(R.id.textRecibidosMQTT);

        conectar();

        btnEnviar.setOnClickListener(v -> enviar());
    }

    private void conectar() {
        try {
            mqttClient = new MqttClient(SERVER_URI, MqttClient.generateClientId(), new MemoryPersistence());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            mqttClient.connect(options);
            mqttClient.subscribe(TOPIC);

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) { }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    runOnUiThread(() ->
                            textRecibidos.append("Mensaje lunar: " + message.toString() + "\n")
                    );
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) { }
            });

            Toast.makeText(this, "Conectada al Reino Lunar", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Falló en la conexión Lunar", Toast.LENGTH_SHORT).show();
        }
    }

    private void enviar() {
        try {
            String msg = editMensaje.getText().toString().trim();

            if (msg.isEmpty()) {
                Toast.makeText(this, "Escribe un mensaje mágico", Toast.LENGTH_SHORT).show();
                return;
            }

            MqttMessage message = new MqttMessage(msg.getBytes());
            mqttClient.publish(TOPIC, message);

            editMensaje.setText("");
            Toast.makeText(this, "Mensaje enviado al Reino Lunar", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al enviar", Toast.LENGTH_SHORT).show();
        }
    }
}
