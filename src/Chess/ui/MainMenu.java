package Chess.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * The class represents the main entry screen of the Chess Game Management System
 * It displays the title, subtitle, and a vertical stack of menu buttons that allow the user to
 * start a new game, load an existing one, access the tutorial, open settings, or view help information
 *
 * @author Group 3
 */

/**
 * Main menu screen with gradient background and centered card.
 * Fully themed and responsive.
 */

public class MainMenu extends JPanel {

    private final ThemeManager theme;
    private JPanel card;
    private JLabel title;
    private JLabel subtitle;
    private JLabel versionLabel;

    private MenuButton startButton;
    private MenuButton loadButton;
    private MenuButton tutorialButton;
    private MenuButton settingsButton;
    private MenuButton helpButton;

    private JLabel trophy;

    public interface Callbacks {
        void onNewGame();

        void onLoadGame();

        void onTutorial();

        void onSettings();

        void onHelp();
    }

    /**
     * Constructs a new {@code MainMenu} instance
     *
     * @param callbacks a {@link Callbacks} implementation to handle button actions
     */

    public MainMenu(Callbacks callbacks) {
        this.theme = ThemeManager.getInstance();

        setLayout(new GridBagLayout());
        setOpaque(false); // Let gradient host show through
        // Content wrapper (max width responsive)
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(new EmptyBorder(32, 24, 24, 24));
        wrapper.setMaximumSize(new Dimension(880, Integer.MAX_VALUE));

        // Header
        // JLabel trophy = new JLabel(ResourceManager.icon("trophy", 96));
        String iconName = theme.isDarkMode() ? "trophy" : "Blacktrophy";
        // ====JLabel trophy = new JLabel(ResourceManager.icon(iconName, 96));
        trophy = new JLabel(ResourceManager.icon(iconName, 96));

        trophy.setAlignmentX(Component.CENTER_ALIGNMENT);

        title = new JLabel("Chess Game Management System");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(theme.getTitleFont());
        title.setForeground(theme.getPrimaryText());
        title.setBorder(new EmptyBorder(16, 0, 6, 0));

        subtitle = new JLabel("Experience the timeless game of strategy");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(theme.getBodyFont());
        subtitle.setForeground(theme.getSecondaryText());

        // Spacing
        wrapper.add(trophy);
        wrapper.add(title);
        wrapper.add(subtitle);
        wrapper.add(Box.createVerticalStrut(24));

        // Card container with rounded border + shadow
        card = new JPanel() {

            /**
             * Paints the main menu card with a shadow, rounded border, and themed
             * background
             * 
             * @param g the graphics context used for painting
             */

            @Override
            protected void paintComponent(Graphics g) { 
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Drop shadow
                g2.setColor(new Color(0, 0, 0, 110));
                g2.fillRoundRect(6, 8, getWidth() - 12, getHeight() - 12,
                        theme.getBorderRadius() + 8, theme.getBorderRadius() + 8);

                // Main card
                g2.setColor(theme.getCardBackground()); // Mini background where buttons are
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                        theme.getBorderRadius(), theme.getBorderRadius());

                // Border
                g2.setColor(theme.getBorder()); // Mini Boarder where buttons are
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
                        theme.getBorderRadius(), theme.getBorderRadius());

                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Vertical button stack inside card
        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
        stack.setMaximumSize(new Dimension(860, Integer.MAX_VALUE));
        /**
         * stack.add(makeButton("Start New Game", "blackplay",
         * MenuButton.Variant.PRIMARY, callbacks::onNewGame));
         * stack.add(Box.createVerticalStrut(12));
         */
        String startIcon = theme.isDarkMode() ? "play" : "blackplay";
        startButton = makeButton("Start New Game", startIcon, MenuButton.Variant.PRIMARY, callbacks::onNewGame);
        stack.add(startButton);
        stack.add(Box.createVerticalStrut(12));

        // LOAD GAME
        String loadIcon = theme.isDarkMode() ? "upload" : "blackupload";
        loadButton = makeButton("Load Game", loadIcon, MenuButton.Variant.SECONDARY, callbacks::onLoadGame);
        stack.add(loadButton);
        stack.add(Box.createVerticalStrut(12));

        // TUTORIAL
        String tutorialIcon = theme.isDarkMode() ? "whiteBook" : "blackbook";
        tutorialButton = makeButton("Tutorial Mode", tutorialIcon, MenuButton.Variant.SECONDARY, callbacks::onTutorial);
        stack.add(tutorialButton);
        stack.add(Box.createVerticalStrut(12));

        // SETTINGS
        String settingsIcon = theme.isDarkMode() ? "setting" : "blacksetting";
        settingsButton = makeButton("Settings", settingsIcon, MenuButton.Variant.SECONDARY, callbacks::onSettings);
        stack.add(settingsButton);
        stack.add(Box.createVerticalStrut(12));

        // HELP / ABOUT
        String helpIcon = theme.isDarkMode() ? "question" : "blackquestion";
        helpButton = makeButton("Help / About", helpIcon, MenuButton.Variant.SECONDARY, callbacks::onHelp);
        stack.add(helpButton);

        /**
         * stack.add(makeButton("Load Game", "upload", MenuButton.Variant.SECONDARY,
         * callbacks::onLoadGame));
         * stack.add(Box.createVerticalStrut(12));
         * stack.add(makeButton("Tutorial Mode", "book", MenuButton.Variant.SECONDARY,
         * callbacks::onTutorial));
         * stack.add(Box.createVerticalStrut(12));
         * stack.add(makeButton("Settings", "setting", MenuButton.Variant.SECONDARY,
         * callbacks::onSettings));
         * stack.add(Box.createVerticalStrut(12));
         * stack.add(makeButton("Help / About", "question",
         * MenuButton.Variant.SECONDARY, callbacks::onHelp));
         */
        card.add(stack);
        wrapper.add(card);

        // Footer version text
        versionLabel = new JLabel("v1.0.0 Desktop Edition");
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        versionLabel.setFont(theme.getSmallFont());
        versionLabel.setForeground(theme.getSecondaryText());
        versionLabel.setBorder(new EmptyBorder(18, 0, 0, 0));
        wrapper.add(versionLabel);

        // Add wrapper centered
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        add(wrapper, gc);

        // Listen for theme changes
        // ======== theme.addListener(this::updateTheme);
        theme.addListener(() -> {
            updateTheme();

            // Update START NEW GAME icon
            String newStartIcon = theme.isDarkMode() ? "play" : "blackplay";
            startButton.setIcon(ResourceManager.icon(newStartIcon, 24));

            // Update trophy icon
            String newTrophyIcon = theme.isDarkMode() ? "trophy" : "Blacktrophy";
            trophy.setIcon(ResourceManager.icon(newTrophyIcon, 96));

            // LOAD GAME
            String newLoadIcon = theme.isDarkMode() ? "upload" : "backUploadd";
            loadButton.setIcon(ResourceManager.icon(newLoadIcon, 24));

            // TUTORIAL
            String newTutorialIcon = theme.isDarkMode() ? "whiteBook" : "blackbook";
            tutorialButton.setIcon(ResourceManager.icon(newTutorialIcon, 24));

            // SETTINGS
            String newSettingsIcon = theme.isDarkMode() ? "setting" : "blackSetting";
            settingsButton.setIcon(ResourceManager.icon(newSettingsIcon, 24));

            // HELP / ABOUT
            String newHelpIcon = theme.isDarkMode() ? "question" : "question_black";
            helpButton.setIcon(ResourceManager.icon(newHelpIcon, 24));

        });

    }

    private void updateTheme() {
        title.setForeground(theme.getPrimaryText());
        title.setFont(theme.getTitleFont());

        subtitle.setForeground(theme.getSecondaryText());
        subtitle.setFont(theme.getBodyFont());

        versionLabel.setForeground(theme.getSecondaryText());
        versionLabel.setFont(theme.getSmallFont());

        card.repaint();
        revalidate();
        repaint();
    }

    /**
     * Creates and styles a menu button with a given label, icon, and click action
     *
     * @param text     the text displayed on the button
     * @param iconName the icon name
     * @param variant  defining the button style
     * @param onClick  the action to execute when clicked
     * @return the configured button component
     */

    /**
     * private JComponent makeButton(String text, String iconName,
     * MenuButton.Variant variant, Runnable onClick) {
     * MenuButton btn = new MenuButton(text, iconName, variant);
     * btn.addActionListener(e -> onClick.run());
     * btn.setAlignmentX(Component.CENTER_ALIGNMENT);
     * btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
     * return btn;
     * }
     */

    private MenuButton makeButton(String text, String iconName, MenuButton.Variant variant, Runnable onClick) {
        MenuButton btn = new MenuButton(text, iconName, variant);
        btn.addActionListener(e -> onClick.run());
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        return btn;
    }

    /**
     * Panel with gradient background.
     * Gradient colors adapt to theme (dark/light).
     */
    /**
     * A {@code JPanel} subclass that renders a themed gradient background
     * adapting to light or dark mode
     */

    public static class GradientHost extends JPanel {
        private final ThemeManager theme;

        public GradientHost() {
            this.theme = ThemeManager.getInstance();
            setLayout(new BorderLayout());

            // Listen for theme changes to update gradient
            theme.addListener(this::repaint);
        }

        /**
         * Paints the adaptive gradient background according to the current theme mode
         *
         * @param g the graphics context used for painting
         */

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int w = getWidth();
            int h = getHeight();

            // Gradient adapts to theme
            Color gradientStart, gradientEnd;
            if (theme.isDarkMode()) { // dark mode background
                gradientStart = new Color(0, 0, 0);
                gradientEnd = new Color(67, 67, 67);
            } else { // light mode background
                gradientStart = new Color(127, 127, 213);
                gradientEnd = new Color(145, 234, 228);
            }

            GradientPaint gp = new GradientPaint(0, 0, gradientStart, w, h, gradientEnd);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
            g2.dispose();
        }
    }

    // Standalone launcher for testing
    /**
     * Entry point for standalone testing of the {@code MainMenu} interface
     *
     * @param args command-line arguments
     */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Chess");
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            GradientHost host = new GradientHost();
            f.setContentPane(host);

            MainMenu menu = new MainMenu(new Callbacks() {
                public void onNewGame() {
                    System.out.println("New Game");
                }

                public void onLoadGame() {
                    System.out.println("Load Game");
                }

                public void onTutorial() {
                    System.out.println("Tutorial");
                }

                public void onSettings() {
                    System.out.println("Settings");
                }

                public void onHelp() {
                    System.out.println("Help");
                }
            });

            host.add(menu, BorderLayout.CENTER);
            f.setSize(1024, 700);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}