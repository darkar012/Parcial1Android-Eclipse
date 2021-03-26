package com.example.parcial1clientand;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnMessageListener {
    private ImageView green, yellow, red;
    private Button previa, confirmar;
    private String color, posX, posY, msg, vistaOconfirmar;
    private EditText x, y, record;
    private boolean yellowPress, greenPress, completo;
    private TcpConnection tcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tcp = TcpConnection.getInstance();
        tcp.setObserver(this);

        green = findViewById(R.id.greenBtn);
        yellow = findViewById(R.id.yellowBtn);
        red = findViewById(R.id.redBtn);
        x = findViewById(R.id.xET);
        y = findViewById(R.id.yET);
        record = findViewById(R.id.recordatorio);
        previa = findViewById(R.id.vistaBtn);
        confirmar = findViewById(R.id.confirmarBtn);
        yellowPress = true;
        greenPress = true;


        green.setOnClickListener(
                (v) -> {
                    yellowPress = false;
                    greenPress = true;
                    colorBtn();
                }
        );
        yellow.setOnClickListener(
                (v) -> {
                    yellowPress = true;
                    greenPress = false;
                    colorBtn();
                }
        );
        red.setOnClickListener(
                (v) -> {
                    yellowPress = false;
                    greenPress = false;
                    colorBtn();
                }
        );
        confirmar.setOnClickListener(
                v -> {
                    vistaOconfirmar = "1";
                    comprobacion();
                    if (completo) {
                        String json1 = color + "," + posX + "," + posY + "," + msg + "," + vistaOconfirmar;
                        tcp.enviarMensaje(json1);
                        reset();
                    }
                }
        );
        previa.setOnClickListener(
                v -> {
                    vistaOconfirmar = "2";
                    comprobacion();
                    if (completo) {
                        String json1 = color + "," + posX + "," + posY + "," + msg + "," + vistaOconfirmar;
                        tcp.enviarMensaje(json1);
                    }
                }
        );
    }

    private void comprobacion() {
        completo = true;
        msg = record.getText().toString();
        posX = x.getText().toString();
        posY = y.getText().toString();

        if (msg.isEmpty() || msg.equals(null) || posX.isEmpty() || posX.equals(null) || posY.isEmpty() || posY.equals(null)) {
            Toast.makeText(this, "Hay campos vacios", Toast.LENGTH_LONG).show();
            completo = false;
        } else if (yellowPress && greenPress) {
            Toast.makeText(this, "No ha seleccionado un color", Toast.LENGTH_LONG).show();
            completo = false;
        }
    }

    private void colorBtn() {
        if (greenPress) {
            green.setImageResource(R.drawable.group168);
            yellow.setImageResource(R.drawable.ellipse3);
            red.setImageResource(R.drawable.ellipse5);
            color = "verde";
        } else if (yellowPress) {
            green.setImageResource(R.drawable.ellipse1);
            yellow.setImageResource(R.drawable.group169);
            red.setImageResource(R.drawable.ellipse5);
            color = "amarillo";
        } else {
            green.setImageResource(R.drawable.ellipse1);
            yellow.setImageResource(R.drawable.ellipse3);
            red.setImageResource(R.drawable.group170);
            color = "rojo";
        }
    }

    @Override
    public void OnMessage(String msg) {
        runOnUiThread(
                () -> {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
        );
    }

    public void reset() {
        x.setText("");
        y.setText("");
        record.setText("");
        color = "rojo";

        yellow.setImageResource(R.drawable.ellipse3);
        red.setImageResource(R.drawable.ellipse5);
        green.setImageResource(R.drawable.ellipse1);
        yellowPress = true;
        greenPress = true;
    }
}