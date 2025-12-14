package Chess.ui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
A helper class that creates reusable Swing UI components for the Chess application
 * @author Group 3
 */

/**
 * Library of reusable, themed UI components.
 * All components automatically update when theme changes.
 */
public class UIComponents {

    private static ThemeManager theme = ThemeManager.getInstance();

    // ========== BUTTONS ==========

    /**
     * Creates a button (white/blue with rounded corners)
     * 
     * @param text the button label text
     * @return a styled button
     */
    public static JButton createPrimaryButton(String text) {
        return createPrimaryButton(text, null);
    }

    /**
     * Makes a main button with optional icon
     * 
     * @param text text on the button
     * @param icon optional icon to show beside the text
     * @return a styled button
     */

    public static JButton createPrimaryButton(String text, Icon icon) {
        JButton button = new JButton(text, icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), theme.getButtonRadius(), theme.getButtonRadius());
                g2.dispose();
                super.paintComponent(g);
            }
        };

        styleButton(button, true);
        return button;
    }

    /**
     * Creates a secondary button (gray/outlined)
     *
     * @param text text on the button
     * @return a styled secondary button
     */
    public static JButton createSecondaryButton(String text) {
        return createSecondaryButton(text, null);
    }

    /**
     * Makes a secondary button with optional icon
     *
     * @param text text on the button
     * @param icon optional icon to show beside the text
     * @return a styled secondary button
     */
    public static JButton createSecondaryButton(String text, Icon icon) {
        JButton button = new JButton(text, icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), theme.getButtonRadius(), theme.getButtonRadius());
                g2.dispose();
                super.paintComponent(g);
            }
        };

        styleButton(button, false);
        return button;
    }

    /**
     * @param text   text label for the button
     * @param action code to run when clicked
     * @return a back button
     */
    public static JButton createBackButton(String text, Runnable action) {
        JButton button = new JButton(text, ResourceManager.icon("left", 18));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        // Default theme Colors 
        button.setForeground(theme.getSecondaryText());
        button.setFont(theme.getSmallFont());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(theme.getPrimaryText()); // Light/Dark mode colors
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(theme.getSecondaryText());
            }
        });

        if (action != null) {
            button.addActionListener(e -> action.run());
        }

        // Theme listener for icons and color
        theme.addListener(() -> {
            // Update Icon
            String iconName = theme.isDarkMode() ? "whiteArrow" : "blackArrow";
            button.setIcon(ResourceManager.icon(iconName, 18));

            // Update Color
            button.setForeground(theme.getSecondaryText());
            button.setFont(theme.getSmallFont());
        });

        return button;
    }

    /**
     * Adds colors, padding, and hover effect to a button
     *
     * @param button    button to style
     * @param isPrimary true for main button, false for secondary
     */
    private static void styleButton(JButton button, boolean isPrimary) {
        if (isPrimary) {
            button.setBackground(theme.getPrimaryButton());
            button.setForeground(theme.getPrimaryButtonText());
        } else {
            button.setBackground(theme.getSecondaryButton());
            button.setForeground(theme.getSecondaryButtonText());
        }

        button.setFont(theme.getBodyFont());
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 26, 12, 26));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(10);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(isPrimary ? theme.getPrimaryButtonHover() : theme.getSecondaryButtonHover());
                }
            }

            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(isPrimary ? theme.getPrimaryButton() : theme.getSecondaryButton());
                }
            }
        });

        // Theme listener
        theme.addListener(() -> {
            if (isPrimary) {
                button.setBackground(theme.getPrimaryButton());
                button.setForeground(theme.getPrimaryButtonText());
            } else {
                button.setBackground(theme.getSecondaryButton());
                button.setForeground(theme.getSecondaryButtonText());
            }
            button.setFont(theme.getBodyFont());
        });
    }

    // ========== CARDS ==========

    /**
     * Makes a card panel with rounded corners and a border
     *
     * @return a styled card panel
     */
    public static JPanel createCard() {
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
        card.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 2, true),
                new EmptyBorder(theme.getSpacingL(), theme.getSpacingL(), theme.getSpacingL(), theme.getSpacingL())));

        // Theme listener
        theme.addListener(() -> {
            card.setBorder(new CompoundBorder(
                    new LineBorder(theme.getBorder(), 2, true),
                    new EmptyBorder(theme.getSpacingL(), theme.getSpacingL(), theme.getSpacingL(),
                            theme.getSpacingL())));
            card.repaint();
        });

        return card;
    }

    // ========== LABELS ==========

    /**
     * Makes a large title label
     *
     * @param text text to show
     * @return a styled title label
     */

    public static JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(theme.getPrimaryText());
        label.setFont(theme.getTitleFont());

        theme.addListener(() -> {
            label.setForeground(theme.getPrimaryText());
            label.setFont(theme.getTitleFont());
        });

        return label;
    }

    /**
     * Makes a heading label
     *
     * @param text text to show
     * @return a styled heading label
     */
    public static JLabel createHeading(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(theme.getPrimaryText());
        label.setFont(theme.getHeadingFont());

        theme.addListener(() -> {
            label.setForeground(theme.getPrimaryText());
            label.setFont(theme.getHeadingFont());
        });

        return label;
    }

    /**
     * Makes a normal body text label
     *
     * @param text text to show
     * @return a styled body label
     */
    public static JLabel createBodyText(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(theme.getSecondaryText());
        label.setFont(theme.getBodyFont());

        theme.addListener(() -> {
            label.setForeground(theme.getSecondaryText());
            label.setFont(theme.getBodyFont());
        });

        return label;
    }

    /**
     * Makes a small text label
     *
     * @param text text to show
     * @return a styled small label
     */
    public static JLabel createSmallText(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(theme.getSecondaryText());
        label.setFont(theme.getSmallFont());

        theme.addListener(() -> {
            label.setForeground(theme.getSecondaryText());
            label.setFont(theme.getSmallFont());
        });

        return label;
    }

    /**
     *  Create a JLabel that holds an icon which switch color based on theme
     * 
     * @param baseName The base name for the icon (example: book)
     * @return JLabel with the theme icon
    */
   

    // ========== PANELS ==========

    /**
     * Creates a themed base panel for screens
     *
     * @return a themed base panel
     */
    public static JPanel createBasePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(theme.getPrimaryBackground());

        theme.addListener(() -> {
            panel.setBackground(theme.getPrimaryBackground());
        });

        return panel;
    }

    /**
     * Creates a top bar with back button
     *
     * @param backText   text for back button
     * @param backAction what to do when clicked
     * @return a top bar panel
     */
    public static JPanel createTopBar(String backText, Runnable backAction) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(20, 40, 10, 40));

        JButton backBtn = createBackButton(backText, backAction);
        topBar.add(backBtn, BorderLayout.WEST);

        return topBar;
    }

    // ========== COMBO BOX ==========

    /**
     * Makes a dropdown menu with theme colors
     *
     * @param items list of options
     * @return a styled combo box
     */
    public static JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);

        comboBox.setBackground(theme.getSecondaryBackground());
        comboBox.setForeground(theme.getPrimaryText());
        comboBox.setFont(theme.getBodyFont());
        comboBox.setFocusable(false);

        comboBox.setRenderer(new DefaultListCellRenderer() {
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

        comboBox.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 1, true),
                new EmptyBorder(10, 15, 10, 15)));

        // Theme listener
        theme.addListener(() -> {
            comboBox.setBackground(theme.getSecondaryBackground());
            comboBox.setForeground(theme.getPrimaryText());
            comboBox.setFont(theme.getBodyFont());
            comboBox.setBorder(new CompoundBorder(
                    new LineBorder(theme.getBorder(), 1, true),
                    new EmptyBorder(10, 15, 10, 15)));
        });

        return comboBox;
    }

    // ========== TEXT AREA ==========

    /**
     * Makes a text box for typing multiple lines
     *
     * @param placeholder text shown when empty
     * @return a styled text area
     */
    public static JTextArea createTextArea(String placeholder) {
        JTextArea textArea = new JTextArea();
        textArea.setFont(theme.getBodyFont());
        textArea.setBackground(theme.getSecondaryBackground());
        textArea.setForeground(theme.getSecondaryText());
        textArea.setCaretColor(theme.getPrimaryText());
        textArea.setText(placeholder);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Theme listener
        theme.addListener(() -> {
            textArea.setFont(theme.getBodyFont());
            textArea.setBackground(theme.getSecondaryBackground());
            if (!textArea.getText().equals(placeholder)) {
                textArea.setForeground(theme.getPrimaryText());
            } else {
                textArea.setForeground(theme.getSecondaryText());
            }
            textArea.setCaretColor(theme.getPrimaryText());
        });

        return textArea;
    }

    // ========== SCROLL PANE ==========

    /**
     * Wraps a component in a scrollable panel
     *
     * @param component item to make scrollable
     * @return a styled scroll pane
     */
    public static JScrollPane createScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(new LineBorder(theme.getBorder(), 1, true));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }
}