package Chess.ui;
import Chess.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Base class for all screens in the application.
 * Serves as the foundation for all screens in the Chess Game Management System.
 * Provides consistent theming, layout setup, and shared UI helper methods such as 
 * content wrappers, button panels, and message dialogs.
 * 
 * @author Group 3
 */
public abstract class BaseScreen extends JPanel {
    
    protected final GameManager manager;
    protected final ThemeManager theme;
    protected JPanel contentPanel;
    
    /**
     * Constructs a new {@code BaseScreen} with the given game manager.
     * Sets up the layout, background, and registers a listener for theme changes.
     *
     * @param manager the {@link GameManager} managing screen navigation and game state
     */
    public BaseScreen(GameManager manager) {
        this.manager = manager;
        this.theme = ThemeManager.getInstance();
        
        setLayout(new BorderLayout());
        setBackground(theme.getPrimaryBackground());
        
        // Listen for theme changes
        theme.addListener(this::onThemeChanged);
        
        initializeUI();
    }
    
    /**
     * Initialize the UI components. Override this in subclasses.
     */
    protected abstract void initializeUI();
    
    /**
     * Called when theme changes. Override to update custom components.
     */
    protected void onThemeChanged() {
        setBackground(theme.getPrimaryBackground());
        revalidate();
        repaint();
    }
    
    // ========== HELPER METHODS ==========
    
    /**
     * Creates a standard top bar with a back button that returns to the main menu.
     *
     * @param backText the label for the back button
     * @return a {@link JPanel} representing the top bar
     */
    protected JPanel createTopBar(String backText) {
        return UIComponents.createTopBar(backText, () -> manager.showScreen("menu"));
    }
    
    /**
     * Creates main content wrapper with padding.
     *
     * @return a padded {@link JPanel} configured for central content
     */
    protected JPanel createContentWrapper() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(
            theme.getSpacingM(), 
            theme.getSpacingXL(), 
            theme.getSpacingXL(), 
            theme.getSpacingXL()
        ));
        return wrapper;
    }
    
    /**
     * Creates a vertical box layout panel.
     *
     * @return a transparent {@link JPanel} with a vertical {@link BoxLayout}
     */
    protected JPanel createVerticalPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        return panel;
    }
    
    /**
     * Creates a scrollable content area.
     *
     * @param content the content panel to make scrollable
     * @return a configured {@link JScrollPane} containing the given content
     */
    protected JScrollPane createScrollableContent(JPanel content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }
    
    /**
     * Creates a vertical spacing component.
     *
     * @param size the vertical spacing height in pixels
     * @return a {@link Component} representing the vertical strut
     */
    protected Component createVerticalStrut(int size) {
        return Box.createVerticalStrut(size);
    }
    
    /**
     * Creates a horizontal spacing component.
     *
     * @param size the horizontal spacing width in pixels
     * @return a {@link Component} representing the horizontal strut
     */
    protected Component createHorizontalStrut(int size) {
        return Box.createHorizontalStrut(size);
    }
    
    /**
     * Creates a bottom button panel with Cancel and Action buttons.
     *
     * @param cancelText     the label for the cancel button
     * @param actionText     the label for the main action button
     * @param actionCallback a {@link Runnable} to execute when the action button is pressed
     * @return a {@link JPanel} containing both buttons
     */
    protected JPanel createBottomButtons(String cancelText, String actionText, Runnable actionCallback) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, theme.getSpacingS(), 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(theme.getSpacingL(), 0, 0, 0));
        
        JButton cancelBtn = UIComponents.createSecondaryButton(cancelText);
        cancelBtn.addActionListener(e -> manager.showScreen("menu"));
        
        JButton actionBtn = UIComponents.createPrimaryButton(actionText);
        if (actionCallback != null) {
            actionBtn.addActionListener(e -> actionCallback.run());
        }
        
        panel.add(cancelBtn);
        panel.add(actionBtn);
        
        return panel;
    }
    
    /**
     * Shows a success message using a modern dialog.
     *
     * @param message the message text to display
     */
    protected void showSuccess(String message) {
        ModernDialog.showInfo(this, message, "Success");
    }
    
    /**
     * Shows an error message using a modern dialog.
     *
     * @param message the error message text to display
     */
    protected void showError(String message) {
        ModernDialog.showError(this, message, "Error");
    }
    
    /**
     * Shows a warning message using a modern dialog.
     *
     * @param message the warning message text to display
     */
    protected void showWarning(String message) {
        ModernDialog.showWarning(this, message, "Warning");
    }
    
    /**
     * Shows a confirmation dialog using a modern dialog.
     * 
     * @param message the confirmation question
     * @return {@code true} if the user selected "Yes", {@code false} otherwise
     */
    protected boolean showConfirmation(String message) {
        int result = ModernDialog.showConfirm(this, message, "Confirm");
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Shows a confirmation dialog with a custom title.
     *
     * @param message the confirmation question
     * @param title   the title of the dialog
     * @return {@code true} if the user selected "Yes", {@code false} otherwise
     */
    protected boolean showConfirmation(String message, String title) {
        int result = ModernDialog.showConfirm(this, message, title);
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Shows a Yes/No/Cancel dialog using a modern dialog.
     *
     * @param message the message text
     * @param title   the title of the dialog
     * @return the selected option constant (JOptionPane.YES_OPTION, JOptionPane.NO_OPTION, or JOptionPane.CANCEL_OPTION)
     */
    protected int showYesNoCancel(String message, String title) {
        return ModernDialog.showYesNoCancel(this, message, title);
    }
}