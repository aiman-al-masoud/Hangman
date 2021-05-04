package net;

import hangman.Game;
import hangman.Player;

//This is the server's model of the player

public class RemotePlayer extends Player {
	
	Character letter;
	
	public RemotePlayer() {
		
	}
	

	@Override
	public void update(Game game) {
		
	}

	@Override
	public char chooseLetter(Game game) {
		return letter;
	}
	
	
	//set letter to be passed to game for an attempt
	public void setLetter(Character letter) {
		this.letter = letter;
	}
	
	
	
	

}
