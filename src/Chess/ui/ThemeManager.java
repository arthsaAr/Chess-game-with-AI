package Chess.ui;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * The central class that controls how the Chess application looks
 * @author Group 3
 */

/**
 * Central theme manager for the entire application.
 * Manages colors, fonts, spacing, board styles, and notifies listeners of theme
 * changes.
 */
public class ThemeManager {
    private static ThemeManager instance;
    private Theme currentTheme;
    private String currentBoardStyle = "Wooden";
    private List<ThemeListener> listeners = new ArrayList<>();

    private ThemeManager() {
        currentTheme = new DarkTheme();
    }

    /**
     * @return the global ThemeManager instance
     */
    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    // ========== Theme Management ==========

    /**
     * Sets a new theme (dark or light) and tells all listeners to update
     * 
     * @param theme the new theme to apply
     */
    public void setTheme(Theme theme) {
        this.currentTheme = theme;
        notifyListeners();
    }

    /**
     * Switches between light and dark mode
     * 
     * @param dark {@code true} for dark mode, {@code false} for light mode
     */
    public void setDarkMode(boolean dark) {
        if (dark) {
            setTheme(new DarkTheme());
        } else {
            setTheme(new LightTheme());
        }
    }

    /**
     * Checks if the current theme is dark mode
     *
     * @return {@code true} if dark theme is active
     */

    public boolean isDarkMode() {
        return currentTheme instanceof DarkTheme;

        /**
         * Color bg = getPrimaryBackground();
         * // Simple brightness check
         * int brightness = bg.getRed() + bg.getGreen() + bg.getBlue();
         * return brightness < 400; // Dark backgrounds → lower brightness
         */
    }

    // ========== Board Style Management ==========

    /**
     * Changes how the chessboard squares look (e.g. Wooden, Classic, Blue)
     *
     * @param style the name of the new board style
     */
    public void setBoardStyle(String style) {
        System.out.println("ThemeManager.setBoardStyle called with: " + style);
        this.currentBoardStyle = style;
        System.out.println("Number of listeners: " + listeners.size());
        notifyListeners();
        System.out.println("Listeners notified");
    }

    /**
     * Gets the name of the current board style
     *
     * @return the current board style
     */
    public String getBoardStyle() {
        return currentBoardStyle;
    }

    /**
     * Gets the color for the light squares of the chessboard
     *
     * @return a {@link Color} for the light squares
     */
    public Color getLightSquare() {
        switch (currentBoardStyle) {
            case "Wooden":
                return new Color(240, 217, 181);
            case "Pink":
                return new Color(242, 82, 120);
            case "Modern":
                return new Color(236, 239, 244);
            case "Blue":
                return new Color(222, 235, 247);
            case "Green":
                return new Color(234, 240, 206);

            default:
                return new Color(240, 217, 181);
        }
    }

    /**
     * Gets the color for the dark squares of the chessboard
     *
     * @return a {@link Color} for the dark squares
     */

    public Color getDarkSquare() {
        switch (currentBoardStyle) {
            case "Wooden":
                return new Color(181, 136, 99);
            case "Classic":
                return new Color(118, 150, 86);
            case "Modern":
                return new Color(119, 153, 180);
            case "Blue":
                return new Color(100, 142, 183);
            case "Green":
                return new Color(119, 149, 86);
            case "Pink":
                return new Color(219, 112, 147);
            default:
                return new Color(181, 136, 99);
        }
    }

    /**
     * Gets the color used to show a selected square
     *
     * @return a {@link Color} for the selected square
     */
    public Color getSelectedSquare() {
        switch (currentBoardStyle) {
            case "Wooden":
            case "Classic":
            case "Green":
                return new Color(186, 202, 68);
            case "Modern":
                return new Color(255, 205, 86);
            case "Blue":
                return new Color(97, 165, 194);
            case "Pink":
                return new Color(255, 105, 180);
            default:
                return new Color(186, 202, 68);
        }
    }

    /**
     * Gets the color used to highlight move options
     *
     * @return a {@link Color} for highlighted squares
     */
    public Color getHighlightSquare() {
        switch (currentBoardStyle) {
            case "Wooden":
            case "Classic":
                return new Color(246, 246, 130);
            case "Modern":
                return new Color(255, 235, 156);
            case "Blue":
                return new Color(152, 217, 238);
            case "Green":
                return new Color(218, 233, 153);
            case "Pink":
                return new Color(255, 160, 200);
            default:
                return new Color(246, 246, 130);
        }
    }

    /**
     * @return main background color
     */
    public Color getPrimaryBackground() {
        return currentTheme.primaryBackground;
    }

    /** @return secondary background color */
    public Color getSecondaryBackground() {
        return currentTheme.secondaryBackground;
    }

    /** @return card background color */
    public Color getCardBackground() {
        return currentTheme.cardBackground;
    }

    /** @return main text color */
    public Color getPrimaryText() {
        return currentTheme.primaryText;
    }

    /** @return lighter or secondary text color */
    public Color getSecondaryText() {
        return currentTheme.secondaryText;
    }

    /** @return accent color used for borders or highlights */
    public Color getAccent() {
        return currentTheme.accent;
    }

    /** @return border color */
    public Color getBorder() {
        return currentTheme.border;
    }

    /** @return hover background color */
    public Color getHover() {
        return currentTheme.hover;
    }

    /** @return color of main (primary) buttons */
    public Color getPrimaryButton() {
        return currentTheme.primaryButton;
    }

    /** @return text color for main buttons */
    public Color getPrimaryButtonText() {
        return currentTheme.primaryButtonText;
    }

    /** @return hover color for main buttons */
    public Color getPrimaryButtonHover() {
        return currentTheme.primaryButtonHover;
    }

    /** @return color of secondary buttons */
    public Color getSecondaryButton() {
        return currentTheme.secondaryButton;
    }

    /** @return text color for secondary buttons */
    public Color getSecondaryButtonText() {
        return currentTheme.secondaryButtonText;
    }

    /** @return hover color for secondary buttons */
    public Color getSecondaryButtonHover() {
        return currentTheme.secondaryButtonHover;
    }

    /** @return disabled text color */
    public Color getDisabledText() {
        return currentTheme.disabledText;
    }

    /** @return success color (used for messages or highlights) */
    public Color getSuccess() {
        return currentTheme.success;
    }

    /** @return error color */
    public Color getError() {
        return currentTheme.error;
    }

    /** @return warning color */
    public Color getWarning() {
        return currentTheme.warning;
    }

    // ========== Font Getters ==========

    /** @return font for large titles */
    public Font getTitleFont() {
        return currentTheme.titleFont;
    }

    /** @return font for headings */
    public Font getHeadingFont() {
        return currentTheme.headingFont;
    }

    /** @return font for normal body text */
    public Font getBodyFont() {
        return currentTheme.bodyFont;
    }

    /** @return font for small text */
    public Font getSmallFont() {
        return currentTheme.smallFont;
    }

    // ========== Spacing Getters ==========

    /** @return extra small spacing */
    public int getSpacingXS() {
        return currentTheme.spacingXS;
    }

    /** @return small spacing */
    public int getSpacingS() {
        return currentTheme.spacingS;
    }

    /** @return medium spacing */
    public int getSpacingM() {
        return currentTheme.spacingM;
    }

    /** @return large spacing */
    public int getSpacingL() {
        return currentTheme.spacingL;
    }

    /** @return extra large spacing */
    public int getSpacingXL() {
        return currentTheme.spacingXL;
    }

    /** @return border corner roundness */
    public int getBorderRadius() {
        return currentTheme.borderRadius;
    }

    /** @return button corner roundness */
    public int getButtonRadius() {
        return currentTheme.buttonRadius;
    }

    // ========== Observer Pattern ==========

    /**
     * Adds a listener that gets notified whenever the theme changes
     *
     * @param listener the object to notify when theme updates
     */
    public void addListener(ThemeListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener so it no longer receives updates
     *
     * @param listener the listener to remove
     */
    public void removeListener(ThemeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (ThemeListener listener : listeners) {
            listener.onThemeChanged();
        }
    }

    // ========== Theme Interface ==========

    public interface ThemeListener {
        void onThemeChanged();
    }

    // ========== Theme Base Class ==========

    public abstract static class Theme {
        // Background colors
        Color primaryBackground;
        Color secondaryBackground;
        Color cardBackground;

        // Text colors
        Color primaryText;
        Color secondaryText;
        Color disabledText;

        // UI colors
        Color accent;
        Color border;
        Color hover;

        // Button colors
        Color primaryButton;
        Color primaryButtonText;
        Color primaryButtonHover;
        Color secondaryButton;
        Color secondaryButtonText;
        Color secondaryButtonHover;

        // Status colors
        Color success;
        Color error;
        Color warning;

        // Fonts
        Font titleFont;
        Font headingFont;
        Font bodyFont;
        Font smallFont;

        // Spacing
        int spacingXS = 5;
        int spacingS = 10;
        int spacingM = 20;
        int spacingL = 30;
        int spacingXL = 40;
        int borderRadius = 20;
        int buttonRadius = 40;
    }

    // ========== Dark Theme ==========

    public static class DarkTheme extends Theme {
        public DarkTheme() {

            // Backgrounds
            primaryBackground = new Color(18, 18, 18); // Deep charcoal
            secondaryBackground = new Color(28, 28, 28); // Slightly lighter
            cardBackground = new Color(32, 32, 32);

            // Text
            primaryText = Color.white;
            secondaryText = new Color(200, 200, 200);
            secondaryText = new Color(180, 180, 180);
            disabledText = new Color(110, 110, 110);

            // UI
            accent = new Color(52, 120, 246); // Soft blue
            border = new Color(60, 60, 60);
            hover = new Color(45, 45, 45);

            // Buttons
            primaryButton = new Color(52, 120, 246); // Blue button
            primaryButtonText = Color.WHITE;
            primaryButtonHover = new Color(44, 105, 215);

            secondaryButton = new Color(45, 45, 45);
            secondaryButtonText = new Color(230, 230, 230);
            secondaryButtonHover = new Color(60, 60, 60);

            // Status
            success = new Color(67, 160, 71);
            error = new Color(229, 57, 53);
            warning = new Color(251, 140, 0);

            // Fonts
            titleFont = new Font("SansSerif", Font.PLAIN, 28);
            headingFont = new Font("SansSerif", Font.PLAIN, 20);
            bodyFont = new Font("SansSerif", Font.PLAIN, 16);
            smallFont = new Font("SansSerif", Font.PLAIN, 14);
        }
    }

    

   public static class LightTheme extends Theme {
       public LightTheme() {


           // Backgrounds
           primaryBackground = new Color(134, 168, 231); // Soft white 250, 250, 250
           secondaryBackground = new Color(245, 245, 245);
           cardBackground = new Color(107, 134, 185); // soft, visible grey, changed full background

            // Buttons
            primaryButton = new Color(0, 191, 255); // Soft blue button
            primaryButtonText = Color.BLACK;
            primaryButtonHover = new Color(44, 105, 215);

            secondaryButton = new Color(117, 216, 255); // button home page
            secondaryButtonText = new Color(30, 30, 30);
            secondaryButtonHover = new Color(225, 225, 225);


           // UI
           accent = new Color(52, 120, 246); // Same accent as dark mode
           border = new Color(230, 230, 230); // 230, 230, 230
           hover = new Color(235, 235, 235);


           // Buttons
           primaryButton = new Color(52, 120, 246); // Soft blue button
           primaryButtonText = Color.WHITE;
           primaryButtonHover = new Color(44, 105, 215);


           secondaryButton = new Color(255, 255, 255); // button home page
           secondaryButtonText = new Color(30, 30, 30); //main page button text
           secondaryButtonHover = new Color(225, 225, 225);


           // Status
           success = new Color(67, 160, 71);
           error = new Color(229, 57, 53);
           warning = new Color(251, 140, 0);


           // Fonts
           titleFont = new Font("SansSerif", Font.PLAIN, 28);
           headingFont = new Font("SansSerif", Font.PLAIN, 20);
           bodyFont = new Font("SansSerif", Font.PLAIN, 16);
           smallFont = new Font("SansSerif", Font.PLAIN, 14);
       }
   }


}
