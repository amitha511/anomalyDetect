package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import test.AnomalyDetectionHandler.SocketIO;

public class Server {

	public interface ClientHandler{
		void handleClient(InputStream inFromClient, OutputStream outToClient);

	}

	volatile boolean stop;
	public Server() {
		stop=false;
	}
	
	
	private void startServer(int port, ClientHandler ch) {
		//ch =new AnomalyDetectionHandler();
		try {
			ServerSocket server = new ServerSocket(port);
			server.setSoTimeout(1000);
			while(!stop) {
				try {
					Socket aClient = server.accept();
					ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
					aClient.close();
				} catch (SocketTimeoutException e) {}
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
			// implement here the server...

	}
	
	// runs the server in its own thread
	public void start(int port, ClientHandler ch) {
		new Thread(()->startServer(port,ch)).start();
	}
	
	public void stop() {
		stop=true;
	}
}
