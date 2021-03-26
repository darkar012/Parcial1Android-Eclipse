package com.example.parcial1clientand;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpConnection extends Thread {
    private static TcpConnection unicainstancia;

    public static TcpConnection getInstance() {
        if (unicainstancia == null) {
            unicainstancia = new TcpConnection();
            unicainstancia.start();
        }
        return unicainstancia;
    }

    private TcpConnection() {
    }

    private Socket socket;
    private BufferedWriter writer;
    private OnMessageListener observer;

    public void setObserver(OnMessageListener observer) {
        this.observer = observer;
    }

    public void run() {
        try {
            socket = new Socket("10.0.2.2", 5000);

            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            writer = new BufferedWriter(osw);


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void enviarMensaje(String msg) {
        Gson gson = new Gson();

        String action = msg;

        //Serializacion
        String Str = gson.toJson(action);
        new Thread(
                () -> {
                    try {
                        writer.write(Str + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }
}

