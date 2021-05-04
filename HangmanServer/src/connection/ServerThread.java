package connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

//A ServerThread will use a socket to communicate with a single
//client, once the connection has been established by the Server.

public class ServerThread extends Thread {
	
	//socket that is used to communicate with a client
	Socket clientSocket;
	
	
	public ServerThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	
    //read a message from client
	public String readFromClient() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			closeClientSocket();
		}
			
		return null;
	}
		
		
	//send client a message
	public void writeToClient(String message) {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			writer.write(message);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			closeClientSocket();
		}
	}
	
	//close client socket (to be called in case reading or writing fails
	//and so the client is probably disconnected)
	protected void closeClientSocket() {
		//close the client socket
		try {
			clientSocket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//stop this thread
		this.stop(); 
	}


	
	//run the ServerThread's routine
	@Override
	public void run() {
		while(true) {
			threadRoutine();
		}
	}
	
	
	//OVERRIDE ME! This is the routine to be put in the 
	//thread's infinite loop.
	public void threadRoutine() {
		writeToClient("You're being taken care of by "+this.getName());
	}
	
	
	
	
	

}
