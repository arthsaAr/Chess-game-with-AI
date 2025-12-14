package Chess.ui;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;

/**
 * The ResourceManager class provides utility methods for loading and managing
 * UI resources such as icons, chess piece images, and fonts used across the
 * application.
 * 
 * @author Group 3
 */

public final class ResourceManager {
    private static final String ICON_BASE_PATH = "src/assets/Icons/";
    private static final String PIECES_BASE_PATH = "src/pieces/";

    private ResourceManager() {
    }

    /**
     * Loads and scales an icon image from the assets folder
     *
     * @param name the name of the icon file (no ".png" extension)
     * @param size the desired size in pixels for both width and height
     * @return a scaled {@link ImageIcon} of the requested size; if not found,
     *         returns a transparent placeholder
     */

    public static ImageIcon icon(String name, int size) {
        String fullPath = ICON_BASE_PATH + name + ".png";
        File file = new File(fullPath);

        if (!file.exists()) {
            // System.err.println("Icon not found: " + fullPath);
            return new ImageIcon(new BufferedImage(size, size,
                    BufferedImage.TYPE_INT_ARGB));
        }

        ImageIcon base = new ImageIcon(fullPath);
        Image scaled = base.getImage().getScaledInstance(size, size,
                Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // =================================== ENDED OF EDIT

    /**
     * Loads and scales a chess piece image from the pieces folder
     *
     * @param name the name of the piece image file (no".png" extension)
     * @param size the desired size in pixels for both width and height
     * @return a scaled {@link ImageIcon} representing the chess piece; if not
     *         found, returns a transparent placeholder
     */
    public static ImageIcon piece(String name, int size) {
        String fullPath = PIECES_BASE_PATH + name + ".png";
        File file = new File(fullPath);

        if (!file.exists()) {

            return new ImageIcon(new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB));
        }

        ImageIcon base = new ImageIcon(fullPath);
        Image scaled = base.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * Returns a UI font with the specified size using a sans-serif typeface
     * 
     * @param size the font size in points
     * @return a {@link Font} object with the given size and plain style
     */
    public static Font uiFont(float size) {
        return new Font(Font.SANS_SERIF, Font.PLAIN, Math.round(size));
    }
}