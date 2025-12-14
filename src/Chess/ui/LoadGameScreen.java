package Chess.ui;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import Chess.FileHandler;
import Chess.GameManager;
import Chess.ModernDialog;

/**
 * The class provides the user interface for loading
 * previously saved chess games in Portable Game Notation
 *
 * @author Group 3
 */

public class LoadGameScreen extends JPanel {

    private final GameManager manager;
    private final ThemeManager theme;
    private final FileHandler fileHandler;
    private JTextArea pgnTextArea;
    private JLabel uploadLabel;
    private JPanel mainCard;
    private JPanel previewCard;
    private File selectedFile;
    private boolean showingPreview = false;
    private JLabel title;
    private JLabel previewTitle;
    private JPanel gameInfoPanel;
    private JPanel movesPanel;
    private JLabel moveCountLabel;

    /**
     * Constructs a new {@code LoadGameScreen} with full theming and layout
     *
     * @param manager the {@link GameManager} used for navigation and game control
     */
    public LoadGameScreen(GameManager manager) {
        this.manager = manager;
        this.theme = ThemeManager.getInstance();
        this.fileHandler = new FileHandler();

        setLayout(new BorderLayout());
        setBackground(theme.getPrimaryBackground());

        theme.addListener(this::updateTheme);

        add(UIComponents.createTopBar("Back to Menu", () -> manager.showScreen("menu")), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(createMainContent());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustLayoutForSize();
            }
        });
    }

    /**
     * Builds the main scrollable content area including upload and preview cards
     *
     * @return the populated main content panel
     */

    private JPanel createMainContent() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(
                new EmptyBorder(theme.getSpacingM(), theme.getSpacingXL(), theme.getSpacingXL(), theme.getSpacingXL()));

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);

        title = UIComponents.createTitle("Load Game");
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        contentWrapper.add(title, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setOpaque(false);

        mainCard = createUploadCard();
        previewCard = createPreviewCard();

        cardsPanel.add(mainCard);
        cardsPanel.add(Box.createVerticalStrut(theme.getSpacingM()));
        cardsPanel.add(previewCard);

        contentWrapper.add(cardsPanel, BorderLayout.CENTER);

        JPanel bottomButtons = createBottomButtons();
        contentWrapper.add(bottomButtons, BorderLayout.SOUTH);

        centerPanel.add(contentWrapper, BorderLayout.CENTER);
        return centerPanel;
    }

    /**
     * Creates the main upload card
     *
     * @return the upload card panel
     */

    private JPanel createUploadCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.getCardBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), theme.getBorderRadius(), theme.getBorderRadius());
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 2, true),
                new EmptyBorder(40, 50, 40, 50)));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.WEST;

        JLabel uploadTitle = UIComponents.createHeading("Upload PGN File");
        c.gridy = 0;
        c.insets = new Insets(0, 0, theme.getSpacingM(), 0);
        card.add(uploadTitle, c);

        JPanel uploadArea = createUploadArea();
        c.gridy++;
        c.insets = new Insets(0, 0, theme.getSpacingL(), 0);
        card.add(uploadArea, c);

        JLabel orLabel = UIComponents.createBodyText("- or -");
        orLabel.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy++;
        c.insets = new Insets(0, 0, theme.getSpacingL(), 0);
        card.add(orLabel, c);

        JLabel pasteTitle = UIComponents.createHeading("Paste PGN Text");
        c.gridy++;
        c.insets = new Insets(0, 0, theme.getSpacingS(), 0);
        card.add(pasteTitle, c);

        JScrollPane textAreaScroll = createTextArea();
        c.gridy++;
        c.insets = new Insets(0, 0, theme.getSpacingM(), 0);
        card.add(textAreaScroll, c);

        JButton previewBtn = UIComponents.createSecondaryButton("Preview Game", ResourceManager.icon("upload", 18));
        previewBtn.addActionListener(e -> previewGame());
        c.gridy++;
        c.insets = new Insets(0, 0, 0, 0);
        card.add(previewBtn, c);

        return card;
    }

    /**
     * Creates the upload area
     *
     * @return the interactive upload panel
     */

    private JPanel createUploadArea() {
        JPanel uploadPanel = new JPanel() {
            private boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        openFileChooser();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                float[] dash = { 10.0f, 10.0f };
                g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));

                Color borderColor = hovered ? theme.getAccent() : theme.getBorder();
                g2.setColor(borderColor);
                g2.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 12, 12);

                g2.dispose();
            }
        };

        uploadPanel.setLayout(new GridBagLayout());
        uploadPanel.setOpaque(false);
        uploadPanel.setPreferredSize(new Dimension(100, 200));
        uploadPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.insets = new Insets(5, 0, 5, 0);

        JLabel uploadIcon = new JLabel(ResourceManager.icon("book", 48));
        c.gridy = 0;
        uploadPanel.add(uploadIcon, c);

        uploadLabel = UIComponents.createBodyText("Click to upload a PGN file");
        c.gridy++;
        c.insets = new Insets(15, 0, 5, 0);
        uploadPanel.add(uploadLabel, c);

        JLabel subtitle = UIComponents.createSmallText("Portable Game Notation (.pgn)");
        c.gridy++;
        c.insets = new Insets(5, 0, 5, 0);
        uploadPanel.add(subtitle, c);

        return uploadPanel;
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".pgn");
            }

            @Override
            public String getDescription() {
                return "PGN Files (*.pgn)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            uploadLabel.setText("Selected: " + selectedFile.getName());

            try {
                StringBuilder content = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }
                pgnTextArea.setText(content.toString());
            } catch (IOException e) {
                ModernDialog.showWarning(this, "Error reading file: " + e.getMessage(), "File Error");
            }
        }
    }

    /**
     * Creates the text area for manual
     *
     * @return the scrollable text area
     */

    private JScrollPane createTextArea() {
        pgnTextArea = UIComponents.createTextArea("Paste your PGN notation here...");
        pgnTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(pgnTextArea);
        scrollPane.setPreferredSize(new Dimension(100, 150));
        scrollPane.setBorder(new LineBorder(theme.getBorder(), 1, true));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        return scrollPane;
    }

    /**
     * Creates the preview card that displays sample game metadata and moves
     *
     * @return card , the preview panel
     */

    private JPanel createPreviewCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.getCardBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), theme.getBorderRadius(), theme.getBorderRadius());
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 2, true),
                new EmptyBorder(40, 50, 40, 50)));
        card.setVisible(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.WEST;

        previewTitle = UIComponents.createHeading("Game Preview");
        c.gridy = 0;
        c.insets = new Insets(0, 0, theme.getSpacingM(), 0);
        card.add(previewTitle, c);

        JLabel infoTitle = UIComponents.createHeading("Game Information");
        c.gridy++;
        c.insets = new Insets(10, 0, 10, 0);
        card.add(infoTitle, c);

        gameInfoPanel = new JPanel();
        gameInfoPanel.setLayout(new BoxLayout(gameInfoPanel, BoxLayout.Y_AXIS));
        gameInfoPanel.setOpaque(false);

        c.gridy++;
        c.insets = new Insets(0, 0, theme.getSpacingM(), 0);
        card.add(gameInfoPanel, c);

        moveCountLabel = UIComponents.createHeading("Move List");
        c.gridy++;
        c.insets = new Insets(10, 0, 10, 0);
        card.add(moveCountLabel, c);

        movesPanel = new JPanel();
        movesPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 5));
        movesPanel.setOpaque(false);

        JScrollPane movesScroll = new JScrollPane(movesPanel);
        movesScroll.setPreferredSize(new Dimension(100, 120));
        movesScroll.setBorder(new LineBorder(theme.getBorder(), 1, true));
        movesScroll.setOpaque(false);
        movesScroll.getViewport().setOpaque(false);

        c.gridy++;
        c.insets = new Insets(0, 0, theme.getSpacingM(), 0);
        card.add(movesScroll, c);

        JPanel notePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.getSecondaryBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        notePanel.setOpaque(false);
        notePanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        notePanel.setLayout(new BorderLayout());

        JLabel noteLabel = new JLabel(
                "<html><b>Note:</b> Loading a game will start from the initial position and replay all moves. Click 'Load Game' below to import this game.</html>");
        noteLabel.setForeground(theme.getSecondaryText());
        noteLabel.setFont(theme.getSmallFont());
        notePanel.add(noteLabel);

        c.gridy++;
        c.insets = new Insets(10, 0, 0, 0);
        card.add(notePanel, c);

        return card;
    }

    protected void previewGame() {
        String pgnContent = pgnTextArea.getText().trim();

        if (pgnContent.isEmpty()) {
            ModernDialog.showWarning(this, "Please paste PGN content or upload a file", "Empty PGN");
            return;
        }

        try {
            Map<String, String> pgnHeaders = parsePGNHeaders(pgnContent);
            java.util.List<String> moves = extractMoves(pgnContent);

            gameInfoPanel.removeAll();
            addInfoLine(gameInfoPanel, "Event: " + pgnHeaders.getOrDefault("Event", "Unknown"));
            addInfoLine(gameInfoPanel, "Site: " + pgnHeaders.getOrDefault("Site", "Unknown"));
            addInfoLine(gameInfoPanel, "Date: " + pgnHeaders.getOrDefault("Date", "Unknown"));
            addInfoLine(gameInfoPanel, "White: " + pgnHeaders.getOrDefault("White", "White"));
            addInfoLine(gameInfoPanel, "Black: " + pgnHeaders.getOrDefault("Black", "Black"));
            addInfoLine(gameInfoPanel, "Result: " + pgnHeaders.getOrDefault("Result", "*"));

            movesPanel.removeAll();
            moveCountLabel.setText("Move List (" + moves.size() + " moves)");

            for (String move : moves) {
                JLabel moveLabel = new JLabel(move);
                moveLabel.setForeground(theme.getSecondaryText());
                moveLabel.setFont(theme.getSmallFont());
                moveLabel.setBorder(new EmptyBorder(3, 6, 3, 6));
                movesPanel.add(moveLabel);
            }

            showingPreview = true;
            previewCard.setVisible(true);
            revalidate();
            repaint();

        } catch (Exception e) {
            ModernDialog.showWarning(this, "Error parsing PGN: " + e.getMessage(), "Parse Error");
            e.printStackTrace();
        }
    }

    /**
     * @param pgnContent the complete PGN file content as a string
     * @return a map of header keys to their corresponding values; empty map if no
     *         valid headers are found
     */
    private Map<String, String> parsePGNHeaders(String pgnContent) {
        Map<String, String> headers = new HashMap<>();

        String[] lines = pgnContent.split("\n");
        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("[") && line.endsWith("]")) {
                int colonIdx = line.indexOf(" ");
                int quoteStart = line.indexOf("\"");
                int quoteEnd = line.lastIndexOf("\"");

                if (colonIdx > 0 && quoteStart > 0 && quoteEnd > quoteStart) {
                    String key = line.substring(1, colonIdx);
                    String value = line.substring(quoteStart + 1, quoteEnd);
                    headers.put(key, value);
                }
            }
        }

        return headers;
    }

    /**
     * Extracts chess moves from PGN file content
     * <p>
     * Parses the move section of a PGN file and returns a list of moves in
     * algebraic notation.
     * </p>
     *
     * @param pgnContent the complete PGN file content as a string
     * @return a list of chess moves in algebraic notation; empty list if no moves
     *         found
     */
    private java.util.List<String> extractMoves(String pgnContent) {
        java.util.List<String> moves = new ArrayList<>();

        String[] lines = pgnContent.split("\n");
        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("[")) {
                continue;
            }

            String cleanedLine = line.replaceAll("\\d+\\.", "").replaceAll("\\d+\\.\\.\\.\\s*", "").trim();
            cleanedLine = cleanedLine.replaceAll("\\*|1-0|0-1|1/2-1/2", "").trim();

            String[] tokens = cleanedLine.split("\\s+");
            for (String token : tokens) {
                if (!token.isEmpty() && !token.matches("\\d.*")) {
                    moves.add(token);
                }
            }
        }

        return moves;
    }

    /**
     * Adds a formatted line of text to the provided panel
     *
     * @param panel the container panel
     * @param text  the text content to add
     */

    private void addInfoLine(JPanel panel, String text) {
        JLabel label = UIComponents.createBodyText(text);
        label.setBorder(new EmptyBorder(3, 0, 3, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
    }

    /**
     * Creates bottom navigation buttons ("Cancel" and "Load Game")
     *
     * @return the bottom button panel
     */

    private JPanel createBottomButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, theme.getSpacingS(), 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(theme.getSpacingL(), 0, 0, 0));

        JButton cancelBtn = UIComponents.createSecondaryButton("Cancel");
        cancelBtn.addActionListener(e -> manager.showScreen("menu"));

        JButton loadBtn = UIComponents.createPrimaryButton("Load Game");
        loadBtn.addActionListener(e -> loadGame());

        panel.add(cancelBtn);
        panel.add(loadBtn);

        return panel;
    }

   /**
     * Loads the game from PGN content using GameManager's enhanced load functionality.
     * Handles complete state restoration including player types and AI difficulty.
     */
    private void loadGame() {
        String pgnContent = pgnTextArea.getText().trim();

        if (pgnContent.isEmpty()) {
            ModernDialog.showWarning(this, "Please paste PGN content or upload a file", "Empty PGN");
            return;
        }

        try {
            // Create temporary file with PGN content
            File tempFile = File.createTempFile("chess_game", ".pgn");
            try (java.io.PrintWriter writer = new java.io.PrintWriter(tempFile)) {
                writer.print(pgnContent);
            }

            // Use GameManager's loadGame which handles complete state restoration
            boolean success = manager.loadGame(tempFile.getAbsolutePath());
            
            if (success) {
                ModernDialog.showInfo(this, "Game loaded successfully!", "Load Success");
            
            } else {
                ModernDialog.showWarning(this, "Failed to load game from PGN", "Load Error");
            }
            
            // Clean up temporary file
            tempFile.deleteOnExit();

        } catch (Exception e) {
            ModernDialog.showWarning(this, "Error loading game: " + e.getMessage(), "Load Error");
            e.printStackTrace();
        }
    }

    private void adjustLayoutForSize() {
        int width = getWidth();

        EmptyBorder cardPadding;
        if (width < 700) {
            cardPadding = new EmptyBorder(25, 30, 25, 30);
        } else if (width < 1000) {
            cardPadding = new EmptyBorder(35, 40, 35, 40);
        } else {
            cardPadding = new EmptyBorder(40, 50, 40, 50);
        }

        mainCard.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 2, true),
                cardPadding));

        if (previewCard != null) {
            previewCard.setBorder(new CompoundBorder(
                    new LineBorder(theme.getBorder(), 2, true),
                    cardPadding));
        }

        revalidate();
        repaint();
    }

    private void updateTheme() {
        setBackground(theme.getPrimaryBackground());
        mainCard.repaint();
        previewCard.repaint();
        revalidate();
        repaint();
    }
}