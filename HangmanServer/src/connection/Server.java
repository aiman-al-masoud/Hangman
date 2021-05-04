package connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

//A Server starts listening on a port, creating and starting a new 
//ServerThread for each and every new client that attempts to connect.

public class Server {
	
	//this is the socket that the server will listen for requests on
	ServerSocket listenerSocket;
	
	//the server listens for new requests on this port
	public static int DEFAULT_LISTENER_PORT_NUM = 8881;

	
	//initialize the Server with a port number to listen for requests
	public Server(int listener_socket_port_number) {
		
		try {
			listenerSocket = new ServerSocket(listener_socket_port_number);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//creates a new client socket, and passes it to a new
	//thread that will take care of that client
	public Socket openSocket() {
		try {
			Socket newClientSocket = listenerSocket.accept();
			return newClientSocket;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	//start server: begin listening on the main port that was set
	public void startServer() {
		while(true) {
			//wait for a client to attempt to connect to this server
			Socket clientSocket = openSocket();
			//start a new thread for any new client
			makeServerThread(clientSocket).start();
		}
	}
	
	
	//OVERRIDE ME!
	//create a server thread
	public ServerThread makeServerThread(Socket socket) {
		return new ServerThread(socket);
	}
	
	
	
	
	public static void main(String[] args) {
		Server server = new Server(Server.DEFAULT_LISTENER_PORT_NUM);
		server.startServer();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
