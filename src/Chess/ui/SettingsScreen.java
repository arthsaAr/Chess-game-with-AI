package Chess.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Chess.Board;
import Chess.GUIBoard;
import Chess.GameManager;
import Chess.ModernDialog;

/**
 * The SettingsScreen class displays the application's settings menu,
 * where users can switch between light and dark mode, change the board style,
 * and preview how the chessboard looks before applying changes
 *
 * @author Group 3
 */
public class SettingsScreen extends JPanel {
    private final GameManager manager;
    private JToggleButton themeToggle;
    private JComboBox<String> boardStyleBox;
    private GUIBoard previewBoard;
    private Board board;
    private ThemeManager theme;

    // Store references to cards that need repainting
    private JPanel appearanceCard;
    private JPanel boardCustomizationCard;
    private JLabel titleLabel;
    private JLabel appearanceTitle;
    private JLabel themeLabel;
    private JLabel themeDesc;
    private JLabel boardCustomTitle;
    private JLabel styleLabel;
    private JLabel previewLabel;
    private JButton backBtn;

    /**
     * Creates a new settings screen where users can customize
     * the board appearance and switch between themes
     *
     * @param manager the main GameManager controlling navigation
     */
    public SettingsScreen(GameManager manager) {

        this.manager = manager;
        this.theme = ThemeManager.getInstance();

        setLayout(new BorderLayout());
        setBackground(theme.getPrimaryBackground());

        theme.addListener(() -> updateTheme()); // theme change

        // Initialize preview board FIRST
        board = new Board(null);
        board.initializeBoard(); // Add pieces to the board for preview

        // Listen for theme changes to update this screen
        theme.addListener(() -> {
            updateTheme();
        });

        // === Top Bar with Back Button ===
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(20, 40, 10, 40));

        // JButton backBtn = new JButton(" Back to Menu",
        // ResourceManager.icon("chevron-left", 18));
        String iconName = theme.isDarkMode() ? "whiteArrow" : "blackArrow";
        System.out.println("ICON LOADED: " + iconName);

        backBtn = new JButton("  Back to Menu", ResourceManager.icon(iconName, 18));

        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        theme.addListener(() -> {
            String updatedName = theme.isDarkMode() ? "whiteArrow" : "blackArrow";
            backBtn.setIcon(ResourceManager.icon(updatedName, 18));
        });

        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setForeground(new Color(90, 90, 90));
        // backBtn.setForeground(new Color(200, 200, 200));
        backBtn.setFont(ResourceManager.uiFont(13));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                //Color toggleBlue = new Color(0, 0, 0); // Hover color: black 
                backBtn.setForeground(theme.getPrimaryText());
            }

            public void mouseExited(MouseEvent e) {
                backBtn.setForeground(theme.getSecondaryText()); // Default color: white
            }
        });
        backBtn.addActionListener(e -> manager.showScreen("menu"));
        topBar.add(backBtn, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        // === Scrollable Center Panel ===
        JScrollPane scrollPane = new JScrollPane(createMainContent());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates the main content panel containing all settings sections
     *
     * @return the main content JPanel
     */
    private JPanel createMainContent() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setOpaque(false);

        // === Title ===
        titleLabel = new JLabel("Settings");
        titleLabel.setForeground(theme.getPrimaryText());
        titleLabel.setFont(ResourceManager.uiFont(28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentWrapper.add(titleLabel);

        // === Appearance Card ===
        contentWrapper.add(createAppearanceCard());
        contentWrapper.add(Box.createVerticalStrut(20));

        // === Board Customization Card ===
        contentWrapper.add(createBoardCustomizationCard());
        contentWrapper.add(Box.createVerticalStrut(30));

        // === Bottom Buttons ===
        contentWrapper.add(createBottomButtons());

        centerPanel.add(contentWrapper, BorderLayout.NORTH);
        return centerPanel;
    }

    /**
     * Creates the appearance settings card that allows users to toggle light/dark
     * mode
     *
     * @return the appearance card JPanel
     */
    private JPanel createAppearanceCard() {
        appearanceCard = new JPanel() {
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
        appearanceCard.setOpaque(false);
        appearanceCard.setLayout(new BorderLayout());
        appearanceCard.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 2, true),
                new EmptyBorder(30, 40, 30, 40)));
        appearanceCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        appearanceCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Title
        appearanceTitle = new JLabel("Appearance");
        appearanceTitle.setForeground(theme.getPrimaryText());
        appearanceTitle.setFont(ResourceManager.uiFont(18));
        appearanceTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        appearanceCard.add(appearanceTitle, BorderLayout.NORTH);

        // Theme toggle section
        JPanel themePanel = new JPanel(new BorderLayout());
        themePanel.setOpaque(false);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        themeLabel = new JLabel("Theme");
        themeLabel.setForeground(theme.getPrimaryText());
        themeLabel.setFont(ResourceManager.uiFont(16));
        leftPanel.add(themeLabel);

        themeDesc = new JLabel("Choose between light and dark mode");
        themeDesc.setForeground(theme.getSecondaryText());
        themeDesc.setFont(ResourceManager.uiFont(13));
        leftPanel.add(themeDesc);

        themePanel.add(leftPanel, BorderLayout.WEST);

        // Theme toggle button
        themeToggle = createThemeToggle();
        themePanel.add(themeToggle, BorderLayout.EAST);

        appearanceCard.add(themePanel, BorderLayout.CENTER);

        return appearanceCard;
    }

    /**
     * Creates a custom toggle button for switching between light and dark themes
     *
     * @return a stylized JToggleButton
     */
    private JToggleButton createThemeToggle() {
        JToggleButton toggle = new JToggleButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background track
                g2.setColor(isSelected() ? theme.getAccent() : new Color(60, 60, 60));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

                // Circle slider
                int circleSize = getHeight() - 6;
                int circleX = isSelected() ? getWidth() - circleSize - 3 : 3;
                g2.setColor(Color.WHITE);
                g2.fillOval(circleX, 3, circleSize, circleSize);

                // Icons
                g2.setColor(new Color(200, 200, 200));
                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                if (isSelected()) {
                    g2.drawString("Light", 8, 20);
                } else {
                    g2.drawString("Dark", getWidth() - 50, 20);
                }

                g2.dispose();
            }
        };

        toggle.setPreferredSize(new Dimension(60, 28));
        toggle.setFocusPainted(false);
        toggle.setBorderPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggle.setSelected(theme.isDarkMode());

        toggle.addActionListener(e -> {
            theme.setDarkMode(toggle.isSelected());
            // The theme listener will handle updating everything
        });

        return toggle;
    }

    /**
     * Creates the board customization settings card with style selector and preview
     *
     * @return the board customization JPanel
     */
    private JPanel createBoardCustomizationCard() {
        boardCustomizationCard = new JPanel() {
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
        boardCustomizationCard.setOpaque(false);
        boardCustomizationCard.setLayout(new BorderLayout());
        boardCustomizationCard.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 2, true),
                new EmptyBorder(30, 40, 30, 40)));
        boardCustomizationCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Title
        boardCustomTitle = new JLabel("Board Customization");
        boardCustomTitle.setForeground(theme.getPrimaryText());
        boardCustomTitle.setFont(ResourceManager.uiFont(18));
        boardCustomTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        boardCustomTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(boardCustomTitle);

        // Board Style selector
        styleLabel = new JLabel("Board Style");
        styleLabel.setForeground(theme.getPrimaryText());
        styleLabel.setFont(ResourceManager.uiFont(16));
        styleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        styleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(styleLabel);

        boardStyleBox = new JComboBox<>(new String[] { "Wooden", "Blue", "Green", "Pink" });
        styleComboBox(boardStyleBox);
        boardStyleBox.setSelectedItem(theme.getBoardStyle());
        boardStyleBox.addActionListener(e -> {
            String selectedStyle = (String) boardStyleBox.getSelectedItem();
            if (selectedStyle != null && !selectedStyle.equals(theme.getBoardStyle())) {
                System.out.println("Changing board style to: " + selectedStyle); // Debug
                theme.setBoardStyle(selectedStyle);
                // The theme listener will automatically update the preview board
            }
        });
        boardStyleBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        boardStyleBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        contentPanel.add(boardStyleBox);

        contentPanel.add(Box.createVerticalStrut(20));

        // Preview section
        previewLabel = new JLabel("Preview");
        previewLabel.setForeground(theme.getPrimaryText());
        previewLabel.setFont(ResourceManager.uiFont(16));
        previewLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        previewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(previewLabel);

        // Preview board - CREATE IT HERE
        JPanel previewWrapper = new JPanel(new GridBagLayout());
        previewWrapper.setOpaque(false);
        previewWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        previewBoard = new GUIBoard(manager, board, true);
        previewBoard.setTutorialMode(true);
        previewBoard.setPreferredSize(new Dimension(280, 280));
        previewBoard.setMaximumSize(new Dimension(280, 280));
        previewWrapper.add(previewBoard);

        contentPanel.add(previewWrapper);

        boardCustomizationCard.add(contentPanel, BorderLayout.CENTER);

        return boardCustomizationCard;
    }

    /**
     * Styles the combo box to match the theme
     * 
     * @param box the combo box to style
     */
    private void styleComboBox(JComboBox<String> box) {
        box.setBackground(theme.getSecondaryBackground());
        box.setForeground(theme.getPrimaryText());
        box.setFont(ResourceManager.uiFont(14));
        box.setFocusable(false);
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setBackground(isSelected ? theme.getHover() : theme.getCardBackground());
                lbl.setForeground(theme.getPrimaryText());
                lbl.setBorder(new EmptyBorder(10, 15, 10, 15));
                return lbl;
            }
        });
        box.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 1, true),
                new EmptyBorder(10, 15, 10, 15)));
    }

    /**
     * Creates the bottom panel with Cancel and Apply Settings buttons
     *
     * @return the JPanel containing bottom buttons
     */
    private JPanel createBottomButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Cancel Button
        JButton cancelBtn = new JButton("Cancel") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cancelBtn.setBackground(theme.getSecondaryButton());
        cancelBtn.setForeground(theme.getSecondaryButtonText());
        cancelBtn.setFont(ResourceManager.uiFont(16));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 1, true),
                new EmptyBorder(12, 30, 12, 30)));
        cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setOpaque(false);
        cancelBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                cancelBtn.setBackground(theme.getSecondaryButtonHover());
            }

            public void mouseExited(MouseEvent e) {
                cancelBtn.setBackground(theme.getSecondaryButton());
            }
        });
        cancelBtn.addActionListener(e -> manager.showScreen("menu"));

        // Apply Settings Button
        JButton applyBtn = new JButton("Apply Settings") {
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
        applyBtn.setBackground(theme.getPrimaryButton());
        applyBtn.setForeground(theme.getPrimaryButtonText());
        applyBtn.setFont(ResourceManager.uiFont(16));
        applyBtn.setFocusPainted(false);
        applyBtn.setBorder(new EmptyBorder(12, 30, 12, 30));
        applyBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        applyBtn.setContentAreaFilled(false);
        applyBtn.setOpaque(false);
        applyBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                applyBtn.setBackground(theme.getPrimaryButtonHover());
            }

            public void mouseExited(MouseEvent e) {
                applyBtn.setBackground(theme.getPrimaryButton());
            }
        });
        applyBtn.addActionListener(e -> applySettings());

        panel.add(cancelBtn);
        panel.add(applyBtn);

        return panel;
    }

    private void applySettings() {
        // Settings are applied immediately when changed
        // This just shows a confirmation message
        ModernDialog.showInfo(this,
                "Settings have been applied!",
                "Settings");
        manager.showScreen("menu");
    }

    private void updateTheme() {
        // Update background color
        setBackground(theme.getPrimaryBackground());

        // Update all labels with theme colors
        if (titleLabel != null) {
            titleLabel.setForeground(theme.getPrimaryText());
        }
        if (appearanceTitle != null) {
            appearanceTitle.setForeground(theme.getPrimaryText());
        }
        if (themeLabel != null) {
            themeLabel.setForeground(theme.getPrimaryText());
        }
        if (themeDesc != null) {
            themeDesc.setForeground(theme.getSecondaryText());
        }
        if (boardCustomTitle != null) {
            boardCustomTitle.setForeground(theme.getPrimaryText());
        }
        if (styleLabel != null) {
            styleLabel.setForeground(theme.getPrimaryText());
        }
        if (previewLabel != null) {
            previewLabel.setForeground(theme.getPrimaryText());
        }

        // Update toggle button state
        if (themeToggle != null) {
            themeToggle.setSelected(theme.isDarkMode());
            themeToggle.repaint();
        }

        // Update board style selector
        if (boardStyleBox != null) {
            boardStyleBox.setSelectedItem(theme.getBoardStyle());
            styleComboBox(boardStyleBox);
            boardStyleBox.repaint();
        }

        // Repaint cards
        if (appearanceCard != null) {
            appearanceCard.setBorder(new CompoundBorder(
                    new LineBorder(theme.getBorder(), 2, true),
                    new EmptyBorder(30, 40, 30, 40)));
            appearanceCard.repaint();
        }
        if (boardCustomizationCard != null) {
            boardCustomizationCard.setBorder(new CompoundBorder(
                    new LineBorder(theme.getBorder(), 2, true),
                    new EmptyBorder(30, 40, 30, 40)));
            boardCustomizationCard.repaint();
        }

        // Change icon based on theme

        if (backBtn != null) {
            String iconName = theme.isDarkMode() ? "whiteArrow" : "blackArrow";
            System.out.println("UPDATING ICON TO: " + iconName);
            backBtn.setIcon(ResourceManager.icon(iconName, 18));
        }
        System.out.println("Theme now dark? " + theme.isDarkMode()); // testing if it swiches

        // The GUIBoard has its own listener, so it will update automatically

        // Force repaint of all components
        revalidate();
        repaint();
    }
}