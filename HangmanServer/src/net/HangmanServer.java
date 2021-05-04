/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.net.Socket;

import connection.Server;
import connection.ServerThread;
import hangman.Dictionary;
import hangman.Game;
import hangman.GameResult;


public class HangmanServer extends Server{

    public HangmanServer(int listener_socket_port_number) {
		super(listener_socket_port_number);
	}
    
    
	
    public static void main(String[] args) {
    	HangmanServer hangmanServer = new HangmanServer(Server.DEFAULT_LISTENER_PORT_NUM);
    	hangmanServer.startServer();
    }


    //make a custom server thread
	@Override
	public ServerThread makeServerThread(Socket socket) {
		return new HangmanServerThread(socket);
	}
    
    
   
	//custom ServerThread
	public class HangmanServerThread extends ServerThread{

		//FOR NOW, one RemotePlayer for each client
		RemotePlayer remotePlayer;
		//...one game for each client
		Game game;
	    
		
		//make a new game for the remotePlayer
		private void makeNewGame() {
			game = new Game(new Dictionary().pickWord());
			game.addObserver(remotePlayer);
			game.notifyObservers();
			writeToClient("benvenuto al gioco dell'impiccato!\n");
			writeToClient(game.getKnownLetters()+"\n");
		}
		
		
		//constructor
		public HangmanServerThread(Socket clientSocket) {
			super(clientSocket);
			remotePlayer = new RemotePlayer();
			makeNewGame();
		}

		
		//make a custom routine to let the client play hangman
		//NB: this routine will be put in an infinite loop
		@Override
		public void threadRoutine() {
			
			//end game if game result switches away from OPEN
			if(game.getResult() != GameResult.OPEN) {
				writeToClient(game.getResult().toString()+"\n");
				
				//if the player lost, send them an annoying message :-)
				if(game.getResult()==GameResult.FAILED) {
					writeToClient("La parola era: "+game.getSecretWord()+"!\n");
					writeToClient("ho vinto ioooo! ho vinto iooooo!");
				}
				
				//ask the player if they want to play again, else terminate the connection
				writeToClient("vuoi giocare ancora? (S)/(N)\n");
				String response = readFromClient();
				
				if(response.toUpperCase().contains("N")) {
					//if the player doesn't want to play again, close connection
					closeClientSocket();
				}else {
					makeNewGame();
				}
				
				
				
			}
			
			
			writeToClient("Scegli una lettera!\n");
			
			//wait for input from the client
			String buf = readFromClient();
			
			
			//use input from client (letter)
			remotePlayer.setLetter(buf.charAt(0));
	        char c = remotePlayer.chooseLetter(game);
	        game.makeAttempt(c);
	        
	        
			//send the known letters to the client
			writeToClient(game.getKnownLetters()+"\n");
		}
		
		
	}
	
	
    
    
    
    
    
    
    
    
    
}
