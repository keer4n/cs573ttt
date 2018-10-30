package ttt;

import java.awt.event.MouseEvent;

import ttt.Game.GameState;
import ttt.Game.Seed;

public class Game {
	
	private final int ROWS = 3;
	private final int COLS = 3;
	
	 public enum GameState {
	      PLAYING, DRAW, CROSS_WON, NOUGHT_WON
	   }
	   private GameState currentState;  // the current game state
	 
	   // Use an enumeration (inner class) to represent the seeds and cell contents
	   public enum Seed {
	      EMPTY, CROSS, NOUGHT
	   }
	   private Seed currentPlayer;  // the current player
	 
	   private Seed[][] board   ; // Game board of ROWS-by-COLS cells
	 
	   public void Game() {
		   board = new Seed[ROWS][COLS]; // allocate array
		   initGame(); // initialize the game board contents and game variables
	   }
	   
	   public int getRows() {
		   return this.ROWS;
	   }
	   
	   public int getCols() {
		   return this.COLS;
	   }
	   public void initGame() {
		      for (int row = 0; row < ROWS; ++row) {
		         for (int col = 0; col < COLS; ++col) {
		            board[row][col] = Seed.EMPTY; // all cells empty
		         }
		      }
		      currentState = GameState.PLAYING; // ready to play
		      currentPlayer = Seed.CROSS;       // cross plays first
		   }
	   
	   /** Update the currentState after the player with "theSeed" has placed on
       (rowSelected, colSelected). */
   public void updateState(Seed theSeed, int rowSelected, int colSelected) {
      if (hasWon(theSeed, rowSelected, colSelected)) {  // check for win
         currentState = (theSeed == Seed.CROSS) ? GameState.CROSS_WON : GameState.NOUGHT_WON;
      } else if (isDraw()) {  // check for draw
         currentState = GameState.DRAW;
      }
      // Otherwise, no change to current state (still GameState.PLAYING).
   }
   
   public void updateGame(int rowSelected, int colSelected) {

		if (currentState == GameState.PLAYING) {
			if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0
					&& colSelected < COLS && board[rowSelected][colSelected] == Seed.EMPTY) {
				board[rowSelected][colSelected] = currentPlayer; // Make a move
				updateState(currentPlayer, rowSelected, colSelected); // update state
				// Switch player
				currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
			}
		} else {       // game over
			initGame(); // restart the game
		}
   }
   
   public boolean isDraw() {
	      for (int row = 0; row < ROWS; ++row) {
	         for (int col = 0; col < COLS; ++col) {
	            if (board[row][col] == Seed.EMPTY) {
	               return false; // an empty cell found, not draw, exit
	            }
	         }
	      }
	      return true;  // no more empty cell, it's a draw
	   }
   
   public boolean hasWon(Seed theSeed, int rowSelected, int colSelected) {
	      return (board[rowSelected][0] == theSeed  // 3-in-the-row
	            && board[rowSelected][1] == theSeed
	            && board[rowSelected][2] == theSeed
	       || board[0][colSelected] == theSeed      // 3-in-the-column
	            && board[1][colSelected] == theSeed
	            && board[2][colSelected] == theSeed
	       || rowSelected == colSelected            // 3-in-the-diagonal
	            && board[0][0] == theSeed
	            && board[1][1] == theSeed
	            && board[2][2] == theSeed
	       || rowSelected + colSelected == 2  // 3-in-the-opposite-diagonal
	            && board[0][2] == theSeed
	            && board[1][1] == theSeed
	            && board[2][0] == theSeed);
	   }
   

   
   
}
