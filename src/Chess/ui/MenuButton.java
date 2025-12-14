package Chess.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The {@code MenuButton} class is a special button used on the app’s menu
 * screens
 * It has two styles (primary and secondary) and automatically changes its look
 * to match the current theme
 *
 * @author Group 3
 */

public class MenuButton extends JButton {

    private Color currentBg;
    private Color normalBg;
    private Color hoverBg;
    private Color textColor;
    private final Variant variant;
    private final ThemeManager theme;

    public enum Variant {
        PRIMARY, SECONDARY
    }

    /**
     * Constructs a new {@code MenuButton} with a label, icon, and visual variant
     * 
     * @param text     the button text to display
     * @param iconName the name of the icon file (without extension)
     * @param variant  the button's visual style variant (primary or secondary)
     */
    public MenuButton(String text, String iconName, Variant variant) {
        super(text, ResourceManager.icon(iconName, 24));

        this.variant = variant;
        this.theme = ThemeManager.getInstance();

        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusable(false);

        setHorizontalAlignment(SwingConstants.LEFT);
        setHorizontalTextPosition(SwingConstants.RIGHT);
        setVerticalTextPosition(SwingConstants.CENTER);

        setIconTextGap(14);
        setBorder(new EmptyBorder(0, 24, 0, 24));
        setPreferredSize(new Dimension(10, 56));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Initialize colors from theme
        updateColors();

        // Listen for theme changes
        theme.addListener(this::updateColors);

        // Hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                currentBg = hoverBg;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                currentBg = normalBg;
                repaint();
            }
        });
    }

    /**
     * Updates colors based on current theme and variant
     */
    private void updateColors() {
        if (variant == Variant.PRIMARY) {
            normalBg = theme.getPrimaryButton();
            textColor = theme.getPrimaryButtonText();
            hoverBg = theme.getPrimaryButtonHover();
        } else {
            normalBg = theme.getSecondaryButton();
            textColor = theme.getSecondaryButtonText();
            hoverBg = theme.getSecondaryButtonHover();
        }

        currentBg = normalBg;
        setForeground(textColor);
        setFont(theme.getBodyFont());
        repaint();
    }

    /**
     * Paints the button with rounded corners and background color according to
     * its current state: normal or hover
     *
     * @param g the {@link Graphics} context used for painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded rectangle background
        g2.setColor(currentBg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), theme.getButtonRadius(), theme.getButtonRadius());

        g2.dispose();
        super.paintComponent(g);
    }
}