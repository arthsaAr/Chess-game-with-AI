package Chess;
import javax.swing.SwingUtilities;

/**
 * Entry point of the Chess Game Management System.
 *
 * Launches the Swing-based user interface and main menu.
 *
 * @author Group3
 * @version 1.0.0
 */
public class Main {
    
    /**
     * Main entry point for the Chess Game Management System
     *
     * @param args command line arguments (currently unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameManager manager = new GameManager();
            manager.launchUI();
        });
    }
}
