package edu.kingsu.SoftwareEngineering.Chess.innerclasses;

import edu.kingsu.SoftwareEngineering.Chess.GameManager;
import edu.kingsu.SoftwareEngineering.Chess.ChessGame;

import java.util.ArrayList;
import java.util.List;

import edu.kingsu.SoftwareEngineering.Chess.AIPlayer;
import edu.kingsu.SoftwareEngineering.Chess.Board;
import edu.kingsu.SoftwareEngineering.Chess.GameScreen;
import edu.kingsu.SoftwareEngineering.Chess.HumanPlayer;
import edu.kingsu.SoftwareEngineering.Chess.Player;

public class HelperGameManager extends GameManager {
    
    public HelperGameManager() {
        super(); // Skip UI initialization
    }
    
    @Override
    public void startGame(int difficultyLevel, String gameMode, boolean playerIsWhite) {
        // Create game without UI components
        ChessGame game = new ChessGame(null);
        Board board = new Board(null); // Pass null for GameScreen
        game.setBoard(board);
        
        // Initialize players based on game mode
        List<Player> players = new ArrayList<>();
        
        if (gameMode.equals("Human vs Human")) {
            players.add(new HumanPlayer("White", gameMode));
            players.add(new HumanPlayer("Black", gameMode));
        } else if (gameMode.equals("Human vs AI")) {
            if (playerIsWhite) {
                players.add(new HumanPlayer("White", gameMode));
                players.add(new AIPlayer("Black", gameMode, difficultyLevel));
            } else {
                players.add(new AIPlayer("White", gameMode, difficultyLevel));
                players.add(new HumanPlayer("Black", gameMode));
            }
        } else if (gameMode.equals("AI vs AI")) {
            players.add(new AIPlayer("White", gameMode, difficultyLevel));
            players.add(new AIPlayer("Black", gameMode, difficultyLevel));
        }
        // Initialize the board
        board.initializeBoard();
        
        // Set the current game
        setCurrentGame(game);
    }
    
    // Override other UI-dependent methods as needed
}
