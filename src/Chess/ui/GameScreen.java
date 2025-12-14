package Chess.ui;
import Chess.*;
import javax.swing.*;
import java.awt.*;
/**
 * The class represents the main gameplay interface where users can play chess matches
 * <p>
 * It embeds a {@link GUIBoard} component that displays the chessboard
 * and manages user interaction with the {@link Board} model through
 * the {@link GameManager}.
 * @author Group 3
 */

/**
 * Screen that displays the main chess gameplay.
 */
public class GameScreen extends JPanel {

    /**
     * Constructs the main game screen and initializes the chessboard
     *
     * @param manager the {@link GameManager} controlling the application flow
     */

    public GameScreen(GameManager manager) {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        GUIBoard boardPanel = new GUIBoard(manager, new Board(null), false);
        add(boardPanel, BorderLayout.CENTER);
    }
}
