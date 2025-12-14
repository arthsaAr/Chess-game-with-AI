package Chess;

/**
 * Human-controlled player; moves come from user input in the GUI
 * 
 * @author Group3
 * @version 1.0
 */
public class HumanPlayer extends Player {
    /**
     * Constructs a new {@code HumanPlayer} with the specified name and color
     *
     * @param name  the player's display name; must not be null
     * @param color the player's piece color, either "White" or "Black"; must not be
     *              null
     */
    public HumanPlayer(String name, String color) {
        super(name, color);
    }

    /**
     * Returns null as human moves are handled through the GUI
     *
     * @param board the current chess board state (unused for human players)
     * @return always returns null; actual moves come from GUI interaction
     */
    @Override
    public Move makeMove(Board board) {
        return null; // handled through GUI
    }
}
