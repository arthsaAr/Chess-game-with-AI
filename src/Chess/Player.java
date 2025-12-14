package Chess;

/**
 * Abstract base class for a chess player
 *
 * /**
 * Abstract base class for a chess player.
 *
 * @author Group3
 * @version 1.0
 */

public abstract class Player {
    protected String name;
    protected String color;

    /**
     * Constructs a new player with the specified name and color
     *
     * @param name  the player's display name; must not be null
     * @param color the player's piece color, either "White" or "Black"; must not be
     *              null
     */
    public Player(String name, String color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Creates a move using this player's strategy
     *
     * @param board the current board state; must not be null
     * @return a Move object representing the player's chosen move, or null if moves
     *         are handled externally (e.g., through GUI for human players)
     */
    public abstract Move makeMove(Board board);

    /**
     * Returns the player's name
     *
     * @return the player's display name
     */
    public String getName() { 
        return name; 
    }

    /**
     * Returns the player's color
     *
     * @return the player's piece color ("White" or "Black")
     */
    public String getColor() { 
        return color; 
    }
}
