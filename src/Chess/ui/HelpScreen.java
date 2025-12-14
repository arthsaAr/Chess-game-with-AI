package Chess.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Chess.GameManager;

/**
 * Help and About screen showing rules, controls, notation, and credits.
 * Provides a multi-tab interface that displays help and informational content
 * for the Chess Game Management System. This screen reacts to theme changes
 * and provides a back button to return to the main menu.
 * 
 * @author Group 3
 */
public class HelpScreen extends JPanel {
    private final GameManager manager;
    private final ThemeManager theme;
    private JPanel contentCard;
    private String currentTab = "Rules";
    private JButton rulesBtn, controlsBtn, notationBtn, aboutBtn, backBtn;

    /**
     * Constructs the Help and About screen.
     *
     * @param manager reference to the {@link GameManager} controlling screen
     *                transitions
     */
    public HelpScreen(GameManager manager) {
        this.manager = manager;
        this.theme = ThemeManager.getInstance();

        setLayout(new BorderLayout());
        setBackground(theme.getPrimaryBackground());

        // Listen for theme changes
        theme.addListener(this::updateTheme);

        // === Top Bar with Back Button ===
        add(createTopBar(), BorderLayout.NORTH);

        // === Main Content Area ===
        add(createMainContent(), BorderLayout.CENTER);
    }

    // ===================================HERE
    /**
     * Creates the top navigation bar containing the back button and title.
     *
     * @return the constructed top panel
     */
    private JPanel createTopBar() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(20, 40, 10, 40));

        // Back button
        //JPanel backPanel = new JPanel(new BorderLayout());
        //backPanel.setOpaque(false);
        JPanel backPanel = UIComponents.createTopBar("Back to Menu", () -> manager.showScreen("menu"));
        backPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
       
        // Wrap boarder layout topBar to FlowLayout
        JPanel wrapPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapPanel.setOpaque(false);
        wrapPanel.add(backPanel);
        wrapPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        topPanel.add(backPanel);
        topPanel.add(Box.createVerticalStrut(15));

        // Title
        JLabel title = UIComponents.createTitle("Help & About");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(title);

        return topPanel;
    } 

    // ===================================HERE END

    /**
     * Creates the central content region with tab navigation and the main card
     * area.
     *
     * @return the constructed main content panel
     */
    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(10, 40, 40, 40));

        // Tab buttons
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabPanel.setOpaque(false);
        tabPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        rulesBtn = createTabButton("Rules", ResourceManager.icon("book", 18), true);
        controlsBtn = createTabButton("Controls", ResourceManager.icon("gamepad", 18), false);
        notationBtn = createTabButton("Notation", ResourceManager.icon("edit", 18), false);
        aboutBtn = createTabButton("About", ResourceManager.icon("info", 18), false);

        tabPanel.add(rulesBtn);
        tabPanel.add(controlsBtn);
        tabPanel.add(notationBtn);
        tabPanel.add(aboutBtn);

        mainPanel.add(tabPanel, BorderLayout.NORTH);

        // Content card
        contentCard = createContentCard();
        mainPanel.add(contentCard, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Creates a single tab button that switches the displayed content.
     *
     * @param text     the tab title
     * @param icon     the tab icon
     * @param selected whether this tab is initially selected
     * @return a themed {@link JButton} representing the tab
     */
    private JButton createTabButton(String text, Icon icon, boolean selected) {
        JButton btn = new JButton(text, icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (currentTab.equals(text)) {
                    g2.setColor(theme.getCardBackground());
                } else {
                    g2.setColor(theme.getSecondaryBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setForeground(theme.getPrimaryText());
        btn.setFont(theme.getBodyFont());
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setIconTextGap(8);

        btn.addActionListener(e -> switchTab(text));

        return btn;
    }

    /**
     * Switches between tabs and refreshes the content area.
     *
     * @param tab the name of the selected tab
     */
    private void switchTab(String tab) {
        currentTab = tab;
        rulesBtn.repaint();
        controlsBtn.repaint();
        notationBtn.repaint();
        aboutBtn.repaint();
        updateContentCard();
    }

    /**
     * Creates the initial content card container used for displaying help content.
     *
     * @return a styled content card panel
     */
    private JPanel createContentCard() {
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
        card.setLayout(new BorderLayout());
        card.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 2, true),
                new EmptyBorder(30, 40, 30, 40)));

        // Add scrollable content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        addRulesContent(contentPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    /**
     * Updates the content card based on the currently selected tab.
     */
    private void updateContentCard() {
        // Remove old content
        contentCard.removeAll();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        switch (currentTab) {
            case "Rules":
                addRulesContent(contentPanel);
                break;
            case "Controls":
                addControlsContent(contentPanel);
                break;
            case "Notation":
                addNotationContent(contentPanel);
                break;
            case "About":
                addAboutContent(contentPanel);
                break;
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // disabling the white scroll
                                                                                         // bar

        // we disabled the white scrollbar, which also disables the scroll feature, so
        // we want to enable the scrollable property without the bar.
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0, 0, 0, 0);
                this.trackColor = new Color(0, 0, 0, 0);
            }
        });
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contentCard.add(scrollPane, BorderLayout.CENTER);
        contentCard.revalidate();
        contentCard.repaint();
    }

    /**
     * Adds the Rules tab content, including movement and special move explanations.
     *
     * @param panel the panel to populate
     */
    private void addRulesContent(JPanel panel) {
        addSectionTitle(panel, "Chess Rules Summary");
        panel.add(Box.createVerticalStrut(20));

        // Objective
        addSubheading(panel, "Objective");
        addBodyText(panel,
                "The goal is to checkmate your opponent's king. This means the king is under attack (in check) and cannot escape.");
        panel.add(Box.createVerticalStrut(15));

        // How Pieces Move
        addSubheading(panel, "How Pieces Move");
        addBullet(panel, "Pawn:", "Moves forward one square (two on first move), captures diagonally");
        addBullet(panel, "Rook:", "Moves horizontally or vertically any number of squares");
        addBullet(panel, "Knight:", "Moves in an \"L\" shape, can jump over pieces");
        addBullet(panel, "Bishop:", "Moves diagonally any number of squares");
        addBullet(panel, "Queen:", "Combines rook and bishop movement");
        addBullet(panel, "King:", "Moves one square in any direction");
        panel.add(Box.createVerticalStrut(15));

        // Special Moves
        addSubheading(panel, "Special Moves");
        addBullet(panel, "Castling:", "King and rook move simultaneously (conditions apply)");
        addBullet(panel, "En Passant:", "Special pawn capture move");
        addBullet(panel, "Promotion:", "Pawn reaching the opposite end becomes any piece (usually queen)");
        panel.add(Box.createVerticalStrut(15));

        // Game Ending
        addSubheading(panel, "Game Ending");
        addBullet(panel, "Checkmate:", "King cannot escape check - you win!");
        addBullet(panel, "Stalemate:", "No legal moves available but not in check - draw");
        addBullet(panel, "Draw:", "Can occur by agreement, repetition, or insufficient material");
    }

    /**
     * Adds the Controls tab content explaining user interaction in the game.
     *
     * @param panel the panel to populate
     */
    private void addControlsContent(JPanel panel) {
        addSectionTitle(panel, "Game Controls");
        panel.add(Box.createVerticalStrut(20));

        addSubheading(panel, "Making Moves");
        addBullet(panel, "Select Piece:", "Click on a piece to select it");
        addBullet(panel, "Move Piece:", "Click on the destination square to move");
        addBullet(panel, "Cancel Selection:", "Click on the selected piece again or select another piece");
        panel.add(Box.createVerticalStrut(15));

        addSubheading(panel, "Game Options");
        addBullet(panel, "New Game:", "Start a fresh game from the main menu");
        addBullet(panel, "Settings:", "Customize board style and theme");
        addBullet(panel, "Tutorial:", "Learn how each piece moves");
    }

    /**
     * Adds the Notation tab content describing algebraic chess notation.
     *
     * @param panel the panel to populate
     */
    private void addNotationContent(JPanel panel) {
        addSectionTitle(panel, "Chess Notation");
        panel.add(Box.createVerticalStrut(20));

        addSubheading(panel, "Algebraic Notation");
        addBodyText(panel, "Chess uses algebraic notation to record moves. Each square has a unique coordinate:");
        panel.add(Box.createVerticalStrut(10));
        addBullet(panel, "Files:", "Columns labeled a-h (left to right)");
        addBullet(panel, "Ranks:", "Rows labeled 1-8 (bottom to top for White)");
        panel.add(Box.createVerticalStrut(15));

        addSubheading(panel, "Piece Symbols");
        addBullet(panel, "K", "King");
        addBullet(panel, "Q", "Queen");
        addBullet(panel, "R", "Rook");
        addBullet(panel, "B", "Bishop");
        addBullet(panel, "N", "Knight");
        addBullet(panel, "", "Pawn (no symbol)");
        panel.add(Box.createVerticalStrut(15));

        addSubheading(panel, "Special Symbols");
        addBullet(panel, "x", "Captures");
        addBullet(panel, "+", "Check");
        addBullet(panel, "#", "Checkmate");
        addBullet(panel, "O-O", "Castling kingside");
        addBullet(panel, "O-O-O", "Castling queenside");
    }

    /**
     * Adds the About tab content including app version, features, and credits.
     *
     * @param panel the panel to populate
     */
    private void addAboutContent(JPanel panel) {
        addSectionTitle(panel, "About Chess Game");
        panel.add(Box.createVerticalStrut(8));
        addBodyText(panel,
                "A modern chess application developed as a part of CMPT320 Software Engineering course at The King's University");
        addBodyText(panel,
                "This project focuses on clean gameplay, different levels of AI difficulty, and a user friendly interface suitable for beginners, intermediate and professional players.");
        panel.add(Box.createVerticalStrut(15));

        addSubheading(panel, "Version Information");
        panel.add(Box.createVerticalStrut(8));
        addBullet(panel, "", "Current game Version: v1.8"); // we will consider 2.0 as final version
        addBullet(panel, "", "Last Updated: November 18 2025");
        panel.add(Box.createVerticalStrut(15));

        addSubheading(panel, "Key Features");
        panel.add(Box.createVerticalStrut(8));
        addBullet(panel, "", "Full implementation of official chess rules");
        addBullet(panel, "", "10 levels of AI difficulty");
        addBullet(panel, "", "Multiple board themes (light/dark)");
        addBullet(panel, "", "Clean and modern user interface");
        addBullet(panel, "", "Tutorial mode for begginer players");
        addBullet(panel, "", "Undo/redo options to correct any mistake");
        addBullet(panel, "", "Moves highlight making it begginer friendly");
        panel.add(Box.createVerticalStrut(15));

        addSubheading(panel, "Credits");
        panel.add(Box.createVerticalStrut(8));
        addBodyText(panel, "Developed as Team project for CMPT320 - Software Engineering course");
        addBodyText(panel, "The King's University, 2025");
        panel.add(Box.createVerticalStrut(15));

        addSubheading(panel, "Team Members");
        panel.add(Box.createVerticalStrut(10));
        emailIcon(panel, "Renese", "RENESE.EBANKS.STU@kingsu.ca");
        panel.add(Box.createVerticalStrut(8));
        emailIcon(panel, "Raashtra", "RAASHTRA.KC.STU@kingsu.ca");
        panel.add(Box.createVerticalStrut(8));
        emailIcon(panel, "Israel", "ISRAEL.OGUNSUA.STU@kingsu.ca");
        panel.add(Box.createVerticalStrut(8));
        emailIcon(panel, "Javiera", "JAVIERA.PONCEHEREDIA.STU@kingsu.ca");
    }

    // adding name with specific email icon for each team members,
    private void emailIcon(JPanel panel, String name, String email) {
        JLabel nameLabel = new JLabel(name + ": ");

        ImageIcon gmail = new ImageIcon("src/assets/Icons/outLook.png");
        JLabel iconLabel = new JLabel(gmail);

        iconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // clickable element

        // when clicked we want a email/to open so
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    URI mailto = new URI("mailto:" + email + "?subject=Chess%20Information");
                    desktop.mail(mailto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        JPanel fullLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 7));
        fullLine.setOpaque(false);
        fullLine.setAlignmentX(Component.LEFT_ALIGNMENT);

        fullLine.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        fullLine.setPreferredSize(new Dimension(120, 40));
        fullLine.setMaximumSize(new Dimension(120, 40));

        nameLabel.setForeground(Color.WHITE); // making the overalll color of the label name white
        fullLine.add(nameLabel);
        fullLine.add(iconLabel);

        panel.add(fullLine);
    }

    /**
     * Adds a major section title label.
     *
     * @param panel the container panel
     * @param text  the title text
     */
    private void addSectionTitle(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(theme.getPrimaryText());
        label.setFont(theme.getHeadingFont());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
    }

    /**
     * Adds a subsection heading within a help topic.
     *
     * @param panel the container panel
     * @param text  the subheading text
     */
    private void addSubheading(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(theme.getPrimaryText());
        label.setFont(theme.getBodyFont().deriveFont(Font.BOLD));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
    }

    /**
     * Adds a descriptive body text line under a section or subsection.
     *
     * @param panel the container panel
     * @param text  the paragraph text
     */
    private void addBodyText(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(theme.getSecondaryText());
        label.setFont(theme.getBodyFont());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(5, 0, 5, 0));
        panel.add(label);
    }

    /**
     * Adds a bullet-point line.
     *
     * @param panel the container panel
     * @param title the label before the text (can be empty)
     * @param text  the bullet description
     */
    private void addBullet(JPanel panel, String title, String text) {
        JPanel bulletPanel = new JPanel(new BorderLayout(10, 0));
        bulletPanel.setOpaque(false);
        bulletPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bulletPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        bulletPanel.setBorder(new EmptyBorder(3, 0, 3, 0));

        // Create a small bullet circle
        JPanel bulletCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.getPrimaryText());
                g2.fillOval(0, 6, 6, 6);
                g2.dispose();
            }
        };
        bulletCircle.setOpaque(false);
        bulletCircle.setPreferredSize(new Dimension(15, 20));
        bulletPanel.add(bulletCircle, BorderLayout.WEST);

        // Text panel
        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        textPanel.setOpaque(false);

        if (!title.isEmpty()) {
            JLabel titleLabel = new JLabel(title + " ");
            titleLabel.setForeground(theme.getPrimaryText());
            titleLabel.setFont(theme.getBodyFont().deriveFont(Font.BOLD));
            textPanel.add(titleLabel);
        }

        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(theme.getSecondaryText());
        textLabel.setFont(theme.getBodyFont());
        textPanel.add(textLabel);

        bulletPanel.add(textPanel, BorderLayout.CENTER);
        panel.add(bulletPanel);
    }

    /**
     * Updates the theme for all UI components when theme changes occur.
     */
    private void updateTheme() {
        setBackground(theme.getPrimaryBackground());

        // Update all tab buttons
        if (rulesBtn != null) {
            rulesBtn.setForeground(theme.getPrimaryText());
            rulesBtn.setFont(theme.getBodyFont());
            rulesBtn.repaint();
        }
        if (controlsBtn != null) {
            controlsBtn.setForeground(theme.getPrimaryText());
            controlsBtn.setFont(theme.getBodyFont());
            controlsBtn.repaint();
        }
        if (notationBtn != null) {
            notationBtn.setForeground(theme.getPrimaryText());
            notationBtn.setFont(theme.getBodyFont());
            notationBtn.repaint();
        }
        if (aboutBtn != null) {
            aboutBtn.setForeground(theme.getPrimaryText());
            aboutBtn.setFont(theme.getBodyFont());
            aboutBtn.repaint();
        }

        // Update content card
        if (contentCard != null) {
            contentCard.setBorder(new CompoundBorder(
                    new LineBorder(theme.getBorder(), 2, true),
                    new EmptyBorder(30, 40, 30, 40)));
            contentCard.repaint();
        }

        if (backBtn != null) {
        // Set the default, un-hovered color according to the new theme
        backBtn.setForeground(theme.getSecondaryText()); // new
    }

        // Refresh content to apply theme colors
        updateContentCard();

        revalidate();
        repaint();
    }
}