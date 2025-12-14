package Chess;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Chess.ui.ThemeManager;

/**
 * Modern styled dialog boxes
 *
 * * @author Group3
 * 
 * @version 1.0
 */
public class ModernDialog {
    private static ThemeManager theme = ThemeManager.getInstance();

    /**
     * Show a modern information dialog
     *
     * @param parent  the parent component for centering the dialog; may be null
     * @param message the message to display in the dialog
     * @param title   the title of the dialog window
     */
    public static void showInfo(Component parent, String message, String title) {
        showDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show a modern warning dialog
     *
     * @param parent  the parent component for centering the dialog; may be null
     * @param message the message to display in the dialog
     * @param title   the title of the dialog window
     */
    public static void showWarning(Component parent, String message, String title) {
        showDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Show a modern error dialog
     *
     * @param parent  the parent component for centering the dialog; may be null
     * @param message the message to display in the dialog
     * @param title   the title of the dialog window
     */
    public static void showError(Component parent, String message, String title) {
        showDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show a modern confirmation dialog
     *
     * @param parent  the parent component for centering the dialog; may be null
     * @param message the message to display in the dialog
     * @param title   the title of the dialog window
     * @return the user's selection: {@link JOptionPane#YES_OPTION},
     *         {@link JOptionPane#NO_OPTION},
     *         or {@link JOptionPane#CLOSED_OPTION} if the dialog was closed without
     *         selection
     */
    public static int showConfirm(Component parent, String message, String title) {
        return showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION);
    }

    /**
     * Show a modern Yes/No/Cancel dialog
     *
     * @param parent  the parent component for centering the dialog; may be null
     * @param message the message to display in the dialog
     * @param title   the title of the dialog window
     * @return the user's selection: {@link JOptionPane#YES_OPTION},
     *         {@link JOptionPane#NO_OPTION},
     *         {@link JOptionPane#CANCEL_OPTION}, or
     *         {@link JOptionPane#CLOSED_OPTION}
     *         if the dialog was closed without selection
     */
    public static int showYesNoCancel(Component parent, String message, String title) {
        return showConfirmDialog(parent, message, title, JOptionPane.YES_NO_CANCEL_OPTION);
    }

    /**
     * Internal method to display a styled information/warning/error dialog
     *
     * @param parent      the parent component for centering the dialog
     * @param message     the message to display
     * @param title       the dialog title
     * @param messageType the type of message (INFORMATION, WARNING, or ERROR)
     */
    private static void showDialog(Component parent, String message, String title, int messageType) {
        JOptionPane pane = createStyledPane(message, messageType);
        JDialog dialog = createStyledDialog(parent, pane, title);
        pane.setValue(JOptionPane.UNINITIALIZED_VALUE);
        dialog.setVisible(true);
    }

    /**
     * Internal method to display a styled confirmation dialog
     *
     * @param parent     the parent component for centering the dialog
     * @param message    the message to display
     * @param title      the dialog title
     * @param optionType the type of options (YES_NO or YES_NO_CANCEL)
     * @return the user's selection as an integer constant
     */
    private static int showConfirmDialog(Component parent, String message, String title, int optionType) {
        JOptionPane pane = createStyledConfirmPane(message, optionType);
        JDialog dialog = createStyledDialog(parent, pane, title);
        pane.setValue(JOptionPane.UNINITIALIZED_VALUE);
        dialog.setVisible(true);

        Object value = pane.getValue();
        if (value == null || value.equals(JOptionPane.UNINITIALIZED_VALUE)) {
            return JOptionPane.CLOSED_OPTION;
        }

        if (value instanceof Integer) {
            return (Integer) value;
        }
        return JOptionPane.CLOSED_OPTION;
    }

    /**
     * Creates a styled option pane for information/warning/error dialogs
     *
     * @param message     the message text to display
     * @param messageType the type of message for icon selection
     * @return a styled JOptionPane
     */
    private static JOptionPane createStyledPane(String message, int messageType) {
        // Create custom message panel
        JPanel messagePanel = new JPanel(new BorderLayout(15, 0));
        messagePanel.setOpaque(false);
        messagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Icon
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(getModernIcon(messageType));
        messagePanel.add(iconLabel, BorderLayout.WEST);

        // Message text
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setForeground(theme.getPrimaryText());
        textArea.setFont(theme.getBodyFont());
        textArea.setBorder(null);
        messagePanel.add(textArea, BorderLayout.CENTER);
        // ==Added to make the OKpop up in the hint
        textArea.setColumns(30); // Sets width to ~30 characters
        textArea.setRows(0); // Auto-calculates height based on wrapped text

        JOptionPane pane = new JOptionPane(
                messagePanel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION);

        styleOptionPane(pane);
        return pane;
    }

    /**
     * Creates a customized {@link JOptionPane} for confirmation dialogs with
     * modern styling, including an icon and formatted message text
     *
     * @param message    the text message to display in the dialog
     * @param optionType the type of options for the dialog, e.g.,
     *                   {@link JOptionPane#YES_NO_OPTION} or
     *                   {@link JOptionPane#OK_CANCEL_OPTION}
     * @return a styled {@link JOptionPane} ready to be displayed
     */
    private static JOptionPane createStyledConfirmPane(String message, int optionType) {
        // Create custom message panel
        JPanel messagePanel = new JPanel(new BorderLayout(15, 0));
        messagePanel.setOpaque(false);
        messagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Icon
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(getModernIcon(JOptionPane.QUESTION_MESSAGE));
        messagePanel.add(iconLabel, BorderLayout.WEST);

        // Message text
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setForeground(theme.getPrimaryText());
        textArea.setFont(theme.getBodyFont());
        textArea.setBorder(null);
        messagePanel.add(textArea, BorderLayout.CENTER);

        JOptionPane pane = new JOptionPane(
                messagePanel,
                JOptionPane.PLAIN_MESSAGE,
                optionType);

        styleOptionPane(pane);
        return pane;
    }

    /**
     * Creates a styled dialog window from an option pane
     *
     * @param parent the parent component for positioning
     * @param pane   the option pane to display in the dialog
     * @param title  the dialog title
     * @return a styled JDialog ready to be shown
     */
    private static JDialog createStyledDialog(Component parent, JOptionPane pane, String title) {
        JDialog dialog = pane.createDialog(parent, title);

        // Style the dialog
        dialog.setBackground(theme.getCardBackground());

        // Style the content pane
        Container contentPane = dialog.getContentPane();
        if (contentPane instanceof JComponent) {
            JComponent jComponent = (JComponent) contentPane;
            jComponent.setBackground(theme.getCardBackground());
            jComponent.setBorder(new CompoundBorder(
                    new LineBorder(theme.getBorder(), 1),
                    new EmptyBorder(20, 20, 20, 20)));
        }

        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        return dialog;
    }

    /**
     * Applies theme styling to an option pane and its components.
     *
     * @param pane the option pane to style
     */
    private static void styleOptionPane(JOptionPane pane) {
        pane.setBackground(theme.getCardBackground());
        pane.setBorder(null);

        // Style buttons
        Component[] components = pane.getComponents();
        styleComponents(components);
    }

    /**
     * Recursively styles components within a container.
     *
     * @param components array of components to style
     */
    private static void styleComponents(Component[] components) {
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                panel.setBackground(theme.getCardBackground());
                styleComponents(panel.getComponents());
            } else if (comp instanceof JButton) {
                styleButton((JButton) comp);
            }
        }
    }

    /**
     * Styles a button with theme colors and hover effects
     *
     * @param button the button to style
     */
    private static void styleButton(JButton button) {
        String text = button.getText();
        boolean isPrimary = text.equals("Yes") || text.equals("Ok");

        if (isPrimary) {
            button.setBackground(theme.getPrimaryButton());
            button.setForeground(theme.getPrimaryButtonText());
        } else {
            button.setBackground(theme.getSecondaryButton());
            button.setForeground(theme.getSecondaryButtonText());
        }

        button.setFont(theme.getBodyFont());
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 1, true),
                new EmptyBorder(10, 20, 10, 20)));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private Color originalColor = button.getBackground();

            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (isPrimary) {
                    button.setBackground(theme.getPrimaryButtonHover());
                } else {
                    button.setBackground(theme.getSecondaryButtonHover());
                }
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    /**
     * Creates a custom icon for dialog message types.
     *
     * @param messageType the type of message (INFORMATION, WARNING, ERROR, or
     *                    QUESTION)
     * @return a custom Icon with a colored circle and symbol
     */
    private static Icon getModernIcon(int messageType) {
        int size = 32;
        Color color;
        String symbol;

        switch (messageType) {
            case JOptionPane.INFORMATION_MESSAGE:
                color = new Color(33, 150, 243); // Blue
                symbol = "i";
                break;
            case JOptionPane.WARNING_MESSAGE:
                color = new Color(255, 152, 0); // Orange
                symbol = "!";
                break;
            case JOptionPane.ERROR_MESSAGE:
                color = new Color(244, 67, 54); // Red
                symbol = "×";
                break;
            case JOptionPane.QUESTION_MESSAGE:
                color = new Color(76, 175, 80); // Green
                symbol = "?";
                break;
            default:
                color = theme.getAccent();
                symbol = "i";
        }

        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw circle
                g2.setColor(color);
                g2.fillOval(x, y, size, size);

                // Draw symbol
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                int textX = x + (size - fm.stringWidth(symbol)) / 2;
                int textY = y + ((size - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(symbol, textX, textY);

                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }
}