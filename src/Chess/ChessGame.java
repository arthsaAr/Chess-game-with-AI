package Chess;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single chess game
 * <p>
 * Stores the {@link Board}, participating {@link Player}s, applied
 * {@link Move}s,
 * and delegates legality checks to the {@link RuleEngine}. Handles game flow
 * such as making moves, undoing moves, and determining end conditions
 * </p>
 *
 * <p>
 * <b>Composition:</b> owns a {@code Board} and a {@code RuleEngine}
 * </p>
 *
 * @author Group3
 * @version 1.0.0
 */
public class ChessGame {

    /** Current board state. */
    private Board board;

    /** List of two players (white, black). */
    private List<Player> players;
    private Player currentPlayer;

    /** Rules engine that validates moves and checks end states. */
    private RuleEngine ruleEngine;

    /** Ordered list of moves made in this game. */
    private List<Move> moveHistory;

    private int currPlayerIndex = 0; // 0 for white by default

    /**
     * Constructs a new {@code ChessGame} with the given players and initializes the
     * board
     *
     * @param players two players participating in the game
     */
    public ChessGame(List<Player> players) {
        this.players = players;
        this.currentPlayer = players.get(0);
        this.board = new Board(null);                
        this.ruleEngine = new RuleEngine(board); 
        this.moveHistory = new ArrayList<>();
    }

    /**
     * Returns the current board instance
     *
     * @return current {@link Board}
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the board (used when loading a game from file)
     */

    /**
     * Sets the board to a new state and reinitializes the rule engine
     * 
     * @param newBoard the new {@link Board} to set; must not be null
     */
    public void setBoard(Board newBoard) {
        this.board = newBoard;
        this.ruleEngine = new RuleEngine(newBoard);
    }

    /**
     * Attempts to apply a move on the board
     * Delegates validation to the {@link RuleEngine}
     *
     * @param move move to attempt
     * @return {@code true} if the move was legal and applied; {@code false}
     *         otherwise
     */
    public boolean makeMove(Move move) {
        if (moveHistory == null) {
            moveHistory = new ArrayList<>();
        }
        
        // Add to history
        moveHistory.add(move);
        
        // Execute the move on board
        board.movePiece(
            move.getFrom().getRow(),
            move.getFrom().getCol(),
            move.getTo().getRow(),
            move.getTo().getCol(),
            null, true
        );
        
        // Change turn
        changeTurn();
        
        return true;
    }

    /**
     * Checks whether the game has reached a terminal state
     * 
     * @return {@code true} if checkmate or stalemate has occurred; {@code false}
     *         otherwise
     */
    public boolean checkGameOver() {
        return ruleEngine.isCheckmate(getCurrentPlayer()) ||
                ruleEngine.isStalemate(getCurrentPlayer());
    }

    /**
     * Returns the player whose turn it is to move
     *
     * @return current {@link Player}
     */
    public Player getCurrentPlayer() {
        return players.get(currPlayerIndex);
    }

    /**
     * tracking the turns by switching between regularly
     */
    public void changeTurn() {
        currPlayerIndex = (currPlayerIndex + 1) % players.size();
    }

    /**
     * Removes the last move from history (undo feature)
     */
    public void undoMove() {
        if (!moveHistory.isEmpty())
        {
            // 1. Get last move
            Move lastMove = moveHistory.get(moveHistory.size() - 1);
            // 2. Delegate the board state
            board.undoMove(lastMove); // Uses the undoMove in board.java
            // 3. Remove the move from history list
            moveHistory.remove(moveHistory.size() - 1);
        }

    }
        /**
     * Gets the list of players participating in this game.
     *
     * @return an unmodifiable list containing the white and black {@link Player} objects
     */
    public List<Player> getPlayers() {
        return players;
    }

    // Adds a move to the game history (called after each move)
    /**
     * Adds a move to the game's move history
     * 
     * @param move the {@link Move} to add to the history; must not be null
     */
    public void addMove(Move move) {
        if (moveHistory == null) {
            moveHistory = new ArrayList<>();
        }
        moveHistory.add(move);
        //System.out.println("SUCCESS: Move added to history. Total moves: " + moveHistory.size());
    }

    /**
     * Gets the rule engine for this game
     *
     * @return the {@link RuleEngine} instance
     */
    public RuleEngine getRuleEngine() {
        return ruleEngine;
    }

    /**
     * Returns a list of all moves made so far
     *
     * @return move history list
     */
    public List<Move> getMoveHistory() {
        if (moveHistory == null) {
            moveHistory = new ArrayList<>();
        }
        return moveHistory;
    }
}
