package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;


public class TcpConnection extends Thread{
	
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
	
	String recordatorio;
	private OnMessageListener OML;
	
	 public void setObserver(OnMessageListener observer){
	        this.OML = observer;
	    }
	 
	public void run () {
		
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(5000);
			System.out.println("Esperando cliente...");
			Socket socket = server.accept();
			System.out.println("Cliente esta conectado");

			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			// Hacer que el objeto is tenga la capacidad de leer Strings completos
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader breader = new BufferedReader(isr);

			while (true) {
				// Esperando mensaje
				System.out.println("Esperando mensaje...");
				String mensajeRecibido = breader.readLine(); //BW::X::Y::ALTO::ANCHO
				System.out.println(mensajeRecibido);

				Gson gson = new Gson();

				//Deserializacion
				recordatorio = gson.fromJson(mensajeRecibido, String.class);
				OML.OnMessage(recordatorio);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}
}
