package Chess.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import Chess.AIPlayer;
import Chess.GameManager;
import Chess.HumanPlayer;
import Chess.Player;

/**
 * The NewGameSetup class provides the user interface for configuring a new
 * chess game before it begins.
 * <p>
 * This screen integrates with the {@link GameManager} to
 * start games and navigate between screens.
 * </p>
 *
 * @author Group 3
 */
public class NewGameSetup extends JPanel {

    private final GameManager manager;
    private final ThemeManager theme;

    private JComboBox<String> modeBox;
    private JSlider aiSlider;
    private JRadioButton whiteBtn, blackBtn;
    private JLabel lblAi;
    private JPanel summaryBox;
    private JPanel aiLabelsPanel;
    private JPanel mainCard;
    private JButton backBtn;
    private JButton startBtn;

    private JLabel titleLabel;
    private JLabel lblMode;
    private JLabel lblColor;
    private JLabel summaryTitle;
    private JLabel beginnerLabel;
    private JLabel expertLabel;

    /**
     * Constructs a new game setup screen.
     *
     * @param gm the {@link GameManager} instance controlling navigation and game
     *           start
     */
    public NewGameSetup(GameManager gm) {
        this.manager = gm;
        this.theme = ThemeManager.getInstance();

        setLayout(new BorderLayout());
        setBackground(theme.getPrimaryBackground());

        // === Top Bar with Back Button ===
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(20, 40, 10, 40));

        String backIconName = theme.isDarkMode() ? "left" : "backArrow";
        backBtn = new JButton("  Back to Menu", ResourceManager.icon(backIconName, 18));
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setForeground(theme.getSecondaryText());
        backBtn.setFont(ResourceManager.uiFont(13));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backBtn.setForeground(theme.getPrimaryText());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backBtn.setForeground(theme.getSecondaryText());
            }
        });
        backBtn.addActionListener(e -> manager.showScreen("menu"));

        topBar.add(backBtn, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        // === Scrollable Center Panel ===
        JScrollPane scrollPane = new JScrollPane(createMainCard());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Listen for resize events to adjust padding/border
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustLayoutForSize();
            }
        });

        // Listen for theme changes
        theme.addListener(this::updateTheme);
    }

    /**
     * Builds the main content area containing all setup options.
     *
     * @return the main setup card panel
     */
    private JPanel createMainCard() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 20, 40, 20));

        mainCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.getCardBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        mainCard.setOpaque(true);
        mainCard.setLayout(new GridBagLayout());
        mainCard.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 1, true),
                new EmptyBorder(30, 40, 30, 40)));
        mainCard.setMaximumSize(new Dimension(900, 10000));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 0, 10, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.WEST;

        // === Title ===
        titleLabel = new JLabel("New Game Setup");
        titleLabel.setForeground(theme.getPrimaryText());
        titleLabel.setFont(ResourceManager.uiFont(24));
        c.gridy = 0;
        c.insets = new Insets(0, 0, 20, 0);
        mainCard.add(titleLabel, c);

        // === Game Mode ===
        lblMode = new JLabel("Game Mode");
        lblMode.setForeground(theme.getPrimaryText());
        lblMode.setFont(ResourceManager.uiFont(14));
        c.gridy++;
        c.insets = new Insets(10, 0, 5, 0);
        mainCard.add(lblMode, c);

        modeBox = new JComboBox<>(new String[] {
                "Human vs Human", "Human vs AI", "AI vs AI"
        });
        styleComboBox(modeBox);
        modeBox.setSelectedIndex(1);
        modeBox.addActionListener(e -> updateSummary());
        c.gridy++;
        c.insets = new Insets(5, 0, 15, 0);
        mainCard.add(modeBox, c);

        // === AI Difficulty ===
        lblAi = new JLabel("AI Difficulty: 5 / 10");
        lblAi.setForeground(theme.getPrimaryText());
        lblAi.setFont(ResourceManager.uiFont(14));
        c.gridy++;
        c.insets = new Insets(10, 0, 5, 0);
        mainCard.add(lblAi, c);

        aiSlider = new JSlider(1, 10, 5);
        styleSlider(aiSlider);
        aiSlider.addChangeListener(e -> {
            lblAi.setText("AI Difficulty: " + aiSlider.getValue() + " / 10");
            updateSummary();
        });
        c.gridy++;
        c.insets = new Insets(5, 0, 5, 0);
        mainCard.add(aiSlider, c);

        aiLabelsPanel = new JPanel(new BorderLayout());
        aiLabelsPanel.setOpaque(false);

        beginnerLabel = new JLabel("Beginner");
        beginnerLabel.setForeground(theme.getSecondaryText());
        beginnerLabel.setFont(ResourceManager.uiFont(12));

        expertLabel = new JLabel("Expert");
        expertLabel.setForeground(theme.getSecondaryText());
        expertLabel.setFont(ResourceManager.uiFont(12));

        aiLabelsPanel.add(beginnerLabel, BorderLayout.WEST);
        aiLabelsPanel.add(expertLabel, BorderLayout.EAST);

        c.gridy++;
        c.insets = new Insets(0, 0, 15, 0);
        mainCard.add(aiLabelsPanel, c);

        // === Player Color ===
        lblColor = new JLabel("Player Color");
        lblColor.setForeground(theme.getPrimaryText());
        lblColor.setFont(ResourceManager.uiFont(14));

        c.gridy++;
        c.insets = new Insets(10, 0, 5, 0);
        mainCard.add(lblColor, c);

        whiteBtn = new JRadioButton("White (Moves First)");
        blackBtn = new JRadioButton("Black");
        ButtonGroup colorGroup = new ButtonGroup();
        colorGroup.add(whiteBtn);
        colorGroup.add(blackBtn);
        whiteBtn.setSelected(true);
        styleSquareRadio(whiteBtn);
        styleSquareRadio(blackBtn);
        whiteBtn.addActionListener(e -> updateSummary());
        blackBtn.addActionListener(e -> updateSummary());

        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        colorPanel.setOpaque(false);
        colorPanel.add(whiteBtn);
        colorPanel.add(blackBtn);
        c.gridy++;
        c.insets = new Insets(5, 0, 15, 0);
        mainCard.add(colorPanel, c);

        // === Game Summary ===
        summaryTitle = new JLabel("Game Summary");
        summaryTitle.setForeground(theme.getPrimaryText());
        summaryTitle.setFont(ResourceManager.uiFont(16));

        c.gridy++;
        c.insets = new Insets(10, 0, 10, 0);
        mainCard.add(summaryTitle, c);

        summaryBox = new JPanel();
        summaryBox.setLayout(new BoxLayout(summaryBox, BoxLayout.Y_AXIS));
        summaryBox.setBackground(theme.getCardBackground());
        summaryBox.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 1),
                new EmptyBorder(14, 18, 14, 18)));
        updateSummary();
        c.gridy++;
        c.insets = new Insets(5, 0, 20, 0);
        mainCard.add(summaryBox, c);

        // === Start Game Button ===
        String playIconName = theme.isDarkMode() ? "play" : "blackplay";
        startBtn = new JButton("Start Game", ResourceManager.icon(playIconName, 18)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        stylePrimaryButton(startBtn);
        startBtn.addActionListener(e -> {
            String gameMode = (String) modeBox.getSelectedItem();
            int aiLevel = aiSlider.getValue();
            boolean playerWhite = whiteBtn.isSelected();

            Player whitePlayer;
            Player blackPlayer;

            switch (gameMode) {
                case "Human vs Human":
                    whitePlayer = new HumanPlayer("Human 1", "White");
                    blackPlayer = new HumanPlayer("Human 2", "Black");
                    break;

                case "Human vs AI":
                    if (playerWhite) {
                        whitePlayer = new HumanPlayer("Human", "White");
                        blackPlayer = new AIPlayer("AI Black", "Black", aiLevel);
                    } else {
                        whitePlayer = new AIPlayer("AI White", "White", aiLevel);
                        blackPlayer = new HumanPlayer("Human", "Black");
                    }
                    break;

                case "AI vs AI":
                    whitePlayer = new AIPlayer("AI White", "White", aiLevel);
                    blackPlayer = new AIPlayer("AI Black", "Black", aiLevel);
                    break;

                default:
                    whitePlayer = new HumanPlayer("Human 1", "White");
                    blackPlayer = new HumanPlayer("Human 2", "Black");
                    break;
            }

            // Players are currently not passed into GameManager here, but
            // we preserve your existing signature.
            manager.startGame(aiLevel, gameMode, playerWhite);

        });
        c.gridy++;
        c.insets = new Insets(10, 0, 0, 0);
        mainCard.add(startBtn, c);

        // Add vertical glue to push content to top
        c.gridy++;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        mainCard.add(Box.createVerticalGlue(), c);

        centerPanel.add(mainCard, BorderLayout.CENTER);
        return centerPanel;
    }

    /**
     * Adjusts layout padding and borders based on current window size.
     */
    private void adjustLayoutForSize() {
        int width = getWidth();

        EmptyBorder outerBorder;
        if (width < 600) {
            outerBorder = new EmptyBorder(15, 15, 15, 15);
        } else if (width < 900) {
            outerBorder = new EmptyBorder(20, 25, 20, 25);
        } else {
            outerBorder = new EmptyBorder(30, 40, 30, 40);
        }

        mainCard.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 1, true),
                outerBorder));

        revalidate();
        repaint();
    }

    /**
     * Applies themed styling to combo boxes.
     *
     * @param box the combo box to style
     */
    private void styleComboBox(JComboBox<String> box) {
        Color bg = theme.isDarkMode() ? new Color(28, 28, 28) : Color.WHITE;
        Color borderColor = theme.isDarkMode() ? new Color(60, 60, 60) : new Color(200, 200, 200);

        box.setBackground(bg);
        box.setForeground(theme.getPrimaryText());
        box.setFont(ResourceManager.uiFont(14));
        box.setFocusable(false);
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Color itemBg = theme.isDarkMode()
                        ? (isSelected ? new Color(40, 40, 40) : new Color(18, 18, 18))
                        : (isSelected ? new Color(225, 225, 225) : Color.WHITE);
                lbl.setBackground(itemBg);
                lbl.setForeground(theme.getPrimaryText());
                lbl.setBorder(new EmptyBorder(8, 12, 8, 12));
                return lbl;
            }
        });
        box.setBorder(new CompoundBorder(
                new LineBorder(borderColor, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
    }

    /**
     * Customizes the slider UI for AI difficulty selection with a themed look.
     *
     * @param s the slider to style
     */
    private void styleSlider(JSlider s) {
        s.setOpaque(false);
        s.setUI(new javax.swing.plaf.basic.BasicSliderUI(s) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int y = trackRect.y + trackRect.height / 2 - 2;
                Color trackColor = theme.isDarkMode() ? new Color(60, 60, 60) : new Color(255, 255, 255);
                g2.setColor(trackColor);
                g2.fillRoundRect(trackRect.x, y, trackRect.width, 4, 4, 4);
            }

            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color thumbColor = theme.isDarkMode() ? Color.WHITE : new Color(80, 80, 80);
                g2.setColor(thumbColor);
                g2.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
            }
        });
    }

    /**
     * Styles a radio button with custom square icons for color selection.
     *
     * @param rb the radio button to style
     */
    private void styleSquareRadio(JRadioButton rb) {
        rb.setOpaque(false);
        rb.setForeground(theme.getPrimaryText());
        rb.setFont(ResourceManager.uiFont(14));
        rb.setFocusPainted(false);
        rb.setIcon(new SquareRadioIcon(false));
        rb.setSelectedIcon(new SquareRadioIcon(true));
    }

    private class SquareRadioIcon implements Icon {
        private final boolean selected;
        private final int size = 16;

        /**
         * Creates a square icon representing a selected or unselected state.
         *
         * @param selected whether the icon represents a selected state
         */
        public SquareRadioIcon(boolean selected) {
            this.selected = selected;
        }

        /**
         * Paints the icon graphics for the square radio indicator.
         *
         * @param c the component on which the icon is painted
         * @param g the graphics context
         * @param x the X coordinate
         * @param y the Y coordinate
         */
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color borderColor = theme.isDarkMode() ? new Color(150, 150, 150) : new Color(120, 120, 120);
            g2.setColor(borderColor);
            g2.drawRoundRect(x, y, size - 1, size - 1, 3, 3);

            if (selected) {
                Color fillColor = theme.isDarkMode() ? Color.WHITE : new Color(60, 60, 60);
                g2.setColor(fillColor);
                g2.fillRoundRect(x + 3, y + 3, size - 6, size - 6, 2, 2);
            }

            g2.dispose();
        }

        /**
         * Returns the icon's width.
         *
         * @return the icon width in pixels
         */
        @Override
        public int getIconWidth() {
            return size;
        }

        /**
         * Returns the icon's height.
         *
         * @return the icon height in pixels
         */
        @Override
        public int getIconHeight() {
            return size;
        }
    }

    /**
     * Styles the main "Start Game" button with rounded edges and hover.
     *
     * @param btn the button to style
     */
    private void stylePrimaryButton(JButton btn) {
        Color normalBg = theme.isDarkMode() ? Color.WHITE : new Color(40, 40, 40);
        Color hoverBg = theme.isDarkMode() ? new Color(230, 230, 230) : new Color(25, 25, 25);
        Color textColor = theme.isDarkMode() ? Color.BLACK : Color.WHITE;

        btn.setBackground(normalBg);
        btn.setForeground(textColor);
        btn.setFont(ResourceManager.uiFont(14));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 26, 12, 26));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setIconTextGap(10);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverBg);
                btn.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(normalBg);
                btn.repaint();
            }
        });
    }

    /**
     * Rebuilds the summary box contents based on current selections.
     */
    private void updateSummary() {
        summaryBox.removeAll();
        String mode = (String) modeBox.getSelectedItem();
        int ai = aiSlider.getValue();
        String color = whiteBtn.isSelected() ? "White" : "Black";

        addSummaryLine("Mode: " + (mode.equals("Human vs Human") ? "Player vs Player"
                : mode.equals("Human vs AI") ? "Player vs Computer" : "Computer vs Computer"));
        if (mode.equals("Human vs AI") || mode.equals("AI vs AI")) {
            addSummaryLine("AI Level: " + ai + " / 10");
        }
        if (!mode.equals("AI vs AI")) {
            addSummaryLine("You play as: " + color);
        }

        boolean showAI = mode.equals("Human vs AI") || mode.equals("AI vs AI");
        lblAi.setVisible(showAI);
        aiSlider.setVisible(showAI);
        aiLabelsPanel.setVisible(showAI);

        summaryBox.revalidate();
        summaryBox.repaint();
    }

    /**
     * Adds a single formatted summary line to the game setup summary box.
     *
     * @param text the text to display in the summary line
     */
    private void addSummaryLine(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(theme.getSecondaryText());
        lbl.setFont(ResourceManager.uiFont(14));
        lbl.setBorder(new EmptyBorder(3, 0, 3, 0));
        summaryBox.add(lbl);
    }

    /**
     * Called when the theme changes; updates colors, backgrounds, and icons.
     */
    private void updateTheme() {
        setBackground(theme.getPrimaryBackground());

        // Back button icon + color
        if (backBtn != null) {
            String backIconName = theme.isDarkMode() ? "whiteArrow" : "blackArrow";
            backBtn.setIcon(ResourceManager.icon(backIconName, 18));
            backBtn.setForeground(theme.getSecondaryText());
        }

        // Start button icon
        if (startBtn != null) {
            String playIconName = theme.isDarkMode() ? "play" : "blackplay";
            startBtn.setIcon(ResourceManager.icon(playIconName, 18));
        }

        // 🔹 TITLE
        if (titleLabel != null) {
            titleLabel.setForeground(theme.getPrimaryText());
        }

        // 🔹 GAME MODE
        if (lblMode != null) {
            lblMode.setForeground(theme.getPrimaryText());
        }

        if (lblAi != null) {
            lblAi.setForeground(theme.getPrimaryText());
        }

        // 🔹 PLAYER COLOR
        if (lblColor != null) {
            lblColor.setForeground(theme.getPrimaryText());
        }

        // 🔹 GAME SUMMARY TITLE
        if (summaryTitle != null) {
            summaryTitle.setForeground(theme.getPrimaryText());
        }

        // 🔹 BEGINNER / EXPERT
        if (beginnerLabel != null) {
            beginnerLabel.setForeground(theme.getSecondaryText());
        }
        if (expertLabel != null) {
            expertLabel.setForeground(theme.getSecondaryText());
        }

        // Summary box + card border repaint
        if (summaryBox != null) {
            summaryBox.setBackground(theme.getCardBackground());
            summaryBox.setBorder(new CompoundBorder(
                    new LineBorder(theme.getBorder(), 1),
                    new EmptyBorder(14, 18, 14, 18)));

            // recolor summary lines too
            for (Component c : summaryBox.getComponents()) {
                if (c instanceof JLabel) {
                    c.setForeground(theme.getSecondaryText());
                }
            }
        }

        if (mainCard != null) {
            mainCard.setBorder(new CompoundBorder(
                    new LineBorder(theme.getBorder(), 1, true),
                    ((CompoundBorder) mainCard.getBorder()).getInsideBorder()));
            mainCard.repaint();
        }

        // Re-style components that depend on theme colors
        styleComboBox(modeBox);
        styleSlider(aiSlider);
        styleSquareRadio(whiteBtn);
        styleSquareRadio(blackBtn);
        updateSummary();

        revalidate();
        repaint();
    }

    /**
     * Displays the New Game Setup screen in fullscreen mode.
     *
     * @param gm the {@link GameManager} instance managing the session
     */
    public static void showFullscreen(GameManager gm) {
        JFrame frame = new JFrame("Chess Game Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setContentPane(new NewGameSetup(gm));
        frame.setVisible(true);
    }
}
